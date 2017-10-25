package com.viettel.bccs.inventory.dto;
import java.lang.Long;
import java.util.Date;
import com.viettel.fw.dto.BaseDTO;
import java.io.Serializable;
public class ChangeModelTransSerialDTO extends BaseDTO implements Serializable {
public String getKeySet() {
 return keySet; }
    private Long changeModelTransDetailId;
    private Long changeModelTransSerialId;
    private Date createDate;
    private String fromSerial;
    private Long quantity;
    private String toSerial;
    public Long getChangeModelTransDetailId() {
        return this.changeModelTransDetailId;
    }
    public void setChangeModelTransDetailId(Long changeModelTransDetailId) {
        this.changeModelTransDetailId = changeModelTransDetailId;
    }
    public Long getChangeModelTransSerialId() {
        return this.changeModelTransSerialId;
    }
    public void setChangeModelTransSerialId(Long changeModelTransSerialId) {
        this.changeModelTransSerialId = changeModelTransSerialId;
    }
    public Date getCreateDate() {
        return this.createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public String getFromSerial() {
        return this.fromSerial;
    }
    public void setFromSerial(String fromSerial) {
        this.fromSerial = fromSerial;
    }
    public Long getQuantity() {
        return this.quantity;
    }
    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
    public String getToSerial() {
        return this.toSerial;
    }
    public void setToSerial(String toSerial) {
        this.toSerial = toSerial;
    }
}
