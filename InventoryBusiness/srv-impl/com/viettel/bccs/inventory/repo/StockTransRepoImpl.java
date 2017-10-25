package com.viettel.bccs.inventory.repo;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.model.StockTrans;
import com.viettel.bccs.inventory.model.StockTransAction;
import com.viettel.bccs.inventory.service.ProductOfferTypeServiceImpl;
import com.viettel.bccs.inventory.service.common.IMServiceUtil;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.*;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.model.StockTrans.COLUMNS;
import com.viettel.bccs.inventory.model.QStockTrans;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.viettel.fw.common.util.mapper.BaseMapper;
import org.apache.log4j.Logger;
import org.eclipse.persistence.config.QueryHints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public class StockTransRepoImpl implements StockTransRepoCustom {

    public static final Logger logger = Logger.getLogger(StockTransRepoCustom.class);
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager emIM;
    private final BaseMapper<StockTrans, StockTransDTO> mapper = new BaseMapper(StockTrans.class, StockTransDTO.class);

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM_LIB)
    private EntityManager emIMLib;

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_SALE)
    private EntityManager sale;

    @Autowired
    private StockTransActionRepo stockTransActionRepo;

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QStockTrans stockTrans = QStockTrans.stockTrans;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case CHECKERP:
                        expression = DbUtil.createStringExpression(stockTrans.checkErp, filter);
                        break;
                    case CREATEDATETIME:
                        expression = DbUtil.createDateExpression(stockTrans.createDatetime, filter);
                        break;
                    case DEPOSITSTATUS:
                        expression = DbUtil.createStringExpression(stockTrans.depositStatus, filter);
                        break;
                    case FROMOWNERID:
                        expression = DbUtil.createLongExpression(stockTrans.fromOwnerId, filter);
                        break;
                    case FROMOWNERTYPE:
                        expression = DbUtil.createLongExpression(stockTrans.fromOwnerType, filter);
                        break;
                    case ISAUTOGEN:
                        expression = DbUtil.createLongExpression(stockTrans.isAutoGen, filter);
                        break;
                    case NOTE:
                        expression = DbUtil.createStringExpression(stockTrans.note, filter);
                        break;
                    case PAYSTATUS:
                        expression = DbUtil.createStringExpression(stockTrans.payStatus, filter);
                        break;
                    case REASONID:
                        expression = DbUtil.createLongExpression(stockTrans.reasonId, filter);
                        break;
                    case REGIONSTOCKTRANSID:
                        expression = DbUtil.createLongExpression(stockTrans.regionStockTransId, filter);
                        break;
                    case REQUESTID:
                        expression = DbUtil.createLongExpression(stockTrans.requestId, filter);
                        break;
                    case STATUS:
                        expression = DbUtil.createStringExpression(stockTrans.status, filter);
                        break;
                    case STOCKBASE:
                        expression = DbUtil.createStringExpression(stockTrans.stockBase, filter);
                        break;
                    case STOCKTRANSID:
                        expression = DbUtil.createLongExpression(stockTrans.stockTransId, filter);
                        break;
                    case TOOWNERID:
                        expression = DbUtil.createLongExpression(stockTrans.toOwnerId, filter);
                        break;
                    case TOOWNERTYPE:
                        expression = DbUtil.createLongExpression(stockTrans.toOwnerType, filter);
                        break;
                    case TOTALAMOUNT:
                        expression = DbUtil.createLongExpression(stockTrans.totalAmount, filter);
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

    public Long getStockTransIdByCodeExist(String cmdCode, String noteCode, Long fromShopId, Long toShopId) throws Exception {
        StringBuilder strQuery = new StringBuilder("");
        strQuery.append(" SELECT c.stock_trans_id ");
        strQuery.append("   FROM stock_trans c ");
        strQuery.append("  WHERE c.create_datetime >= ");
        strQuery.append("           SYSDATE ");
        strQuery.append("           - (SELECT TO_NUMBER (VALUE) ");
        strQuery.append("                FROM option_set_value osv, option_set os ");
        strQuery.append("                WHERE  os.id=osv.option_set_id ");
        strQuery.append("                     AND   osv.name = 'AMOUNT_DAY_TO_EXPORT_STOCK' ");
        strQuery.append("                     AND   osv.status = '1' ");
        strQuery.append("                     AND os.status = '1' )  ");
        strQuery.append("        AND c.stock_trans_type = 1 ");
        strQuery.append("        AND c.from_owner_type = 1 ");
        strQuery.append("        AND c.to_owner_type = 1 ");
        strQuery.append("        AND c.to_owner_id = #to_owner_id ");
        strQuery.append("        AND c.from_owner_id = #from_owner_id ");
        strQuery.append("        AND c.status = 2 ");
        strQuery.append("        AND EXISTS ");
        strQuery.append("              (SELECT 1 ");
        strQuery.append("                 FROM stock_trans_action a ");
        strQuery.append("                WHERE 1 = 1 ");
        strQuery.append("                      AND a.create_datetime >= ");
        strQuery.append("                            SYSDATE ");
        strQuery.append("                            - (SELECT TO_NUMBER (VALUE) ");
        strQuery.append("                                 FROM option_set_value osv, option_set os ");
        strQuery.append("                                 WHERE  os.id=osv.option_set_id  ");
        strQuery.append("                                      AND   osv.name = 'AMOUNT_DAY_TO_EXPORT_STOCK' ");
        strQuery.append("                                      AND   osv.status = '1' ");
        strQuery.append("                                      AND os.status = '1' ) ");
        strQuery.append("                      AND a.action_code = #action_code_ma_lenh ");
        strQuery.append("                      AND a.status = 1 ");
        strQuery.append("                      AND a.stock_trans_id = c.stock_trans_id) ");
        strQuery.append("        AND EXISTS ");
        strQuery.append("              (SELECT 1 ");
        strQuery.append("                 FROM stock_trans_action b ");
        strQuery.append("                WHERE b.create_datetime >= ");
        strQuery.append("                         SYSDATE ");
        strQuery.append("                         - (SELECT TO_NUMBER (VALUE) ");
        strQuery.append("                              FROM option_set_value osv, option_set os ");
        strQuery.append("                              WHERE  os.id=osv.option_set_id ");
        strQuery.append("                                   AND   osv.name = 'AMOUNT_DAY_TO_EXPORT_STOCK'  ");
        strQuery.append("                                   AND   osv.status = '1' ");
        strQuery.append("                                   AND os.status = '1' ) ");
        strQuery.append("                      AND b.action_code = #action_code_ma_phieu ");
        strQuery.append("                      AND b.status = 2 ");
        strQuery.append("                      AND b.stock_trans_id = c.stock_trans_id) ");

        Query query = emIM.createNativeQuery(strQuery.toString());
        query.setParameter("to_owner_id", toShopId);
        query.setParameter("from_owner_id", fromShopId);
        query.setParameter("action_code_ma_lenh", cmdCode);
        query.setParameter("action_code_ma_phieu", noteCode);

        List lstTrans = query.getResultList();
        if (!DataUtil.isNullOrEmpty(lstTrans)) {
            return DataUtil.safeToLong(lstTrans.get(0));
        }
        return null;
    }

    public List<StockTransFullDTO> getListStockFullDTOByTransIdAndStatus(List<Long> lsStockTransId, List<Long> lsStatusAction) throws Exception {
        StringBuilder strQuery = new StringBuilder("");
        strQuery.append("	    SELECT   (SELECT   action_code ");
        strQuery.append("					FROM   stock_trans_action c ");
        strQuery.append("				   WHERE   c.stock_trans_id = a.stock_trans_id AND c.status = 1) ma_lenh,");
        strQuery.append("				 (SELECT   action_code");
        strQuery.append("					FROM   stock_trans_action c");
        strQuery.append("				   WHERE   c.stock_trans_id = a.stock_trans_id AND c.status = 2) ma_phieu,");
        strQuery.append("				 (SELECT shop_code FROM shop WHERE shop_id=a.to_owner_id) don_vi,");
        strQuery.append("					c.code mat_hang,");
        strQuery.append("					 b.state_id trang_thai,");
        strQuery.append("					c.product_offer_type_id prodOfferTypeId,");
        strQuery.append("					b.quantity");
        strQuery.append("		  FROM   bccs_im.stock_trans a, bccs_im.stock_trans_detail b, bccs_im.product_offering c");
        strQuery.append("		 WHERE       1 = 1");
        strQuery.append("				 AND a.stock_trans_id = b.stock_trans_id");
        strQuery.append("				 AND c.prod_offer_id = b.prod_offer_id");
        strQuery.append("				 AND a.stock_trans_id " + DbUtil.createInQuery("stock_trans_id", lsStockTransId));
        strQuery.append("				 AND EXISTS");
        strQuery.append("						(SELECT   1");
        strQuery.append("						   FROM   stock_trans_action c");
        strQuery.append("						  WHERE   1 = 1");
        strQuery.append("								  AND c.create_datetime >=");
        strQuery.append("										 SYSDATE");
        strQuery.append("										 - (SELECT   TO_NUMBER (VALUE)");
        strQuery.append("											  FROM   option_set_value osv,");
        strQuery.append("													 option_set os");
        strQuery.append("											 WHERE   os.id = osv.option_set_id");
        strQuery.append("													 AND osv.name =");
        strQuery.append("															'AMOUNT_DAY_TO_EXPORT_STOCK'");
        strQuery.append("													 AND osv.status = '1'");
        strQuery.append("													 AND os.status = '1')");
        strQuery.append("								  AND c.status " + DbUtil.createInQuery("status", lsStatusAction));
        strQuery.append("								  AND c.stock_trans_id = a.stock_trans_id)");
        strQuery.append("	 ORDER BY ma_lenh, ma_phieu, don_vi, mat_hang, trang_thai ");

        Query query = emIM.createNativeQuery(strQuery.toString());
        DbUtil.setParamInQuery(query, "stock_trans_id", lsStockTransId);
        DbUtil.setParamInQuery(query, "status", lsStatusAction);

        List<Object[]> lstTrans = query.getResultList();
        List<StockTransFullDTO> lsResult = Lists.newArrayList();
        if (!DataUtil.isNullOrEmpty(lstTrans)) {
            for (Object[] obj : lstTrans) {
                StockTransFullDTO stock = new StockTransFullDTO();
                int index = 0;
                stock.setActionCode(DataUtil.safeToString(obj[index++]));
                stock.setActionCodeNote(DataUtil.safeToString(obj[index++]));
                stock.setUnit(DataUtil.safeToString(obj[index++]));
                stock.setProdOfferCode(DataUtil.safeToString(obj[index++]));
                stock.setStateId(DataUtil.safeToLong(obj[index++]));
                stock.setProductOfferTypeId(DataUtil.safeToLong(obj[index++]));
                stock.setQuantity(DataUtil.safeToLong(obj[index]));
                lsResult.add(stock);
            }
            return lsResult;
        }
        return lsResult;
    }


    @Override
    public List<VStockTransDTO> searchVStockTrans(VStockTransDTO vStockTransDTO) throws Exception {

        StringBuilder builder = new StringBuilder("");
        List lstParameter = new ArrayList();
        List<String> lstActionCode = new ArrayList<>();

        //Tim phieu theo ma lenh neu nhap ma lenh
        if (!DataUtil.isNullOrEmpty(vStockTransDTO.getActionCode())) {
            lstActionCode = stockTransActionRepo.findListStockTransAction(vStockTransDTO);
        }

        builder.append("select stock_trans_id, stock_trans_type, action_type, create_datetime, user_code, ");
        builder.append("       user_create, from_owner_id, from_owner_type, from_owner_name, to_owner_id, ");
        builder.append("       to_owner_type, to_owner_name, action_code, action_id, note, reason_id, ");
        builder.append("       reason_name, stock_trans_status, status_name, his_action, deposit_status, ");
        builder.append("       pay_status, stock_base, action_cmd_code, create_date_trans, invoice_status, ");
        builder.append("       import_note, is_auto_gen, import_reason_id, import_reason_name, transport ");
        builder.append("       ,sign_ca_status ");
        builder.append("  from v_stock_trans a ");
        builder.append(" where a.create_datetime >= trunc(#P").append(lstParameter.size()).append(")");
        lstParameter.add(vStockTransDTO.getStartDate());
        builder.append("   and a.create_datetime <= trunc(#P").append(lstParameter.size()).append(")+1 ");
        lstParameter.add(vStockTransDTO.getEndDate());

        if (!DataUtil.isNullOrEmpty(vStockTransDTO.getActionCode()) && DataUtil.isNullOrEmpty(lstActionCode)) {
            builder.append(" and a.action_code = #P").append(lstParameter.size());
            lstParameter.add(vStockTransDTO.getActionCode().trim());
        }

        if (!DataUtil.isNullOrEmpty(vStockTransDTO.getActionCode()) && !DataUtil.isNullOrEmpty(lstActionCode)) {
            builder.append(" and (a.action_code = #P").append(lstParameter.size()).append(" OR a.action_code ").append(DbUtil.createInQuery("actionCode", lstActionCode)).append(")");
            lstParameter.add(vStockTransDTO.getActionCode().trim());
        }

        if (!DataUtil.isNullOrEmpty(vStockTransDTO.getStockTransStatus())) {
            builder.append(" and a.stock_trans_status = #P").append(lstParameter.size());
            lstParameter.add(vStockTransDTO.getStockTransStatus());
        }

        if (!DataUtil.isNullOrZero(vStockTransDTO.getFromOwnerType())) {
            builder.append(" and a.from_owner_type = #P").append(lstParameter.size());
            lstParameter.add(vStockTransDTO.getFromOwnerType());
        }

        if (!DataUtil.isNullOrZero(vStockTransDTO.getFromOwnerID())) {
            builder.append(" and a.from_owner_id = #P").append(lstParameter.size());
            lstParameter.add(vStockTransDTO.getFromOwnerID());
        } else {
            builder.append(" and  exists (select 1 from v_shop_staff");
            builder.append(" where owner_id = a.from_owner_id and owner_type =a.from_owner_type and status = #P").append(lstParameter.size());
            lstParameter.add(Const.STATUS.ACTIVE);
            if (!DataUtil.isNullOrZero(vStockTransDTO.getUserShopId())) {
                builder.append(" and parent_shop_id = #P").append(lstParameter.size());
                lstParameter.add(vStockTransDTO.getUserShopId());
//                builder.append("  or owner_id=#P").append(lstParameter.size()).append(" )");
//                lstParameter.add(vStockTransDTO.getUserShopId());
            }
            if (!DataUtil.isNullObject(vStockTransDTO.getVtUnit())) {
                builder.append(" and channel_type_id in (select channel_type_id from channel_type where status = 1 and is_vt_unit = #P").append(lstParameter.size());
                lstParameter.add(vStockTransDTO.getVtUnit());
            }
            if (!DataUtil.isNullObject(vStockTransDTO.getObjectType())) {
                builder.append(" and object_type = #P").append(lstParameter.size()).append(")");
                lstParameter.add(vStockTransDTO.getObjectType());
            }
            builder.append(")");
        }

        if (!DataUtil.isNullOrZero(vStockTransDTO.getToOwnerType())) {
            builder.append(" and a.to_owner_type = #P").append(lstParameter.size());
            lstParameter.add(vStockTransDTO.getToOwnerType());
        }

        if (!DataUtil.isNullOrZero(vStockTransDTO.getToOwnerID())) {
            builder.append(" and a.to_owner_id = #P").append(lstParameter.size());
            lstParameter.add(vStockTransDTO.getToOwnerID());
        } else {
            builder.append(" and exists (select 1 from v_shop_staff");
            builder.append(" where owner_id = a.to_owner_id and owner_type =a.to_owner_type and status = #P").append(lstParameter.size());
            lstParameter.add(Const.STATUS.ACTIVE);
            if (!DataUtil.isNullOrZero(vStockTransDTO.getUserShopId())) {
                builder.append(" and parent_shop_id = #P").append(lstParameter.size());
                lstParameter.add(vStockTransDTO.getUserShopId());
//                builder.append("  or owner_id = #P").append(lstParameter.size()).append(" )");
//                lstParameter.add(vStockTransDTO.getUserShopId());
            }
            if (!DataUtil.isNullObject(vStockTransDTO.getVtUnit())) {
                builder.append(" and channel_type_id in (select channel_type_id from channel_type where status = 1 and is_vt_unit = #P").append(lstParameter.size());
                lstParameter.add(vStockTransDTO.getVtUnit());
            }
            if (!DataUtil.isNullObject(vStockTransDTO.getObjectType())) {
                builder.append(" and object_type = #P").append(lstParameter.size()).append(")");
                lstParameter.add(vStockTransDTO.getObjectType());
            }
            builder.append(")");
        }

        if (!DataUtil.isNullOrEmpty(vStockTransDTO.getActionType())) {
            builder.append(" and a.action_type = #P").append(lstParameter.size());
            lstParameter.add(vStockTransDTO.getActionType());
        }
        builder.append(" and a.stock_trans_type = #P").append(lstParameter.size());

        if (!DataUtil.isNullOrZero(vStockTransDTO.getStockTransType())) {
            lstParameter.add(vStockTransDTO.getStockTransType());
        } else {
            lstParameter.add(Const.STOCK_TRANS_TYPE.UNIT);
        }

        if (!DataUtil.isNullObject(vStockTransDTO.getCurrentUserShopId()) && !DataUtil.isNullObject(vStockTransDTO.getCurrentUserShopPath())) {
            builder.append(" and exists (select 1 from shop where shop_id = a.from_owner_id and (");
            builder.append(" shop_path like #P").append(lstParameter.size()).append(" and status = 1)) ");
            lstParameter.add(vStockTransDTO.getCurrentUserShopPath() + "%");

        }

        if (!DataUtil.isNullOrEmpty(vStockTransDTO.getLstStatus())) {
            builder.append(" and a.stock_trans_status" + DbUtil.createInQuery("status", vStockTransDTO.getLstStatus()));
        }

        if (!DataUtil.isNullOrEmpty(vStockTransDTO.getDepositStatus())) {
            builder.append(" and a.deposit_status = #P").append(lstParameter.size());
            lstParameter.add(vStockTransDTO.getDepositStatus());
            builder.append(" and a.action_type = a.stock_trans_status");
            if (DataUtil.safeEqual(vStockTransDTO.getDepositStatus(), Const.STOCK_STRANS_DEPOSIT.DEPOSIT_RETRIEVE_EXPENSE) && DataUtil.isNullObject(vStockTransDTO.getStockTransStatus())) {
                builder.append(" and a.stock_trans_status <> 7");

            }
        }
        if (vStockTransDTO.isPrintable()) {
            builder.append(" and a.action_type = a.stock_trans_status");
        }
        if (!DataUtil.isNullOrEmpty(vStockTransDTO.getInvoiceStatus())) {
            builder.append(" and a.invoice_status = #P").append(lstParameter.size());
            lstParameter.add(vStockTransDTO.getInvoiceStatus());
        }

        if (!DataUtil.isNullOrEmpty(vStockTransDTO.getLstDepositStatus()) && !DataUtil.isNullOrEmpty(vStockTransDTO.getLstPayStatus())) {
            builder.append(" and (a.deposit_status " + DbUtil.createInQuery("deposit_status", vStockTransDTO.getLstDepositStatus()) + " or a.pay_status " + DbUtil.createInQuery("pay_status", vStockTransDTO.getLstPayStatus()) + ") ");
        } else {
            if (!DataUtil.isNullOrEmpty(vStockTransDTO.getLstDepositStatus())) {
                builder.append(" and a.deposit_status " + DbUtil.createInQuery("deposit_status", vStockTransDTO.getLstDepositStatus()));
            }
            if (!DataUtil.isNullOrEmpty(vStockTransDTO.getLstPayStatus())) {
                builder.append(" and a.pay_status " + DbUtil.createInQuery("pay_status", vStockTransDTO.getLstPayStatus()));
            }
        }

        builder.append(" and rownum <= 100 ");
//        lstParameter.add(Const.NUMBER_SEARCH.NUMBER_ROW);
        builder.append(" order by a.create_datetime DESC ");

        Query query = emIM.createNativeQuery(builder.toString());
        query.setHint(QueryHints.JDBC_TIMEOUT, 300);
        for (int i = 0; i < lstParameter.size(); i++) {
            query.setParameter("P" + i, lstParameter.get(i));
        }

        if (!DataUtil.isNullOrEmpty(vStockTransDTO.getActionCode()) && !DataUtil.isNullOrEmpty(lstActionCode)) {
            DbUtil.setParamInQuery(query, "actionCode", lstActionCode);
        }

        if (!DataUtil.isNullOrEmpty(vStockTransDTO.getLstStatus())) {
            DbUtil.setParamInQuery(query, "status", vStockTransDTO.getLstStatus());
        }
        if (!DataUtil.isNullOrEmpty(vStockTransDTO.getLstDepositStatus())) {
            DbUtil.setParamInQuery(query, "deposit_status", vStockTransDTO.getLstDepositStatus());
        }
        if (!DataUtil.isNullOrEmpty(vStockTransDTO.getLstPayStatus())) {
            DbUtil.setParamInQuery(query, "pay_status", vStockTransDTO.getLstPayStatus());
        }

//        query.setParameter("rownum", Const.NUMBER_SEARCH.NUMBER_ROW);

        List<Object[]> objects = query.getResultList();
        List<VStockTransDTO> result = new ArrayList<>();
        if (!DataUtil.isNullOrEmpty(objects)) {
            for (Object[] object : objects) {
                VStockTransDTO a = new VStockTransDTO();
                int index = 0;
                a.setStockTransID(DataUtil.safeToLong(object[index++]));
                a.setStockTransType(DataUtil.safeToLong(object[index++]));
                a.setActionType(DataUtil.safeToString(object[index++]));
                a.setCreateDateTime((Date) object[index++]);
                a.setUserCode(DataUtil.safeToString(object[index++]));
                a.setUserCreate(DataUtil.safeToString(object[index++]));
                a.setFromOwnerID(DataUtil.safeToLong(object[index++]));
                a.setFromOwnerType(DataUtil.safeToLong(object[index++]));
                a.setFromOwnerName(DataUtil.safeToString(object[index++]));
                a.setToOwnerID(DataUtil.safeToLong(object[index++]));
                a.setToOwnerType(DataUtil.safeToLong(object[index++]));
                a.setToOwnerName(DataUtil.safeToString(object[index++]));
                a.setActionCode(DataUtil.safeToString(object[index++]));
                a.setActionID(DataUtil.safeToLong(object[index++]));
                a.setNote(DataUtil.safeToString(object[index++]));
                a.setReasonID(DataUtil.safeToLong(object[index++]));
                a.setReasonName(DataUtil.safeToString(object[index++]));
                a.setStockTransStatus(DataUtil.safeToString(object[index++]));
                a.setStatusName(DataUtil.safeToString(object[index++]));
                a.setHisAction(DataUtil.safeToString(object[index++]));
                a.setDepositStatus(DataUtil.safeToString(object[index++]));
                a.setPayStatus(DataUtil.safeToString(object[index++]));
                a.setStockBase(DataUtil.safeToString(object[index++]));
                a.setActionCMDCode(DataUtil.safeToString(object[index++]));
                a.setCreateDateTrans((Date) object[index++]);
                a.setInvoiceStatus(DataUtil.safeToString(object[index++]));
                a.setImportNote(DataUtil.safeToString(object[index++]));
                a.setIsAutoGen(DataUtil.safeToLong(object[index++]));
                a.setImportReasonID(DataUtil.safeToLong(object[index++]));
                a.setImportReasonName(DataUtil.safeToString(object[index++]));
                a.setTransport(DataUtil.safeToString(object[index++]));
                a.setSignCaStatus(DataUtil.safeToString(object[index++]));
                result.add(a);
            }
        }
        return result;
    }


    @Override
    public List<VStockTransDTO> searchMaterialVStockTrans(VStockTransDTO vStockTransDTO) throws Exception {

        StringBuilder builder = new StringBuilder("");
        List lstParameter = new ArrayList();

        builder.append("select stock_trans_id, stock_trans_type, action_type, create_datetime, user_code, ");
        builder.append("       user_create, from_owner_id, from_owner_type, from_owner_name, to_owner_id, ");
        builder.append("       to_owner_type, to_owner_name, action_code, action_id, note, reason_id, ");
        builder.append("       reason_name, stock_trans_status, status_name, his_action, deposit_status, ");
        builder.append("       pay_status, stock_base, action_cmd_code, create_date_trans, invoice_status, ");
        builder.append("       import_note, is_auto_gen, import_reason_id, import_reason_name, transport ");
        builder.append("       ,sign_ca_status ");
        builder.append("  from v_stock_trans a ");
        builder.append(" where a.create_datetime >= trunc(#P").append(lstParameter.size()).append(")");
        lstParameter.add(vStockTransDTO.getStartDate());
        builder.append("   and a.create_datetime <= trunc(#P").append(lstParameter.size()).append(")+1 ");
        lstParameter.add(vStockTransDTO.getEndDate());

        if (!DataUtil.isNullOrEmpty(vStockTransDTO.getActionCode())) {
            builder.append(" and lower(a.action_code) like #P").append(lstParameter.size());
            lstParameter.add("%" + vStockTransDTO.getActionCode().trim().toLowerCase() + "%");
        }

        if (!DataUtil.isNullOrEmpty(vStockTransDTO.getStockBase())) {
            builder.append(" and lower(a.stock_base) like #P").append(lstParameter.size());
            lstParameter.add("%" + vStockTransDTO.getStockBase().trim().toLowerCase() + "%");
        }

        if (!DataUtil.isNullOrEmpty(vStockTransDTO.getStockTransStatus())) {
            builder.append(" and a.stock_trans_status = #P").append(lstParameter.size());
            lstParameter.add(vStockTransDTO.getStockTransStatus());
        }

        if (!DataUtil.isNullOrZero(vStockTransDTO.getFromOwnerType())) {
            builder.append(" and a.from_owner_type = #P").append(lstParameter.size());
            lstParameter.add(vStockTransDTO.getFromOwnerType());
        }

        if (!DataUtil.isNullOrZero(vStockTransDTO.getFromOwnerID())) {
            builder.append(" and a.from_owner_id = #P").append(lstParameter.size());
            lstParameter.add(vStockTransDTO.getFromOwnerID());
        }

        if (!DataUtil.isNullOrZero(vStockTransDTO.getToOwnerType())) {
            builder.append(" and a.to_owner_type = 3 ").append(lstParameter.size());
            lstParameter.add(vStockTransDTO.getToOwnerType());
        }

        if (!DataUtil.isNullOrEmpty(vStockTransDTO.getActionType())) {
            builder.append(" and a.action_type = #P").append(lstParameter.size());
            lstParameter.add(vStockTransDTO.getActionType());
        }
        builder.append(" and a.stock_trans_type = #P").append(lstParameter.size());

        if (!DataUtil.isNullOrZero(vStockTransDTO.getStockTransType())) {
            lstParameter.add(vStockTransDTO.getStockTransType());
        } else {
            lstParameter.add(Const.STOCK_TRANS_TYPE.UNIT);
        }
        //reason
        if (!DataUtil.isNullOrZero(vStockTransDTO.getReasonID())) {
            builder.append(" and a.reason_id = #P").append(lstParameter.size());
            lstParameter.add(vStockTransDTO.getReasonID());
        }

        builder.append(" and rownum <= 100 ");
//        lstParameter.add(Const.NUMBER_SEARCH.NUMBER_ROW);
        builder.append(" order by a.create_datetime DESC ");

        Query query = emIM.createNativeQuery(builder.toString());
        query.setHint(QueryHints.JDBC_TIMEOUT, 300);
        for (int i = 0; i < lstParameter.size(); i++) {
            query.setParameter("P" + i, lstParameter.get(i));
        }

        List<Object[]> objects = query.getResultList();
        List<VStockTransDTO> result = new ArrayList<>();
        if (!DataUtil.isNullOrEmpty(objects)) {
            for (Object[] object : objects) {
                VStockTransDTO a = new VStockTransDTO();
                int index = 0;
                a.setStockTransID(DataUtil.safeToLong(object[index++]));
                a.setStockTransType(DataUtil.safeToLong(object[index++]));
                a.setActionType(DataUtil.safeToString(object[index++]));
                a.setCreateDateTime((Date) object[index++]);
                a.setUserCode(DataUtil.safeToString(object[index++]));
                a.setUserCreate(DataUtil.safeToString(object[index++]));
                a.setFromOwnerID(DataUtil.safeToLong(object[index++]));
                a.setFromOwnerType(DataUtil.safeToLong(object[index++]));
                a.setFromOwnerName(DataUtil.safeToString(object[index++]));
                a.setToOwnerID(DataUtil.safeToLong(object[index++]));
                a.setToOwnerType(DataUtil.safeToLong(object[index++]));
                a.setToOwnerName(DataUtil.safeToString(object[index++]));
                a.setActionCode(DataUtil.safeToString(object[index++]));
                a.setActionID(DataUtil.safeToLong(object[index++]));
                a.setNote(DataUtil.safeToString(object[index++]));
                a.setReasonID(DataUtil.safeToLong(object[index++]));
                a.setReasonName(DataUtil.safeToString(object[index++]));
                a.setStockTransStatus(DataUtil.safeToString(object[index++]));
                a.setStatusName(DataUtil.safeToString(object[index++]));
                a.setHisAction(DataUtil.safeToString(object[index++]));
                a.setDepositStatus(DataUtil.safeToString(object[index++]));
                a.setPayStatus(DataUtil.safeToString(object[index++]));
                a.setStockBase(DataUtil.safeToString(object[index++]));
                a.setActionCMDCode(DataUtil.safeToString(object[index++]));
                a.setCreateDateTrans((Date) object[index++]);
                a.setInvoiceStatus(DataUtil.safeToString(object[index++]));
                a.setImportNote(DataUtil.safeToString(object[index++]));
                a.setIsAutoGen(DataUtil.safeToLong(object[index++]));
                a.setImportReasonID(DataUtil.safeToLong(object[index++]));
                a.setImportReasonName(DataUtil.safeToString(object[index++]));
                a.setTransport(DataUtil.safeToString(object[index++]));
                a.setSignCaStatus(DataUtil.safeToString(object[index]));
                result.add(a);
            }
        }
        return result;
    }

    @Override
    public List<StockTransDTO> searchStockTrans(StockTransDTO stockTransDTO) throws Exception {
        StringBuilder builder = new StringBuilder("SELECT /*+ index(a stock_trans_pk)   index(b stan_action_code_idx) */");
        builder.append(" a.STOCK_TRANS_ID,b.action_code, b.STOCK_TRANS_ACTION_ID,a.status,");
        builder.append(" CASE a.from_owner_type")
                .append(" WHEN 1")
                .append("   THEN (SELECT shop_code || ' - ' || to_char(name)")
                .append("           FROM bccs_im.shop")
                .append("      WHERE shop_id = a.from_owner_id)")
                .append(" WHEN 2")
                .append("   THEN (SELECT staff_code || ' - ' || to_char(name)")
                .append("           FROM bccs_im.staff")
                .append("          WHERE staff_id = a.from_owner_id)")
                .append(" WHEN 4")
                .append("   THEN (SELECT    TO_CHAR (partner_code) || ' - ' || TO_CHAR (partner_name)")
                .append("  FROM partner")
                .append("         WHERE partner_id = a.from_owner_id)")
                .append("  ELSE TO_CHAR (a.from_owner_id )")
                .append("  END AS from_owner_name,");
        builder.append(" (SELECT staff.NAME FROM bccs_im.staff WHERE staff_code = b.create_user) as user_create,")
                .append(" 'stock.trans.status'||a.status AS status_name,");
        builder.append(" CASE a.to_owner_type")
                .append("    WHEN 1")
                .append("         THEN (SELECT shop_code || ' - ' || to_char(name)")
                .append("             FROM bccs_im.shop")
                .append("             WHERE shop_id = a.to_owner_id)")
                .append("    WHEN 2 ")
                .append("         THEN (SELECT staff_code || ' - ' || to_char(name)")
                .append("             FROM bccs_im.staff")
                .append("             WHERE staff_id = a.to_owner_id)")
                .append("    WHEN 4  ")
                .append("         THEN (SELECT    TO_CHAR (partner_code) || ' - ' || TO_CHAR (partner_name)")
                .append("             FROM bccs_im.partner")
                .append("             WHERE partner_id = a.to_owner_id)")
                .append("    ELSE TO_CHAR (a.to_owner_id)")
                .append(" END AS to_owner_name,");
        builder.append(" (select reason_name from bccs_im.reason c where c.reason_id = a.reason_id) as reason_name ,")
                .append(" b.note, b.CREATE_DATETIME, a.from_owner_type, a.from_owner_id, a.to_owner_type, a.to_owner_id,");
        builder.append(" a.is_auto_gen, decode(a.is_auto_gen, 2, 'GD logistic'), nvl(a.process_offline,0), a.process_start, a.process_end, ")
                .append(" (select stv.status from stock_trans_voffice stv where stv.stock_trans_action_id=b.stock_trans_action_id),")
                .append(" CASE a.from_owner_type")
                .append("                   WHEN 1")
                .append("                        THEN (SELECT address")
                .append("                            FROM shop")
                .append("                            WHERE shop_id = a.from_owner_id)")
                .append("                   WHEN 2 ")
                .append("                        THEN (SELECT address")
                .append("                            FROM staff")
                .append("                            WHERE staff_id = a.from_owner_id)")
                .append("                   WHEN 4  ")
                .append("                        THEN (SELECT    address")
                .append("                            FROM partner")
                .append("                            WHERE partner_id = a.from_owner_id)")
                .append("                   ELSE TO_CHAR (a.from_owner_id)")
                .append("                END AS from_owner_address,")
                .append(" CASE a.to_owner_type")
                .append("                   WHEN 1")
                .append("                        THEN (SELECT address")
                .append("                            FROM shop")
                .append("                            WHERE shop_id = a.to_owner_id)")
                .append("                   WHEN 2 ")
                .append("                        THEN (SELECT address")
                .append("                            FROM staff")
                .append("                            WHERE staff_id = a.to_owner_id)")
                .append("                   WHEN 4  ")
                .append("                        THEN (SELECT    address")
                .append("                            FROM partner")
                .append("                            WHERE partner_id = a.to_owner_id)")
                .append("                   ELSE TO_CHAR (a.to_owner_id)")
                .append("                END AS to_owner_address")
                .append(" FROM   stock_trans a, stock_trans_action b")
                .append(" WHERE  a.stock_trans_id = b.stock_trans_id and b.action_code != 'SYSTEM'");
        List params = Lists.newArrayList();
        if (!DataUtil.isNullObject(stockTransDTO.getShopId()) && !DataUtil.isNullOrEmpty(stockTransDTO.getActionCode())) {
            if (Const.VT_SHOP_ID.equals(stockTransDTO.getShopId())) {
                builder.append(" and lower(b.action_code) = #P").append(params.size());
                params.add(stockTransDTO.getActionCode().toLowerCase());
            } else {
                builder.append(" and lower(b.action_code) like #P").append(params.size());
                params.add("%" + stockTransDTO.getActionCode().toLowerCase() + "%");
            }
        }
        if (!DataUtil.isNullOrEmpty(stockTransDTO.getActionType())) {
            if (Const.STOCK_TRANS_ACTION_TYPE.COMMAND.equals(stockTransDTO.getActionType())) {
                builder.append(" and a.status in(select c.value from option_set a, option_set_value c where c.option_set_id=a.id and a.code='")
                        .append(Const.OPTION_SET.STOCK_TRANS_STATUS_CMD).append("')");
            } else {
                builder.append(" and a.status in(select c.value from option_set a, option_set_value c where c.option_set_id=a.id and a.code='")
                        .append(Const.OPTION_SET.STOCK_TRANS_STATUS_NOT).append("')");
            }
        }

        builder.append(" and b.create_datetime <= #P +1").append(params.size());
        params.add(stockTransDTO.getToCreateDate());

        builder.append(" and b.create_datetime >= #P").append(params.size());
        params.add(stockTransDTO.getFromCreateDate());

        if (!DataUtil.isNullOrEmpty(stockTransDTO.getStatus())) {
            String[] status = stockTransDTO.getStatus().split(",");
            if (!DataUtil.isNullOrEmpty(status)) {
                builder.append(" and ( a.status = #P").append(params.size());
                params.add(status[0]);
                for (int i = 1; i < status.length; i++) {
                    builder.append(" or a.status = #P").append(params.size());
                    params.add(status[i]);
                }
                builder.append(")");
            } else {
                builder.append(" and a.status =#P").append(params.size());
                params.add(stockTransDTO.getStatus());
            }
        }

        if (!DataUtil.isNullOrEmpty(stockTransDTO.getStockTransStatus())) {
            if (Const.STOCK_TRANS_TYPE.EXPORT.equals(stockTransDTO.getStockTransStatus())) {
                builder.append(" and a.status in(select c.value from option_set a, option_set_value c where c.option_set_id=a.id and a.code='")
                        .append(Const.OPTION_SET.STOCK_TRANS_STATUS_EX).append("')");
                builder.append(" and b.status= ").append(Const.STOCK_TRANS_STATUS.EXPORTED);
            } else {
                builder.append(" and a.status in(select c.value from option_set a, option_set_value c where c.option_set_id=a.id and a.code='")
                        .append(Const.OPTION_SET.STOCK_TRANS_STATUS_IM).append("')");
                builder.append(" and b.status= ").append(Const.STOCK_TRANS_STATUS.IMPORTED);
            }
        }

        boolean owner = false;
        StringBuilder subBuilder = new StringBuilder(" and(");
        if (!DataUtil.isNullObject(stockTransDTO.getFromOwnerType())) {
            owner = true;
            subBuilder.append(" (a.from_owner_type = #P").append(params.size());
            params.add(stockTransDTO.getFromOwnerType());
            subBuilder.append("  and a.from_owner_id = #P").append(params.size()).append(" )");
            //Neu khong chon thi tim cho chinh ban than
            Long ownerId = stockTransDTO.getStaffId();
            if (Const.OWNER_TYPE.SHOP.equals(stockTransDTO.getFromOwnerType())) {
                ownerId = stockTransDTO.getShopId();
            }
            //Neu co chon
            if (!DataUtil.isNullOrZero(stockTransDTO.getFromOwnerId())) {
                ownerId = stockTransDTO.getFromOwnerId();
            }
            params.add(ownerId);
        }
        if (!DataUtil.isNullObject(stockTransDTO.getToOwnerType())) {
            if (owner) {
                if (stockTransDTO.getFromOwnerType().equals(stockTransDTO.getToOwnerType())) {
                    subBuilder.append(" or ");
                } else {
                    subBuilder.append(" and ");
                }
            }
            owner = true;
            subBuilder.append(" (a.to_owner_type = #P").append(params.size());
            params.add(stockTransDTO.getToOwnerType());
            subBuilder.append("  and a.to_owner_id = #P").append(params.size()).append(")");
            //Neu khong chon thi tim cho chinh ban than
            Long ownerId = stockTransDTO.getStaffId();
            if (Const.OWNER_TYPE.SHOP.equals(stockTransDTO.getFromOwnerType())) {
                ownerId = stockTransDTO.getShopId();
            }
            //Neu co chon
            if (!DataUtil.isNullOrZero(stockTransDTO.getToOwnerId())) {
                ownerId = stockTransDTO.getToOwnerId();
            }
            params.add(ownerId);
        }
        subBuilder.append(")");
        if (owner) {
            builder.append(subBuilder);
        } else {
            builder.append(" and ((a.from_owner_type = #P").append(params.size());
            params.add(Const.OWNER_TYPE.SHOP);

            builder.append(" and a.from_owner_id = #P").append(params.size()).append(")");
            params.add(stockTransDTO.getShopId());

            builder.append(" or (a.to_owner_type = #P").append(params.size());
            params.add(Const.OWNER_TYPE.STAFF);

            builder.append(" and a.to_owner_Id = #P").append(params.size()).append(")");
            params.add(stockTransDTO.getStaffId());

            builder.append(" or (a.from_owner_type = #P").append(params.size());
            params.add(Const.OWNER_TYPE.SHOP);

            builder.append(" and a.from_owner_id = #P").append(params.size()).append(")");
            params.add(stockTransDTO.getShopId());

            builder.append(" or (a.to_owner_type = #P").append(params.size());
            params.add(Const.OWNER_TYPE.STAFF);

            builder.append(" and a.to_owner_id = #P").append(params.size()).append("))");
            params.add(stockTransDTO.getStaffId());
        }
        builder.append(" and rownum <= #P").append(params.size());
        params.add(Const.NUMBER_SEARCH.NUMBER_ROW);

        Query query = emIM.createNativeQuery(builder.toString());
        for (int i = 0; i < params.size(); i++) {
            query.setParameter("P" + i, params.get(i));
        }
        List<StockTransDTO> result = Lists.newArrayList();
        List objects = query.getResultList();
        for (Object object : objects) {
            Object[] arrObject = (Object[]) object;
            StockTransDTO item = new StockTransDTO();
            item.setStockTransId(DataUtil.safeToLong(arrObject[0]));
            item.setActionCode(DataUtil.safeToString(arrObject[1]));
            item.setStockTransActionId(DataUtil.safeToLong(arrObject[2]));
            item.setStatus(DataUtil.safeToString(arrObject[3]));
            item.setFromOwnerName(DataUtil.safeToString(arrObject[4]));
            item.setUserCreate(DataUtil.safeToString(arrObject[5]));
            item.setStatusName(GetTextFromBundleHelper.getText(DataUtil.safeToString(arrObject[6])));
            item.setToOwnerName(DataUtil.safeToString(arrObject[7]));
            item.setReasonName(DataUtil.safeToString(arrObject[8]));
            item.setNote(DataUtil.safeToString(arrObject[9]));
            item.setCreateDatetime((Date) arrObject[10]);
            item.setFromOwnerType(DataUtil.safeToLong(arrObject[11]));
            item.setFromOwnerId(DataUtil.safeToLong(arrObject[12]));
            item.setToOwnerType(DataUtil.safeToLong(arrObject[13]));
            item.setToOwnerId(DataUtil.safeToLong(arrObject[14]));
            item.setIsAutoGen(DataUtil.safeToLong(arrObject[15]));
            item.setLogicstic(DataUtil.safeToString(arrObject[16]));
            item.setProcessOffline(DataUtil.safeToString(arrObject[17]));
            item.setProcessStartDate((Date) arrObject[18]);
            item.setProcessEndDate((Date) arrObject[19]);
            item.setVofficeStatus(DataUtil.safeToLong(arrObject[20]));
            item.setFromOwnerAddress(DataUtil.safeToString(arrObject[21]));
            item.setToOwnerAddress(DataUtil.safeToString(arrObject[22]));
            result.add(item);
        }
        return result;
    }

    @Override
    public List<StockTransFullDTO> searchStockTransDetail(Long stockTransActionId) throws Exception {
        StringBuilder builder = new StringBuilder("");
        builder.append(" SELECT a.stock_trans_detail_id, a.product_offer_type_id, a.prod_offer_type_name, a.prod_offer_id, ");
        builder.append(" a.prod_offer_code, a.prod_offer_name, a.check_serial, a.check_deposit, a.accounting_model_code, ");
        builder.append(" a.unit, a.from_owner_type, a.from_owner_id, a.to_owner_type,a.to_owner_id,a.create_datetime, ");
        builder.append(" a.stock_trans_type,a.stock_trans_id,a.reason_id,a.stock_trans_status,a.pay_status,a.deposit_status, ");
        builder.append(" a.action_code,a.stock_trans_action_id,a.action_type,a.username,a.action_staff_id,a.state_id, ");
        builder.append(" a.quantity,a.basis_price,a.state_name,a.region_stock_trans_id,a.is_auto_gen,a.table_name, ");
        builder.append(" a.source_price, a.price, a.amount ");
        builder.append(" FROM v_stock_trans_detail a ");
        builder.append(" where a.stock_trans_action_id= #stockTransActionId and rownum <= #rownum order by a.prod_offer_type_name,a.prod_offer_name ");
        Query query = emIM.createNativeQuery(builder.toString());
        query.setParameter("stockTransActionId", stockTransActionId);
        query.setParameter("rownum", Const.NUMBER_SEARCH.LIMIT_ALL);
        List objects = query.getResultList();
        List<StockTransFullDTO> result = Lists.newArrayList();
        if (!DataUtil.isNullOrEmpty(objects)) {
            for (Object object : objects) {
                int index = 0;
                Object[] array = (Object[]) object;
                StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
                stockTransFullDTO.setStockTransDetailId(DataUtil.safeToLong(array[index++]));
                stockTransFullDTO.setProductOfferTypeId(DataUtil.safeToLong(array[index++]));
                stockTransFullDTO.setProductOfferTypeName(DataUtil.safeToString(array[index++]));
                stockTransFullDTO.setProdOfferId(DataUtil.safeToLong(array[index++]));
                stockTransFullDTO.setProdOfferCode(DataUtil.safeToString(array[index++]));
                stockTransFullDTO.setProdOfferName(DataUtil.safeToString(array[index++]));
                stockTransFullDTO.setCheckSerial(DataUtil.safeToString(array[index++]));
                stockTransFullDTO.setCheckDeposit(DataUtil.safeToString(array[index++]));
                stockTransFullDTO.setAccountingModelCode(DataUtil.safeToString(array[index++]));
                stockTransFullDTO.setUnit(DataUtil.safeToString(array[index++]));
                stockTransFullDTO.setFromOwnerType(DataUtil.safeToString(array[index++]));
                stockTransFullDTO.setFromOwnerId(DataUtil.safeToLong(array[index++]));
                stockTransFullDTO.setToOwnerType(DataUtil.safeToString(array[index++]));
                stockTransFullDTO.setToOwnerId(DataUtil.safeToLong(array[index++]));
                Object obj = array[index++];
                stockTransFullDTO.setCreateDatetime(obj != null ? (Date) obj : null);
                stockTransFullDTO.setStockTransType(DataUtil.safeToLong(array[index++]));
                stockTransFullDTO.setStockTransId(DataUtil.safeToLong(array[index++]));
                stockTransFullDTO.setReasonId(DataUtil.safeToLong(array[index++]));
                stockTransFullDTO.setStockTransStatus(DataUtil.safeToString(array[index++]));
                stockTransFullDTO.setPayStatus(DataUtil.safeToString(array[index++]));
                stockTransFullDTO.setDepositStatus(DataUtil.safeToString(array[index++]));
                stockTransFullDTO.setActionCode(DataUtil.safeToString(array[index++]));
                stockTransFullDTO.setActionId(DataUtil.safeToLong(array[index++]));
                stockTransFullDTO.setActionType(DataUtil.safeToString(array[index++]));
                stockTransFullDTO.setUsername(DataUtil.safeToString(array[index++]));
                stockTransFullDTO.setActionStaffId(DataUtil.safeToLong(array[index++]));
                stockTransFullDTO.setStateId(DataUtil.safeToLong(array[index++]));
                stockTransFullDTO.setQuantity(DataUtil.safeToLong(array[index++]));
                stockTransFullDTO.setBasisPrice(DataUtil.safeToLong(array[index++]));
                stockTransFullDTO.setStateName(DataUtil.safeToString(array[index++]));
                stockTransFullDTO.setRegionStockTransId(DataUtil.safeToLong(array[index++]));
                stockTransFullDTO.setIsAutoGen(DataUtil.safeToString(array[index++]));
                stockTransFullDTO.setTableName(DataUtil.safeToString(array[index++]));
                stockTransFullDTO.setSourcePrice(DataUtil.safeToLong(array[index++]));
                stockTransFullDTO.setPrice(DataUtil.safeToLong(array[index++]));
                stockTransFullDTO.setAmount(DataUtil.safeToLong(array[index]));
                result.add(stockTransFullDTO);
            }
        }
        return result;
    }

    @Override
    public Long getTotalValueStockSusbpendByOwnerId(Long ownerId, String ownerType) throws Exception {
        StringBuilder builder = new StringBuilder(" ");
        builder.append(" SELECT NVL (SUM (amount), 0) as total ");
        builder.append(" FROM (SELECT   NVL (st.total_amount, 0) amount ");
        builder.append(" FROM stock_trans st ");
        builder.append(" WHERE st.to_owner_id =#ownerId ");
        builder.append(" AND st.to_owner_type = #ownerType ");
        builder.append(" AND st.status " + DbUtil.createInQuery("status", Lists.newArrayList(Const.STOCK_TRANS_STATUS.EXPORT_ORDER, Const.STOCK_TRANS_STATUS.EXPORT_NOTE,
                Const.STOCK_TRANS_STATUS.EXPORTED, Const.STOCK_TRANS_STATUS.IMPORT_ORDER, Const.STOCK_TRANS_STATUS.IMPORT_NOTE, Const.STOCK_TRANS_STATUS.PROCESSING)));
        builder.append(" AND st.create_datetime >= TRUNC (SYSDATE)-15 ");
        builder.append(" AND st.create_datetime <= SYSDATE) ");
        Query query = emIM.createNativeQuery(builder.toString());
        query.setParameter("ownerId", ownerId);
        query.setParameter("ownerType", ownerType);
        DbUtil.setParamInQuery(query, "status", Lists.newArrayList(Const.STOCK_TRANS_STATUS.EXPORT_ORDER, Const.STOCK_TRANS_STATUS.EXPORT_NOTE,
                Const.STOCK_TRANS_STATUS.EXPORTED, Const.STOCK_TRANS_STATUS.IMPORT_ORDER, Const.STOCK_TRANS_STATUS.IMPORT_NOTE, Const.STOCK_TRANS_STATUS.PROCESSING));
        BigDecimal bi = (BigDecimal) query.getSingleResult();
        if (bi != null) {
            return bi.longValue();
        }
        return 0L;
    }

    @Override
    public StockTransDTO findStockTransByActionId(Long stockTransActionId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select a.stock_trans_id, a.status, a.region_stock_trans_id  from stock_trans a")
                .append(" where stock_trans_id in(select stock_trans_id ")
                .append(" from stock_trans_action where stock_trans_action_id= #stockTransActionId)");
        Query query = emIM.createNativeQuery(queryBuilder.toString());
        query.setParameter("stockTransActionId", stockTransActionId);
        List result = query.getResultList();
        if (!DataUtil.isNullOrEmpty(result)) {
            Object[] objects = (Object[]) result.get(0);
            StockTransDTO stockTransDTO = new StockTransDTO();
            stockTransDTO.setStockTransId(DataUtil.safeToLong(objects[0]));
            stockTransDTO.setStatus(DataUtil.safeToString(objects[1]));
            stockTransDTO.setRegionStockTransId(DataUtil.safeToLong(objects[2]));
            return stockTransDTO;
        }
        return null;
    }

    @Override
    public List<VStockTransDTO> findStockSuspendDetail(Long ownerId, String ownerType) throws Exception {
        StringBuilder builder = new StringBuilder("SELECT   from_owner_code exportstockname,")
                .append(" to_owner_code importstockname,")
                .append(" REAL_TRANS_DATE realtransdate,");  // tam thoi de la sysdate
        builder.append(" action_code actioncode,")
                .append(" reason_name reasonname,")
                .append(" stock_trans_status_name transstatus, total_amount")
                .append(" FROM   v_stock_trans_suspend")
                .append(" WHERE (from_owner_id = #fromOwnerId AND from_owner_type = #fromOwnerType) ")
                .append("       OR (to_owner_id = #toOwnerId AND to_owner_type = #toOwnerType) ")
                .append(" ORDER BY realtransdate desc, from_owner_code, to_owner_code");
        Query query = emIM.createNativeQuery(builder.toString());
        query.setParameter("fromOwnerId", ownerId);
        query.setParameter("fromOwnerType", ownerType);
        query.setParameter("toOwnerId", ownerId);
        query.setParameter("toOwnerType", ownerType);
        List queryResult = query.getResultList();
        if (!DataUtil.isNullOrEmpty(queryResult)) {
            List<VStockTransDTO> result = Lists.newArrayList();
            for (Object object : queryResult) {
                Object[] arrResult = (Object[]) object;
                VStockTransDTO vStockTransDTO = new VStockTransDTO();
                vStockTransDTO.setFromOwnerName(DataUtil.safeToString(arrResult[0]));
                vStockTransDTO.setToOwnerName(DataUtil.safeToString(arrResult[1]));
                vStockTransDTO.setRealTransDate((Date) arrResult[2]);
                vStockTransDTO.setActionCode(DataUtil.safeToString(arrResult[3]));
                vStockTransDTO.setReasonName(DataUtil.safeToString(arrResult[4]));
                vStockTransDTO.setStockTransStatusName(DataUtil.safeToString(arrResult[5]));
                vStockTransDTO.setTotalAmount(DataUtil.safeToLong(arrResult[6]));
                result.add(vStockTransDTO);
            }
            return result;
        }
        return null;
    }


    @Override
    public List<ManageTransStockViewDto> findStockTrans(ManageTransStockDto transStockDto) throws
            LogicException, Exception {
        List<ManageTransStockViewDto> list = Lists.newArrayList();
        if (DataUtil.isNullObject(transStockDto.getFromDate())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.from.date.not.blank");
        }
        if (DataUtil.isNullObject(transStockDto.getToDate())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.to.date.not.blank");
        }
        if (transStockDto.getFromDate().after(transStockDto.getToDate())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.trans.from.than.to");
        }
        long days = 11;
        try {
            days = DateUtil.daysBetween2Dates(transStockDto.getToDate(), transStockDto.getFromDate());
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }

        if (days > 10) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "trans.stock.from.to.over");
        }
        List params = Lists.newArrayList();
        StringBuilder sql = new StringBuilder();
        sql.append("  SELECT   b.action_code transcode,");
        sql.append("           (SELECT   v.owner_name");
        sql.append("              FROM   v_shop_staff v");
        sql.append("             WHERE   v.owner_type = a.from_owner_type");
        sql.append("                     AND v.owner_id = a.from_owner_id)");
        sql.append("               takestockname,");
        sql.append("           (SELECT   v.owner_name");
        sql.append("              FROM   v_shop_staff v");
        sql.append("             WHERE   v.owner_type = a.to_owner_type");
        sql.append("                     AND v.owner_id = a.to_owner_id)");
        sql.append("               receivestockname,");
        sql.append("           a.status status,");
        sql.append("           d.reason_name reason,");
        sql.append("           a.note notes,");
        sql.append("           b.create_user createdStaff,");
        sql.append("           TO_CHAR(b.create_datetime,'DD/MM/YYYY') createdDate,");
        sql.append("           a.stock_trans_id stockTransId");
        sql.append("    FROM   stock_trans a, stock_trans_action b, stock_trans_detail c,reason d");
        sql.append("   WHERE   a.stock_trans_id = b.stock_trans_id");
        sql.append("           AND a.stock_trans_id = c.stock_trans_id(+)");
        sql.append("           AND a.reason_id=d.reason_id");
        sql.append("           AND a.stock_trans_type = 6");
        if (!DataUtil.isNullObject(transStockDto.getTransCode())) {
            sql.append("           AND upper(b.action_code) LIKE ?");
            params.add("%" + transStockDto.getTransCode().toUpperCase() + "%");
        }
        sql.append("           AND (b.create_datetime BETWEEN trunc(?) AND trunc(?)+1)");
        params.add(transStockDto.getFromDate());
        params.add(transStockDto.getToDate());
        if (!DataUtil.isNullObject(transStockDto.getTypeTakeOwner()) && !DataUtil.isNullObject(transStockDto.getTakeOwnerStockId())) {
            sql.append("           AND a.from_owner_type=?");
            params.add(transStockDto.getTypeTakeOwner());
            sql.append("           AND a.from_owner_id=?");
            params.add(transStockDto.getTakeOwnerStockId());
        }
        if (!DataUtil.isNullObject(transStockDto.getTypeReceiveOwner()) && !DataUtil.isNullObject(transStockDto.getReceiveOwnerStockId())) {
            sql.append("           AND a.to_owner_type=?");
            params.add(transStockDto.getTypeReceiveOwner());
            sql.append("           AND a.to_owner_id =?");
            params.add(transStockDto.getReceiveOwnerStockId());
        }
        if (!DataUtil.isNullObject(transStockDto.getOwnerCreateId())) {
            sql.append("           AND b.action_staff_id=?");
            params.add(transStockDto.getOwnerCreateId());
        }
        if (!DataUtil.isNullObject(transStockDto.getNotes())) {
            sql.append("           AND upper(a.note) LIKE ?");
            params.add("%" + transStockDto.getNotes().toUpperCase() + "%");
        }
        sql.append("           ORDER BY b.create_datetime DESC,b.action_code ASC");
        Query query = emIM.createNativeQuery(sql.toString());
        for (int i = 0; i < params.size(); i++) {
            query.setParameter(i + 1, params.get(i));
        }
        List<Object[]> queryResult = query.getResultList();
        for (Object[] ob : queryResult) {
            ManageTransStockViewDto view = new ManageTransStockViewDto();
            view.setTransCode(DataUtil.safeToString(ob[0]));
            view.setTakeStockName(DataUtil.safeToString(ob[1]));
            view.setReceiveStockName(DataUtil.safeToString(ob[2]));
            view.setStatus(DataUtil.safeToString(ob[3]));
            view.setReason(DataUtil.safeToString(ob[4]));
            view.setNotes(DataUtil.safeToString(ob[5]));
            view.setCreatedStaff(DataUtil.safeToString(ob[6]));
            view.setCreatedDate(DataUtil.safeToString(ob[7]));
            view.setStockTranId(DataUtil.safeToLong(ob[8]));
            list.add(view);
        }
        return list;
    }

    @Override
    public List<ManageStockTransViewDetailDto> viewDetailStockTrans(Long stockTransId) throws Exception {
        List<ManageStockTransViewDetailDto> list = Lists.newArrayList();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT   b.from_serial fromnumber,");
        sql.append("         b.to_serial tonumber,");
        sql.append("         b.quantity quantity");
        sql.append("  FROM   stock_trans_detail a, stock_trans_serial b");
        sql.append(" WHERE   a.stock_trans_detail_id = b.stock_trans_detail_id");
        sql.append("         AND a.stock_trans_id = #stockTransId");
        Query query = emIM.createNativeQuery(sql.toString());
        query.setParameter("stockTransId", stockTransId);
        List<Object[]> queryResult = query.getResultList();
        for (Object[] ob : queryResult) {
            ManageStockTransViewDetailDto detail = new ManageStockTransViewDetailDto();
            detail.setFromNumber(DataUtil.safeToString(ob[0]));
            detail.setToNumber(DataUtil.safeToString(ob[1]));
            detail.setQuantity(DataUtil.safeToString(ob[2]));
            list.add(detail);
        }
        return list;
    }

    @Override
    public Long getSequence() throws Exception {
        return DbUtil.getSequence(emIM, "stock_trans_seq");
    }

    @Override
    public List<VStockTransDTO> searchVStockTransAgent(VStockTransDTO vStockTransDTO) throws Exception {
        StringBuilder builder = new StringBuilder("");
        List lstParameter = new ArrayList();
        builder.append("SELECT   temp2.stockTransId,");
        builder.append("         nameShopExport,");
        builder.append("         nameShopImport,");
        builder.append("         shopIdImport,");
        builder.append("         payStatus,");
        builder.append("         userAction,");
        builder.append("         createDateTime,");
        builder.append("         actionCode,");
        builder.append("         depositStatus, ");
        builder.append("         reason_name");
        builder.append("  FROM       (SELECT   st.stock_trans_id AS stocktransid,");
        builder.append("                       st.deposit_status AS depositstatus,");
        builder.append("                       (select r.reason_name from reason r where r.reason_id = st.reason_id) reason_name,");
        builder.append("                       (SELECT   sh.name");
        builder.append("                          FROM   shop sh");
        builder.append("                         WHERE   sh.shop_id = st.from_owner_id)");
        builder.append("                           AS nameshopexport,");
        builder.append("                       (SELECT   sh1.name");
        builder.append("                          FROM   shop sh1");
        builder.append("                         WHERE   sh1.shop_id = st.to_owner_id)");
        builder.append("                           AS nameshopimport,");
        builder.append("                       st.to_owner_id AS shopidimport,");
        builder.append("                       st.pay_status AS paystatus,");
        builder.append("                       temp.useraction AS useraction,");
        builder.append("                       temp.create_datetime AS createdatetime,");
        builder.append("                       temp.action_code AS actioncode");
        builder.append("                FROM       stock_trans st");
        builder.append("                       INNER JOIN");
        builder.append("                           (SELECT   std.stock_trans_id,");
        builder.append("                                     std.action_code,");
        builder.append("                                     std.create_datetime,");
        builder.append("                                     (SELECT   st1.name");
        builder.append("                                        FROM   staff st1");
        builder.append("                                       WHERE   st1.staff_id =");
        builder.append("                                                   std.action_staff_id)");
        builder.append("                                         AS useraction");
        builder.append("                              FROM   stock_trans_action std ");
        builder.append("                             WHERE   std.create_datetime >= ? ");
        builder.append("                               AND   std.create_datetime < ? + 1) temp");
        builder.append("                       ON st.stock_trans_id = temp.stock_trans_id");
        builder.append("               WHERE       1 = 1");
        lstParameter.add(vStockTransDTO.getStartDate());
        lstParameter.add(vStockTransDTO.getEndDate());
        if (!DataUtil.isNullOrEmpty(vStockTransDTO.getPayStatus())) {
            builder.append("                       AND st.pay_status = ?");
            lstParameter.add(vStockTransDTO.getPayStatus());
        }
        if (!DataUtil.isNullOrEmpty(vStockTransDTO.getActionCode())) {
            builder.append("                       AND temp.action_code = ?");
            lstParameter.add(vStockTransDTO.getActionCode());
        }
        if (!DataUtil.isNullOrZero(vStockTransDTO.getStockTransType())) {
            builder.append("                       AND st.stock_trans_type = ?");
            lstParameter.add(vStockTransDTO.getStockTransType());
        }
        if (!DataUtil.isNullOrZero(vStockTransDTO.getFromOwnerType())) {
            builder.append("                       AND st.from_owner_type = ?");
            lstParameter.add(vStockTransDTO.getFromOwnerType());
        }
        if (!DataUtil.isNullOrZero(vStockTransDTO.getToOwnerType())) {
            builder.append("                       AND st.to_owner_type = ?");
            lstParameter.add(vStockTransDTO.getToOwnerType());
        }
        if (!DataUtil.isNullOrZero(vStockTransDTO.getToOwnerID())) {
            builder.append("                       AND st.to_owner_id = ?");
            lstParameter.add(vStockTransDTO.getToOwnerID());
        }
        builder.append("                       AND st.status = 1");
        builder.append("                       AND st.to_owner_id IN");
        builder.append("                                  (SELECT   sh.shop_id");
        builder.append("                                     FROM       shop sh");
        builder.append("                                            INNER JOIN");
        builder.append("                                                (SELECT   channel_type_id");
        builder.append("                                                   FROM   channel_type ct");
        builder.append("                                                  WHERE   1 = 1");
        builder.append("                                                          AND ct.object_type = 1");
        builder.append("                                                          AND ct.is_vt_unit = 2) temp");
        builder.append("                                            ON sh.channel_type_id = temp.channel_type_id)");
        builder.append("                       AND st.create_datetime >= ?");
        builder.append("                       AND st.create_datetime < ? + 1) temp2");
        builder.append("         INNER JOIN shop sh ON temp2.shopidimport = sh.shop_id");
        builder.append(" WHERE   1 = 1 ");
        lstParameter.add(vStockTransDTO.getStartDate());
        lstParameter.add(vStockTransDTO.getEndDate());
        if (!DataUtil.isNullOrZero(vStockTransDTO.getFromOwnerID())) {
            builder.append("         AND sh.parent_shop_id = ?");
            lstParameter.add(vStockTransDTO.getFromOwnerID());
        }
        builder.append(" order by createdatetime DESC ");

        Query query = emIM.createNativeQuery(builder.toString());

        for (int idx = 0; idx < lstParameter.size(); idx++) {
            query.setParameter(idx + 1, lstParameter.get(idx));
        }

        List<Object[]> objects = query.getResultList();
        List<VStockTransDTO> result = new ArrayList<>();
        if (!DataUtil.isNullOrEmpty(objects)) {
            for (Object[] object : objects) {
                VStockTransDTO a = new VStockTransDTO();
                int index = 0;
                a.setStockTransID(DataUtil.safeToLong(object[index++]));
                a.setFromOwnerName(DataUtil.safeToString(object[index++]));
                a.setToOwnerName(DataUtil.safeToString(object[index++]));
                a.setToOwnerID(DataUtil.safeToLong(object[index++]));
                a.setPayStatus(DataUtil.safeToString(object[index++]));
                a.setUserCreate(DataUtil.safeToString(object[index++]));
                Object obj = object[index++];
                a.setCreateDateTrans(obj != null ? (Date) obj : null);
                a.setActionCode(DataUtil.safeToString(object[index++]));
                a.setDepositStatus(DataUtil.safeToString(object[index++]));
                a.setReasonName(DataUtil.safeToString(object[index]));
                result.add(a);
            }
        }
        return result;
    }


    public StockTransDTO checkAndLockReceipt(Long stockTransId) throws Exception {
        StockTrans stockTrans = emIM.find(StockTrans.class, stockTransId, LockModeType.PESSIMISTIC_WRITE);
        return mapper.toDtoBean(stockTrans);
    }

    public int updateDepositStatus(Long stockTransId) throws Exception {
        StringBuilder strQuery = new StringBuilder();
        strQuery.append("Update stock_trans set deposit_status=1 where stock_trans_id=#stock_trans_id");
        //execute query
        Query query = emIM.createNativeQuery(strQuery.toString(), StockTrans.class);
        query.setParameter("stock_trans_id", stockTransId);
        int total = query.executeUpdate();
        return total;
    }

    @Override
    public void unlockUserInfo(Long transId) throws Exception {
        if (DataUtil.isNullObject(transId)) {
            return;
        }
        StringBuilder sql = new StringBuilder();
        sql.append("delete from lock_user_info where trans_id = #transId");
        Query query = emIM.createNativeQuery(sql.toString());
        query.setParameter("transId", transId);
        query.executeUpdate();
    }

    @Override
    public List<ManageTransStockViewDto> searchStockTransForTransfer(TranferIsdnInfoSearchDto transStockDto) throws LogicException, Exception {
        List<ManageTransStockViewDto> list = Lists.newArrayList();
        if (DataUtil.isNullObject(transStockDto.getFromDate())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.from.date.not.blank");
        }
        if (DataUtil.isNullObject(transStockDto.getToDate())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.to.date.not.blank");
        }
        if (transStockDto.getFromDate().after(transStockDto.getToDate())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.trans.from.than.to");
        }
        long days = 11;
        try {
            days = DateUtil.daysBetween2Dates(transStockDto.getToDate(), transStockDto.getFromDate());
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }

        if (days > 10) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "trans.stock.from.to.over");
        }
        List params = Lists.newArrayList();
        StringBuilder sql = new StringBuilder();
        sql.append("  SELECT   b.action_code transcode,");
        sql.append("           (SELECT   v.owner_name");
        sql.append("              FROM   v_shop_staff v");
        sql.append("             WHERE   v.owner_type = a.from_owner_type");
        sql.append("                     AND v.owner_id = a.from_owner_id)");
        sql.append("               takestockname,");
        sql.append("           (SELECT   v.owner_name");
        sql.append("              FROM   v_shop_staff v");
        sql.append("             WHERE   v.owner_type = a.to_owner_type");
        sql.append("                     AND v.owner_id = a.to_owner_id)");
        sql.append("               receivestockname,");
        sql.append("           a.status status,");
        sql.append("           d.reason_name reason,");
        sql.append("           a.note notes,");
        sql.append("           b.create_user createdStaff,");
        sql.append("           TO_CHAR(b.create_datetime,'DD/MM/YYYY') createdDate,");
        sql.append("           a.stock_trans_id stockTransId,");
        sql.append("           b.action_staff_id actionStaffId,");
        sql.append("           a.from_owner_id fromOwnerId,");
        sql.append("           a.from_owner_type fromOwnerType,");
        sql.append("           a.to_owner_id toOwnerId,");
        sql.append("           a.to_owner_type toOwnerType,");
        sql.append("           a.reason_id reasonId,");
        sql.append("           d.reason_code reasonCode,");
        sql.append("           d.reason_name reasonName,");
        sql.append("           b.stock_trans_action_id stockTransActionId");
        sql.append("    FROM   stock_trans a, stock_trans_action b, stock_trans_detail c,reason d");
        sql.append("   WHERE   a.stock_trans_id = b.stock_trans_id");
        sql.append("           AND a.stock_trans_id = c.stock_trans_id(+)");
        sql.append("           AND a.reason_id=d.reason_id");
        sql.append("           AND a.reason_id=d.reason_id");
        sql.append("           AND b.status IN (1,2,3,7)");
        sql.append("           AND a.stock_trans_type = ? ");
        params.add(Const.STOCK_TRANS_TYPE.ISDN);
        if (!DataUtil.isNullObject(transStockDto.getTransCode())) {
            sql.append("           AND b.action_code LIKE ?");
            params.add("%" + transStockDto.getTransCode() + "%");
        }
        sql.append("           AND (b.create_datetime BETWEEN trunc(?) AND trunc(?) +1 )");
        params.add(transStockDto.getFromDate());
        params.add(transStockDto.getToDate());
        if (!DataUtil.isNullObject(transStockDto.getOwnerDeliverStock())) {
            sql.append("           AND a.from_owner_type=1");
            sql.append("           AND a.from_owner_id=?");
            params.add(transStockDto.getOwnerDeliverStock().getOwnerId());
        }
        if (!DataUtil.isNullObject(transStockDto.getOwnerReceiveStock())) {
            sql.append("           AND a.to_owner_type=1");
            sql.append("           AND a.to_owner_id =?");
            params.add(transStockDto.getOwnerReceiveStock().getOwnerId());
        }
        if (!DataUtil.isNullObject(transStockDto.getStatus())) {
            sql.append("           AND b.status =?");
            params.add(transStockDto.getStatus());
        }
        sql.append("           ORDER BY b.create_datetime DESC,b.action_code ASC");
        Query query = emIM.createNativeQuery(sql.toString());
        for (int i = 0; i < params.size(); i++) {
            query.setParameter(i + 1, params.get(i));
        }
        List<Object[]> queryResult = query.getResultList();
        for (Object[] ob : queryResult) {
            ManageTransStockViewDto view = new ManageTransStockViewDto();
            view.setTransCode(DataUtil.safeToString(ob[0]));
            view.setTakeStockName(DataUtil.safeToString(ob[1]));
            view.setReceiveStockName(DataUtil.safeToString(ob[2]));
            view.setStatus(DataUtil.safeToString(ob[3]));
            view.setReason(DataUtil.safeToString(ob[4]));
            view.setNotes(DataUtil.safeToString(ob[5]));
            view.setCreatedStaff(DataUtil.safeToString(ob[6]));
            view.setCreatedDate(DataUtil.safeToString(ob[7]));
            view.setStockTranId(DataUtil.safeToLong(ob[8]));
            view.setActionStaffId(DataUtil.safeToLong(ob[9]));
            view.setFromOwnerId(DataUtil.safeToLong(ob[10]));
            view.setFromOwnerType(DataUtil.safeToLong(ob[11]));
            view.setToOwnerId(DataUtil.safeToLong(ob[12]));
            view.setToOwnerType(DataUtil.safeToLong(ob[13]));
            view.setReasonId(DataUtil.safeToLong(ob[14]));
            view.setReasonCode(DataUtil.safeToString(ob[15]));
            view.setReasonName(DataUtil.safeToString(ob[16]));
            view.setStockTranActionId(DataUtil.safeToLong(ob[17]));
            list.add(view);
        }
        return list;
    }

    @Override
    public List<VStockTransDTO> mmSearchVStockTrans(VStockTransDTO vStockTransDTO) throws Exception {
        StringBuilder builder = new StringBuilder("");
        List lstParameter = new ArrayList();
        builder.append("SELECT /*+ index(a stock_trans_pk) */");
        builder.append("     a.STOCK_TRANS_ID, b.action_code, b.STOCK_TRANS_ACTION_ID, ");
        builder.append("     a.status, ");
        builder.append("     'stock.trans.status'||nvl(a.status,'x') AS status_name,");
        builder.append("     (SELECT staff.NAME FROM bccs_im.staff WHERE staff_code = b.create_user) as user_create,");
        builder.append("     CASE a.from_owner_type");
        builder.append("             WHEN 1");
        builder.append("               THEN (SELECT shop_code || ' - ' || to_char(name)");
        builder.append("                       FROM bccs_im.shop");
        builder.append("                      WHERE shop_id = a.from_owner_id)");
        builder.append("             WHEN 2");
        builder.append("               THEN (SELECT staff_code || ' - ' || to_char(name)");
        builder.append("                       FROM bccs_im.staff");
        builder.append("                      WHERE staff_id = a.from_owner_id)");
        builder.append("             WHEN 4");
        builder.append("               THEN (SELECT    TO_CHAR (partner_code) || ' - ' || TO_CHAR (partner_name)");
        builder.append("              FROM partner");
        builder.append("                     WHERE partner_id = a.from_owner_id)");
        builder.append("              ELSE TO_CHAR (a.from_owner_id )");
        builder.append("              END AS from_owner_name,");
        builder.append("     CASE a.to_owner_type");
        builder.append("                WHEN 1");
        builder.append("                     THEN (SELECT shop_code || ' - ' || to_char(name)");
        builder.append("                         FROM bccs_im.shop");
        builder.append("                         WHERE shop_id = a.to_owner_id)");
        builder.append("                WHEN 2 ");
        builder.append("                     THEN (SELECT staff_code || ' - ' || to_char(name)");
        builder.append("                         FROM bccs_im.staff");
        builder.append("                         WHERE staff_id = a.to_owner_id)");
        builder.append("                WHEN 4  ");
        builder.append("                     THEN (SELECT    TO_CHAR (partner_code) || ' - ' || TO_CHAR (partner_name)");
        builder.append("                         FROM bccs_im.partner");
        builder.append("                         WHERE partner_id = a.to_owner_id)");
        builder.append("                ELSE TO_CHAR (a.to_owner_id)");
        builder.append("             END AS to_owner_name,");
        builder.append("    case b.status");
        builder.append("        when '4' ");
        builder.append("            then (select reason_name from bccs_im.reason r where r.reason_id=a.import_reason_id )");
        builder.append("        when '5' ");
        builder.append("            then (select reason_name from bccs_im.reason r where r.reason_id=a.import_reason_id )");
        builder.append("        when '6' ");
        builder.append("            then (select reason_name from bccs_im.reason r where r.reason_id=a.import_reason_id )");
        builder.append("        when '8' ");
        builder.append("            then (select reason_name from bccs_im.reason r where r.reason_id=a.reject_reason_id )");
        builder.append("        else");
        builder.append("            (select reason_name from bccs_im.reason r where r.reason_id=a.reason_id)");
        builder.append("        end as reason_name,");
        builder.append("             b.note, b.CREATE_DATETIME, a.from_owner_type, a.from_owner_id, a.to_owner_type, a.to_owner_id,");
        builder.append("     a.is_auto_gen, decode(a.is_auto_gen, 2, 'GD logistic'), ");
        builder.append("     nvl(a.process_offline,0), 'process.offline'||(nvl(a.process_offline, 0)) as process_offline_name, a.process_start, a.process_end, ");
        builder.append("            nvl (c.status, '-2') as voffice,  ");
        builder.append("            decode(b.status, ");
        builder.append("                    1,'stock.trans_type.lx',");
        builder.append("                    2,'stock.trans_type.px',");
        builder.append("                    3,'stock.trans_type.px',");
        builder.append("                    4,'stock.trans_type.ln',");
        builder.append("                    5,'stock.trans_type.pn',");
        builder.append("                    6,'stock.trans_type.pn'");
        builder.append("            ) as stock_trans_type_name, ");
        builder.append("            b.status as action_type,");
        builder.append("            a.stock_trans_type,");
        builder.append("            c.response_code_description");
        builder.append("            FROM   stock_trans a, stock_trans_action b, stock_trans_voffice c");
        builder.append("            WHERE  a.stock_trans_id = b.stock_trans_id and b.stock_trans_action_id =c.stock_trans_action_id(+) ");
        builder.append("            and b.action_code != 'SYSTEM' and a.stock_trans_type !=6");
        builder.append("            and b.create_datetime >= trunc(?)");
        builder.append("            and b.create_datetime <= trunc(? +1)");
        builder.append("            and b.status in (1,2,4,5) "); //Chi lay lenh, phieu xuat nhap
        lstParameter.add(vStockTransDTO.getStartDate());
        lstParameter.add(vStockTransDTO.getEndDate());
        //
        if (!DataUtil.isNullOrZero(vStockTransDTO.getStockTransID())) {
            builder.append(" and a.STOCK_TRANS_ID=?");
            lstParameter.add(vStockTransDTO.getStockTransID());
        }
        //
        if (!DataUtil.isNullOrEmpty(vStockTransDTO.getStockTransStatus())) {
            builder.append(" and a.status=?");
            lstParameter.add(vStockTransDTO.getStockTransStatus());
        }

        if (!DataUtil.isNullOrEmpty(vStockTransDTO.getActionCode())) {
            if (Const.L_VT_SHOP_ID.equals(vStockTransDTO.getShopId())) {
                builder.append(" and b.action_code = ?");
                lstParameter.add(vStockTransDTO.getActionCode());
            } else {
                builder.append(" and b.action_code like ?");
                lstParameter.add(vStockTransDTO.getActionCode() + "%");
            }
        }

        if (!DataUtil.isNullOrEmpty(vStockTransDTO.getActionType())) {
            if (Const.STOCK_TRANS_ACTION_TYPE.COMMAND.equals(vStockTransDTO.getActionType())) {
                builder.append(" and b.status in(select c.value from option_set a, option_set_value c where c.option_set_id=a.id and a.code='")
                        .append(Const.OPTION_SET.STOCK_TRANS_STATUS_CMD).append("')");
            } else {
                builder.append(" and b.status in(select c.value from option_set a, option_set_value c where c.option_set_id=a.id and a.code='")
                        .append(Const.OPTION_SET.STOCK_TRANS_STATUS_NOT).append("')");
            }
        }

        if (!DataUtil.isNullOrZero(vStockTransDTO.getStockTransType())) {
            if (DataUtil.safeToLong(Const.STOCK_TRANS_TYPE.EXPORT).equals(vStockTransDTO.getStockTransType())) {
                builder.append(" and b.status in(select c.value from option_set a, option_set_value c where c.option_set_id=a.id and a.code='")
                        .append(Const.OPTION_SET.STOCK_TRANS_STATUS_EX).append("')");
            } else {
                builder.append(" and b.status in(select c.value from option_set a, option_set_value c where c.option_set_id=a.id and a.code='")
                        .append(Const.OPTION_SET.STOCK_TRANS_STATUS_IM).append("')");
            }
        }

        if (!DataUtil.isNullOrZero(vStockTransDTO.getOwnerID())) {
            builder.append(" and(");
            if (!DataUtil.isNullOrZero(vStockTransDTO.getOwnerType())) {
                builder.append(" (a.from_owner_type=? and a.from_owner_id=?)");
                builder.append(" or (a.to_owner_type=? and a.to_owner_id=?)");
                lstParameter.add(vStockTransDTO.getOwnerType());
                lstParameter.add(vStockTransDTO.getOwnerID());
                lstParameter.add(vStockTransDTO.getOwnerType());
                lstParameter.add(vStockTransDTO.getOwnerID());
            } else {
                builder.append(" a.from_owner_id=?");
                builder.append(" or a.to_owner_id=?");
                lstParameter.add(vStockTransDTO.getOwnerID());
                lstParameter.add(vStockTransDTO.getOwnerID());
            }
            builder.append(")");
        } else {
            if (!DataUtil.isNullOrZero(vStockTransDTO.getOwnerType())) {
                builder.append(" and (");
                if (Const.OWNER_TYPE.SHOP_LONG.equals(vStockTransDTO.getOwnerType())) {
                    builder.append(" (a.from_owner_type = ?");
                    lstParameter.add(Const.OWNER_TYPE.SHOP);
                    builder.append(" and a.to_owner_type = ?");
                    lstParameter.add(Const.OWNER_TYPE.SHOP);
                    builder.append(" and a.from_owner_id = ?)");
                    lstParameter.add(vStockTransDTO.getShopId());
                    //
                    builder.append(" or (a.to_owner_type = ?");
                    lstParameter.add(Const.OWNER_TYPE.SHOP);
                    builder.append(" and a.from_owner_type = ?");
                    lstParameter.add(Const.OWNER_TYPE.SHOP);
                    builder.append(" and a.to_owner_id = ?)").append(")");
                    lstParameter.add(vStockTransDTO.getShopId());
                } else {
                    if (vStockTransDTO.isStaffInShop()) {
                        builder.append(" (a.from_owner_type = ?");
                        lstParameter.add(Const.OWNER_TYPE.STAFF);
                        builder.append(" and a.from_owner_id in (select staff_id from staff where shop_id = ? and status = 1))");
                        lstParameter.add(vStockTransDTO.getShopId());
                        //
                        builder.append(" or (a.to_owner_type = ?");
                        lstParameter.add(Const.OWNER_TYPE.STAFF);
                        builder.append(" and a.to_owner_id in (select staff_id from staff where shop_id = ? and status = 1))").append(")");
                        lstParameter.add(vStockTransDTO.getShopId());
                    } else {
                        builder.append(" (a.from_owner_type = ?");
                        lstParameter.add(Const.OWNER_TYPE.STAFF);
                        builder.append(" and a.from_owner_id = ?)");
                        lstParameter.add(vStockTransDTO.getStaffId());
                        //
                        builder.append(" or (a.to_owner_type = ?");
                        lstParameter.add(Const.OWNER_TYPE.STAFF);
                        builder.append(" and a.to_owner_id = ?)").append(")");
                        lstParameter.add(vStockTransDTO.getStaffId());
                    }
                }
            } else {
                builder.append(" and (");
                builder.append(" (a.from_owner_type = ?");
                lstParameter.add(Const.OWNER_TYPE.SHOP);

                builder.append(" and a.from_owner_id = ?)");
                lstParameter.add(vStockTransDTO.getShopId());

                builder.append(" or (a.from_owner_type = ?");
                lstParameter.add(Const.OWNER_TYPE.STAFF);

                builder.append(" and a.from_owner_id = ?)");
                lstParameter.add(vStockTransDTO.getStaffId());

                builder.append(" or (a.to_owner_type = ?");
                lstParameter.add(Const.OWNER_TYPE.STAFF);

                builder.append(" and a.to_owner_id = ?)");
                lstParameter.add(vStockTransDTO.getStaffId());

                builder.append(" or (a.to_owner_type = ?");
                lstParameter.add(Const.OWNER_TYPE.SHOP);

                builder.append(" and a.to_owner_id = ?)").append(")");
                lstParameter.add(vStockTransDTO.getShopId());
            }
        }


        builder.append(" order by a.create_datetime DESC FETCH FIRST ").append(Const.NUMBER_SEARCH.NUMBER_ALL).append(" ROWS ONLY");
        //set params
        Query query = emIM.createNativeQuery(builder.toString());
        query.setHint(QueryHints.JDBC_TIMEOUT, 300);
        for (int i = 0; i < lstParameter.size(); i++) {
            query.setParameter(i + 1, lstParameter.get(i));
        }
        List<VStockTransDTO> result = Lists.newArrayList();
        List objects = query.getResultList();
        for (Object object : objects) {
            int index = 0;
            Object[] arrObject = (Object[]) object;
            VStockTransDTO item = new VStockTransDTO();
            item.setStockTransID(DataUtil.safeToLong(arrObject[index++]));
            item.setActionCode(DataUtil.safeToString(arrObject[index++]));
            item.setActionID(DataUtil.safeToLong(arrObject[index++]));
            item.setStockTransStatus(DataUtil.safeToString(arrObject[index++]));
            item.setStatusName(GetTextFromBundleHelper.getText(DataUtil.safeToString(arrObject[index++])));
            item.setUserCreate(DataUtil.safeToString(arrObject[index++]));
            item.setFromOwnerName(DataUtil.safeToString(arrObject[index++]));
            item.setToOwnerName(DataUtil.safeToString(arrObject[index++]));
            item.setReasonName(DataUtil.safeToString(arrObject[index++]));
            item.setNote(DataUtil.safeToString(arrObject[index++]));
            item.setCreateDateTime((Date) arrObject[index++]);
            item.setFromOwnerType(DataUtil.safeToLong(arrObject[index++]));
            item.setFromOwnerID(DataUtil.safeToLong(arrObject[index++]));
            item.setToOwnerType(DataUtil.safeToLong(arrObject[index++]));
            item.setToOwnerID(DataUtil.safeToLong(arrObject[index++]));
            index++;//15 is_auto_gen
            index++;//16 logistics
            item.setProcessOffline(DataUtil.safeToString(arrObject[index++]));
            item.setProcessOfflineName(GetTextFromBundleHelper.getText(DataUtil.safeToString(arrObject[index++])));
            item.setStartDate((Date) arrObject[index++]);
            item.setEndDate((Date) arrObject[index++]);
            item.setSignVoffice(DataUtil.safeToString(arrObject[index++]));
            item.setStockTransTypeName(GetTextFromBundleHelper.getText(DataUtil.safeToString(arrObject[index++])));
            item.setActionType(DataUtil.safeToString(arrObject[index++]));
            item.setStockTransType(DataUtil.safeToLong(arrObject[index++]));
            String respons = DataUtil.safeToString(arrObject[index]);
            if (!DataUtil.isNullOrEmpty(respons)) {
                StringBuilder vOfficeName = new StringBuilder("");
                vOfficeName.append(item.getSignVofficeName()).append(" :");
                vOfficeName.append(respons);
                item.setSignVofficeName(vOfficeName.toString());
            }
            result.add(item);
        }
        return result;
    }

    @Override
    public boolean checkExistTransByfieldExportIsdnDTO(FieldExportFileDTO fieldExportFileDTO) throws LogicException, Exception {
        StringBuilder builder = new StringBuilder("");
        builder.append(" SELECT a.stock_trans_detail_id, a.product_offer_type_id, a.prod_offer_type_name, a.prod_offer_id, ");
        builder.append(" a.prod_offer_code, a.prod_offer_name, a.check_serial, a.check_deposit, a.accounting_model_code, ");
        builder.append(" a.unit, a.from_owner_type, a.from_owner_id, a.to_owner_type,a.to_owner_id,a.create_datetime, ");
        builder.append(" a.stock_trans_type,a.stock_trans_id,a.reason_id,a.stock_trans_status,a.pay_status,a.deposit_status, ");
        builder.append(" a.action_code,a.stock_trans_action_id,a.action_type,a.username,a.action_staff_id,a.state_id, ");
        builder.append(" a.quantity,a.basis_price,a.state_name,a.region_stock_trans_id,a.is_auto_gen,a.table_name, ");
        builder.append(" a.source_price");
        builder.append(" FROM v_stock_trans_detail a ");
        builder.append(" where a.action_code= #action_code ");
        builder.append(" and   a.prod_offer_id= #prod_offer_id ");
        builder.append(" and   a.from_owner_id= #from_owner_id ");
        builder.append(" adn   a.to_owner_id= #to_owner_id ");
        Query query = emIM.createNativeQuery(builder.toString());
        query.setParameter("action_code", fieldExportFileDTO.getActionCode());
        query.setParameter("prod_offer_id", fieldExportFileDTO.getProductOfferId());
        query.setParameter("from_owner_id", fieldExportFileDTO.getFromOwnerId());
        query.setParameter("to_owner_id", fieldExportFileDTO.getToOwnerId());
        List objects = query.getResultList();
        if (!DataUtil.isNullOrEmpty(objects))
            return true;
        else
            return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateSerialForRevoke(
            Long prodOfferTypeId,
            Long prodOfferId,
            String serial,
            Long ownerId,
            Long stateId,
            Long oldOwnerType,
            Long oldOwnerId,
            Long revokeType) throws LogicException, Exception {
        try {

            StringBuilder strQuery = new StringBuilder();
            String tableName = IMServiceUtil.getTableNameByOfferType(prodOfferTypeId);

            if (DataUtil.isNullOrEmpty(tableName)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "im.tableName.not.exists");
            }

            strQuery.append("UPDATE   ");
            strQuery.append(tableName);
            strQuery.append("   SET   state_id = ?, ");//--hang thu hoi
            strQuery.append("         status = ?, "); //trong kho
            strQuery.append("         owner_id = ?, ");//--posId_id
            strQuery.append("         owner_type = ?, ");//--old_owner_type
            strQuery.append("         update_datetime = sysdate ");

            if (DataUtil.safeEqual(prodOfferTypeId, Const.STOCK_TYPE.STOCK_HANDSET)) {
                strQuery.append(" WHERE   serial = ? ");
            } else {
                strQuery.append(" WHERE   TO_NUMBER (serial) = ? ");
            }

            strQuery.append(" and prod_offer_id = ? ");
            strQuery.append(" and owner_type = ?  ");
            strQuery.append(" and owner_id = ?  ");

            Query query = emIM.createNativeQuery(strQuery.toString());

            query.setParameter(1, stateId); //trang thai sau khi thu hoi (thu hoi sim: hang hong, thu hoi to doi: hang thu hoi)
            query.setParameter(2, Const.NEW); //trong kho
            query.setParameter(3, ownerId);

            if (revokeType.compareTo(1L) == 0) {
                //Neu to doi thu hoi
                query.setParameter(4, Const.OWNER_TYPE.SHOP_LONG);
            } else {
                //Neu nhan vien thu hoi
                query.setParameter(4, Const.OWNER_TYPE.STAFF_LONG);
            }

            query.setParameter(5, serial);
            query.setParameter(6, prodOfferId);
            query.setParameter(7, oldOwnerType);
            query.setParameter(8, oldOwnerId);

            int numberRowUpdated = query.executeUpdate();
            if (numberRowUpdated != 1) {
                return false;
            }

            return true;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            return false;
        }
    }

    public boolean checkStockSusbpendForProcessStock(Long ownerId, String ownerType, Long productOfferId) throws LogicException, Exception {
        StringBuilder builder = new StringBuilder(" ");
        builder.append(" select a.stock_trans_id ");
        builder.append(" from stock_trans a, stock_trans_detail b ");
        builder.append(" where a.stock_trans_id = b.stock_trans_id ");
        builder.append(" and b.prod_offer_id = #productOfferId ");
        builder.append(" and a.status " + DbUtil.createInQuery("status", Lists.newArrayList(Const.STOCK_TRANS_STATUS.EXPORT_ORDER, Const.STOCK_TRANS_STATUS.EXPORT_NOTE,
                Const.STOCK_TRANS_STATUS.EXPORTED, Const.STOCK_TRANS_STATUS.IMPORT_ORDER, Const.STOCK_TRANS_STATUS.IMPORT_NOTE)));
        builder.append(" and a.to_owner_id =#ownerId ");
        builder.append(" and a.to_owner_type = #ownerType ");
        builder.append(" and a.create_datetime >= TRUNC (SYSDATE)- (");
        builder.append(" SELECT osv.value ");
        builder.append(" FROM bccs_im.option_set os, bccs_im.option_set_value osv");
        builder.append(" WHERE os.id = osv.option_set_id");
        builder.append(" AND os.code = 'SUSBPEND_DAY')");
        builder.append(" AND a.create_datetime <= SYSDATE ");
        Query query = emIM.createNativeQuery(builder.toString());
        query.setParameter("productOfferId", productOfferId);
        query.setParameter("ownerId", ownerId);
        query.setParameter("ownerType", ownerType);
        DbUtil.setParamInQuery(query, "status", Lists.newArrayList(Const.STOCK_TRANS_STATUS.EXPORT_ORDER, Const.STOCK_TRANS_STATUS.EXPORT_NOTE,
                Const.STOCK_TRANS_STATUS.EXPORTED, Const.STOCK_TRANS_STATUS.IMPORT_ORDER, Const.STOCK_TRANS_STATUS.IMPORT_NOTE));
        List objects = query.getResultList();
        if (!DataUtil.isNullOrEmpty(objects)) {
            return false;
        }
        return true;
    }

    @Override
    public int deleteSerialExportRange(String tableName, String fromSerial, String toSerial, Long prodOfferId, Long stateId, Long ownerId, boolean isCharacterSerial) throws LogicException, Exception {
        StringBuilder sqlDelete = new StringBuilder(" ");
        sqlDelete.append(" delete from  ");
        sqlDelete.append(tableName);
        sqlDelete.append(" where prod_offer_id = #prod_offer_id and status = #status and state_id = #state_id ");
        if (!isCharacterSerial) {
            sqlDelete.append(" and to_number(serial)>= #fromSerial and to_number(serial)<= #toSerial and owner_id = #owner_id and owner_type = #owner_type ");
        } else {
            sqlDelete.append(" and serial >= #fromSerial and serial <= #toSerial and length(serial) = length(#fromSerial) and owner_id = #owner_id and owner_type = #owner_type ");
        }
        Query query = emIM.createNativeQuery(sqlDelete.toString());
        query.setParameter("prod_offer_id", prodOfferId);
        query.setParameter("status", Const.STATUS.ACTIVE);
        query.setParameter("state_id", stateId);
        if (!isCharacterSerial) {
            query.setParameter("fromSerial", new BigInteger(fromSerial));
            query.setParameter("toSerial", new BigInteger(toSerial));
        } else {
            query.setParameter("fromSerial", fromSerial);
            query.setParameter("toSerial", toSerial);
        }
        query.setParameter("owner_id", ownerId);
        query.setParameter("owner_type", Const.OWNER_TYPE.SHOP_LONG);
        return query.executeUpdate();
    }

    @Override
    public int deleteSerialIM1ExportRange(String tableName, String fromSerial, String toSerial, Long prodOfferId, Long stateId, Long ownerId, boolean isCharacterSerial) throws LogicException, Exception {
        StringBuilder sqlDelete = new StringBuilder(" ");
        sqlDelete.append(" delete from bccs_im.");
        sqlDelete.append(tableName);
        sqlDelete.append(" where stock_model_id = #stock_model_id and status = #status and state_id = #state_id ");
        if (!isCharacterSerial) {
            sqlDelete.append(" and to_number(serial)>= #fromSerial and to_number(serial)<= #toSerial and owner_id = #owner_id and owner_type = #owner_type ");
        } else {
            sqlDelete.append(" and serial >= #fromSerial and serial <= #toSerial and length(serial) = length(#fromSerial) and owner_id = #owner_id and owner_type = #owner_type ");
        }
        Query query = emIMLib.createNativeQuery(sqlDelete.toString());
        query.setParameter("stock_model_id", prodOfferId);
        query.setParameter("status", Const.STATUS.ACTIVE);
        query.setParameter("state_id", stateId);
        if (!isCharacterSerial) {
            query.setParameter("fromSerial", new BigInteger(fromSerial));
            query.setParameter("toSerial", new BigInteger(toSerial));
        } else {
            query.setParameter("fromSerial", fromSerial);
            query.setParameter("toSerial", toSerial);
        }
        query.setParameter("owner_id", ownerId);
        query.setParameter("owner_type", Const.OWNER_TYPE.SHOP_LONG);
        return query.executeUpdate();
    }

    public List<StockTotalDTO> checkHaveProductOffering(Long ownerId, Long ownerType, boolean checkCollaborator) throws Exception {
        List<StockTotalDTO> lstResult = Lists.newArrayList();
        StringBuilder sqlCheck = new StringBuilder(" ");
        sqlCheck.append(" SELECT   st.owner_id, s.staff_code, ");
        sqlCheck.append(" LISTAGG(p.code || '-'  || (select name from option_set_value where option_set_id =  ");
        sqlCheck.append(" (select id from option_Set where code LIKE 'GOOD_STATE') and value = to_Char(st.state_id)), ',') WITHIN GROUP (ORDER BY st.owner_id)  ");
        sqlCheck.append("   FROM   stock_total st, product_offering p, staff s");
        sqlCheck.append("  WHERE       st.prod_offer_id = p.prod_offer_id ");
        sqlCheck.append("          AND st.owner_id = s.staff_id ");
        sqlCheck.append("          AND st.owner_type = 2 ");
        sqlCheck.append("          AND st.current_quantity > 0 ");
        sqlCheck.append("          AND st.status = 1 ");
        if (checkCollaborator) {
            sqlCheck.append(" and owner_id in (select staff_id from staff where staff_owner_id = #ownerId and status = 1 and rownum = 1) ");
        } else {
            sqlCheck.append(" and owner_id = #ownerId ");
        }
        sqlCheck.append("          group by  st.owner_id, s.staff_code ");
        Query query = emIM.createNativeQuery(sqlCheck.toString());
        query.setParameter("ownerId", ownerId);
        query.setParameter("ownerType", ownerType);
        List objects = query.getResultList();
        for (Object object : objects) {
            Object[] arrObject = (Object[]) object;
            StockTotalDTO dto = new StockTotalDTO();
            dto.setOwnerCode(DataUtil.safeToString(arrObject[1]));
            dto.setCode(DataUtil.safeToString(arrObject[2]));
            lstResult.add(dto);
        }
        return lstResult;
    }


    public List<String> checkStockTransSuppend(Long ownerId, Long ownerType) throws Exception {
        StringBuilder sqlCheck = new StringBuilder(" ");
        sqlCheck.append(" select (select action_code from stock_trans_action sta where sta.stock_trans_id = stock_trans_id   ");
        sqlCheck.append(" and  status = 1 and rownum = 1)  ");
        sqlCheck.append(" FROM stock_trans  ");
        sqlCheck.append(" WHERE 1 = 1 ");
        sqlCheck.append(" AND to_owner_type <> 3 ");
        sqlCheck.append(" AND status IN (1,2,3,4,5) ");
        sqlCheck.append(" AND ((from_owner_id = #ownerId AND from_owner_type = #ownerType) OR (to_owner_id = #ownerId AND to_owner_type = #ownerType)) ");
        Query query = emIM.createNativeQuery(sqlCheck.toString());
        query.setParameter("ownerId", ownerId);
        query.setParameter("ownerType", ownerType);
        return (List<String>) query.getResultList();
    }

    @Override
    public Long getTransAmount(Long transID) throws Exception {

        StringBuilder builder = new StringBuilder("select sum(amount) from stock_trans_detail where stock_trans_id=?");
        Query query = emIM.createNativeQuery(builder.toString());
        query.setParameter(1, transID);
        List result = query.getResultList();
        if (!DataUtil.isNullOrEmpty(result) && result.get(0) != null) {
            return DataUtil.safeToLong(result.get(0));
        }
        return 0L;
    }


    public StockTransDTO getStockTransForLogistics(Long stockTransId) throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT   'VTT' custCode, ")
                .append("           st.stock_trans_id stockTransId, ")
                .append("           st.region_stock_trans_id regionStockTransId, ")
                .append("           st.status stockTransStatus,  ")
                .append("           st.from_owner_id fromOwnerId,  ")
                .append("           st.from_owner_type fromOwnerType, ")
                .append("           st.to_owner_id toOwnerId,  ")
                .append("           st.to_owner_type toOwnerType,  ")
                .append("           st.create_datetime createDateTime, ")
                .append("           st.pay_status payStatus, ")
                .append("           st.deposit_status depositStatus,  ")
                .append("           (CASE ")
                .append("              WHEN st.status IN (1,2,3) ")
                .append("              THEN  ")
                .append("                  DECODE ( ")
                .append("                      st.region_stock_trans_id, ")
                .append("                       NULL, ")
                .append("                       (SELECT   sh.shop_code")
                .append("                         FROM   shop sh ")
                .append("                           WHERE   sh.shop_id = st.from_owner_id), ")
                .append("                           (SELECT   shop_code  ")
                .append("                                 FROM   shop  ")
                .append("                               WHERE   shop_id = (SELECT   from_owner_id FROM   stock_trans WHERE stock_trans_id = ")
                .append("                                   st.region_stock_trans_id)))  ")
                .append("               WHEN st.status IN (4,5,6) ")
                .append("               THEN ")
                .append("                   DECODE (")
                .append("                   st.region_stock_trans_id,")
                .append("                      NULL, ")
                .append("                   (SELECT   sh.shop_code   ")
                .append("                       FROM   shop sh ")
                .append("                       WHERE   sh.shop_id = st.to_owner_id), ")
                .append("                       (SELECT   shop_code FROM   shop WHERE   shop_id =  ")
                .append("                       (SELECT   to_owner_id  ")
                .append("                       FROM   stock_trans  ")
                .append("                       WHERE   stock_trans_id = st.region_stock_trans_id))) END) ")
                .append("                       stockcode, ")
                .append("                       ( CASE ")
                .append("                       WHEN  st.status IN (1,2,3)  ")
                .append("                       THEN ")
                .append("                        (CASE st.to_owner_type  ")
                .append("                          WHEN 1 ")
                .append("                               THEN ")
                .append("                               (SELECT sh.shop_code  ")
                .append("                               FROM shop sh ")
                .append("                               WHERE sh.shop_id = st.to_owner_id) ")
                .append("                          WHEN 2   ")
                .append("                               THEN  ")
                .append("                               (SELECT sf.staff_code ")
                .append("                               FROM staff sf  ")
                .append("                               WHERE sf.staff_id = st.to_owner_id)  ")
                .append("                               ELSE ")
                .append("                               NULL ")
                .append("                           END) ")
                .append("                        WHEN st.status IN (4,5,6)  ")
                .append("                            THEN  ")
                .append("                               NULL  ")
                .append("                     END)  ")
                .append("                        areacode, ")
                .append("         DECODE (st.to_owner_type, 2, 2, 1) AS typeReceive,  ")
                .append("                      (CASE  ")
                .append("                          WHEN st.status IN (1,2,3) THEN (2)  ")
                .append("                          WHEN st.status IN (4,5,6) THEN (1)   ")
                .append("                        END) orderType,   ")
                .append("                     SYSDATE ieExpectDate,   ")
                .append("                     (CASE    ")
                .append("                           WHEN st.status IN (1,2,3) THEN NULL    ")
                .append("                           WHEN st.status IN (4,5,6) THEN DECODE (st.from_owner_type, 4, 1, 2)    ")
                .append("                     END) inputType,  ")
                .append("                     (CASE   ")
                .append("                         WHEN st.status IN (1,2,3) THEN DECODE  (st.to_owner_type, 4, 1, 2)    ")
                .append("                     WHEN st.status IN (4,5,6) THEN NULL   ")
                .append("                    END) outputType,    ")
                .append("              (CASE    ")
                .append("                     WHEN st.status IN (1,2,3)   ")
                .append("                     THEN   ")
                .append("                           (SELECT   sh.address    ")
                .append("                               FROM   shop sh    ")
                .append("                           WHERE   sh.shop_id = st.from_owner_id)    ")
                .append("                     WHEN st.status IN (4,5,6)   ")
                .append("                     THEN   ")
                .append("                           NULL ")
                .append("                     END) location, ")
                .append("                     sta.action_code ")
                .append("             FROM   stock_trans st, stock_trans_action sta ")
                .append("            WHERE       st.stock_trans_id = #stockTransId  ")
                .append("            AND sta.stock_trans_id = st.stock_trans_id ")
                .append("            AND sta.status in (1,4) ")
                .append("            AND st.status in (1,4) ");
        Query query = emIM.createNativeQuery(sql.toString());
        query.setParameter("stockTransId", stockTransId);
        List queryResultList = query.getResultList();
        StockTransDTO stockTransDTO = new StockTransDTO();
        if (!DataUtil.isNullOrEmpty(queryResultList)) {
            for (Object queryResult : queryResultList) {
                Object[] objects = (Object[]) queryResult;
                stockTransDTO.setCustCode(DataUtil.safeToString(objects[0]));
                stockTransDTO.setStockTransId(DataUtil.safeToLong(objects[1]));
                stockTransDTO.setRegionStockTransId(DataUtil.safeToLong(objects[2]));
                stockTransDTO.setStatus(DataUtil.safeToString(objects[3]));
                stockTransDTO.setFromOwnerId(DataUtil.safeToLong(objects[4]));
                stockTransDTO.setFromOwnerType(DataUtil.safeToLong(objects[5]));
                stockTransDTO.setToOwnerId(DataUtil.safeToLong(objects[6]));
                stockTransDTO.setToOwnerType(DataUtil.safeToLong(objects[7]));
                Object createDate = objects[8];
                stockTransDTO.setCreateDatetime(createDate != null ? (Date) createDate : null);
                stockTransDTO.setPayStatus(DataUtil.safeToString(objects[9]));
                stockTransDTO.setDepositStatus(DataUtil.safeToString(objects[10]));

                stockTransDTO.setStockCode(DataUtil.safeToString(objects[11]));
                stockTransDTO.setAreaCode(DataUtil.safeToString(objects[12]));
                stockTransDTO.setTypeReceive(DataUtil.safeToString(objects[13]));
                stockTransDTO.setOrderType(DataUtil.safeToString(objects[14]));
                Object ieExpectDate = objects[15];
                stockTransDTO.setIeExpectDate(ieExpectDate != null ? (Date) ieExpectDate : null);
                stockTransDTO.setInputType(DataUtil.safeToLong(objects[16]));
                stockTransDTO.setOutputType(DataUtil.safeToLong(objects[17]));
                stockTransDTO.setLocation(DataUtil.safeToString(objects[18]));
                stockTransDTO.setActionCode(DataUtil.safeToString(objects[19]));

                return stockTransDTO;
            }
        }
        return null;
    }

    public boolean checkSaleTrans(Long stockTransId, Date saleTransDate) throws Exception {
        StringBuilder strQuery = new StringBuilder("");
        strQuery.append(" SELECT   1 ");
        strQuery.append("   FROM   sale_trans ");
        strQuery.append("  WHERE   sale_trans_date >= TRUNC(#createDate) AND stock_trans_id = #stockTransId AND status = 3 ");
        Query query = sale.createNativeQuery(strQuery.toString());
        query.setParameter("createDate", saleTransDate);
        query.setParameter("stockTransId", stockTransId);
        List queryResultList = query.getResultList();
        if (!DataUtil.isNullOrEmpty(queryResultList)) {
            return true;
        } else {
            return false;
        }
    }

    public List<DOATransferDTO> getContentFileDOA(Long stockTransId) throws Exception {
        StringBuilder strQuery = new StringBuilder("");
        List<DOATransferDTO> lstContents = Lists.newArrayList();
        strQuery.append(" SELECT   d.owner_code, ");
        strQuery.append("          d.owner_name, ");
        strQuery.append("          c.code, ");
        strQuery.append("          c.name, ");
        strQuery.append("          b.from_serial fromSerial, ");
        strQuery.append("          b.to_serial toSerial, ");
        strQuery.append("          b.quantity, ");
        strQuery.append("          e.action_code, ");
        strQuery.append("          e.create_user, ");
        strQuery.append("          d.parent_shop_id, d.owner_type, d.owner_id, a.create_datetime ");
        strQuery.append("   FROM   stock_trans a, ");
        strQuery.append("          stock_trans_serial b, ");
        strQuery.append("          product_offering c, ");
        strQuery.append("          v_shop_staff d, ");
        strQuery.append("          stock_trans_action e ");
        strQuery.append("  WHERE       a.from_owner_id = d.owner_id ");
        strQuery.append("          AND a.from_owner_type = d.owner_type ");
        strQuery.append("          AND a.stock_trans_id = b.stock_trans_id ");
        strQuery.append("          AND b.prod_offer_id = c.prod_offer_id ");
        strQuery.append("          AND a.stock_trans_id = e.stock_trans_id ");
        strQuery.append("          AND e.status = 2 ");
        strQuery.append("          AND a.stock_trans_id = #stockTransId ");

        Query query = emIM.createNativeQuery(strQuery.toString());
        query.setParameter("stockTransId", stockTransId);
        List queryResultList = query.getResultList();
        if (!DataUtil.isNullOrEmpty(queryResultList)) {
            for (Object queryResult : queryResultList) {
                Object[] objects = (Object[]) queryResult;
                DOATransferDTO doaTransferDTO = new DOATransferDTO();
                doaTransferDTO.setOwnerCode(DataUtil.safeToString(objects[0]));
                doaTransferDTO.setOwnerName(DataUtil.safeToString(objects[1]));
                doaTransferDTO.setProdOfferCode(DataUtil.safeToString(objects[2]));
                doaTransferDTO.setProdOfferName(DataUtil.safeToString(objects[3]));
                doaTransferDTO.setFromSerial(DataUtil.safeToString(objects[4]));
                doaTransferDTO.setToSerial(DataUtil.safeToString(objects[5]));
                doaTransferDTO.setQuantity(DataUtil.safeToLong(objects[6]));
                doaTransferDTO.setActionCode(DataUtil.safeToString(objects[7]));
                doaTransferDTO.setCreateUser(DataUtil.safeToString(objects[8]));
                doaTransferDTO.setParrentShopId(DataUtil.safeToLong(objects[9]));
                doaTransferDTO.setOwnerType(DataUtil.safeToLong(objects[10]));
                doaTransferDTO.setOwnerId(DataUtil.safeToLong(objects[11]));
                doaTransferDTO.setCreateDate(objects[12] != null ? (Date) objects[12] : null);
                doaTransferDTO.setStockTransId(stockTransId);
                lstContents.add(doaTransferDTO);
            }
        }
        return lstContents;
    }

    @Override
    public VStockTransDTO findVStockTransDTO(Long transID, Long actionID) throws Exception {
        StringBuilder builder = new StringBuilder("");
        List lstParameter = new ArrayList();

        builder.append("select stock_trans_id, stock_trans_type, action_type, create_datetime, user_code, ");
        builder.append("       user_create, from_owner_id, from_owner_type, from_owner_name, to_owner_id, ");
        builder.append("       to_owner_type, to_owner_name, action_code, action_id, note, reason_id, ");
        builder.append("       reason_name, stock_trans_status, status_name, his_action, deposit_status, ");
        builder.append("       pay_status, stock_base, action_cmd_code, create_date_trans, invoice_status, ");
        builder.append("       import_note, is_auto_gen, import_reason_id, import_reason_name, transport ");
        builder.append("  from v_stock_trans a ");
        builder.append(" where a.action_id=? ");
        if (!DataUtil.isNullOrZero(transID)) {
            builder.append("   and a.stock_trans_id=? ");
            lstParameter.add(transID);
        }

        lstParameter.add(actionID);

        Query query = emIM.createNativeQuery(builder.toString());

        for (int idx = 0; idx < lstParameter.size(); idx++) {
            query.setParameter(idx + 1, lstParameter.get(idx));
        }

        List<Object[]> objects = query.getResultList();
        if (!DataUtil.isNullOrEmpty(objects)) {
            Object[] object = objects.get(0);
            VStockTransDTO a = new VStockTransDTO();
            int index = 0;
            a.setStockTransID(DataUtil.safeToLong(object[index++]));
            a.setStockTransType(DataUtil.safeToLong(object[index++]));
            a.setActionType(DataUtil.safeToString(object[index++]));
            a.setCreateDateTime((Date) object[index++]);
            a.setUserCode(DataUtil.safeToString(object[index++]));
            a.setUserCreate(DataUtil.safeToString(object[index++]));
            a.setFromOwnerID(DataUtil.safeToLong(object[index++]));
            a.setFromOwnerType(DataUtil.safeToLong(object[index++]));
            a.setFromOwnerName(DataUtil.safeToString(object[index++]));
            a.setToOwnerID(DataUtil.safeToLong(object[index++]));
            a.setToOwnerType(DataUtil.safeToLong(object[index++]));
            a.setToOwnerName(DataUtil.safeToString(object[index++]));
            a.setActionCode(DataUtil.safeToString(object[index++]));
            a.setActionID(DataUtil.safeToLong(object[index++]));
            a.setNote(DataUtil.safeToString(object[index++]));
            a.setReasonID(DataUtil.safeToLong(object[index++]));
            a.setReasonName(DataUtil.safeToString(object[index++]));
            a.setStockTransStatus(DataUtil.safeToString(object[index++]));
            a.setStatusName(DataUtil.safeToString(object[index++]));
            a.setHisAction(DataUtil.safeToString(object[index++]));
            a.setDepositStatus(DataUtil.safeToString(object[index++]));
            a.setPayStatus(DataUtil.safeToString(object[index++]));
            a.setStockBase(DataUtil.safeToString(object[index++]));
            a.setActionCMDCode(DataUtil.safeToString(object[index++]));
            a.setCreateDateTrans((Date) object[index++]);
            a.setInvoiceStatus(DataUtil.safeToString(object[index++]));
            a.setImportNote(DataUtil.safeToString(object[index++]));
            a.setIsAutoGen(DataUtil.safeToLong(object[index++]));
            a.setImportReasonID(DataUtil.safeToLong(object[index++]));
            a.setImportReasonName(DataUtil.safeToString(object[index++]));
            a.setTransport(DataUtil.safeToString(object[index]));
            return a;
        }
        return null;
    }

    public List<StockTransDTO> getLstRequestOrderExported() throws Exception {
        List<StockTransDTO> lstResult = Lists.newArrayList();
        StringBuilder strQuery = new StringBuilder("");
        strQuery.append("  SELECT a.stock_trans_id, NVL(b.retry, 0) retry ");
        strQuery.append("  FROM bccs_im.stock_trans a ");
        strQuery.append("         LEFT JOIN stock_request_order_trans b");
        strQuery.append("             ON     b.stock_trans_id = a.stock_trans_id ");
        strQuery.append("                AND b.stock_trans_type = 2");
        strQuery.append("   WHERE     a.create_datetime >= SYSDATE - 30 ");
        strQuery.append("         AND a.create_datetime <= SYSDATE ");
        strQuery.append("         AND a.status = 3   ");
        strQuery.append("        AND a.is_auto_gen = 4 ");
        strQuery.append("        AND (b.retry IS NULL OR b.retry < 5)  ");
        Query query = emIM.createNativeQuery(strQuery.toString());
        List<Object[]> objects = query.getResultList();
        if (!DataUtil.isNullOrEmpty(objects)) {
            for (Object[] obj : objects) {
                StockTransDTO a = new StockTransDTO();
                int index = 0;
                a.setStockTransId(DataUtil.safeToLong(obj[index]));
                lstResult.add(a);
            }
        }
        return lstResult;
    }

    public boolean checkSuspendStock(Long ownerType, Long ownerId) throws Exception {
        StringBuilder strQuery = new StringBuilder("");
        strQuery.append("SELECT * ");
        strQuery.append(" FROM bccs_im.stock_trans st ");
        strQuery.append(" WHERE ( (st.to_owner_id = #ownerId AND st.to_owner_type = #ownerType) OR (st.from_owner_id = #ownerId AND st.from_owner_type = #ownerType)) ");
        strQuery.append("  AND st.status IN (0, 1, 2, 3, 4, 5)");
        strQuery.append("  AND st.create_datetime >= TRUNC (SYSDATE) - 120");
        Query query = emIM.createNativeQuery(strQuery.toString());
        query.setParameter("ownerId", ownerId);
        query.setParameter("ownerType", ownerType);
        List<Object[]> objects = query.getResultList();
        if (!DataUtil.isNullOrEmpty(objects)) {
            return true;
        }
        return false;
    }

    public boolean checkSuspendInspect(Long ownerType, Long ownerId, Long prodOfferId, Long stateId) throws Exception {
        StringBuilder strQuery = new StringBuilder("");
        strQuery.append("SELECT 1 ");
        strQuery.append(" FROM stock_trans st, stock_trans_detail std ");
        strQuery.append(" WHERE 1=1 ");
        strQuery.append("  AND st.stock_trans_id = std.stock_trans_id ");
        strQuery.append("  AND st.status IN (0, 2)");
        strQuery.append("  AND st.from_owner_id = #ownerId ");
        strQuery.append("  AND st.from_owner_type = #ownerType ");
        strQuery.append("  AND std.prod_offer_id = #prodOfferId");
        strQuery.append("  AND std.state_id = #stateId");
        strQuery.append("  AND st.create_datetime >= TRUNC (SYSDATE,'yy')");
        strQuery.append("  AND std.create_datetime >= TRUNC (SYSDATE,'yy')");
        Query query = emIM.createNativeQuery(strQuery.toString());
        query.setParameter("ownerId", ownerId);
        query.setParameter("ownerType", ownerType);
        query.setParameter("prodOfferId", prodOfferId);
        query.setParameter("stateId", stateId);
        List<Object[]> objects = query.getResultList();
        if (!DataUtil.isNullOrEmpty(objects)) {
            return true;
        }
        return false;
    }

}