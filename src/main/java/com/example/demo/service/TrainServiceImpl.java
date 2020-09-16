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
import java.util.Date;
import java.util.List;

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

        List<RecordInfo> recordInfos =  recordInfoMapper.selectTrainData(start,end);
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
            //6、更新归一化矩阵
            bpNeuralNetworkHandle.updateInputNormalization(inputData);
            bpNeuralNetworkHandle.updateOutputNormalization(record.getPredictValue());
        }


        return 0;

    }
}
