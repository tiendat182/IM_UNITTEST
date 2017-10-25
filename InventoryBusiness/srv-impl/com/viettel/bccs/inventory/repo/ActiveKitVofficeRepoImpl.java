package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.ActiveKitVofficeDTO;
import com.viettel.bccs.inventory.model.ActiveKitVoffice;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.model.ActiveKitVoffice.COLUMNS;
import com.viettel.bccs.inventory.model.QActiveKitVoffice;

import java.util.List;

import com.viettel.fw.common.util.DbUtil;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public class ActiveKitVofficeRepoImpl implements ActiveKitVofficeRepoCustom {

    public static final Logger logger = Logger.getLogger(ActiveKitVofficeRepoCustom.class);
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QActiveKitVoffice activeKitVoffice = QActiveKitVoffice.activeKitVoffice;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case ACCOUNTNAME:
                        expression = DbUtil.createStringExpression(activeKitVoffice.accountName, filter);
                        break;
                    case ACCOUNTPASS:
                        expression = DbUtil.createStringExpression(activeKitVoffice.accountPass, filter);
                        break;
                    case ACTIONCODE:
                        expression = DbUtil.createStringExpression(activeKitVoffice.actionCode, filter);
                        break;
                    case ACTIVEKITVOFFICEID:
                        expression = DbUtil.createLongExpression(activeKitVoffice.activeKitVofficeId, filter);
                        break;
                    case CHANGEMODELTRANSID:
                        expression = DbUtil.createLongExpression(activeKitVoffice.changeModelTransId, filter);
                        break;
                    case CREATEDATE:
                        expression = DbUtil.createDateExpression(activeKitVoffice.createDate, filter);
                        break;
                    case CREATEUSERID:
                        expression = DbUtil.createLongExpression(activeKitVoffice.createUserId, filter);
                        break;
                    case LASTMODIFY:
                        expression = DbUtil.createDateExpression(activeKitVoffice.lastModify, filter);
                        break;
                    case NOTE:
                        expression = DbUtil.createStringExpression(activeKitVoffice.note, filter);
                        break;
                    case PREFIXTEMPLATE:
                        expression = DbUtil.createStringExpression(activeKitVoffice.prefixTemplate, filter);
                        break;
                    case SIGNUSERLIST:
                        expression = DbUtil.createStringExpression(activeKitVoffice.signUserList, filter);
                        break;
                    case STATUS:
                        expression = DbUtil.createStringExpression(activeKitVoffice.status, filter);
                        break;
                    case STOCKTRANSACTIONID:
                        expression = DbUtil.createLongExpression(activeKitVoffice.stockTransActionId, filter);
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

    public ActiveKitVoffice findByChangeModelTransId(Long changeModelTransId) throws Exception {
        StringBuilder sql = new StringBuilder(" ");
        sql.append("SELECT * ");
        sql.append("  FROM bccs_im.active_kit_voffice");
        sql.append(" WHERE change_model_trans_id = #id AND status = 0");
        Query query = em.createNativeQuery(sql.toString(), ActiveKitVoffice.class);
        query.setParameter("id", changeModelTransId);
        List lst = query.getResultList();
        if (!DataUtil.isNullOrEmpty(lst)) {
            return (ActiveKitVoffice) lst.get(0);
        }
        return null;
    }
}