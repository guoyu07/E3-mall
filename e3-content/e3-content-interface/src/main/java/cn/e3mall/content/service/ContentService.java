package cn.e3mall.content.service;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.pojo.TbContent;

import java.util.List;

/**
 * Created by cxq on 2017/12/15.
 */
public interface ContentService {

    E3Result addContent(TbContent content);

    List<TbContent> getContentByCid(long cid);
}
