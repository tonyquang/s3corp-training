package com.quan12yt.trackingcronjob.controller;

import com.quan12yt.trackingcronjob.dto.response.ResponseMessage;
import com.quan12yt.trackingcronjob.service.PdfService;
import com.quan12yt.trackingcronjob.service.UserActivityService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;

@Controller
public class PdfController {

    @Autowired
    UserActivityService activityService;
    @Autowired
    PdfService pdfService;


    @RequestMapping(path = "/api/exportCsv")
    @PreAuthorize("hasRole('cronrole')")
    public ResponseEntity<ResponseMessage> exportCsvFile() throws IOException {
        String outputFileName = "activity.csv";
        IOUtils.copy(pdfService.exportCsv(), new FileOutputStream(outputFileName));
        return new ResponseEntity<>(new ResponseMessage("Export csv file succeed !!"
                , LocalDateTime.now().toString()), HttpStatus.OK);
    }

    @RequestMapping(path = "/api/report")
    @PreAuthorize("hasRole('cronrole')")
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
