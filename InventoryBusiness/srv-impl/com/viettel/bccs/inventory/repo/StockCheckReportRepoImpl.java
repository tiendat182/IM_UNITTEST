package com.viettel.bccs.inventory.repo;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.StockCheckReportDTO;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DateUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.model.StockCheckReport.COLUMNS;
import com.viettel.bccs.inventory.model.QStockCheckReport;

import java.util.Date;
import java.util.List;

import com.viettel.fw.common.util.DbUtil;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public class StockCheckReportRepoImpl implements StockCheckReportRepoCustom {

    public static final Logger logger = Logger.getLogger(StockCheckReportRepoCustom.class);
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QStockCheckReport stockCheckReport = QStockCheckReport.stockCheckReport;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case CHECKDATE:
                        expression = DbUtil.createDateExpression(stockCheckReport.checkDate, filter);
                        break;
                    case CREATEDATE:
                        expression = DbUtil.createDateExpression(stockCheckReport.createDate, filter);
                        break;
                    case CREATEUSER:
                        expression = DbUtil.createStringExpression(stockCheckReport.createUser, filter);
                        break;
                    case FILENAME:
                        expression = DbUtil.createStringExpression(stockCheckReport.fileName, filter);
                        break;
                    case OWNERCODE:
                        expression = DbUtil.createStringExpression(stockCheckReport.ownerCode, filter);
                        break;
                    case OWNERID:
                        expression = DbUtil.createLongExpression(stockCheckReport.ownerId, filter);
                        break;
                    case OWNERNAME:
                        expression = DbUtil.createStringExpression(stockCheckReport.ownerName, filter);
                        break;
                    case OWNERTYPE:
                        expression = DbUtil.createShortExpression(stockCheckReport.ownerType, filter);
                        break;
                    case STOCKCHECKREPORTID:
                        expression = DbUtil.createLongExpression(stockCheckReport.stockCheckReportId, filter);
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
    public List<StockCheckReportDTO> onSearch(Long shopId, Date checkDate) throws Exception {
        List<StockCheckReportDTO> result = Lists.newArrayList();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT a.stock_check_report_id,a.owner_name,to_char(a.check_date, 'MM/yyyy') checkDateStr,a.file_name ");
        sql.append(" FROM bccs_im.stock_check_report a, bccs_im.shop b");
        sql.append(" WHERE a.owner_id = b.shop_id");
        List<Object> lstParam = Lists.newArrayList();
        if (!DataUtil.isNullObject(shopId)) {
            sql.append("         AND (a.owner_id = ? or b.parent_shop_id = ?)");
            lstParam.add(shopId.toString().trim());
            lstParam.add(shopId.toString().trim());
        }
        if (!DataUtil.isNullObject(checkDate)) {
            sql.append("         AND trunc(a.check_date, 'mm') =  trunc(to_date(?, 'dd/mm/yyyy'), 'mm')");
            lstParam.add(DateUtil.date2ddMMyyyyString(checkDate));
        }
        sql.append(" ORDER BY   a.create_date DESC");
        Query query = em.createNativeQuery(sql.toString());
        int i = 0;
        for (Object object : lstParam) {
            query.setParameter(++i, object);
        }
        List<Object[]> lstResult = query.getResultList();
        if (lstResult != null && lstResult.size() > 0) {
            for (Object[] object : lstResult) {
                int index = 0;
                StockCheckReportDTO stockCheckReportDTO = new StockCheckReportDTO();
                stockCheckReportDTO.setStockCheckReportId(Long.valueOf(DataUtil.safeToString(object[index++])));
                stockCheckReportDTO.setOwnerName(DataUtil.safeToString(object[index++]));
                stockCheckReportDTO.setCheckDateStr(DataUtil.safeToString(object[index++]));
                stockCheckReportDTO.setFileName(DataUtil.safeToString(object[index]));
                result.add(stockCheckReportDTO);
            }
        }
        return result;
    }

    public boolean isDuplicate(Long shopId, Date checkDate) throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT a.stock_check_report_id,a.owner_name,to_char(a.check_date, 'MM/yyyy') checkDateStr,a.file_name ");
        sql.append(" FROM bccs_im.stock_check_report a, bccs_im.shop b");
        sql.append(" WHERE (a.owner_id = b.shop_id or a.owner_id = b.parent_shop_id)");
        List<Object> lstParam = Lists.newArrayList();
        if (!DataUtil.isNullObject(shopId)) {
            sql.append("         AND b.shop_id = ?");
            lstParam.add(shopId.toString().trim());
        }
        if (!DataUtil.isNullObject(checkDate)) {
            sql.append("         AND trunc(a.check_date, 'mm') =  trunc(to_date(?, 'dd/mm/yyyy'), 'mm')");
            lstParam.add(DateUtil.date2ddMMyyyyString(checkDate));
        }
        sql.append(" ORDER BY   a.create_date DESC");
        Query query = em.createNativeQuery(sql.toString());
        int i = 0;
        for (Object object : lstParam) {
            query.setParameter(++i, object);
        }
        List<Object[]> lstResult = query.getResultList();
        if (lstResult.size() > 0) {
            return false;
        }
        return true;
    }
}