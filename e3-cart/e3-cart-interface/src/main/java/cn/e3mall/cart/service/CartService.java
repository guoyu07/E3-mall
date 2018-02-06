package cn.e3mall.cart.service;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.pojo.TbItem;

import java.util.List;

/**
 * 购物车服务
 * Created by cxq on 2018/1/30.
 */
public interface CartService {

    E3Result addCart(long userId, long itemId, int num);

    E3Result mergeCart(long userId, List<TbItem> itemsList);

    List<TbItem> getCartList(long userId);

    E3Result updateCart(long userId, long itemId, int num);

    E3Result deleteCart(long userId, long itemId);

    E3Result clearCartItem(long userId);
}
