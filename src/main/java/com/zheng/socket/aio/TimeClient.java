package com.zheng.socket.aio;

/**
 * Created by zhenglian on 2017/10/24.
 */
public class TimeClient {
    public static void main(String[] args) {
        String host = "127.0.0.1";
        Integer port = 8080;
        new Thread(new AsyncTimeClientHandler(host, port)).start();
    }
}
