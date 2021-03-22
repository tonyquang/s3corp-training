package com.cronemail.demo.service.timeservices;

import com.cronemail.demo.utils.TimerUtils;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;


@Service
@Slf4j
public class ScheduleService {
    private final Scheduler scheduler;

    @Autowired
    public ScheduleService(Scheduler scheduler){
        this.scheduler = scheduler;
    }

    @PostConstruct
    public void init(){
        try {
            scheduler.start();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public <T extends Job> void schedule(final Class<T> jobClass, final String cronJobExpression) {
        final JobDetail jobDetail = TimerUtils.buildJobDetail(jobClass, cronJobExpression);
        final Trigger trigger = TimerUtils.buildTrigger(jobClass, cronJobExpression);

        try {
            scheduler.scheduleJob(jobDetail, trigger);

        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
        }
    }

    @PreDestroy
    public void preDestroy(){
        try {
            scheduler.shutdown();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}
