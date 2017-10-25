package com.viettel.bccs.inventory.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * @author thanhnt77
 */
public class LookupSerialByFileDTO implements Serializable {

    private String serial;
    private String prodOfferCode;
    private Long prodOfferId;
    private String prodOfferName;
    private Long stateId;
    private String stateName;
    private String state;
    private Long ownerType;
    private String ownerTypeName;
    private Long ownerId;
    private String ownerCode; //co the la shopCode hoac staffCode
    private String ownerName;
    private Long status;
    private String shopImportName;
    private Long connectionStatus;
    private String connectionStatusName;
    private Long connectionType;
    private Date connectionDate;
    private String connectionDateStr;
    private String strStatus;
    private String strownerType;
    private String note;
    private String contractCode;
    private Date contractDate;
    private String poCode;
    private Date poDate;
    private Date requestImportDate;
    private Date importStockDate;
    private String contractDateStr;
    private String poDateStr;
    private String requestImportDateStr;
    private String importStockDateStr;
    private String serialGpon;

    public String getSerialGpon() {
        return serialGpon;
    }

    public void setSerialGpon(String serialGpon) {
        this.serialGpon = serialGpon;
    }

    public LookupSerialByFileDTO() {
    }

    public int compareTo(LookupSerialByFileDTO o) {
        if (!this.ownerType.equals(o.ownerType)) {
            return this.ownerType.compareTo(o.ownerType);
        }
        if (!this.ownerId.equals(o.ownerId)) {
            return this.ownerId.compareTo(o.ownerId);
        }
        if (!this.prodOfferCode.equals(o.prodOfferCode)) {
            return this.prodOfferCode.compareTo(o.prodOfferCode);
        }
        if (!this.stateId.equals(o.stateId)) {
            return this.stateId.compareTo(o.stateId);
        }
        if (!this.serial.equals(o.serial)) {
            return this.serial.compareTo(o.serial);
        }
        return 0;

    }

    @Override
    public boolean equals(Object obj1) {
        if (obj1 == null)
            return false;
        if (this.getClass() != obj1.getClass())
            return false;
        LookupSerialByFileDTO obj = (LookupSerialByFileDTO) obj1;
        return (this.ownerType.equals(obj.ownerType) && this.ownerId.equals(obj.ownerId)
                && this.prodOfferCode.equals(obj.prodOfferCode) && this.stateId.equals(obj.stateId) && this.serial.equals(obj.serial));
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public LookupSerialByFileDTO(String serial, String stockModelCode, String stockModelName, Long stateId,
                                 Long ownerType, Long ownerId, String ownerCode, String ownerName, Long status, String shopImportName) {
        this.serial = serial;
        this.prodOfferCode = stockModelCode;
        this.prodOfferName = stockModelName;
        this.stateId = stateId;
        this.ownerType = ownerType;
        this.ownerId = ownerId;
        this.ownerCode = ownerCode;
        this.ownerName = ownerName;
        this.status = status;
        this.shopImportName = shopImportName;
    }

    public LookupSerialByFileDTO(String serial, String stockModelCode, String stockModelName, Long stateId,
                                 Long ownerType, Long ownerId, String ownerCode, String ownerName, Long status, String shopImportName, Long connectionStatus, Long connectionType, Date connectionDate) {
        this.serial = serial;
        this.prodOfferCode = stockModelCode;
        this.prodOfferName = stockModelName;
        this.stateId = stateId;
        if (stateId != null && stateId == 1L) {
            this.state = "Hàng mới";
        } else if (stateId != null && stateId == 3L) {
            this.state = "Hàng hỏng";
        } else if (stateId != null && stateId == 2L) {
            this.state = "Hàng thu hồi";
        } // 
        if (status != null && status == 1L) {
            this.strStatus = "Trong kho";
        }
        if (status != null && status == 0L) {
            this.strStatus = "Đã bán";

        }
        if (status != null && status == 3L) {
            this.strStatus = "Chờ xác nhận nhập";

        }
        if (status != null && status == 5L) {
            this.strStatus = "Đã hủy";

        }
        //LeVT1 R_SCD
        if (status != null && status == 8L) {
            this.strStatus = "Cho thuê";

        }
        if (status != null && status == 9L) {
            this.strStatus = "Cho mượn";

        }

        if (ownerType == 1L && ownerType != null) {
            this.strownerType = "Cửa hàng";
        }
        if (ownerType == 2L && ownerType != null) {
            this.strownerType = "Nhân viên";
        }
        this.ownerType = ownerType;
        this.ownerId = ownerId;
        this.ownerCode = ownerCode;
        this.ownerName = ownerName;
        this.status = status;
        this.shopImportName = shopImportName;
        this.connectionType = connectionType;
        this.connectionStatus = connectionStatus;
        this.connectionDate = connectionDate;
    }

    public LookupSerialByFileDTO(String serial, String stockModelCode, String stockModelName, Long stateId,
                                 Long ownerType, Long ownerId, String ownerCode, String ownerName, Long status, String shopImportName, Long connectionStatus, Long connectionType, Date connectionDate, String note) {
        this.serial = serial;
        this.prodOfferCode = stockModelCode;
        this.prodOfferName = stockModelName;
        this.stateId = stateId;
        if (stateId != null && stateId == 1L) {
            this.state = "Hàng mới";
        } else if (stateId != null && stateId == 3L) {
            this.state = "Hàng hỏng";
        } else if (stateId != null && stateId == 2L) {
            this.state = "Hàng thu hồi";
        } // LeVT1 R_SCD

        this.ownerType = ownerType;
        this.ownerId = ownerId;
        this.ownerCode = ownerCode;
        this.ownerName = ownerName;
        this.status = status;
        if (status != null && status == 1L) {
            this.strStatus = "Trong kho";
        }
        if (status != null && status == 0L) {
            this.strStatus = "Đã bán";

        }
        if (status != null && status == 3L) {
            this.strStatus = "Chờ xác nhận nhập";

        }
        if (status != null && status == 5L) {
            this.strStatus = "Đã hủy";

        }
        //LeVT1 R_SCD
        if (status != null && status == 8L) {
            this.strStatus = "Cho thuê";

        }
        if (status != null && status == 9L) {
            this.strStatus = "Cho mượn";

        }

        this.strownerType = strownerType;

        this.shopImportName = shopImportName;
        this.connectionType = connectionType;
        this.connectionStatus = connectionStatus;
        this.connectionDate = connectionDate;
        this.note = note;
    }

    public LookupSerialByFileDTO(String serial, String stockModelCode, String stockModelName, Long stateId,
                                 Long ownerType, Long ownerId, String ownerCode, String ownerName, Long status) {
        this.serial = serial;
        this.prodOfferCode = stockModelCode;
        this.prodOfferName = stockModelName;
        this.stateId = stateId;
        this.ownerType = ownerType;
        this.ownerId = ownerId;
        this.ownerCode = ownerCode;
        this.ownerName = ownerName;
        this.status = status;

    }

    public String getContractDateStr() {
        return contractDateStr;
    }

    public void setContractDateStr(String contractDateStr) {
        this.contractDateStr = contractDateStr;
    }

    public String getImportStockDateStr() {
        return importStockDateStr;
    }

    public void setImportStockDateStr(String importStockDateStr) {
        this.importStockDateStr = importStockDateStr;
    }

    public String getPoDateStr() {
        return poDateStr;
    }

    public void setPoDateStr(String poDateStr) {
        this.poDateStr = poDateStr;
    }

    public String getRequestImportDateStr() {
        return requestImportDateStr;
    }

    public void setRequestImportDateStr(String requestImportDateStr) {
        this.requestImportDateStr = requestImportDateStr;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public Date getContractDate() {
        return contractDate;
    }

    public void setContractDate(Date contractDate) {
        this.contractDate = contractDate;
    }

    public Date getImportStockDate() {
        return importStockDate;
    }

    public void setImportStockDate(Date importStockDate) {
        this.importStockDate = importStockDate;
    }

    public String getPoCode() {
        return poCode;
    }

    public void setPoCode(String poCode) {
        this.poCode = poCode;
    }

    public Date getPoDate() {
        return poDate;
    }

    public void setPoDate(Date poDate) {
        this.poDate = poDate;
    }

    public Date getRequestImportDate() {
        return requestImportDate;
    }

    public void setRequestImportDate(Date requestImportDate) {
        this.requestImportDate = requestImportDate;
    }

    public String getStrownerType() {
        return strownerType;
    }

    public void setStrownerType(String strownerType) {
        this.strownerType = strownerType;
    }

    public String getStrStatus() {
        return strStatus;
    }

    public void setStrStatus(String strStatus) {
        this.strStatus = strStatus;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Date getConnectionDate() {
        return connectionDate;
    }

    public void setConnectionDate(Date connectionDate) {
        this.connectionDate = connectionDate;
    }

    public Long getConnectionStatus() {
        return connectionStatus;
    }

    public void setConnectionStatus(Long connectionStatus) {
        this.connectionStatus = connectionStatus;
    }

    public Long getConnectionType() {
        return connectionType;
    }

    public void setConnectionType(Long connectionType) {
        this.connectionType = connectionType;
    }

    public String getProdOfferCode() {
        return prodOfferCode;
    }

    public void setProdOfferCode(String prodOfferCode) {
        this.prodOfferCode = prodOfferCode;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Long getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(Long ownerType) {
        this.ownerType = ownerType;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public Long getStateId() {
        return stateId;
    }

    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }

    public String getProdOfferName() {
        return prodOfferName;
    }

    public void setProdOfferName(String prodOfferName) {
        this.prodOfferName = prodOfferName;
    }

    public String getOwnerCode() {
        return ownerCode;
    }

    public void setOwnerCode(String ownerCode) {
        this.ownerCode = ownerCode;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public String getShopImportName() {
        return shopImportName;
    }

    public void setShopImportName(String shopImportName) {
        this.shopImportName = shopImportName;
    }

    public String getConnectionDateStr() {
        return connectionDateStr;
    }

    public void setConnectionDateStr(String connectionDateStr) {
        this.connectionDateStr = connectionDateStr;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getOwnerTypeName() {
        return ownerTypeName;
    }

    public void setOwnerTypeName(String ownerTypeName) {
        this.ownerTypeName = ownerTypeName;
    }

    public String getConnectionStatusName() {
        return connectionStatusName;
    }

    public void setConnectionStatusName(String connectionStatusName) {
        this.connectionStatusName = connectionStatusName;
    }

    public Long getProdOfferId() {
        return prodOfferId;
    }

    public void setProdOfferId(Long prodOfferId) {
        this.prodOfferId = prodOfferId;
    }
}
