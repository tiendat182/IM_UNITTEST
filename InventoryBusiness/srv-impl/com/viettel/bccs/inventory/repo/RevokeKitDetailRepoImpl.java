package com.viettel.bccs.inventory.repo;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.ReasonDTO;
import com.viettel.bccs.inventory.dto.RevokeKitDetailDTO;
import com.viettel.bccs.inventory.dto.RevokeKitFileImportDTO;
import com.viettel.bccs.inventory.model.RequestLiquidateSerial;
import com.viettel.bccs.inventory.model.RevokeKitDetail;
import com.viettel.bccs.sale.dto.SubscriberDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.model.RevokeKitDetail.COLUMNS;
import com.viettel.bccs.inventory.model.QRevokeKitDetail;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.mapper.BaseMapper;
import org.apache.log4j.Logger;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public class RevokeKitDetailRepoImpl implements RevokeKitDetailRepoCustom {

    public static final Logger logger = Logger.getLogger(RevokeKitDetailRepoCustom.class);

    private static final BaseMapper<RevokeKitDetail, RevokeKitDetailDTO> mapper = new BaseMapper<>(RevokeKitDetail.class, RevokeKitDetailDTO.class);

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_SALE)
    private EntityManager emSale;

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QRevokeKitDetail revokeKitDetail = QRevokeKitDetail.revokeKitDetail;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case ACCOUNTBOOKBANKPLUSID:
                        expression = DbUtil.createLongExpression(revokeKitDetail.accountBookBankplusId, filter);
                        break;
                    case ACTSTATUS:
                        expression = DbUtil.createStringExpression(revokeKitDetail.actStatus, filter);
                        break;
                    case ADDMONEYRESULT:
                        expression = DbUtil.createStringExpression(revokeKitDetail.addMoneyResult, filter);
                        break;
                    case ADDMONEYSTATUS:
                        expression = DbUtil.createLongExpression(revokeKitDetail.addMoneyStatus, filter);
                        break;
                    case CREATEDATE:
                        expression = DbUtil.createDateExpression(revokeKitDetail.createDate, filter);
                        break;
                    case DATAPRODUCTCODE:
                        expression = DbUtil.createStringExpression(revokeKitDetail.dataProductCode, filter);
                        break;
                    case DATAREGISTERDATE:
                        expression = DbUtil.createDateExpression(revokeKitDetail.dataRegisterDate, filter);
                        break;
                    case ID:
                        expression = DbUtil.createLongExpression(revokeKitDetail.id, filter);
                        break;
                    case ISDN:
                        expression = DbUtil.createStringExpression(revokeKitDetail.isdn, filter);
                        break;
                    case MAINBALANCE:
                        expression = DbUtil.createLongExpression(revokeKitDetail.mainBalance, filter);
                        break;
                    case ORGOWNERCODE:
                        expression = DbUtil.createStringExpression(revokeKitDetail.orgOwnerCode, filter);
                        break;
                    case ORGOWNERID:
                        expression = DbUtil.createLongExpression(revokeKitDetail.orgOwnerId, filter);
                        break;
                    case ORGOWNERTYPE:
                        expression = DbUtil.createLongExpression(revokeKitDetail.orgOwnerType, filter);
                        break;
                    case PRICE:
                        expression = DbUtil.createLongExpression(revokeKitDetail.price, filter);
                        break;
                    case PRODOFFERID:
                        expression = DbUtil.createLongExpression(revokeKitDetail.prodOfferId, filter);
                        break;
                    case PRODUCTCODE:
                        expression = DbUtil.createStringExpression(revokeKitDetail.productCode, filter);
                        break;
                    case PROMOTIONBALANCE:
                        expression = DbUtil.createLongExpression(revokeKitDetail.promotionBalance, filter);
                        break;
                    case REGISTERSTATUS:
                        expression = DbUtil.createLongExpression(revokeKitDetail.registerStatus, filter);
                        break;
                    case REVOKEDATE:
                        expression = DbUtil.createDateExpression(revokeKitDetail.revokeDate, filter);
                        break;
                    case REVOKEDESCRIPTION:
                        expression = DbUtil.createStringExpression(revokeKitDetail.revokeDescription, filter);
                        break;
                    case STADATETIME:
                        expression = DbUtil.createDateExpression(revokeKitDetail.staDatetime, filter);
                        break;
                    case REVOKEKITTRANSID:
                        expression = DbUtil.createLongExpression(revokeKitDetail.revokeKitTransId, filter);
                        break;
                    case REVOKESTATUS:
                        expression = DbUtil.createLongExpression(revokeKitDetail.revokeStatus, filter);
                        break;
                    case REVOKETYPE:
                        expression = DbUtil.createLongExpression(revokeKitDetail.revokeType, filter);
                        break;
                    case SALEDATE:
                        expression = DbUtil.createDateExpression(revokeKitDetail.saleDate, filter);
                        break;
                    case SALEDISCOUNT:
                        expression = DbUtil.createLongExpression(revokeKitDetail.saleDiscount, filter);
                        break;
                    case SALEPRICE:
                        expression = DbUtil.createLongExpression(revokeKitDetail.salePrice, filter);
                        break;
                    case SALETRANSID:
                        expression = DbUtil.createLongExpression(revokeKitDetail.saleTransId, filter);
                        break;
                    case SERIAL:
                        expression = DbUtil.createStringExpression(revokeKitDetail.serial, filter);
                        break;
                    case STOCKTRANSID:
                        expression = DbUtil.createLongExpression(revokeKitDetail.stockTransId, filter);
                        break;
                    case VERIFYDESCRIPTION:
                        expression = DbUtil.createStringExpression(revokeKitDetail.verifyDescription, filter);
                        break;
                    case VERIFYSTATUS:
                        expression = DbUtil.createLongExpression(revokeKitDetail.verifyStatus, filter);
                        break;
                    case VERIFYTYPE:
                        expression = DbUtil.createLongExpression(revokeKitDetail.verifyType, filter);
                        break;
                    case REASONCODE:
                        expression = DbUtil.createStringExpression(revokeKitDetail.reasonCode, filter);
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
    public List<RevokeKitDetailDTO> searchRevokeKitDetailByShopAndDate(Long shopId, Date fromDate, Date toDate) throws LogicException, Exception {
        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append("	SELECT   rkd.org_owner_code, ");
        sqlQuery.append("			 rkd.isdn, ");
        sqlQuery.append("			 rkd.serial, ");
        sqlQuery.append("			 rkd.product_code,");
        sqlQuery.append("			 rkd.main_balance,");
        sqlQuery.append("			 rkd.promotion_balance,");
        sqlQuery.append("			 rkd.data_register_date,");
        sqlQuery.append("			 rkd.revoke_type, ");
        sqlQuery.append("			 DECODE (rkd.revoke_type, ");
        sqlQuery.append("					 1, 'Kênh không giữ lại', ");
        sqlQuery.append("					 2, 'Kênh có giữ lại', ");
        sqlQuery.append("					 'Loại khác') ");
        sqlQuery.append("				 revoke_type_name, ");
        sqlQuery.append("			 rkd.revoke_status, ");
        sqlQuery.append("			 DECODE (rkd.revoke_status, ");
        sqlQuery.append("					 0, 'Chưa thu hồi', ");
        sqlQuery.append("					 1, 'Thu hồi thành công', ");
        sqlQuery.append("					 'Thu hồi thất bại') ");
        sqlQuery.append("				 revoke_status_name, ");
        sqlQuery.append("			 rkd.revoke_date,");
        sqlQuery.append("			 rkd.revoke_description, ");
        sqlQuery.append("			 rkd.data_product_code, ");
        sqlQuery.append("			 act_status, ");
        sqlQuery.append("			 DECODE (act_status, '0', 'Chưa kích hoạt', 'Đã kích hoạt') ");
        sqlQuery.append("				 act_status_name, ");
        sqlQuery.append("			 rkd.sale_date,");
        sqlQuery.append("			 rkd.price,");
        sqlQuery.append("			rkd.add_money_status, ");
        sqlQuery.append("			 rkd.add_money_result,");
        sqlQuery.append("				 rkd.verify_status,");
        sqlQuery.append("			 DECODE (rkd.verify_status, ");
        sqlQuery.append("					 0, 'Chưa kiểm tra', ");
        sqlQuery.append("					 1, 'Kiểm tra thành công', ");
        sqlQuery.append("					 'Kiểm tra thất bại') ");
        sqlQuery.append("				 verify_status_name, ");
        sqlQuery.append("			 rkd.verify_description    , " +
                "                    rkd.verify_date, " +
                "                    rkd.last_modify, " +
                "                    rkd.org_owner_type, " +
                "                    rkd.TK_10, " +
                "                    rkd.TK_34, " +
                "                    rkd.TK_27, " +
                "                    rkd.TK_46, " +
                "                    rkd.REGISTER_STATUS, " +
                "                    rkd.tk_data, " +
                "                    rkd.sta_datetime, " +
                "                    rkd.reason_code ");
        sqlQuery.append("	  FROM   bccs_im.revoke_kit_detail rkd, bccs_im.staff sf, bccs_im.shop sh ");
        sqlQuery.append("	 WHERE       rkd.org_owner_type = 2 ");
        sqlQuery.append("			 AND rkd.org_owner_id = sf.staff_id ");
        sqlQuery.append("			 AND sf.shop_id = sh.shop_id ");
        sqlQuery.append("			 AND (sh.shop_id = #shopId OR sh.shop_path LIKE #shopId) ");
        if (fromDate != null) {
            sqlQuery.append("			 AND rkd.revoke_date >= TRUNC (#fromDate)");
        }
        if (toDate != null) {
            sqlQuery.append("			 AND rkd.revoke_date < TRUNC (#toDate) + 1         ");
        }
        sqlQuery.append("	UNION ALL ");
        sqlQuery.append("	SELECT   rkd.org_owner_code, ");
        sqlQuery.append("			 rkd.isdn, ");
        sqlQuery.append("			 rkd.serial, ");
        sqlQuery.append("			 rkd.product_code,");
        sqlQuery.append("			 rkd.main_balance,");
        sqlQuery.append("			 rkd.promotion_balance,");
        sqlQuery.append("			 rkd.data_register_date, ");
        sqlQuery.append("			 rkd.revoke_type, ");
        sqlQuery.append("			 DECODE (rkd.revoke_type, ");
        sqlQuery.append("					 1, 'Kênh không giữ lại', ");
        sqlQuery.append("					 2, 'Kênh có giữ lại', ");
        sqlQuery.append("					 'Loại khác') ");
        sqlQuery.append("				 revoke_type_name, ");
        sqlQuery.append("			 rkd.revoke_status, ");
        sqlQuery.append("			 DECODE (rkd.revoke_status, ");
        sqlQuery.append("					 0, 'Chưa thu hồi', ");
        sqlQuery.append("					 1, 'Thu hồi thành công', ");
        sqlQuery.append("					 'Thu hồi thất bại') ");
        sqlQuery.append("				 revoke_status_name, ");
        sqlQuery.append("			 rkd.revoke_date,");
        sqlQuery.append("			 rkd.revoke_description,");
        sqlQuery.append("			 rkd.data_product_code, ");
        sqlQuery.append("			 act_status, ");
        sqlQuery.append("			 DECODE (act_status, '0', 'Chưa kích hoạt', 'Đã kích hoạt') ");
        sqlQuery.append("				 act_status_name, ");
        sqlQuery.append("			 rkd.sale_date,                                ");
        sqlQuery.append("			 rkd.price,                                  ");
        sqlQuery.append("			rkd.add_money_status, ");
        sqlQuery.append("			 rkd.add_money_result,                            ");
        sqlQuery.append("				 rkd.verify_status,                           ");
        sqlQuery.append("			 DECODE (rkd.verify_status, ");
        sqlQuery.append("					 0, 'Chưa kiểm tra', ");
        sqlQuery.append("					 1, 'Kiểm tra thành công', ");
        sqlQuery.append("					 'Kiểm tra thất bại') ");
        sqlQuery.append("				 verify_status_name, ");
        sqlQuery.append("			 rkd.verify_description, ");
        sqlQuery.append("			 rkd.verify_date, " +
                "                    rkd.last_modify, " +
                "                    rkd.org_owner_type, " +
                "                    rkd.TK_10, " +
                "                    rkd.TK_34, " +
                "                    rkd.TK_27, " +
                "                    rkd.TK_46, " +
                "                    rkd.REGISTER_STATUS, " +
                "                    rkd.tk_data, " +
                "                    rkd.sta_datetime, " +
                "                    rkd.reason_code " +
                "                     ");
        sqlQuery.append("	  FROM   bccs_im.revoke_kit_detail rkd, bccs_im.shop sh ");
        sqlQuery.append("	 WHERE       rkd.org_owner_type = 1 ");
        sqlQuery.append("			 AND rkd.org_owner_id = sh.shop_id ");
        sqlQuery.append("			 AND (sh.shop_id = #shopId OR sh.shop_path LIKE #shopId)  ");
        if (fromDate != null) {
            sqlQuery.append("			 AND rkd.revoke_date >= TRUNC (#fromDate) ");
        }
        if (toDate != null) {
            sqlQuery.append("			 AND rkd.revoke_date < TRUNC (#toDate) + 1 ");
        }

        Query query = em.createNativeQuery(sqlQuery.toString());
        query.setParameter("shopId", shopId);
        if (fromDate != null) {
            query.setParameter("fromDate", fromDate);
        }
        if (toDate != null) {
            query.setParameter("toDate", toDate);
        }

        List<Object[]> data = query.getResultList();
        List<RevokeKitDetailDTO> results = Lists.newArrayList();

        if (!DataUtil.isNullOrEmpty(data)) {
            int index;
            Object tmpDate;
            for (Object[] obj : data) {
                index = 0;
                RevokeKitDetailDTO kitDetailDTO = new RevokeKitDetailDTO();
                kitDetailDTO.setOrgOwnerCode(DataUtil.safeToString(obj[index++]));
                kitDetailDTO.setIsdn(DataUtil.safeToString(obj[index++]));
                kitDetailDTO.setSerial(DataUtil.safeToString(obj[index++]));
                kitDetailDTO.setProductCode(DataUtil.safeToString(obj[index++]));
                kitDetailDTO.setMainBalance(DataUtil.safeToLong(obj[index++]));
                kitDetailDTO.setPromotionBalance(DataUtil.safeToLong(obj[index++]));
                tmpDate = obj[index++];
                kitDetailDTO.setDataRegisterDate(tmpDate != null ? (Date) tmpDate : null);
                kitDetailDTO.setRevokeType(DataUtil.safeToLong(obj[index++]));
                kitDetailDTO.setRevokeTypeName(DataUtil.safeToString(obj[index++]));
                kitDetailDTO.setRevokeStatus(DataUtil.safeToLong(obj[index++]));
                kitDetailDTO.setRevokeStatusName(DataUtil.safeToString(obj[index++]));
                tmpDate = obj[index++];
                kitDetailDTO.setRevokeDate(tmpDate != null ? (Date) tmpDate : null);
                kitDetailDTO.setRevokeDescription(DataUtil.safeToString(obj[index++]));
                kitDetailDTO.setDataProductCode(DataUtil.safeToString(obj[index++]));
                kitDetailDTO.setActStatus(DataUtil.safeToString(obj[index++]));
                kitDetailDTO.setActStatusName(DataUtil.safeToString(obj[index++]));
                tmpDate = obj[index++];
                kitDetailDTO.setSaleDate(tmpDate != null ? (Date) tmpDate : null);
                kitDetailDTO.setPrice(DataUtil.safeToLong(obj[index++]));
                kitDetailDTO.setAddMoneyStatus(DataUtil.safeToLong(obj[index++]));
                kitDetailDTO.setAddMoneyResult(DataUtil.safeToString(obj[index++]));
                kitDetailDTO.setVerifyStatus(DataUtil.safeToLong(obj[index++]));
                kitDetailDTO.setVerifyStatusName(DataUtil.safeToString(obj[index++]));
                kitDetailDTO.setVerifyDescription(DataUtil.safeToString(obj[index++]));
                tmpDate = obj[index++];
                kitDetailDTO.setVerifyDate(tmpDate != null ? (Date) tmpDate : null);
                tmpDate = obj[index++];
                kitDetailDTO.setLastModify(tmpDate != null ? (Date) tmpDate : null);
                kitDetailDTO.setOrgOwnerType(DataUtil.safeToLong(obj[index++]));
                kitDetailDTO.setTk10(DataUtil.safeToLong(obj[index++]));
                kitDetailDTO.setTk34(DataUtil.safeToLong(obj[index++]));
                kitDetailDTO.setTk27(DataUtil.safeToLong(obj[index++]));
                kitDetailDTO.setTk46(DataUtil.safeToLong(obj[index++]));
                kitDetailDTO.setRegisterStatus(DataUtil.safeToLong(obj[index++]));
                kitDetailDTO.setTkData(DataUtil.safeToLong(obj[index++]));
                tmpDate = obj[index++];
                kitDetailDTO.setStaDatetime(tmpDate != null ? (Date) tmpDate : null);
                kitDetailDTO.setReasonCode(DataUtil.safeToString(obj[index++]));
                results.add(kitDetailDTO);
            }
        }
        return results;
    }

    // khong duoc dat Transaction: su dung trycatch bat Transaction
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public List<RevokeKitDetailDTO> findRevokeDetailBySerialAndIsdn(String serial, String isdn) throws LogicException, Exception {
        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append(" SELECT   * ");
        sqlQuery.append("	  FROM   revoke_kit_detail ");
        sqlQuery.append("	 WHERE   TO_NUMBER (serial) = #serial AND isdn = #isdn ");
//                "AND nvl(revoke_status, 0) <> 1  ");//Trang thai chua thu hoi hoac thu hoi that bai
        sqlQuery.append(" FOR UPDATE   NOWAIT ");
        Query query = em.createNativeQuery(sqlQuery.toString(), RevokeKitDetail.class);
        query.setParameter("serial", new BigDecimal(serial));
        query.setParameter("isdn", isdn);
        List<RevokeKitDetail> data = query.getResultList();
        if (!DataUtil.isNullOrEmpty(data)) {
            return mapper.toDtoBean(data);
        }
        return Lists.newArrayList();
    }

    @Override
    public List<RevokeKitDetailDTO> findRevokeDetailByListSearch(List<RevokeKitFileImportDTO> lsRevokeSearch) throws LogicException, Exception {
        if (DataUtil.isNullOrEmpty(lsRevokeSearch)) {
            return Lists.newArrayList();
        }
        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append(" SELECT   * ");
        sqlQuery.append("	  FROM   revoke_kit_detail ");
        sqlQuery.append("	 WHERE   1=1 AND (");
        int index = 0;
        for (RevokeKitFileImportDTO importDTO : lsRevokeSearch) {
            if (index != 0) {
                sqlQuery.append(" OR ");
            }
            if (!DataUtil.isNullOrEmpty(importDTO.getIsdn()) && !DataUtil.isNullOrEmpty(importDTO.getSerial())) {
                sqlQuery.append(" (TO_NUMBER (serial) = #serial" + index + " AND isdn = #isdn" + index + ") ");
            } else if (!DataUtil.isNullOrEmpty(importDTO.getIsdn())) {
                sqlQuery.append(" (isdn = #isdn" + index + ") ");
            } else if (!DataUtil.isNullOrEmpty(importDTO.getSerial())) {
                sqlQuery.append(" (TO_NUMBER (serial) = #serial" + index + ") ");
            }
            index++;
        }
        sqlQuery.append("	) ");
        Query query = em.createNativeQuery(sqlQuery.toString(), RevokeKitDetail.class);
        index = 0;
        for (RevokeKitFileImportDTO importDTO : lsRevokeSearch) {
            if (!DataUtil.isNullOrEmpty(importDTO.getIsdn()) && !DataUtil.isNullOrEmpty(importDTO.getSerial())) {
                query.setParameter("serial" + index, new BigDecimal(importDTO.getSerial()));
                query.setParameter("isdn" + index, importDTO.getIsdn());
            } else if (!DataUtil.isNullOrEmpty(importDTO.getIsdn())) {
                query.setParameter("isdn" + index, importDTO.getIsdn());
            } else if (!DataUtil.isNullOrEmpty(importDTO.getSerial())) {
                query.setParameter("serial" + index, new BigDecimal(importDTO.getSerial()));
            }
            index++;
        }

        List<RevokeKitDetail> data = query.getResultList();
        if (!DataUtil.isNullOrEmpty(data)) {
            return mapper.toDtoBean(data);
        }
        return Lists.newArrayList();
    }

    @Override
    public List<SubscriberDTO> findSubsToRevokeBySerialAndIsdnWithReason(String isdn, String serial) throws LogicException, Exception {
        if (DataUtil.isNullOrEmpty(isdn)
                || DataUtil.isNullOrEmpty(serial)) {
            return new ArrayList<>();
        }
        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append("  SELECT   sub.sub_id, " +
                "         sub.isdn, " +
                "         sub.serial, " +
                "    sub.product_code, " +
                "         sub.status, " +
                "         sub.act_status, " +
                "         sub.sta_datetime, " +
                "         rn.reason_code " +
                "  FROM   bccs_sale.subscriber sub, bccs_product.reason rn " +
                " WHERE       sub.isdn = #isdn " +
                "         AND sub.serial = #serial " +
                "   AND sub.reg_type_id = rn.reason_id " +
                "   ORDER BY sub.ACTIVE_DATETIME DESC ");
        Query query = emSale.createNativeQuery(sqlQuery.toString());
        query.setParameter("isdn", isdn);
        query.setParameter("serial", serial);
        List<Object[]> result = query.getResultList();
        List<SubscriberDTO> lstSub = new ArrayList<>();
        SubscriberDTO sub;
        if (!DataUtil.isNullOrEmpty(result)) {
            for (Object[] obj : result) {
                int i = 0;
                sub = new SubscriberDTO();
                sub.setSubId(DataUtil.safeToLong(obj[i++]));
                sub.setIsdn(DataUtil.safeToString(obj[i++]));
                sub.setSerial(DataUtil.safeToString(obj[i++]));
                sub.setProductCode(DataUtil.safeToString(obj[i++]));
                sub.setStatus(DataUtil.safeToLong(obj[i++]));
                sub.setActStatus(DataUtil.safeToString(obj[i++]));
                Object tmpDate = obj[i++];
                sub.setStaDateTime(!DataUtil.isNullObject(tmpDate) ? (Date) tmpDate : null);
                sub.setReasonCode(DataUtil.safeToString(obj[i++]));
                lstSub.add(sub);
            }
        }
        return DataUtil.defaultIfNull(lstSub, new ArrayList<>());
    }

    @Override
    public List<SubscriberDTO> findSubsToRevokeBySerialAndIsdn(String isdn, String serial) throws LogicException, Exception {
        if (DataUtil.isNullOrEmpty(isdn)
                || DataUtil.isNullOrEmpty(serial)) {
            return new ArrayList<>();
        }
        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append("  SELECT   sub.sub_id, " +
                "         sub.isdn, " +
                "         sub.serial, " +
                "         sub.product_code, " +
                "         sub.status, " +
                "         sub.act_status, " +
                "         sub.sta_datetime," +
                "         sub.reg_type_id " +
                "  FROM   bccs_sale.subscriber sub " +
                " WHERE       sub.isdn = #isdn " +
                "         AND sub.serial = #serial " +
                "   ORDER BY sub.ACTIVE_DATETIME DESC ");
        Query query = emSale.createNativeQuery(sqlQuery.toString());
        query.setParameter("isdn", isdn);
        query.setParameter("serial", serial);
        List<Object[]> result = query.getResultList();
        List<SubscriberDTO> lstSub = new ArrayList<>();
        SubscriberDTO sub;
        if (!DataUtil.isNullOrEmpty(result)) {
            for (Object[] obj : result) {
                int i = 0;
                sub = new SubscriberDTO();
                sub.setSubId(DataUtil.safeToLong(obj[i++]));
                sub.setIsdn(DataUtil.safeToString(obj[i++]));
                sub.setSerial(DataUtil.safeToString(obj[i++]));
                sub.setProductCode(DataUtil.safeToString(obj[i++]));
                sub.setStatus(DataUtil.safeToLong(obj[i++]));
                sub.setActStatus(DataUtil.safeToString(obj[i++]));
                Object tmpDate = obj[i++];
                sub.setStaDateTime(!DataUtil.isNullObject(tmpDate) ? (Date) tmpDate : null);
                sub.setRegTypeID(DataUtil.safeToString(obj[i++]));
                lstSub.add(sub);
            }
        }
        return DataUtil.defaultIfNull(lstSub, new ArrayList<>());
    }

    @Override
    public ReasonDTO findReasonWithReasonId(String reasonId) throws LogicException, Exception {
        if (DataUtil.isNullOrEmpty(reasonId)) {
            return null;
        }
        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append("  SELECT rs.reason_id, " +
                "rs.reason_code, " +
                "rs.reason_type, " +
                "rs.name, " +
                "rs.status " +
                "FROM bccs_product.reason rs " +
                "WHERE reason_id = #reasonId ");
        Query query = emSale.createNativeQuery(sqlQuery.toString());
        query.setParameter("reasonId", reasonId);
        List<Object[]> result = query.getResultList();
        ReasonDTO reason = null;
        if (!DataUtil.isNullOrEmpty(result)) {
            for (Object[] obj : result) {
                int i = 0;
                reason = new ReasonDTO();
                reason.setReasonId(DataUtil.safeToLong(obj[i++]));
                reason.setReasonCode(DataUtil.safeToString(obj[i++]));
                reason.setReasonType(DataUtil.safeToString(obj[i++]));
                reason.setReasonName(DataUtil.safeToString(obj[i++]));
                reason.setReasonStatus(DataUtil.safeToString(obj[i++]));
            }
        }
        return DataUtil.defaultIfNull(reason, new ReasonDTO());
    }

    @Override
    public Long findProductOfferCodeToRevokeKit() throws LogicException, Exception {

        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append(" SELECT   prod_offer_id, code " +
                "  FROM   bccs_im.product_offering " +
                " WHERE   code = " +
                "             (SELECT   VALUE " +
                "                FROM   option_set_value " +
                "               WHERE   option_set_id IN " +
                "                               (SELECT   id " +
                "                                  FROM   option_set " +
                "                                 WHERE   code = " +
                "                                             'REVOKE_KIT_PRODUCT_OFFER_CODE' " +
                "                                         AND status = 1) " +
                "                       AND status = 1) " +
                "         AND status = 1 ");
        Query query = em.createNativeQuery(sqlQuery.toString());
        List<Object[]> result = query.getResultList();
        if (!DataUtil.isNullOrEmpty(result)) {
            return DataUtil.safeToLong(result.get(0)[0]);
        }
        return null;
    }


}