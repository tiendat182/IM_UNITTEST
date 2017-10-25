package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.controller.SolrController;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.dto.ws.ProductOfferingSM;
import com.viettel.bccs.inventory.dto.ws.StockTransSerialSM;
import com.viettel.bccs.inventory.model.StockNumber;
import com.viettel.bccs.inventory.repo.*;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.RequiredRoleMap;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.fw.service.BaseServiceImpl;
import com.viettel.voffice.autosign.Ver3AutoSign;
import com.viettel.voffice.autosign.ws.StaffBO;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.jws.WebParam;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.xml.crypto.Data;
import java.math.BigInteger;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by DungPT16 on 7/14/2016.
 */
@Service
public class SmartphoneServiceImpl extends BaseServiceImpl implements SmartphoneService {
    public static final Logger logger = Logger.getLogger(SmartphoneService.class);
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @Autowired
    private StockTransActionService stockTransActionService;
    @Autowired
    private StockTransService stockTransService;
    @Autowired
    private StaffService staffService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private ShopRepo shopRepo;
    @Autowired
    private StockTransSerialService stockTransSerialService;
    @Autowired
    private StockTransSerialRepo stockTransSerialRepo;
    @Autowired
    private StockTransDetailRepo stockTransDetailRepo;
    @Autowired
    private ProductOfferingService productOfferingService;
    @Autowired
    private ProductOfferingRepo productOfferingRepo;
    @Autowired
    private OptionSetValueService optionSetValueService;
    @Autowired
    private ProductOfferTypeService productOfferTypeService;
    @Autowired
    private StockTotalRepo stockTotalRepo;
    @Autowired
    private StockOrderRepo stockOrderRepo;
    @Autowired
    private StockOrderService stockOrderService;
    @Autowired
    private StockOrderDetailRepo stockOrderDetailRepo;
    @Autowired
    private ReasonService reasonService;
    @Autowired
    private ProductOfferPriceService productOfferPriceService;
    @Autowired
    private SmartphoneTransactionService smartphoneTransactionService;
    @Autowired
    private BrasIppoolService brasIppoolService;
    @Autowired
    private StockNumberRepo stockNumberRepo;
    @Autowired
    private SignFlowService signFlowService;
    @Autowired
    private SignFlowDetailService signFlowDetailService;
    @Autowired
    private ExecuteStockTransService executeStockTransService;
    @Autowired
    private Ver3AutoSign ver3AutoSign;
    @Autowired
    private VofficeRoleService vofficeRoleService;
    @Autowired
    private StockNumberService stockNumberService;


    /**
     * Nhân viên xác nhận hàng.
     *
     * @param staffId
     * @param expNoteCode
     * @param stockTransId
     * @param createExpDate
     * @param impNoteCode
     * @return
     * @throws LogicException
     * @throws Exception
     */
    @Override
    public ImResult confirmNote(Long staffId, String expNoteCode, String stockTransId, Date createExpDate, String impNoteCode) throws LogicException, Exception {
        ImResult imResult = new ImResult();
        StockTransActionDTO stockTransActionSearch = new StockTransActionDTO();
        stockTransActionSearch.setStockTransId(DataUtil.safeToLong(stockTransId));
        stockTransActionSearch.setStatus(Const.STOCK_TRANS_STATUS.EXPORT_NOTE);
        stockTransActionSearch.setCreateDatetime(createExpDate);
        StockTransActionDTO stockTransAction = stockTransActionService.findStockTransActionDTO(stockTransActionSearch);

        if (stockTransAction == null) {
            imResult.setDescription(getTextParam("sp.stock.trans.action.not.found", expNoteCode));
            return imResult;
        }

        StaffDTO staffDTO = staffService.findOne(staffId);

        if (staffDTO == null) {
            imResult.setDescription(getTextParam("common.valid.staff", DataUtil.safeToString(staffId)));
            return imResult;
        }

        StockTransDTO updateStockTrans = stockTransService.findOne(stockTransAction.getStockTransId());
        if (updateStockTrans == null) {
            imResult.setDescription(getTextParam("sp.stock.trans.not.found", expNoteCode));
            return imResult;
        }

        if (!DataUtil.safeEqual(updateStockTrans.getStatus(), Const.STOCK_TRANS_STATUS.EXPORT_ORDER)
                && !DataUtil.safeEqual(updateStockTrans.getStatus(), Const.STOCK_TRANS_STATUS.EXPORT_NOTE)
                && !DataUtil.safeEqual(updateStockTrans.getStatus(), Const.STOCK_TRANS_STATUS.EXPORTED)) {
            imResult.setDescription(getTextParam("sp.stock.trans.status.invalid", expNoteCode));
            return imResult;
        }
        if (DataUtil.isNullOrEmpty(impNoteCode)) {
            imResult.setDescription(getText("mn.stock.impNote.invalid"));
            return imResult;
        }
        if (impNoteCode.getBytes("UTF-8").length > 50 || !impNoteCode.matches("^[A-Za-z0-9_-]+")) {
            imResult.setDescription(getText("code.import.note.valid"));
            return imResult;
        }
        // G�?i hàm nhân viên xác nhận nhập hàng chung với web.
        try {
            List<Long> stockTransActionIds = Lists.newArrayList();
            stockTransActionIds.add(stockTransAction.getStockTransActionId());
            List<StockTransFullDTO> stockTransDetails = DataUtil.defaultIfNull(stockTransService.searchStockTransDetail(stockTransActionIds), new ArrayList<StockTransFullDTO>());
            List<StockTransDetailDTO> lsStockTransDetail = Lists.newArrayList();
            StockTransSerialDTO stockTransSerialDTO = new StockTransSerialDTO();
            for (StockTransFullDTO stockTransFullDTO : stockTransDetails) {
                StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
                stockTransDetailDTO.setStockTransDetailId(stockTransFullDTO.getStockTransDetailId());
                stockTransDetailDTO.setProdOfferId(stockTransFullDTO.getProdOfferId());
                stockTransDetailDTO.setStateId(stockTransFullDTO.getStateId());
                stockTransDetailDTO.setQuantity(stockTransFullDTO.getQuantity());
                stockTransDetailDTO.setProdOfferTypeId(stockTransFullDTO.getProductOfferTypeId());

                if ((Const.PRODUCT_OFFERING.CHECK_SERIAL.toString()).equals(stockTransFullDTO.getCheckSerial())) {
                    stockTransSerialDTO.setStockTransDetailId(stockTransFullDTO.getStockTransDetailId());
                    List<StockTransSerialDTO> lstSerial = stockTransSerialService.findStockTransSerialByDTO(stockTransSerialDTO);
                    stockTransDetailDTO.setLstStockTransSerial(lstSerial);
                    stockTransDetailDTO.setTableName(stockTransFullDTO.getTableName());
                }
                lsStockTransDetail.add(stockTransDetailDTO);
            }
            updateStockTrans.setStockTransActionId(stockTransAction.getStockTransActionId());
            updateStockTrans.setImportReasonId(408L);
            // Stock_trans_action
            StockTransActionDTO action = new StockTransActionDTO();
            action.setActionCode(impNoteCode.trim());
            action.setStockTransId(stockTransAction.getStockTransId());
            action.setActionStaffId(staffDTO.getStaffId());
            action.setCreateUser(staffDTO.getStaffCode());

            BaseMessageStockTrans message = executeStockTransService.executeStockTrans(Const.STOCK_TRANS.EXPORT, Const.STOCK_TRANS_TYPE.STAFF_IMP,
                    updateStockTrans, action, lsStockTransDetail, null);
            if (DataUtil.isNullObject(message) || DataUtil.isNullOrEmpty(message.getErrorCode())) {
                imResult.setResult(true);
            } else {
                imResult.setResult(false);
                imResult.setDescription(getTextParam(message.getKeyMsg(), message.getParamsMsg()));
                return imResult;
            }
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            imResult.setResult(false);
            imResult.setDescription(ex.getMessage());
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            imResult.setResult(false);
            imResult.setDescription(getText("common.error.happen"));
        }
        return imResult;
    }

    /**
     * Lấy danh sách phiếu xuất
     *
     * @param toOwnerCode
     * @return
     * @throws LogicException
     * @throws Exception
     */
    @Override
    public List<StockTransActionDTO> getLstStockTrans(String toOwnerCode) throws LogicException, Exception {
        if (DataUtil.isNullOrEmpty(toOwnerCode)) {
            throw new LogicException("", "mn.stock.track.staff.code.not.blank");
        }
        StaffDTO staffDTO = staffService.getStaffByStaffCode(toOwnerCode.trim());
        if (DataUtil.isNullObject(staffDTO)) {
            throw new LogicException("", "common.valid.staff", toOwnerCode);
        }
        List<StockTransActionDTO> result = stockTransActionService.getStockTransActionByToOwnerId(staffDTO.getStaffId(), Const.OWNER_TYPE.STAFF_LONG);
        return result;
    }

    /**
     * Lấy chi tiết hàng hóa trong phiếu xuất
     *
     * @param stockTransId
     * @return
     * @throws LogicException
     * @throws Exception
     */
    @Override
    public List<StockTransDetailDTO> getLstStockTransDetail(Long stockTransId) throws LogicException, Exception {
        if (DataUtil.isNullObject(stockTransId)) {
            throw new LogicException("", "stock.validate.change.damaged.data.isNull");
        }
        return stockTransDetailRepo.getStockTransDetailByStockTransId(stockTransId, Const.OWNER_TYPE.STAFF_LONG);
    }

    /**
     * Lấy chi tiết serial của hàng hóa trong phiếu xuất
     *
     * @param stockTransId
     * @param prodOfferId
     * @return
     * @throws LogicException
     * @throws Exception
     */
    @Override
    public List<StockTransSerialDTO> getLstStockTransSerial(Long stockTransId, Long prodOfferId) throws LogicException, Exception {
        if (DataUtil.isNullObject(stockTransId)) {
            throw new LogicException("", "sp.getStockTransSerial.stockTransId.isnull");
        }
        if (DataUtil.isNullObject(prodOfferId)) {
            throw new LogicException("", "sp.getStockTransSerial.prodOfferId.isnull");
        }
        ProductOfferingDTO productOfferingDTO = productOfferingService.findOne(prodOfferId);
        if (DataUtil.isNullObject(productOfferingDTO) || !DataUtil.safeEqual(productOfferingDTO.getStatus(), Const.STATUS_ACTIVE)) {
            throw new LogicException("", "validate.stock.inspect.productOffering.notFound");
        }
        return stockTransSerialService.getStockTransSerialByStockTransId(stockTransId, prodOfferId, Const.OWNER_TYPE.STAFF_LONG);
    }

    /**
     * Danh sách kho duoc phep dau noi cua CTV/Nhan vien
     *
     * @param staffCode
     * @return
     * @throws LogicException
     * @throws Exception
     */
    @Override
    public List<SmartPhoneDTO> getListStockByStaffCode(String staffCode) throws LogicException, Exception {
        List<SmartPhoneDTO> listSmartPhoneBO = Lists.newArrayList();
        if (DataUtil.isNullOrEmpty(staffCode)) {
            throw new LogicException("", "mn.stock.track.staff.code.not.blank");
        }
        if (DataUtil.isNullOrEmpty(staffCode)) {
            return listSmartPhoneBO;
        }
        StaffDTO staffDTO = staffService.getStaffByStaffCode(staffCode.trim());
        if (DataUtil.isNullObject(staffDTO)) {
            return listSmartPhoneBO;
        }
        boolean isCTV = shopService.checkChannelIsCollAndPointOfSale(staffDTO.getChannelTypeId());
        // Neu la CTV lay trong cau hinh option_set
        if (isCTV) {
            List<OptionSetValueDTO> optionSetValueDTOs = optionSetValueService.getLsOptionSetValueByCode(Const.OPTION_SET.CTV_STOCK_ISDN_MBCCS);
            if (!DataUtil.isNullOrEmpty(optionSetValueDTOs)) {
                for (OptionSetValueDTO optionSetValueDTO : optionSetValueDTOs) {
                    SmartPhoneDTO smartPhoneDTO = new SmartPhoneDTO();
                    smartPhoneDTO.setOwnerId(DataUtil.safeToLong(optionSetValueDTO.getValue()));
                    smartPhoneDTO.setOwnerType(Const.OWNER_TYPE.SHOP_LONG);
                    smartPhoneDTO.setOwnerCode(optionSetValueDTO.getName());
                    smartPhoneDTO.setOwnerName(optionSetValueDTO.getDescription());
                    listSmartPhoneBO.add(smartPhoneDTO);
                }
            }
        } else {//Neu la NV thi lay tat ca cac kho duoc dau noi cua NV
            listSmartPhoneBO = shopRepo.getListNVStockIsdnMbccs(staffDTO);
        }
        return listSmartPhoneBO;
    }

    /**
     * Tra cứu ISDN
     */
    @Override
    public List<SmartPhoneDTO> getListIsdnMbccsByRangeIsdnOwnerId(Long productOfferTypeId, String fromIsdn, String toIsdn, Long ownerId, Long ownerType, Long status, String staffCode) throws LogicException, Exception {
        List<SmartPhoneDTO> list = Lists.newArrayList();
        if (DataUtil.isNullOrZero(productOfferTypeId) || DataUtil.isNullOrEmpty(fromIsdn) || DataUtil.isNullOrEmpty(toIsdn)) {
            throw new LogicException("", "sp.approve.stockOrder.parametter.isNull");
        }
        if (!DataUtil.isNumber(fromIsdn)) {
            throw new LogicException("", "sp.fromIsdn.negative");
        }
        if (!DataUtil.isNumber(toIsdn)) {
            throw new LogicException("", "sp.toIsdn.negative");
        }
        BigInteger fromIsdnNum = new BigInteger(fromIsdn);
        BigInteger toIsdnNum = new BigInteger(toIsdn);

        if (fromIsdnNum.compareTo(toIsdnNum) > 0) {
            throw new LogicException("", "sp.fromIsdn.less.toIsdn");
        }
        BigInteger quantityBig = toIsdnNum.subtract(fromIsdnNum);
        if (quantityBig.compareTo(new BigInteger("0")) < 0 || quantityBig.compareTo(new BigInteger("500000")) > 0) {
            throw new LogicException("", "sp.fromIsdn.toIsdn.valid.range");
        }
        ProductOfferTypeDTO productOfferTypeDTO = productOfferTypeService.findOne(productOfferTypeId);
        if (DataUtil.isNullObject(productOfferTypeDTO) || !DataUtil.safeEqual(Const.STATUS_ACTIVE, productOfferTypeDTO.getStatus())) {
            throw new LogicException("", "validate.stock.inspect.productOfferType.notFound");
        }
        if (DataUtil.isNullOrEmpty(staffCode)) {
            throw new LogicException("", "mn.stock.track.staff.code.not.blank");
        }
        StaffDTO staffDTO = staffService.getStaffByStaffCode(staffCode.trim());
        if (DataUtil.isNullObject(staffDTO)) {
            return list;
        }
        if (!DataUtil.isNullOrZero(ownerId) && !DataUtil.isNullOrZero(ownerType)) {
            if (DataUtil.safeEqual(ownerType, Const.OWNER_TYPE.SHOP_LONG)) {
                ShopDTO shopDTO = shopService.findOne(ownerId);
                if (DataUtil.isNullObject(shopDTO) || !DataUtil.safeEqual(shopDTO.getStatus(), Const.STATUS_ACTIVE)) {
                    throw new LogicException("", "sp.ownerId.notFoundDB");
                }
            } else if (DataUtil.safeEqual(ownerType, Const.OWNER_TYPE.STAFF_LONG)) {
                StaffDTO staff = staffService.findOne(ownerId);
                if (DataUtil.isNullObject(staff) || !DataUtil.safeEqual(staff.getStatus(), Const.STATUS_ACTIVE)) {
                    throw new LogicException("", "sp.ownerId.notFoundDB");
                }
            } else {
                throw new LogicException("", "sp.ownerType.invalid");
            }
        }
        boolean isCTV = shopService.checkChannelIsCollAndPointOfSale(staffDTO.getChannelTypeId());
        list = stockTransSerialRepo.getListIsdnMbccsByRangeIsdnOwnerId(productOfferTypeId, fromIsdn, toIsdn,
                ownerId, ownerType, status, isCTV, staffDTO);

        return list;
    }

    /**
     * Lay danh sach hang trong kho de tra hang
     *
     * @param staffCode
     * @param prodOfferId
     * @param prodOfferTypeId
     * @return
     * @throws LogicException
     * @throws Exception
     */
    @Override
    public List<ProductOfferingDTO> getStockStaffDetail(String staffCode, Long prodOfferId, Long prodOfferTypeId) throws LogicException, Exception {
        List<ProductOfferingDTO> result = Lists.newArrayList();
        if (DataUtil.isNullOrEmpty(staffCode)) {
            throw new LogicException("", "sp.staff.code.is.null");
        }
        StaffDTO staffDTO = staffService.getStaffByStaffCode(staffCode.trim());
        if (DataUtil.isNullObject(staffDTO)) {
            throw new LogicException("", "sp.staff.not.found");
        }
        List<ProductOfferingDTO> listProduct = stockTotalRepo.getProductInStockTotal(Const.OWNER_TYPE.STAFF_LONG, staffDTO.getStaffId(), prodOfferId, prodOfferTypeId);
        if (!DataUtil.isNullOrEmpty(listProduct)) {
            for (ProductOfferingDTO productOfferingDTO : listProduct) {
                ProductOfferTypeDTO productOfferTypeDTO = productOfferTypeService.findOne(productOfferingDTO.getProductOfferTypeId());
                if (DataUtil.isNullObject(productOfferTypeDTO) || !DataUtil.safeEqual(Const.STATUS_ACTIVE, productOfferTypeDTO.getStatus())) {
                    continue;
                }
                productOfferingDTO.setProductOfferTypeIdName(productOfferTypeDTO.getName());
                if (!DataUtil.safeEqual(Const.PRODUCT_OFFERING.CHECK_SERIAL, productOfferingDTO.getCheckSerial())) {
                    result.add(productOfferingDTO);
                    continue;
                }
                List<StockTransSerialDTO> listProductSerial = stockTransSerialRepo.getGatherSerial(Const.OWNER_TYPE.STAFF_LONG,
                        staffDTO.getStaffId(), productOfferTypeDTO.getTableName(), productOfferingDTO.getProductOfferingId(), productOfferingDTO.getStateId());
                if (!DataUtil.isNullOrEmpty(listProductSerial)) {
                    productOfferingDTO.setListStockTransSerialDTOs(listProductSerial);
                    result.add(productOfferingDTO);
                }
            }
        }
        return result;
    }

    @Override
    public BaseMessage staffExportStockToShop(String staffCode, List<ProductOfferingSM> productOfferingDTOs, boolean isRoleStockNumShop) throws LogicException, Exception {
        BaseMessage result;
        // Validate du lieu dau vao
        if (DataUtil.isNullOrEmpty(staffCode)) {
            return new BaseMessage(false, "ERR_IM", "sp.staff.code.is.null");
        }
        StaffDTO staffDTO = staffService.getStaffByStaffCode(staffCode.trim());
        if (DataUtil.isNullObject(staffDTO)) {
            return new BaseMessage(false, "ERR_IM", "sp.staff.not.found");
        }
        ShopDTO shopDTO = shopService.findOne(staffDTO.getShopId());
        if (DataUtil.isNullObject(shopDTO)) {
            return new BaseMessage(false, "ERR_IM", "sp.shop.not.found");
        }
        if (DataUtil.isNullOrEmpty(productOfferingDTOs)) {
            return new BaseMessage(false, "ERR_IM", "sp.staffExportStockToShop.listProduct.isNull");
        }
        List<String> listProductCode = Lists.newArrayList();
        List<StockTransDetailDTO> listTransDetailDTOs = Lists.newArrayList();
        for (ProductOfferingSM productOfferingSM : productOfferingDTOs) {
            if (DataUtil.isNullObject(productOfferingSM) || DataUtil.isNullOrZero(productOfferingSM.getProductOfferingId())) {
                return new BaseMessage(false, "ERR_IM", "sp.staffExportStockToShop.listProduct.id.isNull");
            }
            if (DataUtil.isNullOrZero(productOfferingSM.getStateId())) {
                return new BaseMessage(false, "ERR_IM", "sp.staffExportStockToShop.listProduct.stateId.isNull");
            }
            if (!DataUtil.safeEqual(productOfferingSM.getStateId(), Const.GOODS_STATE.NEW)
                    && !DataUtil.safeEqual(productOfferingSM.getStateId(), Const.GOODS_STATE.OLD)
                    && !DataUtil.safeEqual(productOfferingSM.getStateId(), Const.GOODS_STATE.BROKEN)
                    && !DataUtil.safeEqual(productOfferingSM.getStateId(), Const.GOODS_STATE.RETRIVE)) {
                return new BaseMessage(false, "ERR_IM", "sp.staffExportStockToShop.listProduct.stateId.invalid");
            }
            if (DataUtil.isNullOrZero(productOfferingSM.getQuantity())) {
                return new BaseMessage(false, "ERR_IM", "sp.staffExportStockToShop.listProduct.quantity.isNull", DataUtil.safeToString(productOfferingSM.getStateId()));
            }
            if (productOfferingSM.getQuantity().longValue() < 0) {
                return new StockCollaboratorDTO(false, "ERR_IM", "sp.exportStockCollaborator.stockTransDetail.quantity.positive");
            }
            ProductOfferingDTO productOfferingDB = productOfferingService.findOne(productOfferingSM.getProductOfferingId());
            if (DataUtil.isNullObject(productOfferingDB) || !DataUtil.safeEqual(Const.STATUS_ACTIVE, productOfferingDB.getStatus())) {
                return new BaseMessage(false, "ERR_IM", "process.stock.detail.serial.require.offering");
            }
            // khoi tao doi tuong stockTransDetail
            StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
            stockTransDetailDTO.setProdOfferId(productOfferingSM.getProductOfferingId());
            stockTransDetailDTO.setStateId(productOfferingSM.getStateId());
            stockTransDetailDTO.setQuantity(productOfferingSM.getQuantity());

            if (listProductCode.contains(productOfferingSM.getCode() + productOfferingSM.getStateId())) {
                return new BaseMessage(false, "ERR_IM", "list.product.validate.duplicate");
            }
            listProductCode.add(productOfferingSM.getCode() + productOfferingSM.getStateId());

            ProductOfferTypeDTO productOfferTypeDTO = productOfferTypeService.findOne(productOfferingDB.getProductOfferTypeId());
            if (DataUtil.isNullObject(productOfferTypeDTO) || !DataUtil.safeEqual(Const.STATUS_ACTIVE, productOfferTypeDTO.getStatus())) {
                return new BaseMessage(false, "ERR_IM", "validate.stock.inspect.productOfferType.notFound");
            }
            stockTransDetailDTO.setTableName(productOfferTypeDTO.getTableName());
            stockTransDetailDTO.setProdOfferTypeId(productOfferTypeDTO.getProductOfferTypeId());
            if (DataUtil.safeEqual(Const.PRODUCT_OFFERING.CHECK_SERIAL, productOfferingDB.getCheckSerial())) {
                if (DataUtil.isNullOrEmpty(productOfferingSM.getListStockTransSerialDTOs())) {
                    return new BaseMessage(false, "ERR_IM", "sp.staffExportStockToShop.listProduct.serial.isNull", DataUtil.safeToString(productOfferingDB.getName()));
                }
                List<StockTransSerialDTO> lstStockTransSerial = Lists.newArrayList();
                for (StockTransSerialSM stockTransSerialSM : productOfferingSM.getListStockTransSerialDTOs()) {
                    if (DataUtil.isNullOrEmpty(stockTransSerialSM.getFromSerial())) {
                        return new BaseMessage(false, "ERR_IM", "sp.staffExportStockToShop.listProduct.fromSerial.isNull");
                    }
                    if (DataUtil.isNullOrEmpty(stockTransSerialSM.getToSerial())) {
                        return new BaseMessage(false, "ERR_IM", "sp.staffExportStockToShop.listProduct.toSerial.isNull");
                    }

                    if (stockTransSerialSM.getFromSerial().trim().length() != stockTransSerialSM.getToSerial().trim().length()) {
                        return new StockCollaboratorDTO(false, "ERR_IM", "mn.stock.partner.length.fromSerial.toSerial.valid");
                    }
                    Long quantity;
                    if (DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_HANDSET, productOfferTypeDTO.getProductOfferTypeId())) {
                        if (!DataUtil.safeEqual(stockTransSerialSM.getFromSerial().trim(), stockTransSerialSM.getToSerial().trim())) {
                            return new StockCollaboratorDTO(false, "ERR_IM", "mn.divide.serial.infile.equal");
                        }
                        for (StockTransSerialDTO serialDetail : lstStockTransSerial) {
                            if (stockTransSerialSM.getFromSerial().trim().equalsIgnoreCase(serialDetail.getFromSerial().trim())
                                    && stockTransSerialSM.getToSerial().trim().equalsIgnoreCase(serialDetail.getToSerial().trim())) {
                                return new StockCollaboratorDTO(false, "ERR_IM", "mn.stock.partner.range.duplidate", stockTransSerialSM.getFromSerial(), stockTransSerialSM.getToSerial());
                            }
                        }
                        quantity = 1L;
                    } else {
                        if (!DataUtil.validateStringByPattern(stockTransSerialSM.getFromSerial(), Const.REGEX.NUMBER_REGEX)
                                || !DataUtil.validateStringByPattern(stockTransSerialSM.getToSerial(), Const.REGEX.NUMBER_REGEX)) {
                            return new StockCollaboratorDTO(false, "ERR_IM", "mn.stock.partner.serial.valid.format", productOfferTypeDTO.getName());
                        }
                        BigInteger fromSerialNum = new BigInteger(stockTransSerialSM.getFromSerial());
                        BigInteger toSerialNum = new BigInteger(stockTransSerialSM.getToSerial());
                        BigInteger quantityBig = toSerialNum.subtract(fromSerialNum);
                        if (quantityBig.compareTo(new BigInteger("0")) < 0 || quantityBig.compareTo(new BigInteger("500000")) > 0) {
                            return new StockCollaboratorDTO(false, "ERR_IM", "mn.stock.partner.serial.valid.range");
                        }
                        for (StockTransSerialDTO serialDetail : lstStockTransSerial) {
                            BigInteger fromSerialCurrent = new BigInteger(serialDetail.getFromSerial());
                            BigInteger toSerialCurrent = new BigInteger(serialDetail.getToSerial());
                            if (fromSerialNum.compareTo(fromSerialCurrent) >= 0 && fromSerialNum.compareTo(toSerialCurrent) <= 0
                                    || toSerialNum.compareTo(fromSerialCurrent) >= 0 && toSerialNum.compareTo(toSerialCurrent) <= 0) {
                                return new StockCollaboratorDTO(false, "ERR_IM", "mn.stock.partner.range.duplidate", stockTransSerialSM.getFromSerial(), stockTransSerialSM.getToSerial());
                            }
                        }
                        quantity = quantityBig.longValue() + 1;
                    }
                    StockTransSerialDTO stockTransSerialDTO = new StockTransSerialDTO();
                    stockTransSerialDTO.setFromSerial(stockTransSerialSM.getFromSerial());
                    stockTransSerialDTO.setToSerial(stockTransSerialSM.getToSerial());
                    stockTransSerialDTO.setProdOfferId(productOfferingDB.getProductOfferingId());
                    stockTransSerialDTO.setQuantity(quantity);
                    lstStockTransSerial.add(stockTransSerialDTO);
                }
                stockTransDetailDTO.setLstStockTransSerial(lstStockTransSerial);
            }
            listTransDetailDTOs.add(stockTransDetailDTO);
        }

        try {
            StockTransDTO stockTransDTO = new StockTransDTO();
            String transCode = staffService.getTransCode(Const.STOCK_TRANS.TRANS_CODE_PX + "SM", Const.STOCK_TRANS_TYPE.EXPORT, staffDTO);

            if (transCode.equals(Const.STOCK_TRANS.TRANS_CODE_PX)) {
                return new BaseMessage(false, "ERR_IM", "sp.staffExportStockToShop.create.cannot.createExportNote");
            }

            StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
            stockTransActionDTO.setActionCode(transCode);
            stockTransActionDTO.setCreateUser(staffDTO.getStaffCode());
            stockTransActionDTO.setActionStaffId(staffDTO.getStaffId());

            if (DataUtil.isNullOrEmpty(listTransDetailDTOs)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "stock.trans.staff.export.detailRequired");
            }
            for (StockTransDetailDTO detailDTO : listTransDetailDTOs) {
                if (DataUtil.safeEqual(detailDTO.getCheckSerial(), Const.PRODUCT_OFFERING.CHECK_SERIAL)
                        && DataUtil.isNullOrEmpty(detailDTO.getLstSerial())) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "staff.return.stock.valid.serial", detailDTO.getProdOfferName());
                }
            }
            stockTransDTO.setFromOwnerId(staffDTO.getStaffId());
            stockTransDTO.setFromOwnerType(Const.OWNER_TYPE.STAFF_LONG);
            stockTransDTO.setToOwnerId(staffDTO.getShopId());
            stockTransDTO.setToOwnerType(Const.OWNER_TYPE.SHOP_LONG);
            stockTransDTO.setReasonId(Const.REASON_ID.STAFF_EXPORT_STOCK_TO_SHOP); // Xuat hang cho cua hang
            stockTransDTO.setStockTransType(Const.STOCK_TRANS_TYPE.EXPORT);

            stockTransDTO.setFromStock(Const.FROM_STOCK.FROM_STAFF);

            RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
            if (isRoleStockNumShop) {
                List<String> roleStockNumShop = Lists.newArrayList();
                roleStockNumShop.add(Const.PERMISION.ROLE_STOCK_NUM_SHOP);
                requiredRoleMap.setValues(roleStockNumShop);
            }
            result = executeStockTransService.executeStockTrans(Const.STOCK_TRANS.EXPORT, Const.STOCK_TRANS_TYPE.STAFF_EXP,
                    stockTransDTO, stockTransActionDTO, listTransDetailDTOs, requiredRoleMap);
            if (!DataUtil.isNullOrEmpty(result.getErrorCode())) {
                throw new LogicException(result.getErrorCode(), result.getKeyMsg(), result.getParamsMsg());
            }
            result = new BaseMessage(true, "", "export.stock.success");
        } catch (LogicException ex) {
            logger.error("LogicException", ex);
            result = new BaseMessage(false, "ERR_IM", ex.getKeyMsg(), ex.getParamsMsg());
        } catch (Exception ex) {
            logger.error("Exception", ex);
            result = new BaseMessage(false, "ERR_IM", "common.error.happen");
        }
        return result;
    }

    /**
     * Lay don dat hang cua nhan vien + danh sach hang hoa da dat
     *
     * @param staffCode
     * @return
     * @throws LogicException
     * @throws Exception
     */
    @Override
    public List<StockOrderDTO> getListStockOrder(String staffCode) throws LogicException, Exception {
        if (DataUtil.isNullOrEmpty(staffCode)) {
            throw new LogicException("", "sp.staff.code.is.null");
        }
        ShopDTO shopDTO = shopRepo.getShopByStaffCode(staffCode);
        if (DataUtil.isNullObject(shopDTO)) {
            throw new LogicException("", "sp.shop.not.found");
        }
        List<StockOrderDTO> result = Lists.newArrayList();
        List<StockOrderDTO> stockOrderDTOs = stockOrderRepo.getListStockOrder(shopDTO.getShopId());
        if (!DataUtil.isNullOrEmpty(stockOrderDTOs)) {
            for (StockOrderDTO stockOrderDTO : stockOrderDTOs) {
                StaffDTO staffDTO = staffService.findOne(stockOrderDTO.getStaffId());
                if (!DataUtil.isNullObject(staffDTO)) {
                    stockOrderDTO.setStaffCode(staffDTO.getStaffCode());
                    stockOrderDTO.setStaffName(staffDTO.getName());
                }
                result.add(stockOrderDTO);
            }
        }
        return result;
    }

    /**
     * Lay chi tiet hang hoa thuoc don hang
     *
     * @param stockOrderId
     * @return
     * @throws LogicException
     * @throws Exception
     */
    @Override
    public List<StockOrderDetailDTO> getListStockOrderDetail(Long stockOrderId) throws LogicException, Exception {
        if (DataUtil.isNullObject(stockOrderId)) {
            return Lists.newArrayList();
        }
        return stockOrderDetailRepo.getListStockOrderDetail(stockOrderId);
    }

    /**
     * Phe duyet don hang
     *
     * @param shopId
     * @param staffId
     * @param stockOrderCode
     * @param listApprove
     * @return
     * @throws LogicException
     * @throws Exception
     */
    @Override
    public BaseMessage approveOrder(Long shopId, Long staffId, String stockOrderCode, List<StockOrderDetailDTO> listApprove) throws LogicException, Exception {
        BaseMessage result = new BaseMessage();
        try {
            if (DataUtil.isNullOrZero(shopId) || DataUtil.isNullOrZero(staffId) || DataUtil.isNullOrEmpty(stockOrderCode) || DataUtil.isNullOrEmpty(listApprove)) {
                return new BaseMessage(false, "ERR_IM", "sp.approve.stockOrder.parametter.isNull");
            }
            ShopDTO shopDTO = shopService.findOne(shopId);
            if (DataUtil.isNullObject(shopDTO) || !DataUtil.safeEqual(Const.STATUS_ACTIVE, shopDTO.getStatus())) {
                return new BaseMessage(false, "ERR_IM", "shop.validate");
            }
            StaffDTO staffDTO = staffService.findOne(staffId);
            if (DataUtil.isNullObject(staffDTO) || !DataUtil.safeEqual(Const.STATUS_ACTIVE, staffDTO.getStatus())) {
                return new BaseMessage(false, "ERR_IM", "sp.staff.not.found");
            }
            if (!DataUtil.safeEqual(staffDTO.getShopId(), shopDTO.getShopId())) {
                return new BaseMessage(false, "ERR_IM", "validate.stock.inspect.staff.not.shop.userLogin");
            }
            StockOrderDTO stockOrderDTO = stockOrderRepo.getStockOrderByCode(stockOrderCode);
            if (DataUtil.isNullObject(stockOrderDTO)) {
                return new BaseMessage(false, "ERR_IM", "sp.approve.stockOrder.code.notFound");
            }
            if (!DataUtil.safeEqual(stockOrderDTO.getShopId(), shopDTO.getShopId())) {
                return new BaseMessage(false, "ERR_IM", "sp.approve.stockOrder.code.not.map.shop");
            }
            List<Long> listProdOfferId = Lists.newArrayList();
            for (StockOrderDetailDTO stockOrderDetail : listApprove) {
                if (DataUtil.isNullOrZero(stockOrderDetail.getProdOfferId())) {
                    return new BaseMessage(false, "ERR_IM", "sp.approve.stockOrder.prodOfferId.isNull");
                }
                if (DataUtil.isNullOrZero(stockOrderDetail.getQuantityReal())) {
                    return new BaseMessage(false, "ERR_IM", "sp.approve.stockOrder.quantityReal.isNull");
                }
                if (stockOrderDetail.getQuantityReal() < 0) {
                    return new BaseMessage(false, "ERR_IM", "sp.approve.stockOrder.quantityReal.small.zezo");
                }
                ProductOfferingDTO productOfferingDTO = productOfferingService.findOne(stockOrderDetail.getProdOfferId());
                if (DataUtil.isNullObject(productOfferingDTO)
                        || DataUtil.isNullOrEmpty(productOfferingDTO.getStatus())
                        || !DataUtil.safeEqual(productOfferingDTO.getStatus(), Const.STATUS_ACTIVE)) {
                    return new BaseMessage(false, "ERR_IM", "validate.stock.inspect.productOffering.not.exist", DataUtil.safeToString(stockOrderDetail.getProdOfferId()));
                }
                if (listProdOfferId.contains(stockOrderDetail.getProdOfferId())) {
                    return new BaseMessage(false, "ERR_IM", "sp.approve.stockOrder.product.offer.duplicate");
                }
                listProdOfferId.add(stockOrderDetail.getProdOfferId());
            }
            Collections.sort(listApprove, new Comparator<StockOrderDetailDTO>() {
                public int compare(StockOrderDetailDTO o1, StockOrderDetailDTO o2) {
                    return o1.getProdOfferId().compareTo(o2.getProdOfferId());
                }
            });

            List<StockOrderDetailDTO> listOrderDB = stockOrderDetailRepo.getListStockOrderDetail(stockOrderDTO.getStockOrderId());
            if (listApprove.size() != listOrderDB.size()) {
                return new BaseMessage(false, "ERR_IM", "sp.approve.stockOrder.stockOrderDetail.notMap");
            }
            Collections.sort(listOrderDB, new Comparator<StockOrderDetailDTO>() {
                public int compare(StockOrderDetailDTO o1, StockOrderDetailDTO o2) {
                    return o1.getProdOfferId().compareTo(o2.getProdOfferId());
                }
            });
            List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();
            for (int i = 0; i < listApprove.size(); i++) {
                if (!listApprove.get(i).getProdOfferId().equals(listOrderDB.get(i).getProdOfferId())) {
                    return new BaseMessage(false, "ERR_IM", "sp.approve.stockOrder.stockOrderDetail.notMap");
                }
                if (listApprove.get(i).getQuantityReal().compareTo(listOrderDB.get(i).getQuantityOrder()) > 0) {
                    return new BaseMessage(false, "ERR_IM", "sp.approve.stockOrder.quantityApprove.greater.quantity");
                }
                listOrderDB.get(i).setQuantityReal(listApprove.get(i).getQuantityReal());
                StockOrderDetailDTO stockOrderDetailDTO = listApprove.get(i);

                StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
                stockTransDetailDTO.setProdOfferId(stockOrderDetailDTO.getProdOfferId());
                stockTransDetailDTO.setStateId(Const.STATE_STATUS.NEW);
                stockTransDetailDTO.setQuantity(stockOrderDetailDTO.getQuantityReal());
                lstStockTransDetail.add(stockTransDetailDTO);
            }
            // Luu DB
            smartphoneTransactionService.processApproveOrder(staffDTO, shopId, stockOrderDTO.getStaffId(), stockOrderDTO, listOrderDB, lstStockTransDetail);

            result = new BaseMessage(true, "", "sp.approve.stockOrder.success");
        } catch (LogicException ex) {
            logger.error("LogicException", ex);
            result = new BaseMessage(false, ex.getErrorCode(), ex.getKeyMsg(), ex.getParamsMsg());
        } catch (Exception ex) {
            logger.error("Exception", ex);
            result = new BaseMessage(false, "Error", "common.error.happen");
        }
        return result;
    }

    @Override
    public BaseMessage refuseOrder(Long shopId, Long staffId, String stockOrderCode) throws LogicException, Exception {
        BaseMessage result = new BaseMessage();
        try {
            if (DataUtil.isNullOrZero(shopId) || DataUtil.isNullOrZero(staffId) || DataUtil.isNullOrEmpty(stockOrderCode)) {
                return new BaseMessage(false, "ERR_IM", "sp.approve.stockOrder.parametter.isNull");
            }
            ShopDTO shopDTO = shopService.findOne(shopId);
            if (DataUtil.isNullObject(shopDTO) || !DataUtil.safeEqual(Const.STATUS_ACTIVE, shopDTO.getStatus())) {
                return new BaseMessage(false, "ERR_IM", "shop.validate");
            }
            StaffDTO staffDTO = staffService.findOne(staffId);
            if (DataUtil.isNullObject(staffDTO) || !DataUtil.safeEqual(Const.STATUS_ACTIVE, staffDTO.getStatus())) {
                return new BaseMessage(false, "ERR_IM", "sp.staff.not.found");
            }
            if (!DataUtil.safeEqual(staffDTO.getShopId(), shopDTO.getShopId())) {
                return new BaseMessage(false, "ERR_IM", "validate.stock.inspect.staff.not.shop.userLogin");
            }
            StockOrderDTO stockOrderDTO = stockOrderRepo.getStockOrderByCode(stockOrderCode);
            if (DataUtil.isNullObject(stockOrderDTO)) {
                return new BaseMessage(false, "ERR_IM", "sp.approve.stockOrder.code.notFound");
            }
            if (!DataUtil.safeEqual(stockOrderDTO.getShopId(), shopDTO.getShopId())) {
                return new BaseMessage(false, "ERR_IM", "sp.approve.stockOrder.code.not.map.shop");
            }
            smartphoneTransactionService.refuseOrder(stockOrderDTO, staffDTO);
            result = new BaseMessage(true, "", "sp.approve.stockOrder.refuse.success");
        } catch (LogicException ex) {
            logger.error("LogicException", ex);
            result = new BaseMessage(false, ex.getErrorCode(), ex.getKeyMsg(), ex.getParamsMsg());
        } catch (Exception ex) {
            logger.error("Exception", ex);
            result = new BaseMessage(false, "Error", "common.error.happen");
        }
        return result;
    }

    @Override
    public BaseMessage requestOrder(Long shopId, Long staffId, List<StockOrderDetailDTO> listOrderDetail) throws LogicException, Exception {
        BaseMessage result;
        if (DataUtil.isNullOrZero(shopId) || DataUtil.isNullOrZero(staffId) || DataUtil.isNullOrEmpty(listOrderDetail)) {
            return new BaseMessage(false, "ERR_IM", "sp.approve.stockOrder.parametter.isNull");
        }
        ShopDTO shopDTO = shopService.findOne(shopId);
        if (DataUtil.isNullObject(shopDTO) || !DataUtil.safeEqual(Const.STATUS_ACTIVE, shopDTO.getStatus())) {
            return new BaseMessage(false, "ERR_IM", "shop.validate");
        }
        StaffDTO staffDTO = staffService.findOne(staffId);
        if (DataUtil.isNullObject(staffDTO) || !DataUtil.safeEqual(Const.STATUS_ACTIVE, staffDTO.getStatus())) {
            return new BaseMessage(false, "ERR_IM", "sp.staff.not.found");
        }
        if (!DataUtil.safeEqual(staffDTO.getShopId(), shopDTO.getShopId())) {
            return new BaseMessage(false, "ERR_IM", "validate.stock.inspect.staff.not.shop.userLogin");
        }
        StockOrderDTO stockOrder = stockOrderRepo.getStockOrderStaff(staffId);
        if (!DataUtil.isNullObject(stockOrder)) {
            return new BaseMessage(false, "ERR_IM", "sp.stockOrder.staff.status.valid");
        }
        List<Long> listProdOfferId = Lists.newArrayList();
        for (StockOrderDetailDTO stockOrderDetailDTO : listOrderDetail) {
            if (DataUtil.isNullOrZero(stockOrderDetailDTO.getProdOfferId())) {
                return new BaseMessage(false, "ERR_IM", "sp.approve.stockOrder.prodOfferId.isNull");
            }
            if (DataUtil.isNullOrZero(stockOrderDetailDTO.getQuantityOrder())) {
                return new BaseMessage(false, "ERR_IM", "sp.approve.stockOrder.quantityOrder.isNull");
            }
            if (stockOrderDetailDTO.getQuantityOrder().longValue() < 0) {
                return new BaseMessage(false, "ERR_IM", "sp.approve.stockOrder.quantityOrder.small.zezo");
            }
            if (stockOrderDetailDTO.getQuantityOrder().toString().length() > 10) {
                return new BaseMessage(false, "ERR_IM", "mn.invoice.invoiceTemplate.amount.msg.err");
            }
            ProductOfferingDTO productOfferingDTO = productOfferingRepo.getProductOfferingStockById(stockOrderDetailDTO.getProdOfferId());
            if (DataUtil.isNullObject(productOfferingDTO)
                    || DataUtil.isNullOrEmpty(productOfferingDTO.getStatus())
                    || !DataUtil.safeEqual(productOfferingDTO.getStatus(), Const.STATUS_ACTIVE)) {
                return new BaseMessage(false, "ERR_IM", "validate.stock.inspect.productOffering.not.exist", DataUtil.safeToString(stockOrderDetailDTO.getProdOfferId()));
            }
            if (listProdOfferId.contains(stockOrderDetailDTO.getProdOfferId())) {
                return new BaseMessage(false, "ERR_IM", "sp.approve.stockOrder.product.offer.duplicate");
            }
            listProdOfferId.add(stockOrderDetailDTO.getProdOfferId());
        }
        // Luu DB
        smartphoneTransactionService.requestOrder(shopId, staffId, listOrderDetail);
        result = new BaseMessage(true, "", "sp.create.stockOrder.success");
        return result;
    }

    @Override
    public BaseMessage cancelOrder(Long staffId) throws LogicException, Exception {
        BaseMessage result;
        if (DataUtil.isNullOrZero(staffId)) {
            return new BaseMessage(false, "ERR_IM", "sp.approve.stockOrder.parametter.isNull");
        }
        StaffDTO staffDTO = staffService.findOne(staffId);
        if (DataUtil.isNullObject(staffDTO) || !DataUtil.safeEqual(Const.STATUS_ACTIVE, staffDTO.getStatus())) {
            return new BaseMessage(false, "ERR_IM", "sp.staff.not.found");
        }
        StockOrderDTO stockOrder = stockOrderRepo.getStockOrderStaff(staffId);
        if (DataUtil.isNullObject(stockOrder)) {
            return new BaseMessage(false, "ERR_IM", "sp.stockOrder.staff.no.status.valid");
        }
        stockOrder.setStatus(Const.STOCK_ORDER.STATUS_CANCEL);
        stockOrder.setCancelDate(getSysDate(em));
        stockOrderService.saveStockOrder(stockOrder);
        result = new BaseMessage(true, "", "sp.cancel.stockOrder.success");
        return result;
    }

    @Override
    public StockCollaboratorDTO exportStockCollaborator(String staffCode, String collCode, List<ProductOfferingSM> lstProductOfferingDTOs) throws LogicException, Exception {
        StockCollaboratorDTO result;
        // Validate du lieu
        if (DataUtil.isNullOrEmpty(staffCode)) {
            return new StockCollaboratorDTO(false, "ERR_IM", "sp.exportStockCollaborator.staffCode.isNull");
        }
        if (DataUtil.isNullOrEmpty(collCode)) {
            return new StockCollaboratorDTO(false, "ERR_IM", "sp.exportStockCollaborator.channelType.isNull");
        }
        if (DataUtil.isNullOrEmpty(lstProductOfferingDTOs)) {
            return new StockCollaboratorDTO(false, "ERR_IM", "sp.exportStockCollaborator.stockTransDetail.isNull");
        }
        StaffDTO staffDTO = staffService.getStaffByStaffCode(staffCode.trim());
        if (DataUtil.isNullObject(staffDTO)) {
            return new StockCollaboratorDTO(false, "ERR_IM", "sp.exportStockCollaborator.staff.notFoundDB");
        }
        StaffDTO collaboratorDTO = staffService.getStaffByStaffCode(collCode.trim());
        if (DataUtil.isNullObject(collaboratorDTO)) {
            return new StockCollaboratorDTO(false, "ERR_IM", "sp.exportStockCollaborator.collaborator.notFoundDB");
        }
        Long channelTypeId = collaboratorDTO.getChannelTypeId();
        if (DataUtil.isNullOrZero(channelTypeId)) {
            return new StockCollaboratorDTO(false, "ERR_IM", "sp.exportStockCollaborator.staff.channelType.isNull");
        }

        //Neu la diem ban
        if (DataUtil.safeEqual(collaboratorDTO.getPointOfSale(), Const.CHANNEL_TYPE.POINT_OF_SALE_DB)) {
            channelTypeId = Const.CHANNEL_TYPE.CHANNEL_TYPE_POINT_OF_SALE;
        }
        if (DataUtil.isNullOrEmpty(collaboratorDTO.getPricePolicy())) {
            return new StockCollaboratorDTO(false, "ERR_IM", "sp.exportStockCollaborator.staff.policy.isNull");
        }
        if (DataUtil.isNullOrEmpty(collaboratorDTO.getDiscountPolicy())) {
            return new StockCollaboratorDTO(false, "ERR_IM", "sp.exportStockCollaborator.staff.discountPolicy.isNull");
        }
        ReasonDTO reasonDTO = reasonService.getReason(Const.REASON_CODE.EXP_TO_COL, Const.REASON_TYPE.STOCK_EXP_COLL);
        if (DataUtil.isNullObject(reasonDTO)) {
            return new StockCollaboratorDTO(false, "ERR_IM", "sp.exportStockCollaborator.reason.notFoundDB");
        }

        //Lay thong tin chi nhanh cua user xuat hang
        Long branchId = shopService.findBranchId(staffDTO.getShopId());

        // Stock_trans_detail
        List<StockTransDetailDTO> stockTransDetailDTOs = Lists.newArrayList();
        List<String> listProductCode = Lists.newArrayList();
        for (ProductOfferingSM productOfferingSM : lstProductOfferingDTOs) {
            // Validate thong tin mat hang truyen vao
            if (DataUtil.isNullOrEmpty(productOfferingSM.getCode())) {
                return new StockCollaboratorDTO(false, "ERR_IM", "sp.exportStockCollaborator.stockTransDetail.prodOfferCode.isNull");
            }
            ProductOfferingDTO productOffering = productOfferingService.findByProductOfferCode(productOfferingSM.getCode(), Const.STATUS_ACTIVE);
            if (DataUtil.isNullObject(productOffering)) {
                return new StockCollaboratorDTO(false, "ERR_IM", "sp.exportStockCollaborator.stockTransDetail.prodOfferCode.notFoundDB");
            }
            if (DataUtil.isNullObject(productOfferingSM.getStateId())) {
                return new StockCollaboratorDTO(false, "ERR_IM", "sp.exportStockCollaborator.stockTransDetail.stateId.isNull");
            }
            if (DataUtil.isNullOrZero(productOfferingSM.getQuantity())) {
                return new StockCollaboratorDTO(false, "ERR_IM", "sp.exportStockCollaborator.stockTransDetail.quantity.isNull");
            }
            if (productOfferingSM.getQuantity().longValue() < 0) {
                return new StockCollaboratorDTO(false, "ERR_IM", "sp.exportStockCollaborator.stockTransDetail.quantity.positive");
            }
            if (listProductCode.contains(productOfferingSM.getCode() + productOfferingSM.getStateId())) {
                return new StockCollaboratorDTO(false, "ERR_IM", "sp.exportStockCollaborator.stockTransDetail.duplicate");
            }
            listProductCode.add(productOfferingSM.getCode() + productOfferingSM.getStateId());
            ProductOfferTypeDTO productOfferTypeDTO = productOfferTypeService.findOne(productOffering.getProductOfferTypeId());
            if (DataUtil.isNullObject(productOfferTypeDTO) || !DataUtil.safeEqual(Const.STATUS_ACTIVE, productOfferTypeDTO.getStatus())) {
                return new StockCollaboratorDTO(false, "ERR_IM", "validate.stock.inspect.productOfferType.notFound");
            }
            List<ProductOfferPriceDTO> listDepositPrice = productOfferPriceService.getPriceDepositAmount(productOffering.getProductOfferingId(),
                    branchId, DataUtil.safeToLong(collaboratorDTO.getPricePolicy()), channelTypeId, null, null);
            if (DataUtil.isNullOrEmpty(listDepositPrice)) {
                return new StockCollaboratorDTO(false, "ERR_IM", "sp.exportStockCollaborator.stockTransDetail.deposit.price.notFound");
            }

            if (listDepositPrice.size() > 1) {
                return new StockCollaboratorDTO(false, "ERR_IM", "sp.exportStockCollaborator.stockTransDetail.deposit.price.duplicate");
            }

            if (DataUtil.isNullObject(listDepositPrice.get(0))) {
                return new StockCollaboratorDTO(false, "ERR_IM", "sp.exportStockCollaborator.stockTransDetail.deposit.price.notFound");
            }
            List<StockTransSerialDTO> lstStockTransSerial = Lists.newArrayList();
            if (DataUtil.safeEqual(Const.PRODUCT_OFFERING.CHECK_SERIAL, productOffering.getCheckSerial())) {
                if (DataUtil.isNullOrEmpty(productOfferingSM.getListStockTransSerialDTOs())) {
                    return new StockCollaboratorDTO(false, "ERR_IM", "sp.exportStockCollaborator.stockTransDetail.serialList.isNull");
                }
                for (StockTransSerialSM stockTransSerialSM : productOfferingSM.getListStockTransSerialDTOs()) {
                    if (DataUtil.isNullOrEmpty(stockTransSerialSM.getFromSerial())) {
                        return new StockCollaboratorDTO(false, "ERR_IM", "sp.staffExportStockToShop.listProduct.fromSerial.isNull");
                    }
                    if (DataUtil.isNullOrEmpty(stockTransSerialSM.getToSerial())) {
                        return new StockCollaboratorDTO(false, "ERR_IM", "sp.staffExportStockToShop.listProduct.toSerial.isNull");
                    }
                    if (stockTransSerialSM.getFromSerial().trim().length() != stockTransSerialSM.getToSerial().trim().length()) {
                        return new StockCollaboratorDTO(false, "ERR_IM", "mn.stock.partner.length.fromSerial.toSerial.valid");
                    }
                    Long quantity;
                    if (DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_HANDSET, productOffering.getProductOfferTypeId())) {
                        if (!DataUtil.safeEqual(stockTransSerialSM.getFromSerial().trim(), stockTransSerialSM.getToSerial().trim())) {
                            return new StockCollaboratorDTO(false, "ERR_IM", "mn.divide.serial.infile.equal");
                        }
                        for (StockTransSerialDTO serialDetail : lstStockTransSerial) {
                            if (stockTransSerialSM.getFromSerial().trim().equalsIgnoreCase(serialDetail.getFromSerial().trim())
                                    && stockTransSerialSM.getToSerial().trim().equalsIgnoreCase(serialDetail.getToSerial().trim())) {
                                return new StockCollaboratorDTO(false, "ERR_IM", "mn.stock.partner.range.duplidate", stockTransSerialSM.getFromSerial(), stockTransSerialSM.getToSerial());
                            }
                        }
                        quantity = 1L;
                    } else {
                        if (!DataUtil.validateStringByPattern(stockTransSerialSM.getFromSerial(), Const.REGEX.NUMBER_REGEX)
                                || !DataUtil.validateStringByPattern(stockTransSerialSM.getToSerial(), Const.REGEX.NUMBER_REGEX)) {
                            return new StockCollaboratorDTO(false, "ERR_IM", "mn.stock.partner.serial.valid.format", productOfferTypeDTO.getName());
                        }
                        BigInteger fromSerialNum = new BigInteger(stockTransSerialSM.getFromSerial());
                        BigInteger toSerialNum = new BigInteger(stockTransSerialSM.getToSerial());
                        BigInteger quantityBig = toSerialNum.subtract(fromSerialNum);
                        if (quantityBig.compareTo(new BigInteger("0")) < 0 || quantityBig.compareTo(new BigInteger("500000")) > 0) {
                            return new StockCollaboratorDTO(false, "ERR_IM", "mn.stock.partner.serial.valid.range");
                        }
                        for (StockTransSerialDTO serialDetail : lstStockTransSerial) {
                            BigInteger fromSerialCurrent = new BigInteger(serialDetail.getFromSerial());
                            BigInteger toSerialCurrent = new BigInteger(serialDetail.getToSerial());
                            if (fromSerialNum.compareTo(fromSerialCurrent) >= 0 && fromSerialNum.compareTo(toSerialCurrent) <= 0
                                    || toSerialNum.compareTo(fromSerialCurrent) >= 0 && toSerialNum.compareTo(toSerialCurrent) <= 0) {
                                return new StockCollaboratorDTO(false, "ERR_IM", "mn.stock.partner.range.duplidate", stockTransSerialSM.getFromSerial(), stockTransSerialSM.getToSerial());
                            }
                        }

                        quantity = quantityBig.longValue() + 1;
                    }
                    StockTransSerialDTO stockTransSerialDTO = new StockTransSerialDTO();
                    stockTransSerialDTO.setFromSerial(stockTransSerialSM.getFromSerial());
                    stockTransSerialDTO.setToSerial(stockTransSerialSM.getToSerial());
                    stockTransSerialDTO.setProdOfferId(productOffering.getProductOfferingId());
                    stockTransSerialDTO.setQuantity(quantity);
                    lstStockTransSerial.add(stockTransSerialDTO);
                }
            } else {
                return new StockCollaboratorDTO(false, "ERR_IM", "sp.exportStockCollaborator.noSerial.notExport");
            }
            StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
            stockTransDetailDTO.setProdOfferId(productOffering.getProductOfferingId());
            stockTransDetailDTO.setProdOfferTypeId(productOffering.getProductOfferTypeId());
            stockTransDetailDTO.setStateId(productOfferingSM.getStateId());
            stockTransDetailDTO.setQuantity(productOfferingSM.getQuantity());
            stockTransDetailDTO.setLstStockTransSerial(lstStockTransSerial);
            stockTransDetailDTO.setTableName(productOfferTypeDTO.getTableName());
            stockTransDetailDTOs.add(stockTransDetailDTO);
        }
        // End validate du lieu
        try {
            // Khoi tao stock_tran
            StockTransDTO stockTransDTO = new StockTransDTO();
            stockTransDTO.setFromOwnerType(Const.OWNER_TYPE.STAFF_LONG);
            stockTransDTO.setFromOwnerId(staffDTO.getStaffId());
            stockTransDTO.setToOwnerType(Const.OWNER_TYPE.STAFF_LONG);
            stockTransDTO.setToOwnerId(collaboratorDTO.getStaffId());
            stockTransDTO.setReasonId(reasonDTO.getReasonId());
            stockTransDTO.setNote(getText("sp.exportStockCollaborator.stockTrans.note"));
            stockTransDTO.setShopId(staffDTO.getShopId());
            stockTransDTO.setUserCreate(staffDTO.getStaffCode());

            // Khoi tao stock_trans_action
            String actionCode = staffCode.trim() + new Date().getTime();
            StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
            stockTransActionDTO.setActionCode(actionCode);
            stockTransActionDTO.setNote(getText("sp.exportStockCollaborator.stockTrans.note"));
            stockTransActionDTO.setCreateUser(staffDTO.getStaffCode());
            stockTransActionDTO.setActionStaffId(staffDTO.getStaffId());
            // Phan quyen
            RequiredRoleMap requiredRoleMap = new RequiredRoleMap();

            BaseMessageStockTrans message = executeStockTransService.executeStockTrans(Const.STOCK_TRANS.EXPORT_COLLABORATOR,
                    Const.STOCK_TRANS_TYPE.EXPORT, stockTransDTO, stockTransActionDTO, stockTransDetailDTOs, requiredRoleMap);
            if (!DataUtil.isNullOrEmpty(message.getErrorCode())) {
                throw new LogicException(message.getErrorCode(), message.getKeyMsg(), message.getParamsMsg());
            }
            result = new StockCollaboratorDTO(true, "", "");
            result.setProductOfferingDTOs(lstProductOfferingDTOs);
        } catch (LogicException ex) {
            logger.error("LogicException", ex);
            result = new StockCollaboratorDTO(false, ex.getErrorCode(), ex.getKeyMsg(), ex.getParamsMsg());
        } catch (Exception ex) {
            logger.error("Exception", ex);
            result = new StockCollaboratorDTO(false, "Error", "common.error.happen");
        }
        return result;
    }

    @Override
    public List<String> getListStaticIPProvince(String domain, String province, Long ipType) throws LogicException, Exception {
        return brasIppoolService.getListStaticIPProvince(domain, province, ipType);
    }

    @Override
    public LookupIsdnDTO lookupIsdn(String fromIsdn, String toIsdn, String stockCode, Long maxRow) throws LogicException, Exception {
        List<String> lstIsdn = null;
        if (DataUtil.isNullOrEmpty(fromIsdn)) {
            return new LookupIsdnDTO(lstIsdn, false, "ERR_IM", "sp.fromIsdn.isNull");
        }
        if (DataUtil.isNullOrEmpty(toIsdn)) {
            return new LookupIsdnDTO(lstIsdn, false, "ERR_IM", "sp.toIsdn.isNull");
        }
        if (DataUtil.isNullOrEmpty(stockCode)) {
            return new LookupIsdnDTO(lstIsdn, false, "ERR_IM", "sp.stockCode.isNull");
        }
        if (DataUtil.isNullOrZero(maxRow)) {
            return new LookupIsdnDTO(lstIsdn, false, "ERR_IM", "sp.maxRow.isNull");
        }
        ShopDTO shopDTO = shopService.getShopByCodeAndActiveStatus(stockCode.trim());
        if (DataUtil.isNullObject(shopDTO)) {
            return new LookupIsdnDTO(lstIsdn, false, "ERR_IM", "sp.StockCode.notFoundDb");
        }
        List<String> lsStatus = Lists.newArrayList(Const.STOCK_NUMBER_STATUS.NEW, Const.STOCK_NUMBER_STATUS.NOT_USE);
        lstIsdn = stockNumberRepo.lookupIsdn(fromIsdn, toIsdn, Lists.newArrayList(shopDTO.getShopId()), lsStatus, maxRow);
        if (DataUtil.isNullOrEmpty(lstIsdn)) {
            return new LookupIsdnDTO(lstIsdn, false, "ERR_IM", "sp.StockCode.not.number", stockCode);
        }
        return new LookupIsdnDTO(lstIsdn, true, "", "sp.StockCode.number", stockCode);
    }


    @Override
    public List<StockSerialDTO> getStockSerialInfomation(Long productOfferTypeId, String serial) throws LogicException, Exception {
        ProductOfferTypeDTO productOfferTypeDTO = productOfferTypeService.findOne(productOfferTypeId);
        if (DataUtil.isNullObject(productOfferTypeDTO) || !DataUtil.safeEqual(Const.STATUS_ACTIVE, productOfferTypeDTO.getStatus())) {
            throw new LogicException("", "validate.stock.inspect.productOfferType.notFound");
        }
        if (DataUtil.isNullOrEmpty(serial)) {
            throw new LogicException("", "validate.stock.balance.serial.empty");
        }
        return stockTransSerialRepo.getStockSerialInfomation(productOfferTypeId, productOfferTypeDTO.getTableName(), serial);
    }

    @Override
    public String getTransCodeExportNote(String staffCode) throws Exception {
        StaffDTO staffDTO = staffService.getStaffByStaffCode(staffCode);
        if (staffDTO != null) {
            return staffService.getTransCode(Const.STOCK_TRANS.TRANS_CODE_PX, Const.STOCK_TRANS_TYPE.EXPORT, staffDTO);
        }
        return "";
    }

    @Override
    public List<ReasonDTO> getLsReasonByType(String reasonType) throws LogicException, Exception {
        return reasonService.getLsReasonByType(reasonType);
    }

    @Override
    public List<SignFlowDTO> getSignFlowByShop(Long shopId) throws Exception {
        return signFlowService.getSignFlowByShop(shopId);
    }

    @Override
    public List<SignFlowDetailDTO> findBySignFlowId(Long signFlowId) throws Exception {
        return signFlowDetailService.findBySignFlowId(signFlowId);
    }

    @Override
    public List<VShopStaffDTO> getListStaffSearch(String inputStaff, Long shopId) throws LogicException, Exception {
        return staffService.getStaffByShopIdAndChanelType(new StaffTagConfigDTO(DataUtil.replaceSpaceSolr(DataUtil.safeToString(inputStaff).trim()), DataUtil.safeToString(shopId), Lists.newArrayList("14")));
    }

    @Override
    public List<VShopStaffDTO> getListShopSearch(String inputSearch, Long shopId) throws LogicException, Exception {
        return shopService.getListShopTransfer(DataUtil.replaceSpaceSolr(DataUtil.safeToString(inputSearch).trim()), DataUtil.safeToString(shopId));
    }

    @Override
    public List<ProductOfferingTotalDTO> getLsProductOfferingDTO(String input, String stateId, String ownerType, Long ownerId) throws Exception {
        List<ProductOfferTypeDTO> lsProdOfferTypeDTO = productOfferTypeService.getListProductOfferTypeByOwnerId(ownerId, ownerType, null);
        if (!DataUtil.isNullOrEmpty(lsProdOfferTypeDTO)) {
            List<Long> lsProdOfferTypeId = lsProdOfferTypeDTO.stream().map(ProductOfferTypeDTO::getProductOfferTypeId).collect(Collectors.toList());
            return productOfferingService.getLsProductOfferingDTO(DataUtil.replaceSpaceSolr(DataUtil.safeToString(input).trim()), lsProdOfferTypeId, ownerId, ownerType, stateId, null);
        }
        return new ArrayList<>();
    }

    @Override
    public BaseMessage executeStockTrans(String statusAutoOrder, StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO,
                                         List<StockTransDetailDTO> lstStockTransDetail, RequiredRoleMap requiredRoleMap) throws Exception {

        BaseMessage baseMessage = new BaseMessage();

        //thuc hien validate check ky vOffice neu co chon tren man hinh
        try {
            if (stockTransDTO.isSignVoffice()) {
                if (DataUtil.isNullOrEmpty(stockTransDTO.getUserName())) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "common.user.require.msg");
                }
                if (DataUtil.isNullOrEmpty(stockTransDTO.getPassWord())) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "common.password.require.msg");
                }
                if (stockTransDTO.getSignFlowId() == null) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "smartphone.createStockTrans.signFlowId.empty");
                }
                SignFlowDTO signFlowDTO = signFlowService.findOne(stockTransDTO.getSignFlowId());
                if (DataUtil.isNullObject(signFlowDTO) || !DataUtil.safeEqual(signFlowDTO.getStatus(), Const.STATUS_ACTIVE)) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "common.signflow.not.exsit.msg");
                }
                List<StaffBO> staffBOs;
                try {
                    staffBOs = ver3AutoSign.checkAccount(stockTransDTO.getUserName(), stockTransDTO.getPassWord());
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "common.voffice.error.ws");
                }
                if (DataUtil.isNullOrEmpty(staffBOs)) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_PASS_INVALID, "common.valid.voffice.account");
                } else if (staffBOs.size() > 1) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_DUPLICATE_OBJECT, "common.valid.voffice.2account", staffBOs.size(), stockTransDTO.getUserName());
                }
            }
            if (DataUtil.isNullOrEmpty(stockTransActionDTO.getActionCode())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_PASS_INVALID, "export.order.code.require.msg");
            }
            if (stockTransActionDTO.getActionStaffId() == null) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_PASS_INVALID, "smartphone.createStockTrans.actionStaffId.empty");
            }
            StaffDTO staffDTO = staffService.findOne(stockTransActionDTO.getActionStaffId());
            if (staffDTO == null || !Const.STATUS.ACTIVE.equals(staffDTO.getStatus())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_PASS_INVALID, "balance.vali.createUser");
            }
            stockTransActionDTO.setCreateUser(staffDTO.getStaffCode());

            //Const.PERMISION.ROLE_STOCK_NUM_SHOP,Const.PERMISION.ROLE_AUTO_ORDER_NOTE

            if (requiredRoleMap == null) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_PASS_INVALID, "smartphone.createStockTrans.requiredRoleMap.empty");
            }

            boolean isCreateAutoOrder = "1".equals(statusAutoOrder);
            if (isCreateAutoOrder && !requiredRoleMap.hasRole(Const.PERMISION.ROLE_AUTO_ORDER_NOTE)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_PASS_INVALID, "smartphone.createStockTrans.and.note.auto.permision");
            }

            if (isCreateAutoOrder) {
                stockTransDTO.setActionCodeNote(staffService.getTransCode(Const.STOCK_TRANS.TRANS_CODE_PX, Const.STOCK_TRANS_TYPE.EXPORT,
                        staffService.getStaffByStaffCode(staffDTO.getStaffCode())));
            }

            baseMessage = executeStockTransService.executeStockTrans(isCreateAutoOrder ? Const.STOCK_TRANS.ORDER_AND_NOTE : Const.STOCK_TRANS.ORDER,
                    Const.STOCK_TRANS_TYPE.EXPORT, stockTransDTO, stockTransActionDTO, lstStockTransDetail, requiredRoleMap);
            if (!DataUtil.isNullOrEmpty(baseMessage.getErrorCode())) {
                LogicException logicException = new LogicException(baseMessage.getErrorCode(), baseMessage.getKeyMsg(), baseMessage.getParamsMsg());
                throw logicException;
            }
            if ("1".equals(statusAutoOrder)) {
                baseMessage.setKeyMsg("export.order.note.create.success");
            } else {
                baseMessage.setKeyMsg("export.order.create.success");
            }
        } catch (LogicException le) {
            baseMessage.setErrorCode(le.getErrorCode());
            baseMessage.setKeyMsg(le.getKeyMsg());
            baseMessage.setParamsMsg(le.getParamsMsg());
            baseMessage.setDescription(le.getDescription());
            logger.error(le.getMessage(), le);
            return baseMessage;
        } catch (Exception ex) {
            baseMessage.setErrorCode(ErrorCode.ERROR_NOT_DEFINE);
            baseMessage.setKeyMsg("common.error.happen");
            logger.error(ex.getMessage(), ex);
            return baseMessage;
        }
        return baseMessage;
    }

    @Override
    public StockOrderDTO getStockOrderStaff(Long staffId) throws Exception {
        return stockOrderRepo.getStockOrderStaff(staffId);
    }

    @Override
    public List<VofficeRoleDTO> getAllListOfficeRole() throws Exception, LogicException {
        return vofficeRoleService.search(new VofficeRoleDTO(), false);
    }

    @Override
    public BaseMessage lockIsdnByStaff(Long staffId, String isdn, String ipAddress, RequiredRoleMap requiredRoleMap) {
        BaseMessage baseMessage = new BaseMessage();
        try {
            String isdnResult = stockNumberService.lockIsdnByStaff(staffId, isdn, ipAddress, requiredRoleMap);
            baseMessage.setKeyMsg("stock.number.isdn.lock.isdn.option.lock.sucess", isdnResult);
        } catch (LogicException le) {
            baseMessage.setErrorCode(le.getErrorCode());
            baseMessage.setKeyMsg(le.getKeyMsg());
            baseMessage.setParamsMsg(le.getParamsMsg());
            baseMessage.setDescription(le.getDescription());
            logger.error(le.getMessage(), le);
            return baseMessage;
        } catch (Exception ex) {
            baseMessage.setErrorCode(ErrorCode.ERROR_NOT_DEFINE);
            baseMessage.setKeyMsg("common.error.happen");
            logger.error(ex.getMessage(), ex);
            return baseMessage;
        }
        return baseMessage;
    }

    public BaseMessage unLockIsdnByStaff(Long staffId, String isdn, String ipAddress) {
        BaseMessage baseMessage = new BaseMessage();
        try {
            stockNumberService.unlockIsdnByStaff(staffId, isdn, ipAddress);
            baseMessage.setKeyMsg("stock.number.isdn.unlock.isdn.option.lock.sucess", DataUtil.safeToString(isdn).trim());
        } catch (LogicException e) {
            baseMessage.setErrorCode(e.getErrorCode());
            baseMessage.setKeyMsg(e.getKeyMsg());
            baseMessage.setParamsMsg(e.getParamsMsg());
            baseMessage.setDescription(e.getDescription());
            logger.error(e.getMessage(), e);
            return baseMessage;
        } catch (Exception ex) {
            baseMessage.setErrorCode(ErrorCode.ERROR_NOT_DEFINE);
            baseMessage.setKeyMsg("common.error.happen");
            logger.error(ex.getMessage(), ex);
            return baseMessage;
        }
        return baseMessage;
    }

    public SearchIsdnDTO searchIsdn(String fromIsdn,
                                    String toIsdn,
                                    String typeIsdn,
                                    Long status,
                                    Long ownerId,
                                    Long ownerType,
                                    Long telecomServiceId,
                                    Long startRow,
                                    Long maxRows) throws LogicException, Exception {

        List<VStockNumberDTO> lstIsdn = null;
        try {
            if (DataUtil.isNullOrEmpty(fromIsdn) && DataUtil.isNullOrEmpty(toIsdn)
                    && DataUtil.isNullOrEmpty(typeIsdn)) {
                return new SearchIsdnDTO(lstIsdn, false, "ERR_IM", "sp.searchIsdn.isdn.isNull");
            }
            if (DataUtil.isNullOrZero(telecomServiceId)) {
                return new SearchIsdnDTO(lstIsdn, false, "ERR_IM", "sp.searchIsdn.telecomServiceId.isNull");
            }
            if (DataUtil.isNullObject(startRow) || DataUtil.safeToLong(startRow) < 0L) {
                return new SearchIsdnDTO(lstIsdn, false, "ERR_IM", "sp.searchIsdn.startRow.isNull");
            }
            if (DataUtil.isNullObject(maxRows) || DataUtil.safeToLong(maxRows) < 0L || DataUtil.safeToLong(maxRows) > 50L) {
                return new SearchIsdnDTO(lstIsdn, false, "ERR_IM", "sp.searchIsdn.maxRows.isNull");
            }
            if (DataUtil.isNullOrZero(ownerId)) {
                return new SearchIsdnDTO(lstIsdn, false, "ERR_IM", "sp.searchIsdn.ownerId.isNull");
            }
            if (DataUtil.isNullOrZero(ownerType)) {
                return new SearchIsdnDTO(lstIsdn, false, "ERR_IM", "sp.searchIsdn.ownerType.isNull");
            }
            if (DataUtil.safeToLong(status) < 0L) {
                return new SearchIsdnDTO(lstIsdn, false, "ERR_IM", "sp.searchIsdn.status.invalid");
            }
            //
            String query = "*:*";
            MvShopStaffDTO mvShopStaffDTO = shopService.getMViewShopStaff(ownerType, ownerId);
            if (DataUtil.isNullObject(mvShopStaffDTO)) {
                return new SearchIsdnDTO(lstIsdn, false, "ERR_IM", "sp.ownerId.notFoundDB");
            }
            query += " AND owner_code:" + "\"" + mvShopStaffDTO.getOwnerCode() + "\"";

            if (!DataUtil.isNullOrEmpty(fromIsdn) && DataUtil.isNullOrEmpty(toIsdn)) {
                query += " AND isdn:[" + DataUtil.safeToLong(fromIsdn.trim()) + " TO *]";
            }
            if (DataUtil.isNullOrEmpty(fromIsdn) && !DataUtil.isNullOrEmpty(toIsdn)) {
                query += " AND isdn:[* TO " + DataUtil.safeToLong(toIsdn.trim()) + "]";
            }
            if (!DataUtil.isNullOrEmpty(fromIsdn) && !DataUtil.isNullOrEmpty(toIsdn)) {
                query += " AND isdn:[" + DataUtil.safeToLong(fromIsdn.trim()) + " TO " + DataUtil.safeToLong(toIsdn.trim()) + "]";
            }
            if (!DataUtil.isNullOrEmpty(typeIsdn)) {
                query += " AND isdn:" + typeIsdn.trim();
            }
            if (!DataUtil.isNullOrZero(status)) {
                query += " AND status:" + status;
            }
            if (DataUtil.safeEqual(telecomServiceId, Const.TELECOM_SERVICE.ISDN)) {
                lstIsdn = SolrController.doSearch(Const.SOLR_CORE.ISDN_MOBILE, query, VStockNumberDTO.class, maxRows, "isdn", Const.SORT_ORDER.ASC, startRow);
            } else if (DataUtil.safeEqual(telecomServiceId, Const.TELECOM_SERVICE.HOMEPHONE)) {
                lstIsdn = SolrController.doSearch(Const.SOLR_CORE.ISDN_HOMEPHONE, query, VStockNumberDTO.class, maxRows, "isdn", Const.SORT_ORDER.ASC, startRow);
            } else {
                lstIsdn = SolrController.doSearch(Const.SOLR_CORE.ISDN_PSTN, query, VStockNumberDTO.class, maxRows, "isdn", Const.SORT_ORDER.ASC, startRow);
            }
            if (DataUtil.isNullOrEmpty(lstIsdn)) {
                return new SearchIsdnDTO(lstIsdn, false, "ERR_IM", "sp.searchIsdn.lst.isdn.not.found");
            }
            return new SearchIsdnDTO(lstIsdn, true, "", "");
        } catch (Exception ex) {
            logger.error("Error :", ex);
            return new SearchIsdnDTO(lstIsdn, false, "ERR_IM", "sp.searchIsdn.error");
        }
    }

    public StockNumber findStockNumberByIsdn(String isdn) throws Exception {
        return stockNumberRepo.findStockNumberByIsdn(isdn);
    }

    public List<ProductOfferPriceDTO> viewIsdnCommitment(Long prodOfferId) {
        List<ProductOfferPriceDTO> listIsdnCommitment = Lists.newArrayList();
        try {
            if (DataUtil.isNullOrZero(prodOfferId)) {
                return listIsdnCommitment;
            }
            ProductOfferingDTO productOffer = productOfferingService.findOne(prodOfferId);

            if (DataUtil.isNullObject(productOffer)) {
                return listIsdnCommitment;
            }
            return productOfferPriceService.getProductOfferPriceByProdOfferId(productOffer.getProductOfferingId());

        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            return listIsdnCommitment;
        }
    }
}

