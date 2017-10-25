/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.bccs.sale.model;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author ThanhNT77
 */
@Entity
@Table(name = "SALE_TRANS", schema = "BCCS_SALE")
@XmlRootElement
public class SaleTrans implements Serializable {

    public static enum COLUMNS {ACTIONCODE, ADDRESS, AMOUNTMODEL, AMOUNTNOTTAX, AMOUNTSERVICE, AMOUNTTAX, APPROVERDATE, APPROVERUSER, CHECKSTOCK, COMPANY, CONTRACTNO, CREATESTAFFID, CUSTNAME, DESTROYDATE, DESTROYUSER, DISCOUNT, ID, INVOICECREATEDATE, INVOICEUSEDID, ISDN, LSTSTOCKISDN, LSTSTOCKSIM, NOTE, PAYMETHOD, PROMOTION, REASONID, SALESERVICEID, SALESERVICEPRICEID, SALETRANSCODE, SALETRANSDATE, SALETRANSID, SALETRANSTYPE, SERIAL, SHOPCODE, SHOPID, STAFFID, STATUS, STOCKTRANSID, SUBID, TAX, TELNUMBER, EMAIL, TELECOMSERVICEID, TIN, TRANSRESULT, TRANSFERGOODS, VAT, EXCLUSE_ID_LIST}

    ;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @SequenceGenerator(name = "SALE_TRANS_ID_GENERATOR", sequenceName = "SALE_TRANS_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SALE_TRANS_ID_GENERATOR")
    @Column(name = "SALE_TRANS_ID")
    private Long saleTransId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "SALE_TRANS_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date saleTransDate;
    @Size(max = 2)
    @Column(name = "SALE_TRANS_TYPE")
    private String saleTransType;
    @Basic(optional = false)
    @NotNull
    @Column(name = "STATUS")
    private String status;
    @Size(max = 2)
    @Column(name = "CHECK_STOCK")
    private String checkStock;
    @Column(name = "INVOICE_USED_ID")
    private Long invoiceUsedId;
    @Column(name = "INVOICE_CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date invoiceCreateDate;
    @Column(name = "SHOP_ID")
    private Long shopId;
    @Column(name = "STAFF_ID")
    private Long staffId;
    @Size(max = 2)
    @Column(name = "PAY_METHOD")
    private String payMethod;
    @Column(name = "SALE_SERVICE_ID")
    private Long saleServiceId;
    @Column(name = "SALE_SERVICE_PRICE_ID")
    private Long saleServicePriceId;
    @Column(name = "AMOUNT_SERVICE")
    private Long amountService;
    @Column(name = "AMOUNT_MODEL")
    private Long amountModel;
    @Column(name = "DISCOUNT")
    private Long discount;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "PROMOTION")
    private BigDecimal promotion;
    @Column(name = "AMOUNT_TAX")
    private Long amountTax;
    @Column(name = "AMOUNT_NOT_TAX")
    private Long amountNotTax;
    @Column(name = "VAT")
    private Long vat;
    @Column(name = "TAX")
    private Long tax;
    @Column(name = "SUB_ID")
    private Long subId;
    @Size(max = 32)
    @Column(name = "ISDN")
    private String isdn;
    @Size(max = 300)
    @Column(name = "CUST_NAME")
    private String custName;
    @Size(max = 50)
    @Column(name = "CONTRACT_NO")
    private String contractNo;
    @Size(max = 100)
    @Column(name = "TEL_NUMBER")
    private String telNumber;
    @Column(name = "EMAIL")
    private String email;
    @Size(max = 100)
    @Column(name = "COMPANY")
    private String company;
    @Size(max = 200)
    @Column(name = "ADDRESS")
    private String address;
    @Size(max = 100)
    @Column(name = "TIN")
    private String tin;
    @Size(max = 200)
    @Column(name = "NOTE")
    private String note;
    @Size(max = 20)
    @Column(name = "DESTROY_USER")
    private String destroyUser;
    @Column(name = "DESTROY_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date destroyDate;
    @Size(max = 20)
    @Column(name = "APPROVER_USER")
    private String approverUser;
    @Column(name = "APPROVER_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date approverDate;
    @Column(name = "REASON_ID")
    private Long reasonId;
    @Column(name = "TELECOM_SERVICE_ID")
    private Long telecomServiceId;
    @Size(max = 1)
    @Column(name = "TRANSFER_GOODS")
    private String transferGoods;
    @Size(max = 30)
    @Column(name = "SALE_TRANS_CODE")
    private String saleTransCode;
    @Column(name = "STOCK_TRANS_ID")
    private Long stockTransId;
    @Column(name = "CREATE_STAFF_ID")
    private Long createStaffId;
    @Size(max = 20)
    @Column(name = "SHOP_CODE")
    private String shopCode;
    @Basic(optional = false)
    @Size(max = 30)
    @Column(name = "SERIAL")
    private String serial;
    @Size(max = 30)
    @Column(name = "ACTION_CODE")
    private String actionCode;
    @Column(name = "RECEIVER_ID")
    private Long receiverId;

    @Column(name = "RECEIVER_TYPE")
    private Long receiverType;

    @Column(name = "RECORD_WORK_ID")
    private Long recordWorkId;

    @Column(name = "SALE_PROGRAM")
    private String saleProgram;

    @Size(max = 500)
    @Column(name = "VOUCHER_CODE")
    private String voucherCode;

    @Column(name = "TOTAL_VOUCHER_AMOUNT")
    private Long totalVoucherAmount;
    //    tienph2
    @Column(name = "FROM_SALE_TRANS_ID")
    private Long fromSaleTransId;

    @Column(name = "INVOICE_OWNER_ID")
    private Long invoiceOwnerId;

    @Column(name = "CHECK_CREATE_INVOICE")
    private Long checkCreateInvoice;

    @Column(name = "SHOP_PATH")
    private String shopPath;

    @Column(name = "STAFF_NAME")
    private String staffName;

    @Column(name = "STAFF_CODE")
    private String staffCode;

    @Column(name = "SHOP_NAME")
    private String shopName;

    @Column(name = "IN_TRANS_ID")
    private Long inTransId;

    @Column(name = "LAST_UPDATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdateDate;

    // Dau noi mobile: luu ten account dau noi (so thue bao)
    // Dau noi co dinh: Luu ten account dau noi
    @Column(name = "PRIMARY_ACCOUNT")
    private String primaryAccount;

    // Luu kho trung tam huyen quan ly nhan vien ky thuat dau noi co dinh
    // Truong nay chi dung cho dau noi co dinh
    @Column(name = "POS_ID")
    private Long posId;

    @Column(name = "IS_ADJUST")
    private Long isAdjust;

    @Column(name = "REQUEST_ID")
    private String requestId;

    @Column(name = "REVENUE_PAY_STATUS")
    private Long revenuePayStatus;

    @Column(name = "BANKPLUS_STATUS")
    private Long bankplusStatus;

    @Column(name = "PAYMENT_DEBIT_STATUS")
    private Long paymentDebitStatus;

    @Column(name = "REQUEST_TYPE")
    private String requestType;

    @Transient
    private String realStep;
    @Transient
    private String revenueObj;
//    @Transient
//    // Dau noi mobile: luu ten account dau noi (so thue bao)
//    // Dau noi co dinh: Luu ten account dau noi
//    private String primaryAccount;
//    @Transient
//    // Luu kho trung tam huyen quan ly nhan vien ky thuat dau noi co dinh
//    // Truong nay chi dung cho dau noi co dinh
//    private Long posId;
//    @Transient
//    private String requestId;

    public SaleTrans() {
    }

    public SaleTrans(Long saleTransId) {
        this.saleTransId = saleTransId;
    }

    public SaleTrans(Long saleTransId, Date saleTransDate, String status, Long transResult) {
        this.saleTransId = saleTransId;
        this.saleTransDate = saleTransDate;
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getSaleTransId() {
        return saleTransId;
    }

    public void setSaleTransId(Long saleTransId) {
        this.saleTransId = saleTransId;
    }

    public Date getSaleTransDate() {
        return saleTransDate;
    }

    public void setSaleTransDate(Date saleTransDate) {
        this.saleTransDate = saleTransDate;
    }

    public String getSaleTransType() {
        return saleTransType;
    }

    public void setSaleTransType(String saleTransType) {
        this.saleTransType = saleTransType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCheckStock() {
        return checkStock;
    }

    public void setCheckStock(String checkStock) {
        this.checkStock = checkStock;
    }

    public Long getInvoiceUsedId() {
        return invoiceUsedId;
    }

    public void setInvoiceUsedId(Long invoiceUsedId) {
        this.invoiceUsedId = invoiceUsedId;
    }

    public Date getInvoiceCreateDate() {
        return invoiceCreateDate;
    }

    public void setInvoiceCreateDate(Date invoiceCreateDate) {
        this.invoiceCreateDate = invoiceCreateDate;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public Long getSaleServiceId() {
        return saleServiceId;
    }

    public void setSaleServiceId(Long saleServiceId) {
        this.saleServiceId = saleServiceId;
    }

    public Long getSaleServicePriceId() {
        return saleServicePriceId;
    }

    public void setSaleServicePriceId(Long saleServicePriceId) {
        this.saleServicePriceId = saleServicePriceId;
    }

    public Long getAmountService() {
        return amountService;
    }

    public void setAmountService(Long amountService) {
        this.amountService = amountService;
    }

    public Long getAmountModel() {
        return amountModel;
    }

    public void setAmountModel(Long amountModel) {
        this.amountModel = amountModel;
    }

    public Long getDiscount() {
        return discount;
    }

    public void setDiscount(Long discount) {
        this.discount = discount;
    }

    public BigDecimal getPromotion() {
        return promotion;
    }

    public void setPromotion(BigDecimal promotion) {
        this.promotion = promotion;
    }

    public Long getAmountTax() {
        return amountTax;
    }

    public void setAmountTax(Long amountTax) {
        this.amountTax = amountTax;
    }

    public Long getAmountNotTax() {
        return amountNotTax;
    }

    public void setAmountNotTax(Long amountNotTax) {
        this.amountNotTax = amountNotTax;
    }

    public Long getVat() {
        return vat;
    }

    public void setVat(Long vat) {
        this.vat = vat;
    }

    public Long getTax() {
        return tax;
    }

    public void setTax(Long tax) {
        this.tax = tax;
    }

    public Long getSubId() {
        return subId;
    }

    public void setSubId(Long subId) {
        this.subId = subId;
    }

    public String getIsdn() {
        return isdn;
    }

    public void setIsdn(String isdn) {
        this.isdn = isdn;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public String getTelNumber() {
        return telNumber;
    }

    public void setTelNumber(String telNumber) {
        this.telNumber = telNumber;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTin() {
        return tin;
    }

    public void setTin(String tin) {
        this.tin = tin;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDestroyUser() {
        return destroyUser;
    }

    public void setDestroyUser(String destroyUser) {
        this.destroyUser = destroyUser;
    }

    public Date getDestroyDate() {
        return destroyDate;
    }

    public void setDestroyDate(Date destroyDate) {
        this.destroyDate = destroyDate;
    }

    public String getApproverUser() {
        return approverUser;
    }

    public void setApproverUser(String approverUser) {
        this.approverUser = approverUser;
    }

    public Date getApproverDate() {
        return approverDate;
    }

    public void setApproverDate(Date approverDate) {
        this.approverDate = approverDate;
    }

    public Long getReasonId() {
        return reasonId;
    }

    public void setReasonId(Long reasonId) {
        this.reasonId = reasonId;
    }

    public Long getTelecomServiceId() {
        return telecomServiceId;
    }

    public void setTelecomServiceId(Long telecomServiceId) {
        this.telecomServiceId = telecomServiceId;
    }

    public String getTransferGoods() {
        return transferGoods;
    }

    public void setTransferGoods(String transferGoods) {
        this.transferGoods = transferGoods;
    }

    public String getSaleTransCode() {
        return saleTransCode;
    }

    public void setSaleTransCode(String saleTransCode) {
        this.saleTransCode = saleTransCode;
    }

    public Long getStockTransId() {
        return stockTransId;
    }

    public void setStockTransId(Long stockTransId) {
        this.stockTransId = stockTransId;
    }

    public Long getCreateStaffId() {
        return createStaffId;
    }

    public void setCreateStaffId(Long createStaffId) {
        this.createStaffId = createStaffId;
    }

    public String getShopCode() {
        return shopCode;
    }

    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getActionCode() {
        return actionCode;
    }

    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
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

    public static long getSerialVersionUID() {
        return serialVersionUID;
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

    public Long getFromSaleTransId() {
        return fromSaleTransId;
    }

    public void setFromSaleTransId(Long fromSaleTransId) {
        this.fromSaleTransId = fromSaleTransId;
    }

    public Long getCheckCreateInvoice() {
        return checkCreateInvoice;
    }

    public void setCheckCreateInvoice(Long checkCreateInvoice) {
        this.checkCreateInvoice = checkCreateInvoice;
    }

    public String getShopPath() {
        return shopPath;
    }

    public void setShopPath(String shopPath) {
        this.shopPath = shopPath;
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

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public Long getInTransId() {
        return inTransId;
    }

    public void setInTransId(Long inTransId) {
        this.inTransId = inTransId;
    }

    public Long getBankplusStatus() {
        return bankplusStatus;
    }

    public void setBankplusStatus(Long bankplusStatus) {
        this.bankplusStatus = bankplusStatus;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (saleTransId != null ? saleTransId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SaleTrans)) {
            return false;
        }
        SaleTrans other = (SaleTrans) object;
        if ((this.saleTransId == null && other.saleTransId != null) || (this.saleTransId != null && !this.saleTransId.equals(other.saleTransId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.bccs.sale.model.SaleTrans[ saleTransId=" + saleTransId + " ]";
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

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public Long getRevenuePayStatus() {
        return revenuePayStatus;
    }

    public void setRevenuePayStatus(Long revenuePayStatus) {
        this.revenuePayStatus = revenuePayStatus;
    }

    public Long getPaymentDebitStatus() {
        return paymentDebitStatus;
    }

    public void setPaymentDebitStatus(Long paymentDebitStatus) {
        this.paymentDebitStatus = paymentDebitStatus;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }
}
