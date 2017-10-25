package com.viettel.bccs.inventory.controller.sale;

import com.google.common.collect.Lists;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.fw.controller.TransCommonController;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.*;
import com.viettel.bccs.inventory.service.common.IMServiceUtil;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.web.common.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import java.util.*;

/**
 * controller doi thiet bi dau cuoi
 * @author thanhnt77
 */
@Component
@Scope("view")
@ManagedBean
public class ChangeTerminalDevideController extends BaseController {

    private static final List<Long> PROD_OFFER_TYPE_HANDSET_ID = Lists.newArrayList(Const.STOCK_TYPE.STOCK_HANDSET);

    private String note;
    private String damageSerial;
    private String changeSerial;
    private Long sourceType = 1L;
    private int limitAutoComplete;
    private String strNoteLimitDayChangeHandset;
    private Date fromDate;
    private Date toDate;
    private ProductOfferingTotalDTO prodOfferDamageDTO;
    private ProductOfferingTotalDTO prodOfferChangeDTO;
    private StaffDTO staffDTO;

    private List<ProductOfferingTotalDTO> lstProdOfferDamageDTO;
    private List<ProductOfferingTotalDTO> lstProdOfferChangeDTO;
    private List<ReportChangeHandsetDTO> lstChangeHandsetDTO;

    @Autowired
    private OptionSetValueService optionSetValueService;
    @Autowired
    private ProductOfferingService productOfferingService;
    @Autowired
    private ReportChangeHandsetService reportChangeHandsetService;
    @Autowired
    private ExecuteStockTransService executeStockTransService;

    @PostConstruct
    public void init() {
        try {
            limitAutoComplete = DataUtil.safeToInt(optionSetValueService.getValueByNameFromOptionSetValue(Const.OPTION_SET.LIMIT_AUTOCOMPLETE));
            String dayLimit =  DataUtil.safeToString(optionSetValueService.getValueByTwoCodeOption(Const.OPTION_SET.AMOUNT_DAY_TO_CHANGE_HANDSET, Const.OPTION_SET.AMOUNT_DAY_TO_CHANGE_HANDSET));
            strNoteLimitDayChangeHandset = getTextParam("mn.sale.change.terminal.day.offer.change.product", dayLimit);
            staffDTO = BccsLoginSuccessHandler.getStaffDTO();
            initData();
        } catch (Exception ex) {
            logger.error(ex);
            reportError("", "", "common.error.happen");
        }
    }

    /**
     * ham xu ly reset thong tin tim kiem
     * @author thanhnt77
     */
    private void initData() {
        prodOfferDamageDTO = new ProductOfferingTotalDTO();
        prodOfferChangeDTO = new ProductOfferingTotalDTO();
        lstProdOfferDamageDTO = Lists.newArrayList();
        lstProdOfferChangeDTO = Lists.newArrayList();
        lstChangeHandsetDTO = Lists.newArrayList();
        sourceType = 1L;
        note = null;
        damageSerial = null;
        changeSerial = null;
        Date currentDate = getSysdateFromDB();
        fromDate = currentDate;
        toDate = currentDate;
    }

    /**
     * get stock_trans_action
     * @author thanhnt77
     * @return
     */
    private StockTransActionDTO getStockTransActionDTO() {
        StockTransActionDTO transActionDTO = new StockTransActionDTO();
        transActionDTO.setCreateUser(staffDTO.getStaffCode());
        transActionDTO.setActionStaffId(staffDTO.getStaffId());
        return transActionDTO;
    }

    /**
     * get stock_trans
     * @author thanhnt77
     * @return
     */
    private StockTransDTO getStockTransDTO() {
        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setSourceType(sourceType);
        stockTransDTO.setNote(note);
        return stockTransDTO;
    }

    /**
     * get stock_trans_detail
     * @author thanhnt77
     * @return
     */
    private List<StockTransDetailDTO> getListTransDetailDTOs() {
        List<StockTransDetailDTO> lsDetail = Lists.newArrayList();

        StockTransDetailDTO stockDetailDamage = new StockTransDetailDTO();
        stockDetailDamage.setProdOfferName(prodOfferDamageDTO.getName());
        stockDetailDamage.setProdOfferId(prodOfferDamageDTO.getProductOfferingId());
        stockDetailDamage.setProdOfferTypeId(prodOfferDamageDTO.getProductOfferTypeId());
        stockDetailDamage.setStateId(prodOfferDamageDTO.getStateId());
        stockDetailDamage.setQuantity(1L);
        stockDetailDamage.setCheckSerial(Const.PRODUCT_OFFERING.CHECK_SERIAL);
        stockDetailDamage.setTableName(IMServiceUtil.getTableNameByOfferType(prodOfferDamageDTO.getProductOfferTypeId()));

        StockTransSerialDTO serialDTO = new StockTransSerialDTO();
        serialDTO.setFromSerial(damageSerial);
        serialDTO.setToSerial(damageSerial);
        serialDTO.setQuantity(1L);

        stockDetailDamage.setLstStockTransSerial(Lists.newArrayList(serialDTO));
        lsDetail.add(stockDetailDamage);

        if (DataUtil.safeEqual(sourceType, 2L)) {
            prodOfferChangeDTO = DataUtil.cloneBean(prodOfferDamageDTO);
        }

        StockTransDetailDTO stockDetailChange = new StockTransDetailDTO();
        stockDetailChange.setProdOfferName(prodOfferChangeDTO.getName());
        stockDetailChange.setProdOfferId(prodOfferChangeDTO.getProductOfferingId());
        stockDetailChange.setProdOfferTypeId(prodOfferChangeDTO.getProductOfferTypeId());
        stockDetailChange.setStateId(prodOfferChangeDTO.getStateId());
        stockDetailChange.setQuantity(1L);
        stockDetailChange.setCheckSerial(Const.PRODUCT_OFFERING.CHECK_SERIAL);
        stockDetailChange.setTableName(IMServiceUtil.getTableNameByOfferType(prodOfferChangeDTO.getProductOfferTypeId()));

        serialDTO = new StockTransSerialDTO();
        serialDTO.setFromSerial(changeSerial);
        serialDTO.setToSerial(changeSerial);
        serialDTO.setQuantity(1L);

        stockDetailChange.setLstStockTransSerial(Lists.newArrayList(serialDTO));
        lsDetail.add(stockDetailChange);
        return lsDetail;
    }

    /**
     * ham thay doi kieu doi may
     * @author thanhnt77
     */
    @Secured("@")
    public void doChangeType() {
        prodOfferChangeDTO = new ProductOfferingTotalDTO();
        prodOfferDamageDTO  = new ProductOfferingTotalDTO();
    }

    /**
     * rest mat hang
     * @author thanhnt77
     */
    @Secured("@")
    public void doResetHandSet() {
        initData();
    }

    /**
     * ham xu ly tim kiem bieu do
     * @author thanhnt77
     */
    @Secured("@")
    public void doCheckHandset() {
        try {
            validateDate(fromDate, toDate);

            if (!DataUtil.validateStringByPattern(damageSerial, Const.REGEX.SERIAL_REGEX) || damageSerial.length() > 19) {
                focusElementError("damageSerial");
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.import.to.partner.balance.serial.length");
            }
            if (!DataUtil.validateStringByPattern(changeSerial, Const.REGEX.SERIAL_REGEX) || changeSerial.length() > 19) {
                focusElementError("changeSerial");
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.import.to.partner.balance.serial.length");
            }


            lstChangeHandsetDTO = Lists.newArrayList();
            StockTransDTO stockTransDTO = getStockTransDTO();
            StockTransActionDTO stockTransActionDTO = getStockTransActionDTO();
            List<StockTransDetailDTO> lsDetail = getListTransDetailDTOs();
            //set them 2 bien fromDate, toDate de truyen sang SALE
            stockTransActionDTO.setFromDate(fromDate);
            stockTransActionDTO.setToDate(toDate);

            lstChangeHandsetDTO = reportChangeHandsetService.getLsChangeHandsetTerminalDevide(stockTransDTO, stockTransActionDTO, lsDetail);
            if (DataUtil.isNullOrEmpty(lstChangeHandsetDTO)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.sale.change.terminal.changeTerminalDevice.notFound");
            }
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex);
            topPage();
        } catch (Exception e) {
            logger.error(e);
            reportError("", "common.error.happened", e);
            topPage();
        }
    }

    /**
     * ham validate
     * @author thanhnt77
     */
    @Secured("@")
    public void doValidateHandset() {

    }

    /**
     * ham xu ly xuat file excel
     * @author thanhnt77
     */
    @Secured("@")
    public void doChangeHandset() {
        try {
            validateDate(fromDate, toDate);

            StockTransDTO stockTransDTO = getStockTransDTO();
            StockTransActionDTO stockTransActionDTO = getStockTransActionDTO();
            List<StockTransDetailDTO> lsDetail = getListTransDetailDTOs();
            /*lstChangeHandsetDTO = reportChangeHandsetService.getLsChangeHandsetTerminalDevide(stockTransDTO, stockTransActionDTO, lsDetail);
            if (DataUtil.isNullOrEmpty(lstChangeHandsetDTO)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.sale.change.terminal.changeTerminalDevice.notFound");
            }
*/
            //set them 2 bien fromDate, toDate de truyen sang SALE
            stockTransActionDTO.setFromDate(fromDate);
            stockTransActionDTO.setToDate(toDate);

            BaseMessageStockTrans message = executeStockTransService.executeStockTrans(Const.STOCK_TRANS.CHANGE_PRODUCT_DAMAGE,
                    Const.STOCK_TRANS_TYPE.EXPORT, stockTransDTO, stockTransActionDTO, lsDetail, null);
            if (!DataUtil.isNullOrEmpty(message.getErrorCode())) {
                LogicException logicException = new LogicException(message.getErrorCode(), message.getKeyMsg(), message.getParamsMsg());
                if (DataUtil.isNullOrEmpty(message.getParamsMsg())) {
                    logicException.setDescription(!DataUtil.isNullOrEmpty(message.getDescription()) ? message.getDescription() : logicException.getDescription());
                }
                throw logicException;
            }
            ReportChangeHandsetDTO reportChangeHandsetDTO = reportChangeHandsetService.findOne(message.getStockTransActionId());
            if (reportChangeHandsetDTO == null || DataUtil.isNullOrZero(reportChangeHandsetDTO.getAdjustAmount())) {
                reportSuccess("frmExportNote:msgExport", "mn.sale.change.terminal.change.sucess");
            } else if (DataUtil.safeToLong(reportChangeHandsetDTO.getAdjustAmount()).compareTo(0L) > 0) { // dieu chinh tang, KH tra them tien
                reportSuccess("frmExportNote:msgExport", "mn.sale.change.terminal.change.sucess.revoke.saletrans", reportChangeHandsetDTO.getAdjustAmount());
            } else if (DataUtil.safeToLong(reportChangeHandsetDTO.getAdjustAmount()).compareTo(0L) < 0) { // dieu chinh giam, phai hoan tien cho KH
                reportSuccess("frmExportNote:msgExport", "mn.sale.change.terminal.change.sucess.return.saletrans", -reportChangeHandsetDTO.getAdjustAmount());
            }
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex);
            topPage();
        } catch (Exception e) {
            logger.error(e);
            reportError("", "common.error.happened", e);
            topPage();
        }

    }

    /**
     * Tim kiem mat hang khi nhap tay
     */
    @Secured("@")
    public List<ProductOfferingTotalDTO> searchListProdDamage(String input) {
        try {
            String inputData = DataUtil.isNullOrEmpty(input) ? "" : input.trim();
            lstProdOfferDamageDTO = DataUtil.defaultIfNull(productOfferingService.getListProductOfferingByProductType(DataUtil.replaceSpaceSolr(inputData), Const.STOCK_TYPE.STOCK_HANDSET), new ArrayList<>());
        } catch (Exception e) {
            reportError("", "common.error.happened", e);
            topPage();
        }
        
        return lstProdOfferDamageDTO;
    }

    /**
     * Tim kiem mat hang khi nhap tay
     */
    @Secured("@")
    public List<ProductOfferingTotalDTO> searchListProdChange(String input) {
        try {
            String inputData = DataUtil.isNullOrEmpty(input) ? "" : input.trim().toUpperCase();
            lstProdOfferChangeDTO = DataUtil.defaultIfNull(productOfferingService.getLsProductOfferingDTO(DataUtil.replaceSpaceSolr(inputData), PROD_OFFER_TYPE_HANDSET_ID,
                    staffDTO.getStaffId(), Const.OWNER_TYPE.STAFF, DataUtil.safeToString(Const.GOODS_STATE.NEW), null), new ArrayList<>());
            /*lstProdOfferChangeDTO = DataUtil.defaultIfNull(productOfferingService.getListProductOfferingByProductType(DataUtil.replaceSpaceSolr(inputData), Const.STOCK_TYPE.STOCK_HANDSET), new ArrayList<>());*/
        }  catch (Exception e) {
            reportError("", "common.error.happened", e);
            topPage();
        }
        return lstProdOfferChangeDTO;
    }

    @Secured("@")
    public void doSelectProductOffering() {

    }

    public boolean isDisableDamageModel() {
        return prodOfferDamageDTO != null && prodOfferDamageDTO.getProductOfferingId() != null;
    }

    public boolean isDisableChangeModel() {
        return prodOfferChangeDTO != null && prodOfferChangeDTO.getProductOfferingId() != null;
    }

    /**
     * reset mat hang da chon
     */
    @Secured("@")
    public void resetLstProdDamage() {
        prodOfferDamageDTO = new ProductOfferingTotalDTO();
    }

    /**
     * reset mat hang da chon
     */
    @Secured("@")
    public void resetLstProdChange() {
        prodOfferChangeDTO = new ProductOfferingTotalDTO();
    }

    @Secured("@")
    public boolean isShowChangeProduct() {
        return DataUtil.safeEqual(1L, sourceType);
    }

    //getter and setter
    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDamageSerial() {
        return damageSerial;
    }

    public void setDamageSerial(String damageSerial) {
        this.damageSerial = damageSerial;
    }

    public String getChangeSerial() {
        return changeSerial;
    }

    public void setChangeSerial(String changeSerial) {
        this.changeSerial = changeSerial;
    }

    public Long getSourceType() {
        return sourceType;
    }

    public void setSourceType(Long sourceType) {
        this.sourceType = sourceType;
    }

    public ProductOfferingTotalDTO getProdOfferDamageDTO() {
        return prodOfferDamageDTO;
    }

    public void setProdOfferDamageDTO(ProductOfferingTotalDTO prodOfferDamageDTO) {
        this.prodOfferDamageDTO = prodOfferDamageDTO;
    }

    public ProductOfferingTotalDTO getProdOfferChangeDTO() {
        return prodOfferChangeDTO;
    }

    public void setProdOfferChangeDTO(ProductOfferingTotalDTO prodOfferChangeDTO) {
        this.prodOfferChangeDTO = prodOfferChangeDTO;
    }

    public List<ProductOfferingTotalDTO> getLstProdOfferDamageDTO() {
        return lstProdOfferDamageDTO;
    }

    public void setLstProdOfferDamageDTO(List<ProductOfferingTotalDTO> lstProdOfferDamageDTO) {
        this.lstProdOfferDamageDTO = lstProdOfferDamageDTO;
    }

    public List<ProductOfferingTotalDTO> getLstProdOfferChangeDTO() {
        return lstProdOfferChangeDTO;
    }

    public void setLstProdOfferChangeDTO(List<ProductOfferingTotalDTO> lstProdOfferChangeDTO) {
        this.lstProdOfferChangeDTO = lstProdOfferChangeDTO;
    }

    public List<ReportChangeHandsetDTO> getLstChangeHandsetDTO() {
        return lstChangeHandsetDTO;
    }

    public void setLstChangeHandsetDTO(List<ReportChangeHandsetDTO> lstChangeHandsetDTO) {
        this.lstChangeHandsetDTO = lstChangeHandsetDTO;
    }

    public int getLimitAutoComplete() {
        return limitAutoComplete;
    }

    public void setLimitAutoComplete(int limitAutoComplete) {
        this.limitAutoComplete = limitAutoComplete;
    }

    public String getStrNoteLimitDayChangeHandset() {
        return strNoteLimitDayChangeHandset;
    }

    public void setStrNoteLimitDayChangeHandset(String strNoteLimitDayChangeHandset) {
        this.strNoteLimitDayChangeHandset = strNoteLimitDayChangeHandset;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public Date getSysdateFromDB() {
        try {
            return optionSetValueService.getSysdateFromDB(true);
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
        }
        return new Date();
    }

    public void validateDate(Date fromDate, Date toDate) throws LogicException, Exception {
        Date sysdate = getSysdateFromDB();
        if (fromDate == null || toDate == null) {
            if (fromDate == null) {
                executeCommand("focusElementError('fromDate')");
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.from.date.not.blank");
            } else {
                executeCommand("focusElementError('toDate')");
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.to.date.not.blank");
            }
        } else {
            if (fromDate.after(sysdate)) {
                executeCommand("focusElementError('fromDate')");
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.trans.from.valid");
            }
            if (toDate.before(fromDate)) {
                executeCommand("focusElementError('fromDate')");
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.trans.from.than.to");
            }
            if (toDate.getMonth() != fromDate.getMonth()) {
                executeCommand("focusElementError('fromDate')");
                executeCommand("focusElementError('toDate')");
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.trans.from.to.must.equals.month.year");
            }if (toDate.getYear() != fromDate.getYear()) {
                executeCommand("focusElementError('fromDate')");
                executeCommand("focusElementError('toDate')");
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.trans.from.to.must.equals.month.year");
            }
        }
    }
}

