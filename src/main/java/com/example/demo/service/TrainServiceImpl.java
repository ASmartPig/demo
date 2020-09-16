package com.example.demo.service;

import com.example.demo.controller.BpNeuralNetworkHandle;
import com.example.demo.dao.RecordInfoMapper;
import com.example.demo.dto.RecordInfo;
import com.example.demo.mapper.ServerTableOneMapper;
import com.example.demo.service.impl.TrainService;
import com.example.demo.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @description:
 * @author: liaoze
 * @date: 2020/9/14 4:41 下午
 * @version:
 */
@Slf4j
@Service
public class TrainServiceImpl implements TrainService {


    @Autowired
    private ServerTableOneMapper serverTableOneMapper;

    @Autowired
    private RecordInfoMapper recordInfoMapper;

    @Autowired
    private BpNeuralNetworkHandle bpNeuralNetworkHandle;

    @Autowired
    @Qualifier("inputWeight")
    private double[][] inputWeight;

    @Resource(name = "outputWeight")
    private double[] outputWeight;



    //训练
    @Override
    public double train() {
        log.info("TrainServiceImpl start ...");
        String start = DateUtil.startDay(new Date(),-1);
        String end = DateUtil.startDay(new Date());
        log.info("TrainServiceImpl start:{},end:{}",start,end);

        //1、读取数据库，并且筛选掉脏数据
        List<RecordInfo> recordInfos =  recordInfoMapper.selectTrainData(start,end);



        //2、更新归一化矩阵
        double[] newXMaxArray = this.getNewInputMaxNormalization(recordInfos);
        double[] newXMinArray = this.getNewInputMinNormalization(recordInfos);

        int count = 100;


        while (1000)


        for (RecordInfo record : recordInfos){

            double[] inputData = new double[7];
            inputData[0] = record.getATime();
            inputData[1] = record.getBTime();
            inputData[2] = record.getInNox();
            inputData[3] = record.getInSo2();
            inputData[4] = record.getInFlux();
            inputData[5] = record.getInO2();
            inputData[6] = record.getInTemp();

            double[] inputValuesNm = bpNeuralNetworkHandle.normalization(inputData);
            // 1、获取输出层的误差；
            double outPutError = bpNeuralNetworkHandle.getOutError(record.getPredictValue(), record.getTrueValue());
            //2、获取隐藏层的输入值
            double[] hiddenInput = bpNeuralNetworkHandle.getHiddenInput(inputValuesNm);
            //3、获取隐含层的误差；
            double[] hide_error = bpNeuralNetworkHandle.getHideError(outPutError, outputWeight, hiddenInput);
            //4、更新输入层->隐含层权值
            bpNeuralNetworkHandle.updateWeight(inputWeight,inputValuesNm,hide_error);
            //5、更新隐含层->输出层权值
            bpNeuralNetworkHandle.updateWeight(outputWeight,hiddenInput,outPutError);

        }


        return 0;

    }

    //归一化矩阵输出值最大值
    public double[] getNewInputMaxNormalization(List<RecordInfo> recordInfos){
        RecordInfo aTimeMax = recordInfos.stream().filter(item -> item.getATime() != null).max(Comparator.comparingLong(RecordInfo::getATime)).get();
        RecordInfo bTimeMax = recordInfos.stream().filter(item -> item.getBTime() != null).max(Comparator.comparingLong(RecordInfo::getBTime)).get();
        RecordInfo inNoxMax = recordInfos.stream().filter(item -> item.getInNox() != null).max(Comparator.comparingDouble(RecordInfo::getInNox)).get();
        RecordInfo inSo2Max = recordInfos.stream().filter(item -> item.getInSo2() != null).max(Comparator.comparingDouble(RecordInfo::getInSo2)).get();
        RecordInfo inFluxMax = recordInfos.stream().filter(item -> item.getInFlux() != null).max(Comparator.comparingDouble(RecordInfo::getInFlux)).get();
        RecordInfo inO2Max = recordInfos.stream().filter(item -> item.getInO2() != null).max(Comparator.comparingDouble(RecordInfo::getInO2)).get();
        RecordInfo inTempMax = recordInfos.stream().filter(item -> item.getInTemp() != null).max(Comparator.comparingDouble(RecordInfo::getInTemp)).get();

        double[] newXMaxArray = new double[7];
        newXMaxArray[0] = aTimeMax.getATime();
        newXMaxArray[1] = bTimeMax.getBTime();
        newXMaxArray[2] = inNoxMax.getInNox();
        newXMaxArray[3] = inSo2Max.getInSo2();
        newXMaxArray[4] = inFluxMax.getInFlux();
        newXMaxArray[5] = inO2Max.getInO2();
        newXMaxArray[6] = inTempMax.getInTemp();

        return newXMaxArray;
    }


    //归一化矩阵输入值最小值
    public double[] getNewInputMinNormalization(List<RecordInfo> recordInfos){
        RecordInfo aTimeMin = recordInfos.stream().filter(item -> item.getATime() != null).min(Comparator.comparingLong(RecordInfo::getATime)).get();
        RecordInfo bTimeMin = recordInfos.stream().filter(item -> item.getBTime() != null).min(Comparator.comparingLong(RecordInfo::getBTime)).get();
        RecordInfo inNoxMin = recordInfos.stream().filter(item -> item.getInNox() != null).min(Comparator.comparingDouble(RecordInfo::getInNox)).get();
        RecordInfo inSo2Min = recordInfos.stream().filter(item -> item.getInSo2() != null).min(Comparator.comparingDouble(RecordInfo::getInSo2)).get();
        RecordInfo inFluxMin = recordInfos.stream().filter(item -> item.getInFlux() != null).min(Comparator.comparingDouble(RecordInfo::getInFlux)).get();
        RecordInfo inO2Min = recordInfos.stream().filter(item -> item.getInO2() != null).min(Comparator.comparingDouble(RecordInfo::getInO2)).get();
        RecordInfo inTempMin = recordInfos.stream().filter(item -> item.getInTemp() != null).min(Comparator.comparingDouble(RecordInfo::getInTemp)).get();

        double[] newXMinArray = new double[7];
        newXMinArray[0] = aTimeMin.getATime();
        newXMinArray[1] = bTimeMin.getBTime();
        newXMinArray[2] = inNoxMin.getInNox();
        newXMinArray[3] = inSo2Min.getInSo2();
        newXMinArray[4] = inFluxMin.getInFlux();
        newXMinArray[5] = inO2Min.getInO2();
        newXMinArray[6] = inTempMin.getInTemp();

        return newXMinArray;
    }
}
