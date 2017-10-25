package com.viettel.bccs.inventory.service;

import com.viettel.bccs.im1.dto.StockModelIm1DTO;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.service.BaseServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by DungPT16 on 7/14/2016.
 */
@Service
public class BankPlusServiceImpl extends BaseServiceImpl implements BankPlusService {
    public static final Logger logger = Logger.getLogger(SmartphoneService.class);
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @Autowired
    private StockKitService stockKitService;
    @Autowired
    private StaffService staffService;
    @Autowired
    private ProductOfferingService productOfferingService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private BankPlusServiceExecute bankPlusServiceExecute;
    @Autowired
    private OptionSetValueService optionSetValueService;


    @Override
    public String checkSerial(String staffCode, String serial, String prodOfferCode) throws Exception {

        if (DataUtil.isNullOrEmpty(staffCode) || DataUtil.isNullOrEmpty(serial) || DataUtil.isNullOrEmpty(prodOfferCode)) {
            return getText("bankplus.service.staff.input.checkserial.notEmpty");
        }

        StaffDTO staffDTO = staffService.getStaffByStaffCode(staffCode);
        if (staffDTO == null) {
            return getText("bankplus.service.staff.empty");
        }

        StockKitDTO stockKitDTO = stockKitService.getStockKitBySerial(serial);
        if (stockKitDTO == null) {
            return getText("bankplus.service.stockkit.empty");
        }

        ProductOfferingDTO offeringDTO = productOfferingService.getProductByCode(prodOfferCode);
        if (offeringDTO == null) {
            return getText("bankplus.service.product.empty");
        }

        if (!DataUtil.safeEqual(stockKitDTO.getProdOfferId(), offeringDTO.getProductOfferingId())) {
            return getText("bankplus.service.stockkit.invalid");
        }

        if (!DataUtil.safeEqual(stockKitDTO.getOwnerId(), staffDTO.getStaffId()) || !DataUtil.safeEqual(stockKitDTO.getOwnerType(), Const.OWNER_TYPE.STAFF_LONG)) {
            return getText("bankplus.service.serial.not.in.stock");
        }

        if (!DataUtil.safeEqual(1L, stockKitDTO.getStatus())) {
            return getText("bankplus.service.serial.invalid.stock.public");
        }

        if (!DataUtil.safeEqual(1L, stockKitDTO.getStateId())) {
            return getText("bankplus.service.stockit.not.new.product");
        }

        List<OptionSetValueDTO> lstConfigEnableBccs1 = optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1");
        if (!DataUtil.isNullOrEmpty(lstConfigEnableBccs1) && !DataUtil.isNullObject(lstConfigEnableBccs1.get(0).getValue())) {
            if (Const.ENABLE_UPDATE_BCCS1.equals(DataUtil.safeToLong(lstConfigEnableBccs1.get(0).getValue()))) {
                return validStockKitProduct(serial, prodOfferCode, staffDTO.getStaffId(), Const.OWNER_TYPE.STAFF_LONG);
            }
        }
        return "OK";
    }

    private String validStockKitProduct(String serial, String stockModeCode, Long ownerId, Long ownerType) {
        StockKitIm1DTO stockKitIm1DTO = bankPlusServiceExecute.getStockKitIm1BySerial(serial);
        if (stockKitIm1DTO == null) {
            return getText("bankplus.service.stockkit.bccs1.empty");
        }
        StockModelIm1DTO stockModelIm1DTO = bankPlusServiceExecute.getStockModelIm1DTO(stockModeCode);
        if (stockModelIm1DTO == null) {
            return getText("bankplus.service.product.bccs1.empty");
        }
        if (!DataUtil.safeEqual(stockKitIm1DTO.getStockModelId(), stockModelIm1DTO.getStockModelId())) {
            return getText("bankplus.service.stockkit.bccs1.invalid");
        }

        if (!DataUtil.safeEqual(stockKitIm1DTO.getOwnerId(), ownerId) || !DataUtil.safeEqual(stockKitIm1DTO.getOwnerType(), ownerType)) {
            return getText("bankplus.service.serial.not.in.bccs1.stock");
        }

        if (!DataUtil.safeEqual(1L, stockKitIm1DTO.getStatus())) {
            return getText("bankplus.service.serial.invalid.stock.bccs1.public");
        }

        if (!DataUtil.safeEqual(1L, stockKitIm1DTO.getStateId())) {
            return getText("bankplus.service.stockit.not.new.bccs1.product");
        }
        return "OK";
    }


    /**
     * ham tao giao dich nhan vien xuat kho
     *
     * @param staffCode
     * @param serial
     * @param prodOfferCode
     * @return
     * @throws Exception
     */
    public String debitStockTotal(String staffCode, String serial, String prodOfferCode) throws Exception {
        return createDebitBankPlus(staffCode, Const.OWNER_TYPE.STAFF_LONG, serial, prodOfferCode, null);
    }

    @Override
    public String debitStockTotalInShop(String shopCode, String serial, String prodOfferCode, String staffCode) throws Exception {
        if (DataUtil.isNullOrEmpty(staffCode)) {
            return getText("bankplus.service.staffCode.empty");
        }
        StaffDTO staffDTO = staffService.getStaffByStaffCode(staffCode);
        if (staffDTO == null) {
            return getText("bankplus.service.staff.empty");
        }
        return createDebitBankPlus(shopCode, Const.OWNER_TYPE.SHOP_LONG, serial, prodOfferCode, staffDTO.getStaffId());
    }

    /**
     * ham validate tao giao dich bankPlus
     *
     * @param ownerCode
     * @param ownerType
     * @param serial
     * @param prodOfferCode
     * @param actionStaffId
     * @return
     * @throws Exception
     * @author thanhnt77
     */
    private String createDebitBankPlus(String ownerCode, Long ownerType, String serial, String prodOfferCode, Long actionStaffId) throws Exception {
        try {
            Long fromOwnerId = 0L;
            Long fromOwnerType = ownerType;

            if (DataUtil.isNullOrEmpty(ownerCode) || DataUtil.isNullOrEmpty(serial) || DataUtil.isNullOrEmpty(prodOfferCode)) {
                return Const.OWNER_TYPE.SHOP_LONG.equals(ownerType) ? getText("bankplus.service.shop.input.checkserial.notEmpty")
                        : getText("bankplus.service.staff.input.checkserial.notEmpty");
            }

            if (!(Const.OWNER_TYPE.SHOP_LONG.equals(ownerType) || Const.OWNER_TYPE.STAFF_LONG.equals(ownerType))) {
                return getText("bankplus.service.ownertype.invalid");
            }
            if (Const.OWNER_TYPE.STAFF_LONG.equals(ownerType)) {
                StaffDTO staffDTO = staffService.getStaffByStaffCode(ownerCode);
                if (staffDTO == null) {
                    return getText("bankplus.service.staff.empty");
                }
                fromOwnerId = staffDTO.getStaffId();
            } else if (Const.OWNER_TYPE.SHOP_LONG.equals(ownerType)) {
                ShopDTO shopDTO = shopService.getShopByShopCode(ownerCode);
                if (shopDTO == null) {
                    return getText("bankplus.service.shop.empty");
                }
                fromOwnerId = shopDTO.getShopId();
            }
            StockKitDTO stockKitDTO = stockKitService.getStockKitBySerial(serial);
            if (stockKitDTO == null) {
                return getText("bankplus.service.stockkit.empty");
            }
            ProductOfferingDTO offeringDTO = productOfferingService.getProductByCode(prodOfferCode);
            if (offeringDTO == null) {
                return getText("bankplus.service.product.empty");
            }
            if (!DataUtil.safeEqual(Const.GOODS_STATE.NEW, stockKitDTO.getStatus())) {
                return getText("bankplus.service.serial.invalid.stock.public");
            }
            if (!DataUtil.safeEqual(Const.GOODS_STATE.NEW, stockKitDTO.getStateId())) {
                return getText("bankplus.service.stockit.not.new.product");
            }
            if (!DataUtil.safeEqual(stockKitDTO.getProdOfferId(), offeringDTO.getProductOfferingId())) {
                return getText("bankplus.service.stockkit.invalid");
            }
            if (!DataUtil.safeEqual(stockKitDTO.getOwnerId(), fromOwnerId) || !DataUtil.safeEqual(stockKitDTO.getOwnerType(), fromOwnerType)) {
                return getText("bankplus.service.serial.not.in.stock");
            }
            List<OptionSetValueDTO> lstConfigEnableBccs1 = optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1");
            if (!DataUtil.isNullOrEmpty(lstConfigEnableBccs1) && !DataUtil.isNullObject(lstConfigEnableBccs1.get(0).getValue())) {
                if (Const.ENABLE_UPDATE_BCCS1.equals(DataUtil.safeToLong(lstConfigEnableBccs1.get(0).getValue()))) {
                    String result = validStockKitProduct(serial, prodOfferCode, fromOwnerId, fromOwnerType);
                    if (!"OK".equals(result)) {
                        return result;
                    }
                }
            }
            //tao giao dich banplus
            bankPlusServiceExecute.createTransBankplus(fromOwnerId, fromOwnerType, offeringDTO.getProductOfferingId(), serial, ownerCode, actionStaffId);
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            return ex.getKeyMsg();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            return "FAIL";
        }
        return "OK";
    }


    @Override
    public String checkSerialInShop(String shopCode, String serial, String prodOfferCode) throws Exception {

        if (DataUtil.isNullOrEmpty(shopCode) || DataUtil.isNullOrEmpty(serial) || DataUtil.isNullOrEmpty(prodOfferCode)) {
            return getText("bankplus.service.shop.input.checkserial.notEmpty");
        }

        ShopDTO shopDTO = shopService.getShopByShopCode(shopCode);
        if (shopDTO == null) {
            return getText("bankplus.service.shop.empty");
        }

        StockKitDTO stockKitDTO = stockKitService.getStockKitBySerial(serial);
        if (stockKitDTO == null) {
            return getText("bankplus.service.stockkit.empty");
        }

        ProductOfferingDTO offeringDTO = productOfferingService.getProductByCode(prodOfferCode);
        if (offeringDTO == null) {
            return getText("bankplus.service.product.empty");
        }

        if (!DataUtil.safeEqual(stockKitDTO.getProdOfferId(), offeringDTO.getProductOfferingId())) {
            return getText("bankplus.service.stockkit.invalid");
        }

        if (!DataUtil.safeEqual(stockKitDTO.getOwnerId(), shopDTO.getShopId()) || !DataUtil.safeEqual(stockKitDTO.getOwnerType(), Const.OWNER_TYPE.SHOP_LONG)) {
            return getText("bankplus.service.serial.not.in.stock");
        }

        if (!DataUtil.safeEqual(1L, stockKitDTO.getStatus())) {
            return getText("bankplus.service.serial.invalid.stock.public");
        }

        if (!DataUtil.safeEqual(1L, stockKitDTO.getStateId())) {
            return getText("bankplus.service.stockit.not.new.product");
        }

        List<OptionSetValueDTO> lstConfigEnableBccs1 = optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1");
        if (!DataUtil.isNullOrEmpty(lstConfigEnableBccs1) && !DataUtil.isNullObject(lstConfigEnableBccs1.get(0).getValue())) {
            if (Const.ENABLE_UPDATE_BCCS1.equals(DataUtil.safeToLong(lstConfigEnableBccs1.get(0).getValue()))) {
                return validStockKitProduct(serial, prodOfferCode, shopDTO.getShopId(), Const.OWNER_TYPE.SHOP_LONG);
            }
        }
        return "OK";
    }

}

