package com.example.android.hellopets.data;

import java.io.Serializable;

/**
 * Created by wkp on 2016/12/8.
 */
public class User implements Serializable {

    private Integer id;
    private String realname;
    private String sex;
    private String idcard;
    private String phone;
    private String address;

    public User(String realname, String sex, String idcard, String phone, String address) {
        this.realname = realname;
        this.sex = sex;
        this.idcard = idcard;
        this.phone = phone;
        this.address = address;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
