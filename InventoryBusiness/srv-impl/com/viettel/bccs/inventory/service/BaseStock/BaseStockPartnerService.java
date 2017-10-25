package com.viettel.bccs.inventory.service.BaseStock;

import com.google.common.collect.Lists;
import com.viettel.bccs.im1.dto.StaffIm1DTO;
import com.viettel.bccs.im1.service.BaseStockIm1Service;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.model.StockTransDetail;
import com.viettel.bccs.inventory.service.OptionSetValueService;
import com.viettel.bccs.inventory.service.StaffService;
import com.viettel.bccs.inventory.service.StockTransDetailService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.RequiredRoleMap;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.service.BaseServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by hoangnt14 on 5/12/2016.
 */
public class BaseStockPartnerService extends BaseStockService {
    public static final Logger logger = Logger.getLogger(BaseStockPartnerService.class);
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM_LIB)
    private EntityManager emLib;

    @Autowired
    private StockTransDetailService stockTransDetailService;

    public void insertStockTrans(StockTransDTO stockTransDTO, Connection conn) throws LogicException, Exception {
        PreparedStatement insertStock = null;
        try {
            StringBuilder strInsert = new StringBuilder();
            strInsert.append(" INSERT INTO STOCK_TRANS (STOCK_TRANS_ID, FROM_OWNER_ID, FROM_OWNER_TYPE, TO_OWNER_ID, TO_OWNER_TYPE, STOCK_TRANS_TYPE, STATUS, CREATE_DATETIME, REASON_ID, NOTE ");
            if (DataUtil.safeEqual(Const.STOCK_TRANS.IS_NOT_ERP, stockTransDTO.getCheckErp())) {
                strInsert.append(", CHECK_ERP");
            }
            strInsert.append(" ) VALUES ( ");
            strInsert.append(" ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ");
            if (DataUtil.safeEqual(Const.STOCK_TRANS.IS_NOT_ERP, stockTransDTO.getCheckErp())) {
                strInsert.append(", ? ");
            }
            strInsert.append(" ) ");
            insertStock = conn.prepareStatement(strInsert.toString());
            insertStock.setLong(1, stockTransDTO.getStockTransId());
            insertStock.setLong(2, stockTransDTO.getFromOwnerId());
            insertStock.setLong(3, stockTransDTO.getFromOwnerType());
            insertStock.setLong(4, stockTransDTO.getToOwnerId());
            insertStock.setLong(5, stockTransDTO.getToOwnerType());
            insertStock.setString(6, stockTransDTO.getStockTransType());
            insertStock.setString(7, stockTransDTO.getStatus());
            insertStock.setDate(8, new java.sql.Date(stockTransDTO.getCreateDatetime().getTime()));
            if (stockTransDTO.getReasonId() != null) {
                insertStock.setLong(9, stockTransDTO.getReasonId());
            } else {
                insertStock.setNull(9, Types.INTEGER);
            }
            insertStock.setString(10, stockTransDTO.getNote());
            if (DataUtil.safeEqual(Const.STOCK_TRANS.IS_NOT_ERP, stockTransDTO.getCheckErp())) {
                insertStock.setString(11, stockTransDTO.getCheckErp());
            }

            insertStock.executeUpdate();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw ex;
        } finally {
            if (insertStock != null) {
                insertStock.close();
            }
        }
    }

    public void insertStockTransAction(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, Connection conn) throws LogicException, Exception {
        PreparedStatement insertStock = null;
        try {
            Long actionID = DbUtil.getSequence(em, "STOCK_TRANS_ACTION_SEQ");
            stockTransDTO.setStockTransActionId(actionID);
            StringBuilder strInsert = new StringBuilder();

            strInsert.append(" INSERT INTO STOCK_TRANS_ACTION (STOCK_TRANS_ACTION_ID,STOCK_TRANS_ID,ACTION_CODE,STATUS,CREATE_DATETIME,NOTE,CREATE_USER,ACTION_STAFF_ID ");
            if (Const.SIGN_VOFFICE.equals(stockTransActionDTO.getSignCaStatus())) {
                strInsert.append(",sign_ca_status");
            }
            strInsert.append(" ) ");
            strInsert.append(" VALUES ");
            strInsert.append(" (?,?,?,?,?,?,?,? ");
            if (Const.SIGN_VOFFICE.equals(stockTransActionDTO.getSignCaStatus())) {
                strInsert.append(",?");
            }
            strInsert.append(") ");
            insertStock = conn.prepareStatement(strInsert.toString());
            insertStock.setLong(1, stockTransDTO.getStockTransActionId());
            insertStock.setLong(2, stockTransDTO.getStockTransId());
            insertStock.setString(3, stockTransActionDTO.getActionCode());
            insertStock.setString(4, stockTransActionDTO.getStatus());
            insertStock.setDate(5, new java.sql.Date(stockTransDTO.getCreateDatetime().getTime()));
            insertStock.setString(6, stockTransDTO.getNote());
            insertStock.setString(7, stockTransDTO.getUserCreate());
            insertStock.setLong(8, stockTransDTO.getStaffId());
            if (Const.SIGN_VOFFICE.equals(stockTransActionDTO.getSignCaStatus())) {
                insertStock.setString(9, stockTransActionDTO.getSignCaStatus());
            }
            insertStock.executeUpdate();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.branch.insert.stock.trans.action.err");
        } finally {
            if (insertStock != null) {
                insertStock.close();
            }
        }
    }

    public void insertVoffice(Connection conn, ImportPartnerRequestDTO requestDTO, StockTransDTO stockTransImpVT, String prefixActionCode) throws LogicException, Exception {
        PreparedStatement ps = null;
        try {
            StockTransVofficeDTO stockTransVofficeDTO = requestDTO.getStockTransVofficeDTO();
            StringBuilder builder = new StringBuilder("insert into stock_trans_voffice");
            builder.append("(STOCK_TRANS_VOFFICE_ID, SIGN_USER_LIST, CREATE_USER_ID, CREATE_DATE,");
            builder.append("STATUS, LAST_MODIFY, STOCK_TRANS_ACTION_ID, ACTION_CODE, ");
            builder.append("PREFIX_TEMPLATE, ACCOUNT_NAME, ACCOUNT_PASS, RECEIPT_NO) values(stock_trans_voffice_seq.nextVal, ");
            builder.append("?, ?, sysdate, ?, sysdate, ?, ?, ?, ?, ?, ?)");
            ps = conn.prepareStatement(builder.toString());
            ps.setString(1, stockTransVofficeDTO.getSignUserList());
            ps.setLong(2, requestDTO.getImportStaffId());
            ps.setLong(3, Const.LongSimpleItem.importPartnerRequestStatusCreate.getValue());
            ps.setLong(4, stockTransImpVT.getStockTransActionId());
            ps.setString(5, stockTransImpVT.getActionCode());
            ps.setString(6, DataUtil.isNullOrEmpty(prefixActionCode) ? Const.ConfigStockTrans.PN.toString() : prefixActionCode);
            ps.setString(7, stockTransVofficeDTO.getAccountName());
            ps.setString(8, stockTransVofficeDTO.getAccountPass());
            //check phan quyen receiptNo
            String receiptNo = stockTransImpVT.getActionCode();
            receiptNo = receiptNo.substring(receiptNo.lastIndexOf("_") + 1, receiptNo.length());
            ps.setString(9, receiptNo);
            ps.executeUpdate();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw ex;
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
    }

    public void insertStockTransDetail(StockTransDTO stockTransDTO, List<StockTransDetailDTO> lstStockTransDetailDTO, Connection conn) throws LogicException, Exception {
        PreparedStatement insertStock = null;
        try {
            StringBuilder strInsert = new StringBuilder();
            strInsert.append(" INSERT INTO STOCK_TRANS_DETAIL (STOCK_TRANS_DETAIL_ID,STOCK_TRANS_ID,PROD_OFFER_ID,STATE_ID,QUANTITY,PRICE, AMOUNT, CREATE_DATETIME) ");
            strInsert.append(" VALUES ");
            strInsert.append(" (?,?,?,?,?,?,?,?) ");
            insertStock = conn.prepareStatement(strInsert.toString());
            for (StockTransDetailDTO stockTransDetailDTO : lstStockTransDetailDTO) {
                insertStock.setLong(1, stockTransDetailDTO.getStockTransDetailId());
                insertStock.setLong(2, stockTransDTO.getStockTransId());
                insertStock.setLong(3, stockTransDetailDTO.getProdOfferId());
                insertStock.setLong(4, stockTransDetailDTO.getStateId());
                if (stockTransDetailDTO.getQuantity() != null) {
                    insertStock.setLong(5, stockTransDetailDTO.getQuantity());
                } else {
                    insertStock.setNull(5, java.sql.Types.INTEGER);
                }
                if (stockTransDetailDTO.getPrice() != null) {
                    insertStock.setLong(6, stockTransDetailDTO.getPrice());
                } else {
                    insertStock.setNull(6, java.sql.Types.INTEGER);
                }
                if (stockTransDetailDTO.getAmount() != null) {
                    insertStock.setLong(7, stockTransDetailDTO.getAmount());
                } else {
                    insertStock.setNull(7, java.sql.Types.INTEGER);
                }
                insertStock.setDate(8, new java.sql.Date(stockTransDTO.getCreateDatetime().getTime()));
            }
            insertStock.executeUpdate();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.branch.insert.stock.trans.detail.err");
        } finally {
            if (insertStock != null) {
                insertStock.close();
            }
        }
    }

    public int doSavePartnerContractAndDetail(ImportPartnerRequestDTO importPartnerRequestDTO, Connection conn, StockTransDTO stockTransImpVT) throws LogicException, Exception {

        Long partnerContractId = DbUtil.getSequence(em, "partner_contract_seq");
        importPartnerRequestDTO.setPartnerContractId(partnerContractId);
        int resultSavePartnerContract = doSavePartnerContract(importPartnerRequestDTO, conn, stockTransImpVT);
        if (resultSavePartnerContract <= 0) {
            //Co loi xay ra khi insert bang PARTNER_CONTRACT
            if (conn != null) {
                conn.rollback();
            }
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.branch.insert.partner.contract");
        }
        // Save partner_contract_detail
        PreparedStatement insertPartnerContractDetail = null;
        try {
            StringBuilder strQueryInsertPartnerContract = new StringBuilder("");
            strQueryInsertPartnerContract.append(" INSERT INTO partner_contract_detail(partner_contract_detail_id, partner_contract_id, create_date, ");
            strQueryInsertPartnerContract.append("             prod_offer_id, state_id, quantity, currency_type, unit_price, order_code, start_date_warranty) ");
            strQueryInsertPartnerContract.append("             VALUES (partner_contract_detail_seq.NEXTVAL,?,sysdate,?,?,0,?,?,?,?) ");
            insertPartnerContractDetail = conn.prepareStatement(strQueryInsertPartnerContract.toString());
            insertPartnerContractDetail.setLong(1, partnerContractId);
            insertPartnerContractDetail.setLong(2, importPartnerRequestDTO.getListImportPartnerDetailDTOs().get(0).getProdOfferId());
            insertPartnerContractDetail.setLong(3, importPartnerRequestDTO.getListImportPartnerDetailDTOs().get(0).getStateId());
            insertPartnerContractDetail.setString(4, (importPartnerRequestDTO.getCurrencyType() == null) ? "" : importPartnerRequestDTO.getCurrencyType().trim());
            insertPartnerContractDetail.setLong(5, importPartnerRequestDTO.getUnitPrice());
            insertPartnerContractDetail.setString(6, (importPartnerRequestDTO.getCodePackage() == null) ? "" : importPartnerRequestDTO.getCodePackage().trim());
            insertPartnerContractDetail.setDate(7, (importPartnerRequestDTO.getEndDate() == null) ? null : new java.sql.Date(importPartnerRequestDTO.getEndDate().getTime()));
            return insertPartnerContractDetail.executeUpdate();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.branch.insert.partner.contract");
        } finally {
            if (insertPartnerContractDetail != null) {
                insertPartnerContractDetail.close();
            }
        }
    }

    public int doSavePartnerContract(ImportPartnerRequestDTO importPartnerRequestDTO, Connection conn, StockTransDTO stockTransImpVT) throws LogicException, Exception {
        PreparedStatement insertPartnerContract = null;
        try {
            Long partnerContractId = null;
            if (DataUtil.isNullOrZero(importPartnerRequestDTO.getPartnerContractId())) {
                partnerContractId = DbUtil.getSequence(em, "partner_contract_seq");
            } else {
                partnerContractId = importPartnerRequestDTO.getPartnerContractId();
            }
            StringBuilder strQueryInsertPartnerContract = new StringBuilder("");
            strQueryInsertPartnerContract.append(" INSERT INTO partner_contract(partner_contract_id, stock_trans_id, action_code, create_date, ");
            strQueryInsertPartnerContract.append("             last_modified, contract_code, po_code, contract_date, po_date, import_date, prod_offer_id, ");
            strQueryInsertPartnerContract.append("             delivery_location, request_date, partner_id, currency_type, unit_price, contract_status, currency_rate, kcs_no, kcs_date) ");
            strQueryInsertPartnerContract.append("             VALUES (?,?,?, sysdate, sysdate,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ");
            insertPartnerContract = conn.prepareStatement(strQueryInsertPartnerContract.toString());
            insertPartnerContract.setLong(1, partnerContractId);
            insertPartnerContract.setLong(2, stockTransImpVT.getStockTransId());
            insertPartnerContract.setString(3, importPartnerRequestDTO.getActionCode());
            insertPartnerContract.setString(4, (importPartnerRequestDTO.getContractCode() == null) ? "" : importPartnerRequestDTO.getContractCode().trim().toUpperCase());
            insertPartnerContract.setString(5, (importPartnerRequestDTO.getPoCode() == null) ? "" : importPartnerRequestDTO.getPoCode().trim().toUpperCase());
            insertPartnerContract.setDate(6, (importPartnerRequestDTO.getContractDate() == null) ? null : new java.sql.Date(importPartnerRequestDTO.getContractDate().getTime()));
            insertPartnerContract.setDate(7, (importPartnerRequestDTO.getPoDate() == null) ? null : new java.sql.Date(importPartnerRequestDTO.getPoDate().getTime()));
            insertPartnerContract.setDate(8, (importPartnerRequestDTO.getImportDate() == null) ? null : new java.sql.Date(importPartnerRequestDTO.getImportDate().getTime()));
//            insertPartnerContract.setLong(9, (importPartnerRequestDTO.getListImportPartnerDetailDTOs().get(0) == null) ? null : importPartnerRequestDTO.getListImportPartnerDetailDTOs().get(0).getProdOfferId());
            insertPartnerContract.setLong(9, 0L);
            insertPartnerContract.setString(10, (importPartnerRequestDTO.getDeliveryLocation() == null) ? "" : importPartnerRequestDTO.getDeliveryLocation().trim());
            insertPartnerContract.setDate(11, (importPartnerRequestDTO.getRequestDate() == null) ? null : new java.sql.Date(importPartnerRequestDTO.getRequestDate().getTime()));
            insertPartnerContract.setLong(12, importPartnerRequestDTO.getPartnerId());
            insertPartnerContract.setString(13, (importPartnerRequestDTO.getCurrencyType() == null) ? "" : importPartnerRequestDTO.getCurrencyType());
            if (importPartnerRequestDTO.getUnitPrice() == null) {
                insertPartnerContract.setNull(14, java.sql.Types.INTEGER);
            } else {
                insertPartnerContract.setDouble(14, importPartnerRequestDTO.getUnitPrice());
            }
            if (DataUtil.isNullOrZero(importPartnerRequestDTO.getContractStatus())) {
                insertPartnerContract.setLong(15, 1L); //Da co thong tin hop dong
            } else {
                insertPartnerContract.setLong(15, importPartnerRequestDTO.getContractStatus());
            }
            if (importPartnerRequestDTO.getRate() == null) {
                insertPartnerContract.setNull(16, Types.DOUBLE);
            } else {
                insertPartnerContract.setDouble(16, importPartnerRequestDTO.getRate());
            }
            insertPartnerContract.setString(17, (importPartnerRequestDTO.getCodeKCS() == null) ? "" : importPartnerRequestDTO.getCodeKCS().trim());
            insertPartnerContract.setDate(18, (importPartnerRequestDTO.getDateKCS() == null) ? null : new java.sql.Date(importPartnerRequestDTO.getDateKCS().getTime()));

            return insertPartnerContract.executeUpdate();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.branch.insert.partner.contract");
        } finally {
            if (insertPartnerContract != null) {
                insertPartnerContract.close();
            }
        }
    }


    public int updateStockSim(Long startStockKitId, Connection conn, StockTransDTO stockTransExpSim, List<StockTransDetailDTO> lstStockTransDetailExpSim, StockTransDTO stockTransImpVT) throws LogicException, Exception {
        PreparedStatement updateStock = null;
        try {
            StringBuilder updateStockSim = new StringBuilder();
            updateStockSim.append(" update STOCK_SIM a set status = ?, update_datetime = sysdate, ");
            updateStockSim.append(" hlr_status = ? ,HLR_REG_DATE = sysdate, ");
            updateStockSim.append(" auc_status = ? ,AUC_REG_DATE= sysdate ");
            updateStockSim.append(" where a.owner_type = ? and a.owner_id = ? and a.prod_offer_id = ?");
            updateStockSim.append(" and exists (select 'X' from stock_kit where to_number(serial) = a.serial and id > ? and stock_trans_id = ?) ");
            updateStockSim.append(" and a.status = ? ");
            updateStock = conn.prepareCall(updateStockSim.toString());
            updateStock.setLong(1, Const.STOCK_GOODS.STATUS_SALE);
            updateStock.setString(2, Const.HLR_STATUS_REG);
            updateStock.setString(3, Const.AUC_STATUS_REG);
            updateStock.setLong(4, stockTransExpSim.getFromOwnerType());
            updateStock.setLong(5, stockTransExpSim.getFromOwnerId());
            updateStock.setLong(6, lstStockTransDetailExpSim.get(0).getProdOfferId());
            updateStock.setLong(7, startStockKitId);
            updateStock.setLong(8, stockTransImpVT.getStockTransId());
            updateStock.setString(9, Const.STATUS_ACTIVE);
            return updateStock.executeUpdate();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.branch.update.stock.sim.err");
        } finally {
            if (updateStock != null) {
                updateStock.close();
            }
        }
    }

    private StockTotalDTO findStockTotal(StockTotalDTO stockTotalDTO, Connection conn) throws LogicException, Exception {
        StockTotalDTO stockTotal = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            StringBuilder strQuery = new StringBuilder(" ");
            strQuery.append(" SELECT * FROM STOCK_TOTAL WHERE ");
            strQuery.append(" OWNER_ID = ? AND OWNER_TYPE = ? AND PROD_OFFER_ID  = ? AND STATE_ID = ?  ");
            strQuery.append(" FOR UPDATE NOWAIT ");
            stm = conn.prepareStatement(strQuery.toString());
            stm.setLong(1, stockTotalDTO.getOwnerId());
            stm.setLong(2, stockTotalDTO.getOwnerType());
            stm.setLong(3, stockTotalDTO.getProdOfferId());
            stm.setLong(4, stockTotalDTO.getStateId());

            rs = stm.executeQuery();
            if (rs.next()) {
                stockTotal = new StockTotalDTO();
                stockTotal.setStockTotalId(rs.getLong("stock_total_id"));
                stockTotal.setProdOfferId(stockTotalDTO.getProdOfferId());
                stockTotal.setOwnerId(stockTotalDTO.getOwnerId());
                stockTotal.setOwnerType(stockTotalDTO.getOwnerType());
                stockTotal.setStateId(stockTotalDTO.getStateId());
                stockTotal.setCurrentQuantity(rs.getLong("current_quantity"));
                stockTotal.setAvailableQuantity(rs.getLong("available_quantity"));
                stockTotal.setHangQuantity(rs.getLong("hang_quantity"));
                stockTotal.setStatus(rs.getLong("status"));
                stockTotal.setAvailableQuantity(rs.getLong("available_quantity"));
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.branch.search.stock.total.err");
        } finally {
            if (stm != null) {
                stm.close();
            }
            if (rs != null) {
                rs.close();
            }
        }

        return stockTotal;
    }

    public void changeStockTotal(StockTotalDTO stockTotalDTO, StockTotalAuditDTO stockTotalAuditDTO, Connection conn) throws LogicException, Exception {

        StockTotalDTO stockTotal = findStockTotal(stockTotalDTO, conn);
        PreparedStatement insertStock = null;
        try {
            if (DataUtil.isNullObject(stockTotal) || DataUtil.isNullOrZero(stockTotal.getStockTotalId())) {
                //insert
                StringBuilder strQuery = new StringBuilder();
                strQuery.append(" INSERT INTO STOCK_TOTAL( ");
                strQuery.append(" STOCK_TOTAL_ID,PROD_OFFER_ID,OWNER_ID,OWNER_TYPE,STATE_ID,CURRENT_QUANTITY,AVAILABLE_QUANTITY,HANG_QUANTITY,MODIFIED_DATE,STATUS ");
                strQuery.append(" ) VALUES (");
                strQuery.append(" STOCK_TOTAL_SEQ.NEXTVAL, ?,?,?,?,?,?,?,sysdate,?");
                strQuery.append(" ) ");
                insertStock = conn.prepareStatement(strQuery.toString());
                insertStock.setLong(1, stockTotalDTO.getProdOfferId());
                insertStock.setLong(2, stockTotalDTO.getOwnerId());
                insertStock.setLong(3, stockTotalDTO.getOwnerType());
                insertStock.setLong(4, stockTotalDTO.getStateId());
                insertStock.setLong(5, stockTotalDTO.getCurrentQuantity());
                insertStock.setLong(6, stockTotalDTO.getAvailableQuantity());
                insertStock.setLong(7, stockTotalDTO.getHangQuantity());
                insertStock.setLong(8, Long.valueOf(Const.STATUS_ACTIVE));
                insertStock.executeUpdate();
                //insert stock_total_audit
                StockTotalAuditDTO stockTotalAuditInsert = DataUtil.cloneBean(stockTotalAuditDTO);
                stockTotalAuditInsert.setOwnerId(stockTotalDTO.getOwnerId());
                stockTotalAuditInsert.setOwnerType(stockTotalDTO.getOwnerType());
                stockTotalAuditInsert.setProdOfferId(stockTotalDTO.getProdOfferId());
                stockTotalAuditInsert.setStateId(stockTotalDTO.getStateId());
                stockTotalAuditInsert.setCurrentQuantityBf(0L);
                stockTotalAuditInsert.setCurrentQuantity(DataUtil.safeToLong(stockTotalDTO.getCurrentQuantity()));
                stockTotalAuditInsert.setCurrentQuantityAf(stockTotalDTO.getCurrentQuantity());
                // available quantity
                stockTotalAuditInsert.setAvailableQuantityBf(0L);
                stockTotalAuditInsert.setAvailableQuantity(DataUtil.safeToLong(stockTotalDTO.getAvailableQuantity()));
                stockTotalAuditInsert.setAvailableQuantityAf(stockTotalDTO.getAvailableQuantity());
                insertStockTotalAudit(stockTotalAuditInsert, conn);
            } else {
                //update
                StringBuilder strQuery = new StringBuilder();
                strQuery.append(" UPDATE STOCK_TOTAL ");
                strQuery.append(" SET CURRENT_QUANTITY = ? ,");
                strQuery.append(" AVAILABLE_QUANTITY = ? ,");
                strQuery.append(" MODIFIED_DATE = sysdate ");
                strQuery.append(" WHERE OWNER_ID = ? AND OWNER_TYPE = ? AND STATE_ID = ? AND PROD_OFFER_ID = ? ");
                insertStock = conn.prepareStatement(strQuery.toString());
                insertStock.setLong(1, stockTotalDTO.getCurrentQuantity() + stockTotal.getCurrentQuantity());
                insertStock.setLong(2, stockTotalDTO.getAvailableQuantity() + stockTotal.getAvailableQuantity());
                insertStock.setLong(3, stockTotalDTO.getOwnerId());
                insertStock.setLong(4, stockTotalDTO.getOwnerType());
                insertStock.setLong(5, stockTotalDTO.getStateId());
                insertStock.setLong(6, stockTotalDTO.getProdOfferId());
                insertStock.executeUpdate();
                //insert stock_total_audit
                StockTotalAuditDTO stockTotalAuditInsert = DataUtil.cloneBean(stockTotalAuditDTO);
                stockTotalAuditInsert.setOwnerId(stockTotalDTO.getOwnerId());
                stockTotalAuditInsert.setOwnerType(stockTotalDTO.getOwnerType());
                stockTotalAuditInsert.setProdOfferId(stockTotalDTO.getProdOfferId());
                stockTotalAuditInsert.setStateId(stockTotalDTO.getStateId());
                stockTotalAuditInsert.setCurrentQuantityBf(DataUtil.safeToLong(stockTotal.getCurrentQuantity()));
                stockTotalAuditInsert.setCurrentQuantity(DataUtil.safeToLong(stockTotalDTO.getCurrentQuantity()));
                stockTotalAuditInsert.setCurrentQuantityAf(DataUtil.safeToLong(stockTotal.getCurrentQuantity()) + stockTotalDTO.getCurrentQuantity());
                // available quantity
                stockTotalAuditInsert.setAvailableQuantityBf(DataUtil.safeToLong(stockTotal.getAvailableQuantity()));
                stockTotalAuditInsert.setAvailableQuantity(DataUtil.safeToLong(stockTotalDTO.getAvailableQuantity()));
                stockTotalAuditInsert.setAvailableQuantityAf(DataUtil.safeToLong(stockTotal.getAvailableQuantity()) + stockTotalDTO.getAvailableQuantity());
                insertStockTotalAudit(stockTotalAuditInsert, conn);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.branch.update.stock.total.err");
        } finally {
            if (insertStock != null) {
                insertStock.close();
            }
        }
    }

    private void insertStockTotalAudit(StockTotalAuditDTO stockTotalAuditDTO, Connection conn) throws LogicException, Exception {
        StringBuilder strQuery = new StringBuilder();
        PreparedStatement insertStock = null;
        try {
            strQuery.append(" INSERT INTO STOCK_TOTAL_AUDIT ( ");
            strQuery.append(" ID,OWNER_ID,OWNER_TYPE,PROD_OFFER_ID,STATE_ID,CURRENT_QUANTITY_BF,CURRENT_QUANTITY,CURRENT_QUANTITY_AF, ");
            strQuery.append(" AVAILABLE_QUANTITY_BF,AVAILABLE_QUANTITY,AVAILABLE_QUANTITY_AF,USER_ID,CREATE_DATE,REASON_ID,TRANS_ID,TRANS_CODE,TRANS_TYPE,SOURCE_TYPE ");
            strQuery.append(" ) VALUES (");
            strQuery.append(" STOCK_TOTAL_AUDIT_SEQ.NEXTVAL,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?");
            strQuery.append(" ) ");
            insertStock = conn.prepareStatement(strQuery.toString());
            insertStock.setLong(1, stockTotalAuditDTO.getOwnerId());
            insertStock.setLong(2, stockTotalAuditDTO.getOwnerType());
            insertStock.setLong(3, stockTotalAuditDTO.getProdOfferId());
            insertStock.setLong(4, stockTotalAuditDTO.getStateId());
            insertStock.setLong(5, stockTotalAuditDTO.getCurrentQuantityBf());
            insertStock.setLong(6, stockTotalAuditDTO.getCurrentQuantity());
            insertStock.setLong(7, stockTotalAuditDTO.getCurrentQuantityAf());
            insertStock.setLong(8, stockTotalAuditDTO.getAvailableQuantityBf());
            insertStock.setLong(9, stockTotalAuditDTO.getAvailableQuantity());
            insertStock.setLong(10, stockTotalAuditDTO.getAvailableQuantityAf());
            insertStock.setLong(11, stockTotalAuditDTO.getUserId());
            insertStock.setDate(12, new java.sql.Date(stockTotalAuditDTO.getCreateDate().getTime()));
            if (stockTotalAuditDTO.getReasonId() != null) {
                insertStock.setLong(13, stockTotalAuditDTO.getReasonId());
            } else {
                insertStock.setNull(13, Types.INTEGER);
            }
            insertStock.setLong(14, stockTotalAuditDTO.getTransId());
            insertStock.setString(15, stockTotalAuditDTO.getTransCode());
            insertStock.setLong(16, stockTotalAuditDTO.getTransType());
            insertStock.setLong(17, stockTotalAuditDTO.getSourceType());
            insertStock.executeUpdate();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new Exception();
        } finally {
            if (insertStock != null) {
                insertStock.close();
            }
        }
    }

    public void updateStockNumber(Long startStockKitId, Connection conn, StockTransDTO stockTransImpVT) throws LogicException, Exception {
        //update thong tin bang STOCK_NUMBER
        PreparedStatement updateStock = null;
        try {
            StringBuilder updateStockNumber = new StringBuilder();
            updateStockNumber.append(" update STOCK_NUMBER a set a.status = ?, last_update_time= sysdate, last_update_user = ? ");
            updateStockNumber.append(" where exists (select 'X' from stock_kit where TO_NUMBER(isdn) = a.isdn and id > ? and stock_trans_id = ?) ");
            updateStockNumber.append(" and status in (?,?) ");
            updateStock = conn.prepareStatement(updateStockNumber.toString());
            updateStock.setString(1, Const.ISDN.USING);
            updateStock.setString(2, stockTransImpVT.getUserCreate());
            updateStock.setLong(3, startStockKitId);
            updateStock.setLong(4, stockTransImpVT.getStockTransId());
            updateStock.setString(5, Const.ISDN.NEW_NUMBER);
            updateStock.setString(6, Const.ISDN.STOP_USE);
            updateStock.executeUpdate();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.branch.update.stock.number.err");
        } finally {
            if (updateStock != null) {
                updateStock.close();
            }
        }
    }

    public StockTotalDTO initStockTotal(Long resultUpdateStockSim, StockTransDTO stockTransDTO, List<StockTransDetailDTO> lstStockTransDetail, FlagStockDTO flagStockDTO) {
        StockTotalDTO stockTotal = new StockTotalDTO();
        if (flagStockDTO.isExportStock()) {
            stockTotal.setOwnerId(stockTransDTO.getFromOwnerId());
            stockTotal.setOwnerType(stockTransDTO.getFromOwnerType());
            stockTotal.setAvailableQuantity(flagStockDTO.getExpAvailableQuantity() * resultUpdateStockSim);
            stockTotal.setCurrentQuantity(flagStockDTO.getExpCurrentQuantity() * resultUpdateStockSim);
        } else {
            stockTotal.setOwnerId(stockTransDTO.getToOwnerId());
            stockTotal.setOwnerType(stockTransDTO.getToOwnerType());
            stockTotal.setAvailableQuantity(flagStockDTO.getImpAvailableQuantity() * resultUpdateStockSim);
            stockTotal.setCurrentQuantity(flagStockDTO.getImpCurrentQuantity() * resultUpdateStockSim);
        }

        stockTotal.setProdOfferId(lstStockTransDetail.get(0).getProdOfferId());
        stockTotal.setStateId(lstStockTransDetail.get(0).getStateId());
        stockTotal.setHangQuantity(0L);
        stockTotal.setModifiedDate(stockTransDTO.getCreateDatetime());
        stockTotal.setStatus(DataUtil.safeToLong(Const.STATUS.ACTIVE));
        return stockTotal;
    }

    public StockTotalAuditDTO initStockTotalAudit(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO) {
        StockTotalAuditDTO stockTotalAuditDTO = new StockTotalAuditDTO();
        stockTotalAuditDTO.setUserId(stockTransActionDTO.getActionStaffId());
        stockTotalAuditDTO.setCreateDate(stockTransActionDTO.getCreateDatetime());
        stockTotalAuditDTO.setReasonId(stockTransDTO.getReasonId());
        stockTotalAuditDTO.setTransId(stockTransDTO.getStockTransId());
        stockTotalAuditDTO.setTransCode(stockTransActionDTO.getActionCode());
        stockTotalAuditDTO.setTransType(DataUtil.safeToLong(stockTransDTO.getStockTransType()));
        stockTotalAuditDTO.setSourceType(Const.STOCK_TOTAL_AUDIT.SOURCE_TYPE_STOCK_TRANS);
        return stockTotalAuditDTO;
    }

    public int countNumberError(String tableName, Connection conn, Long stockTransId) throws LogicException, Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            StringBuffer strQuery = new StringBuffer("");
            strQuery.append("select count(*) number_err_record from ERR$_").append(tableName).append(" a ");
            strQuery.append("where a.stock_trans_id = ? ");

            ps = conn.prepareStatement(strQuery.toString()); // create a statement
            ps.setLong(1, stockTransId); // set input parameter
            rs = ps.executeQuery();
            int numberErrRecord = 0;
            // extract data from the ResultSet
            while (rs.next()) {
                numberErrRecord = rs.getInt("number_err_record");
            }
            return numberErrRecord;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw ex;
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
    }

    public int countNumberErrorForUpdateSim(Connection conn, String sessionId) throws LogicException, Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            StringBuffer strQuery = new StringBuffer("");
            strQuery.append("select count(*) number_err_record from ERR$_STOCK_SIM").append(" a ");
            strQuery.append("where a.ora_err_tag$ = ? ");

            ps = conn.prepareStatement(strQuery.toString()); // create a statement
            ps.setString(1, sessionId); // set input parameter
            rs = ps.executeQuery();
            int numberErrRecord = 0;
            // extract data from the ResultSet
            while (rs.next()) {
                numberErrRecord = rs.getInt("number_err_record");
            }
            return numberErrRecord;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw ex;
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
    }

    public String formatSerial(String serial, boolean isCard) {
        if (DataUtil.isNullObject(serial)) {
            return serial;
        }
        String newSerial = serial.trim();
        if (isCard) {
            while (newSerial.length() < 11) {
                newSerial = "0" + newSerial;
            }
        }
        return newSerial;
    }

    public void doSaveStockTotal(StockTransDTO stockTransDTO, List<StockTransDetailDTO> lstStockTransDetailDTO,
                                 FlagStockDTO flagStockDTO, StockTransActionDTO stockTransActionDTO, Connection conn) throws Exception {
        if (DataUtil.safeEqual(Const.PROCESS_OFFLINE, stockTransDTO.getProcessOffline())) {
            return;
        }
        if (!flagStockDTO.isExportStock() && !flagStockDTO.isImportStock()) {
            return;
        }

        //Neu la giao dich xuat dieu chuyen --> chi cong so luong thuc te, va cap nhap serial ve =3
        if (!DataUtil.isNullOrZero(stockTransDTO.getExchangeStockId())) {
            flagStockDTO.setImpAvailableQuantity(0L);
        }

        List<StockTotalDTO> lstExpStockTotalDTO = new ArrayList<>();
        List<StockTotalDTO> lstImpStockTotalDTO = new ArrayList<>();

        List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList(lstStockTransDetailDTO);


        if (DataUtil.isNullOrEmpty(lstStockTransDetail)) {

            lstStockTransDetail = stockTransDetailService.findByFilter(Lists.newArrayList(
                    new FilterRequest(StockTransDetail.COLUMNS.STOCKTRANSID.name(), FilterRequest.Operator.EQ,
                            stockTransDTO.getStockTransId())));
        }

        //Cap nhat kho xuat
        Long fromOwnerId = stockTransDTO.getFromOwnerId();
        Long fromOwnerType = stockTransDTO.getFromOwnerType();
        if (stockTransDTO.getRegionStockId() != null) {
            fromOwnerId = stockTransDTO.getRegionStockId();
        }

        //Cap nhat kho nhan
        Long toOwnerId = stockTransDTO.getToOwnerId();
        Long toOwnerType = stockTransDTO.getToOwnerType();
        if (stockTransDTO.getRegionStockId() != null) {
            toOwnerId = stockTransDTO.getRegionStockId();
        }

        //init stock_total
        for (StockTransDetailDTO stockTransDetail : lstStockTransDetail) {

            StockTotalDTO stockTotal;
            if (flagStockDTO.isExportStock()) {
                stockTotal = new StockTotalDTO();
                stockTotal.setOwnerId(fromOwnerId);
                stockTotal.setOwnerType(fromOwnerType);
                stockTotal.setProdOfferId(stockTransDetail.getProdOfferId());
                stockTotal.setStateId(stockTransDetail.getStateId());
                stockTotal.setAvailableQuantity(flagStockDTO.getExpAvailableQuantity() * stockTransDetail.getQuantity());
                stockTotal.setCurrentQuantity(flagStockDTO.getExpCurrentQuantity() * stockTransDetail.getQuantity());
                stockTotal.setHangQuantity(flagStockDTO.getExpHangQuantity() * stockTransDetail.getQuantity());
                stockTotal.setStatus(DataUtil.safeToLong(Const.STATUS.ACTIVE));
                stockTotal.setModifiedDate(stockTransDTO.getCreateDatetime());

                lstExpStockTotalDTO.add(stockTotal);
            }

            if (flagStockDTO.isImportStock()) {
                stockTotal = new StockTotalDTO();
                stockTotal.setOwnerId(toOwnerId);
                stockTotal.setOwnerType(toOwnerType);
                stockTotal.setProdOfferId(stockTransDetail.getProdOfferId());
                stockTotal.setStateId(stockTransDetail.getStateId());
                stockTotal.setAvailableQuantity(flagStockDTO.getImpAvailableQuantity() * stockTransDetail.getQuantity());
                stockTotal.setCurrentQuantity(flagStockDTO.getImpCurrentQuantity() * stockTransDetail.getQuantity());
                stockTotal.setHangQuantity(flagStockDTO.getImpHangQuantity() * stockTransDetail.getQuantity());
                stockTotal.setModifiedDate(stockTransDTO.getCreateDatetime());
                stockTotal.setStatus(DataUtil.safeToLong(Const.STATUS.ACTIVE));
                lstImpStockTotalDTO.add(stockTotal);
            }
        }
        // init stock_total_audit
        StockTotalAuditDTO stockTotalAuditDTO = null;
        if (flagStockDTO.isInsertStockTotalAudit()) {
            stockTotalAuditDTO = new StockTotalAuditDTO();
            stockTotalAuditDTO.setUserId(stockTransActionDTO.getActionStaffId());
            stockTotalAuditDTO.setCreateDate(stockTransDTO.getCreateDatetime());
            stockTotalAuditDTO.setReasonId(stockTransDTO.getReasonId());
            stockTotalAuditDTO.setTransId(stockTransDTO.getStockTransId());
            stockTotalAuditDTO.setTransCode(stockTransActionDTO.getActionCode());
            stockTotalAuditDTO.setTransType(DataUtil.safeToLong(stockTransDTO.getStockTransType()));
            stockTotalAuditDTO.setSourceType(Const.STOCK_TOTAL_AUDIT.SOURCE_TYPE_STOCK_TRANS);
        }

        //Save stock_total
        for (StockTotalDTO stockTotalDTO : lstImpStockTotalDTO) {
            changeStockTotal(stockTotalDTO, stockTotalAuditDTO, conn);
        }
    }

    /**
     * ham xu ly update stock_x
     * @author thanhnt77
     * @param tableName
     * @param ownerType
     * @param ownerId
     * @param oldStatus
     * @param newStatus
     * @param stateId
     * @param prodOfferId
     * @param fromSerial
     * @param toSerial
     * @return
     */
    public int updateStockX(String tableName, Long ownerType, Long ownerId, Long oldStatus, Long newStatus, Long stateId,
                            Long prodOfferId, String fromSerial, String toSerial, Long stockTransId, boolean isExactSerial, boolean isWriteLog) throws Exception{
        StringBuffer strUpdate = new StringBuffer();
        strUpdate.append(" update ");
        strUpdate.append(tableName);
        strUpdate.append("  set owner_type = #ownerType, owner_id = #ownerId, status = #newStatus, state_id = #stateId, stock_trans_id=#stockTransId ");
        strUpdate.append(" where prod_offer_id = #prodOfferId and status = #oldStatus ");
        if (!Const.STOCK_TYPE.STOCK_HANDSET_NAME.equals(tableName)) {
            if (isExactSerial) {
                strUpdate.append(" and to_number(serial) = to_number(#fromSerial) ");
            } else {
                strUpdate.append(" and to_number(serial) >= to_number(#fromSerial) and to_number(serial) <= to_number(#toSerial) ");
            }
        } else {
            if (isExactSerial) {
                strUpdate.append(" and serial = #fromSerial ");
            } else {
                strUpdate.append(" and serial >= #fromSerial and serial <= #toSerial ");
            }
        }
        if (isWriteLog) {
            strUpdate.append(" LOG ERRORS INTO ERR$_" + tableName + " ('INSERT') REJECT LIMIT UNLIMITED ");
        }
        Query query = em.createNativeQuery(strUpdate.toString());
        query.setParameter("ownerType",ownerType);
        query.setParameter("ownerId",ownerId);
        query.setParameter("newStatus",newStatus);
        query.setParameter("stateId",stateId);
        query.setParameter("prodOfferId",prodOfferId);
        query.setParameter("oldStatus",oldStatus);
        query.setParameter("fromSerial",fromSerial);
        if (!isExactSerial) {
            query.setParameter("toSerial",toSerial);
        }
        query.setParameter("stockTransId", stockTransId);
        return query.executeUpdate();
    }

    /**
     * ham xu ly update stock_x IM1
     * @author thanhnt77
     * @param tableName
     * @param ownerType
     * @param ownerId
     * @param oldStatus
     * @param newStatus
     * @param stateId
     * @param stockModelId
     * @param fromSerial
     * @param toSerial
     * @return
     */
    public int updateStockXIM1(String tableName, Long ownerType, Long ownerId, Long oldStatus, Long newStatus,
                               Long stateId, Long stockModelId, String fromSerial, String toSerial, Long stockTransId, boolean isExactSerial, boolean isWriteLog) throws Exception{
        StringBuffer strUpdate = new StringBuffer();
        strUpdate.append(" update bccs_im.");
        strUpdate.append(tableName);
        strUpdate.append("  set owner_type = #ownerType, owner_id = #ownerId, status = #newStatus, state_id = #stateId, stock_trans_id=#stockTransId ");
        strUpdate.append(" where stock_model_id = #stockModelId and status = #oldStatus ");
        if (!Const.STOCK_TYPE.STOCK_HANDSET_NAME.equals(tableName)) {
            if (isExactSerial) {
                strUpdate.append(" and to_number(serial) = to_number(#fromSerial) ");
            } else {
                strUpdate.append(" and to_number(serial) >= to_number(#fromSerial) and to_number(serial) <= to_number(#toSerial) ");
            }
        } else {
            if (isExactSerial) {
                strUpdate.append(" and serial = #fromSerial ");
            } else {
                strUpdate.append(" and serial >= #fromSerial and serial <= #toSerial ");
            }
        }
        if (isWriteLog) {
             strUpdate.append(" LOG ERRORS INTO ERR$_" + tableName + " ('INSERT') REJECT LIMIT UNLIMITED ");
        }
        Query query = emLib.createNativeQuery(strUpdate.toString());
        query.setParameter("ownerType",ownerType);
        query.setParameter("ownerId",ownerId);
        query.setParameter("newStatus",newStatus);
        query.setParameter("stateId",stateId);
        query.setParameter("stockTransId",stockTransId);
        query.setParameter("stockModelId",stockModelId);
        query.setParameter("oldStatus",oldStatus);
        query.setParameter("fromSerial",fromSerial);
        if (!isExactSerial) {
            query.setParameter("toSerial",toSerial);
        }
        return query.executeUpdate();
    }

    /**
     * isnert sert stock_x im1
     * @author thanhnt77
     * @param tableName
     * @param ownerType
     * @param ownerId
     * @param status
     * @param stateId
     * @param prodOfferId
     * @param serial
     * @param staffCode
     * @param sysDate
     * @param stockTransId
     * @return
     * @throws Exception
     */
    public int insertStockX(String tableName, Long ownerType, Long ownerId, Long status, Long stateId,
                               Long prodOfferId, String serial, String staffCode, Date sysDate, Long stockTransId, Long telecomServiceId) throws Exception {
        int index = 1;
        StringBuffer query = new StringBuffer();
        query.append("insert into ");
        query.append(tableName);
        query.append("  (ID, OWNER_TYPE, OWNER_ID, STATUS, STATE_ID, PROD_OFFER_ID, SERIAL, CREATE_DATE, CREATE_USER, STOCK_TRANS_ID, update_datetime, TELECOM_SERVICE_ID ");
        query.append(" ) values (").append(tableName).append("_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?)");
//        query.append(" LOG ERRORS INTO ERR$_" + tableName + " ('INSERT') REJECT LIMIT UNLIMITED ");
        Query queryInsert = em.createNativeQuery(query.toString());
        queryInsert.setParameter(index++, ownerType);
        queryInsert.setParameter(index++, ownerId);
        queryInsert.setParameter(index++, status);
        queryInsert.setParameter(index++, stateId);
        queryInsert.setParameter(index++, prodOfferId);
        queryInsert.setParameter(index++, formatSerial(serial, Const.STOCK_TYPE.STOCK_CARD_NAME.equals(tableName)));
        queryInsert.setParameter(index++, sysDate);
        queryInsert.setParameter(index++, staffCode);
        queryInsert.setParameter(index++, stockTransId);
        queryInsert.setParameter(index++, sysDate);
        queryInsert.setParameter(index, telecomServiceId);
        return queryInsert.executeUpdate();
    }

    /**
     * isnert sert stock_x im1
     * @author thanhnt77
     * @param tableName
     * @param ownerType
     * @param ownerId
     * @param status
     * @param stateId
     * @param stockModelId
     * @param serial
     * @param staffCode
     * @param sysDate
     * @param stockTransId
     * @return
     * @throws Exception
     */
    public int insertStockXIM1(String tableName, Long ownerType, Long ownerId, Long status, Long stateId,
                               Long stockModelId, String serial, String staffCode, Date sysDate, Long stockTransId, Long telecomServiceId) throws Exception {
        int index = 1;
        StringBuffer query = new StringBuffer();
        query.append("insert into bccs_im.");
        query.append(tableName);
        query.append("  (ID, OWNER_TYPE, OWNER_ID, STATUS, STATE_ID, STOCK_MODEL_ID, SERIAL, CREATE_DATE, CREATE_USER, STOCK_TRANS_ID, TELECOM_SERVICE_ID");
        query.append(" ) values (").append(tableName).append("_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?,? )");
//        query.append(" LOG ERRORS INTO ERR$_" + tableName + " ('INSERT') REJECT LIMIT UNLIMITED ");
        Query queryInsert = emLib.createNativeQuery(query.toString());
        queryInsert.setParameter(index++, ownerType);
        queryInsert.setParameter(index++, ownerId);
        queryInsert.setParameter(index++, status);
        queryInsert.setParameter(index++, stateId);
        queryInsert.setParameter(index++, stockModelId);
        queryInsert.setParameter(index++, formatSerial(serial, Const.STOCK_TYPE.STOCK_CARD_NAME.equals(tableName)));
        queryInsert.setParameter(index++, sysDate);
        queryInsert.setParameter(index++, staffCode);
        queryInsert.setParameter(index++, stockTransId);
        queryInsert.setParameter(index, telecomServiceId);
        return queryInsert.executeUpdate();
    }

    @Override
    public void doPrepareData(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, FlagStockDTO flagStockDTO) throws LogicException, Exception {

    }

    @Override
    public void doValidate(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, List<StockTransDetailDTO> lstStockTransDetail) throws LogicException, Exception {

    }
}
