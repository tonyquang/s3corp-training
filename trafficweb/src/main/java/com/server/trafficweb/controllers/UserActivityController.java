package com.server.trafficweb.controllers;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.server.trafficweb.constants.IConfigConstants;
import com.server.trafficweb.service.UserActivityService;

@RestController
public class UserActivityController {
	final static Logger LOGGER = Logger.getLogger(UserActivityController.class);
	private String USER_ID = "user_id";
	private String URL = "url";
	private String TIME_STAMP = "@timestamp";
	private String INDEX_NAME = "proxyinfo";

	@Autowired
	private UserActivityService userActivityService;

	@GetMapping("/saveUserActivity")
	public void saveUserActivity() {
		RestHighLevelClient client = userActivityService.createRestHighLevelClient(IConfigConstants.HOSTNAME,
				IConfigConstants.PORT, IConfigConstants.PROTOCOL);
		try {
			List<String> fieldNames = new ArrayList<>();
			fieldNames.add(USER_ID);
			fieldNames.add(URL);
			fieldNames.add(TIME_STAMP);
			userActivityService.saveDocument(client, INDEX_NAME, fieldNames);
		} catch (ParseException e) {
			e.printStackTrace();
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

	}
}
