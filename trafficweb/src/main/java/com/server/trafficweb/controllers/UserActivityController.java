package com.server.trafficweb.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.server.trafficweb.service.Playground;

@RestController
public class UserActivityController {
	final static Logger LOGGER = Logger.getLogger(UserActivityController.class);

	@Autowired
	private Playground playGround;

	@GetMapping("/startCronJob")
	public void saveUserActivity() {
		playGround.startCron();
	}
}
