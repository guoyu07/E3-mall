package cn.e3mall.sso.service;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.pojo.TbUser;

/**
 * Created by cxq on 2018/1/27.
 */
public interface RegisterService {

    E3Result checkData(String param,int type);

    E3Result createUser(TbUser user);


}
