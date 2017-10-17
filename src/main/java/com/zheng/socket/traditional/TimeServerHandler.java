package com.zheng.socket.traditional;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

/**
 * @Author zhenglian
 * @Date 2017/10/17 14:48
 */
public class TimeServerHandler implements Runnable {
    private Socket socket;
    
    public TimeServerHandler(Socket socket) {
        this.socket = socket;
    }
    
    @Override
    public void run() {
        BufferedReader reader = null;
        PrintWriter writer = null;
        try {
            InputStream input = socket.getInputStream();
            OutputStream output = socket.getOutputStream();
            reader = new BufferedReader(new InputStreamReader(input));
            writer = new PrintWriter(output);
            String line = null;
            while(true) {
                line = reader.readLine();
                if (StringUtils.isEmpty(line) || Objects.equals(line, "bye")) {
                    break;
                }
                System.out.println("server receive client: " + line);
                String response = "hello,";
                if (Objects.equals("get time", line)) {
                    response = new SimpleDateFormat("yyyy-mm-dd HH:MM:ss").format(new Date());
                }
                writer.println(response);
                writer.flush();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }finally {
            if(Optional.ofNullable(reader).isPresent()) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (Optional.ofNullable(writer).isPresent()) {
                writer.close();
            }
            if (Optional.ofNullable(socket).isPresent()) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
