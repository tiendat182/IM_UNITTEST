package com.viettel.bccs.im1.repo;

import com.google.common.collect.Lists;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.im1.dto.*;
import com.viettel.bccs.im1.model.QStockTotalIm1;
import com.viettel.bccs.im1.model.StockTotalIm1.COLUMNS;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.StockHandsetDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.extjs.FilterRequest;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

public class StockTotalIm1RepoImpl implements StockTotalIm1RepoCustom {

    public static final Logger logger = Logger.getLogger(StockTotalIm1RepoCustom.class);
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM_LIB)
    private EntityManager emIM;

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QStockTotalIm1 stockTotal = QStockTotalIm1.stockTotal;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case OWNERID:
                        expression = DbUtil.createLongExpression(stockTotal.ownerId, filter);
                        break;
                    case OWNERTYPE:
                        expression = DbUtil.createLongExpression(stockTotal.ownerType, filter);
                        break;
                    case STOCKMODELID:
                        expression = DbUtil.createLongExpression(stockTotal.stockModelId, filter);
                        break;
                    case STATEID:
                        expression = DbUtil.createLongExpression(stockTotal.stateId, filter);
                        break;
                    case QUANTITY:
                        expression = DbUtil.createLongExpression(stockTotal.quantity, filter);
                        break;
                    case QUANTITYISSUE:
                        expression = DbUtil.createLongExpression(stockTotal.quantityIssue, filter);
                        break;
                    case MODIFIEDDATE:
                        expression = DbUtil.createDateExpression(stockTotal.modifiedDate, filter);
                        break;
                    case STATUS:
                        expression = DbUtil.createLongExpression(stockTotal.status, filter);
                        break;
                    case QUANTITYDIAL:
                        expression = DbUtil.createLongExpression(stockTotal.quantityDial, filter);
                        break;
                    case MAXDEBIT:
                        expression = DbUtil.createLongExpression(stockTotal.maxDebit, filter);
                        break;
                    case CURRENTDEBIT:
                        expression = DbUtil.createLongExpression(stockTotal.currentDebit, filter);
                        break;
                    case DATERESET:
                        expression = DbUtil.createDateExpression(stockTotal.dateReset, filter);
                        break;
                    case LIMITQUANTITY:
                        expression = DbUtil.createLongExpression(stockTotal.limitQuantity, filter);
                        break;
                    case QUANTITYHANG:
                        expression = DbUtil.createLongExpression(stockTotal.quantityHang, filter);
                        break;
                    case EXCLUSEIDLIST:
                        break;
                }
                if (expression != null) {
                    result = result.and(expression);
                }
            }
        } catch (Exception ex) {
            logger.error(ex);
        }
        logger.info("Result Predicate :: " + (result != null ? result.toString() : ""));
        logger.info("Exiting predicates");
        return result;
    }

    @Override
    public List<StockTransDetailIm1DTO> findStockTransDetail(Long stockTransId) throws LogicException, Exception {
        List<StockTransDetailIm1DTO> listStockTransDetail = new ArrayList<>();
        String sql = "SELECT a.stock_trans_detail_id, a.stock_trans_id, a.stock_model_id, " +
                "a.state_id, a.quantity_res, a.quantity_real, " +
                "a.note, a.check_dial, a.dial_status, a.model_program_id, " +
                "a.model_program_name, a.stock_model_id_swap, " +
                "a.quantity_overlimit, a.price_overlimit, a.valid ,b.name stockModelName " +
                "FROM bccs_im.stock_trans_detail a, bccs_im.stock_model b  where a.stock_model_id = b.stock_model_id " +
                "and  a.stock_trans_id = #stockTransId ";
        Query query = emIM.createNativeQuery(sql);
        query.setParameter("stockTransId", stockTransId);
        List<Object[]> lstResult = query.getResultList();

        for (Object[] object : lstResult) {
            int index = 0;
            StockTransDetailIm1DTO stockTransDetail = new StockTransDetailIm1DTO();
            stockTransDetail.setStockTransDetailId(DataUtil.safeToLong(object[index++]));
            stockTransDetail.setStockTransId(DataUtil.safeToLong(object[index++]));
            stockTransDetail.setStockModelId(DataUtil.safeToLong(object[index++]));
            stockTransDetail.setStateId(DataUtil.safeToLong(object[index++]));
            stockTransDetail.setQuantityRes(DataUtil.safeToLong(object[index++]));
            stockTransDetail.setQuantityReal(DataUtil.safeToLong(object[index++]));
            stockTransDetail.setNote(DataUtil.safeToString(object[index++]));
            stockTransDetail.setCheckDial(DataUtil.safeToLong(object[index++]));
            stockTransDetail.setDialStatus(DataUtil.safeToLong(object[index++]));
            stockTransDetail.setModelProgramId(DataUtil.safeToLong(object[index++]));
            stockTransDetail.setModelProgramName(DataUtil.safeToString(object[index++]));
            stockTransDetail.setStockModelIdSwap(DataUtil.safeToLong(object[index++]));
            stockTransDetail.setQuantityOverlimit(DataUtil.safeToLong(object[index++]));
            stockTransDetail.setPriceOverlimit(DataUtil.safeToLong(object[index++]));
            stockTransDetail.setValid(DataUtil.safeToLong(object[index++]));
            stockTransDetail.setStockModelName(DataUtil.safeToString(object[index]));
            listStockTransDetail.add(stockTransDetail);

        }
        return listStockTransDetail;
    }

    @Override
    public List<StockTotalIm1DTO> findOneStockTotal(StockTotalIm1DTO stockTotalDTO) throws LogicException, Exception {
        List<StockTotalIm1DTO> stockTotalIm1DTOList = Lists.newArrayList();
        StockTotalIm1DTO stockTotal;
        String sql = "SELECT a.owner_id, a.owner_type, a.stock_model_id, a.state_id, " +
                "       a.quantity, a.quantity_issue,  a.status, " +
                "       a.quantity_dial, a.max_debit, a.current_debit, a.date_reset, " +
                "       a.quantity_hang " +
                "  FROM bccs_im.stock_total a WHERE a.stock_model_id = #stockModelId " +
                "  and a.owner_id = #ownerId and a.state_id = #stateId and a.owner_type = #ownerType ";
        if (!DataUtil.isNullObject(stockTotalDTO.getStatus())) {
            sql += "and a.status = #status";
        }

        sql += " for update nowait ";

        Query query = emIM.createNativeQuery(sql);
        query.setParameter("stockModelId", stockTotalDTO.getStockModelId());
        query.setParameter("ownerId", stockTotalDTO.getOwnerId());
        query.setParameter("ownerType", stockTotalDTO.getOwnerType());
        query.setParameter("stateId", stockTotalDTO.getStateId());
        if (!DataUtil.isNullObject(stockTotalDTO.getStatus())) {
            query.setParameter("status", stockTotalDTO.getStatus());
        }
        List<Object[]> lstResult = Lists.newArrayList();
        try {
            lstResult = query.getResultList();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "basestock1.validate.invalidate.stocktotal.updating");
        }

        for (Object[] object : lstResult) {
            int index = 0;
            stockTotal = new StockTotalIm1DTO();
            stockTotal.setOwnerId(DataUtil.safeToLong(object[index++]));
            stockTotal.setOwnerType(DataUtil.safeToLong(object[index++]));
            stockTotal.setStockModelId(DataUtil.safeToLong(object[index++]));
            stockTotal.setStateId(DataUtil.safeToLong(object[index++]));
            stockTotal.setQuantity(DataUtil.safeToLong(object[index++]));
            stockTotal.setQuantityIssue(DataUtil.safeToLong(object[index++]));
            stockTotal.setStatus(DataUtil.safeToLong(object[index++]));
            stockTotal.setQuantityDial(DataUtil.safeToLong(object[index++]));
            stockTotal.setMaxDebit(DataUtil.safeToLong(object[index++]));
            stockTotal.setCurrentDebit(DataUtil.safeToLong(object[index++]));
            stockTotal.setDateReset(DataUtil.safeToLong(object[index++]));
//            stockTotal.setLimitQuantity(DataUtil.safeToLong(object[index++]));
            stockTotal.setQuantityHang(DataUtil.safeToLong(object[index]));

            stockTotalIm1DTOList.add(stockTotal);
        }
        return stockTotalIm1DTOList;
    }

    @Override
    public StockModelIm1DTO findOneStockModel(Long stockModelId) throws LogicException, Exception {
        StockModelIm1DTO stockModel = null;
        String sql = "SELECT a.stock_model_id, a.stock_model_code, a.stock_type_id, a.name, " +
                "       a.vat, a.check_serial, a.check_deposit, a.check_dial, a.unit, " +
                "       a.status,  a.stock_model_type, a.source_price " +
                "  FROM bccs_im.stock_model a WHERE a.stock_model_id = #stockModelId  ";
        Query query = emIM.createNativeQuery(sql);
        query.setParameter("stockModelId", stockModelId);
        List<Object[]> lstResult = query.getResultList();

        for (Object[] object : lstResult) {
            int index = 0;
            stockModel = new StockModelIm1DTO();
            stockModel.setStockModelId(DataUtil.safeToLong(object[index++]));
            stockModel.setStockModelCode(DataUtil.safeToString(object[index++]));
            stockModel.setStockTypeId(DataUtil.safeToLong(object[index++]));
            stockModel.setName(DataUtil.safeToString(object[index++]));
            stockModel.setVat(DataUtil.safeToLong(object[index++]));
            stockModel.setCheckSerial(DataUtil.safeToLong(object[index++]));
            stockModel.setCheckDeposit(DataUtil.safeToLong(object[index++]));
            stockModel.setCheckDial(DataUtil.safeToLong(object[index++]));
            stockModel.setUnit(DataUtil.safeToString(object[index++]));
            stockModel.setStatus(DataUtil.safeToLong(object[index++]));
            stockModel.setStockModelType(DataUtil.safeToLong(object[index++]));
            stockModel.setSourcePrice(DataUtil.safeToLong(object[index]));

            break;
        }
        return stockModel;
    }

    @Override
    public StockHandsetDTO getStockHandsetIm1(Long prodOfferId, String serial, Long ownerType, Long ownerId, Long stateId) throws LogicException, Exception {
        StockHandsetDTO stockModel = null;
        StringBuilder sqlQuery = new StringBuilder("");
        sqlQuery.append(" SELECT   sh.id, sh.stock_model_id, sh.serial, sh.owner_id, sh.owner_type, sh.status, sh.state_id ");
        sqlQuery.append("  FROM   bccs_im.stock_handset sh, bccs_im.stock_model po");
        sqlQuery.append(" WHERE       sh.stock_model_id = po.stock_model_id");
        sqlQuery.append("		 AND sh.stock_model_id = #prodOfferId");
        sqlQuery.append("		 AND sh.serial = #serial");
        sqlQuery.append("		 AND po.status = #status");
        if (!DataUtil.isNullOrZero(ownerType) && !DataUtil.isNullOrZero(ownerId)) {
            sqlQuery.append(" and sh.owner_type = #ownerType");
            sqlQuery.append(" and sh.owner_id = #ownerId");
        }
        if (!DataUtil.isNullOrZero(stateId)) {
            sqlQuery.append(" and sh.state_id = #stateId");
        }
        Query query = emIM.createNativeQuery(sqlQuery.toString());
        query.setParameter("prodOfferId", prodOfferId);
        query.setParameter("serial", serial);
        query.setParameter("status", Const.STATUS_ACTIVE);
        if (!DataUtil.isNullOrZero(ownerType) && !DataUtil.isNullOrZero(ownerId)) {
            query.setParameter("ownerType", ownerType);
            query.setParameter("ownerId", ownerId);
        }
        if (!DataUtil.isNullOrZero(stateId)) {
            query.setParameter("stateId", stateId);
        }
        List<Object[]> lstResult = query.getResultList();

        int index ;
        for (Object[] object : lstResult) {
            index = 0;
            stockModel = new StockHandsetDTO();
            stockModel.setId(DataUtil.safeToLong(object[index++]));
            stockModel.setProdOfferId(DataUtil.safeToLong(object[index++]));
            stockModel.setSerial(DataUtil.safeToString(object[index++]));
            stockModel.setOwnerId(DataUtil.safeToLong(object[index++]));
            stockModel.setOwnerType(DataUtil.safeToLong(object[index++]));
            stockModel.setStatus(DataUtil.safeToLong(object[index++]));
            stockModel.setStateId(DataUtil.safeToLong(object[index]));
            break;
        }
        return stockModel;
    }

    @Override
    public StockTypeIm1DTO findOneStockType(Long stockModelId) throws LogicException, Exception {
        StockTypeIm1DTO stockType = null;
        String sql = "SELECT a.stock_type_id, a.name, a.status, a.notes, a.table_name, " +
                "       a.check_exp " +
                "  FROM bccs_im.stock_type a " +
                "  where a.stock_type_id in (select b.stock_type_id from bccs_im.stock_model b where b.stock_model_id = #stockModelId)  ";
        Query query = emIM.createNativeQuery(sql);
        query.setParameter("stockModelId", stockModelId);
        List<Object[]> lstResult = query.getResultList();

        for (Object[] object : lstResult) {
            int index = 0;
            stockType = new StockTypeIm1DTO();
            stockType.setStockTypeId(DataUtil.safeToLong(object[index++]));
            stockType.setName(DataUtil.safeToString(object[index++]));
            stockType.setStatus(DataUtil.safeToLong(object[index++]));
            stockType.setNotes(DataUtil.safeToString(object[index++]));
            stockType.setTableName(DataUtil.safeToString(object[index++]));
            stockType.setCheckExp(DataUtil.safeToLong(object[index]));

            break;
        }
        return stockType;
    }

    @Override
    public List<StockTransSerialIm1DTO> findSerialByStockTransDetail(StockTransDetailIm1DTO stockTransDetail) throws LogicException, Exception {
        List<StockTransSerialIm1DTO> listStockTransSerial = new ArrayList<>();
        String sql = "SELECT a.stock_trans_serial_id, a.state_id, a.stock_trans_id, " +
                "       a.stock_model_id, a.from_serial, a.to_serial, a.quantity, " +
                "       a.length_isdn, a.type_isdn,a.valid " +
                "  FROM bccs_im.stock_trans_serial a where a.stock_trans_id = #stockTransId and a.stock_model_id = #stockModelId ";
        Query query = emIM.createNativeQuery(sql);
        query.setParameter("stockTransId", stockTransDetail.getStockTransId());
        query.setParameter("stockModelId", stockTransDetail.getStockModelId());
        List<Object[]> lstResult = query.getResultList();

        for (Object[] object : lstResult) {
            int index = 0;
            StockTransSerialIm1DTO stockTransSerial = new StockTransSerialIm1DTO();
            stockTransSerial.setStockTransSerialId(DataUtil.safeToLong(object[index++]));
            stockTransSerial.setStateId(DataUtil.safeToLong(object[index++]));
            stockTransSerial.setStockTransId(DataUtil.safeToLong(object[index++]));
            stockTransSerial.setStockModelId(DataUtil.safeToLong(object[index++]));
            stockTransSerial.setFromSerial(DataUtil.safeToString(object[index++]));
            stockTransSerial.setToSerial(DataUtil.safeToString(object[index++]));
            stockTransSerial.setQuantity(DataUtil.safeToLong(object[index++]));
            stockTransSerial.setLengthIsdn(DataUtil.safeToLong(object[index++]));
            stockTransSerial.setTypeIsdn(DataUtil.safeToString(object[index++]));
            stockTransSerial.setValid(DataUtil.safeToLong(object[index]));

            listStockTransSerial.add(stockTransSerial);
        }
        return listStockTransSerial;
    }

    @Override
    public int createStockTotal(StockTotalIm1DTO dto) throws LogicException, Exception {
        String sql = "INSERT INTO bccs_im.stock_total a " +
                "       (a.owner_id, a.owner_type, a.stock_model_id, a.state_id," +
                "       a.quantity, a.quantity_issue, a.modified_date, a.status," +
                "       a.quantity_hang) " +
                " VALUES(?,?,?,?,?,?,sysdate,?,?) ";
        Query query = emIM.createNativeQuery(sql);
        query.setParameter((int) 1, dto.getOwnerId());
        query.setParameter((int) 2, dto.getOwnerType());
        query.setParameter((int) 3, dto.getStockModelId());
        query.setParameter((int) 4, dto.getStateId());
        query.setParameter((int) 5, dto.getQuantity());
        query.setParameter((int) 6, dto.getQuantityIssue());
        query.setParameter((int) 7, dto.getStatus());
        query.setParameter((int) 8, dto.getQuantityHang());

        return query.executeUpdate();
    }

    @Override
    public int updateStockTotal(StockTotalIm1DTO dto) throws LogicException, Exception {
        String sql = "update bccs_im.stock_total a set a.quantity_issue = ?, a.quantity = ? , a.quantity_hang = ?, a.modified_date = ? " +
                "where a.owner_type = ? and a.owner_id = ? and a.stock_model_id = ? and a.status = ? and a.state_id = ? ";
        Query query = emIM.createNativeQuery(sql);
        query.setParameter((int) 1, dto.getQuantityIssue());
        query.setParameter((int) 2, dto.getQuantity());
        query.setParameter((int) 3, dto.getQuantityHang());
        query.setParameter((int) 4, dto.getModifiedDate());
        query.setParameter((int) 5, dto.getOwnerType());
        query.setParameter((int) 6, dto.getOwnerId());
        query.setParameter((int) 7, dto.getStockModelId());
        query.setParameter((int) 8, dto.getStatus());
        query.setParameter((int) 9, dto.getStateId());

        return query.executeUpdate();
    }
}