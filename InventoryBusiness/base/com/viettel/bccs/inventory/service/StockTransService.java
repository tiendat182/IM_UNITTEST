package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.*;

import java.util.Date;
import java.util.List;

import com.viettel.bccs.sale.dto.SaleTransDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface StockTransService {

    @WebMethod
    public StockTransDTO findOne(Long id) throws Exception;

    @WebMethod
    public StockTransDTO findOneLock(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<StockTransDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public StockTransDTO save(StockTransDTO stockTransDTO) throws Exception;

    @WebMethod
    public List<VStockTransDTO> searchVStockTrans(VStockTransDTO dto) throws LogicException, Exception;

    /**
     * ham tim kiem list stock trans
     *
     * @param vStockTransDTO
     * @return
     * @throws Exception
     * @author ThanhNT
     */
    public List<VStockTransDTO> searchMaterialVStockTrans(VStockTransDTO vStockTransDTO) throws LogicException, Exception;

    /**
     * @param stockTransActionIds danh sach id cua action
     * @return
     * @throws LogicException, Exception
     * @author luannt23
     * @desc ham tim kiem chi tiet giao dich theo stock_trans_action_id
     */
    @WebMethod
    List<StockTransFullDTO> searchStockTransDetail(List<Long> stockTransActionIds) throws LogicException, Exception;


    /**
     * ham lay tong so hang trong cac giao dich treo cua kho nhan
     *
     * @param ownerId
     * @return
     * @author ThanhNT
     */
    public Long getTotalValueStockSusbpendByOwnerId(Long ownerId, String ownerType) throws LogicException, Exception;

    /**
     * @param actionId
     * @return
     * @throws LogicException
     * @throws Exception
     * @author LuanNT23
     */
    public StockTransDTO findStockTransByActionId(Long actionId) throws LogicException, Exception;

    /**
     * @param ownerId
     * @param ownerType
     * @return
     * @throws LogicException
     * @throws Exception
     * @author LuanNT23
     */
    public List<VStockTransDTO> findStockSuspendDetail(Long ownerId, String ownerType) throws LogicException, Exception;

    public List<ManageTransStockViewDto> findStockTrans(ManageTransStockDto transStockDto) throws LogicException, Exception;

    public List<ManageStockTransViewDetailDto> viewDetailStockTrans(Long stockTransId) throws Exception;

    public StockTransDTO checkAndLockReceipt(Long stockTransId) throws Exception;

    public int updateDepositStatus(Long stockTransId, String status) throws Exception;

    public Long getSequence() throws Exception;

    @WebMethod
    public List<VStockTransDTO> searchVStockTransAgent(VStockTransDTO dto) throws LogicException, Exception;

    @WebMethod
    public List<ManageTransStockViewDto> searchStockTransForTransfer(TranferIsdnInfoSearchDto transStockDto) throws LogicException, Exception;

    /**
     * hoangnt
     *
     * @param stockTransId
     * @param status
     * @param depositStatus
     * @return
     * @throws LogicException
     * @throws Exception
     */
    @WebMethod
    public boolean checkStatusRetrieveExpense(Long stockTransId, String status, String depositStatus) throws LogicException, Exception;

    /**
     * @param vStockTransDTO
     * @return
     * @throws LogicException
     * @throws Exception
     * @author LuanNT23
     */
    @WebMethod
    public List<VStockTransDTO> mmSearchVStockTrans(VStockTransDTO vStockTransDTO) throws LogicException, Exception;

    @WebMethod
    public boolean checkExistTransByfieldExportIsdnDTO(FieldExportFileDTO fieldExportFileDTO) throws LogicException, Exception;

    /**
     * ham tao stock trans
     *
     * @param createDate
     * @param stockTransId
     * @param pos
     * @param stockList
     * @param staff
     * @param revokeType
     * @throws LogicException
     * @throws Exception
     * @author thetm1
     */
    public void createStockTrans(Date createDate, Long stockTransId, ShopDTO pos, List<StockWarrantyDTO> stockList, StaffDTO staff, Long revokeType) throws LogicException, Exception;

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

    public void saveStockTransOffline(List<StockTransDTO> lsStockTransDTOs) throws LogicException, Exception;

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

    public boolean checkStockSusbpendForProcessStock(Long ownerId, String ownerType, Long productOfferId) throws LogicException, Exception;

    public List<SaleTransDTO> getSaleTransFromStockTrans(Long stockTransId) throws LogicException, Exception;


    public void saveAPDeployment(StaffDTO staffDTO, String account, String troubleType, List<StockTransDetailDTO> lstStockTransDetail, String transId) throws LogicException, Exception;

    public void restoreAPDeployment(StaffDTO staffDTO, String account, String troubleType, List<StockTransDetailDTO> lstStockTransDetail, String transId) throws LogicException, Exception;

    public List<StockTotalDTO> checkHaveProductOffering(Long ownerId, Long ownerType, boolean checkCollaborator) throws Exception;

    public List<String> checkStockTransSuppend(Long ownerId, Long ownerType) throws Exception;

    public Long getTransAmount(Long transID) throws Exception;

    public StockTransDTO getStockTransForLogistics(Long stockTransId) throws Exception;

    public boolean checkSaleTrans(Long stockTransId, Date saleTransDate) throws Exception;

    public List<DOATransferDTO> getContentFileDOA(Long stockTransId) throws Exception;

    public VStockTransDTO findVStockTransDTO(Long stockTransID, Long actionID) throws LogicException, Exception;

    public List<StockTransDTO> getLstRequestOrderExported() throws Exception;

}