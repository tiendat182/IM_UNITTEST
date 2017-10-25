package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.ChannelTypeDTO;
import com.viettel.bccs.inventory.dto.MvShopStaffDTO;
import com.viettel.bccs.inventory.dto.ShopDTO;
import com.viettel.bccs.inventory.dto.VShopStaffDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface ShopService {

    @WebMethod
    public ShopDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<ShopDTO> findAll() throws Exception;

    @WebMethod
    public List<ShopDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public BaseMessage create(ShopDTO productSpecCharacterDTO) throws Exception;

    @WebMethod
    public BaseMessage update(ShopDTO productSpecCharacterDTO) throws Exception;

    @WebMethod
    public ShopDTO save(ShopDTO shopDTO) throws Exception;

    @WebMethod
    public List<ShopDTO> getListShopByParentShopId(Long parentShopId) throws Exception, LogicException;

    @WebMethod
    public List<ShopDTO> getListAllBranch() throws Exception;

    /**
     * ham lay ve danh sach kho so
     *
     * @param shopPath
     * @return list kho so List<LstShopDTO>
     * @author ThanhNT
     */
    @WebMethod
    public List<VShopStaffDTO> getListShop(String shopPath, String txtSearch, String parentShopId, boolean isCurrentAndChildShop, boolean isDismissType) throws LogicException, Exception;

    /**
     * ham tra ve danh sach shop van chuyen
     *
     * @param txtSearch
     * @param parrentShopId
     * @return
     * @throws LogicException
     * @throws Exception
     * @author thanhnt77
     */
    public List<VShopStaffDTO> getListShopTransfer(String txtSearch, String parrentShopId) throws LogicException, Exception;

    public List<VShopStaffDTO> getListShopBranchAndVTT(String txtSearch, String parrentShopId) throws LogicException, Exception;

    /**
     * tra ve danh sach khu vuc
     *
     * @param txtSearch
     * @return
     * @throws LogicException
     * @throws Exception
     * @author thanhnt77
     */
    public List<VShopStaffDTO> getListShopKV(String txtSearch) throws LogicException, Exception;

    @WebMethod
    public List<VShopStaffDTO> getListShopAndChildShop(String shopPath, String parentShopId) throws LogicException, Exception;

    /**
     * ham lay ve doi tuong kho(kho don vi hoac kho nhan vien)
     *
     * @param ownerId
     * @author ThanhNT
     */
    @WebMethod
    public VShopStaffDTO getShopByOwnerId(String ownerId, String ownerType) throws LogicException, Exception;

    public ShopDTO getShopByCodeAndActiveStatus(String shopCode) throws Exception;

    public List<ShopDTO> getListShopByCodeAndActiveStatus(List<String> shopCodes) throws Exception;

    public ShopDTO getShopByShopCode(String shopCode) throws Exception;

    /**
     * ham lay danh sach shop, staff tu DB de check validate
     *
     * @param ownerId
     * @author TheTM
     */
    @WebMethod
    public List<VShopStaffDTO> getListShopStaff(Long ownerId, String ownerType, String vtUnit) throws Exception, LogicException;

    public List<ShopDTO> getListShopByObject(ShopDTO shopDTO) throws Exception;

    /**
     * ham check trung tam hoac chi nhanh
     *
     * @param shopId
     * @return
     * @throws Exception
     * @author Thanhnt77
     */
    public boolean isCenterOrBranch(Long shopId) throws LogicException, Exception;

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
     * Ham lay danh sach tat cac cac shop
     * author HoangNT
     *
     * @return
     * @throws LogicException
     * @throws Exception
     */
    @WebMethod
    public List<VShopStaffDTO> getListAllShop() throws LogicException, Exception;


    /**
     * @return danh sach shop va shop con, chau chat theo dieu kien tim kiem
     * @throws Exception
     * @author LuanNT23
     */
    public List<VShopStaffDTO> getListShopAndAllChildShop(String txtSearch, Long shopId, boolean isDismissType, boolean notCurShop, List<Long> channelTypes, String ownerType) throws Exception;

    @WebMethod
    public List<VShopStaffDTO> getListShopHierachy(String shopId, String channelTypeId, String txtSearch) throws Exception;

    public List<VShopStaffDTO> getListNotChanelTypeId(String shopId, String channelTypeId, String txtSearch) throws Exception;

    @WebMethod
    public List<VShopStaffDTO> getAllShop(String txtSearch, String channelTypeId) throws Exception;

    @WebMethod
    public Long checkParentShopIsBranch(Long shopId) throws Exception;

    /**
     * Lay danh sach cac dai ly
     *
     * @param txtSearch
     * @param parentId
     * @return
     * @throws Exception
     */
    @WebMethod
    public List<VShopStaffDTO> getListAgent(String txtSearch, Long parentId) throws Exception;

    @WebMethod
    public List<VShopStaffDTO> getListCollaboratorAndPointOfSale(String txtSearch, Long shopId, List<Long> lstChannelType, Long staffOwnerId) throws Exception;

    @WebMethod
    public boolean checkChannelIsAgent(Long channelTypeId) throws Exception;

    @WebMethod
    public boolean checkChannelIsCollAndPointOfSale(Long channelTypeId) throws Exception;

    @WebMethod
    public List<ChannelTypeDTO> getChannelTypeCollaborator() throws Exception;

    @WebMethod
    public VShopStaffDTO getParentShopByShopId(Long shopId) throws LogicException, Exception;

    @WebMethod
    public List<VShopStaffDTO> getListShopForIsdn(String input, String parentShopId, boolean isCurrentAndChildShop, List<String> lstChanelType);

    /**
     * Lay ID chi nhanh cua cua hang
     *
     * @param shopId
     * @return
     * @throws LogicException
     * @throws Exception
     */
    @WebMethod
    public Long getBranchId(Long shopId) throws LogicException, Exception;

    public Long findBranchId(Long shopId) throws LogicException, Exception;

    @WebMethod
    public VShopStaffDTO getShopByCodeForDistribute(String shopCode, Long parentShopId, String ownerType) throws LogicException, Exception;

    @WebMethod
    public List<ShopDTO> getListShopByStaffShopId(Long shopId, String shopCode) throws LogicException, Exception;

    @WebMethod
    public List<VShopStaffDTO> getListShopForPay(String input, String parentShopId);

    @WebMethod
    public VShopStaffDTO getShopByShopId(Long shopId) throws LogicException, Exception;

    @WebMethod
    public List<Long> getListChannelType() throws Exception;

    @WebMethod
    public ChannelTypeDTO getChannelTypeById(Long channelTypeId) throws LogicException, Exception;

    /**
     * ham tra ve danh sach don vi
     *
     * @param input
     * @param lsShopCodes
     * @return
     * @author thanhnt77
     */
    public List<VShopStaffDTO> getListShopByListShopCode(String input, String shopPathUserLogin, List<String> lsShopCodes);

    @WebMethod
    public boolean checkExsitShopStaff(Long ownerId, Long ownerType) throws Exception;

    public MvShopStaffDTO getMViewShopStaff(Long ownerType, Long ownerId) throws Exception;

    public ShopDTO getShopByShopKeeper(Long staffId) throws Exception;


    public boolean checkShopIsBranch(Long shopId) throws Exception;

}