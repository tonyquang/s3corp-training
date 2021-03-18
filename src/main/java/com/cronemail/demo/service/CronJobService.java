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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
        Timestamp currentDate = new Timestamp(System.currentTimeMillis());
        String strCurrentDate = sdf.format(currentDate);

        Date previousDate = jobExecutionContext.getPreviousFireTime();

        if (previousDate != null) {
            String strPreviousDate = sdf.format(previousDate);
            if (!strPreviousDate.equals(strCurrentDate)) {
                handleMissingCronJob(strCurrentDate, strPreviousDate);
            }
        }

        sendMail(strCurrentDate);
    }

    private void handleMissingCronJob(String currentDate, String previousFireTime) {
        long daysBetween = calculatorBetweenDays(currentDate, previousFireTime);
        if (daysBetween <= 0) return;

        String date = previousFireTime;
        for (int i = 0; i < daysBetween; i++) {
            date = addDate(date, i);
            sendMail(date);
        }
    }

    private void sendMail(String timeStamp) {
        List<UserActivity> usersActivity = userActivityServices
                .selectUserActivityList(Constant.HOST_NAME.name(),
                        Constant.MAX_TIME_ACCESS_FB.ordinal(),
                        timeStamp);
        if (usersActivity.isEmpty())
            return;

        List<String> emails = new ArrayList<>();
        for (UserActivity urAc : usersActivity) {
            emails.add(usersServices.selectEmailFromUserID(urAc.getUserId()));
        }

        emailServices.sendMail(EmailModel.builder()
                .email(emails)
                .content(Constant.EMAIL_CONTENT.name())
                .subject(Constant.EMAIL_SUBJECT.name())
                .build());
    }

    /*
    @param d1 and d2 format is yyyy.MM.dd
     */
    private long calculatorBetweenDays(String d1, String d2) {
        try {
            Date date1 = sdf.parse(d1);
            Date date2 = sdf.parse(d2);
            long diff = date2.getTime() - date1.getTime();
            return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private String addDate(String strDate, int dateNumberToAdd) {
        try {
            Date date = sdf.parse(strDate);
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(Calendar.DATE, dateNumberToAdd);
            Date currentDatePlusOne = c.getTime();
            return sdf.format(currentDatePlusOne);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return strDate;
    }
}
