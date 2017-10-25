package com.viettel.bccs.inventory.dto;

/**
 * Created by TienPH2 on 9/15/2016.
 */
public class ChangeGoodMessage extends BaseResult {


    ReportChangeHandsetDTO reportChangeHandsetDTO;

    Long newSaleTransId;
    Long adjustAmount;

    public ReportChangeHandsetDTO getReportChangeHandsetDTO() {
        return reportChangeHandsetDTO;
    }

    public void setReportChangeHandsetDTO(ReportChangeHandsetDTO reportChangeHandsetDTO) {
        this.reportChangeHandsetDTO = reportChangeHandsetDTO;
    }

    public Long getNewSaleTransId() {
        return newSaleTransId;
    }

    public void setNewSaleTransId(Long newSaleTransId) {
        this.newSaleTransId = newSaleTransId;
    }

    public Long getAdjustAmount() {
        return adjustAmount;
    }

    public void setAdjustAmount(Long adjustAmount) {
        this.adjustAmount = adjustAmount;
    }
}
