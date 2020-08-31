package com.example.demo.task;

import com.alibaba.fastjson.JSON;
import com.example.demo.controller.BpNeuralNetworkHandle;
import com.example.demo.dao.RecordInfoMapper;
import com.example.demo.dto.InputData;
import com.example.demo.dto.RecordInfo;
import com.example.demo.dto.UserInfo;
import com.example.demo.dao.UserInfoMapper;
import com.example.demo.mapper.ServerTableOneMapper;
import com.example.demo.service.impl.InputService;
import com.example.demo.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static com.sun.tools.doclint.Entity.and;

@Slf4j
@Component
@EnableScheduling
public class MonitorTask {

    @Autowired
    private InputService inputService;

    @Scheduled(cron = "*/30 * * * * ?")
    public void predicted(){
        log.info("predicted start ..");
        inputService.predictedAndSave();
        log.info("predicted end ..");

    }
}
