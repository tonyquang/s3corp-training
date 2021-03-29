package com.cronemail.demo.service.impl;

import com.cronemail.demo.model.UserActivity;
import com.cronemail.demo.repository.IUserActivityRepository;
import com.cronemail.demo.service.IUserActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserActivityServices implements IUserActivityService {

    @Autowired
    IUserActivityRepository userActivityRepository;

    @Override
    public List<UserActivity> selectUserActivityList(String hostName,int count, String timeStamp) {
        return userActivityRepository.selectUserActivityBelongToFilter(hostName, count, timeStamp);
    }
}
