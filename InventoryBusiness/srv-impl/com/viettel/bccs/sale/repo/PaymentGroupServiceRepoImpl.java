package com.viettel.bccs.sale.repo;

import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.sale.dto.PaymentGroupServiceDTO;
import com.viettel.bccs.sale.model.PaymentGroup;
import com.viettel.bccs.sale.model.PaymentGroupService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.sale.model.PaymentGroupService.COLUMNS;
import com.viettel.bccs.sale.model.QPaymentGroupService;

import java.util.List;

import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.mapper.BaseMapper;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public class PaymentGroupServiceRepoImpl implements PaymentGroupServiceRepoCustom {

    public static final Logger logger = Logger.getLogger(PaymentGroupServiceRepoCustom.class);
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_SALE)
    private EntityManager sale;

    private final BaseMapper<PaymentGroupService, PaymentGroupServiceDTO> mapper = new BaseMapper<>(PaymentGroupService.class, PaymentGroupServiceDTO.class);

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QPaymentGroupService paymentGroupService = QPaymentGroupService.paymentGroupService;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case CODE:
                        expression = DbUtil.createStringExpression(paymentGroupService.code, filter);
                        break;
                    case CREATEDATE:
                        expression = DbUtil.createDateExpression(paymentGroupService.createDate, filter);
                        break;
                    case CREATEUSER:
                        expression = DbUtil.createStringExpression(paymentGroupService.createUser, filter);
                        break;
                    case ID:
                        expression = DbUtil.createLongExpression(paymentGroupService.id, filter);
                        break;
                    case LASTUPDATEDATE:
                        expression = DbUtil.createDateExpression(paymentGroupService.lastUpdateDate, filter);
                        break;
                    case LASTUPDATEUSER:
                        expression = DbUtil.createStringExpression(paymentGroupService.lastUpdateUser, filter);
                        break;
                    case NAME:
                        expression = DbUtil.createStringExpression(paymentGroupService.name, filter);
                        break;
                    case STATUS:
                        expression = DbUtil.createLongExpression(paymentGroupService.status, filter);
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

    public PaymentGroupServiceDTO getPaymentGroupServiceByName(String name) throws Exception {
        try {
            StringBuilder strQuery = new StringBuilder();
            strQuery.append("SELECT * FROM payment_group_service where lower(name) = #name and status = 1");
            //execute query
            Query query = sale.createNativeQuery(strQuery.toString(), PaymentGroupService.class);
            query.setParameter("name", name.toLowerCase());
            List<PaymentGroupService> lst = query.getResultList();
            if (lst != null && !lst.isEmpty()) {
                return mapper.toDtoBean(lst.get(0));
            }
        } catch (Exception ex) {
            logger.error("Error: ", ex);
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "Error Exception");
        }
        return null;
    }
}