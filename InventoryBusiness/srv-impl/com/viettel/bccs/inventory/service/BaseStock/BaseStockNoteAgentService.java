package com.viettel.bccs.inventory.service.BaseStock;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.model.StockTransAction;
import com.viettel.bccs.inventory.service.StaffService;
import com.viettel.bccs.inventory.service.StockTransActionService;
import com.viettel.bccs.inventory.service.StockTransService;
import com.viettel.bccs.inventory.service.StockTransVofficeService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.extjs.FilterRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by HungPM6 on 1/22/2016.
 */
@Service
public class BaseStockNoteAgentService extends BaseStockService {

    public static final Logger logger = Logger.getLogger(BaseStockNoteOrderService.class);
    @Autowired
    private StaffService staffService;
    @Autowired
    private StockTransService stockTransService;

    @Autowired
    private BaseValidateService baseValidateService;

    @Autowired
    private StockTransActionService stockTransActionService;

    @Autowired
    private StockTransVofficeService stockTransVofficeService;


    @Override
    public void doPrepareData(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, FlagStockDTO flagStockDTO) throws Exception {

        //Cap nhat trang thai giao dich la lap lenh xuat
        stockTransDTO.setStatus(Const.STOCK_TRANS_STATUS.EXPORT_NOTE);
        stockTransActionDTO.setStatus(Const.STOCK_TRANS_STATUS.EXPORT_NOTE);

        //Phieu xuat kho
        flagStockDTO.setPrefixActionCode(Const.STOCK_TRANS.TRANS_CODE_PX);

        //Kho 3 mien
        flagStockDTO.setRegionExportStatus(Const.STOCK_TRANS_STATUS.EXPORT_NOTE);
        flagStockDTO.setRegionImportStatus(Const.STOCK_TRANS_STATUS.IMPORT_NOTE);

        //Prefix ma phieu xuat, nhap kho 3 mien
        flagStockDTO.setPrefixExportActionCode(Const.PREFIX_REGION.PX);
        flagStockDTO.setPrefixImportActionCode(Const.PREFIX_REGION.PN);
    }

    @Override
    public void doValidate(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, List<StockTransDetailDTO> lstStockTransDetail) throws LogicException, Exception {
        StockTransActionDTO oldStockTransActionDTO = stockTransActionService.findOne(stockTransActionDTO.getStockTransActionId());
        baseValidateService.doOrderValidateAgent(stockTransDTO, oldStockTransActionDTO);
        baseValidateService.doSignVofficeValidate(stockTransDTO);
        //validate ma phieu xuat
        StaffDTO staffDTO = staffService.findOne(stockTransActionDTO.getActionStaffId());
        if (DataUtil.isNullObject(staffDTO)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "staff.code.invalid.name");
        }
        String actionNoteCode = stockTransActionDTO.getActionCode();
        if (DataUtil.isNullOrEmpty(actionNoteCode)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "export.order.transCode.require.msg");
        } else if (actionNoteCode.trim().length() > 50) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "export.order.transCode.error.format.msg");
        } else if (DataUtil.safeEqual(staffDTO.getShopId(), Const.VT_SHOP_ID)
                && !staffService.validateTransCode(actionNoteCode, stockTransActionDTO.getActionStaffId(), Const.STOCK_TRANS_TYPE.EXPORT)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.expNote.invalid");
        }
        List<FilterRequest> lsRequest = Lists.newArrayList();
        lsRequest.add(new FilterRequest(StockTransAction.COLUMNS.ACTIONCODE.name(), FilterRequest.Operator.EQ, actionNoteCode));

        //Validate stockTrans
        StockTransDTO stockTrans = stockTransService.findOne(stockTransDTO.getStockTransId());
        if (DataUtil.isNullObject(stockTrans)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "export.order.stocktrans.empty");
        }

        //4.Kiem tra stockTransAction

        if (DataUtil.isNullObject(oldStockTransActionDTO) || !DataUtil.safeEqual(oldStockTransActionDTO.getStatus(), Const.STOCK_TRANS_STATUS.EXPORT_ORDER)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "export.stock.code.export.not.found");
        }

//        StockTransDTO transDTO = stockTransService.findOne(stockTransDTO.getStockTransId());
        //5. Check trang thai ki voffice
        if (DataUtil.safeEqual(oldStockTransActionDTO.getSignCaStatus(), "2")) {
            //Validate giao dich da ky voffice chua
            stockTransVofficeService.doSignedVofficeValidate(oldStockTransActionDTO);
        }

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
    public void doSaveStockGoods(StockTransDTO stockTransDTO, List<StockTransDetailDTO> lstStockTransDetail, FlagStockDTO flagStockDTO) throws Exception {

    }
}
