package com.viettel.bccs.fw.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.*;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DateUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.web.common.controller.BaseController;
import net.sf.cglib.core.Local;
import org.primefaces.context.RequestContext;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import viettel.passport.client.ObjectToken;
import viettel.passport.client.UserToken;

import javax.faces.context.FacesContext;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author luannt23 on 12/5/2015.
 */
public class InventoryController extends BaseController {

    protected static final String[] ALOW_EXTENSION_EXCEL_TYPE_LIST = {"xls", "xlsx"};
    protected static final String[] ALOW_EXTENSION_PDF_TYPE_LIST = {"pdf"};
    protected static final String[] ALOW_EXTENSION_TEXT_TYPE_LIST = {"txt"};
    protected static final String[] ALOW_EXTENSION_CSV_TYPE_LIST = {"txt"};
    protected static final String[] ALOW_EXTENSION_ALL_TYPE_LIST = {"pdf", "png", "jpg", "bmp", "gif", "jpe", "jpeg", "doc", "docx", "zip", "rar", "xls", "xlsx", "pdf", "txt"};
    public static final int MAX_SIZE_2M = 2 * 1024 * 1024;
    public static final int MAX_SIZE_5M = 5 * 1024 * 1024;

    //<editor-fold desc="Khai bao bien" defaultstate="collapsed">
    @Autowired
    protected OptionSetValueService optionSetValueService;

    @Autowired
    protected CommonService commonService;

    @Autowired
    protected StaffService staffService;
    @Autowired
    protected ShopService shopService;
    @Autowired
    private StockTotalService stockTotalService;

    private Map<String, List<OptionSetValueDTO>> mapOptionVal = new HashMap<>();

    public StaffDTO getStaffDTO() {
        StaffDTO loginUser = DataUtil.cloneBean(BccsLoginSuccessHandler.getStaffDTO());
        loginUser.setName(loginUser.getStaffCode() + "-" + loginUser.getName());
        loginUser.setShopName(loginUser.getShopCode() + "-" + loginUser.getShopName());
        return loginUser;
    }
    //</editor-fold>

    public String getMessage(String key, Object... params) {
        String msg = getText(key);
        Object[] arguments = new Object[params.length];
        for (int i = 0; i < arguments.length; i++) {
            try {
                arguments[i] = getText(DataUtil.safeToString(params[i]));
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
            }
        }
        return MessageFormat.format(msg, arguments);
    }

    public List<OptionSetValueDTO> getOptionValByCode(String code) {
        if (DataUtil.isNullOrEmpty(code))
            return Lists.newArrayList();
        try {
            String key = getKey(code, "");
            if (DataUtil.isNullOrEmpty(mapOptionVal) && mapOptionVal.size() < 20) {
                mapOptionVal = new HashMap<String, List<OptionSetValueDTO>>();
            }
            if (!mapOptionVal.containsKey(key)) {
                List<OptionSetValueDTO> options = optionSetValueService.getByOptionSetCode(code);
                if (DataUtil.isNullOrEmpty(options))
                    options = Lists.newArrayList();
                mapOptionVal.put(key, options);
            }
            if (mapOptionVal.containsKey(key)) {
                return mapOptionVal.get(key);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return Lists.newArrayList();
    }

    public void executeCommand(String command) {
        StringBuilder bCommand = new StringBuilder(command);
        if (!DataUtil.isNullOrEmpty(command)) {
            if (!command.endsWith(")") && !command.endsWith(");")) {
                bCommand.append("();");
            }
            RequestContext.getCurrentInstance().execute(bCommand.toString());
        }
    }

    /**
     * ham validate user dang nhap
     *
     * @param fromOwnerId
     * @param toOwnerId
     * @param notThreeRegion true neu ko phai la kho 3 mien
     * @throws LogicException
     * @throws Exception
     * @author ThanhNT
     */
    protected void valdiateUserLogin(Long fromOwnerId, Long toOwnerId, boolean notThreeRegion) throws LogicException, Exception {
        //validate user dang nhap trc khi xuat kho
        StaffDTO staffDTO = BccsLoginSuccessHandler.getStaffDTO();
        StaffDTO staffDTOCurrent = staffService.findOne(staffDTO.getStaffId());
        if (staffDTOCurrent == null || Const.STATUS.NO_ACTIVE.equals(staffDTOCurrent.getStatus())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "staff.code.invalid.name");
        }
        //shopid cua nhan vien dang nhap = fromOwnerId cua kho xuat
        if (notThreeRegion && !staffDTOCurrent.getShopId().equals(fromOwnerId)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "staff.shop.not.permission");
        }
        //validate shop kho xuat
        ShopDTO fromShop = shopService.findOne(fromOwnerId);
        if (fromShop == null || !Const.STATUS.ACTIVE.equals(fromShop.getStatus())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.export.shop.empty");
        }
        //validate shop kho nhan
        ShopDTO toShop = shopService.findOne(toOwnerId);
        if (toShop == null || !Const.STATUS.ACTIVE.equals(toShop.getStatus())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.receive.shop.empty");
        }
    }

    @Secured("@")
    public String getDisplayName(String code, Object value) {
        try {
            if (DataUtil.isNullOrEmpty(code)) {
                return DataUtil.safeToString(value);
            }
            if (DataUtil.isNullOrEmpty(DataUtil.safeToString(value))) {
                return "";
            }
            String val = DataUtil.safeToString(value).trim();
            List<OptionSetValueDTO> options = getOptionValByCode(code);
            for (OptionSetValueDTO optionVal : options) {
                if (optionVal.getValue().equals(val)) {
                    return optionVal.getName();
                }
            }
            String key = getKey(code, DataUtil.safeToString(value));
            return getText(key);
        } catch (Exception e) {
            reportError("", "common.error.happened", e);
            topPage();
        }
        return DataUtil.safeToString(value);
    }

    private String getKey(String key, String value) {
        String[] subs = key.split("_");
        StringBuilder result = new StringBuilder();
        for (String sub : subs) {
            if (!DataUtil.isNullOrEmpty(sub) && !key.startsWith(sub)) {
                result.append(".");
            }
            result.append(sub.toLowerCase());
        }
        return result.append(value).toString();
    }

    @Secured("@")
    public Date getSysdateFromDB() {
        try {
            return optionSetValueService.getSysdateFromDB(true);
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
        }
        return new Date();
    }

    @Secured("@")
    public String formatDateTime(Date date) {
        try {
            return DateUtil.date2ddMMyyyyHHMMss(date);
        } catch (Exception ex) {
            logger.error("Err", ex);
            logger.info(this.getClass().getSimpleName() + new Date() + "format date - exception");
        }
        return "";
    }

    @Secured("@")
    public String formatDate(Date date) {
        try {
            return DateUtil.date2ddMMyyyyString(date);
        } catch (Exception ex) {
            logger.error("Err", ex);
            logger.info(this.getClass().getSimpleName() + new Date() + "format date - exception");
        }
        return "";
    }

    /**
     * @param componentCode
     * @return true neu co quyen su dung component
     * @throws Exception
     * @description ham kiem tra quyen tren component
     * @author LuanNT23
     */
    protected boolean authenticate(String componentCode) throws Exception {
        UserToken token = getUserToken();
        if (!DataUtil.isNullObject(token)) {
            List<ObjectToken> objectTokens = (ArrayList<ObjectToken>) token.getComponentList();
            for (ObjectToken object : objectTokens) {
                if (!DataUtil.isNullOrEmpty(object.getObjectCode()) && object.getObjectCode().equals(componentCode)) {
                    return true;
                }
            }
        }
        StringBuilder logBuilder = new StringBuilder("");
        logBuilder.append(optionSetValueService.getSysdateFromDB(false)).append("||")
                .append(BccsLoginSuccessHandler.getStaffDTO().getStaffCode()).append("||")
                .append(componentCode).append("||Authenticate failed!");
        logger.error(logBuilder);
        return false;
    }

    public void topReportError(String displayArea, LogicException ex) {
        if (ex.getParamsMsg() != null) {
            String[] params = ex.getParamsMsg();
            for (int i = 0; i < params.length; i++) {
                params[i] = getText(params[i]);
            }
            ex = new LogicException(ex.getErrorCode(), ex.getKeyMsg(), params);
        }
        super.reportError(displayArea, ex);
        super.topPage();
    }

    public void topReportError(String displayArea, String keyMsg, Exception ex) {
        super.reportError(displayArea, keyMsg, ex);
        super.topPage();
    }

    public void topReportError(String displayArea, String errorCode, String keyMsg) {
        super.reportError(displayArea, errorCode, keyMsg);
        super.topPage();
    }

    public void topReportSuccess(String displayArea, String keyMsg) {
        super.reportSuccess(displayArea, keyMsg);
        super.topPage();
    }

    public void topReportSuccess(String displayArea, String keyMsg, Object... params) {
        String[] sParam = null;
        if (params != null) {
            sParam = new String[params.length];
            for (int i = 0; i < params.length; i++) {
                sParam[i] = getText(DataUtil.safeToString(params[i]));
            }
        }
        super.reportSuccess(displayArea, keyMsg, sParam);
        super.topPage();
    }

    /**
     * Ham validate dinh dang serial thiet bi
     *
     * @throws LogicException
     * @author ThanhNT
     */
    protected Map<String, String> validateEmptyStockTransDetail(List<StockTransDetailDTO> lsStockTransDetail) {
        Map<String, String> mapError = Maps.newHashMap();
        if (lsStockTransDetail == null) {
            return mapError;
        }
        String strProNameErrIndex = "";//luu tru danh sach cac class ma loi cua ten mat hang
        String strNumberErrIndex = "";//luu tru danh sach cac class ma loi so luong
        String strQuantityAvaiableIndex = "";//luu tru danh sach cac class ma loi so luong
        for (int i = 0; i < lsStockTransDetail.size(); i++) {
            StockTransDetailDTO stockDetail = lsStockTransDetail.get(i);
            if (stockDetail.getProdOfferId() == null) {
                if (!"".equals(strProNameErrIndex)) {
                    strProNameErrIndex += ",";
                }
                strProNameErrIndex += i;
            }
            if (stockDetail.getQuantity() == null || stockDetail.getQuantity() == 0L) {
                if (!"".equals(strNumberErrIndex)) {
                    strNumberErrIndex += ",";
                }
                strNumberErrIndex += i;
            } else {
                if (stockDetail.getAvaiableQuantity() != null && DataUtil.isNullObject(stockDetail.getStrStateIdAfter())) {
                    StockTotalDTO stockTotalDTO = new StockTotalDTO();
                    stockTotalDTO.setOwnerType(stockDetail.getOwnerType());
                    stockTotalDTO.setOwnerId(stockDetail.getOwnerID());
                    stockTotalDTO.setProdOfferId(stockDetail.getProdOfferId());
                    stockTotalDTO.setStateId(stockDetail.getStateId());
                    try {
                        List<StockTotalDTO> stockTotalDTOs = stockTotalService.searchStockTotal(stockTotalDTO);
                        if (DataUtil.isNullOrEmpty(stockTotalDTOs)
                                || stockDetail.getQuantity() > DataUtil.safeToLong(stockTotalDTOs.get(0).getAvailableQuantity())) {
                            if (!"".equals(strQuantityAvaiableIndex)) {
                                strQuantityAvaiableIndex += ",";
                            }
                            strQuantityAvaiableIndex += i;
                        }
                    } catch (Exception ex) {
                        topReportError("", "common.error.happened", ex);
                    }
                }
            }
        }
        mapError.put("VALID_ERROR_EMPTY_PROD_NAME", strProNameErrIndex);
        mapError.put("VALID_ERROR_EMPTY_QUANTITY", strNumberErrIndex);
        mapError.put("VALID_ERROR_QUANTITY", strQuantityAvaiableIndex);
        return mapError;
    }

    protected Map<String, String> validateDuplidateStockTransDetail(List<StockTransDetailDTO> lstDetail) {
        StringBuilder duplicate = new StringBuilder("");
        Map<String, String> mapError = Maps.newHashMap();
        if (lstDetail == null) {
            return mapError;
        }
        for (int i = 0; i < lstDetail.size() - 1; i++) {
            StockTransDetailDTO par = lstDetail.get(i);
            if (!DataUtil.isNullOrZero(par.getStateId()) && !DataUtil.isNullOrZero(par.getProdOfferId())) {
                for (int j = i + 1; j < lstDetail.size(); j++) {
                    StockTransDetailDTO sub = lstDetail.get(j);
                    if ((DataUtil.safeEqual(par.getStateId(), sub.getStateId()) && !DataUtil.isNullObject(par.getStrStateIdAfter()) &&
                            DataUtil.safeEqual(par.getStrStateIdAfter(), sub.getStrStateIdAfter())
                            && DataUtil.safeEqual(par.getProdOfferId(), sub.getProdOfferId())) ||
                            (DataUtil.safeEqual(par.getStateId(), sub.getStateId()) && DataUtil.isNullObject(par.getStrStateIdAfter())
                                    && DataUtil.safeEqual(par.getProdOfferId(), sub.getProdOfferId()))) {
                        if (!DataUtil.isNullOrEmpty(duplicate.toString())) {
                            duplicate.append(",");
                        }
                        duplicate.append(i);
                        duplicate.append(",").append(j);
                    }
                }
            }
        }
        mapError.put("VALID_ERROR_DUPLICATE", duplicate.toString());
        return mapError;
    }

    @Secured("@")
    public String formatNumber(Long number) {
        try {
            DecimalFormat format = new DecimalFormat();
            format.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(FacesContext.getCurrentInstance().getViewRoot().getLocale()));
            return format.format(number);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return number == null ? "0" : number.toString();
    }

    public BaseMessage validateFileName(String fileName) {
        BaseMessage baseMessage = new BaseMessage(true);
        String expression = "[\\s_a-zA-Z0-9\\-\\.]+";
        //Make the comparison case-insensitive.
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(fileName);
        if (!matcher.matches()) {
            baseMessage.setSuccess(false);
            baseMessage.setErrorCode("ERR_FILE_NAME");
            baseMessage.setKeyMsg("fileUtils.err.fileName.invalid");
            baseMessage.setDescription(getText("fileUtils.err.fileName.invalid"));
            return baseMessage;
        }
        if (fileName.length() >= 100) {
            baseMessage.setSuccess(false);
            baseMessage.setErrorCode("ERR_FILE_NAME");
            baseMessage.setKeyMsg("stock.trans.uploadFile.maxlength");
            baseMessage.setDescription(getTextParam("stock.trans.uploadFile.maxlength", fileName));
            return baseMessage;
        }
        return baseMessage;
    }

    public BaseMessage validateFileUploadWhiteList(UploadedFile fileAttachment, String[] extension, int maxSize) {
        BaseMessage baseMessage = new BaseMessage(true);
        if (fileAttachment == null) {
            baseMessage.setSuccess(false);
            baseMessage.setErrorCode("ERR_VALID_FILE_UPLOAD_1");
            baseMessage.setKeyMsg("fileUtils.err.file.not.found");
            baseMessage.setDescription(getText("fileUtils.err.file.not.found"));
            return baseMessage;
        }
        String fileName = fileAttachment.getFileName();

        // check file name
        if (DataUtil.isNullOrEmpty(fileName)) {
            baseMessage.setSuccess(false);
            baseMessage.setErrorCode("ERR_VALID_FILE_UPLOAD_1");
            baseMessage.setKeyMsg("fileUtils.err.file.not.found");
            baseMessage.setDescription(getText("fileUtils.err.file.not.found"));
            return baseMessage;
        }

        // check duoi file
        String[] fileNameArr = fileName.split("\\.");
        if (fileNameArr == null || fileNameArr.length != 2) {

            BaseMessage msgValid = validateFileName(fileName);
            if (!msgValid.isSuccess()) {
                return msgValid;
            }

            baseMessage.setSuccess(false);
            baseMessage.setErrorCode("ERR_VALID_FILE_UPLOAD_2");
            baseMessage.setKeyMsg("fileUtils.err.file.name.invalid");
            baseMessage.setDescription(getText("fileUtils.err.file.name.invalid"));
            return baseMessage;
        }

        if (fileAttachment.getContents().length == 0) {
            baseMessage.setSuccess(false);
            baseMessage.setErrorCode("ERR_VALID_FILE_UPLOAD_3");
            baseMessage.setKeyMsg(getText("fileUtils.err.file.invalid.min.size"));
            baseMessage.setDescription(getText("fileUtils.err.file.invalid.min.size"));
            return baseMessage;
        }

        if (maxSize != 0 && fileAttachment.getContents().length > maxSize) {
            baseMessage.setSuccess(false);
            baseMessage.setErrorCode("ERR_VALID_FILE_UPLOAD_4");
            baseMessage.setKeyMsg(getText("fileUtils.err.file.invalid.size"));
            baseMessage.setDescription(getTextParam("fileUtils.err.file.invalid.size", DataUtil.safeToString(maxSize / (1024 * 1024))));
            return baseMessage;
        }

        //kiem tra ten file mo rong co fai la extension ko
        String extensionFileName = fileNameArr[1];
        boolean isOk = false;
        String strFile = "";
        for (int i = 0; i < extension.length; i++) {
            strFile += extension[i] + ",";
            if (extension[i].equalsIgnoreCase(extensionFileName)) {
                isOk = true;
                break;
            }
        }
        if (!DataUtil.isNullOrEmpty(strFile) && ",".equals(strFile.substring(strFile.length() - 1))) {
            strFile = strFile.substring(0, strFile.length() - 1);
        }
        if (!isOk) {
            baseMessage.setSuccess(false);
            baseMessage.setErrorCode("ERR_VALID_FILE_UPLOAD_2");
            baseMessage.setKeyMsg(getText("fileUtils.err.file.invalid.format"));
            baseMessage.setDescription(getTextParam("fileUtils.err.file.invalid.format", extensionFileName.toString(), strFile));
            return baseMessage;
        }
        return validateFileName(fileName);
    }
}
