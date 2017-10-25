package com.viettel.bccs.inventory.model;

/**
 * Created by hoangnt14 on 1/11/2016.
 */

import java.io.Serializable;
import java.lang.Long;
import java.util.Date;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author hoangnt14
 */
@Entity
@Table(name = "DEPOSIT")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "Deposit.findAll", query = "SELECT d FROM Deposit d")})
public class Deposit implements Serializable {
public static enum COLUMNS {ADDRESS, AMOUNT, BRANCHID, CONTRACTNO, CREATEBY, CREATEDATE, CUSTNAME, DELIVERID, DEPOSITID, DEPOSITTYPEID, DESCRIPTION, IDNO, ISDN, PRIMARYACCOUNT, REASONID, RECEIPTID, REQUESTID, SHOPID, STAFFID, STATUS, STOCKTRANSID, SUBID, TELECOMSERVICEID, TIN, TYPE, EXCLUSE_ID_LIST};
    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "DEPOSIT_ID")
    @SequenceGenerator(name = "DEPOSIT_ID_GENERATOR", sequenceName = "DEPOSIT_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DEPOSIT_ID_GENERATOR")
    private Long depositId;
    @Basic(optional = false)
    @Column(name = "AMOUNT")
    private Long amount;
    @Column(name = "RECEIPT_ID")
    private Long receiptId;
    @Basic(optional = false)
    @Column(name = "TYPE")
    private String type;
    @Column(name = "DEPOSIT_TYPE_ID")
    private Long depositTypeId;
    @Column(name = "REASON_ID")
    private Long reasonId;
    @Basic(optional = false)
    @Column(name = "SHOP_ID")
    private Long shopId;
    @Basic(optional = false)
    @Column(name = "STAFF_ID")
    private Long staffId;
    @Column(name = "DELIVER_ID")
    private Long deliverId;
    @Column(name = "SUB_ID")
    private Long subId;
    @Column(name = "ISDN")
    private String isdn;
    @Column(name = "CUST_NAME")
    private String custName;
    @Column(name = "ADDRESS")
    private String address;
    @Column(name = "TIN")
    private String tin;
    @Basic(optional = false)
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Basic(optional = false)
    @Column(name = "STATUS")
    private String status;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "CREATE_BY")
    private String createBy;
    @Column(name = "TELECOM_SERVICE_ID")
    private Long telecomServiceId;
    @Column(name = "STOCK_TRANS_ID")
    private Long stockTransId;
    @Column(name = "ID_NO")
    private String idNo;
    @Column(name = "BRANCH_ID")
    private String branchId;
    @Column(name = "REQUEST_ID")
    private String requestId;
    @Column(name = "CONTRACT_NO")
    private String contractNo;
    @Column(name = "PRIMARY_ACCOUNT")
    private String primaryAccount;

    public Deposit() {
    }

    public Deposit(Long depositId) {
        this.depositId = depositId;
    }

    public Deposit(Long depositId, Long amount, String type, Long shopId, Long staffId, Date createDate, String status) {
        this.depositId = depositId;
        this.amount = amount;
        this.type = type;
        this.shopId = shopId;
        this.staffId = staffId;
        this.createDate = createDate;
        this.status = status;
    }

    public Long getDepositId() {
        return depositId;
    }

    public void setDepositId(Long depositId) {
        this.depositId = depositId;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Long getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(Long receiptId) {
        this.receiptId = receiptId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getDepositTypeId() {
        return depositTypeId;
    }

    public void setDepositTypeId(Long depositTypeId) {
        this.depositTypeId = depositTypeId;
    }

    public Long getReasonId() {
        return reasonId;
    }

    public void setReasonId(Long reasonId) {
        this.reasonId = reasonId;
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

    public Long getDeliverId() {
        return deliverId;
    }

    public void setDeliverId(Long deliverId) {
        this.deliverId = deliverId;
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

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Long getTelecomServiceId() {
        return telecomServiceId;
    }

    public void setTelecomServiceId(Long telecomServiceId) {
        this.telecomServiceId = telecomServiceId;
    }

    public Long getStockTransId() {
        return stockTransId;
    }

    public void setStockTransId(Long stockTransId) {
        this.stockTransId = stockTransId;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public String getPrimaryAccount() {
        return primaryAccount;
    }

    public void setPrimaryAccount(String primaryAccount) {
        this.primaryAccount = primaryAccount;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (depositId != null ? depositId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Deposit)) {
            return false;
        }
        Deposit other = (Deposit) object;
        if ((this.depositId == null && other.depositId != null) || (this.depositId != null && !this.depositId.equals(other.depositId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.bccs.inventory.model.Deposit[ depositId=" + depositId + " ]";
    }

}
