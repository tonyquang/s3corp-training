package com.quan12yt.trackingcronjob.service.imp;

import com.quan12yt.trackingcronjob.model.UserActivity;
import com.quan12yt.trackingcronjob.repository.UserActivityRepository;
import com.quan12yt.trackingcronjob.service.CsvService;
import com.quan12yt.trackingcronjob.util.CsvUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.List;

@Service
public class CsvServiceImp implements CsvService {

    @Autowired
    UserActivityRepository activityRepository;

    @Override
    public ByteArrayInputStream exportCsv() {
            List<UserActivity> tutorials = activityRepository.findAll();

            ByteArrayInputStream in = CsvUtils.toCSV(tutorials);
            return in;

    }
}
