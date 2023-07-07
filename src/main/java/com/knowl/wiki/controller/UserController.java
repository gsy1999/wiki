package com.knowl.wiki.controller;

import com.knowl.wiki.req.UserQueryReq;
import com.knowl.wiki.req.UserSaveReq;
import com.knowl.wiki.resp.CommonResp;
import com.knowl.wiki.resp.PageResp;
import com.knowl.wiki.resp.UserQueryResp;
import com.knowl.wiki.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @GetMapping("/list")
    public CommonResp list(@Valid UserQueryReq req){  //@Valid意为，这组参数要开启校验规则
        CommonResp<PageResp<UserQueryResp>> resp = new CommonResp<>();
        PageResp<UserQueryResp> list = userService.list(req);
        resp.setContent(list);
        return resp;
    }

    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody UserSaveReq req) {
        CommonResp resp = new CommonResp<>();
        userService.save(req);
        return resp;
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp delete(@PathVariable Long id) {
        CommonResp resp = new CommonResp<>();
        userService.delete(id);
        return resp;
    }
}
