package com.viettel.bccs.im1.repo;

import com.google.common.collect.Lists;
import com.viettel.bccs.im1.model.StockDebitIm1;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.model.DebitRequestDetail;
import com.viettel.bccs.inventory.model.StockDebit;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DbUtil;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;

public class StockDebitIm1RepoImpl implements StockDebitIm1RepoCustom {

    public static final Logger logger = Logger.getLogger(StockDebitIm1RepoCustom.class);
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM_LIB)
    private EntityManager emIm1;

    @Override
    public StockDebitIm1 buildStockDebitFromDebitRequestDetail(DebitRequestDetail debitDetail, String staff) throws Exception {
        StockDebitIm1 stockDebit = new StockDebitIm1();
        stockDebit.setOwnerId(debitDetail.getOwnerId());
        stockDebit.setOwnerType(debitDetail.getOwnerType());
        StringBuilder builder = new StringBuilder("select * from bccs_im.stock_debit a where 1=1");
        List params = Lists.newArrayList();
        if (!DataUtil.isNullObject(debitDetail.getOwnerId())) {
            builder.append(" and owner_id= #ownerId");
            params.add(debitDetail.getOwnerId());
        }
        if (!DataUtil.isNullOrEmpty(debitDetail.getOwnerType())) {
            builder.append(" and owner_type = #ownerType");
            params.add(debitDetail.getOwnerType());
        }
        if (!DataUtil.isNullOrEmpty(debitDetail.getDebitDayType())) {
            builder.append(" and debit_day_type = #dayType");
            params.add(debitDetail.getDebitDayType());
        }
        Query query = emIm1.createNativeQuery(builder.toString(), StockDebit.class);
        if (!DataUtil.isNullObject(debitDetail.getOwnerId())) {
            query.setParameter("ownerId", debitDetail.getOwnerId());
        }
        if (!DataUtil.isNullOrEmpty(debitDetail.getOwnerType())) {
            query.setParameter("ownerType", debitDetail.getOwnerType());
        }
        if (!DataUtil.isNullOrEmpty(debitDetail.getDebitDayType())) {
            query.setParameter("dayType", debitDetail.getDebitDayType());
        }
        List<StockDebitIm1> result = query.getResultList();
        Date currentDate = DbUtil.getSysDate(emIm1);
        if (result != null && !result.isEmpty()) {
            stockDebit = result.get(0);
            stockDebit.setDebitValue(debitDetail.getDebitValue());
            stockDebit.setFinanceType(debitDetail.getFinanceType());
            stockDebit.setLastUpdateTime(currentDate);
            stockDebit.setLastUpdateUser(staff);
            updateStockDebit(stockDebit);
        } else {
            stockDebit.setOwnerId(debitDetail.getOwnerId());
            stockDebit.setOwnerType(debitDetail.getOwnerType());
            stockDebit.setDebitDayType(debitDetail.getDebitDayType());
            stockDebit.setCreateUser(staff);
            stockDebit.setCreateDate(currentDate);
            stockDebit.setDebitValue(debitDetail.getDebitValue());
            stockDebit.setFinanceType(debitDetail.getFinanceType());
            stockDebit.setLastUpdateTime(currentDate);
            stockDebit.setLastUpdateUser(staff);
            createStockDebit(stockDebit);
        }
        return stockDebit;
    }

    private int createStockDebit(StockDebitIm1 dto) throws LogicException, Exception {
        String sql = "INSERT INTO bccs_im.stock_debit a " +
                "       (a.owner_id, a.owner_type, a.debit_day_type, a.create_user," +
                "       a.create_date, a.debit_type, a.last_update_date, a.last_update_user) " +
                " VALUES(?,?,?,?,?,?,?,?) ";
        Query query = emIm1.createNativeQuery(sql);
        query.setParameter((int) 1, dto.getOwnerId());
        query.setParameter((int) 2, dto.getOwnerType());
        query.setParameter((int) 3, dto.getDebitDayType());
        query.setParameter((int) 4, dto.getCreateUser());
        query.setParameter((int) 5, dto.getCreateDate());
        query.setParameter((int) 6, dto.getDebitValue());
        query.setParameter((int) 7, dto.getLastUpdateTime());
        query.setParameter((int) 8, dto.getLastUpdateUser());

        return query.executeUpdate();
    }

    public int updateStockDebit(StockDebitIm1 dto) throws LogicException, Exception {
        String sql = "update bccs_im.stock_debit a set a.debit_type = ?, a.last_update_date = ?, a.last_update_user = ? " +
                "where a.owner_type = ? and a.owner_id = ? and a.debit_day_type = ?  ";
        Query query = emIm1.createNativeQuery(sql);
        query.setParameter((int) 1, dto.getDebitValue());
        query.setParameter((int) 2, dto.getLastUpdateTime());
        query.setParameter((int) 3, dto.getLastUpdateUser());
        query.setParameter((int) 4, dto.getOwnerType());
        query.setParameter((int) 5, dto.getOwnerId());
        query.setParameter((int) 6, dto.getDebitDayType());


        return query.executeUpdate();
    }

}