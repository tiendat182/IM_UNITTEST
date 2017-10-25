package com.viettel.bccs.inventory.controller.isdn;

import com.google.common.collect.Lists;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.fw.controller.TransCommonController;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.common.ExcellUtil;
import com.viettel.bccs.inventory.common.model.FileExportBean;
import com.viettel.bccs.inventory.common.util.FileUtil;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.*;
import com.viettel.bccs.inventory.tag.OrderStockTagNameable;
import com.viettel.bccs.inventory.tag.ShopInfoNameable;
import com.viettel.bccs.inventory.tag.SignOfficeTagNameable;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.RequiredRoleMap;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.web.common.security.CustomAuthenticationProvider;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.omnifaces.util.Faces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.InputStream;
import java.util.*;

/**
 * Created by anhnv33 05/01/2016
 */
@Component
@Scope("view")
@ManagedBean
public class CreatFieldExportIsdnController extends TransCommonController {
    private boolean infoOrderDetail;
    private boolean isDone;
    private boolean inputByFile;
    private boolean writeOffice = true;
    private boolean isEnought;
    private int limitAutoComplete;
    private int inputIsdnType;
    private String exportCode;
    private StockTransDTO stockTransCur;
    private VStockTransDTO forSearch;
    private StockTransSerialDTO stockTransSerial;
    private StockTransFullDTO selectStockTransFull;
    private RequiredRoleMap requiredRoleMap;
    private StaffDTO staffDTO;
    private UploadedFile uploadedFile;
    private String attachFileName;
    private byte[] byteContent;
    private ExcellUtil processingFile;

    private List<OptionSetValueDTO> optionSetValueDTOsList;
    private List<VStockTransDTO> listResultSearch;
    private List<StockTransFullDTO> lstGoods;
    private List<StockTransSerialDTO> lstStockTransSerialDTO;

    @Autowired
    private OptionSetValueService optionSetValueService;
    @Autowired
    private OrderStockTagNameable orderStockTag;
    @Autowired
    private SignOfficeTagNameable signOfficeTag;
    @Autowired
    private ShopInfoNameable shopInfoTagExport;
    @Autowired
    private ShopInfoNameable shopInfoTagReceive;
    @Autowired
    private StockTransService stockTransService;
    @Autowired
    private StockTransSerialService stockTransSerialService;
    @Autowired
    private StockNumberService stockNumberService;
    @Autowired
    private StockTransActionService stockTransActionService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private ReasonService reasonService;
    @Autowired
    private StaffService staffService;
    @Autowired
    private StockTransVofficeService stockTransVofficeService;
    @Autowired
    private ExecuteStockTransService executeStockTransService;

    private boolean hasRoleSignCa = false;

    @Secured("@")
    @PostConstruct
    public void init() {
        try {
            isDone = false;
            infoOrderDetail = false;
            isEnought = false;
            selectStockTransFull = new StockTransFullDTO();
            staffDTO = BccsLoginSuccessHandler.getStaffDTO();
            String shopId = DataUtil.safeToString(BccsLoginSuccessHandler.getStaffDTO().getShopId());
            shopInfoTagReceive.initShop(this, shopId, false);
            shopInfoTagExport.initShop(this, shopId, false);
            shopInfoTagExport.initShopForIsdn(this, shopId, true, null);
            shopInfoTagReceive.initShopForIsdn(this, shopId, true, null);
//            shopInfoTagExport.loadShop(shopId, true);
            optionSetValueDTOsList = Lists.newArrayList();
            List<OptionSetValueDTO> lsOption = DataUtil.defaultIfNull(optionSetValueService.getByOptionSetCode(Const.OPTION_SET.STOCK_TRANS_STATUS), new ArrayList<>());
            for (OptionSetValueDTO osv : lsOption) {
                if (DataUtil.safeEqual(osv.getValue(), Const.STOCK_TRANS_STATUS.EXPORT_ORDER)
                        || DataUtil.safeEqual(osv.getValue(), Const.STOCK_TRANS_STATUS.EXPORT_NOTE)
                        || DataUtil.safeEqual(osv.getValue(), Const.STOCK_TRANS_STATUS.IMPORTED)
                        || DataUtil.safeEqual(osv.getValue(), Const.STOCK_TRANS_STATUS.CANCEL)) {
                    optionSetValueDTOsList.add(osv);
                }
            }
            Collections.sort(optionSetValueDTOsList, new Comparator<OptionSetValueDTO>() {
                public int compare(OptionSetValueDTO o1, OptionSetValueDTO o2) {
                    return Long.valueOf(o1.getValue()).compareTo(Long.valueOf(o2.getValue()));
                }
            });
            signOfficeTag.init(this, BccsLoginSuccessHandler.getStaffDTO().getShopId());
            limitAutoComplete = DataUtil.safeToInt(optionSetValueService.getValueByNameFromOptionSetValue(Const.OPTION_SET.LIMIT_AUTOCOMPLETE));
//            requiredRoleMap = CustomAuthenticationProvider.createRequiredRoleMap(Const.PERMISION.ROLE_SYNC_LOGISTIC, Const.PERMISION.ROLE_TRANSPORT_STOCK, Const.PERMISION.ROLE_XUAT_KHO_BA_MIEN);
            requiredRoleMap = CustomAuthenticationProvider.createRequiredRoleMap(Const.PERMISION.ROLE_TRANSPORT_STOCK, Const.PERMISION.ROLE_STOCK_NUM_SHOP);
//            Date currentDate = Calendar.getInstance().getTime();
            Date currentDate = getSysdate();
            forSearch = new VStockTransDTO(currentDate, currentDate);
            forSearch.setStockTransStatus(Const.STOCK_TRANS_STATUS.EXPORT_ORDER);
            lstStockTransSerialDTO = Lists.newArrayList();
            doSearch();
            orderStockTag.init(this, writeOffice);
            hasRoleSignCa = CustomAuthenticationProvider.hasRole(Const.PERMISION.ROLE_SIGN_CA);
            if (hasRoleSignCa) {
                writeOffice = true;
            }
        } catch (Exception e) {
            logger.error(e);
            reportError("", "common.error.happened", e);
        }
    }

    @Secured("@")
    public void doSearch() {
        try {
            if (forSearch.getStartDate().after(getCurrentDate())) {
                reportErrorValidateFail("frmExportIsdn:msgs", "", "mn.isdn.manage.creat.field.export.startDateAfterToDay");
                focusElementByRawCSSSlector(".fromDate");
                topPage();
                return;
            }
            if (forSearch.getEndDate().after(getCurrentDate())) {
                reportErrorValidateFail("frmExportIsdn:msgs", "", "mn.isdn.manage.creat.field.export.endDateAfterToDay");
                focusElementByRawCSSSlector(".toDate");
                topPage();
                return;
            }
            if (forSearch.getStartDate().getYear() + 1900 > 9999 || forSearch.getStartDate().getYear() + 1900 < 1000) {
                reportErrorValidateFail("frmExportIsdn:msgs", "", "mn.isdn.manage.creat.field.export.startDateLength");
                focusElementByRawCSSSlector(".fromDate");
                topPage();
                return;
            }
            if (forSearch.getEndDate().getYear() + 1900 > 9999 || forSearch.getEndDate().getYear() + 1900 < 1000) {
                reportErrorValidateFail("frmExportIsdn:msgs", "", "mn.isdn.manage.creat.field.export.endDateLength");
                focusElementByRawCSSSlector(".toDate");
                topPage();
                return;
            }
            listResultSearch = Lists.newArrayList();
            infoOrderDetail = false;
            selectStockTransFull = new StockTransFullDTO();
            forSearch.setActionType(Const.STOCK_TRANS_STATUS.EXPORT_ORDER);
//            forSearch.setFromOwnerType(Const.OWNER_TYPE.SHOP_LONG);
//            forSearch.setFromOwnerID(staffDTO.getShopId());
//            forSearch.setToOwnerType(Const.OWNER_TYPE.SHOP_LONG);
//            forSearch.setVtUnit(Const.VT_UNIT.VT);
//            forSearch.setObjectType(Const.OWNER_TYPE.SHOP);
//            forSearch.setUserShopId(staffDTO.getShopId());
            if (DataUtil.isNullObject(forSearch.getFromOwnerID())) {
                forSearch.setCurrentUserShopId(staffDTO.getShopId());
                forSearch.setCurrentUserShopPath(staffDTO.getShopPath());
            }
            forSearch.setStockTransType(DataUtil.safeToLong(Const.STOCK_TRANS_TYPE.ISDN));
            listResultSearch = stockTransService.searchVStockTrans(forSearch);
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            if (DataUtil.safeEqual(ex.getKeyMsg(), "stock.trans.from.to.valid")) {
                focusElementByRawCSSSlector(".fromDate");
                focusElementByRawCSSSlector(".toDate");
            }
            reportErrorValidateFail("frmExportIsdn:msgs", "", ex.getDescription());
            topPage();
        } catch (Exception ex) {
            logger.error(ex);
            reportErrorValidateFail("frmExportIsdn:msgs", "", "common.error.happened");
            topPage();
        }
    }

    @Secured("@")
    public void receiveWriteOffice() {
        orderStockTag.setWriteOffice(writeOffice);
    }

    @Secured("@")
    public void preRemove(Long stockTransId) {
        try {
            stockTransCur = stockTransService.findOne(stockTransId);
            if (!stockTransCur.getStatus().equals(Const.STOCK_TRANS_STATUS.EXPORT_ORDER)
                    && !stockTransCur.getStatus().equals(Const.STOCK_TRANS_STATUS.EXPORT_NOTE)) {
                reportErrorValidateFail("frmExportIsdn:msgs", "", "ws.delete.err.status");
                return;
            }
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportErrorValidateFail("frmExportIsdn:msgs", "", ex.getDescription());
            topPage();
        } catch (Exception ex) {
            logger.error(ex);
            reportErrorValidateFail("frmExportIsdn:msgs", "", "common.error.happened");
            topPage();
        }
    }

    @Secured("@")
    public void doRemoveItem() {
        try {
            StockTransDTO stockTransDTO = stockTransService.findOne(stockTransCur.getStockTransId());
            if (!stockTransDTO.getStatus().equals(Const.STOCK_TRANS_STATUS.EXPORT_ORDER)
                    && !stockTransDTO.getStatus().equals(Const.STOCK_TRANS_STATUS.EXPORT_NOTE)) {
                reportErrorValidateFail("frmExportIsdn:msgs", "", "ws.delete.err.status");
                return;
            } else {
                stockTransDTO.setStatus(Const.STOCK_TRANS_STATUS.CANCEL);
                stockTransService.save(stockTransDTO);
                StockTransActionDTO transActionDTO = new StockTransActionDTO();
                transActionDTO.setStatus(Const.STOCK_TRANS_STATUS.CANCEL);
                transActionDTO.setStockTransId(stockTransDTO.getStockTransId());
                transActionDTO.setCreateDatetime(optionSetValueService.getSysdateFromDB(false));
                transActionDTO.setCreateUser(staffDTO.getStaffCode());
                transActionDTO.setActionStaffId(staffDTO.getStaffId());
                stockTransActionService.save(transActionDTO);
                for (VStockTransDTO dto : listResultSearch) {
                    if (DataUtil.safeEqual(dto.getStockTransID(), stockTransCur.getStockTransId())) {
                        dto.setStockTransStatus(Const.STOCK_TRANS_STATUS.CANCEL);
                    }
                }
            }
            doSearch();
            reportSuccess("frmExportIsdn:msgs", "export.note.isdn.delete.success");
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportErrorValidateFail("frmExportIsdn:msgs", "", ex.getDescription());
            topPage();
        } catch (Exception ex) {
            logger.error(ex);
            reportErrorValidateFail("frmExportIsdn:msgs", "", "common.error.happened");
            topPage();
        }
    }

    @Secured("@")
    public void doShowInfoOrderDetail(Long stockTransActionId) {
        try {
            if (validateVoffice(stockTransActionId)) {
                infoOrderDetail = true;
                isDone = false;
                listResultSearch = Lists.newArrayList();
                orderStockTag.loadOrderStock(stockTransActionId, true);
                lstStockTransSerialDTO = Lists.newArrayList();
                List<Long> lsStockTransId = Lists.newArrayList(stockTransActionId);
                lstGoods = DataUtil.defaultIfNull(stockTransService.searchStockTransDetail(lsStockTransId), new ArrayList<StockTransFullDTO>());
                if (hasRoleSignCa) {
                    writeOffice = true;
                    receiveWriteOffice();
                }
//            exportCode = Const.PREFIX_REGION.PX + staffDTO.getShopCode() + "_" + staffDTO.getStaffCode() + "_" + staffService.getTransCodeByStaffId(staffDTO.getStaffId());
                exportCode = staffService.getTransCode(Const.STOCK_TRANS.TRANS_CODE_PX, Const.STOCK_TRANS_TYPE.ISDN, BccsLoginSuccessHandler.getStaffDTO());
            } else {
                return;
            }
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportErrorValidateFail("frmExportIsdn:msgs", "", ex.getDescription());
            topPage();
        } catch (Exception ex) {
            logger.error(ex);
            reportErrorValidateFail("frmExportIsdn:msgs", "", "common.error.happened");
            topPage();
        }
    }

    @Secured("@")
    public void preAddIsdn(int index) {
        stockTransSerial = new StockTransSerialDTO();
        selectStockTransFull = new StockTransFullDTO();
        selectStockTransFull = lstGoods.get(index);
        lstStockTransSerialDTO = DataUtil.defaultIfNull(selectStockTransFull.getLstSerial(), new ArrayList<StockTransSerialDTO>());
        inputByFile = true;
        isEnought = selectStockTransFull.getEnoughIsdn();
    }

    @Secured("@")
    public void selectIsdn() {
        try {
            String ownerType = selectStockTransFull.getFromOwnerType();
            Long ownerId = selectStockTransFull.getFromOwnerId();
            Long prodOfferId = selectStockTransFull.getProdOfferId();
            List<StockTransSerialDTO> lstSerialSelected;
            if (inputByFile) {
                BaseMessage message = validateFileUploadWhiteList(uploadedFile, ALOW_EXTENSION_EXCEL_TYPE_LIST, MAX_SIZE_5M);
                if (!message.isSuccess()) {
                    LogicException ex = new LogicException(message.getErrorCode(), message.getKeyMsg());
                    ex.setDescription(message.getDescription());
                    throw ex;
                }
                List<String> lstIsdn = validateFileUpload();
                if (DataUtil.isNullOrEmpty(lstIsdn)) {
                    return;
                }
                if (!DataUtil.isNullOrEmpty(lstStockTransSerialDTO)) {
                    for (StockTransSerialDTO dto : lstStockTransSerialDTO) {
                        Long start = DataUtil.safeToLong(dto.getFromSerial());
                        Long end = DataUtil.safeToLong(dto.getToSerial());
                        for (String isdn : lstIsdn) {
                            if ((start <= DataUtil.safeToLong(isdn) && DataUtil.safeToLong(isdn) <= end)) {
                                reportErrorValidateFail("frmInputIsdn:msgInputIsdn", "", "ws.isdn.duplicate");
                                return;
                            }
                        }
                    }
                }

                for (StockTransFullDTO dto : lstGoods) {
                    if (!DataUtil.isNullOrEmpty(dto.getLstSerial())) {
                        for (StockTransSerialDTO stsDTO : dto.getLstSerial()) {
                            Long start = DataUtil.safeToLong(stsDTO.getFromSerial());
                            Long end = DataUtil.safeToLong(stsDTO.getToSerial());
                            for (String isdn : lstIsdn) {
                                if ((start <= DataUtil.safeToLong(isdn) && DataUtil.safeToLong(isdn) <= end)) {
                                    reportErrorValidateFail("frmInputIsdn:msgInputIsdn", "", "ws.isdn.duplicate");
                                    return;
                                }
                            }
                        }
                    }
                }
                lstSerialSelected = stockNumberService.findByListIsdn(lstIsdn, ownerType, ownerId, prodOfferId);
                if (DataUtil.isNullOrEmpty(lstSerialSelected)) {
                    reportErrorValidateFail("frmInputIsdn:msgInputIsdn", "", "create.field.export.isdn.not.isdn.valid");
                }
            } else {
                String fromIsdn = stockTransSerial.getFromSerial();
                String toIsdn = stockTransSerial.getToSerial();
                //validate
                if (DataUtil.isNullObject(fromIsdn)) {
                    reportErrorValidateFail("frmInputIsdn:msgInputIsdn", "", "start.range.require");
                    focusElementByRawCSSSlector(".digitalStartRanges");
                    focusElementWithCursor(".digitalStartRanges");
                    return;
                }
                if (!fromIsdn.matches("^[0-9]+$")) {
                    reportErrorValidateFail("frmInputIsdn:msgInputIsdn", "", "component.invalid", "digital.start.ranges");
                    focusElementByRawCSSSlector(".digitalStartRanges");
                    focusElementWithCursor(".digitalStartRanges");
                    return;
                }
                if (DataUtil.isNullObject(toIsdn)) {
                    reportErrorValidateFail("frmInputIsdn:msgInputIsdn", "", "end.range.require");
                    focusElementByRawCSSSlector(".digitalEndRanges");
                    focusElementWithCursor(".digitalEndRanges");
                    return;
                }
                if (!toIsdn.matches("^[0-9]+$")) {
                    reportErrorValidateFail("frmInputIsdn:msgInputIsdn", "", "component.invalid", "digital.end.ranges");
                    focusElementByRawCSSSlector(".digitalEndRanges");
                    focusElementWithCursor(".digitalEndRanges");
                    return;
                }
                if (fromIsdn.length() != toIsdn.length()) {
                    reportErrorValidateFail("frmInputIsdn:msgInputIsdn", "", "ws.error.range.length");
                    focusElementByRawCSSSlector(".digitalStartRanges");
                    focusElementByRawCSSSlector(".digitalEndRanges");
                    focusElementWithCursor(".digitalStartRanges");
                    return;
                }
                if ((fromIsdn.length() < 8 || fromIsdn.length() > 11)) {
                    reportErrorValidateFail("frmInputIsdn:msgInputIsdn", "", "ws.range.start.length.invalid");
                    focusElementByRawCSSSlector(".digitalStartRanges");
                    focusElementWithCursor(".digitalStartRanges");
                    return;
                }
                if ((toIsdn.length() < 8 || toIsdn.length() > 11)) {
                    reportErrorValidateFail("frmInputIsdn:msgInputIsdn", "", "ws.range.end.length.invalid");
                    focusElementByRawCSSSlector(".digitalEndRanges");
                    focusElementWithCursor(".digitalEndRanges");
                    return;
                }
                Long range = DataUtil.safeToLong(toIsdn) - DataUtil.safeToLong(fromIsdn);
                if (range < 0) {
                    reportErrorValidateFail("frmInputIsdn:msgInputIsdn", "", "ws.error.range");
                    focusElementByRawCSSSlector(".digitalStartRanges");
                    focusElementByRawCSSSlector(".digitalEndRanges");
                    focusElementWithCursor(".digitalStartRanges");
                    return;
                } else if (range > 999999) {
                    reportErrorValidateFail("frmInputIsdn:msgInputIsdn", "", "ws.isdn.per.range.over");
                    focusElementByRawCSSSlector(".digitalStartRanges");
                    focusElementByRawCSSSlector(".digitalEndRanges");
                    focusElementWithCursor(".digitalStartRanges");
                    return;
                }

                if (!DataUtil.isNullOrEmpty(lstStockTransSerialDTO)) {
                    for (StockTransSerialDTO dto : lstStockTransSerialDTO) {
                        Long start = DataUtil.safeToLong(dto.getFromSerial());
                        Long end = DataUtil.safeToLong(dto.getToSerial());
                        Long sIsdn = DataUtil.safeToLong(fromIsdn);
                        Long eIsdn = DataUtil.safeToLong(toIsdn);
                        if ((start <= sIsdn && sIsdn <= end) || (start <= eIsdn && eIsdn <= end) || (sIsdn <= start && eIsdn >= end)) {
                            reportErrorValidateFail("frmInputIsdn:msgInputIsdn", "", "ws.isdn.duplicate");
                            return;
                        }
                    }
                }

                for (StockTransFullDTO dto : lstGoods) {
                    if (!DataUtil.isNullOrEmpty(dto.getLstSerial())) {
                        for (StockTransSerialDTO stsDTO : dto.getLstSerial()) {
                            Long start = DataUtil.safeToLong(stsDTO.getFromSerial());
                            Long end = DataUtil.safeToLong(stsDTO.getToSerial());
                            Long sIsdn = DataUtil.safeToLong(fromIsdn);
                            Long eIsdn = DataUtil.safeToLong(toIsdn);
                            if ((start <= sIsdn && sIsdn <= end) || (start <= eIsdn && eIsdn <= end) || (sIsdn <= start && eIsdn >= end)) {
                                reportErrorValidateFail("frmInputIsdn:msgInputIsdn", "", "ws.isdn.duplicate");
                                return;
                            }
                        }
                    }
                }
                lstSerialSelected = stockNumberService.findRangeForExportNote(fromIsdn, toIsdn, ownerType, ownerId, prodOfferId);
                if (DataUtil.isNullOrEmpty(lstSerialSelected)) {
                    reportErrorValidateFail("frmInputIsdn:msgInputIsdn", "", "create.field.export.isdn.not.isdn.valid");
                } else {
                    stockTransSerial.setFromSerial(null);
                    stockTransSerial.setToSerial(null);
                }
            }

            Long currQuantity = 0L;
            if (!DataUtil.isNullOrEmpty(lstStockTransSerialDTO)) {
                for (StockTransSerialDTO dto : lstStockTransSerialDTO) {
                    currQuantity += dto.getQuantity();
                }
            }
            Long totalQuantity = selectStockTransFull.getQuantity();
            if (currQuantity >= totalQuantity) {
                isEnought = true;
                reportErrorValidateFail("frmInputIsdn:msgInputIsdn", "", "ws.isdn.add.err.full");
                return;
            }

            if (!DataUtil.isNullOrEmpty(lstSerialSelected)) {
                for (StockTransSerialDTO dto : lstSerialSelected) {
                    Long remainQuantity = totalQuantity - currQuantity;
                    if (remainQuantity <= 0)
                        break;
                    if (dto.getQuantity() >= remainQuantity) {
                        Long fIsdn = DataUtil.safeToLong(dto.getFromSerial());
                        Long tIsdn = fIsdn + remainQuantity - 1;
                        dto.setToSerial(DataUtil.safeToString(tIsdn));
                        dto.setQuantity(remainQuantity);
                        isEnought = true;
//                        lstStockTransSerialDTO.add(dto);
                    }
                    boolean isCon = false;
                    StockTransSerialDTO tmpStockTransSerial = new StockTransSerialDTO();
                    for (StockTransSerialDTO serialDTO : lstStockTransSerialDTO) {
                        if (DataUtil.safeToLong(dto.getFromSerial()) == DataUtil.safeToLong(serialDTO.getToSerial()) + 1) {
                            serialDTO.setToSerial(dto.getToSerial());
                            serialDTO.setQuantity(serialDTO.getQuantity() + dto.getQuantity());
                            tmpStockTransSerial = DataUtil.cloneBean(serialDTO);
                            lstStockTransSerialDTO.remove(serialDTO);
                            isCon = true;
                            break;
                        } else if (DataUtil.safeToLong(dto.getToSerial()) == DataUtil.safeToLong(serialDTO.getFromSerial()) - 1) {
                            serialDTO.setFromSerial(dto.getFromSerial());
                            serialDTO.setQuantity(serialDTO.getQuantity() + dto.getQuantity());
                            tmpStockTransSerial = DataUtil.cloneBean(serialDTO);
                            lstStockTransSerialDTO.remove(serialDTO);
                            isCon = true;
                            break;
                        }
                    }
                    if (isCon) {
                        joinIsdn(lstStockTransSerialDTO, tmpStockTransSerial);
                    } else {
                        lstStockTransSerialDTO.add(dto);
                    }
                    currQuantity += dto.getQuantity();
                }
            }

        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportErrorValidateFail("frmExportIsdn:msgs", "", ex.getDescription());
            topPage();
        } catch (Exception ex) {
            logger.error(ex);
            reportErrorValidateFail("frmExportIsdn:msgs", "", "common.error.happened");
            topPage();
        }
    }

    private void joinIsdn(List<StockTransSerialDTO> stockTransSerialDTOs, StockTransSerialDTO stockTransSerial) {
        boolean isCon = false;
        for (StockTransSerialDTO serialDTO : stockTransSerialDTOs) {
            if (DataUtil.safeToLong(stockTransSerial.getFromSerial()) == DataUtil.safeToLong(serialDTO.getToSerial()) + 1) {
                serialDTO.setToSerial(stockTransSerial.getToSerial());
                serialDTO.setQuantity(serialDTO.getQuantity() + stockTransSerial.getQuantity());
                isCon = true;
                break;
            } else if (DataUtil.safeToLong(stockTransSerial.getToSerial()) == DataUtil.safeToLong(serialDTO.getFromSerial()) - 1) {
                serialDTO.setFromSerial(stockTransSerial.getFromSerial());
                serialDTO.setQuantity(serialDTO.getQuantity() + stockTransSerial.getQuantity());
                isCon = true;
                break;
            }
        }
        if (!isCon) {
            stockTransSerialDTOs.add(stockTransSerial);
        }
    }

    @Secured("@")
    public void acceptIsdn() {
        selectStockTransFull.setLstSerial(lstStockTransSerialDTO);
        topPage();
    }

    @Secured("@")
    public void doRemoveIsdn(int index) {
        lstStockTransSerialDTO.remove(index);
        isEnought = false;
//        isEnoughIsdn = false;
//        if(DataUtil.isNullOrEmpty(lstStockTransSerialDTO)){
//            selectStockTransFull.setHasSerial(false);
//        }
    }

    @Secured("@")
    public void doExportShop(VShopStaffDTO vShopStaffDTO) {
        if (vShopStaffDTO != null) {
            forSearch.setFromOwnerID(DataUtil.safeToLong(vShopStaffDTO.getOwnerId()));
            forSearch.setFromOwnerType(DataUtil.safeToLong(vShopStaffDTO.getOwnerType()));
        }
    }

    @Secured("@")
    public void doReceiveShop(VShopStaffDTO vShopStaffDTO) {
        if (vShopStaffDTO != null) {
            forSearch.setToOwnerID(DataUtil.safeToLong(vShopStaffDTO.getOwnerId()));
            forSearch.setToOwnerType(DataUtil.safeToLong(vShopStaffDTO.getOwnerType()));
        }
    }

    @Secured("@")
    public void doResetExportShop() {
        forSearch.setFromOwnerID(null);
        forSearch.setFromOwnerType(null);
    }

    @Secured("@")
    public void doResetReceiveShop() {
        forSearch.setToOwnerID(null);
        forSearch.setToOwnerType(null);
    }

    @Secured("@")
    public void changeTypeInput() {
        uploadedFile = null;
        byteContent = null;
        stockTransSerial.setFromSerial(null);
        stockTransSerial.setToSerial(null);
        attachFileName = "";
    }

    @Secured("@")
    public void downloadFileTemplate() {
        try {
            InputStream createStream = Faces.getResourceAsStream(Const.BRAS_IPPOOL.RESOURCE_TEMPLATE_PATH + Const.REPORT_TEMPLATE.INPUT_ISDN_TEMPLATE);
            HSSFWorkbook workbook = new HSSFWorkbook(createStream);
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"" + "INPUT_ISDN_TEMPLATE.xls" + "\"");
            externalContext.setResponseContentType("application/vnd.ms-excel");
            workbook.write(externalContext.getResponseOutputStream());
            facesContext.responseComplete();
            return;
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
        }
    }

    @Secured("@")
    public void fileUploadAction(FileUploadEvent event) {

        try {
            uploadedFile = event.getFile();
            BaseMessage message = validateFileUploadWhiteList(uploadedFile, ALOW_EXTENSION_EXCEL_TYPE_LIST, MAX_SIZE_5M);
            if (!message.isSuccess()) {
                uploadedFile = null;
                attachFileName = null;
                byteContent = null;
                LogicException ex = new LogicException(message.getErrorCode(), message.getKeyMsg());
                ex.setDescription(message.getDescription());
                throw ex;
            }
            byteContent = uploadedFile.getContents();
            attachFileName = uploadedFile.getFileName();
        } catch (LogicException ex) {
            reportError("", ex);
            topPage();
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
            logger.error(ex.getMessage(), ex);
            topPage();
        }


    }

    @Secured("@")
    public StreamedContent exportOrder() {
        try {
            if (DataUtil.isNullOrEmpty(listResultSearch)) {
                reportError("", ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.trans.search.validate.noOrderResult");
                topPage();
                return null;
            }
            FileExportBean fileExportBean = new FileExportBean();
            fileExportBean.setOutName(Const.EXPORT_FILE_NAME.LIST_NOTE);
            fileExportBean.setTempalatePath(FileUtil.getTemplatePath());
            fileExportBean.setOutPath(FileUtil.getOutputPath());
            fileExportBean.setTemplateName(Const.REPORT_TEMPLATE.DETAIL_LX_ISDN_TEMPLATE);
            fileExportBean.putValue("listNotes", listResultSearch);
            return FileUtil.exportToStreamed(fileExportBean);
//            FileUtil.exportFile(fileExportBean);
//            return fileExportBean.getExportFileContent();
        } catch (Exception ex) {
            logger.error(ex);
            reportError("frmExportIsdn:msgs", "", "common.error.happened");
            topPage();
        }
        return null;
    }

    @Secured("@")
    public StreamedContent exportDetailSerial() {
        try {
            if (DataUtil.isNullOrEmpty(lstStockTransSerialDTO)) {
                reportError("", ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.trans.search.validate.noIsdn");
                topPage();
                return null;
            }
            FileExportBean fileExportBean = new FileExportBean();
            fileExportBean.setOutName(Const.EXPORT_FILE_NAME.DETAIL_SERIAL_ISDN);
            fileExportBean.setTempalatePath(FileUtil.getTemplatePath());
            fileExportBean.setOutPath(FileUtil.getOutputPath());
            fileExportBean.setTemplateName(Const.REPORT_TEMPLATE.SERIAL_DETAIL_TEMPLATE);
            fileExportBean.putValue("state", selectStockTransFull.getStateName());
            fileExportBean.putValue("stockModelName", selectStockTransFull.getProdOfferName());
            fileExportBean.putValue("userCreate", staffDTO.getStaffCode());
            fileExportBean.putValue("dateCreate", getSysdateFromDB());
            fileExportBean.putValue("listStockTransSerials", lstStockTransSerialDTO);
            fileExportBean.putValue("totalReq", selectStockTransFull.getQuantity());
            return FileUtil.exportToStreamed(fileExportBean);
//            FileUtil.exportFile(fileExportBean);
//            return fileExportBean.getExportFileContent();
        } catch (Exception ex) {
            logger.error(ex);
            reportError("frmExportIsdn:msgs", "", "common.error.happened");
            topPage();
        }
        return null;
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
            stockTransDTO.setStockTransType(Const.STOCK_TRANS_TYPE.ISDN);
            stockTransDTO.setActionType(Const.STOCK_TRANS_ACTION_TYPE.NOTE);
            stockTransDTO.setStockTransStatus(Const.STOCK_TRANS_TYPE.EXPORT);
            stockTransDTO.setStockTransType(Const.STOCK_TRANS_TYPE.ISDN);
            stockTransDTO.setActionCode(exportCode);
            stockTransDTO.setStockTransActionId(null);
            return exportStockTransDetail(stockTransDTO, lstGoods);
        } catch (LogicException ex) {
            topReportError("", ex);
            topPage();
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
            topPage();
        }
        return null;
    }

//    @Secured("@")
//    public StreamedContent exportNote() {
//        try {
//            if (DataUtil.isNullOrEmpty(lstGoods)) {
//                reportError("", ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.trans.search.validate.noProduct");
//                topPage();
//                return null;
//            }
//            if (DataUtil.isNullObject(selectStockTransFull.getStockTransId())
//                    || DataUtil.isNullOrEmpty(lstStockTransSerialDTO)) {
//                reportError("", ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.trans.search.validate.noserialresult");
//                topPage();
//                return null;
//            }
//            StockTransDTO stockTransDTO = stockTransService.findOne(selectStockTransFull.getStockTransId());
//            ShopDTO from = shopService.findOne(selectStockTransFull.getFromOwnerId());
//            ShopDTO to = shopService.findOne(selectStockTransFull.getToOwnerId());
//            ReasonDTO reason = reasonService.findOne(selectStockTransFull.getReasonId());
//            FileExportBean fileExportBean = new FileExportBean();
//            fileExportBean.setOutName(Const.EXPORT_FILE_NAME.PX_ISDN);
//            fileExportBean.setTempalatePath(FileUtil.getTemplatePath());
//            fileExportBean.setOutPath(FileUtil.getOutputPath());
//            fileExportBean.setTemplateName(Const.REPORT_TEMPLATE.PX_ISDN_TEMPLATE);
//            fileExportBean.putValue("fromOwnerName", from.getName());
//            fileExportBean.putValue("fromOwnerAddress", from.getAddress());
//            fileExportBean.putValue("dateCreate", getSysdateFromDB());
//            fileExportBean.putValue("toOwnerName", to.getName());
//            fileExportBean.putValue("toOwnerAddress", to.getAddress());
//            fileExportBean.putValue("reasonName", reason.getReasonName());
//            fileExportBean.putValue("fromOwnerName", from.getName());
//            fileExportBean.putValue("actionCode", selectStockTransFull.getActionCode());
//            fileExportBean.putValue("actionCodeFull", exportCode);
//            fileExportBean.putValue("lstStockTransFull", lstGoods);
//            FileUtil.exportFile(fileExportBean);
//            return fileExportBean.getExportFileContent();
//        } catch (Exception ex) {
//            logger.error(ex);
//            reportError("frmExportIsdn:msgs", "", "common.error.happened");
//            topPage();
//        }
//        return null;
//    }

    @Secured("@")
    public void validateCreateExportNote() {
        if (writeOffice) {
            try {
                signOfficeTag.validateVofficeAccount();
            } catch (LogicException e) {
                reportError("", e.getKeyMsg(), e);
                topPage();
            } catch (Exception ex) {
                reportError("frmExportIsdn:msgs", "", "common.error.happened", ex);
                topPage();
            }
        }
    }

    @Secured("@")
    public void createExportNote() {
        try {
            //validate
            StockTransActionDTO stockTransActionDTO = orderStockTag.getStockTransActionDTO();
            stockTransActionDTO.setActionCode(exportCode);
            StockTransDTO stockTransDTO = orderStockTag.getStockTransDTO();
            stockTransDTO.setStockTransType(Const.STOCK_TRANS_TYPE.ISDN);
            stockTransDTO.setStatus(Const.STOCK_TRANS_STATUS.EXPORT_NOTE);
            stockTransDTO.setTotalAmount(null);
            if (writeOffice) {
                SignOfficeDTO signOfficeDTO = signOfficeTag.validateVofficeAccount();
                stockTransDTO.setUserName(signOfficeDTO.getUserName());
                stockTransDTO.setPassWord(signOfficeDTO.getPassWord());
                stockTransDTO.setSignFlowId(signOfficeDTO.getSignFlowId());
                stockTransDTO.setSignVoffice(writeOffice);
                stockTransActionDTO.setSignCaStatus(Const.SIGN_VOFFICE);
            }
            List<StockTransDetailDTO> stockTransDetailDTOs = Lists.newArrayList();
            for (StockTransFullDTO stockTransFullDTO : lstGoods) {
                StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
                stockTransDetailDTO.setStockTransDetailId(stockTransFullDTO.getStockTransDetailId());
                stockTransDetailDTO.setProdOfferId(stockTransFullDTO.getProdOfferId());
                stockTransDetailDTO.setStateId(stockTransFullDTO.getStateId());
                stockTransDetailDTO.setQuantity(stockTransFullDTO.getQuantity());
                stockTransDetailDTO.setLstStockTransSerial(stockTransFullDTO.getLstSerial());
                stockTransDetailDTO.setTableName(stockTransFullDTO.getTableName());
                stockTransDetailDTO.setProdOfferTypeId(stockTransFullDTO.getProductOfferTypeId());
                stockTransDetailDTO.setLstStockTransSerial(stockTransFullDTO.getLstSerial());
                stockTransDetailDTOs.add(stockTransDetailDTO);
            }
            stockTransActionDTO.setStatus(Const.STOCK_TRANS_STATUS.EXPORT_ORDER);
            stockTransActionDTO.setNote(null);
            BaseMessage message = executeStockTransService.executeStockTrans(Const.STOCK_TRANS.EXPORT_NOTE, Const.STOCK_TRANS_TYPE.ISDN, stockTransDTO, stockTransActionDTO, stockTransDetailDTOs, requiredRoleMap);
            if (!DataUtil.isNullOrEmpty(message.getErrorCode())) {
                throw new LogicException(message.getErrorCode(), message.getKeyMsg());
            }
            isDone = true;
            reportSuccess("frmExportIsdn:msgs", "export.note.isdn.create.success");
            focusElementWithCursor(".ipExportCodeField");
            topPage();
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportErrorValidateFail("frmExportIsdn:msgs", "", ex.getDescription());
            focusElementWithCursor(".ipExportCodeField");
            topPage();
        } catch (Exception ex) {
            logger.error(ex);
            reportErrorValidateFail("frmExportIsdn:msgs", "", "common.error.happened");
            topPage();
        }
    }

    private Date getSysdate() {
        try {
            return optionSetValueService.getSysdateFromDB(true);
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
        }
        return new Date();
    }

    private boolean validateVoffice(Long stockTransActionId) {
        try {
            StockTransActionDTO transActionDTO = stockTransActionService.findOne(stockTransActionId);
            if (DataUtil.safeEqual(Const.SIGN_VOFFICE, transActionDTO.getSignCaStatus())) {
                stockTransVofficeService.doSignedVofficeValidate(transActionDTO);
            }
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportErrorValidateFail("frmExportIsdn:msgs", "", ex.getDescription());
            topPage();
            return false;
        } catch (Exception ex) {
            logger.error(ex);
            reportErrorValidateFail("frmExportIsdn:msgs", "", "common.error.happened");
            topPage();
            return false;
        }
        return true;
    }

    private List<String> validateFileUpload() {
        List<String> result = Lists.newArrayList();
        try {
            if (DataUtil.isNullObject(uploadedFile) || DataUtil.isNullObject(byteContent)) {
                reportErrorValidateFail("frmInputIsdn:msgInputIsdn", "", "mn.stock.status.isdn.update.file.noselect");
                focusElementByRawCSSSlector(".fileAtt");
                focusElementWithCursor(".fileAtt");
                return Lists.newArrayList();
            }
            processingFile = new ExcellUtil(uploadedFile, byteContent);
            Sheet sheetProcess = processingFile.getSheetAt(0);
            int totalRow = processingFile.getTotalRowAtSheet(sheetProcess);
            //TODO validate tong so dong
            for (int i = 0; i <= totalRow; i++) {
                Row row = sheetProcess.getRow(i);
                if (DataUtil.isNullObject(row)) {
                    continue;
                }
                String cellContent = processingFile.getStringValue(row.getCell(0));
                if (!DataUtil.isNullObject(cellContent)) {
                    cellContent = cellContent.trim().replaceAll("^0+", "");
                }
                if (!DataUtil.isNumber(cellContent)) {
                    continue;
                }
                result.add(cellContent);
            }
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            result = Lists.newArrayList();
            reportError("", ex);
        } catch (Exception ex) {
//            ex.printStackTrace();
            logger.error(ex.getMessage(), ex);
            result = Lists.newArrayList();
            reportErrorValidateFail("frmInputIsdn:msgInputIsdn", "", "mn.stock.status.isdn.export.nodata");
//            reportError("", "common.error.happened", ex);
        } finally {

        }
        return result;
    }

    public Date getCurrentDate() {
        return Calendar.getInstance().getTime();
    }

    public boolean isInfoOrderDetail() {
        return infoOrderDetail;
    }

    public void setInfoOrderDetail(boolean infoOrderDetail) {
        this.infoOrderDetail = infoOrderDetail;
    }

    public boolean getWriteOffice() {
        return writeOffice;
    }

    public void setWriteOffice(boolean writeOffice) {
        this.writeOffice = writeOffice;
    }

    public OrderStockTagNameable getOrderStockTag() {
        return orderStockTag;
    }

    public void setOrderStockTag(OrderStockTagNameable orderStockTag) {
        this.orderStockTag = orderStockTag;
    }

    public SignOfficeTagNameable getSignOfficeTag() {
        return signOfficeTag;
    }

    public void setSignOfficeTag(SignOfficeTagNameable signOfficeTag) {
        this.signOfficeTag = signOfficeTag;
    }

    public VStockTransDTO getForSearch() {
        return forSearch;
    }

    public void setForSearch(VStockTransDTO forSearch) {
        this.forSearch = forSearch;
    }

    public List<OptionSetValueDTO> getOptionSetValueDTOsList() {
        return optionSetValueDTOsList;
    }

    public void setOptionSetValueDTOsList(List<OptionSetValueDTO> optionSetValueDTOsList) {
        this.optionSetValueDTOsList = optionSetValueDTOsList;
    }

    public int getLimitAutoComplete() {
        return limitAutoComplete;
    }

    public void setLimitAutoComplete(int limitAutoComplete) {
        this.limitAutoComplete = limitAutoComplete;
    }

    public ShopInfoNameable getShopInfoTagExport() {
        return shopInfoTagExport;
    }

    public void setShopInfoTagExport(ShopInfoNameable shopInfoTagExport) {
        this.shopInfoTagExport = shopInfoTagExport;
    }

    public ShopInfoNameable getShopInfoTagReceive() {
        return shopInfoTagReceive;
    }

    public void setShopInfoTagReceive(ShopInfoNameable shopInfoTagReceive) {
        this.shopInfoTagReceive = shopInfoTagReceive;
    }

    public RequiredRoleMap getRequiredRoleMap() {
        return requiredRoleMap;
    }

    public void setRequiredRoleMap(RequiredRoleMap requiredRoleMap) {
        this.requiredRoleMap = requiredRoleMap;
    }

    public List<VStockTransDTO> getListResultSearch() {
        return listResultSearch;
    }

    public void setListResultSearch(List<VStockTransDTO> listResultSearch) {
        this.listResultSearch = listResultSearch;
    }

    public List<StockTransFullDTO> getLstGoods() {
        return lstGoods;
    }

    public void setLstGoods(List<StockTransFullDTO> lstGoods) {
        this.lstGoods = lstGoods;
    }

    public StockTransSerialDTO getStockTransSerial() {
        return stockTransSerial;
    }

    public void setStockTransSerial(StockTransSerialDTO stockTransSerial) {
        this.stockTransSerial = stockTransSerial;
    }

    public int getInputIsdnType() {
        return inputIsdnType;
    }

    public void setInputIsdnType(int inputIsdnType) {
        this.inputIsdnType = inputIsdnType;
    }

    public StockTransFullDTO getSelectStockTransFull() {
        return selectStockTransFull;
    }

    public void setSelectStockTransFull(StockTransFullDTO selectStockTransFull) {
        this.selectStockTransFull = selectStockTransFull;
    }

    public List<StockTransSerialDTO> getLstStockTransSerialDTO() {
        return lstStockTransSerialDTO;
    }

    public void setLstStockTransSerialDTO(List<StockTransSerialDTO> lstStockTransSerialDTO) {
        this.lstStockTransSerialDTO = lstStockTransSerialDTO;
    }

    public boolean isInputByFile() {
        return inputByFile;
    }

    public void setInputByFile(boolean inputByFile) {
        this.inputByFile = inputByFile;
    }

    public boolean getIsDone() {
        return isDone;
    }

    public void setIsDone(boolean isDone) {
        this.isDone = isDone;
    }

    public String getExportCode() {
        return exportCode;
    }

    public void setExportCode(String exportCode) {
        this.exportCode = exportCode;
    }

    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public String getAttachFileName() {
        return attachFileName;
    }

    public void setAttachFileName(String attachFileName) {
        this.attachFileName = attachFileName;
    }

    public StockTransDTO getStockTransCur() {
        return stockTransCur;
    }

    public void setStockTransCur(StockTransDTO stockTransCur) {
        this.stockTransCur = stockTransCur;
    }

    public boolean getIsEnought() {
        return isEnought;
    }

    public boolean getHasRoleSignCa() {
        return hasRoleSignCa;
    }

    public void setHasRoleSignCa(boolean hasRoleSignCa) {
        this.hasRoleSignCa = hasRoleSignCa;
    }

    public void setIsEnought(boolean isEnought) {
        this.isEnought = isEnought;
    }
}

