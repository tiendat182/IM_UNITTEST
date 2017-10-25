package com.viettel.bccs.inventory.service.BaseStock;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.model.StockTransDetail;
import com.viettel.bccs.inventory.service.*;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.extjs.FilterRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by HaiNT41 on 1/14/2016.
 * Service lap phieu nhap kho tu nhan vien
 */

@Service
public class BaseStockStaffNoteService extends BaseStockService {

    public static final Logger logger = Logger.getLogger(BaseStockStaffImpService.class);

    @Autowired
    private BaseValidateService baseValidateService;
    @Autowired
    private StockTransActionService stockTransActionService;
    @Autowired
    private StockTransService stockTransService;
    @Autowired
    private StockTransDetailService stockTransDetailService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private StaffService staffService;
    @Autowired
    private ReasonService reasonService;
    @Autowired
    private StockTransVofficeService stockTransVofficeService;

    @Override
    public void doPrepareData(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, FlagStockDTO flagStockDTO) throws LogicException, Exception {

        //Cap nhat trang thai giao dich la lap lenh xuat
        stockTransDTO.setStatus(Const.STOCK_TRANS_STATUS.IMPORT_NOTE);
        stockTransActionDTO.setStatus(Const.STOCK_TRANS_STATUS.IMPORT_NOTE);

        //Phieu xuat kho
        flagStockDTO.setPrefixActionCode(Const.STOCK_TRANS.TRANS_CODE_PN);
    }

    @Override
    public void doValidate(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, List<StockTransDetailDTO> lstStockTransDetail) throws LogicException, Exception {

        //Validate cac thong tin giao dich phai ton tai
        if (DataUtil.isNullObject(stockTransDTO) || DataUtil.isNullObject(stockTransActionDTO)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "export.order.stock.require.msg");
        }
        if(DataUtil.isNullOrEmpty(lstStockTransDetail)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.stockTransDetail.empty");
        }

        //Kiem tra giao dich stockTransDTO co ton tai trong DB khong
        StockTransDTO oldStockTransDTO = stockTransService.findOne(stockTransDTO.getStockTransId());
        if (DataUtil.isNullObject(oldStockTransDTO)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "export.order.stock.require.msg");
        }

        //Kiem tra stockTransAction
        StockTransActionDTO oldStockTransActionDTO = stockTransActionService.findOne(stockTransDTO.getStockTransActionId());
        if (DataUtil.isNullObject(oldStockTransActionDTO)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "export.order.stock.require.msg");
        }

        //Kiem tra stockTransDetail
        List<StockTransDetailDTO> oldLstStockTransDetail = stockTransDetailService.findByFilter(Lists.newArrayList(
                new FilterRequest(StockTransDetail.COLUMNS.STOCKTRANSID.name(), FilterRequest.Operator.EQ,
                        stockTransDTO.getStockTransId())));
        if (DataUtil.isNullOrEmpty(oldLstStockTransDetail)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "export.order.stock.require.msg");
        }

        //Validate trang thai giao dich phai o trang thai da lap lenh
        if (!DataUtil.safeEqual(Const.STOCK_TRANS_STATUS.IMPORT_ORDER, oldStockTransDTO.getStatus())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.order.status.invalid");
        }

        //Validate cac truong trong giao dich
        //Validate kho nhan vien xuat
        if (DataUtil.isNullOrZero(stockTransDTO.getFromOwnerId()) || DataUtil.isNullOrZero(stockTransDTO.getFromOwnerType())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "export.order.stock.require.msg");
        }

        List<VShopStaffDTO> exportStaffList = shopService.getListShopStaff(stockTransDTO.getFromOwnerId(), stockTransDTO.getFromOwnerType().toString(), Const.VT_UNIT.VT);
        if (DataUtil.isNullOrEmpty(exportStaffList)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.fromStock.notExists");
        }

        //Validate kho nhan
        if (DataUtil.isNullOrZero(stockTransDTO.getToOwnerId()) || DataUtil.isNullOrZero(stockTransDTO.getToOwnerType())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "export.order.stock.require.msg");
        }

        List<VShopStaffDTO> importShopList = shopService.getListShopStaff(stockTransDTO.getToOwnerId(), stockTransDTO.getToOwnerType().toString(), Const.VT_UNIT.VT);
        if (DataUtil.isNullOrEmpty(importShopList)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.toStock.notExists");
        }

        //Kho nhan trong phieu xuat phai bang kho nhan trong lenh xuat
        if (!DataUtil.safeEqual(oldStockTransDTO.getToOwnerId(), stockTransDTO.getToOwnerId())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.impShop.note.order.equal");
        }

        //Kho xuat la cap tren lien ke kho nhan
        if (!DataUtil.safeEqual(exportStaffList.get(0).getParentShopId(), importShopList.get(0).getOwnerId())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.stock.notConsistent");
        }

        //Validate giao dich da ky voffice chua
        if (stockTransDTO.isSignVoffice()) {
            stockTransVofficeService.doSignedVofficeValidate(oldStockTransActionDTO);
        }

        //Validate dinh dang phieu nhap
        if (!staffService.validateTransCode(stockTransActionDTO.getActionCode(), stockTransDTO.getStaffId(), Const.STOCK_TRANS_TYPE.IMPORT)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.impNote.invalid");
        }
    }

    @Override
    public void doSaveStockTransDetail(StockTransDTO stockTransDTO, List<StockTransDetailDTO> lstStockTransDetail) throws Exception {

    }

    @Override
    public void doSaveStockTransSerial(StockTransDTO stockTransDTO, List<StockTransDetailDTO> lstStockTransDetail) throws Exception {

    }
}
