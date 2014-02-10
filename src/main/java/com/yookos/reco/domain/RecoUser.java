package com.yookos.reco.domain;

/**
 * Created by jome on 2014/02/09.
 */
public class RecoUser {
    private int userid;
    private String name;
    private String imageUrl;

    public long getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        String fullUrl = "http://www.yookos.com/api/core/v3/people/" + this.userid + "/images/1/data";
        return fullUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
