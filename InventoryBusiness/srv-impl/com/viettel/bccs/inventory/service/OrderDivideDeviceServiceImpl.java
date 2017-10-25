package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.mysema.query.types.expr.BooleanExpression;
import com.viettel.bccs.im1.dto.StockModelIm1DTO;
import com.viettel.bccs.im1.dto.StockTotalAuditIm1DTO;
import com.viettel.bccs.im1.dto.StockTotalIm1DTO;
import com.viettel.bccs.im1.dto.StockTypeIm1DTO;
import com.viettel.bccs.im1.service.StockTotalAuditIm1Service;
import com.viettel.bccs.im1.service.StockTotalIm1Service;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.controller.SolrController;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.model.*;
import com.viettel.bccs.inventory.repo.*;
import com.viettel.bccs.inventory.service.common.IMServiceUtil;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DateUtil;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.fw.service.BaseServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.faces.context.FacesContext;
import javax.jws.WebMethod;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by vanho on 23/03/2017.
 */
@Service
public class OrderDivideDeviceServiceImpl extends BaseServiceImpl implements OrderDivideDeviceService {
    private final BaseMapper<StockRequest, StockRequestDTO> mapper = new BaseMapper(StockRequest.class, StockRequestDTO.class);
    private final BaseMapper<ProductOffering, ProductOfferingDTO> productMapper = new BaseMapper(ProductOffering.class, ProductOfferingDTO.class);
    private final BaseMapper<StockDeviceTransfer, StockDeviceTransferDTO> stockDeviceTransferMapper = new BaseMapper(StockDeviceTransfer.class, StockDeviceTransferDTO.class);
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM_LIB)
    private EntityManager emLib;
    @Autowired
    private ReasonService reasonService;
    @Autowired
    private ProductOfferingRepo productOfferingRepo;
    @Autowired
    private StockDeviceTransferRepo stockDeviceTransferRepo;
    @Autowired
    private ShopService shopService;
    @Autowired
    private OrderDivideDeviceRepo orderDivideDeviceRepo;
    @Autowired
    private StockRequestService stockRequestService;
    @Autowired
    private StockTransService stockTransService;
    @Autowired
    private StockTransActionService stockTransActionService;
    @Autowired
    private ProductOfferingService productOfferingService;
    @Autowired
    private OptionSetValueRepo optionSetValueRepo;
    @Autowired
    private StockTransDetailService stockTransDetailService;
    @Autowired
    private StockTransSerialService stockTransSerialService;
    @Autowired
    private StockTotalService stockTotalService;
    @Autowired
    private StockTotalAuditService stockTotalAuditService;
    @Autowired
    private OptionSetValueService optionSetValueService;
    @Autowired
    private StockTotalIm1Service stockTotalIm1Service;
    @Autowired
    private StockTotalAuditIm1Service stockTotalAuditIm1Service;
    @Autowired
    private SignFlowDetailService signFlowDetailService;
    @Autowired
    private StockRequestRepo repository;
    @Autowired
    private StaffService staffService;
    public static final Logger logger = Logger.getLogger(OrderDivideDeviceService.class);

    @WebMethod
    @Override
    public List<StockRequestDTO> getListOrderDivideDevice(String shopPath, Date fromDate, Date toDate, Long ownerId, String requestCode, String status) throws Exception {
        return orderDivideDeviceRepo.getListOrderDivideDevice(shopPath, fromDate, toDate, ownerId, requestCode, status);
    }

    @Override
    public List<ProductOfferingTotalDTO> getLsProductOfferingDTO(String inputSearch, Long ownerId, String ownerType, String stateId) throws Exception {
        StringBuilder query = new StringBuilder(" ");
        String inputTmp = DataUtil.safeToString(inputSearch).trim();
        if(DataUtil.isNullOrEmpty(inputTmp)) {
            query.append(" *:* ");
        } else {
            query.append(" ( ").append(" name:*").append(inputTmp).append("* OR code:*").append(inputTmp).append("*").append(" ) ");
        }

        if(!DataUtil.isNullOrZero(ownerId)) {
            query.append(" AND owner_id: ").append(ownerId);
        }

        if(!DataUtil.isNullOrEmpty(ownerType)) {
            query.append(" AND owner_type: ").append(ownerType);
        }

        if(!DataUtil.isNullOrEmpty(stateId)) {
            query.append(" AND state_id: ").append(stateId);
        }

        if(!DataUtil.isNullObject(stateId)) {
                query.append(" AND available_quantity: [1 TO *] ");
        }

        return SolrController.doSearch(Const.SOLR_CORE.OFFERWARRANTY, query.toString(), ProductOfferingTotalDTO.class, Const.NUMBER_SEARCH.NUMBER_ALL, "name", "ASC");
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createOrderDivideDevice(StockRequestDTO stockRequestDTO) throws LogicException, Exception {

        if(stockRequestDTO == null) {
            throw new LogicException("105", "change.request.doa.empty");
        } else if(stockRequestDTO.getSignOfficeDTO() != null && !DataUtil.isNullOrEmpty(stockRequestDTO.getSignOfficeDTO().getUserName()) && !DataUtil.isNullOrEmpty(stockRequestDTO.getSignOfficeDTO().getPassWord())) {
            if(DataUtil.isNullOrEmpty(stockRequestDTO.getStockTransDetailDTOs())) {
                throw new LogicException("105", "change.request.doa.product.empty");
            } else {
                List stockRequestDTOs = stockRequestService.findStockRequestByCode(stockRequestDTO.getRequestCode());
                if(!DataUtil.isNullOrEmpty(stockRequestDTOs)) {
                    throw new LogicException("105", "change.request.doa.requestCode.duplicate", new Object[]{stockRequestDTO.getRequestCode()});
                } else {
                    if(stockRequestDTO.getOwnerId() != null) {
                        if(Const.OWNER_TYPE.SHOP_LONG.equals(stockRequestDTO.getOwnerType())) {
                            ShopDTO sysdate1 = this.shopService.findOne(stockRequestDTO.getOwnerId());
                            if(sysdate1 == null || !"1".equals(sysdate1.getStatus())) {
                                throw new LogicException("04", "warranty.create.underlying.fromOwnerId.exp.shop.notfound", new Object[]{stockRequestDTO.getOwnerCode()});
                            }
                        } else {
                            throw new LogicException("05", "create.request.divide.device.wrongOwnerType");
                        }
                    } else {
                        throw new LogicException("", "create.request.divide.device.shopMissing");
                    }

                    if(DataUtil.isNullOrEmpty(stockRequestDTO.getNote())){
                        throw new LogicException("", "order.divide.product.docCode.empty");
                    } else if(stockRequestDTO.getNote().length() > 50) {
                        throw new LogicException("", "order.divide.product.docCode.errorMaxlength");
                    } else {
                        Pattern pattern = Pattern.compile("^([a-zA-Z0-9_])+$");
                        if(!pattern.matcher(stockRequestDTO.getNote()).matches()){
                            throw new LogicException("", "order.divide.product.docCode.errorCharacter");
                        }
                    }

                    if(stockRequestDTO.getFileContent() == null)
                        throw new LogicException("", "create.request.divide.device.docsMissing");

                    //if()

                    boolean isERP = validateERP(stockRequestDTO.getStockTransDetailDTOs());

                    ShopDTO shopDTO = shopService.findOne(stockRequestDTO.getOwnerId());

                    Date sysdate = this.getSysDate(this.em);
                    //tao yeu cau phan ra thiet bi
                    StockRequestDTO requestDTOInsert = DataUtil.cloneBean(stockRequestDTO);
                    requestDTOInsert.setCreateDatetime(sysdate);
                    requestDTOInsert.setUpdateDatetime(sysdate);
                    requestDTOInsert.setRequestType(Long.valueOf(3L));
                    requestDTOInsert.setStatus(Const.STOCK_REQUEST_ORDER.STATUS_0_CREATE_REQUEST);
                    requestDTOInsert.setAccountName(stockRequestDTO.getSignOfficeDTO().getUserName().trim());
                    requestDTOInsert.setAccountPass(stockRequestDTO.getSignOfficeDTO().getPassWord());
                    requestDTOInsert.setSignUserList(getSignUserList(stockRequestDTO.getSignOfficeDTO()));
                    StockTransDTO stockTransDTO = new StockTransDTO();
                    stockTransDTO.setFromOwnerId(requestDTOInsert.getOwnerId());
                    stockTransDTO.setFromOwnerType(requestDTOInsert.getOwnerType());
                    stockTransDTO.setToOwnerId(shopDTO.getParentShopId());
                    stockTransDTO.setToOwnerType(Const.OWNER_TYPE.SHOP_LONG);
                    stockTransDTO.setCreateDatetime(sysdate);
                    stockTransDTO.setStockTransType(Const.STOCK_TRANS_TYPE.EXPORT);
                    stockTransDTO.setStatus(Const.STOCK_TRANS_STATUS.PROCESSING);

                    ReasonDTO reasonDTO = reasonService.getReason(Const.REASON_CODE.DEVICE_TRANSFER, Const.REASON_CODE.DEVICE_TRANSFER);
                    stockTransDTO.setReasonId(reasonDTO != null ?  reasonDTO.getReasonId() : null);

                    if(!isERP)
                        stockTransDTO.setCheckErp(Const.STOCK_TRANS.IS_NOT_ERP);// ko phai ERP thi set vao

                    //tao giao dich xuat hang
                    StockTransDTO stockTransNew = this.stockTransService.save(stockTransDTO);
                    requestDTOInsert.setStockTransId(stockTransNew.getStockTransId());
                    ////tao yeu cau phan ra thiet bi
                    StockRequest temp = this.mapper.toPersistenceBean(requestDTOInsert);
                    this.repository.save(temp);
                    // ket thuc tao yeu cau phan ra thiet bi
                    //tao log giao dich xuat kho
                    String actionCode = "PX_" + stockTransNew.getStockTransId();
                    StockTransActionDTO stockActionDTO = new StockTransActionDTO();
                    stockActionDTO.setActionCode(actionCode);
                    stockActionDTO.setStockTransId(stockTransNew.getStockTransId());
                    stockActionDTO.setStatus(Const.STOCK_TRANS_STATUS.EXPORT_NOTE);
                    stockActionDTO.setCreateUser(requestDTOInsert.getCreateUser());
                    stockActionDTO.setCreateDatetime(sysdate);
                    stockActionDTO.setActionStaffId(requestDTOInsert.getActionStaffId());
                    stockActionDTO.setSignCaStatus(Const.SIGN_VOFFICE);
                    stockTransActionService.save(stockActionDTO);
                    //tao log giao dich xuat kho 2
                    stockActionDTO.setStatus(Const.STOCK_TRANS_STATUS.EXPORTED);
                    stockActionDTO.setActionCode(null);
                    stockActionDTO.setSignCaStatus(null);
                    stockTransActionService.save(stockActionDTO);
                    boolean isCheckIm1 = false;
                    List lstConfigEnableBccs1 = optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1");
                    if(!DataUtil.isNullOrEmpty(lstConfigEnableBccs1) && !DataUtil.isNullObject(((OptionSetValueDTO)lstConfigEnableBccs1.get(0)).getValue())) {
                        isCheckIm1 = Const.ENABLE_UPDATE_BCCS1.equals(DataUtil.safeToLong(((OptionSetValueDTO)lstConfigEnableBccs1.get(0)).getValue()));
                    }

                    StockTotalAuditDTO totalAuditInput = new StockTotalAuditDTO();
                    totalAuditInput.setUserId(stockActionDTO.getActionStaffId());
                    totalAuditInput.setCreateDate(stockTransNew.getCreateDatetime());
                    totalAuditInput.setTransId(stockTransNew.getStockTransId());
                    totalAuditInput.setTransCode(actionCode);
                    totalAuditInput.setTransType(DataUtil.safeToLong(stockTransDTO.getStockTransType()));
                    totalAuditInput.setUserCode(requestDTOInsert.getCreateUser());
                    totalAuditInput.setStatus(Long.valueOf("1"));
                    StockTotalDTO totalDTOSearch = new StockTotalDTO();
                    totalDTOSearch.setOwnerId(stockTransDTO.getFromOwnerId());
                    totalDTOSearch.setOwnerType(stockTransDTO.getFromOwnerType());

                    List<StockTransDetailDTO> stockTransDetailDTOs = stockRequestDTO.getStockTransDetailDTOs();
                    //tao chi tiet giao dich xuat kho
                    for(StockTransDetailDTO stockDetail : stockTransDetailDTOs) {
                        if(DataUtil.isNullOrZero(stockDetail.getQuantity())) {
                            throw new LogicException("102", "mn.stock.serial.quantity.limit.zezo");
                        }

                        ProductOfferingDTO productOfferingDTO = productOfferingService.findOne(stockDetail.getProdOfferId());
                        if(productOfferingDTO == null || productOfferingDTO.getProductOfferTypeId() == null || productOfferingDTO.getStatus() == null || !"1".equals(productOfferingDTO.getStatus())) {
                            throw new LogicException("105", getText("order.divide.product.productNotFound"), new Object[]{stockDetail.getProdOfferName()});
                        }

                        stockDetail.setStockTransId(stockTransNew.getStockTransId());
                        stockDetail.setCreateDatetime(sysdate);
                        StockTransDetailDTO detailInsert = stockTransDetailService.save(stockDetail);

                        totalDTOSearch.setProdOfferId(stockDetail.getProdOfferId());
                        totalDTOSearch.setStateId(stockDetail.getStateId());
                        totalDTOSearch.setStatus(1L);
                        Long availAbleQuantity = stockRequestService.getAvailableQuantity(stockDetail.getOwnerID(), stockDetail.getOwnerType(), stockDetail.getProdOfferId(), stockDetail.getStateId());
                        if(stockDetail.getQuantity().longValue() > DataUtil.safeToLong(availAbleQuantity).longValue()) {
                            throw new LogicException("102", "export.order.stock.quantity.msg");
                        }

                        this.debitTotal(totalDTOSearch, totalAuditInput, stockTransDTO.getFromOwnerType(), stockTransDTO.getFromOwnerId(), stockDetail.getProdOfferId(), stockDetail.getStateId(), stockDetail.getQuantity(), stockActionDTO.getCreateDatetime(), isCheckIm1);
                        if(Const.PRODUCT_OFFERING.CHECK_SERIAL.equals(stockDetail.getCheckSerial())) {
                            if(DataUtil.isNullOrEmpty(stockDetail.getLstSerial())) {
                                throw new LogicException("105", "mn.stock.utilities.look.serial.not.empty.serial");
                            }

                            List<StockTransSerialDTO> lstStockTransSerial = stockDetail.getLstStockTransSerial();

                            for(StockTransSerialDTO stockSerialNew : lstStockTransSerial) {
                                stockSerialNew.setProdOfferId(stockDetail.getProdOfferId());
                                stockSerialNew.setStateId(stockDetail.getStateId());
                                stockSerialNew.setStockTransId(stockTransNew.getStockTransId());
                                stockSerialNew.setCreateDatetime(sysdate);
                                stockSerialNew.setStockTransDetailId(detailInsert.getStockTransDetailId());
                                stockTransSerialService.save(stockSerialNew);
                                this.updateStockX(stockDetail.getStateId(), stockSerialNew.getFromSerial(), stockSerialNew.getToSerial(), stockTransDTO.getCreateDatetime(), stockDetail.getProdOfferId(), stockDetail.getProdOfferName(), stockDetail.getProdOfferTypeId(), stockTransDTO.getFromOwnerType(), stockTransDTO.getFromOwnerId(),isCheckIm1);
                            }
                        }

                        //luu vao bang stock_device_transfer
                        List<StockDeviceTransferDTO> lstStockDeviceTransfer = stockDetail.getLstStockDeviceTransfer();
                        if(DataUtil.isNullOrEmpty(lstStockDeviceTransfer)){
                            throw new LogicException("", "create.request.divide.device.productNotConfig", stockDetail.getName());
                        }
                        for(StockDeviceTransferDTO stockDeviceTransferDTO : lstStockDeviceTransfer){

                            ProductOffering one = productOfferingRepo.findOne(stockDeviceTransferDTO.getNewProdOfferId());
                            if(one == null || productOfferingDTO.getStatus() == null || !"1".equals(one.getStatus())) {
                                throw new LogicException("", getText("order.divide.product.itemNotFound"), new Object[]{stockDeviceTransferDTO.getNewProbOfferName()});
                            }

                            stockDeviceTransferDTO.setCreateDate(sysdate);
                            stockDeviceTransferDTO.setCreateUser(stockRequestDTO.getCreateUser());
                            stockDeviceTransferDTO.setStockRequestId(temp.getStockRequestId());

                            stockDeviceTransferRepo.saveAndFlush(stockDeviceTransferMapper.toPersistenceBean(stockDeviceTransferDTO));
                        }
                    }

                }
            }
        } else {
            throw new LogicException("105", "change.request.doa.signOffice.empty");
        }
    }

    @Override
    public byte[] getAttachFileContent(Long stockRequestId) throws Exception {
        return orderDivideDeviceRepo.getAttachFileContent(stockRequestId);
    }

    @Override
    public ProductOfferingDTO getProductByCodeAndProbType(String code, Long probTypeId) {

        BooleanExpression predecate = QProductOffering.productOffering.status.eq("1");
        predecate = predecate.and(QProductOffering.productOffering.code.equalsIgnoreCase(code));
        predecate = predecate.and(QProductOffering.productOffering.productOfferTypeId.eq(probTypeId));
        List<ProductOfferingDTO> lstProductOfferingDTOS = productMapper.toDtoBean(Lists.newArrayList(productOfferingRepo.findAll(predecate)));
        return DataUtil.isNullOrEmpty(lstProductOfferingDTOS) ? null : lstProductOfferingDTOS.get(0);
    }

    private void debitTotal(StockTotalDTO totalDTOSearch, StockTotalAuditDTO totalAuditInput, Long ownerType, Long ownerId, Long prodOfferId, Long oldStateId, Long quantity, Date sysdate, boolean isCheckIm1) throws Exception {
        List lstStockTotal = stockTotalService.searchStockTotal(totalDTOSearch);
        if(!DataUtil.isNullOrEmpty(lstStockTotal) && lstStockTotal.size() > 1) {
            ProductOfferingDTO stockTotalDTO = productOfferingService.findOne(((StockTotalDTO)lstStockTotal.get(0)).getProdOfferId());
            if(stockTotalDTO != null) {
                throw new LogicException("105", "stockTrans.validate.stockTotal.empty", new Object[]{stockTotalDTO.getName()});
            }
        }

        StockTotalDTO stockTotalDTO1 = (StockTotalDTO)lstStockTotal.get(0);
        StockTotalAuditDTO totalAuditInsert = DataUtil.cloneBean(totalAuditInput);
        totalAuditInsert.setTransId(totalAuditInput.getTransId());
        totalAuditInsert.setOwnerId(stockTotalDTO1.getOwnerId());
        totalAuditInsert.setOwnerType(stockTotalDTO1.getOwnerType());
        totalAuditInsert.setProdOfferId(stockTotalDTO1.getProdOfferId());
        totalAuditInsert.setStateId(stockTotalDTO1.getStateId());
        totalAuditInsert.setCurrentQuantityBf(DataUtil.safeToLong(stockTotalDTO1.getCurrentQuantity()));
        totalAuditInsert.setCurrentQuantity(Long.valueOf(-DataUtil.safeToLong(quantity).longValue()));
        totalAuditInsert.setCurrentQuantityAf(Long.valueOf(DataUtil.safeToLong(stockTotalDTO1.getCurrentQuantity()).longValue() - DataUtil.safeToLong(quantity).longValue()));
        totalAuditInsert.setAvailableQuantityBf(DataUtil.safeToLong(stockTotalDTO1.getAvailableQuantity()));
        totalAuditInsert.setAvailableQuantity(Long.valueOf(-DataUtil.safeToLong(quantity).longValue()));
        totalAuditInsert.setAvailableQuantityAf(Long.valueOf(DataUtil.safeToLong(stockTotalDTO1.getAvailableQuantity()).longValue() - DataUtil.safeToLong(quantity).longValue()));
        totalAuditInsert.setSourceType(Const.STOCK_TOTAL_AUDIT.SOURCE_TYPE_STOCK_TRANS);
        this.stockTotalAuditService.save(totalAuditInsert);
        String sql = " UPDATE stock_total  SET current_quantity  = current_quantity  - #quantity, available_quantity  = available_quantity - #quantity ,modified_date=#sysdate WHERE owner_id = #ownerId AND owner_type = #onwerType AND prod_offer_id = #prodOfferId AND state_id = #oldStateId AND current_quantity >= #quantity AND available_quantity >= #quantity";
        Query queryObject = this.em.createNativeQuery(sql);
        queryObject.setParameter("ownerId", ownerId);
        queryObject.setParameter("onwerType", ownerType);
        queryObject.setParameter("prodOfferId", prodOfferId);
        queryObject.setParameter("oldStateId", oldStateId);
        queryObject.setParameter("quantity", quantity);
        queryObject.setParameter("sysdate", sysdate);
        int result = queryObject.executeUpdate();
        if(result == 0) {
            throw new LogicException("102", "export.order.stock.quantity.msg");
        } else {
            if(isCheckIm1) {
                this.debitTotalIm1(totalDTOSearch, totalAuditInput, ownerId, ownerType, prodOfferId, oldStateId, quantity, sysdate);
            }
        }
    }

    private void debitTotalIm1(StockTotalDTO totalDTOSearch, StockTotalAuditDTO totalAuditInput, Long ownerId, Long ownerType, Long prodOfferId, Long oldStateId, Long quantity, Date sysdate) throws Exception {
        StockTotalIm1DTO total1DTOSearch = new StockTotalIm1DTO();
        total1DTOSearch.setStockModelId(totalDTOSearch.getProdOfferId());
        total1DTOSearch.setOwnerId(totalDTOSearch.getOwnerId());
        total1DTOSearch.setOwnerType(totalDTOSearch.getOwnerType());
        total1DTOSearch.setStateId(totalDTOSearch.getStateId());
        total1DTOSearch.setStatus(totalDTOSearch.getStatus());
        List lstStockTotal = stockTotalIm1Service.findStockTotal(total1DTOSearch);
        if(!DataUtil.isNullOrEmpty(lstStockTotal) && lstStockTotal.size() > 1) {
            StockModelIm1DTO sql = stockTotalIm1Service.findOneStockModel(((StockTotalIm1DTO)lstStockTotal.get(0)).getStockModelId());
            if(sql != null) {
                throw new LogicException("102", "stockTrans1.validate.limit.product.exist", new Object[]{sql.getName()});
            }
        }

        if(!DataUtil.isNullOrEmpty(lstStockTotal)) {
            StockTotalAuditIm1DTO sql1 = new StockTotalAuditIm1DTO();
            sql1.setUserId(totalAuditInput.getUserId());
            sql1.setCreateDate(totalAuditInput.getCreateDate());
            sql1.setReasonId(totalAuditInput.getReasonId());
            sql1.setTransId(totalAuditInput.getTransId());
            sql1.setTransCode(totalAuditInput.getTransCode());
            sql1.setTransType(DataUtil.safeToLong(totalAuditInput.getTransType()));
            sql1.setOwnerId(((StockTotalIm1DTO)lstStockTotal.get(0)).getOwnerId());
            sql1.setOwnerType(((StockTotalIm1DTO)lstStockTotal.get(0)).getOwnerType());
            sql1.setStockModelId(((StockTotalIm1DTO)lstStockTotal.get(0)).getStockModelId());
            sql1.setStockModelName(((StockTotalIm1DTO)lstStockTotal.get(0)).getStockModelName());
            sql1.setStateId(((StockTotalIm1DTO)lstStockTotal.get(0)).getStateId());
            sql1.setQtyBf(DataUtil.safeToLong(((StockTotalIm1DTO)lstStockTotal.get(0)).getQuantity()));
            sql1.setQty(Long.valueOf(-DataUtil.safeToLong(quantity).longValue()));
            sql1.setQtyAf(Long.valueOf(DataUtil.safeToLong(((StockTotalIm1DTO)lstStockTotal.get(0)).getQuantity()).longValue() - DataUtil.safeToLong(quantity).longValue()));
            sql1.setQtyIssueBf(DataUtil.safeToLong(((StockTotalIm1DTO)lstStockTotal.get(0)).getQuantityIssue()));
            sql1.setQtyIssue(Long.valueOf(-DataUtil.safeToLong(quantity).longValue()));
            sql1.setQtyIssueAf(Long.valueOf(DataUtil.safeToLong(((StockTotalIm1DTO)lstStockTotal.get(0)).getQuantityIssue()).longValue() - DataUtil.safeToLong(quantity).longValue()));
            sql1.setSourceType(Const.STOCK_TOTAL_AUDIT.SOURCE_TYPE_STOCK_TRANS);
            StaffDTO queryObject = staffService.findOne(sql1.getUserId());
            ShopDTO result = this.shopService.findOne(sql1.getOwnerId());
            if(queryObject != null) {
                sql1.setUserCode(queryObject.getStaffCode());
                sql1.setUserName(queryObject.getName());
            }

            if(!DataUtil.isNullObject(result)) {
                sql1.setOwnerCode(result.getShopCode());
                sql1.setOwnerName(result.getName());
            }

            this.stockTotalAuditIm1Service.create(sql1);
        }

        String sql2 = " UPDATE bccs_im.stock_total  SET quantity = quantity - #quantity, quantity_issue = quantity_issue - #quantity,modified_date=#sysdate WHERE owner_id = #ownerId AND owner_type = #onwerType AND stock_model_id = #stockModelId AND state_id = #oldStateId AND quantity >= #quantity AND quantity_issue >= #quantity";
        Query queryObject1 = this.emLib.createNativeQuery(sql2);
        queryObject1.setParameter("ownerId", ownerId);
        queryObject1.setParameter("onwerType", ownerType);
        queryObject1.setParameter("stockModelId", prodOfferId);
        queryObject1.setParameter("oldStateId", oldStateId);
        queryObject1.setParameter("quantity", quantity);
        queryObject1.setParameter("sysdate", sysdate);
        int result1 = queryObject1.executeUpdate();
        if(result1 == 0) {
            throw new LogicException("102", "export.order.stock.quantity.im1.msg");
        }
    }

    public void updateStockX(Long stateId, String fromSerial, String toSerial, Date updateDateTime, Long prodOfferId, String prodOfferName, Long productOfferTypeId, Long ownerType, Long ownerId, boolean isCheckIm1) throws Exception {
        String tableName = IMServiceUtil.getTableNameByOfferType(productOfferTypeId);
        String sql = "update " + tableName + " set state_id = #stateId, status = 3, update_datetime=#updateDateTime   " + "where prod_offer_id=#prodOfferId and owner_type =#ownerType and owner_id = #ownerId and status= 1 ";
        if(!Const.PRODUCT_OFFER_TYPE.PHONE.equals(productOfferTypeId) && !Const.PRODUCT_OFFER_TYPE.ACCESSORIES.equals(productOfferTypeId)) {
            sql = sql + " AND   to_number(serial) >= #fromSerial  AND   to_number(serial) <= #toSerial ";
        } else {
            sql = sql + " AND   serial >= #fromSerial AND   serial <= #toSerial ";
        }


        Query query = this.em.createNativeQuery(sql);
        query.setParameter("updateDateTime", updateDateTime);
        query.setParameter("prodOfferId", prodOfferId);
        query.setParameter("ownerType", ownerType);
        query.setParameter("ownerId", ownerId);
        query.setParameter("stateId", stateId);
        if(!Const.PRODUCT_OFFER_TYPE.PHONE.equals(productOfferTypeId) && !Const.PRODUCT_OFFER_TYPE.ACCESSORIES.equals(productOfferTypeId)) {
            query.setParameter("fromSerial", Long.valueOf(fromSerial));
            query.setParameter("toSerial", Long.valueOf(toSerial));
        } else {
            query.setParameter("fromSerial", fromSerial);
            query.setParameter("toSerial", toSerial);
        }

        int result = query.executeUpdate();
        if(result == 0) {
            List lsOptionState = optionSetValueRepo.getLsOptionSetValueByCode("GOODS_STATE");
            Map mapState = (Map)lsOptionState.stream().collect(Collectors.toMap(OptionSetValue::getValue, OptionSetValue::getName));
            String[] params = new String[4];
            ProductOfferingDTO offeringDTO = productOfferingService.findOne(prodOfferId);
            params[0] = offeringDTO.getName();
            params[1] = (String)mapState.get(stateId + "");
            params[2] = fromSerial;
            params[3] = toSerial;
            throw new LogicException("102", this.getText("stockTrans.validate.quantity.serial"), params);
        }else {
            if(isCheckIm1) {
                this.updateStockXIM1(stateId, fromSerial, toSerial, prodOfferId, prodOfferName, productOfferTypeId, ownerType, ownerId);
            }
        }
    }

    private void updateStockXIM1(Long stateId, String fromSerial, String toSerial, Long stockModelId, String stockModelName, Long productOfferTypeId, Long ownerType, Long ownerId) throws Exception {
        StockModelIm1DTO offeringDTO = this.stockTotalIm1Service.findOneStockModel(stockModelId);
        if(offeringDTO == null) {
            throw new LogicException("09", "validate.stock.inspect.productOffering.bccs1.notFound", new Object[]{stockModelName});
        } else if(!Const.PRODUCT_OFFERING.CHECK_SERIAL.equals(offeringDTO.getCheckSerial())) {
            throw new LogicException("09", "validate.stock.inspect.productOffering.bccs1.notFound", new Object[]{stockModelName});
        } else {
            StockTypeIm1DTO stockTypeIm1DTO = this.stockTotalIm1Service.findOneStockType(stockModelId);
            if(!DataUtil.isNullObject(stockTypeIm1DTO)) {
                String tableName = stockTypeIm1DTO.getTableName();
                String sql1 = "update bccs_im." + tableName + " set state_id = #stateId, status = 3  " + "where stock_model_id=#stockModelId and owner_type =#ownerType and owner_id = #ownerId and status= 1 ";
                if(Const.PRODUCT_OFFER_TYPE.PHONE.equals(productOfferTypeId)) {
                    sql1 = sql1 + " AND   serial >= #fromSerial AND   serial <= #toSerial ";
                } else {
                    sql1 = sql1 + " AND   to_number(serial) >= #fromSerial  AND   to_number(serial) <= #toSerial ";
                }

                Query query = this.emLib.createNativeQuery(sql1);
                query.setParameter("stockModelId", stockModelId);
                query.setParameter("ownerType", ownerType);
                query.setParameter("ownerId", ownerId);
                query.setParameter("stateId", stateId);
                if(Const.PRODUCT_OFFER_TYPE.PHONE.equals(productOfferTypeId)) {
                    query.setParameter("fromSerial", fromSerial);
                    query.setParameter("toSerial", toSerial);
                } else {
                    query.setParameter("fromSerial", fromSerial);
                    query.setParameter("toSerial", toSerial);
                }

                int result = query.executeUpdate();
                if(result == 0) {
                    List lsOptionState = this.optionSetValueRepo.getLsOptionSetValueByCode("GOODS_STATE");
                    Map mapState = (Map)lsOptionState.stream().collect(Collectors.toMap(OptionSetValue::getValue, OptionSetValue::getName));
                    String[] params = new String[]{offeringDTO.getName(), (String)mapState.get(stateId + ""), fromSerial, toSerial};
                    throw new LogicException("102", this.getText("stockTrans.validate.quantity.serial.bccs1"), params);
                }
            } else {
                ProductOfferingDTO sql = this.productOfferingService.findOne(stockModelId);
                throw new LogicException("102", "stockTrans1.validate.stockmodel.not.exist", new Object[]{sql.getName()});
            }
        }
    }

    private String getSignUserList(SignOfficeDTO signOfficeDTO) throws LogicException, Exception {
        List lstSignFlowDetail = signFlowDetailService.findBySignFlowId(signOfficeDTO.getSignFlowId());
        if(DataUtil.isNullOrEmpty(lstSignFlowDetail)) {
            throw new LogicException("102", "stockTrans.validate.signFlow.emptyDetail");
        } else {
            StringBuilder lstUser = new StringBuilder("");
            if (!DataUtil.isNullOrEmpty(lstSignFlowDetail)) {
                SignFlowDetailDTO stVofficeId;
                for (Iterator stockTransVoffice = lstSignFlowDetail.iterator(); stockTransVoffice.hasNext(); lstUser.append(stVofficeId.getEmail().trim().substring(0, stVofficeId.getEmail().lastIndexOf("@")))) {
                    stVofficeId = (SignFlowDetailDTO) stockTransVoffice.next();
                    if (DataUtil.isNullOrEmpty(stVofficeId.getEmail())) {
                        throw new LogicException("102", "stockTrans.validate.signFlow.emptyEmail");
                    }
                    if (!DataUtil.isNullOrEmpty(lstUser.toString())) {
                        lstUser.append(",");
                    }
                }

            }
            return lstUser.toString();
        }
    }

    private boolean validateERP(List<StockTransDetailDTO> stockTransDetailDTOS) throws LogicException, Exception{
        List<String> listStateERP = new ArrayList<>();
        listStateERP.addAll(DataUtil.defaultIfNull(optionSetValueService.getByOptionSetCode(Const.OPTION_SET.WARRANTY_OFFER_STATUS), new ArrayList<OptionSetValueDTO>()).stream().map(s -> s.getValue()).collect(Collectors.toList()));

        boolean hasERP = false;
        boolean conflictERP = false;
        for(StockTransDetailDTO stockTransDetailDTO : stockTransDetailDTOS){
            if(listStateERP.contains(String.valueOf(stockTransDetailDTO.getStateId()))) {
                hasERP = true;
                continue;
            }

            if(hasERP) {
                conflictERP = true;
                break;
            }
        }

        if(conflictERP){
            throw new LogicException("", "create.request.divide.device.conflictERP");
        }

        return hasERP;
    }
}
