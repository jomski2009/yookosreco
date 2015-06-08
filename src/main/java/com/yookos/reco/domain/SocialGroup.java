package com.yookos.reco.domain;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

/**
 * Created by jome on 2014/02/10.
 */

@Entity(value = "sgroups")
public class SocialGroup {

    @Id
    private long groupid;
    private int grouptype;
    private String name;
    private String displayname;
    private String description;
    private long userid;
    private long creationdate;
    private long modificationdate;
    private int status;

    public long getGroupid() {
        return groupid;
    }

    public void setGroupid(long groupid) {
        this.groupid = groupid;
    }

    public int getGrouptype() {
        return grouptype;
    }

    public void setGrouptype(int grouptype) {
        this.grouptype = grouptype;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayname() {
        return displayname;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getUserid() {
        return userid;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }

    public long getCreationdate() {
        return creationdate;
    }

    public void setCreationdate(long creationdate) {
        this.creationdate = creationdate;
    }

    public long getModificationdate() {
        return modificationdate;
    }

    public void setModificationdate(long modificationdate) {
        this.modificationdate = modificationdate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "SocialGroup{" +
                "groupid=" + groupid +
                ", grouptype=" + grouptype +
                ", name='" + name + '\'' +
                ", displayname='" + displayname + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
