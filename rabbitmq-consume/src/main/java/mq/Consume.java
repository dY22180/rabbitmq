package mq;

import com.rabbitmq.client.*;
import consumeUtils.ConnectionUtil;

import java.io.IOException;

/**
 * @Author: wlp
 * @Description: 接收者，用来接收mq发送过来的数据
 * @Date: Created in 14:21 2022/3/8
 * @Modified By;
 */
public class Consume {
    static  final String QUEUE_NAME="holle.rabbitmq";
    public static void main(String[] args)  throws  Exception{
        Connection connection= ConnectionUtil.getConnection();
//         创建channel（信道）
        Channel channel=connection.createChannel();
//      Chnanel 接口实现的类是AutorecoveringConnection，在AutorecoveringConnection类中调用queueDeclare()方法来创建队列
        /**
         *  创建队列
         *  参数1:队列名称
         *  参数2：是否定义持久化
         *  参数3：该队列是否进行共享  true 可以多个消费者消费
         *  参数4：是否自动删除  true 自动删除
         *  参数5：其他参数
         */
        channel.queueDeclare("", true, true, true, null);
//         消费者发送消息
//         创建接收消息者时通过Consumer接口实现handleDelivery()方法，
//        或者通过Consumer的实现类来DefaultConsumer来完成
        Consumer  consumer= new DefaultConsumer (channel) {
            /**
             * 当消息被接收到进行回调,执行改方法
             * @param consumerTag 消息者标签
             * @param envelope 消费包内容
             * @param properties 属性消息
             * @param body 内容
             * @throws IOException
             */
           @Override
            public void handleDelivery(String consumerTag,
                                       Envelope envelope,
                                       AMQP.BasicProperties properties,
                                       byte[] body) throws IOException {
                //路由key
                System.out.println("路由key为：" + envelope.getRoutingKey());
                //交换机
                System.out.println("交换机为：" +envelope.getExchange());
                //消息id
                System.out.println("消息id为：" + envelope.getDeliveryTag());
                //收到的消息
                System.out.println("接收到的消息为：" +new String(body,"utf-8"));
            }
        };
        /**
         * 消费者消费消息
         * 1 消息队列
         * 2 是否自动确认,为true为表示消息接收到自动向mq回复接收到了，mq接收到回复会删除消息,设置为false则需要手动确认
         * 3 消费者未成功消费的回调
         */
        channel.basicConsume(QUEUE_NAME,false,consumer);
        //不关闭资源，会一直监听消息
//        channel.close();
//        connection.close();
        /**
         *   接收者接收消息流程
         * 1 创建连接(Connetion) 开启一个信道(Channel) ,连接到RabbitMQ
         * 2 向Broker(服务节点) 请求队列中的消息,设置回调函数
         * 3 等待Borker回应改队列的消息，消费者成功接收到消息
         * 4 ack 自动确认接收到的消息
         * 5 从RabbitmQ队列中删除相对应的消息
         * 6 关闭信道，关闭连接
         *
         */
    }
}
