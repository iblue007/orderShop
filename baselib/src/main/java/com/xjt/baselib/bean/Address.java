package com.xjt.baselib.bean;


import java.io.Serializable;

public class Address implements Serializable {
    private int id;
    private String name;
    private String phone;
    private int userId;
    private String address;
    private String addressDetail;
    private int state;//1 为选中状态
    private String addressDate;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddressDate() {
        return addressDate;
    }

    public void setAddressDate(String addressDate) {
        this.addressDate = addressDate;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressDetail() {
        return addressDetail;
    }

    public void setAddressDetail(String addressDetail) {
        this.addressDetail = addressDetail;
    }
}
