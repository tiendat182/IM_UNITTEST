package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.common.IMCommonUtils;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.BaseStock.BaseStockPartnerService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by hoangnt14 on 5/24/2016.
 */
@Service
public class BaseStockPartnerUpdateSimService extends BaseStockPartnerService {
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @Autowired
    private ProductOfferingService productOfferingService;
    @Autowired
    ShopService shopService;
    @Autowired
    ProductWs productWs;

    private List<HashMap<String, String>> lstMapParam;
    private String serial;

    @Transactional(rollbackFor = Exception.class)
    public Long executeStockTrans(ImportPartnerRequestDTO importPartnerRequestDTO, RequiredRoleMap requiredRoleMap, String sessionId) throws LogicException, Exception {
        Connection conn = null;
        Long result = 0L;
        try {
            //check validate
            doValidate(importPartnerRequestDTO);
            //prepare data
            lstMapParam = new ArrayList();
            List<String> lstProfile = importPartnerRequestDTO.getListProfile();
            List<String> lstParam = importPartnerRequestDTO.getListImportPartnerDetailDTOs().get(0).getLstParam();
            if (!DataUtil.isNullOrEmpty(lstParam)) {
                for (int i = 0; i < lstParam.size(); i++) {
                    HashMap<String, String> hashMap = new HashMap();
                    String param = lstParam.get(i);
                    String[] arrParam = param.split(Const.COMMA_SEPARATE);
                    for (int j = 0; j < lstProfile.size(); j++) {
                        hashMap.put(lstProfile.get(j), arrParam[j]);
                    }
                    lstMapParam.add(hashMap);
                }
            }
            conn = IMCommonUtils.getDBIMConnection();
            conn.setAutoCommit(false);
            result = updateSimByFile(importPartnerRequestDTO, conn, sessionId);
            conn.commit();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            if (conn != null && !conn.isClosed()) {
                conn.rollback();
            }
            throw ex;
        } finally {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        }
        return result;
    }

    public Long updateSimByFile(ImportPartnerRequestDTO importPartnerRequestDTO, Connection conn, String sessionId) throws Exception {
        PreparedStatement updateSim = null;
        Long numberSuccessRecord = 0L;
        Long numberErrorRecord = 0L;
        try {
            List<String> lstProfile = new ArrayList();
            //get list profile
            ProfileDTO profileDTO = productOfferingService.getProfileByProductId(importPartnerRequestDTO.getListImportPartnerDetailDTOs().get(0).getProdOfferId());
            if (profileDTO != null) {
                lstProfile = profileDTO.getLstColumnName();
            }
            StringBuilder strUpdate = new StringBuilder();
            strUpdate.append("UPDATE STOCK_SIM SET ");
            strUpdate.append("UPDATE_DATETIME = sysdate");
            //Them thong tin profile
            for (int i = 0; i < lstProfile.size(); i++) {
                if (!DataUtil.safeEqual(lstProfile.get(i), "SERIAL")) {
                    strUpdate.append(",");
                    strUpdate.append(lstProfile.get(i));
                    strUpdate.append(" = ?");
                }
            }
            strUpdate.append(" where owner_id = ? and owner_type = ? and to_number(serial) = to_number(?) and prod_offer_id = ?");
            strUpdate.append(" LOG ERRORS INTO  ERR$_STOCK_SIM(?) REJECT LIMIT UNLIMITED ");

            updateSim = conn.prepareStatement(strUpdate.toString());
            Long numberBatch = 0L;
            lstProfile.remove("SERIAL");
            for (int i = 0; i < lstMapParam.size(); i++) {
                HashMap<String, String> mapParam = lstMapParam.get(i);
                for (int j = 0; j < lstProfile.size(); j++) {
                    updateSim.setString(j + 1, mapParam.get(lstProfile.get(j).trim()));
                }
                serial = formatSerial(mapParam.get("SERIAL"), false);
                int next = lstProfile.size() + 1;
                System.out.println("==::"+importPartnerRequestDTO.getRegionStockId());
                if (!DataUtil.isNullOrZero(importPartnerRequestDTO.getRegionStockId())) {
                    updateSim.setLong(next, importPartnerRequestDTO.getRegionStockId());
                } else {
                    updateSim.setLong(next, importPartnerRequestDTO.getToOwnerId());
                }
                updateSim.setLong(next + 1, Const.OWNER_TYPE.SHOP_LONG);
                updateSim.setString(next + 2, serial);
                updateSim.setLong(next + 3, importPartnerRequestDTO.getListImportPartnerDetailDTOs().get(0).getProdOfferId());
                updateSim.setString(next + 4, sessionId);
                updateSim.addBatch();
                if (i % Const.DEFAULT_BATCH_SIZE == 0 && i > 0) {
                    boolean hasErrorInBatch = false; //truong hop co loi xay ra
                    Long tmpErrorRecordInBatch = 0L;
                    Long tmpSuccessRecordInBatch = 0L;
                    try {
                        int[] updateSuccess = updateSim.executeBatch();
                        //so ban ghi insert thanh cong
                        int tmpErrorRecord = countNumberErrorForUpdateSim(conn, sessionId);
                        tmpErrorRecordInBatch = tmpErrorRecord - numberErrorRecord;
                        tmpSuccessRecordInBatch = Const.DEFAULT_BATCH_SIZE - tmpErrorRecord;

                        if (tmpSuccessRecordInBatch <= 0) {
                            continue;
                        }

                        if (hasErrorInBatch) {
                            conn.rollback();
                        } else {
                            conn.commit();
                        }
                        ++numberBatch;
                    } catch (Exception ex) {
                        logger.error(ex.getMessage(), ex);
                        hasErrorInBatch = true;
                        conn.rollback();
                    }
                    if (hasErrorInBatch) {
                        tmpSuccessRecordInBatch = 0L;
                        tmpErrorRecordInBatch = Const.DEFAULT_BATCH_SIZE;
                    }
                    numberSuccessRecord += tmpSuccessRecordInBatch;
                    numberErrorRecord += tmpErrorRecordInBatch;

                }
            }

            //Xu ly voi so ban ghi con lai
            Long numberRemainRecord = lstMapParam.size() - numberBatch * Const.DEFAULT_BATCH_SIZE;
            if (numberRemainRecord > 0) {
                boolean hasErrorInBach = false; //truong hop co loi xay ra
                Long tmpErrorRecordInBatch = 0L;
                Long tmpSuccessRecordInBatch = 0L;
                try {
                    updateSim.executeBatch();
                    //so ban ghi insert thanh cong
                    int tmpErrorRecord = countNumberErrorForUpdateSim(conn, sessionId);
                    tmpErrorRecordInBatch = tmpErrorRecord - numberErrorRecord;
                    tmpSuccessRecordInBatch = Const.DEFAULT_BATCH_SIZE - tmpErrorRecord;

                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                    hasErrorInBach = true;
                    conn.rollback();
                }
                if (hasErrorInBach) {
                    tmpSuccessRecordInBatch = 0L;
                    tmpErrorRecordInBatch = numberRemainRecord;
                }
                numberSuccessRecord += tmpSuccessRecordInBatch;
                numberErrorRecord += tmpErrorRecordInBatch;
            }

            return numberSuccessRecord;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            System.out.println(ex.getMessage());
            throw ex;
        } finally {
            if (updateSim != null) {
                updateSim.close();
            }
        }
    }

    public void doValidate(ImportPartnerRequestDTO importPartnerRequestDTO) throws LogicException, Exception {
        //Cac truong bat buoc nhap
        if (DataUtil.isNullObject(importPartnerRequestDTO) || DataUtil.isNullOrEmpty(importPartnerRequestDTO.getListImportPartnerDetailDTOs())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.trans.search.validate.validStockTrans");
        }
        if (DataUtil.isNullObject(importPartnerRequestDTO.getToOwnerId())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.partner.update.sim.shop.require");
        }
        ShopDTO shopDTO = shopService.findOne(importPartnerRequestDTO.getToOwnerId());
        if (DataUtil.isNullObject(shopDTO)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.divide.shop.code.invalid", importPartnerRequestDTO.getToOwnerId());
        }
        //Cac truong khong hop le
        //Lay id mat hang
        ImportPartnerDetailDTO importPartnerDetail = importPartnerRequestDTO.getListImportPartnerDetailDTOs().get(0);
        if (DataUtil.isNullOrOneNavigate(importPartnerDetail.getProdOfferId())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.isdn.manage.create.field.validate.productOffer");
        }
        ProductOfferingDTO productOffering = productOfferingService.findOne(importPartnerDetail.getProdOfferId());
        if (DataUtil.isNullObject(productOffering)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.status.isdn.update.file.isdn.product.not.contains");
        }
    }

    public List<String> getErrorDetails(String sessionId, Long productOfferId) throws LogicException, Exception {
        List<String> listError = Lists.newArrayList();
        try {
            ProfileDTO profileDTO = productWs.getProfileByProductOfferId(productOfferId);
            //
            StringBuilder builder = new StringBuilder("select ");
            if (!DataUtil.isNullObject(profileDTO)) {
                for (String build : profileDTO.getLstColumnName()) {
                    builder.append(build);
                    builder.append(", ");
                }
            }
            builder.append("ora_err_mesg$");
            builder.append(" from ERR$_STOCK_SIM ");
            builder.append(" where ora_err_tag$ = #userSessionId");

            Query dQuery = em.createNativeQuery(builder.toString());
            dQuery.setParameter("userSessionId", sessionId);
            List lstObject = dQuery.getResultList();
            if (lstObject != null && !lstObject.isEmpty()) {
                for (Object object : lstObject) {
                    Object[] xyz = (Object[]) object;
                    StringBuilder sub = new StringBuilder("");
                    for (Object obj : xyz) {
                        if (!DataUtil.isNullOrEmpty(sub.toString())) {
                            sub.append(", ");
                        }
                        sub.append(DataUtil.safeToString(obj)).append("");
                    }
                    listError.add(sub.toString());
                }
            }
            return listError;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw ex;
        }
    }
}
