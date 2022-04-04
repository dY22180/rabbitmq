package mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @Author: wlp
 * @Description: rabbitmq生产者
 * @Date: Created in 11:08 2022/3/8
 * @Modified By;
 */

public class Producer {
    /**
     *  生产者发送消息流程
     *  1 生产者创建连接(Connection0,开启信道(Channel) 连接RabbitMQ,
     *  2 声明队列并设置属性（）
     *  3 生产者通过路由键将交换机与队列绑定起来
     *  4 生产者发送消息到RabbitMQ
     *  5 关闭信道(Channel)，连接(Connection)
     */
    static  final String QUEUE_NAME="holle.rabbitmq";
    public  static  void  main(String[] args) throws  Exception{
        // 1 创建连接工厂
        ConnectionFactory connectionFactory=new ConnectionFactory();
        //  2 配置主机地址，端口号,用户名，密码
        connectionFactory.setHost("192.168.217.132");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
//         3 创建连接
        Connection connection=connectionFactory.newConnection();
//        4 创建频道
        Channel channel=connection.createChannel();
        /**
         *  声明创建队列
         *  参数1:队列名称
         *  参数2：是否定义持久化
         *  参数3：该队列是否进行共享  true 可以多个消费者消费
         *  参数4：是否自动删除  true 自动删除
         *  参数5：其他参数
         */
            channel.queueDeclare(QUEUE_NAME,true,false,false,null);
//            设置 要发送的消息
        String message = "你好,大佬！";
        /**
         * 发送一个消息
         * 参数1 发送到那个交换机,简单模式默认的是 ""
         * 参数二：路由的 key 是哪个
         * 参数三： 其他的参数信息
         *  参数四：发送消息的内容
         */
        channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
        System.out.println("已发送的消息"+message);
//     6 对资源进行关闭 ,
//        channel.close();
//        connection.close();
        /**
         * 生产者小结
         *  1 创建连接工厂
         *  2 设置参数
         *  3 创建连接   Connection connection=connectionFactory.newConnection();
         *  4 创建Channel ，声明创建队列
         *  5 设置发送消息
         *  6  资源关闭
         */

    }

}
