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
 * Created by thetm1 on 9/12/2015.
 * Description Lap phieu xuat kho (da lap lenh xuat)
 * Detail + Update stock_trans
 * + Khong luu stock_trans_detail (-)
 * + Insert stock_trans_action
 * + Khong luu stock_trans_serial (-)
 * + Khong cap nhat stock_total (-)
 * + Khong cap nhat chi tiet serial (doSaveStockGoods) (-)
 */

@Service
public class BaseStockNoteOrderService extends BaseStockService {

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

        //neu actionCode null thi lay ma phieu tu dong
        if(DataUtil.isNullObject(stockTransActionDTO.getActionCode())){
            StaffDTO staffDTO = staffService.getStaffByStaffCode(stockTransActionDTO.getCreateUser());
            stockTransActionDTO.setActionCode(staffService.getTransCode(Const.STOCK_TRANS.TRANS_CODE_PX, Const.STOCK_TRANS_TYPE.EXPORT, staffDTO));
        }
    }

    @Override
    public void doValidate(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, List<StockTransDetailDTO> lstStockTransDetail) throws LogicException, Exception {
        baseValidateService.doOrderValidate(stockTransDTO);
        //baseValidateService.doSignVofficeValidate(stockTransDTO);
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

        //Validate trang thai phai la da lap lenh
        if (!DataUtil.safeEqual(Const.STOCK_TRANS_STATUS.EXPORT_ORDER, stockTrans.getStatus())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.order.status.invalid");
        }

        //4.Kiem tra stockTransAction
        StockTransActionDTO oldStockTransActionDTO = stockTransActionService.findOne(stockTransActionDTO.getStockTransActionId());
        if (DataUtil.isNullObject(oldStockTransActionDTO) || !DataUtil.safeEqual(oldStockTransActionDTO.getStatus(), Const.STOCK_TRANS_STATUS.EXPORT_ORDER)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "export.stock.code.export.not.found");
        }

        //5. Check trang thai ki voffice cua giao dich cu
        if (DataUtil.safeEqual(Const.SIGN_VOFFICE, oldStockTransActionDTO.getSignCaStatus())) {
            stockTransVofficeService.doSignedVofficeValidate(oldStockTransActionDTO);
        }

        //6.Check giao dich moi co ky voffice khong
        if (stockTrans.isSignVoffice()) {
            baseValidateService.doSignVofficeValidate(stockTransDTO);
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
