package com.cronemail.demo.service;

import com.cronemail.demo.model.EmailModel;

public interface IEmailService {
    void sendMail(EmailModel emailModel);
}
