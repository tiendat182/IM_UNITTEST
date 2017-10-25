package com.viettel.bccs.inventory.controller.stock;

import com.google.common.collect.Lists;
import org.primefaces.model.UploadedFile;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by tuyendv8 on 11/6/2015.
 */
@Component
@Scope("view")
@ManagedBean
public class AboveExportOrderWithFileController {


    private boolean infoOrderDetail ;
    private UploadedFile uploadedFile;

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
    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }
    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }
}
