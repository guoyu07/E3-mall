package com.cxq.test;
import cn.e3mall.common.jedis.JedisClient;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by cxq on 2017/12/15.
 */
public class JedisClientTest {

    @Test
    public void testJedisClient() throws Exception{
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-redis.xml");

        JedisClient jedisClient = applicationContext.getBean(JedisClient.class);
        jedisClient.set("mytest","123");
        String mytest = jedisClient.get("mytest");
        System.out.println(mytest);

    }




}
