package com.zheng.socket.netty.timeserver;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

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
            // 以换行符\r\n作为分隔符
            sc.pipeline().addLast(new LineBasedFrameDecoder(1024));

            // 采用特定分隔符进行分隔
//            DelimiterBasedFrameDecoder
            // 解析定长消息
//            FixedLengthFrameDecoder
            
            // 将读取到的内容转化成string
            sc.pipeline().addLast(new StringDecoder());
            
            // 使用LineBasedFrameDecoder+StringDecoder就构成了一个字符串拆分工具
            
            sc.pipeline().addLast(new TimeServerHandler());
        }
    }
}
