package com.viettel.bccs.inventory.controller.stock;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.common.model.FileExportBean;
import com.viettel.bccs.inventory.common.util.FileUtil;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.StockDebitService;
import com.viettel.bccs.inventory.service.StockTransSerialService;
import com.viettel.bccs.inventory.service.StockTransService;
import com.viettel.bccs.inventory.service.StockTransVofficeService;
import com.viettel.bccs.inventory.tag.ShopInfoNameable;
import com.viettel.bccs.inventory.tag.StaffInfoNameable;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.voffice.autosign.Ver3AutoSign;
import com.viettel.voffice.autosign.ws.FileAttachTranfer;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Component
@Scope("view")
@ManagedBean(name = "manageTransWarehouseController")
public class ManageTransWarehouseController extends ExportFileStockTransController {

    //<editor-fold defaultstate="collapsed" desc="Khai báo">
    @Autowired
    private StockTransService transService;
    @Autowired
    private StockDebitService debitService;

    @Autowired
    private StockTransSerialService stockTransSerialService;
    @Autowired
    private StockTransVofficeService stockTransVofficeService;


    private boolean detailTrans;
    private List<VStockTransDTO> stockTransDTOs;
    private List<VStockTransDTO> selectedStockTransDTOs;
    private VStockTransDTO selectedStockTransDTO;
    private List<OptionSetValueDTO> stockTransStatus;
    private HandleControlsBean handleControlsBean;
    private List<StockTransFullDTO> stockTransDetailDTOs;
    private List<StockDebitDTO> stockDebitDTOs;
    private List<StockTransSerialDTO> stockTransSerialDTOs;
    private StockTransFullDTO selectedStockTransDetail;

    @Autowired
    private ShopInfoNameable shopInfoTag;

    @Autowired
    private StaffInfoNameable staffInfoTag;

    @Autowired
    private Ver3AutoSign ver3AutoSign;


    @PostConstruct
    public void init() {
        initControl();
    }

    public StockTransFullDTO getSelectedStockTransDetail() {
        return selectedStockTransDetail;
    }

    public void setSelectedStockTransDetail(StockTransFullDTO selectedStockTransDetail) {
        this.selectedStockTransDetail = selectedStockTransDetail;
    }

    public List<StockTransSerialDTO> getStockTransSerialDTOs() {
        return stockTransSerialDTOs;
    }

    public void setStockTransSerialDTOs(List<StockTransSerialDTO> stockTransSerialDTOs) {
        this.stockTransSerialDTOs = stockTransSerialDTOs;
    }

    public List<StockDebitDTO> getStockDebitDTOs() {
        return stockDebitDTOs;
    }

    public void setStockDebitDTOs(List<StockDebitDTO> stockDebitDTOs) {
        this.stockDebitDTOs = stockDebitDTOs;
    }

    public List<StockTransFullDTO> getStockTransDetailDTOs() {
        return stockTransDetailDTOs;
    }

    public void setStockTransDetailDTOs(List<StockTransFullDTO> stockTransDetailDTOs) {
        this.stockTransDetailDTOs = stockTransDetailDTOs;
    }

    public HandleControlsBean getHandleControlsBean() {
        return handleControlsBean;
    }

    public void setHandleControlsBean(HandleControlsBean handleControlsBean) {
        this.handleControlsBean = handleControlsBean;
    }

    public ShopInfoNameable getShopInfoTag() {
        return shopInfoTag;
    }

    public void setShopInfoTag(ShopInfoNameable shopInfoTag) {
        this.shopInfoTag = shopInfoTag;
    }

    public StaffInfoNameable getStaffInfoTag() {
        return staffInfoTag;
    }

    public void setStaffInfoTag(StaffInfoNameable staffInfoTag) {
        this.staffInfoTag = staffInfoTag;
    }

    public List<OptionSetValueDTO> getStockTransStatus() {
        return stockTransStatus;
    }

    public void setStockTransStatus(List<OptionSetValueDTO> stockTransStatus) {
        this.stockTransStatus = stockTransStatus;
    }

    public VStockTransDTO getSelectedStockTransDTO() {
        return selectedStockTransDTO;
    }

    public void setSelectedStockTransDTO(VStockTransDTO selectedStockTransDTO) {
        this.selectedStockTransDTO = selectedStockTransDTO;
    }

    public List<VStockTransDTO> getSelectedStockTransDTOs() {
        return selectedStockTransDTOs;
    }

    public void setSelectedStockTransDTOs(List<VStockTransDTO> selectedStockTransDTOs) {
        this.selectedStockTransDTOs = selectedStockTransDTOs;
    }

    public List<VStockTransDTO> getStockTransDTOs() {
        return stockTransDTOs;
    }

    public void setStockTransDTOs(List<VStockTransDTO> stockTransDTOs) {
        this.stockTransDTOs = stockTransDTOs;
    }

    public boolean isDetailTrans() {
        return detailTrans;
    }

    public void setDetailTrans(boolean detailTrans) {
        this.detailTrans = detailTrans;
    }
    //</editor-fold>

    private void initControl() {
        try {
            Date sys = optionSetValueService.getSysdateFromDB(true);
            shopInfoTag.initShopAndAllChild(this, DataUtil.safeToString(getStaffDTO().getShopId()), true, Const.OWNER_TYPE.SHOP);
            //
            StaffTagConfigDTO staffTagConfigDTO = new StaffTagConfigDTO("", DataUtil.safeToString(getStaffDTO().getShopId()), null);
            staffInfoTag.init(this, staffTagConfigDTO);
            //
            handleControlsBean = new HandleControlsBean();
            handleControlsBean.setFromDate(sys);
            handleControlsBean.setToDate(sys);
            //
            stockTransStatus = getOptionValByCode(Const.OPTION_SET.STOCK_TRANS_STATUS);
            stockTransDTOs = Lists.newArrayList();
            detailTrans = false;
            stockTransDetailDTOs = Lists.newArrayList();
            selectedStockTransDTOs = Lists.newArrayList();
            executeCommand("updateControls()");
        } catch (Exception ex) {
            topReportError("", "common.error.happen", ex);
        }
    }

    @Secured("@")
    public void doSearch() {
        try {
            validateDate(handleControlsBean.getFromDate(), handleControlsBean.getToDate());
            if (!DataUtil.isNullOrEmpty(handleControlsBean.getActionCode())) {
                handleControlsBean.setActionCode(handleControlsBean.getActionCode().trim());
            }
            stockTransDTOs = DataUtil.defaultIfNull(transService.mmSearchVStockTrans(handleControlsBean.toStockTransDTO()), new ArrayList<>());
            detailTrans = false;
            stockTransDetailDTOs = Lists.newArrayList();
            stockTransSerialDTOs = Lists.newArrayList();
            selectedStockTransDTOs = Lists.newArrayList();
        } catch (LogicException e) {
            topReportError("", e);
        } catch (Exception e) {
            topReportError("", "common.error.happened", e);
        }
    }

    @Secured("@")
    public void changeOwnerType() {
        handleControlsBean.setShopId(null);
        handleControlsBean.setStaffId(null);
    }

    @Secured("@")
    public void receiveShop(VShopStaffDTO vShopStaffDTO) {
        handleControlsBean.setShopId(DataUtil.safeToLong(vShopStaffDTO.getOwnerId()));
    }

    @Secured("@")
    public void receiveStaff(VShopStaffDTO vShopStaffDTO) {
        handleControlsBean.setStaffId(DataUtil.safeToLong(vShopStaffDTO.getOwnerId()));
    }

    @Secured("@")
    public void clearOwner() {
        handleControlsBean.setShopId(null);
        handleControlsBean.setStaffId(null);
    }

    private void generateControlsTransStatus() {
        List<OptionSetValueDTO> statusFrActionTypes;
        boolean actionTypeSelected = false;
        boolean transTypeSelected = false;
        logger.error("action: ");
        logger.error(handleControlsBean.getActionType());
        logger.error("trans: ");
        logger.error(handleControlsBean.getTransType());
        if (DataUtil.isNullOrEmpty(handleControlsBean.getActionType())) {
            statusFrActionTypes = getOptionValByCode(Const.OPTION_SET.STOCK_TRANS_STATUS);
        } else {
            actionTypeSelected = true;
            if (Const.STOCK_TRANS_ACTION_TYPE.COMMAND.equals(handleControlsBean.getActionType())) {
                statusFrActionTypes = getOptionValByCode(Const.OPTION_SET.STOCK_TRANS_STATUS_CMD);
            } else {
                statusFrActionTypes = getOptionValByCode(Const.OPTION_SET.STOCK_TRANS_STATUS_NOT);
            }
        }
        List<OptionSetValueDTO> statusFrTransTypes;
        if (DataUtil.isNullOrEmpty(handleControlsBean.getTransType())) {
            statusFrTransTypes = getOptionValByCode(Const.OPTION_SET.STOCK_TRANS_STATUS);
        } else {
            transTypeSelected = true;
            if (Const.STOCK_TRANS_TYPE.EXPORT.equals(handleControlsBean.getTransType())) {
                statusFrTransTypes = getOptionValByCode(Const.OPTION_SET.STOCK_TRANS_STATUS_EX);
            } else {
                statusFrTransTypes = getOptionValByCode(Const.OPTION_SET.STOCK_TRANS_STATUS_IM);
            }
        }
        stockTransStatus = Lists.newArrayList();
        if (actionTypeSelected) {
            if (!transTypeSelected) {
                stockTransStatus = statusFrActionTypes;
            } else {
                for (OptionSetValueDTO optionSetValueDTO : statusFrActionTypes) {
                    for (OptionSetValueDTO optionSetValue : statusFrTransTypes) {
                        if (DataUtil.safeEqual(optionSetValue.getValue(), optionSetValueDTO.getValue())) {
                            stockTransStatus.add(optionSetValue);
                        }
                    }
                }
            }
        } else {
            stockTransStatus = statusFrTransTypes;
        }
        logger.error("status: " + stockTransStatus.size());
    }

    @Secured("@")
    public void doClear() {
        initControl();
        topPage();
    }

    @Secured("@")
    public void viewDetailTrans(VStockTransDTO selected, boolean detail) {
        try {
            detailTrans = detail;
            if (!detail) {
                return;
            }
            this.selectedStockTransDTO = selected;
            selectedStockTransDTOs = Lists.newArrayList();
            selectedStockTransDTOs.add(selected);
            List<Long> actionIds = Lists.newArrayList();
            actionIds.add(selected.getActionID());
            stockTransDetailDTOs = transService.searchStockTransDetail(actionIds);
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happen", ex);
        }
    }

    @Secured("@")
    public void viewDetailSerial(StockTransFullDTO detail) {
        try {
            StockTransSerialDTO search = new StockTransSerialDTO();
            search.setStockTransDetailId(detail.getStockTransDetailId());
            stockTransSerialDTOs = stockTransSerialService.findStockTransSerialByDTO(search);
            if (DataUtil.isNullOrEmpty(stockTransSerialDTOs)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "mm.stock.trans.search.validate.serial", detail.getProdOfferName());
            }
            selectedStockTransDetail = detail;
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
    }

    @Secured("@")
    public void viewStockDebit(VStockTransDTO stockTransDTO) {
        try {
            selectedStockTransDTO = stockTransDTO;
            StockDebitDTO stockDebit = debitService.findStockDebitByStockTrans(stockTransDTO.getStockTransID());
            stockDebitDTOs = Lists.newArrayList();
            if (!DataUtil.isNullObject(stockDebit)) {
                stockDebitDTOs.add(stockDebit);
            }
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
    }

    @Secured("@")
    public StreamedContent exportStockTransSerials() {
        if (DataUtil.isNullOrEmpty(selectedStockTransDTOs)) {
            topReportError("", ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "stock.trans.search.validate.selecttofindserial");
        } else {
            try {
                List<Long> stockTransActions = Lists.newArrayList();
                for (VStockTransDTO stockTrans : selectedStockTransDTOs) {
                    stockTransActions.add(stockTrans.getActionID());
                }
                List<StockTransSerialDTO> serialDTOs = stockTransSerialService.findStockTransSerial(stockTransActions);
                if (DataUtil.isNullOrEmpty(serialDTOs)) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.trans.search.validate.noserialresult");
                }
                FileExportBean bean = new FileExportBean();
                bean.setOutName("stock_trans_serial.xls");
                bean.setOutPath(FileUtil.getOutputPath());
                bean.setTemplateName("stock_trans_serial.xls");
                bean.setTempalatePath(FileUtil.getTemplatePath());
                bean.putValue("lstData", serialDTOs);
                bean.putValue("create_user", getStaffDTO().getStaffCode());
                bean.putValue("create_date", optionSetValueService.getSysdateFromDB(true));
                return FileUtil.exportToStreamed(bean);
//                FileUtil.exportFile(bean);
//                return bean.getExportFileContent();
            } catch (LogicException ex) {
                topReportError("", ex);
            } catch (Exception ex) {
                topReportError("", "common.error.happened", ex);
            }
        }
        return null;
    }


    @Secured("@")
    public StreamedContent exportHandOverReport() {
        try {
            if (DataUtil.isNullOrEmpty(selectedStockTransDTOs)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "stock.trans.search.validate.selecttohanover");
            } else {
                if (selectedStockTransDTOs.size() > 1) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "stock.trans.search.validate.selecttohanover");
                }
            }
            selectedStockTransDTO = selectedStockTransDTOs.get(0);
            //Danh dau XY = Goi tu quan ly giao dich
            StreamedContent content = exportHandOverReport(genStockTransDTO(selectedStockTransDTO), "XY");
            detailTrans = true;
            return content;
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
        return null;
    }

    @Secured("@")
    public StreamedContent exportStockTransDetail() {
        try {
            if (DataUtil.isNullOrEmpty(selectedStockTransDTOs)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "stock.trans.search.validate.selecttofinddetail");
            } else {
                if (selectedStockTransDTOs.size() > 1) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "stock.trans.search.validate.selecttofinddetail");
                }
            }
            selectedStockTransDTO = selectedStockTransDTOs.get(0);
            detailTrans = true;
            List<Long> stockTransActionIds = Lists.newArrayList();
            stockTransActionIds.add(selectedStockTransDTO.getActionID());
            stockTransDetailDTOs = transService.searchStockTransDetail(stockTransActionIds);
            return printStockTransDetail(genStockTransDTO(selectedStockTransDTO));
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
        return null;
    }

    @Secured("@")
    public void validateBeforeExportDetail() {

    }


    @Secured("@")
    public String getDlgTitle() {
        if (!DataUtil.isNullObject(selectedStockTransDetail)) {
            return getMessage("stock.trans.detail.serial.dlg", selectedStockTransDetail.getProdOfferName());
        }
        return "";
    }

    @Secured("@")
    public StreamedContent exportSerialsDlg() {
        try {
            if (DataUtil.isNullOrEmpty(stockTransSerialDTOs)) {
                reportError("", ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "mn.stock.trans.search.validate.noserialresult");
                topPage();
                return null;
            }
            FileExportBean fileExportBean = new FileExportBean();
            fileExportBean.setOutName("detail_serial_stock_trans.xls");
            fileExportBean.setTempalatePath(FileUtil.getTemplatePath());
            fileExportBean.setOutPath(FileUtil.getOutputPath());
            fileExportBean.setTemplateName(Const.REPORT_TEMPLATE.SERIAL_DETAIL_TEMPLATE);
            fileExportBean.putValue("state", selectedStockTransDetail.getStateName());
            fileExportBean.putValue("stockModelName", selectedStockTransDetail.getProdOfferName());
            fileExportBean.putValue("userCreate", getStaffDTO().getStaffCode());
            fileExportBean.putValue("dateCreate", optionSetValueService.getSysdateFromDB(true));
            fileExportBean.putValue("listStockTransSerials", stockTransSerialDTOs);
            fileExportBean.putValue("totalReq", selectedStockTransDetail.getQuantity());
            return FileUtil.exportToStreamed(fileExportBean);
//            FileUtil.exportFile(fileExportBean);
//            return fileExportBean.getExportFileContent();
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
        return null;
    }

    public StreamedContent exportVofficeFile() {
        try {
            if (DataUtil.isNullOrEmpty(selectedStockTransDTOs)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mm.stock.trans.action.msg");
            }
            for (VStockTransDTO stockTransDTO : selectedStockTransDTOs) {
                if (!Const.VOFFICE_SIGNED_STATUS.contains(stockTransDTO.getSignVoffice())) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "sign.voffice.msg.nsigned");
                }
            }
            List<byte[]> pdfContent = Lists.newArrayList();
            for (VStockTransDTO stockTransDTO : selectedStockTransDTOs) {
                StockTransVofficeDTO stockTransVofficeDTO = stockTransVofficeService.findStockTransVofficeByActionId(stockTransDTO.getActionID());
                if (stockTransVofficeDTO == null || stockTransVofficeDTO.getStockTransVofficeId() == null) {
                    continue;
                }
                FileAttachTranfer fileAttachTranfer = ver3AutoSign.getFileAttach(stockTransVofficeDTO.getStockTransVofficeId());
                if (fileAttachTranfer != null && fileAttachTranfer.getAttachBytes() != null) {
                    pdfContent.add(fileAttachTranfer.getAttachBytes());
                }
            }
            if (pdfContent.isEmpty()) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_PERMISSION, "mn.stock.export.voffice.file.valid.content");
            }
            return FileUtil.getStreamedContent(FileUtil.mergePdfContent(pdfContent), "trinh_ky_giao_dich.pdf");
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
        return null;
    }

    public class HandleControlsBean implements Serializable {
        private String actionCode;
        private String transType;
        private String actionType;
        private String status;
        private String ownerType;
        private Date fromDate;
        private Date toDate;
        private Long shopId;
        private Long staffId;

        public String getActionCode() {
            return actionCode;
        }

        public void setActionCode(String actionCode) {
            this.actionCode = actionCode;
        }

        public String getTransType() {
            return transType;
        }

        public void setTransType(String transType) {
            this.transType = transType;
        }

        public String getActionType() {
            return actionType;
        }

        public void setActionType(String actionType) {
            this.actionType = actionType;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getOwnerType() {
            return ownerType;
        }

        public void setOwnerType(String ownerType) {
            this.ownerType = ownerType;
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

        public Long getShopId() {
            return shopId;
        }

        public void setShopId(Long shopId) {
            this.shopId = shopId;
        }

        public Long getStaffId() {
            return staffId;
        }

        public void setStaffId(Long staffId) {
            this.staffId = staffId;
        }

        public VStockTransDTO toStockTransDTO() {
            VStockTransDTO stockTransDTO = new VStockTransDTO();
            //-xx-
            stockTransDTO.setStartDate(fromDate);
            stockTransDTO.setEndDate(toDate);
            //-xx-
            stockTransDTO.setActionCode(actionCode);
            stockTransDTO.setActionType(actionType);
            //-xx-
            stockTransDTO.setStockTransStatus(status);
            stockTransDTO.setStockTransType(DataUtil.safeToLong(transType));
            //-xx-
            stockTransDTO.setStaffId(getStaffDTO().getStaffId());
            stockTransDTO.setShopId(getStaffDTO().getShopId());
            //-xx-
            stockTransDTO.setOwnerType(DataUtil.safeToLong(ownerType));
            if (!DataUtil.isNullOrZero(stockTransDTO.getOwnerType())) {
                if (Const.OWNER_TYPE.SHOP_LONG.equals(stockTransDTO.getOwnerType())) {
                    stockTransDTO.setOwnerID(shopId);
                } else {
                    stockTransDTO.setOwnerID(staffId);
                    stockTransDTO.setStaffInShop(true);
                }
            }
            return stockTransDTO;
        }
    }
}
