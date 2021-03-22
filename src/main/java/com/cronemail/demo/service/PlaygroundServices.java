package com.cronemail.demo.service;

import com.cronemail.demo.service.timeservices.ScheduleService;
import com.cronemail.demo.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlaygroundServices {

    private final ScheduleService scheduleService;

    @Autowired
    public PlaygroundServices(final ScheduleService scheduleService){
        this.scheduleService = scheduleService;
    }

    public void runCronJobHandler(){
        scheduleService.schedule(CronJobService.class, Constant.CRONJOB_EXPRESSION);
    }

}
