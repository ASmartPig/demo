package com.example.demo.service;

import java.util.Map;

public interface PredictService {


     void predictedAndSave();

//    void predictedAndSave(long id);

     void predictedAndSave(Map<String,Double> map);

     void predictedAndSave(String start,String end);

    void removeDirty();
}
