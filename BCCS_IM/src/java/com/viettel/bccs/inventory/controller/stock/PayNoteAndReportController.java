package com.viettel.bccs.inventory.controller.stock;

import com.google.common.collect.Lists;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.fw.controller.TransCommonController;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.common.model.FileExportBean;
import com.viettel.bccs.inventory.common.util.FileUtil;
import com.viettel.bccs.inventory.dto.StaffDTO;
import com.viettel.bccs.inventory.dto.StockDocumentDTO;
import com.viettel.bccs.inventory.dto.VShopStaffDTO;
import com.viettel.bccs.inventory.dto.VStockTransDTO;
import com.viettel.bccs.inventory.dto.ws.PayNoteAndReportDTO;
import com.viettel.bccs.inventory.service.OptionSetValueService;
import com.viettel.bccs.inventory.service.PayNoteAndReportService;
import com.viettel.bccs.inventory.service.ShopService;
import com.viettel.bccs.inventory.service.StockDocumentService;
import com.viettel.bccs.inventory.tag.ShopInfoNameable;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.BundleUtil;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DateUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.web.common.security.CustomAuthenticationProvider;
import org.apache.poi.ss.usermodel.Workbook;
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
import java.util.Date;
import java.util.List;

/**
 * Created by tuydv1 on 2/19/2016.
 */
@Component
@Scope("view")
@ManagedBean
public class PayNoteAndReportController extends TransCommonController {
    private Date currentDate;
    private PayNoteAndReportDTO payNoteAndReportDTO = new PayNoteAndReportDTO();
    private PayNoteAndReportDTO forSearchDTO = new PayNoteAndReportDTO();
    private List<VStockTransDTO> listVStockTransDTO;
    private String exportOwnerId;
    private String importOwnerId;
    private VStockTransDTO stockTransDTO;
    private VStockTransDTO stockTransDetail;
    private UploadedFile fileUpload;
    private byte[] byteContent;
    private String fileName;
    private UploadedFile fileUploadAdd;
    private byte[] byteContentAdd;
    private String fileNameAdd;
    private List<VStockTransDTO> selectForCancel = Lists.newArrayList();
    private List<VStockTransDTO> selectForPrint;
    private StaffDTO currentStaff;
    private boolean allForCancel = false;
    private boolean allForPrint = false;
    private boolean showCancel = false;
    private String strCancelPay;
    private boolean search = false;
    private boolean enableCheckBoxViewRole = false;
    private boolean enablePrint = false;


    private enum Mode {
        Print(1), Cancel(2), AllPrint(3), AllCancel(4);
        int value;

        Mode(int value) {
            this.value = value;
        }
    }

    @Autowired
    private OptionSetValueService optionSetValueSv;
    @Autowired
    private ShopInfoNameable shopInfoTag;
    @Autowired
    private ShopInfoNameable shopInfoTagExport;
    @Autowired
    private ShopService shopService;
    @Autowired
    private PayNoteAndReportService payNoteAndReportService;
    @Autowired
    private StockDocumentService stockDocumentService;


    @PostConstruct
    public void init() {
        try {
            reset();
            listVStockTransDTO = Lists.newArrayList();

            if (currentDate == null) {
                currentDate = optionSetValueSv.getSysdateFromDB(true);
            }
            //check quyen
            enableCheckBoxViewRole = CustomAuthenticationProvider.hasRole(Const.PERMISION.ROLE_HUY_TRA_PHIEU);
            currentStaff = BccsLoginSuccessHandler.getStaffDTO();
            shopInfoTagExport.initShopMangeIsdnTrans(this, currentStaff.getShopId().toString(), null, null, Const.MODE_SHOP.PAYNOTEANDREPORT);
            shopInfoTag.initShopMangeIsdnTrans(this, currentStaff.getShopId().toString(), null, null, Const.MODE_SHOP.PAYNOTEANDREPORT);
            shopInfoTagExport.setvShopStaffDTO(shopService.getShopByShopId(currentStaff.getShopId()));
            exportOwnerId = shopInfoTagExport.getvShopStaffDTO().getOwnerId();
            forSearchDTO.setFromDate(currentDate);
            forSearchDTO.setToDate(currentDate);
            executeCommand("updateControls();");
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex.getErrorCode(), ex.getDescription());
        } catch (Exception ex) {
            logger.error(ex);
            reportError("", "", "common.error.happen");
        }
        topPage();
    }

    @Secured("@")
    public void doSearch() {
        try {
            reset();
            search = false;
            validateDate(forSearchDTO.getFromDate(), forSearchDTO.getToDate());
            listVStockTransDTO = payNoteAndReportService.getListStockTransForPay(forSearchDTO,
                    exportOwnerId, importOwnerId, search);
            doShowCancel();
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception e) {
            topReportError("", "common.error.happened", e);
        }
    }

    @Secured("@")
    public void doSearchForCancel() {
        try {
            reset();
            search = true;
            validateDate(forSearchDTO.getFromDate(), forSearchDTO.getToDate());
            listVStockTransDTO = payNoteAndReportService.getListStockTransForPay(forSearchDTO,
                    exportOwnerId, importOwnerId, search);
            doShowCancel();
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception e) {
            topReportError("", "common.error.happened", e);
        }
    }

    @Secured("@")
    public void exportShop(VShopStaffDTO vShopStaffDTO) {
        shopInfoTagExport.setvShopStaffDTO(vShopStaffDTO);
        if (!DataUtil.isNullObject(vShopStaffDTO)) {
            exportOwnerId = vShopStaffDTO.getOwnerId();
        } else {
            exportOwnerId = null;
        }
    }

    @Secured("@")
    public void importShop(VShopStaffDTO vShopStaffDTO) {
        shopInfoTag.setvShopStaffDTO(vShopStaffDTO);
        if (!DataUtil.isNullObject(vShopStaffDTO)) {
            importOwnerId = vShopStaffDTO.getOwnerId();
        } else {
            importOwnerId = null;
        }
    }

    @Secured("@")
    public String getAction(VStockTransDTO stockTrans) {
        if (!Const.STATUS.ACTIVE.equals(stockTrans.getDocumentStatus()) &&
                currentStaff.getShopId().equals(stockTrans.getFromOwnerID())) {
            return BundleUtil.getText("mn.stock.utilities.pay.note");
        } else if (Const.STATUS.ACTIVE.equals(stockTrans.getDocumentStatus()) &&
                currentStaff.getShopId().equals(stockTrans.getFromOwnerID())) {
            return BundleUtil.getText("payNoteAndReport.status.hasPaid");
        } else {
            return "";
        }
    }

    @Secured("@")
    public void clearExportShop() {
        shopInfoTagExport.setvShopStaffDTO(null);
        exportOwnerId = null;
    }

    @Secured("@")
    public void clearImportShop() {
        shopInfoTag.setvShopStaffDTO(null);
        importOwnerId = null;
    }

    @Secured("@")
    public void showPayNote(VStockTransDTO stockTrans) {
        stockTransDetail = new VStockTransDTO();
        stockTransDetail = DataUtil.cloneBean(stockTrans);
        fileUpload = null;
        fileName = null;
        byteContent = null;
        fileUploadAdd = null;
        fileNameAdd = null;
        byteContentAdd = null;
    }

    @Secured("@")
    public void handleFileUpload(FileUploadEvent event) {
        try {
            BaseMessage message = validateFileUploadWhiteList(event.getFile(), ALOW_EXTENSION_PDF_TYPE_LIST, MAX_SIZE_5M);
            if (!message.isSuccess()) {
                LogicException ex = new LogicException(message.getErrorCode(), message.getKeyMsg());
                ex.setDescription(message.getDescription());
                throw ex;
            }
            if (!DataUtil.isNullObject(event) && event.getFile().getFileName().length() > 200) {
                fileName = event.getFile().getFileName();
                fileUpload = null;
                fileName = null;
                byteContent = null;
                focusElementByRawCSSSlector(".clFileName");
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "payNoteAndReport.file.maxlength");
            } else {
                fileUpload = event.getFile();
                fileName = fileUpload.getFileName();
                byteContent = fileUpload.getContents();
            }
        } catch (LogicException lex) {
            reportError("frmPayNoteAndReport:msgPayNoteAndReportDetail", lex);
            updateElemetId("formUpload:msgPayNoteAndReportDetail");
            updateElemetId("formUpload:fileName");
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
            topPage();
        }
    }

    @Secured("@")
    public void handleFileUploadAdd(FileUploadEvent event) {
        try {
            BaseMessage message = validateFileUploadWhiteList(event.getFile(), ALOW_EXTENSION_PDF_TYPE_LIST, MAX_SIZE_5M);
            if (!message.isSuccess()) {
                LogicException ex = new LogicException(message.getErrorCode(), message.getKeyMsg());
                ex.setDescription(message.getDescription());
                throw ex;
            }
            if (!DataUtil.isNullObject(event) && event.getFile().getFileName().length() > 200) {
                fileNameAdd = event.getFile().getFileName();
                fileUploadAdd = null;
                fileNameAdd = null;
                byteContentAdd = null;
                focusElementByRawCSSSlector(".clFileNameAdd");
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.check.report.validate.maxlength.fileName");
            } else {
                fileUploadAdd = event.getFile();
                fileNameAdd = fileUploadAdd.getFileName();
                byteContentAdd = fileUploadAdd.getContents();
            }
        } catch (LogicException lex) {
            reportError("frmPayNoteAndReport:msgPayNoteAndReportDetail", lex);
            updateElemetId("formUpload:msgPayNoteAndReportDetail");
            updateElemetId("formUpload:fileNameAdd");
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
            topPage();
        }
    }

    @Secured("@")
    public void showFileUpload(boolean flag) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            externalContext.setResponseContentType("application/vnd.ms-pdf");
            if (flag) {
                if (DataUtil.isNullOrEmpty(fileName) || DataUtil.isNullObject(fileUpload)) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "payNoteAndReport.no.file.choose");
                }
                externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
                externalContext.getResponseOutputStream().write(byteContent);
            } else {

                if (DataUtil.isNullOrEmpty(fileNameAdd) || DataUtil.isNullObject(fileUploadAdd)) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "payNoteAndReport.no.file.choose");
                }
                externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"" + fileNameAdd + "\"");
                externalContext.getResponseOutputStream().write(byteContentAdd);
            }
            facesContext.responseComplete();
            return;

        } catch (LogicException lex) {
            reportError("frmPayNoteAndReport:msgPayNoteAndReportDetail", lex);
            updateElemetId("frmPayNoteAndReport:msgPayNoteAndReportDetail");
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
            topPage();
        }
    }

    @Secured("@")
    public void exportExcel() {
        try {
            if (DataUtil.isNullOrEmpty(listVStockTransDTO)) {
                reportErrorValidateFail("", "", "common.emty.records");
            } else {
                FileExportBean bean = new FileExportBean();
                bean.setTempalatePath(FileUtil.getTemplatePath());
                bean.setTemplateName("ListDocumentStock_Template.xls");
                bean.putValue("lstData", listVStockTransDTO);
                bean.putValue("fromDate", DateUtil.date2ddMMyyyyString(forSearchDTO.getFromDate()));
                bean.putValue("toDate", DateUtil.date2ddMMyyyyString(forSearchDTO.getToDate()));

                Workbook workbook = FileUtil.exportWorkBook(bean);

                FacesContext facesContext = FacesContext.getCurrentInstance();
                ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
                externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"" + Const.PAY_NOTE_AND_REPORT.LIST_STOCK_DOCUMENT + "_" + currentStaff.getName() + "_" + DateUtil.date2ddMMyyyyHHMMssNoSlash(new Date()) + Const.PAY_NOTE_AND_REPORT.PDF + "\"");
                externalContext.setResponseContentType("application/vnd.ms-excel");
                workbook.write(externalContext.getResponseOutputStream());
                facesContext.responseComplete();
                return;
            }
        } catch (LogicException lex) {
            reportError(lex);
            topPage();
        } catch (Exception ex) {
            logger.error("", ex);
            reportError("", "common.error.happened", ex);
            topPage();
        }
    }

    @Secured("@")
    public void doSendFile() {
        try {
            // gan vao DTO StockDocumentDTO
            StockDocumentDTO stockDocumentDTO = new StockDocumentDTO();
            stockDocumentDTO.setActionId(stockTransDetail.getActionID());
            stockDocumentDTO.setActionCode(stockTransDetail.getActionCode());
            stockDocumentDTO.setUserCreate(currentStaff.getStaffCode());
            stockDocumentDTO.setCreateDate(new Date());
            stockDocumentDTO.setFromShopCode(stockTransDetail.getFromOwnerName());
            stockDocumentDTO.setToShopCode(stockTransDetail.getToOwnerName());
            stockDocumentDTO.setExportNoteName(fileName);
            stockDocumentDTO.setExportNote(byteContent);
            stockDocumentDTO.setDeliveryRecordsName(fileNameAdd);
            stockDocumentDTO.setDeliveryRecords(byteContentAdd);
            BaseMessage baseMessage = payNoteAndReportService.createStockDocumentAndUpdateAction(stockDocumentDTO, stockTransDetail.getActionID());
            if (baseMessage.isSuccess()) {
                listVStockTransDTO = payNoteAndReportService.getListStockTransForPay(forSearchDTO,
                        exportOwnerId, importOwnerId, search);
                doShowCancel();
                reportSuccess("", "payNoteAndReport.sentFile.success", "");
                topPage();
            } else {
                reportError("", "", baseMessage.getDescription());
                topPage();
            }
        } catch (LogicException lex) {
            reportError("", "", lex);
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
            topPage();
        }
    }


    @Secured("@")
    public void validatedateSentFile() {
        try {
            if (byteContent == null || byteContent.length < 1) {
                focusElementByRawCSSSlector(".clFileName");
                reportErrorValidateFail("frmPayNoteAndReport:msgPayNoteAndReportDetail", "", "payNoteAndReport.export.bbbg.valid");
                topPage();
            } else {
                if (DataUtil.isNullOrEmpty(fileName) || !fileName.toLowerCase().endsWith(Const.PAY_NOTE_AND_REPORT.PDF)) {
                    focusElementByRawCSSSlector(".clFileName");
                    reportError("frmPayNoteAndReport:msgPayNoteAndReportDetail", "", "payNoteAndReport.file.report.pdf");
                    topPage();
                }
                if (byteContent.length >= Const.FILE_MAX) {
                    reportError("frmPayNoteAndReport:msgPayNoteAndReportDetail", "", "mn.stock.utilities.monthyear.file.size");
                    focusElementByRawCSSSlector(".clFileName");
                    topPage();
                }
            }
            if (!DataUtil.isNullObject(byteContentAdd)) {
                if (DataUtil.isNullOrEmpty(fileNameAdd) || !fileNameAdd.toLowerCase().endsWith(Const.PAY_NOTE_AND_REPORT.PDF)) {
                    focusElementByRawCSSSlector(".clFileNameAdd");
                    reportError("frmPayNoteAndReport:msgPayNoteAndReportDetail", "", "payNoteAndReport.file.add.pdf");
                    topPage();
                }
                if (byteContentAdd.length >= Const.FILE_MAX) {
                    reportError("frmPayNoteAndReport:msgPayNoteAndReportDetail", "", "mn.stock.utilities.monthyear.file.size");
                    focusElementByRawCSSSlector(".clFileNameAdd");
                    topPage();
                }
            }

        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
            topPage();
        }
    }

    @Secured("@")
    public void validatedateCancelDocument() {
        try {
            if (DataUtil.isNullOrEmpty(selectForCancel)) {
                reportErrorValidateFail("", "", "mn.invoice.invoiceType.no.selected");
                topPage();
            } else {
                if (DataUtil.isNullOrEmpty(strCancelPay)) {
                    focusElementByRawCSSSlector(".clCancelPay");
                    reportErrorValidateFail("", "", "payNoteAndReport.reason.cancelPay.required.true");
                    topPage();
                } else if (strCancelPay.length() > 200) {
                    focusElementByRawCSSSlector(".clCancelPay");
                    reportErrorValidateFail("", "", "payNoteAndReport.reason.cancelPay.maxlength");
                    topPage();
                }
            }
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
            topPage();
        }
    }

    private StreamedContent content;

    @Secured("@")
    public void printStockDocument() {
        try {
            if (DataUtil.isNullOrEmpty(selectForPrint)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "mn.invoice.invoiceType.no.selected");
            }
            List<StockDocumentDTO> stockDocumentDTOs;
            List<byte[]> pdfFileContentsl = Lists.newArrayList();
            for (VStockTransDTO vStockTransDTO : selectForPrint) {
                stockDocumentDTOs = stockDocumentService.getListStockDocumentDTOByActionId(vStockTransDTO.getActionID());
                if (!DataUtil.isNullOrEmpty(stockDocumentDTOs)) {
                    for (StockDocumentDTO dto : stockDocumentDTOs) {
                        if (dto.getExportNote().length > 0) {
                            pdfFileContentsl.add(dto.getExportNote());
                        }
                        if (dto.getDeliveryRecords().length > 0) {
                            pdfFileContentsl.add(dto.getDeliveryRecords());
                        }
                    }
                }
            }
            if (DataUtil.isNullOrEmpty(pdfFileContentsl)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "payNoteAndReport.file.report.empty");
            }
            String fileName = Const.PAY_NOTE_AND_REPORT.PX_BBBG_ + currentStaff.getName() + "_" +
                    DateUtil.dateTime2StringNoSlash(new Date()) + Const.PAY_NOTE_AND_REPORT.PDF;
            content = mergePdfFiles(pdfFileContentsl, fileName);
//            RequestContext.getCurrentInstance().execute("clickLinkByClass(.download)");
        } catch (LogicException lex) {
            reportError(lex);
            updateElemetId("frmPayNoteAndReport:msgPayNoteAndReport");
            topPage();
        } catch (Exception ex) {
            logger.error("", ex);
            reportError("", "common.error.happened", ex);
            updateElemetId("frmPayNoteAndReport:msgPayNoteAndReport");
            topPage();
        } finally {
            updateElemetId("frmPayNoteAndReport:msgPayNoteAndReport");
        }
    }

    public StreamedContent download() {
        if (content != null) {
            return content;
        }
        return null;
    }

    @Secured("@")
    public void cancelStockDocument() {
        try {
            BaseMessage result = payNoteAndReportService.cancelStockDocument(selectForCancel, strCancelPay, currentStaff.getStaffCode());
            if (result.isSuccess()) {
                listVStockTransDTO = payNoteAndReportService.getListStockTransForPay(forSearchDTO,
                        exportOwnerId, importOwnerId, search);
                doShowCancel();
                reportSuccess("", "payNoteAndReport.cancelPay.success", "");
                topPage();
            }
        } catch (LogicException lex) {
            reportError(lex);
            topPage();
        } catch (Exception ex) {
            logger.error("", ex);
            reportError("", "common.error.happened", ex);
            topPage();
        }
    }

    public void validateDate(Date fromDate, Date toDate) throws LogicException, Exception {
        Date sysdate = getSysdateFromDB();
        if (DataUtil.isNullObject(shopInfoTagExport.getvShopStaffDTO()) &&
                DataUtil.isNullObject(shopInfoTag.getvShopStaffDTO())) {
            executeCommand("focusElementError('ipExportShop')");
            executeCommand("focusElementError('inputStore')");
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "payNoteAndReport.export.import.valid");
        }
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
            if (DateUtil.monthsBetween(toDate, fromDate) != 0) {
                executeCommand("focusElementError('fromDate')");
                executeCommand("focusElementError('toDate')");
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "payNoteAndReport.fromDate.toDate.valid");
            }
        }

    }

    public StreamedContent mergePdfFiles(List<byte[]> pdfFileContents, String fileName) throws Exception {
        try {
            byte[] bytes = FileUtil.mergePdfContent(pdfFileContents);
            /*FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            externalContext.getResponseOutputStream().write(bytes);
            facesContext.responseComplete();*/

            return FileUtil.getStreamedContent(FileUtil.mergePdfContent(Lists.newArrayList(bytes)), fileName);
        } catch (Exception e) {
            throw e;
        }
    }

    @Secured("@")
    public void doSelect(VStockTransDTO stockTransDTO, int mode) {
        if (mode == Mode.AllPrint.value) {
            //chon tat ca in
            if (allForPrint) {
                for (VStockTransDTO v : listVStockTransDTO) {
                    if (v.getDocumentStatus() != null && Const.STATUS_ACTIVE.equals(v.getDocumentStatus())) {
                        v.setPrintable(true);
                        selectForPrint.add(v);
                    }
                }
            } else {
                for (VStockTransDTO v : listVStockTransDTO) {
                    v.setPrintable(false);
                }
                selectForPrint = Lists.newArrayList();
            }
        } else if (mode == Mode.AllCancel.value) {
            //Chon tat ca huy
            if (allForCancel) {
                for (VStockTransDTO v : listVStockTransDTO) {
                    if (v.getDocumentStatus() != null && Const.STATUS_ACTIVE.equals(v.getDocumentStatus())
                            && enableCheckBoxViewRole && v.getFromOwnerID().equals(currentStaff.getShopId())) {
                        v.setCancelable(true);
                        selectForCancel.add(v);
                    }
                }
            } else {
                for (VStockTransDTO v : listVStockTransDTO) {
                    v.setCancelable(false);
                }
                selectForCancel = Lists.newArrayList();
            }
        } else if (mode == Mode.Cancel.value) {
            //Chon huy
            if (selectForCancel.contains(stockTransDTO)) {
                for (VStockTransDTO v : listVStockTransDTO) {
                    if (v.equals(stockTransDTO)) {
                        v.setCancelable(false);
                        break;
                    }
                }
                selectForCancel.remove(stockTransDTO);
                if (selectForCancel.isEmpty()) {
                    allForCancel = false;
                }
            } else {
                stockTransDTO.setCancelable(true);
                selectForCancel.add(stockTransDTO);
            }
        } else {
            //Chon in
            if (selectForPrint.contains(stockTransDTO)) {
                for (VStockTransDTO v : listVStockTransDTO) {
                    if (v.equals(stockTransDTO)) {
                        v.setPrintable(false);
                        break;
                    }
                }
                selectForPrint.remove(stockTransDTO);
                if (selectForPrint.isEmpty()) {
                    allForPrint = false;
                }
            } else {
                stockTransDTO.setPrintable(true);
                selectForPrint.add(stockTransDTO);
            }
        }
        executeCommand("updateTable();");
    }

    private void doShowCancel() throws Exception {
        showCancel = false;
        strCancelPay = null;
        if (!DataUtil.isNullOrEmpty(listVStockTransDTO)) {
            enablePrint = true;
            for (VStockTransDTO v : listVStockTransDTO) {
                if (v.getDocumentStatus() != null && Const.STATUS_ACTIVE.equals(v.getDocumentStatus()) &&
                        currentStaff.getShopId().equals(v.getFromOwnerID())) {
                    showCancel = true;
                    break;
                }
            }
        }
    }

    public void reset() {
        allForCancel = false;
        allForPrint = false;
        selectForCancel = Lists.newArrayList();
        selectForPrint = Lists.newArrayList();
        strCancelPay = null;
        showCancel = false;
        enablePrint = false;

    }


    public PayNoteAndReportDTO getPayNoteAndReportDTO() {
        return payNoteAndReportDTO;
    }

    public void setPayNoteAndReportDTO(PayNoteAndReportDTO payNoteAndReportDTO) {
        this.payNoteAndReportDTO = payNoteAndReportDTO;
    }

    public PayNoteAndReportDTO getForSearchDTO() {
        return forSearchDTO;
    }

    public void setForSearchDTO(PayNoteAndReportDTO forSearchDTO) {
        this.forSearchDTO = forSearchDTO;
    }

    public ShopInfoNameable getShopInfoTag() {
        return shopInfoTag;
    }

    public void setShopInfoTag(ShopInfoNameable shopInfoTag) {
        this.shopInfoTag = shopInfoTag;
    }

    public ShopInfoNameable getShopInfoTagExport() {
        return shopInfoTagExport;
    }

    public void setShopInfoTagExport(ShopInfoNameable shopInfoTagExport) {
        this.shopInfoTagExport = shopInfoTagExport;
    }

    public List<VStockTransDTO> getListVStockTransDTO() {
        return listVStockTransDTO;
    }

    public void setListVStockTransDTO(List<VStockTransDTO> listVStockTransDTO) {
        this.listVStockTransDTO = listVStockTransDTO;
    }

    public VStockTransDTO getStockTransDTO() {
        return stockTransDTO;
    }

    public void setStockTransDTO(VStockTransDTO stockTransDTO) {
        this.stockTransDTO = stockTransDTO;
    }

    public StaffDTO getCurrentStaff() {
        return currentStaff;
    }

    public void setCurrentStaff(StaffDTO currentStaff) {
        this.currentStaff = currentStaff;
    }

    public VStockTransDTO getStockTransDetail() {
        return stockTransDetail;
    }

    public void setStockTransDetail(VStockTransDTO stockTransDetail) {
        this.stockTransDetail = stockTransDetail;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public UploadedFile getFileUpload() {
        return fileUpload;
    }

    public void setFileUpload(UploadedFile fileUpload) {
        this.fileUpload = fileUpload;
    }

    public UploadedFile getFileUploadAdd() {
        return fileUploadAdd;
    }

    public void setFileUploadAdd(UploadedFile fileUploadAdd) {
        this.fileUploadAdd = fileUploadAdd;
    }

    public String getFileNameAdd() {
        return fileNameAdd;
    }

    public void setFileNameAdd(String fileNameAdd) {
        this.fileNameAdd = fileNameAdd;
    }

    public List<VStockTransDTO> getSelectForCancel() {
        return selectForCancel;
    }

    public void setSelectForCancel(List<VStockTransDTO> selectForCancel) {
        this.selectForCancel = selectForCancel;
    }

    public List<VStockTransDTO> getSelectForPrint() {
        return selectForPrint;
    }

    public void setSelectForPrint(List<VStockTransDTO> selectForPrint) {
        this.selectForPrint = selectForPrint;
    }

    public boolean isAllForCancel() {
        return allForCancel;
    }

    public void setAllForCancel(boolean allForCancel) {
        this.allForCancel = allForCancel;
    }

    public boolean isAllForPrint() {
        return allForPrint;
    }

    public void setAllForPrint(boolean allForPrint) {
        this.allForPrint = allForPrint;
    }

    public boolean isShowCancel() {
        return showCancel;
    }

    public void setShowCancel(boolean showCancel) {
        this.showCancel = showCancel;
    }

    public String getStrCancelPay() {
        return strCancelPay;
    }

    public void setStrCancelPay(String strCancelPay) {
        this.strCancelPay = strCancelPay;
    }

    public boolean isSearch() {
        return search;
    }

    public void setSearch(boolean search) {
        this.search = search;
    }

    public boolean isEnableCheckBoxViewRole() {
        return enableCheckBoxViewRole;
    }

    public void setEnableCheckBoxViewRole(boolean enableCheckBoxViewRole) {
        this.enableCheckBoxViewRole = enableCheckBoxViewRole;
    }

    public boolean isEnablePrint() {
        return enablePrint;
    }

    public void setEnablePrint(boolean enablePrint) {
        this.enablePrint = enablePrint;
    }
}
