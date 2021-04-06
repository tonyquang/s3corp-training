package com.quan12yt.trackingcronjob.service;

import com.quan12yt.trackingcronjob.model.UserActivity;
import org.quartz.SchedulerException;

public interface JobService {

    boolean runSendEmailJoh();

//    void runUpdateActivityJob(UserActivity userActivity, Integer count, Integer time);
//
//    void stopJob(String job) throws SchedulerException;
//
//    void stop();
}
