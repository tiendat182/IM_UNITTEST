package com.viettel.bccs.inventory.controller.stock;

import com.google.common.collect.Lists;
import org.primefaces.model.UploadedFile;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.faces.bean.ManagedBean;
import java.util.List;

/**
 * Created by tuyendv8 on 11/17/2015.
 */
@Component
@Scope("view")
@ManagedBean
public class DetailSerialController {
    private String typeInput = "1";
    private UploadedFile uploadedFile;
    private List<String> lsTestEmpty = Lists.newArrayList();
    private List<String> lsTestUpload = Lists.newArrayList();
    private List<TestDto> lsTest = Lists.newArrayList();

    public void doResetTblSerial() {
        lsTestUpload = Lists.newArrayList();
        lsTestUpload.add("dsfdsfds");
    }

    public void doRemoveItem(int index) {
        lsTestUpload.remove(index);
    }

    public void doAddSerial() {
        lsTestUpload.add("sdfdsfds");
    }

    //getter and setter

    public String getTypeInput() {
        return typeInput;
    }

    public void setTypeInput(String typeInput) {
        this.typeInput = typeInput;
    }

    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public List<String> getLsTestEmpty() {
        return lsTestEmpty;
    }

    public void setLsTestEmpty(List<String> lsTestEmpty) {
        this.lsTestEmpty = lsTestEmpty;
    }

    public List<String> getLsTestUpload() {
        return lsTestUpload;
    }

    public void setLsTestUpload(List<String> lsTestUpload) {
        this.lsTestUpload = lsTestUpload;
    }

    public List<TestDto> getLsTest() {
        return lsTest;
    }

    public void setLsTest(List<TestDto> lsTest) {
        this.lsTest = lsTest;
    }

    public class TestDto {

        private String str1 = "Điện thoại";
        private String str2 = "Iphone 6S 128G";
        private String str3 = "Mới";
        private String str4 = "Chiếc";
        private String str5 = "100000";
        private String str6 = "ABC";
        private String str7 = "10";

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

        public String getStr7() {
            return str7;
        }

        public void setStr7(String str7) {
            this.str7 = str7;
        }
    }
}
