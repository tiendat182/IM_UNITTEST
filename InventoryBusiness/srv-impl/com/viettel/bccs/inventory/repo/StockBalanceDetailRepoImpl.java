package com.viettel.bccs.inventory.repo;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.StockBalanceDetailDTO;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.model.StockBalanceDetail.COLUMNS;
import com.viettel.bccs.inventory.model.QStockBalanceDetail;

import java.util.Date;
import java.util.List;

import com.viettel.fw.common.util.DbUtil;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public class StockBalanceDetailRepoImpl implements StockBalanceDetailRepoCustom {


    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;
    public static final Logger logger = Logger.getLogger(StockBalanceDetailRepoCustom.class);

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QStockBalanceDetail stockBalanceDetail = QStockBalanceDetail.stockBalanceDetail;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case CREATEDATETIME:
                        expression = DbUtil.createDateExpression(stockBalanceDetail.createDatetime, filter);
                        break;
                    case PRODOFFERID:
                        expression = DbUtil.createLongExpression(stockBalanceDetail.prodOfferId, filter);
                        break;
                    case QUANTITY:
                        expression = DbUtil.createLongExpression(stockBalanceDetail.quantity, filter);
                        break;
                    case QUANTITYBCCS:
                        expression = DbUtil.createLongExpression(stockBalanceDetail.quantityBccs, filter);
                        break;
                    case QUANTITYERP:
                        expression = DbUtil.createLongExpression(stockBalanceDetail.quantityErp, filter);
                        break;
                    case QUANTITYREAL:
                        expression = DbUtil.createLongExpression(stockBalanceDetail.quantityReal, filter);
                        break;
                    case STOCKBALANCEDETAILID:
                        expression = DbUtil.createLongExpression(stockBalanceDetail.stockBalanceDetailId, filter);
                        break;
                    case STOCKREQUESTID:
                        expression = DbUtil.createLongExpression(stockBalanceDetail.stockRequestId, filter);
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
    public List<StockBalanceDetailDTO> getListStockBalanceDetailDTO(Long requestID) throws Exception {
        StringBuilder builder = new StringBuilder("");
        builder.append("select d.stock_balance_detail_id,")
                .append("   d.stock_request_id,")
                .append("   d.prod_offer_id,")
                .append("   d.quantity_bccs,")
                .append("   d.quantity_real,")
                .append("   d.quantity_erp,")
                .append("   d.quantity,")
                .append("   d.create_datetime,")
                .append("   p.code ||' - '||p.name,")
                .append("   p.check_serial")
                .append(" from stock_balance_detail d, product_offering p")
                .append(" where d.prod_offer_id=p.prod_offer_id")
                .append(" and d.stock_request_id=?");
        Query query = em.createNativeQuery(builder.toString());
        query.setParameter(1, requestID);
        List<StockBalanceDetailDTO> result = Lists.newArrayList();
        List<Object[]> objects = query.getResultList();
        if (!DataUtil.isNullOrEmpty(objects)) {
            for (Object[] data : objects) {
                int index = 0;
                StockBalanceDetailDTO detailDTO = new StockBalanceDetailDTO();
                detailDTO.setStockBalanceDetailId(DataUtil.safeToLong(data[index++]));
                detailDTO.setStockRequestId(DataUtil.safeToLong(data[index++]));
                detailDTO.setProdOfferId(DataUtil.safeToLong(data[index++]));
                detailDTO.setQuantityBccs(DataUtil.safeToLong(data[index++]));
                detailDTO.setQuantityReal(DataUtil.safeToLong(data[index++]));
                detailDTO.setQuantityErp(DataUtil.safeToLong(data[index++]));
                detailDTO.setQuantity(DataUtil.safeToLong(data[index++]));
                detailDTO.setCreateDatetime((Date) data[index++]);
                detailDTO.setProdOfferName(DataUtil.safeToString(data[index++]));
                detailDTO.setCheckSerial(DataUtil.safeToString(data[index]));
                result.add(detailDTO);
            }
        }
        return result;
    }
}