/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.bccs.inventory.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author hoangnt14
 */
@Entity
@Table(name = "BRAS")
@NamedQueries({
    @NamedQuery(name = "Bras.findAll", query = "SELECT b FROM Bras b")})
public class Bras implements Serializable {
public static enum COLUMNS {AAAIP, BRASID, CODE, DESCRIPTION, EQUIPMENTID, IP, NAME, PORT, SLOT, STATUS, CREATEDATETIME, CREATEUSER,UPDATEDATETIME, UPDATEUSER, EXCLUSE_ID_LIST};
    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "BRAS_ID")
    @SequenceGenerator(name = "BRAS_ID_GENERATOR", sequenceName = "BRAS_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BRAS_ID_GENERATOR")
    private Long brasId;
    @Basic(optional = false)
    @Column(name = "EQUIPMENT_ID")
    private Long equipmentId;
    @Column(name = "CODE")
    private String code;
    @Column(name = "NAME")
    private String name;
    @Column(name = "IP")
    private String ip;
    @Column(name = "AAA_IP")
    private String aaaIp;
    @Column(name = "SLOT")
    private String slot;
    @Column(name = "PORT")
    private String port;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "CREATE_USER")
    private String createUser;
    @Basic(optional = false)
    @Column(name = "CREATE_DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDatetime;
    @Column(name = "UPDATE_USER")
    private String updateUser;
    @Column(name = "UPDATE_DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDatetime;

    public Bras() {
    }

    public Bras(Long brasId) {
        this.brasId = brasId;
    }

    public Bras(Long brasId, Long equipmentId) {
        this.brasId = brasId;
        this.equipmentId = equipmentId;
    }

    public Long getBrasId() {
        return brasId;
    }

    public void setBrasId(Long brasId) {
        this.brasId = brasId;
    }

    public Long getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(Long equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getAaaIp() {
        return aaaIp;
    }

    public void setAaaIp(String aaaIp) {
        this.aaaIp = aaaIp;
    }

    public String getSlot() {
        return slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
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

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public Date getUpdateDatetime() {
        return updateDatetime;
    }

    public void setUpdateDatetime(Date updateDatetime) {
        this.updateDatetime = updateDatetime;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (brasId != null ? brasId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Bras)) {
            return false;
        }
        Bras other = (Bras) object;
        if ((this.brasId == null && other.brasId != null) || (this.brasId != null && !this.brasId.equals(other.brasId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.bccs.inventory.model.Bras[ brasId=" + brasId + " ]";
    }
    
}
