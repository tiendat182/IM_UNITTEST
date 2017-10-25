package com.viettel.bccs.inventory.mapper;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.bccs.inventory.dto.StockBalanceSerialDTO;
import com.viettel.bccs.inventory.model.StockBalanceSerial;
public class StockBalanceSerialMapper extends BaseMapper<StockBalanceSerial, StockBalanceSerialDTO> {
    @Override
    public StockBalanceSerialDTO toDtoBean(StockBalanceSerial model) {
        StockBalanceSerialDTO obj = null;
        if (model != null) {
           obj =   new StockBalanceSerialDTO(); 
            obj.setCreateDatetime(model.getCreateDatetime());
            obj.setFromSerial(model.getFromSerial());
            obj.setProdOfferId(model.getProdOfferId());
            obj.setStockBalanceDetail(model.getStockBalanceDetail());
            obj.setStockBalanceSerialId(model.getStockBalanceSerialId());
            obj.setStockRequestId(model.getStockRequestId());
            obj.setToSerial(model.getToSerial());
        }
        return obj;
    }
    @Override
    public StockBalanceSerial toPersistenceBean(StockBalanceSerialDTO dtoBean) {
        StockBalanceSerial obj = null;
        if (dtoBean != null) {
           obj =   new StockBalanceSerial(); 
            obj.setCreateDatetime(dtoBean.getCreateDatetime());
            obj.setFromSerial(dtoBean.getFromSerial());
            obj.setProdOfferId(dtoBean.getProdOfferId());
            obj.setStockBalanceDetail(dtoBean.getStockBalanceDetail());
            obj.setStockBalanceSerialId(dtoBean.getStockBalanceSerialId());
            obj.setStockRequestId(dtoBean.getStockRequestId());
            obj.setToSerial(dtoBean.getToSerial());
        }
        return obj;
    }
}
