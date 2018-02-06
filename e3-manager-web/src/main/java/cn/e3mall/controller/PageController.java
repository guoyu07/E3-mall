package cn.e3mall.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by cxq on 2017/10/8.
 */
@Controller
public class PageController {
    @RequestMapping("/")
    public String index(){
        return "index";
    }
    @RequestMapping("/{page}")
    public String page(@PathVariable String page){
        return page;
    }
}
