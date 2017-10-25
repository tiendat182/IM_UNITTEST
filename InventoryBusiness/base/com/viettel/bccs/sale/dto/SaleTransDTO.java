package com.viettel.bccs.sale.dto;

import com.viettel.fw.dto.BaseDTO;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class SaleTransDTO extends BaseDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long saleTransId;
    private Date saleTransDate;
    private String saleTransType;
    private String status;
    private String checkStock;
    private Long invoiceUsedId;
    private Date invoiceCreateDate;
    private Long shopId;
    private Long staffId;
    private String payMethod;
    private Long saleServiceId;
    private Long saleServicePriceId;
    private Long amountService;
    private Long amountModel;
    private Long discount;
    private BigDecimal promotion;
    private Long amountTax;
    private Long amountNotTax;
    private Long vat;
    private Long tax;
    private Long subId;
    private String isdn;
    private String custName;
    private String contractNo;
    private String telNumber;
    private String company;
    private String address;
    private String tin;
    private String note;
    private String destroyUser;
    private Date destroyDate;
    private String approverUser;
    private Date approverDate;
    private Long reasonId;
    private Long telecomServiceId;
    private String transferGoods;
    private String saleTransCode;
    private Long stockTransId;
    private Long createStaffId;
    private String shopCode;
    private String serial;
    private String actionCode;
    private Long receiverId;
    private Long receiverType;
    private Long recordWorkId;
    private String saleProgram;
    private String voucherCode;
    private Long totalVoucherAmount;
    private Long id;

    private String shopPath;
    private String shopName;
    private String staffCode;
    private String staffName;
    private String telecomServiceName;
    private String saleTransTypeName;
    //Phund add
    private String realStep;
    private String revenueObj;
    private String primaryAccount;
    private Long posId;
    private String revenueStatus;
    private String revenueType;
    private Long revenuePayStatus;
    private String syncStatus;
    private Long stockStaffId;
    private Long isAdjust;
    private String requestId;
    private Long amountBasic;
    private Long invoiceOwnerId;
    private Long checkCreateInvoice;
    //tunglv4 add
    private String email;

    private List<SaleTransDetailDTO> listSaleTransDetail;
    //thiendn1: add them danh sach sale trans detail
    private List<SaleTransDetailDTO> saleTransDetailDTOs;

    private String payMethodName;
    private String invoiceNo;
    private String reasonName;
    private Long bankplusStatus;
    private Long paymentDebitStatus;
    private Long fromSaleTransId;
    private String requestType;

    private Date fromDate;
    private Date toDate;
    private String requestCode;
    private String bankplusNumber;
    private String contentTrans;
    private String bankCode;
    private String retry;
    private String description;
    private String bankplusStatusName;
    private String bankplusTransId;
    private Date lastUpdateDate;
    private Long checkType;
    private Long inTransId;


    public SaleTransDTO() {
    }

    public SaleTransDTO(Long saleTransId, String saleTransCode, String saleTransType) {
        this.saleTransId = saleTransId;
        this.saleTransCode = saleTransCode;
        this.saleTransType = saleTransType;
    }

    public Long getCheckType() {
        return checkType;
    }

    public void setCheckType(Long checkType) {
        this.checkType = checkType;
    }

    public String getShopPath() {
        return shopPath;
    }

    public void setShopPath(String shopPath) {
        this.shopPath = shopPath;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getStaffCode() {
        return staffCode;
    }

    public void setStaffCode(String staffCode) {
        this.staffCode = staffCode;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getKeySet() {
        return keySet;
    }

    public String getActionCode() {
        return this.actionCode;
    }

    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getAmountModel() {
        return this.amountModel;
    }

    public void setAmountModel(Long amountModel) {
        this.amountModel = amountModel;
    }

    public Long getAmountNotTax() {
        return this.amountNotTax;
    }

    public void setAmountNotTax(Long amountNotTax) {
        this.amountNotTax = amountNotTax;
    }

    public Long getAmountService() {
        return this.amountService;
    }

    public void setAmountService(Long amountService) {
        this.amountService = amountService;
    }

    public Long getAmountTax() {
        return this.amountTax;
    }

    public void setAmountTax(Long amountTax) {
        this.amountTax = amountTax;
    }

    public Date getApproverDate() {
        return this.approverDate;
    }

    public void setApproverDate(Date approverDate) {
        this.approverDate = approverDate;
    }

    public String getApproverUser() {
        return this.approverUser;
    }

    public void setApproverUser(String approverUser) {
        this.approverUser = approverUser;
    }

    public String getCheckStock() {
        return this.checkStock;
    }

    public void setCheckStock(String checkStock) {
        this.checkStock = checkStock;
    }

    public String getCompany() {
        return this.company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getContractNo() {
        return this.contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public Long getCreateStaffId() {
        return this.createStaffId;
    }

    public void setCreateStaffId(Long createStaffId) {
        this.createStaffId = createStaffId;
    }

    public String getCustName() {
        return this.custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public Date getDestroyDate() {
        return this.destroyDate;
    }

    public void setDestroyDate(Date destroyDate) {
        this.destroyDate = destroyDate;
    }

    public String getDestroyUser() {
        return this.destroyUser;
    }

    public void setDestroyUser(String destroyUser) {
        this.destroyUser = destroyUser;
    }

    public Long getDiscount() {
        return this.discount;
    }

    public void setDiscount(Long discount) {
        this.discount = discount;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getInvoiceCreateDate() {
        return this.invoiceCreateDate;
    }

    public void setInvoiceCreateDate(Date invoiceCreateDate) {
        this.invoiceCreateDate = invoiceCreateDate;
    }

    public Long getInvoiceUsedId() {
        return this.invoiceUsedId;
    }

    public void setInvoiceUsedId(Long invoiceUsedId) {
        this.invoiceUsedId = invoiceUsedId;
    }

    public String getIsdn() {
        return this.isdn;
    }

    public void setIsdn(String isdn) {
        this.isdn = isdn;
    }

    public String getNote() {
        return this.note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getPayMethod() {
        return this.payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public BigDecimal getPromotion() {
        return this.promotion;
    }

    public void setPromotion(BigDecimal promotion) {
        this.promotion = promotion;
    }

    public Long getReasonId() {
        return this.reasonId;
    }

    public void setReasonId(Long reasonId) {
        this.reasonId = reasonId;
    }

    public Long getSaleServiceId() {
        return this.saleServiceId;
    }

    public void setSaleServiceId(Long saleServiceId) {
        this.saleServiceId = saleServiceId;
    }

    public Long getSaleServicePriceId() {
        return this.saleServicePriceId;
    }

    public void setSaleServicePriceId(Long saleServicePriceId) {
        this.saleServicePriceId = saleServicePriceId;
    }

    public String getSaleTransCode() {
        return this.saleTransCode;
    }

    public void setSaleTransCode(String saleTransCode) {
        this.saleTransCode = saleTransCode;
    }

    public Date getSaleTransDate() {
        return this.saleTransDate;
    }

    public void setSaleTransDate(Date saleTransDate) {
        this.saleTransDate = saleTransDate;
    }

    public Long getSaleTransId() {
        return this.saleTransId;
    }

    public void setSaleTransId(Long saleTransId) {
        this.saleTransId = saleTransId;
    }

    public String getSaleTransType() {
        return this.saleTransType;
    }

    public void setSaleTransType(String saleTransType) {
        this.saleTransType = saleTransType;
    }

    public String getSerial() {
        return this.serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getShopCode() {
        return this.shopCode;
    }

    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
    }

    public Long getShopId() {
        return this.shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getStaffId() {
        return this.staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getStockTransId() {
        return this.stockTransId;
    }

    public void setStockTransId(Long stockTransId) {
        this.stockTransId = stockTransId;
    }

    public Long getSubId() {
        return this.subId;
    }

    public void setSubId(Long subId) {
        this.subId = subId;
    }

    public Long getTax() {
        return this.tax;
    }

    public void setTax(Long tax) {
        this.tax = tax;
    }

    public String getTelNumber() {
        return this.telNumber;
    }

    public void setTelNumber(String telNumber) {
        this.telNumber = telNumber;
    }

    public Long getTelecomServiceId() {
        return this.telecomServiceId;
    }

    public void setTelecomServiceId(Long telecomServiceId) {
        this.telecomServiceId = telecomServiceId;
    }

    public String getTin() {
        return this.tin;
    }

    public void setTin(String tin) {
        this.tin = tin;
    }

    public String getTransferGoods() {
        return this.transferGoods;
    }

    public void setTransferGoods(String transferGoods) {
        this.transferGoods = transferGoods;
    }

    public Long getVat() {
        return this.vat;
    }

    public void setVat(Long vat) {
        this.vat = vat;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public Long getReceiverType() {
        return receiverType;
    }

    public void setReceiverType(Long receiverType) {
        this.receiverType = receiverType;
    }

    public Long getRecordWorkId() {
        return recordWorkId;
    }

    public void setRecordWorkId(Long recordWorkId) {
        this.recordWorkId = recordWorkId;
    }

    public String getSaleProgram() {
        return saleProgram;
    }

    public void setSaleProgram(String saleProgram) {
        this.saleProgram = saleProgram;
    }

    public String getVoucherCode() {
        return voucherCode;
    }

    public void setVoucherCode(String voucherCode) {
        this.voucherCode = voucherCode;
    }

    public Long getTotalVoucherAmount() {
        return totalVoucherAmount;
    }

    public void setTotalVoucherAmount(Long totalVoucherAmount) {
        this.totalVoucherAmount = totalVoucherAmount;
    }

    public String getTelecomServiceName() {
        return telecomServiceName;
    }

    public void setTelecomServiceName(String telecomServiceName) {
        this.telecomServiceName = telecomServiceName;
    }

    public String getSaleTransTypeName() {
        return saleTransTypeName;
    }

    public void setSaleTransTypeName(String saleTransTypeName) {
        this.saleTransTypeName = saleTransTypeName;
    }

    public List<SaleTransDetailDTO> getSaleTransDetailDTOs() {
        return saleTransDetailDTOs;
    }

    public void setSaleTransDetailDTOs(List<SaleTransDetailDTO> saleTransDetailDTOs) {
        this.saleTransDetailDTOs = saleTransDetailDTOs;
    }

    public List<SaleTransDetailDTO> getListSaleTransDetail() {
        return listSaleTransDetail;
    }

    public void setListSaleTransDetail(List<SaleTransDetailDTO> listSaleTransDetail) {
        this.listSaleTransDetail = listSaleTransDetail;
    }

    public void increaseAmountTax(Long supplyAmountTax) {
        if (amountTax == null) {
            amountTax = supplyAmountTax;
        } else {
            amountTax += supplyAmountTax;
        }
    }

    public void increaseAmountNotTax(Long supplyAmountNotTax) {
        if (amountNotTax == null) {
            amountNotTax = supplyAmountNotTax;
        } else {
            amountNotTax += supplyAmountNotTax;
        }
    }

    public void increaseTax(Long supplyTax) {
        if (tax == null) {
            tax = supplyTax;
        } else {
            tax += supplyTax;
        }
    }

    public Long getInvoiceOwnerId() {
        return invoiceOwnerId;
    }

    public void setInvoiceOwnerId(Long invoiceOwnerId) {
        this.invoiceOwnerId = invoiceOwnerId;
    }

    public Long getCheckCreateInvoice() {
        return checkCreateInvoice;
    }

    public void setCheckCreateInvoice(Long checkCreateInvoice) {
        this.checkCreateInvoice = checkCreateInvoice;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getRealStep() {
        return realStep;
    }

    public void setRealStep(String realStep) {
        this.realStep = realStep;
    }

    public String getRevenueObj() {
        return revenueObj;
    }

    public void setRevenueObj(String revenueObj) {
        this.revenueObj = revenueObj;
    }

    public String getPrimaryAccount() {
        return primaryAccount;
    }

    public void setPrimaryAccount(String primaryAccount) {
        this.primaryAccount = primaryAccount;
    }

    public Long getPosId() {
        return posId;
    }

    public void setPosId(Long posId) {
        this.posId = posId;
    }

    public String getRevenueStatus() {
        return revenueStatus;
    }

    public void setRevenueStatus(String revenueStatus) {
        this.revenueStatus = revenueStatus;
    }

    public String getRevenueType() {
        return revenueType;
    }

    public void setRevenueType(String revenueType) {
        this.revenueType = revenueType;
    }

    public Long getRevenuePayStatus() {
        return revenuePayStatus;
    }

    public void setRevenuePayStatus(Long revenuePayStatus) {
        this.revenuePayStatus = revenuePayStatus;
    }

    public String getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(String syncStatus) {
        this.syncStatus = syncStatus;
    }

    public Long getStockStaffId() {
        return stockStaffId;
    }

    public void setStockStaffId(Long stockStaffId) {
        this.stockStaffId = stockStaffId;
    }

    public Long getIsAdjust() {
        return isAdjust;
    }

    public void setIsAdjust(Long isAdjust) {
        this.isAdjust = isAdjust;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Long getAmountBasic() {
        return amountBasic;
    }

    public void setAmountBasic(Long amountBasic) {
        this.amountBasic = amountBasic;
    }

    public String getPayMethodName() {
        return payMethodName;
    }

    public void setPayMethodName(String payMethodName) {
        this.payMethodName = payMethodName;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getReasonName() {
        return reasonName;
    }

    public void setReasonName(String reasonName) {
        this.reasonName = reasonName;
    }

    public Long getBankplusStatus() {
        return bankplusStatus;
    }

    public void setBankplusStatus(Long bankplusStatus) {
        this.bankplusStatus = bankplusStatus;
    }

    public Long getPaymentDebitStatus() {
        return paymentDebitStatus;
    }

    public void setPaymentDebitStatus(Long paymentDebitStatus) {
        this.paymentDebitStatus = paymentDebitStatus;
    }

    public Long getFromSaleTransId() {
        return fromSaleTransId;
    }

    public void setFromSaleTransId(Long fromSaleTransId) {
        this.fromSaleTransId = fromSaleTransId;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRequestType() {
        return requestType;
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

    public String getRequestCode() {
        return requestCode;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public void setRequestCode(String requestCode) {
        this.requestCode = requestCode;
    }

    public String getBankplusNumber() {
        return bankplusNumber;
    }

    public void setBankplusNumber(String bankplusNumber) {
        this.bankplusNumber = bankplusNumber;
    }

    public String getContentTrans() {
        return contentTrans;
    }

    public void setContentTrans(String contentTrans) {
        this.contentTrans = contentTrans;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getRetry() {
        return retry;
    }

    public void setRetry(String retry) {
        this.retry = retry;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBankplusStatusName() {
        return bankplusStatusName;
    }

    public void setBankplusStatusName(String bankplusStatusName) {
        this.bankplusStatusName = bankplusStatusName;
    }

    public String getBankplusTransId() {
        return bankplusTransId;
    }

    public void setBankplusTransId(String bankplusTransId) {
        this.bankplusTransId = bankplusTransId;
    }

    public Long getInTransId() {
        return inTransId;
    }

    public void setInTransId(Long inTransId) {
        this.inTransId = inTransId;
    }

    @Override
    public String toString() {
        return saleTransCode;
    }
}
