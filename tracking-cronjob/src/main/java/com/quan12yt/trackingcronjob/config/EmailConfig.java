package com.quan12yt.trackingcronjob.config;

import com.quan12yt.trackingcronjob.util.ValueConstant;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

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

    @Bean
    public MimeMessage mimeMessageTemplate(JavaMailSender sender) throws MessagingException {
        MimeMessage mimeMessage = sender.createMimeMessage();
        mimeMessage.setFrom(ValueConstant.EMAIL);
        mimeMessage.setSubject(ValueConstant.EMAIL_SUBJECT);
        mimeMessage.setText(ValueConstant.EMAIL_TEXT);
        return mimeMessage;
    }
}
