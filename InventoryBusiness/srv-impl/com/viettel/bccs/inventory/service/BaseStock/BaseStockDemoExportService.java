package com.viettel.bccs.inventory.service.BaseStock;

import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.model.ProductOffering;
import com.viettel.bccs.inventory.service.ProductOfferingService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by hoangnt14 on 5/9/2017.
 */
@Service
public class BaseStockDemoExportService extends BaseStockService {

    public static final Logger logger = Logger.getLogger(BaseStockDemoExportService.class);

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
        flagStockDTO.setOldStatus(Const.STOCK_GOODS.STATUS_NEW);
        flagStockDTO.setNewStatus(Const.STOCK_GOODS.STATUS_SALE);
        flagStockDTO.setStateIdForReasonId(Const.GOODS_STATE.DEMO);
        flagStockDTO.setUpdateSaleDate(true);
        //Tru so luong dap ung kho xuat
        flagStockDTO.setExportStock(true);
        flagStockDTO.setExpCurrentQuantity(Const.STOCK_TOTAL.MINUS_QUANTITY);
        flagStockDTO.setExpAvailableQuantity(Const.STOCK_TOTAL.MINUS_QUANTITY);
        // Luu thong tin stock_total_audit
        flagStockDTO.setInsertStockTotalAudit(true);
    }

    @Override
    public void doValidate(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, List<StockTransDetailDTO> lstStockTransDetail) throws LogicException, Exception {
        // Check serial;
        baseValidateService.doInputSerialValidate(stockTransDTO, lstStockTransDetail);
    }

    public StockTransActionDTO doSaveStockTransAction(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO) throws Exception {
        // Lap phieu xuat
        stockTransActionDTO.setStatus(Const.STOCK_TRANS_STATUS.EXPORT_NOTE);
        StockTransActionDTO result = super.doSaveStockTransAction(stockTransDTO, stockTransActionDTO);
        // Xuat kho
        StockTransDTO stockTransDTOData = DataUtil.cloneBean(stockTransDTO);
        stockTransDTOData.setNote(null);
        StockTransActionDTO transActionDTOExported = DataUtil.cloneBean(stockTransActionDTO);
        transActionDTOExported.setActionCode(null);
        transActionDTOExported.setStatus(Const.STOCK_TRANS_STATUS.EXPORTED);
        super.doSaveStockTransAction(stockTransDTOData, transActionDTOExported);
        // nhap kho
        StockTransActionDTO transActionDTOImported = DataUtil.cloneBean(stockTransActionDTO);
        transActionDTOImported.setActionCode(null);
        transActionDTOImported.setNote(null);
        transActionDTOImported.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
        super.doSaveStockTransAction(stockTransDTOData, transActionDTOImported);
        return result;
    }
}
