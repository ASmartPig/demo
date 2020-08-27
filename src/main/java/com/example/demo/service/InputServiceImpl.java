package com.example.demo.service;

import com.alibaba.fastjson.JSON;
import com.example.demo.controller.BpNeuralNetworkHandle;
import com.example.demo.dao.RecordInfoMapper;
import com.example.demo.dao.UserInfoMapper;
import com.example.demo.dto.InputData;
import com.example.demo.dto.RecordInfo;
import com.example.demo.mapper.ServerTableOneMapper;
import com.example.demo.service.impl.InputService;
import com.example.demo.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;

@Service
public class InputServiceImpl implements InputService {

    @Autowired
    private BpNeuralNetworkHandle bpNeuralNetworkHandle;

    @Autowired
    private ServerTableOneMapper serverTableOneMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private RecordInfoMapper recordInfoMapper;

    @Override
    public void predictedAndSave() {
        InputData inp = serverTableOneMapper.selectInputById(1268109756596225l);
        LocalDateTime localDateTime = DateUtil.toLocalDateTime(inp.getCreateTime());
        int min = localDateTime.getMinute();
        Duration durationA = null;
        Duration durationB = null;
        //A炉换向时间
        if (min >= 15 && min < 45) {
            LocalDateTime ATime = LocalDateTime.of(localDateTime.getYear(), localDateTime.getMonth(),
                    localDateTime.getDayOfMonth(), localDateTime.getHour(), 15, 0);
            durationA = Duration.between(ATime, localDateTime);
        }
        if (min < 15) {
            LocalDateTime ATime = LocalDateTime.of(localDateTime.getYear(), localDateTime.getMonth(),
                    localDateTime.getDayOfMonth(), localDateTime.getHour(), 45, 0).plusHours(-1);
            durationA = Duration.between(ATime, localDateTime);
        }
        if (min >= 45) {
            LocalDateTime ATime = LocalDateTime.of(localDateTime.getYear(), localDateTime.getMonth(),
                    localDateTime.getDayOfMonth(), localDateTime.getHour(), 45, 0);
            durationA = Duration.between(ATime, localDateTime);
        }

        //B炉换向时间
        if (min >= 30) {
            LocalDateTime ATime = LocalDateTime.of(localDateTime.getYear(), localDateTime.getMonth(),
                    localDateTime.getDayOfMonth(), localDateTime.getHour(), 30, 0);
            durationB = Duration.between(ATime, localDateTime);
        }
        if (min < 30) {
            LocalDateTime ATime = LocalDateTime.of(localDateTime.getYear(), localDateTime.getMonth(),
                    localDateTime.getDayOfMonth(), localDateTime.getHour(), 0, 0);
            durationB = Duration.between(ATime, localDateTime);
        }

        double[] inputData = new double[7];
        inputData[0] = durationA.getSeconds();
        inputData[1] = durationB.getSeconds();
        inputData[2] = inp.getInNox();
        inputData[3] = inp.getInSo2();
        inputData[4] = inp.getInFlux();
        inputData[5] = inp.getInO2();
        inputData[6] = inp.getInTemp();
        double[] inputValuesNm = bpNeuralNetworkHandle.normalization(inputData);

        double predict = bpNeuralNetworkHandle.getPredictedValue(inputValuesNm);
        double trueValue = bpNeuralNetworkHandle.getResult(predict);
        System.out.println(JSON.toJSONString(inp) + "==" + predict + "==" + DateUtil.getStringTime(inp.getCreateTime(), DateUtil.DEFAULT_DATETIME_PATTERN));

        RecordInfo recordInfo = new RecordInfo();
        recordInfo.setRid(inp.getId());
        recordInfo.setPredictValue(predict);
        recordInfo.setTrueValue(inp.getInNox());
        recordInfo.setCreateTime(new Date());
        recordInfoMapper.insert(recordInfo);
    }






}
