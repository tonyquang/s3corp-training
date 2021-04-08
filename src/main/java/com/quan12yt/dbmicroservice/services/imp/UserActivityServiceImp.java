package com.quan12yt.dbmicroservice.services.imp;


import com.quan12yt.dbmicroservice.exceptions.VariablesUnacceptedException;
import com.quan12yt.dbmicroservice.models.UserActivity;
import com.quan12yt.dbmicroservice.repositories.UserActivityRepository;
import com.quan12yt.dbmicroservice.services.UserActivityService;
import com.quan12yt.dbmicroservice.utils.ValidateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserActivityServiceImp implements UserActivityService {

    @Autowired
    private UserActivityRepository activityRepository;

    @Override
    public List<String> getViolatedUserByDateAndUrl(String url, String date) {
        if (ValidateUtil.isEmptyOrNull(url, date)) {
            throw new VariablesUnacceptedException("Url or date is null or empty");
        }
        return activityRepository.getViolatedUserByDateAndUrl(url, date);
    }

    @Override
    public List<UserActivity> getViolatedUserByYearAndMonth(String url, String userId, String month, String year) {
        if (!ValidateUtil.isValidMonthAndYear(month, year)){
            throw new VariablesUnacceptedException("Month/year is invalid");
        }
        if (ValidateUtil.isEmptyOrNull(url, userId)) {
            throw new VariablesUnacceptedException("Url or date is null or empty");
        }
        String monthYear = year + "-"+month;
        return activityRepository.getByYearAndMonth(url, userId, monthYear);

    }

    @Override
    public List<UserActivity> getUserActivitiesByFilter(String hostName, Integer count, String timeStamp) {
        return activityRepository.selectUserActivityBelongToFilter(hostName, count, timeStamp);
    }

    @Override
    public List<UserActivity> getAllUserActivities() {
        return activityRepository.findAll();
    }

    @Override
    public UserActivity saveUser(UserActivity userActivity) {
        return activityRepository.save(userActivity);
    }


}
