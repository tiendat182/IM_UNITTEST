package com.viettel.bccs.inventory.service.BaseIsdn;

import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.BaseStock.BaseStockService;
import com.viettel.bccs.inventory.service.BaseStock.BaseValidateService;
import com.viettel.bccs.inventory.service.ReasonService;
import com.viettel.bccs.inventory.service.ShopService;
import com.viettel.bccs.inventory.service.StockTotalService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.RequiredRoleMap;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by hoangnt14 on 1/6/2016.
 * Description Lap lenh xuat so
 * Detail + Insert stock_trans
 * + Insert stock_trans_detail
 * + Insert stock_trans_action
 * + Khong luu stock_trans_serial (-)
 * + Khong Cap nhat so luong dap ung stock_total (-)
 * + Khong cap nhat chi tiet serial (doSaveStockGoods) (-)
 */
@Service
public class BaseIsdnOrderService extends BaseStockService {

    public static final Logger logger = Logger.getLogger(BaseIsdnOrderService.class);

    @Autowired
    private BaseValidateService baseValidateService;
    @Autowired
    private ReasonService reasonService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private StockTotalService stockTotalService;

    @Override
    public void doPrepareData(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, FlagStockDTO flagStockDTO) throws Exception {

        stockTransDTO.setNote(stockTransDTO.getNote() != null ? stockTransDTO.getNote().trim() : null);
        stockTransDTO.setStockBase(stockTransDTO.getStockBase() != null ? stockTransDTO.getStockBase().trim() : null);
        //Cap nhat trang thai giao dich la lap lenh xuat
        stockTransDTO.setStatus(Const.STOCK_TRANS_STATUS.EXPORT_ORDER);
        stockTransDTO.setIsAutoGen(null);
        stockTransDTO.setTransport(null);
        stockTransActionDTO.setStatus(Const.STOCK_TRANS_STATUS.EXPORT_ORDER);

        flagStockDTO.setPrefixActionCode(Const.PREFIX_TEMPLATE.LX);
    }

    @Override
    public void doValidate(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, List<StockTransDetailDTO> lstStockTransDetail) throws LogicException, Exception {

        //Validate cac truong trong giao dich
        doOrderValidate(stockTransDTO);

        //Validate ky voffice
        baseValidateService.doSignVofficeValidate(stockTransDTO);

        //Validate han muc kho nhan isdn
        doDebitIsdnValidate(stockTransDTO, lstStockTransDetail);
    }

    public void doOrderValidate(StockTransDTO stockTransDTO) throws LogicException, Exception {
        //1. validate kho xuat
        if (DataUtil.isNullOrZero(stockTransDTO.getFromOwnerId()) || DataUtil.isNullOrZero(stockTransDTO.getFromOwnerType())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "transfer.isdn.from.stock.not.null");
        }

        ShopDTO fromShop = shopService.findOne(stockTransDTO.getFromOwnerId());
        if (DataUtil.isNullObject(fromShop) || !fromShop.getStatus().equals(Const.STATUS_ACTIVE)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "transfer.isdn.from.stock.invalid");
        }

        //2. Validate kho nhan
        if (DataUtil.isNullOrZero(stockTransDTO.getToOwnerId()) || DataUtil.isNullOrZero(stockTransDTO.getToOwnerType())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "transfer.isdn.to.stock.not.null");
        }

        //TODO không chạy được vào line 90 vì nó đã được bắt ở line 78
        ShopDTO toShop = shopService.findOne(stockTransDTO.getToOwnerId());
        if (DataUtil.isNullObject(toShop) || !toShop.getStatus().equals(Const.STATUS_ACTIVE)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "transfer.isdn.to.stock.invalid");
        }

        if (DataUtil.safeEqual(stockTransDTO.getFromOwnerId(), stockTransDTO.getToOwnerId())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.fromtoStock.equal");
        }

        //4. kiem tra ly do co ton tai khong
        if (DataUtil.isNullOrZero(stockTransDTO.getReasonId())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "create.note.reason.export.require.msg");
        }

        ReasonDTO reason = reasonService.findOne(stockTransDTO.getReasonId());
        if (DataUtil.isNullObject(reason) || DataUtil.isNullOrZero(reason.getReasonId())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.reason.notExists");
        }

        //5. kiem tra ghi chu co thoa man khong
        if (!DataUtil.isNullOrEmpty(stockTransDTO.getNote()) && stockTransDTO.getNote().length() > 500) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.note.overLength");
        }
    }

    public void doDebitIsdnValidate(StockTransDTO stockTransDTO, List<StockTransDetailDTO> lstStockTransDetail) throws LogicException, Exception {
        if (DataUtil.isNullOrEmpty(lstStockTransDetail)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.lstStockTransDetail.empty");
        }

        Long totalQuantity = 0L;
        StockTotalDTO stockTotal = new StockTotalDTO();
        Long fromOwnerId = stockTransDTO.getFromOwnerId();
        Long fromOwnerType = stockTransDTO.getFromOwnerType();
        if (stockTransDTO.getRegionStockId() != null) {
            fromOwnerId = stockTransDTO.getRegionStockId();
        }
        stockTotal.setOwnerId(fromOwnerId);
        stockTotal.setOwnerType(fromOwnerType);


        for (StockTransDetailDTO stockTransDetail : lstStockTransDetail) {

            if (DataUtil.isNullOrZero(stockTransDetail.getProdOfferId())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "export.order.stock.inputText.require.msg");
            }

            if (DataUtil.isNullOrZero(stockTransDetail.getStateId())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.state.empty");
            }

            if (DataUtil.isNullOrZero(stockTransDetail.getQuantity())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "export.order.stock.number.format.msg");
            }

            if (stockTransDetail.getQuantity() < 0L) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.quantity.negative");
            }

            //Validate so luong trong kho co du so luong dap ung xuat kho
//            stockTotal.setProdOfferId(stockTransDetail.getProdOfferId());
//            stockTotal.setStateId(stockTransDetail.getStateId());
//            List<StockTotalDTO> lstStockTotal = stockTotalService.searchStockTotal(stockTotal);
//            if (DataUtil.isNullOrEmpty(lstStockTotal)) {
//                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.stockTotal.empty", stockTransDetail.getProdOfferName());
//            }
//
//            if (lstStockTotal.get(0).getAvailableQuantity() < stockTransDetail.getQuantity()) {
//                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.stockTotal.notAvailable");
//            }
            totalQuantity += stockTransDetail.getQuantity();
        }
        if (totalQuantity > 1000000L) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "from.to.number.over.maxlength");
        }
    }


    @Override
    public void doSaveStockTransSerial(StockTransDTO stockTransDTO, List<StockTransDetailDTO> lstStockTransDetail) throws Exception {

    }

    @Override
    public void doSaveStockGoods(StockTransDTO stockTransDTO, List<StockTransDetailDTO> lstStockTransDetail, FlagStockDTO flagStockDTO) throws LogicException, Exception {

    }

    @Override
    public void doSaveRegionStockTrans(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, FlagStockDTO flagStockDTO, List<StockTransDetailDTO> lstStockTransDetail) throws Exception {

    }

    @Override
    public void doSaveStockTotal(StockTransDTO stockTransDTO, List<StockTransDetailDTO> lstStockTransDetailDTO, FlagStockDTO flagStockDTO, StockTransActionDTO stockTransActionDTO) throws Exception {

    }

    @Override
    public void doSyncLogistic(StockTransDTO stockTransDTO, List<StockTransDetailDTO> lstStockTransDetail, RequiredRoleMap requiredRoleMap) throws Exception {

    }

    @Override
    public void doIncreaseStockNum(StockTransActionDTO stockTransActionDTO, String prefixActionCode, RequiredRoleMap requiredRoleMap) throws Exception {

    }

    @Override
    public void doUnlockUser(StockTransDTO stockTransDTO) throws Exception {
//        super.doUnlockUser(stockTransDTO);
    }
}