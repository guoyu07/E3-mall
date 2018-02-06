package cn.e3mall.sso.service;

import cn.e3mall.common.utils.E3Result;

/**
 * 根据token查询用户信息
 * Created by cxq on 2018/1/28.
 */
public interface TokenService {

    E3Result getUserByToken(String token);
}
