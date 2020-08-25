package com.example.demo.task;

import com.example.demo.controller.BpNeuralNetworkHandle;
import com.example.demo.dto.UserInfo;
import com.example.demo.dao.UserInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class MonitorTask {

    @Autowired
    private BpNeuralNetworkHandle bpNeuralNetworkHandle;

    @Autowired
    private UserInfoMapper userInfoMapper;

    //@Scheduled(cron = "0 */2 * * * ?")
    @Scheduled(cron = "*/5 * * * * ?")
    public void predicted(){
        bpNeuralNetworkHandle.printf();
        UserInfo num = userInfoMapper.selectById("f68d8c99-9ec1-4c2f-af82-3796e85e37ae");
        System.out.println(num);



    }
}
