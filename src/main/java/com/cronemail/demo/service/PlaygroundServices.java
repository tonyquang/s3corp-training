package com.cronemail.demo.service;

import com.cronemail.demo.utils.Constant;
import com.easyquartz.scheduler.ScheduleService;
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
        scheduleService.schedule(CronJobService.class, Constant.GROUP_ID ,Constant.CRONJOB_EXPRESSION);
    }

}
