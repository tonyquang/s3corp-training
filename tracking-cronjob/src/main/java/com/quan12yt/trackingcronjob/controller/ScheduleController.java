package com.quan12yt.trackingcronjob.controller;

import com.quan12yt.trackingcronjob.dto.ResponseMessage;
import com.quan12yt.trackingcronjob.dto.UpdateRequest;
import com.quan12yt.trackingcronjob.exception.StartJobFailedException;
import com.quan12yt.trackingcronjob.service.JobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/schedule")
public class ScheduleController {
    private final Logger logger = LoggerFactory.getLogger(ScheduleController.class);

    @Autowired
    private JobService jobService;

    @GetMapping("/send")
    public ResponseEntity<ResponseMessage> sendEmail() {
        try {
            jobService.runSendEmailJoh();
            logger.info("Send email job is running");
            return new ResponseEntity<>(new ResponseMessage("Run Send Email job successfully"
                    , LocalDateTime.now().toString()), HttpStatus.OK);
        } catch (Exception e) {
            throw new StartJobFailedException("Failed to start Send email job");
        }
    }

    @PostMapping("/update")
    public ResponseEntity<ResponseMessage> updateUserActivity(@RequestBody UpdateRequest updateRequest) {
        try {
            jobService.runUpdateActivityJob(updateRequest.getUserActivity(), updateRequest.getCount());
            logger.info("Running User Activity Job");
            return new ResponseEntity<>(new ResponseMessage("Run Update User Activity job successfully"
                    , LocalDateTime.now().toString()), HttpStatus.OK);
        } catch (Exception e) {
            throw new StartJobFailedException("Failed to start update User Activity job");
        }
    }

    @GetMapping("/stop")
    public ResponseEntity<ResponseMessage> stop() {
        try {
            jobService.stop();
            logger.info("Schedule stopped");
            return new ResponseEntity<>(new ResponseMessage("Schedule stopped"
                    , LocalDateTime.now().toString()), HttpStatus.OK);
        } catch (Exception e) {
            throw new StartJobFailedException("Failed to stop schedule");
        }
    }

}
