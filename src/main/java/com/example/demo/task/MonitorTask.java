package com.example.demo.task;

import com.example.demo.controller.OpcHandler;
import com.example.demo.service.impl.InputService;
import com.example.demo.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;


@Slf4j
@Component
@EnableScheduling
public class MonitorTask {

    @Autowired
    private InputService inputService;

    @Autowired
    private OpcHandler opcHandler;

    @Scheduled(cron = "*/30 * * * * ?")
    public void predicted(){
        log.info("predicted start ..");
//        Map<String,Double> map = opcHandler.read();
//        inputService.predictedAndSave( map);
        System.out.println(DateUtil.getStringTime(new Date()));
        log.info("predicted end ..");

    }
}
