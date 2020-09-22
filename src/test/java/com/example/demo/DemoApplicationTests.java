package com.example.demo;

import com.example.demo.service.PredictService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoApplicationTests {

    @Autowired
    private PredictService inputService;

    @Test
    void contextLoads() {
        inputService.predictedAndSave("2020-08-27 12:32:35","2020-08-27 13:22:09");
    }

}
