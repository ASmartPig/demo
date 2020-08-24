package com.example.demo.controller;

import java.util.concurrent.Callable;

public class CallableTask implements Runnable {

    @Override
    public void run() {
        try {
            System.out.println(Thread.currentThread().getId() +"---sleep--"+ Thread.currentThread().getName());
            Thread.sleep(10000000);
            System.out.println(Thread.currentThread().getId() +"---sleep over--"+ Thread.currentThread().getName());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
//    @Override
//    public Boolean call() throws Exception {
//        int i = 3/0;
//        return true;
//    }
}
