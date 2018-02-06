package com.cxq;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

import java.util.List;
import java.util.Map;

/**
 * Created by cxq on 2017/12/17.
 */
public class TestSolr {

    @Test
    public void addDocument() throws Exception {
        //建立连接对象，创建连接（参数，solr服务的url）
        SolrServer solrServer = new HttpSolrServer("http://192.168.25.130:8080/solr/collection1");
        //创建文档对象，solrInputDocument
        SolrInputDocument document = new SolrInputDocument();
        //向文档中添加域，必须包含一个id域，所有域必须在schema.xml中定义
        document.addField("id", "doc1");
        document.addField("item_title", "测试商品01");
        document.addField("item_sell_point", "大飒飒发售发售");
        document.addField("item_price", 11122);
        //吧文档写入索引库，
        solrServer.add(document);
        // 然后提交
        solrServer.commit();
    }

    @Test
    public void deleteDocument() throws Exception {
        //建立连接对象，创建连接（参数，solr服务的url）
        SolrServer solrServer = new HttpSolrServer("http://192.168.25.130:8080/solr/collection1");
        //删除文档
        solrServer.deleteById("doc1");
        //提交
        solrServer.commit();
    }

    @Test
    public void queryIndex() throws Exception {
        //创建solrService对象
        SolrServer solrServer = new HttpSolrServer("http://192.168.25.130:8080/solr/collection1");
        // 创建solrQuery对象
        SolrQuery solrQuery = new SolrQuery();
        // 设置查询条件
//        solrQuery.setQuery("*:*");
        solrQuery.set("q", "*:*");
        // 执行查询queryResponse对象得到。
        QueryResponse queryResponse = solrServer.query(solrQuery);
        //取文档列表，取查询结果的总记录数。
        SolrDocumentList results = queryResponse.getResults();
        System.out.println("总共：" + results.getNumFound());
        //从文档中遍历文档列表，从文档中取域的值
        for (SolrDocument result : results) {
            System.out.println(result.get("id"));
            System.out.println(result.get("item_title"));
            System.out.println(result.get("item_sell_point"));
            System.out.println(result.get("item_price"));
            System.out.println(result.get("item_image"));
            System.out.println(result.get("item_category_name"));
        }
    }
    @Test
    public void testQueryFZ() throws Exception{
        //创建solrService对象
        SolrServer solrServer = new HttpSolrServer("http://192.168.25.130:8080/solr/collection1");
        // 创建solrQuery对象
        SolrQuery solrQuery = new SolrQuery();

        solrQuery.set("q", "手机");
        solrQuery.setStart(0);
        solrQuery.setRows(20);
        solrQuery.set("df","item_title");
        solrQuery.setHighlight(true);
        solrQuery.addHighlightField("item_title");
        solrQuery.setHighlightSimplePre("<em>");
        solrQuery.setHighlightSimplePost("</em>");
        solrServer.query(solrQuery);

        // 执行查询queryResponse对象得到。
        QueryResponse queryResponse = solrServer.query(solrQuery);
        //取文档列表，取查询结果的总记录数。
        SolrDocumentList results = queryResponse.getResults();
        System.out.println("总共：" + results.getNumFound());
        //从文档中遍历文档列表，从文档中取域的值
        for (SolrDocument result : results) {
            Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();

            List<String> lists = highlighting.get(result.get("id")).get("item_title");
            String title = "";
            if(lists != null && lists.size() >0){
                title = lists.get(0);
                System.out.println(title);
            }else{
                title = (String)result.get("item_title");
            }
            System.out.println(result.get("item_sell_point"));
            System.out.println(result.get("item_price"));
            System.out.println(result.get("item_image"));
            System.out.println(result.get("item_category_name"));
        }
    }
}
