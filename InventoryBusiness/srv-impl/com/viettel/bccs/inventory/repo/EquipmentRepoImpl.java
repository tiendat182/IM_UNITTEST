package com.viettel.bccs.inventory.repo;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.model.Equipment.COLUMNS;
import com.viettel.bccs.inventory.model.QEquipment;
import java.util.List;
import com.viettel.fw.common.util.DbUtil;
import org.apache.log4j.Logger;
public class EquipmentRepoImpl implements EquipmentRepoCustom {

    public static final Logger logger = Logger.getLogger(EquipmentRepoCustom.class);

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QEquipment equipment = QEquipment.equipment;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case CODE:
                        expression = DbUtil.createStringExpression(equipment.code, filter); 
                        break;
                    case DESCRIPTION:
                        expression = DbUtil.createStringExpression(equipment.description, filter); 
                        break;
                    case EQUIPMENTID:
                        expression = DbUtil.createLongExpression(equipment.equipmentId, filter); 
                        break;
                    case EQUIPMENTTYPE:
                        expression = DbUtil.createStringExpression(equipment.equipmentType, filter); 
                        break;
                    case EQUIPMENTVENDORID:
                        expression = DbUtil.createLongExpression(equipment.equipmentVendorId, filter); 
                        break;
                    case NAME:
                        expression = DbUtil.createStringExpression(equipment.name, filter); 
                        break;
                    case STATUS:
                        expression = DbUtil.createStringExpression(equipment.status, filter); 
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