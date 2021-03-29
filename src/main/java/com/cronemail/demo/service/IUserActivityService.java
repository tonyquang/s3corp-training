package com.cronemail.demo.service;

import com.cronemail.demo.model.UserActivity;

import java.util.List;

public interface IUserActivityService {
    List<UserActivity> selectUserActivityList(String hostName,int count, String timeStamp);
}
