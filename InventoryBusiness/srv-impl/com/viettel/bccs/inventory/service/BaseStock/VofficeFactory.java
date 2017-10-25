package com.viettel.bccs.inventory.service.BaseStock;

import com.viettel.bccs.inventory.common.Const;
import com.viettel.fw.common.util.DataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Created by hoangnt14 on 12/5/2016.
 */
@Service
public class VofficeFactory {

    @Autowired
    @Qualifier("baseVofficeStockService")
    BaseVofficeStockService baseVofficeStockService;

    @Autowired
    @Qualifier("baseVofficeDOAService")
    BaseVofficeDOAService baseVofficeDOAService;

    @Autowired
    @Qualifier("baseVofficeGoodRevokeService")
    BaseVofficeGoodRevokeService baseVofficeGoodRevokeService;

    @Autowired
    @Qualifier("baseVofficeDebitRequestService")
    BaseVofficeDebitRequestService baseVofficeDebitRequestService;

    public BaseVofficeService getInstance(String mode, String prefixTemplate) {
        if (DataUtil.safeEqual(prefixTemplate, Const.DOA_TRANSFER_PREFIX_TEMPLATE)) {
            return baseVofficeDOAService;
        } else if (DataUtil.safeEqual(prefixTemplate, Const.GOODS_REVOKE_PREFIX_TEMPLATE)) {
            return baseVofficeGoodRevokeService;
        } else if (DataUtil.safeEqual(prefixTemplate, Const.DEBIT_REQUEST_PREFIX_TEMPLATE)) {
            return baseVofficeDebitRequestService;
        }
        return baseVofficeStockService;
    }
}
