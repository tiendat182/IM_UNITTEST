package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.model.StockBalanceRequest;
import com.viettel.bccs.inventory.repo.StockBalanceRequestRepo;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.*;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.fw.service.BaseServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.jws.WebMethod;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class StockBalanceRequestServiceImpl extends BaseServiceImpl implements StockBalanceRequestService {

    private final BaseMapper<StockBalanceRequest, StockBalanceRequestDTO> mapper = new BaseMapper<>(StockBalanceRequest.class, StockBalanceRequestDTO.class);

    @Autowired
    private StaffService staffService;
    @Autowired
    private ShopService shopService;
    @Autowired
    StockTransService stockTransService;
    @Autowired
    private ProductOfferingService productOfferingService;
    @Autowired
    private ProductOfferTypeService productOfferTypeService;
    @Autowired
    private StockBalanceDetailService stockBalanceDetailService;
    @Autowired
    private StockBalanceSerialService stockBalanceSerialService;
    @Autowired
    private StockTransSerialService stockTransSerialService;
    @Autowired
    private SignFlowDetailService signFlowDetailService;
    @Autowired
    private StockTransVofficeService stockTransVofficeService;
    @Autowired
    private StockTransActionService stockTransActionService;

    @Autowired
    private OptionSetValueService optionSetService;
    @Autowired
    StockTransDetailService stockTransDetailService;
    @Autowired
    StockTotalService stockTotalService;

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    public static final String STOCK_TRANS_VOFFICE_SEQ = "STOCK_TRANS_VOFFICE_SEQ";
    public static final String TEN_ZEZO = "0000000000";


    @Autowired
    private StockBalanceRequestRepo repository;
    public static final Logger logger = Logger.getLogger(StockBalanceRequestService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public StockBalanceRequestDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<StockBalanceRequestDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<StockBalanceRequestDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }


    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage create(StockBalanceRequestDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public StockBalanceRequestDTO save(StockBalanceRequestDTO dto) throws Exception {
        try {
            StockBalanceRequest stockBalanceRequest = repository.save(mapper.toPersistenceBean(dto));
            return mapper.toDtoBean(stockBalanceRequest);
        } catch (Exception ex) {
            logger.error("Error: ", ex);
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "common.error.happened");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveStockBalanceRequest(StockBalanceRequestDTO stockBalanceRequestDTO,
                                        List<StockBalanceDetailDTO> lstDetailDTO) throws LogicException, Exception {
        try {
            Date currentDate = getSysDate(em);
            // Validate
            if (!repository.isDulicateRequestCode(stockBalanceRequestDTO.getRequestCode())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.balance.stock.request.validate.requestCode");
            }
            validateStockBalanceRequest(stockBalanceRequestDTO, lstDetailDTO);
            // Luu DB
            List<SignFlowDetailDTO> lstSignFlowDetail = signFlowDetailService.findBySignFlowId(stockBalanceRequestDTO.getSignFlowId());

            if (DataUtil.isNullOrEmpty(lstSignFlowDetail)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.signFlow.emptyDetail");
            }
            StringBuilder lstUser = new StringBuilder("");
            if (!DataUtil.isNullOrEmpty(lstSignFlowDetail)) {
                for (SignFlowDetailDTO signFlowDetailDTO : lstSignFlowDetail) {
                    if (DataUtil.isNullOrEmpty(signFlowDetailDTO.getEmail())) {
                        throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.signFlow.emptyEmail");
                    }
                    if (!DataUtil.isNullOrEmpty(lstUser.toString())) {
                        lstUser.append(",");
                    }
                    lstUser.append(signFlowDetailDTO.getEmail().trim().substring(0, signFlowDetailDTO.getEmail().lastIndexOf("@")));
                }
                stockBalanceRequestDTO.setListEmailSign(lstUser.toString());
            }
            //
            stockBalanceRequestDTO.setStatus(Const.STOCK_BALANCE.BALANCE_TYPE_STATUS_CREATE);
            stockBalanceRequestDTO.setCreateDatetime(currentDate);
            stockBalanceRequestDTO.setUpdateDatetime(currentDate);
            stockBalanceRequestDTO = save(stockBalanceRequestDTO);
            // Luu chi tiet mat hang
            if (!DataUtil.isNullOrEmpty(lstDetailDTO)) {
                for (StockBalanceDetailDTO stockBalanceDetailDTO : lstDetailDTO) {
                    List<StockBalanceSerialDTO> lstSerial = stockBalanceDetailDTO.getLstSerialDTO();
                    stockBalanceDetailDTO.setStockRequestId(stockBalanceRequestDTO.getStockRequestId());
                    stockBalanceDetailDTO.setCreateDatetime(currentDate);
                    stockBalanceDetailDTO = stockBalanceDetailService.save(stockBalanceDetailDTO);
                    // Luu thong tin serial
                    if (!DataUtil.isNullOrEmpty(lstSerial)) {
                        for (StockBalanceSerialDTO stockStockBalanceSerialDTO : lstSerial) {
                            stockStockBalanceSerialDTO.setProdOfferId(stockBalanceDetailDTO.getProdOfferId());
                            stockStockBalanceSerialDTO.setStockRequestId(stockBalanceRequestDTO.getStockRequestId());
                            stockStockBalanceSerialDTO.setStockBalanceDetail(stockBalanceDetailDTO.getStockBalanceDetailId());
                            stockStockBalanceSerialDTO.setCreateDatetime(currentDate);
                            stockBalanceSerialService.save(stockStockBalanceSerialDTO);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            logger.error("Error: ", ex);
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "common.error.happened");
        }
    }

    @Override
    public void validateStockBalanceRequest(StockBalanceRequestDTO stockBalanceRequestDTO,
                                            List<StockBalanceDetailDTO> lstDetailDTO) throws LogicException, Exception {
        // Kiem tra kho nhap co ton tai khong.
        ShopDTO shopDTO = shopService.findOne(stockBalanceRequestDTO.getOwnerId());
        if (DataUtil.isNullObject(shopDTO) || !DataUtil.safeEqual(shopDTO.getStatus(), Const.STATUS.ACTIVE)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.balance.shop.notexistence");
        }
        StaffDTO staffDTO = staffService.findOne(stockBalanceRequestDTO.getOwnerIdLogin());

        if ((DataUtil.isNullObject(staffDTO) || !DataUtil.safeEqual(staffDTO.getShopId(), shopDTO.getParentShopId()))
                && !DataUtil.safeEqual(staffDTO.getShopId(), shopDTO.getShopId())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.balance.stock.request.validate.shop.not.parrent");
        }
        List<String> lstProductOfferingCode = Lists.newArrayList();
        List<Long> lstProdOfferId = Lists.newArrayList();
        List<Long> lstIdForCheck ;
        for (StockBalanceDetailDTO stockBalanceDetailDTO : lstDetailDTO) {
            ProductOfferingDTO productOfferingDTO = productOfferingService.findOne(stockBalanceDetailDTO.getProdOfferId());
            if (DataUtil.isNullObject(productOfferingDTO) || DataUtil.safeEqual(productOfferingDTO.getStatus(), Const.STATUS.NO_ACTIVE)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "validate.stock.inspect.productOffering.notFound");
            }
            ProductOfferTypeDTO productOfferTypeDTO = productOfferTypeService.findOne(productOfferingDTO.getProductOfferTypeId());
            if (DataUtil.isNullObject(productOfferTypeDTO) || DataUtil.isNullOrZero(productOfferTypeDTO.getProductOfferTypeId())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "validate.stock.inspect.productOffering.notFound");
            }
            if (DataUtil.isNullObject(stockBalanceDetailDTO.getQuantity()) || stockBalanceDetailDTO.getQuantity() <= 0) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "validate.stock.balance.quantity.empty");
            }
            if (lstProductOfferingCode.contains(productOfferingDTO.getCode())) {
                throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "stock.rescueInformation.validate.list.duplicate");
            }
            if (DataUtil.safeEqual(stockBalanceRequestDTO.getType(), Const.STOCK_BALANCE.BALANCE_TYPE_EXPORT)) {
                lstIdForCheck = productOfferingService.getProdOfferIdForRequestBalanceExport(stockBalanceRequestDTO.getOwnerId(), stockBalanceRequestDTO.getOwnerType(), stockBalanceDetailDTO.getProdOfferId());
            } else {
                lstIdForCheck = productOfferingService.getProdOfferIdForRequestBalanceImport(stockBalanceRequestDTO.getOwnerId(), stockBalanceRequestDTO.getOwnerType(), stockBalanceDetailDTO.getProdOfferId());
            }
            if (DataUtil.isNullOrEmpty(lstIdForCheck)) {
                throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "mn.balance.stock.request.validate.quantity", stockBalanceDetailDTO.getProdOfferName());
            }
            lstProductOfferingCode.add(productOfferingDTO.getCode());

            // Validate serial nhap vao
            List<StockBalanceSerialDTO> lstSerial = stockBalanceDetailDTO.getLstSerialDTO();
            if (DataUtil.isNullOrEmpty(lstSerial) && DataUtil.safeEqual(stockBalanceDetailDTO.getCheckSerial(), Const.SERIAL_STATUS)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "validate.stock.balance.serial.empty");
            }
            Long quantitySerial = 0L;
            Long quantitySerialDB = 0L;
            if (!DataUtil.isNullOrEmpty(lstSerial)) {
                for (StockBalanceSerialDTO stockBalanceSerialDTO : lstSerial) {
                    String fromSerial = stockBalanceSerialDTO.getFromSerial();
                    String toSerial = stockBalanceSerialDTO.getToSerial();
                    if (DataUtil.isNullOrEmpty(fromSerial) || DataUtil.isNullOrEmpty(toSerial)) {
                        throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "validate.stock.balance.serial.empty");
                    }
                    if (DataUtil.safeEqual(Const.PRODUCT_OFFER_TYPE.PHONE, productOfferingDTO.getProductOfferTypeId())) {
                        if (DataUtil.safeEqual(fromSerial, toSerial)) {
                            ++quantitySerial;
                        } else {
                            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.fromSerial.toSerial.equal");
                        }
                    } else {
                        //Check xem serial nhap vao co phai dang so khong
                        if (!DataUtil.isNumber(fromSerial) || !DataUtil.isNumber(toSerial)) {
                            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.serial.valid");
                        }
                        //fromSerial khong duoc lon hon toSerial
                        if (DataUtil.safeToLong(fromSerial) > DataUtil.safeToLong(toSerial)) {
                            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.serial.valid");
                        } else {
                            quantitySerial += DataUtil.safeToLong(toSerial) - DataUtil.safeToLong(fromSerial) + 1;
                        }
                    }
                    List<StockTransSerialDTO> lstSerialDB = stockTransSerialService.getListSerialQuantity(productOfferingDTO.getProductOfferingId(),
                            stockBalanceRequestDTO.getOwnerId(), stockBalanceRequestDTO.getOwnerType(), null, Const.STATUS.ACTIVE,
                            productOfferTypeDTO.getTableName(), fromSerial, toSerial);
                    if (DataUtil.safeEqual(Const.STOCK_BALANCE.BALANCE_TYPE_EXPORT, stockBalanceRequestDTO.getType())) {
                        if (!DataUtil.isNullOrEmpty(lstSerialDB)) {
                            for (StockTransSerialDTO stockTransSerialDTO : lstSerialDB) {
                                quantitySerialDB += stockTransSerialDTO.getQuantity();
                            }
                        }
                    } else {
                        if (!DataUtil.isNullOrEmpty(lstSerialDB)) {
                            StockTransSerialDTO stockTransSerialDTO = lstSerialDB.get(0);
                            Long ownerType = stockTransSerialDTO.getOwnerType();
                            String ownerName ;
                            if (DataUtil.safeEqual(Const.OWNER_TYPE.STAFF_LONG, ownerType)) {
                                staffDTO = staffService.findOne(stockTransSerialDTO.getOwnerId());
                                ownerName = staffDTO.getName();
                            } else {
                                ShopDTO shopDTOx = shopService.findOne(stockTransSerialDTO.getOwnerId());
                                ownerName = shopDTOx.getName();
                            }
                            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "validate.stock.balance.serial.existence", fromSerial, toSerial, ownerName);
                        } else {
                            quantitySerialDB = quantitySerial;
                        }
                    }
                }
                if (!DataUtil.safeEqual(quantitySerial, stockBalanceDetailDTO.getQuantity())) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "validate.stock.balance.quantity.not.equals.total.serial");
                }
                if (!DataUtil.safeEqual(quantitySerialDB, stockBalanceDetailDTO.getQuantity())) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "validate.stock.balance.quantity.not.equals.total.serial");
                }
            }
            lstProdOfferId.add(stockBalanceDetailDTO.getProdOfferId());
        }
        // Check mat hang da lap yeu cau chua.
        boolean isExistence = repository.checkStockBalanceDetail(stockBalanceRequestDTO.getOwnerType(), stockBalanceRequestDTO.getOwnerId(), lstProdOfferId);
        if (isExistence) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.balance.stock.request.validate.exsit");
        }
    }

    @Override
    public List<ProductOfferingTotalDTO> getProductOfferingExport(Long ownerId, Long ownerType, Long prodOfferId) throws LogicException, Exception {
        return repository.getProductOfferingExport(ownerType, ownerId, prodOfferId);
    }

    @Override
    public List<ProductOfferingTotalDTO> getProductOfferingImport(Long ownerId, Long ownerType, Long prodOfferId) throws LogicException, Exception {
        return repository.getProductOfferingImport(ownerType, ownerId, prodOfferId);
    }

    @Override
    public List<StockBalanceRequestDTO> searchStockBalanceRequest(StockBalanceRequestDTO stockBalanceRequestDTO) throws LogicException, Exception {
        return mapper.toDtoBean(repository.searchStockBalanceRequest(stockBalanceRequestDTO));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void doApproveStockBalanceRequest(StockBalanceRequestDTO stockBalanceRequestDTO) throws LogicException, Exception {
        StockBalanceRequestDTO requestDTO = findOne(stockBalanceRequestDTO.getStockRequestId());
        if (requestDTO == null) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "mn.stock.balance.valid.object");
        }
        if (stockBalanceRequestDTO.getStatus() == null || requestDTO.getStatus() == null || !Const.BalanceRequestStatus.create.toLong().equals(requestDTO.getStatus())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "mn.stock.balance.valid.approveStatus");
        }
        if (DataUtil.isNullOrEmpty(stockBalanceRequestDTO.getUpdateUser())
                || DataUtil.isNullOrZero(stockBalanceRequestDTO.getStaffId())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "balance.vali.approveUser");
        }
        if (DataUtil.isNullOrEmpty(stockBalanceRequestDTO.getCreateUser())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "balance.vali.createUser");
        }

        StaffDTO staffDTO = staffService.findOne(stockBalanceRequestDTO.getStaffId());
        if (staffDTO == null
                || DataUtil.isNullOrEmpty(staffDTO.getStatus())
                || !DataUtil.safeEqual(staffDTO.getStatus(), Const.STATUS_ACTIVE)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "balance.vali.approveUser");
        }
        if (stockBalanceRequestDTO.getStaffId() == null || repository.doValidateContraints(requestDTO.getOwnerId(), stockBalanceRequestDTO.getStaffId())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "balance.vali.subStock");
        }
        validateData(requestDTO);
        //
        if (Const.BalanceRequestStatus.approved.toLong().equals(stockBalanceRequestDTO.getStatus())) {
            StockTransVofficeDTO stockTransVofficeDTO = new StockTransVofficeDTO();
            //
            String vOfficeIds = DateUtil.dateToStringWithPattern(optionSetService.getSysdateFromDB(false), "yyMMdd") + DataUtil.customFormat(TEN_ZEZO, getSequence(em, STOCK_TRANS_VOFFICE_SEQ));
            //
            stockTransVofficeDTO.setStockTransVofficeId(vOfficeIds);
            stockTransVofficeDTO.setStatus(Const.STATUS.NO_ACTIVE);
            stockTransVofficeDTO.setRetry(0L);
            stockTransVofficeDTO.setAccountPass(stockBalanceRequestDTO.getAccountPass());
            stockTransVofficeDTO.setSignUserList(stockBalanceRequestDTO.getListEmailSign());
            stockTransVofficeDTO.setAccountName(stockBalanceRequestDTO.getAccountName());
            stockTransVofficeDTO.setCreateUserId(stockBalanceRequestDTO.getStaffId());
            stockTransVofficeDTO.setCreateDate(optionSetService.getSysdateFromDB(false));
            stockTransVofficeDTO.setLastModify(optionSetService.getSysdateFromDB(false));
            stockTransVofficeDTO.setStockTransActionId(stockTransActionService.getSequence());
            stockTransVofficeDTO.setActionCode(requestDTO.getRequestCode());
            stockTransVofficeDTO.setReceiptNo(requestDTO.getRequestCode());
            stockTransVofficeDTO.setPrefixTemplate(Const.STOCK_BALANCE.STOCK_BALANCE_PREFIX_TEMPLATE);
            //cap nhat lai stock_balance_request
            stockBalanceRequestDTO.setStockTransActionId(stockTransVofficeDTO.getStockTransActionId());
            stockBalanceRequestDTO.setStatus(Const.BalanceRequestStatus.approved.toLong());
            stockTransVofficeService.save(stockTransVofficeDTO);
        }
        stockBalanceRequestDTO.setUpdateDatetime(optionSetService.getSysdateFromDB(false));
        repository.save(mapper.toPersistenceBean(stockBalanceRequestDTO));
    }

    private void validateData(StockBalanceRequestDTO stockBalanceRequestDTO) throws LogicException, Exception {
        List<StockBalanceDetailDTO> stockBalanceDetailDTOs = stockBalanceDetailService.getListStockBalanceDetail(stockBalanceRequestDTO.getStockRequestId());
        if (DataUtil.isNullOrEmpty(stockBalanceDetailDTOs)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "balance.valid.requestDetail");
        }
        for (StockBalanceDetailDTO stockBalanceDetailDTO : stockBalanceDetailDTOs) {
            if (stockBalanceDetailDTO.getStockBalanceDetailId() == null || stockBalanceDetailDTO.getProdOfferId() == null) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "balance.valid.prodInfo");
            }
            if (DataUtil.isNullOrZero(stockBalanceDetailDTO.getQuantity()) || stockBalanceDetailDTO.getQuantity().compareTo(0L) < 0) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "export.order.stock.number.format.msg");
            }
            ProductOfferingDTO productOfferingDTO = productOfferingService.findOne(stockBalanceDetailDTO.getProdOfferId());
            if (productOfferingDTO == null || productOfferingDTO.getProductOfferTypeId() == null || productOfferingDTO.getStatus() == null || !productOfferingDTO.getStatus().equals(Const.STATUS_ACTIVE)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "balance.valid.prodInfo");
            }
            if (productOfferingDTO.getCheckSerial() != null && productOfferingDTO.getCheckSerial().equals(Const.SERIAL_STATUS)) {
                List<StockBalanceSerialDTO> listSerial = stockBalanceSerialService.getListStockBalanceSerialDTO(stockBalanceDetailDTO.getStockBalanceDetailId());
                if (DataUtil.isNullOrEmpty(listSerial)) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "balance.valid.serialDetail", productOfferingDTO.getName());
                }
                if (Const.ConfigIDCheck.HANDSET.validate(productOfferingDTO.getProductOfferTypeId())) {
                    Long quantity = new Long(listSerial.size());
                    if (quantity.compareTo(productOfferingDTO.getQuantity()) != 0) {
                        throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "balance.valid.serialQuantity", productOfferingDTO.getName());
                    }
                    for (StockBalanceSerialDTO stockBalanceSerialDTO : listSerial) {
                        if (DataUtil.isNullOrEmpty(stockBalanceSerialDTO.getFromSerial())
                                || DataUtil.isNullOrEmpty(stockBalanceSerialDTO.getToSerial())
                                || !stockBalanceSerialDTO.getFromSerial().equals(stockBalanceSerialDTO.getToSerial())) {
                            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "balance.valid.serialInfo", productOfferingDTO.getName());
                        }
                    }
                } else {
                    // mac dinh neu serial khong phai kieu bigdecimal thi de exception
                    Long quantity = 0L;
                    for (StockBalanceSerialDTO stockBalanceSerialDTO : listSerial) {
                        BigDecimal from = new BigDecimal(stockBalanceSerialDTO.getFromSerial());
                        BigDecimal to = new BigDecimal(stockBalanceSerialDTO.getToSerial());
                        if (from.compareTo(to) > 0) {
                            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "balance.valid.serialInfo", productOfferingDTO.getName());
                        }
                        quantity += to.add(from.negate()).longValue() + 1L;
                    }
                    if (quantity.compareTo(stockBalanceDetailDTO.getQuantity()) != 0) {
                        throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "balance.valid.serialQuantity", productOfferingDTO.getName());
                    }
                }
            }
        }
    }

    @WebMethod
    public Long getMaxId() throws LogicException, Exception {
        return repository.getMaxId();
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(StockBalanceRequestDTO dto) throws Exception {
        repository.save(mapper.toPersistenceBean(dto));
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRequest(StockBalanceRequestDTO stockBalanceRequestDTO) throws LogicException, Exception {
        processStock(stockBalanceRequestDTO);
    }

    public void processStock(StockBalanceRequestDTO stockBalanceRequestDTO) throws LogicException, Exception {

        StockTransDTO stockTransDTO = doSaveStockTrans(stockBalanceRequestDTO);
        stockTransDTO.setUserCreate(stockBalanceRequestDTO.getCreateUser());
        stockTransDTO.setStaffId(stockBalanceRequestDTO.getCreateStaffId());

        doSaveStockTransAction(stockTransDTO, stockBalanceRequestDTO);
        doSaveStockTransDetail(stockTransDTO, stockBalanceRequestDTO);
    }

    public StockTransDTO doSaveStockTrans(StockBalanceRequestDTO stockBalanceRequestDTO) throws LogicException, Exception {
        Date sysdate = DbUtil.getSysDate(em);
        StockTransDTO stockTransDTO = new StockTransDTO();
        if (DataUtil.safeEqual(Const.STOCK_BALANCE.EXPORT, stockBalanceRequestDTO.getType())) {
            //Xuat hang can kho : xuat cho doi tac khac
//            stockTransDTO.setStockTransId(stockBalanceRequestDTO.getStockTransId());
            stockTransDTO.setFromOwnerId(stockBalanceRequestDTO.getOwnerId());
            stockTransDTO.setFromOwnerType(stockBalanceRequestDTO.getOwnerType());
            stockTransDTO.setToOwnerId(Const.OTHER_PARTNER_ID);
            stockTransDTO.setToOwnerType(Const.OWNER_TYPE.PARTNER_LONG);
            stockTransDTO.setStockTransType(Const.STOCK_TRANS_TYPE.EXPORT);
            stockTransDTO.setReasonId(Const.REASON_ID.EXP_BALANCE);
            stockTransDTO.setNote(GetTextFromBundleHelper.getText("stock.balance.exp.partner"));
        } else {
            //Nhap hang can kho : nhap hang tu doi tac khac
//            stockTransDTO.setStockTransId(stockBalanceRequestDTO.getStockTransId());
            stockTransDTO.setFromOwnerId(Const.OTHER_PARTNER_ID);
            stockTransDTO.setFromOwnerType(Const.OWNER_TYPE.PARTNER_LONG);
            stockTransDTO.setToOwnerId(stockBalanceRequestDTO.getOwnerId());
            stockTransDTO.setToOwnerType(stockBalanceRequestDTO.getOwnerType());
            stockTransDTO.setStockTransType(Const.STOCK_TRANS_TYPE.IMPORT);
            stockTransDTO.setReasonId(Const.REASON_ID.IMP_BALANCE);
            stockTransDTO.setNote(GetTextFromBundleHelper.getText("stock.balance.imp.partner"));
        }
        stockTransDTO.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
        stockTransDTO.setCreateDatetime(sysdate);

        return stockTransService.save(stockTransDTO);
    }

    public void doSaveStockTransDetail(StockTransDTO stockTransDTO, StockBalanceRequestDTO stockBalanceRequestDTO) throws LogicException, Exception {
        List<StockBalanceDetailDTO> lstStockBalanceDetailDTO = stockBalanceDetailService.getListStockBalanceDetail(stockBalanceRequestDTO.getStockRequestId()); //Query de lay lstStockBalanceDetailDTO
        Long changeStock ;
        Long ownerId;
        Long ownerType ;
        if (DataUtil.safeEqual(Const.STOCK_TRANS_TYPE.EXPORT, stockTransDTO.getStockTransType())) {
            changeStock = -1L;
            ownerId = stockTransDTO.getFromOwnerId();
            ownerType = stockTransDTO.getFromOwnerType();
        } else {
            changeStock = 1L;
            ownerId = stockTransDTO.getToOwnerId();
            ownerType = stockTransDTO.getToOwnerType();
        }
        for (StockBalanceDetailDTO stockBalanceDetailDTO : lstStockBalanceDetailDTO) {
            StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
            stockTransDetailDTO.setStockTransId(stockTransDTO.getStockTransId());
            stockTransDetailDTO.setProdOfferId(stockBalanceDetailDTO.getProdOfferId());
            stockTransDetailDTO.setStateId(1L);
            stockTransDetailDTO.setQuantity(stockBalanceDetailDTO.getQuantity());
            stockTransDetailDTO.setCreateDatetime(stockTransDTO.getCreateDatetime());
            StockTransDetailDTO stockTransDetail = stockTransDetailService.save(stockTransDetailDTO);
            //Goi ham save bang stock_trans_serial
            List<StockBalanceSerialDTO> lstStockBalanceSerialDTO = stockBalanceSerialService.getListStockBalanceSerialDTO(stockBalanceDetailDTO.getStockBalanceDetailId());//Query de lay lstStockBalanceSerialDTO
            if (!DataUtil.isNullOrEmpty(lstStockBalanceSerialDTO)) {
                doSaveStockTransSerial(stockTransDTO, stockTransDetail, lstStockBalanceSerialDTO);
            }

            //Cap nhat stock_total, stock_total_audit
            StockTotalMessage result = stockTotalService.changeStockTotal(ownerId, ownerType, stockTransDetailDTO.getProdOfferId(), stockTransDetailDTO.getStateId(),
                    changeStock * stockTransDetailDTO.getQuantity(), changeStock * stockTransDetailDTO.getQuantity(), 0L, stockTransDTO.getStaffId(), stockTransDTO.getReasonId(), stockTransDTO.getStockTransId(),
                    stockTransDTO.getCreateDatetime(), "", Const.STOCK_TRANS_TYPE.UNIT, Const.SourceType.STOCK_TRANS);
            if (result.isSuccess()) {
                continue;
            } else {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, result.getKeyMsg());
            }
        }
    }

    public void doSaveStockTransSerial(StockTransDTO stockTransDTO, StockTransDetailDTO stockTransDetailDTO, List<StockBalanceSerialDTO> lstRequestBalanceSerialDTO) throws LogicException, Exception {
        for (StockBalanceSerialDTO requestBalanceSerialDTO : lstRequestBalanceSerialDTO) {
            StockTransSerialDTO stockTransSerialDTO = new StockTransSerialDTO();
            stockTransSerialDTO.setStockTransDetailId(stockTransDetailDTO.getStockTransDetailId());
            stockTransSerialDTO.setStockTransId(stockTransDetailDTO.getStockTransId());
            stockTransSerialDTO.setProdOfferId(stockTransDetailDTO.getProdOfferId());
            stockTransSerialDTO.setStateId(stockTransDetailDTO.getStateId());
            stockTransSerialDTO.setQuantity(stockTransDetailDTO.getQuantity());
            stockTransSerialDTO.setFromSerial(requestBalanceSerialDTO.getFromSerial());
            stockTransSerialDTO.setToSerial(requestBalanceSerialDTO.getToSerial());
            stockTransSerialDTO.setCreateDatetime(stockTransDetailDTO.getCreateDatetime());
            stockTransSerialService.save(stockTransSerialDTO);

            if (DataUtil.safeEqual(Const.STOCK_TRANS_TYPE.EXPORT, stockTransDTO.getStockTransType())) {
                //neu la giao dich xuat, update trang thai serial thanh da ban
                int result = stockTransSerialService.updateStatusStockSerial(stockTransDTO.getFromOwnerId(), stockTransDTO.getFromOwnerType(), stockTransDetailDTO.getProdOfferId(),
                        stockTransSerialDTO.getFromSerial(), stockTransSerialDTO.getToSerial(), Const.STATE_STATUS.SALE, Const.STATE_STATUS.NEW);
                if (result <= 0 || Long.valueOf(result) < stockTransDetailDTO.getQuantity()) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.balance.update.serial.error");
                }
            } else {
                //neu la giao dich nhap, thuc hien tao dai serial va nhap thang vao kho don vi
                int result = stockTransSerialService.updateStockBalance(stockTransDTO.getToOwnerId(), stockTransDTO.getToOwnerType(), stockTransDetailDTO.getProdOfferId(), stockTransDetailDTO.getStateId(),
                        stockTransSerialDTO.getFromSerial(), stockTransSerialDTO.getToSerial(), stockTransDTO.getStockTransId(), Const.STATE_STATUS.NEW, Const.STATE_STATUS.SALE);
                if (result <= 0 || Long.valueOf(result) < stockTransDetailDTO.getQuantity()) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.balance.update.serial.error");
                }
            }
        }
    }

    public void doSaveStockTransAction(StockTransDTO stockTransDTO, StockBalanceRequestDTO stockBalanceRequestDTO) throws LogicException, Exception {
        StaffDTO staff = staffService.findOne(stockTransDTO.getStaffId());
        ShopDTO shop = shopService.findOne(staff.getShopId());
        staff.setShopCode(shop.getShopCode());
        String transCode = "";
        if (DataUtil.safeEqual(Const.STOCK_TRANS_TYPE.EXPORT, stockTransDTO.getStockTransType())) {
//            transCode = staffService.getTransCode(Const.STOCK_TRANS.TRANS_CODE_PX, Const.STOCK_TRANS_TYPE.EXPORT, staff);
//            Long num = 0L;
//            num = staff.getStockNum() != null ? staff.getStockNum() : num;
//            String actionCodeShop = Const.STOCK_TRANS.TRANS_CODE_PX + DataUtil.customFormat("000000", num + 1);
            staff.setStockNum(DataUtil.safeToLong(staff.getStockNum()) + 1);
            StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
            stockTransActionDTO.setStockTransActionId(stockBalanceRequestDTO.getStockTransActionId());
            stockTransActionDTO.setStockTransId(stockTransDTO.getStockTransId());
            stockTransActionDTO.setActionCode(transCode);

            stockTransActionDTO.setStatus(Const.STOCK_TRANS_STATUS.EXPORT_NOTE);
            stockTransActionDTO.setCreateUser(stockTransDTO.getUserCreate());
            stockTransActionDTO.setActionStaffId(stockTransDTO.getStaffId());
            stockTransActionDTO.setCreateDatetime(stockTransDTO.getCreateDatetime());
            stockTransActionDTO.setCreateUser(stockTransDTO.getUserCreate());
            stockTransActionDTO.setNote(stockTransDTO.getNote());
            stockTransActionDTO.setActionCodeShop(stockBalanceRequestDTO.getRequestCode());
            stockTransActionService.save(stockTransActionDTO);

            //increase stockNum
//            staff.setStockNum(DataUtil.safeToLong(staff.getStockNum()) + 1);
//            staffService.save(staff);

            stockTransActionDTO.setStockTransActionId(null);
            stockTransActionDTO.setStatus(Const.STOCK_TRANS_STATUS.EXPORTED);
            stockTransActionDTO.setActionCode(null);
            stockTransActionDTO.setNote(null);
            stockTransActionDTO.setActionCodeShop(null);
            stockTransActionService.save(stockTransActionDTO);

            stockTransActionDTO.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
            stockTransActionService.save(stockTransActionDTO);
        } else {
//            transCode = staffService.getTransCode(Const.STOCK_TRANS.TRANS_CODE_PN, Const.STOCK_TRANS_TYPE.IMPORT, staff);
//            Long num = 0L;
//            num = staff.getStockNum() != null ? staff.getStockNum() : num;
//            String actionCodeShop = Const.STOCK_TRANS.TRANS_CODE_PN + DataUtil.customFormat("000000", num + 1);
//            staff.setStockNum(DataUtil.safeToLong(staff.getStockNum()) + 1);
            StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
            stockTransActionDTO.setStockTransActionId(stockBalanceRequestDTO.getStockTransActionId());
            stockTransActionDTO.setStockTransId(stockTransDTO.getStockTransId());
            stockTransActionDTO.setActionCode(transCode);
            stockTransActionDTO.setStatus(Const.STOCK_TRANS_STATUS.IMPORT_NOTE);
            stockTransActionDTO.setCreateUser(stockTransDTO.getUserCreate());
            stockTransActionDTO.setActionStaffId(stockTransDTO.getStaffId());
            stockTransActionDTO.setCreateDatetime(stockTransDTO.getCreateDatetime());
            stockTransActionDTO.setCreateUser(stockTransDTO.getUserCreate());
            stockTransActionDTO.setNote(stockTransDTO.getNote());
            stockTransActionDTO.setActionCodeShop(stockBalanceRequestDTO.getRequestCode());
            stockTransActionService.save(stockTransActionDTO);

            //increase stockNum
//            staff.setStockNumImp(DataUtil.safeToLong(staff.getStockNumImp()) + 1);
//            staffService.save(staff);

            stockTransActionDTO.setStockTransActionId(null);
            stockTransActionDTO.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
            stockTransActionDTO.setActionCode(null);
            stockTransActionDTO.setNote(null);
            stockTransActionDTO.setActionCodeShop(null);
            stockTransActionService.save(stockTransActionDTO);
        }


    }
}
