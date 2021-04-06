package com.quan12yt.trackingcronjob.unittest.service;

import com.quan12yt.trackingcronjob.exception.DataNotFoundException;
import com.quan12yt.trackingcronjob.exception.SendEmailFailedException;
import com.quan12yt.trackingcronjob.exception.WrongEmailFormatException;
import com.quan12yt.trackingcronjob.service.imp.EmailService;
import com.quan12yt.trackingcronjob.util.ValueConstant;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RunWith(SpringRunner.class)
public class EmailServiceTest {

    @Autowired
    EmailService emailService;

    @MockBean
    SimpleMailMessage simpleMailMessage;
    List<String> emailList = new ArrayList<>();

    @TestConfiguration
    public static class EmailServiceConfiguration{
        @Bean
        EmailService emailService(){
            return new EmailService();
        }

        @Bean
        JavaMailSender sender(){
            return new JavaMailSenderImpl();
        }
        @Bean
        SimpleMailMessage simpleMailMessage(){
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(ValueConstant.EMAIL);
            message.setSubject(ValueConstant.EMAIL_SUBJECT);
            message.setText(ValueConstant.EMAIL_TEXT);
            return message;
        }

    }

    @Before
    public void setup(){
        emailList.add("quan12yt@gmail.com");
        emailList.add("quan12yt@gmail.com");

    }
    @After
    public void clear(){
        emailList.clear();
    }

//    @Test
//    public void sendEmailSucceed(){
//        Logger fooLogger = (Logger) LoggerFactory.getLogger(EmailService.class);
//
//        // create and start a ListAppender
//        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
//        listAppender.start();
//
//        // add the appender to the logger
//        // addAppender is outdated now
//        fooLogger.addAppender(listAppender);
//
//        // call method under test
//        emailService.sendEmail(emailList);
//        // JUnit assertions
//        List<ILoggingEvent> logsList = listAppender.list;
//        Assert.assertEquals("Send warning email succeed to email : " + emailList.get(0), logsList.get(0)
//                .getMessage());
//    }

    @Test
    public void sendEmptyListEmail(){
        List<String> emptyList = Collections.emptyList();
        Throwable ex = Assert.assertThrows(DataNotFoundException.class, () -> emailService.sendEmail(emptyList));
        Assert.assertSame("List email can't be empty", ex.getMessage());
    }

    @Test
    public void sendWrongFormatEmail(){
        emailList.clear();
        emailList.add("Ads");
        Throwable ex = Assert.assertThrows(WrongEmailFormatException.class, () -> emailService.sendEmail(emailList));
        Assert.assertEquals("Email address is not valid : Ads", ex.getMessage());
    }

    @Test
    public void sendEmailFailed(){
        Throwable ex = Assert.assertThrows(SendEmailFailedException.class, () -> emailService.sendEmail(emailList));
        Assert.assertTrue(ex.getMessage().contains("Failed to send warning email"));
    }
}

