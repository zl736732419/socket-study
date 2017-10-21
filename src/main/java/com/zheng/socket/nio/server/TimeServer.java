package com.zheng.socket.nio.server;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

/**
 * @Author zhenglian
 * @Date 2017/10/19 17:43
 */
public class TimeServer implements Runnable {
    
    private ServerSocketChannel serverSocketChannel;
    private Selector selector;
    private volatile boolean stop = false;

    /**
     * 初始化serverSocket服务器配置信息
     * @param port
     */
    public TimeServer(Integer port) {
        try {
            // 打开selector多路复用选择器
            selector = Selector.open();
            // 打开服务端channel通道
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
        // 轮询查看是否有客户端的可用连接进来
        while(!stop) {
            try {
                // 设置复用器扫描可用连接的超时时间，1秒，无参方法将会阻塞
                selector.select(1000);
                // 获取到建立连接的客户端
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> it = selectionKeys.iterator();
                SelectionKey key = null;
                while(it.hasNext()) {
                    key = it.next();
                    it.remove(); // 处理的客户端连接不能重复处理，所以需要排除掉
                    handleInput(key);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        // 多路复用器关闭后，注册到上面的所有channel和pipe等资源都将自动关闭
        if (Optional.ofNullable(selector).isPresent()) {
            try {
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 处理客户端连接请求
     * @param key
     */
    private void handleInput(SelectionKey key) {
        if (!key.isValid()) {
            return;
        }
        // 处理新接入的请求
        SocketChannel sc = null;
        if (key.isAcceptable()) {
            ServerSocketChannel channel = (ServerSocketChannel) key.channel();
            try {
                sc = channel.accept();
                // 设置连接为非阻塞模式
                sc.configureBlocking(false);
                sc.register(selector, SelectionKey.OP_READ);
            } catch (IOException e) {
                throw new RuntimeException(e.getLocalizedMessage());
            }
        }
        // 当前连接可读
        if (key.isReadable()) {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int readBytes = 0; // 获取接受到的信息长度
            try {
                readBytes = sc.read(buffer);
            } catch (IOException e) {
                throw new RuntimeException("读取客户端发送的数据失败，错误原因：" + e.getLocalizedMessage());
            }
            if (readBytes > 0) {
                buffer.flip();
                byte[] bytes = new byte[buffer.remaining()];
                buffer.get(bytes);
                String body = new String(bytes, StandardCharsets.UTF_8);
                System.out.println("the time server receive order: " + body);
                // 返回响应信息
                String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body)
                        ? new Date(System.currentTimeMillis()).toString()
                        : "BAD ORDER";
                writeResponse(sc, currentTime);
            } else if (readBytes < 0) {
                key.cancel();
                try {
                    sc.close();
                } catch (IOException e) {
                    throw new RuntimeException("关闭socketChannel连接异常");
                }
            } else {
                // 读到0字节，自动忽略
            }
        }
    }

    /**
     * 响应消息
     * @param sc
     * @param response
     */
    private void writeResponse(SocketChannel sc, String response) {
        if (StringUtils.isEmpty(response)) {
            return;
        }
        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
        ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
        buffer.put(bytes);
        buffer.flip();
        try {
            sc.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        this.stop = true;
    }

}
