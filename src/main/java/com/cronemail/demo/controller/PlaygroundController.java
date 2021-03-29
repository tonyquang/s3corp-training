package com.cronemail.demo.controller;

import com.cronemail.demo.service.PlaygroundServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/timer")
public class PlaygroundController {

    private PlaygroundServices playgroundServices;

    @Autowired
    public PlaygroundController(PlaygroundServices playgroundServices){
        this.playgroundServices = playgroundServices;
    }

    @PostMapping("/start-job")
    public void startJob(){
        playgroundServices.runCronJobHandler();
    }

}
