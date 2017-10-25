package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.LookupIsdnDTO;
import com.viettel.bccs.inventory.dto.ShopDTO;
import com.viettel.bccs.inventory.dto.StockIsdnVtShopDTO;
import com.viettel.bccs.inventory.dto.StockIsdnVtShopFeeDTO;
import com.viettel.bccs.inventory.repo.StockNumberRepo;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.fw.service.BaseServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HotlineServiceImpl extends BaseServiceImpl implements HotlineService {
    public static final Logger logger = Logger.getLogger(VtShopService.class);

    @Autowired
    StockIsdnVtShopService stockIsdnVtShopService;
    @Autowired
    private OptionSetValueService optionSetValueService;
    @Autowired
    private StockNumberRepo stockNumberRepo;
    @Autowired
    private ShopService shopService;

    @Override
    public BaseMessage lockIsdn(String isdn, Long requestId, String staffCode) {

        BaseMessage baseMessage = new BaseMessage();

        try {
            stockIsdnVtShopService.lockIsdnVtshop(isdn, requestId, staffCode);
            baseMessage.setSuccess(true);
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            baseMessage.setErrorCode(ex.getErrorCode());
            baseMessage.setDescription(ex.getDescription());
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            baseMessage.setDescription(getText("common.error.happen"));
        }
        return baseMessage;
    }

    @Override
    public List<StockIsdnVtShopDTO> findRequestList(List<Long> requestList) {

        return stockIsdnVtShopService.findRequestList(requestList);
    }

    @Override
    public List<LookupIsdnDTO> getListLookupIsdn(String fromIsdn, String toIsdn, Long maxRow) throws LogicException, Exception {
        if (DataUtil.isNullOrEmpty(fromIsdn)) {
            return Lists.newArrayList(new LookupIsdnDTO(null, false, "ERR_IM", "sp.fromIsdn.isNull"));
        }
        if (DataUtil.isNullOrEmpty(toIsdn)) {
            return Lists.newArrayList(new LookupIsdnDTO(null, false, "ERR_IM", "sp.toIsdn.isNull"));
        }

        if (DataUtil.isNullOrZero(maxRow)) {
            return Lists.newArrayList(new LookupIsdnDTO(null, false, "ERR_IM", "sp.maxRow.isNull"));
        }
        //doc tu cau hinh

        String shopCodes = optionSetValueService.getValueByTwoCodeOption(Const.OPTION_SET.SHOP_CODE_LOOKUP_ISDN, Const.OPTION_SET.SHOP_CODE_LOOKUP_ISDN);

        if (DataUtil.isNullOrEmpty(shopCodes)) {
            return Lists.newArrayList(new LookupIsdnDTO(Lists.newArrayList(fromIsdn, toIsdn), false, "ERR_IM", "sp.StockCode.not.number", shopCodes));
        }

        List<ShopDTO> lsShop = shopService.getListShopByCodeAndActiveStatus(Arrays.asList(shopCodes.split(",")));

        if (DataUtil.isNullOrEmpty(lsShop)) {
            return Lists.newArrayList(new LookupIsdnDTO(Lists.newArrayList(fromIsdn, toIsdn), false, "ERR_IM", "sp.StockCode.not.number", shopCodes));
        }
        List<String> lsStatus = Lists.newArrayList(Const.STOCK_NUMBER_STATUS.NEW);
        List<String> lstIsdn = stockNumberRepo.lookupIsdn(fromIsdn, toIsdn, lsShop.stream().map(ShopDTO::getShopId).collect(Collectors.toList()), lsStatus, maxRow);

        if (DataUtil.isNullOrEmpty(lstIsdn)) {
            return Lists.newArrayList(new LookupIsdnDTO(Lists.newArrayList(fromIsdn, toIsdn), false, "ERR_IM", "sp.StockCode.not.number", shopCodes));
        }

        return Lists.newArrayList(new LookupIsdnDTO(lstIsdn, true, null, null));
    }

    public BaseMessage lockIsdnHotline(String isdn, String staffCode) {

        BaseMessage baseMessage = new BaseMessage();

        try {
            stockIsdnVtShopService.lockIsdnHotline(isdn, staffCode);
            baseMessage.setSuccess(true);
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            baseMessage.setErrorCode(ex.getErrorCode());
            baseMessage.setDescription(ex.getDescription());
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            baseMessage.setDescription(getText("common.error.happen"));
        }
        return baseMessage;
    }

    public BaseMessage unlockIsdnHotline(String isdn) {

        BaseMessage baseMessage = new BaseMessage();

        try {
            stockIsdnVtShopService.unlockIsdnHotline(isdn);
            baseMessage.setSuccess(true);
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            baseMessage.setErrorCode(ex.getErrorCode());
            baseMessage.setDescription(ex.getDescription());
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            baseMessage.setDescription(getText("common.error.happen"));
        }
        return baseMessage;
    }
}
