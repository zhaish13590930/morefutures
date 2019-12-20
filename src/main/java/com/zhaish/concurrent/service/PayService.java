package com.zhaish.concurrent.service;

import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @datetime:2019/12/19 18:58
 * @author: zhaish
 * @desc:
 **/
@Component
public class PayService implements Runnable{
    @Override
    public void run(){
        System.out.println("支付开始");
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("支付成功3s");
    }
}
