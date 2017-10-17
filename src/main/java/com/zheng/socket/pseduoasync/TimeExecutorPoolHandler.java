package com.zheng.socket.pseduoasync;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池，用于处理任务队列
 * @Author zhenglian
 * @Date 2017/10/17 16:17
 */
public class TimeExecutorPoolHandler {
    /**
     * 线程池最大线程数
     */
    private Integer maxPoolSize;
    /**
     * 任务队列容量
     */
    private Integer queueSize;
    
    private ExecutorService executor;
    
    public TimeExecutorPoolHandler(Integer maxPoolSize, Integer queueSize) {
        this.maxPoolSize = maxPoolSize;
        this.queueSize = queueSize;
        executor = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(), maxPoolSize, 120L, 
                TimeUnit.SECONDS, new ArrayBlockingQueue<>(queueSize));
    }
    
    public void execute(Runnable task) {
        executor.execute(task);
    }
}
