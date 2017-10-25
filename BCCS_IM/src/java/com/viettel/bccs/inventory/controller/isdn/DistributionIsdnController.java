package com.viettel.bccs.inventory.controller.isdn;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.fw.controller.InventoryController;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.common.ExcellUtil;
import com.viettel.bccs.inventory.common.model.FileExportBean;
import com.viettel.bccs.inventory.common.util.FileUtil;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.OptionSetValueService;
import com.viettel.bccs.inventory.service.ReasonService;
import com.viettel.bccs.inventory.service.ShopService;
import com.viettel.bccs.inventory.service.StockNumberService;
import com.viettel.bccs.inventory.tag.ShopInfoNameable;
import com.viettel.bccs.inventory.tag.StaffInfoNameable;
import com.viettel.bccs.inventory.tag.impl.ShopInfoTag;
import com.viettel.bccs.inventory.tag.impl.StaffInfoTag;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.BundleUtil;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.RequiredRoleMap;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.web.common.annotation.Security;
import com.viettel.web.common.security.CustomAuthenticationProvider;
import org.apache.commons.collections.map.HashedMap;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.omnifaces.util.Faces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Created by tuyendv8 on 11/19/2015.
 */
@Component
@Scope("view")
@ManagedBean
public class DistributionIsdnController extends InventoryController {

    @Autowired
    private ReasonService reasonService;
    @Autowired
    private ShopInfoNameable shopReceiveInfoTag;
    @Autowired
    private StaffInfoNameable staffReceiveInfoTag;
    @Autowired
    private ShopInfoNameable shopGetInfoTag;
    @Autowired
    private StaffInfoNameable staffGetInfoTag;
    @Autowired
    OptionSetValueService optionSetValueService;
    @Autowired
    StockNumberService stockNumberService;
    @Autowired
    ShopService shopService;
    private RequiredRoleMap requiredRoleMap;


    private String distribution;
    private static final String SELECT_FROM_FILE = "selectFromFile";
    private static final String ENTERED_DIGITAL_RANGES = "enteredDigitalRanges";
    private boolean renderedFromFile;
    private List<ReasonDTO> listReason = Lists.newArrayList();
    private List<OptionSetValueDTO> listService = Lists.newArrayList();
    private List<Object[]> listIsdnPreview = Lists.newArrayList();
    private static final String FILE_TEMPLATE_PATH = "distributeIsdnPattern.xls";
    private UploadedFile uploadedFile;
    private byte[] byteContent;
    private String attachFileName;
    private boolean hasFileError = false;
    private Workbook bookError;
    private String typeStock;
    private int tabIndex = 0;
    private InputDistributeByRangeDTO inputByRange;
    private InputDistributeByRangeDTO inputByRangeForDistribute;
    private InputDistributeByFileDTO inputByFile;
    private StaffDTO currentStaff = BccsLoginSuccessHandler.getStaffDTO();
    private boolean isPreviewFile = false;
    private static final String CHANNEL_TYPE_CTV = "8";
    private HashMap<String, String> mapError = new HashMap<>();

    @PostConstruct
    public void init() {
        try {
            //khoi tao quyen
            inputByRange = new InputDistributeByRangeDTO();
            inputByFile = new InputDistributeByFileDTO();
            requiredRoleMap = CustomAuthenticationProvider.createRequiredRoleMap(Const.PERMISION.ROLE_DISTRIBUTION_NICE_ISDN,
                    Const.PERMISION.ROLE_DISTRIBUTION_NORMAL_ISDN, Const.PERMISION.ROLE_DISTRIBUTION);
            shopReceiveInfoTag.initShopMangeIsdnTrans(this, BccsLoginSuccessHandler.getStaffDTO().getShopId().toString(), CHANNEL_TYPE_CTV, requiredRoleMap, Const.MODE_SHOP.ISDN_DSTRT);
            shopGetInfoTag.initShopMangeIsdnTrans(this, BccsLoginSuccessHandler.getStaffDTO().getShopId().toString(), CHANNEL_TYPE_CTV, requiredRoleMap, Const.MODE_SHOP.ISDN_DSTRT);
            staffGetInfoTag.initStaffManageIsdn(this, BccsLoginSuccessHandler.getStaffDTO().getShopId().toString(), "", Const.MODE_SHOP.ISDN_DSTRT, null);
            staffReceiveInfoTag.initStaffManageIsdn(this, BccsLoginSuccessHandler.getStaffDTO().getShopId().toString(), "", Const.MODE_SHOP.ISDN_DSTRT, null);
            listReason = reasonService.getLsReasonByType(Const.REASON_TYPE.DISTRIBUTE_ISDN_TYPE);
            listService = optionSetValueService.getByOptionSetCode(Const.TELCO_FOR_NUMBER);
            Collections.sort(listService, new Comparator<OptionSetValueDTO>() {
                public int compare(OptionSetValueDTO o1, OptionSetValueDTO o2) {
                    return Long.valueOf(o1.getValue()).compareTo(Long.valueOf(o2.getValue()));
                }
            });
            distribution = "selectFromFile";
            renderedFromFile = true;
            focusElementWithCursor(".typeServiceTxt");
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportErrorValidateFail("frmDistributeNumber:messages", "", "common.error.happen");
        }
    }

    @Secured("@")
    public void changeDistribution() {
        if (DataUtil.safeEqual(distribution, "selectFromFile")) {
            renderedFromFile = true;
        } else {
            renderedFromFile = false;
        }
    }

    private boolean validatePreview() {
        boolean valid = true;
        if (DataUtil.isNullObject(inputByRange.getTelecomServiceId())) {
            reportError("frmDistributeNumber:messages", "", "ws.service.type.not.null");
            focusElementByRawCSSSlector(".typeServiceTxt");
            valid = false;
        }
        if (DataUtil.isNullObject(inputByRange.getStartRange())) {
            reportError("frmDistributeNumber:messages", "", "from.number.require.msg");
            focusElementByRawCSSSlector(".fromNumberTxt");
            valid = false;
        }
        if (!DataUtil.isNullObject(inputByRange.getStartRange()) && (inputByRange.getStartRange().length() < 8 || inputByRange.getStartRange().length() > 11)) {
            reportError("frmDistributeNumber:messageRange", "", "mn.stock.status.isdn.from.length");
            focusElementByRawCSSSlector(".fromNumberTxt");
            valid = false;
        }
        if (DataUtil.isNullObject(inputByRange.getEndRange())) {
            reportError("frmDistributeNumber:messages", "", "to.number.require.msg");
            focusElementByRawCSSSlector(".toNumberTxt");
            valid = false;
        }
        if (!DataUtil.isNullObject(inputByRange.getEndRange()) && (inputByRange.getEndRange().length() < 8 || inputByRange.getEndRange().length() > 11)) {
            reportError("frmDistributeNumber:messageRange", "", "mn.stock.status.isdn.to.length");
            focusElementByRawCSSSlector(".toNumberTxt");
            valid = false;
        }
        if (!DataUtil.isNullObject(inputByRange.getStartRange()) && !DataUtil.isNumber(inputByRange.getStartRange())) {
            reportError("frmDistributeNumber:messages", "", "from.number.invalid.msg");
            focusElementByRawCSSSlector(".fromNumberTxt");
            valid = false;
        }
        if (!DataUtil.isNullObject(inputByRange.getEndRange()) && !DataUtil.isNumber(inputByRange.getEndRange())) {
            reportError("frmDistributeNumber:messages", "", "to.number.invalid.msg");
            focusElementByRawCSSSlector(".fromNumberTxt");
            valid = false;
        }
        if (!DataUtil.isNullObject(inputByRange.getStartRange()) && !DataUtil.isNullObject(inputByRange.getEndRange())) {
            if (inputByRange.getStartRange().length() != inputByRange.getEndRange().length()) {
                reportError("frmDistributeNumber:messages", "", "from.to.number.length.msg");
                focusElementByRawCSSSlector(".toNumberTxt");
                valid = false;
            }

            Long from = DataUtil.safeToLong(inputByRange.getStartRange());
            Long to = DataUtil.safeToLong(inputByRange.getEndRange());
            if (to < from) {
                reportError("frmDistributeNumber:messages", "", "from.to.number.invalid.msg");
                focusElementByRawCSSSlector(".toNumberTxt");
                valid = false;
            }
            if (to - from + 1 > 1000000) {
                reportError("frmDistributeNumber:messages", "", "ws.number.per.range.over");
                focusElementByRawCSSSlector(".toNumberTxt");
                valid = false;
            }
        }
        if (DataUtil.isNullObject(inputByRange.getToOwnerId())) {
            reportError("frmDistributeNumber:messageRange", "", "receive.stock.code.require.msg");
            focusElementByRawCSSSlector(".receiptStock");
            return false;
        }
        if (DataUtil.isNullObject(inputByRange.getToOwnerId()) || DataUtil.isNullObject(inputByRange.getToOwnerType())) {
            reportError("frmDistributeNumber:messageRange", "", "mn.stock.limit.store.code.require.msg");
            return false;
        }
        return valid;
    }

    private boolean validateDistribute() {
        if (DataUtil.isNullObject(inputByRange.getTelecomServiceId())) {
            reportError("frmDistributeNumber:messageRange", "", "ws.service.type.not.null");
            focusElementByRawCSSSlector(".typeServiceTxt");
            return false;
        }
        if (DataUtil.isNullObject(inputByRange.getStartRange())) {
            reportError("frmDistributeNumber:messageRange", "", "from.number.require.msg");
            focusElementByRawCSSSlector(".fromNumberTxt");
            return false;
        }
        if (!DataUtil.isNullObject(inputByRange.getStartRange()) && (inputByRange.getStartRange().length() < 8 || inputByRange.getStartRange().length() > 11)) {
            reportError("frmDistributeNumber:messageRange", "", "mn.stock.status.isdn.from.length");
            focusElementByRawCSSSlector(".fromNumberTxt");
            return false;
        }
        if (!DataUtil.isNumber(inputByRange.getStartRange())) {
            reportError("frmDistributeNumber:messageRange", "", "from.number.invalid.msg");
            focusElementByRawCSSSlector(".fromNumberTxt");
            return false;
        }
        if (DataUtil.isNullObject(inputByRange.getEndRange())) {
            reportError("frmDistributeNumber:messageRange", "", "to.number.require.msg");
            focusElementByRawCSSSlector(".toNumberTxt");
            return false;
        }
        if (!DataUtil.isNullObject(inputByRange.getEndRange()) && (inputByRange.getEndRange().length() < 8 || inputByRange.getEndRange().length() > 11)) {
            reportError("frmDistributeNumber:messageRange", "", "mn.stock.status.isdn.to.length");
            focusElementByRawCSSSlector(".toNumberTxt");
            return false;
        }
        if (!DataUtil.isNumber(inputByRange.getEndRange())) {
            reportError("frmDistributeNumber:messageRange", "", "to.number.invalid.msg");
            focusElementByRawCSSSlector(".fromNumberTxt");
            return false;
        }
        if (inputByRange.getStartRange().length() != inputByRange.getEndRange().length()) {
            reportError("frmDistributeNumber:messageRange", "", "from.to.number.length.msg");
            focusElementByRawCSSSlector(".toNumberTxt");
            return false;
        }
        Long from = DataUtil.safeToLong(inputByRange.getStartRange());
        Long to = DataUtil.safeToLong(inputByRange.getEndRange());
        if (to < from) {
            reportError("frmDistributeNumber:messageRange", "", "from.to.number.invalid.msg");
            focusElementByRawCSSSlector(".toNumberTxt");
            return false;
        }
        if (to - from + 1 > 1000000) {
            reportError("frmDistributeNumber:messages", "", "ws.number.per.range.over");
            focusElementByRawCSSSlector(".toNumberTxt");
            return false;
        }
        if (DataUtil.isNullObject(inputByRange.getFromOwnerType())) {
            reportError("frmDistributeNumber:messageRange", "", "receive.stock.require.msg");
            focusElementByRawCSSSlector(".cbxTypeReceiveStock");
            return false;
        }
        if (DataUtil.isNullObject(inputByRange.getToOwnerId())) {
            reportError("frmDistributeNumber:messageRange", "", "receive.stock.code.require.msg");
            focusElementByRawCSSSlector(".receiptStock");
            return false;
        }
        if (DataUtil.isNullObject(inputByRange.getToOwnerId()) || DataUtil.isNullObject(inputByRange.getToOwnerType())) {
            reportError("frmDistributeNumber:messageRange", "", "mn.stock.limit.store.code.require.msg");
            return false;
        }
        if (DataUtil.isNullObject(inputByRange.getReasonId())) {
            reportError("frmDistributeNumber:messageRange", "", "export.order.by.reason.not.blank");
            focusElementByRawCSSSlector(".cbxReason2");
            return false;
        }
        if (!DataUtil.isNullObject(inputByRange.getNote()) && inputByRange.getNote().length() > 500) {
            reportError("frmDistributeNumber:messageRange", "", "stockTrans.validate.note.overLength");
            focusElementByRawCSSSlector(".cbxReason");
            return false;
        }
//        if (DataUtil.isNullOrEmpty(listIsdnPreview)) {
//            reportError("frmDistributeNumber:messageRange", "", "distribute.list.range.invalid");
//            return false;
//        }
        return true;
    }

    private boolean validatePreviewByFile() {
        boolean valid = true;
        if (DataUtil.isNullObject(inputByFile.getTelecomServiceId())) {
            reportError("frmDistributeNumber:messages", "", "ws.service.type.not.null");
            focusElementByRawCSSSlector(".typeServiceTxt");
            valid = false;
        }
        if (DataUtil.isNullObject(byteContent)) {
            reportError("frmDistributeNumber:messages", "", "mn.stock.status.isdn.update.file.noselect");
            focusElementByRawCSSSlector(".outputAttachFile");
            valid = false;
        }
        return valid;
    }

    private boolean validateDistributeByFile() {
        boolean valid = true;
        if (DataUtil.isNullObject(inputByFile.getTelecomServiceId())) {
            reportError("frmDistributeNumber:messages", "", "ws.service.type.not.null");
            focusElementByRawCSSSlector(".typeServiceTxt");
            valid = false;
        }
        if (DataUtil.isNullObject(byteContent)) {
            reportError("frmDistributeNumber:messages", "", "mn.stock.status.isdn.update.file.noselect");
            focusElementByRawCSSSlector(".outputAttachFile");
            valid = false;
        }
        if (DataUtil.isNullObject(inputByFile.getToOwnerType())) {
            reportError("frmDistributeNumber:messages", "", "stock.type.not.null");
            focusElementByRawCSSSlector(".cbxTypeStock");
            valid = false;
        }
        if (DataUtil.isNullObject(inputByFile.getReasonId())) {
            reportError("frmDistributeNumber:messages", "", "export.order.by.reason.not.blank");
            focusElementByRawCSSSlector(".cbxReason");
            return false;
        }
        if (!DataUtil.isNullObject(inputByFile.getNote()) && inputByFile.getNote().length() > 500) {
            reportError("frmDistributeNumber:messages", "", "stockTrans.validate.note.overLength");
            focusElementByRawCSSSlector(".notesFile");
            valid = false;
        }
        return valid;
    }

    @Security
    public void previewDistributeNumberByFile() {
        try {
            if (validatePreviewByFile()) {
                listIsdnPreview = Lists.newArrayList();
                hasFileError = false;
                BaseMessage message = validateFileUploadWhiteList(uploadedFile, ALOW_EXTENSION_EXCEL_TYPE_LIST, MAX_SIZE_5M);
                if (!message.isSuccess()) {
                    byteContent = null;
                    attachFileName = null;
                    LogicException ex = new LogicException(message.getErrorCode(), message.getKeyMsg());
                    ex.setDescription(message.getDescription());
                    throw ex;
                }
                ExcellUtil excellUtil = new ExcellUtil(uploadedFile, byteContent);
                Sheet sheetProcess = excellUtil.getSheetAt(0);
                int totalRow = excellUtil.getTotalRowAtSheet(sheetProcess);
//                if (totalRow > 1000) {
//                    reportError("frmDistributeNumber:messages", "", "distribute.number.over.by.file");
//                    return;
//                }
                inputByFile.setFromOwnerId(currentStaff.getShopId());
                inputByFile.setFromShopPath(currentStaff.getShopPath());
                List<Object[]> listIsdn = readExcel(byteContent);
                if (!DataUtil.isNullOrEmpty(listIsdn)) {
                    listIsdnPreview = DataUtil.defaultIfNull(stockNumberService.previewDistriButeNumberByFile(listIsdn, inputByFile, requiredRoleMap), Lists.newArrayList());
                }
                Gson gson = new Gson();
                Type type = new TypeToken<VShopStaffDTO>() {
                }.getType();
                if (!DataUtil.isNullOrEmpty(listIsdnPreview)) {
                    for (int i = 0; i < listIsdnPreview.size(); i++) {
                        Object[] obDes = new Object[13];
                        Object[] obSrc = listIsdnPreview.get(i);
                        System.arraycopy(obSrc, 0, obDes, 0, obSrc.length);
                        for (Object[] ob : listIsdn) {
                            if (ob[0].toString().equals(obDes[3].toString())) {
                                VShopStaffDTO toOwner = gson.fromJson(ob[1].toString(), type);
                                obDes[12] = toOwner.getOwnerCode() + "-" + toOwner.getOwnerName();
                                break;
                            }
                        }
                        listIsdnPreview.set(i, obDes);
                    }
                }
//                listIsdnPreview = gson.fromJson(map.get("RESULT"), type);
//
//                List<String> listPreviewed = Lists.newArrayList();
//                if (!DataUtil.isNullOrEmpty(listIsdnPreview)) {
//                    for (Object[] ob : listIsdnPreview) {
//                        long fromIsdn = DataUtil.safeToLong(ob[3]);
//                        long toIsdn = DataUtil.safeToLong(ob[4]);
//                        for (long i = fromIsdn; i <= toIsdn; i++) {
//                            listPreviewed.add(i + "");
//                        }
//                    }
//                }
//                if (DataUtil.isNullOrEmpty(listPreviewed) || listPreviewed.size() < totalRow - 1) {
//                    hasFileError = true;
//                }
                List<String[]> listError = Lists.newArrayList();
                int totalRowNotNull = 0;
                int totalRowFind = 0;
                for (int i = 1; i <= totalRow; i++) {
                    Row row = sheetProcess.getRow(i);
                    if (row == null) {
                        continue;
                    }
                    String isdn = excellUtil.getStringValue(row.getCell(0));
                    String stockCode = excellUtil.getStringValue(row.getCell(1));
                    if (!DataUtil.isNullObject(isdn) || !DataUtil.isNullObject(stockCode)) {
                        totalRowNotNull++;
                    } else {
                        continue;
                    }
                    if (mapError.containsKey(i + "")) {
                        hasFileError = true;
                        String[] error = new String[3];
                        error[0] = isdn;
                        error[1] = stockCode;
                        error[2] = BundleUtil.getText(mapError.get(i + ""));
                        listError.add(error);
                        continue;
                    }
                    totalRowFind++;
//                    if (!DataUtil.isNullOrEmpty(listPreviewed) && listPreviewed.contains(isdn)) {
//                        continue;
//                    }
//                    String[] error = new String[3];
//                    for (int index = 0; index < row.getPhysicalNumberOfCells(); index++) {
//                        Cell cell = row.getCell(index);
//                        if (cell.getColumnIndex() == 0) {
//                            error[0] = excellUtil.getStringValue(cell);
//                        } else if (cell.getColumnIndex() == 1) {
//                            error[1] = excellUtil.getStringValue(cell);
//                        }
//                    }
//                    error[2] = BundleUtil.getText("error.number.isdn.not.exist");
//                    listError.add(error);
                }
                try {
                    FileExportBean bean = new FileExportBean();
                    bean.setTempalatePath(FileUtil.getTemplatePath());
                    bean.setTemplateName("distributeIsdnPatternError.xls");
                    bean.putValue("lstData", listError);
                    bookError = FileUtil.exportWorkBook(bean);
                } catch (Exception ex) {
                    reportError("", "common.error.happen", ex);
                    topPage();
                }
                isPreviewFile = true;
                reportSuccess("frmDistributeNumber:messages", "distribute.preview.msg", listIsdnPreview.size(), totalRowNotNull);
            }
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("frmDistributeNumber:messages", ex);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportErrorValidateFail("frmDistributeNumber:messages", "", "common.error.happen");
        }
    }

    public List<Object[]> readExcel(byte[] byteContent) throws LogicException, Exception {
        mapError.clear();
        Workbook workbook = WorkbookFactory.create(new ByteArrayInputStream(byteContent));
        List<Object[]> listIsdnValidToDistribute = Lists.newArrayList();
        ExcellUtil excellUtil = new ExcellUtil(uploadedFile, byteContent);
        Sheet sheetProcess = workbook.getSheetAt(0);
        int totalRow = sheetProcess.getPhysicalNumberOfRows();
        Gson gson = new Gson();
        Type type = new TypeToken<VShopStaffDTO>() {
        }.getType();
        List<String[]> listRowIsdn = Lists.newArrayList();
        if (totalRow < 2) {
            throw new LogicException("", "distribute.number.file.invalid");
        }
        int rowHasData = 0;
        for (int i = 1; i <= totalRow; i++) {
            Row row = sheetProcess.getRow(i);
            if (row == null) {
                continue;
            }
            String isdn = excellUtil.getStringValue(row.getCell(0)).trim();
            String stockCode = excellUtil.getStringValue(row.getCell(1)).trim();
            if (DataUtil.isNullObject(isdn) && DataUtil.isNullObject(stockCode)) {
                continue;
            }
            rowHasData++;
            if (DataUtil.isNullObject(stockCode)) {
                mapError.put(i + "", "distribute.error." + 5);
                continue;
            }
            VShopStaffDTO toOwner;
            toOwner = shopService.getShopByCodeForDistribute(stockCode, currentStaff.getShopId(), inputByFile.getToOwnerType());
            if (DataUtil.isNullObject(toOwner)) {
                mapError.put(i + "", "distribute.error." + 6);
                continue;
            }
            if (containIsdn(listRowIsdn, isdn)) {
                mapError.put(i + "", "distribute.error." + 7);
                continue;
            }

            String[] rowStringIsdn = new String[3];
            rowStringIsdn[0] = i + "";
            rowStringIsdn[1] = isdn;
            rowStringIsdn[2] = gson.toJson(toOwner, type);
            listRowIsdn.add(rowStringIsdn);
        }
        if (rowHasData <= 0) {
            throw new LogicException("", "distribute.number.file.invalid");
        }
        if (rowHasData > Const.MAX_SIZE_ROW_DISTRIBUTE) {
            throw new LogicException("", "distribute.number.over.by.file", Const.MAX_SIZE_ROW_DISTRIBUTE);
        }
        Map<String, String> checkIsdn = new HashedMap();
        if (!DataUtil.isNullOrEmpty(listRowIsdn)) {
            checkIsdn = stockNumberService.checkIsdnBeforeDistribute(listRowIsdn, currentStaff.getShopId().toString(), currentStaff.getShopPath(), inputByFile.getTelecomServiceId().toString(), requiredRoleMap);
        }
        Type typeStrings = new TypeToken<String[]>() {
        }.getType();
        for (String rowKey : checkIsdn.keySet()) {
            String[] isdnChecked = gson.fromJson(checkIsdn.get(rowKey), typeStrings);
            if (isdnChecked[2] != null) {
                mapError.put(rowKey, isdnChecked[2]);
            } else {
                Object[] ob = new Object[4];
                ob[0] = isdnChecked[0];
                ob[1] = isdnChecked[1];
                ob[2] = rowKey;
                ob[3] = isdnChecked[3];
                listIsdnValidToDistribute.add(ob);
            }
        }
        return listIsdnValidToDistribute;
    }


    private boolean containIsdn(List<String[]> listIsdnValidToDistribute, String isdn) {
        for (int i = 0; i < listIsdnValidToDistribute.size(); i++) {
            if (listIsdnValidToDistribute.get(i)[1].toString().equals(isdn)) {
                return true;
            }
        }
        return false;
    }

    public VShopStaffDTO findStockCode(String stockCode, List<VShopStaffDTO> listStock, int fromIndex, int toIndex) {
        if (stockCode.compareTo(listStock.get(fromIndex).getOwnerCode()) < 0 || stockCode.compareTo(listStock.get(toIndex).getOwnerCode()) > 0) {
            return null;
        }
        int index = (fromIndex + toIndex) / 2;
        if (stockCode.equals(listStock.get(index).getOwnerCode())) {
            return listStock.get(index);
        } else if (stockCode.compareTo(listStock.get(index).getOwnerCode()) > 0) {
            return findStockCode(stockCode, listStock, index + 1, toIndex);
        } else {
            return findStockCode(stockCode, listStock, fromIndex, index - 1);
        }
    }


    @Security
    public void previewDistributeNumber() {
        try {
            if (validatePreview()) {
//                if (DataUtil.isNullObject(inputByRange.getFromOwnerId())) {
//                    inputByRange.setFromOwnerId(currentStaff.getShopId());
//                    inputByRange.setFromOwnerType(VShopStaffDTO.TYPE_SHOP);
//                    inputByRange.setFromOwnerCode(currentStaff.getShopCode());
//                    inputByRange.setFromOwnerName(currentStaff.getShopName());
//                }
                inputByRange.setCurrentShopid(currentStaff.getShopId());
                inputByRange.setCurrentShopPath(currentStaff.getShopPath());
                inputByRangeForDistribute = DataUtil.cloneBean(inputByRange);

                listIsdnPreview = stockNumberService.previewDistriButeNumber(inputByRange, requiredRoleMap);
                int count = 0;
                if (!DataUtil.isNullOrEmpty(listIsdnPreview)) {
                    for (Object[] ob : listIsdnPreview) {
                        count += DataUtil.safeToLong(ob[5]);
                    }
                }
                reportSuccess("frmDistributeNumber:messages", "distribute.preview.msg", count, DataUtil.safeToLong(inputByRange.getEndRange()) - DataUtil.safeToLong(inputByRange.getStartRange()) + 1);
            }
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("frmDistributeNumber:messages", "", ex.getKeyMsg());
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportErrorValidateFail("frmDistributeNumber:messages", "", "common.error.happen");
        }
    }

    @Security
    public void distributeNumber() {
        try {

            if (DataUtil.isNullOrEmpty(listIsdnPreview)) {
                reportErrorValidateFail("frmDistributeNumber:messages", "", "distribute.list.range.invalid");
                return;
            }

            if (validateDistribute()) {
//                previewDistributeNumber();
                inputByRangeForDistribute.setReasonId(inputByRange.getReasonId());
                if (DataUtil.isNullObject(inputByRangeForDistribute.getFromOwnerId())) {
                    inputByRangeForDistribute.setFromOwnerId(currentStaff.getShopOwnerId());
                    inputByRangeForDistribute.setFromOwnerCode(currentStaff.getShopCode());
                    inputByRangeForDistribute.setFromOwnerType(Const.OWNER_TYPE.SHOP);
                    inputByRangeForDistribute.setFromOwnerName(currentStaff.getShopName());
                }
                for (OptionSetValueDTO op : listService) {
                    if (inputByRangeForDistribute.getTelecomServiceId().toString().equals(op.getValue())) {
                        inputByRangeForDistribute.setTelecomServiceName(op.getName());
                        break;
                    }
                }
                for (ReasonDTO reason : listReason) {
                    if (inputByRangeForDistribute.getReasonId().equals(reason.getReasonId())) {
                        inputByRangeForDistribute.setReasonCode(reason.getReasonCode());
                        inputByRangeForDistribute.setReasonName(reason.getReasonName());
                    }
                }
                int count = 0;
                if (!DataUtil.isNullOrEmpty(listIsdnPreview)) {
                    for (Object[] ob : listIsdnPreview) {
                        count += DataUtil.safeToLong(ob[5]);
                    }
                }
                inputByRangeForDistribute.setUserCreate(currentStaff.getName());
                inputByRangeForDistribute.setUserId(currentStaff.getStaffId());
                inputByRangeForDistribute.setUserCode(currentStaff.getStaffCode());
                inputByRangeForDistribute.setUserIp(BccsLoginSuccessHandler.getIpAddress());
                inputByRangeForDistribute.setNote(inputByRange.getNote());
                stockNumberService.distriButeNumber(listIsdnPreview, inputByRangeForDistribute, requiredRoleMap);
                reportSuccess("frmDistributeNumber:messages", "distribute.success", count, DataUtil.safeToLong(inputByRangeForDistribute.getEndRange()) - DataUtil.safeToLong(inputByRangeForDistribute.getStartRange()) + 1);
            }
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("frmDistributeNumber:messages", "", ex.getKeyMsg(), ex.getParamsMsg());
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportErrorValidateFail("frmDistributeNumber:messages", "", "common.error.happen");
        }
    }

    @Security
    public void distributeNumberByFile() {
        try {
            if (!isPreviewFile) {
                reportError("frmDistributeNumber:messages", "", "distribute.preview.before.distribute");
                return;
            }
            BaseMessage message = validateFileUploadWhiteList(uploadedFile, ALOW_EXTENSION_EXCEL_TYPE_LIST, MAX_SIZE_5M);
            if (!message.isSuccess()) {
                LogicException ex = new LogicException(message.getErrorCode(), message.getKeyMsg());
                ex.setDescription(message.getDescription());
                throw ex;
            }
            if (validateDistributeByFile()) {
                int rowUpdated = 0;
                List<String[]> listError = Lists.newArrayList();
                List<Object[]> listIsdn = readExcel(byteContent);
                if (!DataUtil.isNullObject(listIsdn)) {
                    inputByFile.setFromOwnerId(currentStaff.getShopId());
                    inputByFile.setFromShopPath(currentStaff.getShopPath());
                    inputByFile.setUserCreate(currentStaff.getName());
                    inputByFile.setUserCode(currentStaff.getStaffCode());
                    inputByFile.setUserId(currentStaff.getStaffId());
                    inputByFile.setUserIp(BccsLoginSuccessHandler.getIpAddress());
                    for (OptionSetValueDTO op : listService) {
                        if (inputByFile.getTelecomServiceId().toString().equals(op.getValue())) {
                            inputByFile.setTelecomServiceName(op.getName());
                            break;
                        }
                    }
                    for (ReasonDTO reason : listReason) {
                        if (inputByFile.getReasonId().equals(reason.getReasonId())) {
                            inputByFile.setReasonCode(reason.getReasonCode());
                            inputByFile.setReasonName(reason.getReasonName());
                        }
                    }
                    if (!DataUtil.isNullOrEmpty(listIsdn)) {
                        stockNumberService.distriButeNumberByFile(listIsdn, inputByFile, requiredRoleMap);
                        reportSuccess("frmDistributeNumber:messages", "distribute.success", listIsdn.size(), listError.size() + listIsdn.size());
                    } else {
                        reportErrorValidateFail("frmDistributeNumber:messages", "", "search.isdn.file.empty");
                    }
                } else {
                    reportErrorValidateFail("frmDistributeNumber:messages", "", "distribute.number.file.invalid");
                }
                if (!DataUtil.isNullObject(mapError) && !mapError.isEmpty()) {
                    ExcellUtil excellUtil = new ExcellUtil(uploadedFile, byteContent);
                    Sheet sheetProcess = excellUtil.getSheetAt(0);
                    int totalRow = excellUtil.getTotalRowAtSheet(sheetProcess);
                    hasFileError = true;
                    for (int i = 1; i < totalRow; i++) {
                        Row row = sheetProcess.getRow(i);
                        if (row == null) {
                            continue;
                        }
                        String isdn = excellUtil.getStringValue(row.getCell(0)).trim();
                        String stockCode = excellUtil.getStringValue(row.getCell(1)).trim();
                        if (DataUtil.isNullObject(isdn) && DataUtil.isNullObject(stockCode)) {
                            continue;
                        }
                        if (!DataUtil.isNumber(isdn)) {
                            String[] error = new String[3];
                            error[0] = isdn;
                            error[1] = stockCode;
                            error[2] = BundleUtil.getText("error.number.isdn.invalid");
                            listError.add(error);
                            continue;
                        }
                        if (mapError.containsKey(i + "")) {
                            String[] error = new String[3];
                            error[0] = isdn;
                            error[1] = stockCode;
                            error[2] = BundleUtil.getText(mapError.get(i + ""));
                            listError.add(error);
                            continue;
                        }
                        rowUpdated++;
                    }
                    try {
                        FileExportBean bean = new FileExportBean();
                        bean.setTempalatePath(FileUtil.getTemplatePath());
                        bean.setTemplateName("distributeIsdnPatternError.xls");
                        bean.putValue("lstData", listError);
                        bookError = FileUtil.exportWorkBook(bean);
                    } catch (Exception ex) {
                        reportError("", "common.error.happen", ex);
                        topPage();
                    }
                }
            }
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("frmDistributeNumber:messages", "", ex.getKeyMsg(), ex.getParamsMsg());
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportErrorValidateFail("frmDistributeNumber:messages", "", "common.error.happen");
        }
    }

    public String getTypeNumberName(String typeNumber) {
        for (OptionSetValueDTO op : listService) {
            if (op.getValue().equals(typeNumber)) {
                return op.getName();
            }
        }
        return "";
    }

    public String getStatusDigitalName(String status) {
        return BundleUtil.getText("digital.status." + status.trim());
    }

    @Secured("@")
    public void downloadFileError() {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"" + "distributeIsdnPattern.xls" + "\"");
            externalContext.setResponseContentType("application/vnd.ms-excel");
            bookError.write(externalContext.getResponseOutputStream());
            facesContext.responseComplete();
            return;
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
        }
    }

    @Secured("@")
    public void downloadFileTemplate() {
        try {
            InputStream createStream = Faces.getResourceAsStream(Const.BRAS_IPPOOL.RESOURCE_TEMPLATE_PATH + FILE_TEMPLATE_PATH);
            HSSFWorkbook workbook = new HSSFWorkbook(createStream);
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"" + "distributeIsdnPattern.xls" + "\"");
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
            hasFileError = false;
            isPreviewFile = false;
            BaseMessage message = validateFileUploadWhiteList(uploadedFile, ALOW_EXTENSION_EXCEL_TYPE_LIST, MAX_SIZE_5M);
            if (!message.isSuccess()) {
                byteContent = null;
                attachFileName = null;
                LogicException ex = new LogicException(message.getErrorCode(), message.getKeyMsg());
                ex.setDescription(message.getDescription());
                throw ex;
            }

            byteContent = uploadedFile.getContents();
            attachFileName = event.getFile().getFileName();
        } catch (LogicException ex) {
            reportError("", ex);
            topPage();
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
            logger.error(ex.getMessage(), ex);
            topPage();
        }

    }

    public void receiveStock(VShopStaffDTO dto) {
        this.inputByRange.setToOwnerId(DataUtil.safeToLong(dto.getOwnerId()));
        this.inputByRange.setToOwnerName(dto.getOwnerName());
        this.inputByRange.setToOwnerCode(dto.getOwnerCode());
//        this.inputByRange.setToOwnerType(dto.getOwnerType());
    }

    public void receiveStaff(VShopStaffDTO dto) {
        this.inputByRange.setToOwnerId(DataUtil.safeToLong(dto.getOwnerId()));
        this.inputByRange.setToOwnerName(dto.getOwnerName());
        this.inputByRange.setToOwnerCode(dto.getOwnerCode());
        this.inputByRange.setToOwnerType(dto.getOwnerType());
    }

    public void getStock(VShopStaffDTO dto) {
        this.inputByRange.setFromOwnerId(DataUtil.safeToLong(dto.getOwnerId()));
        this.inputByRange.setFromOwnerName(dto.getOwnerName());
        this.inputByRange.setFromOwnerCode(dto.getOwnerCode());
        this.inputByRange.setFromOwnerType(dto.getOwnerType());
    }

    public void getStaff(VShopStaffDTO dto) {
        this.inputByRange.setFromOwnerId(DataUtil.safeToLong(dto.getOwnerId()));
        this.inputByRange.setFromOwnerName(dto.getOwnerName());
        this.inputByRange.setFromOwnerCode(dto.getOwnerCode());
        this.inputByRange.setFromOwnerType(dto.getOwnerType());
    }

    public void setStaffReceiveInfoTag(StaffInfoTag staffReceiveInfoTag) {
        this.staffReceiveInfoTag = staffReceiveInfoTag;
    }

    @Secured("@")
    public void receiveClearGetShop() {
        this.inputByRange.setFromOwnerId(null);
        this.inputByRange.setFromOwnerName(null);
        this.inputByRange.setFromOwnerCode(null);
//        this.inputByRange.setFromOwnerType(null);
    }

    @Secured("@")
    public void receiveClearGetStaff() {
        this.inputByRange.setFromOwnerId(null);
        this.inputByRange.setFromOwnerName(null);
        this.inputByRange.setFromOwnerCode(null);
//        this.inputByRange.setFromOwnerType(null);
    }

    @Secured("@")
    public void receiveClearReceiveShop() {
        this.inputByRange.setToOwnerCode(null);
        this.inputByRange.setToOwnerId(null);
        this.inputByRange.setToOwnerName(null);
//        this.inputByRange.setToOwnerType(null);
    }

    @Secured("@")
    public void receiveClearReceiveStaff() {
        this.inputByRange.setToOwnerCode(null);
        this.inputByRange.setToOwnerId(null);
        this.inputByRange.setToOwnerName(null);
//        this.inputByRange.setToOwnerType(null);
    }

    public void setShopGetInfoTag(ShopInfoTag shopGetInfoTag) {
        this.shopGetInfoTag = shopGetInfoTag;
    }

    public void setStaffGetInfoTag(StaffInfoTag staffGetInfoTag) {
        this.staffGetInfoTag = staffGetInfoTag;
    }

    public Date getCurrentDate() {
        return Calendar.getInstance().getTime();
    }

    public String getDistribution() {
        return distribution;
    }

    public void setDistribution(String Distribution) {
        this.distribution = Distribution;
    }

    public static String getSelectFromFile() {
        return SELECT_FROM_FILE;
    }

    public static String getEnteredDigitalRanges() {
        return ENTERED_DIGITAL_RANGES;
    }

    public boolean isRenderedFromFile() {
        return renderedFromFile;
    }

    public void setRenderedFromFile(boolean renderedFromFile) {
        this.renderedFromFile = renderedFromFile;
    }

    public List<ReasonDTO> getListReason() {
        return listReason;
    }

    public List<OptionSetValueDTO> getListService() {
        return listService;
    }

    public void setListService(List<OptionSetValueDTO> listService) {
        this.listService = listService;
    }

    public void setListReason(List<ReasonDTO> listReason) {
        this.listReason = listReason;
    }

    public List<Object[]> getListIsdnPreview() {
        return listIsdnPreview;
    }

    public void setListIsdnPreview(List<Object[]> listIsdnPreview) {
        this.listIsdnPreview = listIsdnPreview;
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

    public boolean isHasFileError() {
        return hasFileError;
    }

    public void setHasFileError(boolean hasFileError) {
        this.hasFileError = hasFileError;
    }

    public String getTypeStock() {
        return typeStock;
    }

    public void setTypeStock(String typeStock) {
        this.typeStock = typeStock;
    }

    public int getTabIndex() {
        return tabIndex;
    }

    public void setTabIndex(int tabIndex) {
        this.tabIndex = tabIndex;
    }

    public void onChangeTypeReceiptStock() {
        this.inputByRange.setToOwnerCode(null);
        this.inputByRange.setToOwnerId(null);
        this.inputByRange.setToOwnerName(null);
        shopReceiveInfoTag.resetShop();
        staffReceiveInfoTag.resetProduct();
    }

    public void onChangeTypeDeliverStock() {
        this.inputByRange.setFromOwnerCode(null);
        this.inputByRange.setFromOwnerId(null);
        this.inputByRange.setFromOwnerName(null);
        shopGetInfoTag.resetShop();
        staffGetInfoTag.resetProduct();
    }

    public void clearListPreview() {
        this.listIsdnPreview = null;
    }

    public InputDistributeByRangeDTO getInputByRange() {
        return inputByRange;
    }

    public void setInputByRange(InputDistributeByRangeDTO inputByRange) {
        this.inputByRange = inputByRange;
    }

    public InputDistributeByFileDTO getInputByFile() {
        return inputByFile;
    }

    public void setInputByFile(InputDistributeByFileDTO inputByFile) {
        this.inputByFile = inputByFile;
    }

    public StaffInfoNameable getStaffGetInfoTag() {
        return staffGetInfoTag;
    }

    public void setStaffGetInfoTag(StaffInfoNameable staffGetInfoTag) {
        this.staffGetInfoTag = staffGetInfoTag;
    }

    public ShopInfoNameable getShopReceiveInfoTag() {
        return shopReceiveInfoTag;
    }

    public void setShopReceiveInfoTag(ShopInfoNameable shopReceiveInfoTag) {
        this.shopReceiveInfoTag = shopReceiveInfoTag;
    }

    public StaffInfoNameable getStaffReceiveInfoTag() {
        return staffReceiveInfoTag;
    }

    public void setStaffReceiveInfoTag(StaffInfoNameable staffReceiveInfoTag) {
        this.staffReceiveInfoTag = staffReceiveInfoTag;
    }

    public ShopInfoNameable getShopGetInfoTag() {
        return shopGetInfoTag;
    }

    public void setShopGetInfoTag(ShopInfoNameable shopGetInfoTag) {
        this.shopGetInfoTag = shopGetInfoTag;
    }

    public InputDistributeByRangeDTO getInputByRangeForDistribute() {
        return inputByRangeForDistribute;
    }

    public void setInputByRangeForDistribute(InputDistributeByRangeDTO inputByRangeForDistribute) {
        this.inputByRangeForDistribute = inputByRangeForDistribute;
    }
}
