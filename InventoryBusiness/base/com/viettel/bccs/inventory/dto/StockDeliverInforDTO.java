package com.viettel.bccs.inventory.dto;

import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.GetTextFromBundleHelper;
import com.viettel.fw.dto.BaseMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hoangnt14 on 1/24/2017.
 */
public class StockDeliverInforDTO extends BaseMessage {
    private String staffCode;
    private List<String> listError = new ArrayList<>();
    private boolean haveTransfer;

    public String getStaffCode() {
        return staffCode;
    }

    public void setStaffCode(String staffCode) {
        this.staffCode = staffCode;
    }

    public boolean isHaveTransfer() {
        return haveTransfer;
    }

    public void setHaveTransfer(boolean haveTransfer) {
        this.haveTransfer = haveTransfer;
    }

    public List<String> getListError() {
        return listError;
    }

    public void setListError(List<String> listError) {
        this.listError = listError;
    }

    public void addError(String keyMsg, String... params) {
        if (!DataUtil.isNullOrEmpty(keyMsg)) {
            if (DataUtil.isNullOrEmpty(params)) {
                listError.add(GetTextFromBundleHelper.getText(keyMsg));
            } else {
                listError.add(GetTextFromBundleHelper.getTextParam(keyMsg, params));
            }
        }
    }

    public void setDescriptionResult(String description) {
        setDescription(GetTextFromBundleHelper.getText(description));
    }

    public void addErrorMessage(String error) {
        listError.add(error);
    }

    public StockDeliverInforDTO() {

    }

    public StockDeliverInforDTO(String staffCode) {
        this.staffCode = staffCode;
    }


    public StockDeliverInforDTO(String staffCode, boolean haveTransfer, boolean success, String errCode, String description, String... paramsMsg) {
        super(success, errCode, description, paramsMsg);
        this.staffCode = staffCode;
        this.haveTransfer = haveTransfer;
    }

}
