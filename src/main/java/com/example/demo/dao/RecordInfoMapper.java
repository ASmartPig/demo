package com.example.demo.dao;


import com.example.demo.dto.RecordInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface RecordInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RecordInfo record);

    int insertSelective(RecordInfo record);

    RecordInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RecordInfo record);

    int updateByPrimaryKey(RecordInfo record);

    RecordInfo selectByTime(@Param("startTime") String start, @Param("endTime") String end);

    RecordInfo selectByRid(@Param("rid")long rid);

    List<RecordInfo> selectTrainData(@Param("startTime") String startTime, @Param("endTime")String endTime);

    void updateDirty(@Param("min")int min, @Param("max")int max);
}