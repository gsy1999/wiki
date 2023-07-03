package com.knowl.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.knowl.wiki.domain.Ebook;
import com.knowl.wiki.domain.EbookExample;
import com.knowl.wiki.mapper.EbookMapper;
import com.knowl.wiki.req.EbookReq;
import com.knowl.wiki.resp.EbookResp;
import com.knowl.wiki.util.CopyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class EbookService {

    private static final Logger LOG = LoggerFactory.getLogger(EbookService.class);

    @Resource
    private EbookMapper ebookMapper;

    public List<EbookResp> list(EbookReq req){
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        if (!ObjectUtils.isEmpty(req.getName())){
            criteria.andNameLike("%" + req.getName() + "%");
        };
        PageHelper.startPage(1,3);//这一行要跟最重要查询的语句放在一起，不然中间如果还有别的语句就会失效
        List<Ebook> ebookList = ebookMapper.selectByExample(ebookExample);//会根据ebookExample里面的条件，也就是andNameLike设置的查询语句

        PageInfo<Ebook> pageInfo = new PageInfo<>(ebookList);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());
//        List<EbookResp> respList = new ArrayList<>();
//        for (Ebook ebook : ebooklist) {
////            EbookResp ebookResp = new EbookResp();
////           //对象复制
// BeanUtils.copyProperties(ebook, ebookResp);
//            EbookResp ebookResp = CopyUtil.copy(ebook, EbookResp.class);
//            respList.add(ebookResp);
//        }

        List<EbookResp> list = CopyUtil.copyList(ebookList, EbookResp.class);
        return list;
    }

}
