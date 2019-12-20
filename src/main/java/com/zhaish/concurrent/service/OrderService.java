package com.zhaish.concurrent.service;

import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @datetime:2019/12/19 18:57
 * @author: zhaish
 * @desc:
 **/
@Component
public class OrderService implements Runnable{
    @Override
    public void run(){
        System.out.println("下单开始");
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("下单成功 2s");
    }
}
