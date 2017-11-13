package com.zheng.socket.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * netty实现时间服务器
 * @Author zhenglian
 * @Date 2017/10/31 17:38
 */
public class TimeServer {
    public static void main(String[] args) {
        int port = 8080;
        new TimeServer().bind(port);
    }

    private void bind(int port) {
        // 配置服务器端的NIO线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        // reactor线程组，一个用于服务端接受客户端的连接，一个用于socketchannel网络读写
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childHandler(new ChildChannelHandler());

        // 绑定端口，同步等待成功
        try {
            ChannelFuture future = bootstrap.bind(port).sync();
            // 等待服务器监听端口关闭
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 优雅退出，释放线程资源
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }

    /**
     * io事件、记录日志、消息编解码
     */
    private class ChildChannelHandler extends ChannelInitializer<SocketChannel> {

        @Override
        protected void initChannel(SocketChannel sc) throws Exception {
            sc.pipeline().addLast(new TimerServerHandler());
        }
    }
}
