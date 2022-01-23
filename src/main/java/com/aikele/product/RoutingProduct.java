package com.aikele.product;

import com.aikele.utils.ConnectionUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class RoutingProduct {
    public static void main(String[] args) {
        Connection connection = null;
        Channel channel = null;
        try {
            // 1、获取到连接
            connection = ConnectionUtil.getConnection();
            // 2、从连接中创建通道，使用通道才能完成消息相关的操作
            channel = connection.createChannel();
            // 声明exchange，指定类型为direct
            channel.exchangeDeclare("first_exchange", BuiltinExchangeType.TOPIC);
            // 发送消息，并且指定routing key为：quick.orange.rabbit
            channel.basicPublish("first_exchange","a.quick.orange.rabbit",null,"注册成功".getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }finally {
            if(channel!=null){
                try {
                    channel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
            }
            if(connection!=null){
                try {
                    connection.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        }
    }
}
