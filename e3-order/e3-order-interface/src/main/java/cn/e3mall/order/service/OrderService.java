package cn.e3mall.order.service;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.order.pojo.OrderInfo;

/**
 * Created by cxq on 2018/1/31.
 */
public interface OrderService {
    E3Result createOrder(OrderInfo orderInfo);
}
