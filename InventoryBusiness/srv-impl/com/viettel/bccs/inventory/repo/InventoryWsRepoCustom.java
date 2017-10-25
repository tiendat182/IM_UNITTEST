package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.dto.StockBaseDTO;
import com.viettel.bccs.inventory.dto.StockTransSerialDTO;
import com.viettel.bccs.inventory.dto.ws.StockTotalWsDTO;

import java.util.List;

public interface InventoryWsRepoCustom {
    public List<StockTotalWsDTO> getStockTotalShop(Long ownerId,Long ownerType,Long type,String stockModelName) throws Exception;

    public List<StockTotalWsDTO> getLstStockTotalDetail(Long ownerId,List<Long> lstProdOfferId) throws Exception;

    public List<StockTransSerialDTO> getStockTransSerial(StockTotalWsDTO stockTotalWsDTO, int maxValue) throws  Exception;

    /**
     * ham check validet offer serial
     * @author thetm1
     * @param prodOfferTypeId
     * @param prodOfferId
     * @param serial
     * @return
     * @throws Exception
     */
    public StockBaseDTO checkValidOfferOfSerial(Long prodOfferTypeId, Long prodOfferId, String serial) throws Exception;
}