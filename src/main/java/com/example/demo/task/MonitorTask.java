package com.example.demo.task;

import com.example.demo.controller.OpcHandler;
import com.example.demo.dao.CronMapper;
import com.example.demo.service.PredictService;
import com.example.demo.service.TrainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;


@Slf4j
@Component
@EnableScheduling
public class MonitorTask implements SchedulingConfigurer {

    @Autowired
    private CronMapper cronMapper;

    @Autowired
    private PredictService inputService;

    @Autowired
    private OpcHandler opcHandler;

    @Autowired
    private TrainService trainService;


    /**
     * 执行定时任务.
     */
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                log.info("train start ..");
               // trainService.train();
                log.info("train end ..");


            }
        };
        taskRegistrar.addTriggerTask(
                //1.添加任务内容(Runnable)
                runnable,
                //2.设置执行周期(Trigger)
                triggerContext -> {
                    //2.1 从数据库获取执行周期
                    String cron = cronMapper.getTrainCron();
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
