package cn.e3mall.service;

import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;

/**
 * Created by cxq on 2017/10/7.
 */
public interface ItemService {
    TbItem  getItemById(Long itemId);
    EasyUIDataGridResult getItemList(int page, int rows);

    E3Result insert(TbItem item,String desc);

    TbItemDesc getItemDescById(Long itemId);
}
