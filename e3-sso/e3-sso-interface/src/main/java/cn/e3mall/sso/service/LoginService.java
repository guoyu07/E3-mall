package cn.e3mall.sso.service;

import cn.e3mall.common.utils.E3Result;

/**
 * Created by cxq on 2018/1/28.
 */


public interface LoginService {
    /**
     * 用户登录
     * @param userName 用户名
     * @param password 密码
     * @return 返回包含token信息
     */
    E3Result userLogin(String userName, String password);
}
