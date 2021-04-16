package com.server.trafficweb.controllers;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.log4j.Logger;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.server.trafficweb.constants.IConfigConstants;
import com.server.trafficweb.service.PlaygroundServices;
import com.server.trafficweb.service.UserActivityService;

@RestController
public class UserActivityController {
	final static Logger LOGGER = Logger.getLogger(UserActivityController.class);

	@Autowired
	private PlaygroundServices playGround;

	@Autowired
	private UserActivityService<?> service;

	@GetMapping("/runCronJob")
	public void runCronJob() {
		playGround.runCronJobHandler();
	}

	@GetMapping("/saveAll")
	public void testSave() throws ParseException, IOException {
		RestHighLevelClient client = service.createRestHighLevelClient(IConfigConstants.HOSTNAME, IConfigConstants.PORT,
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

		String strScheduledFireTime = "2021-04-15 15:50:00.0";
		long startTime = System.nanoTime();
		try {
			if (!service.saveUserActivitesInDay(client, IConfigConstants.INDEX_NAME, fieldNames, sizes,
					strScheduledFireTime)) {
				LOGGER.error("Failed to save data");
			}
		} catch (ParseException e) {
			LOGGER.error(e.getMessage() + e.getStackTrace());
		} catch (IOException e) {
			LOGGER.error(e.getMessage() + e.getStackTrace());
		} finally {
			try {
				client.close();
			} catch (IOException e) {
				LOGGER.error(e.getMessage() + e.getStackTrace());
			}
		}
		long finishTime = System.nanoTime();
		long durationInMillis = TimeUnit.NANOSECONDS.toMillis(finishTime - startTime);
		System.out.println("That took: " + durationInMillis + " ms");
	}
}