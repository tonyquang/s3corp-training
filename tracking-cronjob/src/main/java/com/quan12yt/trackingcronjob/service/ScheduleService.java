package com.quan12yt.trackingcronjob.service;

import com.quan12yt.trackingcronjob.model.JobInfo;
import org.quartz.Job;

public interface ScheduleService {

    <T extends Job> void schedule(final Class<T> jobClass, final JobInfo info);

}
