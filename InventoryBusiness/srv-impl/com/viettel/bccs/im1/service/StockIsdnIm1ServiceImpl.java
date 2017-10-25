package com.viettel.bccs.im1.service;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.model.StockIsdnTrans;
import com.viettel.bccs.inventory.model.StockIsdnTransDetail;
import com.viettel.bccs.inventory.service.ProductOfferingService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.fw.service.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by sinhhv on 3/7/2016.
 */
@Service
public class StockIsdnIm1ServiceImpl extends BaseServiceImpl implements StockIsdnIm1Service {

    @Autowired
    ProductOfferingService productOfferingService;

    BaseMapper<StockIsdnTrans, StockIsdnTransDTO> mapperStockIsdnTrans = new BaseMapper(StockIsdnTrans.class, StockIsdnTransDTO.class);
    BaseMapper<StockIsdnTransDetail, StockIsdnTransDetailDTO> mapperStockIsdnTransDetail = new BaseMapper(StockIsdnTransDetail.class, StockIsdnTransDetailDTO.class);

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM_LIB)
    private EntityManager emIM;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage doSaveStockIsdn(List<StockIsdnTransDTO> lstStockIsdnTrans, List<StockTransDetailDTO> lstStockTransDetail, List<StockIsdnTransDetailDTO> lstStockIsdnTransDetail) throws LogicException, Exception {
        BaseMessage msg = new BaseMessage();
        //them moi stock_trans_isdn
//        for (StockIsdnTransDTO stockIsdnTransDTO : lstStockIsdnTrans) {
//            saveStockIsdnTrans(stockIsdnTransDTO);
//        }
        //cap nhat lai cua hang
        BigInteger fromIsdn;
        BigInteger toIsdn;
        for (StockTransDetailDTO stockTransDetailDTO : lstStockTransDetail) {
            for (StockTransSerialDTO stockTransSerial : stockTransDetailDTO.getLstStockTransSerial()) {
                StringBuilder sqlUpdate = new StringBuilder();
                fromIsdn = new BigInteger(stockTransSerial.getFromSerial());
                toIsdn = new BigInteger(stockTransSerial.getToSerial());
                ProductOfferingDTO product = productOfferingService.findOne(stockTransDetailDTO.getProdOfferId());
                sqlUpdate.append("UPDATE  ");
                if (!DataUtil.isNullObject(product) && !DataUtil.isNullObject(product.getProductOfferTypeId())) {
                    if (product.getProductOfferTypeId().equals(Const.PRODUCT_OFFER_TYPE.MOBILE)) {
                        sqlUpdate.append("bccs_im.stock_isdn_mobile");
                    } else if (product.getProductOfferTypeId().equals(Const.PRODUCT_OFFER_TYPE.HP)) {
                        sqlUpdate.append("bccs_im.stock_isdn_homephone");
                    } else {
                        sqlUpdate.append("bccs_im.stock_isdn_pstn");
                    }
                } else {
                    throw new LogicException("", "export.isdn.has.error.im1");
                }
                sqlUpdate.append("   SET   ");
                sqlUpdate.append("     owner_type = #newOwnerType ");
                sqlUpdate.append("     ,owner_id = #newOwnerId ");
                sqlUpdate.append("     ,last_update_user = #last_update_user ");
                sqlUpdate.append("     ,last_update_ip_address = #last_update_ip_address ");
                sqlUpdate.append("     ,last_update_time = sysdate ");
                sqlUpdate.append(" WHERE    stock_model_id = #prod_offer_id ");
                sqlUpdate.append("   AND    owner_type = #oldOwnerType ");
                sqlUpdate.append("   AND    owner_id = #oldOwnerId ");
                sqlUpdate.append("AND   to_number(isdn) >= #fromIsdn ");
                sqlUpdate.append("AND   to_number(isdn) <= #toIsdn ");
                sqlUpdate.append("AND   status = #status ");
                Query query = emIM.createNativeQuery(sqlUpdate.toString());
                query.setParameter("newOwnerType", lstStockIsdnTrans.get(0).getToOwnerType());
                query.setParameter("newOwnerId", lstStockIsdnTrans.get(0).getToOwnerId());
                query.setParameter("last_update_user", lstStockIsdnTrans.get(0).getLastUpdatedUserCode());
                query.setParameter("last_update_ip_address", lstStockIsdnTrans.get(0).getLastUpdatedUserIp());
                query.setParameter("prod_offer_id", stockTransDetailDTO.getProdOfferId());
                query.setParameter("oldOwnerType", lstStockIsdnTrans.get(0).getFromOwnerType());
                query.setParameter("oldOwnerId", lstStockIsdnTrans.get(0).getFromOwnerId());
                query.setParameter("fromIsdn", fromIsdn);
                query.setParameter("toIsdn", toIsdn);
                query.setParameter("status", Const.STATUS_ACTIVE);

                int count = query.executeUpdate();
                if (count != (toIsdn.subtract(fromIsdn).intValue() + 1)) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "export.isdn.has.error.im1", fromIsdn.toString(), toIsdn.toString());
                }
            }
        }
//        //them moi stock_isdn_trans_detail
//        for (StockIsdnTransDetailDTO stockIsdnTransDetailDTO : lstStockIsdnTransDetail) {
//            saveStockIsdnTransDetail(stockIsdnTransDetailDTO);
//        }
        msg.setSuccess(true);
        return msg;
    }

    @Override
    public BaseMessage doDistributeIsdn(InputDistributeByRangeDTO inputByRange, List<Object[]> listRangePreviewed, List<StockIsdnTrans> lstStockIsdnTrans, List<StockIsdnTransDetail> lstStockIsdnTransDetail) throws LogicException, Exception {
        BaseMessage msg = new BaseMessage();
        Long from;
        Long to;
        for (Object ob[] : listRangePreviewed) {
            Date sysdate = DbUtil.getSysDate(emIM);
            from = DataUtil.safeToLong(ob[3]);
            to = DataUtil.safeToLong(ob[4]);
            String fromOwnerId = DataUtil.safeToString(ob[8]);
            String fromOwnerType = DataUtil.safeToString(ob[11]);
            List params = Lists.newArrayList();
            StringBuilder sql = new StringBuilder();
            sql.append(" update ");
            if (!DataUtil.isNullObject(inputByRange.getTelecomServiceId())) {
                if (inputByRange.getTelecomServiceId().equals(Const.PRODUCT_OFFER_TYPE.MOBILE)) {
                    sql.append("bccs_im.stock_isdn_mobile s ");
                } else if (inputByRange.getTelecomServiceId().equals(Const.PRODUCT_OFFER_TYPE.HP)) {
                    sql.append("bccs_im.stock_isdn_homephone s ");
                } else {
                    sql.append("bccs_im.stock_isdn_pstn s ");
                }
            } else {
                throw new LogicException("", "export.isdn.has.error.im1");
            }
            sql.append("   SET   s.owner_id = ?, s.owner_type = ?, s.last_update_user = ?, " +
                    "s.last_update_ip_address = ?, s.last_update_time = ?");
            params.add(inputByRange.getToOwnerId());
            params.add(inputByRange.getToOwnerType());
            params.add(inputByRange.getUserCode());
            params.add(inputByRange.getUserIp());
            params.add(sysdate);
            sql.append(" WHERE   s.isdn >= ? AND s.isdn <= ? AND s.status in (1,3) ");//bo AND s.telecom_service_id = ?
            params.add(from);
            params.add(to);
//            if (inputByRange.getTelecomServiceId().equals(Const.PRODUCT_OFFER_TYPE.MOBILE)) {
//                params.add(Const.PRODUCT_OFFER_TYPE.MOBILE);
//            } else if (inputByRange.getTelecomServiceId().equals(Const.PRODUCT_OFFER_TYPE.HP)) {
//                params.add(Const.PRODUCT_OFFER_TYPE.HP);
//            } else {
//                params.add(Const.PRODUCT_OFFER_TYPE.PSTN);
//            }
            sql.append(" AND owner_id = ? ");
            params.add(fromOwnerId);
            sql.append(" AND owner_type = ?  ");
            params.add(fromOwnerType);
            sql.append("AND ( ( exists (SELECT 'X' FROM bccs_im.SHOP WHERE shop_id = s.owner_id and (shop_id = ? or shop_path like ?)  ) and s.owner_type=1 ) or ( exists  (SELECT 'X' FROM bccs_im.STAFF WHERE staff_id = s.owner_id and status=1  and shop_id in  (select shop_id from bccs_im.shop where shop_id = ? or shop_path like ?) ) and s.owner_type=2 ) )");
            params.add(inputByRange.getCurrentShopid());
            params.add(inputByRange.getCurrentShopPath() + "%");
            params.add(inputByRange.getCurrentShopid());
            params.add(inputByRange.getCurrentShopPath() + "%");
            Query query = emIM.createNativeQuery(sql.toString());
            for (int i = 0; i < params.size(); i++) {
                query.setParameter(i + 1, params.get(i));
            }
            long result = query.executeUpdate();
            if (result != (to - from + 1)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "export.isdn.has.error.im1", from, to);
            }
        }
//        for (StockIsdnTrans stockIsdnTrans : lstStockIsdnTrans) {
//            if (inputByRange.getTelecomServiceId().equals(Const.PRODUCT_OFFER_TYPE.MOBILE)) {
//                stockIsdnTrans.setStockTypeName("STOCK_ISDN_MOBILE");
//            } else if (inputByRange.getTelecomServiceId().equals(Const.PRODUCT_OFFER_TYPE.HP)) {
//                stockIsdnTrans.setStockTypeName("STOCK_ISDN_HOMEPHONE");
//            } else {
//                stockIsdnTrans.setStockTypeName("STOCK_ISDN_PSTN");
//            }
//            saveStockIsdnTrans(mapperStockIsdnTrans.toDtoBean(stockIsdnTrans));
//        }
//        for (StockIsdnTransDetail stockIsdnTransDetail : lstStockIsdnTransDetail) {
//            saveStockIsdnTransDetail(mapperStockIsdnTransDetail.toDtoBean(stockIsdnTransDetail));
//        }
        msg.setSuccess(true);
        return msg;
    }

    @Override
    public BaseMessage doDistriButeIsdnByFile(List<Object[]> listIsdn, InputDistributeByFileDTO inputByFile, List<StockIsdnTrans> lstStockIsdnTrans, List<StockIsdnTransDetail> lstStockIsdnTransDetail) throws LogicException, Exception {
        BaseMessage msg = new BaseMessage();
        Gson gson = new Gson();
        Type type = new TypeToken<VShopStaffDTO>() {
        }.getType();
        Date sysdate;
        String isdn;
        sysdate = DbUtil.getSysDate(emIM);
        for (Object[] ob : listIsdn) {
            isdn = ob[0].toString();
            List params = Lists.newArrayList();
            VShopStaffDTO toOwner = gson.fromJson(ob[1].toString(), type);
            StringBuilder sql = new StringBuilder();
            VShopStaffDTO fromOwner = gson.fromJson(ob[3].toString(), type);
            sql.append(" update ");
            if (!DataUtil.isNullObject(inputByFile.getTelecomServiceId())) {
                if (inputByFile.getTelecomServiceId().equals(Const.PRODUCT_OFFER_TYPE.MOBILE)) {
                    sql.append("bccs_im.stock_isdn_mobile s ");
                } else if (inputByFile.getTelecomServiceId().equals(Const.PRODUCT_OFFER_TYPE.HP)) {
                    sql.append("bccs_im.stock_isdn_homephone s ");
                } else {
                    sql.append("bccs_im.stock_isdn_pstn s ");
                }
            } else {
                throw new LogicException("", "export.isdn.has.error.im1");
            }
            sql.append(" set s.owner_id = ? ,s.owner_type = ? , s.last_update_user = ?, s.last_update_ip_address = ?, s.last_update_time = ? ");
            params.add(toOwner.getOwnerId());
            params.add(toOwner.getOwnerType());
            params.add(inputByFile.getUserCreate());
            params.add(inputByFile.getUserIp());
            params.add(sysdate);
            sql.append(" where s.isdn=? AND STATUS IN(1,3)");
            params.add(DataUtil.safeToLong(isdn));
            sql.append(" AND owner_id = ? ");
            params.add(fromOwner.getOwnerId());
            sql.append(" AND owner_type = ?  ");
            params.add(fromOwner.getOwnerType());
            sql.append("AND ( ( exists (SELECT 'X' FROM bccs_im.SHOP WHERE shop_id = s.owner_id and (shop_id = ? or shop_path like ?)  ) and s.owner_type=1 ) or ( exists  (SELECT 'X' FROM bccs_im.STAFF WHERE staff_id = s.owner_id and status=1  and shop_id in  (select shop_id from bccs_im.shop where shop_id = ? or shop_path like ?) ) and s.owner_type=2 ) )");
            params.add(inputByFile.getFromOwnerId());
            params.add(inputByFile.getFromShopPath() + "%");
            params.add(inputByFile.getFromOwnerId());
            params.add(inputByFile.getFromShopPath() + "%");

            Query query = emIM.createNativeQuery(sql.toString());
            for (int i = 0; i < params.size(); i++) {
                query.setParameter(i + 1, params.get(i));
            }

            int numRowUpdate = query.executeUpdate();
            if (numRowUpdate == 0) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "export.isdn.has.error.im1", isdn, isdn);
            }
        }
//        for (StockIsdnTrans stockIsdnTrans : lstStockIsdnTrans) {
//            if (inputByFile.getTelecomServiceId().equals(Const.PRODUCT_OFFER_TYPE.MOBILE)) {
//                stockIsdnTrans.setStockTypeName("STOCK_ISDN_MOBILE");
//            } else if (inputByFile.getTelecomServiceId().equals(Const.PRODUCT_OFFER_TYPE.HP)) {
//                stockIsdnTrans.setStockTypeName("STOCK_ISDN_HOMEPHONE");
//            } else {
//                stockIsdnTrans.setStockTypeName("STOCK_ISDN_PSTN");
//            }
//            saveStockIsdnTrans(mapperStockIsdnTrans.toDtoBean(stockIsdnTrans));
//        }
//        for (StockIsdnTransDetail stockIsdnTransDetail : lstStockIsdnTransDetail) {
//            saveStockIsdnTransDetail(mapperStockIsdnTransDetail.toDtoBean(stockIsdnTransDetail));
//        }
        msg.setSuccess(true);
        return msg;
    }

    @Transactional(rollbackFor = Exception.class)
    public void lockOrUnlockIsdn(String isdn, Long newStatus, List<Long> lstStatus, StaffDTO staff) throws LogicException, Exception {
        //Cap nhat so
        StringBuilder strUpdateQuery = new StringBuilder("");
        HashMap<String, Object> listParameter = new HashMap<>();

        strUpdateQuery.append("UPDATE   bccs_im.stock_isdn_mobile ");

        strUpdateQuery.append("   SET   status = #newStatus, ");
        listParameter.put("newStatus", newStatus);

        if (DataUtil.isNullObject(staff)) {
            strUpdateQuery.append("  lock_by_staff = null, ");
        } else {
            strUpdateQuery.append("  lock_by_staff = #lockByStaff, last_update_user = #lastUpdateUser, ");
            listParameter.put("lockByStaff", staff.getStaffId());
            listParameter.put("lastUpdateUser", staff.getStaffCode());
        }

        strUpdateQuery.append("  last_update_time = sysdate ");

        strUpdateQuery.append(" WHERE  to_number(isdn) = to_number(#isdn) ");
        listParameter.put("isdn", isdn);

        strUpdateQuery.append("  AND  status " + DbUtil.createInQuery("status", lstStatus));
//        listParameter.put("listStatus", lstStatus);
        Query query = emIM.createNativeQuery(strUpdateQuery.toString());
        for (HashMap.Entry<String, Object> param : listParameter.entrySet()) {
            query.setParameter(param.getKey(), param.getValue());
        }
        DbUtil.setParamInQuery(query, "status", lstStatus);
        int result = query.executeUpdate();

        if (result == 0) {
            throw new LogicException("", "hotline1.update.isdn.fail");
        }
    }
//
//    public int saveStockIsdnTrans(StockIsdnTransDTO stockIsdnTransDTO) throws LogicException, Exception {
//        StringBuilder sql = new StringBuilder();
//        sql.append("INSERT INTO bccs_im.stock_isdn_trans(stock_isdn_trans_id, stock_isdn_trans_code,");
//        sql.append("       from_owner_type, from_owner_id, from_owner_code,");
//        sql.append("       from_owner_name, to_owner_type, to_owner_id,");
//        sql.append("       to_owner_code, to_owner_name, stock_type_id,");
//        sql.append("       stock_type_name, quantity, status, reason_id,");
//        sql.append("       reason_code, reason_name, note, created_time,");
//        sql.append("       created_user_id, created_user_code, created_user_name,");
//        sql.append("       created_user_ip, last_updated_time, last_updated_user_id,");
//        sql.append("       last_updated_user_code, last_updated_user_name,");
//        sql.append("       last_updated_user_ip)");
//        sql.append(" VALUES (:stock_isdn_trans_id, :stock_isdn_trans_code,");
//        sql.append("       :from_owner_type, :from_owner_id, :from_owner_code,");
//        sql.append("       :from_owner_name, :to_owner_type, :to_owner_id,");
//        sql.append("       :to_owner_code, :to_owner_name, :stock_type_id,");
//        sql.append("       :stock_type_name, :quantity, :status, :reason_id,");
//        sql.append("       :reason_code, :reason_name, :note, :created_time,");
//        sql.append("       :created_user_id, :created_user_code, :created_user_name,");
//        sql.append("       :created_user_ip, :last_updated_time, :last_updated_user_id,");
//        sql.append("       :last_updated_user_code, :last_updated_user_name,");
//        sql.append("       :last_updated_user_ip)");
//        Query query = emIM.createNativeQuery(sql.toString());
//        query.setParameter("stock_isdn_trans_id", stockIsdnTransDTO.getStockIsdnTransId());
//        query.setParameter("stock_isdn_trans_code", stockIsdnTransDTO.getStockIsdnTransCode());
//        query.setParameter("from_owner_type", stockIsdnTransDTO.getFromOwnerType());
//        query.setParameter("from_owner_id", stockIsdnTransDTO.getFromOwnerId());
//        query.setParameter("from_owner_code", stockIsdnTransDTO.getFromOwnerCode());
//        query.setParameter("from_owner_name", stockIsdnTransDTO.getFromOwnerName());
//        query.setParameter("to_owner_type", stockIsdnTransDTO.getToOwnerType());
//        query.setParameter("to_owner_id", stockIsdnTransDTO.getToOwnerId());
//        query.setParameter("to_owner_code", stockIsdnTransDTO.getToOwnerCode());
//        query.setParameter("to_owner_name", stockIsdnTransDTO.getToOwnerName());
//        query.setParameter("stock_type_id", stockIsdnTransDTO.getStockTypeId());
//        query.setParameter("stock_type_name", stockIsdnTransDTO.getStockTypeName());
//        query.setParameter("quantity", stockIsdnTransDTO.getQuantity());
//        query.setParameter("status", stockIsdnTransDTO.getStatus());
//        query.setParameter("reason_id", stockIsdnTransDTO.getReasonId());
//        query.setParameter("reason_code", stockIsdnTransDTO.getReasonCode());
//        query.setParameter("reason_name", stockIsdnTransDTO.getReasonName());
//        query.setParameter("note", stockIsdnTransDTO.getNote());
//        query.setParameter("created_time", stockIsdnTransDTO.getCreatedTime());
//        query.setParameter("created_user_id", stockIsdnTransDTO.getCreatedUserId());
//        query.setParameter("created_user_code", stockIsdnTransDTO.getCreatedUserCode());
//        query.setParameter("created_user_name", stockIsdnTransDTO.getCreatedUserName());
//        query.setParameter("created_user_ip", stockIsdnTransDTO.getCreatedUserIp());
//        query.setParameter("last_updated_time", stockIsdnTransDTO.getLastUpdatedTime());
//        query.setParameter("last_updated_user_id", stockIsdnTransDTO.getLastUpdatedUserId());
//        query.setParameter("last_updated_user_code", stockIsdnTransDTO.getLastUpdatedUserCode());
//        query.setParameter("last_updated_user_name", stockIsdnTransDTO.getLastUpdatedUserName());
//        query.setParameter("last_updated_user_ip", stockIsdnTransDTO.getLastUpdatedUserIp());
//        return query.executeUpdate();
//    }

//    public int saveStockIsdnTransDetail(StockIsdnTransDetailDTO stockIsdnTransDetailDTO) throws LogicException, Exception {
//        StringBuilder sql = new StringBuilder();
//        sql.append("INSERT INTO bccs_im.stock_isdn_trans_detail(stock_isdn_trans_detail_id, stock_isdn_trans_id, from_isdn,");
//        sql.append("       to_isdn, quantity, created_time, length_isdn,");
//        sql.append("       type_isdn)");
//        sql.append("VALUES(:stock_isdn_trans_detail_id, :stock_isdn_trans_id, :from_isdn,");
//        sql.append("       :to_isdn, :quantity, :created_time, :length_isdn,");
//        sql.append("       :type_isdn)");
//        Query query = emIM.createNativeQuery(sql.toString());
//        query.setParameter("stock_isdn_trans_detail_id", stockIsdnTransDetailDTO.getStockIsdnTransDetailId());
//        query.setParameter("stock_isdn_trans_id", stockIsdnTransDetailDTO.getStockIsdnTransId());
//        query.setParameter("from_isdn", stockIsdnTransDetailDTO.getFromIsdn());
//        query.setParameter("to_isdn", stockIsdnTransDetailDTO.getToIsdn());
//        query.setParameter("quantity", stockIsdnTransDetailDTO.getQuantity());
//        query.setParameter("created_time", stockIsdnTransDetailDTO.getCreatedTime());
//        query.setParameter("length_isdn", DataUtil.defaultIfNull(stockIsdnTransDetailDTO.getLengthIsdn(), ""));
//        query.setParameter("type_isdn", DataUtil.defaultIfNull(stockIsdnTransDetailDTO.getTypeIsdn(), ""));
//        return query.executeUpdate();
//    }

}
