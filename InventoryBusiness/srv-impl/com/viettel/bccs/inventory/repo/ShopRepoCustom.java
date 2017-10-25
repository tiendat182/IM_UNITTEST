package com.viettel.bccs.inventory.repo;

import com.mysema.query.types.Predicate;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;

import java.sql.Connection;
import java.util.List;

public interface ShopRepoCustom {
    public Predicate toPredicate(List<FilterRequest> filters);

    public List<ShopDTO> getListShopByParentShopId(Long parentShopId) throws Exception;

    public List<ShopDTO> getListAllBranch() throws Exception;

    public List<VShopStaffDTO> getListShopStaff(Long ownerId, String ownerType, String vtUnit) throws Exception;

    /**
     * ham check trung tam hoac chi nhanh
     *
     * @param shopId
     * @return
     * @throws Exception
     * @author Thanhnt77
     */
    public boolean isCenterOrBranch(Long shopId) throws Exception;

    /**
     * ham tra ve danh sach shop theo code cua optionsetvalue
     *
     * @param code
     * @return
     * @throws Exception
     * @author ThanhNT
     */
    public List<ShopDTO> getListShopByCodeOption(String code) throws Exception;

    /**
     * @return
     * @throws Exception
     * @author LuanNT23
     */
    public List<Long> getListChannelType(Long vtUnit) throws Exception;

    /**
     * @return
     * @throws Exception
     */
    public List<ChannelTypeDTO> getListChannelTypeParam(Long channelTypeId, String ObjectType, String isVTUnit) throws Exception;

    /**
     * @param shopId
     * @param parrentId
     * @return tra lai true neu shopId la con cua parrentId
     * @throws Exception
     */
    public boolean validateConstraints(Long shopId, Long parrentId) throws Exception;

    /**
     * @param shopId
     * @return tra lai prefix duong template
     * @throws Exception
     * @author LuanNT23
     */
    public String getStockReportTemplate(Long shopId) throws Exception;

    public Long checkParentShopIsBranch(Long shopId) throws Exception;

    public VShopStaffDTO getShopByCodeForDistribute(String shopCode, Long parentShopId, String ownerType) throws LogicException, Exception;

    public List<ShopDTO> getListShopByStaffShopId(Long shopId, String shopCode) throws LogicException, Exception;

    public List<SmartPhoneDTO> getListNVStockIsdnMbccs(StaffDTO staffDTO) throws Exception;

    public ShopDTO getShopByStaffCode(String staffCode) throws Exception;

    public void increaseStockNum(Connection conn, Long shopId) throws Exception;

    /**
     * ham tra ve danh sach cua hang shop code
     *
     * @param shopCodes
     * @return
     * @throws Exception
     * @author thanhnt77
     */
    public List<ShopDTO> getListShopByCodeAndActiveStatus(List<String> shopCodes) throws Exception;

    public boolean checkExsitShopStaff(Long ownerId, Long ownerType) throws Exception;

    public String getProvince(Long shopId) throws Exception;

    public MvShopStaffDTO getMViewShopStaff(Long ownerType, Long ownerId) throws Exception;

    public ShopDTO getShopByShopKeeper(Long staffId) throws Exception;

    public boolean checkShopIsBranch(Long shopId) throws Exception;

}