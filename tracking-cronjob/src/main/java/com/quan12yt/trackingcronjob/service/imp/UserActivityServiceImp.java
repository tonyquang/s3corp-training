package com.quan12yt.trackingcronjob.service.imp;

import com.quan12yt.trackingcronjob.exception.DataNotFoundException;
import com.quan12yt.trackingcronjob.exception.VariablesUnacceptedException;
import com.quan12yt.trackingcronjob.model.UserActivity;
import com.quan12yt.trackingcronjob.model.UserActivityKey;
import com.quan12yt.trackingcronjob.repository.UserActivityRepository;
import com.quan12yt.trackingcronjob.service.UserActivityService;
import com.quan12yt.trackingcronjob.util.ValidateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
    public Optional<UserActivity> updateActivity(UserActivity userActivity, Integer count, Integer time) {
        return activityRepository.findById(
                new UserActivityKey(userActivity.getUserId(), userActivity.getUrl(), userActivity.getDate()))
                .map(t -> {
                    t.setAccessCount(t.getAccessCount() + count);
                    t.setTotalTime(t.getTotalTime() + time);

                    return activityRepository.save(t);
                });
    }


}
