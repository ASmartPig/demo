package com.example.demo.controller;

import com.example.demo.dto.RecordInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class BpNeuralNetworkHandle {

    private static  double yMaxValue = 1;

    private static  double yMinValue = -1;

    //学习速率
    private static  double LEARN_FACTORS = 0.01;


    public double[] normalization(double[] inputValues,double[] xMaxArray,double[] xMinArray){
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
    public double getPredictedValue(double[] inputValues,double[][] inputWeight,double[] outputWeight,double[] bOneArray,double b2Value){
        //隐含层输入值
        double[] hiddenInput = getHiddenInput(inputValues,inputWeight,bOneArray);
        //计算输出值
        double predictedValue = getTarget(hiddenInput,outputWeight,b2Value);
        return predictedValue;
    }


    //获取隐含层输入值
    public double[] getHiddenInput(double[] inputValues,double[][] inputWeight,double[] bOneArray){
        double[] hiddenInput = new double[12];
        //获取i隐含层的输入值
        for (int i=0;i<12;i++){
            hiddenInput[i] = getOutValue(i,inputValues,inputWeight,bOneArray);
        }
        return hiddenInput;

    }

    //获取i隐含层的输入值
    public double getOutValue(int i,double[] inputValues,double[][] inputWeight,double[] bOneArray){
        double count = 0;
        for (int j = 0; j < inputValues.length; j++) {
                count += inputValues[j] * inputWeight[j][i];
        }
        count += bOneArray[i];
        //激励函数
        return Math.tanh(count);
    }

    public double getTarget(double[] hiddenInput,double[] outputWeight,double b2Value){
        double count = 0;
        for (int i = 0; i < hiddenInput.length; i++) {
            count += hiddenInput[i] * outputWeight[i];
        }
        count += b2Value;
        //激励函数 x = y
        return count;
    }

    //反归一化
    public double reverseNormalization(double predict,double xMaxValue,double xMinValue){
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
            hideError[i] = outPutError * outputWeight[i] * (1-Math.pow(hiddenInput[i],2));;
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

    //初始化输入层->隐含层权值
    public double[][] initInputWeight() {
        double inputWeight[][] = new double[7][12];
        for (int i = 0; i < inputWeight.length; i++) {
            for (int j = 0; j < inputWeight[i].length; j++) {
                inputWeight[i][j] = 0.5 - Math.random();
            }
        }
        return  inputWeight;
    }

    //初始化隐含层->输出层权值
    public double[] initOutputWeight() {
        double outputWeight[] = new double[12];
        for (int i = 0; i < outputWeight.length; i++) {
            outputWeight[i] = 0.5 - Math.random();
        }
        return  outputWeight;
    }

    //初始化隐含层阈值
    public double[] initBOneArray() {
        double bOneArray[] = new double[12];
        for (int i = 0; i < bOneArray.length; i++) {
            bOneArray[i] = 0.5 - Math.random();
        }
        return  bOneArray;
    }


    //初始化隐含层阈值
    public double initB2Value() {
        double  b2Value = 0.5 - Math.random();
        return  b2Value;
    }

    //输出归一化
    public double normalizationOutput(Double value, double xMaxValue, double xMinValue) {
        double xMid = xMaxValue - xMinValue;
        double yMid = yMaxValue - yMinValue;
        return  ((value - xMinValue)/xMid)* yMid + yMinValue;
    }

    public void updateThreshold(double[] newBOneArray, double newB2Value, double[] hideError, double outPutError) {
        for (int i = 0; i < hideError.length; i++) {
            newBOneArray[i] = hideError[i] * LEARN_FACTORS;
        }
        newB2Value = outPutError * LEARN_FACTORS;
    }
}