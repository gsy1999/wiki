package com.knowl.wiki.req;

//这就是一个父类，以后很多类都要实现分页功能，直接继承就好
public class EbookQueryReq extends PageReq{
    private Long id;

    private String name;

    private Long categoryId2; //根据前端的请求参数（二级分类）添加的，且跟前端保持一致

    public Long getCategoryId2() {
        return categoryId2;
    }

    public void setCategoryId2(Long categoryId2) {
        this.categoryId2 = categoryId2;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "EbookQueryReq{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", categoryId2=" + categoryId2 +
                "} " + super.toString();
    }
}