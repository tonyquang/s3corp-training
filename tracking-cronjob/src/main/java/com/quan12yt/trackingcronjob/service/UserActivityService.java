package com.quan12yt.trackingcronjob.service;

import com.quan12yt.trackingcronjob.model.UserActivity;

import java.util.List;
import java.util.Optional;

public interface UserActivityService {
    List<String> getViolatedUserByDateAndUrl(String url, String date);

    Object[][] getViolatedUserByDateAndUrlAndMonth(String url, String userId, String month);

}
