package com.viettel.bccs.cc.dto;

import com.viettel.bccs.inventory.dto.CardNumberLockDTO;

import java.io.Serializable;
import java.util.List;

/**
 * Created by duongtv10 on 10/31/2015.
 */
public class HistoryInforCardNumberDTO implements Serializable {
    private String errorCode;
    private String errorDescription;
    private CardInfoProvisioningDTO cardInfoProvisioningDTO;
    private List<CardNumberLockDTO> lstCardNumberLock;
    private List<RefillInfoDTO> lstRefillInfoHistoryCardNumberOnOther;
    private List<RefillInfoDTO> lstRefillInfoHistoryCardNumberOnOCS;
    private StockCardFullDTO stockCardNumber;
    private List<CardInfoExtraDTO> listCardInfoExtra;
    private boolean result;

    public HistoryInforCardNumberDTO() {
        //HistoryInforCardNumberDTO
    }

    public CardInfoProvisioningDTO getCardInfoProvisioningDTO() {
        return cardInfoProvisioningDTO;
    }

    public void setCardInfoProvisioningDTO(CardInfoProvisioningDTO cardInfoProvisioningDTO) {
        this.cardInfoProvisioningDTO = cardInfoProvisioningDTO;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public List<CardNumberLockDTO> getLstCardNumberLock() {
        return lstCardNumberLock;
    }

    public void setLstCardNumberLock(List<CardNumberLockDTO> lstCardNumberLock) {
        this.lstCardNumberLock = lstCardNumberLock;
    }

    public List<RefillInfoDTO> getLstRefillInfoHistoryCardNumberOnOther() {
        return lstRefillInfoHistoryCardNumberOnOther;
    }

    public void setLstRefillInfoHistoryCardNumberOnOther(List<RefillInfoDTO> lstRefillInfoHistoryCardNumberOnOther) {
        this.lstRefillInfoHistoryCardNumberOnOther = lstRefillInfoHistoryCardNumberOnOther;
    }

    public List<RefillInfoDTO> getLstRefillInfoHistoryCardNumberOnOCS() {
        return lstRefillInfoHistoryCardNumberOnOCS;
    }

    public void setLstRefillInfoHistoryCardNumberOnOCS(List<RefillInfoDTO> lstRefillInfoHistoryCardNumberOnOCS) {
        this.lstRefillInfoHistoryCardNumberOnOCS = lstRefillInfoHistoryCardNumberOnOCS;
    }

    public StockCardFullDTO getStockCardNumber() {
        return stockCardNumber;
    }

    public void setStockCardNumber(StockCardFullDTO stockCardNumber) {
        this.stockCardNumber = stockCardNumber;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    public List<CardInfoExtraDTO> getListCardInfoExtra() {
        return listCardInfoExtra;
    }

    public void setListCardInfoExtra(List<CardInfoExtraDTO> listCardInfoExtra) {
        this.listCardInfoExtra = listCardInfoExtra;
    }
}
