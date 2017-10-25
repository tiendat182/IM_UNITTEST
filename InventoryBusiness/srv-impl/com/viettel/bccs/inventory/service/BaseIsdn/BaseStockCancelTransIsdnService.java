package com.viettel.bccs.inventory.service.BaseIsdn;

import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.FlagStockDTO;
import com.viettel.bccs.inventory.dto.StockTransActionDTO;
import com.viettel.bccs.inventory.dto.StockTransDTO;
import com.viettel.bccs.inventory.dto.StockTransDetailDTO;
import com.viettel.bccs.inventory.service.BaseStock.BaseStockService;
import com.viettel.bccs.inventory.service.BaseStock.BaseValidateService;
import com.viettel.bccs.inventory.service.StockTransActionService;
import com.viettel.bccs.inventory.service.StockTransDetailService;
import com.viettel.bccs.inventory.service.StockTransService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.RequiredRoleMap;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by HaiNT41 on 1/13/2016.
 * Ham huy giao dich
 */

@Service
public class BaseStockCancelTransIsdnService extends BaseStockService {

    public static final Logger logger = Logger.getLogger(BaseStockCancelTransIsdnService.class);

    @Autowired
    private BaseValidateService baseValidateService;
    @Autowired
    private StockTransService stockTransService;
    @Autowired
    private StockTransActionService stockTransActionService;
    @Autowired
    private StockTransDetailService stockTransDetailService;

    @Override
    public void doPrepareData(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, FlagStockDTO flagStockDTO) throws LogicException, Exception {

        //Cap nhat trang thai giao dich la da huy
        stockTransDTO.setStatus(Const.STOCK_TRANS_STATUS.CANCEL);
        stockTransActionDTO.setStatus(Const.STOCK_TRANS_STATUS.CANCEL);

        //Cong lai so luong dap ung kho xuat
        flagStockDTO.setExportStock(true);
        flagStockDTO.setExpAvailableQuantity(Const.STOCK_TOTAL.PLUS_QUANTITY);

        //Cap nhat lai trang thai serial kho xuat ve trang thai new
        flagStockDTO.setOldStatus(Const.STOCK_GOODS.STATUS_CONFIRM);
        flagStockDTO.setNewStatus(Const.STOCK_GOODS.STATUS_NEW);
    }

    @Override
    public void doValidate(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, List<StockTransDetailDTO> lstStockTransDetail) throws LogicException, Exception {
        //6. kiem tra trang thai phieu
        StockTransDTO stockCheck = stockTransService.findOne(stockTransDTO.getStockTransId());

        if (DataUtil.isNullObject(stockCheck) || DataUtil.isNullObject(stockCheck.getStatus()) || (!stockCheck.getStatus().equals(Const.STOCK_TRANS_STATUS.EXPORT_ORDER) && !stockCheck.getStatus().equals(Const.STOCK_TRANS_STATUS.EXPORT_NOTE))) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "transfer.isdn.trans.status.invalid");

        }
    }

    @Override
    public void doSaveRegionStockTrans(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, FlagStockDTO flagStockDTO, List<StockTransDetailDTO> lstStockTransDetail) throws Exception {

    }

    @Override
    public void doSaveStockGoods(StockTransDTO stockTransDTO, List<StockTransDetailDTO> lstStockTransDetail, FlagStockDTO flagStockDTO) throws LogicException, Exception {

    }

    @Override
    public void doSaveStockTransDetail(StockTransDTO stockTransDTO, List<StockTransDetailDTO> lstStockTransDetail) throws Exception {

    }

    @Override
    public void doSaveStockTransSerial(StockTransDTO stockTransDTO, List<StockTransDetailDTO> lstStockTransDetail) throws Exception {

    }

    @Override
    public void doSaveStockTotal(StockTransDTO stockTransDTO, List<StockTransDetailDTO> lstStockTransDetailDTO, FlagStockDTO flagStockDTO, StockTransActionDTO stockTransActionDTO) throws Exception {

    }

    /**
     * Goi ws huy giao dich logistic
     *
     * @param stockTransDTO
     * @param lstStockTransDetail
     * @throws Exception
     */
    @Override
    public void doSyncLogistic(StockTransDTO stockTransDTO, List<StockTransDetailDTO> lstStockTransDetail, RequiredRoleMap requiredRoleMap) throws Exception {
        if (Const.STOCK_TRANS.IS_LOGISTIC.equals(stockTransDTO.getLogicstic())) {
            if (DataUtil.safeEqual(Const.STOCK_TRANS_STATUS.IMPORT_ORDER, oldStockTransStatus)
                    || DataUtil.safeEqual(Const.STOCK_TRANS_STATUS.EXPORT_ORDER, oldStockTransStatus)) {
                //Goi ham ws huy giao dich logistic khi lap lenh
                return;
            }
            if (DataUtil.safeEqual(Const.STOCK_TRANS_STATUS.IMPORT_NOTE, oldStockTransStatus)
                    || DataUtil.safeEqual(Const.STOCK_TRANS_STATUS.EXPORT_NOTE, oldStockTransStatus)) {
                //Goi ham ws huy giao dich logistic khi lap phieu
                return;
            }
        }
    }
}
