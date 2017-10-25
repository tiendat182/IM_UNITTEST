package com.viettel.bccs.inventory.dto;

import com.viettel.bccs.inventory.common.Const;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.dto.BaseDTO;

import java.util.List;

/**
 * @author luannt23 on 12/16/2015.
 */
public class ConfigListProductTagDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    private String modeSerial;
    private Long ownerId;
    private String ownerType;
    private String filePath = Const.BRAS_IPPOOL.RESOURCE_TEMPLATE_PATH;
    private String fileName = Const.REPORT_TEMPLATE.IMPORT_SERIAL_FILENAME;
    private String fileNameProd = Const.REPORT_TEMPLATE.IMPORT_SERIAL_BY_PROD_NAME;
    private String productOfferTable;//SINHHV: them lay loai hang hoa theo ten bang
    private boolean fillAllSerial = false;
    private boolean collaborator = false;
    private boolean agent = false;
    private boolean showTotalPrice = true;
    private boolean showSerialView = false;// khi mode nay bang true thi link chi tiet serial luon hien thi ra (case cua thao dung)
    private Long stateId;// them vao de lay stateId
    private Long channelTypeId;// kenh
    private Long branchId;// chi nhanh
    private Long type;
    private boolean validateAvaiableQuantity = true;
    private boolean showRetrive = false;
    private boolean retrieveExpense = false;
    private Long pricePolicyId;
    private boolean balanceStock;
    private Long balanceStockType;
    private List<OptionSetValueDTO> lsProductStatus;
    private boolean changeProduct;
    private String stockModelType;
    private boolean stockOrderAgent;
    private Long stockDemo;//xuat hang demo

    public Long getStockDemo() {
        return stockDemo;
    }

    public void setStockDemo(Long stockDemo) {
        this.stockDemo = stockDemo;
    }

    public boolean isStockOrderAgent() {
        return stockOrderAgent;
    }

    public void setStockOrderAgent(boolean stockOrderAgent) {
        this.stockOrderAgent = stockOrderAgent;
    }

    public boolean isChangeProduct() {
        return changeProduct;
    }

    public void setChangeProduct(boolean changeProduct) {
        this.changeProduct = changeProduct;
    }

    public ConfigListProductTagDTO() {
    }

    public ConfigListProductTagDTO(String modeSerial) {
        this.modeSerial = modeSerial;
    }

    public ConfigListProductTagDTO(String modeSerial, Long ownerId, String ownerType) {
        this.modeSerial = modeSerial;
        this.ownerId = ownerId;
        this.ownerType = ownerType;
    }

    public ConfigListProductTagDTO(Long type, String modeSerial, Long ownerId, String ownerType, String stockModelType) {
        this.type = type;
        this.modeSerial = modeSerial;
        this.ownerId = ownerId;
        this.ownerType = ownerType;
        this.stockModelType = stockModelType;
    }

    public ConfigListProductTagDTO(String modeSerial, String filePath, String fileName) {
        this.modeSerial = modeSerial;
        this.fileName = fileName;
    }

    public ConfigListProductTagDTO(String modeSerial, Long ownerId, String ownerType, Long stateId) {
        this.modeSerial = modeSerial;
        this.ownerId = ownerId;
        this.ownerType = ownerType;
        this.stateId = stateId;
    }


    public Long getBalanceStockType() {
        return balanceStockType;
    }

    public void setBalanceStockType(Long balanceStockType) {
        this.balanceStockType = balanceStockType;
    }

    public boolean isBalanceStock() {
        return balanceStock;
    }

    public void setBalanceStock(boolean balanceStock) {
        this.balanceStock = balanceStock;
    }

    public Long getPricePolicyId() {
        return pricePolicyId;
    }

    public void setPricePolicyId(Long pricePolicyId) {
        this.pricePolicyId = pricePolicyId;
    }

    public boolean isRetrieveExpense() {
        return retrieveExpense;
    }

    public void setRetrieveExpense(boolean retrieveExpense) {
        this.retrieveExpense = retrieveExpense;
    }

    public boolean isValidateAvaiableQuantity() {
        return validateAvaiableQuantity;
    }

    public void setValidateAvaiableQuantity(boolean validateAvaiableQuantity) {
        this.validateAvaiableQuantity = validateAvaiableQuantity;
    }

    public String getTemplateName() {
        return DataUtil.safeToString(filePath) + Const.REPORT_TEMPLATE.IMPORT_SERIAL_FORDER + DataUtil.safeToString(fileName);
    }

    public String getTemplateNameByProd() {
        return DataUtil.safeToString(filePath) + Const.REPORT_TEMPLATE.IMPORT_SERIAL_FORDER + DataUtil.safeToString(fileNameProd);
    }

    //getter and setter
    public String getModeSerial() {
        return modeSerial;
    }

    public void setModeSerial(String modeSerial) {
        this.modeSerial = modeSerial;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getProductOfferTable() {
        return productOfferTable;
    }

    public boolean isFillAllSerial() {
        return fillAllSerial;
    }

    public void setFillAllSerial(boolean fillAllSerial) {
        this.fillAllSerial = fillAllSerial;
    }

    public void setProductOfferTable(String productOfferTable) {
        this.productOfferTable = productOfferTable;
    }

    public boolean isCollaborator() {
        return collaborator;
    }

    public void setCollaborator(boolean collaborator) {
        this.collaborator = collaborator;
    }

    public boolean isShowTotalPrice() {
        return showTotalPrice;
    }

    public void setShowTotalPrice(boolean showTotalPrice) {
        this.showTotalPrice = showTotalPrice;
    }

    public boolean isShowSerialView() {
        return showSerialView;
    }

    public void setShowSerialView(boolean showSerialView) {
        this.showSerialView = showSerialView;
    }

    public Long getStateId() {
        return stateId;
    }

    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }

    public Long getChannelTypeId() {
        return channelTypeId;
    }

    public void setChannelTypeId(Long channelTypeId) {
        this.channelTypeId = channelTypeId;
    }

    public Long getBranchId() {
        return branchId;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public String getFileNameProd() {
        return fileNameProd;
    }

    public void setFileNameProd(String fileNameProd) {
        this.fileNameProd = fileNameProd;
    }

    public boolean isAgent() {
        return agent;
    }

    public void setAgent(boolean agent) {
        this.agent = agent;
    }

    public boolean isShowRetrive() {
        return showRetrive;
    }

    public void setShowRetrive(boolean showRetrive) {
        this.showRetrive = showRetrive;
    }

    public List<OptionSetValueDTO> getLsProductStatus() {
        return lsProductStatus;
    }

    public void setLsProductStatus(List<OptionSetValueDTO> lsProductStatus) {
        this.lsProductStatus = lsProductStatus;
    }

    public String getStockModelType() {
        return stockModelType;
    }

    public void setStockModelType(String stockModelType) {
        this.stockModelType = stockModelType;
    }
}
