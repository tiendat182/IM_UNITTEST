package com.viettel.bccs.inventory.controller.stock;

import com.google.common.collect.Lists;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by tuyendv8 on 11/16/2015.
 */
@Component
@Scope("view")
@ManagedBean
public class SpecialStaffReturnStockController {

    private boolean infoOrderDetail;
    private boolean renderedDeposit = true;
    private String type;
    private List<TestDto> lsTest = Lists.newArrayList();
    private Date date = new Date(20151111);
    private int count;
    private int priceMoney;
    private int sumMoney;

    @PostConstruct
    public void init() {
        lsTest.add(new TestDto());
    }
    @Secured("@")
    public void doAddItem() {
        lsTest.add(new TestDto());
    }

    @Secured("@")
    public void doRemoveItem(int index) {
        lsTest.remove(index);
    }

    @Secured("@")
    public void showInfoOrderDetail() {
        infoOrderDetail = true;
    }

    public void doOncelEdit(){

    }

    //getter and setter

    public List<TestDto> getLsTest() {
        return lsTest;
    }

    public void setLsTest(List<TestDto> lsTest) {
        this.lsTest = lsTest;
    }

    public int getSumMoney() {
        return sumMoney;
    }

    public void setSumMoney(int sumMoney) {
        this.sumMoney = sumMoney;
    }

    public Date getCurrentDate() {
        return Calendar.getInstance().getTime();
    }

    public boolean isInfoOrderDetail() {
        return infoOrderDetail;
    }

    public void setInfoOrderDetail(boolean infoOrderDetail) {
        this.infoOrderDetail = infoOrderDetail;
    }

    public boolean isRenderedDeposit() {
        return renderedDeposit;
    }

    public void setRenderedDeposit(boolean renderedDeposit) {
        this.renderedDeposit = renderedDeposit;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getPriceMoney() {
        return priceMoney;
    }

    public void setPriceMoney(int priceMoney) {
        this.priceMoney = priceMoney;
    }

    public class TestDto {


        private String str1 = "Điện thoại";
        private String str2 = "Iphone 6S 128G";
        private String str3 = "Mới";
        private String str4 = "Chiếc";
        private String price= "1000000";
        private String number= "1";
        private String str5 = "100000";
        private String str6 = "Chi tiết";
        public TestDto() {
        }
        public String getStr1() {
            return str1;
        }

        public void setStr1(String str1) {
            this.str1 = str1;
        }

        public String getStr2() {
            return str2;
        }

        public void setStr2(String str2) {
            this.str2 = str2;
        }

        public String getStr3() {
            return str3;
        }

        public void setStr3(String str3) {
            this.str3 = str3;
        }

        public String getStr4() {
            return str4;
        }

        public void setStr4(String str4) {
            this.str4 = str4;
        }

        public String getStr5() {
            return str5;
        }

        public void setStr5(String str5) {
            this.str5 = str5;
        }

        public String getStr6() {
            return str6;
        }

        public void setStr6(String str6) {
            this.str6 = str6;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }
    }
}
