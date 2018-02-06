package cn.e3mall.search.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 全局异常处理器
 * Created by cxq on 2017/12/27.
 */
public class GlobalExceptionResolve implements HandlerExceptionResolver{

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionResolve.class);

    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handel, Exception e) {
        // 打印控制台
        e.printStackTrace();
        // 写日志
        LOGGER.debug("测试日志输出==============================");

        LOGGER.info("系统发生异常==============================");

        LOGGER.error("系统发生异常", e);
        // 发邮件，发短信，使用jmail工具包发邮件.  发短信用webService第三方

        // 显示错误页面
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("error/exception");
        return modelAndView;
    }
}
