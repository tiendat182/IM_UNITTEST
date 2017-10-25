package com.viettel.bccs.inventory.repo;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.ChangeModelTransDetailDTO;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.model.ChangeModelTransDetail.COLUMNS;
import com.viettel.bccs.inventory.model.QChangeModelTransDetail;

import java.util.List;

import com.viettel.fw.common.util.DbUtil;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public class ChangeModelTransDetailRepoImpl implements ChangeModelTransDetailRepoCustom {

    public static final Logger logger = Logger.getLogger(ChangeModelTransDetailRepoCustom.class);
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QChangeModelTransDetail changeModelTransDetail = QChangeModelTransDetail.changeModelTransDetail;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case CHANGEMODELTRANSDETAILID:
                        expression = DbUtil.createLongExpression(changeModelTransDetail.changeModelTransDetailId, filter);
                        break;
                    case CHANGEMODELTRANSID:
                        expression = DbUtil.createLongExpression(changeModelTransDetail.changeModelTransId, filter);
                        break;
                    case CREATEDATE:
                        expression = DbUtil.createDateExpression(changeModelTransDetail.createDate, filter);
                        break;
                    case NEWPRODOFFERID:
                        expression = DbUtil.createLongExpression(changeModelTransDetail.newProdOfferId, filter);
                        break;
                    case NOTE:
                        expression = DbUtil.createStringExpression(changeModelTransDetail.note, filter);
                        break;
                    case OLDPRODOFFERID:
                        expression = DbUtil.createLongExpression(changeModelTransDetail.oldProdOfferId, filter);
                        break;
                    case QUANTITY:
                        expression = DbUtil.createLongExpression(changeModelTransDetail.quantity, filter);
                        break;
                    case STATEID:
                        expression = DbUtil.createLongExpression(changeModelTransDetail.stateId, filter);
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

    public List<ChangeModelTransDetailDTO> viewDetail(Long changeModelTransId) throws Exception {
        List<ChangeModelTransDetailDTO> lstChangeModelTransDetailDTOs = Lists.newArrayList();
        StringBuilder queryString = new StringBuilder("");
        queryString.append(" SELECT ctd.change_model_trans_detail_id as changeModelTransId, ");
        queryString.append("        (SELECT code || '-' || name ");
        queryString.append("           FROM product_offering ");
        queryString.append("          WHERE prod_offer_id = ctd.old_prod_offer_id) ");
        queryString.append("           AS oldProdOfferName, ");
        queryString.append("        (SELECT code || '-' || name ");
        queryString.append("           FROM product_offering ");
        queryString.append("          WHERE prod_offer_id = ctd.new_prod_offer_id) ");
        queryString.append("           AS newProdOfferName, ");
        queryString.append("        (SELECT   sv.name ");
        queryString.append("           FROM   option_set s, option_set_value sv ");
        queryString.append("            WHERE       s.id = sv.option_set_id ");
        queryString.append("                AND s.status = 1 ");
        queryString.append("                AND sv.status = 1 ");
        queryString.append("                AND s.code = 'GOOD_STATE' ");
        queryString.append("                AND sv.VALUE = to_char(ctd.state_id))  AS stateName, ");
        queryString.append("        ctd.quantity AS quantity ");
        queryString.append("   FROM bccs_im.change_model_trans_detail ctd ");
        queryString.append("  WHERE change_model_trans_id = #changeModelTransId ");
        Query query = em.createNativeQuery(queryString.toString());
        query.setParameter("changeModelTransId", changeModelTransId);
        List<Object[]> lst = query.getResultList();
        if (lst != null && !lst.isEmpty()) {
            for (Object[] objects : lst) {
                ChangeModelTransDetailDTO changeModelTransDetailDTO = new ChangeModelTransDetailDTO();
                int index = 0;
                changeModelTransDetailDTO.setChangeModelTransDetailId(DataUtil.safeToLong(objects[index++]));
                changeModelTransDetailDTO.setOldProdOfferName(DataUtil.safeToString(objects[index++]));
                changeModelTransDetailDTO.setNewProdOfferName(DataUtil.safeToString(objects[index++]));
                changeModelTransDetailDTO.setStateStr(DataUtil.safeToString(objects[index++]));
                changeModelTransDetailDTO.setQuantity(DataUtil.safeToLong(objects[index]));
                lstChangeModelTransDetailDTOs.add(changeModelTransDetailDTO);
            }
        }
        return lstChangeModelTransDetailDTOs;
    }

    @Override
    public List<ChangeModelTransDetailDTO> findModelTransDetailByChangeModelTransId(Long changeModelTransId) throws Exception {
        List<ChangeModelTransDetailDTO> lstChangeModelTransDetailDTOs = Lists.newArrayList();
        StringBuilder queryString = new StringBuilder("");
        queryString.append("SELECT change_model_trans_id, " +
                "       change_model_trans_detail_id, " +
                "       (SELECT code " +
                "          FROM product_offering\n" +
                "         WHERE prod_offer_id = a.old_prod_offer_id) " +
                "           old_offer_code, " +
                "       (SELECT code " +
                "          FROM product_offering " +
                "         WHERE prod_offer_id = a.new_prod_offer_id) " +
                "           new_offer_code, " +
                "       (SELECT osv.name " +
                "          FROM option_set os, option_set_value osv " +
                "         WHERE     os.id = osv.option_set_id " +
                "               AND os.code = 'GOOD_STATE' " +
                "               AND osv.VALUE = TO_CHAR (a.state_id)) " +
                "           state_detail, " +
                "           state_id, " +
                "       quantity " +
                "  FROM change_model_trans_detail a " +
                " WHERE change_model_trans_id = #changeModelTransId ");
        Query query = em.createNativeQuery(queryString.toString());
        query.setParameter("changeModelTransId", changeModelTransId);
        List<Object[]> lst = query.getResultList();
        if (lst != null && !lst.isEmpty()) {
            for (Object[] objects : lst) {
                ChangeModelTransDetailDTO changeModelTransDetailDTO = new ChangeModelTransDetailDTO();
                int index = 0;
                changeModelTransDetailDTO.setChangeModelTransId(DataUtil.safeToLong(objects[index++]));
                changeModelTransDetailDTO.setChangeModelTransDetailId(DataUtil.safeToLong(objects[index++]));
                changeModelTransDetailDTO.setOldProdOfferName(DataUtil.safeToString(objects[index++]));
                changeModelTransDetailDTO.setNewProdOfferName(DataUtil.safeToString(objects[index++]));
                changeModelTransDetailDTO.setStateStr(DataUtil.safeToString(objects[index++]));
                changeModelTransDetailDTO.setStateId(DataUtil.safeToLong(objects[index++]));
                changeModelTransDetailDTO.setQuantity(DataUtil.safeToLong(objects[index]));
                lstChangeModelTransDetailDTOs.add(changeModelTransDetailDTO);
            }
        }
        return lstChangeModelTransDetailDTOs;
    }

}