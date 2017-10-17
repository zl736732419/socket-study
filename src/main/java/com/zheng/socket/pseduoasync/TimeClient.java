package com.zheng.socket.pseduoasync;

import com.zheng.socket.constants.Constants;

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
    public static void main(String[] args) throws Exception {
        TimeClient client = new TimeClient();
        client.start();
    }

    private void start() {
        Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(Constants.HOST, Constants.PORT));
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedReader reader = null;
        PrintWriter writer = null;
        try {
            OutputStream output = socket.getOutputStream();
            InputStream input = socket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(input));
            writer = new PrintWriter(output);

            String line;
            while (true) {
                System.out.println("请输入您要发送的消息：");
                Scanner scanner = new Scanner(System.in);
                line = scanner.nextLine();
                if (line.equals("bye")) {
                    break;
                }
                writer.println(line); // 发送给服务器
                writer.flush();
                System.out.println("响应： " + reader.readLine());
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
