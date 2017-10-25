package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class ImportPartnerRequestDTO extends BaseDTO implements Serializable {

    private Long approveStaffId;
    private String contractCode;
    private Date contractDate;
    private Date contractImportDate;
    private Date createDate;
    private Long createShopId;
    private Long createStaffId;
    private String currencyType;
    private Long currencyRate;
    private String deliveryLocation;
    private byte[] documentContent;
    private String documentName;
    private Date importDate;
    private Long importPartnerRequestId;
    private Long importStaffId;
    private String importStaffCode;
    private Date lastModify;
    private Long partnerId;
    private String partnerName;
    private String poCode;
    private Date poDate;
    private String reason;
    private String requestCode;
    private Date requestDate;
    private Long status;
    private Long toOwnerId;
    private Long toOwnerType;
    private Long unitPrice;
    private Date fromDate;
    private Date toDate;
    private boolean checkData = false;
    private String actionCode;
    private Long impType;
    private boolean signVoffice;
    private boolean syncERP;
    private StockTransVofficeDTO stockTransVofficeDTO;
    private List<ImportPartnerDetailDTO> listImportPartnerDetailDTOs;
    private List<String> listProfile;
    private String profile;
    private String toOwnerName;
    private Long regionStockId;
    private Double rate;
    private String codeKCS;
    private Date dateKCS;
    private String codePackage;
    private Date endDate;
    private Long contractStatus;
    private Long partnerContractId;
    private Long reasonId;
    private List<ProductOfferingDTO> lsProductOfferDto;
    private String note;
    private SignOfficeDTO signOfficeDTO;
    private boolean logistic = false;

    public Long getContractStatus() {
        return contractStatus;
    }

    public void setContractStatus(Long contractStatus) {
        this.contractStatus = contractStatus;
    }

    public String getCodeKCS() {
        return codeKCS;
    }

    public void setCodeKCS(String codeKCS) {
        this.codeKCS = codeKCS;
    }

    public Date getDateKCS() {
        return dateKCS;
    }

    public void setDateKCS(Date dateKCS) {
        this.dateKCS = dateKCS;
    }

    public String getCodePackage() {
        return codePackage;
    }

    public void setCodePackage(String codePackage) {
        this.codePackage = codePackage;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public Long getRegionStockId() {
        return regionStockId;
    }

    public void setRegionStockId(Long regionStockId) {
        this.regionStockId = regionStockId;
    }

    public String getKeySet() {
        return keySet;
    }

    public ImportPartnerRequestDTO() {
        checkData = false;
    }

    public Long getApproveStaffId() {
        return this.approveStaffId;
    }

    public void setApproveStaffId(Long approveStaffId) {
        this.approveStaffId = approveStaffId;
    }

    public String getContractCode() {
        return this.contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public Date getContractDate() {
        return this.contractDate;
    }

    public void setContractDate(Date contractDate) {
        this.contractDate = contractDate;
    }

    public Date getContractImportDate() {
        return this.contractImportDate;
    }

    public void setContractImportDate(Date contractImportDate) {
        this.contractImportDate = contractImportDate;
    }

    public Date getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Long getCreateShopId() {
        return this.createShopId;
    }

    public void setCreateShopId(Long createShopId) {
        this.createShopId = createShopId;
    }

    public Long getCreateStaffId() {
        return this.createStaffId;
    }

    public void setCreateStaffId(Long createStaffId) {
        this.createStaffId = createStaffId;
    }

    public String getCurrencyType() {
        return this.currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    public Long getCurrencyRate() {
        return currencyRate;
    }

    public void setCurrencyRate(Long currencyRate) {
        this.currencyRate = currencyRate;
    }

    public String getDeliveryLocation() {
        return this.deliveryLocation;
    }

    public void setDeliveryLocation(String deliveryLocation) {
        this.deliveryLocation = deliveryLocation;
    }

    public byte[] getDocumentContent() {
        return this.documentContent;
    }

    public void setDocumentContent(byte[] documentContent) {
        this.documentContent = documentContent;
    }

    public String getDocumentName() {
        return this.documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public Date getImportDate() {
        return this.importDate;
    }

    public void setImportDate(Date importDate) {
        this.importDate = importDate;
    }

    public Long getImportPartnerRequestId() {
        return this.importPartnerRequestId;
    }

    public void setImportPartnerRequestId(Long importPartnerRequestId) {
        this.importPartnerRequestId = importPartnerRequestId;
    }

    public Long getImportStaffId() {
        return this.importStaffId;
    }

    public void setImportStaffId(Long importStaffId) {
        this.importStaffId = importStaffId;
    }

    public Date getLastModify() {
        return this.lastModify;
    }

    public void setLastModify(Date lastModify) {
        this.lastModify = lastModify;
    }

    public Long getPartnerId() {
        return this.partnerId;
    }

    public void setPartnerId(Long partnerId) {
        this.partnerId = partnerId;
    }

    public String getPoCode() {
        return this.poCode;
    }

    public void setPoCode(String poCode) {
        this.poCode = poCode;
    }

    public Date getPoDate() {
        return this.poDate;
    }

    public void setPoDate(Date poDate) {
        this.poDate = poDate;
    }

    public String getReason() {
        return this.reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getRequestCode() {
        return this.requestCode;
    }

    public void setRequestCode(String requestCode) {
        this.requestCode = requestCode;
    }

    public Date getRequestDate() {
        return this.requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    public Long getStatus() {
        return this.status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Long getToOwnerId() {
        return this.toOwnerId;
    }

    public void setToOwnerId(Long toOwnerId) {
        this.toOwnerId = toOwnerId;
    }

    public Long getToOwnerType() {
        return this.toOwnerType;
    }

    public void setToOwnerType(Long toOwnerType) {
        this.toOwnerType = toOwnerType;
    }

    public Long getUnitPrice() {
        return this.unitPrice;
    }

    public void setUnitPrice(Long unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public boolean isCheckData() {
        return checkData;
    }

    public void setCheckData(boolean checkData) {
        this.checkData = checkData;
    }

    public List<ImportPartnerDetailDTO> getListImportPartnerDetailDTOs() {
        return listImportPartnerDetailDTOs;
    }

    public void setListImportPartnerDetailDTOs(List<ImportPartnerDetailDTO> listImportPartnerDetailDTOs) {
        this.listImportPartnerDetailDTOs = listImportPartnerDetailDTOs;
    }

    public String getActionCode() {
        return actionCode;
    }

    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }

    public String getImportStaffCode() {
        return importStaffCode;
    }

    public void setImportStaffCode(String importStaffCode) {
        this.importStaffCode = importStaffCode;
    }

    public List<String> getListProfile() {
        return listProfile;
    }

    public void setListProfile(List<String> listProfiles) {
        if (listProfiles != null) {
            profile = "";
            for (String prof : listProfiles) {
                if (profile != null && !profile.isEmpty()) {
                    profile += ",";
                }
                profile += prof;
            }
        }
        this.listProfile = listProfiles;
    }

    public Long getImpType() {
        return impType;
    }

    public void setImpType(Long impType) {
        this.impType = impType;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public boolean isSignVoffice() {
        return signVoffice;
    }

    public void setSignVoffice(boolean signVoffice) {
        this.signVoffice = signVoffice;
    }

    public boolean isSyncERP() {
        return syncERP;
    }

    public void setSyncERP(boolean syncERP) {
        this.syncERP = syncERP;
    }

    public StockTransVofficeDTO getStockTransVofficeDTO() {
        return stockTransVofficeDTO;
    }

    public void setStockTransVofficeDTO(StockTransVofficeDTO stockTransVofficeDTO) {
        this.stockTransVofficeDTO = stockTransVofficeDTO;
    }

    public String getToOwnerName() {
        return toOwnerName;
    }

    public void setToOwnerName(String toOwnerName) {
        this.toOwnerName = toOwnerName;
    }

    public Long getPartnerContractId() {
        return partnerContractId;
    }

    public void setPartnerContractId(Long partnerContractId) {
        this.partnerContractId = partnerContractId;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    public List<ProductOfferingDTO> getLsProductOfferDto() {
        return lsProductOfferDto;
    }

    public void setLsProductOfferDto(List<ProductOfferingDTO> lsProductOfferDto) {
        this.lsProductOfferDto = lsProductOfferDto;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Long getReasonId() {
        return reasonId;
    }

    public void setReasonId(Long reasonId) {
        this.reasonId = reasonId;
    }

    public SignOfficeDTO getSignOfficeDTO() {
        return signOfficeDTO;
    }

    public void setSignOfficeDTO(SignOfficeDTO signOfficeDTO) {
        this.signOfficeDTO = signOfficeDTO;
    }

    public boolean isLogistic() {
        return logistic;
    }

    public void setLogistic(boolean logistic) {
        this.logistic = logistic;
    }
}
