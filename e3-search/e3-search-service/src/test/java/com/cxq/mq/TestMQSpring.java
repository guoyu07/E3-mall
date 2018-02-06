package com.cxq.mq;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by cxq on 2017/12/27.
 */
public class TestMQSpring {
    @Test
    public void testConsumer() throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-activemq.xml");

        System.in.read();






    }
}
