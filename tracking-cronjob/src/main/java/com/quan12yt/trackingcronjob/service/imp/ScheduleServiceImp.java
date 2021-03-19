package com.quan12yt.trackingcronjob.service.imp;

import com.quan12yt.trackingcronjob.model.JobInfo;
import com.quan12yt.trackingcronjob.service.ScheduleService;
import com.quan12yt.trackingcronjob.util.JobUtils;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class ScheduleServiceImp implements ScheduleService {
    private final Scheduler scheduler;
    final Logger logger = LoggerFactory.getLogger(ScheduleServiceImp.class);

    @Autowired
    public ScheduleServiceImp(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public <T extends Job> void schedule(final Class<T> jobClass, final JobInfo info) {
        final JobDetail jobDetail = JobUtils.buildJobDetail(jobClass, info);
        final Trigger trigger = JobUtils.buildTrigger(jobClass, info);
        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            logger.error(e.getMessage());
        }
    }

    @PostConstruct
    public void init() {
        try {
            scheduler.start();
        } catch (SchedulerException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public void preDestroy() {
        try {
            scheduler.shutdown();
        } catch (SchedulerException e) {
            logger.error(e.getMessage(), e);
        }
    }

}
