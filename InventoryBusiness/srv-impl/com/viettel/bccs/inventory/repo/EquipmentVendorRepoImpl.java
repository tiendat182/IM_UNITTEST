package com.viettel.bccs.inventory.repo;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.model.EquipmentVendor.COLUMNS;
import com.viettel.bccs.inventory.model.QEquipmentVendor;
import java.util.List;
import com.viettel.fw.common.util.DbUtil;
import org.apache.log4j.Logger;
public class EquipmentVendorRepoImpl implements EquipmentVendorRepoCustom {

    public static final Logger logger = Logger.getLogger(EquipmentVendorRepoCustom.class);

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QEquipmentVendor equipmentVendor = QEquipmentVendor.equipmentVendor;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case DESCRIPTION:
                        expression = DbUtil.createStringExpression(equipmentVendor.description, filter); 
                        break;
                    case EQUIPMENTVENDORID:
                        expression = DbUtil.createLongExpression(equipmentVendor.equipmentVendorId, filter); 
                        break;
                    case STATUS:
                        expression = DbUtil.createStringExpression(equipmentVendor.status, filter); 
                        break;
                    case VENDORCODE:
                        expression = DbUtil.createStringExpression(equipmentVendor.vendorCode, filter); 
                        break;
                    case VENDORNAME:
                        expression = DbUtil.createStringExpression(equipmentVendor.vendorName, filter); 
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