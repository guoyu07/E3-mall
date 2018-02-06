package cn.e3mall.sso.service.impl;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.mapper.TbUserMapper;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.pojo.TbUserExample;
import cn.e3mall.sso.service.RegisterService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;

/**
 * 用户注册处理
 * Created by cxq on 2018/1/27.
 */
@Service
public class RegisterServiceImpl implements RegisterService{

    @Autowired
    private TbUserMapper userMapper;


    @Override
    public E3Result checkData(String param, int type) {

// 根据不同的type生成不同的查询条件
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        //1用户名2手机号3邮箱
        if(type == 1){
            criteria.andUsernameEqualTo(param);
        }else if(type == 2){
            criteria.andPhoneEqualTo(param);
        }else if (type == 3){
            criteria.andEmailEqualTo(param);
        }else{
            return E3Result.build(400,"数据类型错误");
        }
// 执行查询
        List<TbUser> tbUsers = userMapper.selectByExample(example);
        // 判断书否包含数据
        if(tbUsers != null && tbUsers.size() > 0){
            return E3Result.ok(false);
        }
// 有false 没有true
        return E3Result.ok(true);
    }

    @Override
    public E3Result createUser(TbUser user) {
        // 1、使用TbUser接收提交的请求。

        if (StringUtils.isBlank(user.getUsername())) {
            return E3Result.build(400, "用户名不能为空");
        }
        if (StringUtils.isBlank(user.getPassword())) {
            return E3Result.build(400, "密码不能为空");
        }
        E3Result result = checkData(user.getUsername(), 1);
        if (!(boolean) result.getData()) {
            return E3Result.build(400, "此用户名已经被使用");
        }
        //校验电话是否可以
        if (StringUtils.isNotBlank(user.getPhone())) {
            result = checkData(user.getPhone(), 2);
            if (!(boolean) result.getData()) {
                return E3Result.build(400, "此手机号已经被使用");
            }
        }
        //校验email是否可用
        if (StringUtils.isNotBlank(user.getEmail())) {
            result = checkData(user.getEmail(), 3);
            if (!(boolean) result.getData()) {
                return E3Result.build(400, "此邮件地址已经被使用");
            }
        }
        // 2、补全TbUser其他属性。
        user.setCreated(new Date());
        user.setUpdated(new Date());
        // 3、密码要进行MD5加密。
        String md5Pass = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setPassword(md5Pass);
        // 4、把用户信息插入到数据库中。
        userMapper.insert(user);
        // 5、返回E3Result。
        return E3Result.ok();
    }
}
