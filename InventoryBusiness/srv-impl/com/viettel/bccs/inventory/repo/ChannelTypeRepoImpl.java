package com.viettel.bccs.inventory.repo;

import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.model.ChannelType.COLUMNS;
import com.viettel.bccs.inventory.model.QChannelType;
import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import org.apache.log4j.Logger;

import java.util.List;

public class ChannelTypeRepoImpl implements ChannelTypeRepoCustom {

    public static final Logger logger = Logger.getLogger(ChannelTypeRepoCustom.class);

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QChannelType channelType = QChannelType.channelType;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case ALLOWADDBATCH:
                        expression = DbUtil.createLongExpression(channelType.allowAddBatch, filter); 
                        break;
                    case CHANNELTYPEID:
                        expression = DbUtil.createLongExpression(channelType.channelTypeId, filter); 
                        break;
                    case CHECKCOMM:
                        expression = DbUtil.createStringExpression(channelType.checkComm, filter); 
                        break;
                    case CODE:
                        expression = DbUtil.createStringExpression(channelType.code, filter); 
                        break;
                    case CREATEDATETIME:
                        expression = DbUtil.createDateExpression(channelType.createDatetime, filter); 
                        break;
                    case CREATEUSER:
                        expression = DbUtil.createStringExpression(channelType.createUser, filter); 
                        break;
                    case DISCOUNTPOLICYDEFAUT:
                        expression = DbUtil.createStringExpression(channelType.discountPolicyDefaut, filter); 
                        break;
                    case GROUPCHANNELID:
                        expression = DbUtil.createLongExpression(channelType.groupChannelId, filter); 
                        break;
                    case GROUPCHANNELTYPEID:
                        expression = DbUtil.createLongExpression(channelType.groupChannelTypeId, filter); 
                        break;
                    case ISCOLLCHANNEL:
                        expression = DbUtil.createLongExpression(channelType.isCollChannel, filter); 
                        break;
                    case ISNOTBLANKCODE:
                        expression = DbUtil.createLongExpression(channelType.isNotBlankCode, filter); 
                        break;
                    case ISVHRCHANNEL:
                        expression = DbUtil.createLongExpression(channelType.isVhrChannel, filter); 
                        break;
                    case ISVTUNIT:
                        expression = DbUtil.createStringExpression(channelType.isVtUnit, filter); 
                        break;
                    case NAME:
                        expression = DbUtil.createStringExpression(channelType.name, filter); 
                        break;
                    case OBJECTTYPE:
                        expression = DbUtil.createStringExpression(channelType.objectType, filter); 
                        break;
                    case PRICEPOLICYDEFAUT:
                        expression = DbUtil.createStringExpression(channelType.pricePolicyDefaut, filter); 
                        break;
                    case STATUS:
                        expression = DbUtil.createLongExpression(channelType.status, filter); 
                        break;
                    case STOCKREPORTTEMPLATE:
                        expression = DbUtil.createStringExpression(channelType.stockReportTemplate, filter); 
                        break;
                    case STOCKTYPE:
                        expression = DbUtil.createLongExpression(channelType.stockType, filter); 
                        break;
                    case SUFFIXOBJECTCODE:
                        expression = DbUtil.createStringExpression(channelType.suffixObjectCode, filter); 
                        break;
                    case TOTALDEBIT:
                        expression = DbUtil.createLongExpression(channelType.totalDebit, filter); 
                        break;
                    case UPDATEBLANKCODEROLE:
                        expression = DbUtil.createStringExpression(channelType.updateBlankCodeRole, filter); 
                        break;
                    case UPDATEDATETIME:
                        expression = DbUtil.createDateExpression(channelType.updateDatetime, filter); 
                        break;
                    case UPDATEOBJECTINFOROLE:
                        expression = DbUtil.createStringExpression(channelType.updateObjectInfoRole, filter); 
                        break;
                    case UPDATESHOPROLE:
                        expression = DbUtil.createStringExpression(channelType.updateShopRole, filter); 
                        break;
                    case UPDATESTAFFOWNERROLE:
                        expression = DbUtil.createStringExpression(channelType.updateStaffOwnerRole, filter); 
                        break;
                    case UPDATEUSER:
                        expression = DbUtil.createStringExpression(channelType.updateUser, filter); 
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