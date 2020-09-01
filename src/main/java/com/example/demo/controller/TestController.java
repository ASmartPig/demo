package com.example.demo.controller;

import com.example.demo.task.DynamicScheduledTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/testController")
@Slf4j
public class TestController {

    @Autowired
    private DynamicScheduledTask dynamicScheduledTask;

    @PostMapping("/testMethod")
    public void setCronMethod(){
        log.info("setCronMethod start ...");
        dynamicScheduledTask.setCron("* */2 * * * ?");

    }

}
