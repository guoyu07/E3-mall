package cn.e3mall.content.service.impl;

import cn.e3mall.common.pojo.EasyUITreeNode;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.content.service.ContentCatCategoryService;
import cn.e3mall.mapper.TbContentCategoryMapper;
import cn.e3mall.pojo.TbContentCategory;
import cn.e3mall.pojo.TbContentCategoryExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by cxq on 2017/12/13.
 */
@Service
public class ContentCatCategoryServiceImpl implements ContentCatCategoryService {

    @Autowired
    private TbContentCategoryMapper contentCategoryMapper;

    @Override
    public List<EasyUITreeNode> getContentCatList(long parentId) {

        TbContentCategoryExample example = new TbContentCategoryExample();
        example.createCriteria().andParentIdEqualTo(parentId);

        List<TbContentCategory> categories = contentCategoryMapper.selectByExample(example);

        List<EasyUITreeNode> nodeList = new ArrayList<>();

        for (TbContentCategory category : categories) {
            EasyUITreeNode node = new EasyUITreeNode();
            node.setText(category.getName());
            node.setId(category.getId());
            node.setState(category.getIsParent() ? "closed" : "open");

            nodeList.add(node);
        }
        return nodeList;
    }

    @Override
    public E3Result addContentCategory(long parentId, String name) {

        TbContentCategory contentCategory = new TbContentCategory();

        contentCategory.setParentId(parentId);
        contentCategory.setName(name);
        contentCategory.setStatus(1);
        contentCategory.setSortOrder(1);
        contentCategory.setIsParent(false);
        contentCategory.setCreated(new Date());
        contentCategory.setUpdated(new Date());
        contentCategoryMapper.insert(contentCategory);

        //根据parentId查询父节点
        TbContentCategory parent = contentCategoryMapper.selectByPrimaryKey(parentId);

        if(!parent.getIsParent()){
            parent.setIsParent(true);
            //更新到数据库
            contentCategoryMapper.updateByPrimaryKey(parent);
        }

        return E3Result.ok(contentCategory);
    }
}
