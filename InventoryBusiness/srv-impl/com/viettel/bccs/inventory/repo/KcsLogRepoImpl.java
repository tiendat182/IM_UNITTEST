package com.viettel.bccs.inventory.repo;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.ProductInStockDTO;
import com.viettel.fw.common.util.DataUtil;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by Hellpoethero on 06/09/2016.
 */
public class KcsLogRepoImpl implements KcsLogRepoCustom {
    public static final Logger logger = Logger.getLogger(KcsLogRepoCustom.class);

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM_LIB)
    private EntityManager emIM1;

    /*@Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        return null;
    }*/

    @Override
    public List<ProductInStockDTO> findProductInStock(String code, String serial) throws Exception {
        StringBuilder sql = new StringBuilder(
                "select " +
                        " sh.owner_id, " +
                        " sh.owner_type, " +
                        " sh.prod_offer_id, " +
                        " sh.status stateAfterId, " +
                        " sh.state_id stateId " +
                        " FROM" +
                        " stock_handset sh, " +
                        " product_offering po " +
                        " WHERE " +
                        " po.prod_offer_id = sh.prod_offer_id " +
                        " and lower(po.code) = lower(#code)" +
                        " and sh.serial = #serial"
        );
        Query query = em.createNativeQuery(sql.toString());

        query.setParameter("code", code);
        query.setParameter("serial", serial);
        List queryResult = query.getResultList();
        List<ProductInStockDTO> result = Lists.newArrayList();
        if (!DataUtil.isNullOrEmpty(queryResult)) {
            for (Object obj : queryResult) {
                result.add(productInStockDTOValue((Object[]) obj));
            }
        }
        return result;
    }

    @Override
    public List<ProductInStockDTO> findProductInStockIm1(String code, String serial) throws Exception {
        StringBuilder sql = new StringBuilder(
                "select" +
                        " sh.owner_id," +
                        " sh.owner_type," +
                        " sh.stock_model_id," +
                        " sh.status stateAfterId," +
                        " sh.state_id stateId" +
                        " FROM" +
                        " bccs_im.stock_handset sh," +
                        " bccs_im.stock_model sm" +
                        " WHERE" +
                        " sm.stock_model_id = sh.stock_model_id" +
                        " and lower(sm.stock_model_code) = lower(#code)" +
                        " and sh.serial = #serial"
        );
        Query query = emIM1.createNativeQuery(sql.toString());
        query.setParameter("code", code);
        query.setParameter("serial", serial);
        List queryResult = query.getResultList();
        List<ProductInStockDTO> result = Lists.newArrayList();
        if (!DataUtil.isNullOrEmpty(queryResult)) {
            for (Object obj : queryResult) {
                result.add(productInStockDTOValue((Object[]) obj));
            }
        }
        return result;
    }

    private ProductInStockDTO productInStockDTOValue(Object[] objects) {
        ProductInStockDTO valueDTO = new ProductInStockDTO();

        valueDTO.setOwnerId(DataUtil.safeToLong(objects[0]));
        valueDTO.setOwnerType(DataUtil.safeToLong(objects[1]));
        valueDTO.setProdOfferId(DataUtil.safeToLong(objects[2]));
        valueDTO.setStateAfterId(DataUtil.safeToLong(objects[3]));
        valueDTO.setStateId(DataUtil.safeToLong(objects[4]));
        return valueDTO;
    }
}
