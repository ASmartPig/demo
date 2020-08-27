package com.example.demo.util;

import java.math.BigDecimal;

/**
 * @description:
 * @author: liaoze
 * @date: 2020/5/15 2:34 下午
 * @version: 0.0.0
 */
public class CalculateUtil {

    /**
     * @Author: liaoze
     * @Description: 除法保留几位小数
     * @Date: 2020/5/15
     * @param number1 被除数
     * @param number2 除数
     * @param size 保留几位小数
     * @Return: java.lang.String
     **/
    public static String division(Integer number1,Integer number2,Integer size){
        if (number1 == 0 || number2 == 0){
            return "0";
        }
        if (number1%number2==0){
            return String.valueOf(number1/number2);
        }
        BigDecimal b1 = new BigDecimal(Double.toString(number1));
        BigDecimal b2 = new BigDecimal(Double.toString(number2));

        return b1.divide(b2, size, BigDecimal.ROUND_HALF_UP).toString();
    }
}
