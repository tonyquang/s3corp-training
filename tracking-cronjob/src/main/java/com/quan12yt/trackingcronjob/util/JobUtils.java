package com.quan12yt.trackingcronjob.util;


import com.quan12yt.trackingcronjob.model.JobInfo;
import org.quartz.*;

import java.util.Date;

public final class JobUtils {

    private JobUtils() {}

    public static JobDetail buildJobDetail(final Class jobClass, final JobInfo info) {
        final JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put(jobClass.getSimpleName(), info);
        return JobBuilder
                .newJob(jobClass)
                .withIdentity(jobClass.getSimpleName())
                .requestRecovery(true)
                .setJobData(jobDataMap)
                .build();
    }

    public static Trigger buildTrigger(final Class jobClass, final JobInfo info) {
        final JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("mapData", info);
        return TriggerBuilder
                .newTrigger()
                .usingJobData(jobDataMap)
                .withIdentity(jobClass.getSimpleName())
                .withSchedule(
                        CronScheduleBuilder.cronSchedule(ValueConstant.CRON_EXPRESSION)
                        .withMisfireHandlingInstructionIgnoreMisfires()
                )
                .startAt(new Date(System.currentTimeMillis() + info.getInitialOffsetMs()))
                .build();
    }
}
