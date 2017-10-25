package com.viettel.bccs.inventory.service.BaseStock;

import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.*;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.RequiredRoleMap;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by hoangnt14 on 8/24/2016.
 */
@Service
public class BaseStockNoteChangeProductService extends BaseStockService {

    public static final Logger logger = Logger.getLogger(BaseStockNoteChangeProductService.class);

    @Autowired
    private StaffService staffService;

    @Autowired
    private BaseValidateService baseValidateService;

    @Autowired
    private ChangeModelTransService changeModelTransService;

    @Autowired
    private ChangeModelTransDetailService changeModelTransDetailService;

    @Autowired
    private ChangeModelTransSerialService changeModelTransSerialService;

    @Autowired
    private ProductOfferingService productOfferingService;

    @Autowired
    private StockTransActionService stockTransActionService;


    @Override
    public void doPrepareData(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, FlagStockDTO flagStockDTO) throws Exception {

        //Cap nhat trang thai giao dich la lap lenh xuat
        stockTransDTO.setStatus(Const.STOCK_TRANS_STATUS.EXPORTED);
        stockTransActionDTO.setStatus(Const.STOCK_TRANS_STATUS.EXPORT_NOTE);

        //tru SL dap ung va thuc te kho xuat
        flagStockDTO.setExportStock(true);
        flagStockDTO.setExpAvailableQuantity(Const.STOCK_TOTAL.MINUS_QUANTITY);
        flagStockDTO.setExpCurrentQuantity(Const.STOCK_TOTAL.MINUS_QUANTITY);

        //cap nhat serial voi status = 3
        flagStockDTO.setOldStatus(Const.STOCK_GOODS.STATUS_NEW);
        flagStockDTO.setNewStatus(Const.STOCK_GOODS.STATUS_LOCK);
        flagStockDTO.setPrefixActionCode(Const.STOCK_TRANS.TRANS_CODE_PX);

        //tang stock_num cua don vi va nhan vien

    }

    @Override
    public void doValidate(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, List<StockTransDetailDTO> lstStockTransDetail) throws LogicException, Exception {

        //validate ma phieu xuat
        StaffDTO staffDTO = staffService.findOne(stockTransActionDTO.getActionStaffId());
        if (DataUtil.isNullObject(staffDTO)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "staff.code.invalid.name");
        }
        String actionNoteCode = stockTransActionDTO.getActionCode();
        if (DataUtil.isNullOrEmpty(actionNoteCode)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "export.order.transCode.require.msg");
        } else if (actionNoteCode.trim().length() > 50) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "export.order.transCode.error.format.msg.product");
        } else if (DataUtil.safeEqual(staffDTO.getShopId(), Const.VT_SHOP_ID)
                && !staffService.validateTransCode(actionNoteCode, stockTransActionDTO.getActionStaffId(), Const.STOCK_TRANS_TYPE.EXPORT)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.expNote.invalid");
        }
        //5. kiem tra ghi chu co thoa man khong
        if (!DataUtil.isNullOrEmpty(stockTransDTO.getNote()) && stockTransDTO.getNote().getBytes("UTF-8").length > 500) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.note.overLength");
        }
        //check trung
        if (DataUtil.isNullOrEmpty(lstStockTransDetail)) {
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "validate.agent.retrieve.order.product");
        }
        for (StockTransDetailDTO stockTransDetailDTO : lstStockTransDetail) {
            ProductOfferingDTO productOfferingDTOOld = productOfferingService.findOne(stockTransDetailDTO.getProdOfferId());
            if (DataUtil.isNullObject(productOfferingDTOOld) || DataUtil.safeEqual(productOfferingDTOOld.getStatus(), Const.STATUS_INACTIVE)) {
                throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "create.note.change.product.old.product.esxit", stockTransDetailDTO.getProdOfferName());
            }
            // TODO nó lấy giá trị từ cùng 1 cái mock trả về để so sánh vs nhau nên không thực hiện được case này.
            ProductOfferingDTO productOfferingDTONew = productOfferingService.findOne(stockTransDetailDTO.getProdOfferIdChange());
            if (DataUtil.isNullObject(productOfferingDTONew) || DataUtil.safeEqual(productOfferingDTONew.getStatus(), Const.STATUS_INACTIVE)) {
                throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "create.note.change.product.new.product.esxit", productOfferingDTONew.getName());
            }
            //check trung ma tai chinh
            if (!DataUtil.safeEqual(productOfferingDTONew.getAccountingModelCode(), productOfferingDTOOld.getAccountingModelCode())) {
                throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "create.note.change.product.not.match.accounting.model.code", productOfferingDTONew.getCode(), productOfferingDTOOld.getCode());
            }
            if (DataUtil.safeEqual(productOfferingDTONew.getProductOfferingId(), productOfferingDTOOld.getProductOfferingId())) {
                throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "create.note.change.product.duplicate", productOfferingDTONew.getCode(), productOfferingDTOOld.getCode());
            }
        }
        //validate vuot qua han muc kho xuat
        baseValidateService.doQuantityAvailableValidate(stockTransDTO, lstStockTransDetail);
        //check trung mat hang va state_id
        if (lstStockTransDetail.size() >= 2) {
            for (int i = 0; i < lstStockTransDetail.size() - 1; i++) {
                for (int j = i + 1; j < lstStockTransDetail.size(); j++) {
                    if (DataUtil.safeEqual(lstStockTransDetail.get(i).getProdOfferId(), lstStockTransDetail.get(j).getProdOfferId())
                            && DataUtil.safeEqual(lstStockTransDetail.get(i).getStateId(), lstStockTransDetail.get(j).getStateId())) {
                        throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "create.note.change.product.duplicate.state", lstStockTransDetail.get(i).getProdOfferName(), productOfferingService.getStockStateName(lstStockTransDetail.get(i).getStateId()));
                    }
                }
            }
        }


    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public StockTransActionDTO executeStockTrans(StockTransDTO stockTransDTO, StockTransActionDTO
            stockTransActionDTO, List<StockTransDetailDTO> lstStockTransDetail, RequiredRoleMap requiredRoleMap) throws Exception {
        StockTransActionDTO transActionDTO = new StockTransActionDTO();
        transActionDTO = super.executeStockTrans(stockTransDTO, stockTransActionDTO,
                lstStockTransDetail, requiredRoleMap);
        StockTransActionDTO stockTransActionNew = DataUtil.cloneBean(transActionDTO);
        stockTransActionNew.setStockTransActionId(null);
        stockTransActionNew.setStatus(Const.STOCK_TRANS_STATUS.EXPORTED);
        stockTransActionNew.setSignCaStatus(null);
        stockTransActionNew.setActionCode(null);
        stockTransActionService.save(stockTransActionNew);
        //
        changeProductTrans(stockTransDTO, transActionDTO, lstStockTransDetail);
        return transActionDTO;
    }

    private void changeProductTrans(StockTransDTO stockTransDTO, StockTransActionDTO
            stockTransActionDTO, List<StockTransDetailDTO> lstStockTransDetail) throws Exception, LogicException {
        //Thong tin giao dich chuyen doi mat hang
        ChangeModelTransDTO changeModelTransDTO = new ChangeModelTransDTO();
        changeModelTransDTO.setStockTransId(stockTransActionDTO.getStockTransId());
        changeModelTransDTO.setFromOwnerId(stockTransDTO.getFromOwnerId());
        changeModelTransDTO.setFromOwnerType(stockTransDTO.getFromOwnerType());
        changeModelTransDTO.setToOwnerType(Const.OWNER_TYPE.SHOP_LONG);
        changeModelTransDTO.setToOwnerId(DataUtil.safeToLong(Const.VT_SHOP_ID));
        changeModelTransDTO.setCreateUserId(stockTransActionDTO.getActionStaffId());
        changeModelTransDTO.setStatus(0L);
        changeModelTransDTO.setCreateDate(stockTransDTO.getCreateDatetime());
        ChangeModelTransDTO changeModelTransSave = changeModelTransService.save(changeModelTransDTO);
        //detail
        for (StockTransDetailDTO stockTransDetailDTO : lstStockTransDetail) {
            ChangeModelTransDetailDTO changeModelTransDetailDTO = new ChangeModelTransDetailDTO();
            changeModelTransDetailDTO.setChangeModelTransId(changeModelTransSave.getChangeModelTransId());
            changeModelTransDetailDTO.setNewProdOfferId(stockTransDetailDTO.getProdOfferIdChange());
            changeModelTransDetailDTO.setOldProdOfferId(stockTransDetailDTO.getProdOfferId());
            changeModelTransDetailDTO.setStateId(stockTransDetailDTO.getStateId());
            changeModelTransDetailDTO.setQuantity(stockTransDetailDTO.getQuantity());
            changeModelTransDetailDTO.setNote(stockTransDTO.getNote());
            changeModelTransDetailDTO.setCreateDate(stockTransDTO.getCreateDatetime());
            ChangeModelTransDetailDTO modelTransDetailSave = changeModelTransDetailService.save(changeModelTransDetailDTO);
            List<StockTransSerialDTO> lstStockTransSerial = stockTransDetailDTO.getLstStockTransSerial();
            for (StockTransSerialDTO stockTransSerial : lstStockTransSerial) {
                ChangeModelTransSerialDTO changeModelTransSerialDTO = new ChangeModelTransSerialDTO();
                changeModelTransSerialDTO.setChangeModelTransDetailId(modelTransDetailSave.getChangeModelTransDetailId());
                changeModelTransSerialDTO.setFromSerial(stockTransSerial.getFromSerial());
                changeModelTransSerialDTO.setToSerial(stockTransSerial.getToSerial());
                changeModelTransSerialDTO.setQuantity(stockTransSerial.getQuantity());
                changeModelTransSerialDTO.setCreateDate(stockTransDTO.getCreateDatetime());
                changeModelTransSerialService.save(changeModelTransSerialDTO);
            }
        }
    }

}