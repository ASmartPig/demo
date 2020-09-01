package com.example.demo.task;


import com.example.demo.util.DateUtil;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@EnableScheduling
public class DynamicScheduledTask implements SchedulingConfigurer {

    //时间表达式  每2秒执行一次
    private String cron = "0/2 * * * * ?";

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.addTriggerTask(new Runnable() {
            @Override
            public void run() {
                //System.out.println(DateUtil.getStringTime(new Date(),DateUtil.DEFAULT_DATETIME_PATTERN));
            }
        }, new Trigger() {
            @Override
            public Date nextExecutionTime(TriggerContext triggerContext) {
                CronTrigger cronTrigger = new CronTrigger(cron);
                Date nextExecDate = cronTrigger.nextExecutionTime(triggerContext);
                return nextExecDate;
            }
        });
    }

    public void setCron(String cron) {
        this.cron = cron;
    }
}

