package com.knowl.wiki.req;

//这就是一个父类，以后很多类都要实现分页功能，直接继承就好
public class DocQueryReq extends PageReq{
    @Override
    public String toString() {
        return "DocQueryReq{} " + super.toString();
    }
}