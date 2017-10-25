package com.viettel.bccs.inventory.dto.ws;/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import com.viettel.bccs.inventory.dto.StockHandsetDTO;
import com.viettel.bccs.inventory.dto.StockTotalFullDTO;
import com.viettel.bccs.inventory.dto.ws.StockTotalWsDTO;
import com.viettel.fw.dto.BaseMessage;

import java.util.List;

/**
 *
 * @author thaont19
 */
public class StockTotalResult extends BaseMessage {
    private List<StockTotalWsDTO> lstStockGoods;
    private List<StockTotalFullDTO> lstProduct;
    private List<String> lstSerial;
    private List<StockHandsetDTO> lstSerialHandset;
    private List<String> lstError;

    public List<String> getLstError() {
        return lstError;
    }

    public void setLstError(List<String> lstError) {
        this.lstError = lstError;
    }

    public StockTotalResult() {
    }

    public List<StockTotalWsDTO> getLstStockGoods() {
        return lstStockGoods;
    }

    public void setLstStockGoods(List<StockTotalWsDTO> lstStockGoods) {
        this.lstStockGoods = lstStockGoods;
    }

    public List<StockTotalFullDTO> getLstProduct() {
        return lstProduct;
    }

    public void setLstProduct(List<StockTotalFullDTO> lstProduct) {
        this.lstProduct = lstProduct;
    }

    public List<String> getLstSerial() {
        return lstSerial;
    }

    public void setLstSerial(List<String> lstSerial) {
        this.lstSerial = lstSerial;
    }

    public List<StockHandsetDTO> getLstSerialHandset() {
        return lstSerialHandset;
    }

    public void setLstSerialHandset(List<StockHandsetDTO> lstSerialHandset) {
        this.lstSerialHandset = lstSerialHandset;
    }
}
