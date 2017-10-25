package com.viettel.bccs.im1.repo;

import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.im1.dto.ShopIm1DTO;
import com.viettel.bccs.im1.model.QShopIm1;
import com.viettel.bccs.im1.model.ShopIm1;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.common.util.mapper.BaseMapper;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

public class ShopIm1RepoImpl implements ShopIm1RepoCustom {

    public static final Logger logger = Logger.getLogger(ShopIm1RepoCustom.class);
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM_LIB)
    private EntityManager em;

    private final BaseMapper<ShopIm1, ShopIm1DTO> mapper = new BaseMapper(ShopIm1.class, ShopIm1DTO.class);

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QShopIm1 shop = QShopIm1.shop1;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                ShopIm1.COLUMNS column = ShopIm1.COLUMNS.valueOf(filter.getProperty().toUpperCase());
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

    public void deleteStock(List<Long> lstShopId) throws Exception {
        if (DataUtil.isNullOrEmpty(lstShopId)) {
            return;
        }
        StringBuilder builder = new StringBuilder(" ");
        builder.append(" UPDATE bccs_im.shop SET status = 0 WHERE shop_id " + DbUtil.createInQuery("shop_id", lstShopId));
        Query query = em.createNativeQuery(builder.toString());
        DbUtil.setParamInQuery(query, "shop_id", lstShopId);
        query.executeUpdate();
    }
}