package cn.e3mall.sso.service.impl;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.mapper.TbUserMapper;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.pojo.TbUserExample;
import cn.e3mall.sso.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;
import java.util.UUID;

/**
 * LoginService 用户登录处理
 * Created by cxq on 2018/1/28.
 */
@Service
public class LoginServiceImpl implements LoginService{

    @Autowired
    private TbUserMapper tbUserMapper;

    @Autowired
    private JedisClient jedisClient;

    @Value("${SESSION_EXPIRE}")
    private Integer SESSION_EXPIRE;

    @Override
    public E3Result userLogin(String userName, String password) {
        TbUserExample example = new TbUserExample();

        example.createCriteria().andUsernameEqualTo(userName);

        List<TbUser> tbUsers = tbUserMapper.selectByExample(example);

        if(tbUsers == null || tbUsers.size() == 0){
            return E3Result.build(400,"用户名或者密码错误");
        }
        //加密password对比md5加密后的密码
        TbUser user = tbUsers.get(0);
        if(!DigestUtils.md5DigestAsHex(password.getBytes()).equals(user.getPassword())){
            return E3Result.build(400,"用户名或者密码错误");
        }
        //以下登陆成功的操作
        String token = UUID.randomUUID().toString();
        //把用户信息写到redis里面去，  key：token value:y用户信息
        user.setPassword(null);
        jedisClient.set("SESSION:" + token, JsonUtils.objectToJson(user));
        jedisClient.expire("SESSION:" + token, SESSION_EXPIRE);
        //返回token
        return E3Result.ok(token);
    }
}
