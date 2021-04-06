package com.quan12yt.trackingcronjob.controller;

import com.quan12yt.trackingcronjob.dto.response.ResponseMessage;
import com.quan12yt.trackingcronjob.service.JobService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('cronrole')")
    public ResponseEntity<ResponseMessage> sendEmail() {
            if(jobService.runSendEmailJoh()) {
                logger.info("Send email job is running");
                return new ResponseEntity<>(new ResponseMessage("Run Send Email job successfully"
                        , LocalDateTime.now().toString()), HttpStatus.OK);
            }
            return new ResponseEntity<>(new ResponseMessage("Some thing gone wrong !!"
                , LocalDateTime.now().toString()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
