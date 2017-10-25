package com.viettel.bccs.sale.repo;

import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.sale.dto.SaleTransDetailDTO.COLUMNS;
import com.viettel.bccs.sale.model.QSaleTransDetail;
import com.viettel.bccs.sale.model.SaleTransDetail;
import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;

public class SaleTransDetailRepoImpl implements SaleTransDetailRepoCustom {

    public static final Logger logger = Logger.getLogger(SaleTransDetailRepoCustom.class);
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_SALE)
    private EntityManager em;

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QSaleTransDetail saleTransDetail = QSaleTransDetail.saleTransDetail;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case ACCOUNT:
                        expression = DbUtil.createStringExpression(saleTransDetail.account, filter);
                        break;
                    case ACCOUNTINGMODELCODE:
                        expression = DbUtil.createStringExpression(saleTransDetail.accountingModelCode, filter);
                        break;
                    case ACCOUNTINGMODELNAME:
                        expression = DbUtil.createStringExpression(saleTransDetail.accountingModelName, filter);
                        break;
                    case AMOUNT:
                        expression = DbUtil.createLongExpression(saleTransDetail.amount, filter);
                        break;
                    case DELIVERDATE:
                        expression = DbUtil.createDateExpression(saleTransDetail.deliverDate, filter);
                        break;
                    case DELIVERSTATUS:
                        expression = DbUtil.createStringExpression(saleTransDetail.deliverStatus, filter);
                        break;
                    case DISCOUNTAMOUNT:
                        expression = DbUtil.createLongExpression(saleTransDetail.discountAmount, filter);
                        break;
                    case DISCOUNTID:
                        expression = DbUtil.createLongExpression(saleTransDetail.discountId, filter);
                        break;
                    case EXPIREDDATE:
                        expression = DbUtil.createDateExpression(saleTransDetail.expiredDate, filter);
                        break;
                    case INTRANSID:
                        expression = DbUtil.createStringExpression(saleTransDetail.inTransId, filter);
                        break;
                    case MODELPROGRAMID:
                        expression = DbUtil.createLongExpression(saleTransDetail.modelProgramId, filter);
                        break;
                    case MODELPROGRAMNAME:
                        expression = DbUtil.createStringExpression(saleTransDetail.modelProgramName, filter);
                        break;
                    case NOTE:
                        expression = DbUtil.createStringExpression(saleTransDetail.note, filter);
                        break;
                    case POSID:
                        expression = DbUtil.createLongExpression(saleTransDetail.posId, filter);
                        break;
                    case PRICE:
                        expression = DbUtil.createLongExpression(saleTransDetail.price, filter);
                        break;
                    case PRICEID:
                        expression = DbUtil.createLongExpression(saleTransDetail.priceId, filter);
                        break;
                    case PRICEVAT:
                        expression = DbUtil.createLongExpression(saleTransDetail.priceVat, filter);
                        break;
                    case PROMOTIONAMOUNT:
                        expression = DbUtil.createLongExpression(saleTransDetail.promotionAmount, filter);
                        break;
                    case PROMOTIONID:
                        expression = DbUtil.createLongExpression(saleTransDetail.promotionId, filter);
                        break;
                    case QUANTITY:
                        expression = DbUtil.createLongExpression(saleTransDetail.quantity, filter);
                        break;
                    case REVENUETYPE:
                        expression = DbUtil.createStringExpression(saleTransDetail.revenueType, filter);
                        break;
                    case SALESERVICESCODE:
                        expression = DbUtil.createStringExpression(saleTransDetail.saleServicesCode, filter);
                        break;
                    case SALESERVICESID:
                        expression = DbUtil.createLongExpression(saleTransDetail.saleServicesId, filter);
                        break;
                    case SALESERVICESNAME:
                        expression = DbUtil.createStringExpression(saleTransDetail.saleServicesName, filter);
                        break;
                    case SALESERVICESPRICE:
                        expression = DbUtil.createLongExpression(saleTransDetail.saleServicesPrice, filter);
                        break;
                    case SALESERVICESPRICEID:
                        expression = DbUtil.createLongExpression(saleTransDetail.saleServicesPriceId, filter);
                        break;
                    case SALESERVICESPRICEVAT:
                        expression = DbUtil.createLongExpression(saleTransDetail.saleServicesPriceVat, filter);
                        break;
                    case SALESERVICESPROGRAMID:
                        expression = DbUtil.createLongExpression(saleTransDetail.saleServicesProgramId, filter);
                        break;
                    case SALESERVICESPROGRAMNAME:
                        expression = DbUtil.createStringExpression(saleTransDetail.saleServicesProgramName, filter);
                        break;
                    case SALETRANSDATE:
                        expression = DbUtil.createDateExpression(saleTransDetail.saleTransDate, filter);
                        break;
                    case SALETRANSDETAILID:
                        expression = DbUtil.createLongExpression(saleTransDetail.saleTransDetailId, filter);
                        break;
                    case SALETRANSID:
                        expression = DbUtil.createLongExpression(saleTransDetail.saleTransId, filter);
                        break;
                    case STATEID:
                        expression = DbUtil.createLongExpression(saleTransDetail.stateId, filter);
                        break;
                    case STOCKMODELCODE:
                        expression = DbUtil.createStringExpression(saleTransDetail.stockModelCode, filter);
                        break;
                    case STOCKMODELID:
                        expression = DbUtil.createLongExpression(saleTransDetail.stockModelId, filter);
                        break;
                    case STOCKMODELNAME:
                        expression = DbUtil.createStringExpression(saleTransDetail.stockModelName, filter);
                        break;
                    case STOCKTYPECODE:
                        expression = DbUtil.createStringExpression(saleTransDetail.stockTypeCode, filter);
                        break;
                    case STOCKTYPEID:
                        expression = DbUtil.createLongExpression(saleTransDetail.stockTypeId, filter);
                        break;
                    case STOCKTYPENAME:
                        expression = DbUtil.createStringExpression(saleTransDetail.stockTypeName, filter);
                        break;
                    case SUPPLYMETHOD:
                        expression = DbUtil.createStringExpression(saleTransDetail.supplyMethod, filter);
                        break;
                    case SUPPLYMONTH:
                        expression = DbUtil.createLongExpression(saleTransDetail.supplyMonth, filter);
                        break;
                    case SUPPLYPROGRAM:
                        expression = DbUtil.createStringExpression(saleTransDetail.supplyProgram, filter);
                        break;
                    case TRANSFERGOOD:
                        expression = DbUtil.createStringExpression(saleTransDetail.transferGood, filter);
                        break;
                    case UPDATESTOCKTYPE:
                        expression = DbUtil.createStringExpression(saleTransDetail.updateStockType, filter);
                        break;
                    case USERDELIVER:
                        expression = DbUtil.createLongExpression(saleTransDetail.userDeliver, filter);
                        break;
                    case USERUPDATE:
                        expression = DbUtil.createLongExpression(saleTransDetail.userUpdate, filter);
                        break;
                    case VAT:
                        expression = DbUtil.createLongExpression(saleTransDetail.vat, filter);
                        break;
                    case VATAMOUNT:
                        expression = DbUtil.createLongExpression(saleTransDetail.vatAmount, filter);
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
    public List<SaleTransDetail> findBySaleTransId(Long saleTransId) {
        StringBuilder builder = new StringBuilder();
        builder.append(" SELECT   * ");
        builder.append("    FROM   BCCS_SALE.SALE_TRANS_DETAIL");
        builder.append("   WHERE   sale_trans_id = #saleTransId");
        Query query = em.createNativeQuery(builder.toString(), SaleTransDetail.class);
        query.setParameter("saleTransId", saleTransId);
        return query.getResultList();
    }

    @Override
    public List<SaleTransDetail> findSaleTransDetails(Long saleTransId, Long prodOfferId, Date saleTransDate) {
        StringBuilder builder = new StringBuilder();
        builder.append(" SELECT   * ");
        builder.append("    FROM   BCCS_SALE.SALE_TRANS_DETAIL");
        builder.append("   WHERE   sale_trans_id = #saleTransId");
        builder.append("        AND   stock_model_id = #prodOfferId");
        builder.append("        AND   sale_trans_date >= trunc(#saleTransDate)");
        Query query = em.createNativeQuery(builder.toString(), SaleTransDetail.class);
        query.setParameter("saleTransId", saleTransId);
        query.setParameter("prodOfferId", prodOfferId);
        query.setParameter("saleTransDate", saleTransDate);
        return query.getResultList();
    }
}