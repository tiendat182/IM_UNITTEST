package com.viettel.bccs.inventory.wsesb.service;

import java.util.ArrayList;

import com.viettel.bccs.inventory.dto.*;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.data.*;
import com.viettel.fw.common.util.RequiredRoleMap;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.fw.dto.BaseDTO;
import com.viettel.ws.common.utils.*;

import com.viettel.bccs.inventory.service.ProductExchangeService;

import java.util.Date;
import java.util.List;
import java.lang.Long;

import org.springframework.stereotype.Service;
import org.apache.log4j.Logger;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Qualifier;

@Service("WsProductExchangeServiceImpl")
public class WsProductExchangeServiceImpl implements ProductExchangeService {

    public static final Logger logger = Logger.getLogger(WsProductExchangeServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private ProductExchangeService client;

    @PostConstruct
    public void init() throws Exception {
        try {
            client = wsClientFactory.createWsClient(ProductExchangeService.class);
        } catch (Exception ex) {
            logger.error("init", ex);
        }
    }

    @Override
    public ProductExchangeDTO findOne(Long id) throws Exception {
        return client.findOne(id);
    }

    @Override
    public List<ProductExchangeDTO> findAll() throws Exception {
        return client.findAll();
    }

    @Override
    public List<ProductExchangeDTO> findByFilter(List<FilterRequest> filterList) throws Exception {
        return client.findByFilter(filterList);
    }

    @Override
    public Long count(List<FilterRequest> filterList) throws Exception {
        return client.count(filterList);
    }

    @Override
    public ProductExchangeDTO create(ProductExchangeDTO dto) throws Exception {
        return client.create(dto);
    }

    @Override
    public ProductExchangeDTO update(ProductExchangeDTO dto) throws Exception {
        return client.update(dto);
    }

    @Override
    public void createProductExchange(ProductExchangeDTO productExchangeDTO, Long staffId) throws Exception, LogicException {
        client.createProductExchange(productExchangeDTO, staffId);
    }

    @Override
    public List<ProductExchangeDTO> searchProductExchange(ProductExchangeDTO productExchangeDTO) throws Exception, LogicException {
        return client.searchProductExchange(productExchangeDTO);
    }

    @Override
    public void validateCreateTallyOut(List<StockTransDetailDTO> lsDetailDTOs) throws Exception, LogicException {
        client.validateCreateTallyOut(lsDetailDTOs);
    }

    @Override
    public BaseMessageStockTrans createTallyOut(String mode, String type, StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, List<StockTransDetailDTO> lsDetailDTOs, RequiredRoleMap requiredRoleMap) throws Exception, LogicException {
        return client.createTallyOut(mode, type, stockTransDTO, stockTransActionDTO, lsDetailDTOs, requiredRoleMap);
    }

    @Override
    public void validateExistsProdExchange(Long prodOfferId, Long prodOfferIdNew, Date fromDate, Date toDate) throws Exception, LogicException {
        client.validateExistsProdExchange(prodOfferId, prodOfferIdNew, fromDate, toDate);
    }

    @Override
    public BaseMessageChangeModelTrans findApprovedRequestProductExchangeKit(Long shopId, Date fromDate, Date toDate) throws Exception, LogicException {
        return client.findApprovedRequestProductExchangeKit(shopId, fromDate, toDate);
    }

    @Override
    public BaseMessageChangeModelTransDetail findModelTransDetailByChangeModelTransId(Long changeModelTransId) throws Exception, LogicException {
        return client.findModelTransDetailByChangeModelTransId(changeModelTransId);
    }

    @Override
    public BaseMessageChangeModelTransSerial findModelTransSerialByChangeModelTransDetailId(Long changeModelTransDetailId) throws Exception, LogicException {
        return client.findModelTransSerialByChangeModelTransDetailId(changeModelTransDetailId);
    }

    @Override
    public BaseMessage updateChangeModelTransStatusDestroy(Long changeModelTransId) throws Exception, LogicException {
        return client.updateChangeModelTransStatusDestroy(changeModelTransId);
    }

    @Override
    public BaseMessage updateChangeModelTransStatusSuccess(Long changeModelTransId) throws Exception, LogicException {
        return client.updateChangeModelTransStatusSuccess(changeModelTransId);
    }
}