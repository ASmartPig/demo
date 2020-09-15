package com.example.demo.task;

import com.example.demo.controller.OpcHandler;
import com.example.demo.dao.CronMapper;
import com.example.demo.service.impl.InputService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Slf4j
@Component
@EnableScheduling
public class CompleteScheduleConfig implements SchedulingConfigurer {

    @Autowired
    private CronMapper cronMapper;

    @Autowired
    private InputService inputService;

    @Autowired
    private OpcHandler opcHandler;


    /**
     * 执行定时任务.
     */
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                log.info("predicted start ..");
//        Map<String,Double> map = opcHandler.read();
//        inputService.predictedAndSave( map);


           // inputService.predictedAndSave();
            log.info("predicted end ..");


            }
        };
        taskRegistrar.addTriggerTask(
                //1.添加任务内容(Runnable)
                runnable,
                //2.设置执行周期(Trigger)
                triggerContext -> {
                    //2.1 从数据库获取执行周期
                    String cron = cronMapper.getCron();
                    //2.2 合法性校验.
                    if (StringUtils.isEmpty(cron)) {
                        // Omitted Code ..
                    }
                    //2.3 返回执行周期(Date)
                    return new CronTrigger(cron).nextExecutionTime(triggerContext);
                }
        );
    }

}