package workqueues;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import consumeUtils.ConnectionUtil;

import java.util.Scanner;

/**
 * @Author: wlp
 * @Description:  测试work-queues(工作队列)
 * @Date: Created in 0:09 2022/4/3
 * @Modified By;
 */
public class Producer {
    static final String QUEUE_NAME = "work_queue";

    public static void main(String[] args) throws  Exception{
        //创建连接
        Connection connection = ConnectionUtil.getConnection();
        // 创建信道
        Channel channel = connection.createChannel();
//        创建队列
        /**
         * 参数1：队列名称
         * 参数2：是否定义持久化
         * 参数3：队列是否共享
         * 参数4：是否删除队列
         * 参数5：队列其它参数
         */
        channel.queueDeclare(QUEUE_NAME,true,false,false,null);
        Scanner input =new Scanner(System.in);
//        for (int i=1; i<=num; i++) {
//            // 发送消息
//            String message = "work模式 测试"+i;
//            channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
//        }
        while (input.hasNext()) {
            String message= input.next();
            channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
            System.out.println("消息发送完成");
        }

        // 关闭资源
        channel.close();
        connection.close();
    }
    // 小结：
    // work queues 多个消费端共同消费同一个队列中的消息
    // 在一个队列中如果有多个消息者，那么消费者之间是处于一个竞争的关系，但要值的注意的是生产者发送的消息，只能处理一次，
    // 不可以处理多次，所有Work Queues 常常用来处理任务过重或任务较多情况
}
