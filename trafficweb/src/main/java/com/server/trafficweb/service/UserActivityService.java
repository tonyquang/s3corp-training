package com.server.trafficweb.service;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.sql.DataSource;

import org.apache.http.HttpHost;
import org.apache.log4j.Logger;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.server.trafficweb.constants.IConfigConstants;
import com.server.trafficweb.models.UserActivityDB;
import com.server.trafficweb.repository.UserActivityDBRepo;

/**
 * @author nhut.to
 *
 */
@Service
@Transactional
public class UserActivityService implements IUserActivityService, Job {

	final static Logger LOGGER = Logger.getLogger(UserActivityService.class);
	private final String AGGS_SUFFIX = "_aggs";
	private final String KEYWORD_SUFFIX = ".keyword";
	private final double WAITTIME = 180000.0;
	private String sCONNECT = "CONNECT";

	@Autowired
	private UserActivityDBRepo activityDBRepo;

	@Autowired
	private DataSource source;

	/**
	 * 
	 * @param client
	 * @param indexName
	 * @param url
	 * @param date
	 * @param userId
	 * @param fieldNames
	 * @return
	 * @throws IOException
	 */
	public SearchHit[] findByField(final RestHighLevelClient client, final String indexName, final String url,
			String date, String userId, final List<String> fieldNames) throws IOException {

		if (null == client || null == indexName || indexName.isEmpty() || fieldNames == null || url.isEmpty()
				|| date.isEmpty() || userId.isEmpty()) {
			return null;
		}
		SearchRequest searchRequest = new SearchRequest(indexName);
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		QueryBuilder queryBuilder = QueryBuilders.boolQuery()
				.must(QueryBuilders.matchPhraseQuery(fieldNames.get(0), userId))
				.must(QueryBuilders.matchPhraseQuery(fieldNames.get(1), url))
				.must(QueryBuilders.matchPhraseQuery(fieldNames.get(2), date));
		searchSourceBuilder.query(queryBuilder).sort(new FieldSortBuilder(fieldNames.get(2)).order(SortOrder.ASC));
		searchRequest.source(searchSourceBuilder);
		SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
		return searchResponse.getHits().getHits();
	}

	/**
	 * 
	 * @param host
	 * @param port
	 * @param protocol
	 * @return
	 */
	public RestHighLevelClient createRestHighLevelClient(final String host, final int port, final String protocol) {
		try {
			RestHighLevelClient client = new RestHighLevelClient(
					RestClient.builder(new HttpHost(host, port, protocol)));
			return client;
		} catch (Exception e) {
			return null;
		}

	}

	private AggregationBuilder createAggregationBuilder(final List<String> fieldNames, final List<Integer> sizes) {
		try {
			return AggregationBuilders.terms(fieldNames.get(0) + AGGS_SUFFIX).field(fieldNames.get(0) + KEYWORD_SUFFIX)
					.subAggregation(AggregationBuilders.terms(fieldNames.get(1) + AGGS_SUFFIX)
							.field(fieldNames.get(1) + KEYWORD_SUFFIX)
							.subAggregation(AggregationBuilders.terms(fieldNames.get(2) + AGGS_SUFFIX)
									.field(fieldNames.get(2)).size(sizes.get(2)))
							.size(sizes.get(1)))
					.size(sizes.get(0));

		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 
	 * @param client
	 * @param indexName
	 * @param fieldNames
	 * @return
	 * @throws IOException
	 */
	public SearchResponse getSearchResponseByGroupbyFields(final RestHighLevelClient client, final String indexName,
			final List<String> fieldNames) throws IOException {
		if (null == client || null == indexName || indexName.isEmpty() || fieldNames == null) {
			return null;
		}
		List<Integer> sizes = Stream.of(200, 1000, 100).collect(Collectors.toList());
		SearchRequest searchRequest = new SearchRequest(indexName);
		searchRequest.searchType();

		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.matchAllQuery());
		AggregationBuilder aggregation = createAggregationBuilder(fieldNames, sizes);
		if (aggregation == null) {
			return null;
		}
		searchSourceBuilder.aggregation(aggregation);
		searchRequest.source(searchSourceBuilder);
		SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
		LOGGER.info("Search respones: " + searchResponse);
		return searchResponse;
	}

	/**
	 * 
	 * @param client
	 * @param indexName
	 * @return
	 * @throws IOException
	 */
	public SearchResponse getSearchResponseByOneDocument(final RestHighLevelClient client, final String indexName)
			throws IOException {
		SearchRequest searchRequest = new SearchRequest(indexName);
		searchRequest.searchType();
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.matchAllQuery()).size(1);
		searchRequest.source(searchSourceBuilder);
		SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
		LOGGER.info("Search respones: " + searchResponse);
		return searchResponse;
	}

	/**
	 * 
	 * @param client
	 * @param indexName
	 * @return
	 * @throws IOException
	 */
	public boolean isExistDocuments(final RestHighLevelClient client, final String indexName) throws IOException {
		SearchResponse response = getSearchResponseByOneDocument(client, indexName);
		if (null == response || response.getHits().getHits().length == 0) {
			return false;
		}

		return true;
	}

	/**
	 * 
	 * @param date
	 * @param userId
	 * @param url
	 * @param count
	 * @param totalTime
	 * @return
	 */
	public UserActivityDB saveUserActivity(final String date, final String userId, final String url, final int count,
			final double totalTime) {
		try {
			UserActivityDB userActivity = new UserActivityDB();
			userActivity.setDate(date);
			userActivity.setUser_id(userId);
			userActivity.setUrl(url);
			userActivity.setCount(count);
			userActivity.setTotal_time(totalTime);
			return activityDBRepo.save(userActivity);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 
	 * @param client
	 * @param indexName
	 * @return
	 * @throws IOException
	 */
	public boolean deleteAllDocuments(final RestHighLevelClient client, final String indexName) throws IOException {
		if (!isExistDocuments(client, indexName)) {
			return false;
		}
		DeleteByQueryRequest request = new DeleteByQueryRequest(indexName);
		request.setQuery(QueryBuilders.matchAllQuery());
		BulkByScrollResponse response;
		try {
			response = client.deleteByQuery(request, RequestOptions.DEFAULT);
		} catch (IOException e) {
			LOGGER.error(e.getMessage() + e.getStackTrace());
			return false;
		}
		if (response.getDeleted() == 0)
			return false;
		return true;
	}

	/**
	 * 
	 * @param client
	 * @param indexName
	 * @param fieldNames
	 * @throws ParseException
	 * @throws IOException
	 */
	public boolean saveUserActivitesByGroupby(final RestHighLevelClient client, final String indexName,
			final List<String> fieldNames) throws ParseException, IOException {

		SearchResponse searchResponse = getSearchResponseByGroupbyFields(client, indexName, fieldNames);
		if (searchResponse == null) {
			LOGGER.error("search response is null");
			return false;
		}
		Terms termsUser = searchResponse.getAggregations().get(fieldNames.get(0) + AGGS_SUFFIX);
		if (termsUser == null || termsUser.getBuckets().size() == 0) {
			LOGGER.error("Users is null");
			return false;
		}
		for (Terms.Bucket user : termsUser.getBuckets()) {
			String sUser = user.getKeyAsString();
			Terms termsUrl = user.getAggregations().get(fieldNames.get(1) + AGGS_SUFFIX);
			if (termsUrl == null || termsUrl.getBuckets().size() == 0) {
				LOGGER.error(sUser + " URL is null");
				return false;
			}
			for (Terms.Bucket url : termsUrl.getBuckets()) {
				String sUrl = url.getKeyAsString();
				if (!sUrl.isEmpty() && sUrl.contains(sCONNECT)) {
					Terms termsTime = url.getAggregations().get(fieldNames.get(2) + AGGS_SUFFIX);
					if (termsTime == null || termsTime.getBuckets().size() == 0) {
						LOGGER.error(sUrl + " Time stamp is null");
						return false;
					}
					Set<String> days = new HashSet<>();
					for (Terms.Bucket time : termsTime.getBuckets()) {
						days.add(getDate(time.getKeyAsString()));
					}
					for (String day : days) {
						List<Double> dates = new ArrayList<>();
						SearchHit[] searchHits = findByField(client, indexName, sUrl, day, sUser, fieldNames);
						if (null == searchHits || searchHits.length == 0) {
							LOGGER.error("Not found data in Elastic Search");
							return false;
						}
						for (SearchHit hit : searchHits) {
							dates.add(toMilliseconds(hit.getSourceAsMap().get(fieldNames.get(2)).toString()));
						}
						UserActivityDB userActivity = saveUserActivity(day, sUser, sUrl, countTimesTraffic(dates),
								countTotalTime(dates));
						LOGGER.info("User activity: " + userActivity);
						if (userActivity == null) {
							return false;
						}
					}
				}
			}
		}
		return true;
	}

	/**
	 * 
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public String getDate(final String date) throws ParseException {
		DateTimeFormatter fmDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		LocalDateTime result = LocalDateTime.parse(date, fmDateTime);
		DateTimeFormatter fmDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		return result.format(fmDate);
	}

	/**
	 * 
	 * @param myDate
	 * @return
	 * @throws ParseException
	 */
	public double toMilliseconds(final String myDate) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		Date date = sdf.parse(myDate);
		return date.getTime();
	}

	/**
	 * 
	 * @param time
	 * @return
	 */
	public double MillisecondsToSecondS(final double time) {
		return TimeUnit.MILLISECONDS.toSeconds((long) time);
	}

	private int countTimesTraffic(final List<Double> times) {
		int count = 0;
		int size = times.size();
		if (size <= 1) {
			return 1;
		}
		for (int i = 0; i < size - 1; i++) {
			double diffTime = times.get(i + 1) - times.get(i);
			count = diffTime > WAITTIME ? (count += 2) : ++count;
		}
		return count;
	}

	private double countTotalTime(final List<Double> times) {
		double totalTime = 0;
		int size = times.size();
		if (size <= 1) {
			return MillisecondsToSecondS(WAITTIME);
		}
		for (int i = 0; i < size - 1; i++) {
			double diffTime = times.get(i + 1) - times.get(i);
			totalTime = diffTime > WAITTIME ? (totalTime += WAITTIME) : (totalTime += diffTime);
		}
		return MillisecondsToSecondS(totalTime);
	}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		RestHighLevelClient client = createRestHighLevelClient(IConfigConstants.HOSTNAME, IConfigConstants.PORT,
				IConfigConstants.PROTOCOL);
		if (client == null) {
			LOGGER.error("Failed to create RestHighLevelClient");
			return;
		}
		try {
			List<String> fieldNames = Stream
					.of(IConfigConstants.USER_ID_FIELD, IConfigConstants.URL_FIELD, IConfigConstants.LOCALDATE_FIELD)
					.collect(Collectors.toList());
			if (!saveUserActivitesByGroupby(client, IConfigConstants.INDEX_NAME, fieldNames)) {
				LOGGER.error("Failed to save data");
			}
		} catch (ParseException e) {
			LOGGER.error(e.getMessage() + e.getStackTrace());
		} catch (IOException e) {
			LOGGER.error(e.getMessage() + e.getStackTrace());
		} finally {
			try {
				this.source.getConnection().close();
				client.close();
			} catch (IOException | SQLException e) {
				LOGGER.error(e.getMessage() + e.getStackTrace());
			}
		}
	}
}
