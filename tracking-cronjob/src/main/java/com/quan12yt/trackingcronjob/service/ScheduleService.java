package com.quan12yt.trackingcronjob.service;

import com.quan12yt.trackingcronjob.model.JobMapData;
import org.quartz.Job;
import org.quartz.SchedulerException;

public interface ScheduleService {

    <T extends Job> void schedule(final Class<T> jobClass, final JobMapData info);
    void stopJob(String job) throws SchedulerException;
}
