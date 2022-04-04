package consumeUtils;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Author: wlp
 * @Description: 创建连接工具类
 * @Date: Created in 14:26 2022/3/8
 * @Modified By;
 */
public class ConnectionUtil {
    public static Connection getConnection() throws IOException, TimeoutException {
        //     1 创建连接工厂
        ConnectionFactory connectionFactory=new ConnectionFactory();
        connectionFactory.setHost("192.168.217.132");
        connectionFactory.setPort(5672);
        //虚拟主机名称;默认为
        //连接用户名，密码；默认为guest
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
//         创建连接
        return connectionFactory.newConnection();
    }
}
