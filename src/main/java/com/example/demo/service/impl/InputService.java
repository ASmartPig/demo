package com.example.demo.service.impl;

import java.util.Map;

public interface InputService {


     void predictedAndSave();

     void predictedAndSave(Map<String,Double> map);

     void predictedAndSave(String start,String end);
}
