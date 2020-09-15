package com.example.demo.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CronMapper {

    @Select("select cron from cron where cron_id = '1'")
    String getCron();


    @Select("select cron from cron where cron_id = '2'")
    String getTrainCron();
}