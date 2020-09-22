package com.example.demo.service.impl;

import com.example.demo.controller.BpNeuralNetworkHandle;
import com.example.demo.dao.RecordInfoMapper;
import com.example.demo.dto.RecordInfo;
import com.example.demo.mapper.ServerTableOneMapper;
import com.example.demo.service.TrainService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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

    //private  String path = "/Users/liaoze/Documents/train.txt";

    private  String path = "C:/Users/Lenovo/Desktop/train.txt";

    //训练
    @Override
    public double train(int iterNumber,double error) {
        log.info("TrainServiceImpl start ...");
        double trainError = 1;
        double absError = 0;

//        String start = DateUtil.startDay(new Date(),-1);
//        String end = DateUtil.startDay(new Date());
        String start = "2020-09-15 00:00:00";
        String end = "2020-09-16 23:59:59";
        log.info("TrainServiceImpl start:{},end:{}",start,end);

        int number = 20;
        //1、读取数据库，并且筛选掉脏数据
        List<RecordInfo> filterRecordInfos=  recordInfoMapper.selectTrainData(start,end);

        List<RecordInfo> recordInfos  = Lists.newArrayList();

        for (int i = 0; i < filterRecordInfos.size(); i++) {
            RecordInfo re = filterRecordInfos.get(i);
            if (re.getBelong() == 2 || re.getBelong() == 3){
                continue;
            }
            boolean isBelong = true;
            int min = i-number < 0 ? 0:i-number;
            int max = i+number > recordInfos.size() ? recordInfos.size()-1:i+number;
            for (int j = min; j < max; j++) {
                if (recordInfos.get(j).getBelong() == 2 || recordInfos.get(j).getBelong() == 3){
                    isBelong = false;
                }
            }
            if (isBelong){
                recordInfos.add(re);
            }


        }


        //2、更新归一化矩阵
        double[] newXMaxArray = this.getNewInputMaxNormalization(recordInfos);
        double[] newXMinArray = this.getNewInputMinNormalization(recordInfos);
        double xMaxValue = this.getNewOutputMaxNormalization(recordInfos);
        double xMinValue = this.getNewOutputMinNormalization(recordInfos);

        //3、初始化神经网络
        double[][] newInputWeight = bpNeuralNetworkHandle.initInputWeight();
        double[] newOutputWeight = bpNeuralNetworkHandle.initOutputWeight();
        double[] newBOneArray = bpNeuralNetworkHandle.initBOneArray();
        double newB2Value = bpNeuralNetworkHandle.initB2Value();

        //训练
        while (iterNumber > 0 || trainError < error) {
            double deviation = 0d;
            double midAbsError = 0;
            double[] inputData = new double[7];
            for (RecordInfo record : recordInfos) {
                inputData[0] = record.getATime();
                inputData[1] = record.getBTime();
                inputData[2] = record.getInNox();
                inputData[3] = record.getInSo2();
                inputData[4] = record.getInFlux();
                inputData[5] = record.getInO2();
                inputData[6] = record.getInTemp();

                //4、输入数据归一化
                double[] inputValuesNm = bpNeuralNetworkHandle.normalization(inputData,newXMaxArray,newXMinArray);
                //5、计算现有网络的输出
                double predictValue = bpNeuralNetworkHandle.getPredictedValue(inputValuesNm,newInputWeight,newOutputWeight,newBOneArray,newB2Value);
                double trueValue = bpNeuralNetworkHandle.normalizationOutput(record.getTrueValue(),xMaxValue,xMinValue);

                //6、获取输出层的误差；
                double outPutError = bpNeuralNetworkHandle.getOutError(predictValue, trueValue);
                //7、获取隐藏层的输入值
                double[] hiddenInput = bpNeuralNetworkHandle.getHiddenInput(inputValuesNm,newInputWeight,newBOneArray);
                //8、获取隐含层的残差；
                double[] hideError = bpNeuralNetworkHandle.getHideError(outPutError, newOutputWeight, hiddenInput);
                //9、更新输入层->隐含层权值
                bpNeuralNetworkHandle.updateWeight(newInputWeight, inputValuesNm, hideError);
                //10、更新隐含层->输出层权值
                bpNeuralNetworkHandle.updateWeight(newOutputWeight, hiddenInput, outPutError);
                //11、更新阈值
                bpNeuralNetworkHandle.updateThreshold(newBOneArray,newB2Value,hideError,outPutError);


                //12、计算 count(样本值-预测值)的平方

                double nmTrueValue = bpNeuralNetworkHandle.reverseNormalization(trueValue,xMaxValue,xMinValue);
                double nmPredictValue = bpNeuralNetworkHandle.reverseNormalization(predictValue,xMaxValue,xMinValue);
                deviation = deviation + Math.pow(Math.abs(trueValue-predictValue),2);
                midAbsError = midAbsError + Math.abs(nmTrueValue-nmPredictValue)/nmPredictValue;


            }
            trainError = deviation/recordInfos.size();
            absError = midAbsError/recordInfos.size();
            log.info("train iterator iterNumber:{}，均方差:{},absError:{}", iterNumber,trainError,absError);
            iterNumber--;
        }

        //写文件
        this.wirte(newInputWeight,newOutputWeight,newBOneArray,newB2Value,newXMaxArray,newXMinArray,xMaxValue, xMinValue);

        //应用
        double test = 0;
        for (RecordInfo record : recordInfos){
            double[] inputData = new double[7];
            inputData[0] = record.getATime();
            inputData[1] = record.getBTime();
            inputData[2] = record.getInNox();
            inputData[3] = record.getInSo2();
            inputData[4] = record.getInFlux();
            inputData[5] = record.getInO2();
            inputData[6] = record.getInTemp();

            //4、输入数据归一化
            double[] inputValuesNm = bpNeuralNetworkHandle.normalization(inputData,newXMaxArray,newXMinArray);
            double predictedValue = bpNeuralNetworkHandle.getPredictedValue(inputValuesNm,newInputWeight,newOutputWeight,newBOneArray,newB2Value);
            double trainPreDict = bpNeuralNetworkHandle.reverseNormalization(predictedValue,xMaxValue,xMinValue);
            //recordInfo.setTrainPredict(trainPreDict);
            test = test + Math.abs(record.getTrueValue()-trainPreDict)/trainPreDict;
           // recordInfoMapper.updateByPrimaryKeySelective(recordInfo);
        }
       double  absError2 = test/recordInfos.size();
        log.info("absError2 :{}",absError2);





        return trainError;

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

    //归一化矩阵输出值最大值
    public double getNewOutputMaxNormalization(List<RecordInfo> recordInfos){
        RecordInfo recordInfo = recordInfos.stream().filter(item -> item.getTrueValue() != null).max(Comparator.comparingDouble(RecordInfo::getTrueValue)).get();
        return recordInfo.getTrueValue();

    }


    //归一化矩阵输出值最小值
    public double getNewOutputMinNormalization(List<RecordInfo> recordInfos){
        RecordInfo recordInfo = recordInfos.stream().filter(item -> item.getTrueValue() != null).min(Comparator.comparingDouble(RecordInfo::getTrueValue)).get();
        return recordInfo.getTrueValue();
    }

    //将bp神经网络输出
    private void wirte(double[][] newInputWeight, double[] newOutputWeight, double[] newBOneArray, double newB2Value,double[] newXMaxArray,double[] newXMinArray,double xMaxValue,double xMinValue) {
        FileWriter fw= null;
        try {
            fw = new FileWriter(new File(path));
            //写入中文字符时会出现乱码
            BufferedWriter bw=new BufferedWriter(fw);
            //BufferedWriter  bw=new BufferedWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("E:/phsftp/evdokey/evdokey_201103221556.txt")), "UTF-8")));
            for (int i = 0; i < newInputWeight.length; i++) {
                for (int j = 0; j < newInputWeight[i].length; j++) {
                    bw.write(newInputWeight[i][j]+"\t\n");
                }

            }
            for (int i = 0; i < newBOneArray.length; i++) {
                bw.write(newBOneArray[i]+"\t\n");
            }

            for (int i = 0; i < newOutputWeight.length; i++) {
                bw.write(newOutputWeight[i]+"\t\n");
            }

            bw.write(newB2Value+"\t\n");

            for (int i = 0; i < newXMinArray.length; i++) {
                bw.write(newXMinArray[i]+"\t\n");
            }

            for (int i = 0; i < newXMaxArray.length; i++) {
                bw.write(newXMaxArray[i]+"\t\n");
            }

            bw.write(xMinValue+"\t\n");
            bw.write(xMaxValue+"");


            bw.close();
            fw.close();


        } catch (IOException e) {
            e.printStackTrace();
        }


    }



}
