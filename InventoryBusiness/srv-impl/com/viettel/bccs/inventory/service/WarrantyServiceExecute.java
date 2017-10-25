package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.viettel.bccs.im1.dto.StockModelIm1DTO;
import com.viettel.bccs.im1.dto.StockTransDetailIm1DTO;
import com.viettel.bccs.im1.dto.StockTransSerialIm1DTO;
import com.viettel.bccs.im1.dto.StockTypeIm1DTO;
import com.viettel.bccs.im1.service.StockTotalIm1Service;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.BaseStock.BaseStockService;
import com.viettel.bccs.inventory.service.common.IMServiceUtil;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.ErrorCode;
import org.apache.cxf.jaxws.context.WebServiceContextImpl;
import org.apache.cxf.jaxws.context.WrappedMessageContext;
import org.apache.log4j.Logger;
import org.bouncycastle.util.IPAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.ServletRequest;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class WarrantyServiceExecute extends BaseStockService {

    public static final Logger logger = Logger.getLogger(WarrantyServiceExecute.class);

    private static String rexIp = "[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}";

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM_LIB)
    private EntityManager emLib;
    @Autowired
    private ProductOfferingService productOfferingService;
    @Autowired
    private StaffService staffService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private ReasonService reasonService;
    @Autowired
    private StockTransService stockTransService;
    @Autowired
    private StockDebitService stockDebitService;
    @Autowired
    private StockTotalService stockTotalService;
    @Autowired
    private ProductOfferPriceService productOfferPriceService;
    @Autowired
    private StockTransActionService stockTransActionService;
    @Autowired
    private StockTransDetailService stockTransDetailService;
    @Autowired
    private StockTransSerialService stockTransSerialService;
    @Autowired
    private OptionSetValueService optionSetValueService;
    @Autowired
    private StockHandsetService stockHandsetService;
    @Autowired
    private StockTotalIm1Service stockTotalIm1Service;

    /**
     * ham xu ly xuat kho
     *
     * @param listSerial
     * @param fromOwnerId
     * @param fromOwnerType
     * @param toOwnerId
     * @param toOwnerType
     * @param typeAction    1: ham xuat kho 2: ham nhap kho 3:xuat nhap kho cho nhan vien 4: xuat kho co doi trang thai stateId
     * @throws LogicException
     * @throws Exception
     * @author thanhnt77
     */
    @Transactional(rollbackFor = Exception.class)
    public Long importExportStock(List<ImpExpStockDTO> listSerial, Long fromOwnerId, Long fromOwnerType, Long toOwnerId, Long toOwnerType,
                                  Long newStateId, String typeAction, String staffCode) throws LogicException, Exception {

        if (DataUtil.isNullOrEmpty(listSerial)) {
            throw new LogicException("01", "warranty.create.underlying.list.product.empty");
        }

        if (Const.WARRANTY_TYPE_ACTION.EXPORT_CHANGE_STATEID.equals(typeAction)) {
            if (newStateId == null) {
                throw new LogicException("21", "warranty.create.underlying.newStateId.empty");
            } else {
                List<OptionSetValueDTO> lsStateConfig = DataUtil.defaultIfNull(optionSetValueService.getByOptionSetCode(Const.STOCK_INSPECT.STATE), new ArrayList<>());
                List<String> lsStateValid = lsStateConfig.stream().map(OptionSetValueDTO::getValue).collect(Collectors.toList());
                if (!lsStateValid.contains(DataUtil.safeToString(newStateId))) {
                    throw new LogicException("20", "warranty.create.underlying.goodstate.empty");
                }
            }
        }

        Long staffId = 0L;

        if (Const.WARRANTY_TYPE_ACTION.EXP_IMP_FOR_STAFF.equals(typeAction)) {
            if (DataUtil.isNullOrEmpty(staffCode)) {
                throw new LogicException("20", "stock.rescue.warranty.vaildate.staff");
            }
            StaffDTO staffDTO = staffService.getStaffByStaffCode(staffCode.trim());
            if (staffDTO == null) {
                throw new LogicException("20", "bankplus.service.staff.empty");
            }
            staffId = staffDTO.getStaffId();
        }

        for (ImpExpStockDTO impExpStockBO : listSerial) {
            if (impExpStockBO.getProdOfferId() == null || impExpStockBO.getStateId() == null || impExpStockBO.getQuantity() == null) {
                throw new LogicException("01", "warranty.create.underlying.list.product.empty");
            }
        }

        List<Long> lsOwnerTypeValid = Lists.newArrayList(Const.OWNER_TYPE.STAFF_LONG, Const.OWNER_TYPE.SHOP_LONG, Const.OWNER_TYPE.CUSTOMER_LONG, Const.OWNER_TYPE.PARTNER_LONG);

        if (toOwnerType == null || fromOwnerType == null) {
            throw new LogicException("03", "warranty.create.underlying.fromOnerType.toOwnerType.empty");
        } else if (!lsOwnerTypeValid.contains(fromOwnerType) || !lsOwnerTypeValid.contains(toOwnerType)) {
            throw new LogicException("02", "warranty.create.underlying.fromOnerType.toOwnerType.invalid");
        }

        if ((Const.OWNER_TYPE.SHOP_LONG.equals(fromOwnerType) || Const.OWNER_TYPE.STAFF_LONG.equals(fromOwnerType)) && fromOwnerId == null) {
            throw new LogicException("04", "warranty.create.underlying.value.notempty", "fromOwnerId");
        }

        if ((Const.OWNER_TYPE.SHOP_LONG.equals(toOwnerType) || Const.OWNER_TYPE.STAFF_LONG.equals(toOwnerType)) && toOwnerId == null) {
            throw new LogicException("04", "warranty.create.underlying.value.notempty", "toOwnerId");
        }

        //validate ownerType
        if (Const.WARRANTY_TYPE_ACTION.IMPORT_GPON.equals(typeAction)) {
            if (!(Const.OWNER_TYPE.CUSTOMER_LONG.equals(fromOwnerType) || Const.OWNER_TYPE.PARTNER_LONG.equals(fromOwnerType))) {
                throw new LogicException("04", "warranty.create.underlying.fromOnerType.export.gpon.invalid");
            }
            if (Const.OWNER_TYPE.CUSTOMER_LONG.equals(toOwnerType) || Const.OWNER_TYPE.PARTNER_LONG.equals(toOwnerType)) {
                throw new LogicException("04", "warranty.create.underlying.fromOnerType.import.gpon.invalid");
            }
        } else if (Const.WARRANTY_TYPE_ACTION.IMPORT.equals(typeAction)) {
            if (Const.OWNER_TYPE.CUSTOMER_LONG.equals(toOwnerType) || Const.OWNER_TYPE.PARTNER_LONG.equals(toOwnerType)) {
                throw new LogicException("04", "warranty.create.underlying.fromOnerType.import.gpon.invalid");
            }
            if (!Const.OWNER_TYPE.CUSTOMER_LONG.equals(toOwnerType) && !Const.OWNER_TYPE.PARTNER_LONG.equals(toOwnerType) && toOwnerId == null) {
                throw new LogicException("06", "warranty.create.underlying.fromOnerType.toOwnerType.ivalid");
            }
        } else if (Const.WARRANTY_TYPE_ACTION.EXPORT.equals(typeAction) || Const.WARRANTY_TYPE_ACTION.EXPORT_CHANGE_STATEID.equals(typeAction)) {
            if (Const.OWNER_TYPE.CUSTOMER_LONG.equals(fromOwnerType) || Const.OWNER_TYPE.PARTNER_LONG.equals(fromOwnerType)) {
                throw new LogicException("04", "warranty.create.underlying.fromOnerType.custom.partner.invalid");
            }
            if ((Const.OWNER_TYPE.CUSTOMER_LONG.equals(toOwnerType) || Const.OWNER_TYPE.PARTNER_LONG.equals(toOwnerType))
                    && (Const.OWNER_TYPE.CUSTOMER_LONG.equals(fromOwnerType) || Const.OWNER_TYPE.PARTNER_LONG.equals(fromOwnerType))) {
                throw new LogicException("04", "warranty.create.underlying.fromOnerType.toOwnerType.custom.partner.invalid");
            }
            if (!Const.OWNER_TYPE.CUSTOMER_LONG.equals(toOwnerType) && !Const.OWNER_TYPE.PARTNER_LONG.equals(toOwnerType) && toOwnerId == null) {
                throw new LogicException("06", "warranty.create.underlying.fromOnerType.toOwnerType.ivalid");
            }
        } else if (Const.WARRANTY_TYPE_ACTION.EXP_IMP_FOR_STAFF.equals(typeAction)) {
            if (!Const.OWNER_TYPE.SHOP_LONG.equals(fromOwnerType) && !Const.OWNER_TYPE.STAFF_LONG.equals(fromOwnerType)
                    && !Const.OWNER_TYPE.SHOP_LONG.equals(toOwnerType) && !Const.OWNER_TYPE.STAFF_LONG.equals(toOwnerType)) {
                throw new LogicException("04", "warranty.create.underlying.ownerType.toOwner.staff.export");
            }
        }


        List<StockTransFullDTO> lstGoods = createLstStockFull(listSerial, fromOwnerId, fromOwnerType, toOwnerId, toOwnerType);
        String pricePolicy = "";
        String toOwnerName = "";
        String toOwnerCode = "";
        String fromOwnerCode = "";
        //validate kho xuat
        if (Const.OWNER_TYPE.SHOP_LONG.equals(fromOwnerType)) {
            ShopDTO shopDTO = shopService.findOne(fromOwnerId);
            if (shopDTO == null || !Const.STATUS_ACTIVE.equals(shopDTO.getStatus())) {
                throw new LogicException("04", "warranty.create.underlying.fromOwnerId.exp.shop.notfound", fromOwnerId);
            }
            fromOwnerCode = shopDTO.getShopCode();
        } else if (Const.OWNER_TYPE.STAFF_LONG.equals(fromOwnerType)) {
            StaffDTO staffDTO = staffService.findOne(fromOwnerId);
            if (staffDTO == null || !Const.STATUS_ACTIVE.equals(staffDTO.getStatus())) {
                throw new LogicException("05", "warranty.create.underlying.fromOwnerId.exp.staff.notfound", fromOwnerId);
            }
            fromOwnerCode = staffDTO.getStaffCode();
        }
        //validate kho nhan
        if (Const.OWNER_TYPE.SHOP_LONG.equals(toOwnerType)) {
            ShopDTO shopDTO = shopService.findOne(toOwnerId);
            if (shopDTO == null || !Const.STATUS_ACTIVE.equals(shopDTO.getStatus())) {
                throw new LogicException("06", "warranty.create.underlying.fromOwnerId.exp.shop.notfound", toOwnerId);
            }
            pricePolicy = shopDTO.getPricePolicy();
            toOwnerName = shopDTO.getName();
            toOwnerCode = shopDTO.getShopCode();
        } else if (Const.OWNER_TYPE.STAFF_LONG.equals(toOwnerType)) {
            StaffDTO staffDTO = staffService.findOne(toOwnerId);
            if (staffDTO == null || !Const.STATUS_ACTIVE.equals(staffDTO.getStatus())) {
                throw new LogicException("07", "warranty.create.underlying.fromOwnerId.exp.staff.notfound", toOwnerId);
            }
            ShopDTO shopDTO = shopService.findOne(staffDTO.getShopId());
            if (shopDTO == null || !Const.STATUS_ACTIVE.equals(shopDTO.getStatus())) {
                throw new LogicException("11", "warranty.create.underlying.fromOwnerId.shop.from.staff.notfound");
            }
            pricePolicy = shopDTO.getPricePolicy();
            toOwnerName = shopDTO.getName();
            toOwnerCode = staffDTO.getStaffCode();
        }

        Long prodOfferId;
        Long prodOfferTypeId;
        Long stateId;
        Long quantity;
        String fromSerial;
        String toSerial;
        List<ProductOfferingDTO> lst;
        String tableName;

        boolean isCheckIm1 = false;
        List<OptionSetValueDTO> lstConfigEnableBccs1 = optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1");
        if (!DataUtil.isNullOrEmpty(lstConfigEnableBccs1) && !DataUtil.isNullObject(lstConfigEnableBccs1.get(0).getValue())) {
            isCheckIm1 = Const.ENABLE_UPDATE_BCCS1.equals(DataUtil.safeToLong(lstConfigEnableBccs1.get(0).getValue()));
        }

        for (StockTransFullDTO stockTransFullDTO : lstGoods) {
            List<StockTransSerialDTO> lsSerial = stockTransFullDTO.getLstSerial();
            if (!DataUtil.isNullOrEmpty(lsSerial)) {
                for (StockTransSerialDTO serialDto : lsSerial) {
                    prodOfferId = serialDto.getProdOfferId();
                    stateId = serialDto.getStateId();
                    quantity = serialDto.getQuantity();
                    fromSerial = serialDto.getFromSerial();
                    toSerial = serialDto.getToSerial();

                    if (DataUtil.isNullOrZero(prodOfferId) || DataUtil.isNullOrZero(stateId) || DataUtil.isNullOrZero(quantity)) {
                        throw new LogicException("08", "warranty.create.underlying.fromOwnerId.prodOfferId.quantity.stateId.empty");
                    }

                    ProductOfferingDTO offeringDTO = productOfferingService.findOne(prodOfferId);

                    if (offeringDTO == null || !Const.STATUS_ACTIVE.equals(offeringDTO.getStatus())) {
                        throw new LogicException("09", "warranty.create.underlying.prod.offering.empty", prodOfferId);
                    }
                    stockTransFullDTO.setCheckSerial(DataUtil.safeToString(offeringDTO.getCheckSerial()));

                    if (Const.PRODUCT_OFFERING.CHECK_SERIAL.equals(offeringDTO.getCheckSerial())) {

                        if (!(Const.STOCK_TYPE.STOCK_HANDSET.equals(offeringDTO.getProductOfferTypeId()) || Const.STOCK_TYPE.STOCK_ACCESSORIES.equals(offeringDTO.getProductOfferTypeId()))) {
                            throw new LogicException("10", "warranty.create.underlying.type.stockhandset", prodOfferId, offeringDTO.getCode());
                        }

                        if ((DataUtil.isNullOrEmpty(fromSerial) || DataUtil.isNullOrEmpty(toSerial))) {
                            throw new LogicException("13", "warranty.create.underlying.fromOwnerId.serial.empty", prodOfferId, offeringDTO.getCode());
                        }
                        if (quantity != null && !quantity.equals(1L)) {
                            throw new LogicException("17", "warranty.create.underlying.imp.exp.one.serial", prodOfferId, offeringDTO.getCode());
                        }
                        if (!DataUtil.safeEqual(fromSerial, toSerial)) {
                            throw new LogicException("15", "warranty.create.underlying.imp.exp.one.serial.handset", prodOfferId, offeringDTO.getCode());
                        }

                        if (!DataUtil.validateStringByPattern(fromSerial, Const.REGEX.SERIAL_REGEX) || !DataUtil.validateStringByPattern(toSerial, Const.REGEX.SERIAL_REGEX)
                                || fromSerial.length() > 19 || toSerial.length() > 19) {
                            throw new LogicException("15", "mm.divide.upload.valid.serial.length");
                        }

                        if (!Const.OWNER_TYPE.CUSTOMER_LONG.equals(fromOwnerType) && !Const.OWNER_TYPE.PARTNER_LONG.equals(fromOwnerType)) {
                            prodOfferTypeId = offeringDTO.getProductOfferTypeId();
                            tableName = IMServiceUtil.getTableNameByOfferType(prodOfferTypeId);
                            //check ton tai serial ung voi mat hang
                            if (!checkExistProd(fromOwnerId, fromOwnerType, prodOfferId, stateId, tableName, fromSerial)) {
                                throw new LogicException("15", "validate.stock.inspect.productOffering.notFound2", prodOfferId);
                            }
                            if (isCheckIm1) {
                                if (!checkExistProdIm1(fromOwnerId, fromOwnerType, prodOfferId, offeringDTO.getProductOfferTypeId(), stateId, fromSerial)) {
                                    throw new LogicException("15", "validate.stock.inspect.productOffering.bccs1.notFound", prodOfferId);
                                }
                            }
                        }
                    }
                }
            }
        }

        //tao du lieu thuc hien xuat kho
        ExportStockInputDTO stockInputDTO = new ExportStockInputDTO();
        stockInputDTO.setCheckIm1(isCheckIm1);
        stockInputDTO.setToOwnerId(toOwnerId);
        stockInputDTO.setToOwnerType(toOwnerType);
        stockInputDTO.setFromOwnerId(fromOwnerId);
        stockInputDTO.setFromOwnerType(fromOwnerType);
        if (DataUtil.isNullOrEmpty(toOwnerName)) {
            stockInputDTO.setToOwnerName(getText("warranty.create.underlying.cust.signle"));
        } else {
            stockInputDTO.setToOwnerName(toOwnerName);
        }
        stockInputDTO.setFromOwnerCode(fromOwnerCode);
        stockInputDTO.setToOwnerCode(toOwnerCode);
        stockInputDTO.setNote(getNoteByAction(typeAction));

        stockInputDTO.setLstGoods(lstGoods);
        stockInputDTO.setPricePolicy(pricePolicy);

        stockInputDTO.setNewStateId(newStateId);
        //xu ly tao status
        stockInputDTO.createStatusByTypeAction(typeAction);

        if (Const.WARRANTY_TYPE_ACTION.EXP_IMP_FOR_STAFF.equals(typeAction)) {
            stockInputDTO.setStaffId(staffId);
            stockInputDTO.setStaffCode(DataUtil.safeToString(staffCode).trim());
        }
        ReasonDTO reasonDTOImport = reasonService.getReason(Const.REASON_CODE.IMP_WARRANTY_INFO, Const.REASON_TYPE.IMP_STOCK_WARRANTY);
        ReasonDTO reasonDTOExport = reasonService.getReason(Const.REASON_CODE.EXP_WARRANTY_INFO, Const.REASON_TYPE.EXP_STOCK_WARRANTY);
        if (reasonDTOImport == null) {
            throw new LogicException("15", "warranty.create.underlying.imp.exp.reason.warranty", Const.REASON_CODE.IMP_WARRANTY_INFO);
        }
        if (reasonDTOExport == null) {
            throw new LogicException("15", "warranty.create.underlying.imp.exp.reason.warranty", Const.REASON_CODE.EXP_WARRANTY_INFO);
        }
        stockInputDTO.setReasonIdImport(reasonDTOImport.getReasonId());
        stockInputDTO.setReasonIdExport(reasonDTOImport.getReasonId());
        //thuc hien xuat/nhap kho
        Long stockTransId = createStockTransWarranty(stockInputDTO);

        return stockTransId;
    }

    /**
     * ham xu ly show ghi chu cho nghiep vu bao hanh, phu thuoc vao loai hanh dong
     *
     * @param typeAction
     * @return
     * @author thanhnt77
     */
    private String getNoteByAction(String typeAction) {
        String note = "";
        switch (typeAction) {
            case Const.WARRANTY_TYPE_ACTION.EXPORT://xuat kho
                note = getText("warranty.create.underlying.exp.warranty");
                break;
            case Const.WARRANTY_TYPE_ACTION.IMPORT://nhap kho
                note = getText("warranty.create.underlying.imp.warranty");
                break;
            case Const.WARRANTY_TYPE_ACTION.EXP_IMP_FOR_STAFF://xuat/nhap kho kho cho nhan vien
                note = getText("warranty.create.underlying.exp.imp.warranty");
                break;
            case Const.WARRANTY_TYPE_ACTION.EXPORT_CHANGE_STATEID://xuat kho co thay doi trang thai
                note = getText("warranty.create.underlying.exp.warranty");
                break;
            case Const.WARRANTY_TYPE_ACTION.IMPORT_GPON://nhap kho gpon
                note = getText("warranty.create.underlying.gpon.warranty");
                break;
        }
        return note;
    }

    /**
     * ham xu ly xuat kho bao hanh
     *
     * @author thanhnt77
     */
    @Transactional(rollbackFor = Exception.class)
    public Long createStockTransWarranty(ExportStockInputDTO exportStockInputDTO) throws LogicException, Exception {
        if (exportStockInputDTO == null) {
            throw new LogicException("15", "warranty.create.underlying.export.info.empty");
        } else if (DataUtil.isNullOrEmpty(exportStockInputDTO.getLstGoods())) {
            throw new LogicException("15", "warranty.create.underlying.export.listgood.info.empty");
        }
        Long stockTransId;
        Date sysdate = getSysDate(em);
        Long fromOwnerId = exportStockInputDTO.getFromOwnerId();
        Long fromOwnerType = exportStockInputDTO.getFromOwnerType();
        Long toOwnerId = exportStockInputDTO.getToOwnerId();
        Long toOwnerType = exportStockInputDTO.getToOwnerType();
        Long newStateId = exportStockInputDTO.getNewStateId();
        String note = exportStockInputDTO.getNote();
        List<StockTransFullDTO> lstGoods = exportStockInputDTO.getLstGoods();
        if (toOwnerId != null) {
            Long pricePolicyId = null;
            //validate han muc kho nhan
            if (Const.OWNER_TYPE.SHOP.equals(DataUtil.safeToString(toOwnerType))) {
                ShopDTO shop = shopService.findOne(toOwnerId);
                pricePolicyId = DataUtil.safeToLong(shop.getPricePolicy());
            } else if (Const.OWNER_TYPE.STAFF.equals(DataUtil.safeToString(toOwnerType))) {
                StaffDTO staff = staffService.findOne(toOwnerId);
                pricePolicyId = DataUtil.safeToLong(staff.getPricePolicy());
            }
            //validate check han muc kho nhan, chi danh cho kho NV, don vi
            if (Const.OWNER_TYPE.SHOP_LONG.equals(toOwnerType) || Const.OWNER_TYPE.STAFF_LONG.equals(toOwnerType)) {
                Long debitTrans = 0L;

                for (StockTransFullDTO stockFull : lstGoods) {
                    Long rootPrice = productOfferPriceService.getPriceAmount(stockFull.getProdOfferId(), Const.PRODUCT_OFFERING.PRICE_TYPE_ROOT, pricePolicyId);
                    debitTrans += DataUtil.safeToLong(stockFull.getQuantity()) * rootPrice;
                }

                //Lay han muc hien tai kho nhan
                StockDebitDTO stockDebit = stockDebitService.findStockDebitValue(toOwnerId, DataUtil.safeToString(toOwnerType));
                Long totalDebit = stockDebit == null ? 0L : stockDebit.getDebitValue();

                //Lay cac giao dich treo
                Long suspendDebit = stockTransService.getTotalValueStockSusbpendByOwnerId(toOwnerId, DataUtil.safeToString(toOwnerType));

                //lay gia tri hien tai trong kho
                Long currentDebit = stockTotalService.getTotalValueStock(toOwnerId, DataUtil.safeToString(toOwnerType));

                if (totalDebit < debitTrans + currentDebit + suspendDebit) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "warranty.create.underlying.export.over.limit.debit", toOwnerId, toOwnerType);
                }
            }
        }

        //insert StockTrans
        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setFromOwnerId(fromOwnerId);
        stockTransDTO.setFromOwnerType(fromOwnerType);
        stockTransDTO.setToOwnerId(toOwnerId);
        stockTransDTO.setToOwnerType(toOwnerType);
        stockTransDTO.setCreateDatetime(sysdate);
        stockTransDTO.setStockTransType(Const.STOCK_TRANS_TYPE.EXPORT);
        stockTransDTO.setStatus(exportStockInputDTO.getStockTransStatus());
        if (Const.STOCK_TRANS_TYPE.EXPORT.equals(exportStockInputDTO.getStockTransType())) {
            stockTransDTO.setReasonId(exportStockInputDTO.getReasonIdExport());
        }
        stockTransDTO.setImportReasonId(Const.STOCK_TRANS_TYPE.IMPORT.equals(exportStockInputDTO.getStockTransType())
                || Const.WARRANTY_TYPE_ACTION.EXP_IMP_FOR_STAFF.equals(exportStockInputDTO.getTypeAction()) ? exportStockInputDTO.getReasonIdImport() : null);
        stockTransDTO.setImportNote(Const.STOCK_TRANS_TYPE.IMPORT.equals(exportStockInputDTO.getStockTransType())
                || Const.WARRANTY_TYPE_ACTION.EXP_IMP_FOR_STAFF.equals(exportStockInputDTO.getTypeAction()) ? getText("warranty.create.underlying.import.note") : null);
        stockTransDTO.setNote(note);
        stockTransDTO.setSourceType(Const.STOCK_TRANS_SOURCE_TYPE.SOURCE_WARRANTY);
        StockTransDTO stockTransNew = stockTransService.save(stockTransDTO);
        stockTransId = stockTransNew.getStockTransId();

        //insert StockTransAction
        StockTransActionDTO stockActionDTO = new StockTransActionDTO();
        stockActionDTO.setStockTransId(stockTransNew.getStockTransId());
        stockActionDTO.setStatus(exportStockInputDTO.getStockTransActionStatus1());
        stockActionDTO.setCreateUser(exportStockInputDTO.getStaffCode());
        stockActionDTO.setCreateDatetime(sysdate);
        stockActionDTO.setNote(note);

        stockActionDTO.setActionStaffId(exportStockInputDTO.getStaffId());

        StockTransActionDTO stockTransActionDTONew = stockTransActionService.save(stockActionDTO);

        //update lai actionCode cho gd tren
        stockTransActionDTONew.setActionCode(exportStockInputDTO.getPrefixActionCode() + stockTransActionDTONew.getStockTransActionId());
        stockTransActionService.save(stockTransActionDTONew);

        //insert them mot giao dich  StockTransAction moi
        stockActionDTO.setStatus(exportStockInputDTO.getStockTransActionStatus2());
        stockActionDTO.setActionCode("");
        stockTransActionService.save(stockActionDTO);

        if (!DataUtil.isNullOrEmpty(exportStockInputDTO.getStockTransActionStatus3())) {
            stockActionDTO.setStatus(exportStockInputDTO.getStockTransActionStatus3());
            stockTransActionService.save(stockActionDTO);
        }

        if (Const.WARRANTY_TYPE_ACTION.EXP_IMP_FOR_STAFF.equals(exportStockInputDTO.getTypeAction())) {
            stockActionDTO.setStatus(Const.STOCK_TRANS_STATUS.IMPORT_NOTE);
            StockTransActionDTO stockActionNew = stockTransActionService.save(stockActionDTO);

            stockActionNew.setActionCode("PNBH00" + stockActionNew.getStockTransActionId());
            stockTransActionService.save(stockActionNew);
        }

        StockTransDetailDTO detailDTO;

        List<StockTransDetailDTO> lstStockTransDetailDTO = Lists.newArrayList();

        List<StockTransSerialDTO> lstStockTransSerial;

        for (StockTransFullDTO stockFull : lstGoods) {
            //insert StockTransDetail
            detailDTO = new StockTransDetailDTO();
            detailDTO.setStockTransId(stockTransNew.getStockTransId());
            detailDTO.setProdOfferId(stockFull.getProdOfferId());
            detailDTO.setStateId(stockFull.getStateId());
            detailDTO.setQuantity(stockFull.getQuantity());
            detailDTO.setCreateDatetime(sysdate);
            StockTransDetailDTO detailInsert = stockTransDetailService.save(detailDTO);
            lstStockTransDetailDTO.add(detailInsert);

            //insert StockTransSerial
            lstStockTransSerial = Lists.newArrayList();
            if (Const.PRODUCT_OFFERING._CHECK_SERIAL.equals(stockFull.getCheckSerial()) && !DataUtil.isNullOrEmpty(stockFull.getLstSerial())) {
                //insert StockTransSerial
                for (StockTransSerialDTO stockSerialDTO : stockFull.getLstSerial()) {
                    StockTransSerialDTO stockSerialNew = DataUtil.cloneBean(stockSerialDTO);
                    stockSerialNew.setProdOfferId(stockFull.getProdOfferId());
                    stockSerialNew.setStateId(stockFull.getStateId());
                    stockSerialNew.setStockTransId(stockTransNew.getStockTransId());
                    stockSerialNew.setCreateDatetime(sysdate);
                    stockSerialNew.setStockTransDetailId(detailInsert.getStockTransDetailId());
                    StockTransSerialDTO serialDTOInsert = stockTransSerialService.save(stockSerialNew);
                    stockSerialNew.setStockTransSerialId(serialDTOInsert.getStockTransSerialId());
                    lstStockTransSerial.add(stockSerialNew);
                }
                detailInsert.setLstStockTransSerial(lstStockTransSerial);
            }
        }

        //xu tao cau hinh de cong, tru kho
        if (Const.WARRANTY_TYPE_ACTION.EXP_IMP_FOR_STAFF.equals(exportStockInputDTO.getTypeAction())) {
            FlagStockDTO flagStockDTOExp = getFlagStockDTO(Const.WARRANTY_TYPE_ACTION.EXPORT, toOwnerType, newStateId);
            exportStockInputDTO.setTypeAction(Const.WARRANTY_TYPE_ACTION.EXPORT);
            exportStockInputDTO.setStockTransType(Const.STOCK_TRANS_TYPE.EXPORT);
            exportImportStockTotal(exportStockInputDTO, stockTransNew, stockTransActionDTONew, lstStockTransDetailDTO, flagStockDTOExp);

            FlagStockDTO flagStockDTOImp = getFlagStockDTO(Const.WARRANTY_TYPE_ACTION.IMPORT, toOwnerType, newStateId);
            exportStockInputDTO.setTypeAction(Const.WARRANTY_TYPE_ACTION.IMPORT);
            exportStockInputDTO.setStockTransType(Const.STOCK_TRANS_TYPE.IMPORT);
            exportStockInputDTO.setValidLimitImport(false);
            exportImportStockTotal(exportStockInputDTO, stockTransNew, stockTransActionDTONew, lstStockTransDetailDTO, flagStockDTOImp);
        } else {
            FlagStockDTO flagStockDTO = getFlagStockDTO(exportStockInputDTO.getTypeAction(), toOwnerType, newStateId);
            exportImportStockTotal(exportStockInputDTO, stockTransNew, stockTransActionDTONew, lstStockTransDetailDTO, flagStockDTO);
        }

        return stockTransId;
    }

    /**
     * tao thong tin cau hinh de cong tru kho
     *
     * @param typeAction
     * @param toOwnerType
     * @param newStateId
     * @return
     * @author thanhnt77
     */
    private FlagStockDTO getFlagStockDTO(String typeAction, Long toOwnerType, Long newStateId) {
        FlagStockDTO flagStockDTO = new FlagStockDTO();
        flagStockDTO.setOldStatus(Const.STOCK_GOODS.STATUS_NEW);
        flagStockDTO.setNewStatus(Const.STOCK_GOODS.STATUS_NEW);
        flagStockDTO.setSerialListFromTransDetail(true);

        switch (typeAction) {
            case Const.WARRANTY_TYPE_ACTION.EXPORT://exportStock
                //ham xuat kho
                flagStockDTO.setExportStock(true);
                flagStockDTO.setExpCurrentQuantity(Const.STOCK_TOTAL.MINUS_QUANTITY);
                flagStockDTO.setExpAvailableQuantity(Const.STOCK_TOTAL.MINUS_QUANTITY);
                if (Const.OWNER_TYPE.CUSTOMER_LONG.equals(toOwnerType) || Const.OWNER_TYPE.PARTNER_LONG.equals(toOwnerType)) {
                    flagStockDTO.setOldStatus(Const.STOCK_GOODS.STATUS_NEW);
                    flagStockDTO.setNewStatus(Const.STOCK_GOODS.STATUS_SALE);
                }
                break;
            case Const.WARRANTY_TYPE_ACTION.IMPORT://importStock
                //ham nhap kho
                flagStockDTO.setImportStock(true);
                flagStockDTO.setImpCurrentQuantity(Const.STOCK_TOTAL.PLUS_QUANTITY);
                flagStockDTO.setImpAvailableQuantity(Const.STOCK_TOTAL.PLUS_QUANTITY);
                if (Const.OWNER_TYPE.CUSTOMER_LONG.equals(toOwnerType) || Const.OWNER_TYPE.PARTNER_LONG.equals(toOwnerType)) {
                    flagStockDTO.setOldStatus(Const.STOCK_GOODS.STATUS_SALE);
                    flagStockDTO.setNewStatus(Const.STOCK_GOODS.STATUS_NEW);
                }
                break;
            case Const.WARRANTY_TYPE_ACTION.EXP_IMP_FOR_STAFF://exportImportStockTotal
                //ham xuat/nhap kho chi danh cho don vi nhan vien
                flagStockDTO.setExportStock(true);
                flagStockDTO.setExpCurrentQuantity(Const.STOCK_TOTAL.MINUS_QUANTITY);
                flagStockDTO.setExpAvailableQuantity(Const.STOCK_TOTAL.MINUS_QUANTITY);
                if (Const.OWNER_TYPE.CUSTOMER_LONG.equals(toOwnerType) || Const.OWNER_TYPE.PARTNER_LONG.equals(toOwnerType)) {
                    flagStockDTO.setOldStatus(Const.STOCK_GOODS.STATUS_NEW);
                    flagStockDTO.setNewStatus(Const.STOCK_GOODS.STATUS_SALE);
                }
                break;
            case Const.WARRANTY_TYPE_ACTION.EXPORT_CHANGE_STATEID://exportStockWarranty
                //ham xuat kho co doi trang thai stateId
                flagStockDTO.setExportStock(true);
                flagStockDTO.setExpCurrentQuantity(Const.STOCK_TOTAL.MINUS_QUANTITY);
                flagStockDTO.setExpAvailableQuantity(Const.STOCK_TOTAL.MINUS_QUANTITY);
                if (Const.OWNER_TYPE.CUSTOMER_LONG.equals(toOwnerType) || Const.OWNER_TYPE.PARTNER_LONG.equals(toOwnerType)) {
                    flagStockDTO.setOldStatus(Const.STOCK_GOODS.STATUS_NEW);
                    flagStockDTO.setNewStatus(Const.STOCK_GOODS.STATUS_SALE);
                }
                flagStockDTO.setStateIdForReasonId(newStateId);
                break;
            case Const.WARRANTY_TYPE_ACTION.IMPORT_GPON://importStockGPON
                //ham nhap kho mat hang GPON
                flagStockDTO.setImportStock(true);
                flagStockDTO.setImpCurrentQuantity(Const.STOCK_TOTAL.PLUS_QUANTITY);
                flagStockDTO.setImpAvailableQuantity(Const.STOCK_TOTAL.PLUS_QUANTITY);
                //rieng thang gpon ko check doi tac
                flagStockDTO.setOldStatus(Const.STOCK_GOODS.STATUS_SALE);
                flagStockDTO.setNewStatus(Const.STOCK_GOODS.STATUS_NEW);
                break;
        }
        return flagStockDTO;
    }

    /**
     * cong tru kho, cap nhap trang thai stock_x
     *
     * @param exportStockInputDTO
     * @param stockTransNew
     * @param stockTransActionDTONew
     * @param lstStockTransDetailDTO
     * @param flagStockDTO
     * @throws Exception
     * @author thanhnt77
     */
    private void exportImportStockTotal(ExportStockInputDTO exportStockInputDTO, StockTransDTO stockTransNew, StockTransActionDTO stockTransActionDTONew,
                                        List<StockTransDetailDTO> lstStockTransDetailDTO, FlagStockDTO flagStockDTO) throws Exception {
        Long fromOwnerId = exportStockInputDTO.getFromOwnerId();
        Long fromOwnerType = exportStockInputDTO.getFromOwnerType();
        Long toOwnerId = exportStockInputDTO.getToOwnerId();
        Long toOwnerType = exportStockInputDTO.getToOwnerType();
        if (Const.WARRANTY_TYPE_ACTION.IMPORT.equals(exportStockInputDTO.getTypeAction())) {
            //import stock, import gpon cong kho nhap
            if (toOwnerId != null && toOwnerType != null && toOwnerType.compareTo(3L) != 0 && toOwnerType.compareTo(4L) != 0) {
                //validate tong so luong dap ung
                if (Const.OWNER_TYPE.STAFF_LONG.equals(fromOwnerType) || Const.OWNER_TYPE.SHOP_LONG.equals(fromOwnerType)) {
                    if (exportStockInputDTO.isValidLimitImport()) {//case nay luon bang true, false tai ham xuat/nhap bao hanh
                        for (StockTransDetailDTO detail : lstStockTransDetailDTO) {
                            Long availAbleQuantity = getAvailableQuantity(fromOwnerId, fromOwnerType, detail.getProdOfferId(), detail.getStateId());
                            if (detail.getQuantity() > DataUtil.safeToLong(availAbleQuantity)) {
                                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "export.order.stock.quantity.msg");
                            }
                        }
                    }
                }
                doSaveStockTotal(stockTransNew, lstStockTransDetailDTO, flagStockDTO, stockTransActionDTONew);
            }
        } else if (Const.WARRANTY_TYPE_ACTION.IMPORT_GPON.equals(exportStockInputDTO.getTypeAction())) {
            //import stock, import gpon cong kho nhap
            if (toOwnerId != null && toOwnerType != null && toOwnerType.compareTo(3L) != 0 && toOwnerType.compareTo(4L) != 0) {
                doSaveStockTotal(stockTransNew, lstStockTransDetailDTO, flagStockDTO, stockTransActionDTONew);
            }
        } else if (Const.WARRANTY_TYPE_ACTION.EXPORT.equals(exportStockInputDTO.getTypeAction())
                || Const.WARRANTY_TYPE_ACTION.EXPORT_CHANGE_STATEID.equals(exportStockInputDTO.getTypeAction())) {
            //exportStock,exportStockWarranty,exportImportStockTotal - tru kho xuat
            if (fromOwnerId != null && fromOwnerType != null && fromOwnerType.compareTo(3L) != 0 && fromOwnerType.compareTo(4L) != 0) {
                //validate stockTotal
                for (StockTransDetailDTO detail : lstStockTransDetailDTO) {
                    Long availAbleQuantity = getAvailableQuantity(fromOwnerId, fromOwnerType, detail.getProdOfferId(), detail.getStateId());
                    if (detail.getQuantity() > DataUtil.safeToLong(availAbleQuantity)) {
                        throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "export.order.stock.quantity.msg");
                    }
                }
                doSaveStockTotal(stockTransNew, lstStockTransDetailDTO, flagStockDTO, stockTransActionDTONew);
            }
        }

        if (Const.OWNER_TYPE.CUSTOMER_LONG.equals(fromOwnerType) || Const.OWNER_TYPE.PARTNER_LONG.equals(fromOwnerType)) {
            stockTransNew.setFromOwnerId(0L);
        }
        if (Const.OWNER_TYPE.CUSTOMER_LONG.equals(toOwnerType) || Const.OWNER_TYPE.PARTNER_LONG.equals(toOwnerType)) {
            stockTransNew.setToOwnerId(0L);
        }
        //8. Thuc hien cap nhat chi tiet serial trong kho
        if (!Const.WARRANTY_TYPE_ACTION.IMPORT_GPON.equals(exportStockInputDTO.getTypeAction())) {
            doSaveStockGoodsNew(stockTransNew, lstStockTransDetailDTO, flagStockDTO, exportStockInputDTO);
        } else {
            doSaveStockGoodsGpon(stockTransNew, lstStockTransDetailDTO, flagStockDTO, exportStockInputDTO);
        }
    }

    /**
     * ham tra ve tong so luong dap ung
     *
     * @param ownerId
     * @param ownerType
     * @param prodOfferId
     * @param stateId
     * @return
     * @throws Exception
     * @author thanhnt77
     */
    public Long getAvailableQuantity(Long ownerId, Long ownerType, Long prodOfferId, Long stateId) throws Exception {
        StockTotalDTO stockTotalDTOs = stockTotalService.getStockTotalForProcessStock(ownerId, ownerType, prodOfferId, stateId);
        String params = MessageFormat.format("ownerId:{0}, ownerType:{1}, stateId:{2}, prodOfferId:{3}",
                ownerId, ownerType, stateId, prodOfferId);
        if (stockTotalDTOs == null) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "sell.store.no.info", params);
        }
        if (DataUtil.safeToLong(stockTotalDTOs.getAvailableQuantity()).compareTo(0L) <= 0 || DataUtil.safeToLong(stockTotalDTOs.getCurrentQuantity()).compareTo(0L) <= 0) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "warranty.create.underlying.currentQuantity");
        }
        return stockTotalDTOs.getAvailableQuantity();

    }

    /**
     * ham luu thong tin mat hang bccs2
     * author thanhnt77
     *
     * @param stockTransDTO
     * @param lstStockTransDetail
     * @param flagStockDTO
     * @param inputDTO
     * @throws Exception
     */
    public void doSaveStockGoodsNew(StockTransDTO stockTransDTO, List<StockTransDetailDTO> lstStockTransDetail,
                                    FlagStockDTO flagStockDTO, ExportStockInputDTO inputDTO) throws Exception {
        String SQLUPDATE;

        for (StockTransDetailDTO stockDetail : lstStockTransDetail) {
            ProductOfferingDTO offeringDTO = productOfferingService.findOne(stockDetail.getProdOfferId());
            if (offeringDTO == null) {
                throw new LogicException("09", "validate.stock.inspect.productOffering.notFound2", stockDetail.getProdOfferId());
            }
            if (!Const.PRODUCT_OFFERING.CHECK_SERIAL.equals(offeringDTO.getCheckSerial())) {
                continue;
            }

            String tableName = IMServiceUtil.getTableNameByOfferType(offeringDTO.getProductOfferTypeId());
            List<StockTransSerialDTO> lsSerial = stockDetail.getLstSerial();

            //Giao dich xuat
            if (Const.STOCK_TRANS_TYPE.EXPORT.equals(inputDTO.getStockTransType())) {
                //kho nhan la KH doi tac
                if (DataUtil.safeEqual(inputDTO.getToOwnerType(), Const.OWNER_TYPE.CUSTOMER_LONG) || DataUtil.safeEqual(inputDTO.getToOwnerType(), Const.OWNER_TYPE.PARTNER_LONG)) {
                    SQLUPDATE = "update " + tableName + " set state_id = #stateId, status = #status, update_datetime=#updateDatetime where PROD_OFFER_ID = #prodOfferId and owner_type =#ownerType and owner_id = #ownerId and  to_number(serial) = #serial ";
                    if ((Const.STOCK_TYPE.STOCK_HANDSET.equals(offeringDTO.getProductOfferTypeId()) || Const.STOCK_TYPE.STOCK_ACCESSORIES.equals(offeringDTO.getProductOfferTypeId()))) {
                        SQLUPDATE = "update " + tableName + " set state_id = #stateId, status = #status,update_datetime=#updateDatetime where PROD_OFFER_ID = #prodOfferId and owner_type =#ownerType and owner_id = #ownerId and  serial = #serial ";
                    }

                    if (lsSerial != null) {
                        for (StockTransSerialDTO serialDTO : lsSerial) {
                            Query query = em.createNativeQuery(SQLUPDATE);
                            if (Const.WARRANTY_TYPE_ACTION.EXPORT_CHANGE_STATEID.equals(inputDTO.getTypeAction())) {
                                query.setParameter("stateId", inputDTO.getNewStateId());
                            } else {
                                query.setParameter("stateId", serialDTO.getStateId());
                            }

                            query.setParameter("status", flagStockDTO.getNewStatus());
                            query.setParameter("prodOfferId", offeringDTO.getProductOfferingId());
                            query.setParameter("ownerType", inputDTO.getFromOwnerType());
                            query.setParameter("ownerId", inputDTO.getFromOwnerId());
                            query.setParameter("updateDatetime", stockTransDTO.getCreateDatetime());
                            query.setParameter("serial", serialDTO.getFromSerial());
                            int count = query.executeUpdate();
                            if (count <= 0) {
                                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT,
                                        "warranty.create.underlying.ownerId.empty", serialDTO.getFromSerial(), serialDTO.getToSerial(), count, inputDTO.getFromOwnerType(), inputDTO.getFromOwnerId(), offeringDTO.getProductOfferingId(), Const.STOCK_GOODS.STATUS_SALE);
                            }
                        }
                    }
                } else { ////kho nhan la KH thong thuong
                    SQLUPDATE = "update " + tableName + " set state_id = #stateId, status = #status, owner_type =#toOwnerType ,owner_id =#toOwnerId,update_datetime=#updateDatetime   where prod_offer_id=#prodOfferId " +
                            "and owner_type =#fromOwnerType and owner_id = #fromOwnerId and  to_number(serial) = #serial and status= #status";
                    if ((Const.STOCK_TYPE.STOCK_HANDSET.equals(offeringDTO.getProductOfferTypeId()) || Const.STOCK_TYPE.STOCK_ACCESSORIES.equals(offeringDTO.getProductOfferTypeId()))) {
                        SQLUPDATE = "update " + tableName + " set state_id = #stateId, status = #status, owner_type =#toOwnerType ,owner_id =#toOwnerId,update_datetime=#updateDatetime  where prod_offer_id=#prodOfferId " +
                                "and owner_type =#fromOwnerType and owner_id = #fromOwnerId and serial = #serial and status= #status";
                    }
                    if (lsSerial != null) {
                        for (StockTransSerialDTO serialDTO : lsSerial) {
                            Query query = em.createNativeQuery(SQLUPDATE);
                            if (Const.WARRANTY_TYPE_ACTION.EXPORT_CHANGE_STATEID.equals(inputDTO.getTypeAction())) {
                                query.setParameter("stateId", inputDTO.getNewStateId());
                            } else {
                                query.setParameter("stateId", serialDTO.getStateId());
                            }
                            query.setParameter("status", flagStockDTO.getNewStatus());
                            query.setParameter("prodOfferId", offeringDTO.getProductOfferingId());
                            query.setParameter("toOwnerType", inputDTO.getToOwnerType());
                            query.setParameter("toOwnerId", inputDTO.getToOwnerId());
                            query.setParameter("fromOwnerType", inputDTO.getFromOwnerType());
                            query.setParameter("fromOwnerId", inputDTO.getFromOwnerId());
                            query.setParameter("updateDatetime", stockTransDTO.getCreateDatetime());
                            query.setParameter("serial", serialDTO.getFromSerial());
                            int count = query.executeUpdate();
                            if (count <= 0) {
                                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT,
                                        "warranty.create.underlying.ownerId.empty", serialDTO.getFromSerial(), serialDTO.getToSerial(), count + 1, inputDTO.getFromOwnerType(), inputDTO.getFromOwnerId(), offeringDTO.getProductOfferingId(), Const.STOCK_GOODS.STATUS_SALE);
                            }
                        }
                    }
                }
                // neu la GD nhap va la KH doi tac
            } else if (Const.STOCK_TRANS_TYPE.IMPORT.equals(inputDTO.getStockTransType()) && (Const.OWNER_TYPE.CUSTOMER_LONG.equals(inputDTO.getFromOwnerType())
                    || Const.OWNER_TYPE.PARTNER_LONG.equals(inputDTO.getFromOwnerType()))) {
                SQLUPDATE = "update " + tableName + " set state_id = #stateId, status = #status where PROD_OFFER_ID = #prodOfferId and owner_type =#ownerType and owner_id = #ownerId and  to_number(serial) = #serial ";
                if ((Const.STOCK_TYPE.STOCK_HANDSET.equals(offeringDTO.getProductOfferTypeId()) || Const.STOCK_TYPE.STOCK_ACCESSORIES.equals(offeringDTO.getProductOfferTypeId()))) {
                    SQLUPDATE = "update " + tableName + " set state_id = #stateId, status = #status where PROD_OFFER_ID = #prodOfferId and owner_type =#ownerType and owner_id = #ownerId and  serial = #serial ";
                }
                for (StockTransSerialDTO serialDTO : lsSerial) {
                    //check serial trong kho
                    List lsDataValid = checkExistSerialStock(offeringDTO.getProductOfferingId(), serialDTO.getFromSerial(), tableName);//update_datetime=:updateDatetime
                    StringBuilder query = new StringBuilder("");
                    if (DataUtil.isNullOrEmpty(lsDataValid)) {// ko tim thay xu ly insert mat hang
                        query.append("insert into ");
                        query.append(tableName);
                        query.append("(ID, OWNER_TYPE, OWNER_ID, STATUS, STATE_ID, PROD_OFFER_ID, SERIAL, CREATE_DATE, CREATE_USER,update_datetime,stock_trans_id ");
                        query.append(" ) values (").append(tableName).append("_SEQ.NEXTVAL, #ownerType, #ownerId, #status, #stateId, #prodOfferId, #serial, #createDate, #createUser,#updateDateTime,#stockTransId )");
                        Query queryInsert = em.createNativeQuery(query.toString());
                        queryInsert.setParameter("ownerType", inputDTO.getToOwnerType());
                        queryInsert.setParameter("ownerId", inputDTO.getToOwnerId());
                        queryInsert.setParameter("status", flagStockDTO.getNewStatus());
                        queryInsert.setParameter("stateId", serialDTO.getStateId());
                        queryInsert.setParameter("prodOfferId", offeringDTO.getProductOfferingId());
                        queryInsert.setParameter("serial", serialDTO.getFromSerial());
                        queryInsert.setParameter("createDate", stockTransDTO.getCreateDatetime());
                        queryInsert.setParameter("createUser", inputDTO.getToOwnerCode());
                        queryInsert.setParameter("updateDateTime", stockTransDTO.getCreateDatetime());
                        queryInsert.setParameter("stockTransId", stockTransDTO.getStockTransId());
                        int count = queryInsert.executeUpdate();
                        if (count <= 0) {
                            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "warranty.create.underlying.ownerId.empty", serialDTO.getFromSerial(), serialDTO.getToSerial(),
                                    count, inputDTO.getFromOwnerType(), inputDTO.getFromOwnerId(), offeringDTO.getProductOfferingId(), Const.STOCK_GOODS.STATUS_SALE);
                        }
                    } else { //update mat hang

                        if (Const.STOCK_TRANS_TYPE.IMPORT.equals(inputDTO.getStockTransType())) {
                            Long statusValid = DataUtil.safeToLong(lsDataValid.get(0));
                            Long ownerIdValid = DataUtil.safeToLong(lsDataValid.get(1));
                            Long ownerTypeValid = DataUtil.safeToLong(lsDataValid.get(2));
                            if (!statusValid.equals(0L) || !ownerIdValid.equals(inputDTO.getToOwnerId()) || !ownerTypeValid.equals(inputDTO.getToOwnerType())) {
                                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "warranty.create.underlying.invalidSerial", serialDTO.getFromSerial(), inputDTO.getToOwnerId());
                                //check tiep to_owner_id!=serialDTO.getOwnerId() > bao loi: seriax xxx khong thuoc kho nhap to_owner_id=xxx
                            }
                        }

                        Query queryInsert = em.createNativeQuery(SQLUPDATE);
                        queryInsert.setParameter("stateId", serialDTO.getStateId());
                        queryInsert.setParameter("status", flagStockDTO.getNewStatus());
                        queryInsert.setParameter("ownerType", inputDTO.getToOwnerType());
                        queryInsert.setParameter("ownerId", inputDTO.getToOwnerId());
                        queryInsert.setParameter("prodOfferId", offeringDTO.getProductOfferingId());
                        queryInsert.setParameter("serial", serialDTO.getFromSerial());
                        int count = queryInsert.executeUpdate();
                        if (count <= 0) {
                            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "warranty.create.underlying.ownerId.empty", serialDTO.getFromSerial(), serialDTO.getToSerial(),
                                    count, inputDTO.getFromOwnerType(), inputDTO.getFromOwnerId(), offeringDTO.getProductOfferingId(), Const.STOCK_GOODS.STATUS_SALE);
                        }
                    }
                }
            }
        }
        if (inputDTO.isCheckIm1()) {
            doSaveStockGoodsNewIM1(stockTransDTO, lstStockTransDetail, flagStockDTO, inputDTO);
        }
    }

    /**
     * ham luu thong tin mat hang im1
     *
     * @param stockTransDTO
     * @param lstStockTransDetail
     * @param flagStockDTO
     * @param inputDTO
     * @throws Exception
     * @author thanhnt77
     */
    public void doSaveStockGoodsNewIM1(StockTransDTO stockTransDTO, List<StockTransDetailDTO> lstStockTransDetail,
                                       FlagStockDTO flagStockDTO, ExportStockInputDTO inputDTO) throws LogicException, Exception {
        String SQLUPDATE;

        List<StockTransDetailIm1DTO> lstStockTransDetailIm1 = convertStockTransDetail(lstStockTransDetail, stockTransDTO);

        if (DataUtil.isNullOrEmpty(lstStockTransDetailIm1)) {
            lstStockTransDetailIm1 = stockTotalIm1Service.findStockTransDetail(stockTransDTO.getStockTransId());
        }


        for (StockTransDetailIm1DTO stockDetail : lstStockTransDetailIm1) {

            int index;

            //ProductOfferingDTO offeringDTO = productOfferingService.findOne(stockDetail.getProdOfferId());
            StockModelIm1DTO offeringDTO = stockTotalIm1Service.findOneStockModel(stockDetail.getStockModelId());
            if (offeringDTO == null) {
                throw new LogicException("09", "validate.stock.inspect.productOffering.bccs1.notFound", stockDetail.getStockModelId());
            }
            if (!Const.PRODUCT_OFFERING.CHECK_SERIAL.equals(offeringDTO.getCheckSerial())) {
                continue;
            }

            StockTypeIm1DTO stockTypeIm1DTO = stockTotalIm1Service.findOneStockType(stockDetail.getStockModelId());
            //lay lai tableName tu stockModelId
            String tableName;

            if (!DataUtil.isNullObject(stockTypeIm1DTO)) {
                tableName = stockTypeIm1DTO.getTableName();
            } else {
                //bao loi ko ton tai mat hang tren BCCS1
                ProductOfferingDTO productOfferingDTO = productOfferingService.findOne(stockDetail.getStockModelId());
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans1.validate.stockmodel.not.exist", productOfferingDTO.getName());
            }


            List<StockTransSerialIm1DTO> lsSerial = stockDetail.getLstStockTransSerial();

            //Giao dich xuat
            if (Const.STOCK_TRANS_TYPE.EXPORT.equals(inputDTO.getStockTransType())) {
                //kho nhan la KH doi tac
                if (DataUtil.safeEqual(inputDTO.getToOwnerType(), Const.OWNER_TYPE.CUSTOMER_LONG) || DataUtil.safeEqual(inputDTO.getToOwnerType(), Const.OWNER_TYPE.PARTNER_LONG)) {
                    SQLUPDATE = "update bccs_im." + tableName + " set state_id = ?, status = ? where stock_model_id = ? "
                            + " and owner_type =? and owner_id = ? and  to_number(serial) = ?  ";
                    if (Const.STOCK_TYPE.STOCK_HANDSET.equals(offeringDTO.getStockTypeId())) {
                        SQLUPDATE = "update bccs_im." + tableName + " set state_id = ?, status = ? where stock_model_id = ? "
                                + " and owner_type =? and owner_id = ? and  serial = ?";
                    }

                    if (lsSerial != null) {
                        for (StockTransSerialIm1DTO serialDTO : lsSerial) {
                            index = 1;
                            Query query = emLib.createNativeQuery(SQLUPDATE);
                            if (Const.WARRANTY_TYPE_ACTION.EXPORT_CHANGE_STATEID.equals(inputDTO.getTypeAction())) {
                                query.setParameter(index++, inputDTO.getNewStateId());
                            } else {
                                query.setParameter(index++, serialDTO.getStateId());
                            }
                            query.setParameter(index++, flagStockDTO.getNewStatus());
                            query.setParameter(index++, serialDTO.getStockModelId());
                            query.setParameter(index++, inputDTO.getFromOwnerType());
                            query.setParameter(index++, inputDTO.getFromOwnerId());

                            if (!Const.STOCK_TYPE.STOCK_HANDSET.equals(offeringDTO.getStockTypeId())) {
                                query.setParameter(index, new BigInteger(serialDTO.getFromSerial()));
                            } else {
                                query.setParameter(index, serialDTO.getFromSerial());
                            }
                            int count = query.executeUpdate();
                            if (count <= 0) {
                                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "warranty.create.underlying.ownerId.empty",
                                        serialDTO.getFromSerial(), serialDTO.getToSerial(), count, inputDTO.getFromOwnerType(),
                                        inputDTO.getFromOwnerId(), offeringDTO.getStockModelId(), Const.STOCK_GOODS.STATUS_SALE);
                            }
                        }
                    }
                } else { ////kho nhan la KH thong thuong
                    SQLUPDATE = "update bccs_im." + tableName + " set state_id = ?, status = ?, owner_type =? ,owner_id =? ,check_dial=0, dial_status =0 where stock_model_id = ? "
                            + " and owner_type =? and owner_id = ? and  to_number(serial) = ? and status= ?";
                    if (Const.STOCK_TYPE.STOCK_HANDSET.equals(offeringDTO.getStockTypeId())) {
                        SQLUPDATE = "update bccs_im." + tableName + " set state_id = ?, status = ?, owner_type =? ,owner_id =? ,check_dial=0, dial_status =0 where stock_model_id = ? "
                                + " and owner_type =? and owner_id = ? and  serial = ? and status= ?";
                    }
                    if (lsSerial != null) {
                        for (StockTransSerialIm1DTO serialDTO : lsSerial) {
                            index = 1;
                            Query query = emLib.createNativeQuery(SQLUPDATE);
                            if (Const.WARRANTY_TYPE_ACTION.EXPORT_CHANGE_STATEID.equals(inputDTO.getTypeAction())) {
                                query.setParameter(index++, inputDTO.getNewStateId());
                            } else {
                                query.setParameter(index++, serialDTO.getStateId());
                            }
                            query.setParameter(index++, flagStockDTO.getNewStatus());
                            query.setParameter(index++, inputDTO.getToOwnerType());
                            query.setParameter(index++, inputDTO.getToOwnerId());
                            query.setParameter(index++, offeringDTO.getStockModelId());
                            query.setParameter(index++, inputDTO.getFromOwnerType());
                            query.setParameter(index++, inputDTO.getFromOwnerId());
                            if (!Const.STOCK_TYPE.STOCK_HANDSET.equals(offeringDTO.getStockTypeId())) {
                                query.setParameter(index++, new BigInteger(serialDTO.getFromSerial()));
                            } else {
                                query.setParameter(index++, serialDTO.getFromSerial());
                            }
                            query.setParameter(index, flagStockDTO.getOldStatus());
                            int count = query.executeUpdate();
                            if (count <= 0) {
                                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "warranty.create.underlying.ownerId.im1.empty",
                                        serialDTO.getFromSerial(), serialDTO.getToSerial(), count + 1, inputDTO.getFromOwnerType(),
                                        inputDTO.getFromOwnerId(), offeringDTO.getStockModelId(), Const.STOCK_GOODS.STATUS_SALE);
                            }
                        }
                    }
                }
                // neu la GD nhap va la KH doi tac
            } else if (Const.STOCK_TRANS_TYPE.IMPORT.equals(inputDTO.getStockTransType()) && (Const.OWNER_TYPE.CUSTOMER_LONG.equals(inputDTO.getFromOwnerType())
                    || Const.OWNER_TYPE.PARTNER_LONG.equals(inputDTO.getFromOwnerType()))) {
                SQLUPDATE = "update bccs_im." + tableName + " set state_id = ?, status = ? ,  owner_type =? , owner_id = ? where stock_model_id = ? "
                        + " and  to_number(serial) = ?";
                if (Const.STOCK_TYPE.STOCK_HANDSET.equals(offeringDTO.getStockTypeId())) {
                    SQLUPDATE = "update bccs_im." + tableName + " set state_id = ?, status = ?, owner_type =? , owner_id = ? where stock_model_id = ? "
                            + " and  serial = ? ";
                }
                for (StockTransSerialIm1DTO serialDTO : lsSerial) {
                    //check serial trong kho
                    index = 1;

                    List lsDataValid = checkExistSerialStockIm1(offeringDTO.getStockModelId(), serialDTO.getFromSerial(), tableName, offeringDTO.getStockTypeId());

                    StringBuilder query = new StringBuilder("");
                    if (DataUtil.isNullOrEmpty(lsDataValid)) {// ko tim thay xu ly insert mat hang
                        query.append("insert into bccs_im.");
                        query.append(tableName);
                        query.append("  (ID, OWNER_TYPE, OWNER_ID, STATUS, STATE_ID, STOCK_MODEL_ID, SERIAL, CREATE_DATE, CREATE_USER,STOCK_TRANS_ID ");
                        query.append(" ) values (").append(tableName).append("_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?,? )");

                        Query queryInsert = emLib.createNativeQuery(query.toString());

                        queryInsert.setParameter(index++, inputDTO.getToOwnerType());
                        queryInsert.setParameter(index++, inputDTO.getToOwnerId());
                        queryInsert.setParameter(index++, flagStockDTO.getNewStatus());
                        queryInsert.setParameter(index++, serialDTO.getStateId());
                        queryInsert.setParameter(index++, offeringDTO.getStockModelId());
                        if (!Const.STOCK_TYPE.STOCK_HANDSET.equals(offeringDTO.getStockTypeId())) {
                            queryInsert.setParameter(index++, new BigInteger(serialDTO.getFromSerial()));
                        } else {
                            queryInsert.setParameter(index++, serialDTO.getFromSerial());
                        }

                        queryInsert.setParameter(index++, stockTransDTO.getCreateDatetime());
                        queryInsert.setParameter(index++, inputDTO.getToOwnerCode());
                        queryInsert.setParameter(index, stockTransDTO.getStockTransId());
                        int count = queryInsert.executeUpdate();
                        if (count <= 0) {
                            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "warranty.create.underlying.ownerId.im1.empty",
                                    serialDTO.getFromSerial(), serialDTO.getToSerial(), count, inputDTO.getFromOwnerType(), inputDTO.getFromOwnerId(),
                                    offeringDTO.getStockModelId(), Const.STOCK_GOODS.STATUS_SALE);
                        }
                    } else { //update mat hang

                        if (Const.STOCK_TRANS_TYPE.IMPORT.equals(inputDTO.getStockTransType())) {
                            Long statusValid = DataUtil.safeToLong(lsDataValid.get(0));
                            Long ownerIdValid = DataUtil.safeToLong(lsDataValid.get(1));
                            Long ownerTypeValid = DataUtil.safeToLong(lsDataValid.get(2));
                            if (!statusValid.equals(0L) || !ownerIdValid.equals(inputDTO.getToOwnerId()) || !ownerTypeValid.equals(inputDTO.getToOwnerType())) {
                                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "warranty.create.underlying.invalidSerial.im1", serialDTO.getFromSerial(), inputDTO.getToOwnerId());
                            }
                        }

                        Query queryInsert = emLib.createNativeQuery(SQLUPDATE);
                        queryInsert.setParameter(index++, serialDTO.getStateId());
                        queryInsert.setParameter(index++, flagStockDTO.getNewStatus());
                        queryInsert.setParameter(index++, inputDTO.getToOwnerType());
                        queryInsert.setParameter(index++, inputDTO.getToOwnerId());
                        queryInsert.setParameter(index++, offeringDTO.getStockModelId());
                        if (!Const.STOCK_TYPE.STOCK_HANDSET.equals(offeringDTO.getStockTypeId())) {
                            queryInsert.setParameter(index, new BigInteger(serialDTO.getFromSerial()));
                        } else {
                            queryInsert.setParameter(index, serialDTO.getFromSerial());
                        }
                        int count = queryInsert.executeUpdate();
                        if (count <= 0) {
                            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "warranty.create.underlying.ownerId.empty", serialDTO.getFromSerial(), serialDTO.getToSerial(),
                                    count, inputDTO.getFromOwnerType(), inputDTO.getFromOwnerId(), offeringDTO.getStockModelId(), Const.STOCK_GOODS.STATUS_SALE);
                        }
                    }
                }
            }
        }
    }

    private List<StockTransDetailIm1DTO> convertStockTransDetail(List<StockTransDetailDTO> lstStockTransDetailDTO, StockTransDTO stockTransDTO) {
        List<StockTransDetailIm1DTO> lstStockTransDetailIm1 = Lists.newArrayList();
        for (StockTransDetailDTO detailDTO : lstStockTransDetailDTO) {
            StockTransDetailIm1DTO detailIm1 = new StockTransDetailIm1DTO();
            detailIm1.setCheckSerial(detailDTO.getCheckSerial());
            detailIm1.setCreateDatetime(detailDTO.getCreateDatetime());
            detailIm1.setDepositPrice(detailDTO.getDepositPrice());
            detailIm1.setLstStockTransSerial(convertStockTransSerial(detailDTO.getLstStockTransSerial(), detailDTO));
            detailIm1.setQuantityReal(detailDTO.getQuantity());
            detailIm1.setQuantityRes(detailDTO.getAvaiableQuantity());
            detailIm1.setStateId(detailDTO.getStateId());
            detailIm1.setStockModelId(detailDTO.getProdOfferId());
            detailIm1.setStockTypeId(detailDTO.getProdOfferTypeId());
            detailIm1.setStockTransId(stockTransDTO.getStockTransId());
            detailIm1.setStockTransDetailId(detailDTO.getStockTransDetailId());
            detailIm1.setStrStateIdAfter(detailDTO.getStrStateIdAfter());

            lstStockTransDetailIm1.add(detailIm1);
        }
        return lstStockTransDetailIm1;
    }

    private List<StockTransSerialIm1DTO> convertStockTransSerial(List<StockTransSerialDTO> lstStockTransSerial, StockTransDetailDTO detailDTO) {
        List<StockTransSerialIm1DTO> lstStockTransSerialIm1 = Lists.newArrayList();
        if (!DataUtil.isNullOrEmpty(lstStockTransSerial)) {
            for (StockTransSerialDTO serialDTO : lstStockTransSerial) {
                StockTransSerialIm1DTO serialIm1 = new StockTransSerialIm1DTO();
                serialIm1.setCreateDatetime(serialDTO.getCreateDatetime());
                serialIm1.setFromSerial(serialDTO.getFromSerial());
                serialIm1.setToSerial(serialDTO.getToSerial());
                serialIm1.setQuantity(serialDTO.getQuantity());
                serialIm1.setStateId(detailDTO.getStateId());
                serialIm1.setStockModelId(detailDTO.getProdOfferId());
                serialIm1.setStockTransId(detailDTO.getStockTransId());
                serialIm1.setStockTransSerialId(serialDTO.getStockTransSerialId());
                serialIm1.setSerialGpon(serialDTO.getSerialGpon());
                serialIm1.setTvmsMacId(serialDTO.getTvmsMacId());
                serialIm1.setTvmsCadId(serialDTO.getTvmsCadId());

                lstStockTransSerialIm1.add(serialIm1);

            }
        }

        return lstStockTransSerialIm1;
    }


    private List checkExistSerialStock(Long prodOfferId, String serial, String tableName) {
        //Kiem tra serial co trong kho ?
        String sqlSelect = "select status, owner_id, owner_type from " + tableName + " where prod_offer_id = #prodOfferId ";
        if (Const.STOCK_TYPE.STOCK_HANDSET_NAME.equals(tableName)) {
            sqlSelect += " and  serial = #serial ";
        } else {
            sqlSelect += " and  to_number(serial) >= #serial and to_number(serial) <= #serial ";
        }
        Query queryInsert = em.createNativeQuery(sqlSelect.toString());
        queryInsert.setParameter("prodOfferId", prodOfferId);
        queryInsert.setParameter("serial", serial);
        List<Object[]> result = queryInsert.getResultList();
        if (!DataUtil.isNullOrEmpty(result)) {
            return Lists.newArrayList(result.get(0)[0], result.get(0)[1], result.get(0)[2]);
        }
        return Lists.newArrayList();
    }

    private List checkExistSerialStockIm1(Long stockModelId, String serial, String tableName, Long stockTypeId) {
        //Kiem tra serial co trong kho ?
        String sqlSelect = "select status, owner_id, owner_type from bccs_im." + tableName + " where stock_model_id = #stock_model_id ";
        if (Const.STOCK_TYPE.STOCK_HANDSET.equals(stockTypeId)) {
            sqlSelect += " and  serial = #serial ";
        } else {
            sqlSelect += " and  to_number(serial) >= #serial and to_number(serial) <= #serial ";
        }
        Query query = emLib.createNativeQuery(sqlSelect.toString());
        query.setParameter("stock_model_id", stockModelId);
        if (Const.STOCK_TYPE.STOCK_HANDSET.equals(stockTypeId)) {
            query.setParameter("serial", serial);
        } else {
            query.setParameter("serial", new BigInteger(serial));
        }

        List<Object[]> result = query.getResultList();
        if (!DataUtil.isNullOrEmpty(result)) {
            return Lists.newArrayList(result.get(0)[0], result.get(0)[1], result.get(0)[2]);
        }
        return Lists.newArrayList();
    }


    public void doSaveStockGoodsGpon(StockTransDTO stockTransDTO, List<StockTransDetailDTO> lstStockTransDetail,
                                     FlagStockDTO flagStockDTO, ExportStockInputDTO inputDTO) throws LogicException, Exception {
        String SQLUPDATE;

        for (StockTransDetailDTO stockDetail : lstStockTransDetail) {
            ProductOfferingDTO offeringDTO = productOfferingService.findOne(stockDetail.getProdOfferId());
            if (offeringDTO == null) {
                throw new LogicException("09", "validate.stock.inspect.productOffering.notFound2", stockDetail.getProdOfferId());
            }
            if (!Const.PRODUCT_OFFERING.CHECK_SERIAL.equals(offeringDTO.getCheckSerial())) {
                continue;
            }

            SQLUPDATE = "update STOCK_HANDSET set state_id = #stateId, status = #status ,  owner_type =#ownerType , owner_id = #ownerId ";
            List<StockTransSerialDTO> lsSerial = stockDetail.getLstSerial();
            if (lsSerial != null) {
                for (StockTransSerialDTO serialDTO : lsSerial) {
                    String fromSerial = serialDTO.getFromSerial();
                    String toSerial = serialDTO.getToSerial();
                    StockHandsetDTO stockHandsetDTO = stockHandsetService.findStockHandsetByProdIdAndSerial(offeringDTO.getProductOfferingId(), serialDTO.getFromSerial());

                    //tim kiem serial voi mat hang handset nay thi xu ly insert moi stock_hand_
                    if (stockHandsetDTO == null) {
                        //tao cau lenh insert
                        StringBuilder strInsertQuery = new StringBuilder();
                        strInsertQuery.append(" INSERT INTO ");
                        strInsertQuery.append(" STOCK_HANDSET");
                        strInsertQuery.append("(ID, OWNER_TYPE, OWNER_ID, STATUS, STATE_ID, PROD_OFFER_ID, SERIAL, CREATE_DATE, CREATE_USER, SERIAL_GPON, TVMS_CAD_ID, TVMS_MAC_ID ");
                        strInsertQuery.append(" ) values (").append("STOCK_HANDSET").append("_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )");
                        Query queryInsert = em.createNativeQuery(strInsertQuery.toString());
                        queryInsert.setParameter(1, inputDTO.getToOwnerType());
                        queryInsert.setParameter(2, inputDTO.getToOwnerId());
                        queryInsert.setParameter(3, flagStockDTO.getNewStatus());
                        queryInsert.setParameter(4, serialDTO.getStateId());
                        queryInsert.setParameter(5, offeringDTO.getProductOfferingId());
                        queryInsert.setParameter(6, serialDTO.getFromSerial());
                        queryInsert.setParameter(7, stockTransDTO.getCreateDatetime());
                        queryInsert.setParameter(8, inputDTO.getToOwnerCode());
                        queryInsert.setParameter(9, serialDTO.getSerialGpon() == null ? "" : serialDTO.getSerialGpon());
                        queryInsert.setParameter(10, serialDTO.getTvmsCadId() == null ? "" : serialDTO.getTvmsCadId());
                        queryInsert.setParameter(11, serialDTO.getTvmsMacId() == null ? "" : serialDTO.getTvmsMacId());
                        int count = queryInsert.executeUpdate();
                        if (count <= 0) {
                            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT,
                                    "warranty.create.underlying.ownerId.empty", fromSerial, toSerial, count, inputDTO.getFromOwnerType(), inputDTO.getFromOwnerId(), offeringDTO.getProductOfferingId(), Const.STOCK_GOODS.STATUS_SALE);
                        }
                    } else {
                        List lsDataValid = checkExistSerialStock(offeringDTO.getProductOfferingId(), serialDTO.getFromSerial(), IMServiceUtil.getTableNameByOfferType(offeringDTO.getProductOfferTypeId()));

                        if (Const.STOCK_TRANS_TYPE.IMPORT.equals(inputDTO.getStockTransType())) {
                            Long statusValid = DataUtil.safeToLong(lsDataValid.get(0));
                            Long ownerIdValid = DataUtil.safeToLong(lsDataValid.get(1));
                            Long ownerTypeValid = DataUtil.safeToLong(lsDataValid.get(2));
                            if (!statusValid.equals(0L) || !ownerIdValid.equals(inputDTO.getToOwnerId()) || !ownerTypeValid.equals(inputDTO.getToOwnerType())) {
                                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "warranty.create.underlying.invalidSerial", serialDTO.getFromSerial(), inputDTO.getToOwnerId());
                            }
                        }

                        if (!DataUtil.isNullOrEmpty(serialDTO.getSerialGpon())) {
                            SQLUPDATE += ", SERIAL_GPON = #serialPon ";
                        }
                        if (!DataUtil.isNullOrEmpty(serialDTO.getTvmsCadId())) {
                            SQLUPDATE += ", TVMS_CAD_ID = #tvmCadId ";
                        }
                        if (!DataUtil.isNullOrEmpty(serialDTO.getTvmsMacId())) {
                            SQLUPDATE += ", TVMS_MAC_ID = #tvmsMacId ";
                        }

                        SQLUPDATE += " where PROD_OFFER_ID = #prodOfferId and  serial = #serial  ";

                        Query query = em.createNativeQuery(SQLUPDATE);
                        query.setParameter("stateId", serialDTO.getStateId());
                        query.setParameter("status", flagStockDTO.getNewStatus());
                        query.setParameter("ownerType", inputDTO.getToOwnerType());
                        query.setParameter("ownerId", inputDTO.getToOwnerId());
                        query.setParameter("prodOfferId", offeringDTO.getProductOfferingId());
                        query.setParameter("serial", serialDTO.getFromSerial());
                        if (!DataUtil.isNullOrEmpty(serialDTO.getSerialGpon())) {
                            query.setParameter("serialPon", serialDTO.getSerialGpon());
                        }
                        if (!DataUtil.isNullOrEmpty(serialDTO.getTvmsCadId())) {
                            query.setParameter("tvmCadId", serialDTO.getTvmsCadId());
                        }
                        if (!DataUtil.isNullOrEmpty(serialDTO.getTvmsMacId())) {
                            query.setParameter("tvmsMacId", serialDTO.getTvmsMacId());
                        }
                        int count = query.executeUpdate();
                        if (count <= 0) {
                            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT,
                                    "warranty.create.underlying.ownerId.empty", fromSerial, toSerial, count, inputDTO.getFromOwnerType(), inputDTO.getFromOwnerId(), offeringDTO.getProductOfferingId(), Const.STOCK_GOODS.STATUS_SALE);
                        }
                    }
                }
            }
        }
        if (inputDTO.isCheckIm1()) {
            doSaveStockGoodsGponIM1(stockTransDTO, lstStockTransDetail, flagStockDTO, inputDTO);
        }
    }

    public void doSaveStockGoodsGponIM1(StockTransDTO stockTransDTO, List<StockTransDetailDTO> lstStockTransDetail,
                                        FlagStockDTO flagStockDTO, ExportStockInputDTO inputDTO) throws LogicException, Exception {
        String SQLUPDATE;

        List<StockTransDetailIm1DTO> lstStockTransDetailIm1 = convertStockTransDetail(lstStockTransDetail, stockTransDTO);
        if (DataUtil.isNullOrEmpty(lstStockTransDetailIm1)) {
            lstStockTransDetailIm1 = stockTotalIm1Service.findStockTransDetail(stockTransDTO.getStockTransId());
        }

        for (StockTransDetailIm1DTO stockDetail : lstStockTransDetailIm1) {


            //ProductOfferingDTO offeringDTO = productOfferingService.findOne(stockDetail.getProdOfferId());
            StockModelIm1DTO offeringDTO = stockTotalIm1Service.findOneStockModel(stockDetail.getStockModelId());
            if (offeringDTO == null) {
                throw new LogicException("09", "validate.stock.inspect.productOffering.bccs1.notFound", stockDetail.getStockModelId());
            }
            if (!Const.PRODUCT_OFFERING.CHECK_SERIAL.equals(offeringDTO.getCheckSerial())) {
                continue;
            }

            SQLUPDATE = "update bccs_im.STOCK_HANDSET set state_id = ?, status = ? ,  owner_type =? , owner_id = ? ";
            List<StockTransSerialIm1DTO> lsSerial = stockDetail.getLstStockTransSerial();

            if (lsSerial != null) {
                for (StockTransSerialIm1DTO serialDTO : lsSerial) {
                    int index = 1;

                    String fromSerial = serialDTO.getFromSerial();
                    String toSerial = serialDTO.getToSerial();
                    //Kiem tra serial co trong kho ?
                    String sqlSelect = "select 1 from bccs_im.STOCK_HANDSET where stock_model_id = ? and  serial = ? ";
                    Query querySelect = emLib.createNativeQuery(sqlSelect);
                    querySelect.setParameter(index++, offeringDTO.getStockModelId());
                    querySelect.setParameter(index, fromSerial);

                    List lst = querySelect.getResultList();

                    index = 1;

                    //tim kiem serial voi mat hang handset nay thi xu ly insert moi stock_hand_
                    if (DataUtil.isNullOrEmpty(lst)) {
                        //tao cau lenh insert
                        StringBuilder strInsertQuery = new StringBuilder();
                        strInsertQuery.append(" INSERT INTO ");
                        strInsertQuery.append(" bccs_im.STOCK_HANDSET");
                        strInsertQuery.append(" (ID, OWNER_TYPE, OWNER_ID, STATUS, STATE_ID, STOCK_MODEL_ID, SERIAL, CREATE_DATE, CREATE_USER, SERIAL_GPON, TVMS_CAD_ID, TVMS_MAC_ID ");
                        strInsertQuery.append(" ) values (").append("STOCK_HANDSET").append("_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )");
                        Query queryInsert = emLib.createNativeQuery(strInsertQuery.toString());
                        queryInsert.setParameter(index++, inputDTO.getToOwnerType());
                        queryInsert.setParameter(index++, inputDTO.getToOwnerId());
                        queryInsert.setParameter(index++, flagStockDTO.getNewStatus());
                        queryInsert.setParameter(index++, serialDTO.getStateId());
                        queryInsert.setParameter(index++, offeringDTO.getStockModelId());
                        if (offeringDTO.getStockTypeId() != null && !offeringDTO.getStockTypeId().equals(Const.STOCK_TYPE.STOCK_HANDSET)) {
                            querySelect.setParameter(index++, new BigInteger(fromSerial));
                        } else {
                            queryInsert.setParameter(index++, fromSerial);
                        }

                        queryInsert.setParameter(index++, stockTransDTO.getCreateDatetime());
                        queryInsert.setParameter(index++, inputDTO.getToOwnerCode());
                        queryInsert.setParameter(index++, serialDTO.getSerialGpon() == null ? "" : serialDTO.getSerialGpon());
                        queryInsert.setParameter(index++, serialDTO.getTvmsCadId() == null ? "" : serialDTO.getTvmsCadId());
                        queryInsert.setParameter(index, serialDTO.getTvmsMacId() == null ? "" : serialDTO.getTvmsMacId());
                        int count = queryInsert.executeUpdate();
                        if (count <= 0) {
                            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT,
                                    "warranty.create.underlying.ownerId.im1.empty", fromSerial, toSerial, count, inputDTO.getFromOwnerType(),
                                    inputDTO.getFromOwnerId(), offeringDTO.getStockModelId(), Const.STOCK_GOODS.STATUS_SALE);
                        }
                    } else {

                        List lsDataValid = checkExistSerialStockIm1(offeringDTO.getStockModelId(), serialDTO.getFromSerial(), IMServiceUtil.getTableNameByOfferType(offeringDTO.getStockTypeId()), offeringDTO.getStockTypeId());

                        if (Const.STOCK_TRANS_TYPE.IMPORT.equals(inputDTO.getStockTransType())) {
                            Long statusValid = DataUtil.safeToLong(lsDataValid.get(0));
                            Long ownerIdValid = DataUtil.safeToLong(lsDataValid.get(1));
                            Long ownerTypeValid = DataUtil.safeToLong(lsDataValid.get(2));
                            if (!statusValid.equals(0L) || !ownerIdValid.equals(inputDTO.getToOwnerId()) || !ownerTypeValid.equals(inputDTO.getToOwnerType())) {
                                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "warranty.create.underlying.invalidSerial.im1", serialDTO.getFromSerial(), inputDTO.getToOwnerId());
                            }
                        }

                        if (!DataUtil.isNullOrEmpty(serialDTO.getSerialGpon())) {
                            SQLUPDATE += ", SERIAL_GPON = ? ";
                        }
                        if (!DataUtil.isNullOrEmpty(serialDTO.getTvmsCadId())) {
                            SQLUPDATE += ", TVMS_CAD_ID = ? ";
                        }
                        if (!DataUtil.isNullOrEmpty(serialDTO.getTvmsMacId())) {
                            SQLUPDATE += ", TVMS_MAC_ID = ? ";
                        }

                        if (offeringDTO.getStockTypeId() != null && !offeringDTO.getStockTypeId().equals(Const.STOCK_TYPE.STOCK_HANDSET)) {
                            SQLUPDATE += " where stock_model_id = ? and  to_number(serial) = ? ";
                        } else {
                            SQLUPDATE += " where stock_model_id = ? and  serial = ?  ";
                        }

                        Query query = emLib.createNativeQuery(SQLUPDATE);
                        query.setParameter(index++, serialDTO.getStateId());
                        query.setParameter(index++, flagStockDTO.getNewStatus());
                        query.setParameter(index++, inputDTO.getToOwnerType());
                        query.setParameter(index++, inputDTO.getToOwnerId());

                        if (!DataUtil.isNullOrEmpty(serialDTO.getSerialGpon())) {
                            query.setParameter(index++, serialDTO.getSerialGpon());
                        }
                        if (!DataUtil.isNullOrEmpty(serialDTO.getTvmsCadId())) {
                            query.setParameter(index++, serialDTO.getTvmsCadId());
                        }
                        if (!DataUtil.isNullOrEmpty(serialDTO.getTvmsMacId())) {
                            query.setParameter(index++, serialDTO.getTvmsMacId());
                        }

                        query.setParameter(index++, offeringDTO.getStockModelId());

                        if (offeringDTO.getStockTypeId() != null && !offeringDTO.getStockTypeId().equals(Const.STOCK_TYPE.STOCK_HANDSET)) {
                            query.setParameter(index, new BigInteger(serialDTO.getFromSerial()));
                        } else {
                            query.setParameter(index, serialDTO.getFromSerial());
                        }

                        int count = query.executeUpdate();
                        if (count <= 0) {
                            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT,
                                    "warranty.create.underlying.ownerId.im1.empty", fromSerial, toSerial, count, inputDTO.getFromOwnerType(), inputDTO.getFromOwnerId(), offeringDTO.getStockModelId(), Const.STOCK_GOODS.STATUS_SALE);
                        }
                    }
                }
            }
        }
    }


    /**
     * ham xu ly check ton tai serial voi mat hang tuong ung
     *
     * @param ownerId
     * @param ownerType
     * @param stateId
     * @param tableName
     * @param serial
     * @return true neu co ton tai
     * @author thanhnt77
     */
    private boolean checkExistProd(Long ownerId, Long ownerType, Long prodOfferId, Long stateId, String tableName, String serial) throws Exception {
        StringBuilder sqlBuilder = new StringBuilder("");
        sqlBuilder.append(" SELECT 1 FROM ");
        sqlBuilder.append(tableName);
        sqlBuilder.append(" WHERE status = #status and owner_id = #ownerId and owner_type = #ownerType and prod_offer_id = #prodOfferId and state_id = #stateId  ");
        if (Const.STOCK_TYPE.STOCK_HANDSET_NAME.equals(tableName)) {
            sqlBuilder.append(" and serial=#serial  ");
        } else {
            sqlBuilder.append(" and to_number(serial)=#serial  ");
        }
        Query query = em.createNativeQuery(sqlBuilder.toString());
        query.setParameter("status", Const.STATUS.ACTIVE);
        query.setParameter("ownerId", ownerId);
        query.setParameter("ownerType", ownerType);
        query.setParameter("prodOfferId", prodOfferId);
        query.setParameter("stateId", stateId);
        query.setParameter("serial", serial);
        List<Long> results = query.getResultList();
        return !DataUtil.isNullOrEmpty(results);
    }

    /**
     * ham xu ly check ton tai serial voi mat hang tuong ung
     *
     * @param ownerId
     * @param ownerType
     * @param stateId
     * @param serial
     * @return true neu co ton tai
     * @author thanhnt77
     */
    private boolean checkExistProdIm1(Long ownerId, Long ownerType, Long stockModelId, Long stockTypeId, Long stateId, String serial) throws Exception {
        StockTypeIm1DTO stockTypeIm1DTO = stockTotalIm1Service.findOneStockType(stockModelId);
        //lay lai tableName tu stockModelId
        String tableName;

        if (!DataUtil.isNullObject(stockTypeIm1DTO)) {
            tableName = stockTypeIm1DTO.getTableName();
        } else {
            //bao loi ko ton tai mat hang tren BCCS1
            ProductOfferingDTO productOfferingDTO = productOfferingService.findOne(stockModelId);
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans1.validate.stockmodel.not.exist", productOfferingDTO.getName());
        }

        StringBuilder sqlBuilder = new StringBuilder("");
        sqlBuilder.append(" SELECT 1 FROM bccs_im.");
        sqlBuilder.append(tableName);
        sqlBuilder.append(" WHERE status = #status and owner_id = #ownerId and owner_type = #ownerType and stock_model_id = #stockModelId and state_id = #stateId  ");
        if (!Const.STOCK_TYPE.STOCK_HANDSET.equals(stockTypeId)) {
            sqlBuilder.append(" and to_number(serial)=#serial  ");
        } else {
            sqlBuilder.append(" and serial=#serial  ");
        }
        Query query = emLib.createNativeQuery(sqlBuilder.toString());
        query.setParameter("status", Const.STATUS.ACTIVE);
        query.setParameter("ownerId", ownerId);
        query.setParameter("ownerType", ownerType);
        query.setParameter("stockModelId", stockModelId);
        query.setParameter("stateId", stateId);
        if (!Const.STOCK_TYPE.STOCK_HANDSET.equals(stockTypeId)) {
            query.setParameter("serial", new BigInteger(serial));
        } else {
            query.setParameter("serial", serial);
        }
        List<Long> results = query.getResultList();
        return !DataUtil.isNullOrEmpty(results);
    }

    /**
     * ham xu ly tao list StockTransFullDTO tu du lieu dau vao
     *
     * @param listSerialImp
     * @param fromOwnerId
     * @param fromOwnerType
     * @param toOwnerId
     * @param toOwnerType
     * @return
     * @author thanhnt77
     */
    private List<StockTransFullDTO> createLstStockFull(List<ImpExpStockDTO> listSerialImp, Long fromOwnerId, Long fromOwnerType, Long toOwnerId, Long toOwnerType) {
        List<StockTransFullDTO> listGoods = Lists.newArrayList();
        StockTransFullDTO stockTransFullDTO;

        StockTransSerialDTO stockTransSerial;
        List<StockTransSerialDTO> listSerial;
        for (ImpExpStockDTO impExpStockDTO : listSerialImp) {
            listSerial = Lists.newArrayList();
            //Clone object
            stockTransSerial = new StockTransSerialDTO();
            stockTransSerial.setProdOfferId(impExpStockDTO.getProdOfferId());
            stockTransSerial.setStateId(impExpStockDTO.getStateId());
            stockTransSerial.setQuantity(impExpStockDTO.getQuantity());
            stockTransSerial.setFromSerial(DataUtil.safeToString(impExpStockDTO.getFromSerial()).trim());
            stockTransSerial.setToSerial(DataUtil.safeToString(impExpStockDTO.getToSerial()).trim());
            stockTransSerial.setSerialGpon(impExpStockDTO.getSerialGpon());
            stockTransSerial.setTvmsCadId(impExpStockDTO.getTvmsCadId());
            stockTransSerial.setTvmsMacId(impExpStockDTO.getTvmsMacId());

            if (!DataUtil.isNullOrEmpty(listGoods)) {
                boolean isDuplicate = false;
                for (StockTransFullDTO stockTransFullTmp : listGoods) {
                    if (stockTransFullTmp.getProdOfferId() != null && stockTransFullTmp.getProdOfferId().compareTo(impExpStockDTO.getProdOfferId()) == 0
                            && stockTransFullTmp.getStateId() != null && stockTransFullTmp.getStateId().compareTo(impExpStockDTO.getStateId()) == 0) {
                        stockTransFullTmp.getLstSerial().add(stockTransSerial);
                        stockTransFullTmp.setQuantity(stockTransFullTmp.getQuantity() + impExpStockDTO.getQuantity());
                        //stockTransFullTmp.setQuantityReal(stockTransFullTmp.getQuantityReal() + impExpStockDTO.getQuantity());
                        isDuplicate = true;
                    }
                }
                if (!isDuplicate) {
                    listSerial.add(stockTransSerial);

                    stockTransFullDTO = new StockTransFullDTO();
                    stockTransFullDTO.setFromOwnerId(fromOwnerId);
                    stockTransFullDTO.setFromOwnerType(DataUtil.safeToString(fromOwnerType));
                    stockTransFullDTO.setToOwnerId(toOwnerId);
                    stockTransFullDTO.setToOwnerType(DataUtil.safeToString(toOwnerType));

                    stockTransFullDTO.setProdOfferId(stockTransSerial.getProdOfferId());
                    stockTransFullDTO.setStateId(stockTransSerial.getStateId());
                    stockTransFullDTO.setQuantity(stockTransSerial.getQuantity());
                    stockTransFullDTO.setLstSerial(listSerial);

                    listGoods.add(stockTransFullDTO);
                }
            } else {
                listSerial.add(stockTransSerial);

                stockTransFullDTO = new StockTransFullDTO();
                stockTransFullDTO.setFromOwnerId(fromOwnerId);
                stockTransFullDTO.setFromOwnerType(DataUtil.safeToString(fromOwnerType));
                stockTransFullDTO.setToOwnerId(toOwnerId);
                stockTransFullDTO.setToOwnerType(DataUtil.safeToString(toOwnerType));

                stockTransFullDTO.setProdOfferId(stockTransSerial.getProdOfferId());
                stockTransFullDTO.setStateId(stockTransSerial.getStateId());
                stockTransFullDTO.setQuantity(stockTransSerial.getQuantity());
                stockTransFullDTO.setLstSerial(listSerial);

                listGoods.add(stockTransFullDTO);
            }
        }
        return listGoods;
    }

    /**
     * ham xu ly tra cuu lich su bao hanh
     *
     * @param logId
     * @param methodName
     * @param paramsInput
     * @param responseCode
     * @param description
     * @param fromDate
     * @param endDate
     * @return
     * @author thanhnt77
     */
    public List<WarrantyStockLog> getWarrantyStockLog(Long logId, String methodName, String paramsInput, String responseCode, String description, Date fromDate, Date endDate) {
        StringBuilder sqlBuilder = new StringBuilder("");
        sqlBuilder.append(" SELECT id,node_webservice,method_name,params_input, ");
        sqlBuilder.append(" response_code,description,result_call,start_date,end_date,ip_client,duration ");
        sqlBuilder.append(" FROM warranty_stock_log a  ");
        sqlBuilder.append(" WHERE 1=1 ");
        if (logId != null) {
            sqlBuilder.append("     AND a.id = #logId  ");
        }
        if (fromDate != null) {
            sqlBuilder.append("     AND a.start_date >= #fromDate  ");
        }
        if (endDate != null) {
            sqlBuilder.append("     AND a.start_date <= #endDate ");
        }
        if (!DataUtil.isNullOrEmpty(DataUtil.safeToString(methodName).trim())) {
            sqlBuilder.append("     AND LOWER (a.method_name) LIKE #methodName ");
        }
        if (!DataUtil.isNullOrEmpty(DataUtil.safeToString(paramsInput).trim())) {
            sqlBuilder.append("     AND LOWER (a.params_input) LIKE #paramsInput  ");
        }
        if (!DataUtil.isNullOrEmpty(DataUtil.safeToString(responseCode).trim())) {
            sqlBuilder.append("     AND a.response_code = #responseCode  ");
        }
        if (!DataUtil.isNullOrEmpty(DataUtil.safeToString(description).trim())) {
            sqlBuilder.append("     AND LOWER (a.description) LIKE #description  ");
        }

        Query query = em.createNativeQuery(sqlBuilder.toString());
        if (logId != null) {
            query.setParameter("logId", logId);
        }
        if (fromDate != null) {
            query.setParameter("fromDate", fromDate);
        }
        if (endDate != null) {
            query.setParameter("endDate", endDate);
        }
        if (!DataUtil.isNullOrEmpty(DataUtil.safeToString(methodName).trim())) {
            query.setParameter("methodName", "%" + methodName.trim().toLowerCase() + "%");
        }
        if (!DataUtil.isNullOrEmpty(DataUtil.safeToString(paramsInput).trim())) {
            query.setParameter("paramsInput", "%" + paramsInput.trim().toLowerCase() + "%");
        }
        if (!DataUtil.isNullOrEmpty(DataUtil.safeToString(responseCode).trim())) {
            query.setParameter("responseCode", responseCode.trim());
        }
        if (!DataUtil.isNullOrEmpty(DataUtil.safeToString(description).trim())) {
            query.setParameter("description", "%" + description.trim().toLowerCase() + "%");
        }
        List<Object[]> results = query.getResultList();
        List<WarrantyStockLog> lsStockLogs = Lists.newArrayList();
        if (!DataUtil.isNullOrEmpty(results)) {
            int index;
            /*
                    sqlBuilder.append(" SELECT id,node_webservice,method_name,params_input, ");
        sqlBuilder.append(" response_code,description,result_call,start_date,end_date,ip_client,duration ");
            */
            WarrantyStockLog warrantyStockLog;
            for (Object[] result : results) {
                index = 0;
                warrantyStockLog = new WarrantyStockLog();
                warrantyStockLog.setId(DataUtil.safeToLong(result[index++]));
                warrantyStockLog.setNodeWebservice(DataUtil.safeToString(result[index++]));
                warrantyStockLog.setMethodName(DataUtil.safeToString(result[index++]));
                warrantyStockLog.setParamsInput(DataUtil.safeToString(result[index++]));
                warrantyStockLog.setResponseCode(DataUtil.safeToString(result[index++]));
                warrantyStockLog.setDescription(DataUtil.safeToString(result[index++]));
                warrantyStockLog.setResultCall(DataUtil.safeToString(result[index++]));
                Object dateTmp = result[index++];
                warrantyStockLog.setStartDate(dateTmp != null ? (Date) dateTmp : null);
                dateTmp = result[index++];
                warrantyStockLog.setEndDate(dateTmp != null ? (Date) dateTmp : null);
                warrantyStockLog.setIpClient(DataUtil.safeToString(result[index++]));
                warrantyStockLog.setDuration(DataUtil.safeToString(result[index]));
                lsStockLogs.add(warrantyStockLog);
            }
        }
        return lsStockLogs;
    }

    /**
     * tra cuu hang bao hanh
     *
     * @param ownerType
     * @param ownerId
     * @param stockModelCode
     * @param stockModelName
     * @param stateId
     * @return
     * @author thanhnt77
     */
    public List<StockManagementForWarranty> viewStockModelWarranty(Long ownerType, Long ownerId, String stockModelCode, String stockModelName, Long stateId) {
        StringBuilder sqlBuilder = new StringBuilder("");
        if (ownerType == null || Const.OWNER_TYPE.SHOP_LONG.equals(ownerType)) {
            sqlBuilder.append("	SELECT sh.shop_code ownercode, ");
            sqlBuilder.append("		   sh.name ownername, ");
            sqlBuilder.append("		   sm.code prodOffercode, ");
            sqlBuilder.append("		   sm.name prodOffername, ");
            sqlBuilder.append("		   app.name statename, ");
            sqlBuilder.append("		   shs.state_id stateid, ");
            sqlBuilder.append("		   shs.owner_id ownerid, ");
            sqlBuilder.append("		   shs.owner_type ownertype, ");
            sqlBuilder.append("		   shs.prod_offer_id prodOfferid, ");
            sqlBuilder.append("		   COUNT (*) quantity ");
            sqlBuilder.append("	  FROM bccs_im.stock_handset shs, ");
            sqlBuilder.append("		   bccs_im.shop sh, ");
            sqlBuilder.append("		   bccs_im.product_offering sm, ");
            sqlBuilder.append("		   (SELECT osv.name, trim(osv.VALUE) value ");
            sqlBuilder.append("			  FROM bccs_im.option_set os, bccs_im.option_set_value osv ");
            sqlBuilder.append("			 WHERE os.id = osv.option_set_id AND os.code = 'GOOD_STATE') app ");
            sqlBuilder.append("	 WHERE     1 = 1 ");
            sqlBuilder.append("		   AND shs.prod_offer_id = sm.prod_offer_id ");
            sqlBuilder.append("		   AND to_char(shs.state_id) = app.value ");
            sqlBuilder.append("		   AND shs.owner_type = 1 ");
            sqlBuilder.append("		   AND shs.owner_id = sh.shop_id ");
            sqlBuilder.append("		   AND sh.status = 1 ");
            sqlBuilder.append("		   AND shs.status = 1 ");
            if (stateId != null) {
                sqlBuilder.append("		   AND shs.state_id = #stateId ");
            }
            sqlBuilder.append("		   AND sm.code LIKE 'BH_%' ");
            if (!DataUtil.isNullOrEmpty(stockModelCode)) {
                sqlBuilder.append("		   AND sm.code LIKE #stockModelCode ");
            }
            if (!DataUtil.isNullOrEmpty(stockModelName)) {
                sqlBuilder.append("		   AND lower(sm.name) LIKE #stockModelName ");
            }
            if (ownerId != null) {
                sqlBuilder.append("		   AND sh.shop_id = #ownerId ");
            }
            sqlBuilder.append("	GROUP BY sh.shop_code, ");
            sqlBuilder.append("			 sh.name, ");
            sqlBuilder.append("			 sm.code, ");
            sqlBuilder.append("			 sm.name, ");
            sqlBuilder.append("			 shs.state_id, ");
            sqlBuilder.append("			 shs.owner_id, ");
            sqlBuilder.append("			 shs.owner_type, ");
            sqlBuilder.append("			 shs.prod_offer_id, ");
            sqlBuilder.append("			 app.name ");
            sqlBuilder.append("	UNION ALL ");
            sqlBuilder.append("	SELECT sh.shop_code ownercode, ");
            sqlBuilder.append("		   sh.name ownername, ");
            sqlBuilder.append("		   sm.code prodOffercode, ");
            sqlBuilder.append("		   sm.name prodOffername, ");

            sqlBuilder.append("		   (SELECT   osv.name FROM   bccs_im.option_set os, bccs_im.option_set_value osv ");
            sqlBuilder.append("		   WHERE   os.id = osv.option_set_id AND os.code = 'GOOD_STATE' ");
            sqlBuilder.append("		   AND trim(osv.value) = sac.state_id) statename, ");


            // sqlBuilder.append("		   app.name statename, ");
            sqlBuilder.append("		   sac.state_id stateid, ");
            sqlBuilder.append("		   sac.owner_id ownerid, ");
            sqlBuilder.append("		   sac.owner_type ownertype, ");
            sqlBuilder.append("		   sac.prod_offer_id prodOfferid, ");
            sqlBuilder.append("		   current_quantity quantity ");
            sqlBuilder.append("	  FROM bccs_im.stock_total sac, ");
            sqlBuilder.append("		   bccs_im.shop sh, ");
            sqlBuilder.append("		   bccs_im.product_offering sm ");

            sqlBuilder.append("	 WHERE     1 = 1 ");
//            sqlBuilder.append("		   AND sac.state_id = to_number(app.VALUE) ");
            sqlBuilder.append("		   AND sac.prod_offer_id = sm.prod_offer_id ");
            sqlBuilder.append("		   AND sac.owner_type = 1 ");
            sqlBuilder.append("		   AND sac.owner_id = sh.shop_id ");
            sqlBuilder.append("		   AND sh.status = 1 ");
            sqlBuilder.append("		   AND sac.status = 1 ");
            if (stateId != null) {
                sqlBuilder.append("		   AND sac.state_id = #stateId ");
            }
            sqlBuilder.append("		   AND sm.product_offer_type_id IN (10, 11) ");//mat hang phu kien
            sqlBuilder.append("		   AND sm.code LIKE 'BH_%' ");
            if (ownerId != null) {
                sqlBuilder.append("		   AND sh.shop_id = #ownerId ");
            }
        }
        if (ownerType == null) {
            sqlBuilder.append(" 			UNION ALL ");
        }
        if (ownerType == null || Const.OWNER_TYPE.STAFF_LONG.equals(ownerType)) {
            sqlBuilder.append("	SELECT st.staff_code ownercode, ");
            sqlBuilder.append("		   st.name ownername, ");
            sqlBuilder.append("		   sm.code prodOffercode, ");
            sqlBuilder.append("		   sm.name prodOffername, ");
            sqlBuilder.append("		   app.name statename, ");
            sqlBuilder.append("		   shs.state_id stateid, ");
            sqlBuilder.append("		   shs.owner_id ownerid, ");
            sqlBuilder.append("		   shs.owner_type ownertype, ");
            sqlBuilder.append("		   shs.prod_offer_id prodOfferid, ");
            sqlBuilder.append("		   COUNT (*) quantity ");
            sqlBuilder.append("	  FROM bccs_im.stock_handset shs, ");
            sqlBuilder.append("		   bccs_im.staff st, ");
            sqlBuilder.append("		   bccs_im.product_offering sm, ");
            sqlBuilder.append("		   (SELECT osv.name, trim(osv.VALUE) value ");
            sqlBuilder.append("			  FROM bccs_im.option_set os, bccs_im.option_set_value osv ");
            sqlBuilder.append("			 WHERE os.id = osv.option_set_id AND os.code = 'GOOD_STATE') app ");
            sqlBuilder.append("	 WHERE     1 = 1 ");
            sqlBuilder.append("		   AND to_char(shs.state_id) = app.VALUE ");
            sqlBuilder.append("		   AND shs.prod_offer_id = sm.prod_offer_id ");
            sqlBuilder.append("		   AND shs.owner_type = 2 ");
            sqlBuilder.append("		   AND shs.owner_id = st.staff_id ");
            sqlBuilder.append("		   AND st.status = 1 ");
            sqlBuilder.append("		   AND shs.status = 1 ");
            sqlBuilder.append("		   AND sm.code LIKE 'BH_%' ");
            if (stateId != null) {
                sqlBuilder.append("		   AND shs.state_id = #stateId ");
            }
            if (!DataUtil.isNullOrEmpty(stockModelCode)) {
                sqlBuilder.append("		   AND sm.code LIKE #stockModelCode ");
            }
            if (!DataUtil.isNullOrEmpty(stockModelName)) {
                sqlBuilder.append("		   AND lower(sm.name) LIKE #stockModelName ");
            }
            if (ownerId != null) {
                sqlBuilder.append("		   AND st.staff_id = #ownerId ");
            }
            sqlBuilder.append("	GROUP BY st.staff_code, ");
            sqlBuilder.append("			 st.name, ");
            sqlBuilder.append("			 sm.code, ");
            sqlBuilder.append("			 sm.name, ");
            sqlBuilder.append("			 shs.state_id, ");
            sqlBuilder.append("			 shs.owner_id, ");
            sqlBuilder.append("			 shs.owner_type, ");
            sqlBuilder.append("			 shs.prod_offer_id, ");
            sqlBuilder.append("			 app.name ");
            sqlBuilder.append("	UNION ALL ");
            sqlBuilder.append("	SELECT st.staff_code ownercode, ");
            sqlBuilder.append("		   st.name ownername, ");
            sqlBuilder.append("		   sm.code prodOffercode, ");
            sqlBuilder.append("		   sm.name prodOffername, ");
            sqlBuilder.append("		   (SELECT   osv.name FROM   bccs_im.option_set os, bccs_im.option_set_value osv ");
            sqlBuilder.append("		   WHERE   os.id = osv.option_set_id AND os.code = 'GOOD_STATE' ");
            sqlBuilder.append("		   AND trim(osv.value) = sac.state_id) statename, ");
//            sqlBuilder.append("		   app.name statename, ");
            sqlBuilder.append("		   sac.state_id stateid, ");
            sqlBuilder.append("		   sac.owner_id ownerid, ");
            sqlBuilder.append("		   sac.owner_type ownertype, ");
            sqlBuilder.append("		   sac.prod_offer_id prodOfferid, ");
            sqlBuilder.append("		   current_quantity quantity ");
            sqlBuilder.append("	  FROM bccs_im.stock_total sac, ");
            sqlBuilder.append("		   bccs_im.staff st, ");
            sqlBuilder.append("		   bccs_im.product_offering sm ");

            sqlBuilder.append("	 WHERE     1 = 1 ");
            //sqlBuilder.append("		   and sac.state_id = to_number(app.VALUE) ");
            sqlBuilder.append("		   AND sac.prod_offer_id = sm.prod_offer_id ");
            sqlBuilder.append("		   AND sac.owner_type = 2 ");
            sqlBuilder.append("		   AND sac.owner_id = st.staff_id ");
            sqlBuilder.append("		   AND st.status = 1 ");
            sqlBuilder.append("		   AND sac.status = 1 ");
            if (stateId != null) {
                sqlBuilder.append("		   AND sac.state_id = #stateId ");
            }
            sqlBuilder.append("		   AND sm.product_offer_type_id IN (10, 11) ");
            sqlBuilder.append("		   AND sm.code LIKE 'BH_%' ");
            if (!DataUtil.isNullOrEmpty(stockModelCode)) {
                sqlBuilder.append("		   AND sm.code LIKE #stockModelCode ");
            }
            if (!DataUtil.isNullOrEmpty(stockModelName)) {
                sqlBuilder.append("		   AND lower(sm.name) LIKE #stockModelName ");
            }
            if (ownerId != null) {
                sqlBuilder.append("		   AND st.staff_id = #ownerId ");
            }
        }
        Query query = em.createNativeQuery(sqlBuilder.toString());

        if (stateId != null) {
            query.setParameter("stateId", stateId);
        }
        if (!DataUtil.isNullOrEmpty(DataUtil.safeToString(stockModelCode).trim())) {
            query.setParameter("stockModelCode", "%" + DataUtil.safeToString(stockModelCode).trim() + "%");
        }
        if (!DataUtil.isNullOrEmpty(DataUtil.safeToString(stockModelName).trim())) {
            query.setParameter("stockModelName", "%" + DataUtil.safeToString(stockModelName).trim().toLowerCase() + "%");
        }
        if (ownerId != null) {
            query.setParameter("ownerId", ownerId);
        }
        List<Object[]> results = query.getResultList();
        List<StockManagementForWarranty> lsStockLogs = Lists.newArrayList();
        if (!DataUtil.isNullOrEmpty(results)) {
            int index;
            StockManagementForWarranty warrantyStockLog;
            for (Object[] result : results) {
                index = 0;
                warrantyStockLog = new StockManagementForWarranty();
                warrantyStockLog.setOwnerCode(DataUtil.safeToString(result[index++]));
                warrantyStockLog.setOwnerName(DataUtil.safeToString(result[index++]));
                warrantyStockLog.setStockModelCode(DataUtil.safeToString(result[index++]));
                warrantyStockLog.setStockModelName(DataUtil.safeToString(result[index++]));
                warrantyStockLog.setStateName(DataUtil.safeToString(result[index++]));
                warrantyStockLog.setStateId(DataUtil.safeToLong(result[index++]));
                warrantyStockLog.setOwnerId(DataUtil.safeToLong(result[index++]));
                warrantyStockLog.setOwnerType(DataUtil.safeToLong(result[index++]));
                warrantyStockLog.setStockModelId(DataUtil.safeToLong(result[index++]));
                warrantyStockLog.setQuantity(DataUtil.safeToLong(result[index]));
                lsStockLogs.add(warrantyStockLog);
            }
        }
        return lsStockLogs;
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveLog(String resultCall, String responseCode, String description, String methodName, String paramsInput, Date startDate, Long stockTransId) throws Exception {

        Date endDate = DbUtil.getSysDate(em);
        Long duration = endDate.getTime() - startDate.getTime();

        WarrantyStockLog warrantyStockLog = new WarrantyStockLog();
        Long logId = getSequence(em, "warranty_stock_log_seq");
        warrantyStockLog.setId(logId);
        warrantyStockLog.setResponseCode(responseCode);
        warrantyStockLog.setMethodName(methodName);

        if (paramsInput != null && !"".equals(paramsInput)) {
            if (paramsInput.length() > 4000) {
                paramsInput = paramsInput.substring(1, 3000) + "...";
            }
            warrantyStockLog.setParamsInput(paramsInput);
        }

        if (description != null && !"".equals(description)) {
            if (description.length() > 4000) {
                description = description.substring(1, 3000) + "...";
            }
            warrantyStockLog.setDescription(description);
        }

        if (resultCall != null && !"".equals(resultCall)) {
            if (resultCall.length() > 500) {
                resultCall = resultCall.substring(1, 300) + "...";
            }
            warrantyStockLog.setResultCall(resultCall);
        }

        warrantyStockLog.setIpClient(IMServiceUtil.getIpClient());
        warrantyStockLog.setStartDate(startDate);
        warrantyStockLog.setEndDate(endDate);
        warrantyStockLog.setDuration(DataUtil.safeToString(duration));
        warrantyStockLog.setNodeWebservice(IMServiceUtil.getIpLocal() + ":" + IMServiceUtil.getPortLocal());
        saveWarrantyStockLog(warrantyStockLog);
    }

    @Transactional(rollbackFor = Exception.class)
    private int saveWarrantyStockLog(WarrantyStockLog warrantyStockLog) {
        String sqlInsert = " insert into WARRANTY_STOCK_LOG (id,node_webservice,method_name,params_input,response_code,description,result_call,start_date,end_date,ip_client,duration) ";
        sqlInsert += " values(warranty_stock_log_seq.NEXTVAL,#node_webservice,:#method_name,#params_input,#response_code,#description,#result_call,#start_date,#end_date,#ip_client,#duration) ";
        Query query = em.createNativeQuery(sqlInsert);
        query.setParameter("node_webservice", warrantyStockLog.getNodeWebservice());
        query.setParameter("method_name", warrantyStockLog.getMethodName());
        query.setParameter("params_input", warrantyStockLog.getParamsInput());
        query.setParameter("response_code", warrantyStockLog.getResponseCode());
        query.setParameter("description", warrantyStockLog.getDescription());
        query.setParameter("result_call", warrantyStockLog.getResultCall());
        query.setParameter("start_date", warrantyStockLog.getStartDate());
        query.setParameter("end_date", warrantyStockLog.getEndDate());
        query.setParameter("ip_client", warrantyStockLog.getIpClient());
        query.setParameter("duration", warrantyStockLog.getDuration());
        return query.executeUpdate();
    }

    @Override
    public void doPrepareData(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, FlagStockDTO flagStockDTO) throws LogicException, Exception {

    }

    @Override
    public void doValidate(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, List<StockTransDetailDTO> lstStockTransDetail) throws LogicException, Exception {

    }


}
