package com.example.demo.dto;

import java.util.Date;

public class RecordInfo {
    private Integer id;

    private Double predictValue;

    private Double trueValue;

    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
}