package com.viettel.bccs.inventory.wsesb.service;
import com.viettel.bccs.inventory.dto.FieldExportFileDTO;
import com.viettel.bccs.inventory.dto.StockDeliveringBillDetailDTO;
import com.viettel.bccs.inventory.dto.StockTransActionDTO;
import com.viettel.bccs.inventory.dto.VActionGoodsStatisticDTO;
import com.viettel.bccs.inventory.service.StockTransActionService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;
import org.springframework.stereotype.Service;
import org.apache.log4j.Logger;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.springframework.beans.factory.annotation.Autowired;
import javax.annotation.PostConstruct;
import javax.jws.WebMethod;

import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Date;
import java.util.List;

@Service("WsStockTransActionServiceImpl")
public class WsStockTransActionServiceImpl implements StockTransActionService {

    public static final Logger logger = Logger.getLogger(WsStockTransActionServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private StockTransActionService client;

    @PostConstruct
    public void init() throws Exception {
      try{
         client = wsClientFactory.createWsClient(StockTransActionService.class);
      } catch (Exception ex) {
          logger.error("init", ex);
      }
    }

    @Override
    @WebMethod
    public StockTransActionDTO findOne(Long id) throws Exception {
        return client.findOne(id);
    }

    @Override
    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return client.count(filters);
    }

    @Override
    @WebMethod
    public List<StockTransActionDTO> findByFilter(List<FilterRequest> filters) throws Exception {
        return client.findByFilter(filters);
    }

    @Override
    @WebMethod
    public StockTransActionDTO save(StockTransActionDTO stockTransActionDTO) throws Exception {
        return client.save(stockTransActionDTO);
    }

    @WebMethod
    @Override
    public Long getSequence() throws Exception {
        return client.getSequence();
    }

    @Override
    @WebMethod
    public boolean checkExist(List<FieldExportFileDTO> lstFieldExportIsdn) throws LogicException, Exception{
        return client.checkExist(lstFieldExportIsdn);
    }
    @Override
    @WebMethod
    public StockTransActionDTO getStockTransActionDTOById(Long actionId) throws LogicException, Exception{
        return client.getStockTransActionDTOById(actionId);
    }

    @Override
    @WebMethod
    public StockTransActionDTO findStockTransActionDTO(StockTransActionDTO stockTransActionDTO) {
        return client.findStockTransActionDTO(stockTransActionDTO);
    }

    @Override
    @WebMethod
    public void insertStockTransActionNative(StockTransActionDTO stockTransActionDTO) throws Exception {
        client.insertStockTransActionNative(stockTransActionDTO);
    }

    @Override
    @WebMethod
    public List<StockTransActionDTO> getStockTransActionByToOwnerId(Long toOnwerId, Long toOwnerType) throws Exception {
        return client.getStockTransActionByToOwnerId(toOnwerId, toOwnerType);
    }

    @Override
    @WebMethod
    public StockTransActionDTO getStockTransActionByIdAndStatus(Long stockTransId, List<String> lstStatus) throws Exception {
        return client.getStockTransActionByIdAndStatus(stockTransId, lstStatus);
    }

    @Override
    @WebMethod
    public int unlockUserInfo(Long stockTransActionId) throws Exception {
        return client.unlockUserInfo(stockTransActionId);
    }

    @Override
    @WebMethod
    public int deleteStockTransActionByIdAndStatus(Long stockTransId, List<String> lstStatus) throws Exception {
        return client.deleteStockTransActionByIdAndStatus(stockTransId, lstStatus);
    }

    @Override
    public List<StockDeliveringBillDetailDTO> getStockDeliveringResult(Date startTime, Date endTime) throws LogicException, Exception {
        return client.getStockDeliveringResult(startTime, endTime);
    }

    @Override
    public List<VActionGoodsStatisticDTO> getListVActionStatistic(String actionCode, Long ownerId, Long ownerType, Long ownerLoginId, Long ownerLoginType, Date fromDate, Date toDate, String transType, String typeOrderNote) throws LogicException, Exception {
        return client.getListVActionStatistic(actionCode, ownerId, ownerType, ownerLoginId, ownerLoginType, fromDate, toDate, transType, typeOrderNote);
    }

    @Override
    public int deleteStockTransAction(Long stockTransActionId) throws Exception {
        return client.deleteStockTransAction(stockTransActionId);
    }
}