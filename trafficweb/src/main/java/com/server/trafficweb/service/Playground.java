package com.server.trafficweb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easyquartz.scheduler.ScheduleService;

@Service
public class Playground {

	private ScheduleService scheduleService;

	@Autowired
	public Playground(ScheduleService scheduleService) {
		this.scheduleService = scheduleService;
	}

	public void startCron() {
		scheduleService.schedule(UserActivityService.class, "aaa", "0 0 11,15 ?");
	}

}