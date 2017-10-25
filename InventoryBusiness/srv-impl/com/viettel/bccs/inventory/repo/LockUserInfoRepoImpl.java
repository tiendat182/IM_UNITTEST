package com.viettel.bccs.inventory.repo;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.common.IMCommonUtils;
import com.viettel.bccs.inventory.dto.LockUserInfoDTO;
import com.viettel.bccs.inventory.model.LockUserInfo;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public class LockUserInfoRepoImpl implements LockUserInfoRepoCustom {

    public static final Logger logger = Logger.getLogger(LockUserInfoRepoCustom.class);
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        return null;
    }

    public List<LockUserInfoDTO> getLockUserInfo(String sql, Long lockTypeId, Long validRange, Long checkRange) {
        List<LockUserInfoDTO> lst = Lists.newArrayList();
        PreparedStatement statement = null;
        ResultSet result = null;
        Connection conn = null;
        try {
            conn = IMCommonUtils.getDBIMConnection();
            statement = conn.prepareStatement(sql);
            statement.setLong(1, lockTypeId);
            statement.setLong(2, validRange);
            statement.setLong(3, checkRange);

            result = statement.executeQuery();
            if (result != null) {
                while (result.next()) {
                    LockUserInfoDTO lockUserInfo = new LockUserInfoDTO();
                    lockUserInfo.setShopId(result.getLong("shop_id"));
                    lockUserInfo.setStaffId(result.getLong("staff_id"));
                    lockUserInfo.setTransId(result.getLong("trans_id"));
                    lockUserInfo.setTransCode(result.getString("trans_code"));
                    lockUserInfo.setTransType(result.getLong("trans_type"));
                    lockUserInfo.setTransDate(result.getDate("trans_date"));
                    lockUserInfo.setTransStatus(result.getLong("trans_status"));
                    lockUserInfo.setLockTypeId(result.getLong("lock_type_id"));
                    lst.add(lockUserInfo);
                }
            }
            statement.close();
            if (result != null) {
                result.close();
            }
            conn.close();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return lst;
    }

    public boolean checkExistLockUserInfo(Long transId, Long transType, Long transStatus) throws Exception {
        StringBuilder sql = new StringBuilder("select * from lock_user_info where trans_id = #transId and trans_type = #transType and trans_status = #transStatus");
        Query query = em.createNativeQuery(sql.toString());
        query.setParameter("transId", transId);
        query.setParameter("transType", transType);
        query.setParameter("transStatus", transStatus);
        List listResult = query.getResultList();
        if (!DataUtil.isNullOrEmpty(listResult)) {
            return true;
        }
        return false;
    }

    public List<LockUserInfoDTO> getLstLockUserInfo() throws Exception {
        StringBuilder strQuery = new StringBuilder();
        List<LockUserInfoDTO> lst = Lists.newArrayList();
        strQuery.append("SELECT   a.trans_id transId,");
        strQuery.append("         a.trans_date transDate,");
        strQuery.append("         a.shop_id shopId,");
        strQuery.append("         a.staff_id staffId,");
        strQuery.append("         a.id id,");
        strQuery.append("         b.sql_check_trans sqlCheckTrans");
        strQuery.append("  FROM   lock_user_info a, lock_user_type b");
        strQuery.append(" WHERE   a.lock_type_id = b.id");
        strQuery.append(" AND     b.sql_check_trans is not null and b.status = 1 and b.auto = 1");
        Query query = em.createNativeQuery(strQuery.toString());
        List listResult = query.getResultList();
        if (!DataUtil.isNullOrEmpty(listResult)) {
            for (Object o : listResult) {
                Object[] obj = (Object[]) o;
                LockUserInfoDTO lockUserInfoDTO = new LockUserInfoDTO();
                lockUserInfoDTO.setTransId(DataUtil.safeToLong(obj[0]));
                Object transDate = obj[1];
                lockUserInfoDTO.setTransDate(transDate != null ? (Date) transDate : null);
                lockUserInfoDTO.setShopId(DataUtil.safeToLong(obj[2]));
                lockUserInfoDTO.setStaffId(DataUtil.safeToLong(obj[3]));
                lockUserInfoDTO.setId(DataUtil.safeToLong(obj[4]));
                lockUserInfoDTO.setSqlCheckTrans(DataUtil.safeToString(obj[5]));
                lst.add(lockUserInfoDTO);
            }
        }
        return lst;
    }

    public boolean canUnlock(String sql, Long transId, Date transDate) throws Exception {
        Query query = em.createNativeQuery(sql);
        query.setParameter(1, transId);
        query.setParameter(2, transDate);
        BigDecimal bigDecimal = (BigDecimal) query.getSingleResult();
        if (bigDecimal != null && bigDecimal.longValue() > 0L) {
            return true;
        }
        return false;
    }

    public void deleteLockUser(List<Long> lstId) throws Exception {

        PreparedStatement deleteStm = null;
        Connection conn = null;
        try {
            conn = IMCommonUtils.getDBIMConnection();
            conn.setAutoCommit(false);

            StringBuilder strDetele = new StringBuilder();
            strDetele.append("delete from lock_user_info where id = ? ");
            deleteStm = conn.prepareStatement(strDetele.toString());
            Long numberBatch = 0L;
            for (int i = 0; i < lstId.size(); i++) {
                deleteStm.setLong(1, lstId.get(0));
                deleteStm.addBatch();
                if (i % Const.BATCH_SIZE_1000 == 0 && i > 0) {
                    try {
                        deleteStm.executeBatch();
                        //so ban ghi insert thanh cong
                        conn.commit();
                        ++numberBatch;
                    } catch (Exception ex) {
                        logger.error(ex.getMessage(), ex);
                        conn.rollback();
                    }
                }
            }
            //Xu ly voi so ban ghi con lai
            Long numberRemainRecord = lstId.size() - numberBatch * Const.BATCH_SIZE_1000;
            if (numberRemainRecord > 0) {
                try {
                    deleteStm.executeBatch();
                    conn.commit();
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                    conn.rollback();
                }
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            System.out.println(ex.getMessage());
            throw ex;
        } finally {
            if (deleteStm != null) {
                deleteStm.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

    @Override
    public List<LockUserInfo> findByStaffId(Long staffId) throws Exception {
        String sql = "SELECT * FROM LOCK_USER_INFO WHERE STAFF_ID = #staffId";
        Query query = em.createNativeQuery(sql, LockUserInfo.class);
        query.setParameter("staffId", staffId);
        List<LockUserInfo> lst = query.getResultList();
        return lst;
    }

    @Override
    public List<LockUserInfoDTO> getListLockUserV2(Long staffId, String lockTypeId, String stockTransType) throws Exception {
        lockTypeId = "".equals(lockTypeId) ? null : lockTypeId;
        stockTransType = "".equals(stockTransType) ? null : stockTransType;
        List<LockUserInfoDTO> result = Lists.newArrayList();
        String sql = "SELECT   lui1.shop_id, "
                + "       lui1.staff_id,  "
                + "       stacstock1.stock_trans_action_id action_id,   "
                + "       stacstock1.action_code transCode , "
                + "       lui1.trans_type, "
                + "       stacstock1.create_datetime, "
                + "       lui1.trans_status, "
                + "       lut1.name lock_type_name, "
                + "       sf1.staff_code, "
                + "       sf1.name staff_name, "
                + "       (CASE stock1.from_owner_type "
                + "       WHEN 1 "
                + "       then "
                + "      ( SELECT shop.shop_code || '-' ||shop.name   from shop "
                + "  where shop.shop_id=stock1.from_owner_id) "
                + "        WHEN 2 "
                + "       then "
                + "       (select staff.staff_code || '-' || staff.name from staff  "
                + "       where staff.staff_id=stock1.from_owner_id) "
                + "       end) from_stock, "
                + "      (CASE stock1.to_owner_type "
                + "         WHEN 1 "
                + "       then "
                + "     (  SELECT s.shop_code || '-' || s.name  from shop s "
                + " where shop_id=stock1.to_owner_id) "
                + "         WHEN 2 "
                + "       then "
                + "      ( select staff.staff_code || '-' || staff.name from staff "
                + "     where staff.staff_id=stock1.to_owner_id) "
                + "       end) to_stock,"
                + "      stock1.stock_trans_type stock_trans_type ,"
                + "      re1.reason_name reason_name    "
                + " FROM   stock_trans stock1, "
                + "       stock_trans_action stacstock1, "
                + "       lock_user_info lui1, "
                + "       lock_user_type lut1, "
                + "       staff sf1, "
                + "       reason re1  "
                + "      WHERE       stock1.stock_trans_id = lui1.trans_id "
                + "       AND stock1.stock_trans_id = stacstock1.stock_trans_id "
                + "       AND lui1.lock_type_id = lut1.id "
                //                    + "       AND sh1.shop_id(+) = lui1.shop_id "
                + "       AND sf1.staff_id = lui1.staff_id "
                + "       and re1.reason_id(+)=stock1.reason_id  "
                + "       and stacstock1.status in (1,2,4,5)  ";
        //if (staffId != null) {
        sql += " and sf1.staff_id = #staffId  ";
        //}
        if (lockTypeId != null) {
            sql += " and lui1.lock_type_id = #lockTypeId  ";
        }
        if (stockTransType != null) {
            if ("1".equals(stockTransType)) {
                sql += " and stacstock1.status in (1,2) ";
            } else {
                sql += " and stacstock1.status in (4,5) ";
            }
        }
        //cach ra cho de nhin
        sql += "      UNION ALL "
                //cach ra cho de nhin
                + "      SELECT   lui2.shop_id, "
                + "       lui2.staff_id, "
                + "       lui2.trans_id, "
                + "       stacstock.action_code transCode , "
                + "       lui2.trans_type, "
                + "       stacstock.create_datetime, "
                + "       lui2.trans_status, "
                + "       lut2.name lock_type_name, "
                + "       sf2.staff_code, "
                + "       sf2.name staff_name, "
                + "              (CASE stock2.from_owner_type "
                + "         WHEN 1  "
                + "       then "
                + "      ( SELECT s.shop_code || '-' || s.name  from shop s "
                + "  where shop_id=stock2.from_owner_id) "
                + "         WHEN 2 "
                + "       then "
                + "      ( select staff.staff_code || '-' || staff.name from staff "
                + "     where staff.staff_id=stock2.from_owner_id) "
                + "       end) from_stock, "
                + "         (CASE stock2.to_owner_type "
                + "       WHEN 1 "
                + "       then "
                + "     (  SELECT s.shop_code || '-' || s.name  from shop s"
                + "  where shop_id=stock2.to_owner_id) "
                + "        WHEN 2 "
                + "       then "
                + "   (    select staff.staff_code || '-' || staff.name from staff  "
                + "  where staff.staff_id=stock2.to_owner_id) "
                + "       end) to_stock ,"
                + "      stock2.stock_trans_type stock_trans_type , "
                + "       re2.reason_name reason_name  "
                + "  FROM   stock_trans stock2, "
                + "       stock_trans_action stacstock, "
                + "       lock_user_info lui2, "
                + "       lock_user_type lut2, "
                + "       staff sf2, "
                + "       reason re2 "
                + "  WHERE       stacstock.stock_trans_action_id = lui2.trans_id "
                + "       AND stock2.stock_trans_id = stacstock.stock_trans_id "
                + "       AND lui2.lock_type_id = lut2.id "
                + "       AND sf2.staff_id = lui2.staff_id "
                + "       and re2.reason_id(+)=stock2.reason_id  "
                + "       and stacstock.status in (1,2,4,5)"
        ;
        //if (staffId != null) {
        sql += " and sf2.staff_id = #staffId  ";
        //}
        if (lockTypeId != null) {
            sql += " and lui2.lock_type_id = #lockTypeId  ";
        }
        if (stockTransType != null) {
            if ("1".equals(stockTransType)) {
                sql += " and stacstock.status in (1,2) ";
            } else {
                sql += " and stacstock.status in (4,5) ";
            }
        }
        StringBuilder queryString = new StringBuilder("");
        queryString.append(sql);
        queryString.append(" UNION ALL ");
        queryString.append(" SELECT   lui2.shop_id, ");
        queryString.append("          lui2.staff_id, ");
        queryString.append("          lui2.trans_id, ");
        queryString.append("          lui2.trans_code AS transcode, ");
        queryString.append("          lui2.trans_type, ");
        queryString.append("          lui2.trans_date create_datetime, ");
        queryString.append("          lui2.trans_status, ");
        queryString.append("          lut2.name lock_type_name, ");
        queryString.append("          sf2.staff_code, ");
        queryString.append("          sf2.name staff_name, ");
        queryString.append("          NULL AS from_stock, ");
        queryString.append("          NULL AS to_stock, ");
        queryString.append("          NULL AS stock_trans_type, ");
        queryString.append("          NULL AS reason_name ");
        queryString.append(" FROM  bccs_im.lock_user_info lui2, ");
        queryString.append("          bccs_im.lock_user_type lut2, ");
        queryString.append("          staff sf2 ");
        queryString.append(" WHERE 1=1 ");
        queryString.append("          AND lui2.lock_type_id = lut2.id ");
        queryString.append("          AND sf2.staff_id = lui2.staff_id ");
        //if (staffId != null) {
        queryString.append("          AND sf2.staff_id = #staffId ");
        //}
        //if (lockTypeId != null) {
        //    queryString.append(" and lui2.lock_type_id = :lockTypeId  ");
        //}

        Query query = em.createNativeQuery(queryString.toString());

        //if (staffId != null) {
        query.setParameter("staffId", staffId);
        //}

        if (lockTypeId != null) {
            query.setParameter("lockTypeId", lockTypeId);
        }

        List<Object[]> lst = query.getResultList();

        for (Object[] objects : lst) {
            LockUserInfoDTO valueDTO = new LockUserInfoDTO();

            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
            Date date;
            date = df.parse(objects[5].toString());

            valueDTO.setShopId(DataUtil.safeToLong(objects[0]));
            valueDTO.setStaffId(DataUtil.safeToLong(objects[1]));
//                        valueDTO.settrans(DataUtil.safeToLong(objects[2]));
            valueDTO.setTransCode(DataUtil.safeToString(objects[3]));
//                        valueDTO.setTransType(DataUtil.safeToString(objects[4]));
            valueDTO.setTransDate(date);
            valueDTO.setTransStatus(DataUtil.safeToLong(objects[6]));
            valueDTO.setLockTypeName(DataUtil.safeToString(objects[7]));
            valueDTO.setStaffCode(DataUtil.safeToString(objects[8]));
            valueDTO.setStaffName(DataUtil.safeToString(objects[9]));
            valueDTO.setFromOwner(DataUtil.safeToString(objects[10]));
            valueDTO.setToOwner(DataUtil.safeToString(objects[11]));
            valueDTO.setStockTransType(DataUtil.safeToLong(objects[12]));
            valueDTO.setReasonName(DataUtil.safeToString(objects[13]));

            result.add(valueDTO);
        }
        return result;
    }

    public void unlockUserStockInspect() throws Exception {
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(" DELETE from bccs_im.lock_user_info");
        strQuery.append(" where lock_type_id = 3");
        strQuery.append(" and staff_id in (");
        strQuery.append(" SELECT staff_id");
        strQuery.append("   FROM bccs_im.lock_user_info x");
        strQuery.append(" WHERE NOT EXISTS");
        strQuery.append("                (SELECT owner_id,");
        strQuery.append("                        owner_type,");
        strQuery.append("                        prod_offer_id,");
        strQuery.append("                        state_id");
        strQuery.append("                   FROM bccs_im.stock_daily_remain_general");
        strQuery.append("                  WHERE remain_date = LAST_DAY (ADD_MONTHS (TRUNC (SYSDATE, 'mm'), -1))");
        strQuery.append("                        AND owner_type = 2");
        strQuery.append("                        AND owner_id = x.staff_id");
        strQuery.append("                        AND shop_id <> 7282");
        strQuery.append("                        AND prod_offer_type_id NOT IN (1, 2, 3)");
        strQuery.append("                        AND current_quantity > 0");
        strQuery.append("                 MINUS");
        strQuery.append("                 SELECT owner_id,");
        strQuery.append("                        owner_type,");
        strQuery.append("                        prod_offer_id,");
        strQuery.append("                        state_id");
        strQuery.append("                   FROM bccs_im.stock_inspect");
        strQuery.append("                  WHERE     create_date >= SYSDATE - 20");
        strQuery.append("                        AND owner_type = 2");
        strQuery.append("                        AND owner_id = x.staff_id");
        strQuery.append("                        AND is_finished = 1)");
        strQuery.append("        AND x.lock_type_id = 3)");
        Query query = em.createNativeQuery(strQuery.toString());
        query.executeUpdate();
    }
}