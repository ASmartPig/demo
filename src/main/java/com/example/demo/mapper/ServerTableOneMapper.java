package com.example.demo.mapper;

import com.example.demo.dto.InputData;
import com.example.demo.dto.ServerTableOne;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface ServerTableOneMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ServerTableOne record);

    int insertSelective(ServerTableOne record);

    ServerTableOne selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ServerTableOne record);

    int updateByPrimaryKey(ServerTableOne record);

    int selectnum();

    InputData selectInput();

    List<InputData> selectInputList(@Param("startTime") String startTime, @Param("endTime")String endTime);

    InputData selectInputById(long id);

}