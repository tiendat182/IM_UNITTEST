package com.viettel.bccs.inventory.controller.stock;

import com.google.common.collect.Lists;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.fw.controller.TransCommonController;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.*;
import com.viettel.bccs.inventory.tag.ListProductNameable;
import com.viettel.bccs.inventory.tag.OrderStockTagNameable;
import com.viettel.bccs.inventory.tag.ShopInfoNameable;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.RequiredRoleMap;
import com.viettel.fw.dto.BaseMessage;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by hoangnt on 16/11/2015.
 */
@Component
@Scope("view")
@ManagedBean(name = "agencyImportStockController")
public class AgencyImportStockController extends TransCommonController {
    private static final String FILE_NAME_TEMPLATE = "mau_file_nhap_serial.xls";
    private Boolean infoOrderDetail = false;
    private Boolean writeOffice = false;
    private String nameMethod = "receiveWriteOffice";
    private int limitAutoComplete;
    private String actionCodeNote;
    private VStockTransDTO forSearch;
    private RequiredRoleMap requiredRoleMap;

    private List<OptionSetValueDTO> optionSetValueDTOsList;
    private List<VStockTransDTO> vStockTransDTOList;
    private ReceiptExpenseDTO receiptExpenseDTO;
    private Long stockTransActionId;
    private Long shopId;
    private List<String> lstDepositStatus;
    @Autowired
    private ListProductNameable listProductTag;
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
    private StaffDTO staffDTO;
    private Boolean doDelete;
    private Boolean canPrint;
    private boolean haveDepositPrice;


    @Secured("@")
    @PostConstruct
    public void init() {
        try {
            listProductTag.setAddNewProduct(false);
            staffDTO = BccsLoginSuccessHandler.getStaffDTO();
            lstDepositStatus = new ArrayList<>();
            lstDepositStatus.add(Const.STOCK_STRANS_DEPOSIT.DEPOSIT_RETRIEVE_EXPENSE);
            lstDepositStatus.add(Const.STOCK_STRANS_DEPOSIT.DEPOSIT_IMPORT);
            String shopId = DataUtil.safeToString(BccsLoginSuccessHandler.getStaffDTO().getShopId());
            shopInfoTagReceive.initAgent(this, shopId);
            doResetStockTrans();
            optionSetValueDTOsList = optionSetValueService.getByOptionSetCode(Const.OPTION_SET.AGENT_RETRIEVE_EXPENSE_STATUS);
            limitAutoComplete = DataUtil.safeToInt(optionSetValueService.getValueByNameFromOptionSetValue(Const.OPTION_SET.LIMIT_AUTOCOMPLETE));
        } catch (Exception e) {
            logger.error(e);
            reportError("", "common.error.happened", e);
        }
    }

    @Secured("@")
    public void doSearchStockTrans() {
        try {
//            forSearch.setPrintable(true);
            if (!DataUtil.isNullOrEmpty(forSearch.getActionCode())) {
                forSearch.setActionCode(forSearch.getActionCode().trim());
            }
            vStockTransDTOList = stockTransService.searchVStockTrans(forSearch);
//
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex.getErrorCode(), ex.getDescription());
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
        forSearch.setToOwnerName(BccsLoginSuccessHandler.getStaffDTO().getShopName());
        forSearch.setToOwnerID(BccsLoginSuccessHandler.getStaffDTO().getShopId());
        forSearch.setStockTransType(Const.STOCK_TRANS.STOCK_TRANS_TYPE_RETRIEVE_DEPOSIT);
        forSearch.setLstDepositStatus(lstDepositStatus);
        forSearch.setActionType(Const.STOCK_TRANS_STATUS.IMPORT_NOTE);
        forSearch.setStockTransStatus(Const.STOCK_TRANS_STATUS.IMPORT_NOTE);
        forSearch.setUserShopId(BccsLoginSuccessHandler.getStaffDTO().getShopId());
        forSearch.setVtUnit(Const.VT_UNIT.AGENT);
        forSearch.setObjectType(Const.OWNER_TYPE.SHOP);
        infoOrderDetail = false;
    }

    @Secured("@")
    public void doShowInfoOrderDetail(Long stockTransActionId) {
        try {
            canPrint = false;
            doDelete = false;
            haveDepositPrice = false;
            orderStockTag.loadOrderStock(stockTransActionId, true);
            shopId = DataUtil.safeToLong(orderStockTag.getShopInfoReceiveTag().getvShopStaffDTO().getOwnerId());
//            Long shopImp = DataUtil.safeToLong(orderStockTag.getShopInfoExportTag().getvShopStaffDTO().getOwnerId());
//            ShopDTO shopImpDTO = shopService.findOne(shopImp);
            ConfigListProductTagDTO configListProductTagDTO = new ConfigListProductTagDTO(Const.MODE_SERIAL.MODE_ADD_SERIAL);
//            configListProductTagDTO.setRetrieveExpense(true);
//            configListProductTagDTO.setBranchId(branchId);
//            configListProductTagDTO.setChannelTypeId(channelTypeId);
//            configListProductTagDTO.setPricePolicyId(pricePolicy);
            listProductTag.load(this, stockTransActionId, configListProductTagDTO);
//            if (!DataUtil.isNullObject(listProductTag.getLsStockTransFull())) {
//                for (StockTransFullDTO stockTransFullDTO : listProductTag.getLsStockTransFull()) {
//                    if (DataUtil.isNullObject(stockTransFullDTO.getDepositPrice())) {
//                        haveDepositPrice = true;
//                        break;
//                    }
//                }
//            }
            infoOrderDetail = true;
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
            logger.error(ex.getMessage(), ex);
            topPage();
        }
    }

    @Secured("@")
    public void doCancel(Long stockTransActionId, Long actionId) {
        try {
            this.stockTransActionId = actionId;
            canPrint = false;
            doDelete = true;
            orderStockTag.loadOrderStock(stockTransActionId, true);
            ConfigListProductTagDTO configListProductTagDTO = new ConfigListProductTagDTO(Const.MODE_SERIAL.MODE_NO_SERIAL);
            listProductTag.load(this, stockTransActionId, configListProductTagDTO);
            infoOrderDetail = true;
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
            logger.error(ex.getMessage(), ex);
            topPage();
        }
    }

    @Secured("@")
    public void doDeleteOrder(VStockTransDTO stockTransDTO) {
        try {
            doDelete = true;
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
            logger.error(ex.getMessage(), ex);
            topPage();
        }
    }

    @Secured("@")
    public void doBackPage() {
        infoOrderDetail = false;
        haveDepositPrice = false;
        orderStockTag.resetOrderStock();
        doSearchStockTrans();
    }


    @Secured("@")
    public void doReceiveShop(VShopStaffDTO vShopStaffDTO) {
        if (vShopStaffDTO != null) {
            forSearch.setFromOwnerID(DataUtil.safeToLong(vShopStaffDTO.getOwnerId()));
        }
    }

    @Secured("@")
    public void clearCurrentShop(String nameMethod) {
        forSearch.setFromOwnerID(null);
    }


    @Secured("@")
    public void doValidateCreateStock() {
        List<StockTransDetailDTO> lsDetailDTOs = listProductTag.getListTransDetailDTOs();
        for (StockTransDetailDTO stockTransDetailDTO : lsDetailDTOs) {
            if (DataUtil.safeEqual(stockTransDetailDTO.getCheckSerial(), Const.PRODUCT_OFFERING._CHECK_SERIAL)
                    && DataUtil.isNullOrEmpty(stockTransDetailDTO.getLstSerial())) {
                reportError("", ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "mn.stock.confirm.serial.empty", stockTransDetailDTO.getProdOfferName());
                break;
            }
        }
    }


    @Secured("@")
    public void doImportStock() {
        try {
            //validate ngay nhap phai la dinh dang so, va phai la so nguyen duong
            List<StockTransDetailDTO> lsDetailDTOs = Lists.newArrayList();
            List<StockTransFullDTO> lsStockTransFull = listProductTag.getLsStockTransFull();
            for (StockTransFullDTO stockTransFullDTO : lsStockTransFull) {
                StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
                stockTransDetailDTO.setStockTransDetailId(stockTransFullDTO.getStockTransDetailId());
                stockTransDetailDTO.setProdOfferId(stockTransFullDTO.getProdOfferId());
                stockTransDetailDTO.setStateId(stockTransFullDTO.getStateId());
                stockTransDetailDTO.setQuantity(stockTransFullDTO.getQuantity());
                stockTransDetailDTO.setLstStockTransSerial(stockTransFullDTO.getLstSerial());
                stockTransDetailDTO.setTableName(stockTransFullDTO.getTableName());
                stockTransDetailDTO.setProdOfferTypeId(stockTransFullDTO.getProductOfferTypeId());
                stockTransDetailDTO.setDepositPrice(stockTransFullDTO.getDepositPrice());
                lsDetailDTOs.add(stockTransDetailDTO);
            }
            StockTransActionDTO stockTransActionDTO = orderStockTag.getStockTransActionDTO();
            StockTransDTO stockTransDTO = orderStockTag.getStockTransDTO();
            stockTransDTO.setCreateUserIpAdress(BccsLoginSuccessHandler.getIpAddress());
            BaseMessage message = executeStockTransService.executeStockTrans(Const.STOCK_TRANS.NOTE_AGENT_RETRIEVE, Const.STOCK_TRANS_TYPE.IMPORT, stockTransDTO, stockTransActionDTO, lsDetailDTOs, requiredRoleMap);
            if (!DataUtil.isNullOrEmpty(message.getErrorCode())) {
                throw new LogicException(message.getErrorCode(), message.getKeyMsg(), message.getParamsMsg());
            }
            canPrint = true;
            reportSuccess("", "mn.stock.agency.retrieve.import.report.success");
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex.getErrorCode(), ex.getDescription());
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "common.error.happened", ex);
            topPage();
        }
    }

    @Secured("@")
    public void doValidateDelete() {
        try {
            StockTransDTO stockTransDTO = stockTransService.findOne(stockTransActionId);
            if (DataUtil.isNullObject(stockTransDTO)) {
                throw new LogicException("", "mn.stock.agency.retrieve.expense.not.exsits");
            }
            if (stockTransService.checkStatusRetrieveExpense(stockTransActionId, Const.STOCK_TRANS_STATUS.IMPORT_NOTE, null)) {
                throw new LogicException("", "mn.stock.agency.retrieve.expense.not.status");
            }
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex.getErrorCode(), ex.getDescription());
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "common.error.happened", ex);
            topPage();
        }
    }

    @Secured("@")
    public void doDeleteNote() {
        try {
            List<StockTransDetailDTO> lsDetailDTOs = Lists.newArrayList();
            List<StockTransFullDTO> lsStockTransFull = listProductTag.getLsStockTransFull();
            for (StockTransFullDTO stockTransFullDTO : lsStockTransFull) {
                StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
                stockTransDetailDTO.setStockTransDetailId(stockTransFullDTO.getStockTransDetailId());
                stockTransDetailDTO.setProdOfferId(stockTransFullDTO.getProdOfferId());
                stockTransDetailDTO.setStateId(stockTransFullDTO.getStateId());
                stockTransDetailDTO.setQuantity(stockTransFullDTO.getQuantity());
                stockTransDetailDTO.setLstStockTransSerial(stockTransFullDTO.getLstSerial());
                stockTransDetailDTO.setTableName(stockTransFullDTO.getTableName());
                stockTransDetailDTO.setProdOfferTypeId(stockTransFullDTO.getProductOfferTypeId());

                lsDetailDTOs.add(stockTransDetailDTO);
            }
            StockTransActionDTO stockTransActionDTO = orderStockTag.getStockTransActionDTO();
            StockTransDTO stockTransDTO = orderStockTag.getStockTransDTO();
            BaseMessage message = executeStockTransService.executeStockTrans(Const.STOCK_TRANS.NOTE_AGENT_RETRIEVE, Const.STOCK_STRANS_DEPOSIT.DEPOSIT_IMPORT, stockTransDTO, stockTransActionDTO, lsDetailDTOs, requiredRoleMap);
            if (!DataUtil.isNullOrEmpty(message.getErrorCode())) {
                throw new LogicException(message.getErrorCode(), message.getKeyMsg(), message.getParamsMsg());
            }
            canPrint = true;
            reportSuccess("", "mn.stock.agency.retrieve.import.cancel.success");
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex.getErrorCode(), ex.getDescription());
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
                List<StockTransDetailDTO> stockTransDetailDTOs = listProductTag.getListTransDetailDTOs();
                stockTransDTO.setActionType(Const.STOCK_TRANS_ACTION_TYPE.NOTE);
                stockTransDTO.setStockTransStatus(Const.STOCK_TRANS_TYPE.IMPORT);
                return exportStockTransDetail(stockTransDTO, stockTransDetailDTOs);
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

    // Ham in lenh xuat excel
    public StreamedContent exportBBBG() {
        try {
            StockTransDTO stockTransDTO = orderStockTag.getStockTransDTOForPint();
            StreamedContent content = exportHandOverReport(stockTransDTO);
            return content;
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
        return null;
    }

    //getter and setter

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

    public boolean isHaveDepositPrice() {
        return haveDepositPrice;
    }

    public void setHaveDepositPrice(boolean haveDepositPrice) {
        this.haveDepositPrice = haveDepositPrice;
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

    public ReceiptExpenseDTO getReceiptExpenseDTO() {
        return receiptExpenseDTO;
    }

    public void setReceiptExpenseDTO(ReceiptExpenseDTO receiptExpenseDTO) {
        this.receiptExpenseDTO = receiptExpenseDTO;
    }

    public Long getStockTransActionId() {
        return stockTransActionId;
    }

    public void setStockTransActionId(Long stockTransActionId) {
        this.stockTransActionId = stockTransActionId;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public List<String> getLstDepositStatus() {
        return lstDepositStatus;
    }

    public void setLstDepositStatus(List<String> lstDepositStatus) {
        this.lstDepositStatus = lstDepositStatus;
    }

    public Boolean getDoDelete() {
        return doDelete;
    }

    public void setDoDelete(Boolean doDelete) {
        this.doDelete = doDelete;
    }

    public ListProductNameable getListProductTag() {
        return listProductTag;
    }

    public void setListProductTag(ListProductNameable listProductTag) {
        this.listProductTag = listProductTag;
    }

    public Boolean getCanPrint() {
        return canPrint;
    }

    public void setCanPrint(Boolean canPrint) {
        this.canPrint = canPrint;
    }
}
