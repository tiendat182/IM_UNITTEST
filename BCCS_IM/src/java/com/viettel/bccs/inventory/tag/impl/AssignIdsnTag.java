package com.viettel.bccs.inventory.tag.impl;

import com.google.common.collect.Lists;
import com.viettel.bccs.fw.controller.InventoryController;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.common.ExcellUtil;
import com.viettel.bccs.inventory.common.IsdnUtil;
import com.viettel.bccs.inventory.common.ValidateService;
import com.viettel.bccs.inventory.dto.OptionSetValueDTO;
import com.viettel.bccs.inventory.dto.ProductOfferingTotalDTO;
import com.viettel.bccs.inventory.dto.SearchNumberRangeDTO;
import com.viettel.bccs.inventory.service.OptionSetValueService;
import com.viettel.bccs.inventory.service.ProductOfferingService;
import com.viettel.bccs.inventory.service.StockNumberService;
import com.viettel.bccs.inventory.tag.AssignIsdnTagNameable;
import com.viettel.bccs.inventory.tag.ShopInfoNameable;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.web.common.annotation.Security;
import com.viettel.web.common.controller.BaseController;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.omnifaces.util.Faces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.InputStream;
import java.util.*;

/**
 * Created by hoangnt14 on 12/26/2015.
 */
@Service
@Scope("prototype")
public class AssignIdsnTag extends InventoryController implements AssignIsdnTagNameable {

    @Autowired
    private StockNumberService stockNumberService;
    @Autowired
    private ShopInfoNameable shopInfoTag;
    @Autowired
    private OptionSetValueService optionSetValueService;
    @Autowired
    private ProductOfferingService productOfferingService;
    private Object objectController;
    private String typeService;
    private static final String SELECT_FROM_FILE = "selectFromFile";
    private static final String ENTERED_DIGITAL_RANGES = "enteredDigitalRanges";
    private boolean renderedFromFile = true;
    private SearchNumberRangeDTO searchStockNumberDTO;
    private List<OptionSetValueDTO> optionSetValueDTOs = Lists.newArrayList();
    private UploadedFile uploadedFile;
    private byte[] byteContent;
    private String fileName;
    private ProductOfferingTotalDTO productOfferingTotalDTO = new ProductOfferingTotalDTO();
    private List<ProductOfferingTotalDTO> lsProductOfferingTotalDTO = Lists.newArrayList();
    private ProductOfferingTotalDTO productOfferingTotalNewDTO = new ProductOfferingTotalDTO();
    private List<ProductOfferingTotalDTO> lsProductOfferingTotalNewDTO = Lists.newArrayList();
    private int limitAutoComplete;
    private static final String FILE_TEMPLATE_PATH = "assignStockModelForIsdnPattern.xls";
    private static final String FILE_TEMPLATE_PATH_ERROR = "assignStockModelForIsdnPatternError.xls";
    //map isdn,status
    private HashMap<String, String> mapISDN = new HashMap<String, String>();
    private HashMap<String, String> mapISDNError = new HashMap<String, String>();
    private List<String> lstError = new ArrayList<String>();
    private boolean hasFileError = false;
    private ExcellUtil processingFile;
    private static Long MOBILE_TYPE = 1L;
    private String serviceType = "1";
    private String newServiceType = "1";
    private String serviceName;

    @Override
    public void init(Object objectController) {
        try {
            typeService = SELECT_FROM_FILE;
            initOptionSet();
            this.objectController = objectController;
            shopInfoTag.initAllShop(objectController);
            renderedFromFile = false;
            searchStockNumberDTO = new SearchNumberRangeDTO();
            limitAutoComplete = DataUtil.safeToInt(optionSetValueService.getValueByNameFromOptionSetValue(Const.OPTION_SET.LIMIT_AUTOCOMPLETE));
            lsProductOfferingTotalDTO = DataUtil.defaultIfNull(productOfferingService.getLsProductOfferingByProductType("", 0L), new ArrayList<ProductOfferingTotalDTO>());
            lsProductOfferingTotalNewDTO = DataUtil.defaultIfNull(productOfferingService.getLsProductOfferingByProductType("", 0L), new ArrayList<ProductOfferingTotalDTO>());
        } catch (Exception e) {
            this.reportError("", "common.error.happened", e);
        }
    }


    public void initOptionSet() {
        try {
            List<OptionSetValueDTO> listOptionSet = optionSetValueService.getByOptionSetCode(Const.TELCO_FOR_NUMBER);
            if (listOptionSet != null && listOptionSet.size() > 1) {
                Collections.sort(listOptionSet, new Comparator<OptionSetValueDTO>() {
                            @Override
                            public int compare(OptionSetValueDTO o1, OptionSetValueDTO o2) {
                                if (o1 != null && o1.getName() != null && o2 != null && o2.getName() != null) {
                                    return Long.valueOf(o1.getValue()).compareTo(Long.valueOf(o2.getValue()));
                                } else {
                                    logger.error("o1 or or name is null");
                                    return 0;
                                }
                            }
                        }
                );
            }
            optionSetValueDTOs.addAll(listOptionSet);
//            serviceName = getServiceNameById();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "", "common.error.happened");
        }
    }

    @Override
    public void setServiceNameById() {
        String tmp = null;
        for (OptionSetValueDTO optionSetValueDTO : optionSetValueDTOs) {
            if (optionSetValueDTO.getValue().trim().equals(serviceType.trim())) {
                tmp = optionSetValueDTO.getName();
            }
        }
        this.serviceName = tmp;
        this.newServiceType = serviceType;
    }

    @Override
    public void changeServiceType() {
        if (DataUtil.isNullOrEmpty(serviceType) || !IsdnUtil.checkTelecomService(serviceType, searchStockNumberDTO.getServiceType())) {
            serviceType = IsdnUtil.getTelecomService(searchStockNumberDTO.getServiceType());
//            serviceName = getServiceNameById();
            productOfferingTotalDTO = new ProductOfferingTotalDTO();
            productOfferingTotalNewDTO = new ProductOfferingTotalDTO();
        } else {
            serviceType = IsdnUtil.getTelecomService(searchStockNumberDTO.getServiceType());
//            serviceName = getServiceNameById();
        }

    }


    @Override
    public void fileUploadAction(FileUploadEvent event) {
        try {
            hasFileError = false;
            mapISDN = new HashMap<String, String>();
            mapISDNError = new HashMap<String, String>();
            lstError = new ArrayList<String>();
            uploadedFile = event.getFile();

            BaseMessage message = validateFileUploadWhiteList(uploadedFile, ALOW_EXTENSION_EXCEL_TYPE_LIST, MAX_SIZE_5M);
            if (!message.isSuccess()) {
                uploadedFile = null;
                fileName = null;
                LogicException ex = new LogicException(message.getErrorCode(), message.getKeyMsg());
                ex.setDescription(message.getDescription());
                focusElementByRawCSSSlector(".outputAttachFile");
                throw ex;
            }

            byteContent = uploadedFile.getContents();
            fileName = uploadedFile.getFileName();
        } catch (LogicException ex) {
            reportError("", ex);
            topPage();
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
            logger.error(ex.getMessage(), ex);
            topPage();
        }


    }


    public void downloadFileTemplate() {
        try {
            InputStream createStream = Faces.getResourceAsStream(Const.BRAS_IPPOOL.RESOURCE_TEMPLATE_PATH + FILE_TEMPLATE_PATH);
            HSSFWorkbook workbook = new HSSFWorkbook(createStream);
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"" + "assignStockModelForIsdnPattern.xls" + "\"");
            externalContext.setResponseContentType("application/vnd.ms-excel");
            workbook.write(externalContext.getResponseOutputStream());
            facesContext.responseComplete();
            return;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "", "common.error.happened");
        }
    }

    @Security
    public void onChangeStartRange() {
        String startRange = searchStockNumberDTO.getStartRange();
        startRange = startRange.replaceAll("[^0-9]", "");
        startRange = startRange.replaceAll("^0+", "");
        searchStockNumberDTO.setStartRange(startRange);
    }

    @Security
    public void onChangeEndRange() {
        String endRange = searchStockNumberDTO.getEndRange();
        endRange = endRange.replaceAll("[^0-9]", "");
        endRange = endRange.replaceAll("^0+", "");
        searchStockNumberDTO.setEndRange(endRange);
    }


    public int checkFileImport() {
        try {
            //kiem tra danh sach thue bao khong hop le
            lstError = stockNumberService.checkListStockNumber(searchStockNumberDTO);
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "", ex.getDescription());
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "", "common.error.happened");
        }
        return 0;
    }

    public void clearProductNew() {
        productOfferingTotalNewDTO = new ProductOfferingTotalDTO();
    }

    public void clearProductOld() {
        productOfferingTotalDTO = new ProductOfferingTotalDTO();
    }

    public int processInputExcel() {
        try {
            processingFile = new ExcellUtil(uploadedFile, byteContent);
            Sheet sheetProcess = processingFile.getSheetAt(0);
            int totalRow = sheetProcess.getPhysicalNumberOfRows();
            if (totalRow == 0 || totalRow == 1) {
                return -1;
            }
            if (totalRow > 1001) {
                return -2;
            }
            searchStockNumberDTO.setMapISDN(new HashMap<String, String>());
            mapISDN = searchStockNumberDTO.getMapISDN();
            mapISDNError = new HashMap<String, String>();
            lsProductOfferingTotalDTO = DataUtil.defaultIfNull(productOfferingService.getLsProductOfferingByProductType("", 0L), new ArrayList<ProductOfferingTotalDTO>());
            for (int i = 1; i < totalRow; i++) {
                Row row = sheetProcess.getRow(i);
                if (row == null) {
                    hasFileError = true;
                    continue;
                }
                int totalCol = processingFile.getTotalColumnAtRow(row);
                if (totalCol < 2) {
                    hasFileError = true;
                    continue;
                }
                String isdn = processingFile.getStringValue(row.getCell(0)).trim();
                if (isdn == null) {
                    hasFileError = true;
                    continue;
                }
                String col2 = processingFile.getStringValue(row.getCell(1)).trim();
                if (!DataUtil.isNumber(isdn) || !validateNumberAndLength(isdn, 8, 11)) {
                    hasFileError = true;
                    continue;
                }
                ProductOfferingTotalDTO productOfferingTotalDTO = ValidateService.getProductOfferingByCode(col2, lsProductOfferingTotalDTO);
                Long telecomServiceId;
                if (DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_ISDN_MOBILE.toString(), searchStockNumberDTO.getServiceType())) {
                    telecomServiceId = Const.STOCK_TYPE.STOCK_ISDN_MOBILE;
                } else if (DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_ISDN_HOMEPHONE.toString(), searchStockNumberDTO.getServiceType())) {
                    telecomServiceId = Const.STOCK_TYPE.STOCK_ISDN_HOMEPHONE;
                } else {
                    telecomServiceId = Const.STOCK_TYPE.STOCK_ISDN_PSTN;
                }
                if (DataUtil.isNullObject(productOfferingTotalDTO)
                        || !productOfferingTotalDTO.getProductOfferTypeId().equals(telecomServiceId)) {
                    hasFileError = true;
                    continue;
                } else {
                    mapISDN.put(isdn.replaceAll("^0+", ""), col2 + "");
                }
            }
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "", ex.getDescription());
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "", "common.error.happened");
        }
        return 0;
    }


    @Override
    public void changeTypeService() {
        if (DataUtil.safeEqual(typeService, SELECT_FROM_FILE)) {
            renderedFromFile = true;
        } else {
            renderedFromFile = false;
        }
    }


    @Override
    public List<ProductOfferingTotalDTO> doChangeOffering(String inputProduct) {
        try {
            String input = DataUtil.isNullOrEmpty(inputProduct) ? "" : inputProduct.trim();
            List<ProductOfferingTotalDTO> test = productOfferingService.getLsProductOfferingByProductType(input, DataUtil.safeToLong(serviceType));
            return test;
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex.getKeyMsg(), ex);
            topPage();
        } catch (Exception e) {
            reportError("", "common.error.happened", e);
            logger.error(e.getMessage(), e);
            topPage();
        }
        return new ArrayList<ProductOfferingTotalDTO>();
    }

    @Override
    public List<ProductOfferingTotalDTO> doChangeOfferingNew(String inputProduct) {
        try {
            String input = DataUtil.isNullOrEmpty(inputProduct) ? "" : inputProduct.trim();
            Long serviceTypeToSearch;
            if (Const.STOCK_TYPE.STOCK_ISDN_MOBILE.toString().equals(newServiceType)) {
                serviceTypeToSearch = Const.STOCK_TYPE.STOCK_ISDN_MOBILE;
            } else if (Const.STOCK_TYPE.STOCK_ISDN_HOMEPHONE.toString().equals(newServiceType)) {
                serviceTypeToSearch = Const.STOCK_TYPE.STOCK_ISDN_HOMEPHONE;
            } else {
                serviceTypeToSearch = Const.STOCK_TYPE.STOCK_ISDN_PSTN;
            }
//            List<ProductOfferingTotalDTO> test = productOfferingService.get();
            List<ProductOfferingTotalDTO> test = productOfferingService.getLsProductOfferingByProductType(input, serviceTypeToSearch);
            return test;
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex.getKeyMsg(), ex);
            topPage();
        } catch (Exception e) {
            reportError("", "common.error.happened", e);
            logger.error(e.getMessage(), e);
            topPage();
        }
        return new ArrayList<ProductOfferingTotalDTO>();
    }

//    private int validateFileInput(HSSFWorkbook errorWorkbook) {
//        try {
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            reportError("", "common.error.happened", ex);
//            topPage();
//        }
//    }

    @Secured("@")
    public String getConfirmMessage() {
        if (!DataUtil.isNullObject(this.getProductOfferingTotalNewDTO())
                && !DataUtil.isNullOrEmpty(this.getProductOfferingTotalNewDTO().getName())) {
            return getText("mn.stock.utilities.update.model.confim").replace("{0}", this.getProductOfferingTotalNewDTO().getName());
        } else {
            return null;
        }
    }

    public void downloadFileError() {
        try {

            InputStream createStream = Faces.getResourceAsStream(Const.BRAS_IPPOOL.RESOURCE_TEMPLATE_PATH + FILE_TEMPLATE_PATH_ERROR);
            HSSFWorkbook workbook = new HSSFWorkbook(createStream);
            Sheet errorSheet = workbook.getSheetAt(0);
            Sheet sheetUpload = processingFile.getSheetAt(0);
            int totalRow = sheetUpload.getPhysicalNumberOfRows();
            if (totalRow == 0 || totalRow == 1) {
//                reportErrorValidateFail("frmAssignStatus:assignTypeMsgs", "", "common.error.happened");
                hasFileError = false;
                return;
            }
            //ghi file loi, bo dong header
            int j = 1;
            for (int i = 1; i < totalRow; i++) {
                Row rowUpload = sheetUpload.getRow(i);
                if (rowUpload == null) {
                    Row errorRow = errorSheet.createRow(j);
                    errorRow.createCell(2).setCellValue(getText("mn.stock.status.isdn.file.row.null"));
                    //sang ghi row tiep
                    j++;
                    continue;
                }
                int totalCol = processingFile.getTotalColumnAtRow(rowUpload);
                if (totalCol < 2) {
                    hasFileError = true;
                    Row errorRow = errorSheet.createRow(j);
                    for (int k = 0; k < totalCol; k++) {
                        errorRow.createCell(k).setCellValue(processingFile.getStringValue(rowUpload.getCell(k)));
                    }
                    errorRow.createCell(2).setCellValue(getText("mn.stock.status.isdn.update.file.isdn.product.wrongformat"));
                    j++;
                    continue;
                }
                String isdn = processingFile.getStringValue(rowUpload.getCell(0)).trim();
                if (DataUtil.isNullOrEmpty(isdn)) {
                    hasFileError = true;
                    Row errorRow = errorSheet.createRow(j);
                    for (int k = 0; k < totalCol; k++) {
                        errorRow.createCell(k).setCellValue(processingFile.getStringValue(rowUpload.getCell(k)));
                    }
                    errorRow.createCell(2).setCellValue(getText("mn.stock.status.isdn.file.isdn.null"));
                    j++;
                    continue;
                }
                //kiem tra isdn
                if (!DataUtil.isNumber(isdn) || !validateNumberAndLength(isdn, 8, 11)) {
                    hasFileError = true;
                    Row errorRow = errorSheet.createRow(j);
                    for (int k = 0; k < totalCol; k++) {
                        errorRow.createCell(k).setCellValue(processingFile.getStringValue(rowUpload.getCell(k)));
                    }
                    errorRow.createCell(2).setCellValue(getText("mn.stock.status.isdn.file.isdn.number"));
                    j++;
                    continue;
                }
                //validate status co dung dinh dang khong
                String col2 = processingFile.getStringValue(rowUpload.getCell(1)).trim();
                if (DataUtil.isNullOrEmpty(col2)) {
                    if (DataUtil.isNullObject(productOfferingTotalDTO)) {
                        hasFileError = true;
                        Row errorRow = errorSheet.createRow(j);
                        for (int k = 0; k < totalCol; k++) {
                            errorRow.createCell(k).setCellValue(processingFile.getStringValue(rowUpload.getCell(k)));
                        }
                        errorRow.createCell(2).setCellValue(getText("mn.stock.status.isdn.update.file.isdn.product.not.empty"));
                        j++;
                        continue;
                    }
                } else {
                    ProductOfferingTotalDTO productOfferingTotalDTO = ValidateService.getProductOfferingByCode(col2, lsProductOfferingTotalDTO);
                    if (DataUtil.isNullObject(productOfferingTotalDTO)) {
                        hasFileError = true;
                        Row errorRow = errorSheet.createRow(j);
                        for (int k = 0; k < totalCol; k++) {
                            errorRow.createCell(k).setCellValue(processingFile.getStringValue(rowUpload.getCell(k)));
                        }
                        errorRow.createCell(2).setCellValue(getText("mn.stock.status.isdn.update.file.isdn.product.not.contains"));
                        j++;
                        continue;
                    }
                }
                //kiem tra isdn co trong list error khong
                if (lstError != null && lstError.contains(isdn)) {
                    hasFileError = true;
                    Row errorRow = errorSheet.createRow(j);
                    for (int k = 0; k < totalCol; k++) {
                        errorRow.createCell(k).setCellValue(processingFile.getStringValue(rowUpload.getCell(k)));
                    }
                    errorRow.createCell(2).setCellValue(getText("mn.stock.status.isdn.update.file.isdn.status.wrongisdn"));
                    j++;
                    continue;
                }
            }
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"" + "error.xls" + "\"");
            externalContext.setResponseContentType("application/vnd.ms-excel");
            workbook.write(externalContext.getResponseOutputStream());
            facesContext.responseComplete();
            return;
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
        }
//        return null;
    }

    public boolean validateNumberAndLength(String str, int min, int max) {
        if (str == null || (str.length() < min) || str.length() > max) {
            return false;
        }
        try {
            Long.parseLong(str);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            return false;
        }
        return true;
    }

    public String getTypeService() {
        return typeService;
    }

    public void setTypeService(String typeService) {
        this.typeService = typeService;
    }

    public String getSelectFromFile() {
        return SELECT_FROM_FILE;
    }

    public String getEnteredDigitalRanges() {
        return ENTERED_DIGITAL_RANGES;
    }

    public boolean isRenderedFromFile() {
        return renderedFromFile;
    }

    public void setRenderedFromFile(boolean renderedFromFile) {
        this.renderedFromFile = renderedFromFile;
    }

    public ShopInfoNameable getShopInfoTag() {
        return shopInfoTag;
    }

    public void setShopInfoTag(ShopInfoNameable shopInfoTag) {
        this.shopInfoTag = shopInfoTag;
    }

    public SearchNumberRangeDTO getSearchStockNumberDTO() {
        return searchStockNumberDTO;
    }

    public void setSearchStockNumberDTO(SearchNumberRangeDTO searchStockNumberDTO) {
        this.searchStockNumberDTO = searchStockNumberDTO;
    }

    public List<OptionSetValueDTO> getOptionSetValueDTOs() {
        return optionSetValueDTOs;
    }

    public void setOptionSetValueDTOs(List<OptionSetValueDTO> optionSetValueDTOs) {
        this.optionSetValueDTOs = optionSetValueDTOs;
    }

    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Object getObjectController() {
        return objectController;
    }

    public void setObjectController(Object objectController) {
        this.objectController = objectController;
    }

    public byte[] getByteContent() {
        return byteContent;
    }

    public void setByteContent(byte[] byteContent) {
        this.byteContent = byteContent;
    }

    public OptionSetValueService getOptionSetValueService() {
        return optionSetValueService;
    }

    public void setOptionSetValueService(OptionSetValueService optionSetValueService) {
        this.optionSetValueService = optionSetValueService;
    }


    public ProductOfferingTotalDTO getProductOfferingTotalDTO() {
        return productOfferingTotalDTO;
    }

    public void setProductOfferingTotalDTO(ProductOfferingTotalDTO productOfferingTotalDTO) {
        this.productOfferingTotalDTO = productOfferingTotalDTO;
    }

    public List<ProductOfferingTotalDTO> getLsProductOfferingTotalDTO() {
        return lsProductOfferingTotalDTO;
    }

    public void setLsProductOfferingTotalDTO(List<ProductOfferingTotalDTO> lsProductOfferingTotalDTO) {
        this.lsProductOfferingTotalDTO = lsProductOfferingTotalDTO;
    }

    public int getLimitAutoComplete() {
        return limitAutoComplete;
    }

    public void setLimitAutoComplete(int limitAutoComplete) {
        this.limitAutoComplete = limitAutoComplete;
    }

    public ProductOfferingTotalDTO getProductOfferingTotalNewDTO() {
        return productOfferingTotalNewDTO;
    }

    public void setProductOfferingTotalNewDTO(ProductOfferingTotalDTO productOfferingTotalNewDTO) {
        this.productOfferingTotalNewDTO = productOfferingTotalNewDTO;
    }

    public List<ProductOfferingTotalDTO> getLsProductOfferingTotalNewDTO() {
        return lsProductOfferingTotalNewDTO;
    }

    public void setLsProductOfferingTotalNewDTO(List<ProductOfferingTotalDTO> lsProductOfferingTotalNewDTO) {
        this.lsProductOfferingTotalNewDTO = lsProductOfferingTotalNewDTO;
    }

    public boolean isHasFileError() {
        return hasFileError;
    }

    public void setHasFileError(boolean hasFileError) {
        this.hasFileError = hasFileError;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getNewServiceType() {
        return newServiceType;
    }

    public void setNewServiceType(String newServiceType) {
        this.newServiceType = newServiceType;
    }
}
