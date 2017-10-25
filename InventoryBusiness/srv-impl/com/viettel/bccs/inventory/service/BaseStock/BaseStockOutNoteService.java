package com.viettel.bccs.inventory.service.BaseStock;

import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.FlagStockDTO;
import com.viettel.bccs.inventory.dto.StockTransActionDTO;
import com.viettel.bccs.inventory.dto.StockTransDTO;
import com.viettel.bccs.inventory.dto.StockTransDetailDTO;
import com.viettel.bccs.inventory.service.StaffService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.ErrorCode;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by thetm1 on 9/12/2015.
 * Description Lap phieu xuat kho (chua lap lenh, dung cho Lap phieu xuat kho cho cap tren)
 * Detail + Insert stock_trans
 * + Insert stock_trans_detail
 * + Insert stock_trans_action
 * + Khong luu stock_trans_serial (-)
 * + Cap nhat so luong dap ung stock_total
 * + Khong cap nhat chi tiet serial (doSaveStockGoods) (-)
 */

@Service
public class BaseStockOutNoteService extends BaseStockService {

    public static final Logger logger = Logger.getLogger(BaseStockOutNoteService.class);

    @Autowired
    private BaseValidateService baseValidateService;

    @Autowired
    private StaffService staffService;


    @Override
    public void doPrepareData(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, FlagStockDTO flagStockDTO) throws Exception {

        stockTransDTO.setNote(stockTransDTO.getNote() != null ? stockTransDTO.getNote().trim() : null);
        stockTransDTO.setStockBase(stockTransDTO.getStockBase() != null ? stockTransDTO.getStockBase().trim() : null);
        //Cap nhat trang thai giao dich la lap lenh xuat
        stockTransDTO.setStatus(Const.STOCK_TRANS_STATUS.EXPORT_NOTE);
        stockTransActionDTO.setStatus(Const.STOCK_TRANS_STATUS.EXPORT_NOTE);

        //Tru so luong dap ung kho xuat
        flagStockDTO.setExportStock(true);
        flagStockDTO.setExpAvailableQuantity(Const.STOCK_TOTAL.MINUS_QUANTITY);

        //giao dich xuat tu cap duoi len cap tren
        stockTransDTO.setFromStock(Const.FROM_STOCK.FROM_UNDERLYING);

        //Phieu xuat kho
        flagStockDTO.setPrefixActionCode(Const.STOCK_TRANS.TRANS_CODE_PX);
    }

    @Override
    public void doValidate(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, List<StockTransDetailDTO> lstStockTransDetail) throws LogicException, Exception {

        if (!staffService.validateTransCode(stockTransActionDTO.getActionCode(), stockTransDTO.getStaffId(), Const.STOCK_TRANS_TYPE.EXPORT)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.expNote.invalid");
        }
        //Validate cac truong trong giao dich
        baseValidateService.doOrderValidate(stockTransDTO);

        //Validate ky voffice
        baseValidateService.doSignVofficeValidate(stockTransDTO);

        //Validate han muc kho nhan
        baseValidateService.doDebitValidate(stockTransDTO, lstStockTransDetail);

        //validate hang hoa ko vuot qua tong han muc kho xuat--thaont19
        baseValidateService.doQuantityAvailableValidate(stockTransDTO,lstStockTransDetail);
    }


    @Override
    public void doSaveStockTransSerial(StockTransDTO stockTransDTO, List<StockTransDetailDTO> lstStockTransDetail) throws Exception {

    }

    @Override
    public void doSaveStockGoods(StockTransDTO stockTransDTO, List<StockTransDetailDTO> lstStockTransDetail, FlagStockDTO flagStockDTO) throws Exception {

    }
}
