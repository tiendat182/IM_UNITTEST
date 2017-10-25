package com.viettel.bccs.inventory.service.BaseStock;

import com.google.common.collect.Lists;
import com.viettel.bccs.im1.dto.PaymentDebitIm1DTO;
import com.viettel.bccs.im1.model.StockDebitIm1;
import com.viettel.bccs.im1.repo.StockDebitIm1Repo;
import com.viettel.bccs.im1.service.PaymentDebitIm1Service;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.common.ReportUtil;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.model.DebitRequestDetail;
import com.viettel.bccs.inventory.model.StockDebit;
import com.viettel.bccs.inventory.model.StockTransVoffice;
import com.viettel.bccs.inventory.repo.StockDebitRepo;
import com.viettel.bccs.inventory.repo.StockHandsetRepo;
import com.viettel.bccs.inventory.repo.StockTransVofficeRepo;
import com.viettel.bccs.inventory.service.*;
import com.viettel.bccs.sale.dto.PaymentDebitDTO;
import com.viettel.bccs.sale.service.PaymentDebitService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.voffice.autosign.Ver3AutoSign;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

/**
 * Created by hoangnt14 on 9/9/2016.
 */
@Service
public class BaseStockTransVofficeService extends BaseStockService {
    public static final Logger logger = Logger.getLogger(BaseStockTransVofficeService.class);
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    private final BaseMapper<DebitRequestDetail, DebitRequestDetailDTO> detailMapper = new BaseMapper(DebitRequestDetail.class, DebitRequestDetailDTO.class);

    @Autowired
    private StockTransService stockTransService;

    @Autowired
    private StockTransActionService stockTransActionService;

    @Autowired
    private StockTransDetailService stockTransDetailService;

    @Autowired
    private StockTransSerialService stockTransSerialService;

    @Autowired
    private StockRequestService stockRequestService;

    @Autowired
    private ProductOfferingService productOfferingService;

    @Autowired
    private ProductOfferTypeService productOfferTypeService;

    @Autowired
    private ShopService shopService;

    @Autowired
    private StockTransVofficeRepo repository;

    @Autowired
    private StockDeliverService stockDeliverService;

    @Autowired
    private ReasonService reasonService;

    @Autowired
    DebitRequestService debitRequestService;

    @Autowired
    DebitRequestDetailService debitRequestDetailService;

    @Autowired
    private StockDebitRepo stockDebitRepo;

    @Autowired
    private PaymentDebitService paymentDebitService;

    @Autowired
    private PaymentDebitIm1Service paymentDebitIm1Service;

    @Autowired
    private OptionSetValueService optionSetValueService;

    @Autowired
    private StockDebitIm1Repo stockDebitIm1Repo;


    @Override
    public void doPrepareData(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, FlagStockDTO flagStockDTO) throws Exception {

    }

    @Override
    public void doValidate(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, List<StockTransDetailDTO> lstStockTransDetail) throws LogicException, Exception {

    }

    public void updateVofficeDeliver(StockTransVoffice stockTransVoffice) throws LogicException, Exception {
        Date currentDate = getSysDate(em);
        StockDeliverDTO stockDeliverDTO = stockDeliverService.findOne(stockTransVoffice.getStockTransActionId());
        if (DataUtil.isNullObject(stockDeliverDTO)) {
            throw new LogicException("", "Khong ton tai giao dich stock_deliver");
        }
        if (DataUtil.safeEqual(stockTransVoffice.getStatus(), Const.VOFFICE_DOA.SIGN)) {
            stockDeliverDTO.setStatus(Const.STOCK_DELIVER.STOCK_DELIVER_STATUS_SIGN);
        } else {
            stockDeliverDTO.setStatus(Const.STOCK_DELIVER.STOCK_DELIVER_STATUS_REJECT);
        }
        stockDeliverDTO.setUpdateDate(currentDate);
        stockDeliverService.save(stockDeliverDTO);
        stockTransVoffice.setStatus(Const.VOFFICE_DOA.FINISH);
        stockTransVoffice.setLastModify(currentDate);
        repository.save(stockTransVoffice);
    }

    public void updateVofficeDevice(StockTransVoffice stockTransVoffice) throws LogicException, Exception {
        Date currentDate = getSysDate(em);
        StockTransDTO stockTransDevice = stockTransService.findStockTransByActionId(stockTransVoffice.getStockTransActionId());
        if (DataUtil.isNullObject(stockTransDevice)) {
            throw new LogicException("", "Khong ton tai giao dich stock_trans phan ra thiet bi");
        }
        stockTransDevice = stockTransService.findOne(stockTransDevice.getStockTransId());
        if (!shopService.checkExsitShopStaff(stockTransDevice.getFromOwnerId(), stockTransDevice.getFromOwnerType())) {
            throw new LogicException("", "Khong ton tai kho yeu cau phan ra thiet bi");
        }
        StockTransActionDTO stockTransActionExpNote = stockTransActionService.getStockTransActionByIdAndStatus(stockTransDevice.getStockTransId(), Lists.newArrayList(Const.STOCK_TRANS_STATUS.EXPORT_NOTE));
        if (DataUtil.isNullObject(stockTransActionExpNote)) {
            throw new LogicException("", "Khong ton tai giao dich lap phieu stock_trans_action yeu cau phan ra thiet bi voi stock_trans_id = " + stockTransDevice.getStockTransId());
        }
        List<StockTransDetailDTO> lstDetailExp = stockTransDetailService.findByStockTransId(stockTransDevice.getStockTransId());
        if (DataUtil.isNullOrEmpty(lstDetailExp)) {
            throw new LogicException("", "Khong ton tai giao dich stock_trans_detail yeu cau phan ra thiet bi voi stock_trans_id = " + stockTransDevice.getStockTransId());
        }
        StockRequestDTO stockRequestDTO = stockRequestService.getStockRequestByStockTransId(stockTransDevice.getStockTransId());
        if (DataUtil.isNullObject(stockRequestDTO)) {
            throw new LogicException("", "Khong ton tai giao dich stock_request phan ra thiet bi voi stock_trans_id = " + stockTransDevice.getStockTransId());
        }
        stockRequestDTO.setUpdateUser("SYS");
        stockRequestDTO.setUpdateDatetime(currentDate);
        //
        if (DataUtil.safeEqual(stockTransVoffice.getStatus(), Const.VOFFICE_DOA.SIGN)) {
            stockTransVoffice.setStatus(Const.VOFFICE_DOA.FINISH);
            stockTransVoffice.setLastModify(currentDate);
            repository.save(stockTransVoffice);
            //cap nhat stock_request
            stockRequestDTO.setStatus(Const.STOCK_REQUEST_DEVICE.APPROVE);
            stockRequestService.save(stockRequestDTO);
            //Luu cac giao dich kho
            saveStockTransDevice(stockTransDevice, stockTransActionExpNote, lstDetailExp, stockRequestDTO.getStockRequestId(), currentDate);

        } else if (DataUtil.safeEqual(stockTransVoffice.getStatus(), Const.VOFFICE_DOA.CANCEL)) {
            stockTransVoffice.setStatus(Const.VOFFICE_DOA.FINISH);
            stockTransVoffice.setLastModify(currentDate);
            repository.save(stockTransVoffice);
            //cap nhat stock_request
            stockRequestDTO.setStatus(Const.STOCK_REQUEST_DEVICE.CANCEL);
            stockRequestService.save(stockRequestDTO);
            //cap nhat stock_Trans
            stockTransDevice.setStatus(Const.STOCK_TRANS_STATUS.REJECT);
            stockTransService.save(stockTransDevice);
            //stock_trans_action
            StockTransActionDTO stockTransActionExp = getStockTransActionExp(stockTransDevice.getStockTransId(), currentDate, stockTransActionExpNote);
            stockTransActionExp.setStatus(Const.STOCK_TRANS_STATUS.REJECT);
            stockTransActionService.save(stockTransActionExp);
            //Cong stock_total kho xuat
            FlagStockDTO flagStockExp = new FlagStockDTO();
            flagStockExp.setExportStock(true);
            flagStockExp.setExpAvailableQuantity(Const.STOCK_TOTAL.PLUS_QUANTITY);
            flagStockExp.setExpCurrentQuantity(Const.STOCK_TOTAL.PLUS_QUANTITY);
            flagStockExp.setInsertStockTotalAudit(true);
            doSaveStockTotal(stockTransDevice, lstDetailExp, flagStockExp, stockTransActionExpNote);
            //Cap nhat STOCK_X
            flagStockExp = new FlagStockDTO();
            flagStockExp.setNewStatus(Const.STOCK_GOODS.STATUS_NEW);
            flagStockExp.setOldStatus(Const.STOCK_GOODS.STATUS_CONFIRM);
            doSaveStockGoods(stockTransDevice, lstDetailExp, flagStockExp);
        }

    }

    private void saveStockTransDevice(StockTransDTO stockTransDevice, StockTransActionDTO stockTransActionExpNote, List<StockTransDetailDTO> lstDetailExp, Long stockRequestId, Date currentDate) throws LogicException, Exception {
        ShopDTO shopDTO = shopService.findOne(stockTransDevice.getFromOwnerId());
        if (DataUtil.isNullObject(shopDTO)) {
            throw new LogicException("", "Kho thuc hien phieu yeu cau phan ra khong ton tai");
        }
        String[] arrShopId = shopDTO.getShopPath().split("_");
        if (DataUtil.isNullOrEmpty(arrShopId)) {
            throw new LogicException("", "Khong ton tai shopPath cua kho lap yeu cau");
        }
        ReasonDTO reasonDTO = reasonService.getReason(Const.REASON_TYPE.DEVICE_TRANSFER, Const.REASON_TYPE.DEVICE_TRANSFER);
        if (DataUtil.isNullObject(reasonDTO)) {
            throw new LogicException("", "Khong ton tai reason voi type DEVICE_TRANSFER");
        }
        //cap nhat stock_Trans hien tai
        stockTransDevice.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
        stockTransService.save(stockTransDevice);
        //Tao stock_trans_action
        StockTransActionDTO stockTransActionExp = getStockTransActionExp(stockTransDevice.getStockTransId(), currentDate, stockTransActionExpNote);
        //status = 4
        stockTransActionExp.setStatus(Const.STOCK_TRANS_STATUS.IMPORT_ORDER);
        stockTransActionExp.setActionCode("LN_" + stockTransDevice.getStockTransId());
        stockTransActionService.save(stockTransActionExp);
        //status = 5
        stockTransActionExp.setStatus(Const.STOCK_TRANS_STATUS.IMPORT_NOTE);
        stockTransActionExp.setActionCode("PN_" + stockTransDevice.getStockTransId());
        stockTransActionService.save(stockTransActionExp);
        //status = 6
        stockTransActionExp.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
        stockTransActionExp.setActionCode(null);
        stockTransActionService.save(stockTransActionExp);
        for (int i = arrShopId.length - 1; i > 1; i--) {
            StockTransDTO stockTransExportSave;
            // 1.0 Neu la giao dich hien tai
            if (i == arrShopId.length - 1) {
                stockTransExportSave = DataUtil.cloneBean(stockTransDevice);
            } else {
                // 1.1 Tao giao dich xuat cap tren
                StockTransDTO stockTransExport;
                if (!DataUtil.safeEqual(DataUtil.safeToLong(arrShopId[i]), 7282L)) {
                    stockTransExport = getStockTransDevice(DataUtil.safeToLong(arrShopId[i]), Const.OWNER_TYPE.SHOP_LONG,
                            DataUtil.safeToLong(arrShopId[i - 1]), Const.OWNER_TYPE.SHOP_LONG, currentDate, null, null);
                } else {
                    stockTransExport = getStockTransDevice(DataUtil.safeToLong(arrShopId[i]), Const.OWNER_TYPE.SHOP_LONG,
                            Const.TD_PARTNER_ID, Const.OWNER_TYPE.PARTNER_LONG, currentDate, null, null);
                }
                stockTransExportSave = stockTransService.save(stockTransExport);
                //Tao stock_trans_action
                //status = 2
                StockTransActionDTO stockTransActionExport = getStockTransActionExp(stockTransExportSave.getStockTransId(), currentDate, stockTransActionExpNote);
                stockTransActionExport.setStatus(Const.STOCK_TRANS_STATUS.EXPORT_NOTE);
                stockTransActionExport.setActionCode("PX_" + stockTransExportSave.getStockTransId());
                stockTransActionService.save(stockTransActionExport);
                //status = 3
                stockTransActionExport.setStatus(Const.STOCK_TRANS_STATUS.EXPORTED);
                stockTransActionExport.setActionCode(null);
                stockTransActionService.save(stockTransActionExport);
                if (!DataUtil.safeEqual(DataUtil.safeToLong(arrShopId[i]), 7282L)) {
                    //status = 4
                    stockTransActionExport.setStatus(Const.STOCK_TRANS_STATUS.IMPORT_ORDER);
                    stockTransActionExport.setActionCode("LN_" + stockTransExportSave.getStockTransId());
                    stockTransActionService.save(stockTransActionExport);
                    //status = 5
                    stockTransActionExport.setStatus(Const.STOCK_TRANS_STATUS.IMPORT_NOTE);
                    stockTransActionExport.setActionCode("PN_" + stockTransExportSave.getStockTransId());
                    stockTransActionService.save(stockTransActionExport);
                }
                //status = 6
                stockTransActionExport.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
                stockTransActionExport.setActionCode(null);
                stockTransActionService.save(stockTransActionExport);
                // Tao stock_Trans_Detail
                for (StockTransDetailDTO stockTransDetailDTO : lstDetailExp) {
                    StockTransDetailDTO stockTransDetailExp = getStockTransDetailImp(stockTransExportSave.getStockTransId(), currentDate, stockTransDetailDTO, stockTransDetailDTO.getStateId());
                    StockTransDetailDTO stockTransDetailExpSave = stockTransDetailService.save(stockTransDetailExp);
                    //stock_trans_serial
                    List<StockTransSerialDTO> lstSerialExp = stockTransSerialService.findByStockTransDetailId(stockTransDetailDTO.getStockTransDetailId());
                    if (DataUtil.isNullOrEmpty(lstSerialExp)) {
                        throw new LogicException("", "Khong ton tai giao dich stock_trans_serial phan ra thiet bi voi detail_id = " + stockTransDetailDTO.getStockTransDetailId());
                    }
                    for (StockTransSerialDTO stockTransSerialDTO : lstSerialExp) {
                        StockTransSerialDTO stockTransSerialExp = getStockTransSerialImp(stockTransExportSave.getStockTransId(), stockTransDetailExpSave.getStockTransDetailId(),
                                currentDate, stockTransSerialDTO, stockTransDetailDTO.getStateId());
                        stockTransSerialService.save(stockTransSerialExp);
                    }
                }
            }
            //1.2 Tao giao dich nhap tu cap tren
            StockTransDTO stockTransImport;
            if (!DataUtil.safeEqual(DataUtil.safeToLong(arrShopId[i]), 7282L)) {
                stockTransImport = getStockTransDevice(DataUtil.safeToLong(arrShopId[i - 1]), Const.OWNER_TYPE.SHOP_LONG,
                        DataUtil.safeToLong(arrShopId[i]), Const.OWNER_TYPE.SHOP_LONG, currentDate, reasonDTO.getReasonId(), stockTransExportSave.getStockTransId());
            } else {

                stockTransImport = getStockTransDevice(Const.TD_PARTNER_ID, Const.OWNER_TYPE.PARTNER_LONG,
                        DataUtil.safeToLong(arrShopId[i]), Const.OWNER_TYPE.SHOP_LONG, currentDate, Const.STOCK_TRANS.IMPORT_STOCK_FROM_PARTNER_REASON_ID, stockTransExportSave.getStockTransId());
            }
            StockTransDTO stockTransImportSave = stockTransService.save(stockTransImport);
            //Tao stock_trans_action
            StockTransActionDTO stockTransActionImport = getStockTransActionExp(stockTransImportSave.getStockTransId(), currentDate, stockTransActionExpNote);
            StockTransActionDTO stockTransActionImpNote = new StockTransActionDTO();
            if (!DataUtil.safeEqual(DataUtil.safeToLong(arrShopId[i]), 7282L)) {
                //status = 1
                stockTransActionImport.setStatus(Const.STOCK_TRANS_STATUS.EXPORT_ORDER);
                stockTransActionImport.setActionCode("LX_" + stockTransImportSave.getStockTransId());
                stockTransActionService.save(stockTransActionImport);
                //status = 2
                stockTransActionImport.setStatus(Const.STOCK_TRANS_STATUS.EXPORT_NOTE);
                stockTransActionImport.setActionCode("PX_" + stockTransImportSave.getStockTransId());
                stockTransActionImpNote = stockTransActionService.save(stockTransActionImport);
                //status = 3
                stockTransActionImport.setStatus(Const.STOCK_TRANS_STATUS.EXPORTED);
                stockTransActionImport.setActionCode(null);
                stockTransActionService.save(stockTransActionImport);
            }
            //status = 5
            stockTransActionImport.setStatus(Const.STOCK_TRANS_STATUS.IMPORT_NOTE);
            stockTransActionImport.setActionCode("PN_" + stockTransImportSave.getStockTransId());
            stockTransActionService.save(stockTransActionImport);
            //status = 6
            stockTransActionImport.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
            stockTransActionImport.setActionCode(null);
            stockTransActionService.save(stockTransActionImport);
            // Tao stock_Trans_Detail
            List<StockTransDetailDTO> lstDetailImp = Lists.newArrayList();
            for (StockTransDetailDTO stockTransDetailDTO : lstDetailExp) {
                List<StockDeviceTransferDTO> lstStockTransfer = stockTransSerialService.getLstDeviceTransfer(stockTransDetailDTO.getProdOfferId(), stockTransDetailDTO.getStateId(), stockRequestId);
                if (DataUtil.isNullOrEmpty(lstStockTransfer)) {
                    throw new LogicException("", "Chua cau hinh phan ra mat hang trong device_transfer voi prodOfferId: "
                            + stockTransDetailDTO.getProdOfferId() + " stateId : " + stockTransDetailDTO.getStateId() + " stockRequestId : " + stockRequestId);
                }
                for (StockDeviceTransferDTO stockDeviceTransferDTO : lstStockTransfer) {
                    StockTransDetailDTO transDetailDTO = new StockTransDetailDTO();
                    transDetailDTO.setProdOfferId(stockDeviceTransferDTO.getNewProdOfferId());
                    transDetailDTO.setQuantity(stockTransDetailDTO.getQuantity());
                    StockTransDetailDTO stockTransDetailImp = getStockTransDetailImp(stockTransImportSave.getStockTransId(), currentDate, transDetailDTO, stockDeviceTransferDTO.getNewStateId());
                    StockTransDetailDTO stockTransDetailImpSave = stockTransDetailService.save(stockTransDetailImp);
                    lstDetailImp.add(stockTransDetailImpSave);
                }
            }
            if (i == arrShopId.length - 1) {
                //Cong Stock_total
                FlagStockDTO flagStockImp = new FlagStockDTO();
                flagStockImp.setImportStock(true);
                flagStockImp.setImpAvailableQuantity(Const.STOCK_TOTAL.PLUS_QUANTITY);
                flagStockImp.setImpCurrentQuantity(Const.STOCK_TOTAL.PLUS_QUANTITY);
                flagStockImp.setInsertStockTotalAudit(true);
                doSaveStockTotal(stockTransImportSave, Lists.newArrayList(), flagStockImp, stockTransActionImpNote);
                //Cap nhat STOCK_X
                flagStockImp = new FlagStockDTO();
                flagStockImp.setNewStatus(Const.STOCK_GOODS.STATUS_SALE);
                flagStockImp.setOldStatus(Const.STOCK_GOODS.STATUS_CONFIRM);
                doSaveStockGoods(stockTransDevice, lstDetailExp, flagStockImp);
            }
        }
    }

    public void updateVofficeDOA(StockTransVoffice stockTransVoffice) throws LogicException, Exception {
        Date currentDate = getSysDate(em);
        StockTransDTO stockTransDOA = stockTransService.findStockTransByActionId(stockTransVoffice.getStockTransActionId());
        if (DataUtil.isNullObject(stockTransDOA)) {
            throw new LogicException("", "Khong ton tai giao dich stock_trans DOA");
        }
        stockTransDOA = stockTransService.findOne(stockTransDOA.getStockTransId());
        if (!shopService.checkExsitShopStaff(stockTransDOA.getFromOwnerId(), stockTransDOA.getFromOwnerType())) {
            throw new LogicException("", "Khong ton tai kho yeu cau chuyen doi hang");
        }
        StockTransActionDTO stockTransActionExpNote = stockTransActionService.getStockTransActionByIdAndStatus(stockTransDOA.getStockTransId(), Lists.newArrayList(Const.STOCK_TRANS_STATUS.EXPORT_NOTE));
        if (DataUtil.isNullObject(stockTransActionExpNote)) {
            throw new LogicException("", "Khong ton tai giao dich lap phieu stock_trans_action xuat DOA voi stock_trans_id = " + stockTransDOA.getStockTransId());
        }
        List<StockTransDetailDTO> lstDetailExp = stockTransDetailService.findByStockTransId(stockTransDOA.getStockTransId());
        if (DataUtil.isNullOrEmpty(lstDetailExp)) {
            throw new LogicException("", "Khong ton tai giao dich stock_trans_detail xuat DOA voi stock_trans_id = " + stockTransDOA.getStockTransId());
        }
        if (DataUtil.safeEqual(stockTransVoffice.getStatus(), Const.VOFFICE_DOA.SIGN)) {
            stockTransVoffice.setStatus(Const.VOFFICE_DOA.FINISH);
            stockTransVoffice.setLastModify(currentDate);
            repository.save(stockTransVoffice);
            //cap nhat stock_request
            StockRequestDTO stockRequestDTO = stockRequestService.getStockRequestByStockTransId(stockTransDOA.getStockTransId());
            if (DataUtil.isNullObject(stockRequestDTO)) {
                throw new LogicException("", "Khong ton tai giao dich stock_request DOA voi stock_trans_id = " + stockTransDOA.getStockTransId());
            }
            stockRequestDTO.setStatus(Const.STOCK_REQUEST_DOA.APPROVE);
            stockRequestDTO.setUpdateDatetime(currentDate);
            stockRequestService.save(stockRequestDTO);
            //cap nhat stock_Trans
            stockTransDOA.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
            stockTransService.save(stockTransDOA);
            //Tao stock_trans_action
            StockTransActionDTO stockTransActionExp = getStockTransActionExp(stockTransDOA.getStockTransId(), currentDate, stockTransActionExpNote);
            stockTransActionService.save(stockTransActionExp);
            //Giao dich nhap kho
            StockTransDTO stockTransImp = getStockTransImp(stockTransDOA, currentDate);
            StockTransDTO stockTransImpSave = stockTransService.save(stockTransImp);
            //stock_Trans_action status = 5
            StockTransActionDTO stockTransActionImpNote = getStockTransActionImp(stockTransImpSave.getStockTransId(), currentDate, stockTransActionExpNote);
            stockTransActionService.save(stockTransActionImpNote);
            //status = 6
            stockTransActionImpNote.setStockTransActionId(null);
            stockTransActionImpNote.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
            stockTransActionImpNote.setActionCode(null);
            stockTransActionService.save(stockTransActionImpNote);
            //stock_trans_detail
            List<StockTransDetailDTO> lstDetailImp = Lists.newArrayList();
            for (StockTransDetailDTO stockTransDetailDTO : lstDetailExp) {
                StockTransDetailDTO stockTransDetailImp = getStockTransDetailImp(stockTransImpSave.getStockTransId(), currentDate, stockTransDetailDTO, Const.STATE_STATUS.STATE_DOA);
                StockTransDetailDTO stockTransDetailImpSave = stockTransDetailService.save(stockTransDetailImp);
                stockTransDetailImp.setStockTransDetailId(stockTransDetailImpSave.getStockTransDetailId());
                // check mat hang no_serial
                ProductOfferingDTO productOfferingDTO = productOfferingService.findOne(stockTransDetailImp.getProdOfferId());
                ProductOfferTypeDTO productOfferingTypeDTO = productOfferTypeService.findOne(productOfferingDTO.getProductOfferTypeId());
                if (DataUtil.safeEqual(productOfferingDTO.getCheckSerial(), Const.PRODUCT_OFFERING.CHECK_SERIAL)) {
                    //stock_trans_serial
                    List<StockTransSerialDTO> lstSerialExp = stockTransSerialService.findByStockTransDetailId(stockTransDetailDTO.getStockTransDetailId());
                    if (DataUtil.isNullOrEmpty(lstSerialExp)) {
                        throw new LogicException("", "Khong ton tai giao dich stock_trans_serial xuat DOA voi detail_id = " + stockTransDetailDTO.getStockTransDetailId());
                    }
                    for (StockTransSerialDTO stockTransSerialDTO : lstSerialExp) {
                        StockTransSerialDTO stockTransSerialImp = getStockTransSerialImp(stockTransImpSave.getStockTransId(), stockTransDetailImpSave.getStockTransDetailId(),
                                currentDate, stockTransSerialDTO, Const.STATE_STATUS.STATE_DOA);
                        stockTransSerialService.save(stockTransSerialImp);
                    }
                }
                stockTransDetailImp.setTableName(productOfferingTypeDTO.getTableName());
                stockTransDetailImp.setProdOfferTypeId(productOfferingDTO.getProductOfferTypeId());
                stockTransDetailImp.setStateId(Const.STATE_STATUS.NEW);
                lstDetailImp.add(stockTransDetailImp);
            }
            //Cong Stock_total
            FlagStockDTO flagStockImp = new FlagStockDTO();
            flagStockImp.setImportStock(true);
            flagStockImp.setImpAvailableQuantity(Const.STOCK_TOTAL.PLUS_QUANTITY);
            flagStockImp.setImpCurrentQuantity(Const.STOCK_TOTAL.PLUS_QUANTITY);
            flagStockImp.setStateIdForReasonId(Const.STATE_STATUS.STATE_DOA);
            flagStockImp.setInsertStockTotalAudit(true);
            doSaveStockTotal(stockTransImpSave, lstDetailImp, flagStockImp, stockTransActionImpNote);
            //Cap nhat STOCK_X
            flagStockImp = new FlagStockDTO();
            flagStockImp.setNewStatus(Const.STATE_STATUS.NEW);
            flagStockImp.setOldStatus(Const.STATE_STATUS.RETRIEVE);
            flagStockImp.setUpdateSaleDate(true);
            flagStockImp.setStateIdForReasonId(Const.STATE_STATUS.STATE_DOA);
            doSaveStockGoods(stockTransDOA, lstDetailImp, flagStockImp);

        } else if (DataUtil.safeEqual(stockTransVoffice.getStatus(), Const.VOFFICE_DOA.CANCEL)) {
            stockTransVoffice.setStatus(Const.VOFFICE_DOA.FINISH);
            stockTransVoffice.setLastModify(currentDate);
            repository.save(stockTransVoffice);
            //cap nhat stock_request
            StockRequestDTO stockRequestDTO = stockRequestService.getStockRequestByStockTransId(stockTransDOA.getStockTransId());
            if (DataUtil.isNullObject(stockRequestDTO)) {
                throw new LogicException("", "Khong ton tai giao dich stock_request DOA voi stock_trans_id = " + stockTransDOA.getStockTransId());
            }
            stockRequestDTO.setStatus(Const.STOCK_REQUEST_DOA.CANCEL);
            stockRequestDTO.setUpdateDatetime(currentDate);
            stockRequestService.save(stockRequestDTO);
            //cap nhat stock_Trans
            stockTransDOA.setStatus(Const.STOCK_TRANS_STATUS.REJECT);
            stockTransService.save(stockTransDOA);
            //stock_trans_action
            StockTransActionDTO stockTransActionExp = getStockTransActionExp(stockTransDOA.getStockTransId(), currentDate, stockTransActionExpNote);
            stockTransActionExp.setStatus(Const.STOCK_TRANS_STATUS.REJECT);
            stockTransActionService.save(stockTransActionExp);
            //Cong stock_total kho xuat
            FlagStockDTO flagStockExp = new FlagStockDTO();
            flagStockExp.setExportStock(true);
            flagStockExp.setExpAvailableQuantity(Const.STOCK_TOTAL.PLUS_QUANTITY);
            flagStockExp.setExpCurrentQuantity(Const.STOCK_TOTAL.PLUS_QUANTITY);
            flagStockExp.setInsertStockTotalAudit(true);
            doSaveStockTotal(stockTransDOA, lstDetailExp, flagStockExp, stockTransActionExpNote);
            //Cap nhat STOCK_X
            for (StockTransDetailDTO stockTransDetailDTO : lstDetailExp) {
                ProductOfferingDTO productOfferingDTO = productOfferingService.findOne(stockTransDetailDTO.getProdOfferId());
                ProductOfferTypeDTO productOfferingTypeDTO = productOfferTypeService.findOne(productOfferingDTO.getProductOfferTypeId());
                stockTransDetailDTO.setTableName(productOfferingTypeDTO.getTableName());
                stockTransDetailDTO.setProdOfferTypeId(productOfferingDTO.getProductOfferTypeId());
            }
            flagStockExp = new FlagStockDTO();
            flagStockExp.setNewStatus(Const.STATE_STATUS.NEW);
            flagStockExp.setOldStatus(Const.STATE_STATUS.RETRIEVE);
            flagStockExp.setUpdateSaleDate(true);
            doSaveStockGoods(stockTransDOA, lstDetailExp, flagStockExp);
        }

    }

    private StockTransDTO getStockTransDevice(Long fromOwnerId, Long fromOwnerType, Long toOwnerId, Long toOwnerType, Date currentDate, Long reasonId, Long fromStockTransId) {
        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setCreateDatetime(currentDate);
        stockTransDTO.setFromOwnerId(fromOwnerId);
        stockTransDTO.setFromOwnerType(fromOwnerType);
        stockTransDTO.setToOwnerId(toOwnerId);
        stockTransDTO.setToOwnerType(toOwnerType);
        stockTransDTO.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
        stockTransDTO.setStockTransType(Const.STOCK_TRANS_TYPE.EXPORT);
        if (!DataUtil.isNullOrZero(reasonId)) {
            stockTransDTO.setReasonId(reasonId);
        }
        if (!DataUtil.isNullOrZero(fromStockTransId)) {
            stockTransDTO.setFromStockTransId(fromStockTransId);
        }
        return stockTransDTO;
    }


    private StockTransActionDTO getStockTransActionExp(Long stockTransId, Date currentDate, StockTransActionDTO stockTransExpNote) {
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setStockTransId(stockTransId);
        stockTransActionDTO.setActionCode(null);
        stockTransActionDTO.setSignCaStatus(null);
        stockTransActionDTO.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
        stockTransActionDTO.setCreateDatetime(currentDate);
        stockTransActionDTO.setCreateUser(stockTransExpNote.getCreateUser());
        stockTransActionDTO.setActionStaffId(stockTransExpNote.getActionStaffId());
        return stockTransActionDTO;
    }

    private StockTransDTO getStockTransImp(StockTransDTO stockTransExp, Date currentDate) {
        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setCreateDatetime(currentDate);
        stockTransDTO.setFromOwnerId(null);
        stockTransDTO.setFromOwnerType(Const.OWNER_TYPE_CUST);
        stockTransDTO.setToOwnerId(stockTransExp.getFromOwnerId());
        stockTransDTO.setToOwnerType(stockTransExp.getFromOwnerType());
        stockTransDTO.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
        stockTransDTO.setStockTransType(Const.STOCK_TRANS_TYPE.EXPORT);
        stockTransDTO.setFromStockTransId(stockTransExp.getStockTransId());
        return stockTransDTO;
    }

    private StockTransActionDTO getStockTransActionImp(Long stockTransId, Date currentDate, StockTransActionDTO stockTransExpNote) {
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setStockTransId(stockTransId);
        stockTransActionDTO.setStatus(Const.STOCK_TRANS_STATUS.IMPORT_NOTE);
        stockTransActionDTO.setCreateDatetime(currentDate);
        stockTransActionDTO.setActionCode("PN" + stockTransId);
        stockTransActionDTO.setCreateUser(stockTransExpNote.getCreateUser());
        stockTransActionDTO.setActionStaffId(stockTransExpNote.getActionStaffId());
        return stockTransActionDTO;
    }

    private StockTransDetailDTO getStockTransDetailImp(Long stockTransId, Date currentDate, StockTransDetailDTO stockTransDetailExp, Long stateId) {
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setStockTransId(stockTransId);
        stockTransDetailDTO.setProdOfferId(stockTransDetailExp.getProdOfferId());
        stockTransDetailDTO.setQuantity(stockTransDetailExp.getQuantity());
        stockTransDetailDTO.setStateId(stateId);
        stockTransDetailDTO.setCreateDatetime(currentDate);
        return stockTransDetailDTO;

    }

    private StockTransSerialDTO getStockTransSerialImp(Long stockTransId, Long stockTransDetailId, Date currentDate, StockTransSerialDTO stockTransSerialExp, Long stateId) {
        StockTransSerialDTO stockTransSerialDTO = new StockTransSerialDTO();
        stockTransSerialDTO.setStockTransId(stockTransId);
        stockTransSerialDTO.setStockTransDetailId(stockTransDetailId);
        stockTransSerialDTO.setFromSerial(stockTransSerialExp.getFromSerial());
        stockTransSerialDTO.setToSerial(stockTransSerialExp.getToSerial());
        stockTransSerialDTO.setProdOfferId(stockTransSerialExp.getProdOfferId());
        stockTransSerialDTO.setStateId(stateId);
        stockTransSerialDTO.setCreateDatetime(currentDate);
        return stockTransSerialDTO;
    }

    public void updateVofficeDebit(StockTransVoffice stockTransVoffice) throws LogicException, Exception {
        Date currentDate = getSysDate(em);
        DebitRequestDTO debitRequestDTO = debitRequestService.findOne(stockTransVoffice.getStockTransActionId());
        if (DataUtil.isNullObject(debitRequestDTO)) {
            throw new LogicException("", "process.voffice.debit.request.debit.not.found", stockTransVoffice.getStockTransActionId());
        }
        List<DebitRequestDetailDTO> lstDetailDTOs = debitRequestDetailService.getLstDetailByRequestId(debitRequestDTO.getRequestId());
        if (DataUtil.isNullOrEmpty(lstDetailDTOs)) {
            throw new LogicException("", "process.voffice.debit.request.detail.not.found", stockTransVoffice.getStockTransActionId());
        }
        //ky duyet yeu cau
        if (DataUtil.safeEqual(stockTransVoffice.getStatus(), Const.VOFFICE_DOA.SIGN)) {
            debitRequestDTO.setStatus(Const.DEBIT_REQUEST_STATUS.STATUS_APPROVED);
            for (DebitRequestDetailDTO debitRequestDetailDTO : lstDetailDTOs) {
                //cap nhat trang thai
                debitRequestDetailDTO.setStatus(Const.DEBIT_REQUEST_STATUS.STATUS_APPROVED);
                debitRequestDetailService.create(debitRequestDetailDTO);
                //Neu la han muc kho hang
                if (DataUtil.safeEqual(debitRequestDetailDTO.getPaymentType(), Const.DEBIT_TYPE.DEBIT_STOCK)) {
                    // luu han muc
                    DebitRequestDetail debitRequestDetail = detailMapper.toPersistenceBean(debitRequestDetailDTO);
                    StockDebit stockDebit = stockDebitRepo.buildStockDebitFromDebitRequestDetail(debitRequestDetail, "SYS");
                    stockDebitRepo.save(stockDebit);
                    //Cap nhat IM1
                    List<OptionSetValueDTO> lstConfigEnableBccs1 = optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_PAYMENT_BCCS1");
                    if (!DataUtil.isNullOrEmpty(lstConfigEnableBccs1) && !DataUtil.isNullObject(lstConfigEnableBccs1.get(0).getValue())) {
                        if (Const.ENABLE_UPDATE_BCCS1.equals(DataUtil.safeToLong(lstConfigEnableBccs1.get(0).getValue()))) {
                            StockDebitIm1 stockDebitIm1 = stockDebitIm1Repo.buildStockDebitFromDebitRequestDetail(debitRequestDetail, "SYS");
                            //stockDebitIm1Repo.save(stockDebitIm1);
                        }
                    }
                } else {
                    //Neu la han muc cong no
                    PaymentDebitDTO paymentDebitDTO = paymentDebitService.getPaymentDebit(debitRequestDetailDTO.getOwnerId(), DataUtil.safeToLong(debitRequestDetailDTO.getOwnerType()));
                    if (DataUtil.isNullObject(paymentDebitDTO)) {
                        paymentDebitDTO = new PaymentDebitDTO();
                        paymentDebitDTO.setOwnerId(debitRequestDetailDTO.getOwnerId());
                        paymentDebitDTO.setOwnerType(DataUtil.safeToLong(debitRequestDetailDTO.getOwnerType()));
                        paymentDebitDTO.setCreateDate(currentDate);
                        paymentDebitDTO.setCreateUser("SYS");
                    }
                    paymentDebitDTO.setLastUpdateDate(currentDate);
                    paymentDebitDTO.setLastUpdateUser("SYS");
                    paymentDebitDTO.setPaymentGroupId(debitRequestDetailDTO.getPaymentGroupId());
                    paymentDebitDTO.setPaymentGroupServiceId(debitRequestDetailDTO.getPaymentGroupServiceId());
                    PaymentDebitDTO paymentDebitSave = paymentDebitService.create(paymentDebitDTO);

                    //Cap nhat IM1
                    List<OptionSetValueDTO> lstConfigEnableBccs1 = optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_PAYMENT_BCCS1");
                    if (!DataUtil.isNullOrEmpty(lstConfigEnableBccs1) && !DataUtil.isNullObject(lstConfigEnableBccs1.get(0).getValue())) {
                        if (Const.ENABLE_UPDATE_BCCS1.equals(DataUtil.safeToLong(lstConfigEnableBccs1.get(0).getValue()))) {
                            PaymentDebitIm1DTO paymentDebitIm1DTO = paymentDebitIm1Service.getPaymentDebit(debitRequestDetailDTO.getOwnerId(), DataUtil.safeToLong(debitRequestDetailDTO.getOwnerType()));
                            if (DataUtil.isNullObject(paymentDebitIm1DTO)) {
                                paymentDebitIm1DTO = new PaymentDebitIm1DTO();
                                paymentDebitIm1DTO.setPaymentDebitId(paymentDebitSave.getPaymentDebitId());
                                paymentDebitIm1DTO.setOwnerId(debitRequestDetailDTO.getOwnerId());
                                paymentDebitIm1DTO.setOwnerType(DataUtil.safeToLong(debitRequestDetailDTO.getOwnerType()));
                                paymentDebitIm1DTO.setCreateDate(currentDate);
                                paymentDebitIm1DTO.setCreateUser("SYS");
                            }
                            paymentDebitIm1DTO.setLastUpdateDate(currentDate);
                            paymentDebitIm1DTO.setLastUpdateUser("SYS");
                            paymentDebitIm1DTO.setPaymentGroupId(debitRequestDetailDTO.getPaymentGroupId());
                            paymentDebitIm1DTO.setPaymentGroupServiceId(debitRequestDetailDTO.getPaymentGroupServiceId());
                            paymentDebitIm1Service.create(paymentDebitIm1DTO);
                        }
                    }
                }
            }
        } else {
            debitRequestDTO.setStatus(Const.DEBIT_REQUEST_STATUS.STATUS_REJECT);
            for (DebitRequestDetailDTO debitRequestDetailDTO : lstDetailDTOs) {
                debitRequestDetailDTO.setStatus(Const.DEBIT_REQUEST_STATUS.STATUS_REJECT);
                debitRequestDetailService.create(debitRequestDetailDTO);
            }
        }

        debitRequestDTO.setLastUpdateTime(currentDate);
        debitRequestDTO.setLastUpdateUser("SYS");
        debitRequestService.save(debitRequestDTO);
        //Cap nhat stock_trans_voffice
        stockTransVoffice.setStatus(Const.VOFFICE_DOA.FINISH);
        stockTransVoffice.setLastModify(currentDate);
        repository.save(stockTransVoffice);
    }

}
