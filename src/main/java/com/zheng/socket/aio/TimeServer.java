package com.zheng.socket.aio;

/**
 * Created by zhenglian on 2017/10/24.
 */
public class TimeServer {
    public static void main(String[] args) {
        int port = 8080;
        AsyncTimeServerHandler handler = new AsyncTimeServerHandler(port);
        new Thread(handler, "timeServer").start();
    }
}
