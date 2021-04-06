package com.quan12yt.trackingcronjob.controller;

import com.quan12yt.trackingcronjob.exception.VariablesUnacceptedException;
import com.quan12yt.trackingcronjob.model.Users;
import com.quan12yt.trackingcronjob.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UsersController {

    @Autowired
    UserService userService;

    @GetMapping("api/users")
    public ResponseEntity<?> getUsers(){
        List<Users> lsUsers = userService.getUsers();
        if(lsUsers.isEmpty()){
           return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(lsUsers, HttpStatus.OK);
    }

    @PostMapping("api/user")
    public ResponseEntity<?> getUsersById(@RequestBody List<String> userIds){
        if(userIds == null || userIds.isEmpty()){
            throw new VariablesUnacceptedException("List user id cant be null or empty !!");
        }
        List<Users> lsUsers = userService.getUsersById(userIds);
        if(lsUsers.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(lsUsers, HttpStatus.OK);
    }

}
