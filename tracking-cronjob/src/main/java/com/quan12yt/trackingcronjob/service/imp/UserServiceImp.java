package com.quan12yt.trackingcronjob.service.imp;

import com.quan12yt.trackingcronjob.exception.DataNotFoundException;
import com.quan12yt.trackingcronjob.model.Users;
import com.quan12yt.trackingcronjob.repository.UserRepository;
import com.quan12yt.trackingcronjob.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImp implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Users> getUsers() {
        return userRepository.findAll();
    }

    public List<Users> getUsersById(List<String> userIds) {
        List<Users> usersList;
        usersList = userRepository.findAllById(userIds);
        if (usersList.isEmpty()) {
            throw new DataNotFoundException("Can't find any user for the given user id");
        }
        return usersList;
    }


}
