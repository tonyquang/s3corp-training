package com.quan12yt.trackingcronjob.service.imp;

import com.quan12yt.trackingcronjob.exception.DataNotFoundException;
import com.quan12yt.trackingcronjob.exception.VariablesUnacceptedException;
import com.quan12yt.trackingcronjob.model.Users;
import com.quan12yt.trackingcronjob.repository.UserRepository;
import com.quan12yt.trackingcronjob.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImp implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Users> getUsers() {
        List<Users> lsUsers = userRepository.findAll();
        return lsUsers;
    }

    public List<Users> getUsersById(List<String> userIds) {
        List<Users> usersList;
        usersList = userRepository.findAllById(userIds);
        return usersList;
    }


}
