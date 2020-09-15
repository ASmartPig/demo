package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class BpNeuralNetworkHandle {

    @Autowired
    @Qualifier("inputWeight")
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

    private static  double yMaxValue = 1;

    private static  double yMinValue = -1;

    //学习速率
    private static  double LEARN_FACTORS = 0.01;

    public double[] normalization(double[] inputValues){
        double[] inputValuesNm = new double[inputValues.length];
        for (int i = 0; i < inputValuesNm.length; i++) {
            double xMid = xMaxArray[i] - xMinArray[i];
            double yMid = yMaxValue - yMinValue;
            inputValuesNm[i] = ((inputValues[i] - xMinArray[i])/xMid)* yMid + yMinValue;
        }
        return inputValuesNm;
    }

    //long x1,long x2,double x3,double x4,double x5,double x6,double x7
    //计算预测值
    public double getPredictedValue(double[] inputValues){
        //隐含层输入值
        double[] hiddenInput = getHiddenInput(inputValues);
        //计算输出值
        double predictedValue = getTarget(hiddenInput,outputWeight);
        return predictedValue;
    }


    //获取隐含层输入值
    public double[] getHiddenInput(double[] inputValues){
        double[] hiddenInput = new double[12];
        //获取i隐含层的输入值
        for (int i=0;i<12;i++){
            hiddenInput[i] = getOutValue(i,inputValues,inputWeight);
        }
        return hiddenInput;

    }

    //获取i隐含层的输入值
    public double getOutValue(int i,double[] inputValues,double[][] inputWeight){
        double count = 0;
        for (int j = 0; j < inputValues.length; j++) {
                count += inputValues[j] * inputWeight[j][i];
        }
        count += bOneArray[i];
        //激励函数
        return Math.tanh(count);
    }

    public double getTarget(double[] hiddenInput,double[] outputWeight){
        double count = 0;
        for (int i = 0; i < hiddenInput.length; i++) {
            count += hiddenInput[i] * outputWeight[i];
        }
        count += b2Value;
        //激励函数 x = y
        return count;
    }

    //反归一化
    public double reverseNormalization(double predict){
        double xMid = xMaxValue - xMinValue;
        double yMid = yMaxValue - yMinValue;
        double trueValue = ((predict - yMinValue)/yMid) * xMid + xMinValue;
        return trueValue;
    }

    //输出层残差
    public double getOutError(double predict,double trueValue){
        return trueValue - predict;
    }

    //隐含层残差
    public double[] getHideError(double outPutError,double[] outputWeight,double[] hiddenInput){
        double[] hideError = new double[12];
        for (int i = 0; i < outputWeight.length; i++) {
            hideError[i] = outPutError * outputWeight[i] * Math.pow(1 - hiddenInput[i],2);;
        }
        return hideError;
    }

    //更新输入层->隐含层权值
    public void updateWeight(double[][] newInputWeight,double[] inputValues,double[] hideError){
        for (int i = 0; i < newInputWeight.length; i++) {
            for (int j = 0; j < newInputWeight[i].length; j++) {
                newInputWeight[i][j] = (inputValues[i] * hideError[j] * LEARN_FACTORS ) + newInputWeight[i][j];
            }
        }
    }

    //更新隐含层->输出层权值
    public void updateWeight(double[] newOutputWeight,double[] hiddenInput,double outPutError){
        for (int i = 0; i < hiddenInput.length; i++) {
            newOutputWeight[i] = (hiddenInput[i] * outPutError * LEARN_FACTORS) + newOutputWeight[i];
        }
    }








}
