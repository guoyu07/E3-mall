package cn.e3mall.activemq;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;


/**
 * Created by cxq on 2017/12/27.
 */
public class TestActiveMqSpring {

    @Test
    public void testSend(){
        //初始化spring容器
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-activemq.xml");

        // 从容器里面获得JmsTemplate
        JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);
        // 从容器中获得Destination对象
        Destination destination = (Destination)context.getBean("queueDestination");
        // 发送消息
        jmsTemplate.send(destination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage("send activemq message");
            }
        });
    }
}
