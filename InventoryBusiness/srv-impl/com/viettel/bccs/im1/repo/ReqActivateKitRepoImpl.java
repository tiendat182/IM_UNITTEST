package com.viettel.bccs.im1.repo;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.im1.model.ReqActivateKit.COLUMNS;
import com.viettel.bccs.im1.model.QReqActivateKit;
import java.util.List;
import com.viettel.fw.common.util.DbUtil;
import org.apache.log4j.Logger;
public class ReqActivateKitRepoImpl implements ReqActivateKitRepoCustom {

    public static final Logger logger = Logger.getLogger(ReqActivateKitRepoCustom.class);

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QReqActivateKit reqActivateKit = QReqActivateKit.reqActivateKit;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case CREATEDATE:
                        expression = DbUtil.createDateExpression(reqActivateKit.createDate, filter); 
                        break;
                    case ERRORDESCRIPTION:
                        expression = DbUtil.createStringExpression(reqActivateKit.errorDescription, filter); 
                        break;
                    case FROMSERIAL:
                        expression = DbUtil.createStringExpression(reqActivateKit.fromSerial, filter); 
                        break;
                    case PROCESSCOUNT:
                        expression = DbUtil.createLongExpression(reqActivateKit.processCount, filter); 
                        break;
                    case PROCESSDATE:
                        expression = DbUtil.createDateExpression(reqActivateKit.processDate, filter); 
                        break;
                    case PROCESSSTATUS:
                        expression = DbUtil.createLongExpression(reqActivateKit.processStatus, filter); 
                        break;
                    case REQID:
                        expression = DbUtil.createLongExpression(reqActivateKit.reqId, filter); 
                        break;
                    case SALETRANSDATE:
                        expression = DbUtil.createDateExpression(reqActivateKit.saleTransDate, filter); 
                        break;
                    case SALETRANSID:
                        expression = DbUtil.createLongExpression(reqActivateKit.saleTransId, filter); 
                        break;
                    case SALETRANSTYPE:
                        expression = DbUtil.createLongExpression(reqActivateKit.saleTransType, filter); 
                        break;
                    case SALETYPE:
                        expression = DbUtil.createLongExpression(reqActivateKit.saleType, filter); 
                        break;
                    case SHOPID:
                        expression = DbUtil.createLongExpression(reqActivateKit.shopId, filter); 
                        break;
                    case STAFFID:
                        expression = DbUtil.createLongExpression(reqActivateKit.staffId, filter); 
                        break;
                    case TOSERIAL:
                        expression = DbUtil.createStringExpression(reqActivateKit.toSerial, filter); 
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

}