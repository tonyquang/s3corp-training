package com.quan12yt.trackingcronjob.controller;

import com.quan12yt.trackingcronjob.dto.ResponseMessage;
import com.quan12yt.trackingcronjob.dto.UpdateRequest;
import com.quan12yt.trackingcronjob.exception.StartJobFailedException;
import com.quan12yt.trackingcronjob.job.SendEmailJob;
import com.quan12yt.trackingcronjob.job.UpdateActivityJob;
import com.quan12yt.trackingcronjob.service.JobService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/schedule")
@Api(value="schedule", description="Run, stop Quartz Schedule jobs include sending email, update user's activity")
public class ScheduleController {
    private final Logger logger = LoggerFactory.getLogger(ScheduleController.class);

    @Autowired
    private JobService jobService;

    @ApiOperation(value = "Run send aware email job - send email at 11am and 16pm every day to users that have cross accesses limitation")
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

    @ApiOperation(value = "Run update user activity job - check and update accesses count and total time at 11am and 16pm every day")
    @PostMapping("/update")
    public ResponseEntity<ResponseMessage> updateUserActivity(@RequestBody UpdateRequest updateRequest) {
        try {
            jobService.runUpdateActivityJob(updateRequest.getUserActivity(), updateRequest.getCount(), updateRequest.getTime());
            logger.info("Running User Activity Job");
            return new ResponseEntity<>(new ResponseMessage("Run Update User Activity job successfully"
                    , LocalDateTime.now().toString()), HttpStatus.OK);
        } catch (Exception e) {
            throw new StartJobFailedException("Failed to start update User Activity job");
        }
    }

    @ApiOperation(value = "Stop send job")
    @GetMapping("/send/stop")
    public ResponseEntity<ResponseMessage> stopSendEmailJob() {
        try {
            jobService.stopJob(SendEmailJob.class.getSimpleName());
            logger.info("Send email job stopped");
            return new ResponseEntity<>(new ResponseMessage("Send email job stopped"
                    , LocalDateTime.now().toString()), HttpStatus.OK);
        } catch (Exception e) {
            throw new StartJobFailedException("Failed to stop Send email job");
        }
    }
    @ApiOperation(value = "Stop all jobs")
    @GetMapping("/stop")
    public ResponseEntity<ResponseMessage> stopSchedule() {
        try {
            jobService.stop();
            logger.info("Schedule stopped");
            return new ResponseEntity<>(new ResponseMessage("Schedule stopped"
                    , LocalDateTime.now().toString()), HttpStatus.OK);
        } catch (Exception e) {
            throw new StartJobFailedException("Failed to stop schedule");
        }
    }

    @ApiOperation(value = "Stop update jobs")
    @GetMapping("/update/stop")
    public ResponseEntity<ResponseMessage> stopUpdateJob() {
        try {
            jobService.stopJob(UpdateActivityJob.class.getSimpleName());
            logger.info("Update job stopped");
            return new ResponseEntity<>(new ResponseMessage("Update job stopped"
                    , LocalDateTime.now().toString()), HttpStatus.OK);
        } catch (Exception e) {
            throw new StartJobFailedException("Failed to stop Update job");
        }
    }

}
