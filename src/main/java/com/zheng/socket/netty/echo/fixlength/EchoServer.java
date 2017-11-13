package com.zheng.socket.netty.echo.fixlength;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * 使用fixlengthframeDecoder解码器
 * @Author zhenglian
 * @Date 2017/11/13 13:50
 */
public class EchoServer {
    
    public void bind(int port) {
        EventLoopGroup worker = new NioEventLoopGroup();
        EventLoopGroup boss = new NioEventLoopGroup();
        
        try {

            ServerBootstrap b = new ServerBootstrap();
            b.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel sc) throws Exception {
                            sc.pipeline().addLast(new FixedLengthFrameDecoder(20));
                            sc.pipeline().addLast(new StringDecoder());
                            sc.pipeline().addLast(new FixedEchoServerHandler());
                            
                            
                        }
                    });
            ChannelFuture f = b.bind(port).sync();
            f.channel().closeFuture().sync();
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            worker.shutdownGracefully();
            boss.shutdownGracefully();
        }
    }
    
    public static void main(String[] args) {
        new EchoServer().bind(8080);
    }
    
}
