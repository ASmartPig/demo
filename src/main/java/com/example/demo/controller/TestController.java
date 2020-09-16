package com.example.demo.controller;

import com.example.demo.service.impl.InputService;
import com.example.demo.service.impl.TrainService;
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

    @Autowired
    private TrainService trainService;

    //迭代次数
    private static int iterNumber = 100;

    //误差
    private static  double error = 0.0001;






    @PostMapping("/testPredict")
    public void testPredict(){
        log.info("testPredict start ...");
        inputService.predictedAndSave("2020-09-14 11:56:30","2020-09-14 23:59:59");
    }

    @PostMapping("/testTrain")
    public void testTrain(){
        log.info("testTrain start ...");
        double trainError = trainService.train(iterNumber, error);
        log.info("testTrain end ...trainError:{}",trainError);
    }

}
