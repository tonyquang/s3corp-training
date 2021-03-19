package com.quan12yt.trackingcronjob.job;

import com.quan12yt.trackingcronjob.model.JobInfo;
import com.quan12yt.trackingcronjob.model.UserActivity;
import com.quan12yt.trackingcronjob.service.UserActivityService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import java.util.Optional;

@Component
public class UpdateActivityJob implements Job {
    private final Logger logg = LoggerFactory.getLogger(UpdateActivityJob.class);

    @Autowired
    private UserActivityService activityService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
        JobInfo jobInfo = (JobInfo) jobExecutionContext.getMergedJobDataMap().get("mapData");

        Optional<UserActivity> uActivity = activityService
                .updateActivity(jobInfo.getUserActivity(), jobInfo.getAccessCount(), jobInfo.getTime());
        logg.info("User Activity Detail: " + uActivity.toString());

    }
}
