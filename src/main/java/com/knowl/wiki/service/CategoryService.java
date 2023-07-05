package com.knowl.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.knowl.wiki.domain.Category;
import com.knowl.wiki.domain.CategoryExample;
import com.knowl.wiki.mapper.CategoryMapper;
import com.knowl.wiki.req.CategoryQueryReq;
import com.knowl.wiki.req.CategorySaveReq;
import com.knowl.wiki.resp.CategoryQueryResp;
import com.knowl.wiki.resp.PageResp;
import com.knowl.wiki.util.CopyUtil;
import com.knowl.wiki.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CategoryService {

    private static final Logger LOG = LoggerFactory.getLogger(CategoryService.class);

    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private SnowFlake snowFlake;

    public PageResp<CategoryQueryResp> list(CategoryQueryReq req){
        CategoryExample categoryExample = new CategoryExample();
        CategoryExample.Criteria criteria = categoryExample.createCriteria();
        PageHelper.startPage(req.getPage(), req.getSize());//这一行要跟最重要查询的语句放在一起，不然中间如果还有别的语句就会失效
        List<Category> categoryList = categoryMapper.selectByExample(categoryExample);//会根据categoryExample里面的条件，也就是andNameLike设置的查询语句

        PageInfo<Category> pageInfo = new PageInfo<>(categoryList);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());
//        List<CategoryResp> respList = new ArrayList<>();
//        for (Category category : categorylist) {
////            CategoryResp categoryResp = new CategoryResp();
////           //对象复制
// BeanUtils.copyProperties(category, categoryResp);
//            CategoryResp categoryResp = CopyUtil.copy(category, CategoryResp.class);
//            respList.add(categoryResp);
//        }

        List<CategoryQueryResp> list = CopyUtil.copyList(categoryList, CategoryQueryResp.class);
        PageResp<CategoryQueryResp> pageResp = new PageResp();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        return pageResp;
    }

    /**
     * 保存
     */
    public void save(CategorySaveReq req) {
        Category category = CopyUtil.copy(req, Category.class);  //将请求参数转变为category实体，再更新进
        if (ObjectUtils.isEmpty(req.getId())) {
            // 新增
            category.setId(snowFlake.nextId());
            categoryMapper.insert(category);
        } else {
            // 更新
            categoryMapper.updateByPrimaryKey(category);
        }
    }

    public void delete(Long id) {
        categoryMapper.deleteByPrimaryKey(id);
    }

}
