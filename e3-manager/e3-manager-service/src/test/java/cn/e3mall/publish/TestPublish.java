package cn.e3mall.publish;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by cxq on 2017/10/13.
 */
public class TestPublish {

    @Test
    public void publishService() throws Exception {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-*.xml");
        while (true){
            Thread.sleep(1000);
        }
    }
}
