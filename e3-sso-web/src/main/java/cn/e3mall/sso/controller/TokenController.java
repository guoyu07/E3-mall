package cn.e3mall.sso.controller;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.sso.service.TokenService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.awt.*;

/**
 * token的Controller
 * Created by cxq on 2018/1/28.
 */
@Controller
public class TokenController {

    @Autowired
    private TokenService tokenService;

    @RequestMapping(value = "/user/token/{token}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String getUserByToken(@PathVariable String token,String callback){

        E3Result e3Result = tokenService.getUserByToken(token);
        String json = JsonUtils.objectToJson(e3Result);
        //响应之前判断是否是jsonp请求
        if(StringUtils.isNotBlank(callback)){
            //把结果封装成一个js语句响应
            return callback + "(" + json + ");";
        }
        return json;
    }
}
