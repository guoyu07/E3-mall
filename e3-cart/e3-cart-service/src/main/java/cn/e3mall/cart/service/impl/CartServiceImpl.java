package cn.e3mall.cart.service.impl;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * cart service
 * Created by cxq on 2018/1/30.
 */
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private JedisClient jedisClient;

    @Value("${REDIS_CART_PRE}")
    private String REDIS_CART_PRE;

    @Autowired
    private TbItemMapper itemMapper;

    @Override
    public E3Result addCart(long userId, long itemId, int num) {
        //使用redis中hash key：用户id field:商品Id value：商品信息
        Boolean hexists = jedisClient.hexists(REDIS_CART_PRE + ":" + userId, itemId + "");
        //判断商品是否存在
        if (hexists) {
            String json = jedisClient.hget(REDIS_CART_PRE + ":" + userId, itemId + "");
            TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
            tbItem.setNum(tbItem.getNum() + num);
            //写入redis
            jedisClient.hset(REDIS_CART_PRE + ":" + userId, itemId + "", JsonUtils.objectToJson(tbItem));
            return E3Result.ok();
        }
        //不存在查询商品添加进去
        TbItem tbItem = itemMapper.selectByPrimaryKey(itemId);
        tbItem.setNum(num);
        String image = tbItem.getImage();
        if (StringUtils.isNotBlank(image)) {
            tbItem.setImage(image.split(",")[0]);
        }
        //添加到购物车列表
        jedisClient.hset(REDIS_CART_PRE + ":" + userId, itemId + "", JsonUtils.objectToJson(tbItem));
        return E3Result.ok();
    }

    @Override
    public E3Result mergeCart(long userId, List<TbItem> itemsList) {

        for (TbItem tbItem : itemsList) {
            addCart(userId, tbItem.getId(), tbItem.getNum());
        }
        return E3Result.ok();
    }

    @Override
    public List<TbItem> getCartList(long userId) {
        //根据用户id查询购物车列表
        List<String> jsonList = jedisClient.hvals(REDIS_CART_PRE + ":" + userId);
        List<TbItem> itemList = new ArrayList<>();
        for (String json : jsonList) {
            TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
            itemList.add(tbItem);
        }
        return itemList;
    }

    @Override
    public E3Result updateCart(long userId, long itemId, int num) {
        // get items from redis
        String json = jedisClient.hget(REDIS_CART_PRE + ":" + userId, itemId + "");
        // update items
        TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
        if(tbItem != null){
            tbItem.setNum(num);
            jedisClient.hset(REDIS_CART_PRE + ":" + userId, itemId + "", JsonUtils.objectToJson(tbItem));
        }
        return E3Result.ok();
    }

    @Override
    public E3Result deleteCart(long userId, long itemId) {
        //delete cartItem
        jedisClient.hdel(REDIS_CART_PRE + ":" + userId, itemId + "");
        return E3Result.ok();
    }

    @Override
    public E3Result clearCartItem(long userId) {
        jedisClient.del(REDIS_CART_PRE + ":" + userId);
        return E3Result.ok();
    }
}
