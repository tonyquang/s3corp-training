package com.server.trafficweb.service;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.sql.DataSource;

import org.apache.http.HttpHost;
import org.apache.log4j.Logger;
import org.elasticsearch.action.search.ClearScrollRequest;
import org.elasticsearch.action.search.ClearScrollResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
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
import com.server.trafficweb.models.MyKey;
import com.server.trafficweb.models.UserActivityDB;
import com.server.trafficweb.models.Users;
import com.server.trafficweb.repository.UserActivityDBRepo;
import com.server.trafficweb.repository.UserRepository;

/**
 * @author nhut.to
 * @param <E>
 *
 */
@Service
@Transactional
public class UserActivityService<E> implements IUserActivityService, Job {

	final static Logger LOGGER = Logger.getLogger(UserActivityService.class);
	private final String AGGS_SUFFIX = "_aggs";
	private final String KEYWORD_SUFFIX = ".keyword";
	private final double WAITTIME = 180000.0;
	private final String sCONNECT = "CONNECT";
	//T08:39:55.037+0700
	private final String M_START_TIME = "T08:00:00.000+0700";
	private final String M_END_TIME = "T11:30:00.000+0700";
	private final String A_START_TIME = "T11:31:00.000+0700";
	private final String A_END_TIME = "T15:49:00.000+0700";
	private final DateTimeFormatter SCHEDULE_FIRETIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
	private final DateTimeFormatter LOCAL_TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'+0700'");

	@Autowired
	private UserActivityDBRepo activityDBRepo;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private DataSource source;

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

//	private AggregationBuilder aggByUserIdAndUrlAndDate(final List<String> fieldNames, final List<Integer> sizes) {
//		if (fieldNames.size() != sizes.size()) {
//			LOGGER.error("Size of list not match");
//			return null;
//		}
//		try {
//			return AggregationBuilders.terms(fieldNames.get(0) + AGGS_SUFFIX).field(fieldNames.get(0) + KEYWORD_SUFFIX)
//					.subAggregation(AggregationBuilders.terms(fieldNames.get(1) + AGGS_SUFFIX)
//							.field(fieldNames.get(1) + KEYWORD_SUFFIX)
//							.subAggregation(AggregationBuilders.terms(fieldNames.get(2) + AGGS_SUFFIX)
//									.field(fieldNames.get(2)).size(sizes.get(2)))
//							.size(sizes.get(1)))
//					.size(sizes.get(0));
//		} catch (Exception e) {
//			return null;
//		}
//	}

	private AggregationBuilder aggByUserIdAndUrl(final List<String> fieldNames, final List<Integer> sizes) {
		if (fieldNames.size() != sizes.size()) {
			LOGGER.error("Size of list not match");
			return null;
		}
		try {
			return AggregationBuilders.terms(fieldNames.get(0) + AGGS_SUFFIX).field(fieldNames.get(0) + KEYWORD_SUFFIX)
					.subAggregation(AggregationBuilders.terms(fieldNames.get(1) + AGGS_SUFFIX)
							.field(fieldNames.get(1) + KEYWORD_SUFFIX).size(sizes.get(1)))
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
	public SearchResponse groupByUserActs(final RestHighLevelClient client, final String indexName,
			final List<String> fieldNames, final List<String> times, final AggregationBuilder aggregation)
			throws IOException {
		if (null == client || null == indexName || indexName.isEmpty() || fieldNames == null || times == null
				|| times.isEmpty() || aggregation == null) {
			return null;
		}
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.aggregation(aggregation);
		QueryBuilder queryBuilder = QueryBuilders.boolQuery()
				.must(QueryBuilders.matchPhraseQuery(fieldNames.get(2), "2021-04-15"))
				.must(QueryBuilders.rangeQuery(fieldNames.get(2)).gte(times.get(0)).lte(times.get(1)));
		searchSourceBuilder.query(queryBuilder);
		// searchSourceBuilder.query(QueryBuilders.rangeQuery(fieldNames.get(2)).gte(times.get(0)).lte(times.get(1)));
		//searchSourceBuilder.query(QueryBuilders.rangeQuery(fieldNames.get(2)).from("2021-04-12").to("2021-04-16"));
		// searchSourceBuilder.query(QueryBuilders.rangeQuery(fieldNames.get(2)).from("2021-04-15T11:31+0700").to("2021-04-15T16:00+0700"));
		
		SearchRequest searchRequest = new SearchRequest(indexName);
		searchRequest.searchType();
		searchRequest.source(searchSourceBuilder);
		SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
		LOGGER.info("Search respones after group by: " + searchResponse);
		return searchResponse;
	}

	private SearchHit[] getHitsFromSearch(final String indexName, final SearchSourceBuilder searchSourceBuilder,
			final RestHighLevelClient client) throws IOException {

		SearchRequest searchRequest = new SearchRequest(indexName);
		searchRequest.source(searchSourceBuilder);
		// first scroll
		final Scroll scroll = new Scroll(TimeValue.timeValueMinutes(1L));
		searchRequest.scroll(scroll);
		SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
		LOGGER.info("First scroll response: " + searchResponse);
		String scrollId = searchResponse.getScrollId();
		SearchHit[] searchHits = searchResponse.getHits().getHits();

//		// request more scroll
//		List<SearchHit[]> lstSearchHits = new ArrayList<>();
//		lstSearchHits.add(searchHits);
//		while (searchHits != null && searchHits.length > 0) {
//			SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId);
//			scrollRequest.scroll(scroll);
//			searchResponse = client.scroll(scrollRequest, RequestOptions.DEFAULT);
//			LOGGER.info("Other scroll response: " + searchResponse);
//
//			searchHits = searchResponse.getHits().getHits();
//			if (searchHits != null && searchHits.length > 0) {
//				lstSearchHits.add(searchHits);
//				scrollId = searchResponse.getScrollId();
//			}
//		}
//		LOGGER.info("Size of search hit list: " + lstSearchHits.size());
//		SearchHit[] resultSearchHits = lstSearchHits.get(0);
//		for (int i = 1; i < lstSearchHits.size(); i++) {
//			resultSearchHits = concatenate(resultSearchHits, lstSearchHits.get(i));
//		}

		ClearScrollRequest clearScrollRequest = new ClearScrollRequest();
		clearScrollRequest.addScrollId(scrollId);
		ClearScrollResponse clearScrollResponse = client.clearScroll(clearScrollRequest, RequestOptions.DEFAULT);
		if (!clearScrollResponse.isSucceeded()) {
			LOGGER.error("Clear scroll was not successful");
			return null;
		}
		LOGGER.info("Size search hit after concat: " + searchHits.length);
		return searchHits;
	}

	public SearchHit[] concatenate(SearchHit[] first, SearchHit[] second) {
		if (second == null || second.length == 0) {
			return first;
		}
		return Stream.of(first, second).flatMap(Stream::of).toArray(SearchHit[]::new);
	}

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
	public SearchHit[] searchByUserAndUrlAndDate(final RestHighLevelClient client, final String indexName,
			final String url, String date, String userId, final List<String> fieldNames) throws IOException {
		if (null == client || null == indexName || indexName.isEmpty() || fieldNames == null || url.isEmpty()
				|| date.isEmpty() || userId.isEmpty()) {
			return null;
		}
		QueryBuilder queryBuilder = QueryBuilders.boolQuery()
				.must(QueryBuilders.matchPhraseQuery(fieldNames.get(0), userId))
				.must(QueryBuilders.matchPhraseQuery(fieldNames.get(1), url))
				.must(QueryBuilders.matchPhraseQuery(fieldNames.get(2), date));
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(queryBuilder).size(IConfigConstants.MAX_NUM_TIMES_TRAFFIC)
				.sort(new FieldSortBuilder(fieldNames.get(2)).order(SortOrder.ASC));

		return getHitsFromSearch(indexName, searchSourceBuilder, client);
	}

	/**
	 * 
	 * @param client
	 * @param indexName
	 * @param sUrl
	 * @param sUser
	 * @param fieldNames
	 * @return
	 * @throws IOException
	 */
	private SearchHit[] searchByUserAndUrl(RestHighLevelClient client, String indexName, String url, String userId,
			List<String> fieldNames) throws IOException {

		QueryBuilder queryBuilder = QueryBuilders.boolQuery()
				.must(QueryBuilders.matchPhraseQuery(fieldNames.get(0), userId))
				.must(QueryBuilders.matchPhraseQuery(fieldNames.get(1), url));

		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(queryBuilder).size(IConfigConstants.MAX_SIZE_SHOW_DOCUMENT)
				.sort(new FieldSortBuilder(fieldNames.get(2)).order(SortOrder.ASC)).sort("_doc", SortOrder.ASC);
		return getHitsFromSearch(indexName, searchSourceBuilder, client);
	}

	/**
	 * 
	 * @param client
	 * @param indexName
	 * @param fieldNames
	 * @throws ParseException
	 * @throws IOException
	 */
//	public boolean saveUserActivitesInAllDays(final RestHighLevelClient client, final String indexName,
//			final List<String> fieldNames, final String scheduledFireTime) throws ParseException, IOException {
//		List<UserActivityDB> addUserActs = new ArrayList<>();
//
//		System.out.println("scheduledFireTime " + scheduledFireTime);
//		int hour = 0;
//		String date = "";
//		String fromDate = "";
//		String toDate = "";
//		LocalDateTime dataFire = LocalDateTime.parse(scheduledFireTime, SCHEDULE_FIRETIME);
//		hour = dataFire.getHour();
//		String[] temp = scheduledFireTime.split(" ");
//		date = temp[0];
//
//		if (hour < 12) {
//			fromDate = date + "T08+0700";
//			toDate = date + "T11:30+0700";
//		} else {
//			fromDate = date + "T11:31+0700";
//			toDate = date + "T15:30+0700";
//		}
//
//		SearchResponse searchResponse = groupByUserActs(client, indexName, fieldNames, fromDate, toDate);
//		if (searchResponse == null) {
//			LOGGER.error("search response is null");
//			return false;
//		}
//		Terms termsUser = searchResponse.getAggregations().get(fieldNames.get(0) + AGGS_SUFFIX);
//		if (termsUser == null || termsUser.getBuckets().size() == 0) {
//			LOGGER.error("Users is null");
//			return false;
//		}
//		for (Terms.Bucket user : termsUser.getBuckets()) {
//			String sUser = user.getKeyAsString();
//			Terms termsUrl = user.getAggregations().get(fieldNames.get(1) + AGGS_SUFFIX);
//			if (termsUrl == null || termsUrl.getBuckets().size() == 0) {
//				LOGGER.error(sUser + " URL is null");
//				return false;
//			}
//			for (Terms.Bucket url : termsUrl.getBuckets()) {
//				String sUrl = url.getKeyAsString();
//				if (!sUrl.isEmpty() && sUrl.contains(sCONNECT)) {
//
//					List<Double> dates = new ArrayList<>();
//					SearchHit[] searchHitUserActs = searchByUserAndUrlAndDate(client, indexName, sUrl,
//							scheduledFireTime, sUser, fieldNames);
//					if (null == searchHitUserActs || searchHitUserActs.length == 0) {
//						LOGGER.error("Not found data in Elastic Search");
//						return false;
//					}
//					Set<String> days = new HashSet<>();
//					for (SearchHit hitUserAct : searchHitUserActs) {
//						days.add(getDate(hitUserAct.getSourceAsMap().get(fieldNames.get(2)).toString()));
//					}
//
//					for (String day : days) {
//						for (SearchHit hitUserAct : searchHitUserActs) {
//							if (hitUserAct.getSourceAsMap().get(fieldNames.get(2)).toString().contains(day)) {
//								dates.add(
//										toMilliseconds(hitUserAct.getSourceAsMap().get(fieldNames.get(2)).toString()));
//							}
//						}
//						UserActivityDB userActivity1 = new UserActivityDB(sUser, sUrl, scheduledFireTime,
//								countTimesTraffic(dates), countTotalTime(dates));
//						LOGGER.info("User activity: " + userActivity1);
//						addUserActs.add(userActivity1);
//					}
//				}
//			}
//		}
//
//		for (Terms.Bucket user : termsUser.getBuckets()) {
//			String sUser = user.getKeyAsString();
//			Terms termsUrl = user.getAggregations().get(fieldNames.get(1) + AGGS_SUFFIX);
//			if (termsUrl == null || termsUrl.getBuckets().size() == 0) {
//				LOGGER.error(sUser + " URL is null");
//				return false;
//			}
//
//			for (Terms.Bucket url : termsUrl.getBuckets()) {
//				String sUrl = url.getKeyAsString();
//				if (sUrl.isEmpty() || !sUrl.contains(sCONNECT)) {
//					LOGGER.error(sUrl + "format is wrong");
//					continue;
//				}
//				SearchHit[] searchHits = searchByUserAndUrl(client, indexName, sUrl, sUser, fieldNames);
//				if (null == searchHits || searchHits.length == 0) {
//					LOGGER.error("Not found data in Elastic Search");
//					return false;
//				}
//
//				String day = getDate(searchHits[0].getSourceAsMap().get(fieldNames.get(2)).toString());
//				List<Double> dates = new ArrayList<>();
//				for (SearchHit hit : searchHits) {
//					dates.add(toMilliseconds(hit.getSourceAsMap().get(fieldNames.get(2)).toString()));
//				}
//				UserActivityDB userActivity1 = new UserActivityDB(sUser, sUrl, day, countTimesTraffic(dates),
//						countTotalTime(dates));
//				LOGGER.info("User activity: " + userActivity1);
//				addUserActs.add(userActivity1);
//			}
//		}
//		return saveAllUserActs(addUserActs);
//	}

	private boolean handleSaveUserActivitesInDay(final RestHighLevelClient client, final String indexName,
			final List<String> fieldNames, final Terms termsUser) throws ParseException, IOException {
		List<UserActivityDB> addUserActs = new ArrayList<>();
		int indexUser = 0;
		int indexUrl = 0;
		int indexTime = 0;
		int traceIndexUser = -1;
		int traceIndexUrl = -1;
		Bucket user;
		Bucket url;
		String sUser = null;
		String sUrl = null;
		String day = null;
		final String localTimeField = fieldNames.get(2);
		final String urlField = fieldNames.get(1);

		Terms termsUrl;
		SearchHit[] dateSearchHits = null;
		List<Double> dates = new ArrayList<>();
		List<? extends Bucket> lstUrls = null;

		List<? extends Bucket> lstUsers = termsUser.getBuckets();

		long startTime = System.nanoTime();

		Map<String, Users> mapUsers = findAllUsers();
		Map<MyKey, UserActivityDB> mapUserActs = findAllUserActs();

		long finishTime = System.nanoTime();
		long durationInMillis = TimeUnit.NANOSECONDS.toMillis(finishTime - startTime);
		LOGGER.info("testtime findAll loop took: " + durationInMillis + " ms");

		long startTime1 = System.nanoTime();
		int sizeUsers = lstUsers.size();
		int sizeUrls = 0;
		while (indexUser < sizeUsers) {
			if (traceIndexUser != indexUser) {
				user = lstUsers.get(indexUser);
				sUser = user.getKeyAsString();
				if (sUser.isEmpty() || sUser == null) {
					LOGGER.error(sUser + " not found");
					return false;
				}
				termsUrl = user.getAggregations().get(urlField + AGGS_SUFFIX);
				lstUrls = termsUrl.getBuckets();
				sizeUrls = lstUrls.size();
			}
			if (indexUrl < sizeUrls) {
				if (traceIndexUrl != indexUrl) {
					url = lstUrls.get(indexUrl);
					sUrl = url.getKeyAsString();
					if (!sUrl.contains(sCONNECT) || sUrl == null) {
						LOGGER.error("wrong format: " + sUrl);
						indexTime = 0;
						dates.clear();
						traceIndexUrl = indexUrl;
						indexUrl++;
						continue;
					}
					dateSearchHits = searchByUserAndUrl(client, indexName, sUrl, sUser, fieldNames);
					if (null == dateSearchHits || dateSearchHits.length == 0) {
						LOGGER.error("Not found data in Elastic Search");
						return false;
					}
					day = getDate(dateSearchHits[0].getSourceAsMap().get(localTimeField).toString());
				}
				if (indexTime < dateSearchHits.length) {
					dates.add(
							toMilliseconds(dateSearchHits[indexTime].getSourceAsMap().get(localTimeField).toString()));
					indexTime++;
					traceIndexUrl = indexUrl;
				} else {
					if (mapUsers.get(sUser) != null) {
						UserActivityDB userActivity = initUserToUpdateOrInsert(sUser, sUrl, day, dates, mapUserActs);
						addUserActs.add(userActivity);
					} else {
						LOGGER.error(sUser + " is not exist in table");
						return false;
					}
					dates.clear();
					indexTime = 0;
					indexUrl++;
				}
				traceIndexUser = indexUser;
			} else {
				indexUser++;
				indexUrl = 0;
			}
		}
		long finishTime1 = System.nanoTime();
		long durationInMillis1 = TimeUnit.NANOSECONDS.toMillis(finishTime1 - startTime1);
		LOGGER.info("testtime once While loop took: " + durationInMillis1 + " ms");
		return saveAllUserActs(addUserActs);
	}

	/**
	 * 
	 * @param userActKey
	 * @param dates
	 * @return
	 */
	public UserActivityDB initUserToUpdateOrInsert(String sUser, String sUrl, String day, final List<Double> dates,
			Map<MyKey, UserActivityDB> mapUserActs) {
		MyKey userActKey = new MyKey(sUser, sUrl, day);
		UserActivityDB userActivity = mapUserActs.get(userActKey);
		if (userActivity != null) {
			userActivity.setCount(userActivity.getCount() + countTimesTraffic(dates));
			userActivity.setTotal_time(userActivity.getTotal_time() + countTotalTime(dates));
		} else {
			userActivity = new UserActivityDB(userActKey.getUserId(), userActKey.getUrl(), userActKey.getDate(),
					countTimesTraffic(dates), countTotalTime(dates));
		}
		LOGGER.info("User activity: " + userActivity);
		return userActivity;
	}

	/**
	 * 
	 * @return
	 */
	public Map<MyKey, UserActivityDB> findAllUserActs() {
		return activityDBRepo.findAll().stream()
				.collect(Collectors.toMap(k -> new MyKey(k.getUserId(), k.getUrl(), k.getDate()), Function.identity()));
	}

	/**
	 * 
	 * @return
	 */
	public Map<String, Users> findAllUsers() {
		return userRepo.findAll().stream().collect(Collectors.toMap(Users::getUserId, Function.identity()));
	}

	/**
	 * 
	 * @param client
	 * @param indexName
	 * @param fieldNames
	 * @param scheduledFireTime
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 */
	public boolean saveUserActivitesInDay(final RestHighLevelClient client, final String indexName,
			final List<String> fieldNames, final List<Integer> sizes, final String scheduledFireTime)
			throws ParseException, IOException {
		List<String> scheduledTimes = getScheduledFireTime(scheduledFireTime);
		long startTime = System.nanoTime();

		AggregationBuilder aggregationBuilder = aggByUserIdAndUrl(fieldNames, sizes);
		SearchResponse searchResponse = groupByUserActs(client, indexName, fieldNames, scheduledTimes,
				aggregationBuilder);

		long finishTime = System.nanoTime();
		long durationInMillis = TimeUnit.NANOSECONDS.toMillis(finishTime - startTime);
		LOGGER.info("testtime Groupby took: " + durationInMillis + " ms");
		if (searchResponse == null) {
			LOGGER.error("search response is null");
			return false;
		}
		Terms termsUser = searchResponse.getAggregations().get(fieldNames.get(0) + AGGS_SUFFIX);
		if (termsUser == null || termsUser.getBuckets().size() == 0) {
			LOGGER.error("Users is null");
			return false;
		}
		return handleSaveUserActivitesInDay(client, indexName, fieldNames, termsUser);
	}

	private List<String> getScheduledFireTime(String scheduledFireTime) {
		String date = scheduledFireTime.substring(0, scheduledFireTime.indexOf(" "));
		int hour = 0;
		LocalDateTime dataFire = LocalDateTime.parse(scheduledFireTime, SCHEDULE_FIRETIME);
		hour = dataFire.getHour();
		List<String> dates = new ArrayList<>();

		dates = hour < 12 ? Stream.of(date + M_START_TIME, date + M_END_TIME).collect(Collectors.toList())
				: Stream.of(date + A_START_TIME, date + A_END_TIME).collect(Collectors.toList());
		return dates;
	}

	/**
	 * 
	 * @param userActs
	 * @return
	 */
	public boolean saveAllUserActs(final List<UserActivityDB> userActs) {
		if (userActs == null || userActs.size() == 0) {
			return false;
		}
		long startTime = System.nanoTime();

		if (activityDBRepo.saveAll(userActs).isEmpty())
			return false;

		long finishTime = System.nanoTime();
		long durationInMillis = TimeUnit.NANOSECONDS.toMillis(finishTime - startTime);
		LOGGER.info("testtime Insert data loop took: " + durationInMillis + " ms");
		return true;
	}

	/**
	 * 
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public String getDate(final String date) throws ParseException {
		LocalDateTime result = LocalDateTime.parse(date, LOCAL_TIME);
		return result.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	}

	/**
	 * 
	 * @param myDate
	 * @return
	 * @throws ParseException
	 */
	public double toMilliseconds(final String myDate) {
		LocalDateTime milis = LocalDateTime.parse(myDate, LOCAL_TIME);
		return milis.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
	}

	/**
	 * 
	 * @param time
	 * @return
	 */
	public double MillisecondsToSecondS(final double time) {
		return TimeUnit.MILLISECONDS.toSeconds((long) time);
	}

	/**
	 * 
	 * @param times
	 * @return
	 */
	public int countTimesTraffic(final List<Double> times) {
		int size = times.size();
		if (size <= 1) {
			return 1;
		}
		int count = (size % 2 == 0) ? 0 : 1;
		for (int i = 0; i < size - 1; i += 2) {
			double diffTime = times.get(i + 1) - times.get(i);
			count = diffTime > WAITTIME ? (count += 2) : ++count;
		}
		return count;
	}

	/**
	 * 
	 * @param times
	 * @return
	 */
	public double countTotalTime(final List<Double> times) {
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
		List<String> fieldNames = Stream
				.of(IConfigConstants.USER_ID_FIELD, IConfigConstants.URL_FIELD, IConfigConstants.LOCALDATE_FIELD)
				.collect(Collectors.toList());
		List<Integer> sizes = Stream
				.of(IConfigConstants.MAX_NUM_USER, IConfigConstants.MAX_NUM_URL, IConfigConstants.MAX_NUM_TIMES_TRAFFIC)
				.collect(Collectors.toList());
		Timestamp scheduledFireTime = new Timestamp(context.getScheduledFireTime().getTime());
		try {
			if (!saveUserActivitesInDay(client, IConfigConstants.INDEX_NAME, fieldNames, sizes,
					scheduledFireTime.toString())) {
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
