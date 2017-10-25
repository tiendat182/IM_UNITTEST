package com.viettel.bccs.inventory.controller.stock;

import com.google.common.collect.Lists;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.fw.controller.TransCommonController;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.model.OptionSetValue;
import com.viettel.bccs.inventory.service.*;
import com.viettel.bccs.inventory.tag.ShopInfoNameable;
import com.viettel.bccs.inventory.tag.StaffInfoNameable;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.security.e;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.util.*;
import java.util.stream.Collectors;

/**
 * anonymous
 */
@Component
@Scope("view")
@ManagedBean(name = "stockInspectController")
public class StockInspectController extends TransCommonController {
    private boolean timeCheckStock;
    private boolean showStockNoSerial;
    private boolean showCheckStock;
    private boolean showInputMethodManual;
    private boolean confirmStockInspectApprover;
    private boolean showIsFinished;
    // Quyen VTT
    private boolean permissionVTT;
    private boolean confirmInspectQuantity;
    private String typeInspec = "1"; //1 kiem ke theo serial (mac dinh), 2 kiem ke theo so luong

    private ProductOfferingTotalDTO productOfferingTotalDTODlg;
    private List<ProductOfferTypeDTO> lstProductOfferTypeDTODlg = Lists.newArrayList();
    private List<ProductOfferingTotalDTO> lstProductOfferingTotalDTODlg = Lists.newArrayList();
    private StockTransFullDTO stockTransFullDTODlg;
    private Map<String, String> mapState;

    private StockInspectDTO checkStockInspectDTO = new StockInspectDTO();
    private StockInspectRealDTO stockInspectRealDTO = new StockInspectRealDTO();
    private String productOfferTypeId;
    private String stateId;
    private ProductOfferingTotalDTO productOfferingTotalDTO = new ProductOfferingTotalDTO();
    private int limitAutoComplete;
    private VShopStaffDTO vShopStaffDTO = new VShopStaffDTO();
    private VShopStaffDTO vShopDTO = new VShopStaffDTO();
    private VShopStaffDTO vShopStaffApproverDTO = new VShopStaffDTO();
    private VShopStaffDTO vShopStaffApproverNewDTO = new VShopStaffDTO();
    private List<OptionSetValueDTO> listState = Lists.newArrayList();
    private List<ProductOfferTypeDTO> listProductOfferType = Lists.newArrayList();
    private List<ProductOfferingTotalDTO> lsProductOfferingTotalDTO = Lists.newArrayList();
    private List<StockInspectCheckDTO> listProductCheck = Lists.newArrayList();
    private List<OptionSetValueDTO> listInputMethod = Lists.newArrayList();
    private String messageConfirmStockInspect;
    private boolean inspectFinish;
    private Long inputMethodId;
    private StockInspectExportDTO stockInspectExportDTO;
    private StockInspectDTO stockInspectSaveDTO = new StockInspectDTO();
    private List<String> lstChanelTypeId = Lists.newArrayList();

    @Autowired
    private StaffInfoNameable staffInfoTag;
    @Autowired
    private StaffInfoNameable staffInfoApproverTag;
    @Autowired
    private StaffInfoNameable staffInfoApproverTagNew;
    @Autowired
    private OptionSetValueService optionSetValueService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private StaffService staffService;
    @Autowired
    private ProductOfferTypeService productOfferTypeService;
    @Autowired
    private ProductOfferingService productOfferingService;
    @Autowired
    private StockInspectService stockInspectService;
    @Autowired
    private ShopInfoNameable shopInfoTag;

    @PostConstruct
    public void init() {
        try {
            stockTransFullDTODlg = new StockTransFullDTO();
            stockTransFullDTODlg.setProductOfferTypeId(Const.STOCK_TYPE.STOCK_HANDSET);
            timeCheckStock = true;
            permissionVTT = true;
            lsProductOfferingTotalDTO = Lists.newArrayList();
            setDefaultValue();
            // Kiem tra ngay duoc phep thuc hien chuc nang

            StaffDTO staffDTO = BccsLoginSuccessHandler.getStaffDTO();
            String shopId = DataUtil.safeToString(staffDTO.getShopId());
            ShopDTO shopDTO = shopService.findOne(DataUtil.safeToLong(shopId));
            if (DataUtil.isNullObject(shopDTO) || DataUtil.isNullObject(shopDTO.getShopId()) || !DataUtil.safeEqual(shopDTO.getStatus(), Const.STATUS.ACTIVE)) {
                timeCheckStock = false;
                reportError("", "", "stock.inspect.shop.not.found", staffDTO.getShopName());
                return;
            }
            checkStockInspectDTO.setShopId(shopDTO.getShopId());
            checkStockInspectDTO.setShopName(shopDTO.getName());
            checkStockInspectDTO.setShopCode(shopDTO.getShopCode());
            checkStockInspectDTO.setCreateDate(new Date());
            checkStockInspectDTO.setOwnerId(staffDTO.getShopId());
            checkStockInspectDTO.setOwnerType(Const.OWNER_TYPE.SHOP_LONG);
            vShopDTO.setOwnerId(shopId);
            lstChanelTypeId = Lists.newArrayList();
            lstChanelTypeId.add(Const.CHANNEL_TYPE_ID.CHANNEL_TYPE_STAFF);
            lstChanelTypeId.add(Const.CHANNEL_TYPE_ID.CHANNEL_TYPE_INSURRANCE);
            shopInfoTag.initShopAndAllChild(this, staffDTO.getShopId().toString(), true, Const.OWNER_TYPE.SHOP);
            shopInfoTag.loadShop(staffDTO.getShopId().toString(), false);
            staffInfoTag.initStaffWithChanelTypes(this, shopId, null, lstChanelTypeId, false);
            //neu la kiem ke theo serial set mac dinh thang nhan vien
            if ("1".equals(typeInspec)) {
                vShopStaffDTO = staffService.findStaffById(DataUtil.safeToString(staffDTO.getStaffId()));
                staffInfoTag.loadStaff(vShopStaffDTO);
            }

            if (!DataUtil.isNullObject(vShopStaffDTO) && !DataUtil.isNullOrEmpty(vShopStaffDTO.getOwnerId()) && DataUtil.isNumber(vShopStaffDTO.getOwnerId())) {
                checkStockInspectDTO.setOwnerId(DataUtil.safeToLong(vShopStaffDTO.getOwnerId()));
                checkStockInspectDTO.setOwnerType(Const.OWNER_TYPE.STAFF_LONG);
            }
            limitAutoComplete = DataUtil.safeToInt(optionSetValueService.getValueByNameFromOptionSetValue(Const.OPTION_SET.LIMIT_AUTOCOMPLETE));
            listState = optionSetValueService.getByOptionSetCode(Const.STOCK_INSPECT.STATE);
            listProductOfferType = productOfferTypeService.getListProduct();
            // Khoi tao hinh thuc nhap.
            listInputMethod = optionSetValueService.getByOptionSetCode(Const.STOCK_INSPECT.INPUT_METHOD);

            lstProductOfferingTotalDTODlg = productOfferingService.getListProductOfferingByProductType("", stockTransFullDTODlg.getProductOfferTypeId());
            lstProductOfferTypeDTODlg = DataUtil.cloneBean(listProductOfferType);
            List<OptionSetValueDTO> lsOptionState = optionSetValueService.getByOptionSetCode(Const.OPTION_SET.GOODS_STATE);
            mapState = lsOptionState.stream().collect(Collectors.toMap(OptionSetValueDTO::getValue, OptionSetValueDTO::getName));
            executeCommand("renderAll");
        } catch (LogicException ex) {
            reportError(ex.getMessage(), ex);
            topPage();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            reportError("", "common.error.happened", e);
        }
    }

    private void setDefaultValue() {
        inspectFinish = false;
        showIsFinished = false;
        showCheckStock = false;
        confirmStockInspectApprover = false;
        showInputMethodManual = false;
        inputMethodId = Const.STOCK_INSPECT.INPUT_METHOD_MACHINE;
        vShopStaffApproverDTO = new VShopStaffDTO();
        vShopStaffApproverNewDTO = new VShopStaffDTO();
        listProductCheck = Lists.newArrayList();
    }

    @Secured("@")
    public void onChangeState() {
        if (!DataUtil.isNullObject(stateId) && DataUtil.isNumber(stateId) && !DataUtil.isNullObject(checkStockInspectDTO)) {
            checkStockInspectDTO.setStateId(DataUtil.safeToLong(stateId));
        } else {
            checkStockInspectDTO.setStateId(null);
        }
    }

    @Secured("@")
    public void onChangeProductOfferType() {
        try {
            productOfferingTotalDTO = new ProductOfferingTotalDTO();
            if (DataUtil.isNullOrEmpty(productOfferTypeId)) {
                lsProductOfferingTotalDTO = Lists.newArrayList();
//            } else {
//                if (DataUtil.isNullObject(checkStockInspectDTO.getStateId())) {
//                    lsProductOfferingTotalDTO = DataUtil.defaultIfNull(productOfferingService.getLsProductOfferingByProductType(null, DataUtil.safeToLong(productOfferTypeId)), new ArrayList<ProductOfferingTotalDTO>());
//                } else {
//                    lsProductOfferingTotalDTO = DataUtil.defaultIfNull(productOfferingService.getProductOfferingTotalDTO(null, DataUtil.safeToLong(productOfferTypeId),
//                            checkStockInspectDTO.getOwnerId(), checkStockInspectDTO.getOwnerType(),
//                            checkStockInspectDTO.getStateId()), new ArrayList<ProductOfferingTotalDTO>());
//                }
            }
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
            logger.error(ex.getMessage(), ex);
            topPage();
        }
    }

    @Secured("@")
    public void onChangeInputMethod() {
        if (DataUtil.safeEqual(inputMethodId, Const.STOCK_INSPECT.INPUT_METHOD_MANUAL)) {
            showInputMethodManual = true;
        } else {
            showInputMethodManual = false;
        }
        stockInspectRealDTO = new StockInspectRealDTO();
    }

    private void clearDataChecked() {
        stockInspectRealDTO = new StockInspectRealDTO();
        listProductCheck = Lists.newArrayList();
    }

    @Secured("@")
    public void doCheckStock() {
        StaffDTO staffDTO = BccsLoginSuccessHandler.getStaffDTO();
        try {
            clearDataChecked();
            if (!DataUtil.isNullObject(productOfferingTotalDTO) && !DataUtil.isNullOrZero(productOfferingTotalDTO.getProductOfferingId())) {
                checkStockInspectDTO.setProdOfferId(productOfferingTotalDTO.getProductOfferingId());
            } else {
                checkStockInspectDTO.setProdOfferId(null);
            }
            confirmInspectQuantity = "2".equals(typeInspec);

            String inspectType = "1".equals(typeInspec) ? "ENABLE_INSPECT_SERIAL" : "ENABLE_INSPECT_QUANTITY";
            String enableInspect = optionSetValueService.getValueByTwoCodeOption(inspectType, inspectType);
            if (!DataUtil.safeEqual(enableInspect, "1")) {
                reportError("", "", "stock.inspect.expired");
                return;
            }

            List<StockInspectCheckDTO> stockInspectCheckDTOs = stockInspectService.checkStockInspect(checkStockInspectDTO, staffDTO, confirmInspectQuantity);
            if (DataUtil.safeEqual(checkStockInspectDTO.getProdOfferTypeId(), Const.PRODUCT_OFFER_TYPE.PRODUCT_NO_SERIAL) || confirmInspectQuantity) {
                showStockNoSerial = true;
            } else {
                showStockNoSerial = false;
                Long quantity = stockInspectService.getQuantityStockTotal(checkStockInspectDTO.getOwnerType(), checkStockInspectDTO.getOwnerId(),
                        checkStockInspectDTO.getProdOfferTypeId(), checkStockInspectDTO.getProdOfferId(), checkStockInspectDTO.getStateId());
                Long quantityInspected = DataUtil.isNullOrEmpty(stockInspectCheckDTOs) ? 0L : stockInspectCheckDTOs.stream().mapToLong(x -> x.getQuantity()).sum();
                if (!DataUtil.isNullObject(productOfferingTotalDTO) && !DataUtil.isNullOrZero(productOfferingTotalDTO.getProductOfferingId())) {
                    reportSuccess("", "stock.inspect.check.product.serial.msg", productOfferingTotalDTO.getName(), quantity);
                    reportError("", "", "stock.inspected.check.product.serial.msg", productOfferingTotalDTO.getName(), quantityInspected);
                } else {
                    ProductOfferTypeDTO productOfferTypeDTO = productOfferTypeService.findOne(checkStockInspectDTO.getProdOfferTypeId());
                    reportSuccess("", "stock.inspect.check.product.type.serial.msg", productOfferTypeDTO.getName(), quantity);
                    reportError("", "", "stock.inspected.check.product.type.serial.msg", productOfferTypeDTO.getName(), quantityInspected);
                }
            }
            //hoangnt
            stockInspectSaveDTO = DataUtil.cloneBean(checkStockInspectDTO);
            setDefaultValue();
            listProductCheck = stockInspectCheckDTOs;
            showCheckStock = true;
            RequestContext.getCurrentInstance().execute("PrimeFaces.focus('frmStockInspect:singleSerial');");

            if (confirmInspectQuantity && DataUtil.isNullOrEmpty(listProductCheck)) {
                reportWarn("", "mn.invoice.invoiceType.nodata");
            }
        } catch (LogicException ex) {
            topReportError(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, ex);
            if (DataUtil.safeEqual(ErrorCode.ERROR_STANDARD.SUCCESS, ex.getErrorCode())) {
                try {
                    List<Date> fromToDate = stockInspectService.getFromDateToDateCheck("2".equals(typeInspec));
                    if (!DataUtil.isNullOrEmpty(fromToDate) && fromToDate.size() > 1) {
                        Date fromDate = fromToDate.get(0);
                        Date toDate = fromToDate.get(1);
                        boolean isStockInspect = stockInspectService.isFinishNotCheckStockTotal(checkStockInspectDTO.getOwnerType(),
                                checkStockInspectDTO.getOwnerId(), checkStockInspectDTO.getStateId(), checkStockInspectDTO.getProdOfferTypeId(),
                                checkStockInspectDTO.getProdOfferId(), fromDate, toDate, Const.STOCK_INSPECT.STATUS_NOT_APPROVE);
                        if (isStockInspect) {
                            showIsFinished = true;
                            List<String> lstChanelTypeId = Lists.newArrayList();
                            lstChanelTypeId.add(Const.CHANNEL_TYPE_ID.CHANNEL_TYPE_STAFF);
                            staffInfoApproverTagNew.initStaffWithChanelTypesMode(this, DataUtil.safeToString(staffDTO.getShopId()), DataUtil.safeToString(staffDTO.getStaffId()),
                                    lstChanelTypeId, false, Const.MODE_SHOP.CHILD_OF_PARRENT_SHOP_NO_STAFF);
                        } else {
                            showIsFinished = false;
                        }
                    }
                } catch (Exception ex1) {
                    logger.error(ex1.getMessage(), ex1);
                    reportError("", "common.error.happened", ex1);
                    topPage();
                }
            }
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "common.error.happened", ex);
            topPage();
        }
    }

    @Secured("@")
    public void getDataForExport() {
        try {
            //La mat hang no-serial
            /*if (DataUtil.safeEqual(productOfferTypeId, Const.PRODUCT_OFFER_TYPE.PRODUCT_NO_SERIAL.toString())) {
                throw new LogicException("", "stock.inspect.export.validate");
            }*/

            stockInspectExportDTO = stockInspectService.exportStockInspect(checkStockInspectDTO.getOwnerType(), checkStockInspectDTO.getOwnerId(),
                    DataUtil.safeToLong(productOfferTypeId), checkStockInspectDTO.getStateId(), "2".equals(typeInspec));
        } catch (LogicException ex) {
            reportError(ex.getMessage(), ex);
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "common.error.happened", ex);
            topPage();
        }
    }

    // todo: HoangNT
    @Secured("@")
    public void doExportStockInspect() {
        try {
            HSSFWorkbook workbook = getContentExportStockInspect(stockInspectExportDTO.getLstRealSerial(), stockInspectExportDTO.getLstSysSerial(),
                    stockInspectExportDTO.getLstProductNotApprove(), stockInspectExportDTO.getLstTotal());
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"" + "exportInspectStock.xls" + "\"");
            externalContext.setResponseContentType("application/vnd.ms-excel");
            workbook.write(externalContext.getResponseOutputStream());
            facesContext.responseComplete();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "common.error.happened", ex);
            topPage();
        }
    }

    @Secured("@")
    public void doStockInspectSerial() {
        doStockInspect();
    }

    @Secured("@")
    public void doStockInspectNoSerial() {
        doStockInspect();
    }

    private void doStockInspect() {
        try {
            StaffDTO staffDTO = BccsLoginSuccessHandler.getStaffDTO();
//            Long ownerId = staffDTO.getShopId();
//            Long ownerType = Const.OWNER_TYPE.SHOP_LONG;
//            if (!DataUtil.isNullObject(vShopStaffDTO) && !DataUtil.isNullOrEmpty(vShopStaffDTO.getOwnerId())) {
//                ownerId = DataUtil.safeToLong(vShopStaffDTO.getOwnerId());
//                ownerType = Const.OWNER_TYPE.STAFF_LONG;
//            }
            StaffDTO staffApprover = null;
            if (confirmStockInspectApprover && !DataUtil.isNullObject(vShopStaffApproverDTO) && !DataUtil.isNullOrEmpty(vShopStaffApproverDTO.getOwnerId())) {
                staffApprover = staffService.findOne(DataUtil.safeToLong(vShopStaffApproverDTO.getOwnerId()));
            }
            stockInspectService.saveStockInspect(stockInspectSaveDTO.getProdOfferId(), stockInspectSaveDTO.getProdOfferTypeId(), stockInspectSaveDTO.getStateId(),
                    checkStockInspectDTO.getOwnerType(), checkStockInspectDTO.getOwnerId(), listProductCheck, confirmStockInspectApprover, staffDTO, staffApprover, confirmInspectQuantity);
            inspectFinish = true;
            if (confirmStockInspectApprover) {
                reportSuccess("", "stock.inpsect.process.finish");
            } else {
                reportSuccess("", "stock.inpsect.process.success");
            }
        } catch (LogicException ex) {
            reportError("", ex);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "common.error.happened", ex);
        }
        topPage();
    }

    @Secured("@")
    public void doValidateStockInspect() {
        // Validate du lieu
        try {
//            if (confirmStockInspectApprover) {
//                if (DataUtil.isNullObject(vShopStaffApproverDTO) || DataUtil.isNullObject(vShopStaffApproverDTO.getOwnerId())) {
//                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.inspect.staff.approver.require");
//                }
//                StaffDTO staffDTO = staffService.findOne(DataUtil.safeToLong(vShopStaffApproverDTO.getOwnerId()));
//                if (DataUtil.isNullObject(staffDTO)) {
//                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.inspect.staff.approver.not.found");
//                }
//                StaffDTO userLogin = BccsLoginSuccessHandler.getStaffDTO();
//                if (DataUtil.safeEqual(staffDTO.getStaffId(), userLogin.getStaffId())) {
//                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.inspect.staff.approver.user.login.error");
//                }
//            }
            // set lai message
            if (!DataUtil.isNullObject(vShopStaffDTO) && !DataUtil.isNullOrEmpty(vShopStaffDTO.getOwnerId())) {
                messageConfirmStockInspect = getTextParam("stock.inspect.button.confirm.check.stock.staff", vShopStaffDTO.getOwnerName());
            } else {
                messageConfirmStockInspect = getText("stock.inspect.button.confirm.check.stock.shop");
            }
            List<Long> lsDamageStateId = Lists.newArrayList(Const.GOODS_STATE.BROKEN, Const.GOODS_STATE.DOA, Const.GOODS_STATE.BROKEN_15_DAY_CUSTOMER, Const.GOODS_STATE.BROKEN_WARRANTY);
            if (confirmInspectQuantity && lsDamageStateId.contains(checkStockInspectDTO.getStateId())) {
                for (StockInspectCheckDTO stockInspectCheckDTO : listProductCheck) {
                    if (stockInspectCheckDTO.isDisableQuantityPoor()) {
                        stockInspectCheckDTO.setQuantityPoor(stockInspectCheckDTO.getQuanittyReal());
                    }
                }
            }

        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "common.error.happened", ex);
            topPage();
        }
    }

    @Secured("@")
    public void doAddSerial() {
        try {
            String singleSerial;
            String fromSerial;
            String toSerial;
            if (!DataUtil.isNullOrEmpty(stockInspectRealDTO.getSingleSerial())) {
                singleSerial = stockInspectRealDTO.getSingleSerial();
                fromSerial = null;
                toSerial = null;
            } else {
                singleSerial = null;
                fromSerial = stockInspectRealDTO.getFromSerial();
                toSerial = stockInspectRealDTO.getToSerial();
            }
            List<StockInspectCheckDTO> lstSerial = stockInspectService.addSerialToListManual(stockInspectSaveDTO.getProdOfferTypeId(),
                    stockInspectSaveDTO.getProdOfferId(), stockInspectSaveDTO.getOwnerType(),
                    checkStockInspectDTO.getOwnerId(), checkStockInspectDTO.getStateId(),
                    singleSerial, fromSerial, toSerial, listProductCheck);
            if (DataUtil.isNullOrEmpty(listProductCheck)) {
                listProductCheck = Lists.newArrayList();
            }
            if (DataUtil.isNullOrEmpty(lstSerial)) {
                if (DataUtil.isNullObject(checkStockInspectDTO.getProdOfferId()) || DataUtil.isNullObject(checkStockInspectDTO.getStateId())) {
                    reportError("", "", "stock.add.serial.not.found");
                    reportSuccess("", "stock.add.serial.success", DataUtil.isNullOrEmpty(listProductCheck) ? 0L : listProductCheck.stream().mapToLong(x -> x.getQuantity()).sum());
                } else {
                    ProductOfferingDTO productOfferingDTO = productOfferingService.findOne(checkStockInspectDTO.getProdOfferId());
                    if (DataUtil.isNullObject(productOfferingDTO) || DataUtil.isNullObject(productOfferingDTO.getProductOfferingId())) {
                        reportError("", "", "stock.add.serial.not.found.product.not.found");
                    } else {
                        reportError("", "", "stock.add.serial.choose.product.offer.not.found");
                        String fromSerialTemp = fromSerial;
                        String toSerialTemp = toSerial;
                        Long quantity;
                        if (!DataUtil.isNullOrEmpty(singleSerial)) {
                            fromSerialTemp = singleSerial;
                            toSerialTemp = singleSerial;
                            quantity = 1L;
                        } else {
                            quantity = DataUtil.safeToLong(toSerialTemp) - DataUtil.safeToLong(fromSerialTemp) + 1;
                        }
                        StockInspectCheckDTO stockInspectCheckDTO = new StockInspectCheckDTO();
                        stockInspectCheckDTO.setProdOfferId(checkStockInspectDTO.getProdOfferId());
                        stockInspectCheckDTO.setStateId(checkStockInspectDTO.getStateId());
                        stockInspectCheckDTO.setStateName(mapState.get(DataUtil.safeToString(checkStockInspectDTO.getStateId())));
                        stockInspectCheckDTO.setProductName(productOfferingDTO.getName());
                        stockInspectCheckDTO.setProductCode(productOfferingDTO.getCode());
                        stockInspectCheckDTO.setProductOfferTypeId(productOfferingDTO.getProductOfferTypeId());
                        stockInspectCheckDTO.setFromSerial(fromSerialTemp);
                        stockInspectCheckDTO.setToSerial(toSerialTemp);
                        stockInspectCheckDTO.setQuantity(quantity);
                        lstSerial = Lists.newArrayList();
                        lstSerial.add(stockInspectCheckDTO);
                        listProductCheck.addAll(0, lstSerial);
                        int count = 0;
                        for (StockInspectCheckDTO stockInspectCheck : listProductCheck) {
                            count += stockInspectCheck.getQuantity();
                        }
                        reportSuccess("", "stock.add.serial.success", count);
                    }
                }
            } else {
                listProductCheck.addAll(0, lstSerial);
                int count = 0;
                for (StockInspectCheckDTO stockInspectCheck : listProductCheck) {
                    stockInspectCheck.setStateName(mapState.get(DataUtil.safeToString(stockInspectCheck.getStateId())));
                    count += stockInspectCheck.getQuantity();
                }
                reportSuccess("", "stock.add.serial.success", count);
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
    public void doApproveNew() {
        try {
            StaffDTO staffApprover = staffService.findOne(DataUtil.safeToLong(vShopStaffApproverNewDTO.getOwnerId()));
            StaffDTO staffDTO = BccsLoginSuccessHandler.getStaffDTO();
            stockInspectService.updateApproveStaffNew(stockInspectSaveDTO.getProdOfferTypeId(), stockInspectSaveDTO.getProdOfferId(),
                    stockInspectSaveDTO.getStateId(), stockInspectSaveDTO.getOwnerType(), stockInspectSaveDTO.getOwnerId(), staffDTO, staffApprover, "2".equals(typeInspec));
            reportSuccess("", "message.stock.inspect.approve.new.success");
        } catch (LogicException ex) {
            reportError(ex.getMessage(), ex);
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "common.error.happened", ex);
            topPage();
        }
    }

    @Secured("@")
    public void doValidateCheckStock() {
        try {
            productOfferingTotalDTODlg = new ProductOfferingTotalDTO();
            boolean error = false;
//            if (DataUtil.isNullObject(checkStockInspectDTO) || DataUtil.isNullOrZero(checkStockInspectDTO.getStateId())) {
//                reportError("", "", "mn.stock.track.state.stock.require.msg");
//                topPage();
//                focusElementError("cobState");
//                error = true;
//            }
            if (DataUtil.isNullObject(checkStockInspectDTO) || DataUtil.isNullOrZero(checkStockInspectDTO.getProdOfferTypeId())) {
                reportError("", "", "export.order.type.product.not.blank");
                topPage();
                focusElementError("cobProductOfferType");
                error = true;
            }
            if (DataUtil.isNullObject(checkStockInspectDTO) || DataUtil.isNullObject(checkStockInspectDTO.getOwnerId())) {
                reportError("", "", "mn.stock.limit.store.code.require.msg");
                topPage();
                focusElementError("inputStore");
                error = true;
            }
            if (error) {
                return;
            }

            if (!DataUtil.isNullObject(vShopStaffDTO) && !DataUtil.isNullOrEmpty(vShopStaffDTO.getOwnerId())) {
                messageConfirmStockInspect = getTextParam("stock.inspect.button.confirm.check.stock.staff", vShopStaffDTO.getOwnerName());
            } else {
                messageConfirmStockInspect = getText("stock.inspect.button.confirm.check.stock.shop");
            }
            stockTransFullDTODlg.setProductOfferTypeId(checkStockInspectDTO.getProdOfferTypeId());
            stockTransFullDTODlg.setStateId(checkStockInspectDTO.getStateId());
            if (productOfferingTotalDTO != null && productOfferingTotalDTO.getProductOfferingId() != null) {
                productOfferingTotalDTODlg = DataUtil.cloneBean(productOfferingTotalDTO);
                lstProductOfferingTotalDTODlg = productOfferingService.getListProductOfferingByProductType(productOfferingTotalDTODlg.getCode(), stockTransFullDTODlg.getProductOfferTypeId());
            }
        } catch (LogicException ex) {
            reportError(ex.getMessage(), ex);
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

    public void doReset() {
        checkStockInspectDTO = new StockInspectDTO();
        productOfferingTotalDTO = new ProductOfferingTotalDTO();
        checkStockInspectDTO.setCreateDate(new Date());
        listProductCheck = Lists.newArrayList();
        showCheckStock = false;

    }

    @Secured("@")
    public void clearShop() {
        this.vShopDTO = null;
        this.vShopStaffDTO = null;
        staffInfoTag.initEmptyStaff();
        checkStockInspectDTO.setOwnerId(null);
        checkStockInspectDTO.setOwnerType(null);
        doReset();
    }

    @Secured("@")
    public void receiveStaff(VShopStaffDTO vShopStaffDTO) {
        this.vShopStaffDTO = vShopStaffDTO;
        if (!DataUtil.isNullObject(vShopStaffDTO) && !DataUtil.isNullOrEmpty(vShopStaffDTO.getOwnerId())) {
            checkStockInspectDTO.setOwnerId(DataUtil.safeToLong(vShopStaffDTO.getOwnerId()));
            checkStockInspectDTO.setOwnerType(Const.OWNER_TYPE.STAFF_LONG);
        }
        showCheckStock = false;
    }

    @Secured("@")
    public void clearStaff() {
        doReset();
        this.vShopStaffDTO = null;
        if (!DataUtil.isNullObject(vShopDTO) && !DataUtil.isNullOrEmpty(vShopDTO.getOwnerId())) {
            checkStockInspectDTO.setOwnerId(DataUtil.safeToLong(vShopDTO.getOwnerId()));
            checkStockInspectDTO.setOwnerType(Const.OWNER_TYPE.SHOP_LONG);
        }
    }

    @Secured("@")
    public void onShowStaffApprover() {
        if (confirmStockInspectApprover) {
//            StaffDTO staffDTO = BccsLoginSuccessHandler.getStaffDTO();
//            List<String> lstChanelTypeId = Lists.newArrayList();
//            lstChanelTypeId.add(Const.CHANNEL_TYPE_ID.CHANNEL_TYPE_STAFF);
//            staffInfoApproverTag.initStaffWithChanelTypesMode(this, DataUtil.safeToString(staffDTO.getShopId()),
//                    DataUtil.safeToString(staffDTO.getStaffId()), lstChanelTypeId, false, Const.MODE_SHOP.CHILD_OF_PARRENT_SHOP_NO_STAFF);
        }
    }

    @Secured("@")
    public void onChooseInspectQuantity() {
        try {
            doReset();
            Long ownerId;
            Long ownerType;
            StaffDTO staffDTO = BccsLoginSuccessHandler.getStaffDTO();
            shopInfoTag.loadShop(staffDTO.getShopId().toString(), false);
            confirmInspectQuantity = true;
            if ("1".equals(typeInspec)) {
                vShopStaffDTO = staffService.findStaffById(DataUtil.safeToString(staffDTO.getStaffId()));
                staffInfoTag.loadStaff(vShopStaffDTO);
                ownerId = staffDTO.getStaffId();
                ownerType = Const.OWNER_TYPE.STAFF_LONG;
                confirmInspectQuantity = false;
            } else {
                vShopStaffDTO = new VShopStaffDTO();
                staffInfoTag.resetProduct();
                ownerId = staffDTO.getShopId();
                ownerType = Const.OWNER_TYPE.SHOP_LONG;
            }
            checkStockInspectDTO.setOwnerType(ownerType);
            checkStockInspectDTO.setOwnerId(ownerId);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "common.error.happened", ex);
            topPage();
        }
    }

    @Secured("@")
    public void receiveStaffApprover(VShopStaffDTO vShopStaffDTO) {
        this.vShopStaffApproverDTO = vShopStaffDTO;
    }

    @Secured("@")
    public void clearStaffApprover() {
        this.vShopStaffApproverDTO = null;
    }

    @Secured("@")
    public void receiveStaffApproverNew(VShopStaffDTO vShopStaffDTO) {
        this.vShopStaffApproverNewDTO = vShopStaffDTO;
    }

    @Secured("@")
    public void clearStaffApproverNew() {
        this.vShopStaffApproverNewDTO = null;
    }

    @Secured("@")
    public List<ProductOfferingTotalDTO> doChangeOffering(String inputProduct) {
        try {
            if (DataUtil.isNullOrEmpty(productOfferTypeId)) {
                return new ArrayList<ProductOfferingTotalDTO>();
            }
            String input = DataUtil.isNullOrEmpty(inputProduct) ? "" : inputProduct.trim();
            if ("2".equals(typeInspec)) {
                return lsProductOfferingTotalDTO = DataUtil.defaultIfNull(productOfferingService.getListProductOfferingByProductType(input,
                        DataUtil.safeToLong(productOfferTypeId)), new ArrayList<ProductOfferingTotalDTO>());
            } else {
                List<ProductOfferingTotalDTO> test = DataUtil.defaultIfNull(productOfferingService.getProductOfferingTotalDTO(input, DataUtil.safeToLong(productOfferTypeId),
                        checkStockInspectDTO.getOwnerId(), checkStockInspectDTO.getOwnerType(),
                        checkStockInspectDTO.getStateId()), new ArrayList<ProductOfferingTotalDTO>());
                lsProductOfferingTotalDTO = Lists.newArrayList();
                for (ProductOfferingTotalDTO dto : test) {
                    if (!lsProductOfferingTotalDTO.contains(dto)) {
                        lsProductOfferingTotalDTO.add(dto);
                    }
                }
                return lsProductOfferingTotalDTO;
            }

        } catch (Exception e) {
            reportError("", "common.error.happened", e);
            logger.error(e.getMessage(), e);
            topPage();
        }
        return new ArrayList<ProductOfferingTotalDTO>();
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

    @Secured("@")
    public boolean isDisabledState() {
        return DataUtil.isNullObject(productOfferingTotalDTO) ? false : (DataUtil.isNullOrZero(productOfferingTotalDTO.getProductOfferingId()) ? false : true);
    }

    @Secured("@")
    public void clearProduct() {
        this.productOfferingTotalDTO = null;
        checkStockInspectDTO.setProdOfferId(null);
    }

    //Hoangnt
    @Secured("@")
    public void addSingleSerialAuto() {
        String input = stockInspectRealDTO.getSingleSerial();
        if (DataUtil.isNullOrEmpty(input)) {
            return;
        }
        // them serial
        doAddSerial();
        stockInspectRealDTO = new StockInspectRealDTO();
        RequestContext.getCurrentInstance().execute("PrimeFaces.focus('frmStockInspect:singleSerial');");
    }

    public boolean isExistDataSpecQuantity() {
        return listProductCheck != null && listProductCheck.size() > 0;
    }

    @Secured("@")
    public void addListSerialAuto() {
        // them serial
        doAddSerial();
        stockInspectRealDTO = new StockInspectRealDTO();
        RequestContext.getCurrentInstance().execute("PrimeFaces.focus('frmStockInspect:fromSerial');");
    }

    @Secured("@")
    public void addListSerial2D() {
        try {
            String serial2D = stockInspectRealDTO.getSerial2D();
            if (DataUtil.isNullOrEmpty(serial2D)) {
                return;
            }
            // them serial
            List<StockInspectCheckDTO> lstSerial = stockInspectService.addSerialToList2D(stockInspectSaveDTO.getProdOfferTypeId(),
                    stockInspectSaveDTO.getProdOfferId(), stockInspectSaveDTO.getOwnerType(),
                    stockInspectSaveDTO.getOwnerId(), stockInspectSaveDTO.getStateId(),
                    serial2D, listProductCheck);
            if (DataUtil.isNullOrEmpty(listProductCheck)) {
                listProductCheck = Lists.newArrayList();
            }
            if (DataUtil.isNullOrEmpty(lstSerial)) {
                reportError("", "", "mn.stock.inspect.serial2d.not.find");
            } else {
                listProductCheck.addAll(lstSerial);
                int count = 0;
                for (StockInspectCheckDTO stockInspectCheck : listProductCheck) {
                    stockInspectCheck.setStateName(mapState.get(DataUtil.safeToString(stockInspectCheck.getStateId())));
                    count += stockInspectCheck.getQuantity();
                }
                reportSuccess("", "stock.add.serial.success", count);
            }
        } catch (LogicException ex) {
            reportError(ex.getMessage(), ex);
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "common.error.happened", ex);
            topPage();
        }
        stockInspectRealDTO = new StockInspectRealDTO();
        RequestContext.getCurrentInstance().execute("PrimeFaces.focus('frmStockInspect:serial2d');");
    }

    /**
     * Load danh sach mat hang theo loai hang hoa
     */
    @Secured("@")
    public void onChangeOfferType() {
        try {
            lstProductOfferingTotalDTODlg = Lists.newArrayList();
            productOfferingTotalDTODlg = new ProductOfferingTotalDTO();
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
            logger.error(ex.getMessage(), ex);
            topPage();
        }
    }

    /**
     * Tim kiem mat hang khi nhap tay
     */
    @Secured("@")
    public List<ProductOfferingTotalDTO> searchListProductOffering(String input) {
        try {
            lstProductOfferingTotalDTODlg = Lists.newArrayList();
            if (!DataUtil.isNullOrZero(stockTransFullDTODlg.getProductOfferTypeId())) {
                return lstProductOfferingTotalDTODlg = DataUtil.defaultIfNull(productOfferingService.getListProductOfferingByProductType(input,
                        DataUtil.safeToLong(stockTransFullDTODlg.getProductOfferTypeId())), new ArrayList<ProductOfferingTotalDTO>());
            }
        } catch (LogicException e) {
            logger.error(e.getMessage(), e);
            reportError(e.getMessage(), e);
        } catch (Exception e) {
            topReportError("", "common.error.happened", e);
        }
        return lstProductOfferingTotalDTODlg;
    }

    /**
     * reset mat hang da chon
     */
    @Secured("@")
    public void resetLstProductOfferingTotal() {
        productOfferingTotalDTODlg = null;
    }

    /**
     * reset mat hang da chon
     */
    @Secured("@")
    public void doAddProduct() {
        try {
            if (listProductCheck != null) {
                //check trung lap mat hang
                List<Long> lsProdOfferIdCheck = listProductCheck.stream().map(x -> x.getProdOfferId()).distinct().collect(Collectors.toList());
                if (lsProdOfferIdCheck.contains(productOfferingTotalDTODlg.getProductOfferingId())) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.inspect.duplicat.prodOfferId", productOfferingTotalDTODlg.getCode());
                }
                List<Long> lsDamageStateId = Lists.newArrayList(Const.GOODS_STATE.BROKEN, Const.GOODS_STATE.DOA, Const.GOODS_STATE.BROKEN_15_DAY_CUSTOMER, Const.GOODS_STATE.BROKEN_WARRANTY);

                //them moi danh sach mat hang vao list kiem ke
                StockInspectCheckDTO specDTO = new StockInspectCheckDTO();
                specDTO.setProductOfferTypeId(stockTransFullDTODlg.getProductOfferTypeId());
                specDTO.setProdOfferId(productOfferingTotalDTODlg.getProductOfferingId());
                specDTO.setProductCode(productOfferingTotalDTODlg.getCode());
                specDTO.setProductName(productOfferingTotalDTODlg.getName());
                specDTO.setOwnerId(checkStockInspectDTO.getOwnerId());
                specDTO.setOwnerType(checkStockInspectDTO.getOwnerType());
                specDTO.setStateId(stockTransFullDTODlg.getStateId());
                specDTO.setStateName(mapState.get(DataUtil.safeToString(specDTO.getStateId())));
                specDTO.setCurrentQuantity(0L);
                specDTO.setQuanittyReal(0L);
                specDTO.setQuantityPoor(0L);
                specDTO.setDisableQuantityPoor(specDTO.getStateId() != null && lsDamageStateId.contains(specDTO.getStateId()));
                listProductCheck.add(0, specDTO);
                productOfferingTotalDTODlg = new ProductOfferingTotalDTO();
                updateElemetId("frmStockInspect:tblLsProduct");
            }
        } catch (LogicException e) {
            logger.error(e.getMessage(), e);
            reportError(e.getMessage(), e);
        } catch (Exception e) {
            topReportError("", "common.error.happened", e);
        }


    }


    @Secured("@")
    public void doRemoveItemProductOffer(int index) {
        listProductCheck.remove(index);
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

    public VShopStaffDTO getvShopStaffDTO() {
        return vShopStaffDTO;
    }

    public void setvShopStaffDTO(VShopStaffDTO vShopStaffDTO) {
        this.vShopStaffDTO = vShopStaffDTO;
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

    public boolean isTimeCheckStock() {
        return timeCheckStock;
    }

    public void setTimeCheckStock(boolean timeCheckStock) {
        this.timeCheckStock = timeCheckStock;
    }

    public boolean isShowStockNoSerial() {
        return showStockNoSerial;
    }

    public void setShowStockNoSerial(boolean showStockNoSerial) {
        this.showStockNoSerial = showStockNoSerial;
    }

    public boolean isShowCheckStock() {
        return showCheckStock;
    }

    public void setShowCheckStock(boolean showCheckStock) {
        this.showCheckStock = showCheckStock;
    }

    public List<StockInspectCheckDTO> getListProductCheck() {
        return listProductCheck;
    }

    public void setListProductCheck(List<StockInspectCheckDTO> listProductCheck) {
        this.listProductCheck = listProductCheck;
    }

    public Long getInputMethodId() {
        return inputMethodId;
    }

    public void setInputMethodId(Long inputMethodId) {
        this.inputMethodId = inputMethodId;
    }

    public List<OptionSetValueDTO> getListInputMethod() {
        return listInputMethod;
    }

    public void setListInputMethod(List<OptionSetValueDTO> listInputMethod) {
        this.listInputMethod = listInputMethod;
    }

    public boolean isShowInputMethodManual() {
        return showInputMethodManual;
    }

    public void setShowInputMethodManual(boolean showInputMethodManual) {
        this.showInputMethodManual = showInputMethodManual;
    }

    public StockInspectRealDTO getStockInspectRealDTO() {
        return stockInspectRealDTO;
    }

    public void setStockInspectRealDTO(StockInspectRealDTO stockInspectRealDTO) {
        this.stockInspectRealDTO = stockInspectRealDTO;
    }

    public boolean isConfirmStockInspectApprover() {
        return confirmStockInspectApprover;
    }

    public void setConfirmStockInspectApprover(boolean confirmStockInspectApprover) {
        this.confirmStockInspectApprover = confirmStockInspectApprover;
    }

    public VShopStaffDTO getvShopStaffApproverDTO() {
        return vShopStaffApproverDTO;
    }

    public void setvShopStaffApproverDTO(VShopStaffDTO vShopStaffApproverDTO) {
        this.vShopStaffApproverDTO = vShopStaffApproverDTO;
    }

    public StaffInfoNameable getStaffInfoApproverTag() {
        return staffInfoApproverTag;
    }

    public void setStaffInfoApproverTag(StaffInfoNameable staffInfoApproverTag) {
        this.staffInfoApproverTag = staffInfoApproverTag;
    }

    public String getMessageConfirmStockInspect() {
        return messageConfirmStockInspect;
    }

    public void setMessageConfirmStockInspect(String messageConfirmStockInspect) {
        this.messageConfirmStockInspect = messageConfirmStockInspect;
    }

    public boolean isInspectFinish() {
        return inspectFinish;
    }

    public void setInspectFinish(boolean inspectFinish) {
        this.inspectFinish = inspectFinish;
    }

    public boolean isPermissionVTT() {
        return permissionVTT;
    }

    public void setPermissionVTT(boolean permissionVTT) {
        this.permissionVTT = permissionVTT;
    }

    public boolean isConfirmInspectQuantity() {
        return confirmInspectQuantity;
    }

    public void setConfirmInspectQuantity(boolean confirmInspectQuantity) {
        this.confirmInspectQuantity = confirmInspectQuantity;
    }

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public boolean isShowIsFinished() {
        return showIsFinished;
    }

    public void setShowIsFinished(boolean showIsFinished) {
        this.showIsFinished = showIsFinished;
    }

    public StaffInfoNameable getStaffInfoApproverTagNew() {
        return staffInfoApproverTagNew;
    }

    public void setStaffInfoApproverTagNew(StaffInfoNameable staffInfoApproverTagNew) {
        this.staffInfoApproverTagNew = staffInfoApproverTagNew;
    }

    public VShopStaffDTO getvShopStaffApproverNewDTO() {
        return vShopStaffApproverNewDTO;
    }

    public void setvShopStaffApproverNewDTO(VShopStaffDTO vShopStaffApproverNewDTO) {
        this.vShopStaffApproverNewDTO = vShopStaffApproverNewDTO;
    }

    public String getTypeInspec() {
        return typeInspec;
    }

    public void setTypeInspec(String typeInspec) {
        this.typeInspec = typeInspec;
    }

    public ProductOfferingTotalDTO getProductOfferingTotalDTODlg() {
        return productOfferingTotalDTODlg;
    }

    public void setProductOfferingTotalDTODlg(ProductOfferingTotalDTO productOfferingTotalDTODlg) {
        this.productOfferingTotalDTODlg = productOfferingTotalDTODlg;
    }

    public List<ProductOfferTypeDTO> getLstProductOfferTypeDTODlg() {
        return lstProductOfferTypeDTODlg;
    }

    public void setLstProductOfferTypeDTODlg(List<ProductOfferTypeDTO> lstProductOfferTypeDTODlg) {
        this.lstProductOfferTypeDTODlg = lstProductOfferTypeDTODlg;
    }

    public List<ProductOfferingTotalDTO> getLstProductOfferingTotalDTODlg() {
        return lstProductOfferingTotalDTODlg;
    }

    public void setLstProductOfferingTotalDTODlg(List<ProductOfferingTotalDTO> lstProductOfferingTotalDTODlg) {
        this.lstProductOfferingTotalDTODlg = lstProductOfferingTotalDTODlg;
    }

    public StockTransFullDTO getStockTransFullDTODlg() {
        return stockTransFullDTODlg;
    }

    public void setStockTransFullDTODlg(StockTransFullDTO stockTransFullDTODlg) {
        this.stockTransFullDTODlg = stockTransFullDTODlg;
    }

    public ShopInfoNameable getShopInfoTag() {
        return shopInfoTag;
    }

    public void setShopInfoTag(ShopInfoNameable shopInfoTag) {
        this.shopInfoTag = shopInfoTag;
    }
}
