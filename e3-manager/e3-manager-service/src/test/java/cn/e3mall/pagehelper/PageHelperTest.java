package cn.e3mall.pagehelper;

import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemExample;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

/**
 * Created by cxq on 2017/10/8.
 */
public class PageHelperTest {
    @Test
    public void testPageHelper(){
        //初始化spring容器
        ApplicationContext applicationContext =
                new ClassPathXmlApplicationContext("classpath:spring/applicationContext-dao.xml");
        //从容器中获取Mapper的代理对象
        TbItemMapper itemMapper = applicationContext.getBean(TbItemMapper.class);
        //设置分页信息，使用PageHelper的静态方法，然后执行查询
        PageHelper.startPage(1,10);
        TbItemExample example = new TbItemExample();
        List<TbItem> items = itemMapper.selectByExample(example);
        //取分页信息PageInfo。1.总记录数；2.总页数；3.当前页码；等
        PageInfo<TbItem> pageInfo = new PageInfo<TbItem>(items);
        System.out.println(pageInfo.getTotal());
        System.out.println(pageInfo.getPages());
        System.out.println(items.size());



    }
}
