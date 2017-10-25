package com.viettel.bccs.inventory.service.BaseStock;

import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.StaffService;
import com.viettel.bccs.inventory.service.StockTransActionService;
import com.viettel.bccs.inventory.service.StockTransService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.RequiredRoleMap;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thetm1 on 9/12/2015.
 * Description Lap phieu nhap kho tu cap tren (khong qua lap lenh nhap)
 * Detail + Update stock_trans
 * + Khong luu stock_trans_detail (-)
 * + Insert stock_trans_action
 * + Khong luu stock_trans_serial (-)
 * + Khong Cap nhat stock_total (-)
 * + Khong cap nhat chi tiet serial (doSaveStockGoods) (-)
 */

@Service
public class BaseStockInNoteService extends BaseStockService {

    public static final Logger logger = Logger.getLogger(BaseStockInNoteService.class);
    @Autowired
    private StockTransService stockTransService;
    @Autowired
    private StockTransActionService stockTransActionService;
    @Autowired
    private BaseValidateService baseValidateService;
    @Autowired
    private StaffService staffService;


    @Override
    public void doPrepareData(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, FlagStockDTO flagStockDTO) throws Exception {

        //Cap nhat trang thai giao dich la lap phieu nhap
        stockTransDTO.setStatus(Const.STOCK_TRANS_STATUS.IMPORT_NOTE);
        stockTransActionDTO.setStatus(Const.STOCK_TRANS_STATUS.IMPORT_NOTE);

        //Phieu nhap kho
        flagStockDTO.setPrefixActionCode(Const.STOCK_TRANS.TRANS_CODE_PN);

        //Kho 3 mien
        flagStockDTO.setRegionExportStatus(Const.STOCK_TRANS_STATUS.EXPORT_NOTE);
        flagStockDTO.setRegionImportStatus(Const.STOCK_TRANS_STATUS.IMPORT_NOTE);

        //Prefix ma phieu xuat, nhap kho 3 mien
        flagStockDTO.setPrefixExportActionCode(Const.PREFIX_REGION.PX);
        flagStockDTO.setPrefixImportActionCode(Const.PREFIX_REGION.PN);

        //neu actionCode null thi lay ma phieu tu dong
        if (DataUtil.isNullObject(stockTransActionDTO.getActionCode())) {
            StaffDTO staffDTO = staffService.getStaffByStaffCode(stockTransActionDTO.getCreateUser());
            stockTransActionDTO.setActionCode(staffService.getTransCode(Const.STOCK_TRANS.TRANS_CODE_PN, Const.STOCK_TRANS_TYPE.IMPORT, staffDTO));
        }
    }

    @Override
    public void doValidate(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, List<StockTransDetailDTO> lstStockTransDetail) throws LogicException, Exception {
        baseValidateService.doOrderValidateCommon(stockTransDTO, Const.VT_UNIT.VT);

        if (stockTransActionDTO.getActionCode() != null && stockTransActionDTO.getActionCode().length() > 50) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.import.note.action.code.over.maxlength");
        }

        if (!staffService.validateTransCode(stockTransActionDTO.getActionCode(), stockTransActionDTO.getActionStaffId(), Const.STOCK_TRANS_TYPE.IMPORT)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.import.note.action.code.vt.invalid");
        }


        //Kiem tra giao dich stockTransDTO co ton tai trong DB khong
        StockTransDTO oldStockTransDTO = stockTransService.findOne(stockTransDTO.getStockTransId());
        if (DataUtil.isNullObject(oldStockTransDTO)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.import.note.stock.trans.not.exist");
        }
        //check trang thai =3 : dã xuat kho
        if (!(DataUtil.safeEqual(Const.STOCK_TRANS_STATUS.EXPORTED, oldStockTransDTO.getStatus()) ||
                DataUtil.safeEqual(Const.STOCK_TRANS_STATUS.IMPORT_ORDER, oldStockTransDTO.getStatus()))) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.import.note.status.invalid");
        }
        //check ma phieu nhap chua ton tai
//        List<StockTransActionDTO> listStockTransActionDTOs = stockTransActionService.findByFilter(Lists.newArrayList(
//                new FilterRequest(StockTransAction.COLUMNS.ACTIONCODE.name(), FilterRequest.Operator.EQ,
//                        stockTransActionDTO.getActionCode())));
//        if (!DataUtil.isNullOrEmpty(listStockTransActionDTOs)) {
//            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.import.note.action.code.exist");
//        }
        //kiem tra ds hang hoa khong de trong
        if (DataUtil.isNullOrEmpty(lstStockTransDetail)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.trans.staff.export.detailRequired");
        }
        //Validate ky voffice
        baseValidateService.doSignVofficeValidate(stockTransDTO);

        //Validate han muc kho nhan
        baseValidateService.doDebitValidate(stockTransDTO, new ArrayList());
    }

    @Override
    public StockTransDTO doSaveStockTrans(StockTransDTO stockTransDTO) throws Exception {
        StockTransDTO transDTOImport = DataUtil.cloneBean(stockTransDTO);
        //Truong hop update khi lap phieu nhap
        if (transDTOImport.getStockTransId() != null) {
            StockTransDTO stockTransToUpdate = stockTransService.findOneLock(stockTransDTO.getStockTransId());

            //Neu trang thai stockTrans da duoc cap nhat thi thong bao loi
            if (DataUtil.safeEqual(stockTransDTO.getStatus(), stockTransToUpdate.getStatus())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.stockTrans.updated");
            }

            stockTransToUpdate.setStatus(stockTransDTO.getStatus());
            if (!DataUtil.isNullOrZero(stockTransDTO.getImportReasonId())) {//dung cho lap phieu nhap voi cap tren
                stockTransToUpdate.setImportNote(stockTransDTO.getImportNote());
                stockTransToUpdate.setImportReasonId(stockTransDTO.getImportReasonId());
            }

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
        //do nothing
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
        if (!stockTransDTO.isSignVoffice()) {
            return;
        }
        if (!DataUtil.isNullOrZero(stockTransDTO.getExchangeStockId())) {
            StockTransActionDTO stockTransActionExchange = stockTransActionService.findOne(stockTransDTO.getExchangeStockTransActionId());
            if (!DataUtil.isNullObject(stockTransActionExchange)) {
                FlagStockDTO flagStockExchange = new FlagStockDTO();
                flagStockExchange.setPrefixActionCode(Const.STOCK_TRANS.TRANS_CODE_PX);
                stockTransActionExchange.setSignCaStatus(Const.SIGN_VOFFICE);
                stockTransActionService.save(stockTransActionExchange);
                super.doSignVoffice(stockTransDTO, stockTransActionExchange, requiredRoleMap, flagStockExchange);
            }
        }
    }
}
