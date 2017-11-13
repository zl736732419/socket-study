package com.zheng.socket.serializes;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * @Author zhenglian
 * @Date 2017/11/13 14:25
 */
public class User implements Serializable {
    private Integer userId;
    private String username;
    
    

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    public byte[] codec() {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        byte[] bytes = this.username.getBytes(StandardCharsets.UTF_8);
        buffer.putInt(bytes.length);
        buffer.put(bytes);
        buffer.putInt(this.userId);
        buffer.flip();
        
        byte[] result = new byte[buffer.remaining()];
        buffer.get(result);
        return result;
    }
}
