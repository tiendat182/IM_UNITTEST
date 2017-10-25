package com.viettel.bccs.inventory.model;

import java.sql.Time;
import java.util.Date;

import javax.persistence.*;

/**
 * Created by vanho on 29/05/2017.
 */
@Entity
@Table(name = "IMSI_MADE", schema = "BCCS_IM", catalog = "")
public class ImsiMade {
	public static enum COLUMNS {CONTENT, CREATEDATE, FROMIMSI, ID, OPKEY, PO, PRODOFFERID, QUANTITY, SERIALNO, SIMTYPE, TOIMSI, USERCREATE, EXCLUSE_ID_LIST};

    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    @SequenceGenerator(name = "IMSI_MADE_SEQ_GEN", sequenceName = "IMSI_MADE_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "IMSI_MADE_SEQ_GEN")
    private Long id;
    @Column(name = "FROM_IMSI")
    private String fromImsi;
    @Column(name = "TO_IMSI")
    private String toImsi;
    @Column(name = "QUANTITY")
    private Long quantity;
    @Column(name = "PO")
    private String po;
    @Column(name = "OP_KEY")
    private String opKey;
    @Column(name = "SIM_TYPE")
    private String simType;
    @Column(name = "PROD_OFFER_ID")
    private Long prodOfferId;
    @Column(name = "SERIAL_NO")
    private String serialNo;
    @Column(name = "USER_CREATE")
    private String userCreate;
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Lob
    @Column(name = "CONTENT")
    private byte[] content;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFromImsi() {
        return fromImsi;
    }

    public void setFromImsi(String fromImsi) {
        this.fromImsi = fromImsi;
    }

    public String getToImsi() {
        return toImsi;
    }

    public void setToImsi(String toImsi) {
        this.toImsi = toImsi;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public String getPo() {
        return po;
    }

    public void setPo(String po) {
        this.po = po;
    }

    public String getOpKey() {
        return opKey;
    }

    public void setOpKey(String opKey) {
        this.opKey = opKey;
    }

    public String getSimType() {
        return simType;
    }

    public void setSimType(String simType) {
        this.simType = simType;
    }

    public Long getProdOfferId() {
        return prodOfferId;
    }

    public void setProdOfferId(Long prodOfferId) {
        this.prodOfferId = prodOfferId;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getUserCreate() {
        return userCreate;
    }

    public void setUserCreate(String userCreate) {
        this.userCreate = userCreate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ImsiMade imsiMade = (ImsiMade) o;

        if (id != imsiMade.id) return false;
        if (fromImsi != null ? !fromImsi.equals(imsiMade.fromImsi) : imsiMade.fromImsi != null) return false;
        if (toImsi != null ? !toImsi.equals(imsiMade.toImsi) : imsiMade.toImsi != null) return false;
        if (quantity != null ? !quantity.equals(imsiMade.quantity) : imsiMade.quantity != null) return false;
        if (po != null ? !po.equals(imsiMade.po) : imsiMade.po != null) return false;
        if (opKey != null ? !opKey.equals(imsiMade.opKey) : imsiMade.opKey != null) return false;
        if (simType != null ? !simType.equals(imsiMade.simType) : imsiMade.simType != null) return false;
        if (prodOfferId != null ? !prodOfferId.equals(imsiMade.prodOfferId) : imsiMade.prodOfferId != null)
            return false;
        if (serialNo != null ? !serialNo.equals(imsiMade.serialNo) : imsiMade.serialNo != null) return false;
        if (userCreate != null ? !userCreate.equals(imsiMade.userCreate) : imsiMade.userCreate != null) return false;
        if (createDate != null ? !createDate.equals(imsiMade.createDate) : imsiMade.createDate != null) return false;
        if (content != null ? !content.equals(imsiMade.content) : imsiMade.content != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = 0;
        result = 31 * result + (fromImsi != null ? fromImsi.hashCode() : 0);
        result = 31 * result + (toImsi != null ? toImsi.hashCode() : 0);
        result = 31 * result + (quantity != null ? quantity.hashCode() : 0);
        result = 31 * result + (po != null ? po.hashCode() : 0);
        result = 31 * result + (opKey != null ? opKey.hashCode() : 0);
        result = 31 * result + (simType != null ? simType.hashCode() : 0);
        result = 31 * result + (prodOfferId != null ? prodOfferId.hashCode() : 0);
        result = 31 * result + (serialNo != null ? serialNo.hashCode() : 0);
        result = 31 * result + (userCreate != null ? userCreate.hashCode() : 0);
        result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
        result = 31 * result + (content != null ? content.hashCode() : 0);
        return result;
    }
}
