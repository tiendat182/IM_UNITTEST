package com.viettel.bccs.inventory.controller.stock;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.fw.controller.TransCommonController;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.common.model.FileExportBean;
import com.viettel.bccs.inventory.common.util.FileUtil;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.*;
import com.viettel.bccs.inventory.service.common.IMServiceUtil;
import com.viettel.bccs.inventory.tag.ShopInfoNameable;
import com.viettel.bccs.inventory.tag.StaffInfoNameable;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DateUtil;
import com.viettel.fw.common.util.ErrorCode;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.chart.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import java.text.Normalizer;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * controller tra cu serial don le
 *
 * @author thanhnt77
 */
@Component
@Scope("view")
@ManagedBean
public class ViewStockOfferController extends TransCommonController {

    public enum TYPE_CYCLE {
        CYCLE_0("0"), CYCLE_1("1"), CYCLE_2("2"), CYCLE_3("3"), CYCLE_4("4"), CYCLE_5("5"), CYCLE_6("6");

        TYPE_CYCLE(String val) {
            value = val;
        }

        private String value;

        public String value() {
            return value;
        }
    }

    private Date fromDate;
    private Date endDate;
    private String stateId;
    private String stateIdDlg;
    private int limitAutoComplete;
    private Long ownerType;
    private Long ownerTypeDlg;
    private StaffDTO staffDTO;

    private VShopStaffDTO vShopStaffDTO;
    private VShopStaffDTO vShopStaffDTODlg;


    private LineChartModel lineModel2;
    private ProductOfferingTotalDTO productOfferingTotalDTO = new ProductOfferingTotalDTO();
    private ProductOfferingTotalDTO productOfferingTotalDTODlg = new ProductOfferingTotalDTO();
    private ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
    private ProductOfferTypeDTO productOfferTypeDTODlg = new ProductOfferTypeDTO();

    private List<ProductOfferTypeDTO> lstProductOfferTypeDTO = Lists.newArrayList();
    private List<ProductOfferTypeDTO> lstProductOfferTypeDTODlg = Lists.newArrayList();
    private List<ProductOfferingTotalDTO> lstProductOfferingTotalDTO = Lists.newArrayList();
    private List<ProductOfferingTotalDTO> lstProductOfferingTotalDTODlg = Lists.newArrayList();
    private List<StockTotalCycleReportDTO> lsStockCycleReport;
    private List<StockTotalCycleDTO> lsStockCycle;
    private List<String> lsSerialExport;
    private List<OptionSetValueDTO> lsProductStatus;

    private Long typeCycle = -1L;

    @Autowired
    private OptionSetValueService optionSetValueService;
    @Autowired
    private ShopInfoNameable shopInfoTag;
    @Autowired
    private ShopInfoNameable shopInfoTagDlg;
    @Autowired
    private ShopService shopService;
    @Autowired
    private ProductOfferTypeService productOfferTypeService;
    @Autowired
    private StockTotalCycleService stockTotalCycleService;
    @Autowired
    private ProductOfferingService productOfferingService;
    @Autowired
    private StaffInfoNameable staffInforTag;
    @Autowired
    private StaffInfoNameable staffInforTagDlg;

    @PostConstruct
    public void init() {
        try {
            staffDTO = BccsLoginSuccessHandler.getStaffDTO();

            shopInfoTag.initShopAndAllChild(this, DataUtil.safeToString(staffDTO.getShopId()), true, Const.OWNER_TYPE.SHOP);
            shopInfoTag.loadShop(DataUtil.safeToString(staffDTO.getShopId()), false);

            staffInforTag.initStaff(this, DataUtil.safeToString(staffDTO.getShopId()));
            staffInforTagDlg.initStaff(this, DataUtil.safeToString(staffDTO.getShopId()));

            limitAutoComplete = DataUtil.safeToInt(optionSetValueService.getValueByNameFromOptionSetValue(Const.OPTION_SET.LIMIT_AUTOCOMPLETE));
            lstProductOfferTypeDTO = DataUtil.defaultIfNull(productOfferTypeService.getListProduct(), new ArrayList<>());
            lstProductOfferTypeDTO = lstProductOfferTypeDTO.stream().filter(x -> !Const.PRODUCT_OFFER_TYPE.PRODUCT_NO_SERIAL.equals(DataUtil.safeToLong(x.getProductOfferTypeId()))).collect(Collectors.toList());

            lsProductStatus = DataUtil.defaultIfNull(optionSetValueService.getByOptionSetCode(Const.OPTION_SET.PRODUCT_STATUS), new ArrayList<>());

            initInputSearch();
        } catch (Exception ex) {
            logger.error(ex);
            reportError("", "", "common.error.happen");
        }
    }

    /**
     * ham xu ly reset thong tin tim kiem
     *
     * @author thanhnt77
     */
    private void initInputSearch() throws Exception {
        endDate = Calendar.getInstance().getTime();
        fromDate = DateUtil.addDay(endDate, -10);
        productOfferingTotalDTO = new ProductOfferingTotalDTO();
        productOfferTypeDTO = new ProductOfferTypeDTO();
        lstProductOfferingTotalDTO = Lists.newArrayList();
        stateId = DataUtil.safeToString(Const.GOODS_STATE.NEW);
        lineModel2 = null;
        lsStockCycleReport = Lists.newArrayList();
        typeCycle = -1L;
        ownerType = Const.OWNER_TYPE.SHOP_LONG;
        this.vShopStaffDTO = shopService.getShopByShopId(staffDTO.getShopId());
        shopInfoTag.loadShop(DataUtil.safeToString(staffDTO.getShopId()), false);
    }

    /**
     * thay doi loai kho xuat o vung tim kiem
     *
     * @author thanhnt77
     */
    @Secured("@")
    public void changeOwnerTypeSearch() {
        try {
            this.vShopStaffDTO = null;
            if (Const.OWNER_TYPE.SHOP_LONG.equals(this.ownerType)) {
                shopInfoTag.resetShop();
                shopInfoTag.loadShop(DataUtil.safeToString(staffDTO.getShopId()), false);
                this.vShopStaffDTO = shopService.getShopByShopId(staffDTO.getShopId());
            } else if (Const.OWNER_TYPE.STAFF_LONG.equals(this.ownerType)) {
                staffInforTag.resetProduct();
                this.vShopStaffDTO = staffService.findStaffById(DataUtil.safeToString(staffDTO.getStaffId()));
                staffInforTag.loadStaff(this.vShopStaffDTO);
            }
        } catch (Exception ex) {
            logger.error(ex);
            reportError("", "", "common.error.happen");
        }
    }

    /**
     * thay doi loai kho xuat o vung tim kiem
     *
     * @author thanhnt77
     */
    @Secured("@")
    public void changeOwnerTypeSearchDlg() {
        try {
            this.vShopStaffDTODlg = null;
            if (Const.OWNER_TYPE.SHOP_LONG.equals(this.ownerTypeDlg)) {
                shopInfoTagDlg.resetShop();
                this.vShopStaffDTODlg = shopService.getShopByShopId(staffDTO.getShopId());
                shopInfoTagDlg.loadShop(DataUtil.safeToString(staffDTO.getShopId()), false);
            } else if (Const.OWNER_TYPE.STAFF_LONG.equals(this.ownerTypeDlg)) {
                staffInforTagDlg.resetProduct();
                this.vShopStaffDTODlg = staffService.findStaffById(DataUtil.safeToString(staffDTO.getStaffId()));
                staffInforTagDlg.loadStaff(this.vShopStaffDTODlg);
            }
        } catch (Exception ex) {
            logger.error(ex);
            reportError("", "", "common.error.happen");
        }
    }

    /**
     * init reset dialog
     */
    private void initDialog() {
        productOfferingTotalDTODlg = new ProductOfferingTotalDTO();
        productOfferTypeDTODlg = new ProductOfferTypeDTO();
        lsSerialExport = Lists.newArrayList();
        this.vShopStaffDTODlg = null;
        shopInfoTagDlg.resetShop();
        staffInforTagDlg.resetProduct();
        ownerTypeDlg = ownerType;
        changeOwnerTypeSearchDlg();

    }

    /**
     * ham mo diloag
     *
     * @author thanhnt77
     */
    public void doOpenDialog() {
        try {
            shopInfoTagDlg.initShopAndAllChild(this, DataUtil.safeToString(BccsLoginSuccessHandler.getStaffDTO().getShopId()), true, Const.OWNER_TYPE.SHOP);
            lstProductOfferTypeDTODlg = productOfferTypeService.getListProduct();
            lstProductOfferTypeDTODlg.removeIf(x -> Const.STOCK_TYPE.STOCK_ISDN_MOBILE.equals(x.getProductOfferTypeId())
                    || Const.STOCK_TYPE.STOCK_ISDN_HOMEPHONE.equals(x.getProductOfferTypeId())
                    || Const.STOCK_TYPE.STOCK_ISDN_PSTN.equals(x.getProductOfferTypeId()));
            initDialog();
            copySelectedDialog();
        } catch (Exception ex) {
            logger.error(ex);
            reportError("", "", "common.error.happen");
        }
    }

    private void copySelectedDialog() {
        ownerTypeDlg = ownerType;
        if (this.vShopStaffDTO != null) {
            this.vShopStaffDTODlg = DataUtil.cloneBean(this.vShopStaffDTO);
            if (Const.OWNER_TYPE.SHOP_LONG.equals(this.ownerTypeDlg)) {
                shopInfoTagDlg.resetShop();
                shopInfoTagDlg.loadShop(vShopStaffDTODlg.getOwnerId(), false);
            } else if (Const.OWNER_TYPE.STAFF_LONG.equals(this.ownerTypeDlg)) {
                staffInforTagDlg.resetProduct();
                staffInforTagDlg.loadStaff(this.vShopStaffDTODlg);
            }
        }

        if (!DataUtil.isNullOrEmpty(this.stateId)) {
            this.stateIdDlg = stateId;
        }
        if (this.productOfferTypeDTO != null) {
            this.productOfferTypeDTODlg = DataUtil.cloneBean(this.productOfferTypeDTO);
        }
        if (this.productOfferingTotalDTO != null) {
            this.productOfferingTotalDTODlg = DataUtil.cloneBean(this.productOfferingTotalDTO);
        }
    }

    /**
     * ham reset dialog
     *
     * @author thanhnt77
     */
    @Secured("@")
    public void doResetDialog() {
        initDialog();
    }

    /**
     * ham xu ly tim kiem bieu do
     *
     * @author thanhnt77
     */
    @Secured("@")
    public void doSearchCycle() {
        try {
            lineModel2 = null;
            if (fromDate == null) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.from.date.not.blank");
            }
            if (endDate == null) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.to.date.not.blank");
            }
            if ((DateUtil.compareDateToDate(fromDate, endDate) > 0) || DateUtil.daysBetween2Dates(endDate, fromDate) > 10L) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "view.stock.offer.cycel.fromDate.endDate", 10);
            }

            lineModel2 = searchChart();
            if (lineModel2 != null) {
                lineModel2.setTitle(getText("view.stock.offer.cycel.header.title"));
                lineModel2.setShowPointLabels(true);
                lineModel2.getAxes().put(AxisType.X, new CategoryAxis(getText("view.stock.offer.cycel")));
                lineModel2.setAnimate(true);
                lineModel2.setBreakOnNull(true);
                lineModel2.setDatatipFormat("");

                Axis yAxis = lineModel2.getAxis(AxisType.Y);
                yAxis.setLabel(getText("view.stock.offer.cycel.number.offer"));
                yAxis.setMin(0);
            }
            if (lineModel2 == null) {
                reportError("", ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.import.note.stock.trans.not.exist");
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
     * ham xu ly xuat file excel
     *
     * @author thanhnt77
     */
    public void doExportStockTotalCycle() {
        try {
            lineModel2 = null;
            if (fromDate == null) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.from.date.not.blank");
            }
            if (endDate == null) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.to.date.not.blank");
            }
            if ((DateUtil.compareDateToDate(fromDate, endDate) > 0) || DateUtil.daysBetween2Dates(endDate, fromDate) > 30L) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "view.stock.offer.cycel.fromDate.endDate", 30);
            }

            if (this.vShopStaffDTO == null) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "mn.stock.partner.warehouse.not.emty");
            }

            Long offerTypeId = productOfferTypeDTO == null || DataUtil.safeEqual(productOfferTypeDTO.getProductOfferTypeId(), -1L) ? null : productOfferTypeDTO.getProductOfferTypeId();
            Long offerId = productOfferingTotalDTO != null ? productOfferingTotalDTO.getProductOfferingId() : null;
            lsStockCycle = DataUtil.defaultIfNull(stockTotalCycleService.getStockCycleExport(fromDate, endDate, DataUtil.isNullOrEmpty(stateId) ? null : DataUtil.safeToLong(stateId),
                    offerTypeId, offerId, DataUtil.safeToLong(vShopStaffDTO.getOwnerType()), DataUtil.safeToLong(vShopStaffDTO.getOwnerId())), new ArrayList<>());
            if (DataUtil.isNullOrEmpty(lsStockCycle)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.import.note.stock.trans.not.exist");
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
     * ham xu ly xuat file excel
     *
     * @author thanhnt77
     */
    public StreamedContent exportStockTotalCycle() {
        try {
            Date currentDate = getSysdateFromDB();
            FileExportBean fileExportBean = new FileExportBean();
            fileExportBean.setTempalatePath(FileUtil.getTemplatePath());
            fileExportBean.setTemplateName(Const.REPORT_TEMPLATE.STOCK_TRANS_TEMPLATE_SUB_PATH + Const.FILE_TEMPLATE.TEMPLATE_STOCK_CYCLE_MAU_01);
            fileExportBean.setOutPath(FileUtil.getOutputPath());
            fileExportBean.setOutName(BccsLoginSuccessHandler.getStaffDTO().getStaffCode()
                    + "_" + DateUtil.dateTime2StringNoSlash(currentDate) + "_" + Const.FILE_TEMPLATE.TEMPLATE_STOCK_CYCLE_MAU_01);
            fileExportBean.putValue("lsStockCycle", lsStockCycle);
            fileExportBean.putValue("reportDate", DateUtil.date2ddMMyyyyString(currentDate));
            return FileUtil.exportToStreamed(fileExportBean);
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex);
            topPage();
        } catch (Exception e) {
            logger.error(e);
            reportError("", "common.error.happened", e);
            topPage();
        }
        return null;
    }

    private String getStrStateName(String stateId) {
        if (!DataUtil.isNullOrEmpty(lsProductStatus)) {
            for (OptionSetValueDTO status : lsProductStatus) {
                if (status.getValue().equals(stateId)) {
                    return status.getName();
                }
            }
        }
        return "";
    }

    /**
     * ham xu ly xuat file excel
     *
     * @author thanhnt77
     */
    public void doExportStockTotalCycleDlg() {
        try {
            if (vShopStaffDTODlg == null) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "mn.stock.partner.warehouse.not.emty");
            }

            if (productOfferTypeDTODlg == null || productOfferTypeDTODlg.getProductOfferTypeId() == null || DataUtil.safeEqual(productOfferTypeDTODlg.getProductOfferTypeId(), -1L)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "mn.stock.branch.type.product.msg.require");
            }

            String tableName = IMServiceUtil.getTableNameByOfferType(productOfferTypeDTODlg.getProductOfferTypeId());

            Long offerId = productOfferingTotalDTODlg != null && productOfferingTotalDTODlg.getProductOfferingId() != null ? productOfferingTotalDTODlg.getProductOfferingId() : null;

            Long dayStockRemain = null;
            if (offerId != null) {
                ProductOfferingDTO productOfferingDTO = productOfferingService.findOne(offerId);
                if (productOfferingDTO == null || !Const.STATUS.ACTIVE.equals(productOfferingDTO.getStatus())) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "input.list.product.file.prod.not.valid");
                }
                dayStockRemain = productOfferingDTO.getDayStockRemain();
            }

            lsSerialExport = stockTotalCycleService.getListSerialStockXByDayRemain(offerId, DataUtil.safeToLong(vShopStaffDTODlg.getOwnerId()),
                    DataUtil.safeToLong(vShopStaffDTODlg.getOwnerType()), typeCycle, tableName, dayStockRemain, DataUtil.safeToLong(stateIdDlg));
            if (DataUtil.isNullOrEmpty(lsSerialExport)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "mn.stock.status.isdn.export.nodata");
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
     * ham hien thi title header
     *
     * @return
     * @author thanhnt77
     */
    public String getTitleHeader() {
        if (Const.STOCK_CYCLE_TYPE.EXIST_IN_CYCLE.equals(typeCycle)) {
            return getText("view.stock.offer.cycel.number.offer.quantity.exist");
        } else if (Const.STOCK_CYCLE_TYPE.EXIST_IN_CYCLE1.equals(typeCycle)) {
            return getText("view.stock.offer.cycel.number.offer.quantity1.exist");
        } else if (Const.STOCK_CYCLE_TYPE.EXIST_IN_CYCLE2.equals(typeCycle)) {
            return getText("view.stock.offer.cycel.number.offer.quantity2.exist");
        } else if (Const.STOCK_CYCLE_TYPE.EXIST_IN_CYCLE3.equals(typeCycle)) {
            return getText("view.stock.offer.cycel.number.offer.quantity3.exist");
        } else if (Const.STOCK_CYCLE_TYPE.EXIST_IN_CYCLE4.equals(typeCycle)) {
            return getText("view.stock.offer.cycel.number.offer.quantity4.exist");
        } else if (Const.STOCK_CYCLE_TYPE.EXIST_IN_CYCLE4_OVER.equals(typeCycle)) {
            return getText("view.stock.offer.cycel.number.offer.quantityOver.exist");
        } else if (Const.STOCK_CYCLE_TYPE.EXIST_IN_CYCLE_TOTAL.equals(typeCycle)) {
            return getText("view.stock.offer.cycel.number.offer.quantity.total.exist");
        }
        return getText("view.stock.offer.cycel.number.offer");
    }

    /**
     * ham xu ly xuat file excel
     *
     * @author thanhnt77
     */
    public StreamedContent exportStockTotalCycleDlg() {
        try {
            if (!DataUtil.isNullOrEmpty(lsSerialExport)) {
                String outName = this.vShopStaffDTODlg != null ? this.vShopStaffDTODlg.getOwnerCode() : "";
                outName += "_" + (productOfferingTotalDTODlg != null ? DataUtil.safeToString(productOfferingTotalDTODlg.getCode()) : "");
                outName += "_" + IMServiceUtil.unAccent(getTitleHeader());
                outName += "_" + Const.EXPORT_FILE_NAME.LIST_SERIAL_NAME;
                return FileUtil.exportToStreamedText(lsSerialExport, outName);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            topReportError("", "common.error.happened", ex);
        }
        return null;
    }

    /**
     * ham check dieu kien hien thi xuat bao cao
     *
     * @return
     * @author thanhnt77
     */
    public boolean isShowExportStockCycle() {
        return !DataUtil.isNullOrEmpty(lsStockCycle);
    }

    /**
     * ham xu ly tim kiem bieu do
     *
     * @author thanhnt77
     */
    @Secured("@")
    public void doResetCycle() {
        try {
            initInputSearch();
            shopInfoTag.loadShop(DataUtil.safeToString(BccsLoginSuccessHandler.getStaffDTO().getShopId()), false);
        } catch (LogicException e) {
            logger.error(e.getMessage(), e);
            topReportError("", e.getErrorCode(), e.getKeyMsg());
        } catch (Exception e) {
            topReportError("", "common.error.happened", e);
        }
    }

    /**
     * Load danh sach mat hang theo loai hang hoa
     */
    @Secured("@")
    public void onChangeOfferType() {
        lstProductOfferingTotalDTO = Lists.newArrayList();
    }

    /**
     * Load danh sach mat hang theo loai hang hoa
     */
    @Secured("@")
    public void onChangeOfferTypeDlg() {
        lstProductOfferingTotalDTODlg = Lists.newArrayList();
        this.productOfferingTotalDTODlg = new ProductOfferingTotalDTO();
    }

    /**
     * Tim kiem mat hang khi nhap tay
     */
    @Secured("@")
    public List<ProductOfferingTotalDTO> searchListProductOffering(String input) {
        try {
            lstProductOfferingTotalDTO = Lists.newArrayList();
            if (productOfferTypeDTO.getProductOfferTypeId() != null || productOfferTypeDTO.getProductOfferTypeId().equals(-1L)) {
                CharSequence inputData = DataUtil.isNullOrEmpty(input) ? "" : input.trim().toUpperCase();
                lstProductOfferingTotalDTO = productOfferingService.getListProductOfferingByProductType(inputData.toString(), productOfferTypeDTO.getProductOfferTypeId());
            }
        } catch (LogicException e) {
            logger.error(e.getMessage(), e);
            topReportError("", e.getErrorCode(), e.getKeyMsg());
        } catch (Exception e) {
            topReportError("", "common.error.happened", e);
        }
        return lstProductOfferingTotalDTO;
    }

    /**
     * Tim kiem mat hang khi nhap tay
     */
    @Secured("@")
    public List<ProductOfferingTotalDTO> searchListProductOfferingDlg(String input) {
        try {
            lstProductOfferingTotalDTODlg = Lists.newArrayList();
            if (productOfferTypeDTODlg != null && !DataUtil.safeEqual(productOfferTypeDTODlg.getProductOfferTypeId(), -1L)) {
                CharSequence inputData = DataUtil.isNullOrEmpty(input) ? "" : input.trim().toUpperCase();
                lstProductOfferingTotalDTODlg = productOfferingService.getListProductOfferingByProductType(inputData.toString(), productOfferTypeDTODlg.getProductOfferTypeId());
            }
        } catch (LogicException e) {
            logger.error(e.getMessage(), e);
            topReportError("", e.getErrorCode(), e.getKeyMsg());
        } catch (Exception e) {
            topReportError("", "common.error.happened", e);
        }
        return lstProductOfferingTotalDTODlg;
    }

    @Secured("@")
    public void doSelectProductOffering() {

    }

    /**
     * reset mat hang da chon
     */
    @Secured("@")
    public void resetLstProductOfferingTotal() {
        productOfferingTotalDTO = null;
    }

    /**
     * reset mat hang da chon
     */
    @Secured("@")
    public void resetLstProductOfferingTotalDlg() {
        productOfferingTotalDTODlg = null;
    }

    @Secured("@")
    public void receiveShop(VShopStaffDTO vShopStaffDTO) {
        this.vShopStaffDTO = vShopStaffDTO;
    }

    @Secured("@")
    public void clearShop() {
        this.vShopStaffDTO = null;
        shopInfoTag.resetShop();
    }

    @Secured("@")
    public void receiveStaff(VShopStaffDTO vShopStaffDTO) {
        this.vShopStaffDTO = vShopStaffDTO;
    }

    @Secured("@")
    public void clearStaff() {
        this.vShopStaffDTO = null;
        staffInforTag.resetProduct();
    }

    @Secured("@")
    public void receiveShopDlg(VShopStaffDTO vShopStaffDTO) {
        this.vShopStaffDTODlg = vShopStaffDTO;
    }

    @Secured("@")
    public void clearShopDlg() {
        this.vShopStaffDTODlg = null;
        shopInfoTagDlg.resetShop();
    }

    @Secured("@")
    public void receiveStaffDlg(VShopStaffDTO vShopStaffDTO) {
        this.vShopStaffDTODlg = vShopStaffDTO;
    }

    @Secured("@")
    public void clearStaffDlg() {
        this.vShopStaffDTODlg = null;
        staffInforTagDlg.resetProduct();
    }

    @Secured("@")
    public void exportSerialByTypeCycle(Long typeCycle) {
        this.typeCycle = typeCycle;
        doOpenDialog();
    }

    /**
     * ham xu ly khoi tao bieu do
     *
     * @return
     * @author thanhnt77
     */
    private LineChartModel searchChart() throws LogicException, Exception {
        Map<String, List<StockTotalCycleReportDTO>> mapByDate = initMapStockCycleByDate();
        LineChartModel model = new LineChartModel();
        model.setSeriesColors("58BA27,FFCC33,F5A9F2,F74A4A,F52F2F,A30303,0040FF");
        Map<String, List<CycleDTO>> mapCycleByType = convertToMapCycleByType(mapByDate);
        ChartSeries chart;
        for (String s : mapCycleByType.keySet()) {
            List<CycleDTO> lsCycle = mapCycleByType.get(s);
            if (lsCycle != null) {
                Collections.sort(lsCycle, new Comparator<CycleDTO>() {
                    @Override
                    public int compare(CycleDTO o1, CycleDTO o2) {
                        return DateUtil.string2Date(o1.getCreateDate()).compareTo(DateUtil.string2Date(o2.getCreateDate()));
                    }
                });
                chart = new ChartSeries();
                chart.setLabel(lsCycle.get(0).getLable());

                for (CycleDTO cycleDTO : lsCycle) {
                    chart.set(cycleDTO.getCreateDate(), DataUtil.safeToInt(cycleDTO.getQuantity()));
                }
                model.addSeries(chart);

            }
        }
        return mapByDate.size() > 0 ? model : null;
    }

    /**
     * ham xu ly khoi tao du lieu bieu do phan loai theo date
     *
     * @return
     * @author thanhnt77
     */
    private Map<String, List<StockTotalCycleReportDTO>> initMapStockCycleByDate() throws LogicException, Exception {
        Map<String, List<StockTotalCycleReportDTO>> mapData = Maps.newHashMap();

        Long offerTypeId = productOfferTypeDTO == null || DataUtil.safeEqual(productOfferTypeDTO.getProductOfferTypeId(), -1L) ? null : productOfferTypeDTO.getProductOfferTypeId();
        Long offerId = productOfferingTotalDTO != null ? productOfferingTotalDTO.getProductOfferingId() : null;
        lsStockCycleReport = DataUtil.defaultIfNull(stockTotalCycleService.getStockCycle(fromDate, endDate, DataUtil.isNullOrEmpty(stateId) ? null : DataUtil.safeToLong(stateId),
                offerTypeId, offerId, DataUtil.safeToLong(vShopStaffDTO.getOwnerType()), DataUtil.safeToLong(this.vShopStaffDTO.getOwnerId())), new ArrayList<>());
        for (StockTotalCycleReportDTO stockTotalCycleDTO : lsStockCycleReport) {
            if (!mapData.containsKey(stockTotalCycleDTO.getCreateDatetime())) {
                mapData.put(stockTotalCycleDTO.getCreateDatetime(), Lists.newArrayList(stockTotalCycleDTO));
            } else {
                mapData.get(stockTotalCycleDTO.getCreateDatetime()).add(stockTotalCycleDTO);
            }
        }
        return mapData;
    }

    /**
     * ham chuyen doi map du lieu cycle tu kieu phan loai theo date sang phan loai theo chu ky
     *
     * @param mapByDate
     * @return
     * @author thanhnt77
     */
    public Map<String, List<CycleDTO>> convertToMapCycleByType(Map<String, List<StockTotalCycleReportDTO>> mapByDate) {
        Map<String, List<CycleDTO>> mapCycle = Maps.newHashMap();
        for (String strDate : mapByDate.keySet()) {
            //lay danh sach bao cao theo ngay
            List<StockTotalCycleReportDTO> lsStockCycle = mapByDate.get(strDate);
            for (StockTotalCycleReportDTO stock : lsStockCycle) {


                CycleDTO cycleDTO0 = new CycleDTO(TYPE_CYCLE.CYCLE_0.value, stock.getQuantityCycle(), stock.getCreateDatetime(), getText("view.stock.offer.cycel.number.offer.quantity.exist"));
                CycleDTO cycleDTO1 = new CycleDTO(TYPE_CYCLE.CYCLE_1.value, stock.getQuantityCycle1(), stock.getCreateDatetime(), getText("view.stock.offer.cycel.number.offer.quantity1.exist"));
                CycleDTO cycleDTO2 = new CycleDTO(TYPE_CYCLE.CYCLE_2.value, stock.getQuantityCycle2(), stock.getCreateDatetime(), getText("view.stock.offer.cycel.number.offer.quantity2.exist"));
                CycleDTO cycleDTO3 = new CycleDTO(TYPE_CYCLE.CYCLE_3.value, stock.getQuantityCycle3(), stock.getCreateDatetime(), getText("view.stock.offer.cycel.number.offer.quantity3.exist"));
                CycleDTO cycleDTO4 = new CycleDTO(TYPE_CYCLE.CYCLE_4.value, stock.getQuantityCycle4(), stock.getCreateDatetime(), getText("view.stock.offer.cycel.number.offer.quantity4.exist"));
                CycleDTO cycleDTO5 = new CycleDTO(TYPE_CYCLE.CYCLE_5.value, stock.getQuantityOver(), stock.getCreateDatetime(), getText("view.stock.offer.cycel.number.offer.quantityOver.exist"));
                CycleDTO cycleDTO6 = new CycleDTO(TYPE_CYCLE.CYCLE_6.value, stock.getQuantity(), stock.getCreateDatetime(), getText("view.stock.offer.cycel.number.offer.quantity.total.exist"));

                if (!mapCycle.containsKey(TYPE_CYCLE.CYCLE_0.value)) {
                    mapCycle.put(TYPE_CYCLE.CYCLE_0.value, Lists.newArrayList(cycleDTO0));
                } else {
                    mapCycle.get(TYPE_CYCLE.CYCLE_0.value).add(cycleDTO0);
                }
                if (!mapCycle.containsKey(TYPE_CYCLE.CYCLE_1.value)) {
                    mapCycle.put(TYPE_CYCLE.CYCLE_1.value, Lists.newArrayList(cycleDTO1));
                } else {
                    mapCycle.get(TYPE_CYCLE.CYCLE_1.value).add(cycleDTO1);
                }
                if (!mapCycle.containsKey(TYPE_CYCLE.CYCLE_2.value)) {
                    mapCycle.put(TYPE_CYCLE.CYCLE_2.value, Lists.newArrayList(cycleDTO2));
                } else {
                    mapCycle.get(TYPE_CYCLE.CYCLE_2.value).add(cycleDTO2);
                }
                if (!mapCycle.containsKey(TYPE_CYCLE.CYCLE_3.value)) {
                    mapCycle.put(TYPE_CYCLE.CYCLE_3.value, Lists.newArrayList(cycleDTO3));
                } else {
                    mapCycle.get(TYPE_CYCLE.CYCLE_3.value).add(cycleDTO3);
                }
                if (!mapCycle.containsKey(TYPE_CYCLE.CYCLE_4.value)) {
                    mapCycle.put(TYPE_CYCLE.CYCLE_4.value, Lists.newArrayList(cycleDTO4));
                } else {
                    mapCycle.get(TYPE_CYCLE.CYCLE_4.value).add(cycleDTO4);
                }
                if (!mapCycle.containsKey(TYPE_CYCLE.CYCLE_5.value)) {
                    mapCycle.put(TYPE_CYCLE.CYCLE_5.value, Lists.newArrayList(cycleDTO5));
                } else {
                    mapCycle.get(TYPE_CYCLE.CYCLE_5.value).add(cycleDTO5);
                }
                if (!mapCycle.containsKey(TYPE_CYCLE.CYCLE_6.value)) {
                    mapCycle.put(TYPE_CYCLE.CYCLE_6.value, Lists.newArrayList(cycleDTO6));
                } else {
                    mapCycle.get(TYPE_CYCLE.CYCLE_6.value).add(cycleDTO6);
                }
            }
        }
        return mapCycle;
    }

    //getter and setter
    public Long randLong(int min, int max) {
        Random rand = new Random();
        return DataUtil.safeToLong(rand.nextInt((max - min) + 1) + min);

    }

    public LineChartModel getLineModel2() {
        return lineModel2;
    }

    public void setLineModel2(LineChartModel lineModel2) {
        this.lineModel2 = lineModel2;
    }

    public ShopInfoNameable getShopInfoTag() {
        return shopInfoTag;
    }

    public void setShopInfoTag(ShopInfoNameable shopInfoTag) {
        this.shopInfoTag = shopInfoTag;
    }

    public List<ProductOfferTypeDTO> getLstProductOfferTypeDTO() {
        return lstProductOfferTypeDTO;
    }

    public void setLstProductOfferTypeDTO(List<ProductOfferTypeDTO> lstProductOfferTypeDTO) {
        this.lstProductOfferTypeDTO = lstProductOfferTypeDTO;
    }

    public ProductOfferingTotalDTO getProductOfferingTotalDTO() {
        return productOfferingTotalDTO;
    }

    public void setProductOfferingTotalDTO(ProductOfferingTotalDTO productOfferingTotalDTO) {
        this.productOfferingTotalDTO = productOfferingTotalDTO;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public int getLimitAutoComplete() {
        return limitAutoComplete;
    }

    public void setLimitAutoComplete(int limitAutoComplete) {
        this.limitAutoComplete = limitAutoComplete;
    }

    public ProductOfferTypeDTO getProductOfferTypeDTO() {
        return productOfferTypeDTO;
    }

    public void setProductOfferTypeDTO(ProductOfferTypeDTO productOfferTypeDTO) {
        this.productOfferTypeDTO = productOfferTypeDTO;
    }

    public List<ProductOfferingTotalDTO> getLstProductOfferingTotalDTO() {
        return lstProductOfferingTotalDTO;
    }

    public void setLstProductOfferingTotalDTO(List<ProductOfferingTotalDTO> lstProductOfferingTotalDTO) {
        this.lstProductOfferingTotalDTO = lstProductOfferingTotalDTO;
    }

    public class CycleDTO {
        private String typeQuantity;
        private Long quantity;
        private String createDate;
        private String lable;

        public CycleDTO() {

        }

        public CycleDTO(String typeQuantity, Long quantity, String createDate, String lable) {
            this.typeQuantity = typeQuantity;
            this.quantity = quantity;
            this.createDate = createDate;
            this.lable = lable;
        }

        public String getTypeQuantity() {
            return typeQuantity;
        }

        public void setTypeQuantity(String typeQuantity) {
            this.typeQuantity = typeQuantity;
        }

        public Long getQuantity() {
            return quantity;
        }

        public void setQuantity(Long quantity) {
            this.quantity = quantity;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public String getLable() {
            return lable;
        }

        public void setLable(String lable) {
            this.lable = lable;
        }
    }

    public Long getTypeCycle() {
        return typeCycle;
    }

    public void setTypeCycle(Long typeCycle) {
        this.typeCycle = typeCycle;
    }

    public ShopInfoNameable getShopInfoTagDlg() {
        return shopInfoTagDlg;
    }

    public void setShopInfoTagDlg(ShopInfoNameable shopInfoTagDlg) {
        this.shopInfoTagDlg = shopInfoTagDlg;
    }

    public ProductOfferTypeDTO getProductOfferTypeDTODlg() {
        return productOfferTypeDTODlg;
    }

    public void setProductOfferTypeDTODlg(ProductOfferTypeDTO productOfferTypeDTODlg) {
        this.productOfferTypeDTODlg = productOfferTypeDTODlg;
    }

    public ProductOfferingTotalDTO getProductOfferingTotalDTODlg() {
        return productOfferingTotalDTODlg;
    }

    public void setProductOfferingTotalDTODlg(ProductOfferingTotalDTO productOfferingTotalDTODlg) {
        this.productOfferingTotalDTODlg = productOfferingTotalDTODlg;
    }

    public String getStateIdDlg() {
        return stateIdDlg;
    }

    public void setStateIdDlg(String stateIdDlg) {
        this.stateIdDlg = stateIdDlg;
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

    public Long getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(Long ownerType) {
        this.ownerType = ownerType;
    }

    public StaffInfoNameable getStaffInforTag() {
        return staffInforTag;
    }

    public void setStaffInforTag(StaffInfoNameable staffInforTag) {
        this.staffInforTag = staffInforTag;
    }

    public StaffInfoNameable getStaffInforTagDlg() {
        return staffInforTagDlg;
    }

    public void setStaffInforTagDlg(StaffInfoNameable staffInforTagDlg) {
        this.staffInforTagDlg = staffInforTagDlg;
    }

    public Long getOwnerTypeDlg() {
        return ownerTypeDlg;
    }

    public void setOwnerTypeDlg(Long ownerTypeDlg) {
        this.ownerTypeDlg = ownerTypeDlg;
    }

    public List<OptionSetValueDTO> getLsProductStatus() {
        return lsProductStatus;
    }

    public void setLsProductStatus(List<OptionSetValueDTO> lsProductStatus) {
        this.lsProductStatus = lsProductStatus;
    }
}

