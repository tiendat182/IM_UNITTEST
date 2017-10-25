package com.viettel.bccs.inventory.service.BaseStock;

import com.google.common.collect.Lists;
import com.viettel.bccs.im1.dto.StockTransIm1DTO;
import com.viettel.bccs.im1.service.StockTransIm1Service;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.model.StockTransDetail;
import com.viettel.bccs.inventory.service.OptionSetValueService;
import com.viettel.bccs.inventory.service.StockTransActionService;
import com.viettel.bccs.inventory.service.StockTransDetailService;
import com.viettel.bccs.inventory.service.StockTransService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.RequiredRoleMap;
import com.viettel.fw.common.util.extjs.FilterRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by HaiNT41 on 1/13/2016.
 * Ham huy giao dich
 */

@Service
public class BaseStockCancelTransService extends BaseStockService {

    public static final Logger logger = Logger.getLogger(BaseStockOutOrderService.class);

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
        stockTransActionDTO.setActionCode(null);
        stockTransActionDTO.setSignCaStatus(null);
        stockTransActionDTO.setActionCodeShop(null);
        if (!DataUtil.isNullObject(stockTransDTO.getRegionStockTransId())) {
            StockTransDTO regionStockTransDTO = stockTransService.findOne(stockTransDTO.getRegionStockTransId());
            if (!DataUtil.isNullObject(regionStockTransDTO)) {
                stockTransDTO.setRegionStockId(regionStockTransDTO.getFromOwnerId());
                flagStockDTO.setRegionImportStatus(Const.STOCK_TRANS_STATUS.CANCEL);
            }
        }
        //Cong lai so luong dap ung kho xuat
        flagStockDTO.setExportStock(true);
        flagStockDTO.setExpAvailableQuantity(Const.STOCK_TOTAL.PLUS_QUANTITY);
    }

    @Override
    public void doValidate(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, List<StockTransDetailDTO> lstStockTransDetail) throws LogicException, Exception {

        //Validate cac thong tin giao dich phai ton tai
        if (DataUtil.isNullObject(stockTransDTO) || DataUtil.isNullObject(stockTransActionDTO)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "export.order.stock.require.msg");
        }
        //Kiem tra giao dich stockTransDTO co ton tai trong DB khong
        StockTransDTO oldStockTransDTO = stockTransService.findOne(stockTransDTO.getStockTransId());
        if (DataUtil.isNullObject(oldStockTransDTO)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "export.order.stock.require.msg");
        }

        oldStockTransStatus = oldStockTransDTO.getStatus();

        //Kiem tra stockTransAction
        if (DataUtil.isNullObject(stockTransActionService.findOne(stockTransActionDTO.getStockTransActionId()))) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "export.order.stock.require.msg");
        }

        //Kiem tra stockTransDetail
        List<StockTransDetailDTO> lst = stockTransDetailService.findByFilter(Lists.newArrayList(
                new FilterRequest(StockTransDetail.COLUMNS.STOCKTRANSID.name(), FilterRequest.Operator.EQ, stockTransDTO.getStockTransId())));
        if (DataUtil.isNullOrEmpty(lst)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "export.order.stock.require.msg");
        }

        lstStockTransDetail = Lists.newArrayList();
        lstStockTransDetail.addAll(lst);

        //Validate thong tin chung c?a giao dich huy
        baseValidateService.doOrderValidate(stockTransDTO);

        //Khong duoc huy giao dich kho 3 mien
//        if (!DataUtil.isNullObject(stockTransActionDTO.getRegionOwnerId())) {
//            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.three.region.effectInvalid");
//        }

        //Giao dich phai o trang thai da lap lenh/phieu
        if (!(DataUtil.safeEqual(Const.STOCK_TRANS_STATUS.EXPORT_ORDER, oldStockTransDTO.getStatus())
                || DataUtil.safeEqual(Const.STOCK_TRANS_STATUS.EXPORT_NOTE, oldStockTransDTO.getStatus()))) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.cancel.trans.status.invalid");
        }

        //Khong duoc phep huy giao dich dieu chuyen hang chi nhanh : isAutoGen = 3
        if (DataUtil.safeEqual(Const.STOCK_TRANS.IS_TRANSFER, stockTransDTO.getIsAutoGen())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.cancel.trans.transfer.invalid");
        }
    }

    @Override
    public void doSaveStockTransDetail(StockTransDTO stockTransDTO, List<StockTransDetailDTO> lstStockTransDetail) throws Exception {

    }

    @Override
    public void doSaveStockTransSerial(StockTransDTO stockTransDTO, List<StockTransDetailDTO> lstStockTransDetail) throws Exception {

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

    @Override
    public void doSaveRegionStockTrans(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, FlagStockDTO flagStockDTO, List<StockTransDetailDTO> lstStockTransDetail) throws Exception {

        if (DataUtil.isNullOrZero(stockTransDTO.getRegionStockId())) {
            return;
        }

        StockTransDTO exportDTO = new StockTransDTO();
        exportDTO.setStockTransId(stockTransDTO.getRegionStockTransId());
        exportDTO.setStatus(flagStockDTO.getRegionImportStatus());
        exportDTO = doSaveStockTrans(exportDTO);
        exportDTO.setCreateDatetime(stockTransDTO.getCreateDatetime());//Set lai cho truong hop update

        StockTransActionDTO exportActionDTO = DataUtil.cloneBean(stockTransActionDTO);
        exportActionDTO.setActionCode(null);
        exportActionDTO.setRegionOwnerId(stockTransDTO.getRegionStockId());
        exportActionDTO.setNote(flagStockDTO.getNote());
        exportActionDTO.setIsRegionExchange(true);
        doSaveStockTransAction(exportDTO, exportActionDTO);

        return;
    }
}

