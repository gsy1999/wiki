package com.knowl.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.knowl.wiki.domain.User;
import com.knowl.wiki.domain.UserExample;
import com.knowl.wiki.mapper.UserMapper;
import com.knowl.wiki.req.UserQueryReq;
import com.knowl.wiki.req.UserSaveReq;
import com.knowl.wiki.resp.PageResp;
import com.knowl.wiki.resp.UserQueryResp;
import com.knowl.wiki.util.CopyUtil;
import com.knowl.wiki.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    @Resource
    private UserMapper userMapper;

    @Resource
    private SnowFlake snowFlake;

    public PageResp<UserQueryResp> list(UserQueryReq req){
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        if (!ObjectUtils.isEmpty(req.getLoginName())) {
            criteria.andLoginNameEqualTo(req.getLoginName());
        }
        PageHelper.startPage(req.getPage(), req.getSize());
        List<User> userList = userMapper.selectByExample(userExample);

        PageInfo<User> pageInfo = new PageInfo<>(userList);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());
//        List<UserResp> respList = new ArrayList<>();
//        for (User user : userlist) {
////            UserResp userResp = new UserResp();
////           //对象复制
// BeanUtils.copyProperties(user, userResp);
//            UserResp userResp = CopyUtil.copy(user, UserResp.class);
//            respList.add(userResp);
//        }

        List<UserQueryResp> list = CopyUtil.copyList(userList, UserQueryResp.class);
        PageResp<UserQueryResp> pageResp = new PageResp();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        return pageResp;
    }

    /**
     * 保存
     */
    public void save(UserSaveReq req) {
        User user = CopyUtil.copy(req, User.class);  //将请求参数转变为user实体，再更新进
        if (ObjectUtils.isEmpty(req.getId())) {
            // 新增
            user.setId(snowFlake.nextId());
            userMapper.insert(user);
        } else {
            // 更新
            userMapper.updateByPrimaryKey(user);
        }
    }

    public void delete(Long id) {
        userMapper.deleteByPrimaryKey(id);
    }

}
