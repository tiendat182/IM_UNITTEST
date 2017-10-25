package com.viettel.bccs.im1.repo;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.im1.model.VcRequest.COLUMNS;
import com.viettel.bccs.im1.model.QVcRequest;
import java.util.List;
import com.viettel.fw.common.util.DbUtil;
import org.apache.log4j.Logger;
public class VcRequestRepoImpl implements VcRequestRepoCustom {

    public static final Logger logger = Logger.getLogger(VcRequestRepoCustom.class);

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QVcRequest vcRequest = QVcRequest.vcRequest;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case CREATETIME:
                        expression = DbUtil.createDateExpression(vcRequest.createTime, filter); 
                        break;
                    case FROMSERIAL:
                        expression = DbUtil.createStringExpression(vcRequest.fromSerial, filter); 
                        break;
                    case LASTPROCESSTIME:
                        expression = DbUtil.createDateExpression(vcRequest.lastProcessTime, filter); 
                        break;
                    case RANGEID:
                        expression = DbUtil.createLongExpression(vcRequest.rangeId, filter); 
                        break;
                    case REQUESTID:
                        expression = DbUtil.createLongExpression(vcRequest.requestId, filter); 
                        break;
                    case REQUESTTYPE:
                        expression = DbUtil.createLongExpression(vcRequest.requestType, filter); 
                        break;
                    case SHOPID:
                        expression = DbUtil.createLongExpression(vcRequest.shopId, filter); 
                        break;
                    case STAFFID:
                        expression = DbUtil.createLongExpression(vcRequest.staffId, filter); 
                        break;
                    case STARTPROCESSTIME:
                        expression = DbUtil.createDateExpression(vcRequest.startProcessTime, filter); 
                        break;
                    case STATUS:
                        expression = DbUtil.createLongExpression(vcRequest.status, filter); 
                        break;
                    case SYSCREATETIME:
                        expression = DbUtil.createDateExpression(vcRequest.sysCreateTime, filter); 
                        break;
                    case SYSPROCESSTIME:
                        expression = DbUtil.createDateExpression(vcRequest.sysProcessTime, filter); 
                        break;
                    case TOSERIAL:
                        expression = DbUtil.createStringExpression(vcRequest.toSerial, filter); 
                        break;
                    case TRANSID:
                        expression = DbUtil.createLongExpression(vcRequest.transId, filter); 
                        break;
                    case USERID:
                        expression = DbUtil.createStringExpression(vcRequest.userId, filter); 
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