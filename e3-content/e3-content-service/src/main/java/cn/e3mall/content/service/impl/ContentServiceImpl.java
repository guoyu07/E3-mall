package cn.e3mall.content.service.impl;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.content.service.ContentService;
import cn.e3mall.mapper.TbContentMapper;
import cn.e3mall.pojo.TbContent;
import cn.e3mall.pojo.TbContentExample;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

/**
 * Created by cxq on 2017/12/15.
 */
@Service
public class ContentServiceImpl implements ContentService{
    @Autowired
    private TbContentMapper tbContentMapper;

    @Autowired
    private JedisClient jedisClient;

    @Value("CONTENT_LIST")
    private String CONTENT_LIST;


    @Override
    public E3Result addContent(TbContent content) {

        content.setCreated(new Date());
        content.setUpdated(new Date());
        tbContentMapper.insert(content);
        //实现缓存同步，更新谁就删谁
        jedisClient.hdel(CONTENT_LIST,content.getCategoryId().toString());
        return E3Result.ok();
    }

    @Override
    public List<TbContent> getContentByCid(long cid) {
        //查询缓存，
        try{
            // 如果缓存中有直接相应结果，
            String json = jedisClient.hget(CONTENT_LIST, cid + "");
            if(StringUtils.isNotBlank(json)){
                List<TbContent> list = JsonUtils.jsonToList(json, TbContent.class);
                return list;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        // 如果没有查询数据库把结果添加到缓存之后再返回结果。
        TbContentExample example = new TbContentExample();
        example.createCriteria().andCategoryIdEqualTo(cid);
        List<TbContent> tbContents = tbContentMapper.selectByExampleWithBLOBs(example);
        try{
            jedisClient.hset(CONTENT_LIST,cid + "", JsonUtils.objectToJson(tbContents));
        }catch (Exception e){
            e.printStackTrace();
        }
        return tbContents;
    }
}
