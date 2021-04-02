//package com.quan12yt.trackingcronjob.util;
//
//
//import com.quan12yt.trackingcronjob.model.JobMapData;
//import org.quartz.*;
//
//import java.util.Date;
//
//public final class JobUtils {
//
//    private JobUtils() {}
//
//    public static JobDetail buildJobDetail(final Class jobClass) {
//        return JobBuilder
//                .newJob(jobClass)
//                .withIdentity(jobClass.getSimpleName(), "quan")
//                .build();
//    }
//
//    public static Trigger buildTrigger(final Class jobClass, final JobMapData info) {
//        final JobDataMap jobDataMap = new JobDataMap();
//        jobDataMap.put("mapData", info);
//        return TriggerBuilder
//                .newTrigger()
//                .usingJobData(jobDataMap)
//                .withIdentity(jobClass.getSimpleName(), "quan")
//                .withSchedule(
//                        CronScheduleBuilder.cronSchedule(ValueConstant.CRON_EXPRESSION)
//                       .withMisfireHandlingInstructionIgnoreMisfires()
//                )
//                .startAt(new Date(System.currentTimeMillis() + info.getInitialOffsetMs()))
//                .build();
//    }
//
//
//}
