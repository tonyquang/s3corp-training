package com.cronemail.demo.service;

import com.cronemail.demo.model.EmailModel;
import com.cronemail.demo.model.UserActivity;
import com.cronemail.demo.utils.Constant;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CronJobService implements Job {

    @Autowired
    IEmailService emailServices;

    @Autowired
    IUserService usersServices;

    @Autowired
    IUserActivityService userActivityServices;

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Timestamp scheduledFireTime = new Timestamp(jobExecutionContext.getScheduledFireTime().getTime());
        String strScheduledFireTime = sdf.format(scheduledFireTime);
        sendMail(strScheduledFireTime);
    }

    private void sendMail(String timeStamp) {
        log.info(timeStamp);
        List<UserActivity> usersActivity = userActivityServices
                .selectUserActivityList(Constant.HOST_NAME,
                        Constant.MAX_TIME_ACCESS_FB,
                        timeStamp);
        if (usersActivity.isEmpty())
            return;

        List<String> emails = new ArrayList<>();
        for (UserActivity urAc : usersActivity) {
            String userID = usersServices.selectEmailFromUserID(urAc.getUserId());
            if(userID.isEmpty())
                continue;
            emails.add(usersServices.selectEmailFromUserID(urAc.getUserId()));
        }

        emailServices.sendMail(EmailModel.builder()
                .email(emails)
                .content(Constant.EMAIL_CONTENT)
                .subject(Constant.EMAIL_SUBJECT)
                .build());
    }

}
