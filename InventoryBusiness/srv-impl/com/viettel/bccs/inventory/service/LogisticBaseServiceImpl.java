package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.model.StockTrans;
import com.viettel.bccs.inventory.model.StockTransAction;
import com.viettel.bccs.inventory.repo.StockTransRepo;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.fw.service.BaseServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

/**
 * Created by hoangnt14 on 7/19/2016.
 */
@Service
public class LogisticBaseServiceImpl extends BaseServiceImpl implements LogisticBaseService {

    private final BaseMapper<StockTrans, StockTransDTO> mapperStockTrans = new BaseMapper<>(StockTrans.class, StockTransDTO.class);

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @Autowired
    private StockTransService stockTransService;
    @Autowired
    private StockTransRepo stockTransRepo;
    @Autowired
    private StockTransActionService stockTransActionService;
    @Autowired
    private StockTransDetailService stockTransDetailService;
    @Autowired
    private StockTransVofficeService stockTransVofficeService;
    @Autowired
    private ProductOfferingService productOfferingService;
    @Autowired
    private StockTotalService stockTotalService;
    @Autowired
    private StockHandsetService stockHandsetService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private StockTransOfflineService stockTransOfflineService;
    @Autowired
    private StockTransDetailOfflineService stockTransDetailOfflineService;
    @Autowired
    private StockTransSerialOfflineService stockTransSerialOfflineService;

    public static final Logger logger = Logger.getLogger(LogisticBaseServiceImpl.class);

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultLogisticsDTO createNote(String orderAction, BillStockDTO billStockDTO) throws LogicException, Exception {
        String actionCodeReturn;
        Long stockTransId;
        if (DataUtil.isNullOrEmpty(billStockDTO.getOrderCode())) {
            return new ResultLogisticsDTO(Const.LOGISTICS.FAILED, getText("logistics.create.bill.orderCode.null"));
        }
        if (DataUtil.isNullOrEmpty(orderAction) || orderAction.getBytes().length > 20) {
            return new ResultLogisticsDTO(Const.LOGISTICS.FAILED, getText("logistics.create.bill.order.action.null"));
        }
        try {
            stockTransId = Long.parseLong(billStockDTO.getOrderCode().trim());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResultLogisticsDTO(Const.LOGISTICS.FAILED, getTextParam("logistics.create.bill.orderCode.not.digit", billStockDTO.getOrderCode().trim()));
        }
        StockTrans stockTrans = stockTransRepo.findOne(stockTransId);
        if (DataUtil.isNullObject(stockTrans)) {
            return new ResultLogisticsDTO(Const.LOGISTICS.FAILED, getTextParam("logistics.create.bill.not.found.stock.trans", stockTransId.toString()));
        }
        //lock ban ghi
        try {
            em.lock(stockTrans, LockModeType.PESSIMISTIC_READ);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            return new ResultLogisticsDTO(Const.LOGISTICS.FAILED, getText("logistics.create.bill.lock.stock.trans"));
        }
        //tim kiem lenh
        List<String> lstStatus = Lists.newArrayList();
        lstStatus.add(Const.STOCK_TRANS_STATUS.EXPORT_ORDER);
        lstStatus.add(Const.STOCK_TRANS_STATUS.IMPORT_ORDER);
        StockTransActionDTO stockTransActionDTO = stockTransActionService.getStockTransActionByIdAndStatus(stockTransId, lstStatus);
        if (DataUtil.isNullObject(stockTransActionDTO.getStockTransActionId())) {
            return new ResultLogisticsDTO(Const.LOGISTICS.FAILED, getTextParam("logistics.create.bill.not.found.stock.trans", stockTransId.toString()));
        }
        //Kiem tra ky vOffice
        if (stockTransActionDTO.getSignCaStatus() != null && "2".equalsIgnoreCase(stockTransActionDTO.getSignCaStatus())) {
            StockTransVofficeDTO stockTransVofficeDTO = stockTransVofficeService.findStockTransVofficeByActionId(stockTransActionDTO.getStockTransActionId());
            if (DataUtil.isNullObject(stockTransVofficeDTO) ||  (!DataUtil.isNullObject(stockTransVofficeDTO) && !DataUtil.safeEqual(stockTransVofficeDTO.getStatus(), Const.VOFFICE_STATUS.SIGNED))) {
                return new ResultLogisticsDTO(Const.LOGISTICS.FAILED, getText("logistics.create.bill.lock.voffice.not.found"));
            }
        }
        //validate thong tin hang hoa
        ResultLogisticsDTO stockGeneralDTO = validateBillStockGeneral(billStockDTO);
        if (!DataUtil.isNullObject(stockGeneralDTO) && !DataUtil.isNullOrEmpty(stockGeneralDTO.getResponseCode()) && DataUtil.safeEqual(stockGeneralDTO.getResponseCode(), Const.LOGISTICS.FAILED)) {
            return stockGeneralDTO;
        }
        ResultLogisticsDTO resultLstGood = validateLstGoods(billStockDTO.getOrderCode().trim(), billStockDTO.getLstGoods());
        if (!DataUtil.isNullObject(resultLstGood) && !DataUtil.isNullOrEmpty(resultLstGood.getResponseCode()) && DataUtil.safeEqual(resultLstGood.getResponseCode(), Const.LOGISTICS.FAILED)) {
            return resultLstGood;
        }
        Long fromOwnerIdToCheckStock = stockTrans.getFromOwnerId();
        if (DataUtil.safeEqual(stockTrans.getStatus(), Const.STOCK_TRANS_STATUS.EXPORT_ORDER)
                || DataUtil.safeEqual(stockTrans.getStatus(), Const.STOCK_TRANS_STATUS.IMPORT_ORDER)) {
            //Neu la giao dich nhap kho 3 mien
            if (stockTrans.getRegionStockTransId() != null && stockTrans.getRegionStockTransId().compareTo(0L) > 0) {
                //Tim ID cua kho 3 mien
                StockTransActionDTO stockTransActionRegion = stockTransActionService.getStockTransActionByIdAndStatus(stockTrans.getRegionStockTransId(), Lists.newArrayList(Const.STOCK_TRANS_STATUS.EXPORT_ORDER));
                fromOwnerIdToCheckStock = stockTransActionRegion.getRegionOwnerId();

            }
        }
        //validate stock_total
        ResultLogisticsDTO resultStockTotal = validateStockTotalGeneral(fromOwnerIdToCheckStock, stockTrans.getFromOwnerType(), billStockDTO);
        if (!DataUtil.isNullObject(resultStockTotal) && !DataUtil.isNullOrEmpty(resultStockTotal.getResponseCode()) && DataUtil.safeEqual(resultStockTotal.getResponseCode(), Const.LOGISTICS.FAILED)) {
            return resultStockTotal;
        }
        //Neu da lap phieu roi thi van tra lai thanh cong va tra lai ma phieu cho client
        if (DataUtil.safeEqual(stockTrans.getStatus(), Const.STOCK_TRANS_STATUS.EXPORT_NOTE)
                || DataUtil.safeEqual(stockTrans.getStatus(), Const.STOCK_TRANS_STATUS.IMPORT_NOTE)) {
            List<String> lstStatusResult = Lists.newArrayList();
            lstStatusResult.add(Const.STOCK_TRANS_STATUS.EXPORT_NOTE);
            lstStatusResult.add(Const.STOCK_TRANS_STATUS.IMPORT_NOTE);
            StockTransActionDTO stockTransActionResult = stockTransActionService.getStockTransActionByIdAndStatus(stockTransId, lstStatusResult);
            ResultLogisticsDTO resultLogisticsDTO = new ResultLogisticsDTO();
            resultLogisticsDTO.setResponseCode(Const.LOGISTICS.SUCCESS);
            if (stockTransActionResult != null) {

                resultLogisticsDTO.setActionCode(stockTransActionResult.getActionCode());
                resultLogisticsDTO.setOrderAction(stockTransActionResult.getLogCmdCode());
            }
            return resultLogisticsDTO;
            //Neu chua lap phieu thi tien hanh lap phieu
        } else if (DataUtil.safeEqual(stockTrans.getStatus(), Const.STOCK_TRANS_STATUS.EXPORT_ORDER)
                || DataUtil.safeEqual(stockTrans.getStatus(), Const.STOCK_TRANS_STATUS.IMPORT_ORDER)) {
            if (DataUtil.safeEqual(stockTrans.getStatus(), Const.STOCK_TRANS_STATUS.EXPORT_ORDER)) {
                stockTrans.setStatus(Const.STOCK_TRANS_STATUS.EXPORT_NOTE);
            } else {
                stockTrans.setStatus(Const.STOCK_TRANS_STATUS.IMPORT_NOTE);
            }
            stockTransRepo.save(stockTrans);
            //Them moi stock_trans_action
            StockTransActionDTO stockTransActionNew = new StockTransActionDTO();
            stockTransActionNew.setStockTransId(stockTrans.getStockTransId());
            StockTransActionDTO stockTransActionRegion;
            boolean isExpTrans = true;
            Long shopId = null;
            ShopDTO shopDTO = null;
            if (DataUtil.safeEqual(stockTrans.getStatus(), Const.STOCK_TRANS_STATUS.EXPORT_NOTE)
                    && DataUtil.safeEqual(stockTrans.getFromOwnerType(), Const.OWNER_TYPE.SHOP_LONG)) {
                shopId = stockTrans.getFromOwnerId();
            } else {
                isExpTrans = false;
                if (DataUtil.safeEqual(stockTrans.getToOwnerType(), Const.OWNER_TYPE.SHOP_LONG)) {
                    shopId = stockTrans.getToOwnerId();
                }
            }
            //Neu la giao dich nhap kho 3 mien
            if (stockTrans.getRegionStockTransId() != null && stockTrans.getRegionStockTransId().compareTo(0L) > 0) {
                //Tim ID cua kho 3 mien
                stockTransActionRegion = stockTransActionService.getStockTransActionByIdAndStatus(stockTrans.getRegionStockTransId(), Lists.newArrayList(Const.STOCK_TRANS_STATUS.EXPORT_ORDER));
                shopId = stockTransActionRegion.getRegionOwnerId();
            }
            if (!DataUtil.isNullObject(shopId)) {
                shopDTO = shopService.findOne(shopId);
            }
            if (DataUtil.isNullObject(shopDTO)) {
                throw new LogicException("", "logistics.create.bill.not.esxit.shop");
            }
            //GD xuat
            if (isExpTrans) {
                //Ma phieu xuat
                stockTransActionNew.setActionCodeShop("PX_" + shopDTO.getShopCode());
                stockTransActionNew.setStatus(Const.STOCK_TRANS_STATUS.EXPORT_NOTE);
            } else {
                //Ma phieu nhap
                stockTransActionNew.setActionCodeShop("PN_" + shopDTO.getShopCode()); //Ma phieu xuat
                stockTransActionNew.setStatus(Const.STOCK_TRANS_STATUS.IMPORT_NOTE);
            }
            Date currentDate = getSysDate(em);
            stockTransActionNew.setCreateUser("LOGISTIC_SYSTEM");
            stockTransActionNew.setCreateDatetime(currentDate);
            //cap nhat ma lenh tren Logistic
            stockTransActionDTO.setLogCmdCode(orderAction);
            stockTransActionService.save(stockTransActionDTO);
//            stockTransActionNew.setSignCaStatus("2");

            Long actionCodeShop = getSequence(em, "ACTION_CODE_SHOP_SEQ");
            if (isExpTrans) {
                stockTransActionNew.setActionCode("PX_" + normalActionCodeShop(String.valueOf(actionCodeShop), 6L));
            } else {
                stockTransActionNew.setActionCode("PN_" + normalActionCodeShop(String.valueOf(actionCodeShop), 6L));
            }
            actionCodeReturn = stockTransActionNew.getActionCode();
            stockTransActionNew.setLogCmdCode(orderAction);
            stockTransActionService.save(stockTransActionNew);
            //Neu la giao dich nhap kho 3 mien
            if (stockTrans.getRegionStockTransId() != null && stockTrans.getRegionStockTransId().compareTo(0L) > 0) {
                //Thuc hien lap phieu cho cac giao dich 3 mien
                createRegionReceiveNote(stockTrans, currentDate);
            }
            //unlock
            stockTransActionService.unlockUserInfo(stockTransActionDTO.getStockTransActionId());
        } else {
            return new ResultLogisticsDTO(Const.LOGISTICS.FAILED, getTextParam("logistics.create.bill.status.not.valid", stockTransId.toString(), stockTrans.getStatus()));
        }
        ResultLogisticsDTO resultLogisticsDTO = new ResultLogisticsDTO(Const.LOGISTICS.SUCCESS, getText("logistics.create.bill.success"));
        resultLogisticsDTO.setActionCode(actionCodeReturn);
        return resultLogisticsDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultLogisticsDTO transStock(BillStockDTO billStockDTO) throws LogicException, Exception {
        //validate thong tin hang hoa
        ResultLogisticsDTO stockGeneralDTO = validateBillStockGeneral(billStockDTO);
        if (!DataUtil.isNullObject(stockGeneralDTO) && !DataUtil.isNullOrEmpty(stockGeneralDTO.getResponseCode()) && DataUtil.safeEqual(stockGeneralDTO.getResponseCode(), Const.LOGISTICS.FAILED)) {
            return stockGeneralDTO;
        }
        if (!DataUtil.isNullOrEmpty(billStockDTO.getTransCode()) && billStockDTO.getTransCode().getBytes("UTF-8").length > 20) {
            return new ResultLogisticsDTO(Const.LOGISTICS.FAILED, getText("logistics.create.bill.trans.code"));
        }
        Long stockTransId = Long.parseLong(billStockDTO.getOrderCode().trim());
        StockTrans stockTrans = stockTransRepo.findOne(stockTransId);
        if (DataUtil.isNullObject(stockTrans)) {
            return new ResultLogisticsDTO(Const.LOGISTICS.FAILED, getTextParam("logistics.create.bill.not.found.stock.trans", stockTransId.toString()));
        }
        if (DataUtil.safeEqual(stockTrans.getStatus(), Const.STOCK_TRANS_STATUS.EXPORT_ORDER) || DataUtil.safeEqual(stockTrans.getStatus(), Const.STOCK_TRANS_STATUS.IMPORT_ORDER)) {
            return new ResultLogisticsDTO(Const.LOGISTICS.FAILED, getTextParam("logistics.trans.stock.status.not.valid", stockTransId.toString()));
        }
        //Lay thong tin phieu
        List<String> lstStatusNote = Lists.newArrayList();
        lstStatusNote.add(Const.STOCK_TRANS_STATUS.EXPORT_NOTE);
        lstStatusNote.add(Const.STOCK_TRANS_STATUS.IMPORT_NOTE);
        StockTransActionDTO stockTransActionNote = stockTransActionService.getStockTransActionByIdAndStatus(stockTransId, lstStatusNote);
        if (DataUtil.isNullObject(stockTransActionNote.getStockTransActionId())) {
            return new ResultLogisticsDTO(Const.LOGISTICS.FAILED, getTextParam("logistics.create.bill.not.found.stock.trans", stockTransId.toString()));
        }
        //Kiem tra ky vOffice
        if (stockTransActionNote.getSignCaStatus() != null && "2".equalsIgnoreCase(stockTransActionNote.getSignCaStatus())) {
            StockTransVofficeDTO stockTransVofficeDTO = stockTransVofficeService.findStockTransVofficeByActionId(stockTransActionNote.getStockTransActionId());
            if (DataUtil.isNullObject(stockTransVofficeDTO) ||
                    (!DataUtil.isNullObject(stockTransVofficeDTO) && !DataUtil.safeEqual(stockTransVofficeDTO.getStatus(), Const.VOFFICE_STATUS.SIGNED))) {
                return new ResultLogisticsDTO(Const.LOGISTICS.FAILED, getText("logistics.create.bill.lock.voffice.not.found"));
            }
        }
        ResultLogisticsDTO resultLstGood = validateLstGoods(billStockDTO.getOrderCode().trim(), billStockDTO.getLstGoods());
        if (!DataUtil.isNullObject(resultLstGood) && !DataUtil.isNullOrEmpty(resultLstGood.getResponseCode()) && DataUtil.safeEqual(resultLstGood.getResponseCode(), Const.LOGISTICS.FAILED)) {
            return resultLstGood;
        }
        Long fromOwnerIdToCheckStock = stockTrans.getFromOwnerId();
        if (DataUtil.safeEqual(stockTrans.getStatus(), Const.STOCK_TRANS_STATUS.EXPORT_NOTE)
                && DataUtil.safeEqual(stockTrans.getFromOwnerType(), Const.OWNER_TYPE.SHOP_LONG)) {
            //Neu la giao dich nhap kho 3 mien
            if (stockTrans.getRegionStockTransId() != null && stockTrans.getRegionStockTransId().compareTo(0L) > 0) {
                //Tim ID cua kho 3 mien
                StockTransActionDTO stockTransActionRegion = stockTransActionService.getStockTransActionByIdAndStatus(stockTrans.getRegionStockTransId(), Lists.newArrayList(Const.STOCK_TRANS_STATUS.EXPORT_ORDER));
                fromOwnerIdToCheckStock = stockTransActionRegion.getRegionOwnerId();

            }
        }
        //validate stock_total
        ResultLogisticsDTO resultStockTotal = validateStockTotalGeneral(fromOwnerIdToCheckStock, stockTrans.getFromOwnerType(), billStockDTO);
        if (!DataUtil.isNullObject(resultStockTotal) && !DataUtil.isNullOrEmpty(resultStockTotal.getResponseCode()) && DataUtil.safeEqual(resultStockTotal.getResponseCode(), Const.LOGISTICS.FAILED)) {
            return resultStockTotal;
        }
        //lay danh sach hang hoa trong lenh
        List<StockTransDetailDTO> lstStockTransDetailDTOs = stockTransDetailService.findByStockTransId(stockTransId);
        if (DataUtil.isNullOrEmpty(lstStockTransDetailDTOs)) {
            return new ResultLogisticsDTO(Const.LOGISTICS.FAILED, getTextParam("logistics.create.bill.good.not.found.detail", stockTransId.toString()));
        }
        stockTransActionNote.setLogNoteCode(billStockDTO.getTransCode());
        stockTransActionService.save(stockTransActionNote);
        //Neu da xuat/nhap kho thi van bao thanh cong
        if (DataUtil.safeEqual(billStockDTO.getOrderType(), Const.LOGISTICS.ORDER_TYPE_EXPORT)) {
            if (DataUtil.safeEqual(stockTrans.getStatus(), Const.STOCK_TRANS_STATUS.EXPORTED)) {
                throw new LogicException("", "logistics.trans.stock.export.success.before", stockTransId.toString());
            }
        } else {
            if (DataUtil.safeEqual(stockTrans.getStatus(), Const.STOCK_TRANS_STATUS.IMPORTED)) {
                throw new LogicException("", "logistics.trans.stock.import.success.before", stockTransId.toString());
            }
        }
        //lock ban ghi
        try {
            em.lock(stockTrans, LockModeType.PESSIMISTIC_READ);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new LogicException("", "logistics.create.bill.lock.stock.trans");
        }
        //Cap nhat stock_trans_status = 0 de xu ly offline
        stockTrans.setStatus(Const.STOCK_TRANS_STATUS.PROCESSING);
        stockTransRepo.save(stockTrans);
        //Dua du lieu vao cac bang Offline
        //Bang stockTransOffline
        StockTransOfflineDTO stockTransOfflineDTO = new StockTransOfflineDTO(mapperStockTrans.toDtoBean(stockTrans));
//        stockTransOfflineDTO.setLogOrderType(billStock.getOrderType());
//        stockTransOfflineDTO.setLogInputType(billStock.getInputType());
//        stockTransOfflineDTO.setLogOutputType(billStock.getOutputType());
        stockTransOfflineService.save(stockTransOfflineDTO);
        //Bang stockTransDetailOffline
        for (StockTransDetailDTO stockTransDetailDTO : lstStockTransDetailDTOs) {
            StockTransDetailOfflineDTO stockTransDetailOfflineDTO = new StockTransDetailOfflineDTO(stockTransDetailDTO);
            StockTransDetailOfflineDTO stockTransDetailOfflineResult = stockTransDetailOfflineService.save(stockTransDetailOfflineDTO);
            for (int i = 0; i < billStockDTO.getLstGoods().size(); i++) {
                if ((stockTransDetailDTO.getProdOfferId().compareTo(billStockDTO.getLstGoods().get(i).getStockModelId()) == 0) && (stockTransDetailDTO.getStateId().compareTo(Long.parseLong(billStockDTO.getLstGoods().get(i).getGoodsState())) == 0)) {
                    billStockDTO.getLstGoods().get(i).setStockTransDetailOfflineId(stockTransDetailOfflineResult.getStockTransDetailId());
                    break;
                }
            }
        }
        Date currentDate = getSysDate(em);
        //Bang stockTransSerialOffline
        //Duyet tung mat hang
        for (int i = 0; i < billStockDTO.getLstGoods().size(); i++) {
            GoodsDTO goods = billStockDTO.getLstGoods().get(i);
            StockTransSerialOfflineDTO stockTransSerialOfflineDTO;
            if (DataUtil.safeEqual(goods.getCheckSerial(), Const.LOGISTIC.HAVE_CHECK_SERIAL)) {
                if (goods.getLstSerial() == null || goods.getLstSerial().isEmpty()) {
                    throw new LogicException("", "logistics.trans.stock.have.not.serial", goods.getGoodsCode());
                }
            }
            //Mot mat hang co nhieu dai serial
            if (!DataUtil.isNullOrEmpty(goods.getLstSerial())) {
                for (int idx = 0; idx < goods.getLstSerial().size(); idx++) {
                    Serial serial = goods.getLstSerial().get(idx);
                    stockTransSerialOfflineDTO = new StockTransSerialOfflineDTO();
                    stockTransSerialOfflineDTO.setStockTransId(stockTransId);
                    stockTransSerialOfflineDTO.setStockTransDetailId(goods.getStockTransDetailOfflineId());
                    stockTransSerialOfflineDTO.setProdOfferId(goods.getStockModelId());
                    stockTransSerialOfflineDTO.setStateId(Long.parseLong(goods.getGoodsState()));
                    stockTransSerialOfflineDTO.setFromSerial(serial.getFromSerial());
                    stockTransSerialOfflineDTO.setToSerial(serial.getToSerial());
                    stockTransSerialOfflineDTO.setQuantity(Long.parseLong(serial.getQuantity()));
                    stockTransSerialOfflineDTO.setCreateDatetime(currentDate);

                    stockTransSerialOfflineService.save(stockTransSerialOfflineDTO);
                }
            }
        }
        ResultLogisticsDTO resultLogisticsDTO = new ResultLogisticsDTO(Const.LOGISTICS.SUCCESS, getText("logistics.trans.stock.success"));
        return resultLogisticsDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultLogisticsDTO cancelOrderOrBill(OrderObjectDTO orderObjectDTO) throws LogicException, Exception {
        if (DataUtil.isNullObject(orderObjectDTO)) {
            return new ResultLogisticsDTO(Const.LOGISTICS.FAILED, getText("logistics.cancel.order.object.not.null"));
        }
        if (DataUtil.isNullOrEmpty(orderObjectDTO.getOrderCode())) {
            return new ResultLogisticsDTO(Const.LOGISTICS.FAILED, getText("logistics.cancel.order.object.code.not.null"));
        }
        Long stockTransId = null;

        try {
            stockTransId = Long.parseLong(orderObjectDTO.getOrderCode().trim());
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            return new ResultLogisticsDTO(Const.LOGISTICS.FAILED, getTextParam("logistics.cancel.order.object.code.not.valid", orderObjectDTO.getOrderCode()));
        }
        //Thay doi trang thai giao dich
        StockTrans stockTrans = stockTransRepo.findOne(stockTransId);
        if (DataUtil.isNullObject(stockTrans)) {
            return new ResultLogisticsDTO(Const.LOGISTICS.FAILED, getTextParam("logistics.create.bill.not.found.stock.trans", stockTransId.toString()));
        }
        //lock ban ghi
        try {
            em.lock(stockTrans, LockModeType.PESSIMISTIC_READ);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            return new ResultLogisticsDTO(Const.LOGISTICS.FAILED, getText("logistics.create.bill.lock.stock.trans"));
        }
        //Neu giao dich da huy roi ma huy lai thi van thong bao thanh cong
        if (DataUtil.safeEqual(stockTrans.getStatus(), Const.STOCK_TRANS_STATUS.CANCEL)) {
            return new ResultLogisticsDTO(Const.LOGISTICS.SUCCESS, getText("logistics.cancel.order.success"));
        }
        Date currentDate = getSysDate(em);
        Long fromOwnerId = stockTrans.getFromOwnerId();
        //Giao dich co status khac 1,2,4,5 --> khong cho huy
        if (!DataUtil.safeEqual(stockTrans.getStatus(), Const.STOCK_TRANS_STATUS.EXPORT_ORDER) && !DataUtil.safeEqual(stockTrans.getStatus(), Const.STOCK_TRANS_STATUS.EXPORT_NOTE)
                && !DataUtil.safeEqual(stockTrans.getStatus(), Const.STOCK_TRANS_STATUS.IMPORT_ORDER) && !DataUtil.safeEqual(stockTrans.getStatus(), Const.STOCK_TRANS_STATUS.IMPORT_NOTE)) {
            return new ResultLogisticsDTO(Const.LOGISTICS.FAILED, getText("logistics.cancel.order.status.not.valid"));
        }
        //Khong cho huy nhung lenh xuat da thanh toan
        if (DataUtil.safeEqual(stockTrans.getPayStatus(), Const.PAY_STATUS.PAY_HAVE)) {
            return new ResultLogisticsDTO(Const.LOGISTICS.FAILED, getText("logistics.cancel.order.status.pay.have"));
        }
        //Khong cho phep huy nhung lenh xuat da dat coc
        if (DataUtil.safeEqual(stockTrans.getDepositStatus(), Const.DEPOSIT_STATUS.DEPOSIT_HAVE)) {
            return new ResultLogisticsDTO(Const.LOGISTICS.FAILED, getText("logistics.cancel.order.status.deposit.have"));
        }
        //Neu la giao dich nhap kho 3 mien
        if (stockTrans.getRegionStockTransId() != null && stockTrans.getRegionStockTransId().compareTo(0L) > 0) {
            //Tim ID cua kho 3 mien
            StockTransActionDTO stockTransActionRegion = stockTransActionService.getStockTransActionByIdAndStatus(stockTrans.getRegionStockTransId(), Lists.newArrayList(Const.STOCK_TRANS_STATUS.EXPORT_ORDER));
            fromOwnerId = stockTransActionRegion.getRegionOwnerId();
            //cap nhat trang thai huy
            StockTransDTO stockTransRegionDto = stockTransService.findOne(stockTrans.getRegionStockTransId());
            if (!DataUtil.isNullObject(stockTransRegionDto)) {
                stockTransRegionDto.setStatus(Const.STOCK_TRANS_STATUS.CANCEL);
                stockTransService.save(stockTransRegionDto);
                //Them moi stock_trans_action = 7
                StockTransActionDTO stockTransActionRegionCancel = new StockTransActionDTO();
                stockTransActionRegionCancel.setStockTransId(stockTransActionRegion.getStockTransId());
                stockTransActionRegionCancel.setCreateDatetime(currentDate);
                stockTransActionRegionCancel.setCreateUser("LOGISTIC_SYSTEM");
                stockTransActionRegionCancel.setStatus(Const.STOCK_TRANS_STATUS.CANCEL);
                stockTransActionRegionCancel.setRegionOwnerId(fromOwnerId);
                stockTransActionService.save(stockTransActionRegionCancel);
            }
        }
        List<String> lstStatus = Lists.newArrayList();
        lstStatus.add(Const.STOCK_TRANS_STATUS.EXPORT_ORDER);
        lstStatus.add(Const.STOCK_TRANS_STATUS.EXPORT_NOTE);
        StockTransActionDTO stockTransActionDTO = stockTransActionService.getStockTransActionByIdAndStatus(stockTransId, lstStatus);
        if (DataUtil.isNullObject(stockTransActionDTO.getStockTransActionId())) {
            return new ResultLogisticsDTO(Const.LOGISTICS.FAILED, getTextParam("logistics.create.bill.not.found.stock.trans", stockTransId.toString()));
        }
        //Chi cap nhat lai so luong dap ung neu la giao dich xuat
        if (DataUtil.safeEqual(stockTrans.getStatus(), Const.STOCK_TRANS_STATUS.EXPORT_ORDER) || DataUtil.safeEqual(stockTrans.getStatus(), Const.STOCK_TRANS_STATUS.EXPORT_NOTE)) {

            //lay danh sach hang hoa trong lenh
            List<StockTransDetailDTO> lstStockTransDetailDTOs = stockTransDetailService.findByStockTransId(stockTransId);
            if (DataUtil.isNullOrEmpty(lstStockTransDetailDTOs)) {
                return new ResultLogisticsDTO(Const.LOGISTICS.FAILED, getTextParam("logistics.create.bill.good.not.found.detail", stockTransId.toString()));
            }
            for (StockTransDetailDTO stockTransDetailDTO : lstStockTransDetailDTOs) {
                StockTotalDTO stockTotalDTO = stockTotalService.getStockTotalForProcessStock(fromOwnerId, stockTrans.getFromOwnerType(), stockTransDetailDTO.getProdOfferId(), stockTransDetailDTO.getStateId());
                if (!DataUtil.isNullObject(stockTotalDTO)) {
                    if (DataUtil.safeEqual(stockTrans.getStatus(), Const.STOCK_TRANS_STATUS.EXPORT_ORDER)) {
                        stockTotalService.changeStockTotal(fromOwnerId, stockTrans.getFromOwnerType(), stockTransDetailDTO.getProdOfferId(),
                                stockTransDetailDTO.getStateId(), 0L, stockTransDetailDTO.getQuantity(), 0L,
                                1L, stockTrans.getReasonId(), stockTrans.getStockTransId(), currentDate, stockTrans.getStockTransId().toString(), stockTrans.getStockTransType().toString(), Const.SourceType.CMD_TRANS);
                    } else {
                        stockTotalService.changeStockTotal(fromOwnerId, stockTrans.getFromOwnerType(), stockTransDetailDTO.getProdOfferId(),
                                stockTransDetailDTO.getStateId(), 0L, stockTransDetailDTO.getQuantity(), 0L,
                                1L, stockTrans.getReasonId(), stockTrans.getStockTransId(), currentDate, stockTrans.getStockTransId().toString(), stockTrans.getStockTransType().toString(), Const.SourceType.STICK_TRANS);
                    }
                }
            }
            stockTrans.setStatus(Const.STOCK_TRANS_STATUS.CANCEL);
            stockTransRepo.save(stockTrans);
            //Them moi stock_trans_action = 7
            StockTransActionDTO stockTransActionCancel = new StockTransActionDTO();
            stockTransActionCancel.setStockTransId(stockTransActionDTO.getStockTransId());
            stockTransActionCancel.setCreateDatetime(currentDate);
            stockTransActionCancel.setCreateUser("LOGISTIC_SYSTEM");
            stockTransActionCancel.setStatus(Const.STOCK_TRANS_STATUS.CANCEL);
            stockTransActionService.save(stockTransActionCancel);

        } else {
            //Neu giao dich nhap, cap nhat ve da xuat kho
            stockTrans.setStatus(Const.STOCK_TRANS_STATUS.EXPORTED);
            stockTransRepo.save(stockTrans);
            //xoa stock_trans_action tuong ung
            stockTransActionService.deleteStockTransActionByIdAndStatus(stockTrans.getStockTransId(), Lists.newArrayList(Const.STOCK_TRANS_STATUS.IMPORT_ORDER, Const.STOCK_TRANS_STATUS.IMPORT_NOTE));
        }

        //unlock
        stockTransActionService.unlockUserInfo(stockTransActionDTO.getStockTransActionId());
        ResultLogisticsDTO resultLogisticsDTO = new ResultLogisticsDTO(Const.LOGISTICS.SUCCESS, getText("logistics.cancel.order.success.mgs"));
        return resultLogisticsDTO;
    }

    private void createRegionReceiveNote(StockTrans stockTrans, Date createDate) throws Exception {
        try {
            // Cap nhat giao dich VT xuat cho 3 mien
            StockTransDTO expStockTransDTO = stockTransService.findOne(stockTrans.getRegionStockTransId());
            expStockTransDTO.setStatus(Const.STOCK_TRANS_STATUS.IMPORT_NOTE);
            stockTransService.save(expStockTransDTO);
            ShopDTO shopDTO = shopService.findOne(expStockTransDTO.getFromOwnerId());

            Long actionCodeShop = getSequence(em, "ACTION_CODE_SHOP_SEQ");

            // Tao phieu xuat kho VT cho 3 mien
            //status = 2
            StockTransActionDTO expTransActionDTO = new StockTransActionDTO();
            expTransActionDTO.setStockTransId(expStockTransDTO.getStockTransId());
            expTransActionDTO.setActionCodeShop("PX_" + "PX_" + shopDTO.getShopCode()); //Ma phieu xuat
            expTransActionDTO.setStatus(Const.STOCK_TRANS_STATUS.EXPORT_NOTE);
            expTransActionDTO.setNote(getText("export.order.three.region.note"));

            expTransActionDTO.setCreateUser("LOGISTIC_SYSTEM");
            expTransActionDTO.setCreateDatetime(createDate);
            expTransActionDTO.setActionCode("PX_" + normalActionCodeShop(String.valueOf(actionCodeShop), 6L));
            stockTransActionService.save(expTransActionDTO);
            //status = 5
            expTransActionDTO.setActionCodeShop("PN_" + "PN_" + shopDTO.getShopCode()); //Ma phieu nhap
            expTransActionDTO.setStatus(Const.STOCK_TRANS_STATUS.IMPORT_NOTE);
            expTransActionDTO.setActionCode("PN_" + normalActionCodeShop(String.valueOf(actionCodeShop), 6L));
            stockTransActionService.save(expTransActionDTO);
        } catch (Exception ex) {
            logger.error("Error: ", ex);
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "common.error.happened");
        }
    }

    private String normalActionCodeShop(String actionCodeShop, Long length) {
        if (actionCodeShop != null && !"".equals(actionCodeShop.trim())) {
            while (actionCodeShop.length() < length) {
                actionCodeShop = "0" + actionCodeShop;
            }
        }
        return actionCodeShop;
    }

    private ResultLogisticsDTO validateStockModelQuantity(Long ownerId, Long ownerType, List<GoodsDTO> lstGoods, Long status) throws Exception {
        if (lstGoods == null || lstGoods.isEmpty()) {
            return new ResultLogisticsDTO(Const.LOGISTICS.FAILED, getText("logistics.create.bill.goods.empty"));
        }
        try {
            for (int i = 0; i < lstGoods.size(); i++) {
                GoodsDTO goods = lstGoods.get(i);
                Long amountTotalGoodsSerial = 0L;
                ProductOfferingDTO productOfferingDTO = productOfferingService.findByProductOfferCode(goods.getGoodsCode(), Const.STATUS_ACTIVE);

                if (productOfferingDTO == null) {
                    return new ResultLogisticsDTO(Const.LOGISTICS.FAILED, getTextParam("logistics.create.bill.good.not.found", goods.getGoodsCode()));
                }
                if (!DataUtil.isNullOrEmpty(goods.getLstSerial())) {
                    for (Serial serial : goods.getLstSerial()) {
                        amountTotalGoodsSerial += Long.parseLong(serial.getQuantity());
                    }
                }
                //Tong so luong serial thuc te co trong kho
                Long amountStockModelTotal = stockHandsetService.getQuantityStockX(ownerId, ownerType, productOfferingDTO.getProductOfferingId(), goods.getLstSerial(), status, DataUtil.safeToLong(goods.getGoodsState()));

                //Neu so luong serial thuc te trong kho < So luong yeu cau xuat/nhap thi bao loi
                if ((amountTotalGoodsSerial != null) && (amountStockModelTotal == null || amountStockModelTotal.compareTo(amountTotalGoodsSerial) < 0)) {
                    return new ResultLogisticsDTO(Const.LOGISTICS.FAILED, getTextParam("logistics.create.bill.good.not.equal.quantity.serial", goods.getGoodsCode(), DataUtil.safeToString(amountStockModelTotal), amountTotalGoodsSerial.toString()));
                }
            }
        } catch (Exception ex) {
            logger.error("Error: ", ex);
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "common.error.happened");
        }

        return new ResultLogisticsDTO(Const.LOGISTICS.SUCCESS, "Validate Success");

    }

    private ResultLogisticsDTO validateStockTotalQuantity(Long ownerId, Long ownerType, List<GoodsDTO> lstGoods) throws Exception {
        if (lstGoods == null || lstGoods.isEmpty()) {
            return new ResultLogisticsDTO(Const.LOGISTICS.FAILED, getText("logistics.create.bill.goods.empty"));
        }
        try {
            for (int i = 0; i < lstGoods.size(); i++) {
                GoodsDTO goods = lstGoods.get(i);
                ProductOfferingDTO productOfferingDTO = productOfferingService.findByProductOfferCode(goods.getGoodsCode(), Const.STATUS_ACTIVE);

                if (productOfferingDTO == null) {
                    return new ResultLogisticsDTO(Const.LOGISTICS.FAILED, getTextParam("logistics.create.bill.good.not.found", goods.getGoodsCode()));
                }
                String stateId = goods.getGoodsState();
                if ("2".equals(stateId)) {
                    stateId = "3";
                }
                //Tong so luong serial thuc te co trong kho
                StockTotalDTO stockTotalDTO = stockTotalService.getStockTotalForProcessStock(ownerId, ownerType, productOfferingDTO.getProductOfferingId(), DataUtil.safeToLong(stateId));
                Long stockTotalQuantity = 0L;
                if (!DataUtil.isNullObject(stockTotalDTO)) {
                    stockTotalQuantity = stockTotalDTO.getCurrentQuantity();
                }
                //Neu so luong thuc te trong kho < so luong thuc te xuat
                if (stockTotalQuantity != null && stockTotalQuantity.compareTo(Long.parseLong(goods.getAmountReal())) < 0) {
                    return new ResultLogisticsDTO(Const.LOGISTICS.FAILED, getTextParam("logistics.create.bill.good.not.equal.quantity.stock.total", goods.getGoodsCode(), stockTotalQuantity.toString(), goods.getAmountReal()));
                }
            }
        } catch (Exception ex) {
            logger.error("Error: ", ex);
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "common.error.happened");
        }

        return new ResultLogisticsDTO(Const.LOGISTICS.SUCCESS, "Validate Success");

    }

    private ResultLogisticsDTO validateStockTotalGeneral(Long ownerId, Long ownerType, BillStockDTO billStockDTO) throws Exception {
        //N?u l� y�u c?u nh?p
        if ("1".equals(billStockDTO.getOrderType())) {
            //Chi validate so luong serial o trang thai hop le neu la nhap hang luan chuyen
            if ("2".equals(billStockDTO.getInputType())) {
                ResultLogisticsDTO resultBOStockModelQuantity = validateStockModelQuantity(ownerId, ownerType, billStockDTO.getLstGoods(), 3L);
                if (!DataUtil.isNullObject(resultBOStockModelQuantity) && !DataUtil.isNullOrEmpty(resultBOStockModelQuantity.getResponseCode()) && DataUtil.safeEqual(resultBOStockModelQuantity.getResponseCode(), Const.LOGISTICS.FAILED)) {
                    return resultBOStockModelQuantity;
                }
            }
            //Neu la yeu cau xuat
        } else {
            //Validate So luong thuc te trong stock_total
            ResultLogisticsDTO resultBOStockTotalQuantity = validateStockTotalQuantity(ownerId, ownerType, billStockDTO.getLstGoods());
            if (!DataUtil.isNullObject(resultBOStockTotalQuantity) && !DataUtil.isNullOrEmpty(resultBOStockTotalQuantity.getResponseCode()) && DataUtil.safeEqual(resultBOStockTotalQuantity.getResponseCode(), Const.LOGISTICS.FAILED)) {
                return resultBOStockTotalQuantity;
            }

            //Validate so luong serial o trang thai hop le de thuc hien xuat kho
            ResultLogisticsDTO resultBOStockModelQuantity = validateStockModelQuantity(ownerId, ownerType, billStockDTO.getLstGoods(), 1L);
            if (!DataUtil.isNullObject(resultBOStockModelQuantity) && !DataUtil.isNullOrEmpty(resultBOStockModelQuantity.getResponseCode()) && DataUtil.safeEqual(resultBOStockModelQuantity.getResponseCode(), Const.LOGISTICS.FAILED)) {
                return resultBOStockModelQuantity;
            }
        }
        return new ResultLogisticsDTO(Const.LOGISTICS.SUCCESS, "Validate Success");
    }

    private ResultLogisticsDTO validateLstGoods(String orderCode, List<GoodsDTO> lstGoods) throws Exception {
        Long stockTransId;

        if (DataUtil.isNullOrEmpty(orderCode)) {
            return new ResultLogisticsDTO(Const.LOGISTICS.FAILED, getText("logistics.create.bill.orderCode.null"));
        }

        try {
            stockTransId = Long.parseLong(orderCode);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResultLogisticsDTO(Const.LOGISTICS.FAILED, getTextParam("logistics.create.bill.orderCode.not.digit", orderCode));
        }
        try {
            StockTrans stockTrans = stockTransRepo.findOne(stockTransId);
            if (DataUtil.isNullObject(stockTrans)) {
                return new ResultLogisticsDTO(Const.LOGISTICS.FAILED, getTextParam("logistics.create.bill.not.found.stock.trans", stockTransId.toString()));
            }
            List<Long> lstStockModelIdLogistics = Lists.newArrayList();
            //validate danh sach ma hang hoa truyen vao
            ResultLogisticsDTO resultGoodCode = validateGoodsCode(lstGoods, lstStockModelIdLogistics);
            if (!DataUtil.isNullObject(resultGoodCode) && !DataUtil.isNullOrEmpty(resultGoodCode.getResponseCode()) && DataUtil.safeEqual(resultGoodCode.getResponseCode(), Const.LOGISTICS.FAILED)) {
                return resultGoodCode;
            }
            //lay danh sach hang hoa trong lenh
            List<StockTransDetailDTO> lstStockTransDetailDTOs = stockTransDetailService.findByStockTransId(stockTransId);
            if (DataUtil.isNullOrEmpty(lstStockTransDetailDTOs)) {
                return new ResultLogisticsDTO(Const.LOGISTICS.FAILED, getTextParam("logistics.create.bill.good.not.found.detail", orderCode));
            }
            //Kiem tra so luong mat hang trong lenh xuat/nhap va so luong mat hang ma Logistic truyen sang co map voi nhau khong
            if (lstStockTransDetailDTOs.size() != lstGoods.size()) {
                return new ResultLogisticsDTO(Const.LOGISTICS.FAILED, getText("logistics.create.bill.good.size.not.match"));
            }
            List<Long> lstStockModelIdBCCS = Lists.newArrayList();
            for (int i = 0; i < lstStockTransDetailDTOs.size(); i++) {
                lstStockModelIdBCCS.add(lstStockTransDetailDTOs.get(i).getProdOfferId());
            }
            //Kiem tra xem co thieu mat hang nao khong
            for (int i = 0; i < lstStockModelIdBCCS.size(); i++) {
                if (!lstStockModelIdLogistics.contains(lstStockModelIdBCCS.get(i))) {
                    return new ResultLogisticsDTO(Const.LOGISTICS.FAILED, getTextParam("logistics.create.bill.good.prodOfferId.require", lstStockModelIdBCCS.get(i).toString()));
                }
            }
            //Kiem tra xem co thua mat hang nao khong
            for (int i = 0; i < lstStockModelIdLogistics.size(); i++) {
                if (!lstStockModelIdBCCS.contains(lstStockModelIdLogistics.get(i))) {
                    return new ResultLogisticsDTO(Const.LOGISTICS.FAILED, getTextParam("logistics.create.bill.good.prodOfferId.too.enough", lstStockModelIdBCCS.get(i).toString()));
                }
            }
            //Mapping lai GoodState
            for (int i = 0; i < lstGoods.size(); i++) {
                GoodsDTO goods = lstGoods.get(i);
                if (goods.getGoodsState() != null) {
                    if ("2".equals(goods.getGoodsState().trim())) {
                        goods.setGoodsState("3");
                    }
                }
            }
            //Ki?m tra t�nh tr?ng h�ng trong l?nh v� t�nh tr?ng h�ng m� Logistic truy?n sang c� kh?p nhau kh�ng
            for (int i = 0; i < lstStockTransDetailDTOs.size(); i++) {
                for (int j = 0; j < lstGoods.size(); j++) {

                    //Trung nhau mat hang va tinh trang hang
                    System.out.println("=getProdOfferId="+lstStockTransDetailDTOs.get(i).getProdOfferId());
                    System.out.println("=getStockModelId="+lstGoods.get(j).getStockModelId());
                    System.out.println("=getStateId="+lstStockTransDetailDTOs.get(i).getStateId());
                    System.out.println("=getGoodsState="+lstGoods.get(j).getGoodsState());
                    if ((lstStockTransDetailDTOs.get(i).getProdOfferId().compareTo(lstGoods.get(j).getStockModelId()) == 0) && (lstStockTransDetailDTOs.get(i).getStateId().compareTo(Long.parseLong(lstGoods.get(j).getGoodsState())) == 0)) {
                        //Nhung lai khac nhau so luong trong lenh va so luong nhan duoc co khop nhau khong
                        if (lstStockTransDetailDTOs.get(i).getQuantity().compareTo(Long.parseLong(lstGoods.get(j).getAmountOrder().trim())) != 0) {
                            return new ResultLogisticsDTO(Const.LOGISTICS.FAILED, getTextParam("logistics.create.bill.good.quantity.not.equal", lstGoods.get(j).getGoodsCode(), lstStockTransDetailDTOs.get(i).getQuantity().toString(), lstGoods.get(j).getAmountOrder().trim()));
                        }
                    }
                }
            }
        } catch (Exception ex) {
            logger.error("Error: ", ex);
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "common.error.happened");
        }

        return new ResultLogisticsDTO(Const.LOGISTICS.SUCCESS, "Validate Success");
    }

    private ResultLogisticsDTO validateGoodsCode(List<GoodsDTO> lstGoods, List<Long> lstStockModelIdLogistics) throws Exception {
        if (lstGoods == null || lstGoods.isEmpty()) {
            return new ResultLogisticsDTO(Const.LOGISTICS.FAILED, getText("logistics.create.bill.goods.empty"));
        }
        try {
            for (int i = 0; i < lstGoods.size(); i++) {
                GoodsDTO goods = lstGoods.get(i);
                //Kiem tra chua truyen ma mat hang
                if (DataUtil.isNullOrEmpty(goods.getGoodsCode())) {
                    return new ResultLogisticsDTO(Const.LOGISTICS.FAILED, getText("logistics.create.bill.goods.code.null"));
                }
                //Kiem tra chua truyen so luong
                if (DataUtil.isNullOrEmpty(goods.getAmountOrder()) || DataUtil.isNullOrEmpty(goods.getAmountReal())) {
                    return new ResultLogisticsDTO(Const.LOGISTICS.FAILED, getTextParam("logistics.create.bill.goods.code.quantity.null", goods.getGoodsCode()));
                }
                //Kiem tra so luong co phai la so khong
                try {
                    if (Long.parseLong(goods.getAmountOrder().trim()) <= 0L || Long.parseLong(goods.getAmountReal().trim()) <= 0L) {
                        return new ResultLogisticsDTO(Const.LOGISTICS.FAILED, getTextParam("logistics.create.bill.goods.code.quantity.not.match", goods.getGoodsCode()));
                    }
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                    return new ResultLogisticsDTO(Const.LOGISTICS.FAILED, getTextParam("logistics.create.bill.goods.code.quantity.not.match", goods.getGoodsCode()));
                }
                //Kiem tra chua truyen t�nh tr?ng h�ng
                if (DataUtil.isNullOrEmpty(goods.getGoodsState())) {
                    return new ResultLogisticsDTO(Const.LOGISTICS.FAILED, getTextParam("logistics.create.bill.goods.state.null", goods.getGoodsCode()));
                }
                //Kiem tra tinh trang hang co phai la so khong
                try {
                    if (Long.parseLong(goods.getGoodsState().trim()) <= 0L) {
                        return new ResultLogisticsDTO(Const.LOGISTICS.FAILED, getTextParam("logistics.create.bill.goods.state.not.match", goods.getGoodsCode()));
                    }
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                    return new ResultLogisticsDTO(Const.LOGISTICS.FAILED, getTextParam("logistics.create.bill.goods.state.not.match", goods.getGoodsCode()));
                }
                //Kiem tra ma mat hang c� t?n t?i tr�n BCCS kh�ng
                ProductOfferingDTO productOfferingDTO = productOfferingService.findByProductOfferCode(goods.getGoodsCode(), Const.STATUS_ACTIVE);

                if (productOfferingDTO == null) {
                    return new ResultLogisticsDTO(Const.LOGISTICS.FAILED, getTextParam("logistics.create.bill.good.not.found", goods.getGoodsCode()));
                }
                //Check mat hang quan ly theo serial nhung khong nhap serial
                if (productOfferingDTO.getCheckSerial() != null && productOfferingDTO.getCheckSerial().compareTo(1L) == 0) {
                    if (goods.getLstSerial() == null || goods.getLstSerial().isEmpty()) {
                        return new ResultLogisticsDTO(Const.LOGISTICS.FAILED, getTextParam("logistics.create.bill.good.check.serial.null", goods.getGoodsCode()));
                    }
                    goods.setCheckSerial(productOfferingDTO.getCheckSerial());
                    //Kiem tra xem da truyen dai serial va so luong tuong ung chua
                    for (Serial serial : goods.getLstSerial()) {
                        if (DataUtil.isNullOrEmpty(serial.getFromSerial())) {
                            return new ResultLogisticsDTO(Const.LOGISTICS.FAILED, getTextParam("logistics.create.bill.good.check.serial.from.serial.null", goods.getGoodsCode()));
                        }
                    }
                    //Kiem tra xem da truyen dai serial va so luong tuong ung chua
                    for (Serial serial : goods.getLstSerial()) {
                        if (DataUtil.isNullOrEmpty(serial.getToSerial())) {
                            return new ResultLogisticsDTO(Const.LOGISTICS.FAILED, getTextParam("logistics.create.bill.good.check.serial.to.serial.null", goods.getGoodsCode()));
                        }
                    }
                    //Kiem tra xem da truyen dai serial va so luong tuong ung chua
                    for (Serial serial : goods.getLstSerial()) {
                        if (DataUtil.isNullOrEmpty(serial.getQuantity())) {
                            return new ResultLogisticsDTO(Const.LOGISTICS.FAILED, getTextParam("logistics.create.bill.good.check.serial.quantity.null", goods.getGoodsCode()));
                        }
                        try {
                            if (Long.parseLong(serial.getQuantity()) <= 0L) {
                                return new ResultLogisticsDTO(Const.LOGISTICS.FAILED, getTextParam("logistics.create.bill.good.check.serial.quantity.not.macth", goods.getGoodsCode()));
                            }
                        } catch (Exception e) {
                            logger.error(e.getMessage(), e);
                            return new ResultLogisticsDTO(Const.LOGISTICS.FAILED, getTextParam("logistics.create.bill.good.check.serial.quantity.not.macth", goods.getGoodsCode()));
                        }
                    }
                }
                goods.setStockModelId(productOfferingDTO.getProductOfferingId());
                lstStockModelIdLogistics.add(productOfferingDTO.getProductOfferingId());
            }

            //Check trung mat hang va trung trang thai trong danh sach mat hang
            for (int i = 0; i < lstGoods.size() - 1; i++) {
                for (int j = i + 1; j < lstGoods.size(); j++) {
                    if ((lstGoods.get(i).getGoodsCode().trim().toLowerCase().equals(lstGoods.get(j).getGoodsCode().trim().toLowerCase())) && (lstGoods.get(i).getGoodsState().equals(lstGoods.get(j).getGoodsState()))) {
                        return new ResultLogisticsDTO(Const.LOGISTICS.FAILED, getTextParam("logistics.create.bill.good.check.serial.exsit", lstGoods.get(i).getGoodsCode()));
                    }
                }
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }

        return new ResultLogisticsDTO(Const.LOGISTICS.SUCCESS, "Validate Success");
    }

    private ResultLogisticsDTO validateBillStockGeneral(BillStockDTO billStockDTO) throws Exception {

        if (billStockDTO == null) {
            return new ResultLogisticsDTO(Const.LOGISTICS.FAILED, getText("logistics.create.bill.null"));
        }

        if (DataUtil.isNullOrEmpty(billStockDTO.getOrderCode())) {
            return new ResultLogisticsDTO(Const.LOGISTICS.FAILED, getText("logistics.create.bill.orderCode.null"));
        }

        if (DataUtil.isNullOrEmpty(billStockDTO.getOrderType())) {
            return new ResultLogisticsDTO(Const.LOGISTICS.FAILED, getText("logistics.create.bill.orderType.null"));
        }

        if (!"1".equals(billStockDTO.getOrderType().trim()) && !"2".equals(billStockDTO.getOrderType().trim())) {
            return new ResultLogisticsDTO(Const.LOGISTICS.FAILED, getTextParam("logistics.create.bill.orderType.not.match", billStockDTO.getOrderType()));
        }

        if ("1".equals(billStockDTO.getOrderType().trim())) {

            if (DataUtil.isNullOrEmpty(billStockDTO.getInputType())) {
                return new ResultLogisticsDTO(Const.LOGISTICS.FAILED, getTextParam("logistics.create.bill.inputType.null", billStockDTO.getOrderCode()));
            }

            if (!"1".equals(billStockDTO.getInputType().trim()) && !"2".equals(billStockDTO.getInputType().trim())) {
                return new ResultLogisticsDTO(Const.LOGISTICS.FAILED, getTextParam("logistics.create.bill.inputType.not.match", billStockDTO.getInputType()));
            }
        }

        if ("2".equals(billStockDTO.getOrderType().trim())) {

            if (DataUtil.isNullOrEmpty(billStockDTO.getOutputType())) {
                return new ResultLogisticsDTO(Const.LOGISTICS.FAILED, getTextParam("logistics.create.bill.outputType.null", billStockDTO.getOrderCode()));
            }

            if (!"1".equals(billStockDTO.getOutputType().trim()) && !"2".equals(billStockDTO.getOutputType().trim())) {
                return new ResultLogisticsDTO(Const.LOGISTICS.FAILED, getTextParam("logistics.create.bill.outputType.not.match", billStockDTO.getOutputType()));
            }
        }

        return new ResultLogisticsDTO(Const.LOGISTICS.SUCCESS, "Validate Success");
    }
}
