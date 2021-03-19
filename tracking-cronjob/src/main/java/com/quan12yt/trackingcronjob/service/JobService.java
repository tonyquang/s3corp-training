package com.quan12yt.trackingcronjob.service;

import com.quan12yt.trackingcronjob.model.UserActivity;

public interface JobService {

    void runSendEmailJoh();

    void runUpdateActivityJob(UserActivity userActivity, Integer count);

    void stop();
}
