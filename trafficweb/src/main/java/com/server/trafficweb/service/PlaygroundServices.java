package com.server.trafficweb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easyquartz.scheduler.ScheduleService;

@Service
public class PlaygroundServices {

    private final ScheduleService scheduleService;

    @Autowired
    public PlaygroundServices(final ScheduleService scheduleService){
        this.scheduleService = scheduleService;
    }

    public void runCronJobHandler(){
        scheduleService.schedule(UserActivityService.class, "cronJobDB", "0 50 11,15 ? * *");
    }

}