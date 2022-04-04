package workqueues;

import com.rabbitmq.client.*;
import consumeUtils.ConnectionUtil;

import java.io.IOException;

/**
 * @Author: wlp
 * @Description: 消费者1
 * @Date: Created in 0:18 2022/4/3
 * @Modified By;
 */
public class Consumer1 {
    public static void main(String[] args) throws  Exception{
        //  //创建连接
        Connection connection = ConnectionUtil.getConnection();
        // 创建频道
        Channel channel = connection.createChannel();
        /**
         *  创建队列
         * 参数1：队列名称
         * 参数2：是否定义持久化
         * 参数3：队列是否共享
         * 参数4：是否删除队列
         * 参数5：队列其它参数
         */
        channel.queueDeclare(Producer.QUEUE_NAME,true,false,false,null);
        // 主要 work模式只能一次性接收消息并处理一条消息
        channel.basicQos(1);
        // 创建消费者，并设置消息处理
        DefaultConsumer consumer = new DefaultConsumer(channel) {

            /**
             *  消息接收到消息，执行此方法，进行回调
             * @param consumerTag  消息者标签
             * @param envelope  消费包内容
             * @param properties 属性消息
             * @param body 消息
             * @throws IOException
             */
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
               try {
                   //路由key
                   System.out.println("路由key为：" + envelope.getRoutingKey());
                   //交换机
                   System.out.println("交换机为：" + envelope.getExchange());
                   //消息id
                   System.out.println("消息id为：" + envelope.getDeliveryTag());
                   //收到的消息
                   System.out.println("消费者1-接收到的消息为：" + new String(body, "utf-8"));
               }catch (Exception e) {
                   e.printStackTrace();
               }
            }
        };
        // 消息接收取消时，执行改方法
        CancelCallback cancelCallback=(consumerTag)-> {
            System.out.println(consumerTag + "消费者取消消费接口回调逻辑");
        };
        /**
         *  接收机接收消息
         *  参数1：队列名称
         *  参数2：是否自动确认  为true为表示消息接收到自动向mq回复接收到了，mq接收到回复会删除消息,设置为false则需要手动确认
         *  参数3：消息接收到后回调
         *  参数4: 消息接收取消时回调
         */
        channel.basicConsume(Producer.QUEUE_NAME,false,consumer);
    }

}
