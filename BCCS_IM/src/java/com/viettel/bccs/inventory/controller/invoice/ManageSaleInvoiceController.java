package com.viettel.bccs.inventory.controller.invoice;

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
 * Created by tuyendv8 on 11/12/2015.
 */
@Component
@Scope("view")
@ManagedBean
public class ManageSaleInvoiceController {
    private boolean infoOrderDetail ;
    private List<String> lsTest = Lists.newArrayList();

    @PostConstruct
    public void init() {
        lsTest.add("sdfdsfds");
        lsTest.add("sdfdsfds");
        lsTest.add("sdfdsfds");
        lsTest.add("sdfdsfds");
        lsTest.add("sdfdsfds");
        lsTest.add("sdfdsfds");
        lsTest.add("sdfdsfds");
        lsTest.add("sdfdsfds");
    }

    @Secured("@")
    public void showInfoOrderDetail(){
        infoOrderDetail = true;
    }

    //getter and setter
    public List<String> getLsTest() {
        return lsTest;
    }
    public void setLsTest(List<String> lsTest) {
        this.lsTest = lsTest;
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
}
