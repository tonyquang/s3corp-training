package com.quan12yt.dbmicroservice.controllers;

import com.quan12yt.dbmicroservice.dtos.request.GetByMonthAndYearRequest;
import com.quan12yt.dbmicroservice.dtos.request.UserActivityFilterRequest;
import com.quan12yt.dbmicroservice.dtos.response.SaveActivitySucceedResponse;
import com.quan12yt.dbmicroservice.models.UserActivity;
import com.quan12yt.dbmicroservice.models.Users;
import com.quan12yt.dbmicroservice.services.UserActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserActivityController {

    @Autowired
    UserActivityService userActivityService;

    @GetMapping("/activities")
    @PreAuthorize("hasRole('CRON')")
    public ResponseEntity<?> getAllUserActivities(){
        List<UserActivity> lsUserActivities = userActivityService.getAllUserActivities();
        if(lsUserActivities.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<>(lsUserActivities, HttpStatus.OK);
    }

    @PostMapping("/activities/filter")
    @PreAuthorize("hasRole('CRON')")
    public ResponseEntity<?> getUserActivitiesBelongToFilter(@Valid @RequestBody UserActivityFilterRequest filterRequest){
        List<UserActivity> lsUserActivities = userActivityService
                .getUserActivitiesByFilter(
                        filterRequest.getHostName()
                        , filterRequest.getCount()
                        , filterRequest.getTimestamp());
        if(lsUserActivities.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<>(lsUserActivities, HttpStatus.OK);
    }
    @PostMapping("/activities/export")
    @PreAuthorize("hasRole('CRON')")
    public ResponseEntity<?> getUserActivitiesByMonthAndYear(@Valid @RequestBody GetByMonthAndYearRequest request){
        List<UserActivity> lsUserActivities = userActivityService
                .getViolatedUserByYearAndMonth(
                        request.getHostName()
                        , request.getUserName()
                        , request.getMonth()
                        , request.getYear());
        if(lsUserActivities.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<>(lsUserActivities, HttpStatus.OK);
    }

    @PostMapping("/activities/save")
    @PreAuthorize("hasRole('CRON')")
    public ResponseEntity<?> save(@Valid @RequestBody UserActivity userActivity){
        UserActivity activity = userActivityService.saveUser(userActivity);
        if(activity == null){
            return ResponseEntity.badRequest().build();
        }
        return new ResponseEntity<>(new SaveActivitySucceedResponse(
                "Save user activity succeed !!"
                        , new Date().toString()
                        , activity)
                , HttpStatus.OK);
    }

}
