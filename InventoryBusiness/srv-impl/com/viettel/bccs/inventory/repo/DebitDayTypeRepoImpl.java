package com.viettel.bccs.inventory.repo;

import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.DebitDayTypeDTO;
import com.viettel.bccs.inventory.model.DebitDayType;
import com.viettel.bccs.inventory.model.DebitDayType.COLUMNS;
import com.viettel.bccs.inventory.model.QDebitDayType;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.common.util.mapper.BaseMapper;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DebitDayTypeRepoImpl implements DebitDayTypeRepoCustom {
    private final BaseMapper<DebitDayType, DebitDayTypeDTO> mapper = new BaseMapper(DebitDayType.class, DebitDayTypeDTO.class);
    public static final Logger logger = Logger.getLogger(DebitDayTypeRepoCustom.class);
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager emIM;

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QDebitDayType debitDayType = QDebitDayType.debitDayType1;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case CREATEDATE:
                        expression = DbUtil.createDateExpression(debitDayType.createDate, filter);
                        break;
                    case CREATEUSER:
                        expression = DbUtil.createStringExpression(debitDayType.createUser, filter);
                        break;
                    case DEBITDAYTYPE:
                        expression = DbUtil.createStringExpression(debitDayType.debitDayType, filter);
                        break;
                    case ENDDATE:
                        expression = DbUtil.createDateExpression(debitDayType.endDate, filter);
                        break;
                    case FILENAME:
                        expression = DbUtil.createStringExpression(debitDayType.fileName, filter);
                        break;
                    case ID:
                        expression = DbUtil.createLongExpression(debitDayType.id, filter);
                        break;
                    case LASTUPDATETIME:
                        expression = DbUtil.createDateExpression(debitDayType.lastUpdateTime, filter);
                        break;
                    case LASTUPDATEUSER:
                        expression = DbUtil.createStringExpression(debitDayType.lastUpdateUser, filter);
                        break;
                    case STARTDATE:
                        expression = DbUtil.createDateExpression(debitDayType.startDate, filter);
                        break;
                    case STATUS:
                        expression = DbUtil.createStringExpression(debitDayType.status, filter);
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
    public List<DebitDayTypeDTO> searchDebitDayType(DebitDayTypeDTO debitDayType) {
        StringBuilder builder = new StringBuilder("SELECT c.*" +
                "FROM   debit_day_type c " +
                "WHERE  c.status = '1' ");
        if (!DataUtil.isNullOrEmpty(debitDayType.getDebitDayType())) {
            builder.append("and c.debit_day_type =#debit_day_type ");
        }
        if (debitDayType.getStartDate() != null) {
            builder.append(" and trunc(c.start_date) >= trunc(#start_date) ");
        }
        if (debitDayType.getEndDate() != null) {
            builder.append(" and trunc(c.end_date) <= trunc(#end_date) ");
        }
        if (!DataUtil.isNullOrEmpty(debitDayType.getDebitLevel())) {
            builder.append(" and c.debit_day_type in(select dl.debit_day_type from debit_level dl where debit_level=#debitLevel) ");
        }
        builder.append(" order by last_update_time desc ");
        Query query = emIM.createNativeQuery(builder.toString(), DebitDayType.class);
        if (!DataUtil.isNullOrEmpty(debitDayType.getDebitDayType())) {
            query.setParameter("debit_day_type", debitDayType.getDebitDayType());
        }
        if (debitDayType.getStartDate() != null) {
            query.setParameter("start_date", debitDayType.getStartDate());
        }
        if (debitDayType.getEndDate() != null) {
            query.setParameter("end_date", debitDayType.getEndDate());
        }
        if (!DataUtil.isNullOrEmpty(debitDayType.getDebitLevel())) {
            query.setParameter("debitLevel", debitDayType.getDebitLevel());
        }
        List<DebitDayType> result = query.getResultList();
        if (!DataUtil.isNullOrEmpty(result)) {
            return mapper.toDtoBean(result);
        }
        return new ArrayList<DebitDayTypeDTO>();

    }

    @Override
    public byte[] getAttachFileContent(Long id) throws Exception {
        StringBuilder builder = new StringBuilder("select file_content from debit_day_type where id= #requestId");
        Query query = emIM.createNativeQuery(builder.toString());
        query.setParameter("requestId", id);
        List<Object> lstResult = query.getResultList();
        if (!DataUtil.isNullObject(lstResult.get(0))) {
            return (byte[]) lstResult.get(0);
        }
        return new byte[]{};
    }

}