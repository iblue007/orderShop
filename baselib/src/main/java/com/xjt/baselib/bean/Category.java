package com.xjt.baselib.bean;

import java.io.Serializable;

/**
 * Create by xuqunxing on  2020/3/6
 */
public class Category implements Serializable {
    private int id;
    private String categoryName;
    private String cdate;

    public String getCdate() {
        return cdate;
    }

    public void setCdate(String cdate) {
        this.cdate = cdate;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
