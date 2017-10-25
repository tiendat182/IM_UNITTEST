package com.viettel.bccs.inventory.wsesb.service;

import java.util.ArrayList;

import com.viettel.bccs.inventory.dto.ApproveChangeProductDTO;
import com.viettel.bccs.inventory.dto.ChangeModelTransDTO;
import com.viettel.bccs.inventory.dto.StaffDTO;
import com.viettel.bccs.inventory.dto.StockTransDTO;
import com.viettel.bccs.inventory.model.ChangeModelTrans;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.RequiredRoleMap;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import com.viettel.bccs.inventory.service.ChangeModelTransService;

import java.util.List;
import java.lang.Long;

import org.springframework.stereotype.Service;
import org.apache.log4j.Logger;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.jws.WebMethod;

import org.springframework.beans.factory.annotation.Qualifier;

@Service("WsChangeModelTransServiceImpl")
public class WsChangeModelTransServiceImpl implements ChangeModelTransService {

    public static final Logger logger = Logger.getLogger(WsChangeModelTransServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private ChangeModelTransService client;

    @PostConstruct
    public void init() throws Exception {
        try {
            client = wsClientFactory.createWsClient(ChangeModelTransService.class);
        } catch (Exception ex) {
            logger.error("init", ex);
        }
    }

    @Override
    public ChangeModelTransDTO findOne(Long id) throws Exception {
        return client.findOne(id);
    }

    @Override
    public List<ChangeModelTransDTO> findAll() throws Exception {
        return client.findAll();
    }

    @Override
    public List<ChangeModelTransDTO> findByFilter(List<FilterRequest> filterList) throws Exception {
        return client.findByFilter(filterList);
    }

    @Override
    public Long count(List<FilterRequest> filterList) throws Exception {
        return client.count(filterList);
    }

    @Override
    public ChangeModelTransDTO save(ChangeModelTransDTO dto) throws Exception {
        return client.save(dto);
    }

    @Override
    public BaseMessage update(ChangeModelTransDTO dto) throws Exception {
        return client.update(dto);
    }

    @Override
    @WebMethod
    public List<ApproveChangeProductDTO> getLstChangeStockModel(ApproveChangeProductDTO searchInputDTO) throws LogicException, Exception {
        return client.getLstChangeStockModel(searchInputDTO);
    }

    @Override
    public void approveChangeProductKit(Long changeModelTransId, Long staffId, StockTransDTO stockTransVoficeDTO, RequiredRoleMap requiredRoleMap) throws LogicException, Exception {
        client.approveChangeProductKit(changeModelTransId, staffId, stockTransVoficeDTO, requiredRoleMap);
    }


    @Override
    @WebMethod
    public void approveChangeProduct(Long changeModelTransId, StaffDTO approveStaffDTO, StockTransDTO stockTransVoficeDTO, RequiredRoleMap requiredRoleMap) throws LogicException, Exception {
        client.approveChangeProduct(changeModelTransId, approveStaffDTO, stockTransVoficeDTO, requiredRoleMap);
    }

    @Override
    @WebMethod
    public void cancelChangeProduct(Long changeModelTransId, StaffDTO approveStaffDTO) throws LogicException, Exception {
        client.cancelChangeProduct(changeModelTransId, approveStaffDTO);
    }

    @Override
    @WebMethod
    public List<ChangeModelTrans> getLstCancelRequestThread(Long maxDay) throws LogicException, Exception {
        return client.getLstCancelRequestThread(maxDay);
    }

    @Override
    @WebMethod
    public List<ChangeModelTrans> getLstApproveRequestThread(Long maxDay) throws LogicException, Exception {
        return client.getLstApproveRequestThread(maxDay);
    }

    @Override
    public void processCancelRequest(Long changeModelTransId) throws LogicException, Exception {
        client.processCancelRequest(changeModelTransId);
    }

    @Override
    public void processApproveRequest(Long changeModelTransId) throws LogicException, Exception {
        client.processApproveRequest(changeModelTransId);
    }
    @Override
    public ChangeModelTransDTO updateChangeModelTransStatus(Long changeModelTransId, Long changeModelTransStatus) throws Exception, LogicException {
        return client.updateChangeModelTransStatus(changeModelTransId, changeModelTransStatus);
    }
}