package com.viettel.bccs.inventory.controller.isdn;

import com.google.common.collect.Lists;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.fw.controller.TransCommonController;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.common.model.FileExportBean;
import com.viettel.bccs.inventory.common.util.FileUtil;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.*;
import com.viettel.bccs.inventory.tag.ListProductNameable;
import com.viettel.bccs.inventory.tag.OrderStockTagNameable;
import com.viettel.bccs.inventory.tag.ShopInfoNameable;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.BundleUtil;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DateUtil;
import com.viettel.fw.common.util.RequiredRoleMap;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.web.common.security.CustomAuthenticationProvider;
import org.apache.poi.ss.usermodel.Workbook;
import org.primefaces.context.RequestContext;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.util.Date;
import java.util.List;

/**
 * Created by tuyendv8 on 11/5/2015.
 */
@Component
@Scope("view")
@ManagedBean
public class TransferIsdnController extends TransCommonController {

    @Autowired
    private ShopInfoNameable shopReceiveInfoTag;
    //    @Autowired
//    private ShopInfoTag shopDeliverInfoTag;
    @Autowired
    private ShopInfoNameable orderShopReceiveInfoTag;
    @Autowired
    private ShopInfoNameable shopDeliverInfoTag;
    @Autowired
    private StockTransService stockTransService;
    @Autowired
    private StockTransActionService stockTransActionService;
    @Autowired
    private OrderStockTagNameable orderStockTag;
    @Autowired
    private ReasonService reasonService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private StockTransVofficeService stockTransVofficeService;
    @Autowired
    private ListProductNameable listProductTag;//khai bao tag danh sach hang hoa
    @Autowired
    private ExecuteStockTransService executeStockTransService;
    @Autowired
    OptionSetValueService optionSetValueService;
    @Autowired
    StockTransSerialService stockTransSerialService;
    @Autowired
    StockNumberService stockNumberService;

    private boolean infoOrderDetail;
    private VStockTransDTO infoSearchDto = new VStockTransDTO();
    private List<VStockTransDTO> listTrans = Lists.newArrayList();
    private VStockTransDTO currentStockTransDelivery;
    private List<OptionSetValueDTO> listTransStatus = Lists.newArrayList();
    private RequiredRoleMap requiredRoleMap;
    private boolean showPrint = false;
    private StaffDTO currentStaffDTO;

    @Secured("@")
    @PostConstruct
    public void init() {
        try {
//        shopDeliverInfoTag.initShop(this, currentStaffDTO.getShopId().toString(), true);
            currentStaffDTO = BccsLoginSuccessHandler.getStaffDTO();
            listTransStatus = optionSetValueService.getStatusOptionSetValueByStockState(Const.OPTION_SET.STOCK_TRANS_STATUS, Lists.newArrayList(Const.STOCK_TRANS_STATUS.EXPORT_NOTE, Const.STOCK_TRANS_STATUS.IMPORTED, Const.STOCK_TRANS_STATUS.DESTROYED));
            shopReceiveInfoTag.initShopForIsdn(this, currentStaffDTO.getShopId().toString(), true, null);
            shopDeliverInfoTag.initShopForIsdn(this, currentStaffDTO.getShopId().toString(), true, null);
//            shopDeliverInfoTag.loadShop(currentStaffDTO.getShopId().toString(), false);
            infoOrderDetail = false;
            orderStockTag.init(this, false);
            infoSearchDto.setStartDate(new Date());
            infoSearchDto.setStockTransStatus(Const.STOCK_TRANS_STATUS.EXPORT_NOTE);
            infoSearchDto.setEndDate(new Date());
//            infoSearchDto.setFromOwnerID(currentStaffDTO.getShopId());
            infoSearchDto.setFromOwnerType(DataUtil.safeToLong(VShopStaffDTO.TYPE_SHOP));
            infoSearchDto.setToOwnerType(DataUtil.safeToLong(VShopStaffDTO.TYPE_SHOP));
            listProductTag.setAddNewProduct(false);
            requiredRoleMap = CustomAuthenticationProvider.createRequiredRoleMap(Const.PERMISION.ROLE_TRANSPORT_STOCK, Const.PERMISION.ROLE_STOCK_NUM_SHOP);
            RequestContext.getCurrentInstance().update("frmExportIsdn");
            search(false);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportErrorValidateFail("frmExportIsdn:messages", "", "common.error.happen");
            topPage();
        }
    }

    public void resetFrom() {
        infoSearchDto.setActionCode(null);
        infoSearchDto.setStockTransStatus(null);
        infoSearchDto.setStockTransStatus(Const.STOCK_TRANS_STATUS.EXPORT_NOTE);
        shopDeliverInfoTag.resetShop();
//        shopDeliverInfoTag.loadShop(DataUtil.safeToString(currentStaffDTO.getShopId()), false);
        shopReceiveInfoTag.resetShop();
        infoSearchDto.setStartDate(new Date());
        infoSearchDto.setEndDate(new Date());
    }

    @Secured("@")
    public void backView() {
        infoOrderDetail = false;
    }

    public boolean validate() {
        StockTransActionDTO stockTransActionDTO = null;
        try {
            stockTransActionDTO = stockTransActionService.findOne(currentStockTransDelivery.getActionID());
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        StockTransDTO stockTransDTO = null;
        try {
            stockTransDTO = stockTransService.findOne(currentStockTransDelivery.getStockTransID());
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        try {
            if (!DataUtil.isNullObject(stockTransActionDTO.getSignCaStatus()) && stockTransActionDTO.getSignCaStatus().equals(Const.SIGN_VOFFICE)) {
                stockTransVofficeService.doSignedVofficeValidate(stockTransActionDTO);
            }
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("frmExportIsdn:messageRange", "", BundleUtil.getText(ex.getKeyMsg()));
            topPage();
            return false;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("frmExportIsdn:messageRange", "", "transfer.isdn.check.status.voffice");
            topPage();
            return false;
        }
        ShopDTO shop = null;
        try {
            if (!DataUtil.isNullObject(stockTransDTO)) {
                shop = shopService.findOne(stockTransDTO.getFromOwnerId());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        if (DataUtil.isNullObject(shop) || DataUtil.isNullObject(shop.getChannelTypeId())) {
            reportError("frmExportIsdn:messageRange", "", "transfer.isdn.deliver.stock.chanel.null");
            topPage();
            return false;
        }
        if (DataUtil.isNullObject(stockTransDTO.getStatus()) || !stockTransDTO.getStatus().equals(Const.STOCK_TRANS_STATUS.EXPORT_NOTE)) {
            reportError("frmExportIsdn:messageRange", "", "transfer.isdn.status.invalid");
            topPage();
            return false;
        }
        List<StockTransFullDTO> lstStockTransFull = null;
        try {
            lstStockTransFull = stockTransService.searchStockTransDetail(Lists.newArrayList(stockTransActionDTO.getStockTransActionId()));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        if (DataUtil.isNullOrEmpty(lstStockTransFull)) {
            reportError("frmExportIsdn:messageRange", "", "transfer.isdn.product.null");
            topPage();
            return false;
        }
        for (StockTransFullDTO product : lstStockTransFull) {
            if (product.getCheckSerial().equals(Const.PRODUCT_OFFERING.CHECK_SERIAL.toString())) {
                List<StockTransSerialDTO> listSeri = null;
                try {
                    listSeri = stockTransSerialService.findByStockTransDetailId(product.getStockTransDetailId());
                    for (StockTransSerialDTO seri : listSeri) {
                        List<StockTransSerialDTO> lstCheckSeri = stockNumberService.findRangeForExportNote(seri.getFromSerial(), seri.getToSerial(), stockTransDTO.getFromOwnerType().toString(), stockTransDTO.getFromOwnerId(), product.getProdOfferId());
                        if (DataUtil.isNullOrEmpty(lstCheckSeri) || lstCheckSeri.size() != 1
                                || !(lstCheckSeri.get(0).getFromSerial().equals(seri.getFromSerial()) && lstCheckSeri.get(0).getToSerial().equals(seri.getToSerial()))) {
                            reportError("frmExportIsdn:messageRange", "", "transfer.isdn.isdn.invalid", seri.getFromSerial(), seri.getToSerial());
                            topPage();
                            return false;
                        }
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
                if (DataUtil.isNullOrEmpty(listSeri)) {
                    reportError("frmExportIsdn:messageRange", "", "transfer.isdn.product.seri.null", product.getProdOfferName());
                    topPage();
                    return false;
                }
            }
        }
        return true;
    }


    public void deliverStock() {
        try {
            showPrint = false;
            if (validate()) {
                StockTransActionDTO stockTransActionDTO = orderStockTag.getStockTransActionDTO();
                stockTransActionDTO.setActionCode(null);
                stockTransActionDTO.setNote(null);
                stockTransActionDTO.setCreateUser(currentStaffDTO.getStaffCode());
                stockTransActionDTO.setActionStaffId(currentStaffDTO.getStaffId());
                stockTransActionDTO.setSignCaStatus(null);
                StockTransDTO stockTransDTO = orderStockTag.getStockTransDTO();
                ShopDTO fromShop = shopService.findOne(stockTransDTO.getFromOwnerId());
                stockTransDTO.setFromOwnerCode(fromShop.getShopCode());
                stockTransDTO.setFromOwnerName(fromShop.getName());
                ShopDTO toShop = shopService.findOne(stockTransDTO.getToOwnerId());
                stockTransDTO.setToOwnerCode(toShop.getShopCode());
                stockTransDTO.setToOwnerName(toShop.getName());
                ReasonDTO reason = reasonService.findOne(stockTransDTO.getReasonId());
                stockTransDTO.setReasonName(reason.getReasonName());
                stockTransDTO.setReasonCode(reason.getReasonCode());
                stockTransDTO.setUserCreate(currentStaffDTO.getStaffCode());
                stockTransDTO.setCreateUserIpAdress(BccsLoginSuccessHandler.getIpAddress());
                stockTransDTO.setStaffId(currentStaffDTO.getStaffId());
                stockTransDTO.setUserName(currentStaffDTO.getName());
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
                BaseMessage message = executeStockTransService.executeStockTrans(Const.STOCK_TRANS.EXPORT, Const.STOCK_TRANS_TYPE.ISDN, stockTransDTO, stockTransActionDTO, lsDetailDTOs, requiredRoleMap);
                if (!message.isSuccess() && !DataUtil.isNullObject(message.getKeyMsg())) {
                    reportErrorValidateFail("frmExportIsdn:messages", "", message.getKeyMsg(), message.getParamsMsg());
                    topPage();
                    return;
                }
//                search(false);
                showPrint = true;
                currentStockTransDelivery.setStockTransStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
                for (OptionSetValueDTO optionValue : listTransStatus) {
                    if (DataUtil.safeEqual(optionValue.getValue(), Const.STOCK_TRANS_STATUS.IMPORTED)) {
                        currentStockTransDelivery.setStatusName(optionValue.getName());
                    }
                }
                reportSuccess("", "export.stock.success");
                topPage();
            }
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex);
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportErrorValidateFail("frmExportIsdn:messages", "", "common.error.happen");
            topPage();
        }
    }

    @Secured("@")
    public void validateCancel(VStockTransDTO dto) {
        currentStockTransDelivery = dto;
    }

    @Secured("@")
    public void cancelTransaction() {
        try {
//            if (validate()) {
            showPrint = false;
            System.out.println("deliver");

            StockTransActionDTO stockTransActionDTO = stockTransActionService.findOne(currentStockTransDelivery.getActionID());
            stockTransActionDTO.setActionCode(null);
            stockTransActionDTO.setNote(null);
            stockTransActionDTO.setCreateUser(currentStaffDTO.getStaffCode());
            stockTransActionDTO.setActionStaffId(currentStaffDTO.getStaffId());
            stockTransActionDTO.setSignCaStatus(null);

            StockTransDTO stockTransDTO = stockTransService.findOne(currentStockTransDelivery.getStockTransID());

            List<StockTransFullDTO> lsStockTransFull = stockTransService.searchStockTransDetail(Lists.newArrayList(currentStockTransDelivery.getActionID()));

            List<StockTransDetailDTO> lsDetailDTOs = Lists.newArrayList();
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

            BaseMessage message = executeStockTransService.executeStockTrans(Const.STOCK_TRANS.CANCEL_TRANS, Const.STOCK_TRANS_TYPE.ISDN, stockTransDTO, stockTransActionDTO, lsDetailDTOs, requiredRoleMap);

            if (!DataUtil.isNullOrEmpty(message.getErrorCode())) {
                throw new LogicException(message.getErrorCode(), message.getKeyMsg());
            }

            currentStockTransDelivery.setStockTransStatus(Const.STOCK_TRANS_STATUS.CANCEL);
            for (OptionSetValueDTO optionValue : listTransStatus) {
                if (DataUtil.safeEqual(optionValue.getValue(), Const.STOCK_TRANS_STATUS.CANCEL)) {
                    currentStockTransDelivery.setStatusName(optionValue.getName());
                }
            }
            currentStockTransDelivery.setStockTransStatus(Const.STOCK_TRANS_STATUS.CANCEL);
            reportSuccess("", "transfer.isdn.cancel.transaction.success");
            topPage();
//            }
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex);
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportErrorValidateFail("frmExportIsdn:messages", "", "common.error.happen");
            topPage();
        }
    }

    private boolean validateSearch() {
        if (DataUtil.isNullObject(infoSearchDto.getStartDate())) {
            reportError("frmExportIsdn:messageRange", "", "mn.stock.from.date.not.blank");
            focusElementError("fromDate");
            topPage();
            return false;
        }
        if (DataUtil.isNullObject(infoSearchDto.getEndDate())) {
            reportError("frmExportIsdn:messageRange", "", "mn.stock.to.date.not.blank");
            focusElementError("toDate");
            topPage();
            return false;
        }
        if (DateUtil.compareDateToDate(infoSearchDto.getStartDate(), infoSearchDto.getEndDate()) > 0) {
            reportError("frmExportIsdn:messageRange", "", "stock.trans.from.to.valid");
            focusElementError("toDate");
            topPage();
            return false;
        }
        try {
            if (DateUtil.daysBetween2Dates(infoSearchDto.getEndDate(), infoSearchDto.getStartDate()) > 30) {
                reportError("frmExportIsdn:messageRange", "", "stock.trans.from.to.valid");
                focusElementError("toDate");
                topPage();
                return false;
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            return false;
        }
        return true;
    }

    @Secured("@")
    public void search(boolean showMsg) {
        try {
            if (validateSearch()) {
                infoOrderDetail = false;
                infoSearchDto.setToOwnerType(Const.OWNER_TYPE.SHOP_LONG);
                infoSearchDto.setLstStatus(Lists.newArrayList("2", "6", "7"));
                infoSearchDto.setVtUnit(null);
//                infoSearchDto.setActionType(null);
                infoSearchDto.setObjectType(null);
                infoSearchDto.setUserShopId(null);
                infoSearchDto.setStockTransType(DataUtil.safeToLong(Const.STOCK_TRANS_TYPE.ISDN));
                infoSearchDto.setCurrentUserShopId(currentStaffDTO.getShopId());
                infoSearchDto.setCurrentUserShopPath(currentStaffDTO.getShopPath());
                infoSearchDto.setActionType(Const.STOCK_TRANS_STATUS.EXPORT_NOTE);
                listTrans = Lists.newArrayList();
                listTrans = stockTransService.searchVStockTrans(infoSearchDto);
//                if (!DataUtil.isNullOrEmpty(lstTemp)) {
//                    for (VStockTransDTO stockTrans : lstTemp) {
//                        if (stockTrans.getStockTransStatus().equals(stockTrans.getActionType())) {
//                            listTrans.add(stockTrans);
//                        }
//                    }
//                }
                showPrint = false;
//                if (showMsg) {
//                    reportSuccess("frmManaTransDigital:messages", "mn.invoice.invoiceType.searchData", DataUtil.isNullOrEmpty(listTrans) ? 0 : listTrans.size());
//                }
            }

        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex);
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportErrorValidateFail("frmExportIsdn:messages", "", "common.error.happen");
            topPage();
        }
    }

    @Secured("@")
    public void previewDeliverStockTrans(VStockTransDTO dto) {
        try {
            currentStockTransDelivery = dto;
//            if (validate()) {
            infoOrderDetail = true;
            orderStockTag.setLsReasonDto(reasonService.findAll());
            orderStockTag.loadOrderStock(dto.getActionID(), true);
            ConfigListProductTagDTO configListProductTagDTO = new ConfigListProductTagDTO(Const.MODE_SERIAL.MODE_VIEW, "", "");
            configListProductTagDTO.setFillAllSerial(true);
            listProductTag.setShowSerial(true);
            listProductTag.load(this, dto.getActionID(), configListProductTagDTO);
//            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportErrorValidateFail("frmExportIsdn:messages", "", "common.error.happen");
            topPage();
        }
    }

    @Secured("@")
    public void printField() {
        try {
            //xuat file
            StockTransDTO stockTransDTO = orderStockTag.getStockTransDTO();
            StockTransActionDTO actionDto = orderStockTag.getStockTransActionDTO();
            ShopDTO fromShop = shopService.findOne(stockTransDTO.getFromOwnerId());
            ShopDTO toShop = shopService.findOne(stockTransDTO.getToOwnerId());
            ReasonDTO reason = reasonService.findOne(stockTransDTO.getReasonId());
            List<StockTransDetailDTO> stockTransDetailDTOs = listProductTag.getListTransDetailDTOs();
            FileExportBean bean = new FileExportBean();
            bean.setTempalatePath(FileUtil.getTemplatePath());
            bean.setTemplateName(getExportDetailTemplate(stockTransDTO));
            bean.putValue("note", stockTransDTO.getNote());
            bean.putValue("actionCode", actionDto.getActionCode());
            bean.putValue("fromOwnerAddress", fromShop.getAddress());
            bean.putValue("fromOwnerName", fromShop.getShopCode() + "-" + fromShop.getName());
            bean.putValue("toOwnerAddress", toShop.getAddress());
            bean.putValue("toOwnerName", toShop.getShopCode() + "-" + toShop.getName());
            bean.putValue("lstStockTransFull", stockTransDetailDTOs);
            bean.putValue("createDate", DateUtil.date2ddMMyyyyString(stockTransDTO.getCreateDatetime()));
            bean.putValue("reasonName", reason.getReasonName());
            Workbook book = FileUtil.exportWorkBook(bean);
            //down file
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"" + "EstablishExportIsdn.xls" + "\"");
            externalContext.setResponseContentType("application/vnd.ms-excel");
            book.write(externalContext.getResponseOutputStream());
            facesContext.responseComplete();
            return;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "common.error.happened", ex);
            topPage();
        }
    }

    public StreamedContent exportHandOverReport() {
        try {
            //xuat file
            StockTransDTO stockTransDTO = orderStockTag.getStockTransDTOForPint();
            StockTransActionDTO action = new StockTransActionDTO();
            action.setStockTransId(stockTransDTO.getStockTransId());
            action.setStatus(Const.STOCK_TRANS_STATUS.EXPORT_NOTE);
            StockTransActionDTO stockTransActionDTO = stockTransActionService.findStockTransActionDTO(action);
            stockTransDTO.setStockTransActionId(stockTransActionDTO.getStockTransActionId());
            stockTransDTO.setActionCode(stockTransActionDTO.getActionCode());
            return exportHandOverReport(stockTransDTO);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "common.error.happened", ex);
            topPage();
        }
        return null;
    }

    @Secured("@")
    public StreamedContent doPrintStockTransDetail() {
        try {
            StockTransDTO stockTransDTO = orderStockTag.getStockTransDTOForPint();
            stockTransDTO.setActionType(Const.STOCK_TRANS_ACTION_TYPE.NOTE);
            stockTransDTO.setStockTransStatus(Const.STOCK_TRANS_TYPE.IMPORT);
            stockTransDTO.setStockTransActionId(null);
            stockTransDTO.setStockTransType(Const.STOCK_TRANS_TYPE.ISDN);
            return exportStockTransDetail(stockTransDTO, listProductTag.getLsStockTransFull());
        } catch (LogicException ex) {
            topReportError("", ex);
            topPage();
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
            topPage();
        }
        return null;
    }

    public void printBBBG() {
        try {
            //xuat file
            StockTransDTO stockTransDTO = orderStockTag.getStockTransDTO();
            StockTransActionDTO actionDto = orderStockTag.getStockTransActionDTO();
            ShopDTO fromShop = shopService.findOne(stockTransDTO.getFromOwnerId());
            ShopDTO toShop = shopService.findOne(stockTransDTO.getToOwnerId());
            ReasonDTO reason = reasonService.findOne(stockTransDTO.getReasonId());
            List<StockTransFullDTO> listStockTransFull = listProductTag.getLsStockTransFull();
            FileExportBean bean = new FileExportBean();
            bean.setTempalatePath(FileUtil.getTemplatePath());
            bean.setTemplateName(getHandOverReportTemplateCommon(stockTransDTO, "XY"));
            bean.putValue("note", stockTransDTO.getNote());
            bean.putValue("actionCode", actionDto.getActionCode());
            bean.putValue("fromOwnerAddress", fromShop.getAddress());
            bean.putValue("fromOwnerName", fromShop.getShopCode() + "-" + fromShop.getName());
            bean.putValue("toOwnerAddress", toShop.getAddress());
            bean.putValue("toOwnerName", toShop.getShopCode() + "-" + toShop.getName());
            bean.putValue("lstStockTransFull", listStockTransFull);
            bean.putValue("createDate", DateUtil.date2ddMMyyyyString(stockTransDTO.getCreateDatetime()));
            bean.putValue("reasonNameBBBG", reason.getReasonName());
            Workbook book = FileUtil.exportWorkBook(bean);
            //down file
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"" + "EstablishExportIsdn.xls" + "\"");
            externalContext.setResponseContentType("application/vnd.ms-excel");
            book.write(externalContext.getResponseOutputStream());
            facesContext.responseComplete();
            return;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "common.error.happened", ex);
            topPage();
        }
    }

    @Secured("@")
    public void takeStock(VShopStaffDTO dto) {
        setShopInfoExport(dto.getOwnerName(), dto.getOwnerCode(), DataUtil.safeToLong(dto.getOwnerId()));
    }

    private void setShopInfoExport(String ownerName, String ownerCode, Long ownerId) {
        infoSearchDto.setFromOwnerID(ownerId);
        infoSearchDto.setFromOwnerName(ownerCode + "-" + ownerName);
        infoSearchDto.setFromOwnerType(DataUtil.safeToLong(VShopStaffDTO.TYPE_SHOP));
    }

    @Secured("@")
    public void clearDeliverStock() {
        infoSearchDto.setFromOwnerID(null);
        infoSearchDto.setFromOwnerName(null);
        infoSearchDto.setFromOwnerType(null);
    }

    @Secured("@")
    public void receiveStock(VShopStaffDTO dto) {
        infoSearchDto.setToOwnerID(DataUtil.safeToLong(dto.getOwnerId()));
        infoSearchDto.setToOwnerName(dto.getOwnerCode() + "-" + dto.getOwnerName());
        infoSearchDto.setToOwnerType(DataUtil.safeToLong(VShopStaffDTO.TYPE_SHOP));
    }

    @Secured("@")
    public void clearReceiveShop() {
        infoSearchDto.setToOwnerID(null);
        infoSearchDto.setToOwnerName(null);
        infoSearchDto.setToOwnerType(null);
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

    public VStockTransDTO getInfoSearchDto() {
        return infoSearchDto;
    }

    public void setInfoSearchDto(VStockTransDTO infoSearchDto) {
        this.infoSearchDto = infoSearchDto;
    }

//    public ShopInfoTag getShopDeliverInfoTag() {
//        return shopDeliverInfoTag;
//    }

    public List<VStockTransDTO> getListTrans() {
        return listTrans;
    }

    public void setListTrans(List<VStockTransDTO> listTrans) {
        this.listTrans = listTrans;
    }

//    public void setShopDeliverInfoTag(ShopInfoTag shopDeliverInfoTag) {
//        this.shopDeliverInfoTag = shopDeliverInfoTag;
//    }

    public String getStockTransStatusName(String status) {
        if (!DataUtil.isNullObject(status)) {
            return BundleUtil.getText("stock.trans.status." + status);
        } else {
            return "";
        }

    }

    public OrderStockTagNameable getOrderStockTag() {
        return orderStockTag;
    }

    public void setOrderStockTag(OrderStockTagNameable orderStockTag) {
        this.orderStockTag = orderStockTag;
    }

    public ListProductNameable getListProductTag() {
        return listProductTag;
    }

    public List<OptionSetValueDTO> getListTransStatus() {
        return listTransStatus;
    }

    public void setListTransStatus(List<OptionSetValueDTO> listTransStatus) {
        this.listTransStatus = listTransStatus;
    }

    public void setListProductTag(ListProductNameable listProductTag) {
        this.listProductTag = listProductTag;
    }

    public boolean isShowPrint() {
        return showPrint;
    }

    public void setShowPrint(boolean showPrint) {
        this.showPrint = showPrint;
    }

    public ShopInfoNameable getShopDeliverInfoTag() {
        return shopDeliverInfoTag;
    }

    public void setShopDeliverInfoTag(ShopInfoNameable shopDeliverInfoTag) {
        this.shopDeliverInfoTag = shopDeliverInfoTag;
    }

    public ShopInfoNameable getShopReceiveInfoTag() {
        return shopReceiveInfoTag;
    }

    public void setShopReceiveInfoTag(ShopInfoNameable shopReceiveInfoTag) {
        this.shopReceiveInfoTag = shopReceiveInfoTag;
    }

    public ShopInfoNameable getOrderShopReceiveInfoTag() {
        return orderShopReceiveInfoTag;
    }

    public void setOrderShopReceiveInfoTag(ShopInfoNameable orderShopReceiveInfoTag) {
        this.orderShopReceiveInfoTag = orderShopReceiveInfoTag;
    }
}
