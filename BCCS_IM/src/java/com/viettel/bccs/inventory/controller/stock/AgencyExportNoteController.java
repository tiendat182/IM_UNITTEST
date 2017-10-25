package com.viettel.bccs.inventory.controller.stock;

import com.google.common.collect.Lists;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.fw.controller.TransCommonController;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.*;
import com.viettel.bccs.inventory.tag.OrderStockTagNameable;
import com.viettel.bccs.inventory.tag.ShopInfoNameable;
import com.viettel.bccs.inventory.tag.SignOfficeTagNameable;
import com.viettel.bccs.sale.dto.SaleTransDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
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
import java.util.*;

/**
 * Created by hoangnt14 on 01/13/2016.
 */
@Component
@Scope("view")
@ManagedBean
public class AgencyExportNoteController extends TransCommonController {

    private Boolean infoOrderDetail = false;
    private Boolean writeOffice = true;
    private String nameMethod = "receiveWriteOffice";
    private int limitAutoComplete;
    private String actionCodeNote;
    private VStockTransDTO forSearch;
    private RequiredRoleMap requiredRoleMap;
    private List<String> lstStatus;
    private Long stockTransActionId;

    private List<OptionSetValueDTO> optionSetValueDTOsList;
    private List<VStockTransDTO> vStockTransDTOList;
    private List<StockTransFullDTO> lsStockTransFull;

    private Boolean canPrint;
    private boolean logictis;
    private String status;
    @Autowired
    private SignOfficeTagNameable writeOfficeTag;//khai bao tag ky vOffice
    @Autowired
    private OrderStockTagNameable orderStockTag;
    @Autowired
    private StockTransService stockTransService;
    @Autowired
    private OptionSetValueService optionSetValueService;
    @Autowired
    private ShopInfoNameable shopInfoTagReceive;
    @Autowired
    private ExecuteStockTransService executeStockTransService;
    @Autowired
    private StaffService staffService;
    @Autowired
    private StockTransLogisticService stockTransLogisticService;
    @Autowired
    private StockTransActionService stockTransActionService;
    @Autowired
    private StockTransVofficeService stockTransVofficeService;

    @Secured("@")
    @PostConstruct
    public void init() {
        try {
            lstStatus = Lists.newArrayList();
            lstStatus.add(Const.STOCK_TRANS_STATUS.EXPORT_ORDER);
            lstStatus.add(Const.STOCK_TRANS_STATUS.EXPORT_NOTE);
            lstStatus.add(Const.STOCK_TRANS_STATUS.DESTROYED);
            String shopId = DataUtil.safeToString(BccsLoginSuccessHandler.getStaffDTO().getShopId());
            shopInfoTagReceive.initAgent(this, shopId);
            doResetStockTrans();
            optionSetValueDTOsList = optionSetValueService.getByOptionSetCode(Const.OPTION_SET.STOCK_TRANS_AGENT_EXPORT_STATUS);
            Collections.sort(optionSetValueDTOsList, new Comparator<OptionSetValueDTO>() {

                @Override
                public int compare(OptionSetValueDTO o1, OptionSetValueDTO o2) {
                    return Integer.compare(Integer.parseInt(o1.getValue()), Integer.parseInt(o2.getValue()));
                }
            });
            limitAutoComplete = DataUtil.safeToInt(optionSetValueService.getValueByNameFromOptionSetValue(Const.OPTION_SET.LIMIT_AUTOCOMPLETE));
            requiredRoleMap = CustomAuthenticationProvider.createRequiredRoleMap(Const.PERMISION.ROLE_SYNC_LOGISTIC, Const.PERMISION.ROLE_TRANSPORT_STOCK, Const.PERMISION.ROLE_XUAT_KHO_BA_MIEN, Const.PERMISION.ROLE_STOCK_NUM_SHOP);
            orderStockTag.init(this, writeOffice);
            logictis = false;
        } catch (Exception e) {
            logger.error(e);
            reportError("", "common.error.happened", e);
        }
    }

    @Secured("@")
    public void doSearchStockTrans() {
        try {
            customStockTrans(forSearch);
            forSearch.setActionType(Const.STOCK_TRANS_ACTION_TYPE.COMMAND);
            vStockTransDTOList = stockTransService.searchVStockTrans(forSearch);
            if (!DataUtil.isNullObject(vStockTransDTOList)) {
                for (VStockTransDTO vStockTransDTO : vStockTransDTOList) {
                    for (OptionSetValueDTO optionSetValueDTO : optionSetValueDTOsList) {
                        if (DataUtil.safeEqual(vStockTransDTO.getStockTransStatus(), optionSetValueDTO.getValue())) {
                            vStockTransDTO.setStatusName(optionSetValueDTO.getName());
                            break;
                        }
                    }

                }
                for (VStockTransDTO vStockTransDTO : vStockTransDTOList) {
                    // set giao dich ban dut, dat coc
                    if (!DataUtil.isNullObject(vStockTransDTO) &&
                            !DataUtil.isNullOrEmpty(vStockTransDTO.getDepositStatus()) &&
                            DataUtil.safeEqual(vStockTransDTO.getDepositStatus(), Const.DEPOSIT_STATUS.DEPOSIT_HAVE)) {
                        vStockTransDTO.setStockTransAgent(Const.STOCK_TRANS_TYPE.DEPOSIT);
                    } else if (!DataUtil.isNullObject(vStockTransDTO) &&
                            !DataUtil.isNullOrEmpty(vStockTransDTO.getPayStatus())) {
                        vStockTransDTO.setStockTransAgent(Const.STOCK_TRANS_TYPE.PAY);
                    }
                }
            }
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex);
            topPage();
        } catch (Exception e) {
            logger.error(e);
            reportError("", "common.error.happened", e);
        }
    }

    @Secured("@")
    public void doResetStockTrans() {
        forSearch = new VStockTransDTO();
        vStockTransDTOList = Lists.newArrayList();
        shopInfoTagReceive.resetShop();
        Date currentDate = Calendar.getInstance().getTime();
        forSearch = new VStockTransDTO(currentDate, currentDate);
        forSearch.setFromOwnerName(BccsLoginSuccessHandler.getStaffDTO().getShopName());
        forSearch.setFromOwnerID(BccsLoginSuccessHandler.getStaffDTO().getShopId());
        forSearch.setStockTransType(Const.STOCK_TRANS.STOCK_TRANS_TYPE_DEPOSIT);
        forSearch.setLstStatus(lstStatus);
        forSearch.setUserShopId(BccsLoginSuccessHandler.getStaffDTO().getShopId());
        forSearch.setVtUnit(Const.VT_UNIT.AGENT);
        forSearch.setObjectType(Const.OWNER_TYPE.SHOP);
        //custom
        infoOrderDetail = false;
        stockTransActionId = null;
    }

    private void customStockTrans(VStockTransDTO search) {
        if (DataUtil.safeEqual(status, "1")) {
            search.setStockTransStatus(Const.STOCK_TRANS_STATUS.EXPORT_ORDER);
            search.setDepositStatus(null);
            search.setPayStatus(null);
            search.setInvoiceStatus(null);
//            search.setLstDepositStatus(Lists.newArrayList(Const.DEPOSIT_STATUS.DEPOSIT_HAVE));
//            search.setLstPayStatus(Lists.newArrayList(Const.PAY_STATUS.NOT_PAY));
            search.setInvoiceStatus(null);
        } else if (DataUtil.safeEqual(status, "3")) {
            search.setStockTransStatus(Const.STOCK_TRANS_STATUS.EXPORT_NOTE);
            search.setLstDepositStatus(null);
            search.setLstPayStatus(null);
            search.setInvoiceStatus(null);
            search.setDepositStatus(null);
            search.setPayStatus(null);
        } else if (DataUtil.safeEqual(status, "4")) {
            search.setStockTransStatus(Const.STOCK_TRANS_STATUS.DESTROYED);
//            search.setLstDepositStatus(Lists.newArrayList(Const.DEPOSIT_STATUS.NOT_DEPOSIT));
//            search.setLstPayStatus(Lists.newArrayList(Const.PAY_STATUS.NOT_PAY));
            search.setInvoiceStatus(null);
            search.setDepositStatus(null);
            search.setPayStatus(null);
            forSearch.setLstStatus(null);
        } else {
            forSearch.setLstStatus(lstStatus);
            search.setStockTransStatus(null);
            search.setLstDepositStatus(null);
            search.setLstPayStatus(null);
            search.setInvoiceStatus(null);
            search.setDepositStatus(null);
        }

    }

    @Secured("@")
    public void doShowInfoOrderDetail(Long stockTransActionId, Long stockTransStatus) {
        try {
            canPrint = false;
            orderStockTag.loadOrderStock(stockTransActionId, true);
            StockTransActionDTO oldStockTransActionDTO = stockTransActionService.findOne(stockTransActionId);
            // Check trang thai thanh toan. Neu giao dich ban dut
            StockTransInputDTO stockTransInputDTO = orderStockTag.getTransInputDTO();
            if (DataUtil.isNullObject(stockTransInputDTO) || DataUtil.isNullObject(stockTransInputDTO.getStockTransId())) {
                throw new LogicException("", "stock.rescue.warranty.view.detail.not.esxit");
            }
            if (!DataUtil.isNullOrEmpty(stockTransInputDTO.getPayStatus()) && DataUtil.safeEqual(Const.PAY_STATUS.PAY_HAVE, stockTransInputDTO.getPayStatus())) {
                List<SaleTransDTO> lstSaleTrans = stockTransService.getSaleTransFromStockTrans(oldStockTransActionDTO.getStockTransId());
                if (DataUtil.isNullOrEmpty(lstSaleTrans)) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.exp.error.notAllowExpNonPay");
                }
                SaleTransDTO saleTransDTO = lstSaleTrans.get(0);
                if (!DataUtil.safeEqual(saleTransDTO.getStatus(), Const.SALE_TRANS.SALE_BILLED)) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.exp.error.notAllowExpNonBill");
                }
            }

            StockTransLogisticDTO stockTransLogisticDTO = stockTransLogisticService.findByStockTransId(oldStockTransActionDTO.getStockTransId());
            if (!DataUtil.isNullObject(stockTransLogisticDTO)) {
                logictis = false;
                throw new LogicException("", "export.order.logicstics.export.note");
            }
            //5. Check trang thai ki voffice
            if (DataUtil.safeEqual(oldStockTransActionDTO.getSignCaStatus(), "2")) {
                //Validate giao dich da ky voffice chua
                stockTransVofficeService.doSignedVofficeValidate(oldStockTransActionDTO);
            }
            if (orderStockTag.getTransInputDTO().getRegionStockTransId() != null) {
                StockTransDTO regionTransDTO = stockTransService.findOne(orderStockTag.getTransInputDTO().getRegionStockTransId());
                if (regionTransDTO != null) {
                    orderStockTag.getTransInputDTO().setRegionStockId(regionTransDTO.getFromOwnerId());
                    ShopDTO shopDTO = shopService.findOne(regionTransDTO.getFromOwnerId());
                    if (shopDTO != null) {
                        orderStockTag.getTransInputDTO().setRegionStockName(shopDTO.getName());
                    }
                }
            }
            if (stockTransStatus != null && stockTransStatus >= 2) {
                canPrint = true;
            } else {
                canPrint = false;
            }

            List<Long> lsStockTransId = Lists.newArrayList(stockTransActionId);
            lsStockTransFull = DataUtil.defaultIfNull(stockTransService.searchStockTransDetail(lsStockTransId), new ArrayList<StockTransFullDTO>());
            infoOrderDetail = true;
            actionCodeNote = staffService.getTransCode(Const.STOCK_TRANS.TRANS_CODE_PX, Const.STOCK_TRANS_TYPE.EXPORT, BccsLoginSuccessHandler.getStaffDTO());
            // Khoi tao Voffice
            StaffDTO staffDTO = BccsLoginSuccessHandler.getStaffDTO();
            writeOfficeTag.init(this, staffDTO.getShopId());
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex);
            topPage();
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
            logger.error(ex.getMessage(), ex);
            topPage();
        }
    }

    @Secured("@")
    public void doDeleteOrder(Long stockTransActionId) {
        try {
//            stockTransActionId = null; Sao lai set = null?
            StockTransActionDTO oldStockTransActionDTO = stockTransActionService.findOne(stockTransActionId);
            if (DataUtil.safeEqual(oldStockTransActionDTO.getSignCaStatus(), "2")) {
                //Validate giao dich da ky voffice chua
                stockTransVofficeService.doSignedVofficeValidate(oldStockTransActionDTO);
            }
            orderStockTag.loadOrderStock(stockTransActionId, true);
            StockTransActionDTO stockTransActionDTO = orderStockTag.getStockTransActionDTO();
            StockTransDTO stockTransDTO = orderStockTag.getStockTransDTO();
            //set kho 3 mien
//            if (!DataUtil.isNullObject(stockTransDTO.getRegionStockTransId())) {
//                StockTransDTO regionStockTrans = stockTransService.findOne(stockTransDTO.getRegionStockTransId());
//                if (!DataUtil.isNullObject(regionStockTrans)) {
//                    stockTransDTO.setRegionStockId(regionStockTrans.getFromOwnerId());
//                }
//            }

//            List<StockTransDetailDTO> stockTransDetailDTOs = stockTransDetailService.findByFilter(Lists.newArrayList(
//                    new FilterRequest(StockTransDetail.COLUMNS.STOCKTRANSID.name(), FilterRequest.Operator.EQ,
//                            stockTransDTO.getStockTransId())));
            List<StockTransDetailDTO> stockTransDetailDTOs = null;
            BaseMessage message = executeStockTransService.executeStockTrans(Const.STOCK_TRANS.CANCEL_TRANS_AGENT, Const.STOCK_TRANS_TYPE.EXPORT, stockTransDTO, stockTransActionDTO, stockTransDetailDTOs, requiredRoleMap);
            if (!DataUtil.isNullOrEmpty(message.getErrorCode())) {
                throw new LogicException(message.getErrorCode(), message.getKeyMsg());
            }
            reportSuccess("frmExportNote:msgExport", "stock.export.stock.confirm.cancel.success");
            topPage();
            doSearchStockTrans();
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("frmExportNote:msgExport", ex);
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportErrorValidateFail("frmExportNote:msgExport", "", "common.error.happen");
            topPage();
        }
    }

    @Secured("@")
    public void doBackPage() {
        infoOrderDetail = false;
        logictis = false;
        orderStockTag.resetOrderStock();
        lsStockTransFull = Lists.newArrayList();
        doSearchStockTrans();
    }

    @Secured("@")
    public void doReceiveShop(VShopStaffDTO vShopStaffDTO) {
        if (vShopStaffDTO != null) {
            forSearch.setToOwnerID(DataUtil.safeToLong(vShopStaffDTO.getOwnerId()));
        }
    }

    @Secured("@")
    public void clearCurrentShop(String strValue) {
        forSearch.setToOwnerID(null);
    }

    @Secured("@")
    public void doCreateExportNote() {
        try {
            stockTransActionId = null;
            StockTransActionDTO stockTransActionDTO = orderStockTag.getStockTransActionDTO();
            StockTransDTO stockTransDTO = orderStockTag.getStockTransDTO();
            if (writeOffice) {
                SignOfficeDTO signOfficeDTO = writeOfficeTag.validateVofficeAccount();
                stockTransDTO.setUserName(signOfficeDTO.getUserName());
//                String passwordEncrypt1 = PassWordUtil.getInstance().encrypt(signOfficeDTO.getPassWord());
//                String passwordEncrypt2 = EncryptionUtils.encrypt(passwordEncrypt1, EncryptionUtils.getKey());
                stockTransDTO.setPassWord(signOfficeDTO.getPassWord());
                stockTransDTO.setSignFlowId(signOfficeDTO.getSignFlowId());
                stockTransDTO.setSignVoffice(writeOffice);
            }
            stockTransActionDTO.setActionCode(actionCodeNote);
            BaseMessageStockTrans message = executeStockTransService.executeStockTrans(Const.STOCK_TRANS.NOTE_AGENT, Const.STOCK_TRANS_TYPE.EXPORT, stockTransDTO, stockTransActionDTO, new ArrayList<StockTransDetailDTO>(), requiredRoleMap);
            if (!DataUtil.isNullOrEmpty(message.getErrorCode())) {
                throw new LogicException(message.getErrorCode(), message.getKeyMsg(), message.getParamsMsg());
            }
            reportSuccess("", "export.note.create.success", actionCodeNote);
            stockTransActionId = message.getStockTransActionId();

            canPrint = true;
            StockTransDTO stockTransDTOForPint = orderStockTag.getStockTransDTOForPint();
            stockTransDTOForPint.setActionCode(actionCodeNote);
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

// Ham in lenh xuat excel

    public StreamedContent exportStockTransDetail() {
        try {
            StockTransDTO stockTransDTO = orderStockTag.getStockTransDTOForPint();
            if (stockTransDTO != null && canPrint) {
                stockTransDTO.setActionType(Const.STOCK_TRANS_ACTION_TYPE.NOTE);
                stockTransDTO.setStockTransStatus(Const.STOCK_TRANS_TYPE.EXPORT);
                stockTransDTO.setStockTransActionId(stockTransActionId);
                return exportStockTransDetail(stockTransDTO, lsStockTransFull);
            } else {
                throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "stockTrans.dto.print.not.exists");
            }
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happen", ex);
        }
        return null;
    }

    @Secured("@")
    public void doChangeWriteOffice() {
        //init cho vung thong tin ky vOffice
        if (writeOffice) {
//            StaffDTO staffDTO = BccsLoginSuccessHandler.getStaffDTO();
//            writeOfficeTag.init(this, staffDTO.getShopId());
        }
    }

    //getter and setter


    public boolean isLogictis() {
        return logictis;
    }

    public void setLogictis(boolean logictis) {
        this.logictis = logictis;
    }

    public Boolean getInfoOrderDetail() {
        return infoOrderDetail;
    }

    public void setInfoOrderDetail(Boolean infoOrderDetail) {
        this.infoOrderDetail = infoOrderDetail;
    }

    public Boolean getWriteOffice() {
        return writeOffice;
    }

    public void setWriteOffice(Boolean writeOffice) {
        this.writeOffice = writeOffice;
    }

    public String getNameMethod() {
        return nameMethod;
    }

    public void setNameMethod(String nameMethod) {
        this.nameMethod = nameMethod;
    }

    public List<OptionSetValueDTO> getOptionSetValueDTOsList() {
        return optionSetValueDTOsList;
    }

    public void setOptionSetValueDTOsList(List<OptionSetValueDTO> optionSetValueDTOsList) {
        this.optionSetValueDTOsList = optionSetValueDTOsList;
    }

    public List<VStockTransDTO> getvStockTransDTOList() {
        return vStockTransDTOList;
    }

    public void setvStockTransDTOList(List<VStockTransDTO> vStockTransDTOList) {
        this.vStockTransDTOList = vStockTransDTOList;
    }

    public VStockTransDTO getForSearch() {
        return forSearch;
    }

    public void setForSearch(VStockTransDTO forSearch) {
        this.forSearch = forSearch;
    }

    public List<StockTransFullDTO> getLsStockTransFull() {
        return lsStockTransFull;
    }

    public void setLsStockTransFull(List<StockTransFullDTO> lsStockTransFull) {
        this.lsStockTransFull = lsStockTransFull;
    }

    public OrderStockTagNameable getOrderStockTag() {
        return orderStockTag;
    }

    public void setOrderStockTag(OrderStockTagNameable orderStockTag) {
        this.orderStockTag = orderStockTag;
    }

    public ShopInfoNameable getShopInfoTagReceive() {
        return shopInfoTagReceive;
    }

    public void setShopInfoTagReceive(ShopInfoNameable shopInfoTagReceive) {
        this.shopInfoTagReceive = shopInfoTagReceive;
    }

    public int getLimitAutoComplete() {
        return limitAutoComplete;
    }

    public void setLimitAutoComplete(int limitAutoComplete) {
        this.limitAutoComplete = limitAutoComplete;
    }

    public String getActionCodeNote() {
        return actionCodeNote;
    }

    public void setActionCodeNote(String actionCodeNote) {
        this.actionCodeNote = actionCodeNote;
    }

    public Boolean getCanPrint() {
        return canPrint;
    }

    public void setCanPrint(Boolean canPrint) {
        this.canPrint = canPrint;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public SignOfficeTagNameable getWriteOfficeTag() {
        return writeOfficeTag;
    }

    public void setWriteOfficeTag(SignOfficeTagNameable writeOfficeTag) {
        this.writeOfficeTag = writeOfficeTag;
    }

    public Long getStockTransActionId() {
        return stockTransActionId;
    }

    public void setStockTransActionId(Long stockTransActionId) {
        this.stockTransActionId = stockTransActionId;
    }
}

