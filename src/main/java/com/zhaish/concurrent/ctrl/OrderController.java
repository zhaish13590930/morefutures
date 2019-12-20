package com.zhaish.concurrent.ctrl;

import com.google.common.util.concurrent.*;
import com.zhaish.concurrent.service.OrderService;
import com.zhaish.concurrent.service.PayService;
import com.zhaish.concurrent.service.RecommenderService;
import com.zhaish.concurrent.service.StockService;
import org.apache.tomcat.util.threads.ThreadPoolExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @datetime:2019/12/19 18:53
 * @author: zhaish
 * @desc:
 **/
@RestController
public class OrderController {
    private static final AtomicInteger threadNum = new AtomicInteger();
    private ListeningExecutorService listeningEs;
    static {

    }
    {
        int cpuNum = Runtime.getRuntime().availableProcessors();
        ThreadFactory threadFactory = new ThreadFactory() {
            private String name;
            @Override
            public Thread newThread(Runnable r) {
                this.name = "work-thread"+threadNum.getAndIncrement();
                return new Thread(r);
            }
        };
        ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(10000);
        ThreadPoolExecutor executor = new ThreadPoolExecutor(cpuNum,cpuNum*2+1,5, TimeUnit.MINUTES,
                queue,threadFactory,
                new ThreadPoolExecutor.AbortPolicy());
        listeningEs = MoreExecutors.listeningDecorator(executor);
    }

    @Autowired private OrderService orderService;
    @Autowired private PayService payService;
    @Autowired private RecommenderService recommenderService;
    @Autowired private StockService stockService;

    @GetMapping("/order/{id}")
    public String getOrder(@PathVariable String id){
        long start = System.currentTimeMillis();
        orderService.run();
        payService.run();
        recommenderService.run();
        stockService.run();
        long end  = System.currentTimeMillis();
        System.out.println("order spent time:"+(end - start)+"ms");
        return String.format("orderInfo:%s",id+(end - start)+"ms");
    }

    @GetMapping("/order1/{id}")
    public String getOrder1(@PathVariable String id) throws ExecutionException, InterruptedException {
        long start = System.currentTimeMillis();
        ListenableFuture f1 =listeningEs.submit(orderService);
        ListenableFuture f2 =listeningEs.submit(payService);
        ListenableFuture f3 = listeningEs.submit(recommenderService);
        ListenableFuture f4 = listeningEs.submit(stockService);
        ListenableFuture futureList = Futures.allAsList(f1,f2,f3,f4);
        futureList.get();
        FutureCallback callback = new FutureCallback() {
            @Override
            public void onSuccess(Object result) {
                // System.out.println(result+" is ok");
            }

            @Override
            public void onFailure(Throwable t) {
                System.out.println("fail"+t);
            }
        };
        Futures.addCallback(f1,callback, listeningEs);
        Futures.addCallback(f2,callback, listeningEs);
        Futures.addCallback(f3,callback, listeningEs);
        Futures.addCallback(f4,callback, listeningEs);
        long end  = System.currentTimeMillis();
        System.out.println("order spent time:"+(end - start)+"ms");
        return String.format("orderInfo:%s",id+(end - start)+"ms");
    }


    @GetMapping("/order2/{id}")
    public String getOrder2(@PathVariable String id){
        long start = System.currentTimeMillis();
        listeningEs.submit(orderService);
        listeningEs.submit(payService);
        listeningEs.submit(recommenderService);
        listeningEs.submit(stockService);
        long end  = System.currentTimeMillis();
        System.out.println("order spent time:"+(end - start)+"ms");
        return String.format("orderInfo:%s",id+(end - start)+"ms");
    }

    @GetMapping("/order3/{id}")
    public String getOrder3(@PathVariable String id) throws ExecutionException, InterruptedException {
        long start = System.currentTimeMillis();
        //Callable
        ListenableFuture f1 =listeningEs.submit(orderService);
        ListenableFuture f2 =listeningEs.submit(payService);
        ListenableFuture f3 = listeningEs.submit(recommenderService);
        ListenableFuture f4 = listeningEs.submit(stockService);

        f2.get();

        //ListenableFuture futureList = Futures.allAsList(f1,f2,f3,f4);
        //futureList.get();
        FutureCallback callback = new FutureCallback() {
            @Override
            public void onSuccess(Object result) {
                // System.out.println(result+" is ok");
            }

            @Override
            public void onFailure(Throwable t) {
                System.out.println("fail"+t);
            }
        };
        Futures.addCallback(f1,callback, listeningEs);
        Futures.addCallback(f2,callback, listeningEs);
        Futures.addCallback(f3,callback, listeningEs);
        Futures.addCallback(f4,callback, listeningEs);
        long end  = System.currentTimeMillis();
        System.out.println("order spent time:"+(end - start)+"ms");
        return String.format("orderInfo:%s",id+(end - start)+"ms");
    }


}
