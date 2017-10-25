package com.viettel.bccs.inventory.repo;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.model.PartnerContract.COLUMNS;
import com.viettel.bccs.inventory.model.QPartnerContract;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.viettel.fw.common.util.DbUtil;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public class PartnerContractRepoImpl implements PartnerContractRepoCustom {

    public static final Logger logger = Logger.getLogger(PartnerContractRepoCustom.class);
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QPartnerContract partnerContract = QPartnerContract.partnerContract;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case ACTIONCODE:
                        expression = DbUtil.createStringExpression(partnerContract.actionCode, filter); 
                        break;
                    case CONTRACTCODE:
                        expression = DbUtil.createStringExpression(partnerContract.contractCode, filter); 
                        break;
                    case CONTRACTDATE:
                        expression = DbUtil.createDateExpression(partnerContract.contractDate, filter); 
                        break;
                    case CONTRACTSTATUS:
                        expression = DbUtil.createShortExpression(partnerContract.contractStatus, filter); 
                        break;
                    case CREATEDATE:
                        expression = DbUtil.createDateExpression(partnerContract.createDate, filter); 
                        break;
                    case CURRENCYRATE:
                        expression = DbUtil.createLongExpression(partnerContract.currencyRate, filter); 
                        break;
                    case CURRENCYTYPE:
                        expression = DbUtil.createStringExpression(partnerContract.currencyType, filter); 
                        break;
                    case DELIVERYLOCATION:
                        expression = DbUtil.createStringExpression(partnerContract.deliveryLocation, filter); 
                        break;
                    case IMPORTDATE:
                        expression = DbUtil.createDateExpression(partnerContract.importDate, filter); 
                        break;
                    case KCSDATE:
                        expression = DbUtil.createDateExpression(partnerContract.kcsDate, filter); 
                        break;
                    case KCSNO:
                        expression = DbUtil.createStringExpression(partnerContract.kcsNo, filter); 
                        break;
                    case LASTMODIFIED:
                        expression = DbUtil.createDateExpression(partnerContract.lastModified, filter); 
                        break;
                    case ORDERCODE:
                        expression = DbUtil.createStringExpression(partnerContract.orderCode, filter); 
                        break;
                    case PARTNERCONTRACTID:
                        expression = DbUtil.createLongExpression(partnerContract.partnerContractId, filter); 
                        break;
                    case PARTNERID:
                        expression = DbUtil.createLongExpression(partnerContract.partnerId, filter); 
                        break;
                    case POCODE:
                        expression = DbUtil.createStringExpression(partnerContract.poCode, filter); 
                        break;
                    case PODATE:
                        expression = DbUtil.createDateExpression(partnerContract.poDate, filter); 
                        break;
                    case PRODOFFERID:
                        expression = DbUtil.createLongExpression(partnerContract.prodOfferId, filter); 
                        break;
                    case REQUESTDATE:
                        expression = DbUtil.createDateExpression(partnerContract.requestDate, filter); 
                        break;
                    case STARTDATEWARRANTY:
                        expression = DbUtil.createDateExpression(partnerContract.startDateWarranty, filter); 
                        break;
                    case STOCKTRANSID:
                        expression = DbUtil.createLongExpression(partnerContract.stockTransId, filter); 
                        break;
                    case UNITPRICE:
                        expression = DbUtil.createLongExpression(partnerContract.unitPrice, filter); 
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

    public List<String> searchContract(String contractCode, String prodOfferCode, Date fromDate, Date toDate) throws Exception {

        StringBuilder sql = new StringBuilder("");
        sql.append(" SELECT   distinct pc.contract_code ");
        sql.append("FROM   partner_contract pc,");
        sql.append("		 partner_contract_detail pcd,");
        sql.append("		 product_offering po");
        sql.append(" WHERE       1 = 1");
        sql.append("		 AND pc.partner_contract_id = pcd.partner_contract_id ");
        sql.append("		 AND pcd.prod_offer_id = po.prod_offer_id ");
        sql.append("		 AND po.product_offer_type_id=11 ");
        sql.append("		 AND po.stock_model_type=2 ");
        sql.append("		 AND pc.contract_code IS NOT NULL");
        sql.append("		 AND pc.contract_date IS NOT NULL");
        if (!DataUtil.isNullOrEmpty(contractCode)) {
            sql.append("		 AND lower(pc.contract_code) LIKE #contractCode");
        }
        if (!DataUtil.isNullOrEmpty(prodOfferCode)) {
            sql.append("		 AND lower(po.code) LIKE #prodOfferCode");
        }
        sql.append("		 AND pc.contract_date >= TRUNC (#fromDate)");
        sql.append("		 AND pc.contract_date < TRUNC (#toDate) + 1");


        Query query = em.createNativeQuery(sql.toString());
        if (!DataUtil.isNullOrEmpty(contractCode)) {
            query.setParameter("contractCode", "%" + contractCode.trim().toLowerCase() + "%");
        }
        if (!DataUtil.isNullOrEmpty(prodOfferCode)) {
            query.setParameter("prodOfferCode", "%" + prodOfferCode.trim().toLowerCase() + "%");
        }
        query.setParameter("fromDate", fromDate);
        query.setParameter("toDate", toDate);

        List<String> lst = query.getResultList();
        if (!DataUtil.isNullOrEmpty(lst)) {
            return lst;
        }
        return new ArrayList<>();
    }

}