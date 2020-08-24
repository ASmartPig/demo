package com.example.demo.mapper;

import com.example.demo.dto.ServerTableOne;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface ServerTableOneMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ServerTableOne record);

    int insertSelective(ServerTableOne record);

    ServerTableOne selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ServerTableOne record);

    int updateByPrimaryKey(ServerTableOne record);

    int selectnum();
}