package com.viettel.bccs.inventory.repo;

import com.google.common.collect.Lists;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.ProductOfferPriceDTO;
import com.viettel.bccs.inventory.dto.StaffDTO;
import com.viettel.bccs.inventory.dto.StockOrderAgentDTO;
import com.viettel.bccs.inventory.dto.StockOrderAgentForm;
import com.viettel.bccs.inventory.model.QStockOrderAgent;
import com.viettel.bccs.inventory.model.StockOrderAgent;
import com.viettel.bccs.inventory.model.StockOrderAgent.COLUMNS;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DateUtil;
import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * TuyDV1 - SDT: 0969051866
 */
public class StockOrderAgentRepoImpl implements StockOrderAgentRepoCustom {

    public static final Logger logger = Logger.getLogger(StockOrderAgentRepoCustom.class);

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM_LIB)
    private EntityManager emIM1;

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QStockOrderAgent stockOrderAgent = QStockOrderAgent.stockOrderAgent;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case BANKCODE:
                        expression = DbUtil.createStringExpression(stockOrderAgent.bankCode, filter);
                        break;
                    case CREATEDATE:
                        expression = DbUtil.createDateExpression(stockOrderAgent.createDate, filter);
                        break;
                    case CREATESTAFFID:
                        expression = DbUtil.createLongExpression(stockOrderAgent.createStaffId, filter);
                        break;
                    case LASTMODIFY:
                        expression = DbUtil.createDateExpression(stockOrderAgent.lastModify, filter);
                        break;
                    case NOTE:
                        expression = DbUtil.createStringExpression(stockOrderAgent.note, filter);
                        break;
                    case ORDERTYPE:
                        expression = DbUtil.createLongExpression(stockOrderAgent.orderType, filter);
                        break;
                    case REQUESTCODE:
                        expression = DbUtil.createStringExpression(stockOrderAgent.requestCode, filter);
                        break;
                    case SHOPID:
                        expression = DbUtil.createLongExpression(stockOrderAgent.shopId, filter);
                        break;
                    case STATUS:
                        expression = DbUtil.createLongExpression(stockOrderAgent.status, filter);
                        break;
                    case STOCKORDERAGENTID:
                        expression = DbUtil.createLongExpression(stockOrderAgent.stockOrderAgentId, filter);
                        break;
                    case STOCKTRANSID:
                        expression = DbUtil.createLongExpression(stockOrderAgent.stockTransId, filter);
                        break;
                    case UPDATESTAFFID:
                        expression = DbUtil.createLongExpression(stockOrderAgent.updateStaffId, filter);
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
    public List<StockOrderAgent> getStockOrderAgent(StockOrderAgentDTO dto) throws Exception, LogicException {
        List lstParam = new ArrayList();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append("select * from  stock_order_agent soa where 1=1  ");
        strQuery.append(" and order_type = 2 ");
        if (!DataUtil.isNullObject(dto.getShopId())) {
            strQuery.append(" and shop_id = ? ");
            lstParam.add(dto.getShopId());
        }
        if (!DataUtil.isNullObject(dto.getStockTransId())) {
            strQuery.append(" and stock_trans_id = ? ");
            lstParam.add(dto.getStockTransId());
        }
        strQuery.append(" order by soa.stock_order_agent_id DESC ");
        Query query = em.createNativeQuery(strQuery.toString(), StockOrderAgent.class);
        for (int i = 0; i < lstParam.size(); i++) {
            query.setParameter(i + 1, lstParam.get(i));
        }
        List<StockOrderAgent> list = query.getResultList();
        return list;
    }

    @Override
    public List<StockOrderAgent> getStockOrderAgentIM1(StockOrderAgentDTO dto) throws Exception, LogicException {
        List lstParam = new ArrayList();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append("select * from  bccs_im.stock_order_agent soa where 1=1  ");
        strQuery.append(" and order_type = 2 ");
        if (!DataUtil.isNullObject(dto.getShopId())) {
            strQuery.append(" and shop_id = ? ");
            lstParam.add(dto.getShopId());
        }
        if (!DataUtil.isNullObject(dto.getStockTransId())) {
            strQuery.append(" and stock_trans_id = ? ");
            lstParam.add(dto.getStockTransId());
        }
        strQuery.append(" order by soa.stock_order_agent_id DESC ");
        Query query = emIM1.createNativeQuery(strQuery.toString(), StockOrderAgent.class);
        for (int i = 0; i < lstParam.size(); i++) {
            query.setParameter(i + 1, lstParam.get(i));
        }
        List<StockOrderAgent> list = query.getResultList();
        return list;
    }

    @Override
    public List<StockOrderAgent> search(StockOrderAgentDTO dto) throws Exception, LogicException {
        List lstParam = new ArrayList();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append("select * from  stock_order_agent where 1=1  ");
        strQuery.append(" and order_type = 2 ");
        if (!DataUtil.isNullObject(dto.getShopId())) {
            strQuery.append(" and shop_id = ? ");
            lstParam.add(dto.getShopId());
        }
        if (!DataUtil.isNullObject(dto.getStockTransId())) {
            strQuery.append(" and stock_trans_id = ? ");
            lstParam.add(dto.getStockTransId());
        }
        if (!DataUtil.isNullOrEmpty(dto.getRequestCode())) {
            strQuery.append(" and upper(request_code) like ?  escape '\\'  ");
            lstParam.add("%" + dto.getRequestCode().trim().toUpperCase().replace("\\", "\\\\").replaceAll("%", "\\\\%").replaceAll("_", "\\\\_").replaceAll("!", "\\\\!") + "%");

        }
        if (!DataUtil.isNullObject(dto.getCreateStaffId())) {
            strQuery.append(" and create_staff_id =? ");
            lstParam.add(dto.getCreateStaffId());
        }
        if (!DataUtil.isNullObject(dto.getStatus())) {
            strQuery.append(" and status =? ");
            lstParam.add(dto.getStatus());
        }
        if (!DataUtil.isNullObject(dto.getBankCode())) {
            strQuery.append(" and bank_code = ? ");
            lstParam.add(dto.getBankCode());
        }
        if (!DataUtil.isNullObject(dto.getFromDate())) {
            strQuery.append(" AND create_date >= ? ");
            lstParam.add(dto.getFromDate());
        }
        if (!DataUtil.isNullObject(dto.getToDate())) {
            strQuery.append(" AND create_date < ?  ");
            lstParam.add(DateUtil.addDay(dto.getToDate(), 1));
        }
        if (!DataUtil.isNullOrEmpty(dto.getNote())) {
            strQuery.append(" and upper(note) like ? escape '\\' ");
            lstParam.add("%" + dto.getNote().trim().toUpperCase().replace("\\", "\\\\").replaceAll("%", "\\\\%").replaceAll("_", "\\\\_").replaceAll("!", "\\\\!") + "%");

        }
        strQuery.append(" order by stock_order_agent_id DESC ");
        Query query = em.createNativeQuery(strQuery.toString(), StockOrderAgent.class);
        for (int i = 0; i < lstParam.size(); i++) {
            query.setParameter(i + 1, lstParam.get(i));
        }
        List<StockOrderAgent> list = query.getResultList();
        return list;
    }

    @Override
    public Long getMaxStockOrderAgentId() throws Exception {
        StringBuilder strQuery = new StringBuilder("");
        strQuery.append("select max(to_number(substr(request_code,4))) from stock_order_agent  ");
        Query query = em.createNativeQuery(strQuery.toString());
        List queryResult = query.getResultList();
        return DataUtil.safeToLong(queryResult.get(0));
    }

    @Override
    public List<StockOrderAgentForm> getStockOrderAgentForm(StockOrderAgentDTO searchForm, StaffDTO currentStaff) {
        List<StockOrderAgentForm> listStockOrderAgent = new ArrayList<>();
        try {
            List params = new ArrayList();
            StringBuilder sql = new StringBuilder("");
            sql.append(" SELECT  soa.stock_order_agent_id stockOrderAgentId, ");
            sql.append("         soa.request_code requestCode, ");
            sql.append("         (sh.shop_code || '-' || sh.name) as shopName, ");
            sql.append("    (select name from option_set_value where value = soa.bank_code and status=1 and option_set_id = (select id from option_set where status=1 and code = 'LIST_BANK')) bankName, ");
            sql.append("    st.staff_code || ' - ' || st.name staffName, ");
            sql.append("    soa.status status, ");
            sql.append("    DECODE (soa.status, ");
            sql.append("      0, 'Chưa lập lệnh DB', ");
            sql.append("      1, 'Đã lập lệnh DB', ");
            sql.append("      2, 'Từ chối lập lệnh DB', ");
            sql.append("      3, 'Đã hủy DB', ");
            sql.append("      '') ");
            sql.append("     strStatus, ");
            sql.append("    soa.create_date createDate, ");
            sql.append("    soa.last_modify lastModify, ");
            sql.append("    TO_CHAR(soa.create_date, 'dd/MM/yyyy HH24:mm:ss') strCreateDate, ");
            sql.append("    TO_CHAR(soa.last_modify, 'dd/MM/yyyy HH24:mm:ss') strLastModify, ");
            sql.append("    soa.note note ");
            sql.append("   FROM  stock_order_agent soa, staff st, shop sh ");
            sql.append("  WHERE  soa.create_staff_id = st.staff_id ");
            sql.append("  AND sh.shop_id = soa.shop_id ");
            sql.append("  AND sh.parent_shop_id = ? ");
            params.add(currentStaff.getShopId());
            if (!DataUtil.isNullObject(searchForm.getFromDate()) && !DataUtil.isNullObject(searchForm.getToDate())) {
                sql.append("                 AND soa.create_date >= trunc(?) ");
                sql.append("                 AND soa.create_date < trunc(?) + 1 ");
                params.add(searchForm.getFromDate());
                params.add(searchForm.getToDate());
            }
            if (!DataUtil.isNullObject(searchForm.getShopId())) {
                sql.append("    AND sh.shop_id = ? ");
                params.add(searchForm.getShopId());
            }
            if (!DataUtil.isNullObject(searchForm.getRequestCode())) {
                sql.append("    AND lower(soa.request_code) like ? escape '\\'");
                params.add("%" + searchForm.getRequestCode().toLowerCase().trim().toLowerCase().replace("\\", "\\\\").replaceAll("%", "\\\\%").replaceAll("_", "\\\\_").replaceAll("!", "\\!") + "%");
            }
            if (!DataUtil.isNullObject(searchForm.getStatus())) {
                sql.append("    AND soa.status = ? ");
                params.add(searchForm.getStatus());
            }
            if (!DataUtil.isNullObject(searchForm.getBankCode())) {
                sql.append("    AND soa.bank_code = ? ");
                params.add(searchForm.getBankCode().trim());
            }
            if (!DataUtil.isNullObject(searchForm.getNote())) {
                sql.append("    AND lower(soa.note) like ? escape '\\'");
                params.add("%" + searchForm.getNote().trim().toLowerCase().replace("\\", "\\\\").replaceAll("%", "\\\\%").replaceAll("_", "\\\\_").replaceAll("!", "\\!") + "%");
            }
            sql.append(" ORDER BY soa.stock_order_agent_id DESC ");
            Query query = em.createNativeQuery(sql.toString());
            for (int i = 0; i < params.size(); i++) {
                query.setParameter(i + 1, params.get(i));
            }
            List<Object[]> tmp = query.getResultList();
            for (Object[] ob : tmp) {
                StockOrderAgentForm form = new StockOrderAgentForm();
                int index = 0;
                form.setStockOrderAgentId(DataUtil.safeToLong(ob[index++]));
                form.setRequestCode(DataUtil.safeToString(ob[index++]));
                form.setShopName(DataUtil.safeToString(ob[index++]));
                form.setBankName(DataUtil.safeToString(ob[index++]));
                form.setStaffName(DataUtil.safeToString(ob[index++]));
                form.setStatus(DataUtil.safeToLong(ob[index++]));
                form.setStrStatus(DataUtil.safeToString(ob[index++]));
                form.setCreateDate(ob[index] != null ? new Date(((java.sql.Timestamp) ob[index]).getTime()) : null);
                index++;
                form.setLastModify(ob[index] != null ? new Date(((java.sql.Timestamp) ob[index]).getTime()) : null);
                index++;
                form.setStrCreateDate(DataUtil.safeToString(ob[index++]));
                form.setStrLastModify(DataUtil.safeToString(ob[index++]));
                form.setNote(DataUtil.safeToString(ob[index]));
                listStockOrderAgent.add(form);
            }
        } catch (Exception e) {
            logger.error(e);
        }
        return listStockOrderAgent;
    }

    @Override
    public List checkShopAgent(Long shopId) {
        StringBuilder queryString = new StringBuilder(" SELECT   '1' FROM   bccs_im.shop sh, bccs_im.channel_type ct ");
        queryString.append(" WHERE       sh.channel_type_id = ct.channel_type_id ");
        queryString.append(" AND sh.shop_id = ? ");
        queryString.append(" AND ct.is_vt_unit = 2 ");
        queryString.append(" AND ct.object_type = 1 ");
        queryString.append(" AND sh.status = 1 ");
        Query query = em.createNativeQuery(queryString.toString());
        query.setParameter(1, shopId);
        return query.getResultList();
    }
}