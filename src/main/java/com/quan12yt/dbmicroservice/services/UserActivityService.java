package com.quan12yt.dbmicroservice.services;

import com.quan12yt.dbmicroservice.models.UserActivity;

import java.util.List;

public interface UserActivityService {
    List<String> getViolatedUserByDateAndUrl(String url, String date);

    List<UserActivity> getViolatedUserByYearAndMonth(String url, String userId, String month, String year);

    List<UserActivity> getUserActivitiesByFilter(String hostName, Integer count, String timeStamp);

    List<UserActivity> getAllUserActivities();

    UserActivity saveUser(UserActivity userActivity);
}
