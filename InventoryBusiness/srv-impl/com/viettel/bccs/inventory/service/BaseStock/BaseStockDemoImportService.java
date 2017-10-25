package com.viettel.bccs.inventory.service.BaseStock;

import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.ProductOfferingService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by hoangnt14 on 5/10/2017.
 */
@Service
public class BaseStockDemoImportService extends BaseStockService {

    public static final Logger logger = Logger.getLogger(BaseStockDemoImportService.class);

    @Autowired
    private BaseValidateService baseValidateService;
    @Autowired
    private ProductOfferingService productOfferingService;

    @Override
    public void doPrepareData(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, FlagStockDTO flagStockDTO) throws LogicException, Exception {
        stockTransDTO.setNote(stockTransDTO.getNote() != null ? stockTransDTO.getNote().trim() : null);
        //Cap nhat trang thai giao dich la lap lenh xuat
        stockTransDTO.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
        stockTransActionDTO.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
        stockTransDTO.setStockTransType(Const.STOCK_TRANS_STATUS.EXPORT_ORDER);

        // Cap nhat trang thai stock_x
        flagStockDTO.setOldStatus(Const.STOCK_GOODS.STATUS_SALE);
        flagStockDTO.setNewStatus(Const.STOCK_GOODS.STATUS_NEW);
        flagStockDTO.setUpdateStateDemo(true);
        flagStockDTO.setUpdateOwnerId(true);
        //Cong SL kho nhap
        flagStockDTO.setImportStock(true);
        flagStockDTO.setImpCurrentQuantity(Const.STOCK_TOTAL.PLUS_QUANTITY);
        flagStockDTO.setImpAvailableQuantity(Const.STOCK_TOTAL.PLUS_QUANTITY);
        // Luu thong tin stock_total_audit
        flagStockDTO.setInsertStockTotalAudit(true);
    }

    @Override
    public void doValidate(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, List<StockTransDetailDTO> lstStockTransDetail) throws LogicException, Exception {
        //validate hang demo
        // Check serial;
        baseValidateService.doInputSerialValidate(stockTransDTO, lstStockTransDetail);
    }

    public StockTransActionDTO doSaveStockTransAction(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO) throws Exception {
        // Lap phieu xuat
        stockTransActionDTO.setStatus(Const.STOCK_TRANS_STATUS.IMPORT_NOTE);
        StockTransActionDTO result = super.doSaveStockTransAction(stockTransDTO, stockTransActionDTO);
        StockTransDTO stockTransDTOData = DataUtil.cloneBean(stockTransDTO);
        // nhap kho
        StockTransActionDTO transActionDTOImported = DataUtil.cloneBean(stockTransActionDTO);
        transActionDTOImported.setActionCode(null);
        transActionDTOImported.setNote(null);
        transActionDTOImported.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
        super.doSaveStockTransAction(stockTransDTOData, transActionDTOImported);
        return result;
    }
}
