package com.viettel.bccs.inventory.service.BaseStock;

import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.*;
import com.viettel.bccs.partner.dto.AccountBalanceDTO;
import com.viettel.bccs.partner.service.AccountBalanceService;
import com.viettel.bccs.sale.dto.SaleTransDTO;
import com.viettel.bccs.sale.repo.SaleTransRepo;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.service.BaseServiceImpl;
import com.viettel.ws.common.utils.*;
import com.viettel.ws.provider.WsCallerFactory;
import com.viettel.ws.provider.WsDtoContainer;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by thetm1 on 09/12/2015.
 */

@Service
public class BaseValidateService extends BaseServiceImpl {

    public static final Logger logger = Logger.getLogger(BaseValidateService.class);

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @Autowired
    private ShopService shopService;
    @Autowired
    private StaffService staffService;
    @Autowired
    private ReasonService reasonService;
    @Autowired
    private SignFlowService signFlowService;
    @Autowired
    private StockDebitService stockDebitService;
    @Autowired
    private StockTransService stockTransService;
    @Autowired
    private StockTotalService stockTotalService;
    @Autowired
    private ProductOfferPriceService productOfferPriceService;
    @Autowired
    private ProductOfferingService productOfferingService;
    @Autowired
    private WsCallerFactory wsCallerFactory;
    @Autowired
    private StockTransSerialService stockTransSerialService;
    @Autowired
    private AccountBalanceService accountBalanceService;
    @Autowired
    private SaleTransRepo saleTransRepo;
    @Autowired
    private SaleWs saleWs;

    public void doOrderValidate(StockTransDTO stockTransDTO) throws LogicException, Exception {
        doOrderValidateCommon(stockTransDTO, Const.VT_UNIT.VT);
    }

    /**
     * DungPT
     *
     * @param stockTransDTO
     * @param stockTransActionDTO
     * @throws LogicException
     * @throws Exception
     */
    public void doOrderValidateAgent(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO) throws LogicException, Exception {
        doOrderValidateCommon(stockTransDTO, Const.VT_UNIT.AGENT);
        // Validate length theo utf-8
        if (!DataUtil.isNullOrEmpty(stockTransActionDTO.getActionCode()) && stockTransActionDTO.getActionCode().getBytes("UTF-8").length > 50) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.actionCode.overLength");
        }
    }

    public void doOrderValidateCommon(StockTransDTO stockTransDTO, String isVtUnit) throws LogicException, Exception {

        //0. Validate

        //1. Validate kho xuat
        if (DataUtil.isNullOrZero(stockTransDTO.getFromOwnerId()) || DataUtil.isNullOrZero(stockTransDTO.getFromOwnerType())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "export.order.stock.require.msg");
        }

        List<VShopStaffDTO> exportShopList = shopService.getListShopStaff(stockTransDTO.getFromOwnerId(), stockTransDTO.getFromOwnerType().toString(), Const.VT_UNIT.VT);
        if (DataUtil.isNullOrEmpty(exportShopList)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.fromStock.notExists");
        }

        //2. Validate kho nhan
        if (DataUtil.isNullOrZero(stockTransDTO.getToOwnerId()) || DataUtil.isNullOrZero(stockTransDTO.getToOwnerType())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "store.import.stock.not.blank");
        }

        List<VShopStaffDTO> importShopList = shopService.getListShopStaff(stockTransDTO.getToOwnerId(), stockTransDTO.getToOwnerType().toString(), isVtUnit);
        if (DataUtil.isNullOrEmpty(importShopList)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.toStock.notExists");
        }

        //3. Kho xuat la cap tren lien ke kho nhan
        if ((Const.FROM_STOCK.FROM_SENIOR.equals(stockTransDTO.getFromStock()) || DataUtil.isNullOrEmpty(stockTransDTO.getFromStock())) && !DataUtil.safeEqual(exportShopList.get(0).getOwnerId(), importShopList.get(0).getParentShopId())) {
            if (DataUtil.safeEqual(Const.OWNER_TYPE.STAFF, importShopList.get(0).getOwnerType())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.exportShop.invalid");
            } else {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.stock.notConsistent");
            }
        } else if (Const.FROM_STOCK.FROM_UNDERLYING.equals(stockTransDTO.getFromStock()) && !DataUtil.safeEqual(exportShopList.get(0).getParentShopId(), importShopList.get(0).getOwnerId())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.stockUnder.notConsistent");
        } else if (Const.FROM_STOCK.FROM_STAFF.equals(stockTransDTO.getFromStock()) && !DataUtil.safeEqual(exportShopList.get(0).getOwnerId(), importShopList.get(0).getStaffOwnerId())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.stock.notStaffOwner");
        } else if (Const.FROM_STOCK.STAFF_SHOP.equals(stockTransDTO.getFromStock()) && !DataUtil.safeEqual(exportShopList.get(0).getParentShopId(), importShopList.get(0).getOwnerId())) {
            //check cho truong hop nhan vien xuat tra cua hang (NV->CH)
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "staff.import.stock.valid.toOwner");
        }

        //4. kiem tra ly do co ton tai khong
        if (DataUtil.isNullOrOneNavigate(stockTransDTO.getReasonId())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "create.note.reason.export.require.msg");
        }

        ReasonDTO reason = reasonService.findOne(stockTransDTO.getReasonId());
        if (DataUtil.isNullObject(reason) || DataUtil.isNullOrZero(reason.getReasonId()) || !DataUtil.safeEqual(Const.STATUS_ACTIVE, reason.getReasonStatus())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.reason.notExists");
        }

        //5. kiem tra ghi chu co thoa man khong
        if (!DataUtil.isNullOrEmpty(stockTransDTO.getNote()) && stockTransDTO.getNote().getBytes("UTF-8").length > 500) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.note.overLength");
        }

        //6. kiem tra can cu xuat kho co thoa man khong
        if (!DataUtil.isNullOrEmpty(stockTransDTO.getStockBase()) && stockTransDTO.getStockBase().getBytes("UTF-8").length > 350) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.stockBase.overLength");
        }
    }

    public void doSignVofficeValidate(StockTransDTO stockTransDTO) throws LogicException, Exception {

        if (!stockTransDTO.isSignVoffice()) {
            return;
        }

        //1. Validate username va password bat buoc nhap
        if (DataUtil.isNullOrEmpty(stockTransDTO.getUserName())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "common.user.require.msg");
        }

        if (DataUtil.isNullOrEmpty(stockTransDTO.getPassWord())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "common.password.require.msg");
        }

        //2. Validate username password ton tai tren voffice

        //3. kiem tra luong ky co ton tai ko
        if (DataUtil.isNullOrZero(stockTransDTO.getSignFlowId())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "export.order.airflow.require.msg");
        }

        if (!DataUtil.isNullOrEmpty(stockTransDTO.getUserName()) && stockTransDTO.getUserName().getBytes("UTF-8").length > 100) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTransVoffice.validate.user.overLength");
        }

        SignFlowDTO signFlow = signFlowService.findOne(stockTransDTO.getSignFlowId());
        if (DataUtil.isNullObject(signFlow) || DataUtil.isNullOrZero(signFlow.getSignFlowId())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.signFlow.notExists");
        }

    }

    public void doDebitValidate(StockTransDTO stockTransDTO, List<StockTransDetailDTO> lstStockTransDetail) throws LogicException, Exception {
        if (DataUtil.isNullObject(lstStockTransDetail)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.lstStockTransDetail.empty");
        }


        Long debitTrans = 0L;
        StockTotalDTO stockTotal = new StockTotalDTO();
        Long fromOwnerId = stockTransDTO.getFromOwnerId();
        Long fromOwnerType = stockTransDTO.getFromOwnerType();

        //neu xuat kho 3 mien --> check han muc kho 3 mien
        if (stockTransDTO.getRegionStockId() != null && Const.STOCK_TRANS_STATUS.EXPORT_ORDER.equals(stockTransDTO.getStatus())) {
            fromOwnerId = stockTransDTO.getRegionStockId();
        }
        stockTotal.setOwnerId(fromOwnerId);
        stockTotal.setOwnerType(fromOwnerType);

        Long pricePolicyId = null;
        if (Const.OWNER_TYPE.SHOP.equals(DataUtil.safeToString(stockTransDTO.getToOwnerType()))) {
            ShopDTO shop = shopService.findOne(stockTransDTO.getToOwnerId());
            pricePolicyId = DataUtil.safeToLong(shop.getPricePolicy());
        } else if (Const.OWNER_TYPE.STAFF.equals(DataUtil.safeToString(stockTransDTO.getToOwnerType()))) {
            StaffDTO staff = staffService.findOne(stockTransDTO.getToOwnerId());
            pricePolicyId = DataUtil.safeToLong(staff.getPricePolicy());
        }


        boolean isDeposit = false;
        Long debitTransForDeposit = 0L;
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

            if (!DataUtil.safeEqual(Const.GOODS_STATE.NEW, stockTransDetail.getStateId())) {
                continue;
            }

            //validate han muc kho nhan
            Long rootPrice = productOfferPriceService.getPriceAmount(stockTransDetail.getProdOfferId(), Const.PRODUCT_OFFERING.PRICE_TYPE_ROOT, pricePolicyId);
            debitTrans += stockTransDetail.getQuantity() * rootPrice;

            if (DataUtil.safeEqual(stockTransDTO.getDepositStatus(), Const.STOCK_STRANS_DEPOSIT.DEPOSIT_NOTE)
                    && !DataUtil.isNullOrZero(stockTransDetail.getProductOfferPriceId())) {
                isDeposit = true;
                debitTransForDeposit += DataUtil.safeToLong(stockTransDetail.getQuantity()) * DataUtil.safeToLong(stockTransDetail.getPrice());
                continue;
            } else {
                debitTransForDeposit += DataUtil.safeToLong(stockTransDetail.getQuantity()) * DataUtil.safeToLong(stockTransDetail.getDepositPrice());
            }

            stockTransDetail.setPrice(rootPrice);
            stockTransDetail.setAmount(stockTransDetail.getQuantity() * rootPrice);
        }
        if (isDeposit) {
            stockTransDTO.setTotalAmount(debitTransForDeposit);
        } else {
            stockTransDTO.setTotalAmount(debitTrans);
            stockTransDTO.setDepositPrice(debitTransForDeposit);
        }
        //Neu tra hang kho VT thi khong check han muc
        if (DataUtil.safeEqual(stockTransDTO.getToOwnerId().toString(), Const.VT_SHOP_ID)) {
            return;
        }
        // Neu la xuat kho cho Dai ly, CTV thi ko checn han muc
        if (DataUtil.safeEqual(stockTransDTO.getStockAgent(), Const.STOCK_TRANS.STOCK_TRANS_DEPOSIT)
                || DataUtil.safeEqual(stockTransDTO.getStockAgent(), Const.STOCK_TRANS.STOCK_TRANS_PAY)
                || !DataUtil.isNullOrEmpty(stockTransDTO.getPayStatus())) {
            return;
        }

        //Lay han muc hien tai kho nhan
        StockDebitDTO stockDebit = stockDebitService.findStockDebitValue(stockTransDTO.getToOwnerId(), stockTransDTO.getToOwnerType().toString());
        Long totalDebit = stockDebit == null ? 0L : stockDebit.getDebitValue();

        //Lay cac giao dich treo
        Long suspendDebit = stockTransService.getTotalValueStockSusbpendByOwnerId(stockTransDTO.getToOwnerId(), DataUtil.safeToString(stockTransDTO.getToOwnerType()));

        //lay gia tri hien tai trong kho
        Long currentDebit = stockTotalService.getTotalValueStock(stockTransDTO.getToOwnerId(), DataUtil.safeToString(stockTransDTO.getToOwnerType()));

        if (!DataUtil.isNullObject(stockTransDTO.getStockTransId())) {
            debitTrans = 0L;
        }

        if (totalDebit < debitTrans + currentDebit + suspendDebit) {
            if (DataUtil.safeEqual(stockTransDTO.getDepositStatus(), Const.STOCK_TRANS.STOCK_TRANS_DEPOSIT)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.debit.agent");
            } else {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.debit.over");
            }
        }
    }

    public void doQuantityAvailableValidate(StockTransDTO stockTransDTO, List<StockTransDetailDTO> lstStockTransDetail) throws LogicException, Exception {
        StockTotalDTO stockTotal = new StockTotalDTO();
        Long fromOwnerId = stockTransDTO.getFromOwnerId();
        Long fromOwnerType = stockTransDTO.getFromOwnerType();

        //neu xuat kho 3 mien --> check han muc kho 3 mien
        if (stockTransDTO.getRegionStockId() != null && Const.STOCK_TRANS_STATUS.EXPORT_ORDER.equals(stockTransDTO.getStatus())) {
            fromOwnerId = stockTransDTO.getRegionStockId();
        }
        stockTotal.setOwnerId(fromOwnerId);
        stockTotal.setOwnerType(fromOwnerType);

        for (StockTransDetailDTO stockTransDetail : lstStockTransDetail) {
            //Validate so luong trong kho co du so luong dap ung xuat kho
            stockTotal.setProdOfferId(stockTransDetail.getProdOfferId());
            stockTotal.setStateId(stockTransDetail.getStateId());
            List<StockTotalDTO> lstStockTotal = stockTotalService.searchStockTotal(stockTotal);
            //Neu khong phai Tu choi thi thuc hien validate nay
            if (!Const.STOCK_TRANS_STATUS.REJECT.equals(stockTransDTO.getStatus())) {
                ProductOfferingDTO offeringDTO = productOfferingService.findOne(stockTransDetail.getProdOfferId());
                String prodOfferName = offeringDTO != null && !DataUtil.isNullOrEmpty(offeringDTO.getName()) ? offeringDTO.getName() : "";
                if (DataUtil.isNullOrEmpty(lstStockTotal)) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.stockTotal.empty", prodOfferName);
                }
                if (lstStockTotal.get(0).getAvailableQuantity() < stockTransDetail.getQuantity()) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.stockTotal.notAvailable", prodOfferName);
                }
            }
        }
    }

    public void doCurrentQuantityValidate(StockTransDTO stockTransDTO, List<StockTransDetailDTO> lstStockTransDetail) throws LogicException, Exception {
        StockTotalDTO stockTotal = new StockTotalDTO();
        Long fromOwnerId = stockTransDTO.getFromOwnerId();
        Long fromOwnerType = stockTransDTO.getFromOwnerType();

        //neu xuat kho 3 mien --> check han muc kho 3 mien
        if (stockTransDTO.getRegionStockId() != null && (Const.STOCK_TRANS_STATUS.EXPORTED.equals(stockTransDTO.getStatus()) ||
                Const.STOCK_TRANS_STATUS.EXPORTED.equals(stockTransDTO.getStockAgent()))) {
            fromOwnerId = stockTransDTO.getRegionStockId();
        }
        stockTotal.setOwnerId(fromOwnerId);
        stockTotal.setOwnerType(fromOwnerType);

        for (StockTransDetailDTO stockTransDetail : lstStockTransDetail) {
            //Validate so luong trong kho co du so luong hien tai khong
            stockTotal.setProdOfferId(stockTransDetail.getProdOfferId());
            stockTotal.setStateId(stockTransDetail.getStateId());
            List<StockTotalDTO> lstStockTotal = stockTotalService.searchStockTotal(stockTotal);
            //Neu khong phai Tu choi thi thuc hien validate nay
            if (!Const.STOCK_TRANS_STATUS.REJECT.equals(stockTransDTO.getStatus())) {
                ProductOfferingDTO offeringDTO = productOfferingService.findOne(stockTransDetail.getProdOfferId());
                String prodOfferName = offeringDTO != null && !DataUtil.isNullOrEmpty(offeringDTO.getName()) ? offeringDTO.getName() : "";
                if (DataUtil.isNullOrEmpty(lstStockTotal)) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.stockTotal.empty", prodOfferName);
                }
                if (lstStockTotal.get(0).getCurrentQuantity() < stockTransDetail.getQuantity()) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "validate.stockTotal.current.smaller.input.quantity", prodOfferName);
                }
            }
        }
    }

    //Neu giao dich xuat kho, phai check thong tin serial voi cac mat hang phai nhap serial
    public void doInputSerialValidate(StockTransDTO stockTransDTO, List<StockTransDetailDTO> lstStockTransDetail) throws LogicException, Exception {
        if (DataUtil.isNullObject(stockTransDTO) || DataUtil.isNullOrEmpty(lstStockTransDetail)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "export.order.stock.require.msg");
        }
        if (DataUtil.safeEqual(Const.STOCK_TRANS_STATUS.EXPORTED, stockTransDTO.getStatus())
//                || DataUtil.safeEqual(Const.STOCK_STRANS_DEPOSIT.DEPOSIT_IMPORT, stockTransDTO.getDepositStatus())
                || DataUtil.safeEqual(Const.STOCK_TRANS_STATUS.IMPORTED, stockTransDTO.getStockAgent())
                || DataUtil.safeEqual(Const.STOCK_TRANS_STATUS.EXPORTED, stockTransDTO.getStockAgent())) {
            for (StockTransDetailDTO stockTransDetail : lstStockTransDetail) {
                ProductOfferingDTO productOffering = productOfferingService.findOne(stockTransDetail.getProdOfferId());
                if (DataUtil.safeEqual(productOffering.getCheckSerial(), Const.PRODUCT_OFFERING.CHECK_SERIAL)) {
                    Long quantitySerial = 0L;
                    Long quantitySerialDB = 0L;
                    if (!DataUtil.isNullOrEmpty(stockTransDetail.getLstStockTransSerial())) {
                        for (StockTransSerialDTO stockTransSerial : stockTransDetail.getLstStockTransSerial()) {
                            //Tu serial, den serial bat buoc phai nhap
                            if (DataUtil.isNullObject(stockTransSerial.getFromSerial()) || DataUtil.isNullObject(stockTransSerial.getToSerial())) {
                                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "export.stock.serial.not.imported");
                            } else {
                                //Neu mat hang la HANDSET : to_serial = from_serial, quantity +1
                                if (DataUtil.safeEqual(Const.PRODUCT_OFFER_TYPE.PHONE, stockTransDetail.getProdOfferTypeId())) {
                                    if (DataUtil.safeEqual(stockTransSerial.getFromSerial(), stockTransSerial.getToSerial())) {
                                        ++quantitySerial;
                                    } else {
                                        throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.fromSerial.toSerial.equal");
                                    }
                                } else {
                                    //Check xem serial nhap vao co phai dang so khong
                                    if (!DataUtil.isNumber(stockTransSerial.getFromSerial()) || !DataUtil.isNumber(stockTransSerial.getToSerial())) {
                                        throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.serial.valid");
                                    }
                                    //fromSerial khong duoc lon hon toSerial
                                    if (DataUtil.safeToLong(stockTransSerial.getFromSerial()) > DataUtil.safeToLong(stockTransSerial.getToSerial())) {
                                        throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.serial.valid");
                                    } else {
                                        quantitySerial += DataUtil.safeToLong(stockTransSerial.getToSerial()) - DataUtil.safeToLong(stockTransSerial.getFromSerial()) + 1;
                                    }
                                }
                            }
                            // Check trang thai + so luong thuc te.
                            Long fromOwnerid = stockTransDTO.getFromOwnerId();
                            if (!DataUtil.isNullOrZero(stockTransDTO.getRegionStockId())) {
                                fromOwnerid = stockTransDTO.getRegionStockId();
                            }
                            List<String> lsSerialValid = stockTransSerialService.getListSerialValid(DataUtil.safeToLong(stockTransDTO.getFromOwnerType()),
                                    fromOwnerid, stockTransDetail.getTableName(), productOffering.getProductOfferingId(),
                                    stockTransDetail.getStateId(),
                                    stockTransSerial.getFromSerial(), stockTransSerial.getToSerial(), stockTransDetail.getQuantity(), null);
                            //bao loi neu ko co bat ky serial nao hop le
                            if (DataUtil.isNullOrEmpty(lsSerialValid)) {
                                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "mn.stock.partner.serial.valid.notExist", stockTransSerial.getFromSerial(), stockTransSerial.getToSerial());
                            }
                            quantitySerialDB += stockTransSerial.getQuantity();
                        }
                        if (!DataUtil.safeEqual(stockTransDetail.getQuantity(), quantitySerial)) {
                            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.serial.notEnough");
                        }
                        if (!DataUtil.safeEqual(stockTransDetail.getQuantity(), quantitySerialDB)) {
                            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.serial.notEnough");
                        }
                    } else {
                        throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.serial.empty");
                    }
                }
            }
        }
    }

    public void findTransExpiredPay(Long staffId) throws LogicException, Exception {
//        saleWs.findTransExpiredPayWS(staffId);  TheTM: bo check han muc cong no nhan vien
    }

    public boolean doAccountBankplusValidate(StockTransDTO stockTransDTO) throws LogicException, Exception {
        boolean result = false;
        Long ownerType = stockTransDTO.getToOwnerType();
        Long ownerId = stockTransDTO.getToOwnerId();
        //
        Long totalAmount = stockTransDTO.getTotalAmount();
        if (!DataUtil.isNullObject(stockTransDTO.getDepositPrice())) {
            totalAmount = stockTransDTO.getDepositPrice();
        }
        AccountBalanceDTO accountBalanceDTO = accountBalanceService.getAccountBalanceBankplus(ownerType, ownerId);
        if ((!DataUtil.isNullObject(accountBalanceDTO) && accountBalanceDTO.getRealBalance() >= totalAmount)) {
            stockTransDTO.setBankplusStatus(Const.STOCK_TRANS.BANKPLUS_STATUS_PENDING_1);
            stockTransDTO.setAccountBalanceId(accountBalanceDTO.getBalanceId());
            result = true;
        }
        return result;
    }

    public boolean doAccountBankplusRetrieveValidate(StockTransDTO stockTransDTO) throws LogicException, Exception {
        boolean result = false;
        Long ownerType = stockTransDTO.getFromOwnerType();
        Long ownerId = stockTransDTO.getFromOwnerId();

        AccountBalanceDTO accountBalanceDTO = accountBalanceService.getAccountBalanceBankplus(ownerType, ownerId);
        if (!DataUtil.isNullObject(accountBalanceDTO) && stockTransDTO.getDepositTotal() <= 0) {
            stockTransDTO.setBankplusStatus(Const.STOCK_TRANS.BANKPLUS_STATUS_PENDING_1);
            stockTransDTO.setAccountBalanceId(accountBalanceDTO.getBalanceId());
            result = true;
        }
        return result;
    }
}
