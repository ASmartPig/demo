package com.example.demo.service;

public interface BpNetUseService {

    void predictedAndSave();

    void predictedAndSave(String start,String end);

    void removeDirty();
}
