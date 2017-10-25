package com.viettel.bccs.inventory.service.BaseStock;

import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by thetm1 on 9/12/2015.
 * Description Lap lenh xuat kho
 * Detail + Insert stock_trans
 * + Insert stock_trans_detail
 * + Insert stock_trans_action
 * + Khong luu stock_trans_serial (-)
 * + Cap nhat so luong dap ung stock_total
 * + Khong cap nhat chi tiet serial (doSaveStockGoods) (-)
 */

@Service
public class BaseStockOutOrderService extends BaseStockService {

    public static final Logger logger = Logger.getLogger(BaseStockOutOrderService.class);

    @Autowired
    private BaseValidateService baseValidateService;


    @Override
    public void doPrepareData(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, FlagStockDTO flagStockDTO) throws Exception {

        stockTransDTO.setNote(stockTransDTO.getNote() != null ? stockTransDTO.getNote().trim() : null);
        stockTransDTO.setStockBase(stockTransDTO.getStockBase() != null ? stockTransDTO.getStockBase().trim() : null);
        //Cap nhat trang thai giao dich la lap lenh xuat
        stockTransDTO.setStatus(Const.STOCK_TRANS_STATUS.EXPORT_ORDER);
        stockTransActionDTO.setStatus(Const.STOCK_TRANS_STATUS.EXPORT_ORDER);

        //Kho 3 mien
        flagStockDTO.setRegionExportStatus(Const.STOCK_TRANS_STATUS.EXPORT_ORDER);
        flagStockDTO.setRegionImportStatus(Const.STOCK_TRANS_STATUS.IMPORT_ORDER);

        //Prefix ma phieu xuat, nhap kho 3 mien
        flagStockDTO.setPrefixExportActionCode(Const.PREFIX_REGION.LX);
        flagStockDTO.setPrefixImportActionCode(Const.PREFIX_REGION.LN);

        //Tru so luong dap ung kho xuat
        flagStockDTO.setExportStock(true);
        flagStockDTO.setExpAvailableQuantity(Const.STOCK_TOTAL.MINUS_QUANTITY);
        //lap lenh
        flagStockDTO.setPrefixActionCode(Const.STOCK_TRANS.TRANS_CODE_LX);

        //giao dich xuat tu cap tren xuong cap duoi
        stockTransDTO.setFromStock(Const.FROM_STOCK.FROM_SENIOR);
    }

    @Override
    public void doValidate(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, List<StockTransDetailDTO> lstStockTransDetail) throws LogicException, Exception {

        //Validate cac truong trong giao dich
        baseValidateService.doOrderValidate(stockTransDTO);

        //Validate ky voffice
        baseValidateService.doSignVofficeValidate(stockTransDTO);

        //Validate han muc kho nhan
        baseValidateService.doDebitValidate(stockTransDTO, lstStockTransDetail);

        //validate hang hoa ko vuot qua tong han muc kho xuat--thaont19
        baseValidateService.doQuantityAvailableValidate(stockTransDTO,lstStockTransDetail);

        //Neu la lap lenh xuat cho nhan vien : Validate ngay nop tien cua nhan vien
        if (DataUtil.safeEqual(stockTransDTO.getToOwnerType(), Const.OWNER_TYPE.STAFF_LONG)) {
            baseValidateService.findTransExpiredPay(stockTransDTO.getToOwnerId());
        }
    }


    @Override
    public void doSaveStockTransSerial(StockTransDTO stockTransDTO, List<StockTransDetailDTO> lstStockTransDetail) throws Exception {

    }

    @Override
    public void doSaveStockGoods(StockTransDTO stockTransDTO, List<StockTransDetailDTO> lstStockTransDetail, FlagStockDTO flagStockDTO) throws Exception {

    }
}
