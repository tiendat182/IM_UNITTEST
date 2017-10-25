package com.viettel.bccs.inventory.tag;

import com.viettel.bccs.inventory.dto.*;
import org.springframework.security.access.annotation.Secured;

import java.util.List;

/**
 * interface ta thong tin lenh xuat
 *
 * @author ThanhNT77
 */
public interface OrderStockTagNameable {

    /**
     * ham init cua orderStock tag
     *
     * @param objectController
     * @param writeOffice
     * @author ThanhNT77
     */
    @Secured("@")
    public void init(Object objectController, Boolean writeOffice);

    @Secured("@")
    public void initAgent(Object objectController, Boolean writeOffice);

    @Secured("@")
    public void initCollaborator(Object objectController);

    @Secured("@")
    public void initCollaboratorRetrieve(Object objectController);

    @Secured("@")
    public void resetOrderStock();

    /**
     * ham load data cu cua tag phieu xuat, dau vao phai la doi tuong input
     *
     * @param stockTransActionId
     * @author ThanhNT77
     */
    @Secured("@")
    public void loadOrderStock(Long stockTransActionId, boolean isDisableEdit);

    /**
     * ham load data cu cua tag phieu nhap, dau vao phai la doi tuong input
     *
     * @param stockTransActionId
     * @author Thaont19
     */
    @Secured("@")
    public void loadOrderStockImport(Long stockTransActionId, boolean isDisableEdit);

    /**
     * ham xu ly click checkbox ky office
     *
     * @param methodName ten method
     * @author ThanhNT77
     */
    @Secured("@")
    public void doChangeWriteOffice(String methodName);

    /**
     * ham xu ly click checkbox ky office
     *
     * @param methodName ten method
     * @author ThanhNT77
     */
    @Secured("@")
    public void doChangeAutoCreateNote(String methodName);

    /**
     * ham nhan data cua tag kho xuat hang
     *
     * @param vShopStaffDTO
     * @author ThanhNT77
     */
    @Secured("@")
    public void receiveShop(VShopStaffDTO vShopStaffDTO, String methodNameReceiveShop);

    /**
     * ham xu ly thay doi gia tri kho 3 mien
     * @author ThanhNT77
     * @param methodThreeStock
     */
    @Secured("@")
    public void doChangeThreeStock(String methodThreeStock);

    @Secured("@")
    public void clearCurrentShop(String methodName);

    /**
     * ham nhan data cua tag kho nhan hang
     *
     * @param vShopStaffDTO
     */
    @Secured("@")
    public void exportShop(VShopStaffDTO vShopStaffDTO);

    /**
     * ham tra ve dto StockTransActionDTO tu tag
     *
     * @return
     * @author ThanhNT77
     */
    public StockTransActionDTO getStockTransActionDTO();

    /**
     * ham tra ve dto StockTransDTO tu tag
     *
     * @return
     * @author ThanhNT77
     */
    public StockTransDTO getStockTransDTO();

    /**
     * xu ly get data stocktrans dug de in phieu
     * @author ThanhNT
     * @return
     */
    public StockTransDTO getStockTransDTOForPint();

    /**
     * dieu kien thi kho 3 mien
     *
     * @return
     * @author ThanhNT77
     */
    public Boolean getShowThreeStock();

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
     * @author ThanhNT77
     */
    public Boolean getShowLogistic();

    /**
     * check quyen hien thi checkbox van chuyen
     *
     * @return
     * @author ThanhNT77
     */
    public Boolean getShowTransfer();

    /**
     * ham check hien thi check box lap phieu tu dong
     * @author thanhnt77
     * @return
     */
    public Boolean getShowAutoOrderNote();

    /**
     * ham check dieu kien hien thi kho
     *
     * @return
     * @author ThanhNT77
     */
    public boolean getShowStockVTT();

    /**
     * ham set name cho kho 3 mien
     * typeReion = 1 : xuat cho cap duoi, typeRegion = 2 xuat cho cap tren
     * @author ThanhNT77
     * @param typeRegion
     */
    public void setNameThreeRegion(String typeRegion);

    //getter and setter
    public Boolean getWriteOffice();

    public void setWriteOffice(Boolean writeOffice);

    public Object getObjectController();

    public void setObjectController(Object objectController);

    public StockTransInputDTO getTransInputDTO();

    public void setTransInputDTO(StockTransInputDTO transInputDTO);

    public VShopStaffDTO getvShopStaffDTO();

    public void setvShopStaffDTO(VShopStaffDTO vShopStaffDTO);

    public ShopInfoNameable getShopInfoReceiveTag();

    public void setShopInfoReceiveTag(ShopInfoNameable shopInfoReceiveTag);

    public ShopInfoNameable getShopInfoExportTag();

    public void setShopInfoExportTag(ShopInfoNameable shopInfoExportTag);

    public List<ReasonDTO> getLsReasonDto();

    public void setLsReasonDto(List<ReasonDTO> lsReasonDto);

    public Boolean getTranLogistics();

    public void setTranLogistics(Boolean tranLogistics);

    public Boolean getTranfer();

    public void setTranfer(Boolean tranfer);

    public int getLimitAutoComplete();

    public void setLimitAutoComplete(int limitAutoComplete);

    public List<ShopDTO> getLsRegionShop();

    public void setLsRegionShop(List<ShopDTO> lsRegionShop);

    public Boolean getIsDisableEdit();

    public void setIsDisableEdit(Boolean isDisableEdit);

    public ShopInfoNameable getShopInfoExportTagIsdn();

    public void setShopInfoExportTagIsdn(ShopInfoNameable shopInfoExportTagIsdn);

    public List<ChannelTypeDTO> getLsChannelTypeDto();

    public void setLsChannelTypeDto(List<ChannelTypeDTO> lsChannelTypeDto);

    public List<OptionSetValueDTO> getLsPayMethod();

    public void setLsPayMethod(List<OptionSetValueDTO> lsPayMethod);

    public List<ProgramSaleDTO> getLsProgramSaleDto();

    public void setLsProgramSaleDto(List<ProgramSaleDTO> lsProgramSaleDto);

    public void changeChannelType();

    public void clearExportShop();

    public void clearReceiveShop();

    public Boolean getAutoCreateNote();

    public void setAutoCreateNote(Boolean autoCreateNote);

    public List<OptionSetValueDTO> getOptionSetValueDTOTransports();

    public void setOptionSetValueDTOTransports(List<OptionSetValueDTO> optionSetValueDTOTransports);

    public String getTransportType();

    //OUTSOURCE
    public String getTransportName();

    public void setTransportType(String transportType);

    public boolean getEnableTransfer();

    public void setEnableTransfer(boolean enableTransfer);
}
