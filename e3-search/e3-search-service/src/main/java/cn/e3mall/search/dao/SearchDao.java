package cn.e3mall.search.dao;

import cn.e3mall.common.pojo.SearchItem;
import cn.e3mall.common.pojo.SearchResult;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 商品搜索dao
 * Created by cxq on 2017/12/17.
 */
@Repository
public class SearchDao {

    @Autowired
    private SolrServer solrServer;

    public SearchResult search(SolrQuery solrQuery) throws Exception{
        //根据查询条件查询索引库
        QueryResponse queryResponse = solrServer.query(solrQuery);
        // 取查询结果
        SolrDocumentList results = queryResponse.getResults();
        // 取查询结果总记录数
        long numFound = results.getNumFound();
        SearchResult result = new SearchResult();
        List<SearchItem> itemList = new ArrayList<>();
        // 取高亮显示
        Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
        String title = "";
        // 取商品列表
        for (SolrDocument entries : results) {
            SearchItem item = new SearchItem();
            item.setId((String) entries.get("id"));
            item.setCategory_name((String) entries.get("item_category_name"));
            item.setTitle((String) entries.get("item_title"));
            item.setImage((String) entries.get("item_image"));
            item.setPrice((Long) entries.get("item_price"));
            List<String> list = highlighting.get(entries.get("id")).get("item_title");
            if(list != null && list.size() > 0){
                title = list.get(0);
            }else{
                title = (String) entries.get("item_title");
            }
            item.setTitle(title);
            itemList.add(item);
        }
        result.setItemList(itemList);
        result.setRecordCount(numFound);
        // 返回结果
        return result;
    }


}
