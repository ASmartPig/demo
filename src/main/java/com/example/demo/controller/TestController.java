package com.example.demo.controller;

import com.example.demo.service.BpNetUseService;
import com.example.demo.service.PredictService;
import com.example.demo.service.TrainService;
import com.example.demo.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;


@RestController
@RequestMapping("/testController")
@Slf4j
public class TestController {

    @Autowired
    private BpNetUseService inputService;

    @Autowired
    private TrainService trainService;

    //迭代次数
    private static int iterNumber = 200;

    //误差
    private static  double error = 0.0001;






    @PostMapping("/testPredict")
    public void testPredict(){
        log.info("testPredict start ...");
        //inputService.predictedAndSave(1275498501861377l);
        inputService.predictedAndSave("2020-09-18 00:00:00","2020-09-18 23:59:59");
        log.info("testPredict start ...");
    }

    @PostMapping("/testTrain")
    public void testTrain(){
        log.info("testTrain start ...");
        double trainError = trainService.train(iterNumber, error);
        log.info("testTrain end ...trainError:{}",trainError);
    }

    @PostMapping("/removeDirty")
    public void testRemoveDirty(){
        log.info("removeDirty start ...");
        inputService.removeDirty();
        log.info("removeDirty end ...");
    }



}
