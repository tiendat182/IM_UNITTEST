package com.viettel.bccs.inventory.dto;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DateUtil;
import com.viettel.fw.dto.BaseDTO;
import java.util.Date;
import java.util.List;

/**
 * @author ngocdm4 on 12/14/2015.
 */
public class VStockTransDTO extends BaseDTO {

    private Long stockTransID;
    private Long stockTransType;
    private String actionType;
    private Date createDateTime;
    private String userCode;
    private String userCreate;
    private Long fromOwnerID;
    private Long fromOwnerType;
    private String fromOwnerName;
    private Long toOwnerID;
    private Long toOwnerType;
    private String toOwnerName;
    private String actionCode;
    private Long actionID;
    private String note;
    private Long reasonID;
    private String reasonName;
    private String stockTransStatus;
    private String statusName;
    private String hisAction;
    private String depositStatus;
    private String stockBase;
    private String actionCMDCode;
    private Date createDateTrans;
    private Date startDate;
    private Date endDate;
    private Date realTransDate;
    private String stockTransStatusName;
    private String signCaStatus;
    private Long totalAmount;
    private List<String> lstStatus;
    private String vtUnit;
    private String objectType;
    private Long userShopId;
    private List<String> lstDepositStatus;
    private List<String> lstPayStatus;
    private String payStatus;
    private String invoiceStatus;
    //Nhap tu usertoken
    private Long staffId;
    private Long shopId;
    //Nhap tu giao dien
    private Long ownerType;
    private Long ownerID;
    private String signVoffice;
    private String signVofficeName;
    private String processOffline;
    private String processOfflineName;
    private String stockTransTypeName;
    //**end
    private Long isAutoGen;
    private String importNote;
    private Long currentUserShopId;
    private String currentUserShopPath;
    private Long importReasonID;
    private String importReasonName;
    private String transport;
    private boolean anotherTranType = true;
    Long index;
    private String documentStatus;
    private String errorDescription;
    private boolean printable = false;
    private boolean cancelable = false;
    private String stockTransAgent;
    private boolean staffInShop = false;
    private byte[] fileAttach;
    private List<StockTransDetailDTO> transDetailDTOs;


    /**
     * hien cap nhap voffice bang tay
     * @author thanhnt77
     */
    public boolean getIsSignOfficeHandle(String status) {
        return DataUtil.safeEqual(status, stockTransStatus) && Const.SIGN_VOFFICE.equals(signCaStatus);
    }

    private List<StockTransFullDTO> listStockTransDetail;

    public String getStrCreateDate() {
        return DateUtil.date2ddMMyyyyString(createDateTime);
    }

    public String getStrCreateDateTrans() {
        return DateUtil.date2ddMMyyyyString(createDateTrans);
    }

    public String getInvoiceStatus() {
        return invoiceStatus;
    }

    public void setInvoiceStatus(String invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public List<String> getLstPayStatus() {
        return lstPayStatus;
    }

    public void setLstPayStatus(List<String> lstPayStatus) {
        this.lstPayStatus = lstPayStatus;
    }

    public List<String> getLstDepositStatus() {
        return lstDepositStatus;
    }

    public void setLstDepositStatus(List<String> lstDepositStatus) {
        this.lstDepositStatus = lstDepositStatus;
    }

    public Long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Long totalAmount) {
        this.totalAmount = totalAmount;
    }

    public VStockTransDTO() {
    }

    public VStockTransDTO(Date startDate, Date endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }


    public String getStockTransStatusName() {
        return stockTransStatusName;
    }

    public void setStockTransStatusName(String stockTransStatusName) {
        this.stockTransStatusName = stockTransStatusName;
    }

    public Date getRealTransDate() {
        return realTransDate;
    }

    public void setRealTransDate(Date realTransDate) {
        this.realTransDate = realTransDate;
    }

    public String getKeySet() {
        return keySet;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Long getStockTransID() {
        return stockTransID;
    }

    public void setStockTransID(Long stockTransID) {
        this.stockTransID = stockTransID;
    }

    public Long getStockTransType() {
        return stockTransType;
    }

    public void setStockTransType(Long stockTransType) {
        this.stockTransType = stockTransType;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public Date getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(Date createDateTime) {
        this.createDateTime = createDateTime;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getUserCreate() {
        return userCreate;
    }

    public void setUserCreate(String userCreate) {
        this.userCreate = userCreate;
    }

    public Long getFromOwnerID() {
        return fromOwnerID;
    }

    public void setFromOwnerID(Long fromOwnerID) {
        this.fromOwnerID = fromOwnerID;
    }

    public Long getFromOwnerType() {
        return fromOwnerType;
    }

    public void setFromOwnerType(Long fromOwnerType) {
        this.fromOwnerType = fromOwnerType;
    }

    public String getFromOwnerName() {
        return fromOwnerName;
    }

    public void setFromOwnerName(String fromOwnerName) {
        this.fromOwnerName = fromOwnerName;
    }

    public Long getToOwnerID() {
        return toOwnerID;
    }

    public void setToOwnerID(Long toOwnerID) {
        this.toOwnerID = toOwnerID;
    }

    public Long getToOwnerType() {
        return toOwnerType;
    }

    public void setToOwnerType(Long toOwnerType) {
        this.toOwnerType = toOwnerType;
    }

    public String getToOwnerName() {
        return toOwnerName;
    }

    public void setToOwnerName(String toOwnerName) {
        this.toOwnerName = toOwnerName;
    }

    public String getActionCode() {
        return actionCode;
    }

    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }

    public Long getActionID() {
        return actionID;
    }

    public void setActionID(Long actionID) {
        this.actionID = actionID;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Long getReasonID() {
        return reasonID;
    }

    public void setReasonID(Long reasonID) {
        this.reasonID = reasonID;
    }

    public String getReasonName() {
        return reasonName;
    }

    public void setReasonName(String reasonName) {
        this.reasonName = reasonName;
    }

    public String getStockTransStatus() {
        return stockTransStatus;
    }

    public void setStockTransStatus(String stockTransStatus) {
        this.stockTransStatus = stockTransStatus;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getHisAction() {
        return hisAction;
    }

    public void setHisAction(String hisAction) {
        this.hisAction = hisAction;
    }

    public String getDepositStatus() {
        return depositStatus;
    }

    public void setDepositStatus(String depositStatus) {
        this.depositStatus = depositStatus;
    }

    public String getStockBase() {
        return stockBase;
    }

    public void setStockBase(String stockBase) {
        this.stockBase = stockBase;
    }

    public String getActionCMDCode() {
        return actionCMDCode;
    }

    public void setActionCMDCode(String actionCMDCode) {
        this.actionCMDCode = actionCMDCode;
    }

    public Date getCreateDateTrans() {
        return createDateTrans;
    }

    public void setCreateDateTrans(Date createDateTrans) {
        this.createDateTrans = createDateTrans;
    }

    public List<String> getLstStatus() {
        if (DataUtil.isNullOrEmpty(lstStatus)) {
            lstStatus = Lists.newArrayList();
        }
        return lstStatus;
    }

    public void setLstStatus(List<String> lstStatus) {
        this.lstStatus = lstStatus;
    }

    public Long getUserShopId() {
        return userShopId;
    }

    public void setUserShopId(Long userShopId) {
        this.userShopId = userShopId;
    }

    public String getVtUnit() {
        return vtUnit;
    }

    public void setVtUnit(String vtUnit) {
        this.vtUnit = vtUnit;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }


    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(Long ownerType) {
        this.ownerType = ownerType;
    }

    public Long getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(Long ownerID) {
        this.ownerID = ownerID;
    }

    public String getSignVoffice() {
        return signVoffice;
    }

    public void setSignVoffice(String signVoffice) {
        this.signVoffice = signVoffice;
    }

    public String getSignVofficeName() {
        return signVofficeName;
    }

    public void setSignVofficeName(String signVofficeName) {
        this.signVofficeName = signVofficeName;
    }

    public String getProcessOffline() {
        return processOffline;
    }

    public void setProcessOffline(String processOffline) {
        this.processOffline = processOffline;
    }

    public String getProcessOfflineName() {
        return processOfflineName;
    }

    public void setProcessOfflineName(String processOfflineName) {
        this.processOfflineName = processOfflineName;
    }

    public String getStockTransTypeName() {
        return stockTransTypeName;
    }

    public void setStockTransTypeName(String stockTransTypeName) {
        this.stockTransTypeName = stockTransTypeName;
    }

    public Long getIsAutoGen() {
        return isAutoGen;
    }

    public void setIsAutoGen(Long isAutoGen) {
        this.isAutoGen = isAutoGen;
    }

    public String getImportNote() {
        return importNote;
    }

    public Long getCurrentUserShopId() {
        return currentUserShopId;
    }

    public void setCurrentUserShopId(Long currentUserShopId) {
        this.currentUserShopId = currentUserShopId;
    }

    public String getCurrentUserShopPath() {
        return currentUserShopPath;
    }

    public void setCurrentUserShopPath(String currentUserShopPath) {
        this.currentUserShopPath = currentUserShopPath;
    }

    public void setImportNote(String importNote) {
        this.importNote = importNote;
    }

    public Long getImportReasonID() {
        return importReasonID;
    }

    public void setImportReasonID(Long importReasonID) {
        this.importReasonID = importReasonID;
    }

    public String getImportReasonName() {
        return importReasonName;
    }

    public void setImportReasonName(String importReasonName) {
        this.importReasonName = importReasonName;
    }

    public String getTransport() {
        return transport;
    }

    public void setTransport(String transport) {
        this.transport = transport;
    }

    public StockTransDTO toStockTransDTO() {
        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setFromOwnerId(getFromOwnerID());
        stockTransDTO.setFromOwnerType(getFromOwnerType());
        stockTransDTO.setToOwnerId(getToOwnerID());
        stockTransDTO.setToOwnerType(getToOwnerType());
        stockTransDTO.setStockTransId(getStockTransID());
        stockTransDTO.setImportReasonId(getReasonID());
        stockTransDTO.setNote(getNote());
        stockTransDTO.setStockBase(getStockBase());
        stockTransDTO.setStockTransActionId(getActionID());
        return stockTransDTO;
    }

    public boolean isAnotherTranType() {
        return anotherTranType;
    }

    public void setAnotherTranType(boolean anotherTranType) {
        this.anotherTranType = anotherTranType;
    }

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public List<StockTransFullDTO> getListStockTransDetail() {
        return listStockTransDetail;
    }

    public void setListStockTransDetail(List<StockTransFullDTO> listStockTransDetail) {
        this.listStockTransDetail = listStockTransDetail;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    public String getDocumentStatus() {
        return documentStatus;
    }

    public void setDocumentStatus(String documentStatus) {
        this.documentStatus = documentStatus;
    }

    public boolean isPrintable() {
        return printable;
    }

    public void setPrintable(boolean printable) {
        this.printable = printable;
    }

    public boolean isCancelable() {
        return cancelable;
    }

    public void setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
    }

    public String getStockTransAgent() {
        return stockTransAgent;
    }

    public void setStockTransAgent(String stockTransAgent) {
        this.stockTransAgent = stockTransAgent;
    }

    public boolean isStaffInShop() {
        return staffInShop;
    }

    public void setStaffInShop(boolean staffInShop) {
        this.staffInShop = staffInShop;
    }

    public String getSignCaStatus() {
        return signCaStatus;
    }

    public void setSignCaStatus(String signCaStatus) {
        this.signCaStatus = signCaStatus;
    }

    public List<StockTransDetailDTO> getTransDetailDTOs() {
        return transDetailDTOs;
    }

    public void setTransDetailDTOs(List<StockTransDetailDTO> transDetailDTOs) {
        this.transDetailDTOs = transDetailDTOs;
    }

    public byte[] getFileAttach() {
        return fileAttach;
    }

    public void setFileAttach(byte[] fileAttach) {
        this.fileAttach = fileAttach;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof VStockTransDTO) {
            VStockTransDTO test = (VStockTransDTO) obj;
            if (DataUtil.safeEqual(stockTransID, test.getStockTransID()) && DataUtil.safeEqual(actionID, test.getActionID())) {
                return true;
            }
            return false;
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
