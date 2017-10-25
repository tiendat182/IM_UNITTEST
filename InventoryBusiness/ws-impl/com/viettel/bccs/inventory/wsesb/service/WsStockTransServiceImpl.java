package com.viettel.bccs.inventory.wsesb.service;

import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.StockTransService;
import com.viettel.bccs.sale.dto.SaleTransDTO;
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

@Service("WsStockTransServiceImpl")
public class WsStockTransServiceImpl implements StockTransService {

    public static final Logger logger = Logger.getLogger(WsStockTransServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private StockTransService client;

    @PostConstruct
    public void init() throws Exception {
        try {
            client = wsClientFactory.createWsClient(StockTransService.class);
        } catch (Exception ex) {
            logger.error("init", ex);
        }
    }

    @Override
    @WebMethod
    public StockTransDTO findOne(Long id) throws Exception {
        return client.findOne(id);
    }

    @Override
    @WebMethod
    public StockTransDTO findOneLock(Long id) throws Exception {
        return client.findOneLock(id);
    }

    @Override
    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return client.count(filters);
    }

    @Override
    @WebMethod
    public List<StockTransDTO> findByFilter(List<FilterRequest> filters) throws Exception {
        return client.findByFilter(filters);
    }

    @Override
    @WebMethod
    public StockTransDTO save(StockTransDTO stockTransDTO) throws Exception {
        return client.save(stockTransDTO);
    }

    @Override
    public List<VStockTransDTO> searchMaterialVStockTrans(VStockTransDTO vStockTransDTO) throws LogicException, Exception {
        return client.searchMaterialVStockTrans(vStockTransDTO);
    }

    @Override
    @WebMethod
    public List<VStockTransDTO> searchVStockTrans(VStockTransDTO dto) throws LogicException, Exception {
        return client.searchVStockTrans(dto);
    }

    @Override
    @WebMethod
    public List<StockTransFullDTO> searchStockTransDetail(List<Long> stockTransActionIds) throws LogicException, Exception {
        return client.searchStockTransDetail(stockTransActionIds);
    }

    @Override
    public Long getTotalValueStockSusbpendByOwnerId(Long ownerId, String ownerType) throws LogicException, Exception {
        return client.getTotalValueStockSusbpendByOwnerId(ownerId, ownerType);
    }

    @Override
    public StockTransDTO findStockTransByActionId(Long actionId) throws LogicException, Exception {
        return client.findStockTransByActionId(actionId);
    }

    @Override
    public List<VStockTransDTO> findStockSuspendDetail(Long ownerId, String ownerType) throws LogicException, Exception {
        return client.findStockSuspendDetail(ownerId, ownerType);
    }

    @Override
    public List<ManageTransStockViewDto> findStockTrans(ManageTransStockDto transStockDto) throws LogicException, Exception {
        return client.findStockTrans(transStockDto);
    }

    @Override
    public List<ManageStockTransViewDetailDto> viewDetailStockTrans(Long stockTransId) throws Exception {
        return client.viewDetailStockTrans(stockTransId);
    }

    @Override
    public StockTransDTO checkAndLockReceipt(Long stockTransId) throws Exception {
        return client.checkAndLockReceipt(stockTransId);
    }

    @Override
    public int updateDepositStatus(Long stockTransId, String status) throws Exception {
        return client.updateDepositStatus(stockTransId, status);
    }

    @Override
    public Long getSequence() throws Exception {
        return client.getSequence();
    }

    @Override
    @WebMethod
    public List<VStockTransDTO> searchVStockTransAgent(VStockTransDTO dto) throws LogicException, Exception {
        return client.searchVStockTransAgent(dto);
    }

    @Override
    @WebMethod
    public List<ManageTransStockViewDto> searchStockTransForTransfer(TranferIsdnInfoSearchDto transStockDto) throws LogicException, Exception {
        return client.searchStockTransForTransfer(transStockDto);
    }

    @Override
    public boolean checkStatusRetrieveExpense(Long stockTransId, String status, String depositStatus) throws LogicException, Exception {
        return client.checkStatusRetrieveExpense(stockTransId, status, depositStatus);
    }

    @Override
    @WebMethod
    public List<VStockTransDTO> mmSearchVStockTrans(VStockTransDTO vStockTransDTO) throws LogicException, Exception {
        return client.mmSearchVStockTrans(vStockTransDTO);
    }

    @Override
    @WebMethod
    public boolean checkExistTransByfieldExportIsdnDTO(FieldExportFileDTO fieldExportFileDTO) throws LogicException, Exception {
        return client.checkExistTransByfieldExportIsdnDTO(fieldExportFileDTO);
    }

    @Override
    public void createStockTrans(Date createDate, Long stockTransId, ShopDTO pos, List<StockWarrantyDTO> stockList, StaffDTO staff, Long revokeType) throws LogicException, Exception {
        client.createStockTrans(createDate, stockTransId, pos, stockList, staff, revokeType);
    }

    @Override
    public Long getStockTransIdByCodeExist(String cmdCode, String noteCode, Long fromShopId, Long toShopId) throws Exception {
        return client.getStockTransIdByCodeExist(cmdCode, noteCode, fromShopId, toShopId);
    }

    @Override
    public void saveStockTransOffline(List<StockTransDTO> lsStockTransDTOs) throws LogicException, Exception {
        client.saveStockTransOffline(lsStockTransDTOs);
    }

    @Override
    public List<StockTransFullDTO> getListStockFullDTOByTransIdAndStatus(List<Long> lsStockTransId, List<Long> lsStatusAction) throws Exception {
        return client.getListStockFullDTOByTransIdAndStatus(lsStockTransId, lsStatusAction);
    }

    @Override
    public boolean checkStockSusbpendForProcessStock(Long ownerId, String ownerType, Long productOfferId) throws LogicException, Exception {
        return client.checkStockSusbpendForProcessStock(ownerId, ownerType, productOfferId);
    }

    @Override
    public List<SaleTransDTO> getSaleTransFromStockTrans(Long stockTransId) throws LogicException, Exception {
        return client.getSaleTransFromStockTrans(stockTransId);
    }

    @Override
    public void saveAPDeployment(StaffDTO staffDTO, String account, String troubleType, List<StockTransDetailDTO> lstStockTransDetail, String transId) throws LogicException, Exception {
        client.saveAPDeployment(staffDTO, account, troubleType, lstStockTransDetail, transId);
    }

    @Override
    public List<String> checkStockTransSuppend(Long ownerId, Long ownerType) throws Exception {
        return client.checkStockTransSuppend(ownerId, ownerType);
    }

    @Override
    public List<StockTotalDTO> checkHaveProductOffering(Long ownerId, Long ownerType, boolean checkCollaborator) throws Exception {
        return client.checkHaveProductOffering(ownerId, ownerType, checkCollaborator);
    }

    @Override
    public List<DOATransferDTO> getContentFileDOA(Long stockTransId) throws Exception {
        return client.getContentFileDOA(stockTransId);
    }

    @Override
    public boolean checkSaleTrans(Long stockTransId, Date saleTransDate) throws Exception {
        return client.checkSaleTrans(stockTransId, saleTransDate);
    }

    @Override
    public StockTransDTO getStockTransForLogistics(Long stockTransId) throws Exception {
        return client.getStockTransForLogistics(stockTransId);
    }

    @Override
    public Long getTransAmount(Long transID) throws Exception {
        return client.getTransAmount(transID);
    }

    @Override
    public VStockTransDTO findVStockTransDTO(Long stockTransID, Long actionID) throws LogicException, Exception {
        return client.findVStockTransDTO(stockTransID, actionID);
    }

    @Override
    public List<StockTransDTO> getLstRequestOrderExported() throws Exception {
        return client.getLstRequestOrderExported();
    }

    @Override
    public void restoreAPDeployment(StaffDTO staffDTO, String account, String troubleType, List<StockTransDetailDTO> lstStockTransDetail, String transId) throws LogicException, Exception {
        client.restoreAPDeployment(staffDTO, account, troubleType, lstStockTransDetail, transId);
    }
}