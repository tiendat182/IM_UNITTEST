package com.viettel.bccs.inventory.repo;

import com.google.common.collect.Lists;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.dto.ImsiInfoDTO;
import com.viettel.bccs.inventory.dto.ImsiInfoInputSearch;
import com.viettel.bccs.inventory.dto.StockRequestDTO;
import com.viettel.bccs.inventory.dto.UpdateImsiInfoDTO;
import com.viettel.bccs.inventory.model.ImsiInfo;
import com.viettel.bccs.inventory.model.QImsiInfo;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DateUtil;
import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ImsiInfoRepoImpl implements ImsiInfoRepoCustom {

    public static final Logger logger = Logger.getLogger(ImsiInfoRepoCustom.class);

    @PersistenceContext(
            unitName = "BCCS_IM"
    )
    private EntityManager em;
    @PersistenceContext(
            unitName = "BCCS_SALE"
    )
    private EntityManager emSale;

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QImsiInfo imsiInfo = QImsiInfo.imsiInfo;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                ImsiInfo.COLUMNS column = ImsiInfo.COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case CREATEDATE:
                        expression = DbUtil.createDateExpression(imsiInfo.createDate, filter); 
                        break;
                    case DOCNO:
                        expression = DbUtil.createStringExpression(imsiInfo.docNo, filter); 
                        break;
                    case ENDDATE:
                        expression = DbUtil.createDateExpression(imsiInfo.endDate, filter); 
                        break;
                    case ID:
                        expression = DbUtil.createLongExpression(imsiInfo.id, filter); 
                        break;
                    case IMSI:
                        expression = DbUtil.createStringExpression(imsiInfo.imsi, filter); 
                        break;
                    case LASTUPDATETIME:
                        expression = DbUtil.createDateExpression(imsiInfo.lastUpdateTime, filter); 
                        break;
                    case LASTUPDATEUSER:
                        expression = DbUtil.createStringExpression(imsiInfo.lastUpdateUser, filter); 
                        break;
                    case STARTDATE:
                        expression = DbUtil.createDateExpression(imsiInfo.startDate, filter); 
                        break;
                    case STATUS:
                        expression = DbUtil.createStringExpression(imsiInfo.status, filter); 
                        break;
                    case USERCREATE:
                        expression = DbUtil.createStringExpression(imsiInfo.userCreate, filter); 
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

    public List<ImsiInfoDTO> search(ImsiInfoInputSearch imsiInfoInputSearch) throws Exception {
        StringBuilder sql = new StringBuilder("");
        List<Object> params = new ArrayList<Object>();
        sql.append("SELECT   MIN (imsi) from_imsi,\n" +
                "           MAX (imsi) to_imsi,\n" +
                "           status,\n" +
                "           start_date,\n" +
                "           end_date, doc_no, \n" +
                "           COUNT ( * ) AS quantity\n" +
                "    FROM   (SELECT   imsi,\n" +
                "                     status,\n" +
                "                     start_date,\n" +
                "                     end_date, doc_no, \n" +
                "                     (imsi\n" +
                "                      - ROW_NUMBER ()\n" +
                "                            OVER (ORDER BY \n" +
                "                                           s.status,\n" +
                "                                           s.start_date,\n" +
                "                                           s.end_date, s.doc_no, s.imsi))\n" +
                "                         dif\n" +
                "              FROM   imsi_info s\n" +
                "             WHERE       1 = 1");
        if (!DataUtil.isNullObject(imsiInfoInputSearch.getStatus())) {
            sql.append(" AND status = ? ");
            params.add(imsiInfoInputSearch.getStatus());
        }

        if (!DataUtil.isNullOrEmpty(imsiInfoInputSearch.getImsiFrom())) {
            sql.append(" AND TO_NUMBER (imsi) >= ? ");
            params.add(imsiInfoInputSearch.getImsiFrom());
        }

        if (!DataUtil.isNullOrEmpty(imsiInfoInputSearch.getImsiTo())) {
            sql.append(" AND TO_NUMBER (imsi) <= ? ");
            params.add(imsiInfoInputSearch.getImsiTo());
        }

        if (!DataUtil.isNullObject(imsiInfoInputSearch.getDocNo())) {
            sql.append(" AND doc_no = ? ");
            params.add(imsiInfoInputSearch.getDocNo());
        }

        if (!DataUtil.isNullObject(imsiInfoInputSearch.getFromDate())) {
            sql.append(" AND trunc(start_date) >=  to_date(?,'DD-MON-YYYY')");
            params.add(DateUtil.dateToStringWithPattern(imsiInfoInputSearch.getFromDate(), "dd-MMM-yyyy"));
        }

        if (!DataUtil.isNullObject(imsiInfoInputSearch.getToDate())) {
            sql.append(" AND trunc(end_date) <=  to_date(?,'DD-MON-YYYY')");
            params.add(DateUtil.dateToStringWithPattern(imsiInfoInputSearch.getToDate(), "dd-MMM-yyyy"));
        }
        sql.append(" ) ");
        sql.append("GROUP BY   status,\n" +
                "           start_date,\n" +
                "           end_date,\n" +
                "           dif, doc_no \n" +
                " ORDER BY   from_imsi");
        Query query = em.createNativeQuery(sql.toString());

        for (int i = 0; i < params.size(); i++) {
            query.setParameter(i+1, params.get(i));
        }

        List<Object[]> lsRequest = DataUtil.defaultIfNull(query.getResultList(), new ArrayList());
        List<ImsiInfoDTO> lsResult = new ArrayList<ImsiInfoDTO>();
        for (Object[] obj : lsRequest) {
            ImsiInfoDTO imsiInfoDTO = new ImsiInfoDTO();
            imsiInfoDTO.setFromImsi(DataUtil.safeToString(obj[0]));
            imsiInfoDTO.setToImsi(DataUtil.safeToString(obj[1]));
            imsiInfoDTO.setStatus(DataUtil.safeToString(obj[2]));
            imsiInfoDTO.setStartDate(obj[3] == null ? null : ((Date)obj[3]));
            imsiInfoDTO.setEndDate(obj[4] == null ? null : ((Date)obj[4]));
            imsiInfoDTO.setDocNo(DataUtil.safeToString(obj[5]));
            imsiInfoDTO.setQuantity(DataUtil.safeToLong(obj[6]));
            lsResult.add(imsiInfoDTO);
        }
        return lsResult;
    }

    @Override
    public Long countImsiRange(String fromImsi, String toImsi) throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT   COUNT (1)\n" +
                "  FROM   imsi_info\n" +
                " WHERE   TO_NUMBER (imsi) >= #from_imsi AND TO_NUMBER (imsi) <= #to_imsi");
        Query query = em.createNativeQuery(sql.toString());
        query.setParameter("from_imsi", fromImsi);
        query.setParameter("to_imsi", toImsi);

        return DataUtil.safeToLong(query.getSingleResult());
    }

    public Long countImsiRangeToCheckDelete(String fromImsi, String toImsi) throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT   COUNT (1)\n" +
                "  FROM   imsi_info\n" +
                " WHERE  status = '1' AND TO_NUMBER (imsi) >= #from_imsi AND TO_NUMBER (imsi) <= #to_imsi");
        Query query = em.createNativeQuery(sql.toString());
        query.setParameter("from_imsi", fromImsi);
        query.setParameter("to_imsi", toImsi);
        return DataUtil.safeToLong(query.getSingleResult());
    }

    public void deleteImsi(String fromImsi, String toImsi) throws Exception {
        StringBuilder sql = new StringBuilder("");
        sql.append("DELETE FROM   imsi_info \n" +
                "      WHERE   TO_NUMBER (imsi) >= #from_imsi \n" +
                "         AND TO_NUMBER (imsi) <= #to_imsi\n");
        Query query = em.createNativeQuery(sql.toString());
        query.setParameter("from_imsi", Long.valueOf(fromImsi));
        query.setParameter("to_imsi", Long.valueOf(toImsi));
        query.executeUpdate();
    }

    public void updateBatch(ImsiInfoDTO imsiInfoDTO) throws Exception {
        StringBuilder sql = new StringBuilder("");
        sql.append("UPDATE IMSI_INFO\n " +
                " SET START_DATE = #start_date, END_DATE = #end_date, DOC_NO = #doc_no, LAST_UPDATE_TIME = #lastUpdateTime, LAST_UPDATE_USER = #lastUpdateUser" +
                " WHERE TO_NUMBER (imsi) >= #from_imsi AND TO_NUMBER (imsi) <= #to_imsi");
        Query query = em.createNativeQuery(sql.toString());
        query.setParameter("start_date", imsiInfoDTO.getStartDate());
        query.setParameter("end_date", imsiInfoDTO.getEndDate());
        query.setParameter("doc_no", imsiInfoDTO.getDocNo());
        query.setParameter("lastUpdateTime", DbUtil.getSysDate(em));
        query.setParameter("lastUpdateUser", imsiInfoDTO.getLastUpdateUser());
        query.setParameter("from_imsi", Long.valueOf(imsiInfoDTO.getFromImsi()));
        query.setParameter("to_imsi", Long.valueOf(imsiInfoDTO.getToImsi()));
        query.executeUpdate();
    }

    @Override
    public List<UpdateImsiInfoDTO> getTransactionToUpdateImsi() throws Exception {

        String sql = "select from_serial, to_serial, stock_model_id " +
                "from sale_trans_serial a, product_offering b " +
                "where a.stock_model_id= b.product_offering_id " +
                "and b.product_offer_type_id in (4,5) " +
                "and a.sale_trans_date >= trunc (sysdate) ";

        Query query = emSale.createNativeQuery(sql);
        List<Object[]> resultObj = query.getResultList();

        if(DataUtil.isNullOrEmpty(resultObj))
            return new ArrayList<>();
        List<UpdateImsiInfoDTO> result = new ArrayList<>();
        for(Object [] obj : resultObj){
            UpdateImsiInfoDTO updateImsiInfoDTO = new UpdateImsiInfoDTO();
            updateImsiInfoDTO.setFromSerial(DataUtil.safeToString(obj[0]));
            updateImsiInfoDTO.setToSerial(DataUtil.safeToString(obj[1]));
            updateImsiInfoDTO.setStockModelId(DataUtil.safeToLong(obj[2]));
            result.add(updateImsiInfoDTO);
        }

        return result;
    }

    @Override
    public List<UpdateImsiInfoDTO> getImsiHasFillPartner() throws Exception {
        String sql = "select b.from_serial, b.to_serial, b.prod_offer_id " +
                "from bccs_im.stock_trans a, bccs_im.stock_trans_serial b, bccs_im.product_offering c " +
                "where a.stock_trans_id=b.stock_trans_id " +
                "and b.prod_offer_id= c.prod_offer_id " +
                "and from_owner_type=4 " +
                "and to_owner_type=1 " +
                "and to_owner_id=7282 " +
                "and c.product_offer_type_id in (4,5)";

        Query query = em.createNativeQuery(sql);
        List<Object[]> resultObj = query.getResultList();

        if(DataUtil.isNullOrEmpty(resultObj))
            return new ArrayList<>();
        List<UpdateImsiInfoDTO> result = new ArrayList<>();
        for(Object [] obj : resultObj){
            UpdateImsiInfoDTO updateImsiInfoDTO = new UpdateImsiInfoDTO();
            updateImsiInfoDTO.setFromSerial(DataUtil.safeToString(obj[0]));
            updateImsiInfoDTO.setToSerial(DataUtil.safeToString(obj[1]));
            updateImsiInfoDTO.setStockModelId(DataUtil.safeToLong(obj[2]));
            result.add(updateImsiInfoDTO);
        }

        return result;
    }

    @Override
    public void updateImsiInfoHasConnectSim(UpdateImsiInfoDTO updateImsiInfoDTO) throws Exception {
        String sql = "UPDATE   imsi_info a " +
                "   SET   status = 3 " +
                " WHERE   EXISTS " +
                "             (SELECT   1 " +
                "                FROM   bccs_im.stock_sim " +
                "               WHERE       prod_offer_id = #prod_offer_id " +
                "                       AND TO_NUMBER (serial) >= #from_serial " +
                "                       AND TO_NUMBER (serial) <= #to_serial " +
                "                       AND imsi = a.imsi)";

        Query query = em.createNativeQuery(sql);
        query.setParameter("prod_offer_id", updateImsiInfoDTO.getStockModelId());
        query.setParameter("from_serial", updateImsiInfoDTO.getFromSerial());
        query.setParameter("to_serial", updateImsiInfoDTO.getToSerial());
        query.executeUpdate();
    }

    @Override
    public void updateImsiInfoToHasUsedSim(UpdateImsiInfoDTO updateImsiInfoDTO) throws Exception {
        String sql = "UPDATE   imsi_info a " +
                "   SET   status = 4 " +
                " WHERE   EXISTS " +
                "             (SELECT   1 " +
                "                FROM   bccs_im.stock_sim " +
                "               WHERE       prod_offer_id = #stock_model_id " +
                "                       AND TO_NUMBER (serial) >= #from_serial " +
                "                       AND TO_NUMBER (serial) <= #to_serial " +
                "                       AND imsi = a.imsi) ";

        Query query = em.createNativeQuery(sql);
        query.setParameter("stock_model_id", updateImsiInfoDTO.getStockModelId());
        query.setParameter("from_serial", updateImsiInfoDTO.getFromSerial());
        query.setParameter("to_serial", updateImsiInfoDTO.getToSerial());
        query.executeUpdate();
    }
}