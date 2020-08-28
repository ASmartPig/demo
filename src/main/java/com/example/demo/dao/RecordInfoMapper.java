package com.example.demo.dao;


import com.example.demo.dto.RecordInfo;
import org.apache.ibatis.annotations.Param;

public interface RecordInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RecordInfo record);

    int insertSelective(RecordInfo record);

    RecordInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RecordInfo record);

    int updateByPrimaryKey(RecordInfo record);

    RecordInfo selectByTime(@Param("startTime") String start, @Param("endTime") String end);
}