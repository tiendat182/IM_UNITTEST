package com.viettel.bccs.inventory.service.BaseStock;

import com.viettel.anypay.database.BO.AnypayMsg;
import com.viettel.bccs.anypay.dto.AgentDTO;
import com.viettel.bccs.anypay.service.AnypayTransService;
import com.viettel.bccs.im1.dto.ReqActivateKitDTO;
import com.viettel.bccs.im1.dto.StockTransIm1DTO;
import com.viettel.bccs.im1.dto.VcRequestDTO;
import com.viettel.bccs.im1.service.ReqActivateKitService;
import com.viettel.bccs.im1.service.StockTransIm1Service;
import com.viettel.bccs.im1.service.VcRequestService;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.*;
import com.viettel.bccs.partner.dto.AccountAgentDTO;
import com.viettel.bccs.partner.dto.StockOwnerTmpDTO;
import com.viettel.bccs.partner.service.AccountAgentService;
import com.viettel.bccs.partner.service.StockOwnerTmpService;
import com.viettel.bccs.sale.dto.SaleTransDTO;
import com.viettel.bccs.sale.dto.SaleTransDetailDTO;
import com.viettel.bccs.sale.dto.SaleTransSerialDTO;
import com.viettel.bccs.sale.service.SaleTransDetailService;
import com.viettel.bccs.sale.service.SaleTransSerialService;
import com.viettel.bccs.sale.service.SaleTransService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

/**
 * Created by HungPM6 on 1/18/2016.
 */
@Service
public class BaseStockExpNoteAgentService extends BaseStockService {
    private final Logger anypayLog = Logger.getLogger("AnypayLog");
    @Autowired
    private BaseValidateService baseValidateService;
    @Autowired
    private StockTransVofficeService stockTransVofficeService;
    @Autowired
    private StockTransActionService stockTransActionService;
    @Autowired
    private StockCardService stockCardService;
    @Autowired
    private ProductOfferingService productOfferingService;
    @Autowired
    private StockTransService stockTransService;
    @Autowired
    private OptionSetValueService optionSetValueService;
    @Autowired
    private MtService mtService;
    @Autowired
    private SaleTransService saleTransService;
    @Autowired
    private SaleTransDetailService saleTransDetailService;
    @Autowired
    private SaleTransSerialService saleTransSerialService;
    @Autowired
    private AccountAgentService accountAgentService;
    @Autowired
    private StockOwnerTmpService stockOwnerTmpService;
    @Autowired
    private AnypayTransService anypayTransService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private VcRequestService vcRequestService;
    @Autowired
    private ReqActivateKitService reqActivateKitService;
    @Autowired
    private ProductOfferPriceService productOfferPriceService;
    @Autowired
    private StockTransIm1Service stockTransServiceIm1;
    // DB sale
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_SALE)
    private EntityManager emSale;
    // DB IM1
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM_LIB)
    private EntityManager emIm1;

    public void doPrepareData(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, FlagStockDTO flagStockDTO) throws LogicException, Exception {
        stockTransDTO.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
        stockTransActionDTO.setStatus(Const.STOCK_TRANS_STATUS.EXPORTED);
        stockTransActionDTO.setActionCode(null);

        //Tru so luong thuc te kho xuat
        flagStockDTO.setObjectType(Const.STOCK_TRANS.OBJECT_TYPE_AGENT_EXPORT);
        flagStockDTO.setExportStock(true);
        flagStockDTO.setExpCurrentQuantity(Const.STOCK_TOTAL.MINUS_QUANTITY);
//        flagStockDTO.setExpHangQuantity(Const.STOCK_TOTAL.MINUS_QUANTITY);

        //Cong so luong thuc te kho nhap
        flagStockDTO.setImportStock(true);
        flagStockDTO.setImpCurrentQuantity(Const.STOCK_TOTAL.PLUS_QUANTITY);
        flagStockDTO.setImpAvailableQuantity(Const.STOCK_TOTAL.PLUS_QUANTITY);
        // Trang thai xuat kho hay nhap kho
        flagStockDTO.setStatusForAgent(Const.STOCK_TRANS_STATUS.EXPORTED);
        flagStockDTO.setInsertStockTotalAudit(true);
        stockTransDTO.setStockAgent(Const.STOCK_TRANS_STATUS.EXPORTED);

        //cap nhat trang thai stock_x
        flagStockDTO.setOldStatus(Const.STOCK_GOODS.STATUS_NEW);
        flagStockDTO.setNewStatus(Const.STOCK_GOODS.STATUS_NEW);

        //Kho 3 mien
        flagStockDTO.setRegionExportStatus(Const.STOCK_TRANS_STATUS.EXPORTED);
        flagStockDTO.setRegionImportStatus(Const.STOCK_TRANS_STATUS.IMPORTED);

        //Cap nhat serial ve kho nhan
        flagStockDTO.setUpdateOwnerId(true);
        // bankplus
        flagStockDTO.setUpdateAccountBalance(true);
        flagStockDTO.setRequestType(Const.STOCK_STRANS_DEPOSIT.DEPOSIT_TRANS_TYPE_MINUS_EXPORT);
        stockTransDTO.setBankplusStatus(Const.STOCK_TRANS.BANKPLUS_STATUS_NOT_APPROVE);
        //cap nhat sale_date
        flagStockDTO.setUpdateSaleDate(true);
        //set deposit_price
        flagStockDTO.setUpdateDepositPrice(true);

    }

    //hoangnt : check thuc hien xuat kho tren 2 he thong IM1 va IM2
    @Override
    public StockTransDTO doSaveStockTrans(StockTransDTO stockTransDTO) throws Exception {
        StockTransDTO transDTOImport = DataUtil.cloneBean(stockTransDTO);
        //Truong hop update
        if (transDTOImport.getStockTransId() != null) {
            StockTransDTO stockTransToUpdate = stockTransService.findOneLock(stockTransDTO.getStockTransId());

            if (DataUtil.isNullObject(stockTransToUpdate)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.stockTrans.notExists");
            }

            //Neu trang thai stockTrans da duoc cap nhat thi thong bao loi
            if (DataUtil.safeEqual(stockTransDTO.getStatus(), stockTransToUpdate.getStatus())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.stockTrans.updated");
            }
            stockTransToUpdate.setStatus(stockTransDTO.getStatus());
            List<OptionSetValueDTO> lstConfigEnableBccs1 = optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1");
            if (!DataUtil.isNullOrEmpty(lstConfigEnableBccs1) && !DataUtil.isNullObject(lstConfigEnableBccs1.get(0).getValue())) {
                if (Const.ENABLE_UPDATE_BCCS1.equals(DataUtil.safeToLong(lstConfigEnableBccs1.get(0).getValue()))) {
                    if (DataUtil.safeEqual(stockTransDTO.getStatus(), Const.STOCK_TRANS_STATUS.IMPORTED)) {
                        StockTransIm1DTO stockTransToUpdateIM1 = stockTransServiceIm1.findOneLock(stockTransDTO.getStockTransId());
                        if (!DataUtil.isNullObject(stockTransToUpdateIM1)) {
                            //check da xuat kho tren im1 chua
                            if (!DataUtil.safeEqual(stockTransToUpdateIM1.getStockTransStatus(), DataUtil.safeToLong(Const.STOCK_TRANS_STATUS.EXPORT_ORDER))
                                    && !DataUtil.safeEqual(stockTransToUpdateIM1.getStockTransStatus(), DataUtil.safeToLong(Const.STOCK_TRANS_STATUS.EXPORT_NOTE))) {
                                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.stockTrans.duplicate.export.im1");
                            }
                            stockTransToUpdateIM1.setStockTransStatus(DataUtil.safeToLong(Const.STOCK_TRANS_STATUS.IMPORT_ORDER));
                            stockTransServiceIm1.updateStatusStockTrans(stockTransToUpdateIM1);
                        }
                    }
                }
            }
            //Neu process offline: status = 0
            if (DataUtil.safeEqual(Const.PROCESS_OFFLINE, stockTransDTO.getProcessOffline())) {
                stockTransToUpdate.setStatus(Const.STOCK_TRANS_STATUS.PROCESSING);
            }
            if (!DataUtil.isNullObject(stockTransDTO.getDepositStatus())) {
                stockTransToUpdate.setDepositStatus(stockTransDTO.getDepositStatus());
            }
            if (!DataUtil.isNullObject(stockTransDTO.getPayStatus())) {
                stockTransToUpdate.setPayStatus(stockTransDTO.getPayStatus());
            }
            if (!DataUtil.isNullObject(stockTransDTO.getBankplusStatus())) {
                stockTransToUpdate.setBankplusStatus(stockTransDTO.getBankplusStatus());
            }
            if (!DataUtil.isNullObject(stockTransDTO.getCreateUserIpAdress())) {
                stockTransToUpdate.setCreateUserIpAdress(stockTransDTO.getCreateUserIpAdress());
            }
            if (!DataUtil.isNullObject(stockTransDTO.getDepositPrice())) {
                stockTransToUpdate.setDepositPrice(stockTransDTO.getDepositPrice());
            }
            stockTransToUpdate.setImportReasonId(stockTransDTO.getImportReasonId());
            stockTransToUpdate.setImportNote(stockTransDTO.getImportNote());
            stockTransToUpdate.setRejectReasonId(stockTransDTO.getRejectReasonId());
            stockTransToUpdate.setRejectNote(stockTransDTO.getRejectNote());
            stockTransService.save(stockTransToUpdate);
            transDTOImport.setCreateDatetime(stockTransDTO.getCreateDatetime());//Lay lai de ghi log stock_trans_action
            transDTOImport.setStockTransDate(stockTransToUpdate.getCreateDatetime());
            return transDTOImport;
        }
        //Truong hop insert
        if (DataUtil.isNullObject(stockTransDTO.getStockTransType())) {
            stockTransDTO.setStockTransType(Const.STOCK_TRANS_TYPE.UNIT);
        }
        StockTransDTO stockTrans = stockTransService.save(stockTransDTO);
        transDTOImport.setStockTransId(stockTrans.getStockTransId());
        return transDTOImport;
    }

    @Override
    public void doValidate(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, List<StockTransDetailDTO> lstStockTransDetail) throws LogicException, Exception {
        baseValidateService.doSignVofficeValidate(stockTransDTO);
        exportValidate(stockTransDTO, lstStockTransDetail);
        //Validate han muc kho nhan
        if (DataUtil.isNullOrEmpty(stockTransDTO.getPayStatus())) {
            boolean isAccountBankplus = baseValidateService.doAccountBankplusValidate(stockTransDTO);
            if (!isAccountBankplus) {
                baseValidateService.doDebitValidate(stockTransDTO, lstStockTransDetail);
            }
        }
        // Validate kho xuat con so luong xuat khong.
        baseValidateService.doCurrentQuantityValidate(stockTransDTO, lstStockTransDetail);
    }

    public void exportValidate(StockTransDTO stockTransDTO, List<StockTransDetailDTO> lstStockTransDetail) throws LogicException, Exception {

        StockTransDTO transDTO = stockTransService.findOne(stockTransDTO.getStockTransId());

        if (DataUtil.isNullObject(transDTO)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "export.stock.trans.not.found");
        }

        if (transDTO.getStatus() == null || !DataUtil.safeEqual(Const.STOCK_TRANS_STATUS.EXPORT_NOTE, transDTO.getStatus())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "export.stock.code.export.not.found");
        }
        // Khong cho phep xuat nhap offline voi kho 3 mien.
        if (DataUtil.safeEqual(Const.PROCESS_OFFLINE, stockTransDTO.getProcessOffline())) {
            if (!DataUtil.isNullOrZero(stockTransDTO.getRegionStockTransId())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "offline.region.error");
            }
        }
        //2. Check trang thai dat coc
        if (!DataUtil.isNullOrEmpty(transDTO.getDepositStatus()) && !DataUtil.safeEqual(Const.DEPOSIT_STATUS.DEPOSIT_HAVE, transDTO.getDepositStatus())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "export.stock.status.not.deposit");
        }

        //3. Check trang thai thanh toan, lap hoa don cua giao dic
        if (!DataUtil.isNullOrEmpty(transDTO.getPayStatus()) && !DataUtil.safeEqual(Const.PAY_STATUS.PAY_HAVE, transDTO.getPayStatus())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "export.stock.status.not.pay");
        }
        //TODO - goi webservice sale check trang thai thanh toan, lap hoa don

        //4.Kiem tra stockTransAction
        StockTransActionDTO oldStockTransActionDTO = stockTransActionService.findOne(stockTransDTO.getStockTransActionId());
        if (DataUtil.isNullObject(oldStockTransActionDTO) || !DataUtil.safeEqual(oldStockTransActionDTO.getStatus(), Const.STOCK_TRANS_STATUS.EXPORT_NOTE)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "export.stock.code.export.not.found");
        }

        //5. Check trang thai ki voffice
        if (DataUtil.safeEqual(oldStockTransActionDTO.getSignCaStatus(), "2")) {
            //Validate giao dich da ky voffice chua
            stockTransVofficeService.doSignedVofficeValidate(oldStockTransActionDTO);
        }
        // Validate so luong serial thuc te trong kho voi so luong hang xuat.
        baseValidateService.doInputSerialValidate(stockTransDTO, lstStockTransDetail);

        List<OptionSetValueDTO> optionSetValueDTOs = optionSetValueService.getByOptionSetCode(Const.OPTION_SET.CHECK_STOCK_CARD_VC);
        if (!DataUtil.isNullOrEmpty(optionSetValueDTOs)
                && !DataUtil.isNullObject(optionSetValueDTOs.get(0))
                && DataUtil.safeEqual(optionSetValueDTOs.get(0).getValue(), Const.STATUS_ACTIVE)) {
            if (stockTransDTO.getToOwnerId() != null && stockTransDTO.getToOwnerId().equals(Const.VT_SHOP_ID)) {
                for (StockTransDetailDTO stockTransDetail : lstStockTransDetail) {
                    ProductOfferingDTO productOffering = productOfferingService.findOne(stockTransDetail.getProdOfferId());
                    if (productOffering != null && DataUtil.safeEqual(productOffering.getCheckSerial(), Const.PRODUCT_OFFERING.CHECK_SERIAL)
                            && DataUtil.safeEqual(productOffering.getProductOfferTypeId(), Const.PRODUCT_OFFER_TYPE.CARD)) {
                        if (!DataUtil.isNullOrEmpty(stockTransDetail.getLstStockTransSerial())) {
                            for (StockTransSerialDTO stockTransSerial : stockTransDetail.getLstStockTransSerial()) {
                                if (stockCardService.checkNotExistInVC(productOffering.getProductOfferingId(), stockTransSerial.getFromSerial(), stockTransSerial.getToSerial())) {
                                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "export.stock.vc.status.not");
                                }
                            }
                        }
                    }
                }
            }
        }
        // Lay gia dat coc
        if (!DataUtil.isNullOrEmpty(transDTO.getDepositStatus())) {
            Long totalDepositPrice = 0L;
            ShopDTO shopImpDTO = shopService.findOne(transDTO.getToOwnerId());
            if (DataUtil.isNullObject(shopImpDTO)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.toStock.notExists");
            }
            Long branchId = shopService.findBranchId(transDTO.getToOwnerId());
            for (StockTransDetailDTO stockTransDetail : lstStockTransDetail) {
                List<ProductOfferPriceDTO> lstPrice = productOfferPriceService.getPriceDepositAmount(stockTransDetail.getProdOfferId(),
                        branchId, DataUtil.safeToLong(shopImpDTO.getPricePolicy()), shopImpDTO.getChannelTypeId(), null, null);

                if (DataUtil.isNullOrEmpty(lstPrice) || DataUtil.isNullObject(lstPrice.get(0))) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "export.stock.coll.deposit.price.not.define");
                }
                ProductOfferPriceDTO productOfferPriceDTO = lstPrice.get(0);
                if (DataUtil.isNullObject(productOfferPriceDTO.getPrice())) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "export.stock.coll.deposit.price.not.define");
                }
                if (!DataUtil.safeEqual(productOfferPriceDTO.getProdOfferId(), stockTransDetail.getProdOfferId())) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "export.stock.coll.deposit.price.not.define");
                }
                totalDepositPrice += productOfferPriceDTO.getPrice() * stockTransDetail.getQuantity();
            }
            stockTransDTO.setDepositPrice(totalDepositPrice);
        }
    }

    @Override
    public void doSignVoffice(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, RequiredRoleMap requiredRoleMap,
                              FlagStockDTO flagStockDTO) throws Exception {

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public StockTransActionDTO executeStockTrans(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO,
                                                 List<StockTransDetailDTO> lstStockTransDetail, RequiredRoleMap requiredRoleMap) throws Exception {
        super.executeStockTrans(stockTransDTO, stockTransActionDTO,
                lstStockTransDetail, requiredRoleMap);

        exportStockAgent(stockTransDTO, stockTransActionDTO, lstStockTransDetail);
        return stockTransActionDTO;
    }

    @Transactional(rollbackFor = Exception.class)
    private void exportStockAgent(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, List<StockTransDetailDTO> lstStockTransDetail) throws Exception, LogicException {
        if (DataUtil.safeEqual(Const.PROCESS_OFFLINE, stockTransDTO.getProcessOffline())) {
            return;
        }
        // Xuat hang ban dut
        saveVcRequestAndActiveKit(stockTransDTO, stockTransActionDTO, lstStockTransDetail);
        // Luu anypay
        if (!DataUtil.isNullOrEmpty(stockTransDTO.getPayStatus()) && DataUtil.safeEqual(Const.PAY_STATUS.PAY_HAVE, stockTransDTO.getPayStatus())) {
            // Lay thong tin tu sale.
            List<SaleTransDTO> lstSaleTrans = saleTransService.findSaleTransByStockTransId(stockTransDTO.getStockTransId(), null);// sale: goi ham ben sale
            if (DataUtil.isNullOrEmpty(lstSaleTrans)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.exp.error.notAllowExpNonPay");
            }
            SaleTransDTO saleTransDTO = lstSaleTrans.get(0);
            if (!DataUtil.safeEqual(saleTransDTO.getStatus(), Const.SALE_TRANS.SALE_BILLED)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.exp.error.notAllowExpNonBill");
            }
            // 1. Thuc hien nhan tin cho dai ly
            Date currentDate = new Date();
            SaleTransDetailDTO saleTransDetailTCDT = null; // Lay thong tin giao dich cua the cao dien tu
            List<SaleTransDetailDTO> saleTransDetailDTOs = saleTransDetailService.findBySaleTransId(saleTransDTO.getSaleTransId()); // sale: Goi ham ben sale
            String message = getTextParam("export.agent.content.send.sms", DateUtil.date2ddMMyyyyString(currentDate));
            for (int i = 0; i < saleTransDetailDTOs.size(); i++) {
                SaleTransDetailDTO saleTransDetail = saleTransDetailDTOs.get(i);
                if (i != 0) {
                    message += ", " + saleTransDetail.getQuantity() + " " + saleTransDetail.getStockModelName();
                } else {
                    message += " " + saleTransDetail.getQuantity() + " " + saleTransDetail.getStockModelName();
                }
                if (!DataUtil.isNullObject(saleTransDetail.getStockModelId())) {
                    ProductOfferingDTO productOfferingDTO = productOfferingService.findOne(saleTransDetail.getStockModelId());
                    if (productOfferingDTO != null && !DataUtil.isNullObject(productOfferingDTO.getCode()) &&
                            DataUtil.safeEqual(Const.SALE_TRANS.PRODUCT_OFFERING_CODE_TCDT, productOfferingDTO.getCode().toUpperCase())) {
                        saleTransDetailTCDT = saleTransDetail;
                    }
                }

            }
            message += getText("export.agent.content.send.sms.thank");
            List<AccountAgentDTO> accountAgentDTOs = accountAgentService.getAccountAgentFromOwnerId(stockTransDTO.getToOwnerId(), stockTransDTO.getToOwnerType());
            if (!DataUtil.isNullOrEmpty(accountAgentDTOs)) {
                AccountAgentDTO accountAgentDTO = accountAgentDTOs.get(0);
                Long checkISDN = -1L;
                if (accountAgentDTO.getCheckIsdn() != null) {
                    checkISDN = accountAgentDTO.getCheckIsdn();
                }
                if (checkISDN != 0L) {
                    String isdn = accountAgentDTO.getIsdn();
                    if (DataUtil.checkPhone(isdn)) {
                        mtService.saveSms(isdn, message);
                    }
                }
            }
            // 2. Xu ly the cao dien tu.
            if (!DataUtil.isNullObject(saleTransDetailTCDT)) {
                Long amount = saleTransDetailTCDT.getAmount();
                if (amount.equals(0L)) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "export.agent.amount.tcdt.invalid");
                }
                // 2.1 lay thong tin stock_owner_tmp.
                List<StockOwnerTmpDTO> stockOwnerTmpDTOList = stockOwnerTmpService.getStockOwnerTmpByOwnerId(stockTransDTO.getToOwnerId(), stockTransDTO.getToOwnerType());
                if (DataUtil.isNullOrEmpty(stockOwnerTmpDTOList) || DataUtil.isNullObject(stockOwnerTmpDTOList.get(0))) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "export.agent.stock.owner.tmp.not.existence");
                }
                Long agentId = stockOwnerTmpDTOList.get(0).getStockId();
                if (DataUtil.isNullOrZero(agentId)) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "export.agent.stock.owner.tmp.not.existence");
                }
                // 2.2. check anypay
                AgentDTO agentDTO = anypayTransService.findAgentById(agentId);
                if (DataUtil.isNullObject(agentDTO)
                        || DataUtil.isNullObject(agentDTO.getStaffStkId())
                        || DataUtil.isNullObject(agentDTO.getStatus())
                        || !DataUtil.safeEqual(agentDTO.getStatus(), Const.STATUS_ACTIVE)) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "export.agent.anypay.agent.not.existence");
                }
                // Ghi log truoc khi thuc hien
                writeAnypay(saleTransDTO.getSaleTransId(), agentId, amount, currentDate, stockTransActionDTO.getCreateUser(), stockTransDTO.getCreateUserIpAdress(), "START", null);
                AnypayMsg anyPayMsg = anypayTransService.saleAnypay(saleTransDTO.getSaleTransId(), Const.SALE_TRANS.PRODUCT_OFFERING_ID_TCDT, agentId, amount, currentDate, stockTransActionDTO.getCreateUser(), stockTransDTO.getCreateUserIpAdress());
                // Ghi log sau khi thuc hien
                writeAnypay(saleTransDTO.getSaleTransId(), agentId, amount, new Date(), stockTransActionDTO.getCreateUser(), stockTransDTO.getCreateUserIpAdress(), "END", anyPayMsg);

                if (anyPayMsg != null && (anyPayMsg.getTransAnypayId() == null || (anyPayMsg.getErrMsg() != null && !"".equals(anyPayMsg.getErrMsg()))
                        || (anyPayMsg.getErrCode() != null && !"".equals(anyPayMsg.getErrCode())))) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "export.agent.create.trans.error", anyPayMsg.getErrMsg());
                }
                if (anyPayMsg != null) {
                    saleTransDTO.setInTransId(anyPayMsg.getTransAnypayId());
                    saleTransService.updateInTransIdById(saleTransDTO.getSaleTransId(), anyPayMsg.getTransAnypayId());
                }
                // save lai saleTransDTO sale
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    private void saveVcRequestAndActiveKit(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, List<StockTransDetailDTO> lstStockTransDetail) throws Exception, LogicException {
        if (!DataUtil.isNullOrEmpty(stockTransDTO.getPayStatus()) && DataUtil.safeEqual(Const.PAY_STATUS.PAY_HAVE, stockTransDTO.getPayStatus())) {
            ShopDTO shopDTO = shopService.findOne(stockTransDTO.getToOwnerId());
            boolean isAgent = shopService.checkChannelIsAgent(shopDTO.getChannelTypeId());
            if (isAgent) {
                List<SaleTransDTO> lstSaleTrans = saleTransService.findSaleTransByStockTransId(stockTransDTO.getStockTransId(), stockTransDTO.getStockTransDate());
                if (DataUtil.isNullOrEmpty(lstSaleTrans)
                        || DataUtil.isNullObject(lstSaleTrans.get(0))
                        || DataUtil.isNullObject(lstSaleTrans.get(0).getSaleTransId())) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "export.agent.sale.trans.not.found");
                }
                SaleTransDTO saleTransDTO = lstSaleTrans.get(0);
                for (StockTransDetailDTO stockTransDetail : lstStockTransDetail) {
                    List<SaleTransDetailDTO> saleTransDetailDTOList = saleTransDetailService.findSaleTransDetails(saleTransDTO.getSaleTransId(), stockTransDetail.getProdOfferId(), stockTransDTO.getStockTransDate());
                    if (DataUtil.isNullOrEmpty(saleTransDetailDTOList)) {
                        throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "export.agent.sale.trans.detail.not.found", saleTransDTO.getSaleTransCode());
                    }
                    SaleTransDetailDTO saleTransDetailDTO = saleTransDetailDTOList.get(0);
                    List<StockTransSerialDTO> lstStockTransSerial = stockTransDetail.getLstStockTransSerial();
                    if (!DataUtil.isNullOrEmpty(lstStockTransSerial)) {
                        for (StockTransSerialDTO stockTransSerial : lstStockTransSerial) {
                            // Luu thong tin vao sale_trans_serial.
                            SaleTransSerialDTO saleTransSerial = new SaleTransSerialDTO();
                            Long saleTransSerialId = getSequence(emSale, "BCCS_SALE.SALE_TRANS_SERIAL_SEQ");
                            saleTransSerial.setSaleTransSerialId(saleTransSerialId);
                            saleTransSerial.setSaleTransDetailId(saleTransDetailDTO.getSaleTransDetailId());
                            saleTransSerial.setFromSerial(stockTransSerial.getFromSerial());
                            saleTransSerial.setToSerial(stockTransSerial.getToSerial());
                            saleTransSerial.setQuantity(stockTransSerial.getQuantity());
                            saleTransSerial.setStockModelId(stockTransSerial.getProdOfferId());
                            saleTransSerial.setSaleTransDate(saleTransDTO.getSaleTransDate());
                            saleTransSerial.setUserDeliver(stockTransActionDTO.getActionStaffId());
                            saleTransSerial.setUserUpdate(stockTransActionDTO.getActionStaffId());
                            saleTransSerial.setDateDeliver(stockTransDTO.getStockTransDate());
                            saleTransSerial.setSaleTransId(saleTransDTO.getSaleTransId());// TienPH2: lay luon sale_trans_id tu sale_trans_detail
                            saleTransSerialService.createNewSaleTransSerialDTO(saleTransSerial);
                            saleTransDetailDTO.setTransferGood(Const.SALE_TRANS.SALE_TRANSFER_GOOD);
                            saleTransDetailService.update(saleTransDetailDTO);

                            // Luu thong tin the cao vao bang vc_request.
                            ProductOfferingDTO productOfferingDTO = productOfferingService.findOne(stockTransDetail.getProdOfferId());
                            if (!DataUtil.isNullObject(productOfferingDTO)
                                    && !DataUtil.isNullObject(productOfferingDTO.getProductOfferTypeId())
                                    && DataUtil.safeEqual(productOfferingDTO.getProductOfferTypeId(), Const.STOCK_TYPE.STOCK_CARD)) {
                                // Neu mat hang truyen vao la the cao data thi khong active tong dai.
                                List<OptionSetValueDTO> optionSetValueDTOs = optionSetValueService.getByOptionSetCode(Const.OPTION_SET.STOCK_MODEL_CARD_DATA);
                                boolean isCardData = false;
                                if (!DataUtil.isNullOrEmpty(optionSetValueDTOs)) {
                                    for (OptionSetValueDTO optionSetValueDTO : optionSetValueDTOs) {
                                        if (DataUtil.safeEqual(optionSetValueDTO.getValue(), productOfferingDTO.getCode())) {
                                            isCardData = true;
                                            break;
                                        }
                                    }
                                }
                                if (!isCardData) {
                                    VcRequestDTO vcRequestDTO = new VcRequestDTO();
                                    Long requestId = getSequence(emIm1, "BCCS_IM.VC_REQ_ID_SEQ");
                                    vcRequestDTO.setRequestId(requestId);
                                    vcRequestDTO.setCreateTime(getSysDate(emIm1));
                                    vcRequestDTO.setUserId(stockTransDTO.getUserName());
                                    vcRequestDTO.setFromSerial(stockTransSerial.getFromSerial());
                                    vcRequestDTO.setToSerial(stockTransSerial.getToSerial());
                                    vcRequestDTO.setStaffId(stockTransActionDTO.getActionStaffId());
                                    vcRequestDTO.setShopId(stockTransDTO.getShopId());
                                    if (!DataUtil.isNullObject(saleTransDetailDTO.getSaleTransId())) {
                                        vcRequestDTO.setTransId(saleTransDetailDTO.getSaleTransId());
                                    }
                                    vcRequestDTO.setRequestType(Const.SALE_TRANS.REQUEST_TYPE_SALE_AGENTS);
                                    vcRequestDTO.setStatus(0L);
                                    VcRequestDTO result = vcRequestService.create(vcRequestDTO);
                                    if (DataUtil.isNullObject(result)) {
                                        throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "export.agent.create.vc.request.error");
                                    }
                                }
                            }
                            // Luu thong tin kit vao bang vc_active_kit.
                            if (!DataUtil.isNullObject(productOfferingDTO)
                                    && !DataUtil.isNullObject(productOfferingDTO.getProductOfferTypeId())
                                    && DataUtil.safeEqual(productOfferingDTO.getProductOfferTypeId(), Const.STOCK_TYPE.STOCK_KIT)) {
                                ReqActivateKitDTO reqActivateKitDTO = new ReqActivateKitDTO();
                                Long reqId = getSequence(emIm1, "BCCS_IM.REQ_ID_SEQ");
                                reqActivateKitDTO.setReqId(reqId);
                                reqActivateKitDTO.setSaleTransId(saleTransDetailDTO.getSaleTransId());
                                reqActivateKitDTO.setFromSerial(stockTransSerial.getFromSerial());
                                reqActivateKitDTO.setToSerial(stockTransSerial.getToSerial());
                                reqActivateKitDTO.setShopId(stockTransDTO.getShopId());
                                reqActivateKitDTO.setStaffId(stockTransActionDTO.getActionStaffId());
                                reqActivateKitDTO.setSaleTransDate(saleTransDTO.getSaleTransDate());
                                reqActivateKitDTO.setCreateDate(getSysDate(emIm1));
                                reqActivateKitDTO.setSaleType(Const.SALE_TRANS.SALE_TYPE);
                                reqActivateKitDTO.setSaleTransType(Const.SALE_TRANS.SALE_TRANS_TYPE_AGENT);
                                reqActivateKitDTO.setProcessStatus(0L);
                                ReqActivateKitDTO result = reqActivateKitService.create(reqActivateKitDTO);
                                if (DataUtil.isNullObject(result)) {
                                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "export.agent.create.req_activate_kit.error");
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void doIncreaseStockNum(StockTransActionDTO stockTransActionDTO, String prefixActionCode, RequiredRoleMap requiredRoleMap) throws Exception {

    }

    @Override
    public void doSaveStockTransDetail(StockTransDTO stockTransDTO, List<StockTransDetailDTO> lstStockTransDetail) throws Exception {

    }

    @Override
    public StockTransActionDTO doSaveStockTransAction(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO) throws Exception {
        // Lap phieu xuat
        StockTransActionDTO result = super.doSaveStockTransAction(stockTransDTO, stockTransActionDTO);
        // Xuat kho
        StockTransDTO stockTransDTOData = DataUtil.cloneBean(stockTransDTO);
        stockTransDTOData.setNote(null);
        StockTransActionDTO transActionDTOExported = DataUtil.cloneBean(stockTransActionDTO);
        transActionDTOExported.setActionCode(null);
        transActionDTOExported.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
        super.doSaveStockTransAction(stockTransDTOData, transActionDTOExported);
        return result;
    }


    private ThreadLocal callIdLocal = new ThreadLocal<String>();

    private void writeAnypay(Long stockTransId, Long agentId, Long amount, Date currentDate, String createUser, String ipAddress, String action, AnypayMsg anyPayMsg) {
        String callId = GetTextFromBundleHelper.getReqId();
        if (DataUtil.isNullOrEmpty(callId)) {
            callId = (String) callIdLocal.get();
            if (DataUtil.isNullOrEmpty(callId)) {
                callId = String.valueOf(System.currentTimeMillis());
                callIdLocal.set(callId);
            }
        }
        String strCurrentDate = DateUtil.date2ddMMyyyyHHMMss(currentDate);
        String message = "callId=" + callId + "|" + action + "|currentDate=" + strCurrentDate + "|stockTransId=" + stockTransId
                + "|agentId=" + agentId + "|amount=" + amount + "|createUser=" + createUser + "|ipAddress=" + ipAddress;
        if (anyPayMsg != null) {
            message = message + "|result=" + anyPayMsg.getTransAnypayId() + ";" + anyPayMsg.getErrMsg();
        }
        anypayLog.info(message);
    }
}
