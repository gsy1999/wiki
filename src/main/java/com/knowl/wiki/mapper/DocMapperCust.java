package com.knowl.wiki.mapper;

import org.apache.ibatis.annotations.Param;

public interface DocMapperCust {

    public void increaseViewCount(@Param("id") Long id);  //引号里的id是和xml里的#{id}对应

//    public void increaseVoteCount(@Param("id") Long id);
//
//    public void updateEbookInfo();




}
