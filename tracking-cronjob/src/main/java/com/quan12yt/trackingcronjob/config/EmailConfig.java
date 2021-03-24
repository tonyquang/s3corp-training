package com.quan12yt.trackingcronjob.config;

import com.quan12yt.trackingcronjob.util.ValueConstant;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;

@Configuration
public class EmailConfig {
    @Bean
    public SimpleMailMessage emailTemplate()
    {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(ValueConstant.EMAIL);
        message.setSubject(ValueConstant.EMAIL_SUBJECT);
        message.setText(ValueConstant.EMAIL_TEXT);
        return message;
    }

}
