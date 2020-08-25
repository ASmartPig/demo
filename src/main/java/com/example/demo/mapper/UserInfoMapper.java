package com.example.demo.mapper;


import com.example.demo.dto.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserInfoMapper {
    int insert(UserInfo record);

    int insertSelective(UserInfo record);

    UserInfo selectById(String id);

    @Select("select * from user_info where id = #{id}")
    UserInfo selectById2(String id);
}