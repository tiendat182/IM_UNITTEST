package com.viettel.bccs.inventory.tag;

import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import org.springframework.security.access.annotation.Secured;

import java.util.List;

/**
 * interface ta thong tin lenh xuat
 *
 * @author TuanNM33
 */
public interface OrderStaffTagNameable {

    /**
     * ham init cua orderStock tag
     *
     * @param objectController
     * @param writeOffice
     * @author TuanNM33
     */
    @Secured("@")
    public void init(Object objectController, Boolean writeOffice);

    @Secured("@")
    public void resetOrderStaff();

    @Secured("@")
    public void exportShop(VShopStaffDTO vShopStaffDTO);

    @Secured("@")
    public void receiveStaff(VShopStaffDTO vShopStaffDTO, String methodNameReceiveStaff);

    public void clearCurrentStaff(String methodName);

    /**
     * ham xu ly click checkbox ky office
     *
     * @param methodName ten method
     * @author TuanNM33
     */
    @Secured("@")
    public void doChangeWriteOffice(String methodName);

    /**
     * check quyen hthi checkErp
     *
     * @return
     * @author ThanhNT77
     */
    public Boolean getShowCheckErp();

    /**
     * check quyen hien thi checkbox logictistic
     *
     * @return
     * @author TuanNM33
     */
    public Boolean getShowLogistic();

    /**
     * check quyen hien thi checkbox van chuyen
     *
     * @return
     * @author TuanNM33
     */
    public Boolean getShowTransfer();

    public Boolean getShowAutoOrderNote();

    /**
     * ham tra ve dto StockTransActionDTO tu tag
     *
     * @return
     * @author TuanNM33
     */
    public StockTransActionDTO getStockTransActionDTO();

    /**
     * ham tra ve dto StockTransDTO tu tag
     *
     * @return
     * @author TuanNM33
     */
    public StockTransDTO getStockTransDTO();

    public StockTransDTO getStockTransDTOForPint();

    public void doChangeAutoCreateNote(String methodName);

    /**
     * ham load data cu cua tag, dau vao phai la doi tuong input
     *
     * @param stockTransActionId
     * @author TuanNM33
     */
    public void loadOrderStaff(Long stockTransActionId, boolean isDisableEdit);


    // Getter and setter
    public Boolean getIsDisableEdit();

    public void setIsDisableEdit(Boolean isDisableEdit);

    public Boolean getWriteOffice();

    public void setWriteOffice(Boolean writeOffice);

    public StockTransInputDTO getTransInputDTO();

    public void setTransInputDTO(StockTransInputDTO transInputDTO);

    public ShopInfoNameable getShopInfoExportTag();

    public void setShopInfoExportTag(ShopInfoNameable shopInfoExportTag);

    public int getLimitAutoComplete();

    public void setLimitAutoComplete(int limitAutoComplete);

    public StaffInfoNameable getStaffInfoReceiveTag();

    public void setStaffInfoReceiveTag(StaffInfoNameable staffInfoReceiveTag);

    public VShopStaffDTO getvShopStaffDTO();

    public void setvShopStaffDTO(VShopStaffDTO vShopStaffDTO);

    public List<ReasonDTO> getLsReasonDto();

    public void setLsReasonDto(List<ReasonDTO> lsReasonDto);

    public Boolean getTranLogistics();

    public void setTranLogistics(Boolean tranLogistics);

    public Boolean getTranfer();

    public void setTranfer(Boolean tranfer);

    public Boolean getAutoCreateNote();

    public void setAutoCreateNote(Boolean autoCreateNote);
}
