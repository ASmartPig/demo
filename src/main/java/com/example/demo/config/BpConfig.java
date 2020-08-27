package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.*;

@Configuration
public class BpConfig {

    private static String path = "/Users/liaoze/Documents/1.txt";

    //sprivate static String path = "C:/Users/Lenovo/Desktop/1.txt";


    @Bean(name = "inputWeight")
    public double[][] getInputWeight() throws IOException {
        double inputWeight[][] = new double[7][12];
        FileReader fileReader = new FileReader(path);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String num;
        int line = 1 ;//行数
        while ((num = bufferedReader.readLine())  !=null){
            if (line <85){
                int num1 = (line-1)/12;
                int num2 = (line-1)%12;
                inputWeight[num1][num2] = Double.valueOf(num);
            }
            line++;

        }
        bufferedReader.close();
        fileReader.close();
        return inputWeight;
    }

    @Bean(name = "outputWeight")
    public double[] getOutputWeight() throws IOException {
        double outputWeight[] = new double[12];
        FileReader fileReader = new FileReader(path);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String num;
        int line = 1 ; //函数
        int i = 0 ;
        while ((num = bufferedReader.readLine())  !=null){
            if (line >= 97 && line <= 108){
                outputWeight[i] = Double.valueOf(num);
                i++;
            }
            line++;

        }
        bufferedReader.close();
        fileReader.close();
        return outputWeight;
    }

    @Bean(name = "bOneArray")
    public double[] getBOneArray() throws IOException {
        double bOneArray[] = new double[12];
        FileReader fileReader = new FileReader(path);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String num;
        int line = 1 ; //函数
        int i = 0 ;
        while ((num = bufferedReader.readLine())  !=null){
            if (line >= 85 && line <= 96){
                bOneArray[i] = Double.valueOf(num);
                i++;
            }
            line++;

        }
        bufferedReader.close();
        fileReader.close();
        return bOneArray;
    }

    @Bean(name = "b2Value")
    public double getB2Value() throws IOException {
        double b2Value = 0;
        FileReader fileReader = new FileReader(path);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String num;
        int line = 1 ; //函数
        int i = 0 ;
        while ((num = bufferedReader.readLine())  !=null){
            if (line == 109){
                b2Value= Double.valueOf(num);
                i++;
            }
            line++;

        }
        bufferedReader.close();
        fileReader.close();
        return b2Value;
    }


    @Bean(name = "xMinArray")
    public double[] getXMinArray() throws IOException {
        double xMinArray[] = new double[7];
        FileReader fileReader = new FileReader(path);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String num;
        int line = 1 ; //函数
        int i = 0 ;
        while ((num = bufferedReader.readLine())  !=null){
            if (line >= 110 && line <= 116){
                xMinArray[i] = Double.valueOf(num);
                i++;
            }
            line++;

        }
        bufferedReader.close();
        fileReader.close();
        return xMinArray;
    }

    @Bean(name = "xMaxArray")
    public double[] getXMaxArray() throws IOException {
        double xMaxArray[] = new double[7];
        FileReader fileReader = new FileReader(path);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String num;
        int line = 1 ; //函数
        int i = 0 ;
        while ((num = bufferedReader.readLine())  !=null){
            if (line >= 117 && line <= 123){
                xMaxArray[i] = Double.valueOf(num);
                i++;
            }
            line++;

        }
        bufferedReader.close();
        fileReader.close();
        return xMaxArray;
    }

    @Bean(name = "xMinValue")
    public double getYMinValue() throws IOException {
        double xMinValue = 0;
        FileReader fileReader = new FileReader(path);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String num;
        int line = 1 ; //函数
        int i = 0 ;
        while ((num = bufferedReader.readLine())  !=null){
            if (line == 124){
                xMinValue= Double.valueOf(num);
                i++;
            }
            line++;

        }
        bufferedReader.close();
        fileReader.close();
        return xMinValue;
    }

    @Bean(name = "xMaxValue")
    public double getYMaxValue() throws IOException {
        double xMaxValue = 0;
        FileReader fileReader = new FileReader(path);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String num;
        int line = 1 ; //函数
        int i = 0 ;
        while ((num = bufferedReader.readLine())  !=null){
            if (line == 125){
                xMaxValue= Double.valueOf(num);
                i++;
            }
            line++;

        }
        bufferedReader.close();
        fileReader.close();
        return xMaxValue;
    }









}
