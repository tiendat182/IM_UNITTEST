package com.viettel.bccs.inventory.service.common;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.controller.SolrController;
import com.viettel.bccs.inventory.dto.MvShopStaffDTO;
import com.viettel.bccs.inventory.dto.VShopStaffDTO;
import com.viettel.bccs.inventory.model.Partner;
import com.viettel.bccs.inventory.model.Shop;
import com.viettel.bccs.inventory.model.Staff;
import com.viettel.bccs.inventory.repo.PartnerRepo;
import com.viettel.bccs.inventory.repo.ShopRepo;
import com.viettel.bccs.inventory.repo.StaffRepo;
import com.viettel.bccs.inventory.service.CommonService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ResourceBundle;

/**
 * @author luannt23 on 1/7/2016.
 */

@Service
public class CommonServiceImpl implements CommonService {
    private Logger logger = Logger.getLogger(CommonServiceImpl.class);
    @Autowired
    StaffRepo staffRepo;

    @Autowired
    ShopRepo shopRepo;

    @Autowired
    PartnerRepo partnerRepo;

    @Override
    public String getStockReportTemplate(Long ownerId, String ownerType) throws LogicException, Exception {
        try {
            if (Const.OWNER_TYPE.SHOP.equals(ownerType)) {
                return shopRepo.getStockReportTemplate(ownerId);
            } else {
                return staffRepo.getStockReportTemplate(ownerId);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.transDetail.export.noConfigTemplate");
        }


    }

    @Override
    public List<String> getChannelTypes(String ownerType) throws LogicException, Exception {

        List<Long> channelTypes;
        try {
            List<String> result = Lists.newArrayList();
            if (Const.OWNER_TYPE.SHOP.equals(ownerType)) {
                channelTypes = shopRepo.getListChannelType(null);
            } else {
                channelTypes = staffRepo.getListChannelTypeForStaff(null);
            }
            if (!DataUtil.isNullOrEmpty(channelTypes)) {
                for (Long channelType : channelTypes) {
                    result.add(DataUtil.safeToString(channelType));
                }
            }
            return result;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.transDetail.export.noConfigTemplate");
        }
    }

    @Override
    public String getOwnerName(String ownerID, String ownerType) throws Exception {
        if (DataUtil.safeEqual(Const.STOCK_TRANS_VOFFICE.OWNERTYPE_PARTNER, DataUtil.safeToLong(ownerType))) {
            Partner partner = partnerRepo.findOne(Long.valueOf(ownerID));
            if (partner == null) {
                return "";
            }
            return partner.getPartnerName();
        } else {
            MvShopStaffDTO mvShopStaffDTO = shopRepo.getMViewShopStaff(DataUtil.safeToLong(ownerType), DataUtil.safeToLong(ownerID));
            if (!DataUtil.isNullObject(mvShopStaffDTO)) {
                return mvShopStaffDTO.getOwnerCode() + '-' + mvShopStaffDTO.getOwnerName();
            }
        }
        return "";
    }


    @Override
    public List<Long> getChannelTypeByVtUnit(String ownerType, Long vtUnit) throws LogicException, Exception {
        List<Long> channelTypes;
        try {
            if (Const.OWNER_TYPE.SHOP.equals(ownerType)) {
                channelTypes = shopRepo.getListChannelType(vtUnit);
            } else {
                channelTypes = staffRepo.getListChannelTypeForStaff(vtUnit);
            }
//            if (!DataUtil.isNullOrEmpty(channelTypes)) {
//                for (Long channelType : channelTypes) {
//                    result.add(DataUtil.safeToString(channelType));
//                }
//            }
            return channelTypes;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.transDetail.export.noConfigTemplate");
        }
    }

    @Override
    public boolean checkIsdnVietel(String sNumber) throws LogicException, Exception {
        ResourceBundle resource = ResourceBundle.getBundle("config");
        boolean result = false;
        if (chkNumber(sNumber) == true) {
            for (int i = 0; i <= Integer.parseInt(resource.getString("viettel.isdn.count")); i++) {
                String value = resource.getString("viettel.isdn." + i);
                String arrValue[] = value.split(",");
                int minLength = Integer.parseInt(resource.getString("viettel.isdn.minLength"));
                int maxLength = Integer.parseInt(resource.getString("viettel.isdn.maxLength"));
                if (arrValue.length > 0) {
                    for (int idxIsdnLength = minLength; idxIsdnLength <= maxLength; idxIsdnLength++) {
                        if (sNumber.length() == idxIsdnLength) {
                            if (sNumber.substring(0, idxIsdnLength - 7).equals(arrValue[0])) {
                                result = true;
                            }
                            break;
                        }
                    }
                }
            }
        }
        return result;
    }

    private boolean chkNumber(String sNumber) {
        boolean result = true;
        int i;
        for (i = 0; i < sNumber.length(); i++) {
            // Check that current character is number.
            if (!Character.isDigit(sNumber.charAt(i))) {
                result = false;
            }
        }
        return result;
    }

    @Override
    public String getProvince(Long shopId) throws Exception {
        return shopRepo.getProvince(shopId);
    }

    @Override
    public String getOwnerAddress(String ownerID, String ownerType) throws Exception {
        if (DataUtil.safeEqual(Const.STOCK_TRANS_VOFFICE.OWNERTYPE_PARTNER, DataUtil.safeToLong(ownerType))) {
            Partner partner = partnerRepo.findOne(Long.valueOf(ownerID));
            if (partner == null) {
                return "";
            }
            return partner.getAddress();
        }
        if (DataUtil.safeEqual(Const.STOCK_TRANS_VOFFICE.OWNERTYPE_SHOP, DataUtil.safeToLong(ownerType))) {
            Shop s = shopRepo.findOne(Long.valueOf(ownerID));
            if (s != null) {
                return s.getAddress();
            }
        } else {
            Staff s = staffRepo.findOne(Long.valueOf(ownerID));
            if (s != null) {
                return s.getAddress();
            }
        }
        return "";
    }
}
