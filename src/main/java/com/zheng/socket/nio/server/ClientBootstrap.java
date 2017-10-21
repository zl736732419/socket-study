package com.zheng.socket.nio.server;

/**
 * @Author zhenglian
 * @Date 2017/10/21 14:17
 */
public class ClientBootstrap {
    public static void main(String[] args) {
        String host = "127.0.0.1";
        int port = 8000;
        new Thread(new TimeClientHandler(host, port)).start();
    }
}
