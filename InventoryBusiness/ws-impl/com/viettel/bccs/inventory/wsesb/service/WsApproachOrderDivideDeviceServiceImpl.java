package com.viettel.bccs.inventory.wsesb.service;

import com.viettel.bccs.inventory.dto.StockRequestDTO;
import com.viettel.bccs.inventory.dto.StockTransFullDTO;
import com.viettel.bccs.inventory.service.ApproachOrderDivideDeviceService;
import com.viettel.bccs.inventory.service.OrderDivideDeviceService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.jws.WebMethod;
import java.util.Date;
import java.util.List;

/**
 * Created by hoangnt14 on 5/4/2017.
 */
@Service("WsApproachOrderDivideDeviceServiceImpl")
public class WsApproachOrderDivideDeviceServiceImpl implements ApproachOrderDivideDeviceService {

    public static final Logger logger = Logger.getLogger(WsApproachOrderDivideDeviceServiceImpl.class);
    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private ApproachOrderDivideDeviceService client;

    @PostConstruct
    public void init() throws Exception {
        try {
            client = wsClientFactory.createWsClient(ApproachOrderDivideDeviceService.class);
        } catch (Exception ex) {
            logger.error("init", ex);
        }
    }

    @Override
    @WebMethod
    public List<StockRequestDTO> getListOrderDivideDevicePendingApproach(String shopPath, Date fromDate, Date toDate, Long ownerId, String requestCode, String status) throws Exception {
        return client.getListOrderDivideDevicePendingApproach(shopPath, fromDate, toDate, ownerId, requestCode, status);
    }

    @Override
    @WebMethod
    public void approachOrderDivide(StockRequestDTO stockRequestDTOSelected) throws LogicException, Exception {
        client.approachOrderDivide(stockRequestDTOSelected);
    }

    @Override
    @WebMethod
    public void rejectOrderDivide(StockRequestDTO stockRequestDTOSelected, List<StockTransFullDTO> stockTransFullDTOList, Long userRejectId) throws LogicException, Exception {
        client.rejectOrderDivide(stockRequestDTOSelected, stockTransFullDTOList, userRejectId);
    }
}
