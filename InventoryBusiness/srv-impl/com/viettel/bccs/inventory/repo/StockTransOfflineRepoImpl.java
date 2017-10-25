package com.viettel.bccs.inventory.repo;

import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.dto.SendSmsDTO;
import com.viettel.bccs.inventory.common.BaseMsg;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.PartnerContractDTO;
import com.viettel.bccs.inventory.model.*;
import com.viettel.bccs.inventory.model.StockTransOffline.COLUMNS;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

public class StockTransOfflineRepoImpl implements StockTransOfflineRepoCustom {

    public static final Logger logger = Logger.getLogger(StockTransOfflineRepoCustom.class);

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QStockTransOffline stockTransOffline = QStockTransOffline.stockTransOffline;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
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
    public List<StockTrans> getListStockTransOffline(int processTotal, int jobNumber) throws Exception {
        StringBuilder sqlQuery = new StringBuilder(" ");
        sqlQuery.append(" SELECT   sto.stock_trans_id stockTransId, sto.stock_trans_type stockTransType, sto.retry retry");
        sqlQuery.append(" FROM   bccs_im.stock_trans_offline sto, bccs_im.stock_trans st");
        sqlQuery.append(" WHERE   sto.stock_trans_id = st.stock_trans_id AND st.stock_trans_status = 0 AND (sto.time_to_retry IS NULL or sto.time_to_retry <= SYSDATE) AND MOD (sto.stock_trans_id, #totalProcess) = #jobNumber ORDER BY   sto.create_datetime ASC");
        Query query = em.createNativeQuery(sqlQuery.toString(), StockTrans.class);
        query.setParameter("totalProcess", processTotal);
        query.setParameter("jobNumber", jobNumber);
        return query.getResultList();
    }

    @Override
    public PartnerContract getPartnerContractByStockTransId(Long stockTransId) throws Exception {
        String sqlQuery = "SELECT partner_contract_id partnerContractId, stock_trans_id stockTransId, contract_code contractCode, po_code poCode FROM partner_contract  WHERE stock_trans_id = #stockTransId";
        Query query = em.createNativeQuery(sqlQuery, PartnerContract.class);
        query.setParameter("stockTransId", stockTransId);
        List lst = query.getResultList();
        if (!DataUtil.isNullOrEmpty(lst)) {
            return (PartnerContract) lst.get(0);
        }
        return null;
    }


    @Override
    public BaseMsg impStockToCompany(Long stockTransId, SendSmsDTO sendSmsDTO, PartnerContractDTO partnerContract) throws Exception {
        return null;
    }

}