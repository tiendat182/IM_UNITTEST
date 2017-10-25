package com.viettel.bccs.inventory.wsesb.service;

import com.viettel.bccs.inventory.dto.ChannelTypeDTO;
import com.viettel.bccs.inventory.dto.MvShopStaffDTO;
import com.viettel.bccs.inventory.dto.ShopDTO;
import com.viettel.bccs.inventory.dto.VShopStaffDTO;
import com.viettel.bccs.inventory.service.ShopService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.jws.WebMethod;
import java.util.List;

@Service("WsShopServiceImpl")
public class WsShopServiceImpl implements ShopService {

    public static final Logger logger = Logger.getLogger(WsShopServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private ShopService client;

    @PostConstruct
    public void init() throws Exception {
        try {
            client = wsClientFactory.createWsClient(ShopService.class);
        } catch (Exception ex) {
            logger.error("init", ex);
        }
    }

    @Override
    @WebMethod
    public ShopDTO findOne(Long id) throws Exception {
        return client.findOne(id);
    }

    @Override
    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return client.count(filters);
    }

    @Override
    @WebMethod
    public List<ShopDTO> findAll() throws Exception {
        return client.findAll();
    }

    @Override
    @WebMethod
    public List<ShopDTO> findByFilter(List<FilterRequest> filters) throws Exception {
        return client.findByFilter(filters);
    }

    @Override
    @WebMethod
    public BaseMessage create(ShopDTO productSpecCharacterDTO) throws Exception {
        return client.create(productSpecCharacterDTO);
    }

    @Override
    @WebMethod
    public BaseMessage update(ShopDTO productSpecCharacterDTO) throws Exception {
        return client.update(productSpecCharacterDTO);
    }

    @Override
    @WebMethod
    public List<ShopDTO> getListShopByParentShopId(Long parentShopId) throws Exception, LogicException {
        return client.getListShopByParentShopId(parentShopId);
    }

    @Override
    @WebMethod
    public List<ShopDTO> getListAllBranch() throws Exception {
        return client.getListAllBranch();
    }

    @Override
    @WebMethod
    public List<VShopStaffDTO> getListShop(String shopPath, String txtSearch, String parentShopId, boolean isCurrentAndChildShop, boolean isDismissType) throws LogicException, Exception {
        return client.getListShop(shopPath, txtSearch, parentShopId, isCurrentAndChildShop, isDismissType);
    }

    @Override
    @WebMethod
    public List<VShopStaffDTO> getListShopAndChildShop(String shopPath, String parentShopId) throws LogicException, Exception {
        return client.getListShopAndChildShop(shopPath, parentShopId);
    }

    @Override
    public List<VShopStaffDTO> getListShopBranchAndVTT(String txtSearch, String parrentShopId) throws LogicException, Exception {
        return client.getListShopBranchAndVTT(txtSearch, parrentShopId);
    }

    @Override
    @WebMethod
    public VShopStaffDTO getShopByOwnerId(String ownerId, String ownerType) throws LogicException, Exception {
        return client.getShopByOwnerId(ownerId, ownerType);
    }

    @Override
    public ShopDTO getShopByCodeAndActiveStatus(String shopCode) throws Exception {
        return client.getShopByCodeAndActiveStatus(shopCode);
    }

    @Override
    public ShopDTO getShopByShopCode(String shopCode) throws Exception {
        return client.getShopByShopCode(shopCode);
    }

    @Override
    @WebMethod
    public List<VShopStaffDTO> getListShopStaff(Long ownerId, String ownerType, String vtUnit) throws Exception, LogicException {
        return client.getListShopStaff(ownerId, ownerType, vtUnit);
    }

    @Override
    public List<ShopDTO> getListShopByObject(ShopDTO shopDTO) throws Exception {
        return client.getListShopByObject(shopDTO);
    }

    @Override
    public boolean isCenterOrBranch(Long shopId) throws LogicException, Exception {
        return client.isCenterOrBranch(shopId);
    }

    @Override
    public List<ShopDTO> getListShopByCodeOption(String code) throws Exception {
        return client.getListShopByCodeOption(code);
    }

    @WebMethod
    @Override
    public List<VShopStaffDTO> getListShopHierachy(String shopId, String channelTypeId, String txtSearch) throws Exception {
        return client.getListShopHierachy(shopId, channelTypeId, txtSearch);
    }

    @WebMethod
    @Override
    public List<VShopStaffDTO> getAllShop(String txtSearch, String channelTypeId) throws Exception {
        return client.getAllShop(txtSearch, channelTypeId);
    }

    @Override
    @WebMethod
    public List<VShopStaffDTO> getListAllShop() throws LogicException, Exception {
        return client.getListAllShop();
    }

    @Override
    public List<VShopStaffDTO> getListShopAndAllChildShop(String txtSearch, Long shopId, boolean isDismissType, boolean notCurShop, List<Long> chanelType, String ownerType) throws Exception {
        return client.getListShopAndAllChildShop(txtSearch, shopId, isDismissType, notCurShop, chanelType, ownerType);
    }

    @Override
    public List<VShopStaffDTO> getListAgent(String txtSearch, Long parentId) throws Exception {
        return client.getListAgent(txtSearch, parentId);
    }

    @WebMethod
    @Override
    public boolean checkChannelIsAgent(Long channelTypeId) throws Exception {
        return client.checkChannelIsAgent(channelTypeId);
    }

    @Override
    @WebMethod
    public ShopDTO save(ShopDTO shopDTO) throws Exception {
        return client.save(shopDTO);
    }

    @Override
    @WebMethod
    public Long checkParentShopIsBranch(Long shopId) throws Exception {
        return client.checkParentShopIsBranch(shopId);
    }

    @Override
    @WebMethod
    public VShopStaffDTO getParentShopByShopId(Long shopId) throws LogicException, Exception {
        return client.getParentShopByShopId(shopId);
    }

    @Override
    @WebMethod
    public List<ChannelTypeDTO> getChannelTypeCollaborator() throws Exception {
        return client.getChannelTypeCollaborator();
    }

    @Override
    @WebMethod
    public List<VShopStaffDTO> getListCollaboratorAndPointOfSale(String txtSearch, Long shopId, List<Long> lstChannelType, Long staffOwnerId) throws Exception {
        return client.getListCollaboratorAndPointOfSale(txtSearch, shopId, lstChannelType, staffOwnerId);
    }

    @Override
    @WebMethod
    public boolean checkChannelIsCollAndPointOfSale(Long channelTypeId) throws Exception {
        return client.checkChannelIsCollAndPointOfSale(channelTypeId);
    }

    @Override
    @WebMethod
    public List<VShopStaffDTO> getListShopForIsdn(String input, String parentShopId, boolean isCurrentAndChildShop, List<String> lstChanelType) {
        return client.getListShopForIsdn(input, parentShopId, isCurrentAndChildShop, lstChanelType);
    }

    @Override
    @WebMethod
    public Long getBranchId(Long shopId) throws LogicException, Exception {
        return client.getBranchId(shopId);
    }

    @Override
    @WebMethod
    public VShopStaffDTO getShopByCodeForDistribute(String shopCode, Long parentShopId, String ownerType) throws LogicException, Exception {
        return client.getShopByCodeForDistribute(shopCode, parentShopId, ownerType);
    }

    @Override
    @WebMethod
    public List<ShopDTO> getListShopByStaffShopId(Long shopId, String shopCode) throws LogicException, Exception {
        return client.getListShopByStaffShopId(shopId, shopCode);
    }

    @Override
    @WebMethod
    public List<VShopStaffDTO> getListShopForPay(String input, String parentShopId) {
        return client.getListShopForPay(input, parentShopId);
    }

    @Override
    @WebMethod
    public VShopStaffDTO getShopByShopId(Long shopId) throws LogicException, Exception {
        return client.getShopByShopId(shopId);
    }

    @Override
    @WebMethod
    public List<Long> getListChannelType() throws Exception {
        return client.getListChannelType();
    }

    @Override
    @WebMethod
    public ChannelTypeDTO getChannelTypeById(Long channelTypeId) throws LogicException, Exception {
        return client.getChannelTypeById(channelTypeId);
    }

    @Override
    public List<VShopStaffDTO> getListNotChanelTypeId(String shopId, String channelTypeId, String txtSearch) throws Exception {
        return client.getListNotChanelTypeId(shopId, channelTypeId, txtSearch);
    }

    @Override
    public List<VShopStaffDTO> getListShopByListShopCode(String input, String shopPathUserLogin, List<String> lsShopCodes) {
        return client.getListShopByListShopCode(input, shopPathUserLogin, lsShopCodes);
    }

    @Override
    public List<VShopStaffDTO> getListShopTransfer(String txtSearch, String parrentShopId) throws LogicException, Exception {
        return client.getListShopTransfer(txtSearch, parrentShopId);
    }

    @Override
    public List<ShopDTO> getListShopByCodeAndActiveStatus(List<String> shopCodes) throws Exception {
        return client.getListShopByCodeAndActiveStatus(shopCodes);
    }

    @Override
    @WebMethod
    public boolean checkExsitShopStaff(Long ownerId, Long ownerType) throws Exception {
        return client.checkExsitShopStaff(ownerId, ownerType);
    }

    @Override
    public MvShopStaffDTO getMViewShopStaff(Long ownerType, Long ownerId) throws Exception {
        return client.getMViewShopStaff(ownerType, ownerId);
    }
    
    @Override
    public List<VShopStaffDTO> getListShopKV(String txtSearch) throws LogicException, Exception {
        return client.getListShopKV(txtSearch);
    }

    @Override
    public ShopDTO getShopByShopKeeper(Long staffId) throws Exception {
        return client.getShopByShopKeeper(staffId);
    }

    @Override
    public boolean checkShopIsBranch(Long shopId) throws Exception {
        return client.checkShopIsBranch(shopId);
    }

    @Override
    public Long findBranchId(Long shopId) throws LogicException, Exception {
        return client.findBranchId(shopId);
    }
}