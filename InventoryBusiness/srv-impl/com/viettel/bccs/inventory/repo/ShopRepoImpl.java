package com.viettel.bccs.inventory.repo;

import com.google.common.collect.Lists;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.model.QShop;
import com.viettel.bccs.inventory.model.Shop;
import com.viettel.bccs.inventory.model.Shop.COLUMNS;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.common.util.mapper.BaseMapper;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

public class ShopRepoImpl implements ShopRepoCustom {

    public static final Logger logger = Logger.getLogger(ShopRepoCustom.class);
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    private final BaseMapper<Shop, ShopDTO> mapper = new BaseMapper(Shop.class, ShopDTO.class);

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QShop shop = QShop.shop1;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case ACCOUNT:
                        expression = DbUtil.createStringExpression(shop.account, filter);
                        break;
                    case ADDRESS:
                        expression = DbUtil.createStringExpression(shop.address, filter);
                        break;
                    case AREACODE:
                        expression = DbUtil.createStringExpression(shop.areaCode, filter);
                        break;
                    case BANKNAME:
                        expression = DbUtil.createStringExpression(shop.bankName, filter);
                        break;
                    case BANKPLUSMOBILE:
                        expression = DbUtil.createStringExpression(shop.bankplusMobile, filter);
                        break;
                    case BUSINESSLICENCE:
                        expression = DbUtil.createStringExpression(shop.businessLicence, filter);
                        break;
                    case CENTERCODE:
                        expression = DbUtil.createStringExpression(shop.centerCode, filter);
                        break;
                    case CHANNELTYPEID:
                        expression = DbUtil.createLongExpression(shop.channelTypeId, filter);
                        break;
                    case COMPANY:
                        expression = DbUtil.createStringExpression(shop.company, filter);
                        break;
                    case CONTACTNAME:
                        expression = DbUtil.createStringExpression(shop.contactName, filter);
                        break;
                    case CONTACTTITLE:
                        expression = DbUtil.createStringExpression(shop.contactTitle, filter);
                        break;
                    case CONTRACTFROMDATE:
                        expression = DbUtil.createDateExpression(shop.contractFromDate, filter);
                        break;
                    case CONTRACTNO:
                        expression = DbUtil.createStringExpression(shop.contractNo, filter);
                        break;
                    case CONTRACTTODATE:
                        expression = DbUtil.createDateExpression(shop.contractToDate, filter);
                        break;
                    case CREATEDATE:
                        expression = DbUtil.createDateExpression(shop.createDate, filter);
                        break;
                    case DEPOSITVALUE:
                        expression = DbUtil.createLongExpression(shop.depositValue, filter);
                        break;
                    case DESCRIPTION:
                        expression = DbUtil.createStringExpression(shop.description, filter);
                        break;
                    case DISCOUNTPOLICY:
                        expression = DbUtil.createStringExpression(shop.discountPolicy, filter);
                        break;
                    case DISTRICT:
                        expression = DbUtil.createStringExpression(shop.district, filter);
                        break;
                    case EMAIL:
                        expression = DbUtil.createStringExpression(shop.email, filter);
                        break;
                    case FAX:
                        expression = DbUtil.createStringExpression(shop.fax, filter);
                        break;
                    case FILENAME:
                        expression = DbUtil.createStringExpression(shop.fileName, filter);
                        break;
                    case HOME:
                        expression = DbUtil.createStringExpression(shop.home, filter);
                        break;
                    case IDISSUEDATE:
                        expression = DbUtil.createDateExpression(shop.idIssueDate, filter);
                        break;
                    case IDISSUEPLACE:
                        expression = DbUtil.createStringExpression(shop.idIssuePlace, filter);
                        break;
                    case IDNO:
                        expression = DbUtil.createStringExpression(shop.idNo, filter);
                        break;
                    case NAME:
                        expression = DbUtil.createStringExpression(shop.name, filter);
                        break;
                    case OLDSHOPCODE:
                        expression = DbUtil.createStringExpression(shop.oldShopCode, filter);
                        break;
                    case PARSHOPCODE:
                        expression = DbUtil.createStringExpression(shop.parShopCode, filter);
                        break;
                    case PARENTSHOPID:
                        expression = DbUtil.createLongExpression(shop.parentShopId, filter);
                        break;
                    case PAYCOMM:
                        expression = DbUtil.createStringExpression(shop.payComm, filter);
                        break;
                    case PRECINCT:
                        expression = DbUtil.createStringExpression(shop.precinct, filter);
                        break;
                    case PRICEPOLICY:
                        expression = DbUtil.createStringExpression(shop.pricePolicy, filter);
                        break;
                    case PROVINCE:
                        expression = DbUtil.createStringExpression(shop.province, filter);
                        break;
                    case PROVINCECODE:
                        expression = DbUtil.createStringExpression(shop.provinceCode, filter);
                        break;
                    case SHOP:
                        expression = DbUtil.createStringExpression(shop.shop, filter);
                        break;
                    case SHOPCODE:
                        expression = DbUtil.createStringExpression(shop.shopCode, filter);
                        break;
                    case SHOPID:
                        expression = DbUtil.createLongExpression(shop.shopId, filter);
                        break;
                    case SHOPPATH:
                        expression = DbUtil.createStringExpression(shop.shopPath, filter);
                        break;
                    case SHOPTYPE:
                        expression = DbUtil.createStringExpression(shop.shopType, filter);
                        break;
                    case STATUS:
                        expression = DbUtil.createStringExpression(shop.status, filter);
                        break;
                    case STOCKNUM:
                        expression = DbUtil.createLongExpression(shop.stockNum, filter);
                        break;
                    case STOCKNUMIMP:
                        expression = DbUtil.createLongExpression(shop.stockNumImp, filter);
                        break;
                    case STREET:
                        expression = DbUtil.createStringExpression(shop.street, filter);
                        break;
                    case STREETBLOCK:
                        expression = DbUtil.createStringExpression(shop.streetBlock, filter);
                        break;
                    case TEL:
                        expression = DbUtil.createStringExpression(shop.tel, filter);
                        break;
                    case TELNUMBER:
                        expression = DbUtil.createStringExpression(shop.telNumber, filter);
                        break;
                    case TIN:
                        expression = DbUtil.createStringExpression(shop.tin, filter);
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

    /**
     * author HoangNT
     *
     * @param parentShopId
     * @return
     */
    @Override
    public List<ShopDTO> getListShopByParentShopId(Long parentShopId) throws Exception {
        try {
            List<Shop> lst;
            StringBuilder strQuery = new StringBuilder();
            strQuery.append("SELECT * FROM SHOP WHERE PARENT_SHOP_ID = #shopId OR SHOP_ID = #shopId");
            //execute query
            Query query = em.createNativeQuery(strQuery.toString(), Shop.class);
            query.setParameter("shopId", parentShopId);
            lst = query.getResultList();
            if (lst != null && !lst.isEmpty()) {
                return mapper.toDtoBean(lst);
            }
        } catch (Exception ex) {
            logger.error("Error: ", ex);
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "Error Exception");
        }
        return null;

    }

    /**
     * author HoangNT
     *
     * @return
     */
    @Override
    public List<ShopDTO> getListAllBranch() throws Exception {
        try {
            List<Shop> lst;
            StringBuilder strQuery = new StringBuilder();
            strQuery.append("SELECT * FROM SHOP WHERE PARENT_SHOP_ID = #parentShopId AND SHOP_TYPE = 2");
            //execute query
            Query query = em.createNativeQuery(strQuery.toString(), Shop.class);
            query.setParameter("parentShopId", Const.SHOP.SHOP_VTT_ID);
            lst = query.getResultList();
            if (lst != null && !lst.isEmpty()) {
                return mapper.toDtoBean(lst);
            }
        } catch (Exception ex) {
            logger.error("Error: ", ex);
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "Error Exception");
        }
        return null;
    }

    /**
     * @param ownerId
     * @param ownerType
     * @return
     * @throws Exception
     * @author thetm1
     */
    @Override
    public List<VShopStaffDTO> getListShopStaff(Long ownerId, String ownerType, String vtUnit) throws Exception {
        List<VShopStaffDTO> result = Lists.newArrayList();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append("SELECT owner_id, owner_code, owner_name, owner_address, owner_type, channel_type_id, status, shop_path, parent_shop_id, staff_owner_id ");
        strQuery.append(" FROM v_shop_staff ");
        strQuery.append("WHERE 1 = 1 ");
        strQuery.append("AND owner_id = #p1 ");
        strQuery.append("AND owner_type = #p2 ");
        strQuery.append("AND status= #p3 ");
        strQuery.append("AND channel_type_id in (select channel_type_id from channel_type where status = #p4 and object_type = #p5 ");
        if (!DataUtil.isNullOrEmpty(vtUnit)) {
            strQuery.append(" and is_vt_unit = #p6 ");
        }
        strQuery.append(" )");
        Query query = em.createNativeQuery(strQuery.toString());
        query.setParameter("p1", ownerId);
        query.setParameter("p2", ownerType);
        query.setParameter("p3", Const.STATUS.ACTIVE);
        query.setParameter("p4", Const.STATUS.ACTIVE);
        query.setParameter("p5", ownerType);
        if (!DataUtil.isNullOrEmpty(vtUnit)) {
            query.setParameter("p6", vtUnit);
        }

        List<Object[]> lst = query.getResultList();
        if (lst != null && !lst.isEmpty()) {
            for (Object[] object : lst) {
                int index = 0;
                VShopStaffDTO vShopStaff = new VShopStaffDTO();
                vShopStaff.setOwnerId(DataUtil.safeToString(object[index++]));
                vShopStaff.setOwnerCode(DataUtil.safeToString(object[index++]));
                vShopStaff.setOwnerName(DataUtil.safeToString(object[index++]));
                vShopStaff.setOwnerAddress(DataUtil.safeToString(object[index++]));
                vShopStaff.setOwnerType(DataUtil.safeToString(object[index++]));
                vShopStaff.setChannelTypeId(DataUtil.safeToString(object[index++]));
                vShopStaff.setStatus(DataUtil.safeToString(object[index++]));
                vShopStaff.setShopPath(DataUtil.safeToString(object[index++]));
                vShopStaff.setParentShopId(DataUtil.safeToString(object[index++]));
                vShopStaff.setStaffOwnerId(DataUtil.safeToString(object[index]));

                result.add(vShopStaff);
            }
            return result;
        }
        return null;
    }

    @Override
    public boolean isCenterOrBranch(Long shopId) throws Exception {
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(" select count(*) as total from shop where instr(shop_path, '_', 1, 4) > 0 and shop_id = #shopId ");
        Query query = em.createNativeQuery(strQuery.toString());
        query.setParameter("shopId", shopId);
        BigDecimal result = (BigDecimal) query.getSingleResult();
        if (result != null) {
            return result.longValue() > 0;
        }
        return false;
    }

    @Override
    public List<ShopDTO> getListShopByCodeOption(String code) throws Exception {
        StringBuilder builder = new StringBuilder(" SELECT c.* ");
        builder.append(" FROM option_set a, option_set_value b, shop c ");
        builder.append(" WHERE  a.id = b.option_set_id ");
        builder.append("    AND c.shop_code = b.VALUE ");
        builder.append("    AND a.code = #code ");
        builder.append("    AND b.status = #status ");
        builder.append("    AND c.status = #status ");
        builder.append("    ORDER BY c.name ");
        Query query = em.createNativeQuery(builder.toString(), Shop.class);
        query.setParameter("code", code);
        query.setParameter("status", Const.STATUS.ACTIVE);
        return mapper.toDtoBean(query.getResultList());
    }

    @Override
    public List<Long> getListChannelType(Long vtUnit) throws Exception {
        StringBuilder builder = new StringBuilder("select channel_type_id from channel_type where object_type = 1 and is_vt_unit = ? and status = 1");
        Query query = em.createNativeQuery(builder.toString());
        query.setParameter(1, vtUnit == null ? Const.VT_UNIT.VT : vtUnit);
        List queryResult = query.getResultList();
        List<Long> lstResult = Lists.newArrayList();
        for (Object result : queryResult) {
            lstResult.add(DataUtil.safeToLong(result));
        }
        return lstResult;
    }

    @Override
    public List<ChannelTypeDTO> getListChannelTypeParam(Long channelTypeId, String ObjectType, String isVTUnit) throws Exception {
        StringBuilder builder = new StringBuilder("select channel_type_id, name, is_vt_unit from channel_type where 1 = 1 and status = 1 ");
        if (!DataUtil.isNullOrZero(channelTypeId)) {
            builder.append(" and channel_type_id = #channel_type_id ");
        }
        if (!DataUtil.isNullOrEmpty(ObjectType)) {
            builder.append(" and object_type = #object_type ");
        }
        if (!DataUtil.isNullOrEmpty(isVTUnit)) {
            builder.append(" and is_vt_unit = #is_vt_unit ");
        }
        builder.append(" ORDER BY   NLSSORT (name, 'nls_sort=Vietnamese') ASC ");
        Query query = em.createNativeQuery(builder.toString());
        if (!DataUtil.isNullOrZero(channelTypeId)) {
            query.setParameter("channel_type_id", channelTypeId);
        }
        if (!DataUtil.isNullOrEmpty(ObjectType)) {
            query.setParameter("object_type", ObjectType);
        }
        if (!DataUtil.isNullOrEmpty(isVTUnit)) {
            query.setParameter("is_vt_unit", isVTUnit);
        }
        List<Object[]> lst = query.getResultList();
        List<ChannelTypeDTO> lstResult = Lists.newArrayList();
        if (lst != null && !lst.isEmpty()) {
            for (Object[] object : lst) {
                int index = 0;
                ChannelTypeDTO channelTypeDTO = new ChannelTypeDTO();
                channelTypeDTO.setChannelTypeId(DataUtil.safeToLong(object[index++]));
                channelTypeDTO.setName(DataUtil.safeToString(object[index++]));
                channelTypeDTO.setIsVtUnit(DataUtil.safeToString(object[index]));
                lstResult.add(channelTypeDTO);
            }
        }
        return lstResult;
    }

    @Override
    public boolean validateConstraints(Long shopId, Long parrentId) throws Exception {

        StringBuilder builder = new StringBuilder("select * from shop sh")
                .append(" where sh.shop_id=#sID")
                .append(" and (sh.shop_path like (select psh.shop_path from shop psh where psh.shop_id=#prID)||'$_%' ESCAPE '$'")
                .append(" or sh.shop_path = (select psh.shop_path from shop psh where psh.shop_id=#pr1ID))");
        Query query = em.createNativeQuery(builder.toString());
        query.setParameter("sID", shopId);
        query.setParameter("prID", parrentId);
        query.setParameter("pr1ID", parrentId);
        if (DataUtil.isNullOrEmpty(query.getResultList())) {
            return false;
        }
        return true;
    }

    @Override
    public String getStockReportTemplate(Long shopId) throws Exception {
        StringBuilder builder = new StringBuilder("");
        builder.append("select stock_report_template ");
        builder.append(" from channel_type cn");
        builder.append(" where cn.channel_type_id in(select channel_type_id from shop where shop_id=#ownerId)");
        Query query = em.createNativeQuery(builder.toString());
        query.setParameter("ownerId", shopId);
        Object object = query.getSingleResult();
        return DataUtil.safeToString(object);
    }

    @Override
    public Long checkParentShopIsBranch(Long shopId) throws Exception {
        StringBuilder builder = new StringBuilder("");
        builder.append("select shop_id ");
        builder.append("  from shop where  shop_type =2 ");
        builder.append("   and status =1 ");
        builder.append("  start with shop_id =#shop_id ");
        builder.append("  connect by prior parent_shop_id = shop_id");

        Query query = em.createNativeQuery(builder.toString());
        query.setParameter("shop_id", shopId);
        List queryResult = query.getResultList();
        if (queryResult != null && !queryResult.isEmpty()) {
            return DataUtil.safeToLong(queryResult.get(0));
        }
        return null;
    }

    @Override
    public VShopStaffDTO getShopByCodeForDistribute(String shopCode, Long parentShopId, String ownerType) throws LogicException, Exception {

        StringBuilder strQuery = new StringBuilder();
        strQuery.append("SELECT owner_id, owner_code, owner_name, owner_type, channel_type_id, status, shop_path, parent_shop_id, staff_owner_id ");
        strQuery.append(" FROM v_shop_staff v WHERE 1=1 AND status=#status");
        strQuery.append(" AND v.owner_code=#shopCode ");
        strQuery.append(" AND v.owner_type=#ownerType ");
        if (!DataUtil.isNullObject(ownerType) && ownerType.equals(Const.OWNER_TYPE.SHOP)) {
            strQuery.append(" AND (v.owner_id = #parentShopId OR v.shop_path LIKE #parentShopPath ) ");
        } else {
            strQuery.append(" AND (v.parent_shop_id = #parentShopId OR v.shop_path LIKE #parentShopPath ) ");
        }
        Query query = em.createNativeQuery(strQuery.toString());
        query.setParameter("shopCode", shopCode);
        query.setParameter("ownerType", ownerType);
        query.setParameter("parentShopId", parentShopId);
        query.setParameter("parentShopPath", "%_" + parentShopId + "_%");
        query.setParameter("status", Const.STATUS.ACTIVE);
        Object ob = null;
        try {
            List<Object> lst = query.getResultList();
            if (!DataUtil.isNullOrEmpty(lst)) {
                ob = lst.get(0);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }

        if (!DataUtil.isNullObject(ob)) {
            Object[] object = (Object[]) ob;
            int index = 0;
            VShopStaffDTO vShopStaff = new VShopStaffDTO();
            vShopStaff.setOwnerId(DataUtil.safeToString(object[index++]));
            vShopStaff.setOwnerCode(DataUtil.safeToString(object[index++]));
            vShopStaff.setOwnerName(DataUtil.safeToString(object[index++]));
            vShopStaff.setOwnerType(DataUtil.safeToString(object[index++]));
            vShopStaff.setChannelTypeId(DataUtil.safeToString(object[index++]));
            vShopStaff.setStatus(DataUtil.safeToString(object[index++]));
            vShopStaff.setShopPath(DataUtil.safeToString(object[index++]));
            vShopStaff.setParentShopId(DataUtil.safeToString(object[index++]));
            vShopStaff.setStaffOwnerId(DataUtil.safeToString(object[index]));
            return vShopStaff;
        }
        return null;
    }

    public List<ShopDTO> getListShopByStaffShopId(Long shopId, String shopCode) throws LogicException, Exception {
        try {
            List<Shop> lst;
            StringBuilder strQuery = new StringBuilder();
            strQuery.append("SELECT * FROM SHOP WHERE STATUS = #status and (shop_path like #shopIdLike or shop_id = #shopId) and shop_code = #shopCode");
            //execute query
            Query query = em.createNativeQuery(strQuery.toString(), Shop.class);
            query.setParameter("status", Const.STATUS.ACTIVE);
            query.setParameter("shopIdLike", "%_" + shopId + "_%");
            query.setParameter("shopId", shopId);
            query.setParameter("shopCode", shopCode);
            lst = query.getResultList();
            if (lst != null && !lst.isEmpty()) {
                return mapper.toDtoBean(lst);
            }
        } catch (Exception ex) {
            logger.error("Error: ", ex);
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "Error Exception");
        }
        return null;
    }

    @Override
    public List<SmartPhoneDTO> getListNVStockIsdnMbccs(StaffDTO staffDTO) throws Exception {
        StringBuilder builder = new StringBuilder(" ");
        builder.append("   SELECT   ownerId, ownerType, ownerCode, ownerName ");
        builder.append(" 	FROM   (SELECT	 staff_id ownerId, 2 ownerType, staff_code ownerCode, name ownerName ");
        builder.append(" 			  FROM	 bccs_im.staff ");
        builder.append(" 			 WHERE	 staff_id = #staffId ");
        builder.append(" 			UNION ALL ");
        builder.append(" 			SELECT	 shop_id ownerId, 1 ownerType, shop_code ownerCode, name ownerName ");
        builder.append(" 			  FROM	 bccs_im.shop ");
        builder.append(" 			 WHERE	 shop_id = #shopId ");
        builder.append(" 			UNION ALL ");
        builder.append(" 			SELECT	 shop_id ownerId, 1 ownerType, shop_code ownerCode, name ownerName ");
        builder.append(" 			  FROM	 bccs_im.shop ");
        builder.append(" 			 WHERE	 channel_type_id = 8 AND status = 1) ");
        builder.append("  order by nlssort(ownerName,'nls_sort=Vietnamese') asc ");
        Query query = em.createNativeQuery(builder.toString());
        query.setParameter("staffId", staffDTO.getStaffId());
        query.setParameter("shopId", staffDTO.getShopId());
        List<Object[]> lst = query.getResultList();
        List<SmartPhoneDTO> lstResult = Lists.newArrayList();
        if (lst != null && !lst.isEmpty()) {
            for (Object[] object : lst) {
                int index = 0;
                SmartPhoneDTO smartPhoneDTO = new SmartPhoneDTO();
                smartPhoneDTO.setOwnerId(DataUtil.safeToLong(object[index++]));
                smartPhoneDTO.setOwnerType(DataUtil.safeToLong(object[index++]));
                smartPhoneDTO.setOwnerCode(DataUtil.safeToString(object[index++]));
                smartPhoneDTO.setOwnerName(DataUtil.safeToString(object[index]));
                lstResult.add(smartPhoneDTO);
            }
        }
        return lstResult;
    }

    @Override
    public ShopDTO getShopByStaffCode(String staffCode) throws Exception {
        try {
            List<Shop> lst;
            StringBuilder strQuery = new StringBuilder();
            strQuery.append("SELECT * FROM SHOP WHERE shop_id = " +
                    " (select shop_id from staff where lower(staff_code) = lower(#staffCode) and status = #status )");
            Query query = em.createNativeQuery(strQuery.toString(), Shop.class);
            query.setParameter("staffCode", staffCode.trim());
            query.setParameter("status", Const.STATUS.ACTIVE);
            lst = query.getResultList();
            if (lst != null && !lst.isEmpty()) {
                return mapper.toDtoBean(lst.get(0));
            }
        } catch (Exception ex) {
            logger.error("Error: ", ex);
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "Error Exception");
        }
        return null;
    }

    @Override
    public void increaseStockNum(Connection conn, Long shopId) throws Exception {
        PreparedStatement ps = null;
        try {
            StringBuilder builder = new StringBuilder(" update shop set stock_num_imp=nvl(stock_num_imp,0)+1 where shop_id=? ");
            ps = conn.prepareCall(builder.toString());
            ps.setLong(1, shopId);
            ps.executeUpdate();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw ex;
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
    }

    @Override
    public List<ShopDTO> getListShopByCodeAndActiveStatus(List<String> shopCodes) throws Exception {
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(" select * from SHOP where 1=1 and (upper(SHOP_CODE) " + DbUtil.createInQuery("SHOP_CODE", shopCodes) + ") and STATUS=#status");
        Query query = em.createNativeQuery(strQuery.toString(), Shop.class);
        DbUtil.setParamInQuery(query, "SHOP_CODE", shopCodes);
        query.setParameter("status", Const.STATUS.ACTIVE);
        List<Shop> lst = query.getResultList();
        if (lst != null && !lst.isEmpty()) {
            return mapper.toDtoBean(lst);
        }
        return new ArrayList<>();
    }

    public boolean checkExsitShopStaff(Long ownerId, Long ownerType) throws Exception {
        StringBuilder builder = new StringBuilder("SELECT owner_id, owner_code, owner_name, owner_address");
        builder.append(" FROM v_shop_staff ");
        builder.append(" WHERE 1 = 1 ");
        builder.append(" AND owner_id = #p1 ");
        builder.append(" AND owner_type = #p2 ");
        builder.append(" AND status= 1 ");
        Query query = em.createNativeQuery(builder.toString());
        query.setParameter("p1", ownerId);
        query.setParameter("p2", ownerType);
        List result = query.getResultList();
        if (!DataUtil.isNullOrEmpty(result)) {
            return true;
        }
        return false;
    }

    @Override
    public String getProvince(Long shopId) throws Exception {
        StringBuilder builder = new StringBuilder("select name from area where area_code in (select province from shop where shop_id=?)");
        Query query = em.createNativeQuery(builder.toString());
        query.setParameter(1, shopId);
        List result = query.getResultList();
        if (!DataUtil.isNullOrEmpty(result)) {
            return result.get(0).toString();
        }
        return null;
    }

    public MvShopStaffDTO getMViewShopStaff(Long ownerType, Long ownerId) throws Exception {
        StringBuilder builder = new StringBuilder("SELECT OWNER_CODE, OWNER_NAME, CONTACT_NAME, CONTACT_MOBILE,PROVINCE_NAME,DISTRICT_NAME");
        builder.append(" FROM mv_shop_staff ");
        builder.append(" WHERE 1 = 1 AND status = 1 ");
        builder.append(" AND owner_id = #p1 ");
        builder.append(" AND owner_type = #p2 ");
        Query query = em.createNativeQuery(builder.toString());
        query.setParameter("p1", ownerId);
        query.setParameter("p2", ownerType);
        List<Object[]> result = query.getResultList();
        if (!DataUtil.isNullOrEmpty(result)) {
            Object[] item = result.get(0);
            MvShopStaffDTO mvShopStaffDTO = new MvShopStaffDTO();
            int index = 0;
            mvShopStaffDTO.setOwnerCode(DataUtil.safeToString(item[index++]));
            mvShopStaffDTO.setOwnerName(DataUtil.safeToString(item[index++]));
            mvShopStaffDTO.setContactName(DataUtil.safeToString(item[index++]));
            mvShopStaffDTO.setContactMobile(DataUtil.safeToString(item[index++]));
            mvShopStaffDTO.setProvinceName(DataUtil.safeToString(item[index++]));
            mvShopStaffDTO.setDistrictName(DataUtil.safeToString(item[index]));
            return mvShopStaffDTO;
        }
        return null;
    }

    public ShopDTO getShopByShopKeeper(Long staffId) throws Exception {
        StringBuilder builder = new StringBuilder(" ");
        builder.append(" SELECT * FROM shop WHERE status = 1 and shop_keeper_id = #staffId");
        Query query = em.createNativeQuery(builder.toString(), Shop.class);
        query.setParameter("staffId", staffId);
        List<Shop> result = query.getResultList();
        if (!DataUtil.isNullOrEmpty(result)) {
            if (result.size() > 1) {
                //thu kho nhieu hon 1 don vi
                return null;
            }
            return mapper.toDtoBean(result.get(0));
        }
        return null;
    }

    public boolean checkShopIsBranch(Long shopId) throws Exception {
        List<Shop> lst;
        StringBuilder strQuery = new StringBuilder();
        strQuery.append("SELECT * FROM SHOP WHERE PARENT_SHOP_ID = 7282 AND SHOP_TYPE = 2 AND SHOP_ID = #shopId");
        //execute query
        Query query = em.createNativeQuery(strQuery.toString());
        query.setParameter("shopId", shopId);
        lst = query.getResultList();
        if (lst != null && !lst.isEmpty()) {
            return true;
        }
        return false;
    }

}