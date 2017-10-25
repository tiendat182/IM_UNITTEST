/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.bccs.inventory.model;


import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author tuydv1
 */
@Entity
@Table(name = "DIVIDE_SERIAL_ACTION")
@XmlRootElement
public class DivideSerialAction implements Serializable {
    public static enum COLUMNS {CREATEDATETIME, CREATEUSER, ID, SERIALCONTENT, STOCKTRANSACTIONID, EXCLUSE_ID_LIST}

    ;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    @SequenceGenerator(name = "DIVIDE_SERIAL_ACTION_ID_GENERATOR", sequenceName = "DIVIDE_SERIAL_ACTION_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DIVIDE_SERIAL_ACTION_ID_GENERATOR")
    private Long id;
    @Column(name = "STOCK_TRANS_ACTION_ID")
    private Long stockTransActionId;
    @Lob
    @Column(name = "SERIAL_CONTENT")
    private String serialContent;
    @Column(name = "CREATE_DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDatetime;
    @Column(name = "CREATE_USER")
    private String createUser;
    @Column(name = "FROM_SERIAL")
    private String fromSerial;
    @Column(name = "TO_SERIAL")
    private String toSerial;

    public DivideSerialAction() {
    }

    public DivideSerialAction(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStockTransActionId() {
        return stockTransActionId;
    }

    public void setStockTransActionId(Long stockTransActionId) {
        this.stockTransActionId = stockTransActionId;
    }

    public String getSerialContent() {
        return serialContent;
    }

    public void setSerialContent(String serialContent) {
        this.serialContent = serialContent;
    }

    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getFromSerial() {
        return fromSerial;
    }

    public void setFromSerial(String fromSerial) {
        this.fromSerial = fromSerial;
    }

    public String getToSerial() {
        return toSerial;
    }

    public void setToSerial(String toSerial) {
        this.toSerial = toSerial;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DivideSerialAction)) {
            return false;
        }
        DivideSerialAction other = (DivideSerialAction) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.bccs.inventory.model.DivideSerialAction[ id=" + id + " ]";
    }

}
