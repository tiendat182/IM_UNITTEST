package com.viettel.bccs.sale.repo;

import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.sale.dto.SaleTransSerialDTO.COLUMNS;
import com.viettel.bccs.sale.model.QSaleTransSerial;
import com.viettel.bccs.sale.model.SaleTransSerial;
import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;

public class SaleTransSerialRepoImpl implements SaleTransSerialRepoCustom {

    public static final Logger logger = Logger.getLogger(SaleTransSerialRepoCustom.class);

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_SALE)
    private EntityManager em;

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QSaleTransSerial saleTransSerial = QSaleTransSerial.saleTransSerial;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case DATEDELIVER:
                        expression = DbUtil.createDateExpression(saleTransSerial.dateDeliver, filter);
                        break;
                    case FROMSERIAL:
                        expression = DbUtil.createStringExpression(saleTransSerial.fromSerial, filter);
                        break;
                    case QUANTITY:
                        expression = DbUtil.createLongExpression(saleTransSerial.quantity, filter);
                        break;
                    case SALETRANSDATE:
                        expression = DbUtil.createDateExpression(saleTransSerial.saleTransDate, filter);
                        break;
                    case SALETRANSDETAILID:
                        expression = DbUtil.createLongExpression(saleTransSerial.saleTransDetailId, filter);
                        break;
                    case SALETRANSID:
                        expression = DbUtil.createLongExpression(saleTransSerial.saleTransId, filter);
                        break;
                    case SALETRANSSERIALID:
                        expression = DbUtil.createLongExpression(saleTransSerial.saleTransSerialId, filter);
                        break;
                    case STOCKMODELID:
                        expression = DbUtil.createLongExpression(saleTransSerial.stockModelId, filter);
                        break;
                    case TOSERIAL:
                        expression = DbUtil.createStringExpression(saleTransSerial.toSerial, filter);
                        break;
                    case USERDELIVER:
                        expression = DbUtil.createLongExpression(saleTransSerial.userDeliver, filter);
                        break;
                    case USERUPDATE:
                        expression = DbUtil.createLongExpression(saleTransSerial.userUpdate, filter);
                        break;
                    default:
                        result = result.and(BooleanTemplate.create("1 = 0"));
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
    public List<SaleTransSerial> findByDetailId(Long saleTransDetailId, Date saleTransDate) {
        try {
            String sql = "FROM SaleTransSerial a WHERE a.saleTransDetailId = ?1 AND a.saleTransDate >= ?2 ";
            Query query = em.createQuery(sql);
            query.setParameter(1, saleTransDetailId);
            query.setParameter(2, saleTransDate);

            return query.getResultList();
        } catch (RuntimeException re) {
            throw re;
        }
    }

    @Override
    public List<SaleTransSerial> findBySaleTransDetailIdAndDate(Long saleTransDetailId, Date saleTransDate) throws Exception {
        QSaleTransSerial saleTransSerial = QSaleTransSerial.saleTransSerial;
        return new JPAQuery(em).from(saleTransSerial).where(saleTransSerial.saleTransDetailId.eq(saleTransDetailId), saleTransSerial.saleTransDate.eq(saleTransDate)).list(saleTransSerial);
    }
}