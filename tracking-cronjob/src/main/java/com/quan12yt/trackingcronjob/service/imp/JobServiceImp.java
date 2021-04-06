package com.quan12yt.trackingcronjob.service.imp;

import com.easyquartz.scheduler.ScheduleService;
import com.quan12yt.trackingcronjob.job.SendEmailJob;
import com.quan12yt.trackingcronjob.service.JobService;
import com.quan12yt.trackingcronjob.util.ValueConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobServiceImp implements JobService {

    final Logger logger = LoggerFactory.getLogger(JobServiceImp.class);

    @Autowired
    private ScheduleService scheduleService;

    @Override
    public boolean runSendEmailJoh() {
        try {
            scheduleService.schedule(SendEmailJob.class, "quanpham", ValueConstant.CRON_EXPRESSION);
            logger.info("Send email job is running");
            return true;
        }catch (Exception e){
            logger.error(e.getMessage());
            return false;
        }
    }
}
