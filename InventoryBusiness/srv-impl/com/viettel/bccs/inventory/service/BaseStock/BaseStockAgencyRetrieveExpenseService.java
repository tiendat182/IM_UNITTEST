package com.viettel.bccs.inventory.service.BaseStock;

import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.RequiredRoleMap;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by hoangnt14 on 2/17/2016.
 */
@Service
public class BaseStockAgencyRetrieveExpenseService extends BaseStockService {

    public static final Logger logger = Logger.getLogger(BaseStockAcencyDepositMoneyServices.class);

    @Override
    public void doPrepareData(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, FlagStockDTO flagStockDTO) throws LogicException, Exception {

        stockTransDTO.setDepositStatus(Const.STOCK_STRANS_DEPOSIT.DEPOSIT_RETRIEVE_EXPENSE);
        stockTransDTO.setStatus(Const.STOCK_TRANS_STATUS.EXPORT_NOTE);
        // Luu thong tin dat coc
        flagStockDTO.setInsertReceiptExpense(true);
        flagStockDTO.setReceiptExpenseType(Const.STOCK_TRANS_STATUS.EXPORT_NOTE);
        flagStockDTO.setReceiptExpenseTypeId(Const.STOCK_TRANS.STOCK_TRANS_TYPE_EXPENSE);
        flagStockDTO.setReceiptExpensePayMethod(Const.STOCK_STRANS_DEPOSIT.HTTT);
        flagStockDTO.setReceiptExpenseStatus(Const.STOCK_TRANS.STOCK_TRANS_STATUS_DEPOSIT);
        flagStockDTO.setDepositStatus(Const.STOCK_STRANS_DEPOSIT.DEPOSIT_NOTE);
        flagStockDTO.setDepositType(Const.STOCK_STRANS_DEPOSIT.DEPOSIT_TYPE_EXPENSE);
        flagStockDTO.setDepositTypeId(Const.STOCK_STRANS_DEPOSIT.DEPOSIT_TYPE_EXPENSE);
    }

    @Override
    public void doValidate(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, List<StockTransDetailDTO> lstStockTransDetail) throws LogicException, Exception {
    }


    @Override
    public StockTransActionDTO doSaveStockTransAction(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO) throws Exception {
        return stockTransActionDTO;
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

    @Override
    public void doSignVoffice(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, RequiredRoleMap requiredRoleMap,
                              FlagStockDTO flagStockDTO) throws Exception {

    }

    @Override
    public void doSyncLogistic(StockTransDTO stockTransDTO, List<StockTransDetailDTO> lstStockTransDetail, RequiredRoleMap requiredRoleMap) throws Exception {

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void doIncreaseStockNum(StockTransActionDTO stockTransActionDTO, String prefixActionCode, RequiredRoleMap requiredRoleMap) throws Exception {


    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStockNumShop(Long shopId, String prefixActionCode, Long stockTransActionId) throws Exception {

    }

    @Override
    public void doSaveStockTotalAudit(StockTotalAuditDTO stockTotalAuditDTO) throws Exception {

    }

    @Override
    public void doSaveExchangeStockTrans(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, FlagStockDTO flagStockDTO, List<StockTransDetailDTO> lstStockTransDetail) throws Exception {

    }

}
