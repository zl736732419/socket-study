package com.zheng.socket.serializes;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

/**
 * 序列化选择的几个考虑因素：
 * 1. 是否支持跨语言
 * 2. 编码的大小
 * 3. 编码的性能
 * 4. api是否方便
 * 5. 手工开发的工作量
 * 
 * @Author zhenglian
 * @Date 2017/11/13 14:28
 */
public class MainApp {
    public static void main(String[] args) throws Exception {
        User user = new User();
        user.setUserId(1);
        user.setUsername("xiaolian");

        ByteArrayOutputStream result = new ByteArrayOutputStream();
        ObjectOutputStream output = new ObjectOutputStream(result);
        output.writeObject(user);
        
        byte[] bytes = result.toByteArray();
        System.out.println(bytes.length); // 204
        
        byte[] bytes2 = user.codec();
        System.out.println(bytes2.length); // 16
    }
}
