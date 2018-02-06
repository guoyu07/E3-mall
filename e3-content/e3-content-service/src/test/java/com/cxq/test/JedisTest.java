package com.cxq.test;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Created by cxq on 2017/12/15.
 */
public class JedisTest {

    @Test
    public void testJedis() throws Exception{
        //创建一个连接jedis的对象，参数是port还有host
        Jedis jedis = new Jedis("192.168.25.129", 6379);
        //直接使用jedis的方法操作redis
        jedis.set("set1","213");
        String s = jedis.get("set1");
        System.out.println(s);
        //关闭连接,
        jedis.close();
    }

    @Test
    public void testJedisPool() throws Exception{
        //创建一个连接jedis的对象，参数是port还有host
        JedisPool jedisPool = new JedisPool("192.168.25.129", 6379);
        Jedis jedis = jedisPool.getResource();
        //直接使用jedis的方法操作redis
        jedis.set("set2","213");
        String s = jedis.get("set2");
        System.out.println(s);
        //关闭连接,
        jedis.close();
        jedisPool.close();
    }



}
