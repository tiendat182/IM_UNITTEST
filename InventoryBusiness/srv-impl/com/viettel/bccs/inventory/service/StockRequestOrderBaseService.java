package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.model.StockRequestOrder;
import com.viettel.bccs.inventory.model.StockRequestOrderDetail;
import com.viettel.bccs.inventory.model.StockTransVoffice;
import com.viettel.bccs.inventory.service.BaseStock.BaseStockService;
import com.viettel.bccs.inventory.service.BaseStock.BaseValidateService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

/**
 * Created by hoangnt14 on 10/26/2016.
 */
@Service
public class StockRequestOrderBaseService extends BaseStockService {

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @Autowired
    private StockRequestOrderService stockRequestOrderService;

    @Autowired
    private StockRequestOrderDetailService stockRequestOrderDetailService;

    @Autowired
    private StockTransService stockTransService;

    @Autowired
    private StockTransActionService stockTransActionService;

    @Autowired
    private StockTransDetailService stockTransDetailService;

    @Autowired
    private StockTransSerialService stockTransSerialService;

    @Autowired
    private ProductOfferTypeService productOfferTypeService;

    @Autowired
    private ProductOfferingService productOfferingService;

    @Autowired
    private StaffService staffService;

    @Autowired
    private ReasonService reasonService;

    @Autowired
    private ShopService shopService;

    @Autowired
    private BaseValidateService baseValidateService;

    public void processCreateExpNote(Long stockRequestOrderId, Long fromOwnerId, Long toOwnerId) throws LogicException, Exception {
        StockRequestOrderDTO stockRequestOrderDTO = stockRequestOrderService.findOne(stockRequestOrderId);
        if (DataUtil.isNullObject(stockRequestOrderDTO)) {
            throw new LogicException("", "process.stock.request.order.not.found", stockRequestOrderId);
        }
        StaffDTO staffDTO = staffService.getStaffByStaffCode(stockRequestOrderDTO.getCreateUser());
        ReasonDTO reasonDTO = reasonService.getReason(Const.REASON_CODE.REASON_GOODS_REVOKE, Const.REASON_CODE.REASON_GOODS_REVOKE);
        ShopDTO shopDTO = shopService.findOne(toOwnerId);
        // Tao phieu xuat dieu chuyen tu CN --> VT
        Date currentDate = getSysDate(em);
        ShopDTO shopFromDTO = shopService.findOne(fromOwnerId);
        StockTransDTO stockTransDTO = getStockTransExpNote(fromOwnerId, Const.OWNER_TYPE.SHOP_LONG,
                Const.L_VT_SHOP_ID, Const.OWNER_TYPE.SHOP_LONG, currentDate);
        stockTransDTO.setNote(getTextParam("process.stock.request.order.note", shopFromDTO.getName(), shopDTO.getName()));
        stockTransDTO.setReasonId(reasonDTO.getReasonId());
        StockTransDTO stockTransSave = stockTransService.save(stockTransDTO);
        StockTransActionDTO stockTransActionDTO = getStockTransActionExpNote(currentDate, stockTransSave.getStockTransId());
        stockTransActionDTO.setActionStaffId(staffDTO.getStaffId());
        stockTransActionDTO.setCreateUser(staffDTO.getStaffCode());
        StockTransActionDTO stockTransActionSave = stockTransActionService.save(stockTransActionDTO);
        stockTransActionSave.setActionCode("PX_GOODS_REVOKE_" + stockTransActionSave.getStockTransActionId());
        stockTransActionService.save(stockTransActionSave);
        List<StockRequestOrderDetailDTO> lstOrderDetail = stockRequestOrderDetailService.getLstByStockRequestId(stockRequestOrderId,
                fromOwnerId, toOwnerId);
        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();
        for (StockRequestOrderDetailDTO stockRequestOrderDetailDTO : lstOrderDetail) {
            //detail
            StockTransDetailDTO stockTransDetailDTO = getDetailExpNote(currentDate, stockTransSave.getStockTransId(), stockRequestOrderDetailDTO);
            StockTransDetailDTO stockTransDetailSave = stockTransDetailService.save(stockTransDetailDTO);
            lstStockTransDetail.add(stockTransDetailSave);
            //Cap nhat export_stock_trans_id
            stockRequestOrderDetailDTO.setExportTransId(stockTransSave.getStockTransId());
            stockRequestOrderDetailDTO.setStatus(DataUtil.safeToLong(Const.STOCK_REQUEST_ORDER.STATUS_1_APPROVE));
            stockRequestOrderDetailService.save(stockRequestOrderDetailDTO);
        }
        baseValidateService.doQuantityAvailableValidate(stockTransSave, lstStockTransDetail);
        //Tru SL dap ung
        FlagStockDTO flagStockDTO = new FlagStockDTO();
        flagStockDTO.setExportStock(true);
        flagStockDTO.setExpAvailableQuantity(Const.STOCK_TOTAL.MINUS_QUANTITY);
        doSaveStockTotal(stockTransSave, lstStockTransDetail, flagStockDTO, stockTransActionSave);
    }

    public void processImportStock(Long stockTransId) throws LogicException, Exception {
        StockTransDTO stockTransImport = stockTransService.findOne(stockTransId);
        if (DataUtil.isNullObject(stockTransImport)) {
            throw new LogicException("", "stockTrans.validate.stockTrans.notExists", stockTransId);
        }
        Date currentDate = getSysDate(em);
        // Cap nhat da nhap kho
        stockTransImport.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
        stockTransService.save(stockTransImport);
        //Them moi stock_Trans_action
        StockTransActionDTO stockTransActionNoteExp = stockTransActionService.getStockTransActionByIdAndStatus(stockTransId, Lists.newArrayList(Const.STOCK_TRANS_STATUS.EXPORT_NOTE));
        StockTransActionDTO stockTransActionDTO = getStockTransActionExpNote(currentDate, stockTransId);
        stockTransActionDTO.setStatus(Const.STOCK_TRANS_STATUS.IMPORT_ORDER);
        stockTransActionDTO.setActionStaffId(stockTransActionNoteExp.getActionStaffId());
        stockTransActionDTO.setCreateUser(stockTransActionNoteExp.getCreateUser());
        StockTransActionDTO stockTransActionSave = stockTransActionService.save(stockTransActionDTO);
        stockTransActionSave.setActionCode("LN_GOODS_REVOKE_" + stockTransActionSave.getStockTransActionId());
        stockTransActionService.save(stockTransActionSave);
        stockTransActionDTO.setStatus(Const.STOCK_TRANS_STATUS.IMPORT_NOTE);
        stockTransActionSave = stockTransActionService.save(stockTransActionDTO);
        stockTransActionSave.setActionCode("PN_GOODS_REVOKE_" + stockTransActionSave.getStockTransActionId());
        stockTransActionDTO.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
        stockTransActionService.save(stockTransActionDTO);
        //Cap nhat ve kho VT
        //Lay cac giao dich chi tiet
        List<StockTransDetailDTO> lstDetail = stockTransDetailService.findByStockTransId(stockTransId);
        for (StockTransDetailDTO transDetailDTO : lstDetail) {
            ProductOfferingDTO productOfferingDTO = productOfferingService.findOne(transDetailDTO.getProdOfferId());
            ProductOfferTypeDTO productOfferTypeDTO = productOfferTypeService.findOne(productOfferingDTO.getProductOfferTypeId());
            transDetailDTO.setTableName(productOfferTypeDTO.getTableName());
            transDetailDTO.setProdOfferTypeId(productOfferTypeDTO.getProductOfferTypeId());
        }
        FlagStockDTO flagStockDTO = new FlagStockDTO();
        flagStockDTO.setOldStatus(Const.STOCK_GOODS.STATUS_CONFIRM);
        flagStockDTO.setNewStatus(Const.STOCK_GOODS.STATUS_CONFIRM);
        flagStockDTO.setUpdateOwnerId(true);
        doSaveStockGoods(stockTransImport, lstDetail, flagStockDTO);
        //Lap giao dich xuat hang tu VT ve CN yeu cau
        List<StockRequestOrderDetailDTO> lstOrderDetail = stockRequestOrderDetailService.getOrderDetailByStockTransId(stockTransId);
        StockTransDTO stockTransDTO = getStockTransExpNote(Const.L_VT_SHOP_ID, Const.OWNER_TYPE.SHOP_LONG,
                lstOrderDetail.get(0).getToOwnerId(), lstOrderDetail.get(0).getToOwnerType(), currentDate);
        stockTransDTO.setStatus(Const.STOCK_TRANS_STATUS.IMPORT_NOTE);
        stockTransDTO.setFromStockTransId(stockTransId);
        stockTransDTO.setImportReasonId(stockTransImport.getReasonId());
        StockTransDTO stockTransSave = stockTransService.save(stockTransDTO);
        //stock_trans_action 1,2,3,5
        //1
        StockTransActionDTO stockTransActionExpDTO = getStockTransActionExpNote(currentDate, stockTransSave.getStockTransId());
        stockTransActionExpDTO.setStatus(Const.STOCK_TRANS_STATUS.EXPORT_ORDER);
        stockTransActionExpDTO.setActionStaffId(stockTransActionNoteExp.getActionStaffId());
        stockTransActionExpDTO.setCreateUser(stockTransActionNoteExp.getCreateUser());
        StockTransActionDTO stockTransActionExpSave = stockTransActionService.save(stockTransActionExpDTO);
        stockTransActionExpSave.setActionCode("LX_GOODS_REVOKE_" + stockTransActionExpSave.getStockTransActionId());
        stockTransActionService.save(stockTransActionExpSave);
        //2
        stockTransActionExpDTO.setStatus(Const.STOCK_TRANS_STATUS.EXPORT_NOTE);
        stockTransActionExpSave = stockTransActionService.save(stockTransActionExpDTO);
        stockTransActionExpSave.setActionCode("PX_GOODS_REVOKE_" + stockTransActionExpSave.getStockTransActionId());
        stockTransActionService.save(stockTransActionExpSave);
        //3
        stockTransActionExpDTO.setStatus(Const.STOCK_TRANS_STATUS.EXPORTED);
        stockTransActionService.save(stockTransActionExpDTO);
        //5
        stockTransActionExpDTO.setStatus(Const.STOCK_TRANS_STATUS.IMPORT_NOTE);
        stockTransActionExpSave = stockTransActionService.save(stockTransActionExpDTO);
        stockTransActionExpSave.setActionCode("PN_GOODS_REVOKE_" + stockTransActionExpSave.getStockTransActionId());
        stockTransActionService.save(stockTransActionExpSave);
        //detail
        for (StockRequestOrderDetailDTO stockRequestOrderDetailDTO : lstOrderDetail) {
            StockTransDetailDTO stockTransDetailDTO = getDetailExpNote(currentDate, stockTransSave.getStockTransId(), stockRequestOrderDetailDTO);
            StockTransDetailDTO stockTransDetailSave = stockTransDetailService.save(stockTransDetailDTO);
            StockTransDetailDTO stockTransDetailExp = stockTransDetailService.getSingleDetail(stockTransId, stockRequestOrderDetailDTO.getProdOfferId()
                    , stockRequestOrderDetailDTO.getStateId());
            if (DataUtil.isNullObject(stockTransDetailExp)) {
                throw new LogicException("", "good.revoke.import.not.found.detail", stockTransId, stockRequestOrderDetailDTO.getProdOfferId(), stockRequestOrderDetailDTO.getStateId());
            }
            List<StockTransSerialDTO> lstSerial = stockTransSerialService.findByStockTransDetailId(stockTransDetailExp.getStockTransDetailId());
            if (DataUtil.isNullOrEmpty(lstSerial)) {
                throw new LogicException("", "good.revoke.import.not.found.serial", stockTransDetailExp.getStockTransDetailId());
            }
            for (StockTransSerialDTO stockTransSerialDTO : lstSerial) {
                StockTransSerialDTO stockTransSerialImp = getStockTransSerial(currentDate, stockTransDetailSave, stockTransSerialDTO);
                stockTransSerialService.save(stockTransSerialImp);
            }
            //cap nhat imp_trans_id
            stockRequestOrderDetailDTO.setImportTransId(stockTransSave.getStockTransId());
            stockRequestOrderDetailService.save(stockRequestOrderDetailDTO);
        }
    }

    private StockTransSerialDTO getStockTransSerial(Date currentDate, StockTransDetailDTO stockTransDetailDTO, StockTransSerialDTO stockTransSerialDTO) throws Exception {
        StockTransSerialDTO stockTransSerialImp = DataUtil.cloneBean(stockTransSerialDTO);
        stockTransSerialImp.setStockTransSerialId(null);
        stockTransSerialImp.setCreateDatetime(currentDate);
        stockTransSerialImp.setStockTransId(stockTransDetailDTO.getStockTransId());
        stockTransSerialImp.setStockTransDetailId(stockTransDetailDTO.getStockTransDetailId());
        stockTransSerialImp.setStateId(stockTransDetailDTO.getStateId());
        return stockTransSerialImp;
    }

    StockTransDTO getStockTransExpNote(Long fromOwnerId, Long fromOwnerType, Long toOwnerId, Long toOwnerType, Date currentDate) {
        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setFromOwnerId(fromOwnerId);
        stockTransDTO.setFromOwnerType(fromOwnerType);
        stockTransDTO.setToOwnerId(toOwnerId);
        stockTransDTO.setToOwnerType(toOwnerType);
        stockTransDTO.setCreateDatetime(currentDate);
        stockTransDTO.setStatus(Const.STOCK_TRANS_STATUS.EXPORT_NOTE);
        stockTransDTO.setStockTransType(Const.STOCK_TRANS_TYPE.EXPORT);
        stockTransDTO.setIsAutoGen(4L);
        return stockTransDTO;
    }

    private StockTransActionDTO getStockTransActionExpNote(Date currentDate, Long stockTransId) throws Exception {
        StockTransActionDTO stockTransActionExp = new StockTransActionDTO();
        stockTransActionExp.setCreateDatetime(currentDate);
        stockTransActionExp.setStockTransId(stockTransId);
        stockTransActionExp.setStatus(Const.STOCK_TRANS_STATUS.EXPORT_NOTE);
        return stockTransActionExp;
    }

    private StockTransDetailDTO getDetailExpNote(Date currentDate, Long stockTransId, StockRequestOrderDetailDTO stockRequestOrderDetailDTO) throws Exception {
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setStockTransId(stockTransId);
        stockTransDetailDTO.setCreateDatetime(currentDate);
        stockTransDetailDTO.setProdOfferId(stockRequestOrderDetailDTO.getProdOfferId());
        stockTransDetailDTO.setStateId(stockRequestOrderDetailDTO.getStateId());
        stockTransDetailDTO.setQuantity(stockRequestOrderDetailDTO.getApprovedQuantity());
        return stockTransDetailDTO;
    }

    @Override
    public void doPrepareData(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, FlagStockDTO flagStockDTO) throws LogicException, Exception {

    }

    @Override
    public void doValidate(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, List<StockTransDetailDTO> lstStockTransDetail) throws LogicException, Exception {

    }
}
