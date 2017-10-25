package com.viettel.bccs.inventory.service.BaseIsdn;

import com.viettel.bccs.im1.dto.StaffIm1DTO;
import com.viettel.bccs.im1.service.StaffIm1Service;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.repo.StockTransRepo;
import com.viettel.bccs.inventory.service.*;
import com.viettel.bccs.inventory.service.BaseStock.BaseStockService;
import com.viettel.bccs.inventory.service.BaseStock.BaseValidateService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.RequiredRoleMap;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by anhnv33 on 20/01/2016.
 * Description Lap phieu xuat so
 * Detail + Insert stock_trans
 * + Insert stock_trans_detail
 * + Insert stock_trans_action
 * + Khong luu stock_trans_serial (-)
 * + Khong Cap nhat so luong dap ung stock_total (-)
 * + Khong cap nhat chi tiet serial (doSaveStockGoods) (-)
 */
@Service
public class BaseIsdnExportService extends BaseStockService {

    public static final Logger logger = Logger.getLogger(BaseIsdnExportService.class);

    @Autowired
    private BaseValidateService baseValidateService;
    @Autowired
    private ReasonService reasonService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private StockTotalService stockTotalService;
    @Autowired
    StockTransSerialService stockTransSerialService;
    @Autowired
    StaffService staffService;
    @Autowired
    StockTransService stockTransService;
    @Autowired
    ProductOfferingService productOfferingService;
    @Autowired
    private StockTransRepo stockTransRepo;
    @Autowired
    private OptionSetValueService optionSetValueService;
    @Autowired
    private StaffIm1Service staffIm1Service;

    @Override
    public void doPrepareData(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, FlagStockDTO flagStockDTO) throws Exception {
        stockTransDTO.setNote(stockTransDTO.getNote() != null ? stockTransDTO.getNote().trim() : null);
        stockTransDTO.setStockBase(stockTransDTO.getStockBase() != null ? stockTransDTO.getStockBase().trim() : null);
        //Cap nhat trang thai giao dich la lap lenh xuat
        stockTransDTO.setStatus(Const.STOCK_TRANS_STATUS.EXPORT_NOTE);
        stockTransDTO.setIsAutoGen(null);
        stockTransDTO.setTransport(null);
        stockTransActionDTO.setStatus(Const.STOCK_TRANS_STATUS.EXPORT_NOTE);

        flagStockDTO.setPrefixActionCode(Const.PREFIX_TEMPLATE.PXS);
        flagStockDTO.setFindSerial(Const.FIND_SERIAL);

//        StaffDTO staff = staffService.findOne(stockTransDTO.getStaffId());
//        Long num = 0L;
//        num = staff.getStockNum() != null ? staff.getStockNum() : num;
//        String actionCodeShop = Const.STOCK_TRANS.TRANS_CODE_PX + DataUtil.customFormat("000000", num + 1);

        stockTransDTO.setNote(null); //Lap phieu khong co ghi chu

    }


    @Override
    public void doValidate(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, List<StockTransDetailDTO> lstStockTransDetail) throws LogicException, Exception {

        //Validate cac truong trong giao dich
        doOrderValidate(stockTransDTO);

        //Validate ky voffice
        baseValidateService.doSignVofficeValidate(stockTransDTO);

        //Validate Chi tiet serial
        doValidateStockTransDetail(lstStockTransDetail);
//        doDebitIsdnValidate(stockTransDTO, lstStockTransDetail);
    }

    public void doOrderValidate(StockTransDTO stockTransDTO) throws LogicException, Exception {
        StockTransDTO stockTrans = stockTransService.findOne(stockTransDTO.getStockTransId());
        if (!DataUtil.safeEqual(stockTrans.getStatus(), Const.STOCK_TRANS_STATUS.EXPORT_ORDER)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.status.invalid");
        }
        if (DataUtil.isNullOrZero(stockTransDTO.getFromOwnerId()) || DataUtil.isNullOrZero(stockTransDTO.getFromOwnerType())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "export.order.stock.require.msg");
        }

        List<VShopStaffDTO> exportShopList = shopService.getListShopStaff(stockTransDTO.getFromOwnerId(), stockTransDTO.getFromOwnerType().toString(), Const.VT_UNIT.VT);
        if (DataUtil.isNullOrEmpty(exportShopList)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.fromStock.notExists");
        }

        //2. Validate kho
        if (DataUtil.isNullOrZero(stockTransDTO.getToOwnerId()) || DataUtil.isNullOrZero(stockTransDTO.getToOwnerType())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "store.import.stock.not.blank");
        }

        if (DataUtil.safeEqual(stockTransDTO.getFromOwnerId(), stockTransDTO.getToOwnerId())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.fromtoStock.equal");
        }
        ShopDTO shopFrom = shopService.findOne(stockTrans.getFromOwnerId());
        if (!DataUtil.safeEqual(shopFrom.getStatus(), Const.STATUS.ACTIVE)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.isdn.manage.creat.field.export.fromStop");
        }
        if (DataUtil.isNullObject(shopFrom.getChannelTypeId())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.isdn.manage.creat.field.export.fromStopInvalid");
        }
        ShopDTO shopTo = shopService.findOne(stockTrans.getToOwnerId());
        if (!DataUtil.safeEqual(shopTo.getStatus(), Const.STATUS.ACTIVE)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.isdn.manage.creat.field.export.toStop");
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
            stockTotal.setProdOfferId(stockTransDetail.getProdOfferId());
            stockTotal.setStateId(stockTransDetail.getStateId());
            List<StockTotalDTO> lstStockTotal = stockTotalService.searchStockTotal(stockTotal);
            if (DataUtil.isNullOrEmpty(lstStockTotal)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.stockTotal.empty", stockTransDetail.getProdOfferName());
            }

            if (lstStockTotal.get(0).getAvailableQuantity() < stockTransDetail.getQuantity()) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.stockTotal.notAvailable", stockTransDetail.getProdOfferName());
            }
            totalQuantity += stockTransDetail.getQuantity();
        }
        if (totalQuantity > 1000000000L) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "from.to.number.over.maxlength");
        }
    }

    public void doValidateStockTransDetail(List<StockTransDetailDTO> lstStockTransDetail) throws LogicException, Exception {
        if (DataUtil.isNullOrEmpty(lstStockTransDetail)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.isdn.manage.creat.field.export.notDetail");
        }
        for (StockTransDetailDTO stockTransDetail : lstStockTransDetail) {
            ProductOfferingDTO prodOffering = productOfferingService.findOne(stockTransDetail.getProdOfferId());
            if (prodOffering.getProductOfferTypeId() != Const.PRODUCT_OFFER_TYPE.HP
                    && prodOffering.getProductOfferTypeId() != Const.PRODUCT_OFFER_TYPE.MOBILE
                    && prodOffering.getProductOfferTypeId() != Const.PRODUCT_OFFER_TYPE.PSTN) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.isdn.manage.creat.field.export.invalidProd");
            }
            if (!DataUtil.safeEqual(prodOffering.getStatus(), Const.STATUS.ACTIVE)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.isdn.manage.creat.field.export.invalidProd");
            }
        }
        for (StockTransDetailDTO stockTransDetail : lstStockTransDetail) {
            Long currQuantity = 0L;
            if (!DataUtil.isNullOrEmpty(stockTransDetail.getLstSerial())) {
                for (StockTransSerialDTO dto : stockTransDetail.getLstSerial()) {
                    currQuantity += dto.getQuantity();
                }
            }
            Long totalQuantity = stockTransDetail.getQuantity();
            if (!currQuantity.equals(totalQuantity)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.isdn.manage.creat.field.export.notEnoughIsdn");
            }
        }
    }

    @Override
    public void doSaveStockTransSerial(StockTransDTO stockTransDTO, List<StockTransDetailDTO> lstStockTransDetail) throws Exception {
        for (StockTransDetailDTO stockTransDetail : lstStockTransDetail) {

            List<StockTransSerialDTO> lstStockTransSerial = stockTransDetail.getLstStockTransSerial();
            if (!DataUtil.isNullOrEmpty(lstStockTransSerial)) {
                for (StockTransSerialDTO stockTransSerial : lstStockTransSerial) {
                    stockTransSerial.setStockTransSerialId(null); // luon insert
                    stockTransSerial.setStockTransDetailId(stockTransDetail.getStockTransDetailId());
                    stockTransSerial.setCreateDatetime(stockTransDTO.getCreateDatetime());
                    stockTransSerial.setProdOfferId(stockTransDetail.getProdOfferId());
                    stockTransSerial.setStateId(stockTransDetail.getStateId());
                    stockTransSerial.setStockTransId(stockTransDTO.getStockTransId());
                    stockTransSerialService.save(stockTransSerial);
                }
            }
        }
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
        List<OptionSetValueDTO> lstConfigEnableBccs1 = optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1");
        if (!DataUtil.isNullOrEmpty(lstConfigEnableBccs1)
                && !DataUtil.isNullObject(lstConfigEnableBccs1.get(0).getValue())
                && Const.ENABLE_UPDATE_BCCS1.equals(DataUtil.safeToLong(lstConfigEnableBccs1.get(0).getValue()))) {
            StaffIm1DTO staff = staffIm1Service.findOne(stockTransActionDTO.getActionStaffId());
            if (staff != null) {
                // Bo sung action_code_shop theo nhan vien neu ko co quyen ROLE_STOCK_NUM_SHOP 09/03/2016
                if (Const.STOCK_TRANS.TRANS_CODE_PX.equals(prefixActionCode)
                        || Const.PREFIX_TEMPLATE.PXS.equals(prefixActionCode)) {
                    staff.setStockNum(DataUtil.safeToLong(staff.getStockNum()) + 1);
                } else if (Const.STOCK_TRANS.TRANS_CODE_PN.equals(prefixActionCode)
                        || Const.STOCK_TRANS.TRANS_CODE_LN.equals(prefixActionCode)) {
                    staff.setStockNumImp(DataUtil.safeToLong(staff.getStockNumImp()) + 1);
                }
                staffIm1Service.save(staff);
            }
        } else {
            StaffDTO staff = staffService.findOne(stockTransActionDTO.getActionStaffId());
            if (staff != null) {
                if (Const.STOCK_TRANS.TRANS_CODE_PX.equals(prefixActionCode)
                        || Const.PREFIX_TEMPLATE.PXS.equals(prefixActionCode)) {
                    staff.setStockNum(DataUtil.safeToLong(staff.getStockNum()) + 1);
                } else if (Const.STOCK_TRANS.TRANS_CODE_PN.equals(prefixActionCode)
                        || Const.STOCK_TRANS.TRANS_CODE_LN.equals(prefixActionCode)) {
                    staff.setStockNumImp(DataUtil.safeToLong(staff.getStockNumImp()) + 1);
                }
                staffService.save(staff);
            }
        }
    }

    @Override
    public void doSaveStockTransDetail(StockTransDTO stockTransDTO, List<StockTransDetailDTO> lstStockTransDetail) throws Exception{

    }

    @Override
    public void doUnlockUser(StockTransDTO stockTransDTO) throws Exception {
        stockTransRepo.unlockUserInfo(stockTransDTO.getStockTransActionId());
    }
}