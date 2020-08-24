package com.example.demo.task;

import com.example.demo.controller.BpNeuralNetworkHandle;
import com.example.demo.dto.ServerTableOne;
import com.example.demo.mapper.ServerTableOneMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@EnableScheduling
public class MonitorTask {

    @Autowired
    private BpNeuralNetworkHandle bpNeuralNetworkHandle;

    @Autowired
    private ServerTableOneMapper serverTableOneMapper;

    //@Scheduled(cron = "0 */2 * * * ?")
    @Scheduled(cron = "*/5 * * * * ?")
    public void predicted(){
        bpNeuralNetworkHandle.printf();
        int num = serverTableOneMapper.selectnum();
        System.out.println(num);



    }
}
