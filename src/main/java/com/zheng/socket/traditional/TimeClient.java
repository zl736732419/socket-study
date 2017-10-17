package com.zheng.socket.traditional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

/**
 * @Author zhenglian
 * @Date 2017/10/17 15:29
 */
public class TimeClient {
    
    private static final String host = "127.0.0.1";
    private static final Integer port = 8000;
    
    public static void main(String[] args) throws Exception {
        TimeClient client = new TimeClient();
        client.start();
    }

    private void start() {
        Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(host, port));
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedReader reader = null;
        PrintWriter writer = null;
        try {
            InputStream input = socket.getInputStream();
            OutputStream output = socket.getOutputStream();
            reader = new BufferedReader(new InputStreamReader(input));
            writer = new PrintWriter(output);

            String line;
            String response;
            while (true) {
                System.out.println("请输入您要发送的消息：");
                Scanner scanner = new Scanner(System.in);
                line = scanner.nextLine();
                if (line.equals("bye")) {
                    break;
                }
                writer.println(line); // 发送给服务器
                writer.flush();
                response = reader.readLine();
                System.out.println("响应： " + response);
            }
            System.out.println("bye...");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
                writer.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
