package com.quan12yt.trackingcronjob.job;

import com.quan12yt.trackingcronjob.model.Users;
import com.quan12yt.trackingcronjob.service.UserActivityService;
import com.quan12yt.trackingcronjob.service.UserService;
import com.quan12yt.trackingcronjob.service.imp.EmailService;
import com.quan12yt.trackingcronjob.util.DateUtils;
import lombok.SneakyThrows;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Component
public class SendEmailJob implements Job {
    private final Logger logger = LoggerFactory.getLogger(SendEmailJob.class);

    @Autowired
    private UserActivityService activityService;
    @Autowired
    private UserService userService;
    @Autowired
    private EmailService emailService;


    @SneakyThrows
    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        logger.info("Previous schedule fire time :" + DateUtils.convertDateToString(jobExecutionContext.getPreviousFireTime()));
        logger.info("Schedule fire time :" + DateUtils.convertDateToString(jobExecutionContext.getScheduledFireTime()));
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
        try {
            String scheduleFireTime = DateUtils.convertDateToString(jobExecutionContext.getScheduledFireTime());
           // String date = DateUtils.convertDateToString(new Date());
            List<String> violatedUsers = activityService.getViolatedUserByDateAndUrl("facebook.com", scheduleFireTime);
            if (violatedUsers.isEmpty()) {
                logger.info("There isn't any violated user");
                return;
            }
            List<Users> users = userService.getUsersById(violatedUsers);
            if(!users.isEmpty()) {
                emailService.sendEmail(users.stream().map(t -> t.getEmail()).collect(Collectors.toList()));
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
