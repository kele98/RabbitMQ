package com.aikele.utils;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

//rabbitmq工具类
public class ConnectionUtil {
    public static Connection getConnection() throws IOException, TimeoutException {
        //定义连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //设置服务地址
        factory.setHost("a.aikele.top");
        //端口
        factory.setPort(5672);
        //设置账号信息，用户名、密码、vhost
       // factory.setVirtualHost("/kavito");//设置虚拟机，一个mq服务可以设置多个虚拟机，每个虚拟机就相当于一个独立的mq
        factory.setUsername("rabbit");
        factory.setPassword("rabbit");
        // 通过工厂获取连接
        Connection connection = factory.newConnection();
        return connection;
    }
}
