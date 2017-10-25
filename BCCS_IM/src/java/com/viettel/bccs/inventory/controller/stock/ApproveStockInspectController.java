package com.viettel.bccs.inventory.controller.stock;

import com.google.common.collect.Lists;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.*;
import com.viettel.bccs.inventory.tag.ShopInfoNameable;
import com.viettel.bccs.inventory.tag.StaffInfoNameable;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.web.common.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by hoangnt14 on 2/18/2016.
 */
@Component
@Scope("view")
@ManagedBean(name = "approveStockInspectController")
public class ApproveStockInspectController extends BaseController {

    private StockInspectDTO checkStockInspectDTO = new StockInspectDTO();
    private List<ApproveStockInspectDTO> lsApproveStockInspectDTOs = Lists.newArrayList();
    private List<StockInspectRealDTO> lsStockInspectSysDTOs = Lists.newArrayList();
    private List<ApproveStockInspectDTO> lsApproveStockSelection = Lists.newArrayList();
    private String productOfferTypeId;
    private ProductOfferingTotalDTO productOfferingTotalDTO = new ProductOfferingTotalDTO();
    private int limitAutoComplete;
    private VShopStaffDTO vStaffDTO = null;
    private VShopStaffDTO vShopDTO = null;
    private List<OptionSetValueDTO> listState = Lists.newArrayList();
    private List<OptionSetValueDTO> listApproveDate = Lists.newArrayList();
    private List<ProductOfferTypeDTO> listProductOfferType = Lists.newArrayList();
    private List<ProductOfferingTotalDTO> lsProductOfferingTotalDTO = Lists.newArrayList();
    private String strDate;
    private Boolean reasonRender;
    private Boolean detail;
    private String reason;
    private int fromValue;
    private int toValue;
    private Boolean selectAll;
    private List<String> lstChanelTypeId = Lists.newArrayList();
    private Long selectedStockInspectId;
    private boolean permission;

    @Autowired
    private StaffInfoNameable staffInfoTag;
    @Autowired
    private OptionSetValueService optionSetValueService;
    @Autowired
    private ProductOfferTypeService productOfferTypeService;
    @Autowired
    private ProductOfferingService productOfferingService;
    @Autowired
    private StockInspectService stockInspectService;
    @Autowired
    private StockInspectRealService stockInspectRealService;
    @Autowired
    private ShopInfoNameable shopInfoTag;

    @PostConstruct
    public void init() {
        try {
            permission = true;
            String enableApprove = optionSetValueService.getValueByTwoCodeOption("ENABLE_APPROVE_STOCK_INSPECT", "ENABLE_APPROVE_STOCK_INSPECT");
            if (!DataUtil.safeEqual(enableApprove, "1")) {
                permission = false;
                reportError("", "", "stock.inspect.not.approve");
                return;
            }
            strDate = (new SimpleDateFormat("MM/yyyy")).format(new Date());
            reasonRender = false;
            detail = false;
            staffInfoTag.initEmptyStaff();
            shopInfoTag.initShopAndAllChild(this, BccsLoginSuccessHandler.getStaffDTO().getShopId().toString(), true, Const.OWNER_TYPE.SHOP);
            lstChanelTypeId.add(Const.CHANNEL_TYPE_ID.CHANNEL_TYPE_STAFF);
            lstChanelTypeId.add(Const.CHANNEL_TYPE_ID.CHANNEL_TYPE_INSURRANCE);
            limitAutoComplete = DataUtil.safeToInt(optionSetValueService.getValueByNameFromOptionSetValue(Const.OPTION_SET.LIMIT_AUTOCOMPLETE));
            listState = optionSetValueService.getByOptionSetCode(Const.STOCK_INSPECT.STOCK_INSPECT_STATUS);
            listApproveDate = optionSetValueService.getByOptionSetCode(Const.STOCK_INSPECT.CHECK_STOCK_DAY_IN_MOTH);
            listProductOfferType = productOfferTypeService.getListProduct();
            int aboveDay = 0;
            int aboveQuantityDay = 0;
            for (OptionSetValueDTO optionSetValueDTO : listApproveDate) {
                if (DataUtil.safeEqual(optionSetValueDTO.getName(), Const.STOCK_INSPECT.BELLOW)) {
                    fromValue = DataUtil.safeToInt(optionSetValueDTO.getValue());
                }
                if (DataUtil.safeEqual(optionSetValueDTO.getName(), Const.STOCK_INSPECT.ABOVE)) {
                    aboveDay = DataUtil.safeToInt(optionSetValueDTO.getValue());
                }
                if (DataUtil.safeEqual(optionSetValueDTO.getName(), Const.STOCK_INSPECT.ABOVE_QUANTITY)) {
                    aboveQuantityDay = DataUtil.safeToInt(optionSetValueDTO.getValue());
                }
            }
            toValue = aboveDay > aboveQuantityDay ? aboveDay : aboveQuantityDay;
            selectedStockInspectId = 0L;
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex.getErrorCode(), ex.getDescription());
        } catch (Exception e) {
            logger.error(e);
            reportError("", "common.error.happened", e);
        }
    }

    @Secured("@")
    public void doShowDetail(Long stockInspectId, String productName, boolean showDetail) {
        try {
            detail = showDetail;
            if (!showDetail) {
                return;
            }
            selectedStockInspectId = stockInspectId;
            lsStockInspectSysDTOs = stockInspectRealService.getStockInspectReal(stockInspectId);
            if (!DataUtil.isNullObject(lsStockInspectSysDTOs)) {
                for (StockInspectRealDTO stockInspectRealDTO : lsStockInspectSysDTOs) {
                    stockInspectRealDTO.setProductOfferName(productName);
                }
            }
        } catch (Exception e) {
            logger.error(e);
            reportError("", "common.error.happened", e);
        }
    }

    @Secured("@")
    public void doSearchApproveStockInspect() {
        Date date = new Date();
        try {
            String format = "MM/yyyy";
            date = (new SimpleDateFormat(format)).parse(strDate);
        } catch (Exception e) {
            logger.error(e);
            reportError("", "mn.stock.utilities.month.year.validate", e);
            return;
        }
        try {
            detail = false;
            reason = null;
            selectAll = false;
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.DAY_OF_MONTH, fromValue);
            Date fromDate = calendar.getTime();
            int toMonth = calendar.get(Calendar.MONTH) + 1;
            calendar.set(Calendar.DAY_OF_MONTH, toValue);
            calendar.set(Calendar.MONTH, toMonth);
            Date toDate = calendar.getTime();
            Long ownerId = null;
            Long ownerType = null;
            String code = "";
            if (!DataUtil.isNullObject(vShopDTO)) {
                ownerId = DataUtil.safeToLong(vShopDTO.getOwnerId());
                ownerType = Const.OWNER_TYPE.SHOP_LONG;
                if (!DataUtil.isNullObject(vStaffDTO)) {
                    ownerId = DataUtil.safeToLong(vStaffDTO.getOwnerId());
                    ownerType = Const.OWNER_TYPE.STAFF_LONG;
                }
            }
            if (!DataUtil.isNullObject(productOfferingTotalDTO)) {
                code = productOfferingTotalDTO.getCode();
            }
            lsApproveStockInspectDTOs = stockInspectService.searchApproveInspect(DataUtil.safeToLong(BccsLoginSuccessHandler.getStaffDTO().getStaffId()),
                    ownerId, ownerType, checkStockInspectDTO.getStateId(), checkStockInspectDTO.getProdOfferTypeId(), code, fromDate, toDate);
            reasonRender = false;
            if (!DataUtil.isNullOrEmpty(lsApproveStockInspectDTOs)) {
                for (ApproveStockInspectDTO approveStockInspectDTO : lsApproveStockInspectDTOs) {
                    if (DataUtil.safeEqual(approveStockInspectDTO.getApproveStatus(), Const.INSPECT_APPROVE_STATUS.NOT_HAVE_APPROVE)) {
                        reasonRender = true;
                        break;
                    }
                }
            }
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex.getErrorCode(), ex.getDescription());
        } catch (Exception e) {
            logger.error(e);
            reportError("", "common.error.happened", e);
        }
    }

    @Secured("@")
    public void doResetApproveStock() {
        this.vShopDTO = null;
        this.vStaffDTO = null;
        shopInfoTag.initShopAndAllChild(this, BccsLoginSuccessHandler.getStaffDTO().getShopId().toString(), true, Const.OWNER_TYPE.SHOP);
        staffInfoTag.initEmptyStaff();
        strDate = null;
        checkStockInspectDTO = new StockInspectDTO();
        productOfferingTotalDTO = new ProductOfferingTotalDTO();
        detail = false;
        reason = null;
        selectAll = false;
        lsApproveStockInspectDTOs = Lists.newArrayList();
        reasonRender = false;

    }


    @Secured("@")
    public void onChangeProductOfferType() {
        try {
            productOfferingTotalDTO = new ProductOfferingTotalDTO();
            if (DataUtil.isNullOrEmpty(productOfferTypeId)) {
                lsProductOfferingTotalDTO = Lists.newArrayList();
            }
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
            logger.error(ex.getMessage(), ex);
            topPage();
        }
    }

    @Secured("@")
    public void doSelectProductOffering() {
        try {
            if (DataUtil.isNullObject(productOfferingTotalDTO)) {
                checkStockInspectDTO.setProdOfferId(null);
                reportError("", "", "stock.inspect.not.choose.product.offer");
            } else {
                checkStockInspectDTO.setProdOfferId(productOfferingTotalDTO.getProductOfferingId());
            }
//        } catch (LogicException ex) {
//            logger.error(ex.getMessage(), ex);
//            reportError("", ex);
//            topPage();
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
            logger.error(ex.getMessage(), ex);
            topPage();
        }
    }

    private List<StockInspectDTO> convertStockInspect(List<ApproveStockInspectDTO> lst, Long approveStatus, Long isFinish) {
        List<StockInspectDTO> lstResult = Lists.newArrayList();
        for (ApproveStockInspectDTO approveStockInspectDTO : lst) {
//                StockInspectDTO stockInspectDTO = stockInspectService.findOne(approveStockInspectDTO.getStockInspectId());
            StockInspectDTO stockInspectDTO = new StockInspectDTO();
            stockInspectDTO.setStockInspectId(approveStockInspectDTO.getStockInspectId());
            stockInspectDTO.setQuantityFinance(DataUtil.safeToLong(approveStockInspectDTO.getQuantityFinance()));
            stockInspectDTO.setApproveStatus(approveStatus);
            stockInspectDTO.setIsFinished(isFinish);
            stockInspectDTO.setApproveNote(reason);
            lstResult.add(stockInspectDTO);
        }
        return lstResult;
    }

    @Secured("@")
    public void doApproveInspect() {
        try {
            validateApprove();
            stockInspectService.updateApprove(convertStockInspect(lsApproveStockSelection, Const.INSPECT_APPROVE_STATUS.HAVE_APPROVE, Const.INSPECT_APPROVE_STATUS.HAVE_FINISH));
            reportSuccess("", "mn.stock.track.number.approve.success");
            doSearchApproveStockInspect();
            topPage();
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex);
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "common.error.happened", ex);
            topPage();
        }
    }

    @Secured("@")
    public void doValidateApproveInspect() {
        try {
            lsApproveStockSelection = Lists.newArrayList();
            for (ApproveStockInspectDTO approveStockInspectDTO : lsApproveStockInspectDTOs) {
                if (DataUtil.safeEqual(approveStockInspectDTO.getApproveStatus(), Const.INSPECT_APPROVE_STATUS.NOT_HAVE_APPROVE)) {
                    if (!DataUtil.isNullObject(approveStockInspectDTO.getSelected()) && approveStockInspectDTO.getSelected()) {
                        lsApproveStockSelection.add(approveStockInspectDTO);
                    }
                }
            }
            if (lsApproveStockSelection.size() == 0) {
                throw new LogicException("", "mn.stock.track.number.not.selection");
            }
        } catch (LogicException ex) {
            reportError("", ex);
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "common.error.happened", ex);
            topPage();
        }
    }

    private void validateApprove() {
        try {
            if (!DataUtil.isNullObject(vShopDTO) && !DataUtil.safeEqual(vShopDTO.getStatus(), Const.STATUS_ACTIVE)) {
                throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_INNER_VALIDATE, "export.order.view.approve.stock.inspect.shop.status");
            }
            if (!DataUtil.isNullObject(vStaffDTO) && !DataUtil.safeEqual(vStaffDTO.getStatus(), Const.STATUS_ACTIVE)) {
                throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_INNER_VALIDATE, "export.order.view.approve.stock.inspect.staff.status");
            }
        } catch (LogicException ex) {
            reportError("", ex);
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "common.error.happened", ex);
            topPage();
        }
    }

    @Secured("@")
    public void doCancelInspect() {
        try {
            validateApprove();
            stockInspectService.updateApprove(convertStockInspect(lsApproveStockSelection, Const.INSPECT_APPROVE_STATUS.CANCEL_APPROVE, Const.INSPECT_APPROVE_STATUS.NOT_HAVE_FINISH));
            reportSuccess("", "mn.stock.track.number.cancel.success");
            doSearchApproveStockInspect();
        } catch (LogicException ex) {
            reportError("", ex);
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "common.error.happened", ex);
            topPage();
        }
    }


    @Secured("@")
    public void receiveShop(VShopStaffDTO vShopStaffDTO) {
        this.vShopDTO = vShopStaffDTO;
        staffInfoTag.initStaffWithChanelTypesAndParrentShop(this, vShopDTO.getOwnerId(), null, lstChanelTypeId, false);
        try {
            checkStockInspectDTO.setOwnerId(DataUtil.safeToLong(vShopDTO.getOwnerId()));
            checkStockInspectDTO.setOwnerType(Const.OWNER_TYPE.SHOP_LONG);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "common.error.happened", ex);
            topPage();
        }
    }

    @Secured("@")
    public void clearShop() {
        this.vShopDTO = null;
        this.vStaffDTO = null;
        staffInfoTag.initEmptyStaff();
    }

    @Secured("@")
    public void receiveStaff(VShopStaffDTO vShopStaffDTO) {
        this.vStaffDTO = vShopStaffDTO;
        try {
            checkStockInspectDTO.setOwnerId(DataUtil.safeToLong(vStaffDTO.getOwnerId()));
            checkStockInspectDTO.setOwnerType(Const.OWNER_TYPE.STAFF_LONG);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "common.error.happened", ex);
            topPage();
        }
    }

    @Secured("@")
    public void clearStaff() {
        this.vStaffDTO = null;
    }

    @Secured("@")
    public List<ProductOfferingTotalDTO> doChangeOffering(String inputProduct) {
        try {
            if (DataUtil.isNullOrEmpty(productOfferTypeId)) {
                return new ArrayList<ProductOfferingTotalDTO>();
            }
            List<Long> lstTypesId = Lists.newArrayList(DataUtil.safeToLong(productOfferTypeId));
            return lsProductOfferingTotalDTO = productOfferingService.getAllLsProductOfferingDTOForProcessStock(inputProduct.trim(), lstTypesId);

        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex.getKeyMsg(), ex);
            topPage();
        } catch (Exception e) {
            reportError("", "common.error.happened", e);
            logger.error(e.getMessage(), e);
            topPage();
        }
        return new ArrayList<ProductOfferingTotalDTO>();
    }

    @Secured("@")
    public void reasonTrimChange() {
        reason = reason.trim();
    }

    @Secured("@")
    public void selectAllRow() {
        try {
            for (ApproveStockInspectDTO approveStockInspectDTO : lsApproveStockInspectDTOs) {
                if (DataUtil.safeEqual(approveStockInspectDTO.getApproveStatus(), Const.INSPECT_APPROVE_STATUS.NOT_HAVE_APPROVE)) {
                    if (selectAll) {
                        approveStockInspectDTO.setSelected(true);
                    } else {
                        approveStockInspectDTO.setSelected(false);
                    }
                }
            }

        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
            logger.error(ex.getMessage(), ex);
            topPage();
        }
    }

    @Secured("@")
    public void selectRow(ApproveStockInspectDTO dto) {
        try {
            int countChecked = 0;
            int countUnChecked = 0;
            int countNotApprove = 0;
            for (ApproveStockInspectDTO approveStockInspectDTO : lsApproveStockInspectDTOs) {
                if (DataUtil.safeEqual(approveStockInspectDTO.getApproveStatus(), Const.INSPECT_APPROVE_STATUS.NOT_HAVE_APPROVE)) {
                    if (!DataUtil.isNullObject(approveStockInspectDTO.getSelected()) && approveStockInspectDTO.getSelected()) {
                        countChecked++;
                    } else {
                        countUnChecked++;
                    }
                    countNotApprove++;
                }
            }
            // Check so luong checked
            if ((countChecked == countNotApprove) || (countUnChecked == countNotApprove)) {
                if (dto.getSelected()) {
                    selectAll = true;
                } else {
                    selectAll = false;
                }
            }
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
            logger.error(ex.getMessage(), ex);
            topPage();
        }
    }

    @Secured("@")
    public boolean disabledState() {
        return DataUtil.isNullObject(productOfferingTotalDTO) ? false : (DataUtil.isNullOrZero(productOfferingTotalDTO.getProductOfferingId()) ? false : true);
    }

    public boolean disableCheckAll() {
        if (!DataUtil.isNullObject(lsApproveStockInspectDTOs)) {
            for (ApproveStockInspectDTO approveStockInspectDTO : lsApproveStockInspectDTOs) {
                if (DataUtil.safeEqual(approveStockInspectDTO.getApproveStatus(), Const.INSPECT_APPROVE_STATUS.NOT_HAVE_APPROVE)) {
                    return false;
                }
            }
        }
        return true;
    }

    public Boolean getSelectAll() {
        return selectAll;
    }

    public void setSelectAll(Boolean selectAll) {
        this.selectAll = selectAll;
    }

    public ShopInfoNameable getShopInfoTag() {
        return shopInfoTag;
    }

    public void setShopInfoTag(ShopInfoNameable shopInfoTag) {
        this.shopInfoTag = shopInfoTag;
    }

    @Secured("@")
    public void clearProduct() {
        this.productOfferingTotalDTO = null;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Boolean getReasonRender() {
        return reasonRender;
    }

    public void setReasonRender(Boolean reasonRender) {
        this.reasonRender = reasonRender;
    }

    public List<ApproveStockInspectDTO> getLsApproveStockSelection() {
        return lsApproveStockSelection;
    }

    public void setLsApproveStockSelection(List<ApproveStockInspectDTO> lsApproveStockSelection) {
        this.lsApproveStockSelection = lsApproveStockSelection;
    }

    public List<ApproveStockInspectDTO> getLsApproveStockInspectDTOs() {
        return lsApproveStockInspectDTOs;
    }

    public void setLsApproveStockInspectDTOs(List<ApproveStockInspectDTO> lsApproveStockInspectDTOs) {
        this.lsApproveStockInspectDTOs = lsApproveStockInspectDTOs;
    }

    public StaffInfoNameable getStaffInfoTag() {
        return staffInfoTag;
    }

    public void setStaffInfoTag(StaffInfoNameable staffInfoTag) {
        this.staffInfoTag = staffInfoTag;
    }

    public StockInspectDTO getCheckStockInspectDTO() {
        return checkStockInspectDTO;
    }

    public void setCheckStockInspectDTO(StockInspectDTO checkStockInspectDTO) {
        this.checkStockInspectDTO = checkStockInspectDTO;
    }

    public Boolean getDetail() {
        return detail;
    }

    public void setDetail(Boolean detail) {
        this.detail = detail;
    }

    public List<StockInspectRealDTO> getLsStockInspectSysDTOs() {
        return lsStockInspectSysDTOs;
    }

    public void setLsStockInspectSysDTOs(List<StockInspectRealDTO> lsStockInspectSysDTOs) {
        this.lsStockInspectSysDTOs = lsStockInspectSysDTOs;
    }

    public List<OptionSetValueDTO> getListState() {
        return listState;
    }

    public void setListState(List<OptionSetValueDTO> listState) {
        this.listState = listState;
    }

    public int getLimitAutoComplete() {
        return limitAutoComplete;
    }

    public void setLimitAutoComplete(int limitAutoComplete) {
        this.limitAutoComplete = limitAutoComplete;
    }

    public List<ProductOfferTypeDTO> getListProductOfferType() {
        return listProductOfferType;
    }

    public void setListProductOfferType(List<ProductOfferTypeDTO> listProductOfferType) {
        this.listProductOfferType = listProductOfferType;
    }

    public ProductOfferingTotalDTO getProductOfferingTotalDTO() {
        return productOfferingTotalDTO;
    }

    public void setProductOfferingTotalDTO(ProductOfferingTotalDTO productOfferingTotalDTO) {
        this.productOfferingTotalDTO = productOfferingTotalDTO;
    }

    public List<ProductOfferingTotalDTO> getLsProductOfferingTotalDTO() {
        return lsProductOfferingTotalDTO;
    }

    public void setLsProductOfferingTotalDTO(List<ProductOfferingTotalDTO> lsProductOfferingTotalDTO) {
        this.lsProductOfferingTotalDTO = lsProductOfferingTotalDTO;
    }

    public String getProductOfferTypeId() {
        return productOfferTypeId;
    }

    public void setProductOfferTypeId(String productOfferTypeId) {
        this.productOfferTypeId = productOfferTypeId;
    }

    public String getStrDate() {
        return strDate;
    }

    public void setStrDate(String strDate) {
        this.strDate = strDate;
    }

    public Long getSelectedStockInspectId() {
        return selectedStockInspectId;
    }

    public void setSelectedStockInspectId(Long selectedStockInspectId) {
        this.selectedStockInspectId = selectedStockInspectId;
    }

    public boolean isPermission() {
        return permission;
    }

    public void setPermission(boolean permission) {
        this.permission = permission;
    }
}
