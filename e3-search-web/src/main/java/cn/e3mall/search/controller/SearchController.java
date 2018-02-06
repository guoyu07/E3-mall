package cn.e3mall.search.controller;

import cn.e3mall.common.pojo.SearchResult;
import cn.e3mall.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by cxq on 2017/12/17.
 */
@Controller
public class SearchController {

    @Autowired
    private SearchService searchService;
    @Value("${SEARCH_RESULT_ROWS}")
    private Integer SEARCH_RESULT_ROWS;

    @RequestMapping("/search")
    public String searchItemList(String keyword, @RequestParam(defaultValue = "1") Integer page, Model model) throws Exception {
        keyword = new String(keyword.getBytes("iso-8859-1"),"utf-8");
        SearchResult search = searchService.search(keyword, page, SEARCH_RESULT_ROWS);
        //结果传递给页面
        model.addAttribute("query",keyword);
        model.addAttribute("totalPages",search.getTotalPages());
        model.addAttribute("page",page);
        model.addAttribute("recordCount",search.getRecordCount());
        model.addAttribute("itemList",search.getItemList());
        return "search";
    }
}
