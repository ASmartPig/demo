package com.example.demo.service;

import com.alibaba.fastjson.JSON;
import com.example.demo.controller.BpNeuralNetworkHandle;
import com.example.demo.controller.OpcHandler;
import com.example.demo.dao.RecordInfoMapper;
import com.example.demo.dao.UserInfoMapper;
import com.example.demo.dto.InputData;
import com.example.demo.dto.RecordInfo;
import com.example.demo.enums.CEMS;
import com.example.demo.mapper.ServerTableOneMapper;
import com.example.demo.service.impl.InputService;
import com.example.demo.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class InputServiceImpl implements InputService {

    @Autowired
    private BpNeuralNetworkHandle bpNeuralNetworkHandle;

    @Autowired
    private ServerTableOneMapper serverTableOneMapper;

    @Autowired
    private RecordInfoMapper recordInfoMapper;

    @Autowired
    private OpcHandler opcHandler;

    //真实所需氨水因子
    private static double ratio = 1.5;

    //真实所需氨水常数
    private static double constant = 10;

    //排放标准值
    private static double standardValue = 100;

    //比例
    private static double k = 138.7516;

    @Override
    public void predictedAndSave() {
        InputData inp = serverTableOneMapper.selectInput();
        long rid = inp.getId();
        log.info("predictedAndSave rid:{}",rid);
        RecordInfo recordInfo = recordInfoMapper.selectByRid(inp.getId());
        if (Objects.isNull(recordInfo)){
            log.info("predictedAndSave handle:{}",rid);
            handle(inp);
        }

    }

    @Override
    public void predictedAndSave(String start, String end) {
        List<InputData> inputDataList =  serverTableOneMapper.selectInputList(start,end);
        for (InputData inputData : inputDataList){
            handle(inputData);
        }
    }


    @Override
    public void predictedAndSave(Map<String,Double> map) {
        InputData inp = new InputData();
        inp.setInNox(map.get(CEMS.CEMS_in_NOX.getStr()));
        inp.setInSo2(map.get(CEMS.CEMS_in_SO2.getStr()));
        inp.setInO2(map.get(CEMS.CEMS_in_O2.getStr()));
        inp.setInFlux(map.get(CEMS.CEMS_in_flux.getStr()));
        inp.setInTemp(map.get(CEMS.CEMS_in_temp.getStr()));
        inp.setCreateTime(new Date());
        handle(inp);


    }

    private void handle(InputData inp){
        log.info("handle start ... inp:{}",JSON.toJSONString(inp));
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
        double predictValue = bpNeuralNetworkHandle.reverseNormalization(predict);
        //氨水质量
        double NH3 = (predictValue - standardValue) * inp.getInFlux() / k;

        //实际氨水质量
        double actualNH3 = NH3 * ratio + constant;
        log.info("ratio :{},constant:{},standardValue:{}",ratio,constant,standardValue);
        log.info("inp id:{},predict:{},predictValue:{},NH3:{},actualNH3:{}",inp.getId(),predict,predictValue,NH3,actualNH3);

        RecordInfo recordInfo = new RecordInfo();
        //server table 1的值
        recordInfo.setRid(inp.getId());
        //预测值
        recordInfo.setPredictValue(predictValue);
        opcHandler.wirte(predictValue);
        //初始化真实值-1
        recordInfo.setTrueValue(-1d);
        //server table 1 数据 生成时间
        recordInfo.setCreateTime(inp.getCreateTime());
        if (inp.getInO2()>15){
            //吹气
            recordInfo.setBelong(2);
        }else if (inp.getInNox() >1200){
            //标气
            recordInfo.setBelong(3);
        }else{
            //正常
            recordInfo.setBelong(1);
        }
        //数据插入时间
        recordInfo.setInsertTime(new Date());
        //插入预测值
        recordInfoMapper.insert(recordInfo);

        //获取5分钟后(加减10s)的真实值
        LocalDateTime dateTime =  DateUtil.toLocalDateTime(inp.getCreateTime());
        String start = DateUtil.getStringTime(dateTime.plusSeconds(-310),DateUtil.DEFAULT_DATETIME_PATTERN);
        String end = DateUtil.getStringTime(dateTime.plusSeconds(-290),DateUtil.DEFAULT_DATETIME_PATTERN);;
        RecordInfo updateRecord = recordInfoMapper.selectByTime(start,end);
        if (Objects.nonNull(updateRecord)){
            updateRecord.setTrueValue(inp.getInNox());
            recordInfoMapper.updateByPrimaryKeySelective(updateRecord);
        }




    }






}
