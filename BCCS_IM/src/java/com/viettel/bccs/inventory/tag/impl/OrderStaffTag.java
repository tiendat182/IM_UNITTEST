package com.viettel.bccs.inventory.tag.impl;

import com.google.common.collect.Lists;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.fw.controller.TransCommonController;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.*;
import com.viettel.bccs.inventory.tag.OrderStaffTagNameable;
import com.viettel.bccs.inventory.tag.ShopInfoNameable;
import com.viettel.bccs.inventory.tag.StaffInfoNameable;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.RequiredRoleMap;
import com.viettel.web.common.security.CustomAuthenticationProvider;
import org.springframework.beans.BeanUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by thanhnt77 on 12/11/2015.
 */
@Service
@Scope("prototype")
public class OrderStaffTag extends TransCommonController implements OrderStaffTagNameable {

    private Object objectController;
    private Boolean writeOffice = false;
    private Boolean isDisableEdit = false;
    private Boolean tranLogistics = true;
    private Boolean tranfer = false;
    private Date currentDate;
    private List<String> lstChanelTypeId = Lists.newArrayList();
    private RequiredRoleMap requiredRoleMap;
    private StaffDTO staffDTO;
    private StockTransInputDTO transInputDTO = new StockTransInputDTO();
    private List<ReasonDTO> lsReasonDto = Lists.newArrayList();
    private VShopStaffDTO vShopStaffDTO = new VShopStaffDTO();
    private int limitAutoComplete;
    private boolean existLogistics;
    private Boolean autoCreateNote = false;

    @Autowired
    private ShopInfoNameable shopInfoExportTag;
    @Autowired
    private StaffInfoNameable staffInfoReceiveTag;
    @Autowired
    private OptionSetValueService optionSetValueService;
    @Autowired
    private StockTransService stockTransService;
    @Autowired
    private StockTransActionService stockTransActionService;
    @Autowired
    private ReasonService reasonService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private StaffService staffService;

    @Override
    public void init(Object objectController, Boolean writeOffice) {
        try {
            if (staffDTO == null) {
                staffDTO = BccsLoginSuccessHandler.getStaffDTO();
            }
            this.autoCreateNote = false;
            lstChanelTypeId = commonService.getChannelTypes(Const.OWNER_TYPE.STAFF);
            this.objectController = objectController;
            this.writeOffice = writeOffice;
            shopInfoExportTag.initShop(this, DataUtil.safeToString(staffDTO.getShopId()), false);
            shopInfoExportTag.loadShop(DataUtil.safeToString(staffDTO.getShopId()), true);
            staffInfoReceiveTag.initStaffWithChanelTypes(this, DataUtil.safeToString(staffDTO.getShopId()), null, lstChanelTypeId, false);
            staffInfoReceiveTag.setMultiTag(true);
            lsReasonDto = DataUtil.defaultIfNull(reasonService.getLsReasonByType(Const.REASON_TYPE.STOCK_EXP_STAFF), new ArrayList<ReasonDTO>());
            limitAutoComplete = DataUtil.safeToInt(optionSetValueService.getValueByNameFromOptionSetValue(Const.OPTION_SET.LIMIT_AUTOCOMPLETE));
            requiredRoleMap = CustomAuthenticationProvider.createRequiredRoleMap(Const.PERMISION.ROLE_SYNC_LOGISTIC, Const.PERMISION.ROLE_TRANSPORT_STOCK, Const.PERMISION.ROLE_AUTO_ORDER_NOTE, Const.PERMISION.ROLE_DONGBO_ERP);
            if (currentDate == null) {
                currentDate = optionSetValueService.getSysdateFromDB(true);
            }
            resetOrderStaff();
            initLogistics();
            tranLogistics = getShowLogistic();
        } catch (Exception e) {
            this.reportError("", "common.error.happened", e);
        }
    }

    private void initLogistics() throws LogicException, Exception {
        List<OptionSetValueDTO> options = DataUtil.defaultIfNull(optionSetValueService.getByOptionsetCodeAndValue(Const.OPTION_SET.LOGISTIC_SHOP_ID_LIST, staffDTO.getShopCode()), new ArrayList<>());
        existLogistics = !DataUtil.isNullOrEmpty(options);
    }

    @Override
    public void resetOrderStaff() {
        transInputDTO = new StockTransInputDTO();
        transInputDTO.setFromOwnerId(staffDTO.getShopId());
        transInputDTO.setFromOwnerType(DataUtil.safeToLong(Const.OWNER_TYPE.SHOP));
        transInputDTO.setCreateUser(staffDTO.getStaffCode());
        transInputDTO.setCreateDatetime(currentDate);
        transInputDTO.setActionStaffId(staffDTO.getStaffOwnerId());
    }

    @Override
    public void exportShop(VShopStaffDTO vShopStaffDTO) {
        this.vShopStaffDTO = vShopStaffDTO;
        transInputDTO.setFromOwnerId(DataUtil.safeToLong(vShopStaffDTO.getOwnerId()));
        transInputDTO.setFromOwnerType(DataUtil.safeToLong(Const.OWNER_TYPE.SHOP));
    }

    @Override
    public void receiveStaff(VShopStaffDTO vShopStaffDTO, String methodNameReceiveStaff) {
        this.vShopStaffDTO = vShopStaffDTO;
        transInputDTO.setToOwnerId(DataUtil.safeToLong(vShopStaffDTO.getOwnerId()));
        transInputDTO.setToOwnerType(DataUtil.safeToLong(Const.OWNER_TYPE.STAFF));
        try {
            Class<?> c = this.objectController.getClass();
            Method method = c.getMethod(methodNameReceiveStaff, VShopStaffDTO.class);
            method.invoke(this.objectController, this.vShopStaffDTO);
        } catch (NoSuchMethodException ex) {
            logger.error("", ex);
            reportError("", "msg.error.happened", ex);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            reportError("", "msg.error.happened", ex);
            logger.error("", ex);
        }
    }

    @Override
    public void clearCurrentStaff(String methodName) {
        this.vShopStaffDTO = null;
        try {
            Class<?> c = this.objectController.getClass();
            Method method = c.getMethod(methodName);
            method.invoke(this.objectController);
        } catch (NoSuchMethodException ex) {
            logger.error("", ex);
            reportError("", "common.error.happened", ex);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            reportError("", "common.error.happened", ex);
            logger.error("", ex);
        }
    }

    @Override
    public void doChangeWriteOffice(String methodName) {
        try {
            if (DataUtil.isNullOrEmpty(methodName)) {
                return;
            }
            callBackMethod(this.objectController, methodName, writeOffice, null);
        } catch (Exception e) {
            this.reportError("", "common.error.happened", e);
        }
    }

    @Override
    public StockTransActionDTO getStockTransActionDTO() {
        StockTransActionDTO transActionDTO = new StockTransActionDTO();
        transActionDTO.setActionCode(transInputDTO.getActionCode());
        transActionDTO.setStockTransActionId(transInputDTO.getStockTransActionId());
        transActionDTO.setCreateUser(staffDTO.getStaffCode());
        transActionDTO.setActionStaffId(staffDTO.getStaffId());
        return transActionDTO;
    }

    @Override
    public StockTransDTO getStockTransDTO() {
        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setFromOwnerId(transInputDTO.getFromOwnerId());
        stockTransDTO.setFromOwnerType(transInputDTO.getFromOwnerType());
        stockTransDTO.setToOwnerId(transInputDTO.getToOwnerId());
        stockTransDTO.setToOwnerType(transInputDTO.getToOwnerType());
        stockTransDTO.setStockTransId(transInputDTO.getStockTransId());
        stockTransDTO.setReasonId(transInputDTO.getReasonId());
        stockTransDTO.setRegionStockId(transInputDTO.getRegionStockId());
        stockTransDTO.setRegionStockTransId(transInputDTO.getRegionStockTransId());
        stockTransDTO.setNote(transInputDTO.getNote());
        stockTransDTO.setStockBase(transInputDTO.getStockBase());
        stockTransDTO.setCheckErp(transInputDTO.getCheckErp() ? Const.STOCK_TRANS.IS_NOT_ERP : null);
        stockTransDTO.setTransport(this.tranfer ? Const.STOCK_TRANS.IS_TRANFER : null);
        stockTransDTO.setLogicstic(this.tranLogistics ? Const.STOCK_TRANS.IS_LOGISTIC : null);
        //stockTransDTO.setLogicstic(!getShowLogistic() || !tranLogistics ? null : Const.STOCK_TRANS.IS_LOGISTIC);
        stockTransDTO.setIsAutoGen(this.tranLogistics ? Const.STOCK_TRANS.IS_AUTO_GEN_LOGISTIC : null);
        return stockTransDTO;
    }

    /**
     * ham lay ve reason name
     *
     * @param reasonId
     * @param lsReasonDto
     * @return
     * @author TuanNM33
     */
    private String getReasonName(Long reasonId, List<ReasonDTO> lsReasonDto) {
        if (reasonId != null && !DataUtil.isNullOrEmpty(lsReasonDto)) {
            for (ReasonDTO option : lsReasonDto) {
                if (reasonId.equals(option.getReasonId())) {
                    return option.getReasonName();
                }
            }
        }
        return "";
    }

    @Override
    public void doChangeAutoCreateNote(String methodName) {
        try {
            if (DataUtil.isNullOrEmpty(methodName)) {
                return;
            }
            callBackMethod(this.objectController, methodName, autoCreateNote, null);
        } catch (Exception e) {
            this.reportError("", "common.error.happened", e);
        }
    }

    @Override
    public void loadOrderStaff(Long stockTransActionId, boolean isDisableEdit) {
        try {
            initLogistics();
            tranLogistics = false;
            if (transInputDTO == null) {
                transInputDTO = new StockTransInputDTO();
            }
            StockTransActionDTO transActionDTO = stockTransActionService.findOne(stockTransActionId);
            if (transActionDTO != null) {
                StockTransDTO transDTO = stockTransService.findOne(transActionDTO.getStockTransId());
                if (transDTO != null) {
                    BeanUtils.copyProperties(transDTO, transInputDTO);
                    transInputDTO.setCreateDatetime(transDTO.getCreateDatetime());
                    transInputDTO.setCreateUser(transActionDTO.getCreateUser());
                    transInputDTO.setActionStaffId(transActionDTO.getActionStaffId());
                    transInputDTO.setStockTransActionId(transActionDTO.getStockTransActionId());
                    transInputDTO.setActionCode(transActionDTO.getActionCode());
                    transInputDTO.setStockTransId(transDTO.getStockTransId());
                    transInputDTO.setRegionStockTransId(transDTO.getRegionStockTransId());
                    transInputDTO.setCheckErp(Const.STOCK_TRANS.IS_NOT_ERP.equals(transDTO.getCheckErp()));
                    this.tranfer = Const.STOCK_TRANS.IS_TRANFER.equals(transDTO.getTransport());
                    this.tranLogistics = Const.STOCK_TRANS.IS_LOGISTIC.equals(transDTO.getLogicstic());
                    //load du lieu cho 2 cai tag kho xuat hang/nhan vien nhan hang
                    if (transInputDTO.getFromOwnerId() != null) {
                        shopInfoExportTag.loadShop(DataUtil.safeToString(transInputDTO.getFromOwnerId()), true);
                    }
                    if (transInputDTO.getToOwnerId() != null) {
                        staffInfoReceiveTag.initStaff(this, null, DataUtil.safeToString(transInputDTO.getToOwnerId()), true);
                    }
                    transInputDTO.setReasonName(getReasonName(transDTO.getReasonId(), lsReasonDto));
                    //transInputDTO.setReasonName(vStockTransDTO.getReasonName());
                    //transInputDTO.setRegionStockName(getRegionShopName("", lsRegionShop));
                }
            }
            this.isDisableEdit = isDisableEdit;
        } catch (Exception e) {
            this.reportError("", "common.error.happened", e);
        }
    }

    @Override
    public StockTransDTO getStockTransDTOForPint() {
        StockTransDTO stockTransDTO = getStockTransDTO();
        try {
            stockTransDTO.setActionCode(transInputDTO.getActionCode());
            String fromOwnerName = null;
            String fromOwnerAddress = null;
            String toOwnerName = null;
            String toOwnerAddress = null;
            if (DataUtil.safeEqual(transInputDTO.getFromOwnerType(), Const.OWNER_TYPE.SHOP_LONG)) {
                ShopDTO shopDTO = shopService.findOne(transInputDTO.getFromOwnerId());
                if (shopDTO != null) {
                    fromOwnerName = shopDTO.getName();
                    fromOwnerAddress = shopDTO.getAddress();
                }
            } else if (DataUtil.safeEqual(transInputDTO.getFromOwnerType(), Const.OWNER_TYPE.STAFF_LONG)) {
                StaffDTO staffDTO = staffService.findOne(transInputDTO.getFromOwnerId());
                if (staffDTO != null) {
                    fromOwnerName = staffDTO.getName();
                    fromOwnerAddress = staffDTO.getAddress();
                }
            }

            if (DataUtil.safeEqual(transInputDTO.getToOwnerType(), Const.OWNER_TYPE.SHOP_LONG)) {
                ShopDTO shopDTO = shopService.findOne(transInputDTO.getToOwnerId());
                if (shopDTO != null) {
                    toOwnerName = shopDTO.getName();
                    toOwnerAddress = shopDTO.getAddress();
                }
            } else if (DataUtil.safeEqual(transInputDTO.getToOwnerType(), Const.OWNER_TYPE.STAFF_LONG)) {
                StaffDTO staffDTO = staffService.findOne(transInputDTO.getToOwnerId());
                if (staffDTO != null) {
                    toOwnerName = staffDTO.getName();
                    toOwnerAddress = staffDTO.getAddress();
                }
            }
            stockTransDTO.setCreateDatetime(transInputDTO.getCreateDatetime());
            stockTransDTO.setFromOwnerName(fromOwnerName);
            stockTransDTO.setFromOwnerAddress(fromOwnerAddress);
            stockTransDTO.setToOwnerName(toOwnerName);
            stockTransDTO.setToOwnerAddress(toOwnerAddress);
            if (!DataUtil.isNullOrZero(transInputDTO.getReasonId())) {
                ReasonDTO reasonDTO = reasonService.findOne(transInputDTO.getReasonId());
                stockTransDTO.setReasonName(reasonDTO.getReasonName());
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return stockTransDTO;
    }

    @Override
    public Boolean getShowCheckErp() {
        return requiredRoleMap != null && requiredRoleMap.hasRole(Const.PERMISION.ROLE_DONGBO_ERP);
    }

    @Override
    public Boolean getShowLogistic() {
        return existLogistics && requiredRoleMap != null && requiredRoleMap.hasRole(Const.PERMISION.ROLE_SYNC_LOGISTIC);
    }

    @Override
    public Boolean getShowTransfer() {
        //return requiredRoleMap != null && requiredRoleMap.hasRole(Const.PERMISION.ROLE_TRANSPORT_STOCK);
        return false; //a The bao sua
    }

    @Override
    public Boolean getShowAutoOrderNote() {
        return requiredRoleMap != null && requiredRoleMap.hasRole(Const.PERMISION.ROLE_AUTO_ORDER_NOTE);
    }

    // Getter and setter
    public Boolean getIsDisableEdit() {
        return isDisableEdit;
    }

    public void setIsDisableEdit(Boolean isDisableEdit) {
        this.isDisableEdit = isDisableEdit;
    }

    public Boolean getWriteOffice() {
        return writeOffice;
    }

    public void setWriteOffice(Boolean writeOffice) {
        this.writeOffice = writeOffice;
    }

    public StockTransInputDTO getTransInputDTO() {
        return transInputDTO;
    }

    public void setTransInputDTO(StockTransInputDTO transInputDTO) {
        this.transInputDTO = transInputDTO;
    }

    public ShopInfoNameable getShopInfoExportTag() {
        return shopInfoExportTag;
    }

    public void setShopInfoExportTag(ShopInfoNameable shopInfoExportTag) {
        this.shopInfoExportTag = shopInfoExportTag;
    }

    public int getLimitAutoComplete() {
        return limitAutoComplete;
    }

    public void setLimitAutoComplete(int limitAutoComplete) {
        this.limitAutoComplete = limitAutoComplete;
    }

    public StaffInfoNameable getStaffInfoReceiveTag() {
        return staffInfoReceiveTag;
    }

    public void setStaffInfoReceiveTag(StaffInfoNameable staffInfoReceiveTag) {
        this.staffInfoReceiveTag = staffInfoReceiveTag;
    }

    public VShopStaffDTO getvShopStaffDTO() {
        return vShopStaffDTO;
    }

    public void setvShopStaffDTO(VShopStaffDTO vShopStaffDTO) {
        this.vShopStaffDTO = vShopStaffDTO;
    }

    public List<ReasonDTO> getLsReasonDto() {
        return lsReasonDto;
    }

    public void setLsReasonDto(List<ReasonDTO> lsReasonDto) {
        this.lsReasonDto = lsReasonDto;
    }

    public Boolean getTranLogistics() {
        return tranLogistics;
    }

    public void setTranLogistics(Boolean tranLogistics) {
        this.tranLogistics = tranLogistics;
    }

    public Boolean getTranfer() {
        return tranfer;
    }

    public void setTranfer(Boolean tranfer) {
        this.tranfer = tranfer;
    }

    public Boolean getAutoCreateNote() {
        return autoCreateNote;
    }

    public void setAutoCreateNote(Boolean autoCreateNote) {
        this.autoCreateNote = autoCreateNote;
    }
}
