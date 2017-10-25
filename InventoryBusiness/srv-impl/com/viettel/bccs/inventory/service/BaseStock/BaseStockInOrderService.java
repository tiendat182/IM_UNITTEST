package com.viettel.bccs.inventory.service.BaseStock;

import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.ShopService;
import com.viettel.bccs.inventory.service.StockTransActionService;
import com.viettel.bccs.inventory.service.StockTransDetailService;
import com.viettel.bccs.inventory.service.StockTransService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DateUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.RequiredRoleMap;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by thetm1 on 9/12/2015.
 * Description Lap lenh nhap kho tu cap duoi
 * Detail + Update stock_trans
 * + Khong luu stock_trans_detail (-)
 * + Insert stock_trans_action
 * + Khong luu stock_trans_serial (-)
 * + Khong Cap nhat stock_total (-)
 * + Khong cap nhat chi tiet serial (doSaveStockGoods) (-)
 */

@Service
public class BaseStockInOrderService extends BaseStockService {

    public static final Logger logger = Logger.getLogger(BaseStockInOrderService.class);

    @Autowired
    private StockTransService stockTransService;
    @Autowired
    private StockTransActionService stockTransActionService;
    @Autowired
    private BaseValidateService baseValidateService;
    @Autowired
    private StockTransDetailService stockTransDetailService;
    @Autowired
    private ShopService shopService;

    @Override
    public void doPrepareData(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, FlagStockDTO flagStockDTO) throws Exception {

        //Cap nhat trang thai giao dich la lap phieu nhap
        stockTransDTO.setStatus(Const.STOCK_TRANS_STATUS.IMPORT_ORDER);
        stockTransActionDTO.setStatus(Const.STOCK_TRANS_STATUS.IMPORT_ORDER);

        //Lenh nhap kho
        flagStockDTO.setPrefixActionCode(Const.STOCK_TRANS.TRANS_CODE_LN);

        //Kho 3 mien
        flagStockDTO.setRegionExportStatus(Const.STOCK_TRANS_STATUS.EXPORT_ORDER);
        flagStockDTO.setRegionImportStatus(Const.STOCK_TRANS_STATUS.IMPORT_ORDER);

        //Prefix ma phieu xuat, nhap kho 3 mien
        flagStockDTO.setPrefixExportActionCode(Const.PREFIX_REGION.LX);
        flagStockDTO.setPrefixImportActionCode(Const.PREFIX_REGION.LN);
    }

    @Override
    public void doValidate(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, List<StockTransDetailDTO> lstStockTransDetail) throws LogicException, Exception {
        baseValidateService.doOrderValidateCommon(stockTransDTO, Const.VT_UNIT.VT);

        if (stockTransActionDTO.getActionCode() != null && stockTransActionDTO.getActionCode().length() > 50) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.import.order.action.code.over.maxlength");
        }

        //Kiem tra giao dich stockTransDTO co ton tai trong DB khong
        StockTransDTO oldStockTransDTO = stockTransService.findOne(stockTransDTO.getStockTransId());
        if (DataUtil.isNullObject(oldStockTransDTO)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.import.note.stock.trans.not.exist");
        }
        //check trang thai =3 : dã xuat kho
        if (!(DataUtil.safeEqual(Const.STOCK_TRANS_STATUS.EXPORTED, oldStockTransDTO.getStatus()))) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.import.order.status.invalid");
        }

        //validate kho dieu chuyen
        validateExchangeStockTrans(stockTransDTO);

        //Validate ky voffice
        baseValidateService.doSignVofficeValidate(stockTransDTO);

        //Validate han muc kho nhan
        baseValidateService.doDebitValidate(stockTransDTO, lstStockTransDetail);
    }

    private void validateExchangeStockTrans(StockTransDTO stockTransDTO) throws Exception {
        if (DataUtil.isNullOrZero(stockTransDTO.getExchangeStockId())) {
            return;
        }
        Long fromOwnerId = stockTransDTO.getFromOwnerId();
        Long toOwnerId = stockTransDTO.getToOwnerId();
        Long exChangeOwnerId = stockTransDTO.getExchangeStockId();
        ShopDTO shopDTO = shopService.findOne(exChangeOwnerId);
        if (shopDTO == null || !Const.STATUS.ACTIVE.equals(shopDTO.getStatus())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "sell.store.no.info", exChangeOwnerId);
        }
        if (!DataUtil.safeEqual(toOwnerId, shopDTO.getParentShopId())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "tranfer.order.stock.require.exchangeid.msg");
        }
        if (DataUtil.safeEqual(fromOwnerId, exChangeOwnerId)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "tranfer.order.stock.require.exchangeid.invalid.msg");
        }
    }

    @Override
    public StockTransDTO doSaveStockTrans(StockTransDTO stockTransDTO) throws Exception {
        StockTransDTO transDTOImport = DataUtil.cloneBean(stockTransDTO);
        //Truong hop update khi lap lenh nhap
        if (transDTOImport.getStockTransId() != null) {
            StockTransDTO stockTransToUpdate = stockTransService.findOneLock(stockTransDTO.getStockTransId());

            //Neu trang thai stockTrans da duoc cap nhat thi thong bao loi
            if (DataUtil.safeEqual(stockTransDTO.getStatus(), stockTransToUpdate.getStatus())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.stockTrans.updated");
            }

            stockTransToUpdate.setStatus(stockTransDTO.getStatus());
            stockTransToUpdate.setImportReasonId(stockTransDTO.getImportReasonId());
            stockTransToUpdate.setImportNote(stockTransDTO.getNote());
            stockTransToUpdate.setRegionStockTransId(stockTransDTO.getRegionStockTransId());

            //out source add 5.7.2017
            stockTransToUpdate.setTransport(stockTransDTO.getTransport());
            stockTransToUpdate.setTransportSource(stockTransDTO.getTransportSource());
            //out source add 5.7.2017

            stockTransService.save(stockTransToUpdate);
            transDTOImport.setCreateDatetime(stockTransDTO.getCreateDatetime());//Lay lai de ghi log stock_trans_action
            return transDTOImport;
        }
        //Truong hop insert giao dich kho 3 mien
        StockTransDTO stockTrans = stockTransService.save(stockTransDTO);
        transDTOImport.setStockTransId(stockTrans.getStockTransId());
        return transDTOImport;

    }

    @Override
    public void doSaveStockTransDetail(StockTransDTO stockTransDTO, List<StockTransDetailDTO> lstStockTransDetail) throws Exception {

        //Neu la giao dich dieu chuyen hoac 3 mien thi luu detail giao dich nhap kho
        if (Const.STOCK_TRANS.IS_TRANSFER.equals(DataUtil.safeToLong(stockTransDTO.getStatus()))
                || Const.STOCK_TRANS.IS_AUTO_GEN_LOGISTIC_THREE_REGION.equals(DataUtil.safeToLong(stockTransDTO.getStatus()))) {
            for (StockTransDetailDTO stockTransDetail : lstStockTransDetail) {
                stockTransDetail.setStockTransDetailId(null);
                stockTransDetail.setStockTransId(stockTransDTO.getStockTransId());
                stockTransDetail.setCreateDatetime(stockTransDTO.getCreateDatetime());
                stockTransDetailService.save(stockTransDetail);
            }
        }
    }

    @Override
    public void doSaveStockTransSerial(StockTransDTO stockTransDTO, List<StockTransDetailDTO> lstStockTransDetail) throws Exception {
        //do nothing
    }

    @Override
    public void doSaveStockTotal(StockTransDTO stockTransDTO, List<StockTransDetailDTO> lstStockTransDetailDTO, FlagStockDTO flagStockDTO, StockTransActionDTO stockTransActionDTO) throws Exception {
        //do nothing
    }

    @Override
    public void doSaveStockGoods(StockTransDTO stockTransDTO, List<StockTransDetailDTO> lstStockTransDetail, FlagStockDTO flagStockDTO) throws Exception {
        //do nothing
    }

    @Override
    public void doSignVoffice(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, RequiredRoleMap
            requiredRoleMap, FlagStockDTO flagStockDTO) throws Exception {
        super.doSignVoffice(stockTransDTO, stockTransActionDTO, requiredRoleMap, flagStockDTO);
        //Ky voffice giao dich dieu chuyen
        if (!DataUtil.isNullOrZero(stockTransDTO.getExchangeStockId())) {
            StockTransActionDTO stockTransActionExchange = stockTransActionService.findOne(stockTransDTO.getExchangeStockTransActionId());
            if (!DataUtil.isNullObject(stockTransActionExchange)) {
                FlagStockDTO flagStockExchange = new FlagStockDTO();
                flagStockExchange.setPrefixActionCode(Const.STOCK_TRANS.TRANS_CODE_LX);
                super.doSignVoffice(stockTransDTO, stockTransActionExchange, requiredRoleMap, flagStockExchange);
            }
        }
    }
}
