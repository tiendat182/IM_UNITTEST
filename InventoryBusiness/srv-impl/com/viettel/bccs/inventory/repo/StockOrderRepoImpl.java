package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.StockOrderDTO;
import com.viettel.bccs.inventory.model.StockOrder;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.mapper.BaseMapper;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;

public class StockOrderRepoImpl implements StockOrderRepoCustom {
    private final BaseMapper<StockOrder, StockOrderDTO> mapper = new BaseMapper<>(StockOrder.class, StockOrderDTO.class);

    public static final Logger logger = Logger.getLogger(StockOrderRepoCustom.class);
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @Override
    public List<StockOrderDTO> getListStockOrder(Long shopId) throws Exception {
        StringBuilder strQuery = new StringBuilder();
        strQuery.append("select * from  stock_order where 1=1  ");
        strQuery.append(" and status = #status ");
        strQuery.append(" and shop_id = #shopId ");
        Query query = em.createNativeQuery(strQuery.toString(), StockOrder.class);
        query.setParameter("status", Const.STOCK_ORDER.STATUS_NEW);
        query.setParameter("shopId", shopId);
        List<StockOrder> list = query.getResultList();
        if (!DataUtil.isNullOrEmpty(list)) {
            return mapper.toDtoBean(list);
        }
        return null;
    }

    @Override
    public StockOrderDTO getStockOrderByCode(String stockOrderCode) throws Exception {
        if (DataUtil.isNullOrEmpty(stockOrderCode)) {
            return null;
        }
        StringBuilder strQuery = new StringBuilder();
        strQuery.append("select * from stock_order where 1=1  ");
        strQuery.append(" and status = #status ");
        strQuery.append(" and lower(stock_order_code) = #stockOrderCode ");
        Query query = em.createNativeQuery(strQuery.toString(), StockOrder.class);
        query.setParameter("status", Const.STOCK_ORDER.STATUS_NEW);
        query.setParameter("stockOrderCode", stockOrderCode.trim().toLowerCase());
        List<StockOrder> list = query.getResultList();
        if (!DataUtil.isNullOrEmpty(list)) {
            return mapper.toDtoBean(list.get(0));
        }
        return null;
    }

    @Override
    public List<StockOrderDTO> getStockOrderList(Long staffId, Date createDate) throws Exception {
        StringBuilder strQuery = new StringBuilder();
        strQuery.append("select * from  stock_order where 1=1  ");
        strQuery.append(" and create_date >= trunc(#createDate) ");
        strQuery.append(" and create_date <= trunc(#createDate) + 1 ");
        strQuery.append(" and staff_id = #staffId ");
        Query query = em.createNativeQuery(strQuery.toString(), StockOrder.class);
        query.setParameter("createDate", createDate);
        query.setParameter("staffId", staffId);
        List<StockOrder> list = query.getResultList();
        if (!DataUtil.isNullOrEmpty(list)) {
            return mapper.toDtoBean(list);
        }
        return null;
    }

    @Override
    public StockOrderDTO getStockOrderStaff(Long staffId) throws Exception {
        StringBuilder strQuery = new StringBuilder();
        strQuery.append("select * from  stock_order where 1=1  ");
        strQuery.append(" and status = #status");
        strQuery.append(" and staff_id = #staffId ");
        Query query = em.createNativeQuery(strQuery.toString(), StockOrder.class);
        query.setParameter("status", Const.STOCK_ORDER.STATUS_NEW);
        query.setParameter("staffId", staffId);
        List<StockOrder> list = query.getResultList();
        if (!DataUtil.isNullOrEmpty(list)) {
            return mapper.toDtoBean(list.get(0));
        }
        return null;
    }
}