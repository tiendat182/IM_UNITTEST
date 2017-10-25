package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.repo.StockHandsetRepo;
import com.viettel.bccs.inventory.repo.StockTotalRepo;
import com.viettel.bccs.inventory.service.BaseStock.BaseStockService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.fw.service.BaseServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.jws.WebParam;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.xml.crypto.Data;
import java.util.Date;
import java.util.List;

@Service
public class ExternalInventoryServiceImpl extends BaseServiceImpl implements ExternalInventoryService {
    public static final Logger logger = Logger.getLogger(ExternalInventoryService.class);
    @Autowired
    private StockTotalRepo stockTotalRepo;
    @Autowired
    private StockHandsetRepo stockHandsetRepo;

    @Autowired
    private ProductOfferingService productOfferingService;
    @Autowired
    private ProductOfferTypeService productOfferTypeService;
    @Autowired
    private StaffService staffService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private ExternalInventoryTransactionService externalInventoryTransactionService;
    @Autowired
    private StockTotalCycleService stockTotalCycleService;
    @Autowired
    private StockTransService stockTransService;

    @Override
    public ChangeResultDTO change2gTo3g(String serial2G, Long prodOfferId2G, String serial3G, Long prodOfferId3G, Long staffId, Long collStaffId, String custName, String custTel) throws LogicException, Exception {
        // Kiem tra mat hang truyen vao co ton tai khong.
        ChangeResultDTO result = new ChangeResultDTO();
        try {
            ProductOfferingDTO productOffering2G = productOfferingService.findOne(prodOfferId2G);
            if (DataUtil.isNullObject(productOffering2G)) {
                throw new LogicException("", getTextParam("sp.exportStockCollaborator.stockTransDetail.prodOfferCode.notFoundDB", String.valueOf(prodOfferId2G)));
            }
            ProductOfferTypeDTO productOfferingType2G = productOfferTypeService.findOne(productOffering2G.getProductOfferTypeId());
            if (DataUtil.isNullObject(productOfferingType2G)) {
                throw new LogicException("", getText("validate.stock.inspect.productOfferType.notFound"));
            }
            if (!DataUtil.safeEqual(Const.PRODUCT_OFFER_TYPE.PHONE, productOfferingType2G.getProductOfferTypeId())) {
                throw new LogicException("", getTextParam("change2gTo3g.serial2g.not.handset", serial2G));
            }
            ProductOfferingDTO productOffering3G = productOfferingService.findOne(prodOfferId3G);
            if (DataUtil.isNullObject(productOffering3G)) {
                throw new LogicException("", getTextParam("sp.exportStockCollaborator.stockTransDetail.prodOfferCode.notFoundDB", String.valueOf(prodOfferId2G)));
            }
            ProductOfferTypeDTO productOfferingType3G = productOfferTypeService.findOne(productOffering3G.getProductOfferTypeId());
            if (DataUtil.isNullObject(productOfferingType3G)) {
                throw new LogicException("", getTextParam("validate.stock.inspect.productOfferType.notFound"));
            }
            if (!DataUtil.safeEqual(Const.PRODUCT_OFFER_TYPE.PHONE, productOfferingType3G.getProductOfferTypeId())) {
                throw new LogicException("", getTextParam("change2gTo3g.serial3g.not.handset", serial3G));
            }
            StaffDTO staffDTO = staffService.findOne(staffId);
            if (DataUtil.isNullObject(staffDTO) || !DataUtil.safeEqual(Const.STATUS_ACTIVE, staffDTO.getStatus())) {
                throw new LogicException("", getText("sp.staff.not.found"));
            }
            if (!DataUtil.isNullOrZero(collStaffId)) {
                StaffDTO staffCTV = staffService.findOne(collStaffId);
                if (DataUtil.isNullObject(staffCTV) || !DataUtil.safeEqual(Const.STATUS_ACTIVE, staffCTV.getStatus())) {
                    throw new LogicException("", getText("change2gTo3g.staff.CTV.notFoundDB"));
                }
                if (!DataUtil.safeEqual(staffDTO.getStaffId(), staffCTV.getStaffOwnerId())) {
                    throw new LogicException("", getText("change2gTo3g.staff.CTV.not.owner"));
                }
            }
            if (DataUtil.isNullOrEmpty(serial3G)) {
                throw new LogicException("", getText("change2gTo3g.serial2g.isNull"));
            }
            if (DataUtil.isNullOrEmpty(serial3G)) {
                throw new LogicException("", getText("change2gTo3g.serial3g.isNull"));
            }
            if (DataUtil.isNullOrEmpty(custName)) {
                throw new LogicException("", getText("err.change2GTo3GForm.Empty.CustomerName"));
            }
            if (DataUtil.isNullOrEmpty(custTel)) {
                throw new LogicException("", getText("err.change2GTo3GForm.Empty.CustomerTel"));
            }
            // Kiem tra serial 2G da ton tai chua.
            StockHandsetDTO stockHandset2G = stockHandsetRepo.getStockHandset(prodOfferId2G, serial2G, null, null, null);
            if (!DataUtil.isNullObject(stockHandset2G)) {
                throw new LogicException("", getTextParam("change2gTo3g.already", serial2G));
            }
            StockHandsetDTO stockHandsetDTO = stockHandsetRepo.getStockHandset(prodOfferId3G, serial3G, Const.OWNER_TYPE.STAFF_LONG, staffDTO.getStaffId(), null);
            if (DataUtil.isNullObject(stockHandsetDTO)) {
                throw new LogicException("", getTextParam("change2gTo3g.serial3g.not.foundDB", String.valueOf(prodOfferId3G), serial3G));
            }
            if (!DataUtil.safeEqual(DataUtil.safeToLong(Const.STATUS.ACTIVE), stockHandsetDTO.getStatus())
                    || !DataUtil.safeEqual(Const.STATE_STATUS.NEW, stockHandsetDTO.getStateId())) {
                throw new LogicException("", getTextParam("change2gTo3g.serial3g.status.inactive", String.valueOf(prodOfferId3G), serial3G));
            }
            result = externalInventoryTransactionService.saveChange2gTo3g(staffDTO, serial2G, serial3G, productOfferingType2G, productOfferingType3G, productOffering2G,
                    productOffering3G, collStaffId, custName, custTel);
        } catch (LogicException ex) {
            logger.error("LogicException", ex);
            result = new ChangeResultDTO(false, ex.getErrorCode(), ex.getKeyMsg(), ex.getParamsMsg());
        } catch (Exception ex) {
            logger.error("Exception", ex);
            result = new ChangeResultDTO(false, "Error", getText("common.error.happen"));
        }
        // </editor-fold>
        return result;
    }


    @Override
    public List<ProductOfferingDTO> getProductInStock(Long ownerType, Long ownerId, Long prodOfferId) throws LogicException, Exception {
        if (DataUtil.isNullOrZero(ownerType)) {
            throw new LogicException("", getText("warranty.stock.ownerType.invalid.empty.format"));
        }
        if (DataUtil.isNullOrZero(ownerId)) {
            throw new LogicException("", getText("warranty.stock.ownerid.invalid.empty.format"));
        }
        if (DataUtil.safeEqual(ownerType, Const.OWNER_TYPE.SHOP_LONG)) {
            ShopDTO shopDTO = shopService.findOne(ownerId);
            if (DataUtil.isNullObject(shopDTO) || !DataUtil.safeEqual(shopDTO.getStatus(), Const.STATUS_ACTIVE)) {
                throw new LogicException("", getText("sp.ownerId.notFoundDB"));
            }
        } else if (DataUtil.safeEqual(ownerType, Const.OWNER_TYPE.STAFF_LONG)) {
            StaffDTO staff = staffService.findOne(ownerId);
            if (DataUtil.isNullObject(staff) || !DataUtil.safeEqual(staff.getStatus(), Const.STATUS_ACTIVE)) {
                throw new LogicException("", getText("sp.ownerId.notFoundDB"));
            }
        } else {
            throw new LogicException("", getText("sp.ownerType.invalid"));
        }
        return stockTotalRepo.getProductInStockTotal(ownerType, ownerId, prodOfferId, null);
    }

    @Override
    public StockHandsetDTO getStockHandset(Long prodOfferId, String serial) throws LogicException, Exception {
        if (DataUtil.isNullOrZero(prodOfferId)) {
            throw new LogicException("", getText("sp.getStockTransSerial.prodOfferId.isnull"));
        }
        if (DataUtil.isNullOrEmpty(serial)) {
            throw new LogicException("", getText("sp.staffExportStockToShop.serial.invalid"));
        }
        serial = serial.trim();
        return stockHandsetRepo.getStockHandset(prodOfferId, serial, null, null, null);
    }

    @Override
    public List<StockTotalCycleReportDTO> getStockTotalCycle(Date fromDate, Date toDate, Long stateId, Long productOfferTypeId, Long prodOfferId, Long ownerType, Long ownerId) throws Exception {
        return stockTotalCycleService.getStockCycle(fromDate, toDate, stateId, productOfferTypeId, prodOfferId, ownerType, ownerId);
    }

    @Override
    public BaseMessage cancelChange2gTo3g(Long stockTransId2G, Long stockTransId3G) throws LogicException, Exception {
        BaseMessage result = new BaseMessage();
        try {
            StockTransDTO stockTrans2GDTO = stockTransService.findOne(stockTransId2G);
            if (DataUtil.isNullObject(stockTrans2GDTO)) {
                throw new LogicException("", getTextParam("cancel.change.2g.3g.transaction.2g.not.found", stockTransId2G.toString()));
            }
            if (!DataUtil.safeEqual(stockTrans2GDTO.getStatus(), Const.STOCK_TRANS_STATUS.IMPORTED)) {
                throw new LogicException("", getText("cancel.change.2g.3g.transaction.2g.status"));
            }
            StockTransDTO stockTrans3GDTO = stockTransService.findOne(stockTransId3G);
            if (DataUtil.isNullObject(stockTrans3GDTO)) {
                throw new LogicException("", getTextParam("cancel.change.2g.3g.transaction.3g.not.found", stockTransId3G.toString()));
            }
            if (!DataUtil.safeEqual(stockTrans3GDTO.getStatus(), Const.STOCK_TRANS_STATUS.IMPORTED)) {
                throw new LogicException("", getText("cancel.change.2g.3g.transaction.3g.status"));
            }
            if (!DataUtil.safeEqual(stockTrans2GDTO.getStockTransId(), stockTrans3GDTO.getFromStockTransId())) {
                throw new LogicException("", getText("cancel.change.2g.3g.transaction.3g.not.match"));
            }
            result = externalInventoryTransactionService.cancelChange2gTo3g(stockTrans2GDTO, stockTrans3GDTO);
        } catch (LogicException ex) {
            logger.error("LogicException", ex);
            result = new BaseMessage(false, ex.getErrorCode(), ex.getKeyMsg(), ex.getParamsMsg());
        } catch (Exception ex) {
            logger.error("Exception", ex);
            result = new BaseMessage(false, "Error", getText("common.error.happen"));
        }
        // </editor-fold>
        return result;
    }
}

