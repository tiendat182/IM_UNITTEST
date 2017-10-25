package com.viettel.bccs.inventory.dto;

import java.util.List;

/**
 * Created by hoangnt14 on 2/23/2016.
 */
public class StockInspectExportDTO {

    private List<ApproveStockInspectDTO> lstRealSerial;
    private List<ApproveStockInspectDTO> lstSysSerial;
    private List<ApproveStockInspectDTO> lstProductNotApprove;
    private List<ApproveStockInspectDTO> lstTotal;

    public List<ApproveStockInspectDTO> getLstRealSerial() {
        return lstRealSerial;
    }

    public void setLstRealSerial(List<ApproveStockInspectDTO> lstRealSerial) {
        this.lstRealSerial = lstRealSerial;
    }

    public List<ApproveStockInspectDTO> getLstSysSerial() {
        return lstSysSerial;
    }

    public void setLstSysSerial(List<ApproveStockInspectDTO> lstSysSerial) {
        this.lstSysSerial = lstSysSerial;
    }

    public List<ApproveStockInspectDTO> getLstProductNotApprove() {
        return lstProductNotApprove;
    }

    public void setLstProductNotApprove(List<ApproveStockInspectDTO> lstProductNotApprove) {
        this.lstProductNotApprove = lstProductNotApprove;
    }

    public List<ApproveStockInspectDTO> getLstTotal() {
        return lstTotal;
    }

    public void setLstTotal(List<ApproveStockInspectDTO> lstTotal) {
        this.lstTotal = lstTotal;
    }
}
