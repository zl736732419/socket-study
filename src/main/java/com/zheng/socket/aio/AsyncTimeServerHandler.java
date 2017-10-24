package com.zheng.socket.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.CountDownLatch;

/**
 * Created by zhenglian on 2017/10/24.
 */
public class AsyncTimeServerHandler implements Runnable {
    private Integer port;
    private CountDownLatch latch;
    private AsynchronousServerSocketChannel channel; 
    
    
    public AsyncTimeServerHandler(Integer port) {
        this.port = port;
        try {
            channel = AsynchronousServerSocketChannel.open();
            channel.bind(new InetSocketAddress(port));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void run() {
        latch = new CountDownLatch(1);
        // 开始监听客户端连接
        doAccept();
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void doAccept() {
        channel.accept(this, new AcceptCompletionHandler());
    }

    public CountDownLatch getLatch() {
        return latch;
    }

    public void setLatch(CountDownLatch latch) {
        this.latch = latch;
    }

    public AsynchronousServerSocketChannel getChannel() {
        return channel;
    }

    public void setChannel(AsynchronousServerSocketChannel channel) {
        this.channel = channel;
    }
}
