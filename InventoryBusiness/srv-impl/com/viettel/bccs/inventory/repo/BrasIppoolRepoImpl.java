package com.viettel.bccs.inventory.repo;

import com.google.common.collect.Lists;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.BrasIppoolDTO;
import com.viettel.bccs.inventory.dto.DomainDTO;
import com.viettel.bccs.inventory.model.BrasIppool;
import com.viettel.bccs.inventory.model.BrasIppool.COLUMNS;
import com.viettel.bccs.inventory.model.QBrasIppool;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.common.util.mapper.BaseMapper;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

public class BrasIppoolRepoImpl implements BrasIppoolRepoCustom {

    public static final Logger logger = Logger.getLogger(BrasIppoolRepoCustom.class);
    private final BaseMapper<BrasIppool, BrasIppoolDTO> mapper = new BaseMapper(BrasIppool.class, BrasIppoolDTO.class);
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_SALE)
    private EntityManager emSale;

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QBrasIppool brasIppool = QBrasIppool.brasIppool;
        BooleanExpression result = BooleanTemplate.create("1 = 1 ");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case BRASIP:
                        expression = DbUtil.createLongExpression(brasIppool.brasIp, filter);
                        break;
                    case CENTER:
                        expression = DbUtil.createStringExpression(brasIppool.center, filter);
                        break;
                    case DOMAIN:
                        expression = DbUtil.createLongExpression(brasIppool.domain, filter);
                        break;
                    case IPLABEL:
                        expression = DbUtil.createStringExpression(brasIppool.ipLabel, filter);
                        break;
                    case IPMASK:
                        expression = DbUtil.createStringExpression(brasIppool.ipMask, filter);
                        break;
                    case IPPOOL:
                        expression = DbUtil.createStringExpression(brasIppool.ipPool, filter);
                        break;
                    case IPTYPE:
                        expression = DbUtil.createStringExpression(brasIppool.ipType, filter);
                        break;
                    case POOLID:
                        expression = DbUtil.createLongExpression(brasIppool.poolId, filter);
                        break;
                    case POOLNAME:
                        expression = DbUtil.createStringExpression(brasIppool.poolName, filter);
                        break;
                    case POOLUNIQ:
                        expression = DbUtil.createStringExpression(brasIppool.poolUniq, filter);
                        break;
                    case PROVINCE:
                        expression = DbUtil.createStringExpression(brasIppool.province, filter);
                        break;
                    case STATUS:
                        expression = DbUtil.createStringExpression(brasIppool.status, filter);
                        break;
                    case CREATEDATETIME:
                        expression = DbUtil.createDateExpression(brasIppool.createDatetime, filter);
                        break;
                    case CREATEUSER:
                        expression = DbUtil.createStringExpression(brasIppool.createUser, filter);
                        break;
                    case UPDATEDATETIME:
                        expression = DbUtil.createDateExpression(brasIppool.updateDatetime, filter);
                        break;
                    case UPDATEUSER:
                        expression = DbUtil.createStringExpression(brasIppool.updateUser, filter);
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
    public List<DomainDTO> getAllDomain() throws Exception {
        List<DomainDTO> lst = Lists.newArrayList();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append("SELECT id, domain, description FROM DOMAIN WHERE STATUS = 1");
        //execute query
        Query query = em.createNativeQuery(strQuery.toString());
        List<Object[]> lstObj = query.getResultList();
        if (lstObj != null && !lstObj.isEmpty()) {
            for (Object obj[] : lstObj) {
                DomainDTO domainDTO = new DomainDTO();
                domainDTO.setId(Long.valueOf(obj[0].toString()));
                domainDTO.setDomain(obj[1].toString());
                domainDTO.setDescription(obj[2].toString());
                lst.add(domainDTO);
            }
            return lst;
        }
        return null;
    }

    @Override
    public List<BrasIppoolDTO> search(BrasIppoolDTO brasIppoolDTO) throws Exception {
        List<BrasIppoolDTO> result = Lists.newArrayList();
        List lstParam = new ArrayList();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append("SELECT * From bras_ippool where 1=1 ");
        // chu y truoc lay theo bras_ip, domain bay gio lay theo bras_id, domain_id
        if (!brasIppoolDTO.isExportExcel()) {
            strQuery.append(" and rownum < 1000 ");
        } else {
            strQuery.append(" and rownum < 50000 ");
        }
        if (!DataUtil.isNullOrZero(brasIppoolDTO.getBrasIp())) {
            strQuery.append(" and bras_id =? ");
            lstParam.add(brasIppoolDTO.getBrasIp());
        }
        if (!DataUtil.isNullOrEmpty(brasIppoolDTO.getPoolName())) {
            strQuery.append(" and upper(pool_name) like ? ");
            lstParam.add("%" + brasIppoolDTO.getPoolName().toUpperCase().trim() + "%");
        }
        if (!DataUtil.isNullOrEmpty(brasIppoolDTO.getPoolUniq())) {
            strQuery.append(" and upper(pool_uniq) like ? ");
            lstParam.add("%" + brasIppoolDTO.getPoolUniq().toUpperCase().trim() + "%");
        }
        if (!DataUtil.isNullOrZero(brasIppoolDTO.getDomain())) {
            strQuery.append(" and domain_id =? ");
            lstParam.add(brasIppoolDTO.getDomain());
        }
        if (!DataUtil.isNullOrEmpty(brasIppoolDTO.getIpPool())) {
            if (brasIppoolDTO.getIpPool().split("[.]").length == 4) {
                strQuery.append(" and upper(ip_pool) = ? ");
                lstParam.add(brasIppoolDTO.getIpPool().toUpperCase().trim());
            } else {
                strQuery.append(" and upper(ip_pool) like ? ");
                lstParam.add("%" + brasIppoolDTO.getIpPool().toUpperCase().trim() + "%");
            }
        }
        if (!DataUtil.isNullOrEmpty(brasIppoolDTO.getIpMask())) {
            strQuery.append(" and upper(ip_mask) like ? ");
            lstParam.add("%" + brasIppoolDTO.getIpMask().toUpperCase().trim() + "%");
        }
        if (!DataUtil.isNullOrEmpty(brasIppoolDTO.getCenter())) {
            strQuery.append(" and upper(center) like ? ");
            lstParam.add("%" + brasIppoolDTO.getCenter().toUpperCase().trim() + "%");
        }
        if (!DataUtil.isNullOrEmpty(brasIppoolDTO.getProvince())) {
            String[] arrProvince = brasIppoolDTO.getProvince().split(",");
            strQuery.append(" and (");
            for (String areaCode : arrProvince) {
                strQuery.append(" upper(province) like ? OR");
                lstParam.add("%" + areaCode.toUpperCase().trim() + "%");
            }
            strQuery.delete(strQuery.length() - 3, strQuery.length());
            strQuery.append(")");

        }
        if (!DataUtil.isNullOrEmpty(brasIppoolDTO.getIpType())) {
            strQuery.append(" and upper(ip_type) like ? ");
            lstParam.add("%" + brasIppoolDTO.getIpType().toUpperCase().trim() + "%");
        }
        if (!DataUtil.isNullOrEmpty(brasIppoolDTO.getIpLabel())) {
            strQuery.append(" and upper(ip_label) like ? ");
            lstParam.add("%" + brasIppoolDTO.getIpLabel().toUpperCase().trim() + "%");
        }
        if (!DataUtil.isNullOrEmpty(brasIppoolDTO.getStatus())) {
            strQuery.append(" and status =? ");
            lstParam.add(brasIppoolDTO.getStatus());
        }
        if (!DataUtil.isNullObject(brasIppoolDTO.getUpdateDatetime())) {
            strQuery.append(" and trunc(update_datetime) = trunc(?) ");
            lstParam.add(brasIppoolDTO.getUpdateDatetime());
        }
        strQuery.append(" order by update_datetime desc ");
        Query query = em.createNativeQuery(strQuery.toString(), BrasIppool.class);
        if (query != null) {
            for (int i = 0; i < lstParam.size(); i++) {
                query.setParameter(i + 1, lstParam.get(i));
            }
            List<BrasIppool> list = query.getResultList();
            if (!DataUtil.isNullOrEmpty(list)) {
                result = mapper.toDtoBean(list);
            }
        }

        return result;
    }

    @Override
    public List<String> getListStaticIpProvince(String domain, String province, Long ipType, Long status, Long specialType) throws Exception {
        List<String> lstIp = Lists.newArrayList();
        String[] params = province.trim().split(",");
        for (int i = 0; i < params.length; i++) {
            String areaTemp = params[i];
            StringBuffer strBuff = new StringBuffer();
            strBuff.append("SELECT a.ip_pool FROM bras_ippool a, domain c WHERE AND a.domain_id = c.id AND UPPER(c.domain) = UPPER(#domain) AND EXISTS (select 1 from ");
            strBuff.append("ippool_province b where a.pool_id=b.pool_id AND UPPER(b.province)=UPPER(#province) )  ");
            strBuff.append(" AND a.status = #status");
            if (DataUtil.isNullObject(specialType)) {
                strBuff.append(" AND a.ip_type IS NULL ");
            } else {
                strBuff.append(" AND (a.ip_type IS NULL OR a.ip_type = #ip_type) ");
            }
            if (DataUtil.safeEqual(ipType, 1L)) {
                strBuff.append("AND ip_pool like '%/30%' ");
            } else {
                strBuff.append("AND ip_pool not like '%/30%' ");
            }
            Query query = em.createNativeQuery(strBuff.toString());
            query.setParameter("domain", domain);
            query.setParameter("province", areaTemp);
            query.setParameter("status", status);
            if (!DataUtil.isNullObject(specialType)) {
                query.setParameter("ip_type", specialType);
            }
            List queryResult = query.getResultList();
            for (Object tmpStr : queryResult) {
                boolean exist = false;
                for (String str : lstIp) {
                    if (str.equals(tmpStr)) {
                        exist = true;
                        break;
                    }
                }
                if (!exist) {
                    lstIp.add(DataUtil.safeToString(tmpStr));
                }
            }
        }
        return lstIp;
    }

    @Override
    public void insertIppoolProvince(Long poolId, String province) throws Exception {
        StringBuilder sqlInsert = new StringBuilder("");
        sqlInsert.append("insert into ippool_province (pool_id,province) values (#poolId, #province)");
        Query query = em.createNativeQuery(sqlInsert.toString());
        query.setParameter("poolId", poolId);
        query.setParameter("province", province);
        int i = query.executeUpdate();
        if (i < 1) {
            throw new Exception("bras.ippool.insert.ippool.province.error");
        }
    }

    @Override
    public int deleteIpPool(Long poolId) throws Exception {
        String sql = "delete IPPOOL_PROVINCE where pool_Id= #poolId ";
        Query query = em.createNativeQuery(sql);
        query.setParameter("poolId", poolId);
        return query.executeUpdate();
    }

    public BrasIppoolDTO checkIpHaveUsedCM(String ip) throws Exception {
        StringBuilder sqlQuery = new StringBuilder(" ");
        sqlQuery.append(" SELECT   b.isdn, ");
        sqlQuery.append("          a.ip, ");
        sqlQuery.append("          '1' as status ");
        sqlQuery.append("   FROM   bccs_sale.sub_ip a, bccs_sale.subscriber b ");
        sqlQuery.append("  WHERE       a.status IN (1) ");
        sqlQuery.append("          AND EXISTS (SELECT   * ");
        sqlQuery.append("                        FROM   bccs_sale.subscriber ");
        sqlQuery.append("                       WHERE   status IN (7, 2) AND a.sub_id = sub_id) ");
        sqlQuery.append("          AND a.ip_block IS NOT NULL ");
        sqlQuery.append("          AND a.sub_id = b.sub_id ");
        sqlQuery.append("          AND a.ip = #ip ");
        sqlQuery.append(" UNION ");
        sqlQuery.append(" SELECT   b.isdn, ");
        sqlQuery.append("          a.ip, ");
        sqlQuery.append("          '2' as status ");
        sqlQuery.append("   FROM   bccs_sale.sub_ip a, bccs_sale.subscriber b ");
        sqlQuery.append("  WHERE       a.status IN (2) ");
        sqlQuery.append("          AND EXISTS (SELECT   * ");
        sqlQuery.append("                        FROM   bccs_sale.subscriber ");
        sqlQuery.append("                       WHERE   status IN (7, 2) AND a.sub_id = sub_id) ");
        sqlQuery.append("          AND a.ip_block IS NOT NULL ");
        sqlQuery.append("          AND a.sub_id = b.sub_id ");
        sqlQuery.append("          AND a.ip = #ip ");
        Query query = emSale.createNativeQuery(sqlQuery.toString());
        query.setParameter("ip", ip);
        List<Object[]> lstObj = query.getResultList();
        if (lstObj != null && !lstObj.isEmpty()) {
            for (Object obj[] : lstObj) {
                BrasIppoolDTO brasIppoolDTO = new BrasIppoolDTO();
                brasIppoolDTO.setIsdn(DataUtil.safeToString(obj[0]));
                brasIppoolDTO.setIpPool(DataUtil.safeToString(obj[1]));
                brasIppoolDTO.setStatus(DataUtil.safeToString(obj[2]));
                return brasIppoolDTO;
            }
        }
        return null;
    }

    public boolean checkIpAssignSubsubcriber(String ip) throws Exception {
        StringBuilder sqlQuery = new StringBuilder(" ");
        sqlQuery.append(" SELECT   *");
        sqlQuery.append("   FROM   bccs_sale.sub_ip a");
        sqlQuery.append("  WHERE       status IN (1, 2)");
        sqlQuery.append("          AND EXISTS (SELECT   *");
        sqlQuery.append("                        FROM   bccs_sale.subscriber");
        sqlQuery.append("                       WHERE   status IN (7, 2) AND a.sub_id = sub_id)");
        sqlQuery.append("          AND ip_block IS NOT NULL");
        sqlQuery.append("          AND a.ip = #ip");
        Query query = emSale.createNativeQuery(sqlQuery.toString());
        query.setParameter("ip", ip);
        List<Object[]> lstObj = query.getResultList();
        if (lstObj != null && !lstObj.isEmpty()) {
            return true;
        }
        return false;
    }

}