package com.example.demo.controller;

import com.example.demo.service.impl.InputService;
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
    private InputService inputService;


    @PostMapping("/testMethod")
    public void setCronMethod(){
        log.info("setCronMethod start ...");
        inputService.predictedAndSave("2020-09-14 00:00:00","2020-09-14 23:59:59");

    }

}
