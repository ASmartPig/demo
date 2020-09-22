package com.example.demo.dto;

import lombok.Data;

import java.util.Date;

@Data
public class RecordInfo {
    private Integer id;

    private Long rid;

    private Long ATime;

    private Long BTime;

    private Double inNox;

    private Double inSo2;

    private Double inFlux;

    private Double inO2;

    private Double inTemp;

    private Double predictValue;

    private Double trainPredict;

    private Double trueValue;

    private Date createTime;

    private Date insertTime;

    private Integer belong;

    private Double absError;

    private Integer isDirtyNeighbor;
}