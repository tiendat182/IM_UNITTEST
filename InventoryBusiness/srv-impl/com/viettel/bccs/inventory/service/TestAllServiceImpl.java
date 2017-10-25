package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.BaseMessageStockTrans;
import com.viettel.bccs.inventory.dto.StockTransActionDTO;
import com.viettel.bccs.inventory.dto.StockTransDTO;
import com.viettel.bccs.inventory.dto.StockTransDetailDTO;
import com.viettel.bccs.sale.service.TestAllService;
import com.viettel.fw.common.util.RequiredRoleMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by PM2-LAMNV5 on 11/16/2016.
 */
@Service
public class TestAllServiceImpl implements TestAllService {
    @Autowired
    ExecuteStockTransServiceImpl transService;
    @Autowired
    StockTransService stockTransService;
    @Autowired
    StockTransActionService stockTransActionService;

    @Override
    public String test_executeStockTrans() throws Exception {
        StockTransDTO stockTransDTO = stockTransService.findOne(58591L);
        stockTransDTO.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);

        StockTransActionDTO stockTransActionDTO = stockTransActionService.findOne(590014L);
        List< StockTransDetailDTO > lstStockTransDetail = null;
        RequiredRoleMap requiredRoleMap = null;
        String mode = Const.STOCK_TRANS.EXPORT_NOTE;
        String type = Const.STOCK_TRANS_TYPE.IMPORT;
        BaseMessageStockTrans msg = transService.executeStockTrans(mode, type, stockTransDTO, stockTransActionDTO, lstStockTransDetail, requiredRoleMap);
        return msg.getDescription();
    }
}
