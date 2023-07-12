package com.knowl.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.knowl.wiki.domain.Content;
import com.knowl.wiki.domain.Doc;
import com.knowl.wiki.domain.DocExample;
import com.knowl.wiki.exception.BusinessException;
import com.knowl.wiki.exception.BusinessExceptionCode;
import com.knowl.wiki.mapper.ContentMapper;
import com.knowl.wiki.mapper.DocMapper;
import com.knowl.wiki.mapper.DocMapperCust;
import com.knowl.wiki.req.DocQueryReq;
import com.knowl.wiki.req.DocSaveReq;
import com.knowl.wiki.resp.DocQueryResp;
import com.knowl.wiki.resp.PageResp;
import com.knowl.wiki.util.CopyUtil;
import com.knowl.wiki.util.RedisUtil;
import com.knowl.wiki.util.RequestContext;
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
    private DocMapperCust docMapperCust;

    @Resource
    private SnowFlake snowFlake;

    @Resource
    private ContentMapper contentMapper;

    @Resource
    public RedisUtil redisUtil;

    public List<DocQueryResp> all(Long ebookId) {
        DocExample docExample = new DocExample();
        docExample.createCriteria().andEbookIdEqualTo(ebookId);
        docExample.setOrderByClause("sort asc");
        List<Doc> docList = docMapper.selectByExample(docExample);

        // 列表复制
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
        Content content = CopyUtil.copy(req, Content.class);  //将请求参数中的content转变为content实体，再更新进
        if (ObjectUtils.isEmpty(req.getId())) {
            // 新增
            doc.setId(snowFlake.nextId());
            doc.setViewCount(0);
            doc.setVoteCount(0);
            docMapper.insert(doc);

            content.setId(doc.getId()); //因为nextid会重新生成一个id，导致跟上面的id不同，所以直接把上面能生成的拿过来用
            contentMapper.insert(content);
        } else {
            // 更新
            docMapper.updateByPrimaryKey(doc);
            int count = contentMapper.updateByPrimaryKeyWithBLOBs(content); //这个方法包含对大字段的操作
            if(count==0){
                contentMapper.insert(content);
            }
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

    public String findContent(Long id){
        Content content = contentMapper.selectByPrimaryKey(id);
        //文档阅读数+1
        docMapperCust.increaseViewCount(id);
        if (ObjectUtils.isEmpty(content)) {
            return "";
        } else {
            return content.getContent();
        }
    }

    /*
    点赞
     */
    public void vote(Long id){
        // docMapperCust.increaseVoteCount(id);
        // 远程IP+doc.id作为key，24小时内不能重复
        String ip = RequestContext.getRemoteAddr();
        if (redisUtil.validateRepeat("DOC_VOTE_" + id + "_" + ip, 5000)) {
            docMapperCust.increaseVoteCount(id);
        } else {
            throw new BusinessException(BusinessExceptionCode.VOTE_REPEAT);
        }

//        // 推送消息
//        Doc docDb = docMapper.selectByPrimaryKey(id);
//        String logId = MDC.get("LOG_ID");
//        wsService.sendInfo("【" + docDb.getName() + "】被点赞！", logId);
//        // rocketMQTemplate.convertAndSend("VOTE_TOPIC", "【" + docDb.getName() + "】被点赞！");
    }

    public void updateEbookInfo(){
        docMapperCust.updateEbookInfo();
    }

}
