package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.KcsDTO;
import com.viettel.bccs.inventory.dto.ProductInStockDTO;
import com.viettel.bccs.inventory.dto.StockTotalMessage;
import com.viettel.bccs.inventory.model.KcsLog;
import com.viettel.bccs.inventory.model.KcsLogDetail;
import com.viettel.bccs.inventory.model.KcsLogSerial;
import com.viettel.bccs.inventory.model.StockHandset;
import com.viettel.bccs.inventory.repo.*;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.fw.service.BaseServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.jws.WebMethod;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Hellpoethero on 06/09/2016.
 */

@Service
public class KcsServiceImpl extends BaseServiceImpl implements KcsService {
    public final static Logger logger = Logger.getLogger(KcsService.class);

    @Autowired
    private KcsLogRepo kcsLogRepo;
    @Autowired
    private KcsLogDetailRepo kcsLogDetailRepo;
    @Autowired
    private KcsLogSerialRepo kcsLogSerialRepo;
    @Autowired
    private StockTotalService stockTotalService;
    @Autowired
    private ProductOfferingService productOfferingService;
    @Autowired
    private StockHandsetRepo stockHandsetRepo;
    @Autowired
    private StaffRepo staffRepo;
    @Autowired
    private ShopRepo shopRepo;

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @WebMethod
    @Override
    public List<ProductInStockDTO> findProductInStock(String code, String serial) throws Exception {
        if (!DataUtil.isNullOrEmpty(serial) && !DataUtil.isNullOrEmpty(code)) {
            return kcsLogRepo.findProductInStock(code, serial);

        }
        return null;
    }

    @Override
    public List<ProductInStockDTO> findProductInStockIm1(String code, String serial) throws Exception {
        if (!DataUtil.isNullOrEmpty(serial) && !DataUtil.isNullOrEmpty(code)) {
            return kcsLogRepo.findProductInStockIm1(code, serial);
        }
        return null;
    }

    /*@WebMethod
    @Override
    public BaseMessage insertKcsLogDetail(KcsLogDetail kcsLogDetail) throws Exception {
        BaseMessage message = new BaseMessage(false);
        try {
            if (kcsLogDetail.getKcsLogId() != null && kcsLogDetail.getQuantity() != null
                    && kcsLogDetail.getCreateDate() != null && kcsLogDetail.getStatus() != null
                    && kcsLogDetail.getStateId() != null && kcsLogDetail.getStockModelId() != null) {
                kcsLogDetailRepo.save(kcsLogDetail);
                message.setSuccess(true);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return message;
    }*/

    /*@WebMethod
    @Override
    public BaseMessage insertKcsLogSerial(KcsLogSerial kcsLogSerial) throws Exception {
        BaseMessage message = new BaseMessage(false);
        try {
            if (kcsLogSerial.getSerial() != null && kcsLogSerial.getKcsLogDetailId() != null
                    && kcsLogSerial.getCreateDate() != null) {
                kcsLogSerialRepo.save(kcsLogSerial);
                message.setSuccess(true);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return message;
    }

    @WebMethod
    @Override
    public BaseMessage insertKcsLog(KcsLog kcsLog) throws Exception {
        BaseMessage message = new BaseMessage(false);
        try {
            if (kcsLog.getOwnerId() != null && kcsLog.getOwnerType() != null
                    && kcsLog.getCreateDate() != null && kcsLog.getStatus() != null) {
                kcsLogRepo.save(kcsLog);
                message.setSuccess(true);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return message;
    }*/

    @Override
    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage importKcs(Long userId, Long ownerId, HashMap<String, KcsDTO> mapKCS) throws Exception {
        BaseMessage message = new BaseMessage(false);
        if (userId == null || DataUtil.isNullOrEmpty(mapKCS) || ownerId == null) {
            message.setDescription("view.warehouse.import.kcs.null.ownerId");
        } else {
            Long ownerShopId = staffRepo.findOne(ownerId).getShopId();
            Long userShopId = staffRepo.findOne(userId).getShopId();
            String userShopPath = shopRepo.findOne(userShopId).getShopPath();
            String ownerShopPath = shopRepo.findOne(ownerShopId).getShopPath();
            if (ownerShopPath.contains(userShopPath)) {
                executeImportKcs(userId, ownerId, mapKCS, message);
                message.setSuccess(true);
            } else {
                message.setDescription("view.warehouse.import.kcs.user.not.permission");
            }
        }
        return message;
    }

    public void executeImportKcs(Long userId, Long ownerId, HashMap<String, KcsDTO> mapKCS, BaseMessage message) throws Exception {
        KcsLog kcsLog = new KcsLog();
        kcsLog.setOwnerId(userId);
        kcsLog.setOwnerType(Const.OWNER_TYPE_STAFF);
        kcsLog.setCreateDate(getSysDate(em));
        kcsLog.setStatus(Long.valueOf(Const.STATUS_ACTIVE));
        kcsLogRepo.save(kcsLog);

        Long kcsLogId = kcsLog.getKcsLogId();
        Iterator<String> iterator = mapKCS.keySet().iterator();
        while (iterator.hasNext()) {
            Long qtyDamage = 0L;
            Long qtyGood = 0L;

            String code = iterator.next();
            Long prodOfferId = productOfferingService.getProductByCode(code).getProductOfferingId();
            KcsDTO kcsDTO = mapKCS.get(code);

            List<String[]> serialList = new ArrayList<String[]>();
            for (int i = 0; i < kcsDTO.getSerialList().size(); i++) {
                String[] value = new String[2];
                value[0] = kcsDTO.getSerialList().get(i);
                value[1] = kcsDTO.getStateIdList().get(i).toString();
                serialList.add(value);
                if (value[1].equals(Const.STATE_DAMAGE.toString())) {
                    qtyDamage++;
                } else if (value[1].equals(Const.STATE_GOOD.toString())) {
                    qtyGood++;
                } else {
                    throw new Exception();
                }
            }

            BaseMessage updateSerialKcsMessage = updateSerialKCS(ownerId, kcsLogId, prodOfferId, serialList);
            if (!updateSerialKcsMessage.isSuccess()) {
                message.setDescription(updateSerialKcsMessage.getDescription());
                throw new Exception();
            }

            Long quantity = Long.valueOf(serialList.size());
            StockTotalMessage changeStockTotalMessage;
            if (qtyDamage!=0) {
                changeStockTotalMessage = stockTotalService.changeStockTotal(ownerId, Const.OWNER_TYPE_STAFF, prodOfferId, Const.STATE_DAMAGE,
                        qtyDamage, qtyDamage, 0L, userId, Const.STOCK_TRANS.IMPORT_STOCK_FROM_PARTNER_REASON_ID, 0L,getSysDate(em), "GD_KCS",
                        Const.TRANS_IMPORT.toString(), Const.SourceType.STOCK_TRANS);
                if (!changeStockTotalMessage.isSuccess()) {
                    message.setDescription(changeStockTotalMessage.getDescription());
                    throw new Exception();
                }
            }

            if (qtyGood!=0) {
                changeStockTotalMessage = stockTotalService.changeStockTotal(ownerId, Const.OWNER_TYPE_STAFF, prodOfferId, Const.STATE_GOOD,
                        qtyGood, qtyGood, 0L, userId, Const.STOCK_TRANS.IMPORT_STOCK_FROM_PARTNER_REASON_ID, 0L,getSysDate(em), "GD_KCS",
                        Const.TRANS_IMPORT.toString(), Const.SourceType.STOCK_TRANS);
                if (!changeStockTotalMessage.isSuccess()) {
                    message.setDescription(changeStockTotalMessage.getDescription());
                    throw new Exception();
                }
            }
            changeStockTotalMessage = stockTotalService.changeStockTotal(ownerId, Const.OWNER_TYPE_STAFF, prodOfferId, Const.STATE_EXPIRE,
                    -quantity, -quantity, 0L, userId, Const.STOCK_TRANS.EXPORT_STOCK_FROM_PARTNER_REASON_ID, 0L, getSysDate(em), "GD_KCS",
                    Const.TRANS_IMPORT.toString(), Const.SourceType.STOCK_TRANS);
            if (!changeStockTotalMessage.isSuccess()) {
                message.setDescription(changeStockTotalMessage.getDescription());
                throw new Exception();
            }
        }
        message.setSuccess(true);
    }

    public BaseMessage updateSerialKCS(Long ownerId, Long kcsLogId, Long prodOfferId, List<String[]> serialList) throws Exception {
        BaseMessage message = new BaseMessage(false);
        if (prodOfferId == null || DataUtil.isNullOrEmpty(serialList) || ownerId == null || kcsLogId == null) {
            return message;
        }
        KcsLogDetail kcsLogDetail = new KcsLogDetail();
        //try {
        kcsLogDetail.setKcsLogId(kcsLogId);
        kcsLogDetail.setCreateDate(getSysDate(em));
        kcsLogDetail.setStockModelId(prodOfferId);
        kcsLogDetail.setQuantity((long) serialList.size());
        kcsLogDetail.setStateId(Const.STATE_EXPIRE);
        kcsLogDetail.setStatus(Long.valueOf(Const.STATUS_ACTIVE));
        kcsLogDetailRepo.save(kcsLogDetail);

       /* } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return message;
        }*/

        Long kcsLogDetailId = kcsLogDetail.getKcsLogDetailId();
        // insert kcs_log_serial
        for (int i = 0; i < serialList.size(); i++) {
            String serial = serialList.get(i)[0];
            Long stateId = Long.valueOf(serialList.get(i)[1]);
            StockHandset stockHandset = stockHandsetRepo.findBySerialForUpdate(serial, prodOfferId, ownerId);
            if (stockHandset != null && stateId != null && (stateId == Const.STATE_DAMAGE || stateId == Const.STATE_GOOD)) {
                //try {
                if (stockHandsetRepo.updateForStockRescue(stateId, ownerId, serial, prodOfferId)==0) {
                    return message;
                }
                if (stockHandsetRepo.updateForStockRescueIM1(stateId, ownerId, serial, prodOfferId)==0) {
                    return message;
                }
                /*} catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    return message;
                }*/
                //try {
                KcsLogSerial kcsLogSerial = new KcsLogSerial();
                kcsLogSerial.setKcsLogDetailId(kcsLogDetailId);
                kcsLogSerial.setCreateDate(getSysDate(em));
                kcsLogSerial.setSerial(serial);
                kcsLogSerialRepo.save(kcsLogSerial);
                /*} catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    return message;
                }*/
            } else {
                return message;
            }
        }
        message.setSuccess(true);
        return message;
    }
}
