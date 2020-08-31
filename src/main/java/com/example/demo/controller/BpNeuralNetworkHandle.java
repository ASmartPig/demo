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
        double[] hiddenInput = new double[12];
        //获取i隐含层的输入值
        for (int i=0;i<12;i++){
            hiddenInput[i] = getOutValue(i,inputValues,inputWeight);
        }
        //计算输出值
        double predictedValue = getTarget(hiddenInput,outputWeight);
        return predictedValue;
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

    public double reverseNormalization(double predict){
        double xMid = xMaxValue - xMinValue;
        double yMid = yMaxValue - yMinValue;
        double trueValue = ((predict - yMinValue)/yMid) * xMid + xMinValue;
        return trueValue;
    }



}
