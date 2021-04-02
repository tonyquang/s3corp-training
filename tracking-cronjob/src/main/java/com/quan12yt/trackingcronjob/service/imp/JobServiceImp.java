package com.quan12yt.trackingcronjob.service.imp;

import com.easyquartz.scheduler.ScheduleService;
import com.quan12yt.trackingcronjob.job.SendEmailJob;
import com.quan12yt.trackingcronjob.model.JobMapData;
import com.quan12yt.trackingcronjob.service.JobService;
import com.quan12yt.trackingcronjob.util.ValueConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobServiceImp implements JobService {

    @Autowired
    private ScheduleService scheduleService;

    @Override
    public void runSendEmailJoh() {
        final JobMapData info = new JobMapData();
        info.setInitialOffsetMs(1000L);
        scheduleService.schedule(SendEmailJob.class,"quanpham", ValueConstant.CRON_EXPRESSION);
    }

//    @Override
//    public void runUpdateActivityJob(UserActivity userActivity, Integer count, Integer time) {
//        final JobMapData info = new JobMapData();
//        info.setInitialOffsetMs(1000L);
//        info.setTime(time);
//        info.setUserActivity(userActivity);
//        info.setAccessCount(count);
//        scheduleService.schedule(UpdateActivityJob.class, info);
//    }

//    @Override
//    public void stopJob(String job) throws SchedulerException {
//        scheduleService.stopJob(job);
//    }
//
//    @Override
//    public void stop() {
//        scheduleService.shutDownSchedule();
//    }
}
