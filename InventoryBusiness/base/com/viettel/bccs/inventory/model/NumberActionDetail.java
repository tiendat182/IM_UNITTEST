/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.bccs.inventory.model;


import java.io.Serializable;
import javax.persistence.*;

/**
 *
 * @author tuydv1
 */
@Entity
@Table(name = "NUMBER_ACTION_DETAIL")
public class NumberActionDetail implements Serializable {
public static enum COLUMNS {COLUMNNAME, NEWDETAILVALUE, NEWVALUE, NUMBERACTIONDETAILID, NUMBERACTIONID, OLDDETAILVALUE, OLDVALUE, EXCLUSE_ID_LIST};
    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name = "NUMBER_ACTION_DETAIL_ID_GENERATOR", sequenceName = "NUMBER_ACTION_DETAIL_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NUMBER_ACTION_DETAIL_ID_GENERATOR")
    @Column(name = "NUMBER_ACTION_DETAIL_ID")
    private Long numberActionDetailId;
    @Column(name = "COLUMN_NAME")
    private String columnName;
    @Column(name = "OLD_VALUE")
    private String oldValue;
    @Column(name = "NEW_VALUE")
    private String newValue;
    @Column(name = "OLD_DETAIL_VALUE")
    private String oldDetailValue;
    @Column(name = "NEW_DETAIL_VALUE")
    private String newDetailValue;
    @Column(name = "NUMBER_ACTION_ID")
    private Long numberActionId;

    public NumberActionDetail() {
    }

    public NumberActionDetail(Long numberActionDetailId) {
        this.numberActionDetailId = numberActionDetailId;
    }

    public Long getNumberActionDetailId() {
        return numberActionDetailId;
    }

    public void setNumberActionDetailId(Long numberActionDetailId) {
        this.numberActionDetailId = numberActionDetailId;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public String getOldDetailValue() {
        return oldDetailValue;
    }

    public void setOldDetailValue(String oldDetailValue) {
        this.oldDetailValue = oldDetailValue;
    }

    public String getNewDetailValue() {
        return newDetailValue;
    }

    public void setNewDetailValue(String newDetailValue) {
        this.newDetailValue = newDetailValue;
    }

    public Long getNumberActionId() {
        return numberActionId;
    }

    public void setNumberActionId(Long numberActionId) {
        this.numberActionId = numberActionId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (numberActionDetailId != null ? numberActionDetailId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NumberActionDetail)) {
            return false;
        }
        NumberActionDetail other = (NumberActionDetail) object;
        if ((this.numberActionDetailId == null && other.numberActionDetailId != null) || (this.numberActionDetailId != null && !this.numberActionDetailId.equals(other.numberActionDetailId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "javaapplication2.NumberActionDetail[ numberActionDetailId=" + numberActionDetailId + " ]";
    }
    
}
