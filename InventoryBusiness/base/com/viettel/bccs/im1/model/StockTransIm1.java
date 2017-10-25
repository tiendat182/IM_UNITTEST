package com.viettel.bccs.im1.model;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 * @author hoangnt14
 */
@Entity
@Table(name = "STOCK_TRANS", schema = "BCCS_IM")
@XmlRootElement
public class StockTransIm1 implements Serializable {
public static enum COLUMNS {ACCTYPE, ACCOUNT, APPROVEDATE, APPROVEREASONID, APPROVEUSERID, BANKPLUSSTATUS, CHECKERP, CREATEDATETIME, DEPOSITSTATUS, DRAWSTATUS, FROMOWNERID, FROMOWNERTYPE, FROMSTOCKTRANSID, GOODSWEIGHT, ISAUTOGEN, NOTE, PARTNERCONTRACTNO, PAYSTATUS, PROCESSEND, PROCESSOFFLINE, PROCESSRESULT, PROCESSSTART, REALTRANSDATE, REALTRANSUSERID, REASONID, REGIONSTOCKTRANSID, REJECTDATE, REJECTNOTE, REJECTREASONID, REJECTUSERID, REQUESTID, STOCKBASE, STOCKTRANSID, STOCKTRANSSTATUS, STOCKTRANSTYPE, SYNSTATUS, TOOWNERID, TOOWNERTYPE, TOTALDEBIT, TRANSPORT, TROUBLETYPE, VALID, WARRANTYSTOCK, EXCLUSE_ID_LIST};

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "STOCK_TRANS_ID")
    @SequenceGenerator(name = "STOCK_TRANS_IM1_ID_GENERATOR", sequenceName = "BCCS_IM.STOCK_TRANS_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STOCK_TRANS_IM1_ID_GENERATOR")
    private Long stockTransId;
    @Column(name = "FROM_OWNER_ID")
    private Long fromOwnerId;
    @Column(name = "FROM_OWNER_TYPE")
    private Long fromOwnerType;
    @Column(name = "TO_OWNER_ID")
    private Long toOwnerId;
    @Column(name = "TO_OWNER_TYPE")
    private Long toOwnerType;
    @Column(name = "CREATE_DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDatetime;
    @Basic(optional = false)
    @Column(name = "STOCK_TRANS_TYPE")
    private Long stockTransType;
    @Column(name = "REASON_ID")
    private Long reasonId;
    @Column(name = "STOCK_TRANS_STATUS")
    private Long stockTransStatus;
    @Column(name = "PAY_STATUS")
    private Long payStatus;
    @Column(name = "DEPOSIT_STATUS")
    private Long depositStatus;
    @Column(name = "DRAW_STATUS")
    private Long drawStatus;
    @Column(name = "REAL_TRANS_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date realTransDate;
    @Column(name = "REAL_TRANS_USER_ID")
    private Long realTransUserId;
    @Column(name = "FROM_STOCK_TRANS_ID")
    private Long fromStockTransId;
    @Column(name = "SYN_STATUS")
    private String synStatus;
    @Column(name = "NOTE")
    private String note;
    @Column(name = "STOCK_BASE")
    private String stockBase;
    @Column(name = "REJECT_USER_ID")
    private Long rejectUserId;
    @Column(name = "REJECT_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date rejectDate;
    @Column(name = "REJECT_REASON_ID")
    private Long approveReasonId;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "GOODS_WEIGHT")
    private Long goodsWeight;
    @Column(name = "REQUEST_ID")
    private String requestId;
    @Column(name = "CHECK_ERP")
    private Long checkErp;
    @Column(name = "TOTAL_DEBIT")
    private Long totalDebit;
    @Column(name = "PROCESS_OFFLINE")
    private Long processOffline;
    @Column(name = "PROCESS_START")
    @Temporal(TemporalType.TIMESTAMP)
    private Date processStart;
    @Column(name = "PROCESS_END")
    @Temporal(TemporalType.TIMESTAMP)
    private Date processEnd;
    @Column(name = "PROCESS_RESULT")
    private String processResult;
    @Column(name = "REGION_STOCK_TRANS_ID")
    private Long regionStockTransId;
    @Column(name = "IS_AUTO_GEN")
    private Long isAutoGen;
    @Column(name = "WARRANTY_STOCK")
    private Long warrantyStock;
    @Column(name = "TRANSPORT")
    private Long transport;
    @Column(name = "ACC_TYPE")
    private Long accType;
    @Column(name = "ACCOUNT")
    private String account;
    @Column(name = "VALID")
    private Long valid;
    @Column(name = "BANKPLUS_STATUS")
    private Long bankplusStatus;
    @Column(name = "TROUBLE_TYPE")
    private String troubleType;

    public StockTransIm1() {
    }

    public StockTransIm1(Long stockTransId) {
        this.stockTransId = stockTransId;
    }

    public StockTransIm1(Long stockTransId, Long stockTransType) {
        this.stockTransId = stockTransId;
        this.stockTransType = stockTransType;
    }

    public Long getStockTransId() {
        return stockTransId;
    }

    public void setStockTransId(Long stockTransId) {
        this.stockTransId = stockTransId;
    }

    public Long getFromOwnerId() {
        return fromOwnerId;
    }

    public void setFromOwnerId(Long fromOwnerId) {
        this.fromOwnerId = fromOwnerId;
    }

    public Long getFromOwnerType() {
        return fromOwnerType;
    }

    public void setFromOwnerType(Long fromOwnerType) {
        this.fromOwnerType = fromOwnerType;
    }

    public Long getToOwnerId() {
        return toOwnerId;
    }

    public void setToOwnerId(Long toOwnerId) {
        this.toOwnerId = toOwnerId;
    }

    public Long getToOwnerType() {
        return toOwnerType;
    }

    public void setToOwnerType(Long toOwnerType) {
        this.toOwnerType = toOwnerType;
    }

    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public Long getStockTransType() {
        return stockTransType;
    }

    public void setStockTransType(Long stockTransType) {
        this.stockTransType = stockTransType;
    }

    public Long getReasonId() {
        return reasonId;
    }

    public void setReasonId(Long reasonId) {
        this.reasonId = reasonId;
    }

    public Long getStockTransStatus() {
        return stockTransStatus;
    }

    public void setStockTransStatus(Long stockTransStatus) {
        this.stockTransStatus = stockTransStatus;
    }

    public Long getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Long payStatus) {
        this.payStatus = payStatus;
    }

    public Long getDepositStatus() {
        return depositStatus;
    }

    public void setDepositStatus(Long depositStatus) {
        this.depositStatus = depositStatus;
    }

    public Long getDrawStatus() {
        return drawStatus;
    }

    public void setDrawStatus(Long drawStatus) {
        this.drawStatus = drawStatus;
    }

    public Date getRealTransDate() {
        return realTransDate;
    }

    public void setRealTransDate(Date realTransDate) {
        this.realTransDate = realTransDate;
    }

    public Long getRealTransUserId() {
        return realTransUserId;
    }

    public void setRealTransUserId(Long realTransUserId) {
        this.realTransUserId = realTransUserId;
    }

    public Long getFromStockTransId() {
        return fromStockTransId;
    }

    public void setFromStockTransId(Long fromStockTransId) {
        this.fromStockTransId = fromStockTransId;
    }

    public String getSynStatus() {
        return synStatus;
    }

    public void setSynStatus(String synStatus) {
        this.synStatus = synStatus;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getStockBase() {
        return stockBase;
    }

    public void setStockBase(String stockBase) {
        this.stockBase = stockBase;
    }

    public Long getRejectUserId() {
        return rejectUserId;
    }

    public void setRejectUserId(Long rejectUserId) {
        this.rejectUserId = rejectUserId;
    }

    public Date getRejectDate() {
        return rejectDate;
    }

    public void setRejectDate(Date rejectDate) {
        this.rejectDate = rejectDate;
    }


    public Long getApproveReasonId() {
        return approveReasonId;
    }

    public void setApproveReasonId(Long approveReasonId) {
        this.approveReasonId = approveReasonId;
    }

    public Long getGoodsWeight() {
        return goodsWeight;
    }

    public void setGoodsWeight(Long goodsWeight) {
        this.goodsWeight = goodsWeight;
    }


    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Long getCheckErp() {
        return checkErp;
    }

    public void setCheckErp(Long checkErp) {
        this.checkErp = checkErp;
    }

    public Long getTotalDebit() {
        return totalDebit;
    }

    public void setTotalDebit(Long totalDebit) {
        this.totalDebit = totalDebit;
    }

    public Long getProcessOffline() {
        return processOffline;
    }

    public void setProcessOffline(Long processOffline) {
        this.processOffline = processOffline;
    }

    public Date getProcessStart() {
        return processStart;
    }

    public void setProcessStart(Date processStart) {
        this.processStart = processStart;
    }

    public Date getProcessEnd() {
        return processEnd;
    }

    public void setProcessEnd(Date processEnd) {
        this.processEnd = processEnd;
    }

    public String getProcessResult() {
        return processResult;
    }

    public void setProcessResult(String processResult) {
        this.processResult = processResult;
    }

    public Long getRegionStockTransId() {
        return regionStockTransId;
    }

    public void setRegionStockTransId(Long regionStockTransId) {
        this.regionStockTransId = regionStockTransId;
    }

    public Long getIsAutoGen() {
        return isAutoGen;
    }

    public void setIsAutoGen(Long isAutoGen) {
        this.isAutoGen = isAutoGen;
    }

    public Long getWarrantyStock() {
        return warrantyStock;
    }

    public void setWarrantyStock(Long warrantyStock) {
        this.warrantyStock = warrantyStock;
    }

    public Long getTransport() {
        return transport;
    }

    public void setTransport(Long transport) {
        this.transport = transport;
    }

    public Long getAccType() {
        return accType;
    }

    public void setAccType(Long accType) {
        this.accType = accType;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Long getValid() {
        return valid;
    }

    public void setValid(Long valid) {
        this.valid = valid;
    }

    public Long getBankplusStatus() {
        return bankplusStatus;
    }

    public void setBankplusStatus(Long bankplusStatus) {
        this.bankplusStatus = bankplusStatus;
    }

    public String getTroubleType() {
        return troubleType;
    }

    public void setTroubleType(String troubleType) {
        this.troubleType = troubleType;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (stockTransId != null ? stockTransId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StockTransIm1)) {
            return false;
        }
        StockTransIm1 other = (StockTransIm1) object;
        if ((this.stockTransId == null && other.stockTransId != null) || (this.stockTransId != null && !this.stockTransId.equals(other.stockTransId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bai1.StockTrans[ stockTransId=" + stockTransId + " ]";
    }

}

