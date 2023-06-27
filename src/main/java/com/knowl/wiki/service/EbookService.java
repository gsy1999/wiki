package com.knowl.wiki.service;

import com.knowl.wiki.domain.Ebook;
import com.knowl.wiki.domain.EbookExample;
import com.knowl.wiki.mapper.EbookMapper;
import com.knowl.wiki.req.EbookReq;
import com.knowl.wiki.resp.EbookResp;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class EbookService {
    @Resource
    private EbookMapper ebookMapper;

    public List<EbookResp> list(EbookReq req){
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        criteria.andNameLike("%" + req.getName() + "%");
        List<Ebook> ebooklist = ebookMapper.selectByExample(ebookExample);//会根据ebookExample里面的条件，也就是andNameLike设置的查询语句

        List<EbookResp> respList = new ArrayList<>();
        for (Ebook ebook : ebooklist) {
            EbookResp ebookResp = new EbookResp();
            BeanUtils.copyProperties(ebook, ebookResp);  //对象复制
            respList.add(ebookResp);
        }
        return respList;
    }

}
