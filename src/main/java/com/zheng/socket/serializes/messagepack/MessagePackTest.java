package com.zheng.socket.serializes.messagepack;

import org.msgpack.MessagePack;
import org.msgpack.template.Templates;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author zhenglian
 * @Date 2017/11/13 14:54
 */
public class MessagePackTest {
    
    public static void main(String[] args) throws IOException {

        List<String> list = new ArrayList<>();
        list.add("zhangsan");
        list.add("lisi");
        list.add("wangwu");
        list.add("zhaoliu");

        MessagePack pack = new MessagePack();
        // serialize list
        byte[] bytes = pack.write(list);
        System.out.println(bytes.length);
        List<String> result = pack.read(bytes, Templates.tList(Templates.TString));
        System.out.println(result);

    }
}
