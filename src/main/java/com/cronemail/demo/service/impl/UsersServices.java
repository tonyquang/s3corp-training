package com.cronemail.demo.service.impl;

import com.cronemail.demo.repository.IUsersRepository;
import com.cronemail.demo.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsersServices implements IUserService {

    @Autowired
    IUsersRepository usersRepository;

    @Override
    public String selectEmailFromUserID(String userID) {
        return usersRepository.findByUserId(userID).getEmail();
    }
}
