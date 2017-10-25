package com.viettel.bccs.inventory.wsesb.service;

import com.viettel.bccs.inventory.dto.ProductOfferingDTO;
import com.viettel.bccs.inventory.dto.ProductOfferingTotalDTO;
import com.viettel.bccs.inventory.dto.StockRequestDTO;
import com.viettel.bccs.inventory.service.DeviceConfigService;
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
@Service("WsOrderDivideDeviceServiceImpl")
public class WsOrderDivideDeviceServiceImpl implements OrderDivideDeviceService {

    public static final Logger logger = Logger.getLogger(WsOrderDivideDeviceServiceImpl.class);
    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private OrderDivideDeviceService client;

    @PostConstruct
    public void init() throws Exception {
        try {
            client = wsClientFactory.createWsClient(OrderDivideDeviceService.class);
        } catch (Exception ex) {
            logger.error("init", ex);
        }
    }

    @Override
    @WebMethod
    public List<StockRequestDTO> getListOrderDivideDevice(String shopPath, Date fromDate, Date toDate, Long ownerId, String requestCode, String status) throws Exception {
        return client.getListOrderDivideDevice(shopPath, fromDate, toDate, ownerId, requestCode, status);
    }

    @Override
    @WebMethod
    public List<ProductOfferingTotalDTO> getLsProductOfferingDTO(String inputSearch, Long ownerId, String ownerType, String stateId) throws Exception {
        return client.getLsProductOfferingDTO(inputSearch, ownerId, ownerType, stateId);
    }

    @Override
    @WebMethod
    public void createOrderDivideDevice(StockRequestDTO stockRequestDTO) throws LogicException, Exception {
        client.createOrderDivideDevice(stockRequestDTO);
    }

    @Override
    @WebMethod
    public byte[] getAttachFileContent(Long stockRequestId) throws Exception {
        return client.getAttachFileContent(stockRequestId);
    }

    @Override
    @WebMethod
    public ProductOfferingDTO getProductByCodeAndProbType(String code, Long probTypeId) {
        return client.getProductByCodeAndProbType(code, probTypeId);
    }
}
