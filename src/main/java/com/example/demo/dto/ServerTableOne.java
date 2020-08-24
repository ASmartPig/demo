package com.example.demo.dto;

import java.util.Date;

public class ServerTableOne {
    private Long id;

    private Long userId;

    private Long devGroupId;

    private Long devId;

    private Long sensorId;

    private Date createTime;

    private Double cemsOutNox;

    private Double cemsOutSo2;

    private Double denoxOutTemp;

    private Double denoxOutPress;

    private Double ammoniaEscape;

    private Double denoxInTemp;

    private Double denoxInPress;

    private Double denoxSmokeInFlux;

    private Double denoxSmokeInFluxTotal;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getDevGroupId() {
        return devGroupId;
    }

    public void setDevGroupId(Long devGroupId) {
        this.devGroupId = devGroupId;
    }

    public Long getDevId() {
        return devId;
    }

    public void setDevId(Long devId) {
        this.devId = devId;
    }

    public Long getSensorId() {
        return sensorId;
    }

    public void setSensorId(Long sensorId) {
        this.sensorId = sensorId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Double getCemsOutNox() {
        return cemsOutNox;
    }

    public void setCemsOutNox(Double cemsOutNox) {
        this.cemsOutNox = cemsOutNox;
    }

    public Double getCemsOutSo2() {
        return cemsOutSo2;
    }

    public void setCemsOutSo2(Double cemsOutSo2) {
        this.cemsOutSo2 = cemsOutSo2;
    }

    public Double getDenoxOutTemp() {
        return denoxOutTemp;
    }

    public void setDenoxOutTemp(Double denoxOutTemp) {
        this.denoxOutTemp = denoxOutTemp;
    }

    public Double getDenoxOutPress() {
        return denoxOutPress;
    }

    public void setDenoxOutPress(Double denoxOutPress) {
        this.denoxOutPress = denoxOutPress;
    }

    public Double getAmmoniaEscape() {
        return ammoniaEscape;
    }

    public void setAmmoniaEscape(Double ammoniaEscape) {
        this.ammoniaEscape = ammoniaEscape;
    }

    public Double getDenoxInTemp() {
        return denoxInTemp;
    }

    public void setDenoxInTemp(Double denoxInTemp) {
        this.denoxInTemp = denoxInTemp;
    }

    public Double getDenoxInPress() {
        return denoxInPress;
    }

    public void setDenoxInPress(Double denoxInPress) {
        this.denoxInPress = denoxInPress;
    }

    public Double getDenoxSmokeInFlux() {
        return denoxSmokeInFlux;
    }

    public void setDenoxSmokeInFlux(Double denoxSmokeInFlux) {
        this.denoxSmokeInFlux = denoxSmokeInFlux;
    }

    public Double getDenoxSmokeInFluxTotal() {
        return denoxSmokeInFluxTotal;
    }

    public void setDenoxSmokeInFluxTotal(Double denoxSmokeInFluxTotal) {
        this.denoxSmokeInFluxTotal = denoxSmokeInFluxTotal;
    }
}