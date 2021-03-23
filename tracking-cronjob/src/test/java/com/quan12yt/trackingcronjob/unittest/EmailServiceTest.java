package com.quan12yt.trackingcronjob.unittest;

import com.quan12yt.trackingcronjob.TrackingCronjobApplication;
import com.quan12yt.trackingcronjob.exception.DataNotFoundException;
import com.quan12yt.trackingcronjob.exception.SendEmailFailedException;
import com.quan12yt.trackingcronjob.exception.WrongEmailFormatException;
import com.quan12yt.trackingcronjob.service.imp.EmailService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SpringBootTest(classes = TrackingCronjobApplication.class)
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class EmailServiceTest {

    @Autowired
    EmailService emailService;

    @Autowired
    SimpleMailMessage simpleMailMessage;
    List<String> emailList = new ArrayList<>();

    @Before
    public void setup(){
        emailList.add("quan12yt@gmail.com");
        emailList.add("quan12yt@gmail.com");

    }

    @Test
    public void sendEmptyListEmail(){
        List<String> emptyList = Collections.emptyList();
        Throwable ex = Assert.assertThrows(DataNotFoundException.class, () -> emailService.sendEmail(emptyList));
        Assert.assertSame("List email can't be empty", ex.getMessage());
    }

    @Test
    public void sendWrongFormatEmail(){
        emailList.add("Ads");
        Throwable ex = Assert.assertThrows(WrongEmailFormatException.class, () -> emailService.sendEmail(emailList));
        Assert.assertEquals("Email address is not valid : Ads", ex.getMessage());
    }
}
