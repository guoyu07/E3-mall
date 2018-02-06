package cn.e3mall.controller;

import cn.e3mall.common.pojo.EasyUITreeNode;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.content.service.ContentCatCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by cxq on 2017/12/13.
 */
@Controller
@RequestMapping("/content")
public class ContentCatController {

    @Autowired
    private ContentCatCategoryService contentCatCategoryService;

    @RequestMapping("/category/list")
    @ResponseBody
    public List<EasyUITreeNode> getContentCatList(@RequestParam(name="id",defaultValue = "0") Long parentId){
        List<EasyUITreeNode> contentCatList = contentCatCategoryService.getContentCatList(parentId);
        return contentCatList;
    }

    @RequestMapping(value = "/category/create",method = RequestMethod.POST)
    @ResponseBody
    public E3Result createContentCategory(Long parentId, String name){
        E3Result e3Result = contentCatCategoryService.addContentCategory(parentId, name);
        return e3Result;
    }






}
