package com.viettel.bccs.inventory.dto;

import java.io.Serializable;

/**
 * Created by DungPT16 on 4/12/2016.
 */
public class CardNumberLockDTO implements Serializable {
    private String dateBlock;
    //Ngày khóa
    private String userName;
    // Người khóa
    private String shopCode;
    // Cửa hàng
    private String status;
    //Trạng thái
    //1 Thành công
    //2 Không thành công
    private String description;
    //Lý do khóa


    public CardNumberLockDTO(String dateBlock, String userName, String shopCode, String status, String description) {
        this.dateBlock = dateBlock;
        this.userName = userName;
        this.shopCode = shopCode;
        this.status = status;
        this.description = description;
    }

    public String getDateBlock() {
        return dateBlock;
    }

    public void setDateBlock(String dateBlock) {
        this.dateBlock = dateBlock;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getShopCode() {
        return shopCode;
    }

    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
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

    public CardNumberLockDTO() {
        //CardNumberLockDTO
    }
}

