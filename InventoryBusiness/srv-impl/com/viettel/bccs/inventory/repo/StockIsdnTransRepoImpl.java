package com.viettel.bccs.inventory.repo;

import com.google.common.collect.Lists;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.ManageTransStockDto;
import com.viettel.bccs.inventory.model.QStockIsdnTrans;
import com.viettel.bccs.inventory.model.StockIsdnTrans;
import com.viettel.bccs.inventory.model.StockIsdnTrans.COLUMNS;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DateUtil;
import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

public class StockIsdnTransRepoImpl implements StockIsdnTransRepoCustom {

    public static final Logger logger = Logger.getLogger(StockIsdnTransRepoCustom.class);
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QStockIsdnTrans stockIsdnTrans = QStockIsdnTrans.stockIsdnTrans;
        BooleanExpression result = BooleanTemplate.create("1 = 1 ");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case CREATEDTIME:
                        expression = DbUtil.createDateExpression(stockIsdnTrans.createdTime, filter);
                        break;
                    case CREATEDUSERCODE:
                        expression = DbUtil.createStringExpression(stockIsdnTrans.createdUserCode, filter);
                        break;
                    case CREATEDUSERID:
                        expression = DbUtil.createLongExpression(stockIsdnTrans.createdUserId, filter);
                        break;
                    case CREATEDUSERIP:
                        expression = DbUtil.createStringExpression(stockIsdnTrans.createdUserIp, filter);
                        break;
                    case CREATEDUSERNAME:
                        expression = DbUtil.createStringExpression(stockIsdnTrans.createdUserName, filter);
                        break;
                    case FROMOWNERCODE:
                        expression = DbUtil.createStringExpression(stockIsdnTrans.fromOwnerCode, filter);
                        break;
                    case FROMOWNERID:
                        expression = DbUtil.createLongExpression(stockIsdnTrans.fromOwnerId, filter);
                        break;
                    case FROMOWNERNAME:
                        expression = DbUtil.createStringExpression(stockIsdnTrans.fromOwnerName, filter);
                        break;
                    case FROMOWNERTYPE:
                        expression = DbUtil.createLongExpression(stockIsdnTrans.fromOwnerType, filter);
                        break;
                    case LASTUPDATEDTIME:
                        expression = DbUtil.createDateExpression(stockIsdnTrans.lastUpdatedTime, filter);
                        break;
                    case LASTUPDATEDUSERCODE:
                        expression = DbUtil.createStringExpression(stockIsdnTrans.lastUpdatedUserCode, filter);
                        break;
                    case LASTUPDATEDUSERID:
                        expression = DbUtil.createLongExpression(stockIsdnTrans.lastUpdatedUserId, filter);
                        break;
                    case LASTUPDATEDUSERIP:
                        expression = DbUtil.createStringExpression(stockIsdnTrans.lastUpdatedUserIp, filter);
                        break;
                    case LASTUPDATEDUSERNAME:
                        expression = DbUtil.createStringExpression(stockIsdnTrans.lastUpdatedUserName, filter);
                        break;
                    case NOTE:
                        expression = DbUtil.createStringExpression(stockIsdnTrans.note, filter);
                        break;
                    case QUANTITY:
                        expression = DbUtil.createLongExpression(stockIsdnTrans.quantity, filter);
                        break;
                    case REASONCODE:
                        expression = DbUtil.createStringExpression(stockIsdnTrans.reasonCode, filter);
                        break;
                    case REASONID:
                        expression = DbUtil.createLongExpression(stockIsdnTrans.reasonId, filter);
                        break;
                    case REASONNAME:
                        expression = DbUtil.createStringExpression(stockIsdnTrans.reasonName, filter);
                        break;
                    case STATUS:
                        expression = DbUtil.createLongExpression(stockIsdnTrans.status, filter);
                        break;
                    case STOCKISDNTRANSCODE:
                        expression = DbUtil.createStringExpression(stockIsdnTrans.stockIsdnTransCode, filter);
                        break;
                    case STOCKISDNTRANSID:
                        expression = DbUtil.createLongExpression(stockIsdnTrans.stockIsdnTransId, filter);
                        break;
                    case STOCKTYPEID:
                        expression = DbUtil.createLongExpression(stockIsdnTrans.stockTypeId, filter);
                        break;
                    case STOCKTYPENAME:
                        expression = DbUtil.createStringExpression(stockIsdnTrans.stockTypeName, filter);
                        break;
                    case TOOWNERCODE:
                        expression = DbUtil.createStringExpression(stockIsdnTrans.toOwnerCode, filter);
                        break;
                    case TOOWNERID:
                        expression = DbUtil.createLongExpression(stockIsdnTrans.toOwnerId, filter);
                        break;
                    case TOOWNERNAME:
                        expression = DbUtil.createStringExpression(stockIsdnTrans.toOwnerName, filter);
                        break;
                    case TOOWNERTYPE:
                        expression = DbUtil.createLongExpression(stockIsdnTrans.toOwnerType, filter);
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
    public List<StockIsdnTrans> searchHistory(ManageTransStockDto searchDTO) throws LogicException, Exception {
        StringBuilder sql = new StringBuilder();
        List params = Lists.newArrayList();
        if (DataUtil.isNullObject(searchDTO.getIsdn())) {
            throw new LogicException("", "search.isdn.isdn.null");
        }
        if (!DataUtil.isNumber(searchDTO.getIsdn())) {
            throw new LogicException("", "search.isdn.isdn.invalid");
        }
        if (DataUtil.isNullObject(searchDTO.getFromDate())) {
            throw new LogicException("", "mn.stock.from.date.not.blank");
        }
        if (DataUtil.isNullObject(searchDTO.getToDate())) {
            throw new LogicException("", "mn.stock.to.date.not.blank");
        }
        if (DateUtil.monthsBetween(searchDTO.getFromDate(), searchDTO.getToDate()) > 1) {
            throw new LogicException("", "search.isdn.from.to.invalid");
        }
        sql.append("SELECT sit.* FROM Stock_Isdn_Trans sit,Stock_Isdn_Trans_Detail sitd WHERE 1=1 ");
        sql.append("AND sit.stock_Isdn_Trans_Id = sitd.stock_Isdn_Trans_Id ");
        if (!DataUtil.isNullObject(searchDTO.getFromDate())) {
            sql.append("AND sit.created_Time >= trunc(?)");
            params.add(searchDTO.getFromDate());
        }
        if (!DataUtil.isNullObject(searchDTO.getToDate())) {
            sql.append("AND sit.created_Time < trunc(?)+1 ");
            params.add(searchDTO.getToDate());
        }

        if (!DataUtil.isNullObject(searchDTO.getFromDate())) {
            sql.append("AND sitd.created_Time >= trunc(?)");
            params.add(searchDTO.getFromDate());
        }
        if (!DataUtil.isNullObject(searchDTO.getToDate())) {
            sql.append("AND sitd.created_Time < trunc(?)+1 ");
            params.add(searchDTO.getToDate());
        }
        if (!DataUtil.isNullObject(searchDTO.getIsdn())) {
            sql.append("AND to_number(sitd.from_Isdn) <= ? AND to_number(sitd.to_Isdn) >= ?");
            params.add(DataUtil.safeToLong(searchDTO.getIsdn()));
            params.add(DataUtil.safeToLong(searchDTO.getIsdn()));
        }
        Query query = em.createNativeQuery(sql.toString(), StockIsdnTrans.class);
        for (int i = 0; i < params.size(); i++) {
            query.setParameter(i + 1, params.get(i));
        }
        return query.getResultList();
    }
}