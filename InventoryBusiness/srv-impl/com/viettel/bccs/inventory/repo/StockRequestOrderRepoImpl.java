package com.viettel.bccs.inventory.repo;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.StockOrderDTO;
import com.viettel.bccs.inventory.dto.StockRequestOrderDTO;
import com.viettel.bccs.inventory.model.StockOrder;
import com.viettel.bccs.inventory.model.StockRequestOrder;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DateUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.model.StockRequestOrder.COLUMNS;
import com.viettel.bccs.inventory.model.QStockRequestOrder;

import java.util.Date;
import java.util.List;

import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.mapper.BaseMapper;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import static com.viettel.fw.common.util.GetTextFromBundleHelper.getText;

public class StockRequestOrderRepoImpl implements StockRequestOrderRepoCustom {

    public static final Logger logger = Logger.getLogger(StockRequestOrderRepoCustom.class);
    private final BaseMapper<StockRequestOrder, StockRequestOrderDTO> mapper = new BaseMapper<>(StockRequestOrder.class, StockRequestOrderDTO.class);

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QStockRequestOrder stockRequestOrder = QStockRequestOrder.stockRequestOrder;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case CREATEDATETIME:
                        expression = DbUtil.createDateExpression(stockRequestOrder.createDatetime, filter);
                        break;
                    case CREATEUSER:
                        expression = DbUtil.createStringExpression(stockRequestOrder.createUser, filter);
                        break;
                    case DESCRIPTION:
                        expression = DbUtil.createStringExpression(stockRequestOrder.description, filter);
                        break;
                    case ORDERCODE:
                        expression = DbUtil.createStringExpression(stockRequestOrder.orderCode, filter);
                        break;
                    case ORDERTYPE:
                        expression = DbUtil.createLongExpression(stockRequestOrder.orderType, filter);
                        break;
                    case OWNERID:
                        expression = DbUtil.createLongExpression(stockRequestOrder.ownerId, filter);
                        break;
                    case OWNERTYPE:
                        expression = DbUtil.createLongExpression(stockRequestOrder.ownerType, filter);
                        break;
                    case STATUS:
                        expression = DbUtil.createStringExpression(stockRequestOrder.status, filter);
                        break;
                    case STOCKREQUESTORDERID:
                        expression = DbUtil.createLongExpression(stockRequestOrder.stockRequestOrderId, filter);
                        break;
                    case UPDATEDATETIME:
                        expression = DbUtil.createDateExpression(stockRequestOrder.updateDatetime, filter);
                        break;
                    case UPDATEUSER:
                        expression = DbUtil.createStringExpression(stockRequestOrder.updateUser, filter);
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
    public List<StockRequestOrderDTO> findSearhStockOrder(String orderCode, Date fromDate, Date endDate, String status, Long ownerId, Long ownerType) throws Exception {
        StringBuilder sql = new StringBuilder(" ");
        sql.append(" SELECT   a.stock_request_order_id, ");
        sql.append("     a.order_code, ");
        sql.append("     a.owner_id, ");
        sql.append("     (select shop_code || '-' || name from shop where shop_id= a.owner_id) ownerName, ");
        sql.append("     a.owner_type,");
        sql.append("     a.status, ");
        sql.append("     a.create_datetime, ");
        sql.append("     a.create_user, ");
        sql.append("     a.order_type ");
        sql.append("   FROM   stock_request_order  a ");
        sql.append("  WHERE 1=1 ");
        if (!DataUtil.isNullOrEmpty(orderCode)) {
            sql.append("  AND a.order_code LIKE #orderCode ");
        }
        if (ownerId != null && ownerType != null) {
            sql.append("          AND a.owner_id = #ownerId ");
            sql.append("          AND a.owner_type = #ownerType ");
        }
        sql.append("          AND a.create_datetime >= TRUNC (#fromDate) ");
        sql.append("          AND a.create_datetime <= TRUNC (#endDate) + 1 ");
        sql.append("          AND a.order_type=1 ");
        if (!DataUtil.isNullOrEmpty(status)) {
            sql.append("          AND a.status = #status ");
        }
        sql.append("   ORDER BY a.create_datetime DESC ");
        Query query = em.createNativeQuery(sql.toString());
        if (!DataUtil.isNullOrEmpty(orderCode)) {
            query.setParameter("orderCode", "%" + orderCode.trim().toUpperCase() + "%");
        }
        if (ownerId != null && ownerType != null) {
            query.setParameter("ownerId", ownerId);
            query.setParameter("ownerType", ownerType);
        }
        query.setParameter("fromDate", fromDate);
        query.setParameter("endDate", endDate);
        if (!DataUtil.isNullOrEmpty(status)) {
            query.setParameter("status", status);
        }
        List<Object[]> lsData = query.getResultList();
        List<StockRequestOrderDTO> lsRequest = Lists.newArrayList();
        if (!DataUtil.isNullOrEmpty(lsData)) {
            int index;
            for (Object[] objects : lsData) {
                index = 0;
                StockRequestOrderDTO orderDTO = new StockRequestOrderDTO();
                orderDTO.setStockRequestOrderId(DataUtil.safeToLong(objects[index++]));
                orderDTO.setOrderCode(DataUtil.safeToString(objects[index++]));
                orderDTO.setOwnerId(DataUtil.safeToLong(objects[index++]));
                orderDTO.setOwnerFullName(DataUtil.safeToString(objects[index++]));
                orderDTO.setOwnerType(DataUtil.safeToLong(objects[index++]));
                orderDTO.setStatus(DataUtil.safeToString(objects[index++]));
                orderDTO.setStatusName(getStatusName(orderDTO.getStatus()));
                Object obj = objects[index++];
                orderDTO.setCreateDatetime(obj != null ? (Date) obj : null);
                orderDTO.setCreateUser(DataUtil.safeToString(objects[index++]));
                orderDTO.setOrderType(DataUtil.safeToLong(objects[index]));
                lsRequest.add(orderDTO);
            }
        }
        return lsRequest;
    }

    /**
     * ham xu ly hien thi trang thai yeu cau
     *
     * @param status
     * @return
     * @author thanhnt77
     */
    private String getStatusName(String status) {
        String statusName;
        switch (status) {
            case "0":
                statusName = getText("mn.stock.branch.btn.create.request");
                break;
            case "1":
                statusName = getText("import.partner.request.status1");
                break;
            case "2":
                statusName = getText("import.partner.request.status2");
                break;
            case "3":
                statusName = getText("common.cancel");
                break;
            default:
                statusName = "";
        }
        return statusName;
    }

    public List<StockRequestOrderDTO> getLstApproveAndSignVoffice() throws Exception {
        StringBuilder sql = new StringBuilder(" ");
        sql.append(" SELECT   a.* ");
        sql.append("   FROM stock_request_order a, stock_trans_voffice b  ");
        sql.append("  WHERE     a.stock_request_order_id = b.stock_trans_action_id ");
        sql.append("  AND a.status = 1 ");
        sql.append("  AND b.status IN (1, 2) ");
        sql.append("  AND b.prefix_template = 'GOODS_REVOKE' ");
        Query query = em.createNativeQuery(sql.toString(), StockRequestOrder.class);
        return mapper.toDtoBean(query.getResultList());
    }

}