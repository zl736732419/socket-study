package com.zheng.socket.nio.server;

/**
 * @Author zhenglian
 * @Date 2017/10/19 17:38
 */
public class ServerBootstrap {
    
    private Integer port;
    public ServerBootstrap(Integer port) {
        this.port = port;
    }

    /**
     * 启动服务器
     * @param args
     */
    public static void main(String[] args) {
        int port = 8000;
        new Thread(new TimeServerHandler(port)).start();
    }
}
