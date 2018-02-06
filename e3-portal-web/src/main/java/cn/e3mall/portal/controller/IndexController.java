package cn.e3mall.portal.controller;

import cn.e3mall.content.service.ContentService;
import cn.e3mall.pojo.TbContent;
import org.jboss.netty.handler.ipfilter.CIDR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Created by cxq on 2017/12/10.
 */
@Controller

public class IndexController {

    @Value("${CONTENT_LUO_BO}")
    private Long CONTENT_LUO_BO;

    @Autowired
    private ContentService contentService;

    @RequestMapping("/index")
    public String showIndex(Model model) {
        //查询内容列表
        List<TbContent> ad1List = contentService.getContentByCid(CONTENT_LUO_BO);
        model.addAttribute("ad1List",ad1List);
        return "index";
    }


}
