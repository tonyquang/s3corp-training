package com.cronemail.demo;

import com.cronemail.demo.model.EmailModel;
import com.cronemail.demo.service.IEmailService;
import com.cronemail.demo.utils.Constant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = DemoApplication.class)
@TestPropertySource("/TestEmailAuthenticationFail.properties")
public class EmailServicesSendMailAuthenticationFailTest {
    @Autowired
    IEmailService iEmailService;

    private String[] emailListValid;

    @BeforeEach
    public void setup(){
        emailListValid = new String[]{"quangbui14041999@gmail.com", "tonyquang9x@gmail.com"};
    }

    @Test
    void sendMailAuthenticationFailTest() {
        Throwable ex = assertThrows(MailAuthenticationException.class, () -> iEmailService.sendMail(
                EmailModel.builder()
                        .subject(Constant.EMAIL_SUBJECT)
                        .content(Constant.EMAIL_CONTENT)
                        .email(Arrays.asList(emailListValid))
                        .build()
        ));
        assertThat(ex.getMessage(), containsString("Authentication failed"));
    }
}
