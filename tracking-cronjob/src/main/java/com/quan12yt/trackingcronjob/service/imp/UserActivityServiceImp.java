package com.quan12yt.trackingcronjob.service.imp;

import com.quan12yt.trackingcronjob.model.UserActivity;
import com.quan12yt.trackingcronjob.model.UserActivityKey;
import com.quan12yt.trackingcronjob.repository.UserActivityRepository;
import com.quan12yt.trackingcronjob.service.UserActivityService;
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
        date = date.replace("-","/");
        return activityRepository.getViolatedUserByDateAndUrl(url, date);
    }

    @Override
        public Optional<UserActivity> updateActivity(UserActivity userActivity, Integer count) {
        return activityRepository.findById(
                new UserActivityKey(userActivity.getUserId(), userActivity.getUrl(), userActivity.getDate()))
                .map(t -> {
            t.setAccessCount(t.getAccessCount() + count);
            return activityRepository.save(t);
        });
    }




}
