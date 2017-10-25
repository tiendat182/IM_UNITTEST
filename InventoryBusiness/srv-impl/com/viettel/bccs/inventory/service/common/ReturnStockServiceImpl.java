package com.viettel.bccs.inventory.service.common;

import com.google.common.collect.Lists;
import com.viettel.bccs.im1.dto.ShopIm1DTO;
import com.viettel.bccs.im1.dto.StaffIm1DTO;
import com.viettel.bccs.im1.service.ShopIm1Service;
import com.viettel.bccs.im1.service.StaffIm1Service;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.model.StockTransAction;
import com.viettel.bccs.inventory.model.StockTransSerial;
import com.viettel.bccs.inventory.service.*;
import com.viettel.bccs.inventory.service.BaseStock.BaseStockService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.RequiredRoleMap;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.fw.service.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.jws.WebMethod;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

/**
 * Created by Dungha7 on 9/27/2016.
 */
@Service
public class ReturnStockServiceImpl extends BaseStockService implements ReturnStockService {
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;
    @Autowired
    private StockRequestService stockRequestService;
    @Autowired
    private StaffService staffService;
    @Autowired
    private StockTransService stockTransService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private StockTransActionService stockTransActionService;
    @Autowired
    private ProductOfferingService productOfferingService;
    @Autowired
    private StockTransDetailService stockTransDetailService;
    @Autowired
    private StockTransSerialService stockTransSerialService;
    @Autowired
    private OptionSetValueService optionSetValueService;
    @Autowired
    private StaffIm1Service staffIm1Service;
    @Autowired
    private ShopIm1Service shopIm1Service;

    public void validateCreateRequest(StockRequestDTO stockRequestDTO) throws Exception, LogicException {
        if (DataUtil.isNullObject(stockRequestDTO)
                || DataUtil.isNullOrEmpty(stockRequestDTO.getRequestCode())
                || DataUtil.isNullOrZero(stockRequestDTO.getStockTransId())
                || DataUtil.isNullOrZero(stockRequestDTO.getOwnerId())) {
            throw new LogicException("", getText("mn.return.stock.create.request.validate"));
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void createRequestReturnStock(StockTransDTO stockTransDTO) throws Exception, LogicException {
        StockRequestDTO stockRequestDTO = new StockRequestDTO();
        stockRequestDTO.setStockTransId(stockTransDTO.getStockTransId());
        stockRequestDTO.setRequestCode(stockTransDTO.getRequest());
        stockRequestDTO.setStatus(Const.STATUS.NO_ACTIVE);
        stockRequestDTO.setOwnerType(Const.OWNER_TYPE.SHOP_LONG);
        stockRequestDTO.setOwnerId(stockTransDTO.getFromOwnerId());
        stockRequestDTO.setRequestType(Const.SALE_TRANS.REQUEST_TYPE_CHANGE_GOODS);
        stockRequestDTO.setCreateUser(stockTransDTO.getStaffCode());
        stockRequestDTO.setUpdateUser(stockTransDTO.getStaffCode());
        stockRequestDTO.setCreateDatetime(stockTransDTO.getCreateDatetime());
        stockRequestDTO.setUpdateDatetime(stockTransDTO.getCreateDatetime());

        validateCreateRequest(stockRequestDTO);
        BaseMessage baseMessage = stockRequestService.save(stockRequestDTO);
        if (!baseMessage.isSuccess()) {
            throw new LogicException("", getText("mn.return.stock.create.request.error"));
        }
    }

    public void validateCreateReturnStockTrans(StockTransDTO stockTransDTO) throws Exception, LogicException {
        if (DataUtil.isNullObject(stockTransDTO)
                || DataUtil.isNullOrZero(stockTransDTO.getStaffId())
                || DataUtil.isNullOrZero(stockTransDTO.getShopId())) {
            throw new LogicException("", getText("mn.return.stock.create.request.validate"));
        }
        if (DataUtil.isNullOrZero(stockTransDTO.getFromOwnerId())) {
            ShopDTO shopDTO = shopService.findOne(stockTransDTO.getShopId());
            ShopDTO parentShopDTO = shopService.findOne(shopDTO.getShopId());
            if (DataUtil.isNullObject(parentShopDTO)
                    || DataUtil.isNullOrZero(parentShopDTO.getShopId())) {
                throw new LogicException("", getText("mn.return.stock.create.request.validate"));
            }
        }
    }

    public StockTransDTO createReturnStockTrans(StockTransDTO stockTransDTO) throws Exception, LogicException {
        validateCreateReturnStockTrans(stockTransDTO);
        StockTransDTO saveStockTransDTO = new StockTransDTO();
        saveStockTransDTO.setFromOwnerId(stockTransDTO.getShopId());
        saveStockTransDTO.setFromOwnerType(Const.OWNER_TYPE.SHOP_LONG);
        saveStockTransDTO.setToOwnerId(stockTransDTO.getToOwnerId());
        saveStockTransDTO.setToOwnerType(Const.OWNER_TYPE.SHOP_LONG);
        saveStockTransDTO.setStatus(Const.STATUS.NO_ACTIVE);
        saveStockTransDTO.setCheckErp(Const.STRING_CONST_ZERO);
        saveStockTransDTO.setStockTransType(Const.STOCK_TRANS_TYPE.EXPORT);
        saveStockTransDTO.setCreateDatetime(stockTransDTO.getCreateDatetime());
        saveStockTransDTO.setNote(stockTransDTO.getNote());
        StockTransDTO stockTransSaveDTO = stockTransService.save(saveStockTransDTO);
        stockTransDTO.setStockTransId(saveStockTransDTO.getStockTransId());

        return stockTransSaveDTO;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    @WebMethod
    public StockTransDTO saveReturnStock(StockTransDTO stockTransDTO, List<StockTransDetailDTO> listTransDetailDTOs, RequiredRoleMap requiredRoleMap) throws Exception, LogicException {
        StaffDTO staffDTO = staffService.findOne(stockTransDTO.getStaffId());
        if (DataUtil.isNullObject(staffDTO)
                || DataUtil.isNullOrZero(staffDTO.getStaffId())) {
            throw new LogicException("", "", getText("mn.return.stock.create.return.stock.staff.err"));
        }
        //save SaleTrans
        stockTransDTO.setCreateDatetime(DbUtil.getSysDate(em));
        stockTransDTO.setNote(DataUtil.defaultIfNull(stockTransDTO.getNote(), getText("mn.return.stock.create.return.note")));
        if (DataUtil.isNullOrEmpty(stockTransDTO.getActionCode())) {
            stockTransDTO.setActionCode(staffService.getTransCode(Const.STOCK_TRANS.TRANS_CODE_PX, Const.STOCK_TRANS_TYPE.EXPORT, staffDTO));
        }
        //createStockTrans
        StockTransDTO stockTransSaveDTO = createReturnStockTrans(stockTransDTO);

        boolean isCheckIm1 = false;
        List<OptionSetValueDTO> lstConfigEnableBccs1 = optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1");
        if (!DataUtil.isNullOrEmpty(lstConfigEnableBccs1) && !DataUtil.isNullObject(lstConfigEnableBccs1.get(0).getValue())) {
            isCheckIm1 = Const.ENABLE_UPDATE_BCCS1.equals(DataUtil.safeToLong(lstConfigEnableBccs1.get(0).getValue()));
        }

        Long stockTransId = stockTransSaveDTO.getStockTransId();
        stockTransDTO.setStockTransId(stockTransId);
        //createRequest
        createRequestReturnStock(stockTransDTO);
        //createStockTransAction
        StockTransActionDTO stockTransActionSaveDTO =
                createStockTransAction(stockTransId, stockTransDTO.getStaffId(), stockTransDTO.getStaffCode(), stockTransDTO.getCreateDatetime(), stockTransDTO.getActionCode());
        stockTransDTO.setStockTransActionId(stockTransActionSaveDTO.getStockTransActionId());
        //create stock_trans_detail
        List<StockTransDetailDTO> lsDetail = Lists.newArrayList();
        for (StockTransDetailDTO stockTransDetailDTO : listTransDetailDTOs) {
            lsDetail.add(createStockTransDetail(stockTransDTO, stockTransDetailDTO, isCheckIm1));
            //executeStockTotal(stockTransDTO, stockTransDetailDTO, stockTransActionSaveDTO, isCheckIm1);
        }

        FlagStockDTO flagStockDTO = new FlagStockDTO();
        flagStockDTO.setExportStock(true);
        flagStockDTO.setExpAvailableQuantity(Const.STOCK_TOTAL.MINUS_QUANTITY);
        flagStockDTO.setExpCurrentQuantity(Const.STOCK_TOTAL.MINUS_QUANTITY);


        doSaveStockTotal(stockTransSaveDTO, lsDetail, flagStockDTO, stockTransActionSaveDTO);

        doIncreaseStockNum(stockTransActionSaveDTO, Const.STOCK_TRANS.TRANS_CODE_PX, requiredRoleMap);

        return stockTransDTO;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void doIncreaseStockNum(StockTransActionDTO stockTransActionDTO, String prefixActionCode, RequiredRoleMap requiredRoleMap) throws Exception {

        if (DataUtil.isNullOrEmpty(prefixActionCode)) {
            return;
        }
        // Cap nhat du lieu sang IM1.
        List<OptionSetValueDTO> lstConfigEnableBccs1 = optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1");
        if (!DataUtil.isNullOrEmpty(lstConfigEnableBccs1)
                && !DataUtil.isNullObject(lstConfigEnableBccs1.get(0).getValue())
                && Const.ENABLE_UPDATE_BCCS1.equals(DataUtil.safeToLong(lstConfigEnableBccs1.get(0).getValue()))) {
            doIncreaseStockNumIm1(stockTransActionDTO, prefixActionCode, requiredRoleMap);
            return;
        }
        StaffDTO staff = staffService.findOne(stockTransActionDTO.getActionStaffId());
        if (staff != null) {
            // Bo sung action_code_shop theo nhan vien neu ko co quyen ROLE_STOCK_NUM_SHOP 09/03/2016
            if (Const.STOCK_TRANS.TRANS_CODE_PX.equals(prefixActionCode)) {
                staff.setStockNum(DataUtil.safeToLong(staff.getStockNum()) + 1);
            } else if (Const.STOCK_TRANS.TRANS_CODE_PN.equals(prefixActionCode)
                    || Const.STOCK_TRANS.TRANS_CODE_LN.equals(prefixActionCode)) {
                staff.setStockNumImp(DataUtil.safeToLong(staff.getStockNumImp()) + 1);
            }
            staffService.save(staff);
            //lock ban ghi
            //em.refresh(staffMapper.toPersistenceBean(staff), LockModeType.PESSIMISTIC_WRITE);
        }

        if (staff != null && requiredRoleMap != null && requiredRoleMap.hasRole(Const.PERMISION.ROLE_STOCK_NUM_SHOP)) {
            updateStockNumShop(staff.getShopId(), prefixActionCode, stockTransActionDTO.getStockTransActionId());
        }
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateStockNumShop(Long shopId, String prefixActionCode, Long stockTransActionId) throws Exception {
        ShopDTO shopDTO = shopService.findOne(shopId);
        if (shopDTO != null) {
            Long num = 0L;
            if (Const.STOCK_TRANS.TRANS_CODE_PX.equals(prefixActionCode)) {
                num = shopDTO.getStockNum() != null ? shopDTO.getStockNum() : num;
                shopDTO.setStockNum(num + 1);
            } else if (Const.STOCK_TRANS.TRANS_CODE_PN.equals(prefixActionCode)) {
                num = shopDTO.getStockNumImp() != null ? shopDTO.getStockNumImp() : num;
                shopDTO.setStockNumImp(num + 1);
            }
            shopService.save(shopDTO);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateStockNumShopIm1(Long shopId, String prefixActionCode, Long stockTransActionId) throws Exception {
        ShopIm1DTO shopDTO = shopIm1Service.findOne(shopId);
        if (shopDTO != null) {
            Long num = 0L;
            if (Const.STOCK_TRANS.TRANS_CODE_PX.equals(prefixActionCode)) {
                num = shopDTO.getStockNum() != null ? shopDTO.getStockNum() : num;
                shopDTO.setStockNum(num + 1);
            } else if (Const.STOCK_TRANS.TRANS_CODE_PN.equals(prefixActionCode)) {
                num = shopDTO.getStockNumImp() != null ? shopDTO.getStockNumImp() : num;
                shopDTO.setStockNumImp(num + 1);
            }
            shopIm1Service.save(shopDTO);
        }
    }

    public StockTransActionDTO createStockTransAction(Long stockTransID, Long staffID, String staffCode, Date createDate, String actionCode) throws Exception, LogicException {
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        // tao moi status = 2 - da lap phieu
        stockTransActionDTO.setStockTransId(stockTransID);
        stockTransActionDTO.setActionCode(actionCode);
        stockTransActionDTO.setActionCodeShop("");
        stockTransActionDTO.setStatus(Const.STOCK_TRANS_STATUS.EXPORT_NOTE);
        stockTransActionDTO.setCreateUser(staffCode);
        stockTransActionDTO.setCreateDatetime(createDate);
        stockTransActionDTO.setActionStaffId(staffID);
        stockTransActionService.save(stockTransActionDTO);

        // tao moi status = 3 - da xuat kho
        stockTransActionDTO.setActionCode("");
        stockTransActionDTO.setStatus(Const.STOCK_TRANS_STATUS.EXPORTED);
        StockTransActionDTO stockTransActionReturnSaveDTO = stockTransActionService.save(stockTransActionDTO);

        return stockTransActionReturnSaveDTO;
    }

    public StockTransDetailDTO createStockTransDetail(StockTransDTO stockTransSaveDTO, StockTransDetailDTO stockDetail, boolean isCheckIm1) throws Exception, LogicException {
        if (DataUtil.isNullOrZero(stockDetail.getQuantity())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.serial.quantity.limit.zezo");
        }
        Date sysDate = stockTransSaveDTO.getCreateDatetime();
        //check ton tai mat hang
        ProductOfferingDTO productOfferingDTO = productOfferingService.findOne(stockDetail.getProdOfferId());
        if (productOfferingDTO == null
                || productOfferingDTO.getProductOfferTypeId() == null
                || DataUtil.isNullOrEmpty(productOfferingDTO.getStatus())
                || !Const.STATUS_ACTIVE.equals(productOfferingDTO.getStatus())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "balance.valid.prodInfo");
        }
        Long stockTransID = stockTransSaveDTO.getStockTransId();
        stockDetail.setStockTransId(stockTransID);
        stockDetail.setCreateDatetime(stockTransSaveDTO.getCreateDatetime());
        stockDetail.setStateId(stockDetail.getStateId());
        stockDetail.setPrice(null);
        stockDetail.setAmount(null);
        StockTransDetailDTO detailInsert = stockTransDetailService.save(stockDetail);
        stockDetail.setTransDetailID(detailInsert.getTransDetailID());
//        totalDTOSearch.setProdOfferId(stockDetail.getProdOfferId());
//        totalDTOSearch.setStateId(stockDetail.getStateId());
//        totalDTOSearch.setStatus(DataUtil.safeToLong(Const.STATUS.ACTIVE));
        //create stockTransSerial
        if (Const.PRODUCT_OFFERING.CHECK_SERIAL.equals(stockDetail.getCheckSerial())) {
            //validate so luong dap ung kho xuat
            Long availAbleQuantity = stockRequestService.getAvailableQuantity(stockDetail.getOwnerID(), stockDetail.getOwnerType(), stockDetail.getProdOfferId(), stockDetail.getStateId());
            if (stockDetail.getQuantity() > DataUtil.safeToLong(availAbleQuantity)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "export.order.stock.quantity.msg");
            }
            if (DataUtil.isNullOrEmpty(stockDetail.getLstSerial())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "mn.stock.utilities.look.serial.not.empty.serial");
            }
            for (StockTransSerialDTO stockSerialNew : stockDetail.getLstSerial()) {
                stockSerialNew.setProdOfferId(stockDetail.getProdOfferId());
                stockSerialNew.setStateId(stockDetail.getStateId());
                stockSerialNew.setStockTransId(stockTransID);
                stockSerialNew.setCreateDatetime(sysDate);

                stockSerialNew.setStockTransDetailId(detailInsert.getStockTransDetailId());
                StockTransSerialDTO serialDTOInsert = stockTransSerialService.save(stockSerialNew);
                if (DataUtil.isNullObject(serialDTOInsert)
                        || DataUtil.isNullOrZero(serialDTOInsert.getStockTransSerialId())) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "mn.return.stock.create.return.stock.serial");
                }
                //cap nhap stock_x bccs2
                stockRequestService.updateStockXTTBH(stockSerialNew.getFromSerial(), stockSerialNew.getToSerial(), sysDate, stockDetail.getProdOfferId(),
                        stockDetail.getProdOfferTypeId(), null, null, stockTransSaveDTO.getFromOwnerType(), stockTransSaveDTO.getFromOwnerId(),
                        Const.STATE_STATUS.DAMAGE, Const.STATE_STATUS.NEW, Const.GOODS_STATE.BROKEN, isCheckIm1);
            }
        }
        return detailInsert;

    }

    public void executeStockTotal(StockTransDTO stockTransSaveDTO, StockTransDetailDTO stockDetail, StockTransActionDTO stockTransActionSaveDTO, boolean isCheckIm1) throws Exception, LogicException {
        StockTotalDTO totalDTOSearch = new StockTotalDTO();
        totalDTOSearch.setOwnerId(stockTransSaveDTO.getFromOwnerId());
        totalDTOSearch.setOwnerType(stockTransSaveDTO.getFromOwnerType());
        totalDTOSearch.setProdOfferId(stockDetail.getProdOfferId());
        totalDTOSearch.setStateId(stockDetail.getStateId());
        totalDTOSearch.setStatus(DataUtil.safeToLong(Const.STATUS.ACTIVE));

        StockTotalAuditDTO totalAuditInput = new StockTotalAuditDTO();
        totalAuditInput.setUserId(stockTransActionSaveDTO.getActionStaffId());
        totalAuditInput.setCreateDate(stockTransSaveDTO.getCreateDatetime());
        totalAuditInput.setTransId(stockTransSaveDTO.getStockTransId());
        totalAuditInput.setTransCode("");
        totalAuditInput.setTransType(DataUtil.safeToLong(stockTransSaveDTO.getStockTransType()));
        totalAuditInput.setUserCode(stockTransSaveDTO.getStaffCode());
        totalAuditInput.setStatus(Long.valueOf(Const.STATUS_ACTIVE));
        Long quantyti = stockDetail.getQuantity() * (-1);
        stockRequestService.addTotalTTBH(totalDTOSearch, totalAuditInput, stockTransSaveDTO.getFromOwnerType(), stockTransSaveDTO.getFromOwnerId(), stockDetail.getProdOfferId(),
                stockDetail.getStateId(), quantyti, stockTransActionSaveDTO.getCreateDatetime(), isCheckIm1);
    }

    @Transactional(rollbackFor = Exception.class)
    private void doIncreaseStockNumIm1(StockTransActionDTO stockTransActionDTO, String prefixActionCode, RequiredRoleMap requiredRoleMap) throws Exception {

        if (DataUtil.isNullOrEmpty(prefixActionCode)) {
            return;
        }

        StaffIm1DTO staff = staffIm1Service.findOne(stockTransActionDTO.getActionStaffId());
        if (staff != null) {
            // Bo sung action_code_shop theo nhan vien neu ko co quyen ROLE_STOCK_NUM_SHOP 09/03/2016
            if (Const.STOCK_TRANS.TRANS_CODE_PX.equals(prefixActionCode)) {
                staff.setStockNum(DataUtil.safeToLong(staff.getStockNum()) + 1);
            } else if (Const.STOCK_TRANS.TRANS_CODE_PN.equals(prefixActionCode)
                    || Const.STOCK_TRANS.TRANS_CODE_LN.equals(prefixActionCode)) {
                staff.setStockNumImp(DataUtil.safeToLong(staff.getStockNumImp()) + 1);
            }
            staffIm1Service.save(staff);

            if (requiredRoleMap != null && requiredRoleMap.hasRole(Const.PERMISION.ROLE_STOCK_NUM_SHOP)) {
                updateStockNumShopIm1(staff.getShopId(), prefixActionCode, stockTransActionDTO.getStockTransActionId());
            }
        } else {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "crt.err.staff.not.found.im1");
        }
    }

    private void validateSaveReturnStock(StockTransDTO stockTransDTO) throws Exception, LogicException {

    }

    @Override
    public void doPrepareData(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, FlagStockDTO flagStockDTO) throws LogicException, Exception {

    }

    @Override
    public void doValidate(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, List<StockTransDetailDTO> lstStockTransDetail) throws LogicException, Exception {

    }
}
