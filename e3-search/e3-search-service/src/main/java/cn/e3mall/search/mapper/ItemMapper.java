package cn.e3mall.search.mapper;

import cn.e3mall.common.pojo.SearchItem;

import java.util.List;

/**
 * Created by cxq on 2017/12/16.
 */
public interface ItemMapper {

    List<SearchItem> getItemList();

    SearchItem getItemById(long itemId);
}
