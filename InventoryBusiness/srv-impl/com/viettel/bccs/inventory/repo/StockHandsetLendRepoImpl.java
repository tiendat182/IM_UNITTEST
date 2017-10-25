package com.viettel.bccs.inventory.repo;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.model.StockHandsetLend.COLUMNS;
import com.viettel.bccs.inventory.model.QStockHandsetLend;
import java.util.List;
import com.viettel.fw.common.util.DbUtil;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class StockHandsetLendRepoImpl implements StockHandsetLendRepoCustom {

    public static final Logger logger = Logger.getLogger(StockHandsetLendRepoCustom.class);
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QStockHandsetLend stockHandsetLend = QStockHandsetLend.stockHandsetLend;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case CREATEDATE:
                        expression = DbUtil.createDateExpression(stockHandsetLend.createDate, filter); 
                        break;
                    case CREATESTAFFID:
                        expression = DbUtil.createLongExpression(stockHandsetLend.createStaffId, filter);
                        break;
                    case ID:
                        expression = DbUtil.createLongExpression(stockHandsetLend.id, filter); 
                        break;
                    case LASTMODIFY:
                        expression = DbUtil.createDateExpression(stockHandsetLend.lastModify, filter); 
                        break;
                    case OWNERID:
                        expression = DbUtil.createLongExpression(stockHandsetLend.ownerId, filter);
                        break;
                    case OWNERTYPE:
                        expression = DbUtil.createLongExpression(stockHandsetLend.ownerType, filter);
                        break;
                    case PRODOFFERID:
                        expression = DbUtil.createLongExpression(stockHandsetLend.prodOfferId, filter);
                        break;
                    case SERIAL:
                        expression = DbUtil.createStringExpression(stockHandsetLend.serial, filter); 
                        break;
                    case STATUS:
                        expression = DbUtil.createLongExpression(stockHandsetLend.status, filter);
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
    public Long getSequence() throws Exception {
        return DbUtil.getSequence(em, "STOCK_HANDSET_LEND_SEQ");
    }

}