package com.example.demo.dto;

import java.util.Date;

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

    private Double trueValue;

    private Date createTime;

    private Date insertTime;

    private Integer belong;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getRid() {
        return rid;
    }

    public void setRid(Long rid) {
        this.rid = rid;
    }

    public Double getPredictValue() {
        return predictValue;
    }

    public void setPredictValue(Double predictValue) {
        this.predictValue = predictValue;
    }

    public Double getTrueValue() {
        return trueValue;
    }

    public void setTrueValue(Double trueValue) {
        this.trueValue = trueValue;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(Date insertTime) {
        this.insertTime = insertTime;
    }

    public Integer getBelong() {
        return belong;
    }

    public void setBelong(Integer belong) {
        this.belong = belong;
    }

    public Double getInNox() {
        return inNox;
    }

    public void setInNox(Double inNox) {
        this.inNox = inNox;
    }

    public Double getInSo2() {
        return inSo2;
    }

    public void setInSo2(Double inSo2) {
        this.inSo2 = inSo2;
    }

    public Double getInFlux() {
        return inFlux;
    }

    public void setInFlux(Double inFlux) {
        this.inFlux = inFlux;
    }

    public Double getInO2() {
        return inO2;
    }

    public void setInO2(Double inO2) {
        this.inO2 = inO2;
    }

    public Double getInTemp() {
        return inTemp;
    }

    public void setInTemp(Double inTemp) {
        this.inTemp = inTemp;
    }

    public Long getATime() {
        return ATime;
    }

    public void setATime(Long ATime) {
        this.ATime = ATime;
    }

    public Long getBTime() {
        return BTime;
    }

    public void setBTime(Long BTime) {
        this.BTime = BTime;
    }
}