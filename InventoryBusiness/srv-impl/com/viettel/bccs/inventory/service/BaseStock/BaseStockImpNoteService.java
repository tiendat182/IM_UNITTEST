package com.viettel.bccs.inventory.service.BaseStock;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.model.StockTransDetail;
import com.viettel.bccs.inventory.service.StaffService;
import com.viettel.bccs.inventory.service.StockTransDetailService;
import com.viettel.bccs.inventory.service.StockTransSerialService;
import com.viettel.bccs.inventory.service.StockTransService;
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
 * Description Nhap kho (da lap phieu nhap)
 * Detail + Update stock_trans
 * + Khong luu stock_trans_detail (-)
 * + Insert stock_trans_action
 * + Khong luu stock_trans_serial (-)
 * + Cap nhat stock_total
 * + Cap nhat chi tiet serial (doSaveStockGoods)
 */

@Service
public class BaseStockImpNoteService extends BaseStockService {

    public static final Logger logger = Logger.getLogger(BaseStockImpNoteService.class);

    @Autowired
    private BaseValidateService baseValidateService;
    @Autowired
    private StockTransService stockTransService;
    @Autowired
    private StockTransDetailService stockTransDetailService;
    @Autowired
    private StockTransSerialService stockTransSerialService;
    @Autowired
    private StaffService staffService;

    @Override
    public void doPrepareData(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, FlagStockDTO flagStockDTO) throws Exception {

        //Cap nhat trang thai giao dich la lap lenh xuat
        stockTransDTO.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
        stockTransActionDTO.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);

        //Cong so luong kho nhan
        if (DataUtil.isNullOrZero(stockTransDTO.getExchangeStockId())) {
            flagStockDTO.setImportStock(true);
            flagStockDTO.setImpAvailableQuantity(Const.STOCK_TOTAL.PLUS_QUANTITY);
            flagStockDTO.setImpCurrentQuantity(Const.STOCK_TOTAL.PLUS_QUANTITY);
        }

        //cap nhat trang thai stock_x ve da nhap (status=3 --> status=1)
        flagStockDTO.setOldStatus(Const.STOCK_GOODS.STATUS_CONFIRM);
        flagStockDTO.setNewStatus(Const.STOCK_GOODS.STATUS_NEW);

        //Cap nhat serial ve kho nhan
        flagStockDTO.setUpdateOwnerId(true);

        //Kho 3 mien
        flagStockDTO.setRegionExportStatus(Const.STOCK_TRANS_STATUS.EXPORTED);
        flagStockDTO.setRegionImportStatus(Const.STOCK_TRANS_STATUS.IMPORTED);

        //set null
        stockTransActionDTO.setActionCode(null);
        stockTransActionDTO.setNote(null);

    }

    @Override
    public void doValidate(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, List<StockTransDetailDTO> lstStockTransDetail) throws LogicException, Exception {
        baseValidateService.doOrderValidateCommon(stockTransDTO, Const.VT_UNIT.VT);

        //Kiem tra giao dich stockTransDTO co ton tai trong DB khong
        StockTransDTO oldStockTransDTO = stockTransService.findOne(stockTransDTO.getStockTransId());
        if (DataUtil.isNullObject(oldStockTransDTO)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.import.note.stock.trans.not.exist");
        }
        //kiem tra trang thai hop le
        if (!DataUtil.safeEqual(Const.STOCK_TRANS_STATUS.IMPORT_NOTE, oldStockTransDTO.getStatus())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.import.stock.status.invalid");
        }
        //kiem tra ds hang hoa khong de trong
        if (DataUtil.isNullOrEmpty(lstStockTransDetail)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.trans.staff.export.detailRequired");
        }
        //Validate ky voffice
        baseValidateService.doSignVofficeValidate(stockTransDTO);
        //Validate han muc kho nhan
        baseValidateService.doDebitValidate(stockTransDTO, lstStockTransDetail);
    }

//    @Override
//    public StockTransDTO doSaveStockTrans(StockTransDTO stockTransDTO) throws Exception {
//        StockTransDTO transDTOImport = DataUtil.cloneBean(stockTransDTO);
//        //Truong hop update khi lap phieu nhap
//        if (transDTOImport.getStockTransId() != null) {
//            StockTransDTO stockTransToUpdate = stockTransService.findOneLock(stockTransDTO.getStockTransId());
//
//            //Neu trang thai stockTrans da duoc cap nhat thi thong bao loi
//            if (DataUtil.safeEqual(stockTransDTO.getStatus(), stockTransToUpdate.getStatus())) {
//                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.stockTrans.updated");
//            }
//
//            stockTransToUpdate.setStatus(stockTransDTO.getStatus());
//            stockTransService.save(stockTransToUpdate);
//            transDTOImport.setCreateDatetime(stockTransDTO.getCreateDatetime());//Lay lai de ghi log stock_trans_action
//            return transDTOImport;
//        }
//        //Truong hop insert giao dich kho 3 mien
//        StockTransDTO stockTrans = stockTransService.save(stockTransDTO);
//        transDTOImport.setStockTransId(stockTrans.getStockTransId());
//        return transDTOImport;
//
//    }

    @Override
    public void doSaveStockTransDetail(StockTransDTO stockTransDTO, List<StockTransDetailDTO> lstStockTransDetail) throws Exception {
        //do nothing
    }

    @Override
    public void doSaveStockTransSerial(StockTransDTO stockTransDTO, List<StockTransDetailDTO> lstStockTransDetail) throws Exception {
        //Luu giao dich xuat kho 3 mien hoac xuat dieu chuyen
        if (DataUtil.isNullOrZero(stockTransDTO.getRegionStockTransId())) {
            return;
        }
        StockTransDTO stockTrans = stockTransService.findOne(stockTransDTO.getRegionStockTransId());

        List<StockTransDetailDTO> lstStockTransDetailRegion = stockTransDetailService.findByFilter(Lists.newArrayList(
                new FilterRequest(StockTransDetail.COLUMNS.STOCKTRANSID.name(), FilterRequest.Operator.EQ,
                        stockTrans.getStockTransId())));

        if (DataUtil.isNullOrEmpty(lstStockTransDetailRegion)) {
            return;
        }

        for (StockTransDetailDTO stockTransDetail : lstStockTransDetail) {
//            List<StockTransSerialDTO> lstStockTransSerial = stockTransDetail.getLstStockTransSerial();
            List<StockTransSerialDTO> lstStockTransSerial = stockTransSerialService.findByStockTransDetailId(stockTransDetail.getStockTransDetailId());
            if (!DataUtil.isNullOrEmpty(lstStockTransSerial)) {
                for (StockTransSerialDTO stockTransSerial : lstStockTransSerial) {
                    stockTransSerial.setStockTransSerialId(null); // luon insert

                    StockTransDetailDTO stocktransDetail = findStockTransDetailRegion(lstStockTransDetailRegion, stockTransDetail.getProdOfferId(), stockTransDetail.getStateId());
                    stockTransSerial.setStockTransDetailId(stocktransDetail.getStockTransDetailId());
                    stockTransSerial.setStockTransId(stocktransDetail.getStockTransId());
                    stockTransSerial.setCreateDatetime(stockTransDTO.getCreateDatetime());
                    stockTransSerialService.save(stockTransSerial);
                }
            }
        }
    }

    private StockTransDetailDTO findStockTransDetailRegion(List<StockTransDetailDTO> lstStockTransDetail, Long prodOfferId, Long stateId) {
        for (StockTransDetailDTO stockTransDetail : lstStockTransDetail) {
            if (DataUtil.safeEqual(stockTransDetail.getProdOfferId(), prodOfferId)
                    && DataUtil.safeEqual(stockTransDetail.getStateId(), stateId)) {
                return stockTransDetail;
            }
        }
        return null;
    }


}
