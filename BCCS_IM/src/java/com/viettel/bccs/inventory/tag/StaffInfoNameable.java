package com.viettel.bccs.inventory.tag;

import com.viettel.bccs.inventory.dto.ManageTransStockDto;
import com.viettel.bccs.inventory.dto.StaffTagConfigDTO;
import com.viettel.bccs.inventory.dto.VShopStaffDTO;
import org.springframework.security.access.annotation.Secured;

import java.util.List;

/**
 * @author ThanhNT
 */
public interface StaffInfoNameable {

    /**
     * hien thi danh sach goi cuoc theo thong tin nhap tren man hinh
     *
     * @param inputStaff giá trị input của gói cước
     * @return List<Product> danh sach sản phẩm
     */
    public List<VShopStaffDTO> doSelectStaff(String inputStaff) throws Exception;

    /**
     * hàm init Staff
     *
     * @author ThanhNT
     */
    public void initStaff(Object objectController, String shopId);

    /**
     * author TuanNM33
     *
     * @param objectController
     * @param shopId
     * @param staffId
     * @param isDisableEdit
     */
    public void initStaff(Object objectController, String shopId, String staffId, boolean isDisableEdit);

    public void initStaffManageIsdn(Object objectController, String shopId, String chanelType, String mode, ManageTransStockDto transStock);

    /**
     * @param objectController
     * @param shopId
     * @param chanelTypes
     * @author TuanNM33
     */
    public void initStaffWithChanelTypes(Object objectController, String shopId, String staffId, List<String> chanelTypes, boolean isDisableEdit);

    public void initStaffWithChanelTypesMode(Object objectController, String shopId, String staffId, List<String> chanelTypes, boolean isDisableEdit, String mode);

    public void initStaffWithChanelTypesAndParrentShop(Object objectController, String shopId, String staffId, List<String> chanelTypes, boolean isDisableEdit);

    /**
     * load Staff
     *
     * @param vShopStaffDTO
     * @Author ThanhNT
     */
    public void loadStaff(VShopStaffDTO vShopStaffDTO);

    /**
     * @author ThanhNT
     */
    public void resetProduct();

    public void initEmptyStaff();

    /**
     * get data from method of object
     *
     * @return methodName tên method set data
     * @author ThanhNT
     */
    @Secured("@")
    public void doChangeStaff(String methodName);


    @Secured("@")
    public void doClearStaff(String methodName);

    //getter and setter

    public List<VShopStaffDTO> getLsShopDto();

    public void setLsShopDto(List<VShopStaffDTO> lsShopDto);

    public VShopStaffDTO getvShopStaffDTO();

    public void setvShopStaffDTO(VShopStaffDTO vShopStaffDTO);

    public boolean disabledState();

    public Boolean getIsDisableEdit();

    public void setIsDisableEdit(Boolean isDisableEdit);

    public void init(Object controller, StaffTagConfigDTO staffTagConfigDTO) throws Exception;

    public boolean isMultiTag();

    public void setMultiTag(boolean multiTag);
}
