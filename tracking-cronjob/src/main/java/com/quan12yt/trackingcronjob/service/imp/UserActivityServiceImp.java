package com.quan12yt.trackingcronjob.service.imp;

import com.quan12yt.trackingcronjob.dto.UserActivityResponse;
import com.quan12yt.trackingcronjob.exception.DataNotFoundException;
import com.quan12yt.trackingcronjob.exception.VariablesUnacceptedException;
import com.quan12yt.trackingcronjob.model.UserActivity;
import com.quan12yt.trackingcronjob.repository.UserActivityRepository;
import com.quan12yt.trackingcronjob.service.UserActivityService;
import com.quan12yt.trackingcronjob.util.ValidateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        List<UserActivityResponse> lsUserActivityResponses = new ArrayList<>();
        if (ValidateUtil.isEmptyOrNull(url, month)) {
            throw new VariablesUnacceptedException("Url or date is null or empty");
        }
        List<UserActivity> listUserId;
        listUserId = activityRepository.findByUserIdAndUrlAndMonth(url, userId, month);
        if (listUserId.isEmpty()) {
            throw new DataNotFoundException("Can't find any violated user");
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

//    @Override
//    public Optional<UserActivity> updateActivity(UserActivity userActivity, Integer count, Integer time) {
//        return activityRepository.findById(
//                new UserActivityKey(userActivity.getUserId(), userActivity.getUrl(), userActivity.getDate()))
//                .map(t -> {
//                    t.setCount(t.getCount() + count);
//                    t.setTotalTime(t.getTotalTime() + time);
//
//                    return activityRepository.save(t);
//                });
//    }


}
