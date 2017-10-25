package com.viettel.bccs.inventory.repo;

import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.model.QVoucherInfo;
import com.viettel.bccs.inventory.model.VoucherInfo;
import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * @author HoangAnh
 */
public class VoucherInfoRepoImpl implements VoucherInfoRepoCustom {

    public static final Logger logger = Logger.getLogger(VoucherInfoRepoCustom.class);

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    /*@Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QVoucherInfo qVoucherInfo = QVoucherInfo.voucherInfo;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                VoucherInfo.COLUMNS column = VoucherInfo.COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case VOUCHERINFOID:
                        expression = DbUtil.createLongExpression(qVoucherInfo.voucherInfoId, filter);
                        break;
                    case AMOUNT:
                        expression = DbUtil.createLongExpression(qVoucherInfo.amount, filter);
                        break;
                    case BONUSSTATUS:
                        expression = DbUtil.createShortExpression(qVoucherInfo.bonusStatus, filter);
                        break;
                    case CREATEDATE:
                        expression = DbUtil.createDateExpression(qVoucherInfo.createDate, filter);
                        break;
                    case FROMDATE:
                        expression = DbUtil.createDateExpression(qVoucherInfo.fromDate, filter);
                        break;
                    case LASTUPDATE:
                        expression = DbUtil.createDateExpression(qVoucherInfo.lastUpdate, filter);
                        break;
                    case OBJECT:
                        expression = DbUtil.createStringExpression(qVoucherInfo.object, filter);
                        break;
                    case PASS:
                        expression = DbUtil.createStringExpression(qVoucherInfo.pass, filter);
                        break;
                    case SERIAL:
                        expression = DbUtil.createStringExpression(qVoucherInfo.serial, filter);
                        break;
                    case STAFFCODE:
                        expression = DbUtil.createStringExpression(qVoucherInfo.staffCode, filter);
                        break;
                    case STATUSCONNECT:
                        expression = DbUtil.createLongExpression(qVoucherInfo.statusConnect, filter);
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
    }*/

    @Override
    public VoucherInfo findBySerial(String serial) {
        String strQuery= "select * from voucher_info where serial = #serial";
        Query query = em.createNativeQuery(strQuery, VoucherInfo.class);
        query.setParameter("serial", serial);
        List<VoucherInfo> lst = query.getResultList();
        if (lst.isEmpty()) return null; //neu query ko co du lieu se return list rong
        return lst.get(0);
    }
}