package com.zheng.socket.nio.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

/**
 * @Author zhenglian
 * @Date 2017/10/19 17:43
 */
public class TimeServer implements Runnable {
    
    private ServerSocketChannel serverSocketChannel;
    private Selector selector;
    
    public TimeServer(Integer port) {
        try {
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            // 设置连接为非阻塞的
            serverSocketChannel.configureBlocking(false);
            // 服务器监听端口
            serverSocketChannel.socket().bind(new InetSocketAddress(port));
            // 将channel注册到selector多路复用器
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT); // 等待客户端发起链接
            System.out.println("服务器已启动，监听本地端口: " + port);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("发生错误: " + e.getLocalizedMessage());
        }
    }
    
    @Override
    public void run() {
        
    }
}
