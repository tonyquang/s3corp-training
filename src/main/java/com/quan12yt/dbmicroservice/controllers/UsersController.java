package com.quan12yt.dbmicroservice.controllers;


import com.quan12yt.dbmicroservice.exceptions.VariablesUnacceptedException;
import com.quan12yt.dbmicroservice.models.Users;
import com.quan12yt.dbmicroservice.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UsersController {

    @Autowired
    UserService userService;

    @GetMapping("/users")
    @PreAuthorize("hasRole('CRON')")
    public ResponseEntity<?> getUsers(){
        List<Users> lsUsers = userService.getUsers();
        if(lsUsers.isEmpty()){
           return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<>(lsUsers, HttpStatus.OK);
    }

    @PostMapping("/users/getByListId")
    @PreAuthorize("hasRole('CRON')")
    public ResponseEntity<?> getUsersByListId(@RequestBody List<String> userIds){
        if(userIds == null || userIds.isEmpty()){
            throw new VariablesUnacceptedException("List user id cant be null or empty !!");
        }
        List<Users> lsUsers = userService.getUsersByListId(userIds);
        if(lsUsers.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<>(lsUsers, HttpStatus.OK);
    }

    @GetMapping("/users/getById")
    @PreAuthorize("hasRole('CRON')")
    public ResponseEntity<?> getUsersById(@RequestParam String userId){
        if(userId == null || userId.isEmpty()){
            throw new VariablesUnacceptedException("User id cant be null or empty !!");
        }
        Optional<Users> user = userService.getUserByUserId(userId);
        if(user.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
