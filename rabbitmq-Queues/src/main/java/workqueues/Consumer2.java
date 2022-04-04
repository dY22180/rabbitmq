package workqueues;

import com.rabbitmq.client.*;
import consumeUtils.ConnectionUtil;

import java.io.IOException;

/**
 * @Author: wlp
 * @Description: 消费者2
 * @Date: Created in 0:31 2022/4/3
 * @Modified By;
 */
public class Consumer2 {
    public static void main(String[] args) throws  Exception{
        //创建连接
        Connection connection = ConnectionUtil.getConnection();
        // 创建频道
        Channel channel = connection.createChannel();
        /**
         * 声明（创建）队列
         * 参数1：队列名称
         * 参数2：是否定义持久化队列
         * 参数3：是否独占本次连接
         * 参数4：是否在不使用的时候自动删除队列
         * 参数5：队列其它参数
         */
        channel.queueDeclare(Producer.QUEUE_NAME,true,false,false,null);
        // 一次只能接收一次消息
        channel.basicQos(1);
        // 创建消费者，接收消息
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            /**
             * 消费者接收到消息后，执行此方法，进行回调
             * @param consumerTag 消息者标签
             * @param envelope  消息包内容
             * @param properties  属性消息
             * @param body  内容
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
                   // 接收到的消息
                   System.out.println("消费者2-接收到的消息为"+ new String(body,"utf-8"));
                   // 先暂停执行 3000毫秒
                   Thread.sleep(3000);
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
         */
        channel.basicConsume(Producer.QUEUE_NAME,false,consumer);
    }
}
