package com.viettel.bccs.inventory.repo;

import com.google.common.collect.Lists;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.AreaDTO;
import com.viettel.bccs.inventory.model.Area;
import com.viettel.bccs.inventory.model.Area.COLUMNS;
import com.viettel.bccs.inventory.model.QArea;
import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.common.util.mapper.BaseMapper;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

public class AreaRepoImpl implements AreaRepoCustom {

    public static final Logger logger = Logger.getLogger(AreaRepoCustom.class);

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    private final BaseMapper<Area, AreaDTO> mapper = new BaseMapper(Area.class, AreaDTO.class);

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QArea area = QArea.area;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case AREACODE:
                        expression = DbUtil.createStringExpression(area.areaCode, filter);
                        break;
                    case AREAGROUP:
                        expression = DbUtil.createStringExpression(area.areaGroup, filter);
                        break;
                    case CENTER:
                        expression = DbUtil.createStringExpression(area.center, filter);
                        break;
                    case CREATEDATETIME:
                        expression = DbUtil.createDateExpression(area.createDatetime, filter);
                        break;
                    case CREATEUSER:
                        expression = DbUtil.createStringExpression(area.createUser, filter);
                        break;
                    case DISTRICT:
                        expression = DbUtil.createStringExpression(area.district, filter);
                        break;
                    case FULLNAME:
                        expression = DbUtil.createStringExpression(area.fullName, filter);
                        break;
                    case NAME:
                        expression = DbUtil.createStringExpression(area.name, filter);
                        break;
                    case PARENTCODE:
                        expression = DbUtil.createStringExpression(area.parentCode, filter);
                        break;
                    case PRECINCT:
                        expression = DbUtil.createStringExpression(area.precinct, filter);
                        break;
                    case PROVINCE:
                        expression = DbUtil.createStringExpression(area.province, filter);
                        break;
                    case PROVINCECODE:
                        expression = DbUtil.createStringExpression(area.provinceCode, filter);
                        break;
                    case PSTNCODE:
                        expression = DbUtil.createStringExpression(area.pstnCode, filter);
                        break;
                    case STATUS:
                        expression = DbUtil.createShortExpression(area.status, filter);
                        break;
                    case STREETBLOCK:
                        expression = DbUtil.createStringExpression(area.streetBlock, filter);
                        break;
                    case UPDATEDATETIME:
                        expression = DbUtil.createDateExpression(area.updateDatetime, filter);
                        break;
                    case UPDATEUSER:
                        expression = DbUtil.createStringExpression(area.updateUser, filter);
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
    public List<AreaDTO> getAllProvince() throws Exception {
        List<Area> lst ;
        StringBuilder strQuery = new StringBuilder();
        strQuery.append("select * from Area where district is null and precinct is null and status = 1 ORDER BY name");
        //execute query
        Query query = em.createNativeQuery(strQuery.toString(), Area.class);
        lst = query.getResultList();
        if (lst != null && !lst.isEmpty()) {
            return mapper.toDtoBean(lst);
        }
        return null;
    }

    @Override
    public AreaDTO getProcinve(String province) throws Exception {
        List<Area> lst ;
        StringBuilder strQuery = new StringBuilder();
        strQuery.append("select * from Area where area_code = #areaCode and district is null and precinct is null and status = 1 ORDER BY name");
        //execute query
        Query query = em.createNativeQuery(strQuery.toString(), Area.class);
        query.setParameter("areaCode", province);
        lst = query.getResultList();
        if (lst != null && !lst.isEmpty()) {
            return mapper.toDtoBean(lst.get(0));
        }
        return null;
    }
}