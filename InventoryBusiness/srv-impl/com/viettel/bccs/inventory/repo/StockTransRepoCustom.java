package com.viettel.bccs.inventory.repo;

import com.mysema.query.types.Predicate;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;

import java.util.Date;
import java.util.List;

public interface StockTransRepoCustom {
    public Predicate toPredicate(List<FilterRequest> filters);

    /**
     * ham xu ly tim kiem stockTransFull theo stockTransID va list status
     *
     * @param lsStockTransId
     * @param lsStatusAction
     * @return
     * @throws Exception
     * @author ThanhNT
     */
    public List<StockTransFullDTO> getListStockFullDTOByTransIdAndStatus(List<Long> lsStockTransId, List<Long> lsStatusAction) throws Exception;

    /**
     * ham tim kiem list stock trans
     *
     * @param vStockTransDTO
     * @return
     * @throws Exception
     * @author ThanhNT
     */
    public List<VStockTransDTO> searchVStockTrans(VStockTransDTO vStockTransDTO) throws Exception;

    /**
     * ham tim kiem list stock trans
     *
     * @param vStockTransDTO
     * @return
     * @throws Exception
     * @author ThanhNT
     */
    public List<VStockTransDTO> searchMaterialVStockTrans(VStockTransDTO vStockTransDTO) throws Exception;

    public List<StockTransDTO> searchStockTrans(StockTransDTO stockTransDTO) throws Exception;

    /**
     * ham tim kiem chi tiet stock tran detail
     *
     * @param stockTransActionId
     * @return
     * @author LuanNT23
     */
    public List<StockTransFullDTO> searchStockTransDetail(Long stockTransActionId) throws Exception;

    /**
     * ham lay tong so hang trong cac giao dich treo cua kho nhan
     *
     * @param ownerId
     * @return
     * @author ThanhNT
     */
    public Long getTotalValueStockSusbpendByOwnerId(Long ownerId, String ownerType) throws Exception;

    /**
     * @param stockTransActionId
     * @return
     * @desc ham chia ra cho service khac goi
     * @author LuanNT23
     */
    public StockTransDTO findStockTransByActionId(Long stockTransActionId) throws Exception;

    /**
     * @param ownerId
     * @param ownerType
     * @return
     * @throws Exception
     * @author LuanNT23
     */
    public List<VStockTransDTO> findStockSuspendDetail(Long ownerId, String ownerType) throws Exception;


    public List<ManageTransStockViewDto> findStockTrans(ManageTransStockDto transStockDto) throws LogicException, Exception;

    public List<ManageStockTransViewDetailDto> viewDetailStockTrans(Long stockTransId) throws Exception;

    public StockTransDTO checkAndLockReceipt(Long stockTransId) throws Exception;

    public int updateDepositStatus(Long stockTransId) throws Exception;

    public void unlockUserInfo(Long transId) throws Exception;

    public Long getSequence() throws Exception;

    /**
     * ham lay thong tin giao dich ban dut
     *
     * @param vStockTransDTO
     * @return
     * @throws Exception
     */
    public List<VStockTransDTO> searchVStockTransAgent(VStockTransDTO vStockTransDTO) throws Exception;

    public List<ManageTransStockViewDto> searchStockTransForTransfer(TranferIsdnInfoSearchDto transStockDto) throws LogicException, Exception;

    /**
     * @param vStockTransDTO
     * @return
     * @throws Exception
     * @author LuanNT23
     */
    public List<VStockTransDTO> mmSearchVStockTrans(VStockTransDTO vStockTransDTO) throws Exception;

    public boolean checkExistTransByfieldExportIsdnDTO(FieldExportFileDTO fieldExportFileDTO) throws LogicException, Exception;

    /**
     * ham xu ly update stocktrans revoke
     *
     * @param prodOfferTypeId
     * @param prodOfferId
     * @param serial
     * @param ownerId
     * @param stateId
     * @param oldOwnerType
     * @param oldOwnerId
     * @param revokeType
     * @return
     * @throws LogicException
     * @throws Exception
     * @author thetm1
     */
    public boolean updateSerialForRevoke(
            Long prodOfferTypeId,
            Long prodOfferId,
            String serial,
            Long ownerId,
            Long stateId,
            Long oldOwnerType,
            Long oldOwnerId,
            Long revokeType) throws LogicException, Exception;

    /**
     * ham tra ve stock_trans_id boi ma lenh, ma phieu xuat
     *
     * @param cmdCode    : ma lenh
     * @param noteCode   : ma phieu
     * @param fromShopId
     * @param toShopId
     * @return
     * @throws Exception
     * @author thanhnt77
     */
    public Long getStockTransIdByCodeExist(String cmdCode, String noteCode, Long fromShopId, Long toShopId) throws Exception;

    public boolean checkStockSusbpendForProcessStock(Long ownerId, String ownerType, Long productOfferId) throws LogicException, Exception;

    /**
     * ham xu ly xoa serial
     *
     * @param tableName
     * @param fromSerial
     * @param toSerial
     * @param prodOfferId
     * @param stateId
     * @param ownerId
     * @param isCharacterSerial
     * @return
     * @throws LogicException
     * @throws Exception
     * @author thanhnt77
     */
    int deleteSerialExportRange(String tableName, String fromSerial, String toSerial, Long prodOfferId, Long stateId, Long ownerId, boolean isCharacterSerial) throws LogicException, Exception;

    /**
     * ham xu ly xoa serial tai im1
     *
     * @param tableName
     * @param fromSerial
     * @param toSerial
     * @param prodOfferId
     * @param stateId
     * @param ownerId
     * @param isHandset
     * @return
     * @throws LogicException
     * @throws Exception
     * @author thanhnt77
     */
    int deleteSerialIM1ExportRange(String tableName, String fromSerial, String toSerial, Long prodOfferId, Long stateId, Long ownerId, boolean isHandset) throws LogicException, Exception;

    public List<StockTotalDTO> checkHaveProductOffering(Long ownerId, Long ownerType, boolean checkCollaborator) throws Exception;

    public List<String> checkStockTransSuppend(Long ownerId, Long ownerType) throws Exception;

    public Long getTransAmount(Long transID) throws Exception;

    public StockTransDTO getStockTransForLogistics(Long stockTransId) throws Exception;

    public boolean checkSaleTrans(Long stockTransId, Date saleTransDate) throws Exception;

    public List<DOATransferDTO> getContentFileDOA(Long stockTransId) throws Exception;

    public VStockTransDTO findVStockTransDTO(Long transID, Long actionID) throws Exception;

    public List<StockTransDTO> getLstRequestOrderExported() throws Exception;

    public boolean checkSuspendStock(Long ownerType, Long ownerId) throws Exception;

    public boolean checkSuspendInspect(Long ownerType, Long ownerId, Long prodOfferId, Long stateId) throws Exception;
}