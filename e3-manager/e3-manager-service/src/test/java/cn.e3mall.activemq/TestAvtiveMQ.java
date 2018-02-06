package cn.e3mall.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.junit.Test;

import javax.jms.*;

/**
 * Created by cxq on 2017/12/26.
 */
public class TestAvtiveMQ {
    /**
     * 点到点发布消息
     */
    @Test
    public void testQueueProducer() throws JMSException {
        //创建连接工厂对象，需要ip和端口
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.131:61616");
        // 使用工厂创建连接
        Connection connection = connectionFactory.createConnection();
        // 开启连接，调用connection的start方法
        connection.start();
        // 创建session参数：(是否开启事务，应答模式)开启事务，第二个参数无意义：手动应答和自动应答一般是自动应答
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        // 使用session创建一个destination对象，两种形式queue。topic此案在使用queue
        Queue testQueue = session.createQueue("testQueue");
        // 使用session创建一个producer对象
        MessageProducer producer = session.createProducer(testQueue);
        // 创建message对象，可以使用TestMessage
        TextMessage textMessage = new ActiveMQTextMessage();
        textMessage.setText("Hello ActiveMQ");
//        session.createTextMessage();
        // 发送消息
        producer.send(textMessage);
        // 关闭对象
        producer.close();
        session.close();
        connection.close();
    }

    @Test
    public void textQueueClient() throws Exception {
        //创建连接工厂对象，需要ip和端口
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.131:61616");
        // 使用工厂创建连接
        Connection connection = connectionFactory.createConnection();
        // 开启连接，调用connection的start方法
        connection.start();
        // 创建session参数：(是否开启事务，应答模式)开启事务，第二个参数无意义：手动应答和自动应答一般是自动应答
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        // 使用session创建一个destination对象，两种形式queue。topic此案在使用queue
        Queue testQueue = session.createQueue("spring-queue");
        //创建消费者
        MessageConsumer consumer = session.createConsumer(testQueue);
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                TextMessage textMessage = (TextMessage)message;
                try {
                    System.out.println(textMessage.getText());
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });
        //等待就收消息
        System.in.read();
        //关闭资源
        consumer.close();
        session.close();
        connection.close();

    }

    @Test
    public void textodd(){
        int x = 2;
        int y = 4;
        int z = 5;
        y*= z-- / ++x;
        System.out.println(y);
    }
}