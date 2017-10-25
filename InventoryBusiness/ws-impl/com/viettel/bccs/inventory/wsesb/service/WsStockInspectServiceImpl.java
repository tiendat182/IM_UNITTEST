package com.viettel.bccs.inventory.wsesb.service;

import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.StockInspectService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.jws.WebMethod;
import java.util.Date;
import java.util.List;

@Service("WsStockInspectServiceImpl")
public class WsStockInspectServiceImpl implements StockInspectService {

    public static final Logger logger = Logger.getLogger(WsStockInspectServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private StockInspectService client;

    @PostConstruct
    public void init() throws Exception {
        try {
            client = wsClientFactory.createWsClient(StockInspectService.class);
        } catch (Exception ex) {
            logger.error("init", ex);
        }
    }

    @WebMethod
    @Override
    public StockInspectDTO findOne(Long id) throws Exception {
        return client.findOne(id);
    }

    @WebMethod
    @Override
    public List<StockInspectDTO> findAll() throws Exception {
        return client.findAll();
    }

    @WebMethod
    @Override
    public List<StockInspectDTO> findByFilter(List<FilterRequest> filterList) throws Exception {
        return client.findByFilter(filterList);
    }

    @WebMethod
    @Override
    public Long count(List<FilterRequest> filterList) throws Exception {
        return client.count(filterList);
    }

    @WebMethod
    @Override
    public BaseMessage update(StockInspectDTO dto) throws Exception {
        return client.update(dto);
    }

    @Override
    public List<Date> getFromDateToDateCheck(boolean isSpecConfirmQuantity) throws Exception {
        return client.getFromDateToDateCheck(isSpecConfirmQuantity);
    }

    @Override
    public StockInspectExportDTO exportStockInspect(Long ownerType, Long ownerId, Long prodOfferTypeId, Long stateId, boolean isSpecConfirmQuantity) throws LogicException, Exception {
        return client.exportStockInspect(ownerType, ownerId, prodOfferTypeId, stateId, isSpecConfirmQuantity);
    }

    @Override
    @WebMethod
    public boolean isFinishStockInspect(Long ownerType, Long ownerId, Long stateId, Long productOfferTypeId, Long prodOfferId, Date fromDate, Date toDate) throws LogicException, Exception {
        return client.isFinishStockInspect(ownerType, ownerId, stateId, productOfferTypeId, prodOfferId, fromDate, toDate);
    }

    @Override
    public boolean isFinishStockInspectNotStockTotal(Long ownerType, Long ownerId, Long stateId, Long productOfferTypeId, Long prodOfferId, Date fromDate, Date toDate) throws LogicException, Exception {
        return client.isFinishStockInspectNotStockTotal(ownerType, ownerId, stateId, productOfferTypeId, prodOfferId, fromDate, toDate);
    }

    @Override
    @WebMethod
    public boolean isFinishNotCheckStockTotal(Long ownerType, Long ownerId, Long stateId, Long productOfferTypeId, Long prodOfferId, Date fromDate, Date toDate, Long approveStatus) throws LogicException, Exception {
        return client.isFinishNotCheckStockTotal(ownerType, ownerId, stateId, productOfferTypeId, prodOfferId, fromDate, toDate, approveStatus);
    }

    @Override
    @WebMethod
    public Long getQuantityStockTotal(Long ownerType, Long ownerId, Long productOfferTypeId, Long prodOfferId, Long stateId) throws LogicException, Exception {
        return client.getQuantityStockTotal(ownerType, ownerId, productOfferTypeId, prodOfferId, stateId);
    }

    @Override
    @WebMethod
    public void validateSaveStockInspect(Long prodOfferId, Long productOfferTypeId, Long stateId, Long ownerType, Long ownerId, List<StockInspectCheckDTO> stockInspectList, StaffDTO userLogin, StaffDTO approveStaff, Date fromDate, Date toDate, boolean isInspectQuantity, boolean isApprover) throws LogicException, Exception {
        client.validateSaveStockInspect(prodOfferId, productOfferTypeId, stateId, ownerType, ownerId, stockInspectList, userLogin, approveStaff, fromDate, toDate, isInspectQuantity, isApprover);
    }

    @Override
    @WebMethod
    public void saveStockInspect(Long prodOfferId, Long productOfferTypeId, Long stateId, Long ownerType, Long ownerId, List<StockInspectCheckDTO> stockInspectList, boolean isApprover, StaffDTO staffDTO, StaffDTO approveStaff, boolean isInspectQuantity) throws LogicException, Exception {
        client.saveStockInspect(prodOfferId, productOfferTypeId, stateId, ownerType, ownerId, stockInspectList, isApprover, staffDTO, approveStaff, isInspectQuantity);
    }

    @Override
    @WebMethod
    public List<StockInspectCheckDTO> addSerialToListManual(Long productOfferTypeId, Long prodOfferId, Long ownerType, Long ownerId, Long stateId, String singleSerial, String fromSerial, String toSerial, List<StockInspectCheckDTO> lstSerial) throws LogicException, Exception {
        return client.addSerialToListManual(productOfferTypeId, prodOfferId, ownerType, ownerId, stateId, singleSerial, fromSerial, toSerial, lstSerial);
    }

    @Override
    @WebMethod
    public void validateAddSerialToList(String singleSerial, String fromSerial, String toSerial, List<StockInspectCheckDTO> lstSerial, String tableName) throws LogicException, Exception {
        client.validateAddSerialToList(singleSerial, fromSerial, toSerial, lstSerial, tableName);
    }

    @Override
    @WebMethod
    public StockInspectDTO save(StockInspectDTO productSpecCharacterDTO) throws Exception {
        return client.save(productSpecCharacterDTO);
    }

    @Override
    @WebMethod
    public List<ApproveStockInspectDTO> searchApproveInspect(Long approveUserId, Long ownerId, Long ownerType, Long approveStatus, Long prodOfferTypeId, String code, Date fromDate, Date toDate) throws Exception {
        return client.searchApproveInspect(approveUserId, ownerId, ownerType, approveStatus, prodOfferTypeId, code, fromDate, toDate);
    }

    @Override
    @WebMethod
    public List<StockInspectCheckDTO> checkStockInspect(StockInspectDTO stockInspectDTO, StaffDTO staffDTO, boolean isInspectQuantity) throws LogicException, Exception {
        return client.checkStockInspect(stockInspectDTO, staffDTO, isInspectQuantity);
    }

    @Override
    @WebMethod
    public void validateConfigDate(boolean isInspectQuantity) throws LogicException, Exception {
        client.validateConfigDate(isInspectQuantity);
    }

    @Override
    @WebMethod
    public void validateStockInspect(StockInspectDTO stockInspectDTO, StaffDTO staffDTO, Date fromDate, Date toDate, boolean isInspectQuantity) throws LogicException, Exception {
        client.validateStockInspect(stockInspectDTO, staffDTO, fromDate, toDate, isInspectQuantity);
    }

    @Override
    @WebMethod
    public BaseMessage updateApprove(List<StockInspectDTO> lsStockInspectDTO) throws LogicException, Exception {
        return client.updateApprove(lsStockInspectDTO);
    }


    @Override
    @WebMethod
    public void updateApproveStaffNew(Long productOfferTypeId, Long prodOfferId, Long stateId, Long ownerType, Long ownerId, StaffDTO staffDTO, StaffDTO approveStaff, boolean isSpecConfirmQuantity) throws LogicException, Exception {
        client.updateApproveStaffNew(productOfferTypeId, prodOfferId, stateId, ownerType, ownerId, staffDTO, approveStaff, isSpecConfirmQuantity);
    }

    @Override
    @WebMethod
    public List<StockInspectCheckDTO> addSerialToList2D(Long productOfferTypeId, Long prodOfferId, Long ownerType, Long ownerId, Long stateId, String serial2D, List<StockInspectCheckDTO> lstSerial) throws LogicException, Exception {
        return client.addSerialToList2D(productOfferTypeId, prodOfferId, ownerType, ownerId, stateId, serial2D, lstSerial);
    }

    @Override
    @WebMethod
    public void validateAddSerialToList2D(List<String> lstSerialInput, List<StockInspectCheckDTO> lstSerialTable, String tableName) throws LogicException, Exception {
        client.validateAddSerialToList2D(lstSerialInput, lstSerialTable, tableName);
    }
}