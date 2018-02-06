package cn.e3mall.search.service;

import cn.e3mall.common.pojo.SearchResult;

/**
 * Created by cxq on 2017/12/17.
 */
public interface SearchService {
    SearchResult search(String keyword, int page, int rows) throws Exception;
}
