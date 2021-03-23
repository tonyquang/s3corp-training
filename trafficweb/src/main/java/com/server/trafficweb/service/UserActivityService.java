/**
 * 
 */

package com.server.trafficweb.service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpHost;
import org.apache.log4j.Logger;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.server.trafficweb.models.UserActivityDB;
import com.server.trafficweb.repository.UserActivityDBRepo;

/**
 * @author nhut.to
 *
 */
@Service
public class UserActivityService implements IUserActivityService {

	final static Logger LOGGER = Logger.getLogger(UserActivityService.class);
	private final String AGGS_SUFFIX = "_aggs";
	private final String KEYWORD_SUFFIX = ".keyword";
	private final double WAITTIME = 180000.0;

	@Autowired
	private UserActivityDBRepo activityDBRepo;

	/**
	 * 
	 * @param host
	 * @param port
	 * @param protocol
	 * @return
	 */
	public RestHighLevelClient createRestHighLevelClient(final String host, final int port, final String protocol) {
		RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost(host, port, protocol)));
		return client;
	}

	private SearchResponse getDistinctAgg(final RestHighLevelClient client, final String indexName,
			final List<String> fieldNames) throws IOException {
		SearchRequest searchRequest = new SearchRequest(indexName);
		searchRequest.searchType();
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.matchAllQuery());

		AggregationBuilder aggregation = AggregationBuilders.terms(fieldNames.get(0) + AGGS_SUFFIX)
				.field(fieldNames.get(0) + KEYWORD_SUFFIX)
				.subAggregation(AggregationBuilders.terms(fieldNames.get(1) + AGGS_SUFFIX)
						.field(fieldNames.get(1) + KEYWORD_SUFFIX).subAggregation(AggregationBuilders
								.terms(fieldNames.get(2) + AGGS_SUFFIX).field(fieldNames.get(2)).size(100))
						.size(1000))
				.size(200);
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
	 * @param fieldNames
	 * @throws ParseException
	 * @throws IOException
	 */
	public void saveDocument(final RestHighLevelClient client, final String indexName, final List<String> fieldNames)
			throws ParseException, IOException {

		SearchResponse searchResponse = getDistinctAgg(client, indexName, fieldNames);
		Terms termsUser = searchResponse.getAggregations().get(fieldNames.get(0) + AGGS_SUFFIX);

		for (Terms.Bucket user : termsUser.getBuckets()) {
			UserActivityDB userActivity = new UserActivityDB();
			userActivity.setDate(getCurrentTime());
			userActivity.setUser_id(user.getKey().toString());
			Terms termsUrl = user.getAggregations().get(fieldNames.get(1) + AGGS_SUFFIX);

			for (Terms.Bucket url : termsUrl.getBuckets()) {
				userActivity.setUrl(url.getKey().toString());

				Terms termsTime = url.getAggregations().get(fieldNames.get(2) + AGGS_SUFFIX);

				List<Double> times = new ArrayList<>();
				for (Terms.Bucket time : termsTime.getBuckets()) {
					times.add(toMilliseconds(time.getKeyAsString()));

				}
				userActivity.setCount(countTimesTraffic(times));
				userActivity.setTotal_time(countTotalTime(times));
				activityDBRepo.save(userActivity);
				LOGGER.info(userActivity);
			}
		}
	}

	private String getCurrentTime() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
		Date date = new Date(System.currentTimeMillis());
		return formatter.format(date);
	}

	private double toMilliseconds(final String myDate) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
		Date date = sdf.parse(myDate);
		long millis = date.getTime();
		return millis;
	}

	private double MillisecondsToSecondS(double time) {
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
}
