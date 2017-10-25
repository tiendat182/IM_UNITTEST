package com.viettel.bccs.inventory.controller.stock;

import com.google.common.collect.Lists;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.fw.controller.InventoryController;
import com.viettel.bccs.inventory.dto.StockCheckReportDTO;
import com.viettel.bccs.inventory.dto.VShopStaffDTO;
import com.viettel.bccs.inventory.service.StockCheckReportService;
import com.viettel.bccs.inventory.tag.ShopInfoNameable;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.dto.BaseMessage;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
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
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by hoangnt14 on 12/23/2015.
 */
@Component
@Scope("view")
@ManagedBean
public class PayInventoryLastMonthController extends InventoryController {

    @Autowired
    private StockCheckReportService stockCheckReportSv;
    @Autowired
    private ShopInfoNameable shopInfoTag;

    private List<StockCheckReportDTO> listStockDTOEdit = Lists.newArrayList();
    private List<StockCheckReportDTO> listStockDTOSearch;
    private StockCheckReportDTO forEditStockList;
    private StockCheckReportDTO forSearchStockList;
    private List<StockCheckReportDTO> curStockList;
    private UploadedFile fileUpload;
    private byte[] byteContent;
    private String fileName;
    private int index;
    private VShopStaffDTO curShopStaff;
    private String strDate;
    private final String dateFormat = "MM/yyyy";

    @Secured("@")
    @PostConstruct
    public void init() {
        try {
            strDate = (new SimpleDateFormat(dateFormat)).format(new Date());
            createData();
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "", ex.getDescription());
        } catch (Exception ex) {
            logger.error(ex);
            reportError("", "", "common.error.happen");
        }
    }

    private void createData() throws Exception {
        shopInfoTag.initShop(this, BccsLoginSuccessHandler.getStaffDTO().getShopId().toString(), true);
        index = 0;
        doResetSearchStockCheckReport();
        initCurStockList(forSearchStockList);
        forEditStockList = new StockCheckReportDTO();

    }

    @Secured("@")
    public void doResetSearchStockCheckReport() {
        try {
            fileUpload = null;
            fileName = null;
            shopInfoTag.loadShop(BccsLoginSuccessHandler.getStaffDTO().getShopId().toString(), false);
            curShopStaff = shopInfoTag.getvShopStaffDTO();
            forSearchStockList = new StockCheckReportDTO();
            forSearchStockList.setOwnerId(BccsLoginSuccessHandler.getStaffDTO().getShopId());
            forSearchStockList.setCheckDate(new Date());
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "common.error.happened", ex);
            topPage();
        }
    }

    private void initCurStockList(StockCheckReportDTO stockCheckReportDTO) {
        try {
            curStockList = Lists.newArrayList();
            if (!DataUtil.isNullOrEmpty(strDate)) {
                Date date = (new SimpleDateFormat(dateFormat)).parse(strDate);
                stockCheckReportDTO.setCheckDate(date);
            }
            curStockList = stockCheckReportSv.onSearch(stockCheckReportDTO.getOwnerId(), stockCheckReportDTO.getCheckDate());
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "", ex.getDescription());
        } catch (Exception ex) {
            logger.error(ex);
            reportError("", "", "common.error.happen");
        }
    }

    @Secured("@")
    public void searchStockCheckReportList() {
        try {
            if (validate(true) != 0) {
                topPage();
                return;
            }
            if (!formatDate()) {
                topPage();
                return;
            }
            initCurStockList(forSearchStockList);
        } catch (Exception ex) {
            logger.error(ex);
            reportError("", "", "common.error.happen");
        }
    }

    @Secured("@")
    public boolean formatDate() {
        if (!DataUtil.isNullOrEmpty(strDate)) {
            if (strDate.contains("/")) {
                if (!checkDate(strDate)) {
                    //bao loi
                    reportError("", "", "mn.stock.utilities.paylastmonth.date.format");
                    return false;
                }
            } else {
                String tmp = strDate;
                if (tmp.length() == 5) {
                    tmp = "0" + tmp;
                }
                if (tmp.length() == 6) {
                    tmp = tmp.substring(0, 2) + "/" + tmp.substring(2);
                    if (checkDate(tmp)) {
                        strDate = tmp;
                    } else {
                        //bao loi
                        reportError("", "", "mn.stock.utilities.paylastmonth.date.format");
                        return false;
                    }
                } else {
                    //bao loi
                    reportError("", "", "mn.stock.utilities.paylastmonth.date.format");
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkDate(String strMonth) {
        if (DataUtil.isNullOrEmpty(strMonth)) {
            return true;
        }
        if (strMonth.contains("/")) {
            String[] arrMY = strMonth.split("/");
            if (arrMY.length > 2) {
                //bao loi
                return false;
            } else {
                if (DataUtil.isNumber(arrMY[0]) && DataUtil.isNumber(arrMY[1])) {
                    long month = DataUtil.safeToLong(arrMY[0]);
                    long year = DataUtil.safeToLong(arrMY[1]);
                    if (month < 1 || month > 12 || year < 1970 || year > 3000) {
                        //bao loi
                        return false;
                    }
                } else {
                    //bao loi khong phai sang so
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    @Secured("@")
    public void handleFileUpload(FileUploadEvent event) {

        try {
            fileUpload = event.getFile();
            BaseMessage message = validateFileUploadWhiteList(fileUpload, ALOW_EXTENSION_PDF_TYPE_LIST, MAX_SIZE_5M);
            if (!message.isSuccess()) {
                LogicException ex = new LogicException(message.getErrorCode(), message.getKeyMsg());
                ex.setDescription(message.getDescription());
                throw ex;
            }
            fileName = fileUpload.getFileName();
            byteContent = fileUpload.getContents();
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
    public void uploadFileStockCheckReport() {
        try {
            if (validate(false) != 0) {
                topPage();
                return;
            }
            if (!formatDate()) {
                topPage();
                return;
            }
            if (!DataUtil.isNullOrEmpty(strDate)) {
                String format = "MM/yyyy";
                Date date = (new SimpleDateFormat(format)).parse(strDate);
                forSearchStockList.setCheckDate(date);
            }
            forSearchStockList.setOwnerId(DataUtil.safeToLong(curShopStaff.getOwnerId()));
            forSearchStockList.setFileName(fileName);
            forSearchStockList.setFileContent(byteContent);
            stockCheckReportSv.uploadFileStockCheckReport(forSearchStockList, BccsLoginSuccessHandler.getStaffDTO().getStaffCode());
//            doResetSearchStockCheckReport();
//            initCurStockList(forSearchStockList);
            reportSuccess("", "mn.stock.utilities.success.upload");
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "", ex.getDescription());
        } catch (Exception ex) {
            logger.error(ex);
            reportError("", "", "common.error.happen");
        }
        topPage();
    }

    private int validate(boolean search) {
        if (DataUtil.isNullObject(curShopStaff) || DataUtil.isNullOrEmpty(curShopStaff.getOwnerId())) {
            reportError("", "", "mn.stock.utilities.monthyear.shop");
            focusElementByRawCSSSlector(".shopChecked");
            return -1;
        }
        if (DataUtil.isNullOrEmpty(strDate)) {
            reportError("", "", "mn.stock.utilities.monthyear.date.null");
            focusElementByRawCSSSlector(".dateChecked");
            return -1;
        }
        if (!search) {
            if (byteContent == null || byteContent.length < 1) {
                focusElementByRawCSSSlector(".outputAttachFile");
                reportError("", "", "mn.stock.utilities.monthyear.file.null");
                return -1;
            }
            if (DataUtil.isNullOrEmpty(fileName) || !fileName.toUpperCase().endsWith(".PDF")) {
                focusElementByRawCSSSlector(".outputAttachFile");
                reportError("", "", "mn.stock.utilities.monthyear.filename.pdf");
                return -1;
            }
            if (byteContent.length >= 5 * 1024 * 1024) {
                reportError("", "", "mn.stock.utilities.monthyear.file.size");
                focusElementByRawCSSSlector(".outputAttachFile");
                return -1;
            }
        }
        return 0;
    }

    @Secured("@")
    public void receiveShop(VShopStaffDTO vShopStaffDTO) {
        curShopStaff = DataUtil.cloneBean(vShopStaffDTO);
        forSearchStockList.setOwnerId(DataUtil.safeToLong(curShopStaff.getOwnerId()));
    }

    @Secured("@")
    public void validatePreUpload() {
        try {
            if (validate(false) != 0) {
                topPage();
                return;
            }
            if (DataUtil.isNullObject(fileUpload)) {
                reportError("", "", "mn.stock.limit.createFile.require.msg");
                focusElementByRawCSSSlector(".outputAttachFile");
            }
        } catch (Exception ex) {
            logger.error(ex);
            reportError("", "", "common.error.happen");
        }
        topPage();
    }

    @Secured("@")
    public StreamedContent downloadFileAttach(StockCheckReportDTO stockCheckReportDTO) {
        try {
            byte[] content = stockCheckReportSv.getFileStockCheckReport(stockCheckReportDTO.getStockCheckReportId());
            InputStream is = new ByteArrayInputStream(content);
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            StreamedContent streamedContent = new DefaultStreamedContent(is, externalContext.getMimeType(stockCheckReportDTO.getFileName()), stockCheckReportDTO.getFileName());
            return streamedContent;
        } catch (Exception ex) {
            logger.error(ex);
            reportError("", "", "common.error.happened");
        }
        return null;
    }


    public ShopInfoNameable getShopInfoTag() {
        return shopInfoTag;
    }

    public void setShopInfoTag(ShopInfoNameable shopInfoTag) {
        this.shopInfoTag = shopInfoTag;
    }

    public List<StockCheckReportDTO> getListStockDTOEdit() {
        return listStockDTOEdit;
    }

    public void setListStockDTOEdit(List<StockCheckReportDTO> listStockDTOEdit) {
        this.listStockDTOEdit = listStockDTOEdit;
    }

    public List<StockCheckReportDTO> getListStockDTOSearch() {
        return listStockDTOSearch;
    }

    public void setListStockDTOSearch(List<StockCheckReportDTO> listStockDTOSearch) {
        this.listStockDTOSearch = listStockDTOSearch;
    }

    public StockCheckReportDTO getForEditStockList() {
        return forEditStockList;
    }

    public void setForEditStockList(StockCheckReportDTO forEditStockList) {
        this.forEditStockList = forEditStockList;
    }

    public StockCheckReportDTO getForSearchStockList() {
        return forSearchStockList;
    }

    public void setForSearchStockList(StockCheckReportDTO forSearchStockList) {
        this.forSearchStockList = forSearchStockList;
    }

    public List<StockCheckReportDTO> getCurStockList() {
        return curStockList;
    }

    public void setCurStockList(List<StockCheckReportDTO> curStockList) {
        this.curStockList = curStockList;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public UploadedFile getFileUpload() {
        return fileUpload;
    }

    public void setFileUpload(UploadedFile fileUpload) {
        this.fileUpload = fileUpload;
    }

    public byte[] getByteContent() {
        return byteContent;
    }

    public void setByteContent(byte[] byteContent) {
        this.byteContent = byteContent;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getStrDate() {
        return strDate;
    }

    public void setStrDate(String strDate) {
        this.strDate = strDate;
    }
}
