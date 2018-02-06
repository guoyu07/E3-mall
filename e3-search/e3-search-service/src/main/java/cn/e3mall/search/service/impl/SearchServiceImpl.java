package cn.e3mall.search.service.impl;

import cn.e3mall.common.pojo.SearchResult;
import cn.e3mall.search.dao.SearchDao;
import cn.e3mall.search.service.SearchService;
import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 商品搜索service
 * Created by cxq on 2017/12/17.
 */
@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private SearchDao searchDao;

    @Override
    public SearchResult search(String keyword, int page, int rows) throws Exception {
        //创建solrquery对象
        SolrQuery query = new SolrQuery();
        // 设置查询条件
        query.setQuery(keyword);
        // 分页信息
        if(page <= 0) page = 1;
        query.setStart((page-1)*rows);
        query.setRows(rows);
        // 设置默认搜索域
        query.set("df","item_title");
        // 高亮开启
        query.setHighlight(true);
        query.addHighlightField("item_title");
        query.setHighlightSimplePre("<em style='color:red'>");
        query.setHighlightSimplePost("</em>");
        // 调用dao执行查询
        SearchResult search = searchDao.search(query);
        //计算总页数
        long recordCount = search.getRecordCount();
        int totalPage = (int) (recordCount/rows);
        if (totalPage % rows > 0) totalPage ++;
        //添加返回结果
        search.setTotalPages(totalPage);
        //返回结果
        return search;
    }
}
