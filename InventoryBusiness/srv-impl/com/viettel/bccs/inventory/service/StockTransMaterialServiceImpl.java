package com.viettel.bccs.inventory.service;

import com.viettel.bccs.im1.dto.StockModelIm1DTO;
import com.viettel.bccs.im1.dto.StockTotalAuditIm1DTO;
import com.viettel.bccs.im1.dto.StockTotalIm1DTO;
import com.viettel.bccs.im1.dto.StockTypeIm1DTO;
import com.viettel.bccs.im1.service.StockTotalAuditIm1Service;
import com.viettel.bccs.im1.service.StockTotalIm1Service;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.model.OptionSetValue;
import com.viettel.bccs.inventory.repo.OptionSetValueRepo;
import com.viettel.bccs.inventory.repo.ProductOfferingRepo;
import com.viettel.bccs.inventory.service.common.IMServiceUtil;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.fw.service.BaseServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StockTransMaterialServiceImpl extends BaseServiceImpl implements StockTransMaterialService {

    public static final Logger logger = Logger.getLogger(StockTransMaterialService.class);

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
    private StockTotalAuditService stockTotalAuditService;
    @Autowired
    private StaffService staffService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private StockTotalAuditIm1Service stockTotalAuditIm1Service;
    @Autowired
    private ReasonService reasonService;
    @Autowired
    private StockTransDocumentService stockTransDocumentService;
    @Autowired
    private ProductOfferingRepo productOfferingRepo;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void excuteCreateTransMaterial(VStockTransDTO vStockTransDTO) throws LogicException, Exception {
        if (vStockTransDTO == null) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.export.material.stockbase.empty");
        }
        if (DataUtil.isNullOrEmpty(vStockTransDTO.getTransDetailDTOs())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "change.request.doa.product.empty");
        }
        if (vStockTransDTO.getFromOwnerID() == null || vStockTransDTO.getFromOwnerType() == null) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "mn.stock.liquidate.export.stock.required");
        }
        if (vStockTransDTO.getFileAttach() == null || vStockTransDTO.getFileAttach().length == 0) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.export.material.fileAttack.empty");
        }

        //check ton tai ma kho xuat
        if (Const.OWNER_TYPE.SHOP_LONG.equals(vStockTransDTO.getFromOwnerType())) {
            ShopDTO shopDTO = shopService.findOne(vStockTransDTO.getFromOwnerID());
            if (shopDTO == null || !Const.STATUS_ACTIVE.equals(shopDTO.getStatus())) {
                throw new LogicException("04", "warranty.create.underlying.fromOwnerId.exp.shop.notfound", vStockTransDTO.getFromOwnerName());
            }
        }

        Date sysdate = getSysDate(em);

        //them moi stock_trans
        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setFromOwnerId(vStockTransDTO.getFromOwnerID());
        stockTransDTO.setFromOwnerType(vStockTransDTO.getFromOwnerType());
        stockTransDTO.setToOwnerId(null);
        stockTransDTO.setToOwnerType(Const.OWNER_TYPE.CUSTOMER_LONG);
        stockTransDTO.setCreateDatetime(sysdate);
        stockTransDTO.setStockTransType(Const.STOCK_TRANS_TYPE.EXPORT);
        stockTransDTO.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
        stockTransDTO.setStockBase(vStockTransDTO.getStockBase());
        ReasonDTO reasonDTO = reasonService.getReason(Const.REASON_CODE.EXP_MATERIAL_DEPLOY, Const.REASON_CODE.EXP_MATERIAL_DEPLOY);
        stockTransDTO.setReasonId(reasonDTO != null ?  reasonDTO.getReasonId() : null);
        StockTransDTO stockTransNew = stockTransService.save(stockTransDTO);

        //them moi stock_trans_document luu tru ho so du an
        StockTransDocumentDTO stockTransDocumentDTO = new StockTransDocumentDTO();
        stockTransDocumentDTO.setStockTransId(stockTransNew.getStockTransId());
        stockTransDocumentDTO.setFileAttach(vStockTransDTO.getFileAttach());
        stockTransDocumentDTO.setTransType(1L);
        stockTransDocumentService.save(stockTransDocumentDTO);

        //tao moi action status = 2
        String actionCode = "PX_" + stockTransNew.getStockTransId();
        StockTransActionDTO stockActionDTO = new StockTransActionDTO();
        stockActionDTO.setActionCode(actionCode);
        stockActionDTO.setStockTransId(stockTransNew.getStockTransId());
        stockActionDTO.setStatus(Const.STOCK_TRANS_STATUS.EXPORT_NOTE);
        stockActionDTO.setCreateUser(vStockTransDTO.getUserCreate());
        stockActionDTO.setCreateDatetime(sysdate);
        stockActionDTO.setActionStaffId(vStockTransDTO.getStaffId());
        stockTransActionService.save(stockActionDTO);

        //tao moi action status = 3
        stockActionDTO.setStatus(Const.STOCK_TRANS_STATUS.EXPORTED);
        stockActionDTO.setActionCode(null);
        stockActionDTO.setSignCaStatus(null);
        stockTransActionService.save(stockActionDTO);

        //tao moi action status = 6
        stockActionDTO.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
        stockTransActionService.save(stockActionDTO);

        boolean isCheckIm1 = false;
        List<OptionSetValueDTO> lstConfigEnableBccs1 = optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1");
        if (!DataUtil.isNullOrEmpty(lstConfigEnableBccs1) && !DataUtil.isNullObject(lstConfigEnableBccs1.get(0).getValue())) {
            isCheckIm1 = Const.ENABLE_UPDATE_BCCS1.equals(DataUtil.safeToLong(lstConfigEnableBccs1.get(0).getValue()));
        }

        StockTotalAuditDTO totalAuditInput = new StockTotalAuditDTO();
        totalAuditInput.setUserId(stockActionDTO.getActionStaffId());
        totalAuditInput.setCreateDate(stockTransNew.getCreateDatetime());
        totalAuditInput.setTransId(stockTransNew.getStockTransId());
        totalAuditInput.setReasonId(reasonDTO != null ?  reasonDTO.getReasonId() : null);
        totalAuditInput.setTransCode(actionCode);
        totalAuditInput.setTransType(DataUtil.safeToLong(stockTransDTO.getStockTransType()));
        totalAuditInput.setUserCode(vStockTransDTO.getUserCreate());
        totalAuditInput.setStatus(Long.valueOf(Const.STATUS_ACTIVE));

        StockTotalDTO totalDTOSearch = new StockTotalDTO();
        totalDTOSearch.setOwnerId(stockTransDTO.getFromOwnerId());
        totalDTOSearch.setOwnerType(stockTransDTO.getFromOwnerType());


        for (StockTransDetailDTO stockDetail : vStockTransDTO.getTransDetailDTOs()) {
            //check so luong mat hang
            if (DataUtil.isNullOrZero(stockDetail.getQuantity()) || stockDetail.getQuantity().compareTo(0L) < 0) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.serial.quantity.limit.zezo");
            }
            //check ton tai mat hang
            ProductOfferingDTO productOfferingDTO = productOfferingService.findOne(stockDetail.getProdOfferId());
            if (productOfferingDTO == null || productOfferingDTO.getProductOfferTypeId() == null || productOfferingDTO.getStatus() == null || !productOfferingDTO.getStatus().equals(Const.STATUS_ACTIVE)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "balance.valid.prodInfo");
            }
            StockTransDetailDTO detailDTO = DataUtil.cloneBean(stockDetail);

            detailDTO.setStockTransId(stockTransNew.getStockTransId());
            detailDTO.setCreateDatetime(sysdate);
            detailDTO.setStateId(stockDetail.getStateId());
            detailDTO.setPrice(null);
            detailDTO.setAmount(null);

            stockTransDetailService.save(detailDTO);

            totalDTOSearch.setProdOfferId(stockDetail.getProdOfferId());
            totalDTOSearch.setStateId(stockDetail.getStateId());
            totalDTOSearch.setStatus(DataUtil.safeToLong(Const.STATUS.ACTIVE));

            //validate so luong dap ung
            validateExistProdStockTotal(vStockTransDTO.getFromOwnerType(), vStockTransDTO.getFromOwnerID(),detailDTO.getStateId(), detailDTO.getProdOfferId(), detailDTO.getQuantity());
            //tru kho stock_total
            debitTotal(totalDTOSearch, totalAuditInput, stockTransDTO.getFromOwnerType(), stockTransDTO.getFromOwnerId(), stockDetail.getProdOfferId(),
                    detailDTO.getStateId(), detailDTO.getQuantity(), stockActionDTO.getCreateDatetime(), isCheckIm1);
        }
    }

    public void validateStockTotal(String staffCode, List<StockTransDetailDTO> lsTransDetailDTOs) throws LogicException, Exception {

        //check ton tai ma kho xuat
        StaffDTO staffDTO = staffService.getStaffByStaffCode(staffCode);
        if (staffDTO == null) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "warranty.create.underlying.fromOwnerId.exp.staff.notfound", staffCode);
        }

        for (StockTransDetailDTO stockDetail : lsTransDetailDTOs) {
            //check so luong mat hang
            if (DataUtil.isNullOrZero(stockDetail.getQuantity()) || stockDetail.getQuantity().compareTo(0L) < 0) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.serial.quantity.limit.zezo");
            }
            //check ton tai mat hang
            ProductOfferingDTO productOfferingDTO = productOfferingService.getProdOfferDtoByCodeAndStockNew(stockDetail.getProdOfferCode(), staffDTO.getStaffId(), Const.OWNER_TYPE.STAFF_LONG, stockDetail.getStateId());
            if (productOfferingDTO == null || productOfferingDTO.getProductOfferTypeId() == null || productOfferingDTO.getStatus() == null || !productOfferingDTO.getStatus().equals(Const.STATUS_ACTIVE)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "balance.valid.prodInfo");
            }

            //validate so luong dap ung
            validateExistProdStockTotal(Const.OWNER_TYPE.STAFF_LONG, staffDTO.getStaffId(), stockDetail.getStateId(), productOfferingDTO.getProductOfferingId(), stockDetail.getQuantity());
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String excuteCreateTransGift(String staffCode, List<StockTransDetailDTO> lsTransDetailDTOs) throws LogicException, Exception {
        if (DataUtil.isNullOrEmpty(lsTransDetailDTOs)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "change.request.doa.product.empty");
        }
        if (DataUtil.isNullOrEmpty(staffCode)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.rescue.warranty.vaildate.staff");
        }

        //check ton tai ma kho xuat
        StaffDTO staffDTO = staffService.getStaffByStaffCode(staffCode);
        if (staffDTO == null) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "warranty.create.underlying.fromOwnerId.exp.staff.notfound", staffCode);
        }

        Date sysdate = getSysDate(em);

        //them moi stock_trans
        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setFromOwnerId(staffDTO.getStaffId());
        stockTransDTO.setFromOwnerType(Const.OWNER_TYPE.STAFF_LONG);
        stockTransDTO.setToOwnerId(null);
        stockTransDTO.setToOwnerType(Const.OWNER_TYPE.CUSTOMER_LONG);
        stockTransDTO.setCreateDatetime(sysdate);
        stockTransDTO.setStockTransType(Const.STOCK_TRANS_TYPE.EXPORT);
        stockTransDTO.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
        ReasonDTO reasonDTO = reasonService.getReason(Const.REASON_CODE.EXP_MATERIAL_GIFT, Const.REASON_CODE.EXP_MATERIAL_GIFT);
        stockTransDTO.setReasonId(reasonDTO != null ?  reasonDTO.getReasonId() : null);
        StockTransDTO stockTransNew = stockTransService.save(stockTransDTO);

        //tao moi action status = 2
        String actionCode = "PX_" + stockTransNew.getStockTransId();
        StockTransActionDTO stockActionDTO = new StockTransActionDTO();
        stockActionDTO.setActionCode(actionCode);
        stockActionDTO.setStockTransId(stockTransNew.getStockTransId());
        stockActionDTO.setStatus(Const.STOCK_TRANS_STATUS.EXPORT_NOTE);
        stockActionDTO.setCreateUser(staffDTO.getStaffCode());
        stockActionDTO.setCreateDatetime(sysdate);
        stockActionDTO.setActionStaffId(staffDTO.getStaffId());
        stockTransActionService.save(stockActionDTO);

        //tao moi action status = 3
        stockActionDTO.setStatus(Const.STOCK_TRANS_STATUS.EXPORTED);
        stockActionDTO.setActionCode(null);
        stockActionDTO.setSignCaStatus(null);
        stockTransActionService.save(stockActionDTO);

        //tao moi action status = 6
        stockActionDTO.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
        stockTransActionService.save(stockActionDTO);

        boolean isCheckIm1 = false;
        List<OptionSetValueDTO> lstConfigEnableBccs1 = optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1");
        if (!DataUtil.isNullOrEmpty(lstConfigEnableBccs1) && !DataUtil.isNullObject(lstConfigEnableBccs1.get(0).getValue())) {
            isCheckIm1 = Const.ENABLE_UPDATE_BCCS1.equals(DataUtil.safeToLong(lstConfigEnableBccs1.get(0).getValue()));
        }

        StockTotalAuditDTO totalAuditInput = new StockTotalAuditDTO();
        totalAuditInput.setUserId(stockActionDTO.getActionStaffId());
        totalAuditInput.setCreateDate(stockTransNew.getCreateDatetime());
        totalAuditInput.setTransId(stockTransNew.getStockTransId());
        totalAuditInput.setReasonId(reasonDTO != null ?  reasonDTO.getReasonId() : null);
        totalAuditInput.setTransCode(actionCode);
        totalAuditInput.setTransType(DataUtil.safeToLong(stockTransDTO.getStockTransType()));
        totalAuditInput.setUserCode(staffDTO.getStaffCode());
        totalAuditInput.setStatus(Long.valueOf(Const.STATUS_ACTIVE));

        StockTotalDTO totalDTOSearch = new StockTotalDTO();
        totalDTOSearch.setOwnerId(stockTransDTO.getFromOwnerId());
        totalDTOSearch.setOwnerType(stockTransDTO.getFromOwnerType());


        for (StockTransDetailDTO stockDetail : lsTransDetailDTOs) {
            //check so luong mat hang
            if (DataUtil.isNullOrZero(stockDetail.getQuantity()) || stockDetail.getQuantity().compareTo(0L) < 0) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.serial.quantity.limit.zezo");
            }
            //check ton tai mat hang
            ProductOfferingDTO productOfferingDTO = productOfferingService.getProdOfferDtoByCodeAndStockNew(stockDetail.getProdOfferCode(), staffDTO.getStaffId(), Const.OWNER_TYPE.STAFF_LONG, stockDetail.getStateId());
            if (productOfferingDTO == null || productOfferingDTO.getProductOfferTypeId() == null || productOfferingDTO.getStatus() == null || !productOfferingDTO.getStatus().equals(Const.STATUS_ACTIVE)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "balance.valid.prodInfo");
            }
            StockTransDetailDTO detailDTO = DataUtil.cloneBean(stockDetail);

            detailDTO.setStockTransId(stockTransNew.getStockTransId());
            detailDTO.setCreateDatetime(sysdate);
            detailDTO.setProdOfferId(productOfferingDTO.getProductOfferingId());
            detailDTO.setStateId(stockDetail.getStateId());
            detailDTO.setPrice(null);
            detailDTO.setAmount(null);

            stockTransDetailService.save(detailDTO);

            totalDTOSearch.setProdOfferId(detailDTO.getProdOfferId());
            totalDTOSearch.setStateId(detailDTO.getStateId());
            totalDTOSearch.setStatus(DataUtil.safeToLong(Const.STATUS.ACTIVE));

            //validate so luong dap ung
            validateExistProdStockTotal(Const.OWNER_TYPE.STAFF_LONG, staffDTO.getStaffId(), detailDTO.getStateId(), detailDTO.getProdOfferId(), detailDTO.getQuantity());
            //tru kho stock_total
            debitTotal(totalDTOSearch, totalAuditInput, stockTransDTO.getFromOwnerType(), stockTransDTO.getFromOwnerId(), detailDTO.getProdOfferId(),
                    detailDTO.getStateId(), detailDTO.getQuantity(), stockActionDTO.getCreateDatetime(), isCheckIm1);
        }
        return actionCode;
    }

    private void validateExistProdStockTotal(Long ownerType, Long ownerId,  Long stateId, Long prodOfferId, Long requestQuantity) throws Exception {
        ProductOfferingDTO offeringDTO = productOfferingService.findOne(prodOfferId);
        List<ProductOfferingTotalDTO> lsTotal = productOfferingRepo.getLsRequestProductByShop(ownerType, ownerId, offeringDTO.getProductOfferTypeId(), prodOfferId, stateId);
        if (DataUtil.isNullOrEmpty(lsTotal)) {
            String code;
            if (Const.OWNER_TYPE.STAFF_LONG.equals(ownerType)) {
                StaffDTO staffDTO = staffService.findOne(ownerId);
                code = staffDTO != null ? staffDTO.getStaffCode() : "";
            } else {
                ShopDTO shopDTO = shopService.findOne(ownerId);
                code = shopDTO != null ? shopDTO.getShopCode() : "";
            }
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "mn.stock.request.not.found.empty", offeringDTO.getCode(), code);
        }

        Long requireQuantityDB = DataUtil.safeToLong(lsTotal.get(0).getAvailableQuantity()) - DataUtil.safeToLong(lsTotal.get(0).getRequestQuantity());
        if (requireQuantityDB.compareTo(requestQuantity) < 0) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "mn.stock.request.quantity.over",
                    offeringDTO.getCode(), requireQuantityDB, requestQuantity);
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
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.limit.product.exist", offeringDTO.getName());
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
        StringBuilder sql = new StringBuilder("");
        sql.append(" UPDATE stock_total  SET current_quantity  = current_quantity  - #quantity, available_quantity  = available_quantity - #quantity, modified_date=#sysdate ");
        sql.append(" WHERE owner_id = #ownerId AND owner_type = #onwerType AND prod_offer_id = #prodOfferId AND state_id = #oldStateId AND current_quantity >= #quantity AND available_quantity >= #quantity ");
        Query queryObject = em.createNativeQuery(sql.toString());
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

        StringBuilder sql = new StringBuilder("");
        sql.append(" UPDATE bccs_im.stock_total  SET quantity = quantity - #quantity, quantity_issue = quantity_issue - #quantity,modified_date=#sysdate ");
        sql.append(" WHERE owner_id = #ownerId AND owner_type = #onwerType AND stock_model_id = #stockModelId AND state_id = #oldStateId AND quantity >= #quantity AND quantity_issue >= #quantity ");
        Query queryObject = emLib.createNativeQuery(sql.toString());
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
                ownerId, ownerType, prodOfferId, stateId);
        if (stockTotalDTOs == null) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "sell.store.no.info", params);
        }
        if (DataUtil.safeToLong(stockTotalDTOs.getAvailableQuantity()).compareTo(0L) <= 0 || DataUtil.safeToLong(stockTotalDTOs.getCurrentQuantity()).compareTo(0L) <= 0) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "warranty.create.underlying.currentQuantity");
        }
        return stockTotalDTOs.getAvailableQuantity();

    }
}
