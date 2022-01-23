package com.aikele.client;


import com.aikele.utils.ConnectionUtil;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitmqClient {
    public static void main(String[] args) {
        Connection connection = null;
        Channel channel = null;
        try {
            // 1、获取到连接
            connection = ConnectionUtil.getConnection();
            // 2、从连接中创建通道，使用通道才能完成消息相关的操作
            channel = connection.createChannel();
            // 3、声明（创建）队列
            //参数：String queue, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments
            /**
             * 参数明细
             * 1、queue 队列名称
             * 2、durable 是否持久化，如果持久化，mq重启后队列还在
             * 3、exclusive 是否独占连接，队列只允许在该连接中访问，如果connection连接关闭队列则自动删除,如果将此参数设置true可用于临时队列的创建
             * 4、autoDelete 自动删除，队列不再使用时是否自动删除此队列，如果将此参数和exclusive参数设置为true就可以实现临时队列（队列不用了就自动删除）
             * 5、arguments 参数，可以设置一个队列的扩展参数，比如：可设置存活时间
             */
            channel.queueDeclare("first_queue",false,false,false,null);

            // 定义队列的消费者
            DefaultConsumer consumer = new DefaultConsumer(channel){
                // 获取消息，并且处理，这个方法类似事件监听，如果有消息的时候，会被自动调用
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    //交换机
                    String exchange = envelope.getExchange();
                    //消息id，mq在channel中用来标识消息的id，可用于确认消息已接收
                    long deliveryTag = envelope.getDeliveryTag();
                    // body 即消息体
                    String msg = new String(body,"utf-8");
                    System.out.println(" received : " + msg );
                }
            };
            // 定义队列的消费者 手动确认ack
            DefaultConsumer ackConsumer = new DefaultConsumer(channel){
                // 获取消息，并且处理，这个方法类似事件监听，如果有消息的时候，会被自动调用
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    //交换机
                    String exchange = envelope.getExchange();
                    //消息id，mq在channel中用来标识消息的id，可用于确认消息已接收
                    long deliveryTag = envelope.getDeliveryTag();
                    // body 即消息体
                    String msg = new String(body,"utf-8");
                    System.out.println(" received : " + msg );
                    // 手动进行ACK
                    /*
                     *  void basicAck(long deliveryTag, boolean multiple) throws IOException;
                     *  deliveryTag:用来标识消息的id
                     *  multiple：是否批量.true:将一次性ack所有小于deliveryTag的消息。
                     */
                    this.getChannel().basicAck(envelope.getDeliveryTag(),false);
                }
            };
            // 监听队列，第二个参数：是否自动进行消息确认。
            //参数：String queue, boolean autoAck, Consumer callback
            /**
             * 参数明细：
             * 1、queue 队列名称
             * 2、autoAck 自动回复，当消费者接收到消息后要告诉mq消息已接收，如果将此参数设置为tru表示会自动回复mq，如果设置为false要通过编程实现回复
             * 3、callback，消费方法，当消费者接收到消息要执行的方法
             */
            //自动确认
           // channel.basicConsume("first_queue",true,consumer);
            //手动确认
            channel.basicConsume("first_queue",false,ackConsumer);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}
