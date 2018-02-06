package cn.e3mall.search.message;

import cn.e3mall.common.pojo.SearchItem;
import cn.e3mall.search.mapper.ItemMapper;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * 监听商品添加时间消息，接收消息查询数据库添加到索引库。
 * Created by cxq on 2017/12/27.
 */
public class ItemAddMessageListener implements MessageListener {

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private SolrServer solrServer;
    @Override
    public void onMessage(Message message) {
        try {
            //取消息内容（商品id）
            TextMessage textMessage = (TextMessage)message;
            String text = textMessage.getText();
            Long itemId = new Long(text);
            Thread.sleep(1000);
            SearchItem searchItem = itemMapper.getItemById(itemId);

            SolrInputDocument document = new SolrInputDocument();
            // 向文档对象添加域，
            document.addField("id", searchItem.getId());
            document.addField("item_title", searchItem.getTitle());
            document.addField("item_sell_point", searchItem.getTitle());
            document.addField("item_price", searchItem.getPrice());
            document.addField("item_image", searchItem.getImage());
            document.addField("item_category_name", searchItem.getCategory_name());

            solrServer.add(document);
            solrServer.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
