package com.viettel.bccs.inventory.repo;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.StockRequestDTO;
import com.viettel.bccs.inventory.service.common.IMServiceUtil;
import com.viettel.fw.common.util.DataUtil;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by vanho on 23/03/2017.
 */
public class OrderDivideDeviceRepoImpl implements OrderDivideDeviceRepoCustom {

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @Override
    public List<StockRequestDTO> getListOrderDivideDevice(String shopPath, Date fromDate, Date toDate, Long ownerId, String requestCode, String status) throws Exception {

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT   sr.stock_request_id, " +
                "           sr.request_code, " +
                "           sr.owner_type, " +
                "           owner_id, " +
                "           sr.status, " +
                "           sr.stock_trans_id, " +
                "           sr.create_user, " +
                "           sr.create_datetime, " +
                "           sr.request_type, " +
                "           s.shop_code owner_code, " +
                "           s.name owner_name, " +
                "           (select b.status from bccs_im.stock_trans_action a, bccs_im.stock_trans_voffice b " +
                "             where a.stock_trans_action_id= b.stock_trans_action_id " +
                "               and a.stock_trans_id=sr.stock_trans_id " +
                "               and a.status=2) signVoffice, " +
                "           sr.note " +
                "    FROM   stock_request sr, shop s " +
                "    WHERE   sr.owner_id = s.shop_id " +
                "           AND sr.request_type = 3 " +
                "           AND sr.owner_type = 1 ");

        sql.append(" AND sr.create_datetime >= TRUNC (#fromDate) ");
        sql.append(" AND sr.create_datetime <= TRUNC (#endDate) + 1 ");
        if (!DataUtil.isNullOrEmpty(status))
            sql.append(" AND sr.status = #status ");
        if (ownerId != null)
            sql.append(" AND sr.owner_id = #ownerId ");
        if (!DataUtil.isNullOrEmpty(shopPath))
            sql.append(" AND s.shop_path like #shopPath ");
        if (!DataUtil.isNullOrEmpty(requestCode))
            sql.append(" AND upper(sr.request_code) LIKE #requestCode");
        sql.append(" ORDER BY sr.create_datetime DESC ");

        Query query = em.createNativeQuery(sql.toString());

        if (!DataUtil.isNullOrEmpty(status))
            query.setParameter("status", status);

        if (!DataUtil.isNullOrEmpty(requestCode))
            query.setParameter("requestCode", "%" + requestCode.trim().toUpperCase() + "%");

        if (ownerId != null)
            query.setParameter("ownerId", ownerId);

        if (!DataUtil.isNullOrEmpty(shopPath))
            query.setParameter("shopPath", "%" + shopPath + "%");
        query.setParameter("fromDate", fromDate);
        query.setParameter("endDate", toDate);
        List<Object[]> lsRequest = DataUtil.defaultIfNull(query.getResultList(), new ArrayList());
        ArrayList<StockRequestDTO> lsResult = Lists.newArrayList();
        for (Object[] obj : lsRequest) {
            int index = 0;
            StockRequestDTO stockRequestDTO = new StockRequestDTO();
            stockRequestDTO.setStockRequestId(DataUtil.safeToLong(obj[index++]));
            stockRequestDTO.setRequestCode(DataUtil.safeToString(obj[index++]));
            stockRequestDTO.setOwnerType(DataUtil.safeToLong(obj[index++]));
            stockRequestDTO.setOwnerId(DataUtil.safeToLong(obj[index++]));
            stockRequestDTO.setStatus(DataUtil.safeToString(obj[index++]));
            stockRequestDTO.setStatusName(IMServiceUtil.getStatusRequestName(stockRequestDTO.getStatus()));
            stockRequestDTO.setStockTransId(DataUtil.safeToLong(obj[index++]));
            stockRequestDTO.setCreateUser(DataUtil.safeToString(obj[index++]));
            Object date1 = obj[index++];
            stockRequestDTO.setCreateDatetime(date1 != null ? (Date) date1 : null);
            stockRequestDTO.setRequestType(DataUtil.safeToLong(obj[index++]));
            stockRequestDTO.setOwnerCode(DataUtil.safeToString(obj[index++]));
            stockRequestDTO.setOwnerName(DataUtil.safeToString(obj[index++]));
            stockRequestDTO.setSignVoffice(DataUtil.safeToString(obj[index++]));
            stockRequestDTO.setNote(DataUtil.safeToString(obj[index]));
            lsResult.add(stockRequestDTO);
        }

        return lsResult;
    }

    public byte[] getAttachFileContent(Long stockRequestId) throws Exception {
        StringBuilder builder = new StringBuilder(" select file_content from STOCK_REQUEST where stock_request_id= #stockRequestId");
        Query query = em.createNativeQuery(builder.toString());
        query.setParameter("stockRequestId", stockRequestId);
        List lstResult = query.getResultList();
        return !DataUtil.isNullObject(lstResult.get(0)) ? (byte[]) ((byte[]) lstResult.get(0)) : new byte[0];
    }
}
