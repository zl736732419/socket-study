package com.zheng.socket.pseduoasync;

import com.zheng.socket.constants.Constants;
import com.zheng.socket.traditional.TimeServerHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Optional;

/**
 * 伪异步，采用线程池+任务队列来解决传统socket线程连接泛滥的问题
 * @Author zhenglian
 * @Date 2017/10/17 15:52
 */
public class TimeServer {
    
    public static void main(String[] args) {
        TimeServer server = new TimeServer();
        server.start();
    }

    private void start() {
        // 初始化线程池与任务队列
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(Constants.PORT);
            Socket socket;
            while(true) {
                System.out.println("服务器启动，监听端口: " + Constants.PORT);
                socket = serverSocket.accept();
                TimeExecutorPoolHandler poolHandler = new TimeExecutorPoolHandler(50, 10000);
                poolHandler.execute(new TimeServerHandler(socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (Optional.ofNullable(serverSocket).isPresent()) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
