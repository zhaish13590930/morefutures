package com.zhaish.concurrent.service;

import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @datetime:2019/12/19 18:59
 * @author: zhaish
 * @desc:
 **/
@Component
public class StockService implements Runnable{
    @Override
    public void run(){
        System.out.println("减库存开始");
        try {
            TimeUnit.SECONDS.sleep(7);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("减库存成功7s");
    }
}
