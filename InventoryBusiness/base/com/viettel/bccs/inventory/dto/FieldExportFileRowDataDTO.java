package com.viettel.bccs.inventory.dto;

import com.google.common.collect.Lists;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.dto.BaseDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AnhNV33 on 01/21/2016.
 */
public class FieldExportFileRowDataDTO extends BaseDTO {

    private StockKitDTO rowStockKitDTO1;
    private StockKitDTO rowStockKitDTO2;
    private StockKitDTO rowStockKitDTO3;
    private StockKitDTO rowStockKitDTO4;
    private StockKitDTO rowStockKitDTO5;

    public StockKitDTO getRowStockKitDTO1() {
        return rowStockKitDTO1;
    }

    public void setRowStockKitDTO1(StockKitDTO rowStockKitDTO1) {
        this.rowStockKitDTO1 = rowStockKitDTO1;
    }

    public StockKitDTO getRowStockKitDTO2() {
        return rowStockKitDTO2;
    }

    public void setRowStockKitDTO2(StockKitDTO rowStockKitDTO2) {
        this.rowStockKitDTO2 = rowStockKitDTO2;
    }

    public StockKitDTO getRowStockKitDTO3() {
        return rowStockKitDTO3;
    }

    public void setRowStockKitDTO3(StockKitDTO rowStockKitDTO3) {
        this.rowStockKitDTO3 = rowStockKitDTO3;
    }

    public StockKitDTO getRowStockKitDTO4() {
        return rowStockKitDTO4;
    }

    public void setRowStockKitDTO4(StockKitDTO rowStockKitDTO4) {
        this.rowStockKitDTO4 = rowStockKitDTO4;
    }

    public StockKitDTO getRowStockKitDTO5() {
        return rowStockKitDTO5;
    }

    public void setRowStockKitDTO5(StockKitDTO rowStockKitDTO5) {
        this.rowStockKitDTO5 = rowStockKitDTO5;
    }

    public boolean getBoolean1(StockKitDTO rowStockKitDTO1) {
        if (DataUtil.isNullObject(rowStockKitDTO1)
                || DataUtil.isNullOrZero(rowStockKitDTO1.getId())) {
            return false;
        }
        return true;
    }

    public boolean getBoolean2(StockKitDTO rowStockKitDTO2) {
        if (DataUtil.isNullObject(rowStockKitDTO2)
                || DataUtil.isNullOrZero(rowStockKitDTO2.getId())) {
            return false;
        }
        return true;
    }

    public boolean getBoolean3(StockKitDTO rowStockKitDTO3) {
        if (DataUtil.isNullObject(rowStockKitDTO3)
                || DataUtil.isNullOrZero(rowStockKitDTO3.getId())) {
            return false;
        }
        return true;
    }

    public boolean getBoolean4(StockKitDTO rowStockKitDTO4) {
        if (DataUtil.isNullObject(rowStockKitDTO4)
                || DataUtil.isNullOrZero(rowStockKitDTO4.getId())) {
            return false;
        }
        return true;
    }

    public boolean getBoolean5(StockKitDTO rowStockKitDTO5) {
        if (DataUtil.isNullObject(rowStockKitDTO5)
                || DataUtil.isNullOrZero(rowStockKitDTO5.getId())) {
            return false;
        }
        return true;
    }

}

