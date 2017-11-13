package com.zheng.socket.serializes.messagepack;

import com.zheng.socket.serializes.User;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * @Author zhenglian
 * @Date 2017/11/13 15:48
 */
public class MessagePackClientHandler extends ChannelHandlerAdapter {

    private int sendNumber;
    
    public MessagePackClientHandler(int sendNumber) {
        this.sendNumber = sendNumber;
    }
    
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        User[] users = userInfos();
        for (User user : users) {
            ctx.write(user);
        }
        ctx.flush();
    }
    
    private User[] userInfos() {
        User[] users = new User[sendNumber];
        for (int i = 0; i < users.length; i++) {
            User user =new User();
            user.setUserId(i);
            user.setUsername("user" + i);
            users[i] = user;
        }
        
        return users;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("recieve response: " + msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
