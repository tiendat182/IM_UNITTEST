package com.viettel.bccs.inventory.controller.stock;

import com.google.common.collect.Lists;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.fw.controller.TransCommonController;
import com.viettel.bccs.fw.logging.Kpi;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.*;
import com.viettel.bccs.inventory.tag.*;
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
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by tuyendv8 on 11/11/2015.
 */
@Component
@Scope("view")
@ManagedBean
public class StaffExportStockController extends TransCommonController {

    private static final String FILE_NAME_TEMPLATE = "mau_file_nhap_serial.xls";

    private boolean infoOrderDetail;
    private boolean isViewListSerial;
    private boolean isViewBtnExportStock;
    private int index;
    private int limitAutoComplete;
    private byte[] byteContent;
    private String attachFileName = "";
    private String typePrint = "1"; //mac dinh la chon file
    private String[] fileType = {".doc", ".docx", ".pdf", ".xls", ".xlsx", ".png", ".jpg", ".jpeg", ".bmp"};
    private String[] fileImportType = {".xls", ".xlsx"};
    private StaffDTO staffDTO;
    private VStockTransDTO forSearch;
    private VStockTransDTO selectedStockTrans;
    private RequiredRoleMap requiredRoleMap;
    private StockTransFullDTO stockTransDetail;
    private List<String> lstChanelTypeId = Lists.newArrayList();
    private List<VStockTransDTO> vStockTransDTOList;
    private List<OptionSetValueDTO> optionSetValueDTOsList;
    private List<StockTransFullDTO> lsStockTransFull;
    private List<StockTransSerialDTO> listSerialView;
    private List<StockTransSerialDTO> listSerialRangeSelect = Lists.newArrayList();
    private Long stockActionId;

    @Autowired
    private OrderStockTagNameable orderStockTag;
    @Autowired
    private OrderStaffTagNameable orderStaffTag;
    @Autowired
    private ShopInfoNameable shopInfoExportTag;
    @Autowired
    private ShopInfoNameable shopInfoTagExportDlg;
    @Autowired
    private StaffInfoNameable staffInfoReceiveTag;
    @Autowired
    private ListFifoProductNameable listProductTag;
    @Autowired
    private OptionSetValueService optionSetValueService;
    @Autowired
    private StockTransService stockTransService;
    @Autowired
    private StockTransActionService stockTransActionService;
    @Autowired
    private StockTransSerialService stockTransSerialService;
    @Autowired
    private ExecuteStockTransService executeStockTransService;
    @Autowired
    private StockTransVofficeService stockTransVofficeService;

    @PostConstruct
    public void init() {
        try {
            doResetStockTrans();
            executeCommand("updateControls()");
            showDialog("guide");
        } catch (Exception e) {
            logger.error(e);
            reportError("", "common.error.happened", e);
        }
    }

    /**
     * ham xu ly xoa thong tin tim kiem
     *
     * @author ThanhNT77
     */
    @Secured("@")
    public void doResetStockTrans() throws LogicException, Exception {
        stockActionId = null;
        staffDTO = BccsLoginSuccessHandler.getStaffDTO();
        lstChanelTypeId = commonService.getChannelTypes(Const.OWNER_TYPE.STAFF);
        lsStockTransFull = Lists.newArrayList();
        shopInfoExportTag.initShop(this, DataUtil.safeToString(staffDTO.getShopId()), false);
        shopInfoExportTag.loadShop(DataUtil.safeToString(staffDTO.getShopId()), true);
        staffInfoReceiveTag.initStaffWithChanelTypes(this, DataUtil.safeToString(staffDTO.getShopId()), null, lstChanelTypeId, false);
        optionSetValueDTOsList = DataUtil.defaultIfNull(optionSetValueService.getByOptionSetCode(Const.OPTION_SET.STOCK_TRANS_STATUS), new ArrayList<OptionSetValueDTO>());
        limitAutoComplete = DataUtil.safeToInt(optionSetValueService.getValueByNameFromOptionSetValue(Const.OPTION_SET.LIMIT_AUTOCOMPLETE));
        requiredRoleMap = CustomAuthenticationProvider.createRequiredRoleMap(Const.PERMISION.ROLE_SYNC_LOGISTIC, Const.PERMISION.ROLE_TRANSPORT_STOCK);
        orderStaffTag.init(this, false);
        // danh sach hang hoa
        listProductTag.setAddNewProduct(false);
        //
        forSearch = new VStockTransDTO();
        Date currentDate = getSysdateFromDB();
        forSearch = new VStockTransDTO(currentDate, currentDate);
        forSearch.setActionType(Const.STOCK_TRANS_STATUS.EXPORT_NOTE);
        forSearch.setStockTransStatus(Const.STOCK_TRANS_STATUS.EXPORT_NOTE);
        vStockTransDTOList = Lists.newArrayList();
        infoOrderDetail = false;
        staffInfoReceiveTag.initStaffWithChanelTypes(this, DataUtil.safeToString(staffDTO.getShopId()), null, lstChanelTypeId, false);
        doSearchStockTrans();
    }

    /**
     * ham xu ly tim kiem
     *
     * @author ThanhNT77
     */
    @Secured("@")
    public void doSearchStockTrans() {
        try {
            validateDate(forSearch.getStartDate(), forSearch.getEndDate());
            forSearch.setFromOwnerType(Const.OWNER_TYPE.SHOP_LONG);
            forSearch.setFromOwnerID(staffDTO.getShopId());
            forSearch.setToOwnerType(Const.OWNER_TYPE.STAFF_LONG);
            forSearch.setVtUnit(Const.VT_UNIT.VT);
            forSearch.setObjectType(Const.OWNER_TYPE.STAFF);
            forSearch.setUserShopId(staffDTO.getShopId());
            vStockTransDTOList = stockTransService.searchVStockTrans(forSearch);
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

    @Secured("@")
    public void doViewStockTransDetail(VStockTransDTO selected) {
        try {
            if (DataUtil.isNullObject(selected)) {
                this.infoOrderDetail = false;
            } else {
                //Validate ky voffice
                StockTransActionDTO actionDTO = stockTransActionService.findOne(selected.getActionID());
                stockTransVofficeService.doSignedVofficeValidate(actionDTO);

                selectedStockTrans = selected;
                orderStaffTag.loadOrderStaff(selected.getActionID(), true);
                ConfigListProductTagDTO configListProductTagDTO = new ConfigListProductTagDTO(Const.MODE_SERIAL.MODE_ADD_SERIAL);
                listProductTag.load(this, selected.getActionID(), configListProductTagDTO);
                infoOrderDetail = true;
                StockTransDTO stockTransDTO = orderStaffTag.getStockTransDTO();
                if (!(Const.SHOP.SHOP_VTT_ID.equals(stockTransDTO.getToOwnerId()) || Const.SHOP.SHOP_PARENT_VTT_ID.equals(stockTransDTO.getToOwnerId()))) {
                    if (stockTransDTO.getToOwnerId() != null && stockTransDTO.getToOwnerType() != null) {
                        stockDebitDTO = stockDebitService.findStockDebitValue(DataUtil.safeToLong(stockTransDTO.getToOwnerId()), DataUtil.safeToString(stockTransDTO.getToOwnerType()));
                    }
                }
            }
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
    public void doDestroyStock(Long stockTransActionId) {
        try {
            StockTransActionDTO stockTransActionDTO = stockTransActionService.findOne(stockTransActionId);

            StockTransDTO stockTransDTO = stockTransService.findOne(stockTransActionDTO.getStockTransId());

            BaseMessage message = executeStockTransService.executeStockTrans(Const.STOCK_TRANS.CANCEL_TRANS, null, stockTransDTO, stockTransActionDTO, new ArrayList<StockTransDetailDTO>(), requiredRoleMap);
            if (!DataUtil.isNullOrEmpty(message.getErrorCode())) {
                throw new LogicException(message.getErrorCode(), message.getKeyMsg());
            }
            reportSuccess("frmExportStock:msgStaffExportStock", "export.order.stock.confirm.cancel.ok");
            doSearchStockTrans();
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("frmExportStock:msgStaffExportStock", ex);
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("frmExportStock:msgStaffExportStock", "common.error.happened", ex);
            topPage();
        }
    }

    /**
     * ham set action id
     *
     * @param currentActionId
     * @author thanhnt77
     */
    public void doSetActionId(Long currentActionId) {
        this.currentActionId = currentActionId;
    }

    /**
     * ham cap nhap trang thai vOffice
     *
     * @author ThanhNT77
     */
    @Secured("@")
    public void doSignVoffice() {
        try {
            if (DataUtil.isNullOrZero(currentActionId)) {
                return;
            }
            doSaveStatusOffice(currentActionId);
            //goi ham cap nhap thong tin vOffice
            reportSuccess("", "voffice.sign.office.susscess");
            doSearchStockTrans();
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("frmExportNote:msgExportNote", ex);
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("frmExportNote:msgExportNote", "common.error.happened", ex);
            topPage();
        }
    }

    /**
     * ham lap lenh xuat
     *
     * @author ThanhNT77
     */
    @Secured("@")
    public void doValidateCreateStock() {
        try {
            List<StockTransDetailDTO> lsStockTransFull = listProductTag.getListTransDetailDTOs();
            for (StockTransDetailDTO stockTransFullDTO : lsStockTransFull) {
                if (Const.PRODUCT_OFFERING.CHECK_SERIAL.equals(stockTransFullDTO.getCheckSerial()) && DataUtil.isNullOrEmpty(stockTransFullDTO.getLstSerial())) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "mn.stock.confirm.serial.empty", stockTransFullDTO.getProdOfferName());
                }
            }
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

    /**
     * ham reset back tro ve man tim kiem
     *
     * @author ThanhNT77
     */
    @Secured("@")
    public void doBackPage() {
        try {
            infoOrderDetail = false;
            orderStaffTag.resetOrderStaff();
            lsStockTransFull = Lists.newArrayList();
            vStockTransDTOList = stockTransService.searchVStockTrans(forSearch);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("frmExportStock:msgStaffExportStock", "common.error.happened", ex);
            topPage();
        }
    }

    @Secured("@")
    public void doExportShop(VShopStaffDTO vShopStaffDTO) {
        if (vShopStaffDTO != null) {
            forSearch.setFromOwnerID(DataUtil.safeToLong(vShopStaffDTO.getOwnerId()));
        }
    }

    @Secured("@")
    public void doReceiveStaff(VShopStaffDTO vShopStaffDTO) {
        if (vShopStaffDTO != null) {
            forSearch.setToOwnerID(DataUtil.safeToLong(vShopStaffDTO.getOwnerId()));
        }
    }

    @Secured("@")
    public void resetReceiveStaff() {
        forSearch.setToOwnerID(null);
    }

    /**
     * ham lap lenh xuat
     *
     * @author ThanhNT77
     */
    @Kpi("ID_KPI_STAFF_EXPORT_STOCK")
    @Secured("@")
    public void doCreateStaffExportStock() {
        try {

            stockActionId = null;
            //validate ngay nhap phai la dinh dang so, va phai la so nguyen duong
            List<StockTransDetailDTO> lsDetailDTOs = listProductTag.getListTransDetailDTOs();

            StockTransActionDTO stockTransActionDTO = orderStaffTag.getStockTransActionDTO();
            stockTransActionDTO.setActionCode(null);
            stockTransActionDTO.setNote(null);

            StockTransDTO stockTransDTO = orderStaffTag.getStockTransDTO();
            stockTransDTO.setStockTransActionId(stockTransActionDTO.getStockTransActionId());
            stockTransDTO.setNote(null);

            //hoangnt: check neu NV chua co tren TTNS thi khong cho xuat kho
            StaffDTO staffDTO = staffService.findOne(stockTransDTO.getToOwnerId());
            if (DataUtil.isNullOrEmpty(staffDTO.getTtnsCode())) {
                throw new LogicException("", "export.order.staff.ttns.not.found");
            }

            BaseMessageStockTrans message = executeStockTransService.executeStockTrans(Const.STOCK_TRANS.EXPORT_NOTE, Const.STOCK_TRANS_TYPE.EXPORT, stockTransDTO, stockTransActionDTO, lsDetailDTOs, null);
            if (DataUtil.isNullObject(message) || DataUtil.isNullOrEmpty(message.getErrorCode())) {
                reportSuccess("frmExportStock:msgStaffExportStock", "export.stock.success");
                topPage();
                stockActionId = message.getStockTransActionId();
                selectedStockTrans.setStockTransStatus(Const.STOCK_TRANS_STATUS.EXPORTED);
            } else {
                throw new LogicException(message.getErrorCode(), message.getKeyMsg(), message.getParamsMsg());
            }
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("frmExportStock:msgStaffExportStock", ex);
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("frmExportStock:msgStaffExportStock", "common.error.happened", ex);
            topPage();
        }
    }

    @Secured("@")
    public StreamedContent printStockTransDetail() {
        try {
            doValidateListDetail(listProductTag.getListTransDetailDTOs());
            StockTransDTO stockTransDTO = orderStaffTag.getStockTransDTOForPint();
            if (stockTransDTO != null && selectedStockTrans.getStockTransStatus().equals(Const.STOCK_TRANS_STATUS.EXPORTED)) {
                stockTransDTO.setActionType(Const.STOCK_TRANS_ACTION_TYPE.NOTE);
                stockTransDTO.setStockTransStatus(Const.STOCK_TRANS_STATUS.EXPORTED);
//                return exportStockTransDetail(stockTransDTO, lsStockTransFull);
                return exportStockTransDetail(stockTransDTO, listProductTag.getListTransDetailDTOs());
            } else {
                throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "stockTrans.dto.print.not.exists");
            }
        } catch (LogicException ex) {
            topReportError("frmExportOrder:msgExport", ex);
        } catch (Exception ex) {
            topReportError("frmExportOrder:msgExport", "common.error.happened", ex);
        }
        return null;
    }

    @Secured("@")
    public StreamedContent printHandOver() {
        try {
            doValidateListDetail(listProductTag.getListTransDetailDTOs());
            StockTransDTO stockTransDTO = orderStaffTag.getStockTransDTOForPint();
            if (stockTransDTO != null && selectedStockTrans.getStockTransStatus().equals(Const.STOCK_TRANS_STATUS.EXPORTED)) {
                stockTransDTO.setStockTransActionId(stockActionId);
                stockTransDTO.setActionType(Const.STOCK_TRANS_ACTION_TYPE.NOTE);
                stockTransDTO.setStockTransStatus(Const.STOCK_TRANS_STATUS.EXPORTED);
                return exportHandOverReport(stockTransDTO, "XY");
            } else {
                throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "stockTrans.dto.print.not.exists");
            }
        } catch (LogicException ex) {
            topReportError("frmExportOrder:msgExport", ex);
        } catch (Exception ex) {
            topReportError("frmExportOrder:msgExport", "common.error.happened", ex);
        }
        return null;
    }

    /**
     * ham open view serial tren dialog
     *
     * @author ThanhNT77
     */
    @Secured("@")
    public void doSelectSerial() {
        try {
            String fromSerial = stockTransDetail.getFromSerial();
            String toSerial = stockTransDetail.getToSerial();
            //dau tien validate voi chon dai so
            if (!"1".equals(typePrint)) {
                if (DataUtil.isNullOrEmpty(fromSerial)) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "mn.stock.partner.serial.valid.start.require");
                }
                if (DataUtil.isNullOrEmpty(toSerial)) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "mn.stock.partner.serial.valid.end.require");
                }

                if (stockTransDetail.getQuantity().compareTo(DataUtil.safeToLong(listSerialRangeSelect.size())) <= 0) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.partner.range.limit");
                }
                //check trung lap serial voi mat hang handset
                if (getIsHandset()) {
                    for (StockTransSerialDTO serialDetail : listSerialRangeSelect) {
                        if (fromSerial.equalsIgnoreCase(serialDetail.getFromSerial()) && toSerial.equalsIgnoreCase(serialDetail.getToSerial())) {
                            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.partner.range.duplidate", fromSerial, toSerial);
                        }
                    }
                } else { //check trung lap serial voi mat hang khac
                    if (!DataUtil.validateStringByPattern(fromSerial, Const.REGEX.NUMBER_REGEX) || !DataUtil.validateStringByPattern(toSerial, Const.REGEX.NUMBER_REGEX)) {
                        throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "mn.stock.partner.serial.valid.format", stockTransDetail.getProductOfferTypeName());
                    }
                    BigInteger fromSerialNum = new BigInteger(fromSerial);
                    BigInteger toSerialNum = new BigInteger(toSerial);
                    BigInteger result = toSerialNum.subtract(fromSerialNum);
                    //neu serial ket thuc nho hon serial bat dau or serial ket thuc lon hon 500000 thi bao loi
                    if (result.compareTo(new BigInteger("0")) < 0 || result.compareTo(new BigInteger("500000")) > 0) {
                        throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "mn.stock.partner.serial.valid.range");
                    }
                    for (StockTransSerialDTO serialDetail : listSerialRangeSelect) {
                        BigInteger fromSerialCurrent = new BigInteger(serialDetail.getFromSerial());
                        BigInteger toSerialCurrent = new BigInteger(serialDetail.getToSerial());
                        if (!(toSerialNum.compareTo(fromSerialCurrent) <= 0 || fromSerialNum.compareTo(toSerialCurrent) >= 0)) {
                            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.partner.range.duplidate", fromSerial, toSerial);
                        }
                    }
                }
            } else {

            }
            //thanhnt comment  >> cho nay tuan tham khao lai code cua anh nhe > a sua lai code , tam thoi coment cua e cho khoi do
            /*List<StockTransSerialDTO> lsSelectSerialTmp = DataUtil.defaultIfNull(stockTransSerialService.getListSerialSelect(stockTransDetail, stockTransDetail.getQuantity()), new ArrayList<StockTransSerialDTO>());
            if (!DataUtil.isNullOrEmpty(lsSelectSerialTmp)) {
                listSerialRangeSelect.addAll(lsSelectSerialTmp);
            }*/
            isViewBtnExportStock = checkViewBtnExportStock();
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex);
            topPage();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            reportError("", "common.error.happened", e);
            topPage();
        }
    }

    /**
     * ham check handset  neu la hanset thi tra ve true
     *
     * @return
     * @author ThanhNT77
     */
    @Secured("@")
    public boolean getIsHandset() {
        return stockTransDetail != null && Const.STOCK_TYPE.STOCK_HANDSET.equals(stockTransDetail.getProductOfferTypeId());
    }

    /**
     * ham open view serial tren dialog
     *
     * @author ThanhNT77
     */
    @Secured("@")
    public void doRemoveSerial(int index) {
        try {
            listSerialRangeSelect.remove(index);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            reportError("", "common.error.happened", e);
            topPage();
        }
    }

    @Secured("@")
    public boolean checkViewBtnExportStock() {
        boolean result = false;
        if (!DataUtil.isNullObject(stockTransDetail)) {
            if (Const.STOCK_TYPE.STOCK_HANDSET.equals(stockTransDetail.getProductOfferTypeId())) {
                result = stockTransDetail.getQuantity() != null && listSerialRangeSelect != null && (stockTransDetail.getQuantity().intValue() == listSerialRangeSelect.size());
            } else {
                Long totalAccount = 0L;
                for (StockTransSerialDTO stockTransSerialDTO : listSerialRangeSelect) {
                    BigInteger fromSerial = new BigInteger(stockTransSerialDTO.getFromSerial());
                    BigInteger toSerial = new BigInteger(stockTransSerialDTO.getToSerial());
                    totalAccount += toSerial.subtract(fromSerial).longValue() + 1L;
                }
                result = totalAccount.equals(stockTransDetail.getQuantity());
            }
        }
        return result;
    }

    /**
     * ham open view serial tren dialog
     *
     * @author ThanhNT77
     */
    @Secured("@")
    public void doShowListSerial() {
        try {
            isViewListSerial = !isViewListSerial;
            if (DataUtil.isNullOrEmpty(listSerialView)) {
                listSerialView = DataUtil.defaultIfNull(stockTransSerialService.getRangeSerial(DataUtil.safeToLong(stockTransDetail.getFromOwnerType()), stockTransDetail.getFromOwnerId(),
                        stockTransDetail.getProdOfferId(), stockTransDetail.getTableName(), stockTransDetail.getStateId(), "", null), new ArrayList<StockTransSerialDTO>());
            }
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex);
            topPage();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            reportError("", "common.error.happened", e);
            topPage();
        }
    }

    /**
     * ham open view serial tren dialog
     *
     * @author ThanhNT77
     */
    @Secured("@")
    public void doOpenDlgSerial(int index) {
        try {
            this.index = index;
            typePrint = "1";
            isViewListSerial = false;
            listSerialRangeSelect = Lists.newArrayList();
            this.stockTransDetail = DataUtil.cloneBean(this.lsStockTransFull.get(index));
            shopInfoTagExportDlg.loadShop(DataUtil.safeToString(stockTransDetail.getFromOwnerId()), true);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            reportError("", "common.error.happened", e);
            topPage();
        }
    }

    /**
     * ham open view serial tren dialog
     *
     * @author ThanhNT77
     */
    @Secured("@")
    public void doConfirmSerial() {
        stockTransDetail.setLstSerial(listSerialRangeSelect);
        lsStockTransFull.set(index, this.stockTransDetail);
    }


    //getter and setter
    public Date getCurrentDate() {
        return Calendar.getInstance().getTime();
    }

    public boolean isInfoOrderDetail() {
        return infoOrderDetail;
    }

    public void setInfoOrderDetail(boolean infoOrderDetail) {
        this.infoOrderDetail = infoOrderDetail;
    }

    public ShopInfoNameable getShopInfoExportTag() {
        return shopInfoExportTag;
    }

    public void setShopInfoExportTag(ShopInfoNameable shopInfoExportTag) {
        this.shopInfoExportTag = shopInfoExportTag;
    }

    public StaffInfoNameable getStaffInfoReceiveTag() {
        return staffInfoReceiveTag;
    }

    public void setStaffInfoReceiveTag(StaffInfoNameable staffInfoReceiveTag) {
        this.staffInfoReceiveTag = staffInfoReceiveTag;
    }

    public VStockTransDTO getForSearch() {
        return forSearch;
    }

    public void setForSearch(VStockTransDTO forSearch) {
        this.forSearch = forSearch;
    }

    public int getLimitAutoComplete() {
        return limitAutoComplete;
    }

    public void setLimitAutoComplete(int limitAutoComplete) {
        this.limitAutoComplete = limitAutoComplete;
    }

    public byte[] getByteContent() {
        return byteContent;
    }

    public void setByteContent(byte[] byteContent) {
        this.byteContent = byteContent;
    }

    public StockTransFullDTO getStockTransDetail() {
        return stockTransDetail;
    }

    public void setStockTransDetail(StockTransFullDTO stockTransDetail) {
        this.stockTransDetail = stockTransDetail;
    }

    public RequiredRoleMap getRequiredRoleMap() {
        return requiredRoleMap;
    }

    public void setRequiredRoleMap(RequiredRoleMap requiredRoleMap) {
        this.requiredRoleMap = requiredRoleMap;
    }

    public List<VStockTransDTO> getvStockTransDTOList() {
        return vStockTransDTOList;
    }

    public void setvStockTransDTOList(List<VStockTransDTO> vStockTransDTOList) {
        this.vStockTransDTOList = vStockTransDTOList;
    }

    public List<StockTransFullDTO> getLsStockTransFull() {
        return lsStockTransFull;
    }

    public void setLsStockTransFull(List<StockTransFullDTO> lsStockTransFull) {
        this.lsStockTransFull = lsStockTransFull;
    }

    public List<StockTransSerialDTO> getListSerialView() {
        return listSerialView;
    }

    public void setListSerialView(List<StockTransSerialDTO> listSerialView) {
        this.listSerialView = listSerialView;
    }

    public List<StockTransSerialDTO> getListSerialRangeSelect() {
        return listSerialRangeSelect;
    }

    public void setListSerialRangeSelect(List<StockTransSerialDTO> listSerialRangeSelect) {
        this.listSerialRangeSelect = listSerialRangeSelect;
    }

    public boolean isViewListSerial() {
        return isViewListSerial;
    }

    public void setIsViewListSerial(boolean isViewListSerial) {
        this.isViewListSerial = isViewListSerial;
    }

    public boolean isViewBtnExportStock() {
        return isViewBtnExportStock;
    }

    public void setIsViewBtnExportStock(boolean isViewBtnExportStock) {
        this.isViewBtnExportStock = isViewBtnExportStock;
    }

    public String getTypePrint() {
        return typePrint;
    }

    public void setTypePrint(String typePrint) {
        this.typePrint = typePrint;
    }

    public List<OptionSetValueDTO> getOptionSetValueDTOsList() {
        return optionSetValueDTOsList;
    }

    public void setOptionSetValueDTOsList(List<OptionSetValueDTO> optionSetValueDTOsList) {
        this.optionSetValueDTOsList = optionSetValueDTOsList;
    }

    public StaffDTO getStaffDTO() {
        return staffDTO;
    }

    public void setStaffDTO(StaffDTO staffDTO) {
        this.staffDTO = staffDTO;
    }

    public OrderStockTagNameable getOrderStockTag() {
        return orderStockTag;
    }

    public void setOrderStockTag(OrderStockTagNameable orderStockTag) {
        this.orderStockTag = orderStockTag;
    }

    public OrderStaffTagNameable getOrderStaffTag() {
        return orderStaffTag;
    }

    public void setOrderStaffTag(OrderStaffTagNameable orderStaffTag) {
        this.orderStaffTag = orderStaffTag;
    }

    public String getAttachFileName() {
        return attachFileName;
    }

    public void setAttachFileName(String attachFileName) {
        this.attachFileName = attachFileName;
    }

    public ShopInfoNameable getShopInfoTagExportDlg() {
        return shopInfoTagExportDlg;
    }

    public void setShopInfoTagExportDlg(ShopInfoNameable shopInfoTagExportDlg) {
        this.shopInfoTagExportDlg = shopInfoTagExportDlg;
    }

    public ListFifoProductNameable getListProductTag() {
        return listProductTag;
    }

    public void setListProductTag(ListFifoProductNameable listProductTag) {
        this.listProductTag = listProductTag;
    }

    public VStockTransDTO getSelectedStockTrans() {
        return selectedStockTrans;
    }

    public void setSelectedStockTrans(VStockTransDTO selectedStockTrans) {
        this.selectedStockTrans = selectedStockTrans;
    }

    public Long getStockActionId() {
        return stockActionId;
    }

    public void setStockActionId(Long stockActionId) {
        this.stockActionId = stockActionId;
    }
}
