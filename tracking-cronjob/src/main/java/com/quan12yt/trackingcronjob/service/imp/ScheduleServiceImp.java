package com.quan12yt.trackingcronjob.service.imp;

import com.quan12yt.trackingcronjob.model.JobMapData;
import com.quan12yt.trackingcronjob.service.ScheduleService;
import com.quan12yt.trackingcronjob.util.JobUtils;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class ScheduleServiceImp implements ScheduleService {
    private final Scheduler scheduler;
    final Logger logger = LoggerFactory.getLogger(ScheduleServiceImp.class);

    @Autowired
    public ScheduleServiceImp(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public <T extends Job> void schedule(final Class<T> jobClass, final JobMapData jobMapData) {
        final JobDetail jobDetail = JobUtils.buildJobDetail(jobClass);
        final Trigger trigger = JobUtils.buildTrigger(jobClass, jobMapData);
        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public void stopJob(String job) throws SchedulerException {
        Scheduler sched = new StdSchedulerFactory().getScheduler();

        List<JobExecutionContext> currentlyExecuting = scheduler.getCurrentlyExecutingJobs();
        logger.info(String.valueOf(currentlyExecuting.stream().count()));
        currentlyExecuting.forEach(t -> {
            if (t.getJobDetail().getKey().getName().equals(job)) {
                try {
                    scheduler.interrupt(t.getJobDetail().getKey());
                } catch (UnableToInterruptJobException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void shutDownSchedule() {
        try {
            scheduler.shutdown();
        } catch (SchedulerException e) {
            e.printStackTrace();
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


}
