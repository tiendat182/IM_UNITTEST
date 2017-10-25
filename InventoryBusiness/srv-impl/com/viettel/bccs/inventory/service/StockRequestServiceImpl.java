package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.im1.dto.StockModelIm1DTO;
import com.viettel.bccs.im1.dto.StockTotalAuditIm1DTO;
import com.viettel.bccs.im1.dto.StockTotalIm1DTO;
import com.viettel.bccs.im1.dto.StockTypeIm1DTO;
import com.viettel.bccs.im1.service.StockTotalAuditIm1Service;
import com.viettel.bccs.im1.service.StockTotalIm1Service;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.model.OptionSetValue;
import com.viettel.bccs.inventory.model.StockRequest;
import com.viettel.bccs.inventory.model.StockTotalAudit;
import com.viettel.bccs.inventory.repo.OptionSetValueRepo;
import com.viettel.bccs.inventory.repo.StockRequestRepo;
import com.viettel.bccs.inventory.service.BaseStock.BaseStockService;
import com.viettel.bccs.inventory.service.common.IMServiceUtil;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DateUtil;
import com.viettel.fw.common.util.ErrorCode;
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
import javax.persistence.Query;
import java.math.BigInteger;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StockRequestServiceImpl extends BaseStockService implements StockRequestService {

    private final BaseMapper<StockRequest, StockRequestDTO> mapper = new BaseMapper<>(StockRequest.class, StockRequestDTO.class);

    private static final String STOCK_TRANS_VOFFICE_SEQ = "STOCK_TRANS_VOFFICE_SEQ";
    private static final String TEN_ZEZO = "0000000000";

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM_LIB)
    private EntityManager emLib;

    @Autowired
    private OptionSetValueRepo optionSetValueRepo;

    @Autowired
    private StockTransService stockTransService;
    @Autowired
    private StockTransActionService stockTransActionService;
    @Autowired
    private StockTransDetailService stockTransDetailService;
    @Autowired
    private StockTransSerialService stockTransSerialService;
    @Autowired
    private StockTotalService stockTotalService;
    @Autowired
    private StockTotalIm1Service stockTotalIm1Service;
    @Autowired
    private ProductOfferingService productOfferingService;
    @Autowired
    private OptionSetValueService optionSetValueService;
    @Autowired
    private SignFlowDetailService signFlowDetailService;
    @Autowired
    private StockTransVofficeService stockTransVofficeService;
    @Autowired
    private StockTotalAuditService stockTotalAuditService;
    @Autowired
    private StaffService staffService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private StockTotalAuditIm1Service stockTotalAuditIm1Service;

    @Autowired
    private StockRequestRepo repository;
    public static final Logger logger = Logger.getLogger(StockRequestService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public StockRequestDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<StockRequestDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<StockRequestDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage create(StockRequestDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(StockRequestDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @Override
    public String getRequest() throws Exception {
        return "RC_" + repository.getMaxId();
    }

    @Override
    public List<StockRequestDTO> findStockRequestByCode(String requestCode) throws Exception {
        List<FilterRequest> requests = Lists.newArrayList(new FilterRequest(StockRequest.COLUMNS.REQUESTCODE.name(), FilterRequest.Operator.EQ, requestCode));
        return findByFilter(requests);
    }

    @Override
    public List<StockRequestDTO> getLsSearchStockRequest(String requestCode, Date fromDate, Date toDate, String status, Long shopIdLogin, Long ownerId, Long ownerType) throws Exception {
        return repository.getLsSearchStockRequest(requestCode, fromDate, toDate, status, shopIdLogin, ownerId, ownerType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void excuteRequestTrans(StockRequestDTO stockRequestDTO) throws LogicException, Exception {
        if (stockRequestDTO == null) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "change.request.doa.empty");
        }
        if (stockRequestDTO.getSignOfficeDTO() == null || DataUtil.isNullOrEmpty(stockRequestDTO.getSignOfficeDTO().getUserName())
                || DataUtil.isNullOrEmpty(stockRequestDTO.getSignOfficeDTO().getPassWord())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "change.request.doa.signOffice.empty");
        }
        if (DataUtil.isNullOrEmpty(stockRequestDTO.getStockTransDetailDTOs())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "change.request.doa.product.empty");
        }

        //check ton tai request code
        List<StockRequestDTO> stockRequestDTOs = findStockRequestByCode(stockRequestDTO.getRequestCode());
        if (!DataUtil.isNullOrEmpty(stockRequestDTOs)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "change.request.doa.requestCode.duplicate", stockRequestDTO.getRequestCode());
        }
        //check ton tai ma cua hang, ma nhan vien
        if (stockRequestDTO.getOwnerId() != null) {
            if (Const.OWNER_TYPE.SHOP_LONG.equals(stockRequestDTO.getOwnerType())) {
                ShopDTO shopDTO = shopService.findOne(stockRequestDTO.getOwnerId());
                if (shopDTO == null || !Const.STATUS_ACTIVE.equals(shopDTO.getStatus())) {
                    throw new LogicException("04", "warranty.create.underlying.fromOwnerId.exp.shop.notfound", stockRequestDTO.getOwnerCode());
                }
            } else if (Const.OWNER_TYPE.STAFF_LONG.equals(stockRequestDTO.getOwnerType())) {
                StaffDTO staffDTO = staffService.findOne(stockRequestDTO.getOwnerId());
                if (staffDTO == null || !Const.STATUS_ACTIVE.equals(staffDTO.getStatus())) {
                    throw new LogicException("05", "warranty.create.underlying.fromOwnerId.exp.staff.notfound", stockRequestDTO.getOwnerCode());
                }
            }
        }

        Date sysdate = getSysDate(em);
        //them moi stock_request

        StockRequestDTO requestDTOInsert = DataUtil.cloneBean(stockRequestDTO);
        requestDTOInsert.setCreateDatetime(sysdate);
        requestDTOInsert.setUpdateDatetime(sysdate);
        requestDTOInsert.setRequestType(1L);
        requestDTOInsert.setStatus(Const.REQUEST_STATUS.CREATE_REQUEST);

        //them moi stock_trans
        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setFromOwnerId(requestDTOInsert.getOwnerId());
        stockTransDTO.setFromOwnerType(requestDTOInsert.getOwnerType());
        stockTransDTO.setToOwnerId(null);
        stockTransDTO.setToOwnerType(Const.OWNER_TYPE.CUSTOMER_LONG);
        stockTransDTO.setCreateDatetime(sysdate);
        stockTransDTO.setStockTransType(Const.STOCK_TRANS_TYPE.EXPORT);
        stockTransDTO.setStatus(Const.STOCK_TRANS_STATUS.EXPORTED);
        StockTransDTO stockTransNew = stockTransService.save(stockTransDTO);

        requestDTOInsert.setStockTransId(stockTransNew.getStockTransId());
        repository.save(mapper.toPersistenceBean(requestDTOInsert));

        //tao moi action status = 2
        String actionCode = "PX_" + stockTransNew.getStockTransId();
        StockTransActionDTO stockActionDTO = new StockTransActionDTO();
        stockActionDTO.setActionCode(actionCode);
        stockActionDTO.setStockTransId(stockTransNew.getStockTransId());
        stockActionDTO.setStatus(Const.STOCK_TRANS_STATUS.EXPORT_NOTE);
        stockActionDTO.setCreateUser(requestDTOInsert.getCreateUser());
        stockActionDTO.setCreateDatetime(sysdate);
        stockActionDTO.setActionStaffId(requestDTOInsert.getActionStaffId());
        stockActionDTO.setSignCaStatus(Const.SIGN_VOFFICE);
        StockTransActionDTO stockTransActionDTONew = stockTransActionService.save(stockActionDTO);

        //tao moi action status = 3
        stockActionDTO.setStatus(Const.STOCK_TRANS_STATUS.EXPORTED);
        stockActionDTO.setActionCode(null);
        stockActionDTO.setSignCaStatus(null);
        stockTransActionService.save(stockActionDTO);

        boolean isCheckIm1 = false;
        List<OptionSetValueDTO> lstConfigEnableBccs1 = optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1");
        if (!DataUtil.isNullOrEmpty(lstConfigEnableBccs1) && !DataUtil.isNullObject(lstConfigEnableBccs1.get(0).getValue())) {
            isCheckIm1 = Const.ENABLE_UPDATE_BCCS1.equals(DataUtil.safeToLong(lstConfigEnableBccs1.get(0).getValue()));
        }

        //ky vOffice
        doSignVoffice(stockTransNew, stockRequestDTO.getSignOfficeDTO(), stockTransActionDTONew);

        StockTotalAuditDTO totalAuditInput = new StockTotalAuditDTO();
        totalAuditInput.setUserId(stockActionDTO.getActionStaffId());
        totalAuditInput.setCreateDate(stockTransNew.getCreateDatetime());
        totalAuditInput.setTransId(stockTransNew.getStockTransId());
        totalAuditInput.setTransCode(actionCode);
        totalAuditInput.setTransType(DataUtil.safeToLong(stockTransDTO.getStockTransType()));
        totalAuditInput.setUserCode(requestDTOInsert.getCreateUser());
        totalAuditInput.setStatus(Long.valueOf(Const.STATUS_ACTIVE));

        StockTotalDTO totalDTOSearch = new StockTotalDTO();
        totalDTOSearch.setOwnerId(stockTransDTO.getFromOwnerId());
        totalDTOSearch.setOwnerType(stockTransDTO.getFromOwnerType());


        for (StockTransDetailDTO stockDetail : stockRequestDTO.getStockTransDetailDTOs()) {
            //check so luong mat hang
            if (DataUtil.isNullOrZero(stockDetail.getQuantity())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.serial.quantity.limit.zezo");
            }
            //check ton tai mat hang
            ProductOfferingDTO productOfferingDTO = productOfferingService.findOne(stockDetail.getProdOfferId());
            if (productOfferingDTO == null || productOfferingDTO.getProductOfferTypeId() == null || productOfferingDTO.getStatus() == null || !productOfferingDTO.getStatus().equals(Const.STATUS_ACTIVE)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "balance.valid.prodInfo");
            }
            stockDetail.setStockTransId(stockTransNew.getStockTransId());
            stockDetail.setCreateDatetime(sysdate);
            stockDetail.setStateId(Const.GOODS_STATE.NEW);
            stockDetail.setPrice(null);
            stockDetail.setAmount(null);
            StockTransDetailDTO detailInsert = stockTransDetailService.save(stockDetail);

            totalDTOSearch.setProdOfferId(stockDetail.getProdOfferId());
            totalDTOSearch.setStateId(stockDetail.getStateId());
            totalDTOSearch.setStatus(DataUtil.safeToLong(Const.STATUS.ACTIVE));


            //tru kho stock_total
            //validate so luong dap ung kho xuat
            Long availAbleQuantity = getAvailableQuantity(stockDetail.getOwnerID(), stockDetail.getOwnerType(), stockDetail.getProdOfferId(), stockDetail.getStateId());
            if (stockDetail.getQuantity() > DataUtil.safeToLong(availAbleQuantity)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "export.order.stock.quantity.msg");
            }
            debitTotal(totalDTOSearch, totalAuditInput, stockTransDTO.getFromOwnerType(), stockTransDTO.getFromOwnerId(), stockDetail.getProdOfferId(),
                    stockDetail.getStateId(), stockDetail.getQuantity(), stockActionDTO.getCreateDatetime(), isCheckIm1);

            if (Const.PRODUCT_OFFERING.CHECK_SERIAL.equals(stockDetail.getCheckSerial())) {

                if (DataUtil.isNullOrEmpty(stockDetail.getLstSerial())) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "mn.stock.utilities.look.serial.not.empty.serial");
                }
                for (StockTransSerialDTO stockSerialNew : stockDetail.getLstStockTransSerial()) {
                    stockSerialNew.setProdOfferId(stockDetail.getProdOfferId());
                    stockSerialNew.setStateId(Const.GOODS_STATE.NEW);
                    stockSerialNew.setStockTransId(stockTransNew.getStockTransId());
                    stockSerialNew.setCreateDatetime(sysdate);
                    stockSerialNew.setStockTransDetailId(detailInsert.getStockTransDetailId());
                    stockTransSerialService.save(stockSerialNew);
                    //cap nhap stock_x bccs2
                    updateStockX(stockSerialNew.getFromSerial(), stockSerialNew.getToSerial(), stockTransDTO.getCreateDatetime(), stockDetail.getProdOfferId(),
                            stockDetail.getProdOfferTypeId(), stockTransDTO.getFromOwnerType(), stockTransDTO.getFromOwnerId(), isCheckIm1);
                }
            }
        }

    }

    public void doSignVoffice(StockTransDTO stockTransDTO, SignOfficeDTO signOfficeDTO, StockTransActionDTO stockTransActionDTO) throws LogicException, Exception {

        List<SignFlowDetailDTO> lstSignFlowDetail = signFlowDetailService.findBySignFlowId(signOfficeDTO.getSignFlowId());

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

            StockTransVofficeDTO stockTransVoffice = new StockTransVofficeDTO();
            String stVofficeId = DateUtil.dateToStringWithPattern(stockTransDTO.getCreateDatetime(), "yyMMdd") + DataUtil.customFormat(TEN_ZEZO, getSequence(em, STOCK_TRANS_VOFFICE_SEQ));
            stockTransVoffice.setStockTransVofficeId(stVofficeId);
            stockTransVoffice.setAccountName(signOfficeDTO.getUserName().trim());
            stockTransVoffice.setAccountPass(signOfficeDTO.getPassWord());
            stockTransVoffice.setSignUserList(lstUser.toString());
            stockTransVoffice.setCreateDate(stockTransDTO.getCreateDatetime());
            stockTransVoffice.setLastModify(stockTransDTO.getCreateDatetime());
            stockTransVoffice.setCreateUserId(stockTransActionDTO.getActionStaffId());
            stockTransVoffice.setStatus(Const.STATUS.NO_ACTIVE);
            stockTransVoffice.setStockTransActionId(stockTransActionDTO.getStockTransActionId());
            stockTransVoffice.setPrefixTemplate("DOA_TRANSFER");
            stockTransVoffice.setActionCode(stockTransActionDTO.getActionCode());
            stockTransVofficeService.save(stockTransVoffice);
        }
    }

    /**
     * ham tru kho
     *
     * @param ownerId
     * @param ownerType
     * @param prodOfferId
     * @param oldStateId
     * @throws Exception
     * @author thanhnt77
     */
    private void debitTotal(StockTotalDTO totalDTOSearch, StockTotalAuditDTO totalAuditInput, Long ownerType, Long ownerId, Long prodOfferId,
                            Long oldStateId, Long quantity, Date sysdate, boolean isCheckIm1) throws Exception {

        List<StockTotalDTO> lstStockTotal = stockTotalService.searchStockTotal(totalDTOSearch);
        if (!DataUtil.isNullOrEmpty(lstStockTotal) && lstStockTotal.size() > 1) {
            ProductOfferingDTO offeringDTO = productOfferingService.findOne(lstStockTotal.get(0).getProdOfferId());
            if (offeringDTO != null) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stockTrans.validate.stockTotal.empty", offeringDTO.getName());
            }
        }
        StockTotalDTO stockTotalDTO = lstStockTotal.get(0);

        StockTotalAuditDTO totalAuditInsert = DataUtil.cloneBean(totalAuditInput);

        totalAuditInsert.setTransId(totalAuditInput.getTransId());
        totalAuditInsert.setOwnerId(stockTotalDTO.getOwnerId());
        totalAuditInsert.setOwnerType(stockTotalDTO.getOwnerType());
        totalAuditInsert.setProdOfferId(stockTotalDTO.getProdOfferId());
        totalAuditInsert.setStateId(stockTotalDTO.getStateId());
        //current quantity

        totalAuditInsert.setCurrentQuantityBf(DataUtil.safeToLong(stockTotalDTO.getCurrentQuantity()));
        totalAuditInsert.setCurrentQuantity(-DataUtil.safeToLong(quantity));
        totalAuditInsert.setCurrentQuantityAf(DataUtil.safeToLong(stockTotalDTO.getCurrentQuantity()) - DataUtil.safeToLong(quantity));
        // available quantity
        totalAuditInsert.setAvailableQuantityBf(DataUtil.safeToLong(stockTotalDTO.getAvailableQuantity()));
        totalAuditInsert.setAvailableQuantity(-DataUtil.safeToLong(quantity));
        totalAuditInsert.setAvailableQuantityAf(DataUtil.safeToLong(stockTotalDTO.getAvailableQuantity()) - DataUtil.safeToLong(quantity));
        totalAuditInsert.setSourceType(Const.STOCK_TOTAL_AUDIT.SOURCE_TYPE_STOCK_TRANS);
        stockTotalAuditService.save(totalAuditInsert);

        //update stock_total
        String sql = " UPDATE stock_total  SET current_quantity  = current_quantity  - #quantity, available_quantity  = available_quantity - #quantity, state_id = 1,modified_date=#sysdate " +
                "WHERE owner_id = #ownerId AND owner_type = #onwerType AND prod_offer_id = #prodOfferId AND state_id = #oldStateId AND current_quantity >= #quantity AND available_quantity >= #quantity";
        Query queryObject = em.createNativeQuery(sql);
        queryObject.setParameter("ownerId", ownerId);
        queryObject.setParameter("onwerType", ownerType);
        queryObject.setParameter("prodOfferId", prodOfferId);
        queryObject.setParameter("oldStateId", oldStateId);
        queryObject.setParameter("quantity", quantity);
        queryObject.setParameter("sysdate", sysdate);
        int result = queryObject.executeUpdate();
        if (result == 0) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "export.order.stock.quantity.msg");
        }

        if (isCheckIm1) {
            debitTotalIm1(totalDTOSearch, totalAuditInput, ownerId, ownerType, prodOfferId, oldStateId, quantity, sysdate);
        }
    }

    /**
     * ham tru kho
     *
     * @param ownerId
     * @param ownerType
     * @param prodOfferId
     * @param oldStateId
     * @throws Exception
     * @author thanhnt77
     */
    private void debitTotalIm1(StockTotalDTO totalDTOSearch, StockTotalAuditDTO totalAuditInput, Long ownerId, Long ownerType, Long prodOfferId,
                               Long oldStateId, Long quantity, Date sysdate) throws Exception {
        //check kho cho StockTotalIm1DTO
        StockTotalIm1DTO total1DTOSearch = new StockTotalIm1DTO();
        total1DTOSearch.setStockModelId(totalDTOSearch.getProdOfferId());
        total1DTOSearch.setOwnerId(totalDTOSearch.getOwnerId());
        total1DTOSearch.setOwnerType(totalDTOSearch.getOwnerType());
        total1DTOSearch.setStateId(totalDTOSearch.getStateId());
        total1DTOSearch.setStatus(totalDTOSearch.getStatus());

        List<StockTotalIm1DTO> lstStockTotal = stockTotalIm1Service.findStockTotal(total1DTOSearch);

        if (!DataUtil.isNullOrEmpty(lstStockTotal) && lstStockTotal.size() > 1) {
            StockModelIm1DTO offeringDTO = stockTotalIm1Service.findOneStockModel(lstStockTotal.get(0).getStockModelId());
            if (offeringDTO != null) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans1.validate.limit.product.exist", offeringDTO.getName());
            }
        }

        //insert StockTotalAuditIm1DTO
        if (!DataUtil.isNullOrEmpty(lstStockTotal)) {
            StockTotalAuditIm1DTO totalAuditIm1DTO = new StockTotalAuditIm1DTO();
            totalAuditIm1DTO.setUserId(totalAuditInput.getUserId());
            totalAuditIm1DTO.setCreateDate(totalAuditInput.getCreateDate());
            totalAuditIm1DTO.setReasonId(totalAuditInput.getReasonId());
            totalAuditIm1DTO.setTransId(totalAuditInput.getTransId());
            totalAuditIm1DTO.setTransCode(totalAuditInput.getTransCode());
            totalAuditIm1DTO.setTransType(DataUtil.safeToLong(totalAuditInput.getTransType()));
            totalAuditIm1DTO.setOwnerId(lstStockTotal.get(0).getOwnerId());
            totalAuditIm1DTO.setOwnerType(lstStockTotal.get(0).getOwnerType());
            totalAuditIm1DTO.setStockModelId(lstStockTotal.get(0).getStockModelId());
            totalAuditIm1DTO.setStockModelName(lstStockTotal.get(0).getStockModelName());
            totalAuditIm1DTO.setStateId(lstStockTotal.get(0).getStateId());

            totalAuditIm1DTO.setQtyBf(DataUtil.safeToLong(lstStockTotal.get(0).getQuantity()));
            totalAuditIm1DTO.setQty(-DataUtil.safeToLong(quantity));
            totalAuditIm1DTO.setQtyAf(DataUtil.safeToLong(lstStockTotal.get(0).getQuantity()) - DataUtil.safeToLong(quantity));

            totalAuditIm1DTO.setQtyIssueBf(DataUtil.safeToLong(lstStockTotal.get(0).getQuantityIssue()));
            totalAuditIm1DTO.setQtyIssue(-DataUtil.safeToLong(quantity));
            totalAuditIm1DTO.setQtyIssueAf(DataUtil.safeToLong(lstStockTotal.get(0).getQuantityIssue()) - DataUtil.safeToLong(quantity));
            totalAuditIm1DTO.setSourceType(Const.STOCK_TOTAL_AUDIT.SOURCE_TYPE_STOCK_TRANS);
            StaffDTO staffDTO = staffService.findOne(totalAuditIm1DTO.getUserId());
            ShopDTO shopDTO = shopService.findOne(totalAuditIm1DTO.getOwnerId());
            if (staffDTO != null) {
                totalAuditIm1DTO.setUserCode(staffDTO.getStaffCode());
                totalAuditIm1DTO.setUserName(staffDTO.getName());
            }
            if (!DataUtil.isNullObject(shopDTO)) {
                totalAuditIm1DTO.setOwnerCode(shopDTO.getShopCode());
                totalAuditIm1DTO.setOwnerName(shopDTO.getName());
            }
            stockTotalAuditIm1Service.create(totalAuditIm1DTO);
        }

        String sql = " UPDATE bccs_im.stock_total  SET quantity = quantity - #quantity, quantity_issue = quantity_issue - #quantity,modified_date=#sysdate " +
                "WHERE owner_id = #ownerId AND owner_type = #onwerType AND stock_model_id = #stockModelId AND state_id = #oldStateId AND quantity >= #quantity AND quantity_issue >= #quantity";
        Query queryObject = emLib.createNativeQuery(sql);
        queryObject.setParameter("ownerId", ownerId);
        queryObject.setParameter("onwerType", ownerType);
        queryObject.setParameter("stockModelId", prodOfferId);
        queryObject.setParameter("oldStateId", oldStateId);
        queryObject.setParameter("quantity", quantity);
        queryObject.setParameter("sysdate", sysdate);
        int result = queryObject.executeUpdate();
        if (result == 0) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "export.order.stock.quantity.im1.msg");
        }
    }

    /**
     * ham tru kho
     *
     * @param ownerId
     * @param ownerType
     * @param prodOfferId
     * @param oldStateId
     * @throws Exception
     * @author thanhnt77
     */
    @Override
    public void addTotalTTBH(StockTotalDTO totalDTOSearch, StockTotalAuditDTO totalAuditInput, Long ownerType, Long ownerId, Long prodOfferId,
                             Long oldStateId, Long quantity, Date sysdate, boolean isCheckIm1) throws Exception {

        List<StockTotalDTO> lstStockTotal = stockTotalService.searchStockTotal(totalDTOSearch);

        StockTotalDTO stockTotalDTO;

        if (DataUtil.isNullOrEmpty(lstStockTotal)) {
            stockTotalDTO = DataUtil.cloneBean(totalDTOSearch);
            stockTotalDTO.setCurrentQuantity(quantity);
            stockTotalDTO.setAvailableQuantity(quantity);
            stockTotalDTO.setModifiedDate(sysdate);
            stockTotalService.save(stockTotalDTO);

            StockTotalAuditDTO totalAuditInsert = DataUtil.cloneBean(totalAuditInput);
            totalAuditInsert.setTransId(totalAuditInput.getTransId());
            totalAuditInsert.setOwnerId(stockTotalDTO.getOwnerId());
            totalAuditInsert.setOwnerType(stockTotalDTO.getOwnerType());
            totalAuditInsert.setProdOfferId(stockTotalDTO.getProdOfferId());
            totalAuditInsert.setStateId(stockTotalDTO.getStateId());
            //current quantity

            totalAuditInsert.setCurrentQuantityBf(0L);
            totalAuditInsert.setCurrentQuantity(DataUtil.safeToLong(stockTotalDTO.getCurrentQuantity()));
            totalAuditInsert.setCurrentQuantityAf(stockTotalDTO.getCurrentQuantity());
            // available quantity
            totalAuditInsert.setAvailableQuantityBf(0L);
            totalAuditInsert.setAvailableQuantity(DataUtil.safeToLong(stockTotalDTO.getAvailableQuantity()));
            totalAuditInsert.setAvailableQuantityAf(stockTotalDTO.getAvailableQuantity());
            totalAuditInsert.setSourceType(Const.STOCK_TOTAL_AUDIT.SOURCE_TYPE_STOCK_TRANS);
            stockTotalAuditService.save(totalAuditInsert);
        } else {
            if (!DataUtil.isNullOrEmpty(lstStockTotal) && lstStockTotal.size() > 1) {
                ProductOfferingDTO offeringDTO = productOfferingService.findOne(prodOfferId);
                if (offeringDTO != null) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stockTrans.validate.stockTotal.empty", offeringDTO.getName());
                }
            }
            stockTotalDTO = lstStockTotal.get(0);

            StockTotalAuditDTO totalAuditInsert = DataUtil.cloneBean(totalAuditInput);
            totalAuditInsert.setTransId(totalAuditInput.getTransId());
            totalAuditInsert.setOwnerId(stockTotalDTO.getOwnerId());
            totalAuditInsert.setOwnerType(stockTotalDTO.getOwnerType());
            totalAuditInsert.setProdOfferId(stockTotalDTO.getProdOfferId());
            totalAuditInsert.setStateId(stockTotalDTO.getStateId());
            //current quantity

            totalAuditInsert.setCurrentQuantityBf(DataUtil.safeToLong(stockTotalDTO.getCurrentQuantity()));
            totalAuditInsert.setCurrentQuantity(DataUtil.safeToLong(quantity));
            totalAuditInsert.setCurrentQuantityAf(DataUtil.safeToLong(stockTotalDTO.getCurrentQuantity()) + DataUtil.safeToLong(quantity));
            // available quantity
            totalAuditInsert.setAvailableQuantityBf(DataUtil.safeToLong(stockTotalDTO.getAvailableQuantity()));
            totalAuditInsert.setAvailableQuantity(DataUtil.safeToLong(quantity));
            totalAuditInsert.setAvailableQuantityAf(DataUtil.safeToLong(stockTotalDTO.getAvailableQuantity()) + DataUtil.safeToLong(quantity));
            totalAuditInsert.setSourceType(Const.STOCK_TOTAL_AUDIT.SOURCE_TYPE_STOCK_TRANS);
            stockTotalAuditService.save(totalAuditInsert);

            //update stock_total
            String sql = " UPDATE stock_total  SET current_quantity  = current_quantity  + #quantity, available_quantity  = available_quantity + #quantity, modified_date=#sysdate " +
                    " WHERE owner_id = #ownerId AND owner_type = #onwerType AND prod_offer_id = #prodOfferId AND state_id = #oldStateId AND current_quantity >= 0 AND available_quantity >= 0";
            Query queryObject = em.createNativeQuery(sql);
            queryObject.setParameter("ownerId", ownerId);
            queryObject.setParameter("onwerType", ownerType);
            queryObject.setParameter("prodOfferId", prodOfferId);
            queryObject.setParameter("oldStateId", oldStateId);
            queryObject.setParameter("quantity", quantity);
            queryObject.setParameter("sysdate", sysdate);
            int result = queryObject.executeUpdate();
            if (result == 0) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "export.order.stock.quantity.msg");
            }

        }


        if (isCheckIm1) {
            addTotalTTBHBCCS1(totalDTOSearch, totalAuditInput, ownerId, ownerType, prodOfferId, oldStateId, quantity, sysdate);
        }
    }

    /**
     * ham tru kho
     *
     * @param ownerId
     * @param ownerType
     * @param prodOfferId
     * @param oldStateId
     * @throws Exception
     * @author thanhnt77
     */
    private void addTotalTTBHBCCS1(StockTotalDTO totalDTOSearch, StockTotalAuditDTO totalAuditInput, Long ownerId, Long ownerType, Long prodOfferId,
                                   Long oldStateId, Long quantity, Date sysdate) throws Exception {
        //check kho cho StockTotalIm1DTO
        StockTotalIm1DTO total1DTOSearch = new StockTotalIm1DTO();
        total1DTOSearch.setStockModelId(totalDTOSearch.getProdOfferId());
        total1DTOSearch.setOwnerId(totalDTOSearch.getOwnerId());
        total1DTOSearch.setOwnerType(totalDTOSearch.getOwnerType());
        total1DTOSearch.setStateId(totalDTOSearch.getStateId());
        total1DTOSearch.setStatus(totalDTOSearch.getStatus());

        List<StockTotalIm1DTO> lstStockTotal = stockTotalIm1Service.findStockTotal(total1DTOSearch);
        if (DataUtil.isNullOrEmpty(lstStockTotal) || lstStockTotal.size() > 1) {
            StockModelIm1DTO offeringDTO = stockTotalIm1Service.findOneStockModel(prodOfferId);
            if (offeringDTO != null) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans1.validate.limit.product.exist", offeringDTO.getName());
            }
        }

        //insert StockTotalAuditIm1DTO
        StockTotalAuditIm1DTO totalAuditIm1DTO = new StockTotalAuditIm1DTO();
        totalAuditIm1DTO.setUserId(totalAuditInput.getUserId());
        totalAuditIm1DTO.setCreateDate(totalAuditInput.getCreateDate());
        totalAuditIm1DTO.setReasonId(totalAuditInput.getReasonId());
        totalAuditIm1DTO.setTransId(totalAuditInput.getTransId());
        totalAuditIm1DTO.setTransCode(totalAuditInput.getTransCode());
        totalAuditIm1DTO.setTransType(DataUtil.safeToLong(totalAuditInput.getTransType()));
        totalAuditIm1DTO.setOwnerId(lstStockTotal.get(0).getOwnerId());
        totalAuditIm1DTO.setOwnerType(lstStockTotal.get(0).getOwnerType());
        totalAuditIm1DTO.setStockModelId(lstStockTotal.get(0).getStockModelId());
        totalAuditIm1DTO.setStockModelName(lstStockTotal.get(0).getStockModelName());
        totalAuditIm1DTO.setStateId(lstStockTotal.get(0).getStateId());

        totalAuditIm1DTO.setQtyBf(DataUtil.safeToLong(lstStockTotal.get(0).getQuantity()));
        totalAuditIm1DTO.setQty(-DataUtil.safeToLong(quantity));
        totalAuditIm1DTO.setQtyAf(DataUtil.safeToLong(lstStockTotal.get(0).getQuantity()) + DataUtil.safeToLong(quantity));

        totalAuditIm1DTO.setQtyIssueBf(DataUtil.safeToLong(lstStockTotal.get(0).getQuantityIssue()));
        totalAuditIm1DTO.setQtyIssue(-DataUtil.safeToLong(quantity));
        totalAuditIm1DTO.setQtyIssueAf(DataUtil.safeToLong(lstStockTotal.get(0).getQuantityIssue()) + DataUtil.safeToLong(quantity));
        totalAuditIm1DTO.setSourceType(Const.STOCK_TOTAL_AUDIT.SOURCE_TYPE_STOCK_TRANS);
        StaffDTO staffDTO = staffService.findOne(totalAuditIm1DTO.getUserId());
        ShopDTO shopDTO = shopService.findOne(totalAuditIm1DTO.getOwnerId());
        if (staffDTO != null) {
            totalAuditIm1DTO.setUserCode(staffDTO.getStaffCode());
            totalAuditIm1DTO.setUserName(staffDTO.getName());
        }
        if (!DataUtil.isNullObject(shopDTO)) {
            totalAuditIm1DTO.setOwnerCode(shopDTO.getShopCode());
            totalAuditIm1DTO.setOwnerName(shopDTO.getName());
        }
        stockTotalAuditIm1Service.create(totalAuditIm1DTO);

        String sql = " UPDATE stock_total  SET quantity = quantity + #quantity, quantity_issue = quantity_issue + #quantity, modified_date=#sysdate " +
                " WHERE owner_id = #ownerId AND owner_type = #onwerType AND stock_model_id = #stockModelId AND state_id = #oldStateId AND quantity >= 0 AND quantity_issue >= 0";
        Query queryObject = emLib.createNativeQuery(sql);
        queryObject.setParameter("ownerId", ownerId);
        queryObject.setParameter("onwerType", ownerType);
        queryObject.setParameter("stockModelId", prodOfferId);
        queryObject.setParameter("oldStateId", oldStateId);
        queryObject.setParameter("quantity", quantity);
        queryObject.setParameter("sysdate", sysdate);
        int result = queryObject.executeUpdate();
        if (result == 0) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "export.order.stock.quantity.im1.msg");
        }
    }

    /**
     * ham update stock_kit
     *
     * @param fromSerial
     * @throws Exception
     * @author thanhnt77
     */
    private void updateStockX(String fromSerial, String toSerial, Date updateDateTime, Long prodOfferId, Long productOfferTypeId, Long ownerType, Long ownerId, boolean isCheckIm1) throws Exception {
        String tableName = IMServiceUtil.getTableNameByOfferType(productOfferTypeId);

        String sql = "update " + tableName + " set state_id = 1, status = 4, update_datetime=#updateDateTime   " +
                "where prod_offer_id=#prodOfferId and owner_type =#ownerType and owner_id = #ownerId and status= 1 ";
        if (Const.PRODUCT_OFFER_TYPE.PHONE.equals(productOfferTypeId) || Const.PRODUCT_OFFER_TYPE.ACCESSORIES.equals(productOfferTypeId)) {
            sql += " AND   serial >= #fromSerial AND   serial <= #toSerial ";
        } else {
            sql += " AND   to_number(serial) >= #fromSerial  AND   to_number(serial) <= #toSerial ";
        }

        Query query = em.createNativeQuery(sql);
        query.setParameter("updateDateTime", updateDateTime);
        query.setParameter("prodOfferId", prodOfferId);
        query.setParameter("ownerType", ownerType);
        query.setParameter("ownerId", ownerId);
        if (Const.PRODUCT_OFFER_TYPE.PHONE.equals(productOfferTypeId) || Const.PRODUCT_OFFER_TYPE.ACCESSORIES.equals(productOfferTypeId)) {
            query.setParameter("fromSerial", fromSerial);
            query.setParameter("toSerial", toSerial);
        } else {
            query.setParameter("fromSerial", fromSerial);
            query.setParameter("toSerial", toSerial);
        }
        int result = query.executeUpdate();
        if (result == 0) {
            List<OptionSetValue> lsOptionState = optionSetValueRepo.getLsOptionSetValueByCode(Const.OPTION_SET.GOODS_STATE);
            Map<String, String> mapState = lsOptionState.stream().collect(Collectors.toMap(OptionSetValue::getValue, OptionSetValue::getName));
            String[] params = new String[4];
            ProductOfferingDTO offeringDTO = productOfferingService.findOne(prodOfferId);
            params[0] = offeringDTO.getName();
            params[1] = mapState.get("1");
            params[2] = fromSerial;
            params[3] = toSerial;
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, getText("stockTrans.validate.quantity.serial"), params);
        }
        if (isCheckIm1) {
            updateStockXIM1(fromSerial, toSerial, prodOfferId, productOfferTypeId, ownerType, ownerId);
        }
    }

    /**
     * ham update stock_kit
     *
     * @param fromSerial
     * @param toSerial
     * @throws Exception
     * @author thanhnt77
     */
    private void updateStockXIM1(String fromSerial, String toSerial, Long stockModelId, Long productOfferTypeId, Long ownerType, Long ownerId) throws Exception {

        //ProductOfferingDTO offeringDTO = productOfferingService.findOne(stockDetail.getProdOfferId());
        StockModelIm1DTO offeringDTO = stockTotalIm1Service.findOneStockModel(stockModelId);
        if (offeringDTO == null) {
            throw new LogicException("09", "validate.stock.inspect.productOffering.bccs1.notFound", stockModelId);
        }
        if (!Const.PRODUCT_OFFERING.CHECK_SERIAL.equals(offeringDTO.getCheckSerial())) {
            throw new LogicException("09", "validate.stock.inspect.productOffering.bccs1.notFound", stockModelId);
        }

        StockTypeIm1DTO stockTypeIm1DTO = stockTotalIm1Service.findOneStockType(stockModelId);
        //lay lai tableName tu stockModelId
        String tableName ;

        if (!DataUtil.isNullObject(stockTypeIm1DTO)) {
            tableName = stockTypeIm1DTO.getTableName();
        } else {
            //bao loi ko ton tai mat hang tren BCCS1
            ProductOfferingDTO productOfferingDTO = productOfferingService.findOne(stockModelId);
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans1.validate.stockmodel.not.exist", productOfferingDTO.getName());
        }

        String sql = "update bccs_im." + tableName + " set state_id = 1, status = 4  " +
                "where stock_model_id=#stockModelId and owner_type =#ownerType and owner_id = #ownerId and status= 1 ";
        if (Const.PRODUCT_OFFER_TYPE.PHONE.equals(productOfferTypeId)) {
            sql += " AND   serial >= #fromSerial AND   serial <= #toSerial ";
        } else {
            sql += " AND   to_number(serial) >= #fromSerial  AND   to_number(serial) <= #toSerial ";
        }

        Query query = emLib.createNativeQuery(sql);
        query.setParameter("stockModelId", stockModelId);
        query.setParameter("ownerType", ownerType);
        query.setParameter("ownerId", ownerId);
        if (Const.PRODUCT_OFFER_TYPE.PHONE.equals(productOfferTypeId)) {
            query.setParameter("fromSerial", fromSerial);
            query.setParameter("toSerial", toSerial);
        } else {
            query.setParameter("fromSerial", fromSerial);
            query.setParameter("toSerial", toSerial);
        }
        int result = query.executeUpdate();
        if (result == 0) {
            List<OptionSetValue> lsOptionState = optionSetValueRepo.getLsOptionSetValueByCode(Const.OPTION_SET.GOODS_STATE);
            Map<String, String> mapState = lsOptionState.stream().collect(Collectors.toMap(OptionSetValue::getValue, OptionSetValue::getName));
            String[] params = new String[4];
            params[0] = offeringDTO.getName();
            params[1] = mapState.get("1");
            params[2] = fromSerial;
            params[3] = toSerial;
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, getText("stockTrans.validate.quantity.serial.bccs1"), params);
        }
    }


    /**
     * ham update stock_x cua TTBH
     *
     * @throws Exception
     * @author thanhnt77
     */
    @Override
    public void updateStockXTTBH(String fromSerial, String toSerial, Date updateDateTime, Long prodOfferId, Long productOfferTypeId, Long newOwnerType,
                                 Long newOwnerId, Long oldOwnerType, Long oldOwnerId, Long newStatus, Long oldStatus, Long oldStateId, boolean isCheckIm1) throws Exception {
        String tableName = IMServiceUtil.getTableNameByOfferType(productOfferTypeId);

        String sql = " update bccs_im." + tableName + " set status = #newStatus, update_datetime=#updateDateTime ";
        if (newOwnerId != null && newOwnerType != null) {
            sql += " ,owner_type =#newOwnerType, owner_id = #newOwnerId ";
        }
        sql += " where prod_offer_id=#prodOfferId and status= #oldStatus and state_id=#oldStateId ";

        if (Const.PRODUCT_OFFER_TYPE.PHONE.equals(productOfferTypeId) || Const.PRODUCT_OFFER_TYPE.ACCESSORIES.equals(productOfferTypeId)) {
            sql += " AND   serial >= #fromSerial AND   serial <= #toSerial ";
        } else {
            sql += " AND   to_number(serial) >= #fromSerial  AND   to_number(serial) <= #toSerial ";
        }

        if (oldOwnerType != null && oldOwnerId != null) {
            sql += " AND   owner_id = #oldOwnerId AND   owner_type = #oldOwnerType ";
        }

        Query query = em.createNativeQuery(sql);
        query.setParameter("updateDateTime", updateDateTime);
        query.setParameter("newStatus", newStatus);
        query.setParameter("prodOfferId", prodOfferId);
        query.setParameter("oldStateId", oldStateId);
        query.setParameter("oldStatus", oldStatus);

        if (newOwnerId != null && newOwnerType != null) {
            query.setParameter("newOwnerType", newOwnerType);
            query.setParameter("newOwnerId", newOwnerId);
        }
        if (oldOwnerType != null && oldOwnerId != null) {
            query.setParameter("oldOwnerType", oldOwnerType);
            query.setParameter("oldOwnerId", oldOwnerId);
        }


        if (Const.PRODUCT_OFFER_TYPE.PHONE.equals(productOfferTypeId) || Const.PRODUCT_OFFER_TYPE.ACCESSORIES.equals(productOfferTypeId)) {
            query.setParameter("fromSerial", fromSerial);
            query.setParameter("toSerial", toSerial);
        } else {
            query.setParameter("fromSerial", new BigInteger(fromSerial));
            query.setParameter("toSerial", new BigInteger(toSerial));

        }
        int result = query.executeUpdate();
        if (result == 0) {
            String[] params = new String[4];
            ProductOfferingDTO offeringDTO = productOfferingService.findOne(prodOfferId);
            params[0] = offeringDTO.getName();
            params[1] = IMServiceUtil.getStateName(oldStateId);
            params[2] = fromSerial;
            params[3] = toSerial;
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, getText("stockTrans.validate.quantity.serial"), params);
        }
        if (isCheckIm1) {
            updateStockXTTBHBCCS1(fromSerial, toSerial, prodOfferId, productOfferTypeId, newOwnerType, newOwnerId, oldStateId, newStatus, oldStatus);
        }
    }

    /**
     * ham update stock_x bccs1
     *
     * @throws Exception
     * @author thanhnt77
     */
    private void updateStockXTTBHBCCS1(String fromSerial, String toSerial, Long stockModelId, Long productOfferTypeId, Long newOwnerType, Long newOwnerId, Long oldStateId, Long newStatus, Long oldStatus) throws Exception {

        //ProductOfferingDTO offeringDTO = productOfferingService.findOne(stockDetail.getProdOfferId());
        StockModelIm1DTO offeringDTO = stockTotalIm1Service.findOneStockModel(stockModelId);
        if (offeringDTO == null) {
            throw new LogicException("09", "validate.stock.inspect.productOffering.bccs1.notFound", stockModelId);
        }
        if (!Const.PRODUCT_OFFERING.CHECK_SERIAL.equals(offeringDTO.getCheckSerial())) {
            throw new LogicException("09", "validate.stock.inspect.productOffering.bccs1.notFound", stockModelId);
        }

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

        String sql = " update bccs_im." + tableName + " set status = #newStatus";

        if (newOwnerId != null && newOwnerType != null) {
            sql += " ,owner_type =#newOwnerType, owner_id = #newOwnerId ";
        }

        sql += " where stock_model_id=#stockModelId and status= #oldStatus and state_id=#oldStateId ";

        if (Const.PRODUCT_OFFER_TYPE.PHONE.equals(productOfferTypeId)) {
            sql += " AND   serial >= #fromSerial AND   serial <= #toSerial ";
        } else {
            sql += " AND   to_number(serial) >= #fromSerial  AND   to_number(serial) <= #toSerial ";
        }

        Query query = emLib.createNativeQuery(sql);
        query.setParameter("newStatus", newStatus);
        if (newOwnerId != null && newOwnerType != null) {
            query.setParameter("newOwnerType", newOwnerType);
            query.setParameter("newOwnerId", newOwnerId);
        }
        query.setParameter("stockModelId", stockModelId);
        query.setParameter("oldStatus", oldStatus);
        query.setParameter("oldStateId", oldStateId);
        if (Const.PRODUCT_OFFER_TYPE.PHONE.equals(productOfferTypeId)) {
            query.setParameter("fromSerial", fromSerial);
            query.setParameter("toSerial", toSerial);
        } else {
            query.setParameter("fromSerial", new BigInteger(fromSerial));
            query.setParameter("toSerial", new BigInteger(toSerial));
        }

        int result = query.executeUpdate();
        if (result == 0) {
            String[] params = new String[4];
            params[0] = offeringDTO.getName();
            params[1] = IMServiceUtil.getStateName(oldStateId);
            params[2] = fromSerial;
            params[3] = toSerial;
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, getText("stockTrans.validate.quantity.serial.bccs1"), params);
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
    @Override
    @WebMethod
    public Long getAvailableQuantity(Long ownerId, Long ownerType, Long prodOfferId, Long stateId) throws Exception {
        StockTotalDTO stockTotalDTOs = stockTotalService.getStockTotalForProcessStock(ownerId, ownerType, prodOfferId, stateId);
        String params = MessageFormat.format("ownerId:{0}, ownerType:{1}, stateId:{2}, prodOfferId:{3}",
                ownerId, ownerType, prodOfferId, stateId);
        if (stockTotalDTOs == null) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "sell.store.no.info", params);
        }
        if (DataUtil.safeToLong(stockTotalDTOs.getAvailableQuantity()).compareTo(0L) <= 0 || DataUtil.safeToLong(stockTotalDTOs.getCurrentQuantity()).compareTo(0L) <= 0) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "warranty.create.underlying.currentQuantity");
        }
        return stockTotalDTOs.getAvailableQuantity();

    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage save(StockRequestDTO dto) throws Exception {
        repository.save(mapper.toPersistenceBean(dto));
        return new BaseMessage(true);
    }

    public StockRequestDTO getStockRequestByStockTransId(Long stockTransId) throws Exception {
        List<FilterRequest> lst = Lists.newArrayList();
        lst.add(new FilterRequest(StockRequest.COLUMNS.STOCKTRANSID.name(), FilterRequest.Operator.EQ, stockTransId));
        List<StockRequestDTO> lstStockRequestDTOs = findByFilter(lst);
        if (!DataUtil.isNullOrEmpty(lstStockRequestDTOs)) {
            return lstStockRequestDTOs.get(0);
        }
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public void excuteImportTTBGRequestTrans(Long requestId, String status, StaffDTO staffDTO) throws LogicException, Exception {

        Date sysdate = getSysDate(em);

        StockRequestDTO stockRequestDTO = findOne(requestId);

        if (stockRequestDTO == null) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "change.request.doa.notfound.request");
        }

        if (Const.STOCK_REQUEST_DOA.CANCEL.equals(stockRequestDTO.getStatus()) || Const.STOCK_REQUEST_DOA.APPROVE.equals(stockRequestDTO.getStatus())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "ws.delete.err.status");
        }

        Long stockTransIdSource = stockRequestDTO.getStockTransId();
        StockTransDTO stockTransDTOSource = stockTransService.findOne(stockTransIdSource);

        if (stockTransDTOSource == null) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "import.partner.valid.trans");
        }

        List<StockTransDetailDTO> lsDetailDTOSource = stockTransDetailService.findByStockTransId(stockTransIdSource);
        if (DataUtil.isNullOrEmpty(lsDetailDTOSource)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.import.to.partner.balance.stockDetail.empty");
        }

        StockTransActionDTO actionSearch = new StockTransActionDTO();
        actionSearch.setStockTransId(stockTransIdSource);
        actionSearch.setStatus(Const.STOCK_TRANS_STATUS.EXPORT_NOTE);

        StockTransActionDTO actionDTOSource = stockTransActionService.findStockTransActionDTO(actionSearch);

        for (StockTransDetailDTO detailSource : lsDetailDTOSource) {
            ProductOfferingDTO offeringDTO = productOfferingService.findOne(detailSource.getProdOfferId());
            if (offeringDTO ==  null || !Const.STATUS.ACTIVE.equals(offeringDTO.getStatus())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "validate.stock.inspect.productOffering.notFound");
            }
            detailSource.setCheckSerial(offeringDTO.getCheckSerial());
            detailSource.setProdOfferTypeId(offeringDTO.getProductOfferTypeId());
            if (Const.PRODUCT_OFFERING.CHECK_SERIAL.equals(offeringDTO.getCheckSerial())) {
                List<StockTransSerialDTO> lsSerialDTO = stockTransSerialService.findByStockTransDetailId(detailSource.getStockTransDetailId());
                if (DataUtil.isNullOrEmpty(lsSerialDTO)) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "input.list.product.file.empty.list.serial");
                }
                detailSource.setLstStockTransSerial(lsSerialDTO);
            }
        }

        stockRequestDTO.setUpdateDatetime(sysdate);
        stockRequestDTO.setUpdateUser(staffDTO.getStaffCode());

        boolean isCheckIm1 = false;
        List<OptionSetValueDTO> lstConfigEnableBccs1 = optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1");
        if (!DataUtil.isNullOrEmpty(lstConfigEnableBccs1) && !DataUtil.isNullObject(lstConfigEnableBccs1.get(0).getValue())) {
            isCheckIm1 = Const.ENABLE_UPDATE_BCCS1.equals(DataUtil.safeToLong(lstConfigEnableBccs1.get(0).getValue()));
        }

        //1.TH chap nhap yeu cau
        if (Const.STOCK_REQUEST_DOA.APPROVE.equals(status)) {
            //1.1.cap nhap stock_request status = 1: chap nhan nhap hang, status=2: tu choi nhap hang
            stockRequestDTO.setStatus(Const.STOCK_REQUEST_DOA.APPROVE);
            save(stockRequestDTO);

            //1.2.update giao dich xuat status=6
            //1.2.1 update giao dich chinh status = 6 : da nhap
            stockTransDTOSource.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
            stockTransService.save(stockTransDTOSource);

            //1.2.2 tao moi action nhap kho cho giao dich goc
            //1.2.2.1 tao moi action status = 4 : lenh nhap
            String actionCode = "LN_" + stockTransIdSource;
            StockTransActionDTO stockActionDTO = new StockTransActionDTO();
            stockActionDTO.setActionCode(actionCode);
            stockActionDTO.setStockTransId(stockTransIdSource);
            stockActionDTO.setStatus(Const.STOCK_TRANS_STATUS.EXPORTED);
            stockActionDTO.setCreateUser(staffDTO.getStaffCode());
            stockActionDTO.setCreateDatetime(sysdate);
            stockActionDTO.setActionStaffId(staffDTO.getStaffId());
            stockTransActionService.save(stockActionDTO);

            //1.2.2.3 tao moi action status = 5 : phieu nhap
            actionCode = "PN_" + stockTransIdSource;
            stockActionDTO.setActionCode(actionCode);
            stockActionDTO.setStatus(Const.STOCK_TRANS_STATUS.IMPORT_NOTE);
            stockActionDTO.setSignCaStatus(null);
            stockTransActionService.save(stockActionDTO);
            //1.2.2.3 tao moi action status = 6 : nhap kho
            stockActionDTO.setActionCode(null);
            stockActionDTO.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
            stockTransActionService.save(stockActionDTO);

            //1.3 Tao moi giao dich - xuat hang tu chi nhanh ve  kho VTT
            StockTransDTO stockTransDTOVTT = new StockTransDTO();
            stockTransDTOVTT.setFromOwnerId(stockTransDTOSource.getToOwnerId());
            stockTransDTOVTT.setFromOwnerType(Const.OWNER_TYPE.SHOP_LONG);
            stockTransDTOVTT.setToOwnerId(Const.L_VT_SHOP_ID);
            stockTransDTOVTT.setToOwnerType(Const.OWNER_TYPE.SHOP_LONG);
            stockTransDTOVTT.setCreateDatetime(sysdate);
            stockTransDTOVTT.setStockTransType(Const.STOCK_TRANS_TYPE.EXPORT);
            stockTransDTOVTT.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
            stockTransDTOVTT.setFromStockTransId(stockTransIdSource);
            StockTransDTO stockTransDTOVTTSave = stockTransService.save(stockTransDTOVTT);
            //1.3.1 tao moi action status=2: da lap phieu xuat
            actionCode = "PX_" + stockTransDTOVTTSave.getStockTransId();
            StockTransActionDTO stockActionDTOVTTExport = new StockTransActionDTO();
            stockActionDTOVTTExport.setActionCode(actionCode);
            stockActionDTOVTTExport.setStockTransId(stockTransDTOVTTSave.getStockTransId());
            stockActionDTOVTTExport.setStatus(Const.STOCK_TRANS_STATUS.EXPORT_NOTE);
            stockActionDTOVTTExport.setCreateUser(staffDTO.getStaffCode());
            stockActionDTOVTTExport.setCreateDatetime(sysdate);
            stockActionDTOVTTExport.setActionStaffId(staffDTO.getStaffId());
            stockTransActionService.save(stockActionDTOVTTExport);
            //1.3.2 tao moi action status=3: da xuat
            stockActionDTOVTTExport.setActionCode(null);
            stockActionDTOVTTExport.setStatus(Const.STOCK_TRANS_STATUS.EXPORTED);
            stockTransActionService.save(stockActionDTOVTTExport);

            //1.3.3 tao moi action status=4 - lap lenh nhap:
            actionCode = "LN_" + stockTransDTOVTTSave.getStockTransId();
            StockTransActionDTO stockActionDTOVTTImport = new StockTransActionDTO();
            stockActionDTOVTTImport.setActionCode(actionCode);
            stockActionDTOVTTImport.setStockTransId(stockTransDTOVTTSave.getStockTransId());
            stockActionDTOVTTImport.setStatus(Const.STOCK_TRANS_STATUS.IMPORT_ORDER);
            stockActionDTOVTTImport.setCreateUser(staffDTO.getStaffCode());
            stockActionDTOVTTImport.setCreateDatetime(sysdate);
            stockActionDTOVTTImport.setActionStaffId(staffDTO.getStaffId());
            stockTransActionService.save(stockActionDTOVTTImport);
            //1.3.2 tao moi action status=5: da lap phieu nhap
            actionCode = "PN_" + stockTransDTOVTTSave.getStockTransId();
            stockActionDTOVTTImport.setActionCode(actionCode);
            stockActionDTOVTTImport.setStatus(Const.STOCK_TRANS_STATUS.IMPORT_NOTE);
            stockTransActionService.save(stockActionDTOVTTImport);
            //1.3.3 tao moi action status=6: da nhap
            stockActionDTOVTTImport.setActionCode(null);
            stockActionDTOVTTImport.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
            stockTransActionService.save(stockActionDTOVTTImport);
            //1.3.4 insert moi stock_trans_detail
            for (StockTransDetailDTO detailSource : lsDetailDTOSource) {
                StockTransDetailDTO stockTransDetailDTOVTT = DataUtil.cloneBean(detailSource);
                stockTransDetailDTOVTT.setStockTransDetailId(null);
                stockTransDetailDTOVTT.setStockTransId(stockTransDTOVTTSave.getStockTransId());
                stockTransDetailDTOVTT.setCreateDatetime(sysdate);
                StockTransDetailDTO stockTransDetailDTOSaveVTT = stockTransDetailService.save(stockTransDetailDTOVTT);
                //1.3.4 insert moi stock_trans_detail
                if (Const.PRODUCT_OFFERING.CHECK_SERIAL.equals(detailSource.getCheckSerial()) && !DataUtil.isNullOrEmpty(detailSource.getLstSerial())) {
                    for (StockTransSerialDTO serialDTO : detailSource.getLstSerial()) {
                        serialDTO.setStockTransSerialId(null);
                        serialDTO.setCreateDatetime(sysdate);
                        serialDTO.setStockTransId(stockTransDTOVTTSave.getStockTransId());
                        serialDTO.setStockTransDetailId(stockTransDetailDTOSaveVTT.getStockTransDetailId());
                        stockTransSerialService.save(serialDTO);
                    }
                }
            }

            //1.4 thuc hien xuat kho VTT ve TTBH
            String shopCodeTTBH = DataUtil.safeToString(optionSetValueService.getValueByTwoCodeOption(Const.OPTION_SET.VT_TTBH_HANGHONG, Const.OPTION_SET.VT_TTBH_HANGHONG));
            ShopDTO shopDTOTTBH = shopService.getShopByCodeAndActiveStatus(shopCodeTTBH);
            if (shopDTOTTBH == null) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.manage.ttbh.notfound.shop");
            }

            StockTransDTO stockTransDTOTTBH = new StockTransDTO();
            stockTransDTOTTBH.setFromOwnerId(Const.L_VT_SHOP_ID);
            stockTransDTOTTBH.setFromOwnerType(Const.OWNER_TYPE.SHOP_LONG);
            stockTransDTOTTBH.setToOwnerId(shopDTOTTBH.getShopId());
            stockTransDTOTTBH.setToOwnerType(Const.OWNER_TYPE.SHOP_LONG);
            stockTransDTOTTBH.setCreateDatetime(sysdate);
            stockTransDTOTTBH.setStockTransType(Const.STOCK_TRANS_TYPE.EXPORT);
            stockTransDTOTTBH.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
            stockTransDTOTTBH.setFromStockTransId(stockTransDTOVTTSave.getStockTransId());
            StockTransDTO stockTransDTOTTBHSave = stockTransService.save(stockTransDTOTTBH);

            //1.4.1 tao moi action status=1: da lap lenh xuat
            actionCode = "LX_" + stockTransDTOTTBHSave.getStockTransId();
            StockTransActionDTO stockActionDTOTTBHExport = new StockTransActionDTO();
            stockActionDTOTTBHExport.setActionCode(actionCode);
            stockActionDTOTTBHExport.setStockTransId(stockTransDTOTTBHSave.getStockTransId());
            stockActionDTOTTBHExport.setStatus(Const.STOCK_TRANS_STATUS.EXPORT_ORDER);
            stockActionDTOTTBHExport.setCreateUser(staffDTO.getStaffCode());
            stockActionDTOTTBHExport.setCreateDatetime(sysdate);
            stockActionDTOTTBHExport.setActionStaffId(staffDTO.getStaffId());
            stockTransActionService.save(stockActionDTOTTBHExport);

            //1.4.2 tao moi action status=2: da lap phieu xuat
            actionCode = "PX_" + stockTransDTOTTBHSave.getStockTransId();
            stockActionDTOTTBHExport.setActionCode(actionCode);
            stockActionDTOTTBHExport.setStatus(Const.STOCK_TRANS_STATUS.EXPORT_NOTE);
            stockTransActionService.save(stockActionDTOTTBHExport);

            //1.4.3 tao moi action status=3: da xuat
            stockActionDTOTTBHExport.setActionCode(null);
            stockActionDTOTTBHExport.setStatus(Const.STOCK_TRANS_STATUS.EXPORTED);
            stockTransActionService.save(stockActionDTOTTBHExport);

            //1.4.4 tao moi action status=4 - lap lenh nhap:
            actionCode = "LN_" + stockTransDTOTTBHSave.getStockTransId();
            StockTransActionDTO stockActionDTOTTBHImport = new StockTransActionDTO();
            stockActionDTOTTBHImport.setActionCode(actionCode);
            stockActionDTOTTBHImport.setStockTransId(stockTransDTOTTBHSave.getStockTransId());
            stockActionDTOTTBHImport.setStatus(Const.STOCK_TRANS_STATUS.IMPORT_ORDER);
            stockActionDTOTTBHImport.setCreateUser(staffDTO.getStaffCode());
            stockActionDTOTTBHImport.setCreateDatetime(sysdate);
            stockActionDTOTTBHImport.setActionStaffId(staffDTO.getStaffId());
            stockTransActionService.save(stockActionDTOTTBHImport);

            //1.4.5 tao moi action status=5: da lap phieu nhap
            actionCode = "PN_" + stockTransDTOTTBHSave.getStockTransId();
            stockActionDTOTTBHImport.setActionCode(actionCode);
            stockActionDTOTTBHImport.setStatus(Const.STOCK_TRANS_STATUS.IMPORT_NOTE);
            StockTransActionDTO actionDTOTTBHSave = stockTransActionService.save(stockActionDTOTTBHImport);

            //1.4.6 tao moi action status=6: da nhap
            stockActionDTOTTBHImport.setActionCode(null);
            stockActionDTOTTBHImport.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
            stockTransActionService.save(stockActionDTOTTBHImport);

            //1.4.7 insert moi stock_trans_detail, stock_stans_serial neu co
            List<StockTransDetailDTO> lsDetailTTBHSave = Lists.newArrayList();

            for (StockTransDetailDTO detailSource : lsDetailDTOSource) {
                StockTransDetailDTO stockDetailDTOTTBH = DataUtil.cloneBean(detailSource);
                stockDetailDTOTTBH.setStockTransDetailId(null);
                stockDetailDTOTTBH.setStockTransId(stockTransDTOTTBHSave.getStockTransId());
                stockDetailDTOTTBH.setCreateDatetime(sysdate);
                StockTransDetailDTO stockDetailTTBHSave = stockTransDetailService.save(stockDetailDTOTTBH);
                lsDetailTTBHSave.add(stockDetailTTBHSave);

                if (Const.PRODUCT_OFFERING.CHECK_SERIAL.equals(detailSource.getCheckSerial()) && !DataUtil.isNullOrEmpty(detailSource.getLstSerial())) {
                    for (StockTransSerialDTO serialDTO : detailSource.getLstSerial()) {
                        StockTransSerialDTO serialDTOTTBH = DataUtil.cloneBean(serialDTO);
                        serialDTOTTBH.setStockTransSerialId(null);
                        serialDTOTTBH.setCreateDatetime(sysdate);
                        serialDTOTTBH.setStockTransId(stockTransDTOTTBH.getStockTransId());
                        serialDTOTTBH.setStockTransDetailId(stockDetailTTBHSave.getStockTransDetailId());
                        stockTransSerialService.save(serialDTOTTBH);
                        updateStockXTTBH(serialDTOTTBH.getFromSerial(), serialDTOTTBH.getToSerial(), sysdate, stockDetailDTOTTBH.getProdOfferId(),
                                stockDetailDTOTTBH.getProdOfferTypeId(), stockTransDTOTTBH.getToOwnerType(), stockTransDTOTTBH.getToOwnerId(),
                                stockTransDTOSource.getFromOwnerType(), stockTransDTOSource.getFromOwnerId(),
                                Const.GOODS_STATE.NEW, Const.GOODS_STATE.BROKEN, serialDTOTTBH.getStateId(), isCheckIm1);
                    }

                }
            }

            // Luu stock_total
            FlagStockDTO flagStockDTO = new FlagStockDTO();
            flagStockDTO.setImportStock(true);
            flagStockDTO.setImpCurrentQuantity(Const.STOCK_TOTAL.PLUS_QUANTITY);
            flagStockDTO.setImpAvailableQuantity(Const.STOCK_TOTAL.PLUS_QUANTITY);
            doSaveStockTotal(stockTransDTOTTBHSave, lsDetailTTBHSave, flagStockDTO, actionDTOTTBHSave);

            //2. TH2 tu choi yeu cau
        } else if (Const.STOCK_REQUEST_DOA.CANCEL.equals(status)) {
            //2.1 cap nhap stock_requeset status = 2: tu choi yeu cau
            stockRequestDTO.setStatus(Const.STOCK_REQUEST_DOA.CANCEL);
            save(stockRequestDTO);

            //2.2 cap nhap stock_trans voi status=8 tu choi nhap
            stockTransDTOSource.setStatus(Const.STOCK_TRANS_STATUS.REJECT);
            stockTransService.save(stockTransDTOSource);

            //2.3 cap nhap stock_trans_action status=8: tu choi nhap
            StockTransActionDTO stockActionDTO = new StockTransActionDTO();
            stockActionDTO.setActionCode(null);
            stockActionDTO.setStockTransId(stockTransIdSource);
            stockActionDTO.setStatus(Const.STOCK_TRANS_STATUS.REJECT);
            stockActionDTO.setCreateUser(staffDTO.getStaffCode());
            stockActionDTO.setCreateDatetime(sysdate);
            stockActionDTO.setActionStaffId(staffDTO.getStaffId());
            stockTransActionService.save(stockActionDTO);

            //2.4 cong kho ve kho khu vuc da xuat hang (tra hang)

            for (StockTransDetailDTO detailSource : lsDetailDTOSource) {
                if (Const.PRODUCT_OFFERING.CHECK_SERIAL.equals(detailSource.getCheckSerial()) && !DataUtil.isNullOrEmpty(detailSource.getLstSerial())) {
                    for (StockTransSerialDTO serialDTO : detailSource.getLstSerial()) {
                        //2.5 cap nhap stock_x
                        updateStockXTTBH(serialDTO.getFromSerial(), serialDTO.getToSerial(),  sysdate, detailSource.getProdOfferId(), detailSource.getProdOfferTypeId(), null, null,
                                stockTransDTOSource.getFromOwnerType(), stockTransDTOSource.getFromOwnerId(),Const.GOODS_STATE.NEW, Const.GOODS_STATE.BROKEN, serialDTO.getStateId(), isCheckIm1);
                    }
                }
            }

            // Luu stock_total
            FlagStockDTO flagStockDTO = new FlagStockDTO();
            flagStockDTO.setImportStock(true);
            flagStockDTO.setImpCurrentQuantity(Const.STOCK_TOTAL.PLUS_QUANTITY);
            flagStockDTO.setImpAvailableQuantity(Const.STOCK_TOTAL.PLUS_QUANTITY);
            Long fromOwnerId = stockTransDTOSource.getFromOwnerId();
            Long tơOwnerId = stockTransDTOSource.getToOwnerId();
            stockTransDTOSource.setFromOwnerId(tơOwnerId);
            stockTransDTOSource.setToOwnerId(fromOwnerId);
            stockTransDTOSource.setCreateDatetime(sysdate);
            actionDTOSource.setCreateUser(staffDTO.getStaffCode());
            actionDTOSource.setActionStaffId(staffDTO.getStaffId());
            doSaveStockTotal(stockTransDTOSource, lsDetailDTOSource, flagStockDTO, actionDTOSource);
        }
    }

    @Override
    public List<StockRequestDTO> getLsSearchStockRequestTTBH(String requestCode, Date fromDate, Date toDate, String status, Long ownerId) throws Exception {
        return repository.getLsSearchStockRequestTTBH(requestCode, fromDate, toDate,status, ownerId);
    }



    @Override
    public void doPrepareData(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, FlagStockDTO flagStockDTO) throws LogicException, Exception {

    }

    @Override
    public void doValidate(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, List<StockTransDetailDTO> lstStockTransDetail) throws LogicException, Exception {

    }
}
