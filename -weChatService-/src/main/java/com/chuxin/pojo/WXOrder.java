package com.chuxin.pojo;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by wangzhen on 2017/8/17.
 */
@Entity
public class WXOrder {

    @Id
    @SequenceGenerator(name = "orderId")
    @GeneratedValue(generator = "orderId")
    private long id;

    @Column(nullable = false)
    private String orderId;

    @Column(nullable = false)
    private int orderPrice;

    @Column(nullable = false)
    private String openId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String grade;

    @Column(nullable = false)
    private Date dateCreate;

    @Column(nullable = true)
    private Date payDate;

    @Column(nullable = false)
    private String status;

    @Column(nullable = true)
    private int discount;

    @Column(nullable = false)
    private int courseId;

    @Column(nullable = false)
    private String date;

    @Column(nullable = true)
    private String wxOrderId;

    @Version
    private long version;

    public WXOrder() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWxOrderId() {
        return wxOrderId;
    }

    public void setWxOrderId(String wxOrderId) {
        this.wxOrderId = wxOrderId;
    }

    public WXOrder(String orderId, int orderPrice, String openId, String name, String phone, String address, String grade, Date dateCreate, Date payDate, String status, int discount, int courseId, String date) {
        this.orderId = orderId;
        this.orderPrice = orderPrice;
        this.openId = openId;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.grade = grade;
        this.dateCreate = dateCreate;
        this.payDate = payDate;
        this.status = status;
        this.discount = discount;
        this.courseId = courseId;
        this.date = date;
    }

    public enum Status{
        NotPay("0"),
        YesPay("1"),
        Refunding("2"),
        Refunded("3");

        final String value;
        Status(String value) {
            this.value = value;
        }

        public String toString() {
           return value;
        }
        String getKey() { return name(); }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(int orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public Date getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(Date dateCreate) {
        this.dateCreate = dateCreate;
    }

    public Date getPayDate() {
        return payDate;
    }

    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }
}
