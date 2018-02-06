package cn.e3mall.service;

import cn.e3mall.common.pojo.EasyUITreeNode;

import java.util.List;

/**
 * Created by cxq on 2017/10/8.
 */
public interface ItemCatService {
    List<EasyUITreeNode> getItemCatList(long parentId);
}
