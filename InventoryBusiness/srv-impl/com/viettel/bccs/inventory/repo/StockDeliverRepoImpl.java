package com.viettel.bccs.inventory.repo;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.StockDeliverDTO;
import com.viettel.bccs.inventory.dto.StockDeliverDetailDTO;
import com.viettel.bccs.inventory.model.StockDeliver;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.model.StockDeliver.COLUMNS;
import com.viettel.bccs.inventory.model.QStockDeliver;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.mapper.BaseMapper;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import static com.viettel.fw.common.util.GetTextFromBundleHelper.getText;

public class StockDeliverRepoImpl implements StockDeliverRepoCustom {

    public static final Logger logger = Logger.getLogger(StockDeliverRepoCustom.class);
    private final BaseMapper<StockDeliver, StockDeliverDTO> mapper = new BaseMapper(StockDeliver.class, StockDeliverDTO.class);

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QStockDeliver stockDeliver = QStockDeliver.stockDeliver;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case CREATEDATE:
                        expression = DbUtil.createDateExpression(stockDeliver.createDate, filter);
                        break;
                    case CREATEUSER:
                        expression = DbUtil.createStringExpression(stockDeliver.createUser, filter);
                        break;
                    case CREATEUSERID:
                        expression = DbUtil.createLongExpression(stockDeliver.createUserId, filter);
                        break;
                    case CREATEUSERNAME:
                        expression = DbUtil.createStringExpression(stockDeliver.createUserName, filter);
                        break;
                    case NEWSTAFFID:
                        expression = DbUtil.createLongExpression(stockDeliver.newStaffId, filter);
                        break;
                    case NEWSTAFFOWNERID:
                        expression = DbUtil.createLongExpression(stockDeliver.newStaffOwnerId, filter);
                        break;
                    case NOTE:
                        expression = DbUtil.createStringExpression(stockDeliver.note, filter);
                        break;
                    case OLDSTAFFID:
                        expression = DbUtil.createLongExpression(stockDeliver.oldStaffId, filter);
                        break;
                    case OLDSTAFFOWNERID:
                        expression = DbUtil.createLongExpression(stockDeliver.oldStaffOwnerId, filter);
                        break;
                    case OWNERID:
                        expression = DbUtil.createLongExpression(stockDeliver.ownerId, filter);
                        break;
                    case OWNERTYPE:
                        expression = DbUtil.createLongExpression(stockDeliver.ownerType, filter);
                        break;
                    case STATUS:
                        expression = DbUtil.createLongExpression(stockDeliver.status, filter);
                        break;
                    case STOCKDELIVERID:
                        expression = DbUtil.createLongExpression(stockDeliver.stockDeliverId, filter);
                        break;
                    case UPDATEDATE:
                        expression = DbUtil.createDateExpression(stockDeliver.updateDate, filter);
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

    public StockDeliverDTO getStockDeliverByOwnerIdAndStatus(Long ownerId, Long status) throws LogicException, Exception {
        StringBuilder builder = new StringBuilder(" ");
        builder.append(" SELECT * FROM stock_deliver WHERE  owner_type = 1 and owner_id = #ownerId and create_date >= sysdate - 30");
        if (status != null) {
            builder.append(" and status = #status");
        }
        Query query = em.createNativeQuery(builder.toString(), StockDeliver.class);
        query.setParameter("ownerId", ownerId);
        if (status != null) {
            query.setParameter("status", status);
        }
        List<StockDeliver> result = query.getResultList();
        if (!DataUtil.isNullOrEmpty(result)) {
            return mapper.toDtoBean(result.get(0));
        }
        return null;
    }

    public List<Long> getAllStockForDelete() throws LogicException, Exception {
        StringBuilder sqlBuilder = new StringBuilder(" ");
        sqlBuilder.append(" SELECT   shop_id, current_quantity ");
        sqlBuilder.append("   FROM   (  SELECT   c.shop_id, SUM (NVL(a.current_quantity,0)) current_quantity ");
        sqlBuilder.append("               FROM   stock_total a, ");
        sqlBuilder.append("                      shop b, ");
        sqlBuilder.append("                      (SELECT   * ");
        sqlBuilder.append("                         FROM   shop a ");
        sqlBuilder.append("                        WHERE       a.create_date <= SYSDATE - 15 ");
        sqlBuilder.append("                                AND a.channel_type_id = 1 ");
        sqlBuilder.append("                                AND a.status = 1 ");
        sqlBuilder.append("                                AND a.shop_keeper_id IS NULL ");
        sqlBuilder.append("                                AND a.shop_director_id IS NULL ");
        sqlBuilder.append("                                AND NOT EXISTS ");
        sqlBuilder.append("                                       (SELECT   1 ");
        sqlBuilder.append("                                          FROM   staff b ");
        sqlBuilder.append("                                         WHERE   a.shop_id = b.shop_id ");
        sqlBuilder.append("                                                 AND b.status = 1)) c ");
        sqlBuilder.append("              WHERE       a.owner_id (+) = b.shop_id ");
        sqlBuilder.append("                      AND a.owner_type (+) = 1 ");
        sqlBuilder.append("                      AND b.channel_type_id = 1 ");
        sqlBuilder.append("                      AND INSTR (b.shop_path, c.shop_path) > 0 ");
        sqlBuilder.append("           GROUP BY   c.shop_id) ");
        sqlBuilder.append("  WHERE   current_quantity = 0 ");
        Query query = em.createNativeQuery(sqlBuilder.toString());
        List<Object[]> result = query.getResultList();
        List<Long> lst = Lists.newArrayList();
        if (!DataUtil.isNullOrEmpty(result)) {
            for (Object[] item : result) {
                lst.add(DataUtil.safeToLong(item[0]));
            }
        }
        return lst;
    }

    public List<StockDeliverDTO> searchHistoryStockDeliver(String code, Date fromDate, Date toDate, String status, Long ownerId, Long ownerType) throws Exception {
        StringBuilder sql = new StringBuilder(" ");
        sql.append(" SELECT stock_deliver_id, ");
        sql.append("     code, ");
        sql.append("    (select shop_code || '-' || name from shop where shop_id= owner_id) ownerName, ");
        sql.append("     status, ");
        sql.append("     create_date, ");
        sql.append("     create_user ");
        sql.append("  FROM stock_deliver ");
        sql.append("  WHERE 1=1 ");
        if (!DataUtil.isNullOrEmpty(code)) {
            sql.append("  AND code LIKE #code ");
        }
        if (ownerId != null && ownerType != null) {
            sql.append("          AND owner_id = #ownerId ");
            sql.append("          AND owner_type = #ownerType ");
        }
        sql.append("          AND create_date >= TRUNC (#fromDate) ");
        sql.append("          AND create_date <= TRUNC (#toDate) + 1 ");
        if (!DataUtil.isNullOrEmpty(status)) {
            sql.append("          AND status = #status ");
        }
        sql.append("   ORDER BY create_date DESC ");
        Query query = em.createNativeQuery(sql.toString());
        if (!DataUtil.isNullOrEmpty(code)) {
            query.setParameter("code", "%" + code.trim().toUpperCase() + "%");
        }
        if (ownerId != null && ownerType != null) {
            query.setParameter("ownerId", ownerId);
            query.setParameter("ownerType", ownerType);
        }
        query.setParameter("fromDate", fromDate);
        query.setParameter("toDate", toDate);
        if (!DataUtil.isNullOrEmpty(status)) {
            query.setParameter("status", status);
        }
        List<Object[]> lsData = query.getResultList();
        List<StockDeliverDTO> lstHistory = Lists.newArrayList();
        if (!DataUtil.isNullOrEmpty(lsData)) {
            int index;
            for (Object[] objects : lsData) {
                index = 0;
                StockDeliverDTO stockDeliverDTO = new StockDeliverDTO();
                stockDeliverDTO.setStockDeliverId(DataUtil.safeToLong(objects[index++]));
                stockDeliverDTO.setCode(DataUtil.safeToString(objects[index++]));
                stockDeliverDTO.setOwnerFullName(DataUtil.safeToString(objects[index++]));
                stockDeliverDTO.setStatus(DataUtil.safeToLong(objects[index++]));
                stockDeliverDTO.setStatusName(getStatusName(stockDeliverDTO.getStatus().toString()));
                Object obj = objects[index++];
                stockDeliverDTO.setCreateDate(obj != null ? (Date) obj : null);
                stockDeliverDTO.setCreateUser(DataUtil.safeToString(objects[index]));
                lstHistory.add(stockDeliverDTO);
            }
        }
        return lstHistory;
    }

    private String getStatusName(String status) {
        String statusName;
        switch (status) {
            case "0":
                statusName = getText("mn.stock.handover");
                break;
            case "1":
                statusName = getText("import.partner.request.status1");
                break;
            case "2":
                statusName = getText("import.partner.request.status2");
                break;
            default:
                statusName = "";
        }
        return statusName;
    }

    public Long getMaxId() throws LogicException, Exception {
        StringBuilder builder = new StringBuilder();
        builder.append("select MAX (stock_deliver_id) stockDeliverId FROM stock_deliver");
        Query query = em.createNativeQuery(builder.toString());
        BigDecimal maxId = (BigDecimal) query.getSingleResult();
        if (!DataUtil.isNullOrZero(maxId)) {
            return maxId.longValue() + 1L;
        }
        return 1L;
    }

    public boolean isDulicateCode(String requestCode) throws Exception {
        StringBuilder builder = new StringBuilder("");
        builder.append("select a.*")
                .append("  from stock_deliver a")
                .append("  where a.code =?");
        Query query = em.createNativeQuery(builder.toString());
        query.setParameter(1, requestCode);
        //
        return DataUtil.isNullOrEmpty(query.getResultList());
    }

}