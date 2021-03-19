package com.quan12yt.trackingcronjob.service.imp;

import com.quan12yt.trackingcronjob.job.SendEmailJob;
import com.quan12yt.trackingcronjob.job.UpdateActivityJob;
import com.quan12yt.trackingcronjob.model.JobInfo;
import com.quan12yt.trackingcronjob.model.UserActivity;
import com.quan12yt.trackingcronjob.service.JobService;
import lombok.SneakyThrows;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobServiceImp implements JobService {

    @Autowired
    private ScheduleServiceImp scheduleService;

    @Override
    public void runSendEmailJoh() {
        final JobInfo info = new JobInfo();
        info.setTotalFireCount(5);
        info.setRemainingFireCount(info.getTotalFireCount());
        info.setInitialOffsetMs(1000L);
        info.setCallbackData("My callback data");
        scheduleService.schedule(SendEmailJob.class, info);
    }

    @Override
    public void runUpdateActivityJob(UserActivity userActivity, Integer count, Integer time) {
        final JobInfo info = new JobInfo();
        info.setTotalFireCount(5);
        info.setRemainingFireCount(info.getTotalFireCount());
        info.setInitialOffsetMs(1000L);
        info.setTime(time);
        info.setCallbackData("My callback data");
        info.setUserActivity(userActivity);
        info.setAccessCount(count);
        scheduleService.schedule(UpdateActivityJob.class, info);
    }

    @Override
    public void stopJob(String job) throws SchedulerException {
        scheduleService.stopJob(job);
    }
}
