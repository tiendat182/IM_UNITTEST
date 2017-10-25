package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.common.Const;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.model.Mt.COLUMNS;
import com.viettel.bccs.inventory.model.QMt;

import java.util.List;

import com.viettel.fw.common.util.DbUtil;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public class MtRepoImpl implements MtRepoCustom {

    public static final Logger logger = Logger.getLogger(MtRepoCustom.class);

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QMt mt = QMt.mt;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case APPID:
                        expression = DbUtil.createStringExpression(mt.appId, filter);
                        break;
                    case APPNAME:
                        expression = DbUtil.createStringExpression(mt.appName, filter);
                        break;
                    case CHANNEL:
                        expression = DbUtil.createStringExpression(mt.channel, filter);
                        break;
                    case MESSAGE:
                        expression = DbUtil.createStringExpression(mt.message, filter);
                        break;
                    case MOHISID:
                        expression = DbUtil.createLongExpression(mt.moHisId, filter);
                        break;
                    case MSISDN:
                        expression = DbUtil.createStringExpression(mt.msisdn, filter);
                        break;
                    case MTID:
                        expression = DbUtil.createLongExpression(mt.mtId, filter);
                        break;
                    case RECEIVETIME:
                        expression = DbUtil.createDateExpression(mt.receiveTime, filter);
                        break;
                    case RETRYNUM:
                        expression = DbUtil.createLongExpression(mt.retryNum, filter);
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
    public void deleteMessage(Long id) throws LogicException, Exception {
        StringBuilder builder = new StringBuilder("DELETE MT where MT_ID=? and msisdn='012345'");
        Query query = em.createNativeQuery(builder.toString());
        query.setParameter(1, id);
        query.executeUpdate();
    }
}