package workqueues;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;
import consumeUtils.ConnectionUtil;

/**
 * @Author: wlp
 * @Description: 消费者3
 * @Date: Created in 1:31 2022/4/3
 * @Modified By;
 */
public class Consumer3 {
    public static void main(String[] args) throws Exception{
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
        DeliverCallback deliverCallback=(consumerTag, delivery)->{
            String receivedMessage = new String(delivery.getBody());
            System.out.println("接收到消息:"+receivedMessage);
        };
        CancelCallback cancelCallback=(consumerTag)->{
            System.out.println(consumerTag+"消费者取消消费接口回调逻辑");
        };
        System.out.println("C 消费者启动等待消费......");
        channel.basicConsume(Producer.QUEUE_NAME,true,deliverCallback,cancelCallback);
    }
}
