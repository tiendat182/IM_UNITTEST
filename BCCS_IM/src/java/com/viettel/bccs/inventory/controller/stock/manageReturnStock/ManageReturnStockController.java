package com.viettel.bccs.inventory.controller.stock.manageReturnStock;

import com.google.common.collect.Lists;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.fw.controller.TransCommonController;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.*;
import com.viettel.bccs.inventory.tag.ListProductNameable;
import com.viettel.bccs.inventory.tag.ShopInfoNameable;
import com.viettel.bccs.inventory.tag.SignOfficeTagNameable;
import com.viettel.bccs.inventory.tag.StaffInfoNameable;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DateUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.RequiredRoleMap;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.web.common.security.CustomAuthenticationProvider;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by DungHA7 on 22/09/2016.
 */
@Component
@Scope("view")
@ManagedBean
public class ManageReturnStockController extends TransCommonController {

    private BaseMessage baseMessage;
    private StockDebitDTO stockDebitDTO;
    @Autowired
    private StockDebitService stockDebitService;
    @Autowired
    private StockRequestService stockRequestService;
    @Autowired
    private ShopInfoNameable shopInfoTag;
    @Autowired
    private ShopInfoNameable shopInfoTagSearch;
    @Autowired
    private StaffInfoNameable staffInfoTag;
    @Autowired
    private StaffInfoNameable staffInfoTagSearch;
    @Autowired
    private ReturnStockService returnStockService;
    @Autowired
    private StockTransService stockTransService;

    @Autowired
    private SignOfficeTagNameable writeOfficeTag;//khai bao tag ky vOffice
    @Autowired
    StaffService staffService;
    @Autowired
    ShopService shopService;

    @Autowired
    ReasonService reasonService;
    @Autowired
    private ListProductNameable lstProductTag;

    List<ReasonDTO> reasons;
    StockTransDTO inputStockTransDTO;
    List<StockTransFullDTO> stockTransDetailDTOs;
    private boolean transport = true;
    private boolean logistics = true;
    private RequiredRoleMap requiredRoleMap;
    private StockRequestDTO stockRequestDTOViewDetail;


    private boolean infoOrderDetail;
    private boolean created = false;

    boolean viewRquestDetail = false;
    private int limitAutoComplete;
    private boolean disableChangeDOA;
    private StaffDTO staffLoginDTO;
    private StockRequestDTO stockRequestDTOSearch;
    private ConfigListProductTagDTO configListProductTagDTO;
    private List<StockRequestDTO> lsRequestSearch;
    private StockRequestDTO stockRequestDTOSelect;
    private List<StockTransFullDTO> lsStockTransFull = new ArrayList<>();
    private List<StockTransSerialDTO> lsSerial = Lists.newArrayList();
    boolean showSearch;


    @PostConstruct
    public void init() {
        try {
            requiredRoleMap = CustomAuthenticationProvider.createRequiredRoleMap(Const.PERMISION.ROLE_STOCK_NUM_SHOP);
            showSearch = false;
            viewRquestDetail = false;
//            initControl();
            initDataSearch();
        } catch (Exception ex) {
            logger.error(ex);
            reportError("", "", "common.error.happen");
        }
    }

    private void initControl() {
        try {

            created = false;
            stockDebitDTO = null;
            inputStockTransDTO = new StockTransDTO();
            staffLoginDTO = getStaffDTO();
            inputStockTransDTO.setActionCode(staffService.getTransCode(Const.STOCK_TRANS.TRANS_CODE_PX, Const.STOCK_TRANS_TYPE.EXPORT, getStaffDTO()));
            inputStockTransDTO.setUserCreate(staffLoginDTO.getStaffCode());
            inputStockTransDTO.setStaffId(staffLoginDTO.getStaffId());
            inputStockTransDTO.setStaffCode(staffLoginDTO.getStaffCode());
            inputStockTransDTO.setShopId(staffLoginDTO.getShopId());
            //
            inputStockTransDTO.setActionType(Const.STOCK_TRANS_ACTION_TYPE.NOTE);
            inputStockTransDTO.setStockTransStatus(Const.STOCK_TRANS_STATUS.EXPORTED);
            //
            StaffDTO staffDTO = staffService.findOne(staffLoginDTO.getStaffId());
            if (DataUtil.isNullObject(staffDTO)
                    || DataUtil.isNullOrZero(staffDTO.getStaffId())) {
                reportError("", "", getText("mn.return.stock.init.validate.parentShop.error"));
                return;
            }
            ShopDTO shopDTO = shopService.findOne(getStaffDTO().getShopId());
            if (DataUtil.isNullObject(shopDTO)
                    || DataUtil.isNullOrZero(shopDTO.getShopId())) {
                reportError("", "", getText("mn.return.stock.init.validate.parentShop.error"));
                return;
            }
            inputStockTransDTO.setUserCreate(staffDTO.getName());
            inputStockTransDTO.setFromOwnerId(shopDTO.getShopId());
            inputStockTransDTO.setFromOwnerType(Const.OWNER_TYPE.SHOP_LONG);
            inputStockTransDTO.setFromOwnerName(shopDTO.getName());
            inputStockTransDTO.setFromOwnerAddress(shopDTO.getAddress());
            //
            ShopDTO parentShopDTO = shopService.findOne(shopDTO.getParentShopId());
            if (DataUtil.isNullObject(parentShopDTO)
                    || DataUtil.isNullOrZero(parentShopDTO.getShopId())) {
                reportError("", "", getText("mn.return.stock.init.validate.parentShop.error"));
                return;
            }
            inputStockTransDTO.setToOwnerName(parentShopDTO.getName());
            inputStockTransDTO.setToOwnerId(parentShopDTO.getShopId());
            inputStockTransDTO.setToOwnerType(Const.OWNER_TYPE.SHOP_LONG);
            inputStockTransDTO.setToOwnerAddress(parentShopDTO.getAddress());
            //
            inputStockTransDTO.setCreateDatetime(getSysdateFromDB());
            inputStockTransDTO.setTransport(Const.STOCK_TRANSPORT_TYPE.YES);
            inputStockTransDTO.setIsAutoGen(Const.STOCK_TRANS.IS_AUTO_GEN_LOGISTIC);
            inputStockTransDTO.setLogicstic(Const.STOCK_TRANS.IS_LOGISTIC);

            inputStockTransDTO.setRequest(stockRequestService.getRequest());
            //
//            reasons = DataUtil.defaultIfNull(reasonService.getLsReasonByType(Const.REASON_TYPE.STOCK_EXP_STAFF_SHOP), new ArrayList<ReasonDTO>());
            reasons = DataUtil.defaultIfNull(reasonService.getLsReasonByType(Const.REASON_CODE.STOCK_EXP_SERNIOR), new ArrayList<ReasonDTO>());
            //
            //lstProductTag.init(this, new ConfigListProductTagDTO(Const.MODE_SERIAL.MODE_ADD_ALL, BccsLoginSuccessHandler.getStaffDTO().getStaffId(), Const.OWNER_TYPE.STAFF));

            configListProductTagDTO = new ConfigListProductTagDTO(Const.MODE_SERIAL.MODE_ADD_ALL, BccsLoginSuccessHandler.getStaffDTO().getShopId(), Const.OWNER_TYPE.SHOP);
            List<OptionSetValueDTO> lsProductStatus = DataUtil.defaultIfNull(optionSetValueService.getByOptionSetCode(Const.OPTION_SET.PRODUCT_STATUS), new ArrayList<>());
            List<OptionSetValueDTO> lsProductStatus2 = lsProductStatus.stream().filter(obj -> Const.GOODS_STATE.BROKEN.equals(DataUtil.safeToLong(obj.getValue()))).collect(Collectors.toList());
            configListProductTagDTO.setLsProductStatus(lsProductStatus2);
            lstProductTag.init(this, configListProductTagDTO);
            //
            transport = false;
            //
            logistics = false;
            //
//            lstProductTag.setLsProductStatus(optionSetValueService.getLsOptionSetValueByCode("PRODUCT_DAMAGE_STATUS"));
            //
            if (!hasLogsitics()) {
                inputStockTransDTO.setIsAutoGen(null);
                inputStockTransDTO.setLogicstic(null);
            }
            if (!hasTransport()) {
                inputStockTransDTO.setTransport(null);
            }
            //xu ly load han muc kho nhan neu kho nhan khong phai la VT
            if (!(Const.SHOP.SHOP_VTT_ID.equals(inputStockTransDTO.getToOwnerId()) || Const.SHOP.SHOP_PARENT_VTT_ID.equals(inputStockTransDTO.getToOwnerId()))) {
                if (inputStockTransDTO.getToOwnerId() != null && inputStockTransDTO.getToOwnerType() != null) {
                    stockDebitDTO = stockDebitService.findStockDebitValue(DataUtil.safeToLong(inputStockTransDTO.getToOwnerId()), DataUtil.safeToString(inputStockTransDTO.getToOwnerType()));
                }
            }
            executeCommand("updateControls()");

        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
    }

    private void initDataSearch() throws LogicException, Exception {
        StaffDTO staffDTO = BccsLoginSuccessHandler.getStaffDTO();
        limitAutoComplete = DataUtil.safeToInt(optionSetValueService.getValueByNameFromOptionSetValue(Const.OPTION_SET.LIMIT_AUTOCOMPLETE));
        Date currentDate = optionSetValueService.getSysdateFromDB(true);
        stockRequestDTOSearch = new StockRequestDTO();
        stockRequestDTOSearch.setFromDate(currentDate);
        stockRequestDTOSearch.setToDate(currentDate);
        stockRequestDTOSearch.setOwnerType(Const.OWNER_TYPE.SHOP_LONG);

        shopInfoTagSearch.initShopAndAllChild(this, DataUtil.safeToString(staffDTO.getShopId()), true, Const.OWNER_TYPE.SHOP);
        staffInfoTagSearch.initStaff(this, DataUtil.safeToString(staffDTO.getShopId()));

        shopInfoTag.initShopAndAllChild(this, DataUtil.safeToString(staffDTO.getShopId()), true, Const.OWNER_TYPE.SHOP);
        staffInfoTag.initStaff(this, DataUtil.safeToString(staffDTO.getShopId()));


//        listProductTag.init(this, configListProductTagDTO);
        writeOfficeTag.init(this, staffDTO.getShopId());
        lsRequestSearch = Lists.newArrayList();
        disableChangeDOA = false;
    }


    @Secured("@")
    public StreamedContent printStockTransDetail() {
        try {
            doValidateListDetail(lstProductTag.getListTransDetailDTOs());
            List<StockTransDetailDTO> details = lstProductTag.getListTransDetailDTOs();
            inputStockTransDTO.setFromOwnerAddress(null);
            inputStockTransDTO.setToOwnerAddress(null);
            inputStockTransDTO.setStockTransActionId(null);
            return exportStockTransDetail(inputStockTransDTO, details);
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
        return null;
    }


    @Secured("@")
    public void doCreateReturnStock() {
        try {
//            StockTransActionDTO stockTransActionDTO = createAction();
            List<StockTransDetailDTO> listTransDetailDTOs = lstProductTag.getListTransDetailDTOs();
            if (inputStockTransDTO.getActionCode().getBytes("UTF-8").length > 50) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "export.note.transCode.valid.maxlength");
            }
            if (DataUtil.isNullOrEmpty(listTransDetailDTOs)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "stock.trans.staff.export.detailRequired");
            }
            for (StockTransDetailDTO detailDTO : listTransDetailDTOs) {
                if (DataUtil.safeEqual(detailDTO.getCheckSerial(), Const.PRODUCT_OFFERING.CHECK_SERIAL)
                        && DataUtil.isNullOrEmpty(detailDTO.getLstSerial())) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "staff.return.stock.valid.serial", detailDTO.getProdOfferName());
                }
            }
            inputStockTransDTO.setFromStock(Const.FROM_STOCK.FROM_STAFF);
//            BaseMessage msg = executeStockTrans.executeStockTrans(Const.STOCK_TRANS.EXPORT, Const.STOCK_TRANS_TYPE.STAFF_EXP, inputStockTransDTO, stockTransActionDTO, listTransDetailDTOs, getTransRequiRedRoleMap());
            StockTransDTO stockTransDTO = returnStockService.saveReturnStock(inputStockTransDTO, listTransDetailDTOs, getTransRequiRedRoleMap());
//            if (msg == null) {
//                throw new Exception("Khong nhan duoc ket qua tu webservice!");
//            }
//            if (!DataUtil.isNullOrEmpty(msg.getErrorCode())) {
//                throw new LogicException(msg.getErrorCode(), msg.getKeyMsg(), msg.getParamsMsg());
//            }
            if (DataUtil.isNullObject(stockTransDTO)
                    || DataUtil.isNullOrZero(stockTransDTO.getStockTransId())) {
                throw new Exception("Khong nhan duoc ket qua tu webservice!");
            }
            inputStockTransDTO.setStockTransId(stockTransDTO.getStockTransId());
            inputStockTransDTO.setStockTransActionId(stockTransDTO.getStockTransActionId());
            topReportSuccess("", "stock.trans.staff.export.susscess");
            created = true;
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
    }

    @Secured("@")
    public void onChangeTransportType() {
        if (transport) {
            inputStockTransDTO.setTransport(Const.STOCK_TRANSPORT_TYPE.YES);
        } else {
            inputStockTransDTO.setTransport(null);
        }
    }

    @Secured("@")
    public void onChangeLogistics() {
        if (logistics) {
            inputStockTransDTO.setIsAutoGen(Const.STOCK_TRANS.IS_AUTO_GEN_LOGISTIC);
            inputStockTransDTO.setLogicstic(Const.STOCK_TRANS.IS_LOGISTIC);
        } else {
            inputStockTransDTO.setIsAutoGen(null);
            inputStockTransDTO.setLogicstic(null);
        }
    }

    @Secured("@")
    public void onChangeReason() {
        if (!DataUtil.isNullOrEmpty(reasons) && inputStockTransDTO.getReasonId() != null) {
            for (ReasonDTO reason : reasons) {
                if (reason.getReasonId().equals(inputStockTransDTO.getReasonId())) {
                    inputStockTransDTO.setReasonName(reason.getReasonName());
                    break;
                }
            }
        }
    }

    private StockTransActionDTO createAction() {
        StockTransActionDTO action = new StockTransActionDTO();
        action.setActionCode(inputStockTransDTO.getActionCode());
        action.setCreateUser(getStaffDTO().getStaffCode());
        action.setActionStaffId(getStaffDTO().getStaffId());
        action.setNote(inputStockTransDTO.getNote());
        return action;
    }

    @Secured("@")
    public void doValidate() {
        try {
            if (inputStockTransDTO.getActionCode().getBytes("UTF-8").length > 50) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "export.note.transCode.valid.maxlength");
            }
            doValidateListDetail(lstProductTag.getListTransDetailDTOs());
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
    }

    @Secured("@")
    public boolean hasLogsitics() {
        try {
            if (getTransRequiRedRoleMap().hasRole(Const.PERMISION.ROLE_SYNC_LOGISTIC)) {
                List<OptionSetValueDTO> options = DataUtil.defaultIfNull(optionSetValueService.getByOptionsetCodeAndValue(Const.OPTION_SET.LOGISTIC_SHOP_ID_LIST, getStaffDTO().getShopCode()), new ArrayList<>());
                return !DataUtil.isNullOrEmpty(options);
            }
        } catch (Exception exx) {
            logger.error(exx.getMessage(), exx);
        }
        return false;
    }

    @Secured("@")
    public void doSearch() {
        try {
            staffLoginDTO = getStaffDTO();
            if (stockRequestDTOSearch.getOwnerType() == null) {
                throw new LogicException("", "stock.type.not.null");
            }
            if (!(Const.OWNER_TYPE.SHOP_LONG.equals(stockRequestDTOSearch.getOwnerType()) || Const.OWNER_TYPE.STAFF_LONG.equals(stockRequestDTOSearch.getOwnerType()))) {
                throw new LogicException("", "stock.type.not.invalid");
            }
            if (stockRequestDTOSearch.getFromDate() == null) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.from.date.not.blank");
            }
            if (stockRequestDTOSearch.getToDate() == null) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.to.date.not.blank");
            }
            if ((DateUtil.compareDateToDate(stockRequestDTOSearch.getFromDate(), stockRequestDTOSearch.getToDate()) > 0)
                    || DateUtil.daysBetween2Dates(stockRequestDTOSearch.getToDate(), stockRequestDTOSearch.getFromDate()) > 30L) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "view.stock.offer.cycel.fromDate.endDate", 30);
            }
            lsRequestSearch = stockRequestService.getLsSearchStockRequest(stockRequestDTOSearch.getRequestCode(), stockRequestDTOSearch.getFromDate(),
                    stockRequestDTOSearch.getToDate(), stockRequestDTOSearch.getStatus(), staffLoginDTO.getShopId(), stockRequestDTOSearch.getOwnerId(), stockRequestDTOSearch.getOwnerType());
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }

    }

    @Secured("@")
    public void doResetSearch() {
        return;
    }

    @Secured("@")
    public void doCreateRequest() {
        initControl();
        viewRquestDetail = true;
    }

    public void doShowInfoOrderDetail(StockRequestDTO stockRequestDTO) {
        try {
            Long stockTransId = stockRequestDTO.getStockTransId();
            StockTransActionDTO stockTransActionDTO = stockTransActionService.getStockTransActionByIdAndStatus(stockTransId, Lists.newArrayList(Const.STOCK_TRANS_STATUS.EXPORT_NOTE));
            if (stockTransActionDTO != null) {
                lsStockTransFull = DataUtil.defaultIfNull(stockTransService.searchStockTransDetail(Lists.newArrayList(stockTransActionDTO.getStockTransActionId())), new ArrayList<>());
                for (StockTransFullDTO stockTransFullDTO : lsStockTransFull) {
                    if ("1".equals(stockTransFullDTO.getCheckSerial())) {
                        stockTransFullDTO.setLstSerial(DataUtil.defaultIfNull(stockTransSerialService.findByStockTransDetailId(stockTransFullDTO.getStockTransDetailId()), new ArrayList<>()));
                    }
                }
            }
            showSearch = true;
            this.stockRequestDTOViewDetail = DataUtil.cloneBean(stockRequestDTO);
            if (Const.OWNER_TYPE.SHOP_LONG.equals(stockRequestDTOViewDetail.getOwnerType())) {
                ShopDTO shopDTO = shopService.findOne(stockRequestDTOViewDetail.getOwnerId());
                if (shopDTO != null) {
                    this.stockRequestDTOViewDetail.setOwnerCode(shopDTO.getShopCode());
                    this.stockRequestDTOViewDetail.setOwnerName(shopDTO.getName());
                }
            } else if (Const.OWNER_TYPE.STAFF_LONG.equals(stockRequestDTOViewDetail.getOwnerType())) {
                StaffDTO staffDTOTmp = staffService.findOne(stockRequestDTOViewDetail.getOwnerId());
                if (staffDTOTmp != null) {
                    this.stockRequestDTOViewDetail.setOwnerCode(staffDTOTmp.getStaffCode());
                    this.stockRequestDTOViewDetail.setOwnerName(staffDTOTmp.getName());
                }
            }
            stockRequestDTOViewDetail.setRequestCode(stockRequestDTO.getRequestCode());
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex);
            topPage();
        } catch (Exception ex) {
            logger.error(ex);
            reportError("", "", "common.error.happened");
            topPage();
        }
    }

    public void doShowViewSerial(Long stockTransDetailId) {
        try {
            lsSerial = DataUtil.defaultIfNull(stockTransSerialService.findByStockTransDetailId(stockTransDetailId), new ArrayList<>());
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex);
            topPage();
        } catch (Exception ex) {
            logger.error(ex);
            reportError("", "", "common.error.happened");
            topPage();
        }
    }

    @Secured("@")
    public void doBackPage() {
        showSearch = false;
        viewRquestDetail = false;
    }

    @Secured("@")
    public StreamedContent exportHandOverReport() {
        try {
            StockTransDTO stockTransDTO = DataUtil.cloneBean(inputStockTransDTO);
            if (DataUtil.isNullObject(stockTransDTO)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "stock.trans.search.validate.selecttohanover");
            }

            StreamedContent content = exportHandOverReport(stockTransDTO);
            return content;
        } catch (LogicException ex) {
            topReportError("", ex);
            topPage();
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
            topPage();
        }
        return null;
    }


    @Secured("@")
    public boolean hasTransport() {
        return getTransRequiRedRoleMap().hasRole(Const.PERMISION.ROLE_TRANSPORT_STOCK);
    }

    public StockDebitDTO getStockDebitDTO() {
        return stockDebitDTO;
    }

    public void setStockDebitDTO(StockDebitDTO stockDebitDTO) {
        this.stockDebitDTO = stockDebitDTO;
    }

    public boolean isViewRquestDetail() {
        return viewRquestDetail;
    }

    public void setViewRquestDetail(boolean viewRquestDetail) {
        this.viewRquestDetail = viewRquestDetail;
    }

    public BaseMessage getBaseMessage() {
        return baseMessage;
    }

    public void setBaseMessage(BaseMessage baseMessage) {
        this.baseMessage = baseMessage;
    }

    public ShopInfoNameable getShopInfoTag() {
        return shopInfoTag;
    }

    public void setShopInfoTag(ShopInfoNameable shopInfoTag) {
        this.shopInfoTag = shopInfoTag;
    }

    public ShopInfoNameable getShopInfoTagSearch() {
        return shopInfoTagSearch;
    }

    public void setShopInfoTagSearch(ShopInfoNameable shopInfoTagSearch) {
        this.shopInfoTagSearch = shopInfoTagSearch;
    }

    public StaffInfoNameable getStaffInfoTag() {
        return staffInfoTag;
    }

    public void setStaffInfoTag(StaffInfoNameable staffInfoTag) {
        this.staffInfoTag = staffInfoTag;
    }

    public StaffInfoNameable getStaffInfoTagSearch() {
        return staffInfoTagSearch;
    }

    public void setStaffInfoTagSearch(StaffInfoNameable staffInfoTagSearch) {
        this.staffInfoTagSearch = staffInfoTagSearch;
    }

    public SignOfficeTagNameable getWriteOfficeTag() {
        return writeOfficeTag;
    }

    public void setWriteOfficeTag(SignOfficeTagNameable writeOfficeTag) {
        this.writeOfficeTag = writeOfficeTag;
    }

    public int getLimitAutoComplete() {
        return limitAutoComplete;
    }

    public void setLimitAutoComplete(int limitAutoComplete) {
        this.limitAutoComplete = limitAutoComplete;
    }

    public boolean isDisableChangeDOA() {
        return disableChangeDOA;
    }

    public void setDisableChangeDOA(boolean disableChangeDOA) {
        this.disableChangeDOA = disableChangeDOA;
    }


    public StockRequestDTO getStockRequestDTOSearch() {
        return stockRequestDTOSearch;
    }

    public void setStockRequestDTOSearch(StockRequestDTO stockRequestDTOSearch) {
        this.stockRequestDTOSearch = stockRequestDTOSearch;
    }

    public ConfigListProductTagDTO getConfigListProductTagDTO() {
        return configListProductTagDTO;
    }

    public void setConfigListProductTagDTO(ConfigListProductTagDTO configListProductTagDTO) {
        this.configListProductTagDTO = configListProductTagDTO;
    }

    public List<StockRequestDTO> getLsRequestSearch() {
        return lsRequestSearch;
    }

    public void setLsRequestSearch(List<StockRequestDTO> lsRequestSearch) {
        this.lsRequestSearch = lsRequestSearch;
    }

    public StockRequestDTO getStockRequestDTOSelect() {
        return stockRequestDTOSelect;
    }

    public void setStockRequestDTOSelect(StockRequestDTO stockRequestDTOSelect) {
        this.stockRequestDTOSelect = stockRequestDTOSelect;
    }

    public List<StockTransFullDTO> getLsStockTransFull() {
        return lsStockTransFull;
    }

    public void setLsStockTransFull(List<StockTransFullDTO> lsStockTransFull) {
        this.lsStockTransFull = lsStockTransFull;
    }

    public boolean isLogistics() {
        return logistics;
    }

    public void setLogistics(boolean logistics) {
        this.logistics = logistics;
    }

    public boolean isTransport() {
        return transport;
    }

    public void setTransport(boolean transport) {
        this.transport = transport;
    }

    public ListProductNameable getLstProductTag() {
        return lstProductTag;
    }

    public void setLstProductTag(ListProductNameable lstProductTag) {
        this.lstProductTag = lstProductTag;
    }

    public List<ReasonDTO> getReasons() {
        return reasons;
    }

    public void setReasons(List<ReasonDTO> reasons) {
        this.reasons = reasons;
    }

    public List<StockTransFullDTO> getStockTransDetailDTOs() {
        return stockTransDetailDTOs;
    }

    public void setStockTransDetailDTOs(List<StockTransFullDTO> stockTransDetailDTOs) {
        this.stockTransDetailDTOs = stockTransDetailDTOs;
    }

    public StockTransDTO getInputStockTransDTO() {
        return inputStockTransDTO;
    }

    public void setInputStockTransDTO(StockTransDTO inputStockTransDTO) {
        this.inputStockTransDTO = inputStockTransDTO;
    }

    public boolean isCreated() {
        return created;
    }

    public void setCreated(boolean created) {
        this.created = created;
    }

    public void showInfoOrderDetail() {
        infoOrderDetail = true;
    }

    public boolean isInfoOrderDetail() {
        return infoOrderDetail;
    }

    public void setInfoOrderDetail(boolean infoOrderDetail) {
        this.infoOrderDetail = infoOrderDetail;
    }

    public boolean isShowStockDebit() {
        return stockDebitDTO != null;
    }

    public boolean isShowSearch() {
        return showSearch;
    }

    public void setShowSearch(boolean showSearch) {
        this.showSearch = showSearch;
    }

    public List<StockTransSerialDTO> getLsSerial() {
        return lsSerial;
    }

    public void setLsSerial(List<StockTransSerialDTO> lsSerial) {
        this.lsSerial = lsSerial;
    }

    public StockRequestDTO getStockRequestDTOViewDetail() {
        return stockRequestDTOViewDetail;
    }

    public void setStockRequestDTOViewDetail(StockRequestDTO stockRequestDTOViewDetail) {
        this.stockRequestDTOViewDetail = stockRequestDTOViewDetail;
    }
}
