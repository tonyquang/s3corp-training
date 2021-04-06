package com.quan12yt.trackingcronjob.service.imp;

import com.lowagie.text.DocumentException;
import com.quan12yt.trackingcronjob.exception.DataNotFoundException;
import com.quan12yt.trackingcronjob.model.UserActivity;
import com.quan12yt.trackingcronjob.repository.UserActivityRepository;
import com.quan12yt.trackingcronjob.service.PdfService;
import com.quan12yt.trackingcronjob.util.CsvUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@Service
public class PdfServiceImp implements PdfService {

    @Autowired
    UserActivityRepository activityRepository;

    @Override
    public ByteArrayInputStream exportCsv() {
            List<UserActivity> tutorials = activityRepository.findAll();
            if(tutorials.isEmpty()){
                throw new DataNotFoundException("Cant find any user activities to export");
            }
            ByteArrayInputStream in = CsvUtils.toCSV(tutorials);
            return in;
    }

}
