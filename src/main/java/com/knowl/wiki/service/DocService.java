package com.knowl.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.knowl.wiki.domain.Doc;
import com.knowl.wiki.domain.DocExample;
import com.knowl.wiki.mapper.DocMapper;
import com.knowl.wiki.req.DocQueryReq;
import com.knowl.wiki.req.DocSaveReq;
import com.knowl.wiki.resp.DocQueryResp;
import com.knowl.wiki.resp.PageResp;
import com.knowl.wiki.util.CopyUtil;
import com.knowl.wiki.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class DocService {

    private static final Logger LOG = LoggerFactory.getLogger(DocService.class);

    @Resource
    private DocMapper docMapper;

    @Resource
    private SnowFlake snowFlake;

    public List<DocQueryResp> all(){
        DocExample docExample = new DocExample();
        docExample.setOrderByClause("sort asc");
        List<Doc> docList = docMapper.selectByExample(docExample);//会根据docExample里面的条件，也就是andNameLike设置的查询语句

        List<DocQueryResp> list = CopyUtil.copyList(docList, DocQueryResp.class);
        return list;
    }

    public PageResp<DocQueryResp> list(DocQueryReq req){
        DocExample docExample = new DocExample();
        DocExample.Criteria criteria = docExample.createCriteria(); //Criteria相当于where条件，这两行无论是用哪张表的example都是这样
        docExample.setOrderByClause("sort asc");
//        criteria.andNameLike("%" + req.getName + "%");
        PageHelper.startPage(req.getPage(), req.getSize());//这一行要跟最重要查询的语句放在一起，不然中间如果还有别的语句就会失效
        List<Doc> docList = docMapper.selectByExample(docExample);//会根据docExample里面的条件，也就是andNameLike设置的查询语句

        PageInfo<Doc> pageInfo = new PageInfo<>(docList);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());
//        List<DocResp> respList = new ArrayList<>();
//        for (Doc doc : doclist) {
////            DocResp docResp = new DocResp();
////           //对象复制
// BeanUtils.copyProperties(doc, docResp);
//            DocResp docResp = CopyUtil.copy(doc, DocResp.class);
//            respList.add(docResp);
//        }

        List<DocQueryResp> list = CopyUtil.copyList(docList, DocQueryResp.class);//因为要将返回值改成通用的返回值
        PageResp<DocQueryResp> pageResp = new PageResp();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        return pageResp;
    }

    /**
     * 保存
     */
    public void save(DocSaveReq req) {
        Doc doc = CopyUtil.copy(req, Doc.class);  //将请求参数转变为doc实体，再更新进
        if (ObjectUtils.isEmpty(req.getId())) {
            // 新增
            doc.setId(snowFlake.nextId());
            docMapper.insert(doc);
        } else {
            // 更新
            docMapper.updateByPrimaryKey(doc);
        }
    }

    public void delete(Long id) {
        docMapper.deleteByPrimaryKey(id);
    }

    public void delete(List<String> ids) {
        DocExample docExample = new DocExample();
        DocExample.Criteria criteria = docExample.createCriteria();
        List<Long> idList = ids.stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
        criteria.andIdIn(idList);
        docMapper.deleteByExample(docExample);
    }

}
