package com.cronemail.demo.service.impl;

import com.cronemail.demo.model.EmailModel;
import com.cronemail.demo.service.IEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServices implements IEmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public void sendMail(EmailModel emailModel) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(emailModel.getEmail().toArray(String[]::new));

        msg.setSubject(emailModel.getSubject());
        msg.setText(emailModel.getContent());

        javaMailSender.send(msg);
    }
}
