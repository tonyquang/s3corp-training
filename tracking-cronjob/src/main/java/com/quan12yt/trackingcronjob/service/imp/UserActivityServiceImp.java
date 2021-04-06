package com.quan12yt.trackingcronjob.service.imp;

import com.quan12yt.trackingcronjob.exception.DataNotFoundException;
import com.quan12yt.trackingcronjob.exception.VariablesUnacceptedException;
import com.quan12yt.trackingcronjob.model.UserActivity;
import com.quan12yt.trackingcronjob.repository.UserActivityRepository;
import com.quan12yt.trackingcronjob.service.UserActivityService;
import com.quan12yt.trackingcronjob.util.ValidateUtil;
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
        List<String> listUserId;
        listUserId = activityRepository.getViolatedUserByDateAndUrl(url, date);
        if (listUserId.isEmpty()) {
            throw new DataNotFoundException("Can't find any violated user");
        }
        return listUserId;
    }

    @Override
    public Object[][] getViolatedUserByDateAndUrlAndMonth(String url, String userId, String month) {
        if (!ValidateUtil.isValidMonth(month)){
            throw new VariablesUnacceptedException("Month value is invalid");
        }
        if (ValidateUtil.isEmptyOrNull(url, userId)) {
            throw new VariablesUnacceptedException("Url or date is null or empty");
        }
        List<UserActivity> listUserId;
        listUserId = activityRepository.findByUserIdAndUrlAndMonth(url, userId, month);
        if (listUserId.isEmpty()) {
            throw new DataNotFoundException("Can't find any user activities for the given data");
        }
        Object[][] result = new Object[listUserId.size()][3];
        int i = 0;
        for (UserActivity activity : listUserId) {
            result[i][0] = activity.getDate().substring(activity.getDate().length()-2);
            result[i][1] = activity.getCount();
            result[i][2] = activity.getTotalTime();
            i++;
        }
        return result;
    }



}
