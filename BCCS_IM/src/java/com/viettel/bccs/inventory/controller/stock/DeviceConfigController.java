package com.viettel.bccs.inventory.controller.stock;

import com.google.common.collect.Lists;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.fw.controller.TransCommonController;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.common.ExcellUtil;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.DeviceConfigService;
import com.viettel.bccs.inventory.service.OrderDivideDeviceService;
import com.viettel.bccs.inventory.tag.ShopInfoNameable;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.web.common.annotation.Security;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.omnifaces.util.Faces;
import org.primefaces.component.tabview.TabView;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by vanho on 01/04/2017.
 */
@Component
@Scope("view")
@ManagedBean
public class DeviceConfigController extends TransCommonController {


    //bien phuc vu autocomplete productOffering
    private ProductOfferingTotalDTO productOfferingTotalNewDTO = new ProductOfferingTotalDTO();
    private List<ProductOfferingTotalDTO> lsProductOfferingTotalNewDTO = Lists.newArrayList();

    private ProductOfferingTotalDTO productOfferingTotalAddDTO = new ProductOfferingTotalDTO();
    private List<ProductOfferingTotalDTO> lsProductOfferingTotalAddDTO = Lists.newArrayList();

    private List<ProductOfferingTotalDTO> lsProductOfferingTotalAddDetailDTO = Lists.newArrayList();

    private StaffDTO currentStaff;
    private List<OptionSetValueDTO> lsProductStatus;
    private List<DeviceConfigDTO> lstDeviceConfig;
    private List<DeviceConfigDTO> lstDeviceConfigSelection;
    private List<DeviceConfigDTO> lstDeviceConfigUpdate;
    private List<DeviceConfigDetailDTO> listAddDeviceConfigDetail = Lists.newArrayList();
    private List<String> listErrFile;
    private byte[] byteContent;
    private boolean selectedFile;
    private boolean showPreview;
    private DeviceConfigDTO inputSearch;
    private DeviceConfigDTO addDeviceConfig;
    private Map<String,DeviceConfigDTO> lstDeviceConfigImport;
    private Map<String,List<DeviceConfigDetailDTO>> lstDeviceDetailConfigImport;
    private UploadedFile uploadedFile;
    private String attachFileName;
    private ExcellUtil processingFile;
    private StreamedContent resultImportFile;
    private File fileResultImport;
    private boolean isCreate;
    private boolean isPreview;
    private boolean disableConfigDevice;
    private String suffix;
    private int tabIndex;
    private int indexDelete;
    private boolean showDownResult;//bien hien thi link tai file ket qua tai nhap theo file
    private List<DivideDevicePreview> previewFile = Lists.newArrayList();
    private List<DivideDevicePreview> previewFileClone = Lists.newArrayList();
    @Autowired
    private DeviceConfigService deviceConfigService;
    @Autowired
    private ShopInfoNameable shopInfoExportTagAdd;
    @Autowired
    private OrderDivideDeviceService orderDivideDeviceService;

    @Security("@")
    @PostConstruct
    public void init() {

        try {
            inputSearch = new DeviceConfigDTO();
            addDeviceConfig = new DeviceConfigDTO();
            currentStaff = BccsLoginSuccessHandler.getStaffDTO();
            productOfferingTotalNewDTO = new ProductOfferingTotalDTO();
            lsProductOfferingTotalNewDTO = DataUtil.defaultIfNull(deviceConfigService.getLsProductOfferingByProductTypeAndState("", Const.DEVICE_CONFIG.PROB_OFFER_TYPE_ID, null), new ArrayList<>());
            lsProductOfferingTotalAddDTO = DataUtil.defaultIfNull(deviceConfigService.getLsProductOfferingByProductTypeAndState("", Const.DEVICE_CONFIG.PROB_OFFER_TYPE_ID, null), new ArrayList<>());
            lsProductOfferingTotalAddDetailDTO = DataUtil.defaultIfNull(deviceConfigService.getLsProductOfferingByProductTypeAndState("", Const.DEVICE_CONFIG.ITEM_OFFER_TYPE_ID, null), new ArrayList<>());
            lsProductStatus = DataUtil.defaultIfNull(optionSetValueService.getByOptionSetCode(Const.OPTION_SET.PRODUCT_STATUS), new ArrayList<>());
            shopInfoExportTagAdd.initShopAndAllChild(this, DataUtil.safeToString(currentStaff.getShopId()), true, Const.OWNER_TYPE.SHOP);
            disableConfigDevice = true;
            attachFileName = "";
            showDownResult = false;
            lstDeviceConfigImport = new HashMap<>();
            lstDeviceDetailConfigImport = new HashMap<>();
            previewFile = new ArrayList<>();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "", "common.error.happened");
        }

    }

    public void preAddDeviceConfig(){
        addDeviceConfig = new DeviceConfigDTO();
        addDeviceConfig.setStateId(Long.valueOf(Const.STATUS.ACTIVE));
        productOfferingTotalAddDTO = new ProductOfferingTotalDTO();
        listAddDeviceConfigDetail = new ArrayList<>();
        DeviceConfigDetailDTO deviceConfigDetailDTO = new DeviceConfigDetailDTO();
        deviceConfigDetailDTO.setProductOfferingTotalDTO(new ProductOfferingTotalDTO());
        deviceConfigDetailDTO.setStatus(Const.STATUS_ACTIVE);
        listAddDeviceConfigDetail.add(deviceConfigDetailDTO);
    }

    @Secured("@")
    public String getStrStatus(String str) {
        if (!DataUtil.isNullObject(str) && str.equals(Const.STATUS_ACTIVE)) {
            return getText("common.type.status.active");
        }
        return getText("common.type.status.inactive");
    }

    private void addNewItem() throws LogicException, Exception {
        DeviceConfigDetailDTO dto = new DeviceConfigDetailDTO();
        dto.setStatus(Const.STATUS_ACTIVE);
        listAddDeviceConfigDetail.add(dto);
    }

    public void doAddItem() {
        try {
            if (listAddDeviceConfigDetail != null && listAddDeviceConfigDetail.size() == 100) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "deviceConfig.validate.limit.detail");
            }
            addNewItem();
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
    public void doFileUploadAction(FileUploadEvent event) {

        try {

            uploadedFile = event.getFile();
            listErrFile = Lists.newArrayList();
            lstDeviceConfigImport = new HashMap<>();
            lstDeviceDetailConfigImport = new HashMap<>();
            showDownResult = false;
            resultImportFile = null;
            fileResultImport = null;
            attachFileName = "";
            previewFile = new ArrayList<>();
            previewFileClone = new ArrayList<>();
            showPreview = false;
            disableConfigDevice = true;

            BaseMessage message = validateFileUploadWhiteList(uploadedFile, ALOW_EXTENSION_EXCEL_TYPE_LIST, MAX_SIZE_5M);
            if (!message.isSuccess()) {
                clearUpload();
                LogicException ex = new LogicException(message.getErrorCode(), message.getKeyMsg());
                ex.setDescription(message.getDescription());
                throw ex;
            }
            selectedFile = true;
            byteContent = uploadedFile.getContents();
            attachFileName = event.getFile().getFileName();

            doValidRequestImport();
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
    private void doValidRequestImport() {
        try {
            BaseMessage message = validateFileUploadWhiteList(uploadedFile, ALOW_EXTENSION_EXCEL_TYPE_LIST, MAX_SIZE_5M);
            if (!message.isSuccess()) {
                clearUpload();
                LogicException ex = new LogicException(message.getErrorCode(), message.getKeyMsg());
                ex.setDescription(message.getDescription());
                throw ex;
            }
            //validate so dong
            processingFile = new ExcellUtil(uploadedFile, byteContent);
            Sheet sheetProcess = processingFile.getSheetAt(0);
            int totalRow = processingFile.getTotalRowAtSheet(sheetProcess);
            if (totalRow > 101) {
                reportErrorValidateFail("", "", "deviceConfig.validate.limit.detail");
                return;
            }
            //validate so cot
            Row test = sheetProcess.getRow(3);
            if (test == null || processingFile.getTotalColumnAtRow(test) != 4) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "list.product.validate.fileError");
            }
            //validate doc file
            validateFileUpload(processingFile);

            List<String> collect = listErrFile.stream().filter(s -> !DataUtil.isNullOrEmpty(s)  && !getText("create.request.divide.device.valid").equalsIgnoreCase(s)).collect(Collectors.toList());
            if(DataUtil.isNullOrEmpty(collect) && !DataUtil.isNullOrEmpty(lstDeviceConfigImport)){
                disableConfigDevice = false;
            } else {
                showDownResult = true;
                Workbook workbook = WorkbookFactory.create(uploadedFile.getInputstream());
                Sheet sheet = workbook.getSheetAt(0);
                CellStyle cs = workbook.createCellStyle();
                cs.setBorderRight(CellStyle.BORDER_THIN);
                cs.setRightBorderColor(IndexedColors.BLACK.getIndex());
                cs.setBorderBottom(CellStyle.BORDER_THIN);
                cs.setBottomBorderColor(IndexedColors.BLACK.getIndex());
                cs.setBorderLeft(CellStyle.BORDER_THIN);
                cs.setLeftBorderColor(IndexedColors.BLACK.getIndex());
                cs.setBorderTop(CellStyle.BORDER_THIN);
                cs.setTopBorderColor(IndexedColors.BLACK.getIndex());
                cs.setWrapText(true);

                Font font = workbook.createFont();
                font.setBoldweight(Font.BOLDWEIGHT_BOLD);
                CellStyle csHeader = workbook.createCellStyle();
                csHeader.setBorderRight(CellStyle.BORDER_THIN);
                csHeader.setRightBorderColor(IndexedColors.BLACK.getIndex());
                csHeader.setBorderBottom(CellStyle.BORDER_THIN);
                csHeader.setBottomBorderColor(IndexedColors.BLACK.getIndex());
                csHeader.setBorderLeft(CellStyle.BORDER_THIN);
                csHeader.setLeftBorderColor(IndexedColors.BLACK.getIndex());
                csHeader.setBorderTop(CellStyle.BORDER_THIN);
                csHeader.setTopBorderColor(IndexedColors.BLACK.getIndex());
                csHeader.setFont(font);
                csHeader.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
                csHeader.setFillPattern(CellStyle.SOLID_FOREGROUND);
                Row rowHeader = sheet.getRow(3);
                Cell cell4 = rowHeader.createCell(4);
                cell4.setCellValue(getText("create.request.divide.device.result"));
                cell4.setCellStyle(csHeader);
                rowHeader.getCell(3).setCellStyle(csHeader);
                rowHeader.getCell(2).setCellStyle(csHeader);
                rowHeader.getCell(1).setCellStyle(csHeader);
                rowHeader.getCell(0).setCellStyle(csHeader);
                int msgErr = 0;
                for (int row = 4; row <= sheet.getLastRowNum(); row++) {
                    Row xRow = sheet.getRow(row);
                    if(xRow == null)
                        continue;
                    if(listErrFile.size() <= msgErr)
                        break;
                    Cell cell = xRow.createCell(4);
                    cell.setCellValue(listErrFile.get(msgErr++));
                    cell.setCellStyle(cs);
                    Cell cell3 = xRow.getCell(3);
                    if(cell3 != null)
                        cell3.setCellStyle(cs);
                    Cell cell2 = xRow.getCell(2);
                    if(cell2 != null)
                        cell2.setCellStyle(cs);
                    Cell cell1 = xRow.getCell(1);
                    if(cell1 != null)
                        cell1.setCellStyle(cs);
                    Cell cell0 = xRow.getCell(0);
                    if(cell0 != null)
                        cell0.setCellStyle(cs);
                }

                String fileName = uploadedFile.getFileName();
                suffix = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
                fileResultImport = File.createTempFile("resultImport", suffix);
                FileOutputStream fileOut = new FileOutputStream(fileResultImport);
                workbook.write(fileOut);
                fileOut.flush();
                fileOut.close();
                reportError("", "create.request.divide.device.importErr");
            }
        } catch (LogicException ex) {
            reportError("", ex);
            topPage();
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
            logger.error(ex.getMessage(), ex);
            topPage();
        }
    }

    public void doImportConfigDevice(){
        List<DeviceConfigDTO> lstDeviceConfigs = new ArrayList<>();

        for(String key : lstDeviceConfigImport.keySet()){
            DeviceConfigDTO deviceConfigDTO = lstDeviceConfigImport.get(key);
            deviceConfigDTO.setListAddDeviceConfigDetail(lstDeviceDetailConfigImport.get(key));
            lstDeviceConfigs.add(deviceConfigDTO);
        }

        try {
            BaseMessage baseMessage = deviceConfigService.createDeviceConfigImport(currentStaff.getStaffCode(), lstDeviceConfigs);
            if(baseMessage.isSuccess()) {
                reportSuccess("", "deviceConfig.import.success");
                searchDeviceConfig();
                doResetRequestImport();
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, null, getTextParam(baseMessage.getKeyMsg(), baseMessage.getParamsMsg())));
                FacesContext.getCurrentInstance().validationFailed();
            }
            topPage();
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("frmAddDeviceConfig:mesageAdd", ex.getErrorCode(), ex.getKeyMsg(), ex.getParamsMsg());
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("frmAddDeviceConfig:mesageAdd", "", "common.error.happened");
            topPage();
        }
    }

    private void validateFileUpload(ExcellUtil processingFile) {
        try {

            Sheet sheetProcess = processingFile.getSheetAt(0);
            int totalRow = processingFile.getTotalRowAtSheet(sheetProcess);
            Row rowTemp = sheetProcess.getRow(3);
            String sttHeader = processingFile.getStringValue(rowTemp.getCell(0)).trim();
            String productHeader = processingFile.getStringValue(rowTemp.getCell(1)).trim();
            String statusProductHeader = processingFile.getStringValue(rowTemp.getCell(2)).trim();
            String itemCodeHeader = processingFile.getStringValue(rowTemp.getCell(3)).trim();
            if(!sttHeader.equalsIgnoreCase(getText("create.request.divide.device.stt")) || !productHeader.equalsIgnoreCase(getText("create.request.divide.device.product"))
                    || !statusProductHeader.equalsIgnoreCase(getText("create.request.divide.device.status")) || !itemCodeHeader.equalsIgnoreCase(getText("deviceConfig.item.code"))){
                reportErrorValidateFail("", "", "list.product.validate.fileError");
                return;
            }
            for (int i = 4; i < totalRow; i++) {
                Row row = sheetProcess.getRow(i);
                if (row == null) {
                    continue;
                }

                String productCode = processingFile.getStringValue(row.getCell(1)).trim().toUpperCase();
                String status = processingFile.getStringValue(row.getCell(2)).trim();
                String itemCode = processingFile.getStringValue(row.getCell(3)).trim();


                if(DataUtil.isNullOrEmpty(productCode) && DataUtil.isNullOrEmpty(status) && DataUtil.isNullOrEmpty(itemCode)){
                    listErrFile.add("");
                    continue;
                }

                DivideDevicePreview devicePreview = new DivideDevicePreview();
                devicePreview.setProductCode(productCode);
                try {
                    devicePreview.setStatus(lsProductStatus.stream().filter(s -> s.getValue().equals(status)).collect(Collectors.toList()).get(0).getName());
                } catch (Exception e){
                    logger.error(e.getMessage(), e);
                    devicePreview.setStatus(status);
                }
                devicePreview.setSerial(itemCode);
                previewFile.add(devicePreview);

                if(DataUtil.isNullOrEmpty(productCode)){
                    listErrFile.add(getText("create.request.divide.device.productNull"));
                    continue;
                }

                if(DataUtil.isNullOrEmpty(status)){
                    listErrFile.add(getText("create.request.divide.device.statusNull"));
                    continue;
                }


                if(DataUtil.isNullOrEmpty(itemCode)){
                    listErrFile.add(getText("deviceConfig.item.code.empty"));
                    continue;
                } else if(!itemCode.toUpperCase().startsWith("BH")){
                    listErrFile.add(getText("deviceConfig.item.code.format"));
                    continue;
                }


                if(lsProductStatus.stream().noneMatch(s -> s.getValue().equals(status))){
                    listErrFile.add(getText("create.request.divide.device.status.inValid"));
                    continue;
                }

                ProductOfferingDTO productOfferingDTO = orderDivideDeviceService.getProductByCodeAndProbType(productCode, Const.DEVICE_CONFIG.PROB_OFFER_TYPE_ID);
                //List<ProductOfferingTotalDTO> lsProductOfferingDTO = orderDivideDeviceService.getLsProductOfferingDTO(productCode, staffDTO.getShopId(), Const.OWNER_TYPE.SHOP_LONG.toString(), status);
                /*if(DataUtil.isNullOrEmpty(offeringTotalDTOList) || !offeringTotalDTOList.get(0).getCode().equalsIgnoreCase(productCode)){
                    listErrFile.add(getTextParam("deviceConfig.product.notFound", productCode));
                    continue;
                }*/
                if(productOfferingDTO == null){
                    listErrFile.add(getTextParam("deviceConfig.product.notFound", productCode));
                    continue;
                }

                /*List<ProductOfferingTotalDTO> offeringTotalItemDTOList = deviceConfigService.getLsProductOfferingByProductTypeAndState(itemCode, 11L, null);
                if(DataUtil.isNullOrEmpty(offeringTotalItemDTOList) || !offeringTotalItemDTOList.get(0).getCode().equalsIgnoreCase(itemCode)){
                    listErrFile.add(getTextParam("deviceConfig.item.notFound", itemCode));
                    continue;
                }*/
                ProductOfferingDTO itemOfferingDTO = orderDivideDeviceService.getProductByCodeAndProbType(itemCode, Const.DEVICE_CONFIG.ITEM_OFFER_TYPE_ID);
                if(itemOfferingDTO == null){
                    listErrFile.add(getTextParam("deviceConfig.item.notFound", itemCode));
                    continue;
                }
                Long productOfferingId = itemOfferingDTO.getProductOfferingId();

                BaseMessage baseMessage = deviceConfigService.checkProductIsConfigWithState(productOfferingDTO.getProductOfferingId(), Long.valueOf(status));

                //BaseMessage baseMessage = deviceConfigService.checkProductIdConfigIsConfig(offeringTotalDTOList.get(0).getProductOfferingId(), Long.valueOf(status), productOfferingId);
                if(baseMessage.isSuccess()){
                    ProductOfferingDTO product = productOfferingService.findOne(productOfferingDTO.getProductOfferingId());
                    //ProductOfferingDTO item = productOfferingService.findOne(productOfferingId);
                    String msg = getTextParam("deviceConfig.config.duplicate.product", product.getName(), deviceConfigService.getStateStr(Long.valueOf(status)));
                    listErrFile.add(msg);
                    continue;
                }

                DeviceConfigDTO deviceConfigDTO = lstDeviceConfigImport.get(productCode + ":" + status);
                if(deviceConfigDTO == null){
                    deviceConfigDTO = new DeviceConfigDTO();
                    deviceConfigDTO.setProdOfferId(productOfferingDTO.getProductOfferingId());
                    deviceConfigDTO.setStateId(Long.valueOf(status));
                    lstDeviceConfigImport.put(productCode + ":" + status, deviceConfigDTO);


                    List<DeviceConfigDetailDTO> deviceConfigDetailDTOS = new ArrayList<>();
                    DeviceConfigDetailDTO deviceConfigDetailDTO = new DeviceConfigDetailDTO();
                    deviceConfigDetailDTO.setStatus(Const.STATUS_ACTIVE);
                    ProductOfferingTotalDTO productOfferingTotalDTO = new ProductOfferingTotalDTO();
                    productOfferingTotalDTO.setProductOfferingId(productOfferingId);
                    deviceConfigDetailDTO.setProductOfferingTotalDTO(productOfferingTotalDTO);
                    deviceConfigDetailDTOS.add(deviceConfigDetailDTO);
                    lstDeviceDetailConfigImport.put(productCode + ":" + status, deviceConfigDetailDTOS);
                } else {
                    List<DeviceConfigDetailDTO> deviceConfigDetailDTOS = lstDeviceDetailConfigImport.get(productCode + ":" + status);
                    if(deviceConfigDetailDTOS.stream().anyMatch(s -> s.getProductOfferingTotalDTO().getProductOfferingId().toString().equals(productOfferingId.toString()))){
                        listErrFile.add(getTextParam("deviceConfig.item.code.duplicate", itemCode));
                        continue;
                    }

                    DeviceConfigDetailDTO deviceConfigDetailDTO = new DeviceConfigDetailDTO();
                    deviceConfigDetailDTO.setStatus(Const.STATUS_ACTIVE);
                    ProductOfferingTotalDTO productOfferingTotalDTO = new ProductOfferingTotalDTO();
                    productOfferingTotalDTO.setProductOfferingId(productOfferingId);
                    deviceConfigDetailDTO.setProductOfferingTotalDTO(productOfferingTotalDTO);
                    deviceConfigDetailDTOS.add(deviceConfigDetailDTO);
                }

                listErrFile.add(getText("create.request.divide.device.valid"));
            }

            if(!DataUtil.isNullOrEmpty(previewFile))
                showPreview = true;

        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
        }
    }

    private void clearUpload() {
        uploadedFile = null;
        selectedFile = false;
        attachFileName = "";
        byteContent = null;
        showDownResult = false;
    }

    @Secured("@")
    public List<ProductOfferingTotalDTO> doChangeOfferingNew(String inputProduct) {
        try {
            String input = inputProduct != null ? inputProduct.trim() : "";
            lsProductOfferingTotalNewDTO = DataUtil.defaultIfNull(deviceConfigService.getLsProductOfferingByProductTypeAndState(input, 7L, null), new ArrayList<>());
            return lsProductOfferingTotalNewDTO;
        } catch (Exception e) {
            reportError("", "common.error.happened", e);
            logger.error(e.getMessage(), e);
            topPage();
        }
        return Lists.newArrayList();
    }

    @Secured("@")
    public List<ProductOfferingTotalDTO> doChangeOfferingAdd(String inputProduct) {
        try {
            String input = inputProduct != null ? inputProduct.trim() : "";
            lsProductOfferingTotalAddDTO = DataUtil.defaultIfNull(deviceConfigService.getLsProductOfferingByProductTypeAndState(input, Const.DEVICE_CONFIG.PROB_OFFER_TYPE_ID, null), new ArrayList<>());
            return lsProductOfferingTotalAddDTO;
        } catch (Exception e) {
            reportError("", "common.error.happened", e);
            logger.error(e.getMessage(), e);
            topPage();
        }
        return Lists.newArrayList();
    }

    @Secured("@")
    public List<ProductOfferingTotalDTO> doChangeOfferingAddDetail(String inputProduct) {
        try {
            String input = inputProduct != null ? inputProduct.trim() : "";
            lsProductOfferingTotalAddDetailDTO = DataUtil.defaultIfNull(deviceConfigService.getLsProductOfferingByProductTypeAndState(input, 11L, null), new ArrayList<>());
            return lsProductOfferingTotalAddDetailDTO;
        } catch (Exception e) {
            reportError("", "common.error.happened", e);
            logger.error(e.getMessage(), e);
            topPage();
        }
        return Lists.newArrayList();
    }


    @Secured("@")
    public void searchDeviceConfig() {
        try {
            lstDeviceConfig = deviceConfigService.searchDeviceByProductOfferIdAndStateId(productOfferingTotalNewDTO == null ? null : productOfferingTotalNewDTO.getProductOfferingId(), inputSearch.getStateId());
            topPage();
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex.getErrorCode(), ex.getDescription());
            topPage();
        } catch (Exception ex) {
            logger.error(ex);
            reportError("", "", "common.error.happened");
            topPage();
        }

    }

    @Secured("@")
    public void receiveProduct() {

    }

    @Secured("@")
    public void receiveProductAdd(SelectEvent event) {
        String s = ((ProductOfferingTotalDTO)event.getObject()).getProductOfferingId() + "";
        if (lsProductOfferingTotalAddDTO != null)
            for (ProductOfferingTotalDTO c : lsProductOfferingTotalAddDTO) {
                if (c != null && c.getProductOfferingId() != null && DataUtil.safeToString(c.getProductOfferingId()).equals(s)) {
                    productOfferingTotalAddDTO = c;
                    break;
                }
            }
        if (productOfferingTotalAddDTO == null) {
            productOfferingTotalAddDTO = new ProductOfferingTotalDTO();
            productOfferingTotalAddDTO.setProductOfferingId(DataUtil.safeToLong(s));
        }
    }

    public String getDlgHeader(){
        if(isPreview)
            return getText("deviceConfig.preview");

        if(!isCreate)
            return getText("deviceConfig.update");
        return getText("deviceConfig.add");
    }

    public void doConfirmAdd(){
        boolean hasErr = false;
        if(productOfferingTotalAddDTO == null || productOfferingTotalAddDTO.getProductOfferingId() == null) {
            hasErr = true;
            reportError("", "deviceConfig.require.product");
            FacesContext.getCurrentInstance().validationFailed();
        }
        if(DataUtil.isNullOrEmpty(listAddDeviceConfigDetail) || listAddDeviceConfigDetail.stream().noneMatch(s -> s.getProductOfferingTotalDTO().getProductOfferingId() != null)){
            hasErr = true;
            reportError("", "deviceConfig.require.item");
            FacesContext.getCurrentInstance().validationFailed();
        }
        if(!hasErr) {
            addDeviceConfig.setProductOfferName(productOfferingTotalAddDTO.getName());
            addDeviceConfig.setProdOfferId(productOfferingTotalAddDTO.getProductOfferingId());
            try {
                BaseMessage base = isCreate ? deviceConfigService.checkProductIsConfigWithState(addDeviceConfig.getProdOfferId(), addDeviceConfig.getStateId()) : new BaseMessage(false);
                if (base.isSuccess()) {
                    hasErr = true;
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, null, getTextParam("deviceConfig.config.duplicate.product", productOfferingTotalAddDTO.getName(), deviceConfigService.getStateStr(addDeviceConfig.getStateId()))));
                    FacesContext.getCurrentInstance().validationFailed();
                }
                /*BaseMessage baseMessage = deviceConfigService.checkProductIdConfig(addDeviceConfig, listAddDeviceConfigDetail);
                if(!baseMessage.isSuccess()){
                    hasErr = true;
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, null, getTextParam(baseMessage.getKeyMsg(), baseMessage.getParamsMsg())));
                    FacesContext.getCurrentInstance().validationFailed();
                }*/
            } catch (Exception e) {
                hasErr = true;
                logger.error(e.getMessage(), e);
                reportError("", "", "common.error.happened");
            }
        }
        if(hasErr)
            topPage();

    }

    private boolean checkDuplicateItem(String profferingId){
        for(DeviceConfigDetailDTO deviceConfigDetailDTO : listAddDeviceConfigDetail){
            if(deviceConfigDetailDTO.getProductOfferingTotalDTO() == null || deviceConfigDetailDTO.getProductOfferingTotalDTO().getProductOfferingId() == null)
                continue;
            if(deviceConfigDetailDTO.getProductOfferingTotalDTO().getProductOfferTypeId() != null &&
                    deviceConfigDetailDTO.getProductOfferingTotalDTO().getProductOfferingId().toString().equals(profferingId)){
                reportError("", "deviceConfig.duplicateItem");
                return false;
            }
        }
        return true;
    }

    @Secured("@")
    public void receiveProductAddDetail(SelectEvent event) {
        ProductOfferingTotalDTO object = (ProductOfferingTotalDTO) event.getObject();
        String s = object.getProductOfferingId() + "";
        if(!checkDuplicateItem(s))
            return;

        int index = (Integer) UIComponent.getCurrentComponent(FacesContext.getCurrentInstance()).getAttributes().get("detailIndex");
        DeviceConfigDetailDTO deviceConfigDetailDTO = listAddDeviceConfigDetail.get(index);

        /*try {
            if(deviceConfigService.checkProductIdConfigIsConfig(productOfferingTotalAddDTO.getProductOfferingId(), addDeviceConfig.getStateId(), Long.valueOf(s)).isSuccess()){
                ProductOfferingDTO one = productOfferingService.findOne(Long.valueOf(s));
                String msg = getTextParam("deviceConfig.msg.configDuplicate", productOfferingTotalAddDTO.getName(), deviceConfigService.getStateStr(addDeviceConfig.getStateId()), one.getName());
                FacesContext.getCurrentInstance().addMessage("frmAddDeviceConfig:mesageAdd", new FacesMessage(FacesMessage.SEVERITY_ERROR, null, msg));
                topPage();
                return;
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            reportError("frmAddDeviceConfig:mesageAdd", "", "common.error.happened");
            topPage();
        }*/
        deviceConfigDetailDTO.setProductOfferingTotalDTO(new ProductOfferingTotalDTO());
        if (lsProductOfferingTotalAddDTO != null)
            for (ProductOfferingTotalDTO c : lsProductOfferingTotalAddDetailDTO) {
                if (c != null && c.getProductOfferingId() != null && DataUtil.safeToString(c.getProductOfferingId()).equals(s)) {
                    try {
                        BeanUtils.copyProperties(deviceConfigDetailDTO.getProductOfferingTotalDTO(), c);
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                        break;
                    }
                    break;
                }
            }
        if (deviceConfigDetailDTO.getProductOfferingTotalDTO() == null) {
            deviceConfigDetailDTO.setProductOfferingTotalDTO(new ProductOfferingTotalDTO());
            deviceConfigDetailDTO.getProductOfferingTotalDTO().setProductOfferingId(DataUtil.safeToLong(s));
        }
    }

    @Secured("@")
    public void deleteSelection(){
        delete(lstDeviceConfigSelection);
    }

    @Secured("@")
    public void actionSetIndexDelete(int index){
        indexDelete = index;
    }

    @Secured("@")
    public void deleteOne(){
        DeviceConfigDTO deviceConfigDTO = lstDeviceConfig.get(indexDelete);
        delete(Arrays.asList(deviceConfigDTO));
    }

    @Secured("@")
    public void delete(List<DeviceConfigDTO> listDelete) {
        try {
            if (DataUtil.isNullObject(listDelete)) {
                reportError("", "", "common.error.noselect.record");
            } else {

                deviceConfigService.delete(listDelete, BccsLoginSuccessHandler.getStaffDTO().getStaffCode());
                reportSuccess("frmConfigDivideDevice:msgConfigDivideDevice", "common.msg.success.delete");
                searchDeviceConfig();
            }
            topPage();
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex.getErrorCode(), ex.getDescription());
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "", ex);
            topPage();
        }
    }

    @Secured("@")
    public void resetProduct() {
        productOfferingTotalNewDTO = new ProductOfferingTotalDTO();
    }

    @Secured("@")
    public void resetProductAdd() {
        productOfferingTotalAddDTO = new ProductOfferingTotalDTO();
    }

    @Secured("@")
    public void doRemoveItem(int index){
        DeviceConfigDetailDTO deviceConfigDetailDTO = listAddDeviceConfigDetail.get(index);
        if(deviceConfigDetailDTO.getDeviceConfigDTO() != null) {
            deviceConfigDetailDTO.setStatus(Const.STATUS_DELETE);
            deviceConfigDetailDTO.getDeviceConfigDTO().setStatus(Const.STATUS_DELETE);
        }
        else listAddDeviceConfigDetail.remove(index);
    }


    @Secured("@")
    public void preToUpdate(int index){
        try {
            listAddDeviceConfigDetail = new ArrayList<>();
            addDeviceConfig = new DeviceConfigDTO();
            DeviceConfigDTO deviceConfigDTO = lstDeviceConfig.get(index);
            List<ProductOfferingTotalDTO> temp = deviceConfigService.getLsProductOfferingByProductTypeAndState(deviceConfigDTO.getProductOfferCode(), Const.DEVICE_CONFIG.PROB_OFFER_TYPE_ID, null);

            if(DataUtil.isNullOrEmpty(temp)){
                throw new LogicException("", "stock.product.offering.not.exist", deviceConfigDTO.getProductOfferCode());
            }

            productOfferingTotalAddDTO = temp.get(0);
            addDeviceConfig.setProdOfferId(deviceConfigDTO.getProdOfferId());
            addDeviceConfig.setStateId(deviceConfigDTO.getStateId());
            lstDeviceConfigUpdate = DataUtil.defaultIfNull(deviceConfigService.getDeviceConfigByProductAndState(addDeviceConfig.getProdOfferId(), addDeviceConfig.getStateId()), new ArrayList<>());

            for (DeviceConfigDTO deviceConfigDTO1 : lstDeviceConfigUpdate) {
                DeviceConfigDetailDTO deviceConfigDetailDTO = new DeviceConfigDetailDTO();
                deviceConfigDetailDTO.setDeviceConfigDTO(new DeviceConfigDTO());

                BeanUtils.copyProperties(deviceConfigDetailDTO.getDeviceConfigDTO(), deviceConfigDTO1);
                deviceConfigDetailDTO.setStatus(Const.STATUS.ACTIVE);
                try {
                    ProductOfferingDTO productOfferingDTO = productOfferingService.findOne(deviceConfigDTO1.getNewProdOfferId());
                    deviceConfigDetailDTO.setProductOfferingTotalDTO(deviceConfigService.getLsProductOfferingByProductTypeAndState(productOfferingDTO.getCode(), Const.DEVICE_CONFIG.ITEM_OFFER_TYPE_ID, null).get(0));
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    throw new LogicException("105", "balance.valid.prodInfo");
                }
                listAddDeviceConfigDetail.add(deviceConfigDetailDTO);
            }
        } catch (LogicException ex){
            logger.error(ex.getMessage(), ex);
            reportError("frmAddDeviceConfig:mesageAdd", ex.getErrorCode(), ex.getDescription());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            reportError("frmAddDeviceConfig:mesageAdd", "", "common.error.happened");
            return;
        }
    }

    public String getCodeAndNameProduct(ProductOfferingTotalDTO productOfferingTotalDTO){
        return productOfferingTotalDTO.getCode() + " - " + productOfferingTotalDTO.getName();
    }

    @Secured("@")
    public void updateConfigDevice(){
        try {
            BaseMessage result = deviceConfigService.updateDeviceConfig(currentStaff.getStaffCode(), addDeviceConfig, listAddDeviceConfigDetail);
            if (result.isSuccess())
                reportSuccess("frmConfigDivideDevice:msgConfigDivideDevice", "mn.invoice.invoiceType.success.edit");
            else {
                FacesContext.getCurrentInstance().addMessage("frmAddDeviceConfig:mesageAdd", new FacesMessage(FacesMessage.SEVERITY_ERROR, null, getTextParam(result.getKeyMsg(), result.getParamsMsg())));
                FacesContext.getCurrentInstance().validationFailed();
            }
            topPage();
            searchDeviceConfig();
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex.getErrorCode(), ex.getDescription());
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex.getMessage(), ex);
            topPage();
        }
    }

    @Secured("@")
    public void addConfigDevice(boolean isClose){
        try {
            BaseMessage baseMessage = deviceConfigService.createDeviceConfig(currentStaff.getStaffCode(), addDeviceConfig, listAddDeviceConfigDetail);
            if(baseMessage.isSuccess()) {
                if (isClose) {
                    reportSuccess("frmConfigDivideDevice:msgConfigDivideDevice", "mn.invoice.invoiceType.success.add");
                } else {
                    reportSuccess("frmAddDeviceConfig:mesageAdd", "mn.invoice.invoiceType.success.add");
                }
                preAddDeviceConfig();
                searchDeviceConfig();
            } else {
                FacesContext.getCurrentInstance().addMessage("frmAddDeviceConfig:mesageAdd", new FacesMessage(FacesMessage.SEVERITY_ERROR, null, getTextParam(baseMessage.getKeyMsg(), baseMessage.getParamsMsg())));
                FacesContext.getCurrentInstance().validationFailed();
            }
            topPage();
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("frmAddDeviceConfig:mesageAdd", ex.getErrorCode(), ex.getKeyMsg(), ex.getParamsMsg());
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("frmAddDeviceConfig:mesageAdd", "", "common.error.happened");
            topPage();
        }
    }

    @Secured("@")
    public void resetProductAddDetail(int index) {
        listAddDeviceConfigDetail.get(index).setProductOfferingTotalDTO(new ProductOfferingTotalDTO());
    }

    @Secured("@")
    public void resetForm() {
        productOfferingTotalNewDTO = new ProductOfferingTotalDTO();
        inputSearch = new DeviceConfigDTO();
        topPage();
    }

/*    @Secured("@")
    public void receiveProductDetail(int index) {
        listAddDeviceConfigDetail.get(0).setProductOfferingTotalDTO(new ProductOfferingTotalDTO());
    }*/

    public void preToDelete(){
        if(DataUtil.isNullOrEmpty(lstDeviceConfigSelection)){
            reportError("", "deviceConfig.msg.delete.empty");
            FacesContext.getCurrentInstance().validationFailed();
        }
    }

    public boolean isShowDownResult() {
        return showDownResult;
    }

    public StreamedContent prepareDownloadFileResult() {
        InputStream stream;
        try {
            stream = new FileInputStream(fileResultImport);
            return new DefaultStreamedContent(stream, "application/excel", "resultImportFile." + suffix);
        } catch (FileNotFoundException e) {
            logger.error(e);
            reportError("", "", "common.error.happened");
            topPage();
        }
        return null;
    }

    @Secured("@")
    public void downloadFileTemplate() {
        try {
            InputStream createStream = Faces.getResourceAsStream(Const.BRAS_IPPOOL.RESOURCE_TEMPLATE_PATH + Const.REPORT_TEMPLATE.IMPORT_DEVICE_TEMPLATE);
            HSSFWorkbook workbook = new HSSFWorkbook(createStream);
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"" + "IMPORT_DEVICE_TEMPLATE.xls" + "\"");
            externalContext.setResponseContentType("application/vnd.ms-excel");
            workbook.write(externalContext.getResponseOutputStream());
            facesContext.responseComplete();
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
        }
    }

    @Secured("@")
    public void onTabChange(TabChangeEvent event) {
        TabView tabView = (TabView) event.getComponent();
        tabIndex = tabView.getChildren().indexOf(event.getTab());
        if(tabIndex == 0){

        } else if(tabIndex == 1){
            attachFileName = "";
            showDownResult = false;
            lstDeviceConfigImport = new HashMap<>();
            lstDeviceDetailConfigImport = new HashMap<>();
            previewFile = new ArrayList<>();
            previewFileClone = new ArrayList<>();
            showPreview = false;
            disableConfigDevice = true;
        }

    }

    @Secured("@")
    public void doResetRequestImport(){
        listErrFile = new ArrayList<>();
        lstDeviceConfigImport = new HashMap<>();
        lstDeviceDetailConfigImport = new HashMap<>();
        showDownResult = false;
        resultImportFile = null;
        fileResultImport = null;
        attachFileName = "";
        previewFile = new ArrayList<>();
        previewFileClone = new ArrayList<>();
        showPreview = false;
        disableConfigDevice = true;
        topPage();
    }

    public String getNameAndCodeDeviceConfig(DeviceConfigDTO deviceConfigDTO){
        return deviceConfigDTO.getProductOfferCode() + " - " + deviceConfigDTO.getProductOfferName();
    }

    public void doShowPreview(){
        previewFileClone = Lists.newArrayList(previewFile);
    }

    public void setShowDownResult(boolean showDownResult) {
        this.showDownResult = showDownResult;
    }

    public ProductOfferingTotalDTO getProductOfferingTotalAddDTO() {
        return productOfferingTotalAddDTO;
    }

    public void setProductOfferingTotalAddDTO(ProductOfferingTotalDTO productOfferingTotalAddDTO) {
        this.productOfferingTotalAddDTO = productOfferingTotalAddDTO;
    }

    public List<ProductOfferingTotalDTO> getLsProductOfferingTotalAddDTO() {
        return lsProductOfferingTotalAddDTO;
    }

    public void setLsProductOfferingTotalAddDTO(List<ProductOfferingTotalDTO> lsProductOfferingTotalAddDTO) {
        this.lsProductOfferingTotalAddDTO = lsProductOfferingTotalAddDTO;
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

    public List<OptionSetValueDTO> getLsProductStatus() {
        return lsProductStatus;
    }

    public void setLsProductStatus(List<OptionSetValueDTO> lsProductStatus) {
        this.lsProductStatus = lsProductStatus;
    }

    public DeviceConfigDTO getInputSearch() {
        return inputSearch;
    }

    public void setInputSearch(DeviceConfigDTO inputSearch) {
        this.inputSearch = inputSearch;
    }

    public List<DeviceConfigDTO> getLstDeviceConfig() {
        return lstDeviceConfig;
    }

    public long getRowKey(DeviceConfigDTO deviceConfigDTO){
        if(deviceConfigDTO == null)
            return Integer.MAX_VALUE;
        return deviceConfigDTO.getProdOfferId().hashCode() + deviceConfigDTO.getStateId().hashCode() * 31;
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

    public void setLstDeviceConfig(List<DeviceConfigDTO> lstDeviceConfig) {
        this.lstDeviceConfig = lstDeviceConfig;
    }

    public List<DeviceConfigDTO> getLstDeviceConfigSelection() {
        return lstDeviceConfigSelection;
    }

    public void setLstDeviceConfigSelection(List<DeviceConfigDTO> lstDeviceConfigSelection) {
        this.lstDeviceConfigSelection = lstDeviceConfigSelection;
    }

    public ShopInfoNameable getShopInfoExportTagAdd() {
        return shopInfoExportTagAdd;
    }

    public void setShopInfoExportTagAdd(ShopInfoNameable shopInfoExportTagAdd) {
        this.shopInfoExportTagAdd = shopInfoExportTagAdd;
    }

    public DeviceConfigDTO getAddDeviceConfig() {
        return addDeviceConfig;
    }

    public void setAddDeviceConfig(DeviceConfigDTO addDeviceConfig) {
        this.addDeviceConfig = addDeviceConfig;
    }

    public List<DeviceConfigDetailDTO> getListAddDeviceConfigDetail() {
        return listAddDeviceConfigDetail;
    }

    public void setListAddDeviceConfigDetail(List<DeviceConfigDetailDTO> listAddDeviceConfigDetail) {
        this.listAddDeviceConfigDetail = listAddDeviceConfigDetail;
    }

    public List<ProductOfferingTotalDTO> getLsProductOfferingTotalAddDetailDTO() {
        return lsProductOfferingTotalAddDetailDTO;
    }

    public void setLsProductOfferingTotalAddDetailDTO(List<ProductOfferingTotalDTO> lsProductOfferingTotalAddDetailDTO) {
        this.lsProductOfferingTotalAddDetailDTO = lsProductOfferingTotalAddDetailDTO;
    }

    public boolean isCreate() {
        return isCreate;
    }

    public void setCreate(boolean create) {
        isCreate = create;
        isPreview = false;
    }

    public boolean isPreview() {
        return isPreview;
    }

    public void setPreview(boolean preview) {
        isPreview = preview;
    }

    public int getTabIndex() {
        return tabIndex;
    }

    public void setTabIndex(int tabIndex) {
        this.tabIndex = tabIndex;
    }

    public boolean isShowPreview() {
        return showPreview;
    }

    public void setShowPreview(boolean showPreview) {
        this.showPreview = showPreview;
    }

    public List<DivideDevicePreview> getPreviewFile() {
        return previewFile;
    }

    public void setPreviewFile(List<DivideDevicePreview> previewFile) {
        this.previewFile = previewFile;
    }

    public boolean isDisableConfigDevice() {
        return disableConfigDevice;
    }

    public void setDisableConfigDevice(boolean disableConfigDevice) {
        this.disableConfigDevice = disableConfigDevice;
    }

    public List<DivideDevicePreview> getPreviewFileClone() {
        return previewFileClone;
    }

    public void setPreviewFileClone(List<DivideDevicePreview> previewFileClone) {
        this.previewFileClone = previewFileClone;
    }
}



