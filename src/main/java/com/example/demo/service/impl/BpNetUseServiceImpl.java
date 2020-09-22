package com.example.demo.service.impl;

import com.alibaba.fastjson.JSON;
import com.example.demo.controller.BpNeuralNetworkHandle;
import com.example.demo.dao.RecordInfoMapper;
import com.example.demo.dto.InputData;
import com.example.demo.dto.RecordInfo;
import com.example.demo.mapper.ServerTableOneMapper;
import com.example.demo.service.BpNetUseService;
import com.example.demo.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class BpNetUseServiceImpl implements BpNetUseService {

    @Autowired
    private BpNeuralNetworkHandle bpNeuralNetworkHandle;

    @Autowired
    private ServerTableOneMapper serverTableOneMapper;

    @Autowired
    private RecordInfoMapper recordInfoMapper;

    @Resource
    private double[][] inputWeight;

    @Resource(name = "outputWeight")
    private double[] outputWeight;

    @Resource(name = "bOneArray")
    private double[] bOneArray;

    @Resource(name = "b2Value")
    private double b2Value;

    @Resource(name = "xMinArray")
    private double[] xMinArray;

    @Resource(name = "xMaxArray")
    private double[] xMaxArray;

    @Resource(name = "xMinValue")
    private double xMinValue;

    @Resource(name = "xMaxValue")
    private double xMaxValue;


    //真实所需氨水因子
    private static double ratio = 1.5;

    //真实所需氨水常数
    private static double constant = 10;

    //排放标准值
    private static double standardValue = 100;

    //比例
    private static double k = 138.7516;

    //值p-直排和总量的比例
    private static double p = 0.11;


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

    private void handle(InputData inp){
        log.info("handle start ... inp:{}", JSON.toJSONString(inp));
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

        //15分钟流量平均值
        //double flux = serverTableOneMapper.selectAvgFlux(DateUtil.getStringTime(LocalDateTime.now().plusSeconds(-900)),DateUtil.getStringTime(LocalDateTime.now()));

        double[] inputData = new double[7];
        inputData[0] = durationA.getSeconds();
        inputData[1] = durationB.getSeconds();
        inputData[2] = inp.getInNox();
        inputData[3] = inp.getInSo2();
        inputData[4] = inp.getInFlux();
        inputData[5] = inp.getInO2();
        inputData[6] = inp.getInTemp();

        //输入数据
        RecordInfo recordInfo = new RecordInfo();
        recordInfo.setInNox(inp.getInNox());
        recordInfo.setInSo2(inp.getInSo2());
        recordInfo.setInFlux(inp.getInFlux());
        recordInfo.setInO2(inp.getInO2());
        recordInfo.setInTemp(inp.getInTemp());
        recordInfo.setATime(durationA.getSeconds());
        recordInfo.setBTime(durationB.getSeconds());

        double[] inputValuesNm = bpNeuralNetworkHandle.normalization(inputData,xMaxArray,xMinArray);

        double predict = bpNeuralNetworkHandle.getPredictedValue(inputValuesNm,inputWeight,outputWeight,bOneArray,b2Value);
        double predictValue = bpNeuralNetworkHandle.reverseNormalization(predict,xMaxValue,xMinValue);
        //预测氮氧化物浓度范围设置限制，150~950
        if (predictValue > 950){
            predictValue = 950.0d;
        }
        if (predictValue<150){
            predictValue = 150.0d;
        }

        //真实需要排放的NOX标准
        double x = (1 + p) * standardValue - p * inp.getInNox();

        //氨水质量
        double NH3 = (predictValue - x) * inp.getInFlux() / k;

        //实际氨水质量
        double actualNH3 = NH3 * ratio + constant;
        log.info("ratio :{},constant:{},standardValue:{}",ratio,constant,standardValue);
        log.info("inp id:{},predict:{},predictValue:{},NH3:{},actualNH3:{}",inp.getId(),predict,predictValue,NH3,actualNH3);
        //向opc server 写数据
        //opcHandler.wirte(actualNH3);

        //server table 1 id值
        recordInfo.setRid(inp.getId());
        //预测值
        recordInfo.setPredictValue(predictValue);
        //初始化真实值-1
        recordInfo.setTrueValue(-1d);
        //server table 1 数据 生成时间
        recordInfo.setCreateTime(inp.getCreateTime());
        if (inp.getInO2()>10){
            //吹气
            recordInfo.setBelong(2);
        }else if (inp.getInNox() >1000){
            //标气
            recordInfo.setBelong(3);
        }else{
            //正常
            recordInfo.setBelong(1);
        }
        //数据插入时间
        recordInfo.setInsertTime(new Date());

        recordInfo.setIsDirtyNeighbor(0);
        //插入预测值
        recordInfoMapper.insert(recordInfo);

        //获取5分钟后(加减10s)的真实值
        LocalDateTime dateTime =  DateUtil.toLocalDateTime(inp.getCreateTime());
        String start = DateUtil.getStringTime(dateTime.plusSeconds(-310),DateUtil.DEFAULT_DATETIME_PATTERN);
        String end = DateUtil.getStringTime(dateTime.plusSeconds(-290),DateUtil.DEFAULT_DATETIME_PATTERN);;
        RecordInfo updateRecord = recordInfoMapper.selectByTime(start,end);
        if (Objects.nonNull(updateRecord)){
            if (updateRecord.getBelong() == 2 || updateRecord.getBelong() == 3){
                updateRecord.setBelong(updateRecord.getBelong());
            }else if (inp.getInO2()>10){
                //吹气
                updateRecord.setBelong(2);
            }else if (inp.getInNox() >1200){
                //标气
                updateRecord.setBelong(3);
            }
            updateRecord.setTrueValue(inp.getInNox());
            Double absError = Math.abs(inp.getInNox() - updateRecord.getPredictValue())/ updateRecord.getPredictValue();
            updateRecord.setAbsError(absError);
            recordInfoMapper.updateByPrimaryKeySelective(updateRecord);
        }

    }

    @Override
    public void removeDirty() {
        int number = 20;
        //1、读取数据库，并且筛选掉脏数据
        List<RecordInfo> filterRecordInfos=  recordInfoMapper.selectTrainData("start","end");
        for (RecordInfo recordInfo : filterRecordInfos){
            if (recordInfo.getBelong() != 1){
                int id = recordInfo.getId();
                recordInfoMapper.updateDirty(id-number,id+number);
            }
        }

    }

}
