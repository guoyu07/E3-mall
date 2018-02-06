package cn.e3mall.sso.service.impl;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.TokenService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 根据token查询用户信息实现类
 * Created by cxq on 2018/1/28.
 */
@Service
public class TokenServiceImpl implements TokenService{

    @Autowired
    private JedisClient jedisClient;

    @Value("${SESSION_EXPIRE}")
    private Integer SESSION_EXPIRE;

    @Override
    public E3Result getUserByToken(String token) {

        // 根据token查询redis的用户信息
        String json = jedisClient.get("SESSION:" + token);
        if (StringUtils.isBlank(json)) {
            // 没有：登陆过期，返回登录页面
            return E3Result.build(201,"用户登录已经过期");
        }
        // 有：更新token的过期时间，返回用户信息
        TbUser tbUser = JsonUtils.jsonToPojo(json, TbUser.class);
        jedisClient.expire("SESSION:" + token, SESSION_EXPIRE);

        return E3Result.ok(tbUser);
    }
}
