package com.viettel.bccs.inventory.repo;

import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.model.QStockTransDocument;
import com.viettel.bccs.inventory.model.StockTransDocument.COLUMNS;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
public class StockTransDocumentRepoImpl implements StockTransDocumentRepoCustom {

    public static final Logger logger = Logger.getLogger(StockTransDocumentRepoCustom.class);

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager emIM;

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QStockTransDocument stockTransDocument = QStockTransDocument.stockTransDocument;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case STOCKTRANSDOCUMENTID:
                        expression = DbUtil.createLongExpression(stockTransDocument.stockTransDocumentId, filter); 
                        break;
                    case STOCKTRANSID:
                        expression = DbUtil.createLongExpression(stockTransDocument.stockTransId, filter); 
                        break;
                    case TRANSTYPE:
                        expression = DbUtil.createLongExpression(stockTransDocument.transType, filter); 
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
    public byte[] getAttachFileContent(Long stockTransId) throws Exception {
        StringBuilder builder = new StringBuilder(" select file_attach from STOCK_TRANS_DOCUMENT where stock_trans_id= #stockTransId");
        Query query = emIM.createNativeQuery(builder.toString());
        query.setParameter("stockTransId", stockTransId);
        List<Object> lstResult = query.getResultList();
        if (!DataUtil.isNullObject(lstResult.get(0))) {
            return (byte[]) lstResult.get(0);
        }
        return new byte[]{};
    }

}