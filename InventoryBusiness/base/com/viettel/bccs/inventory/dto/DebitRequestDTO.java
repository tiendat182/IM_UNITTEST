package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;

import java.util.Date;
import java.util.List;

public class DebitRequestDTO extends BaseDTO {

    private Date createDate;
    private String createUser;
    private String description;
    private String fileName;
    private byte[] fileContent;
    private Date lastUpdateTime;
    private String lastUpdateUser;
    private String requestCode;
    private Long requestId;
    private String requestObjectType;
    private String status;
    private Date fromDate;
    private Date toDate;
    private String currentStaff;
    private Long currentShopId;
    private List<DebitRequestDetailDTO> debitDebitRequestDetails;
    private String userName;
    private String passWord;
    private Long signFlowId;
    private Long createUserId;

    public Long getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public Long getSignFlowId() {
        return signFlowId;
    }

    public void setSignFlowId(Long signFlowId) {
        this.signFlowId = signFlowId;
    }

    public String getKeySet() {
        return keySet;
    }

    public Long getCurrentShopId() {
        return currentShopId;
    }

    public void setCurrentShopId(Long currentShopId) {
        this.currentShopId = currentShopId;
    }

    public void addDebitRequestDetail(DebitRequestDetailDTO detail) {
        debitDebitRequestDetails.add(detail);
    }

    public DebitRequestDetailDTO getDebitRequestDetail(int index) {
        if (debitDebitRequestDetails == null || index >= debitDebitRequestDetails.size())
            return null;
        return debitDebitRequestDetails.get(index);
    }

    public List<DebitRequestDetailDTO> getDebitDebitRequestDetails() {
        return debitDebitRequestDetails;
    }

    public void setDebitDebitRequestDetails(List<DebitRequestDetailDTO> debitDebitRequestDetails) {
        this.debitDebitRequestDetails = debitDebitRequestDetails;
    }

    public Date getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateUser() {
        return this.createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    //    }
    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Date getLastUpdateTime() {
        return this.lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getLastUpdateUser() {
        return this.lastUpdateUser;
    }

    public void setLastUpdateUser(String lastUpdateUser) {
        this.lastUpdateUser = lastUpdateUser;
    }

    public String getRequestCode() {
        return this.requestCode;
    }

    public void setRequestCode(String requestCode) {
        this.requestCode = requestCode;
    }

    public Long getRequestId() {
        return this.requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public String getRequestObjectType() {
        return this.requestObjectType;
    }

    public void setRequestObjectType(String requestObjectType) {
        this.requestObjectType = requestObjectType;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public byte[] getFileContent() {
        return fileContent;
    }

    public void setFileContent(byte[] fileContent) {
        this.fileContent = fileContent;
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

    public String getCurrentStaff() {
        return currentStaff;
    }

    public void setCurrentStaff(String currentStaff) {
        this.currentStaff = currentStaff;
    }
}
