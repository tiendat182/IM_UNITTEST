package com.viettel.bccs.im1.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.im1.dto.*;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.*;
import com.viettel.bccs.inventory.service.common.IMServiceUtil;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.RequiredRoleMap;
import com.viettel.fw.service.BaseServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by thaont19 on 2/27/2016.
 */
@Service
public class BaseStockIm1Service extends BaseServiceImpl {
    public static final Logger logger = Logger.getLogger(BaseStockIm1Service.class);

    @Autowired
    private StockTotalIm1Service stockTotalIm1Service;

    @Autowired
    private StockTotalAuditIm1Service stockTotalAuditIm1Service;

    @Autowired
    private StaffService staffService;

    @Autowired
    private ShopService shopService;

    @Autowired
    private ProductOfferingService productOfferingService;

    @Autowired
    private StaffIm1Service staffIm1Service;

    @Autowired
    private ShopIm1Service shopIm1Service;

    @Autowired
    private StockTransActionService stockTransActionService;

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM_LIB)
    private EntityManager em;

    public void doSaveStockTotal(StockTransDTO stockTransDTO, List<StockTransDetailDTO> lstStockTransDetailDTO,
                                 FlagStockDTO flagStockDTO, StockTransActionDTO stockTransActionDTO) throws LogicException, Exception {
        if (DataUtil.safeEqual(Const.PROCESS_OFFLINE, stockTransDTO.getProcessOffline())) {
            return;
        }
        if (!flagStockDTO.isExportStock() && !flagStockDTO.isImportStock()) {
            return;
        }

//        StaffIm1DTO staff = staffIm1Service.findOne(stockTransActionDTO.getActionStaffId());
//        if (DataUtil.isNullObject(staff)
//                || DataUtil.isNullObject(staff.getStaffId())) {
//            throw new LogicException("", getText("crt.err.staff.not.found.im1"));
//        }
//        flagStockDTO.setShopId(staff.getShopId());

        //Neu la giao dich xuat dieu chuyen --> chi cong so luong thuc te, va cap nhap serial ve =3
        if (!DataUtil.isNullOrZero(stockTransDTO.getExchangeStockId())) {
            flagStockDTO.setImpAvailableQuantity(0L);
        }

        //Cap nhat kho xuat
        Long fromOwnerId = stockTransDTO.getFromOwnerId();
        Long fromOwnerType = stockTransDTO.getFromOwnerType();
        if ((stockTransDTO.getRegionStockId() != null)
                && (DataUtil.isNullOrEmpty(flagStockDTO.getStatusForAgent()) || DataUtil.safeEqual(flagStockDTO.getStatusForAgent(), Const.STOCK_TRANS_STATUS.EXPORTED))
                && (Const.L_VT_SHOP_ID.equals(flagStockDTO.getShopId()))) {
            //Neu o cap VT thi thao tac voi kho 3 mien: Xuat hang tu kho 3 mien
            fromOwnerId = stockTransDTO.getRegionStockId();
        }

        //Cap nhat kho nhan
        Long toOwnerId = stockTransDTO.getToOwnerId();
        Long toOwnerType = stockTransDTO.getToOwnerType();
        if (stockTransDTO.getRegionStockId() != null
                && (DataUtil.isNullOrEmpty(flagStockDTO.getStatusForAgent()) || DataUtil.safeEqual(flagStockDTO.getStatusForAgent(), Const.STOCK_TRANS_STATUS.IMPORTED))
                && (Const.L_VT_SHOP_ID.equals(flagStockDTO.getShopId()))) {
            //Neu o cap VT thi thao tac voi kho 3 mien: Nhap hang ve kho 3 mien
            toOwnerId = stockTransDTO.getRegionStockId();
        }

        List<StockTransDetailIm1DTO> lstStockTransDetailIm1 = convertStockTransDetail(lstStockTransDetailDTO, stockTransDTO);

        if (DataUtil.isNullOrEmpty(lstStockTransDetailIm1)) {
            lstStockTransDetailIm1 = stockTotalIm1Service.findStockTransDetail(stockTransDTO.getStockTransId());
        }

        List<StockTotalIm1DTO> lstExpStockTotalDTO = new ArrayList<>();
        List<StockTotalIm1DTO> lstImpStockTotalDTO = new ArrayList<>();
        for (StockTransDetailIm1DTO stockTransDetail : lstStockTransDetailIm1) {
            StockTotalIm1DTO stockTotal;
            if (flagStockDTO.isExportStock()) {
                stockTotal = new StockTotalIm1DTO();
                stockTotal.setOwnerId(fromOwnerId);
                stockTotal.setOwnerType(fromOwnerType);
                stockTotal.setStockModelId(stockTransDetail.getStockModelId());
                stockTotal.setStockModelName(stockTransDetail.getStockModelName());
                stockTotal.setStateId(stockTransDetail.getStateId());
                stockTotal.setQuantityIssue(flagStockDTO.getExpAvailableQuantity() * stockTransDetail.getQuantityReal());
                stockTotal.setQuantity(flagStockDTO.getExpCurrentQuantity() * stockTransDetail.getQuantityReal());
                stockTotal.setQuantityHang(flagStockDTO.getExpHangQuantity() * stockTransDetail.getQuantityReal());
                stockTotal.setStatus(DataUtil.safeToLong(Const.STATUS.ACTIVE));
                stockTotal.setModifiedDate(stockTransDTO.getCreateDatetime());

                lstExpStockTotalDTO.add(stockTotal);
            }
            if (flagStockDTO.isImportStock()) {
                // Neu xuat kho ban hang dai ly thi khong cong vao stock_total dai ly.
                if (!DataUtil.isNullObject(flagStockDTO.getObjectType()) && DataUtil.safeEqual(flagStockDTO.getObjectType(), Const.STOCK_TRANS.OBJECT_TYPE_AGENT_EXPORT)
                        && !DataUtil.isNullOrEmpty(stockTransDTO.getPayStatus()) && DataUtil.safeEqual(Const.PAY_STATUS.PAY_HAVE, stockTransDTO.getPayStatus())) {
                    continue;
                }
                stockTotal = new StockTotalIm1DTO();
                stockTotal.setOwnerId(toOwnerId);
                stockTotal.setOwnerType(toOwnerType);
                stockTotal.setStockModelId(stockTransDetail.getStockModelId());
                stockTotal.setStockModelName(stockTransDetail.getStockModelName());
                if ((Const.REASON_TYPE.RETRIEVE_STOCK).equals(flagStockDTO.getNote())) {
                    stockTotal.setStateId(DataUtil.safeToLong(stockTransDetail.getStrStateIdAfter()));
                } else if (!DataUtil.isNullOrZero(flagStockDTO.getStateIdForReasonId())) {
                    stockTotal.setStateId(flagStockDTO.getStateIdForReasonId());
                } else {
                    stockTotal.setStateId(stockTransDetail.getStateId());
                }
                stockTotal.setQuantityIssue(flagStockDTO.getImpAvailableQuantity() * stockTransDetail.getQuantityReal());
                stockTotal.setQuantity(flagStockDTO.getImpCurrentQuantity() * stockTransDetail.getQuantityReal());
                stockTotal.setQuantityHang(flagStockDTO.getImpHangQuantity() * stockTransDetail.getQuantityReal());
                stockTotal.setModifiedDate(stockTransDTO.getCreateDatetime());
                stockTotal.setStatus(DataUtil.safeToLong(Const.STATUS.ACTIVE));
                lstImpStockTotalDTO.add(stockTotal);
            }
        }

        // Luu stock_total_audit
        StockTotalAuditIm1DTO stockTotalAuditDTO = null;
        if (flagStockDTO.isInsertStockTotalAudit()) {
            stockTotalAuditDTO = new StockTotalAuditIm1DTO();
            stockTotalAuditDTO.setUserId(stockTransActionDTO.getActionStaffId());
            stockTotalAuditDTO.setCreateDate(stockTransDTO.getCreateDatetime());
            stockTotalAuditDTO.setReasonId(stockTransDTO.getReasonId());
            stockTotalAuditDTO.setTransId(stockTransDTO.getStockTransId());
            stockTotalAuditDTO.setTransCode(stockTransActionDTO.getActionCode());
            stockTotalAuditDTO.setTransType(DataUtil.safeToLong(stockTransDTO.getStockTransType()));
        }

        //Cap nhat so luong kho xuat
        doSaveStockTotal(stockTotalAuditDTO, lstExpStockTotalDTO, stockTransActionDTO);

        //Cap nhat so luong kho nhap
        doSaveStockTotal(stockTotalAuditDTO, lstImpStockTotalDTO, stockTransActionDTO);
    }

    protected void doSaveStockTotal(StockTotalAuditIm1DTO stockTotalAuditDTO, List<StockTotalIm1DTO> lstStockTotalDTO, StockTransActionDTO stockTransAction) throws LogicException, Exception {

        if (DataUtil.isNullOrEmpty(lstStockTotalDTO)) {
            return;
        }

        for (StockTotalIm1DTO stockTotalDTO : lstStockTotalDTO) {

            List<StockTotalIm1DTO> lstStockTotal = stockTotalIm1Service.findStockTotal(stockTotalDTO);
            if (!DataUtil.isNullOrEmpty(lstStockTotal) && lstStockTotal.size() > 1) {
                StockModelIm1DTO offeringDTO = stockTotalIm1Service.findOneStockModel(lstStockTotal.get(0).getStockModelId());
                if (offeringDTO != null) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans1.validate.limit.product.exist", offeringDTO.getName());
                }
            }

            //Truong hop update
            if (!DataUtil.isNullOrEmpty(lstStockTotal)) {
                StockTotalIm1DTO stockTotalToUpdate = lstStockTotal.get(0);
                StockTotalIm1DTO stockTotal = DataUtil.cloneBean(stockTotalToUpdate);
                stockTotalToUpdate.setQuantityIssue(DataUtil.safeToLong(stockTotal.getQuantityIssue()) + stockTotalDTO.getQuantityIssue());
                stockTotalToUpdate.setQuantity(DataUtil.safeToLong(stockTotal.getQuantity()) + stockTotalDTO.getQuantity());
                stockTotalToUpdate.setQuantityHang(DataUtil.safeToLong(stockTotal.getQuantityHang()) + stockTotalDTO.getQuantityHang());
                stockTotalToUpdate.setModifiedDate(stockTotalDTO.getModifiedDate());
                //check quantity >=0
                if (!checkQuantityStockTotalPositve(stockTotalToUpdate)) {
                    ProductOfferingDTO offeringDTO = productOfferingService.findOne(stockTotalToUpdate.getStockModelId());
                    if (offeringDTO != null) {
                        String[] msgParams = new String[2];
                        msgParams[0] = offeringDTO.getName();
                        msgParams[1] = productOfferingService.getStockStateName(stockTotalToUpdate.getStateId());
                        throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans1.validate.quantity.negative", msgParams);
                    }
                }
                stockTotalIm1Service.update(stockTotalToUpdate);
                // Luu stock_total_audit
                if (!DataUtil.isNullObject(stockTotalAuditDTO)) {
                    StockTotalAuditIm1DTO stockTotalAuditInsert = DataUtil.cloneBean(stockTotalAuditDTO);
                    stockTotalAuditInsert.setOwnerId(stockTotalDTO.getOwnerId());
                    stockTotalAuditInsert.setOwnerType(stockTotalDTO.getOwnerType());
                    stockTotalAuditInsert.setStockModelId(stockTotalToUpdate.getStockModelId());
                    stockTotalAuditInsert.setStockModelName(stockTotalDTO.getStockModelName());
                    stockTotalAuditInsert.setStateId(stockTotalToUpdate.getStateId());
                    stockTotalAuditInsert.setQtyBf(DataUtil.safeToLong(stockTotal.getQuantity()));
                    stockTotalAuditInsert.setQty(DataUtil.safeToLong(stockTotalDTO.getQuantity()));
                    stockTotalAuditInsert.setQtyAf(DataUtil.safeToLong(stockTotal.getQuantity()) + stockTotalDTO.getQuantity());
                    // available quantity
                    stockTotalAuditInsert.setQtyIssueBf(DataUtil.safeToLong(stockTotal.getQuantityIssue()));
                    stockTotalAuditInsert.setQtyIssue(DataUtil.safeToLong(stockTotalDTO.getQuantityIssue()));
                    stockTotalAuditInsert.setQtyIssueAf(DataUtil.safeToLong(stockTotal.getQuantityIssue()) + stockTotalDTO.getQuantityIssue());
                    stockTotalAuditInsert.setTransCode(stockTransAction.getActionCode());
                    doSaveStockTotalAudit(stockTotalAuditInsert);
                }
            } else {
                //Truong hop them moi
                //check quantity >=0
                if (!checkQuantityStockTotalPositve(stockTotalDTO)) {
                    ProductOfferingDTO offeringDTO = productOfferingService.findOne(stockTotalDTO.getStockModelId());
                    if (offeringDTO != null) {
                        throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans1.validate.quantity.negative", offeringDTO.getName());
                    }
                }
                stockTotalIm1Service.create(stockTotalDTO);
                // Luu stock_total_audit
                if (!DataUtil.isNullObject(stockTotalAuditDTO)) {
                    StockTotalAuditIm1DTO stockTotalAuditInsert = DataUtil.cloneBean(stockTotalAuditDTO);
                    stockTotalAuditInsert.setOwnerId(stockTotalDTO.getOwnerId());
                    stockTotalAuditInsert.setOwnerType(stockTotalDTO.getOwnerType());
                    stockTotalAuditInsert.setStockModelId(stockTotalDTO.getStockModelId());
                    stockTotalAuditInsert.setStockModelName(stockTotalDTO.getStockModelName());
                    stockTotalAuditInsert.setStateId(stockTotalDTO.getStateId());
                    stockTotalAuditInsert.setQtyBf(0L);
                    stockTotalAuditInsert.setQty(DataUtil.safeToLong(stockTotalDTO.getQuantity()));
                    stockTotalAuditInsert.setQtyAf(stockTotalDTO.getQuantity());
                    // available quantity
                    stockTotalAuditInsert.setQtyIssueBf(0L);
                    stockTotalAuditInsert.setQtyIssue(DataUtil.safeToLong(stockTotalDTO.getQuantityIssue()));
                    stockTotalAuditInsert.setQtyIssueAf(stockTotalDTO.getQuantityIssue());
                    stockTotalAuditInsert.setTransCode(stockTransAction.getActionCode());
                    doSaveStockTotalAudit(stockTotalAuditInsert);
                }
            }
        }
    }

    private boolean checkQuantityStockTotalPositve(StockTotalIm1DTO stockTotal) {
        if (stockTotal.getQuantity() >= 0 && stockTotal.getQuantityIssue() >= 0) {
            return true;
        }
        return false;
    }

    protected void doSaveStockTotalAudit(StockTotalAuditIm1DTO stockTotalAuditDTO) throws Exception {
        if (DataUtil.isNullObject(stockTotalAuditDTO)) {
            return;
        }
        stockTotalAuditDTO.setSourceType(Const.STOCK_TOTAL_AUDIT.SOURCE_TYPE_STOCK_TRANS);
        StaffDTO staffDTO = staffService.findOne(stockTotalAuditDTO.getUserId());
        ShopDTO shopDTO = shopService.findOne(stockTotalAuditDTO.getOwnerId());
        stockTotalAuditDTO.setUserCode(staffDTO.getStaffCode());
        stockTotalAuditDTO.setUserName(staffDTO.getName());
        if (!DataUtil.isNullObject(shopDTO)) {
            stockTotalAuditDTO.setOwnerCode(shopDTO.getShopCode());
            stockTotalAuditDTO.setOwnerName(shopDTO.getName());
        }
        stockTotalAuditIm1Service.create(stockTotalAuditDTO);
    }

    public void doSaveStockGoods(StockTransDTO stockTransDTO, List<StockTransDetailDTO> lstStockTransDetail,
                                 FlagStockDTO flagStockDTO) throws LogicException, Exception {
        if (DataUtil.safeEqual(Const.PROCESS_OFFLINE, stockTransDTO.getProcessOffline())) {
            return;
        }
        if (DataUtil.isNullObject(flagStockDTO.getOldStatus()) || DataUtil.isNullObject(flagStockDTO.getNewStatus())) {
            return;
        }
        //Neu la giao dich xuat dieu chuyen --> cap nhap serial ve =3
        if (!DataUtil.isNullOrZero(stockTransDTO.getExchangeStockId())) {
            flagStockDTO.setNewStatus(Const.STOCK_GOODS.STATUS_CONFIRM);
        }

        Long oldOwnerType, oldOwnerId;
        Long newOwnerType = null, newOwnerId = null;

        if (!DataUtil.isNullOrZero(stockTransDTO.getToOwnerType()) && !DataUtil.isNullOrZero(stockTransDTO.getToOwnerId()) && flagStockDTO.isUpdateOwnerId()) {
            if (DataUtil.isNullOrZero(stockTransDTO.getRegionStockId())
                    || DataUtil.safeEqual(flagStockDTO.getStatusForAgent(), Const.STOCK_TRANS_STATUS.EXPORTED)
                    || (!Const.L_VT_SHOP_ID.equals(flagStockDTO.getShopId()))) {
                newOwnerType = stockTransDTO.getToOwnerType();
                newOwnerId = stockTransDTO.getToOwnerId();
            } else {
                //chon kho 3 mien
                newOwnerType = stockTransDTO.getToOwnerType();
                newOwnerId = stockTransDTO.getRegionStockId();
            }
        }

        if ((Const.STOCK_TRANS_STATUS.EXPORTED.equals(stockTransDTO.getStatus())
                || DataUtil.safeEqual(flagStockDTO.getStatusForAgent(), Const.STOCK_TRANS_STATUS.EXPORTED)
                || Const.STOCK_TRANS_STATUS.IMPORTED.equals(stockTransDTO.getStatus()))
                && !DataUtil.isNullOrZero(stockTransDTO.getRegionStockId())) {
            //neu xuat kho va chon kho 3 mien o lap lenh
            oldOwnerType = stockTransDTO.getFromOwnerType();
            oldOwnerId = stockTransDTO.getRegionStockId();

            //Neu la Nhap hang tu chi nhanh
            if (Const.STOCK_TRANS_STATUS.IMPORTED.equals(stockTransDTO.getStatus()) && Const.L_VT_SHOP_ID.equals(flagStockDTO.getShopId())) {
                oldOwnerType = stockTransDTO.getFromOwnerType();
                oldOwnerId = stockTransDTO.getFromOwnerId();
            }
        } else {
            oldOwnerType = stockTransDTO.getFromOwnerType();
            oldOwnerId = stockTransDTO.getFromOwnerId();
        }

        //thu hoi, xuat UCTT
        if (flagStockDTO.isUpdateRescue() && !DataUtil.isNullOrZero(flagStockDTO.getStateId())) {
            if (!Const.REASON_TYPE.RETRIEVE_STOCK.equals(flagStockDTO.getNote())) {
                oldOwnerType = stockTransDTO.getFromOwnerType();
                oldOwnerId = stockTransDTO.getFromOwnerId();
            } else {
                oldOwnerType = 2L;
                newOwnerType = 2L;
                if (!DataUtil.isNullOrZero(stockTransDTO.getFromOwnerId()) && !DataUtil.isNullOrZero(stockTransDTO.getToOwnerId())) {
                    oldOwnerId = stockTransDTO.getToOwnerId();
                    newOwnerId = stockTransDTO.getFromOwnerId();
                }
            }
        }

        List<StockTransDetailIm1DTO> lstStockTransDetailIm1 = convertStockTransDetail(lstStockTransDetail, stockTransDTO);
        int count;
        int total;
        BigInteger fromSerial = null;
        BigInteger toSerial = null;
        for (StockTransDetailIm1DTO stockTransDetail : lstStockTransDetailIm1) {
            //check trang thai hang hoa
            if (DataUtil.isNullOrZero(stockTransDetail.getStockModelId())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans1.validate.quantity.serial.detail");
            }
            StockTypeIm1DTO stockTypeIm1DTO = stockTotalIm1Service.findOneStockType(stockTransDetail.getStockModelId());
            //lay lai tableName tu stockModelId
            String tableName;

            if (!DataUtil.isNullObject(stockTypeIm1DTO)) {
                tableName = stockTypeIm1DTO.getTableName();
            } else {
                //bao loi ko ton tai mat hang tren BCCS1
                ProductOfferingDTO productOfferingDTO = productOfferingService.findOne(stockTransDetail.getStockModelId());
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans1.validate.stockmodel.not.exist", productOfferingDTO.getName());
            }

            if (DataUtil.isNullOrEmpty(tableName)) {
                continue;
            }

            StringBuilder sqlUpdate = new StringBuilder("");
            sqlUpdate.append("UPDATE    bccs_im.").append(tableName).append(" ");
            sqlUpdate.append("   SET    status = #newStatus ");
            if (flagStockDTO.isUpdateSaleDate()) {
                sqlUpdate.append("          ,sale_date = sysdate ");
            }
            if (!DataUtil.isNullOrZero(stockTransDTO.getToOwnerType()) && !DataUtil.isNullOrZero(stockTransDTO.getToOwnerId())
                    && flagStockDTO.isUpdateOwnerId()) {
                sqlUpdate.append("     ,owner_type = #newOwnerType ");
                sqlUpdate.append("     ,owner_id = #newOwnerId ");
            }
            boolean isUpdateDepositStatus = false;
            if (flagStockDTO.isUpdateAccountBalance() && !DataUtil.isNullObject(stockTransDTO.getBankplusStatus())) {
                sqlUpdate.append("     ,bankplus_status = #bankplusStatus ");
                if (DataUtil.safeEqual(stockTransDTO.getBankplusStatus(), Const.STOCK_TRANS.BANKPLUS_STATUS_NOT_APPROVE)) {
                    sqlUpdate.append("     ,deposit_price = 0 ");
                    isUpdateDepositStatus = true;
                }
            }
            if (!DataUtil.isNullOrZero(stockTransDetail.getDepositPrice()) && !isUpdateDepositStatus) {
                sqlUpdate.append("     ,deposit_price = #depositPrice ");
            }
            if (!DataUtil.isNullOrZero(flagStockDTO.getStateIdForReasonId())) {
                sqlUpdate.append("     ,state_id = #stateId ");
            }
            if (flagStockDTO.isUpdateProdOfferId()) {
                sqlUpdate.append("     ,stock_model_id = #newStockModelId ");
            }

            //thu hoi, xuat UCTT
            if (flagStockDTO.isUpdateRescue() && !DataUtil.isNullOrZero(flagStockDTO.getStateId())) {
                if (Const.REASON_TYPE.RETRIEVE_STOCK.equals(flagStockDTO.getNote())) {
                    sqlUpdate.append("     ,owner_type = #oldOwnerType, owner_Id = #oldOwnerId ");
                }
                if (!DataUtil.isNullOrZero(flagStockDTO.getStateId())) {
                    sqlUpdate.append("     ,state_id = #stateId ");
                }
            }

            sqlUpdate.append(" WHERE    stock_model_id = #stockModelId ");
            if (!flagStockDTO.isUpdateDamageProduct()) {
                if (flagStockDTO.isUpdateRescue() && Const.REASON_TYPE.RETRIEVE_STOCK.equals(flagStockDTO.getNote())) {
                    sqlUpdate.append("   AND    owner_type = #newOwnerType ");
                    sqlUpdate.append("   AND    owner_id = #newOwnerId ");
                } else {
                    sqlUpdate.append("   AND    owner_type = #oldOwnerType ");
                    sqlUpdate.append("   AND    owner_id = #oldOwnerId ");
                }
            }
            if (Const.PRODUCT_OFFER_TYPE.PHONE.equals(stockTransDetail.getStockTypeId())) {
                sqlUpdate.append("AND   serial >= #fromSerial ");
                sqlUpdate.append("AND   serial <= #toSerial ");
            } else {
                sqlUpdate.append("AND   to_number(serial) >= #fromSerial ");
                sqlUpdate.append("AND   to_number(serial) <= #toSerial ");
            }
            if (!flagStockDTO.isUpdateDamageProduct()) {
                if (flagStockDTO.isUpdateRescue()) {
                    //thu hoi, xuat UCTT
                    sqlUpdate.append("    AND   status = #oldStatus ");
                } else {
                    sqlUpdate.append("    AND   status = #oldStatus AND state_id = #oldStateId ");
                }
//            } else {
//                sqlUpdate.append("    AND   status = :oldStatus ");
            }

            List<StockTransSerialIm1DTO> lstStockTransSerial = stockTransDetail.getLstStockTransSerial();

            if (!DataUtil.isNullOrEmpty(lstStockTransSerial)) {
                total = 0;
                for (StockTransSerialIm1DTO stockTransSerial : lstStockTransSerial) {
                    if (!Const.PRODUCT_OFFER_TYPE.PHONE.equals(stockTransDetail.getStockTypeId())) {
                        fromSerial = new BigInteger(stockTransSerial.getFromSerial());
                        toSerial = new BigInteger(stockTransSerial.getToSerial());
                    }

                    Query query = em.createNativeQuery(sqlUpdate.toString());
                    // Neu xuat ban dut cho dai ly thi cap nhat trang thai hang ve = 0.
                    if (!DataUtil.isNullObject(flagStockDTO.getObjectType()) && DataUtil.safeEqual(flagStockDTO.getObjectType(), Const.STOCK_TRANS.OBJECT_TYPE_AGENT_EXPORT)
                            && !DataUtil.isNullOrEmpty(stockTransDTO.getPayStatus()) && DataUtil.safeEqual(Const.PAY_STATUS.PAY_HAVE, stockTransDTO.getPayStatus())) {
                        query.setParameter("newStatus", Const.STOCK_GOODS.STATUS_SALE);
                    } else {
                        query.setParameter("newStatus", flagStockDTO.getNewStatus());
                    }

                    if (!DataUtil.isNullOrZero(stockTransDTO.getToOwnerType()) && !DataUtil.isNullOrZero(stockTransDTO.getToOwnerId())
                            && flagStockDTO.isUpdateOwnerId()) {
                        query.setParameter("newOwnerType", newOwnerType);
                        query.setParameter("newOwnerId", newOwnerId);
                    }
                    if (!DataUtil.isNullOrZero(stockTransDetail.getDepositPrice()) && !isUpdateDepositStatus) {
                        query.setParameter("depositPrice", stockTransDetail.getDepositPrice());
                    }
                    if (!DataUtil.isNullOrZero(flagStockDTO.getStateIdForReasonId())) {
                        query.setParameter("stateId", flagStockDTO.getStateIdForReasonId());
                    }
                    if (flagStockDTO.isUpdateProdOfferId()) {
                        query.setParameter("newStockModelId", stockTransDetail.getProdOfferIdChange());
                    }
                    if (flagStockDTO.isUpdateAccountBalance() && !DataUtil.isNullObject(stockTransDTO.getBankplusStatus())) {
                        query.setParameter("bankplusStatus", stockTransDTO.getBankplusStatus());
                    }

                    query.setParameter("stockModelId", stockTransDetail.getStockModelId());
                    if (!flagStockDTO.isUpdateDamageProduct()) {
                        query.setParameter("oldOwnerType", oldOwnerType);
                        query.setParameter("oldOwnerId", oldOwnerId);
                        query.setParameter("oldStatus", flagStockDTO.getOldStatus());
//                    } else {
//                        query.setParameter("oldStatus", flagStockDTO.getOldStatus());
                    }
                    if (flagStockDTO.isUpdateRescue() && !DataUtil.isNullOrZero(flagStockDTO.getStateId())) {
                        if (!Const.REASON_TYPE.RETRIEVE_STOCK.equals(flagStockDTO.getNote())) {
                            query.setParameter("stateId", flagStockDTO.getStateId());
                        } else {
                            query.setParameter("stateId", stockTransDetail.getStrStateIdAfter());
                            query.setParameter("oldOwnerType", "2");
                            query.setParameter("newOwnerType", newOwnerType);
                            if (!DataUtil.isNullOrZero(stockTransDTO.getFromOwnerId()) && !DataUtil.isNullOrZero(stockTransDTO.getToOwnerId())) {
                                query.setParameter("newOwnerId", newOwnerId);
                            }
                        }
                    }

                    if (Const.PRODUCT_OFFER_TYPE.PHONE.equals(stockTransDetail.getStockTypeId())) {
                        query.setParameter("fromSerial", stockTransSerial.getFromSerial());
                        query.setParameter("toSerial", stockTransSerial.getToSerial());
                    } else {
                        query.setParameter("fromSerial", fromSerial);
                        query.setParameter("toSerial", toSerial);
                    }
                    if (!flagStockDTO.isUpdateRescue() && !flagStockDTO.isUpdateDamageProduct()) {
                        query.setParameter("oldStateId", stockTransDetail.getStateId());
                    }
                    count = query.executeUpdate();
                    String[] params = new String[4];
                    if (Const.PRODUCT_OFFER_TYPE.PHONE.equals(stockTransDetail.getStockTypeId())) {
                        if (count != 1) {
                            ProductOfferingDTO productOfferingDTO = productOfferingService.findOne(stockTransDetail.getStockModelId());
                            params[0] = productOfferingDTO.getName();
                            params[1] = productOfferingService.getStockStateName(stockTransDetail.getStateId());
                            params[2] = stockTransSerial.getFromSerial();
                            params[3] = stockTransSerial.getToSerial();
                            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans1.validate.quantity.serial", params);
                        }
                    } else {
                        if (count != (toSerial.subtract(fromSerial).intValue() + 1)) {
                            ProductOfferingDTO productOfferingDTO = productOfferingService.findOne(stockTransDetail.getStockModelId());
                            params[0] = productOfferingDTO.getName();
                            params[1] = productOfferingService.getStockStateName(stockTransDetail.getStateId());
                            params[2] = stockTransSerial.getFromSerial();
                            params[3] = stockTransSerial.getToSerial();
                            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans1.validate.quantity.serial", params);
                        }
                    }
                    total += count;
                }

                if (stockTransDetail.getQuantityReal() != total) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans1.validate.quantity.stockDetail");
                }
            } else {
                //bao loi ko co serial
                if (DataUtil.safeEqual(stockTransDetail.getCheckSerial(), 1L)) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock1.import.stock.error.no.serial");
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
            detailIm1.setProdOfferIdChange(detailDTO.getProdOfferIdChange());

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

                lstStockTransSerialIm1.add(serialIm1);

            }
        }

        return lstStockTransSerialIm1;
    }

    public void doSaveStockTotalUctt(StockTotalDTO stockTotalDTO, StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO) throws Exception {

        StockTotalAuditIm1DTO stockTotalAuditInsert = new StockTotalAuditIm1DTO();
        stockTotalAuditInsert.setQtyBf(0L);
        stockTotalAuditInsert.setQtyIssueBf(0L);

        StockTotalIm1DTO stockTotalIm1DTO = new StockTotalIm1DTO();
        stockTotalIm1DTO.setOwnerId(stockTotalDTO.getOwnerId());
        stockTotalIm1DTO.setOwnerType(stockTotalDTO.getOwnerType());
        stockTotalIm1DTO.setStockModelId(stockTotalDTO.getProdOfferId());
        stockTotalIm1DTO.setStateId(stockTotalDTO.getStateId());
        stockTotalIm1DTO.setModifiedDate(stockTotalDTO.getModifiedDate());
        List<StockTotalIm1DTO> listStockTotal = stockTotalIm1Service.findStockTotal(stockTotalIm1DTO);

        StockTotalIm1DTO dtoStockTotal;
        if (!DataUtil.isNullOrEmpty(listStockTotal)) {
            // update
            dtoStockTotal = listStockTotal.get(0);
            stockTotalIm1DTO = DataUtil.cloneBean(dtoStockTotal);

            //Cap nhat stock_total_audit
            stockTotalAuditInsert.setQtyBf(DataUtil.safeToLong(stockTotalIm1DTO.getQuantity()));
            stockTotalAuditInsert.setQtyIssueBf(DataUtil.safeToLong(stockTotalIm1DTO.getQuantityIssue()));

            //Cap nhat stock_total
            stockTotalIm1DTO.setModifiedDate(stockTotalDTO.getModifiedDate());
            stockTotalIm1DTO.setQuantityIssue(DataUtil.safeToLong(stockTotalIm1DTO.getQuantityIssue()) + 1L);
            stockTotalIm1DTO.setQuantity(DataUtil.safeToLong(stockTotalIm1DTO.getQuantity()) + 1L);
            stockTotalIm1DTO.setQuantityHang(DataUtil.safeToLong(stockTotalIm1DTO.getQuantityHang()) + 1L);
            stockTotalIm1Service.update(stockTotalIm1DTO);

        } else {
            //Them moi
            stockTotalIm1DTO.setQuantityIssue(1L);
            stockTotalIm1DTO.setQuantity(1L);
            stockTotalIm1DTO.setQuantityHang(1L);
            stockTotalIm1DTO.setStatus(DataUtil.safeToLong(Const.STATUS.ACTIVE));
            stockTotalIm1Service.create(stockTotalIm1DTO);
        }

        // Luu stock_total_audit
        stockTotalAuditInsert.setUserId(stockTransActionDTO.getActionStaffId());
        stockTotalAuditInsert.setCreateDate(stockTotalDTO.getModifiedDate());
        stockTotalAuditInsert.setReasonId(stockTransDTO.getReasonId());
        stockTotalAuditInsert.setTransId(stockTransDTO.getStockTransId());
        stockTotalAuditInsert.setTransCode(stockTransActionDTO.getActionCode());
        stockTotalAuditInsert.setTransType(DataUtil.safeToLong(stockTransDTO.getStockTransType()));
        stockTotalAuditInsert.setOwnerId(stockTotalDTO.getOwnerId());
        stockTotalAuditInsert.setOwnerType(stockTotalDTO.getOwnerType());
        stockTotalAuditInsert.setStockModelId(stockTotalDTO.getProdOfferId());
        stockTotalAuditInsert.setStateId(stockTotalDTO.getStateId());

        stockTotalAuditInsert.setQty(1L);
        stockTotalAuditInsert.setQtyAf(stockTotalIm1DTO.getQuantity());

        stockTotalAuditInsert.setQtyIssue(1L);
        stockTotalAuditInsert.setQtyIssueAf(stockTotalIm1DTO.getQuantityIssue());
        doSaveStockTotalAudit(stockTotalAuditInsert);

    }

    public boolean updateSerialForRevoke(
            Long prodOfferTypeId,
            Long stockModelId,
            String serial,
            Long ownerId,
            Long stateId,
            Long oldOwnerType,
            Long oldOwnerId,
            Long revokeType) throws LogicException, Exception {
        try {

            StringBuilder strQuery = new StringBuilder();
            String tableName = IMServiceUtil.getTableNameByOfferType(prodOfferTypeId);

            if (DataUtil.isNullOrEmpty(tableName)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "im.tableName.not.exists.bccs1");
            }

            strQuery.append("UPDATE  bccs_im.");
            strQuery.append(tableName);
            strQuery.append("   SET   state_id = ?, ");//--hang thu hoi
            strQuery.append("         status = ?, "); //trong kho
            strQuery.append("         owner_id = ?, ");//--posId_id
            strQuery.append("         owner_type = ?, ");//--old_owner_type
            strQuery.append("         create_date = sysdate ");

            if (DataUtil.safeEqual(prodOfferTypeId, Const.STOCK_TYPE.STOCK_HANDSET)) {
                strQuery.append(" WHERE   serial = ? ");
            } else {
                strQuery.append(" WHERE   TO_NUMBER (serial) = ? ");
            }

            strQuery.append(" and stock_model_id = ? ");
            strQuery.append(" and owner_type = ?  ");
            strQuery.append(" and owner_id = ?  ");

            Query query = em.createNativeQuery(strQuery.toString());

            query.setParameter(1, stateId); //trang thai sau khi thu hoi (thu hoi sim: hang hong, thu hoi to doi: hang cu)
            query.setParameter(2, Const.NEW); //trong kho
            query.setParameter(3, ownerId);

            if (revokeType.compareTo(1L) == 0) {
                //Neu to doi thu hoi
                query.setParameter(4, Const.OWNER_TYPE.SHOP_LONG);
            } else {
                //Neu nhan vien thu hoi
                query.setParameter(4, Const.OWNER_TYPE.STAFF_LONG);
            }

            query.setParameter(5, serial);
            query.setParameter(6, stockModelId);
            query.setParameter(7, oldOwnerType);
            query.setParameter(8, oldOwnerId);

            int numberRowUpdated = query.executeUpdate();
            if (numberRowUpdated != 1) {
                return false;
            }

            return true;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            return false;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void doIncreaseStockNum(StockTransActionDTO stockTransActionDTO, String prefixActionCode, RequiredRoleMap requiredRoleMap) throws Exception {

        if (DataUtil.isNullOrEmpty(prefixActionCode)) {
            return;
        }

        StaffIm1DTO staff = staffIm1Service.findOne(stockTransActionDTO.getActionStaffId());
        if (staff != null) {
            // Bo sung action_code_shop theo nhan vien neu ko co quyen ROLE_STOCK_NUM_SHOP 09/03/2016
            if (Const.STOCK_TRANS.TRANS_CODE_PX.equals(prefixActionCode)) {
                staff.setStockNum(DataUtil.safeToLong(staff.getStockNum()) + 1);
            } else if (Const.STOCK_TRANS.TRANS_CODE_PN.equals(prefixActionCode)
                    || Const.STOCK_TRANS.TRANS_CODE_LN.equals(prefixActionCode)) {
                staff.setStockNumImp(DataUtil.safeToLong(staff.getStockNumImp()) + 1);
            }
            staffIm1Service.save(staff);

            if (requiredRoleMap != null && requiredRoleMap.hasRole(Const.PERMISION.ROLE_STOCK_NUM_SHOP)) {
                updateStockNumShop(staff.getShopId(), prefixActionCode, stockTransActionDTO.getStockTransActionId());
            }
        } else {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "crt.err.staff.not.found.im1");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateStockNumShop(Long shopId, String prefixActionCode, Long stockTransActionId) throws Exception {
        ShopIm1DTO shopDTO = shopIm1Service.findOne(shopId);
        StockTransActionDTO stockTransActionDTO = stockTransActionService.findOne(stockTransActionId);
        if (shopDTO != null) {
            //check action Type
            Long num = 0L;
            String actionCodeShop = "";
            //Thuc hien lock ban ghi stockTrans
            //em.refresh(shopMapper.toPersistenceBean(shopDTO), LockModeType.PESSIMISTIC_WRITE);
            if (Const.STOCK_TRANS.TRANS_CODE_PX.equals(prefixActionCode)) {
                num = shopDTO.getStockNum() != null ? shopDTO.getStockNum() : num;
                actionCodeShop = Const.STOCK_TRANS.TRANS_CODE_PX + DataUtil.customFormat("000000", num + 1);
                shopDTO.setStockNum(num + 1);
            } else if (Const.STOCK_TRANS.TRANS_CODE_PN.equals(prefixActionCode)) {
                num = shopDTO.getStockNumImp() != null ? shopDTO.getStockNumImp() : num;
                actionCodeShop = prefixActionCode + DataUtil.customFormat("000000", num + 1);
                shopDTO.setStockNumImp(num + 1);
            }
            if (stockTransActionDTO != null) {
                stockTransActionDTO.setActionCodeShop(actionCodeShop);
                stockTransActionService.save(stockTransActionDTO);
            }
            shopIm1Service.save(shopDTO);
        }
    }

}
