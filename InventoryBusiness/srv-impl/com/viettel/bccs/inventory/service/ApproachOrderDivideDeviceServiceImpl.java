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
import com.viettel.bccs.inventory.model.StockRequest;
import com.viettel.bccs.inventory.model.StockTrans;
import com.viettel.bccs.inventory.model.StockTransAction;
import com.viettel.bccs.inventory.repo.ApproachOrderDivideDeviceRepo;
import com.viettel.bccs.inventory.repo.OptionSetValueRepo;
import com.viettel.bccs.inventory.repo.StockRequestRepo;
import com.viettel.bccs.inventory.service.common.IMServiceUtil;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DateUtil;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.fw.service.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.jws.WebMethod;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by vanho on 26/03/2017.
 */
@Service
public class ApproachOrderDivideDeviceServiceImpl  extends BaseServiceImpl implements ApproachOrderDivideDeviceService {
    private final BaseMapper<StockRequest, StockRequestDTO> mapper = new BaseMapper(StockRequest.class, StockRequestDTO.class);
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM_LIB)
    private EntityManager emLib;
    @Autowired
    private ApproachOrderDivideDeviceRepo approachOrderDivideDeviceRepo;
    @Autowired
    private StockRequestService stockRequestService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private StockRequestRepo repository;
    @Autowired
    private StockTransActionService stockTransActionService;
    @Autowired
    private StockTransVofficeService stockTransVofficeService;
    @Autowired
    private StockTransService stockTransService;
    @Autowired
    private OptionSetValueService optionSetValueService;
    @Autowired
    private ProductOfferingService productOfferingService;
    @Autowired
    private StockTotalService stockTotalService;
    @Autowired
    private StockTotalAuditService stockTotalAuditService;
    @Autowired
    private StockTotalIm1Service stockTotalIm1Service;
    @Autowired
    private StaffService staffService;
    @Autowired
    private StockTotalAuditIm1Service stockTotalAuditIm1Service;
    @Autowired
    private OptionSetValueRepo optionSetValueRepo;


    @Override
    public List<StockRequestDTO> getListOrderDivideDevicePendingApproach(String shopPath, Date fromDate, Date toDate, Long ownerId, String requestCode, String status) throws Exception {
        return approachOrderDivideDeviceRepo.getListOrderDivideDevicePendingApproach(shopPath, fromDate, toDate, ownerId, requestCode, status);
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void approachOrderDivide(StockRequestDTO stockRequestDTOSelected) throws LogicException, Exception {
        StockRequestDTO stockRequestDTO = validateBeforePerform(stockRequestDTOSelected);
        Date sysdate = this.getSysDate(this.em);

        stockRequestDTO.setStatus(Const.STATUS_ACTIVE); //da duyet
        stockRequestDTO.setUpdateDatetime(sysdate);
        stockRequestDTO.setUpdateUser(stockRequestDTOSelected.getUpdateUser());
        repository.save(mapper.toPersistenceBean(stockRequestDTO));

        StockTransVofficeDTO stockTransVoffice = new StockTransVofficeDTO();
        String stVofficeId1 = DateUtil.dateToStringWithPattern(sysdate, "yyMMdd") + DataUtil.customFormat("0000000000", (double)this.getSequence(this.em, "STOCK_TRANS_VOFFICE_SEQ").longValue());
        stockTransVoffice.setStockTransVofficeId(stVofficeId1);
        stockTransVoffice.setAccountName(stockRequestDTO.getAccountName());
        stockTransVoffice.setAccountPass(stockRequestDTO.getAccountPass());
        stockTransVoffice.setSignUserList(stockRequestDTO.getSignUserList());
        stockTransVoffice.setCreateDate(sysdate);
        stockTransVoffice.setLastModify(sysdate);
        stockTransVoffice.setStatus("0");

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setStatus("2");
        stockTransActionDTO.setStockTransId(stockRequestDTO.getStockTransId());
        StockTransActionDTO resultSearch = stockTransActionService.findStockTransActionDTO(stockTransActionDTO);

        stockTransVoffice.setStockTransActionId(resultSearch.getStockTransActionId());
        stockTransVoffice.setPrefixTemplate(Const.DIVIDE_DEVICE.PREFIX_TEMPLATE);
        stockTransVoffice.setActionCode(resultSearch.getActionCode());
        stockTransVoffice.setReceiptNo(resultSearch.getActionCode());
        stockTransVofficeService.save(stockTransVoffice);
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void rejectOrderDivide(StockRequestDTO stockRequestDTOSelected, List<StockTransFullDTO> stockTransFullDTOList, Long userRejectId) throws LogicException, Exception {
        StockRequestDTO stockRequestDTO = validateBeforePerform(stockRequestDTOSelected);

        Date sysdate = this.getSysDate(this.em);

        stockRequestDTO.setStatus(Const.STATUS_DELETE); //tu choi
        stockRequestDTO.setUpdateDatetime(sysdate);
        stockRequestDTO.setUpdateUser(stockRequestDTOSelected.getUpdateUser());
        repository.save(mapper.toPersistenceBean(stockRequestDTO));

        //update status --> 8
        StockTransDTO stockTransDTO = stockTransService.findOne(stockRequestDTO.getStockTransId());
        stockTransDTO.setStatus("8");
        stockTransService.save(stockTransDTO);

        //tao log giao dich tu choi
        StockTransActionDTO stockActionDTO = new StockTransActionDTO();
        stockActionDTO.setStockTransId(stockTransDTO.getStockTransId());
        stockActionDTO.setStatus(Const.STOCK_TRANS_STATUS.REJECT);
        stockActionDTO.setCreateUser(stockRequestDTO.getCreateUser());
        stockActionDTO.setCreateDatetime(sysdate);
        stockActionDTO.setActionStaffId(stockRequestDTO.getActionStaffId());
        stockTransActionService.save(stockActionDTO);


        //refund quantity
        boolean isCheckIm1 = false;
        List lstConfigEnableBccs1 = optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1");
        if(!DataUtil.isNullOrEmpty(lstConfigEnableBccs1) && !DataUtil.isNullObject(((OptionSetValueDTO)lstConfigEnableBccs1.get(0)).getValue())) {
            isCheckIm1 = Const.ENABLE_UPDATE_BCCS1.equals(DataUtil.safeToLong(((OptionSetValueDTO)lstConfigEnableBccs1.get(0)).getValue()));
        }

        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setStatus("2");
        stockTransActionDTO.setStockTransId(stockRequestDTO.getStockTransId());
        StockTransActionDTO resultSearch = stockTransActionService.findStockTransActionDTO(stockTransActionDTO);

        StockTotalAuditDTO totalAuditInput = new StockTotalAuditDTO();
        totalAuditInput.setUserId(userRejectId);
        totalAuditInput.setCreateDate(sysdate);
        totalAuditInput.setTransId(stockTransDTO.getStockTransId());
        totalAuditInput.setTransCode(resultSearch.getActionCode());
        totalAuditInput.setTransType(DataUtil.safeToLong(stockTransDTO.getStockTransType()));
        totalAuditInput.setUserCode(stockRequestDTO.getUpdateUser());
        totalAuditInput.setStatus(Long.valueOf("1"));
        StockTotalDTO totalDTOSearch = new StockTotalDTO();
        totalDTOSearch.setOwnerId(stockTransDTO.getFromOwnerId());
        totalDTOSearch.setOwnerType(stockTransDTO.getFromOwnerType());
        //tao chi tiet giao dich xuat kho
        for(StockTransFullDTO stockTransFullDTO: stockTransFullDTOList) {

            ProductOfferingDTO productOfferingDTO = productOfferingService.findOne(stockTransFullDTO.getProdOfferId());
            if(productOfferingDTO == null || productOfferingDTO.getProductOfferTypeId() == null || productOfferingDTO.getStatus() == null || !"1".equals(productOfferingDTO.getStatus())) {
                throw new LogicException("105", "balance.valid.prodInfo");
            }

            totalDTOSearch.setProdOfferId(stockTransFullDTO.getProdOfferId());
            totalDTOSearch.setStateId(stockTransFullDTO.getStateId());
            totalDTOSearch.setStatus(DataUtil.safeToLong("1"));

            refundTotal(totalDTOSearch, totalAuditInput, stockTransDTO.getFromOwnerType(), stockTransDTO.getFromOwnerId(), stockTransFullDTO.getProdOfferId(), stockTransFullDTO.getStateId(), stockTransFullDTO.getQuantity(), sysdate, isCheckIm1);
            for(StockTransSerialDTO stockSerialNew : stockTransFullDTO.getLstSerial()) {
                updateStockX(stockTransFullDTO.getStateId(), stockSerialNew.getFromSerial(), stockSerialNew.getToSerial(), sysdate, stockSerialNew.getProdOfferId(), productOfferingDTO.getProductOfferTypeId(), stockTransDTO.getFromOwnerType(), stockTransDTO.getFromOwnerId(), isCheckIm1);
            }
        }
    }

    public void updateStockX(Long stateId, String fromSerial, String toSerial, Date updateDateTime, Long prodOfferId, Long productOfferTypeId, Long ownerType, Long ownerId, boolean isCheckIm1) throws Exception {
        String tableName = IMServiceUtil.getTableNameByOfferType(productOfferTypeId);
        String sql = "update " + tableName + " set state_id = #stateId, status = 1, update_datetime=#updateDateTime   " + "where prod_offer_id=#prodOfferId and owner_type =#ownerType and owner_id = #ownerId and status= 3 ";
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
            params[1] = (String)mapState.get("1");
            params[2] = fromSerial;
            params[3] = toSerial;
            throw new LogicException("102", this.getText("stockTrans.validate.quantity.serial"), params);
        }else {
            if(isCheckIm1) {
                this.updateStockXIM1(stateId, fromSerial, toSerial, prodOfferId, productOfferTypeId, ownerType, ownerId);
            }
        }
    }

    private void updateStockXIM1(Long stateId, String fromSerial, String toSerial, Long stockModelId, Long productOfferTypeId, Long ownerType, Long ownerId) throws Exception {
        StockModelIm1DTO offeringDTO = this.stockTotalIm1Service.findOneStockModel(stockModelId);
        if(offeringDTO == null) {
            throw new LogicException("09", "validate.stock.inspect.productOffering.bccs1.notFound", new Object[]{stockModelId});
        } else if(!Const.PRODUCT_OFFERING.CHECK_SERIAL.equals(offeringDTO.getCheckSerial())) {
            throw new LogicException("09", "validate.stock.inspect.productOffering.bccs1.notFound", new Object[]{stockModelId});
        } else {
            StockTypeIm1DTO stockTypeIm1DTO = this.stockTotalIm1Service.findOneStockType(stockModelId);
            if(!DataUtil.isNullObject(stockTypeIm1DTO)) {
                String tableName = stockTypeIm1DTO.getTableName();
                String sql1 = "update bccs_im." + tableName + " set state_id = #stateId, status = 1  " + "where stock_model_id=#stockModelId and owner_type =#ownerType and owner_id = #ownerId and status= 3 ";
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
                    String[] params = new String[]{offeringDTO.getName(), (String)mapState.get("1"), fromSerial, toSerial};
                    throw new LogicException("102", this.getText("stockTrans.validate.quantity.serial.bccs1"), params);
                }
            } else {
                ProductOfferingDTO sql = this.productOfferingService.findOne(stockModelId);
                throw new LogicException("102", "stockTrans1.validate.stockmodel.not.exist", new Object[]{sql.getName()});
            }
        }
    }

    public void refundTotal(StockTotalDTO totalDTOSearch, StockTotalAuditDTO totalAuditInput, Long ownerType, Long ownerId, Long prodOfferId, Long oldStateId, Long quantity, Date sysdate, boolean isCheckIm1) throws Exception {
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
        totalAuditInsert.setCurrentQuantity(Long.valueOf(DataUtil.safeToLong(quantity).longValue()));
        totalAuditInsert.setCurrentQuantityAf(Long.valueOf(DataUtil.safeToLong(stockTotalDTO1.getCurrentQuantity()).longValue() + DataUtil.safeToLong(quantity).longValue()));
        totalAuditInsert.setAvailableQuantityBf(DataUtil.safeToLong(stockTotalDTO1.getAvailableQuantity()));
        totalAuditInsert.setAvailableQuantity(Long.valueOf(DataUtil.safeToLong(quantity).longValue()));
        totalAuditInsert.setAvailableQuantityAf(Long.valueOf(DataUtil.safeToLong(stockTotalDTO1.getAvailableQuantity()).longValue() + DataUtil.safeToLong(quantity).longValue()));
        totalAuditInsert.setSourceType(Const.STOCK_TOTAL_AUDIT.SOURCE_TYPE_STOCK_TRANS);
        this.stockTotalAuditService.save(totalAuditInsert);
        String sql = " UPDATE stock_total  SET current_quantity  = current_quantity  + #quantity, available_quantity  = available_quantity + #quantity, modified_date=#sysdate WHERE owner_id = #ownerId AND owner_type = #onwerType AND prod_offer_id = #prodOfferId AND state_id = #oldStateId";
        Query queryObject = this.em.createNativeQuery(sql);
        queryObject.setParameter("ownerId", ownerId);
        queryObject.setParameter("onwerType", ownerType);
        queryObject.setParameter("prodOfferId", prodOfferId);
        queryObject.setParameter("oldStateId", oldStateId);
        queryObject.setParameter("quantity", quantity);
        queryObject.setParameter("sysdate", sysdate);
        queryObject.executeUpdate();

        if(isCheckIm1) {
            this.refundTotalIm1(totalDTOSearch, totalAuditInput, ownerId, ownerType, prodOfferId, oldStateId, quantity, sysdate);
        }
    }

    private void refundTotalIm1(StockTotalDTO totalDTOSearch, StockTotalAuditDTO totalAuditInput, Long ownerId, Long ownerType, Long prodOfferId, Long oldStateId, Long quantity, Date sysdate) throws Exception {
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
            sql1.setQtyAf(Long.valueOf(DataUtil.safeToLong(((StockTotalIm1DTO)lstStockTotal.get(0)).getQuantity()).longValue() + DataUtil.safeToLong(quantity).longValue()));
            sql1.setQtyIssueBf(DataUtil.safeToLong(((StockTotalIm1DTO)lstStockTotal.get(0)).getQuantityIssue()));
            sql1.setQtyIssue(Long.valueOf(-DataUtil.safeToLong(quantity).longValue()));
            sql1.setQtyIssueAf(Long.valueOf(DataUtil.safeToLong(((StockTotalIm1DTO)lstStockTotal.get(0)).getQuantityIssue()).longValue() + DataUtil.safeToLong(quantity).longValue()));
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

        String sql2 = " UPDATE bccs_im.stock_total  SET quantity = quantity + #quantity, quantity_issue = quantity_issue + #quantity,modified_date=#sysdate WHERE owner_id = #ownerId AND owner_type = #onwerType AND stock_model_id = #stockModelId AND state_id = #oldStateId AND quantity >= #quantity AND quantity_issue >= #quantity";
        Query queryObject1 = this.emLib.createNativeQuery(sql2);
        queryObject1.setParameter("ownerId", ownerId);
        queryObject1.setParameter("onwerType", ownerType);
        queryObject1.setParameter("stockModelId", prodOfferId);
        queryObject1.setParameter("oldStateId", oldStateId);
        queryObject1.setParameter("quantity", quantity);
        queryObject1.setParameter("sysdate", sysdate);
        queryObject1.executeUpdate();
    }

    private StockRequestDTO validateBeforePerform(StockRequestDTO stockRequestDTOSelected) throws LogicException, Exception{

        if(stockRequestDTOSelected == null){
            throw new LogicException("105", "change.request.doa.empty");
        }

        String requestCode = stockRequestDTOSelected.getRequestCode();
        if (DataUtil.isNullOrEmpty(requestCode)) {
            throw new LogicException("105", "change.request.doa.empty");
        }
        StockRequestDTO stockRequestDTO;
        List<StockRequestDTO> stockRequestDTOs = stockRequestService.findStockRequestByCode(requestCode);
        if (DataUtil.isNullOrEmpty(stockRequestDTOs)) {
            throw new LogicException("105", "order.divide.isNotExist", new Object[]{requestCode});
        } else {
            stockRequestDTO = stockRequestDTOs.get(0);
            if (stockRequestDTO.getOwnerId() != null) {
                if (Const.OWNER_TYPE.SHOP_LONG.equals(stockRequestDTO.getOwnerType())) {
                    ShopDTO sysdate1 = shopService.findOne(stockRequestDTO.getOwnerId());
                    if (sysdate1 == null || !"1".equals(sysdate1.getStatus())) {
                        throw new LogicException("04", "warranty.create.underlying.fromOwnerId.exp.shop.notfound", new Object[]{stockRequestDTO.getOwnerCode()});
                    }
                    if("1".equals(stockRequestDTOs.get(0).getStatus())){
                        throw new LogicException("05", "order.divide.record.hasApproved");
                    } else if("2".equals(stockRequestDTOs.get(0).getStatus())){
                        throw new LogicException("05", "order.divide.record.hasReject");
                    }

                } else {
                    throw new LogicException("05", "create.request.divide.device.wrongOwnerType");
                }
            } else {
                throw new LogicException("05", "order.ownerId.isUndefine");
            }
        }

        return stockRequestDTO;
    }
}
