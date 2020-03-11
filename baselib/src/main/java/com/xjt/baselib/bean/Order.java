package com.xjt.baselib.bean;

import java.io.Serializable;

/**
 * Create by xuqunxing on  2020/3/9
 */
public class Order implements Serializable {

    private int id;
    private int goodId;
    private int categoryId;
    private int goodState;//0 下单中 1 配送中 2 送达客户手中 3 客户确认收到
    private String goodName;
    private String goodDetail;
    private String goodDiscount;
    private String goodPrice;
    private String goodPic;
    private String odate;
    private String buyerName;
    private String buyerphone;
    private String buyerAddress;


    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getBuyerphone() {
        return buyerphone;
    }

    public void setBuyerphone(String buyerphone) {
        this.buyerphone = buyerphone;
    }

    public String getBuyerAddress() {
        return buyerAddress;
    }

    public void setBuyerAddress(String buyerAddress) {
        this.buyerAddress = buyerAddress;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGoodId() {
        return goodId;
    }

    public void setGoodId(int goodId) {
        this.goodId = goodId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getGoodState() {
        return goodState;
    }

    public void setGoodState(int goodState) {
        this.goodState = goodState;
    }

    public String getGoodName() {
        return goodName;
    }

    public void setGoodName(String goodName) {
        this.goodName = goodName;
    }

    public String getGoodDetail() {
        return goodDetail;
    }

    public void setGoodDetail(String goodDetail) {
        this.goodDetail = goodDetail;
    }

    public String getGoodDiscount() {
        return goodDiscount;
    }

    public void setGoodDiscount(String goodDiscount) {
        this.goodDiscount = goodDiscount;
    }

    public String getGoodPrice() {
        return goodPrice;
    }

    public void setGoodPrice(String goodPrice) {
        this.goodPrice = goodPrice;
    }

    public String getGoodPic() {
        return goodPic;
    }

    public void setGoodPic(String goodPic) {
        this.goodPic = goodPic;
    }

    public String getOdate() {
        return odate;
    }

    public void setOdate(String odate) {
        this.odate = odate;
    }
}
