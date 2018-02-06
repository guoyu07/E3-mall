package cn.e3mall.order.interceptor;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.utils.CookieUtils;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.TokenService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录处理拦截器
 * Created by cxq on 2018/1/30.
 */

public class LoginInterceptor implements HandlerInterceptor{

    @Autowired
    private TokenService tokenService;

    @Autowired
    private CartService cartService;

    @Value("${SSO_URL}")
private String SSO_URL;
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) throws Exception {
        //true is interceptor
//        从cookie取token，判断token是否为登录状态。
        String token = CookieUtils.getCookieValue(httpServletRequest, "token");
        if(StringUtils.isBlank(token)) {
            httpServletResponse.sendRedirect(SSO_URL +"/page/login?redirect=" + httpServletRequest.getRequestURI());
            //拦截
            return false;
        }
        E3Result result = tokenService.getUserByToken(token);
        if(result.getStatus() != 200){
            httpServletResponse.sendRedirect(SSO_URL +"/page/login?redirect=" + httpServletRequest.getRequestURI());
            return false;
        }
        TbUser user = (TbUser) result.getData();

        httpServletRequest.setAttribute("user",user);

        String json = CookieUtils.getCookieValue(httpServletRequest, "cart", true);

        if(StringUtils.isNotBlank(json)){
            //合并购物车
            cartService.mergeCart(user.getId(), JsonUtils.jsonToList(json, TbItem.class));
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        //
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        //可以处理异常
    }
}
