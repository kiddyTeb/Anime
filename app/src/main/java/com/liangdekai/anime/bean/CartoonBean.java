package com.liangdekai.anime.bean;

import com.google.gson.annotations.SerializedName;

public class CartoonBean {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getFinish() {
        return finish;
    }

    public void setFinish(String finish) {
        this.finish = finish;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getCoverImg() {
        return coverImg;
    }

    public void setCoverImg(String coverImg) {
        this.coverImg = coverImg;
    }

    private String name ;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String type ;
    private String area ;
    private String finish ;

    @SerializedName("des")
    private String introduction ;

    private String coverImg ;
}
