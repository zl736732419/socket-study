package com.zheng.socket.traditional;

import com.zheng.socket.constants.Constants;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Optional;

/**
 * 传统bio模型 服务器
 * @Author zhenglian
 * @Date 2017/10/17 14:42
 */
public class TimeServer {
    private ServerSocket serverSocket;
    
    public static void main(String[] args) throws Exception {
        TimeServer server = new TimeServer();
        server.start();
    }

    private void start() throws Exception {
        serverSocket = new ServerSocket(Constants.PORT);
        Socket socket;
        try {
            while(true) {
                System.out.println("服务器监听接口: " + Constants.PORT);
                socket = serverSocket.accept();
                // 每次接受到一个客户端请求之后就创建一个线程去负责处理
                new Thread(new TimeServerHandler(socket)).start(); 
            }
        } catch(Exception e) {
            
        } finally {
            if (Optional.ofNullable(serverSocket).isPresent()) {
                serverSocket.close();
            }
        }
        while(true) {
            serverSocket.accept();
        }
    }
}
