package com.waydrow.newloveinn.bean;

/**
 * Created by Waydrow on 2017/4/8.
 */

public class ExchangeItem {

    private String id;

    private String exname;

    private String exmoney;

    public ExchangeItem() {}

    public ExchangeItem(String id, String exname, String exmoney) {
        this.id = id;
        this.exname = exname;
        this.exmoney = exmoney;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExname() {
        return exname;
    }

    public void setExname(String exname) {
        this.exname = exname;
    }

    public String getExmoney() {
        return exmoney;
    }

    public void setExmoney(String exmoney) {
        this.exmoney = exmoney;
    }
}
