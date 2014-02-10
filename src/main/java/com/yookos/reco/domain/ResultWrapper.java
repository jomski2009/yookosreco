package com.yookos.reco.domain;

/**
 * Created by jome on 2014/02/09.
 */
public class ResultWrapper {
    private RecoUser user;
    private RecoUser fof;
    private int count;

    public RecoUser getUser() {
        return user;
    }

    public void setUser(RecoUser user) {
        this.user = user;
    }

    public RecoUser getFof() {
        return fof;
    }

    public void setFof(RecoUser fof) {
        this.fof = fof;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
