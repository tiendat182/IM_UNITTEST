package com.viettel.bccs.inventory.tag;

import com.viettel.bccs.inventory.dto.VShopStaffDTO;
import com.viettel.fw.common.util.RequiredRoleMap;
import org.springframework.security.access.annotation.Secured;

import java.util.List;

/**
 * @author ThanhNT
 */
public interface ShopInfoNameable {

    /**
     * hien thi danh sanh shop tren man hinh
     *
     * @author ThanhNT
     */
    @Secured("@")
    public List<VShopStaffDTO> doSelectShop(String inputShop);

    /**
     * hàm init shop
     *
     * @param objectController
     * @param parentShopId
     * @author ThanhNT
     */
    @Secured("@")
    public void initShop(Object objectController, String parentShopId, boolean isCurrentAndChildShop);

    /**
     * ham init shop cho gom lenh van chuyen
     * @author thanhnt77
     * @param objectController
     * @param parentShopId
     * @param modeShop
     */
    @Secured("@")
    public void initShopTransfer(Object objectController, String parentShopId, String modeShop);

    @Secured("@")
    public void initShopBranchAndVTT(Object objectController, String parentShopId, String modeShop);

    /**
     * hàm init shop code
     *
     * @param objectController
     * @param lsShopCode
     * @author ThanhNT
     */
    @Secured("@")
    public void initShopByListShopCode(Object objectController, String modeShop, List<String> lsShopCode);

    /**
     * @param objectController
     * @param parentShopId
     * @desc hàm init shop bo qua loai shop
     * @author LuanNT23
     */
    @Secured("@")
    public void initShopAndAllChild(Object objectController, String parentShopId, boolean isDismissType, String ownerType);

    @Secured("@")
    public void initShopNotChanelTypeIdCurrentAndChild(Object objectController, String modeShop, String parentShopId,String ownerType, Long chanelTypeId);

    /**
     * @param objectController
     * @param parentShopId
     * @desc hàm init shop bo qua loai shop
     * @author SINHHV
     */
    @Secured("@")
    public void initShopAndAllChildWithChanelType(Object objectController, String parentShopId, List<Long> chanelType);

    @Secured("@")
    public void initShopMangeIsdnTrans(Object objectController, String currentShopId, String chanelType, RequiredRoleMap requiredRoleMap, String mode);

    /**
     * author HoangNT
     *
     * @param objectController
     */
    @Secured("@")
    public void initAllShop(Object objectController);

    /**
     * lay danh sach cac dai ly
     *
     * @param objectController
     * @param parentShopId
     */
    @Secured("@")
    public void initAgent(Object objectController, String parentShopId);

    /**
     * khoi tao NVDB/DB
     *
     * @param objectController
     * @param currentShopId
     * @param channelTypeId
     * @param staffOwnerId
     */
    @Secured("@")
    public void initCollAndPointOfSale(Object objectController, String currentShopId, List<Long> lstChannelType, String staffOwnerId);

    /**
     * author HoangNT
     * Ham lay tat ca cac child shop, ko lay chinh no
     *
     * @param objectController
     * @param parentShopId
     */
    @Secured("@")
    public void initAllChildNotCurrentShop(Object objectController, String parentShopId);

    /**
     * load shop
     *
     * @param shopId
     * @Author ThanhNT
     */
    @Secured("@")
    public void loadShop(String shopId, boolean disableEdit);

    /**
     * @author ThanhNT
     */
    @Secured("@")
    public void resetShop();

    /**
     * get data from method of object
     *
     * @return methodName tên method set data
     * @author ThanhNT
     */
    @Secured("@")
    public void doChangeShop(String methodName);

    @Secured("@")
    public void loadStaff(String shopId, boolean disableEdit);

    @Secured("@")
    public void doClearShop(String methodName);

    //getter and setter
    public List<VShopStaffDTO> getLsShopDto();

    public void setLsShopDto(List<VShopStaffDTO> lsShopDto);

    public VShopStaffDTO getvShopStaffDTO();

    public void setvShopStaffDTO(VShopStaffDTO vShopStaffDTO);

    public String getParentShopId();

    public void setParentShopId(String parentShopId);

    public boolean disabledState();

    public Boolean getIsDisableEdit();

    public void setIsDisableEdit(Boolean isDisableEdit);

    public void setChannelTypeId(Long channelTypeId);

    @Secured("@")
    public void initShopForIsdn(Object objectController, String parentShopId, boolean isCurrentAndChildShop, List<String> lstChanelType);

    /**
     * @param staffId
     * @author LuanNT23
     * @desc load kho nhan vien cho khi nhap serial trong nhân viên xuất trả hàng
     */
    public void loadStaffShop(Long staffId);

    public boolean isAgent();

    public void setIsAgent(boolean isAgent);

    public boolean isMultiTag();

    public void setMultiTag(boolean multiTag);

    public boolean isCollaborator();

    public void setIsCollaborator(boolean isCollaborator);

    public List<String> getLstShopCode();

    public void setLstShopCode(List<String> lstShopCode);
}

