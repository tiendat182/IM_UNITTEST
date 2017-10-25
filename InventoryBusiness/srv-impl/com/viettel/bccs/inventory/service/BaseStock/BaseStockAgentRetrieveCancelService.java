package com.viettel.bccs.inventory.service.BaseStock;

import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.FlagStockDTO;
import com.viettel.bccs.inventory.dto.StockTransActionDTO;
import com.viettel.bccs.inventory.dto.StockTransDTO;
import com.viettel.bccs.inventory.dto.StockTransDetailDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by hoangnt14 on 1/19/2016.
 * + doSaveStockTrans
 * + doSaveStockTransAction
 * + doSaveStockTotal
 */

@Service
public class BaseStockAgentRetrieveCancelService extends BaseStockService {

    public static final Logger logger = Logger.getLogger(BaseStockImpNoteService.class);


    @Override
    public void doPrepareData(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, FlagStockDTO flagStockDTO) throws Exception {

        //Cap nhat trang thai giao dich la lap lenh xuat
        stockTransDTO.setStatus(Const.STOCK_TRANS_STATUS.DESTROYED);
        stockTransActionDTO.setStatus(Const.STOCK_TRANS_STATUS.DESTROYED);

        //cong kho nhan
        flagStockDTO.setExportStock(true);
        flagStockDTO.setExpAvailableQuantity(Const.STOCK_TOTAL.PLUS_QUANTITY);
        flagStockDTO.setExpCurrentQuantity(Const.STOCK_TOTAL.QUANTITY);

    }

    @Override
    public void doValidate(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, List<StockTransDetailDTO> lstStockTransDetail) throws LogicException, Exception {

    }

    @Override
    public void doSaveRegionStockTrans(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, FlagStockDTO flagStockDTO, List<StockTransDetailDTO> lstStockTransDetail) throws Exception {

    }

    @Override
    public void doSaveStockTransDetail(StockTransDTO stockTransDTO, List<StockTransDetailDTO> lstStockTransDetail) throws Exception {
        //do nothing
    }

    @Override
    public void doSaveStockGoods(StockTransDTO stockTransDTO, List<StockTransDetailDTO> lstStockTransDetail,
                                 FlagStockDTO flagStockDTO) throws LogicException, Exception {

    }

    @Override
    public void doSaveStockTransSerial(StockTransDTO stockTransDTO, List<StockTransDetailDTO> lstStockTransDetail) throws Exception {

    }

    @Override
    public StockTransActionDTO doSaveStockTransAction(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO) throws Exception {
        StockTransActionDTO transActionDTO = DataUtil.cloneBean(stockTransActionDTO);
        transActionDTO.setActionCode(null);
        transActionDTO.setNote(null);
        return super.doSaveStockTransAction(stockTransDTO, transActionDTO);
    }
}