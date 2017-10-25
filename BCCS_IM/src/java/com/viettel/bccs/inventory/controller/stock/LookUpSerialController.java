package com.viettel.bccs.inventory.controller.stock;

import com.google.common.collect.Lists;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.fw.controller.TransCommonController;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.common.ExcellUtil;
import com.viettel.bccs.inventory.common.model.FileExportBean;
import com.viettel.bccs.inventory.common.util.FileUtil;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.OptionSetValueService;
import com.viettel.bccs.inventory.service.ProductOfferTypeService;
import com.viettel.bccs.inventory.service.ProductOfferingService;
import com.viettel.bccs.inventory.service.StockTransSerialService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DateUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.dto.BaseMessage;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.omnifaces.util.Faces;
import org.primefaces.component.tabview.TabView;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.TabChangeEvent;
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
import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * controller tra cu serial don le
 * @author thanhnt77
 */
@Component
@Scope("view")
@ManagedBean
public class LookUpSerialController extends TransCommonController {

    private List<LookupSerialByFileDTO> lsLookupSerialByFileDTO = Lists.newArrayList();
    private List<ViewSerialHistoryDTO> lsViewSerialHistoryDTO = Lists.newArrayList();
    private List<ProductOfferTypeDTO> lsProductOfferTypeDTO = Lists.newArrayList();
    private List<LookupSerialByFileValidDTO> listSerialDTOFromFileFull = Lists.newArrayList();

    private String typeImport = "1";// 1 la tra cuu don le, 2 la theo file, mac dinh la tra cuu don le
    private String serial = "";
    private String serialGpon = "";
    private String serialDlg = "";
    private String typeSearch = "1";//1 la tim kiem theo du lieu moi, 2 la tim theo du lieu lich su
    private String typeSearchDlg = "1";//1 la tim kiem theo du lieu moi, 2 la tim theo du lieu lich su
    private Date fromDate = DateUtil.addDay(Calendar.getInstance().getTime(), -10);
    private Date toDate = Calendar.getInstance().getTime();
    private Long productOfferTypeId = Const.STOCK_TYPE.STOCK_KIT;
    private String attachFileName = "";
    private byte[] byteContent;
    private UploadedFile fileUpload;
    private ExcellUtil processingFile;
    private ProductOfferingDTO prodOfferingDTOSelect;
    private StaffDTO staffDTO;
    private boolean hasFileError;
    private LookupSerialByFileDTO selected = new LookupSerialByFileDTO();
    private Long limitSizeUpload;
    private int tabIndex;
    private boolean directLink;

    @Autowired
    private StockTransSerialService stockTransSerialService;
    @Autowired
    private ProductOfferTypeService productOfferTypeService;
    @Autowired
    private ProductOfferingService productOfferingService;
    @Autowired
    private OptionSetValueService optionSetValueService;

    @PostConstruct
    public void init() {
        try {
            helperSystem();
            staffDTO = BccsLoginSuccessHandler.getStaffDTO();
            lsLookupSerialByFileDTO = Lists.newArrayList();
            if (tabIndex == 0) {
                initTab1();
            } else if (tabIndex == 1) {
                initTab2();
            }
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
            logger.error(ex.getMessage(), ex);
            topPage();
        }
    }

    private void helperSystem(){
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String sys = request.getParameter(Const.SYSTEM_SALE);
        directLink = DataUtil.safeEqual(sys,Const.SYSTEM_SALE);
    }

    private void initTab2() throws Exception {
        if (limitSizeUpload == null) {
            limitSizeUpload = DataUtil.safeToLong(optionSetValueService.getValueByNameFromOptionSetValue(Const.OPTION_SET.LOOKUP_SIZE_IMP));
        }
    }

    private void initTab1() throws Exception {
        //load danh sach loai mat hang
        List<ProductOfferTypeDTO> lsData = DataUtil.defaultIfNull(productOfferTypeService.getListProduct(), new ArrayList<>());
        lsProductOfferTypeDTO = lsData.stream().filter(obj -> !DataUtil.safeEqual(obj.getProductOfferTypeId(), 11L)).collect(Collectors.toList());
        if (!DataUtil.isNullOrEmpty(lsProductOfferTypeDTO)) {
            productOfferTypeId = lsProductOfferTypeDTO.get(0).getProductOfferTypeId();
        }
    }

    /**
     * ham xu ly tim kiem serial
     * @author ThanhNT77
     */
    @Secured("@")
    public void doSearchSerial() {
        try {
            serial = DataUtil.safeToString(serial).trim();
            serialGpon = DataUtil.safeToString(serialGpon).trim();
            if (Const.STOCK_TYPE.STOCK_HANDSET.equals(productOfferTypeId)) {
                if (DataUtil.isNullOrEmpty(serial) && DataUtil.isNullOrEmpty(serialGpon)) {
                    reportError("", ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.utilities.look.serial.valid.non.handset.serial.GPON.format");
                    return;
                } else if (!DataUtil.isNullOrEmpty(serialGpon) && !DataUtil.validateStringByPattern(serialGpon, "^[a-zA-Z0-9]+$")) {
                    reportError("", ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.utilities.look.serial.gpon.valid.format");
                    focusElementError("txtSerialGpon");
                    return;
                }
            }
            if (!DataUtil.isNullOrEmpty(serial) && !DataUtil.validateStringByPattern(serial, "^[a-zA-Z0-9]+$")) {
                reportError("", ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.utilities.look.serial.valid.format");
                focusElementError("txtSerial");
                return;
            }

            if (!(Const.STOCK_TYPE.STOCK_HANDSET.equals(productOfferTypeId) || Const.STOCK_TYPE.STOCK_ACCESSORIES.equals(productOfferTypeId))
                    && !DataUtil.validateStringByPattern(serial, "^[0-9]+$")) {
                reportError("", ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.utilities.look.serial.valid.non.handset.format");
                focusElementError("txtSerial");
                return;
            }
            boolean isSearchNew = "1".equalsIgnoreCase(typeSearch);
            List<String> lsSerial = Lists.newArrayList(serial);

            lsLookupSerialByFileDTO = stockTransSerialService.searchSerialExact(productOfferTypeId, lsSerial, serialGpon, isSearchNew);
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            topReportError("", ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
            logger.error(ex.getMessage(), ex);
        }
    }

    public void changeOfferType() {
        if (Const.STOCK_TYPE.STOCK_ACCESSORIES.equals(productOfferTypeId) || Const.STOCK_TYPE.STOCK_HANDSET.equals(productOfferTypeId)) {
            this.serialGpon = "";
        }
    }

    public boolean isHandset() {
        return Const.STOCK_TYPE.STOCK_HANDSET.equals(productOfferTypeId);
    }

    public boolean getIsShowExportListSerial() {
        return lsLookupSerialByFileDTO != null && lsLookupSerialByFileDTO.size() > 0;
    }

    /**
     * ham xu ly export tra cuu serial
     * @author thanhnt77
     * @return
     */
    @Secured("@")
    public StreamedContent doExportSerialSearch() {
        try {
            Date sysDate = getSysdateFromDB();
            FileExportBean bean = new FileExportBean();
            bean.setTempalatePath(FileUtil.getTemplatePath());
            bean.setOutName("List_Serial_History_"+ staffDTO.getName() + "_" + DateUtil.date2ddMMyyyyHHMMssNoSlash(sysDate)+ ".xls");
            bean.setOutPath(FileUtil.getOutputPath());
            bean.setTemplateName(Const.REPORT_TEMPLATE.SEARCH_SERIAL_EXPORT);
            bean.putValue("create_date", sysDate);
            bean.putValue("lsLookupSerialByFileDTO", lsLookupSerialByFileDTO);
            return FileUtil.exportToStreamed(bean);
//            FileUtil.exportFile(bean);
//            return bean.getExportFileContent();
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
        return null;
    }

    /**
     * ham xu ly reset thong tin tim kiem serial
     * @author thanhnt77
     */
    @Secured("@")
    public void doResetSearial() {
        serial = "";
        typeSearch = "1";
        productOfferTypeId = null;
        if (!DataUtil.isNullOrEmpty(lsProductOfferTypeDTO)) {
            productOfferTypeId = lsProductOfferTypeDTO.get(0).getProductOfferTypeId();
        }
    }

    /**
     * ham xu ly tim kiem lich su serial
     * @author ThanhNT77
     */
    @Secured("@")
    public void doSearchSerialHistory() {
        try {
            if (DateUtil.compareDateToDate(toDate, fromDate) < 0) {
                reportError("", ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.trans.from.than.to");
                focusElementError("fromDateDlg");
                return;
            }
            if (DateUtil.monthsBetween(fromDate, toDate) > 6) {
                reportError("", ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.utilities.look.serial.toDate.fromDate.sixMonth");
                focusElementError("fromDateDlg");
                focusElementError("toDateDlg");
                return;
            }
            if (prodOfferingDTOSelect != null) {
                boolean isSearchNew = "1".equalsIgnoreCase(typeSearchDlg);
                lsViewSerialHistoryDTO = DataUtil.defaultIfNull(stockTransSerialService.listLookUpSerialHistory(serial, prodOfferingDTOSelect.getProductOfferingId(),
                        fromDate, toDate, isSearchNew), new ArrayList<>());
            }
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
            logger.error(ex.getMessage(), ex);
            topPage();
        }
    }

    /**
     * ham xu ly tim kiem lich su serial
     * @author ThanhNT77
     */
    @Secured("@")
    public void doResetSeachHistory() {
        try {
            serialDlg = "";
            fromDate = DateUtil.addDay(Calendar.getInstance().getTime(), -10); 
            toDate = Calendar.getInstance().getTime();
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
            logger.error(ex.getMessage(), ex);
            topPage();
        }
    }

    /**
     * xu ly search tim kiem lich su serial
     * @author thanhnt77
     * @param index
     */
    @Secured("@")

    public void doOpenSearchHistoryDlg(int index) {
        try {
            prodOfferingDTOSelect = null;
            fromDate = DateUtil.addDay(Calendar.getInstance().getTime(), -10);
            toDate = Calendar.getInstance().getTime();
            lsViewSerialHistoryDTO = Lists.newArrayList();
            typeSearchDlg = "1";
            Long prodOfferId = !DataUtil.isNullOrEmpty(lsLookupSerialByFileDTO) ? lsLookupSerialByFileDTO.get(index).getProdOfferId() : null;
            if (prodOfferId != null) {
                prodOfferingDTOSelect = productOfferingService.findOne(prodOfferId);
            }
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
            logger.error(ex.getMessage(), ex);
            topPage();
        }

    }

    /**
     * ham xu ly upload file
     *
     * @return
     * @author ThanhNT77
     */
    @Secured("@")
    public void handleFileUpload(FileUploadEvent event) {
        try {
            if (event == null) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "common.error.upload.file");
            }
            hasFileError = false;
            fileUpload = event.getFile();
            BaseMessage message = validateFileUploadWhiteList(fileUpload, ALOW_EXTENSION_EXCEL_TYPE_LIST, MAX_SIZE_5M);
            if (!message.isSuccess()) {
                LogicException ex = new LogicException(message.getErrorCode(), message.getKeyMsg());
                ex.setDescription(message.getDescription());
                throw ex;
            }
            byteContent = fileUpload.getContents();
            attachFileName = new String(fileUpload.getFileName().getBytes(), "UTF-8");
            processingFile = new ExcellUtil(fileUpload, byteContent);
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("msgExportFile", ex);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("msgExportFile", "common.error.happened", ex);
        }
    }

    /**
     * ham xu ly tra cuu theo file
     *
     * @return
     * @author ThanhNT77
     */
    @Secured("@")
    public void doPreviewFileUpload(){
        try {
            BaseMessage message = validateFileUploadWhiteList(fileUpload, ALOW_EXTENSION_EXCEL_TYPE_LIST, MAX_SIZE_5M);
            if (!message.isSuccess()) {
                LogicException ex = new LogicException(message.getErrorCode(), message.getKeyMsg());
                ex.setDescription(message.getDescription());
                throw ex;
            }
            lsLookupSerialByFileDTO = Lists.newArrayList();
            listSerialDTOFromFileFull = Lists.newArrayList();

            boolean isSearchNew = "1".equalsIgnoreCase(typeSearch);
            List<LookupSerialByFileValidDTO> lsSerialDtoCheckDB = new ArrayList<>();
            if (processingFile != null) {
                Sheet sheetProcess = processingFile.getSheetAt(0);
                //validate so dong
                int totalRow = processingFile.getTotalRowAtSheet(sheetProcess);
                if (totalRow > DataUtil.safeToInt(limitSizeUpload)) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.utilities.look.serial.limit.size.upload", limitSizeUpload, limitSizeUpload);
                }

                List<String> serialForDup = Lists.newArrayList();//list serial dung cho check trung lap
                List<String> lsSerialNotEmpty = Lists.newArrayList();//list serial dung cho check trung lap

                int index = 0;
                for (Row row : sheetProcess) {
                    String serial = DataUtil.safeToString(processingFile.getStringValue(row.getCell(0))).trim();
                    LookupSerialByFileValidDTO dto = new LookupSerialByFileValidDTO(serial, index);
                    listSerialDTOFromFileFull.add(dto);
                    index++;
                    if (DataUtil.isNullOrEmpty(serial)) {
                        dto.setErrMsg(getText("mn.stock.utilities.look.serial.not.empty.serial"));
                    } else {
                        lsSerialNotEmpty.add(serial);
                        if (serial.length() > 20L) {//check do dai serial ko duoc vuot qua 20
                            dto.setErrMsg(getText("mn.stock.utilities.look.serial.length.serial"));
                        } else if (!DataUtil.validateStringByPattern(serial, Const.REGEX.NUMBER_REGEX)) {//check serial phai la kieu so
                            dto.setErrMsg(getText("MSG.exp.cmd.underlying.from.file.14"));
                        } else  if (serialForDup.contains(serial)) {//check serial trung lap
                            dto.setErrMsg(getText("mn.stock.utilities.look.serial.duplicate"));
                        } else {
                            serialForDup.add(serial);
                            lsSerialDtoCheckDB.add(dto);
                        }
                    }
                }
                //kiem tra noi dung file cot 1 ma trong het thi bao loi
                if (lsSerialNotEmpty.isEmpty()) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "mn.stock.status.isdn.delete.valid.file.empty");
                }
                List<String> lsSerialCheckDb = Lists.newArrayList();
                lsSerialDtoCheckDB.forEach(obj->lsSerialCheckDb.add(obj.getSerial()));
                if (lsSerialCheckDb.size() > 0) {
                    lsLookupSerialByFileDTO = DataUtil.defaultIfNull(stockTransSerialService.searchSerialExact(Const.STOCK_TYPE.STOCK_CARD, lsSerialCheckDb, null, isSearchNew), new ArrayList<>());
                }

                List<String> lsExistDb = Lists.newArrayList();
                lsLookupSerialByFileDTO.forEach(obj->lsExistDb.add(obj.getSerial()));

                for (LookupSerialByFileValidDTO dto : listSerialDTOFromFileFull) {
                    if (DataUtil.isNullOrEmpty(dto.getErrMsg())) {
                        BigInteger serialFile = new BigInteger(dto.getSerial());
                        boolean isExist = false;
                        for (String serial : lsExistDb) {
                            BigInteger serialDB = new BigInteger(serial);
                            if(serialFile.compareTo(serialDB) == 0){
                                isExist = true;
                                break;
                            }
                        }
                        if (!isExist) {
                            dto.setErrMsg(getText("mn.stock.utilities.look.serial.find.DB"));
                            hasFileError = true;
                        }
                    } else {
                        hasFileError = true;
                    }
                }
            }
        } catch (LogicException ex) {
            reportError("", ex);
            focusElementError("outputAttach");
        } catch (Exception e) {
            reportError("", "common.error.happened", e);
        }
    }

    /**
     * xu ly clear xoa file
     * @author ThanhNT77
     */
    public void doClearFileUpload() {
        attachFileName = null;
        processingFile = null;
        fileUpload = null;
        byteContent = null;
        hasFileError = false;
        typeSearch = "1";
    }

    /**
     * ham dowload temlpate tra cuu serial
     *
     * @author ThanhNT
     */
    @Secured("@")
    public void downloadFileTemplate() {
        try {
            InputStream createStream = Faces.getResourceAsStream(Const.BRAS_IPPOOL.RESOURCE_TEMPLATE_PATH + Const.REPORT_TEMPLATE.SEARCH_SERIAL);
            HSSFWorkbook workbook = new HSSFWorkbook(createStream);
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"" + "LookupSerialByFile_List.xls" + "\"");
            externalContext.setResponseContentType("application/vnd.ms-excel");
            workbook.write(externalContext.getResponseOutputStream());
            facesContext.responseComplete();
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
        }
    }

    /**
     * ham download file loi
     * @author thanhnt77
     * @return
     */
    @Secured("@")
    public StreamedContent downloadFileError() {
        try {
            FileExportBean fileExportBean = new FileExportBean();
            fileExportBean.setOutName("LookupSerialByFile_List_Error_" + staffDTO.getName() + ".xls");
            fileExportBean.setTempalatePath(FileUtil.getTemplatePath());
            fileExportBean.setOutPath(FileUtil.getOutputPath());
            fileExportBean.setTemplateName(Const.REPORT_TEMPLATE.SEARCH_SERIAL_ERROR);
            fileExportBean.putValue("listSerialDTOFromFileFull", listSerialDTOFromFileFull);
            return FileUtil.exportToStreamed(fileExportBean);
//            FileUtil.exportFile(fileExportBean);
//            return fileExportBean.getExportFileContent();
        } catch (Exception ex) {
            logger.error(ex);
            reportError("", "", "common.error.happened");
            topPage();
        }
        return null;
    }

    @Secured("@")
    public void onTabChange(TabChangeEvent event) {
        TabView tabView = (TabView) event.getComponent();
        tabIndex = tabView.getChildren().indexOf(event.getTab());
        doResetSearial();
        doClearFileUpload();
        init();
    }

    //getter and setter
    public List<LookupSerialByFileDTO> getLsLookupSerialByFileDTO() {
        return lsLookupSerialByFileDTO;
    }

    public void setLsLookupSerialByFileDTO(List<LookupSerialByFileDTO> lsLookupSerialByFileDTO) {
        this.lsLookupSerialByFileDTO = lsLookupSerialByFileDTO;
    }

    public List<ViewSerialHistoryDTO> getLsViewSerialHistoryDTO() {
        return lsViewSerialHistoryDTO;
    }

    public void setLsViewSerialHistoryDTO(List<ViewSerialHistoryDTO> lsViewSerialHistoryDTO) {
        this.lsViewSerialHistoryDTO = lsViewSerialHistoryDTO;
    }

    public List<ProductOfferTypeDTO> getLsProductOfferTypeDTO() {
        return lsProductOfferTypeDTO;
    }

    public void setLsProductOfferTypeDTO(List<ProductOfferTypeDTO> lsProductOfferTypeDTO) {
        this.lsProductOfferTypeDTO = lsProductOfferTypeDTO;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getSerialDlg() {
        return serialDlg;
    }

    public void setSerialDlg(String serialDlg) {
        this.serialDlg = serialDlg;
    }

    public String getTypeSearch() {
        return typeSearch;
    }

    public void setTypeSearch(String typeSearch) {
        this.typeSearch = typeSearch;
    }

    public String getTypeSearchDlg() {
        return typeSearchDlg;
    }

    public void setTypeSearchDlg(String typeSearchDlg) {
        this.typeSearchDlg = typeSearchDlg;
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

    public Long getProductOfferTypeId() {
        return productOfferTypeId;
    }

    public void setProductOfferTypeId(Long productOfferTypeId) {
        this.productOfferTypeId = productOfferTypeId;
    }

    public ProductOfferingDTO getProdOfferingDTOSelect() {
        return prodOfferingDTOSelect;
    }

    public void setProdOfferingDTOSelect(ProductOfferingDTO prodOfferingDTOSelect) {
        this.prodOfferingDTOSelect = prodOfferingDTOSelect;
    }

    public LookupSerialByFileDTO getSelected() {
        return selected;
    }

    public void setSelected(LookupSerialByFileDTO selected) {
        this.selected = selected;
    }

    public String getAttachFileName() {
        return attachFileName;
    }

    public void setAttachFileName(String attachFileName) {
        this.attachFileName = attachFileName;
    }

    public byte[] getByteContent() {
        return byteContent;
    }

    public void setByteContent(byte[] byteContent) {
        this.byteContent = byteContent;
    }

    public UploadedFile getFileUpload() {
        return fileUpload;
    }

    public void setFileUpload(UploadedFile fileUpload) {
        this.fileUpload = fileUpload;
    }

    public ExcellUtil getProcessingFile() {
        return processingFile;
    }

    public void setProcessingFile(ExcellUtil processingFile) {
        this.processingFile = processingFile;
    }

    public boolean isHasFileError() {
        return hasFileError;
    }

    public void setHasFileError(boolean hasFileError) {
        this.hasFileError = hasFileError;
    }

    public List<LookupSerialByFileValidDTO> getListSerialDTOFromFileFull() {
        return listSerialDTOFromFileFull;
    }

    public void setListSerialDTOFromFileFull(List<LookupSerialByFileValidDTO> listSerialDTOFromFileFull) {
        this.listSerialDTOFromFileFull = listSerialDTOFromFileFull;
    }

    public String getTypeImport() {
        return typeImport;
    }

    public void setTypeImport(String typeImport) {
        this.typeImport = typeImport;
    }

    public int getTabIndex() {
        return tabIndex;
    }

    public void setTabIndex(int tabIndex) {
        this.tabIndex = tabIndex;
    }

    public boolean isDirectLink() {
        return directLink;
    }

    public void setDirectLink(boolean directLink) {
        this.directLink = directLink;
    }

    public String getSerialGpon() {
        return serialGpon;
    }

    public void setSerialGpon(String serialGpon) {
        this.serialGpon = serialGpon;
    }
}
