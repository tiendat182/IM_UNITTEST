package com.viettel.bccs.inventory.repo;

import com.google.common.collect.Lists;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.dto.StockTransFullOfflineDTO;
import com.viettel.bccs.inventory.model.QStockTransFullOffline;
import com.viettel.bccs.inventory.model.StockTransFullOffline;
import com.viettel.bccs.inventory.model.StockTransFullOffline.COLUMNS;
import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.common.util.mapper.BaseMapper;
import org.apache.log4j.Logger;

import java.util.List;

public class StockTransFullOfflineRepoImpl implements StockTransFullOfflineRepoCustom {

    public static final Logger logger = Logger.getLogger(StockTransFullOfflineRepoCustom.class);
    private final BaseMapper<StockTransFullOffline, StockTransFullOfflineDTO> mapper = new BaseMapper<>(StockTransFullOffline.class, StockTransFullOfflineDTO.class);

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QStockTransFullOffline stockTransFullOffline = QStockTransFullOffline.stockTransFullOffline;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case ACTIONCODE:
                        expression = DbUtil.createStringExpression(stockTransFullOffline.actionCode, filter); 
                        break;
                    case ACTIONSTAFFID:
                        expression = DbUtil.createLongExpression(stockTransFullOffline.actionStaffId, filter); 
                        break;
                    case ACTIONSTATUS:
                        expression = DbUtil.createStringExpression(stockTransFullOffline.actionStatus, filter); 
                        break;
                    case BASISPRICE:
                        expression = DbUtil.createLongExpression(stockTransFullOffline.basisPrice, filter); 
                        break;
                    case CHECKDEPOSIT:
                        expression = DbUtil.createLongExpression(stockTransFullOffline.checkDeposit, filter); 
                        break;
                    case CHECKSERIAL:
                        expression = DbUtil.createLongExpression(stockTransFullOffline.checkSerial, filter); 
                        break;
                    case CREATEDATETIME:
                        expression = DbUtil.createDateExpression(stockTransFullOffline.createDatetime, filter); 
                        break;
                    case CREATEUSER:
                        expression = DbUtil.createStringExpression(stockTransFullOffline.createUser, filter); 
                        break;
                    case DEPOSITSTATUS:
                        expression = DbUtil.createStringExpression(stockTransFullOffline.depositStatus, filter); 
                        break;
                    case FROMOWNERID:
                        expression = DbUtil.createLongExpression(stockTransFullOffline.fromOwnerId, filter); 
                        break;
                    case FROMOWNERTYPE:
                        expression = DbUtil.createLongExpression(stockTransFullOffline.fromOwnerType, filter); 
                        break;
                    case NOTE:
                        expression = DbUtil.createStringExpression(stockTransFullOffline.note, filter); 
                        break;
                    case PAYSTATUS:
                        expression = DbUtil.createStringExpression(stockTransFullOffline.payStatus, filter); 
                        break;
                    case PRODOFFERCODE:
                        expression = DbUtil.createStringExpression(stockTransFullOffline.prodOfferCode, filter); 
                        break;
                    case PRODOFFERID:
                        expression = DbUtil.createLongExpression(stockTransFullOffline.prodOfferId, filter); 
                        break;
                    case PRODOFFERNAME:
                        expression = DbUtil.createStringExpression(stockTransFullOffline.prodOfferName, filter); 
                        break;
                    case PRODUCTOFFERTYPEID:
                        expression = DbUtil.createLongExpression(stockTransFullOffline.productOfferTypeId, filter); 
                        break;
                    case PRODUCTOFFERTYPENAME:
                        expression = DbUtil.createStringExpression(stockTransFullOffline.productOfferTypeName, filter); 
                        break;
                    case QUANTITY:
                        expression = DbUtil.createLongExpression(stockTransFullOffline.quantity, filter); 
                        break;
                    case REASONID:
                        expression = DbUtil.createLongExpression(stockTransFullOffline.reasonId, filter); 
                        break;
                    case STATEID:
                        expression = DbUtil.createLongExpression(stockTransFullOffline.stateId, filter); 
                        break;
                    case STATENAME:
                        expression = DbUtil.createStringExpression(stockTransFullOffline.stateName, filter); 
                        break;
                    case STATUS:
                        expression = DbUtil.createStringExpression(stockTransFullOffline.status, filter); 
                        break;
                    case STOCKTRANSACTIONID:
                        expression = DbUtil.createLongExpression(stockTransFullOffline.stockTransActionId, filter); 
                        break;
                    case STOCKTRANSDETAILID:
                        expression = DbUtil.createLongExpression(stockTransFullOffline.stockTransDetailId, filter); 
                        break;
                    case STOCKTRANSID:
                        expression = DbUtil.createLongExpression(stockTransFullOffline.stockTransId, filter); 
                        break;
                    case STOCKTRANSTYPE:
                        expression = DbUtil.createStringExpression(stockTransFullOffline.stockTransType, filter); 
                        break;
                    case TOOWNERID:
                        expression = DbUtil.createLongExpression(stockTransFullOffline.toOwnerId, filter); 
                        break;
                    case TOOWNERTYPE:
                        expression = DbUtil.createLongExpression(stockTransFullOffline.toOwnerType, filter); 
                        break;
                    case UNIT:
                        expression = DbUtil.createStringExpression(stockTransFullOffline.unit, filter); 
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
    public List<StockTransFullOfflineDTO> findByActionId(Long actionId) {
        StockTransFullOfflineRepo repoStockTransFullOfflineRepo = (StockTransFullOfflineRepo) this;
        List<StockTransFullOffline> listStockTransFullOfflines = Lists.newArrayList(repoStockTransFullOfflineRepo.findAll(repoStockTransFullOfflineRepo.toPredicate(Lists.newArrayList(
                new FilterRequest(StockTransFullOffline.COLUMNS.STOCKTRANSACTIONID.name(), FilterRequest.Operator.EQ, actionId)
        ))));
        List<StockTransFullOfflineDTO> listStockTransFullOfflineDTO = mapper.toDtoBean(listStockTransFullOfflines);

        return null;
    }

}