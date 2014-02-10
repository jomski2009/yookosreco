package com.yookos.reco.jpa.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by jome on 2014/02/06.
 */

@Entity
@Table(name = "jivemongoprofile")
public class UserProfile {
    @Id
    private long userprofileid;
    private long userid;
    private String username;
    private String firstname;
    private String lastname;
    private int fieldid;
    private String name;
    private String value;

    public long getUserprofileid() {
        return userprofileid;
    }

    public void setUserprofileid(long userprofileid) {
        this.userprofileid = userprofileid;
    }

    public long getUserid() {
        return userid;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public int getFieldid() {
        return fieldid;
    }

    public void setFieldid(int fieldid) {
        this.fieldid = fieldid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "UserProfile{" +
                "userprofileid=" + userprofileid +
                ", userid=" + userid +
                ", username='" + username + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", fieldid=" + fieldid +
                ", name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
