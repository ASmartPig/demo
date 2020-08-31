package com.example.demo.controller;

import com.example.demo.config.BpConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.*;

public class MainTest {


    public static void main(String[] args) throws IOException {
      //1、系统启动，将配置文件读到集合里
        // 2、从数据库路取cems值，以及时间、
        // 3、handle，给出预期值
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(BpConfig.class);
        double[][] inputWeight = (double[][]) applicationContext.getBean("inputWeight");
        //System.out.println(inputWeight.length);
        System.out.println(((10 - 1) * 8)/138.7516);
    }
    public static double Mytanh(double value) {
        double ex = Math.pow(Math.E, value);// e^x
        double ey = Math.pow(Math.E, -value);//e^(-x)
        double sinhx = ex-ey;
        double coshx = ex+ey;
        double result = sinhx/coshx;
        return result;
    }




}
