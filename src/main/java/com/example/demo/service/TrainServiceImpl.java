package com.example.demo.service;

import com.example.demo.controller.BpNeuralNetworkHandle;
import com.example.demo.dao.RecordInfoMapper;
import com.example.demo.dto.InputData;
import com.example.demo.dto.RecordInfo;
import com.example.demo.mapper.ServerTableOneMapper;
import com.example.demo.service.impl.TrainService;
import com.example.demo.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

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




    @Override
    public void train() {
        log.info("TrainServiceImpl start ...");
        String start = DateUtil.startDay(new Date(),-1);
        String end = DateUtil.startDay(new Date());
        log.info("TrainServiceImpl start:{},end:{}",start,end);


        List<RecordInfo> recordInfos =  recordInfoMapper.selectTrainData(start,end);
        for (RecordInfo record : recordInfos){
            //训练
                // 1、获取输出层的误差；
                double predict;
                double trueValue;
                double[] inputValues;
                double outPutError = bpNeuralNetworkHandle.getOutError(predict, trueValue);
                //2、获取隐藏值的输入值
                double[] hiddenInput = bpNeuralNetworkHandle.getHiddenInput(inputValues);
                //3、获取隐含层的误差；
                double[] hide_error = bpNeuralNetworkHandle.getHideError(outPutError, outputWeight, hiddenInput);
                //4、更新输入层->隐含层权值
                bpNeuralNetworkHandle.updateWeight(inputWeight,inputValues,hide_error);
                //5、更新隐含层->输出层权值
                bpNeuralNetworkHandle.updateWeight(inputWeight,inputValues,hide_error);
        }

    }
}
