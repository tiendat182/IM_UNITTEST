package com.viettel.bccs.inventory.repo;

import com.fss.util.NumberUtil;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.dto.ImsiMadeDTO;
import com.viettel.bccs.inventory.dto.OutputImsiProduceDTO;
import com.viettel.bccs.inventory.dto.PartnerDTO;
import com.viettel.bccs.inventory.model.ImsiMade;
import com.viettel.bccs.inventory.model.Partner;
import com.viettel.bccs.inventory.model.ProductOffering;
import com.viettel.bccs.inventory.model.QImsiMade;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DateUtil;
import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.common.util.mapper.BaseMapper;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ImsiMadeRepoImpl implements ImsiMadeRepoCustom {

    public static final Logger logger = Logger.getLogger(ImsiMadeRepoCustom.class);
    @PersistenceContext(unitName = "BCCS_IM")
    private EntityManager em;
    private final BaseMapper<ImsiMade,ImsiMadeDTO> mapper = new BaseMapper<>(ImsiMade.class,ImsiMadeDTO.class);
    @Autowired
    private ProductOfferingRepo productOfferingRepo;
    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QImsiMade imsiMade = QImsiMade.imsiMade;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                ImsiMade.COLUMNS column = ImsiMade.COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case CONTENT:
                        expression = DbUtil.createStringExpression(imsiMade.content, filter); 
                        break;
                    case CREATEDATE:
                        expression = DbUtil.createDateExpression(imsiMade.createDate, filter); 
                        break;
                    case FROMIMSI:
                        expression = DbUtil.createStringExpression(imsiMade.fromImsi, filter); 
                        break;
                    case ID:
                        expression = DbUtil.createLongExpression(imsiMade.id, filter); 
                        break;
                    case OPKEY:
                        expression = DbUtil.createStringExpression(imsiMade.opKey, filter); 
                        break;
                    case PO:
                        expression = DbUtil.createStringExpression(imsiMade.po, filter); 
                        break;
                    case PRODOFFERID:
                        expression = DbUtil.createLongExpression(imsiMade.prodOfferId, filter);
                        break;
                    case QUANTITY:
                        expression = DbUtil.createLongExpression(imsiMade.quantity, filter);
                        break;
                    case SERIALNO:
                        expression = DbUtil.createStringExpression(imsiMade.serialNo, filter); 
                        break;
                    case SIMTYPE:
                        expression = DbUtil.createStringExpression(imsiMade.simType, filter); 
                        break;
                    case TOIMSI:
                        expression = DbUtil.createStringExpression(imsiMade.toImsi, filter); 
                        break;
                    case USERCREATE:
                        expression = DbUtil.createStringExpression(imsiMade.userCreate, filter); 
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
    public List<OutputImsiProduceDTO> getListImsiRange(String startImsi, String endImsi, String docCode, Date fromDate, Date toDate, Long status) throws Exception {

        List<Object> params = new ArrayList<>();

        StringBuilder sql = new StringBuilder("SELECT   MIN (imsi) from_imsi, " +
                "           MAX (imsi) to_imsi, " +
                "           start_date, " +
                "           end_date, " +
                "           COUNT ( * ) AS quantity, " +
                "           status " +
                "    FROM   (SELECT   imsi, " +
                "                     start_date, " +
                "                     end_date, status, " +
                "                     (imsi " +
                "                      - ROW_NUMBER () " +
                "                            OVER (ORDER BY s.imsi, " +
                "                                           s.start_date, " +
                "                                           s.end_date)) " +
                "                         dif " +
                "              FROM   imsi_info s " +
                "             WHERE       1 = 1 ");

        if(!DataUtil.isNullOrEmpty(startImsi)) {
            sql.append(" AND TO_NUMBER (imsi) >= ? ");
            params.add(startImsi);
        }

        if(!DataUtil.isNullOrEmpty(endImsi)) {
            sql.append(" AND TO_NUMBER (imsi) <= ? ");
            params.add(endImsi);
        }
        if(!DataUtil.isNullOrEmpty(docCode)) {
            sql.append(" AND upper(doc_no) = ? ");
            params.add(docCode.toUpperCase());
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        if(fromDate != null){
            sql.append(" AND TRUNC(start_date) >= TO_DATE(?, 'dd-MM-yyyy') ");
            params.add(simpleDateFormat.format(fromDate));
        }
        if(toDate != null){
            sql.append(" AND TRUNC(end_date) <= TO_DATE(?, 'dd-MM-yyyy') ");
            params.add(simpleDateFormat.format(toDate));
        }
        if(status != null){
            sql.append(" AND status = ? ");
            params.add(status);
        }

        sql.append(")");

        sql.append(
                "GROUP BY   " +
                "           start_date, " +
                "           end_date, " +
                "           dif, status " +
                "ORDER BY   from_imsi");

        Query query = em.createNativeQuery(sql.toString());
        for(int i = 0 ; i < params.size() ; i++)
            query.setParameter(i + 1, params.get(i));
        List<Object[]> objs = DataUtil.defaultIfNull(query.getResultList(), new ArrayList());
        List<OutputImsiProduceDTO> results = new ArrayList<>();
        for(Object[] obj : objs){
            OutputImsiProduceDTO outputImsiProduceDTO = new OutputImsiProduceDTO();
            outputImsiProduceDTO.setStartImsi(DataUtil.safeToString(obj[0]));
            outputImsiProduceDTO.setEndImsi(DataUtil.safeToString(obj[1]));
            outputImsiProduceDTO.setStartDate(obj[2] == null ? null : (Date) obj[2]);
            outputImsiProduceDTO.setEndDate(obj[3] == null ? null : (Date) obj[3]);
            outputImsiProduceDTO.setQuatity(DataUtil.safeToLong(obj[4]));
            outputImsiProduceDTO.setStatus((DataUtil.safeToLong(obj[5])));
            results.add(outputImsiProduceDTO);
        }


        return results;
    }

    @Override
    public Long checkImsiRange(String fromImsi, String toImsi) throws Exception {

        StringBuilder sql = new StringBuilder("");
        sql.append("SELECT   count (1) ");
        sql.append("  FROM   imsi_info ");
        sql.append(" WHERE       TO_NUMBER (imsi) >= #from_imsi ");
        sql.append("         AND TO_NUMBER (imsi) <= #to_imsi ");
        sql.append("         AND status = 1 ");
        sql.append("         AND TRUNC (start_date) <= TRUNC (SYSDATE) ");
        sql.append("         AND (end_date IS NULL OR TRUNC (end_date) >= TRUNC (SYSDATE))");

        Query query = em.createNativeQuery(sql.toString());
        query.setParameter("from_imsi", fromImsi);
        query.setParameter("to_imsi", toImsi);

        return DataUtil.safeToLong(query.getSingleResult());
    }

    @Override
    public List<Partner> getListPartnerA4keyNotNull() throws Exception {
        String sql = "SELECT * FROM PARTNER WHERE a4key IS NOT NULL ORDER BY PARTNER_NAME";
        Query query = em.createNativeQuery(sql, Partner.class);
        return query.getResultList();

    }

    @Override
    public Long getQuantityByImsi(String fromImsi, String toImsi) throws Exception {

        StringBuilder sql = new StringBuilder("");
        sql.append("SELECT   count (1) ");
        sql.append("  FROM   imsi_info ");
        sql.append(" WHERE       TO_NUMBER (imsi) >= #from_imsi ");
        sql.append("         AND TO_NUMBER (imsi) <= #to_imsi ");
        sql.append("         AND status = 1 ");
        sql.append("         AND start_date <= TRUNC (SYSDATE) ");
        sql.append("         AND (end_date >= TRUNC (SYSDATE) OR end_date IS NULL)");

        Query query = em.createNativeQuery(sql.toString());
        query.setParameter("from_imsi", fromImsi);
        query.setParameter("to_imsi", toImsi);
        return DataUtil.safeToLong(query.getSingleResult());
    }

    @Override
    public List<ImsiMadeDTO> getImsiRangeByDate(Date fromDate, Date toDate) throws Exception {

        StringBuilder sql = new StringBuilder("");
        sql.append("SELECT   * ");
        sql.append("  FROM   imsi_made ");
        sql.append(" WHERE      1=1 ");
        sql.append("         AND TRUNC(create_date) >= TO_DATE(#start_date, 'dd-MM-yyyy') ");
        sql.append("         AND TRUNC(create_date) <= TO_DATE(#end_date, 'dd-MM-yyyy')");

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Query query = em.createNativeQuery(sql.toString(), ImsiMade.class);
        query.setParameter("start_date", simpleDateFormat.format(fromDate));
        query.setParameter("end_date", simpleDateFormat.format(toDate));

        List<ImsiMade> result = query.getResultList();
        List<ImsiMadeDTO> resultDTO = new ArrayList<>();

        for(ImsiMade imsiMade : result){
            ImsiMadeDTO imsiMadeDTO = mapper.toDtoBean(imsiMade);
            ProductOffering one = productOfferingRepo.findOne(imsiMade.getProdOfferId());
            if(one != null)
                imsiMadeDTO.setProdOfferName(one.getName());
            resultDTO.add(imsiMadeDTO);
        }

        return resultDTO;
    }

    @Override
    public boolean validateImsiRange(String fromImsi, String toImsi) throws Exception {
        StringBuilder sql = new StringBuilder("");
        sql.append("SELECT   count (1) ");
        sql.append("  FROM   imsi_info ");
        sql.append(" WHERE       TO_NUMBER (imsi) >= #from_imsi ");
        sql.append("             AND TO_NUMBER (imsi) <= #to_imsi ");
        sql.append("             AND status = 2 ");

        Query query = em.createNativeQuery(sql.toString());
        query.setParameter("from_imsi", fromImsi);
        query.setParameter("to_imsi", toImsi);
        return DataUtil.safeEqual(NumberUtils.toLong(toImsi) - NumberUtils.toLong(fromImsi) + 1, DataUtil.safeToLong(query.getSingleResult()));
    }

    /*@Override
    public void updateImsiInfoTo2Status(String fromImsi, String toImsi) throws Exception {
        String sql = "UPDATE IMSI_INFO SET STATUS=2 WHERE IMSI >= #fromImsi AND IMSI <= #toImsi";
        Query query = em.createNativeQuery(sql);
        query.setParameter("fromImsi", fromImsi);
        query.setParameter("toImsi", toImsi);

        query.executeUpdate();
    }*/

}