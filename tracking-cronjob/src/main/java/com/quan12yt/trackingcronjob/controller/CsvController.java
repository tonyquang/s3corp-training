package com.quan12yt.trackingcronjob.controller;

import com.quan12yt.trackingcronjob.dto.ResponseMessage;
import com.quan12yt.trackingcronjob.dto.UserActivityResponse;
import com.quan12yt.trackingcronjob.service.UserActivityService;
import com.quan12yt.trackingcronjob.service.imp.CsvServiceImp;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Controller
public class CsvController {

    @Autowired
    UserActivityService activityService;
    @Autowired
    CsvServiceImp csvServiceImp;


    @RequestMapping(path = "/api/exportCsv")
    public ResponseEntity<ResponseMessage> exportCsvFile() throws IOException {
        String outputFileName = "activity.csv";
        IOUtils.copy(csvServiceImp.exportCsv(), new FileOutputStream(outputFileName));
        return new ResponseEntity<>(new ResponseMessage("Export csv file succeed !!"
                , LocalDateTime.now().toString()), HttpStatus.OK);
    }

    @RequestMapping(path = "/api/report")
    public String showChartByMonth(ModelMap model, @RequestParam String userName, @RequestParam String month) {
        model.addAttribute("userName", userName);
        model.addAttribute("month", month);
        model.addAttribute("countData", getData(1, userName, month));
        model.addAttribute("totalTimeData", getData(2, userName, month));
        return "report_template";
    }

    public Object[][] getData(Integer position, String userName, String month){
        Object[][] objects = activityService.getViolatedUserByDateAndUrlAndMonth("www.facebook.com", userName, month);
        Object[][] result = new Object[objects.length][2];
        for (int i=0; i<objects.length; i++){
            result[i][0] = objects[i][0];
            result[i][1] = objects[i][position];
        }
        return result;
    }


}
