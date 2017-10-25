package com.viettel.bccs.inventory.repo;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.ReportChangeHandsetDTO;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DateUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.model.ReportChangeHandset.COLUMNS;
import com.viettel.bccs.inventory.model.QReportChangeHandset;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.viettel.fw.common.util.DbUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public class ReportChangeHandsetRepoImpl implements ReportChangeHandsetRepoCustom {

    public static final Logger logger = Logger.getLogger(ReportChangeHandsetRepoCustom.class);
    @Autowired
    private OptionSetValueRepo optionSetValueRepo;

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_SALE)
    private EntityManager emSale;

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QReportChangeHandset reportChangeHandset = QReportChangeHandset.reportChangeHandset;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case ADJUSTAMOUNT:
                        expression = DbUtil.createLongExpression(reportChangeHandset.adjustAmount, filter); 
                        break;
                    case CHANGETYPE:
                        expression = DbUtil.createLongExpression(reportChangeHandset.changeType, filter); 
                        break;
                    case CREATEDATE:
                        expression = DbUtil.createDateExpression(reportChangeHandset.createDate, filter); 
                        break;
                    case CUSTNAME:
                        expression = DbUtil.createStringExpression(reportChangeHandset.custName, filter); 
                        break;
                    case CUSTTEL:
                        expression = DbUtil.createStringExpression(reportChangeHandset.custTel, filter); 
                        break;
                    case DAMAGEGOODSTATUS:
                        expression = DbUtil.createStringExpression(reportChangeHandset.damageGoodStatus, filter); 
                        break;
                    case DEVSTAFFID:
                        expression = DbUtil.createLongExpression(reportChangeHandset.devStaffId, filter); 
                        break;
                    case PRODOFFERIDNEW:
                        expression = DbUtil.createLongExpression(reportChangeHandset.prodOfferIdNew, filter); 
                        break;
                    case PRODOFFERIDOLD:
                        expression = DbUtil.createLongExpression(reportChangeHandset.prodOfferIdOld, filter); 
                        break;
                    case REPORTCHANGEHANDSETID:
                        expression = DbUtil.createLongExpression(reportChangeHandset.reportChangeHandsetId, filter); 
                        break;
                    case SERIALNEW:
                        expression = DbUtil.createStringExpression(reportChangeHandset.serialNew, filter); 
                        break;
                    case SERIALOLD:
                        expression = DbUtil.createStringExpression(reportChangeHandset.serialOld, filter); 
                        break;
                    case SHOPID:
                        expression = DbUtil.createLongExpression(reportChangeHandset.shopId, filter); 
                        break;
                    case STAFFID:
                        expression = DbUtil.createLongExpression(reportChangeHandset.staffId, filter); 
                        break;
                    case STOCKTRANSID:
                        expression = DbUtil.createLongExpression(reportChangeHandset.stockTransId, filter); 
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
    public List<ReportChangeHandsetDTO> getLsReportChangeHandsetTerminal(Long prodOfferId, String serial, String shopPath) throws Exception {

        int numberDate = Const.AMOUNT_DAY_TO_CHANGE_HANDSET_DEFAULT;//so ngay duoc phep doi thiet bi 3

        String strNumberDate = optionSetValueRepo.getValueByOptionSetIdAndCode(Const.OPTION_SET.AMOUNT_DAY_TO_CHANGE_HANDSET, Const.OPTION_SET.AMOUNT_DAY_TO_CHANGE_HANDSET);
        if (!DataUtil.isNullOrEmpty(strNumberDate)) {
            numberDate = DataUtil.safeToInt(strNumberDate);
        }

        List<ReportChangeHandsetDTO> lsReport = Lists.newArrayList();

        List<String> arr = Arrays.asList(shopPath.split("_")).stream().filter(x -> !DataUtil.isNullOrEmpty(x.trim())).collect(Collectors.toList());
        if (arr.size() < 3) {
            return lsReport;
        }
        String shopPathTmp = "_" + Joiner.on("_").join(arr);

        StringBuilder strSql = new StringBuilder(" ");
        strSql.append("	SELECT   st.sale_trans_id AS saleTransId, ");
        strSql.append("			 st.sale_trans_date AS saleTransDate, ");
        strSql.append("			 (SELECT   sh.shop_code || '-' || sh.name ");
        strSql.append("				FROM   bccs_sale.shop sh ");
        strSql.append("				WHERE   sh.shop_id = st.shop_id) shop_name, ");
        strSql.append("			 DECODE (st.staff_id, ");
        strSql.append("					 NULL, NULL, ");
        strSql.append("					 (SELECT   sf.staff_code || '-' || sf.name ");
        strSql.append("						FROM   bccs_sale.staff sf ");
        strSql.append("					   WHERE   sf.staff_id = st.staff_id)) ");
        strSql.append("				 AS staffname, ");
        strSql.append("			 (SELECT   code || '-' || name ");
        strSql.append("				FROM   product_offering po ");
        strSql.append("			   WHERE   product_offering_id = sts.stock_model_id) ");
        strSql.append("				 offer_name, ");
        strSql.append("			 std.price AS pricesaled, ");
        strSql.append("			 sts.from_serial AS serialsaled, ");
        strSql.append("			 st.check_stock AS checkstock, ");
        strSql.append("			 st.pay_method AS paymethod, ");
        strSql.append("			 st.cust_name AS custname, ");
        strSql.append("			 st.contract_no AS contractno, ");
        strSql.append("			 st.tel_number AS telnumber, ");
        strSql.append("			 st.company AS company, ");
        strSql.append("			 st.address AS address, ");
        strSql.append("			 st.tin AS tin, ");
        strSql.append("			 st.note AS note, ");
        strSql.append("			 st.reason_id AS reasonid, ");
        strSql.append("			 st.telecom_service_id AS telecomserviceid, ");
        strSql.append("			 st.shop_id ");
        strSql.append("	  FROM   bccs_sale.sale_trans st, ");
        strSql.append("			 bccs_sale.sale_trans_detail std, ");
        strSql.append("			 bccs_sale.sale_trans_serial sts ");
        strSql.append("	 WHERE       st.sale_trans_date >= TRUNC (SYSDATE - :numberDate) ");
        strSql.append("			 AND std.sale_trans_date >= TRUNC (SYSDATE - :numberDate) ");
        strSql.append("			 AND sts.sale_trans_date >= TRUNC (SYSDATE - :numberDate) ");
        strSql.append("			 AND st.sale_trans_id = sts.sale_trans_id ");
        strSql.append("			 AND st.sale_trans_id = std.sale_trans_id ");
        strSql.append("			 AND std.sale_trans_detail_id = sts.sale_trans_detail_id ");
        strSql.append("			 AND st.status IN (2, 3) ");
        strSql.append("			 AND std.stock_model_id = #prodOfferId ");
        strSql.append("			 AND sts.from_serial = #serial ");
        strSql.append("			 AND sts.to_serial = #serial ");
        strSql.append("			 AND st.shop_path LIKE #shopPathTmp ");

        Query query = emSale.createNativeQuery(strSql.toString());
        query.setParameter("prodOfferId", prodOfferId);
        query.setParameter("serial", serial);
        query.setParameter("numberDate", numberDate);
        query.setParameter("shopPathTmp", "%" + shopPathTmp + "%");
        List<Object[]> objects = query.getResultList();

        if (!DataUtil.isNullOrEmpty(objects)) {
            ReportChangeHandsetDTO report ;
            int index;
            for (Object[] obj : objects) {
                index = 0;
                report = new ReportChangeHandsetDTO();
                report.setSaleTransId(DataUtil.safeToLong(obj[index++]));
                Date objDate = (Date)obj[index++];
                report.setSaleTransDate(objDate);
                report.setShopName(DataUtil.safeToString(obj[index++]));
                report.setStaffName(DataUtil.safeToString(obj[index++]));
                report.setProdOfferName(DataUtil.safeToString(obj[index++]));
                report.setPriceSaled(DataUtil.safeToLong(obj[index++]));
                report.setSerialSaled(DataUtil.safeToString(obj[index++]));
                report.setCheckStock(DataUtil.safeToString(obj[index++]));
                report.setPayMethod(DataUtil.safeToString(obj[index++]));
                report.setCustName(DataUtil.safeToString(obj[index++]));
                report.setContractNo(DataUtil.safeToString(obj[index++]));
                report.setTelNumber(DataUtil.safeToString(obj[index++]));
                report.setCompany(DataUtil.safeToString(obj[index++]));
                report.setAddress(DataUtil.safeToString(obj[index++]));
                report.setTin(DataUtil.safeToString(obj[index++]));
                report.setNote(DataUtil.safeToString(obj[index++]));
                report.setReasonId(DataUtil.safeToLong(obj[index++]));
                report.setTelecomServiceId(DataUtil.safeToLong(obj[index++]));
                report.setShopId(DataUtil.safeToLong(obj[index]));
                lsReport.add(report);
            }
        }
        return lsReport;
    }

}