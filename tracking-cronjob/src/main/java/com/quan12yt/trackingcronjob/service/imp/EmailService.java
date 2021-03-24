package com.quan12yt.trackingcronjob.service.imp;


import com.quan12yt.trackingcronjob.exception.DataNotFoundException;
import com.quan12yt.trackingcronjob.exception.SendEmailFailedException;
import com.quan12yt.trackingcronjob.exception.WrongEmailFormatException;
import com.quan12yt.trackingcronjob.util.ValidateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailService {
    private final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    JavaMailSender emailSender;
    @Autowired
    private SimpleMailMessage simpleMailMessage;

    public void sendEmail(List<String> lstEmail) {
        if (lstEmail.isEmpty()) {
            throw new DataNotFoundException("List email can't be empty");
        }
        lstEmail.forEach(t -> {
            if (!ValidateUtil.isEmail(t)) {
                throw new WrongEmailFormatException("Email address is not valid : " + t);
            }
            try {
                SimpleMailMessage message = new SimpleMailMessage(simpleMailMessage);
                message.setTo(t.trim());
                emailSender.send(message);
                logger.info("Send warning email succeed to email : " + t);
            } catch (Exception e) {
                logger.debug(e.getMessage());
                throw new SendEmailFailedException("Failed to send warning email");
            }
        });


    }
}
