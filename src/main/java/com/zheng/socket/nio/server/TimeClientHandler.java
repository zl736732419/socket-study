package com.zheng.socket.nio.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

/**
 * nio客户端
 *
 * @Author zhenglian
 * @Date 2017/10/21 13:34
 */
public class TimeClientHandler implements Runnable {
    private Selector selector;
    private SocketChannel sc;
    private volatile boolean stop = false;

    private String host;
    private Integer port;

    public void stop() {
        this.stop = true;
    }

    public TimeClientHandler(String host, Integer port) {
        this.host = host;
        this.port = port;
        config();
    }

    /**
     * 配置nio连接通道
     */
    private void config() {
        try {
            selector = Selector.open();
            sc = SocketChannel.open();
            // 设置非阻塞方式连接
            sc.configureBlocking(false);
        } catch (IOException e) {
            throw new RuntimeException("发生错误: " + e.getLocalizedMessage());
        }
    }

    @Override
    public void run() {
        try {
            connectServer();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("建立与服务器的连接发生错误: " + e.getLocalizedMessage());
        }
        while (!stop) {
            try {
                selector.select(1000);
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> it = selectedKeys.iterator();
                SelectionKey key;
                while (it.hasNext()) {
                    key = it.next();
                    it.remove();
                    try {
                        handleInput(key);
                    } catch (IOException e) {
                        e.printStackTrace();
                        if (Optional.ofNullable(key).isPresent()) {
                            key.cancel();
                            if (Optional.ofNullable(key.channel()).isPresent()) {
                                key.channel().close();
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (Optional.ofNullable(selector).isPresent()) {
            try {
                selector.close();
            } catch (IOException e) {
                throw new RuntimeException("关闭连接时异常，异常原因: " + e.getLocalizedMessage());
            }
        }
    }

    /**
     * 处理从服务器端过来的响应信息
     *
     * @param key
     */
    private void handleInput(SelectionKey key) throws IOException {
        if (!key.isValid()) {
            return;
        }
        SocketChannel sc = (SocketChannel) key.channel();
        if (key.isConnectable()) {
            if (sc.finishConnect()) {
                sc.register(selector, SelectionKey.OP_READ);
                writeResponse(sc);
            } else {
                System.exit(1); // 连接失败，进程退出
            }
        }
        
        if (key.isReadable()) {
            ByteBuffer readBuffer = ByteBuffer.allocate(1024);
            int readBytes = sc.read(readBuffer);
            if (readBytes > 0) {
                readBuffer.flip();
                byte[] bytes = new byte[readBuffer.remaining()];
                readBuffer.get(bytes);
                String body = new String(bytes, StandardCharsets.UTF_8);
                System.out.println("Now is: " + body);
                this.stop = true;
            } else if (readBytes < 0) {
                key.cancel();
                sc.close();
            } else {
                // 读到0字节，忽略
            }
        }

    }

    /**
     * 连接服务器
     */
    private void connectServer() throws IOException {
        // 如果直接连接成功，则注册到多路复用器上，发送请求消息，读应答
        if (sc.connect(new InetSocketAddress(host, port))) {
            sc.register(selector, SelectionKey.OP_READ);
            writeResponse(sc);
        } else {
            sc.register(selector, SelectionKey.OP_CONNECT);
        }
    }

    /**
     * 发送请求到服务器端
     *
     * @param sc
     */
    private void writeResponse(SocketChannel sc) {
        byte[] bytes = "QUERY TIME ORDER".getBytes(StandardCharsets.UTF_8);
        ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
        writeBuffer.put(bytes);
        writeBuffer.flip();
        try {
            sc.write(writeBuffer);
        } catch (IOException e) {
            throw new RuntimeException("发送消息失败，错误信息: " + e.getLocalizedMessage());
        }
        if (!writeBuffer.hasRemaining()) {
            System.out.println("发送消息到服务器成功!");
        }
    }
}
