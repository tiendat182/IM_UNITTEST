package com.viettel.bccs.inventory.service;

import com.viettel.bccs.im1.dto.StockModelIm1DTO;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.service.BaseServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;

/**
 * Created by DungPT16 on 7/14/2016.
 */
@Service
public class BankPlusServiceExecute extends BaseServiceImpl {

    public static final Logger logger = Logger.getLogger(BankPlusServiceExecute.class);

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM_LIB)
    private EntityManager emLib;

    @Autowired
    private StaffService staffService;
    @Autowired
    private StockTransService stockTransService;
    @Autowired
    private StockTransActionService stockTransActionService;
    @Autowired
    private StockTransDetailService stockTransDetailService;
    @Autowired
    private StockTransSerialService stockTransSerialService;
    @Autowired
    private OptionSetValueService optionSetValueService;

    /**
     * ham tru kho vaf cap nhap stockit
     *
     * @param fromOwnerId
     * @param fromOwnerType
     * @param prodOfferId
     * @param serial
     * @throws Exception
     * @author thanhnt77
     */
    @Transactional(rollbackFor = Exception.class)
    public void createTransBankplus(Long fromOwnerId, Long fromOwnerType, Long prodOfferId, String serial, String fromOwnerCode, Long staffId) throws LogicException, Exception {
        Date sysdate = getSysDate(em);

        Long actionStaffId = Const.OWNER_TYPE.SHOP_LONG.equals(fromOwnerType) ? staffId : fromOwnerId;

        String createUser = fromOwnerCode;
        if (Const.OWNER_TYPE.SHOP_LONG.equals(fromOwnerType) && actionStaffId != null) {
            StaffDTO staffDTO = staffService.findOne(actionStaffId);
            if (staffDTO != null) {
                createUser = staffDTO.getStaffCode();
            }
        }

        //tao giao dich
        //insert StockTrans
        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setFromOwnerId(fromOwnerId);
        stockTransDTO.setFromOwnerType(fromOwnerType);
        stockTransDTO.setToOwnerId(null);
        stockTransDTO.setToOwnerType(Const.OWNER_TYPE.CUSTOMER_LONG);
        stockTransDTO.setCreateDatetime(sysdate);
        stockTransDTO.setStockTransType(Const.STOCK_TRANS_TYPE.EXPORT);
        stockTransDTO.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
        StockTransDTO stockTransNew = stockTransService.save(stockTransDTO);

        //tao moi action status = 2
        StockTransActionDTO stockActionDTO = new StockTransActionDTO();
        stockActionDTO.setActionCode("PX_BANKPLUS_" + stockTransNew.getStockTransId());
        stockActionDTO.setStockTransId(stockTransNew.getStockTransId());
        stockActionDTO.setStatus(Const.STOCK_TRANS_STATUS.EXPORT_NOTE);
        stockActionDTO.setCreateUser(createUser);
        stockActionDTO.setCreateDatetime(sysdate);
        stockActionDTO.setActionStaffId(actionStaffId);
        stockTransActionService.save(stockActionDTO);

        //tao moi action status = 3
        stockActionDTO.setStatus(Const.STOCK_TRANS_STATUS.EXPORTED);
        stockActionDTO.setActionCode("");
        stockTransActionService.save(stockActionDTO);

        //tao action status = 6
        stockActionDTO.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
        stockTransActionService.save(stockActionDTO);

        //insert detail
        StockTransDetailDTO detailDTO = new StockTransDetailDTO();
        detailDTO.setStockTransId(stockTransNew.getStockTransId());
        detailDTO.setProdOfferId(prodOfferId);
        detailDTO.setStateId(Const.GOODS_STATE.NEW);
        detailDTO.setQuantity(1L);
        detailDTO.setCreateDatetime(sysdate);
        StockTransDetailDTO detailInsert = stockTransDetailService.save(detailDTO);

        //insert serial
        StockTransSerialDTO stockSerialNew = new StockTransSerialDTO();
        stockSerialNew.setProdOfferId(prodOfferId);
        stockSerialNew.setStateId(Const.GOODS_STATE.NEW);
        stockSerialNew.setStockTransId(stockTransNew.getStockTransId());
        stockSerialNew.setCreateDatetime(sysdate);
        stockSerialNew.setStockTransDetailId(detailInsert.getStockTransDetailId());
        stockSerialNew.setFromSerial(serial);
        stockSerialNew.setToSerial(serial);
        stockSerialNew.setQuantity(1L);
        stockTransSerialService.save(stockSerialNew);

        //tru kho stock_total
        debitTotal(fromOwnerId, fromOwnerType, prodOfferId, Const.GOODS_STATE.NEW);
        //update mat hang stock_kit
        updateStockKit(serial);

        List<OptionSetValueDTO> lstConfigEnableBccs1 = optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1");
        if (!DataUtil.isNullOrEmpty(lstConfigEnableBccs1) && !DataUtil.isNullObject(lstConfigEnableBccs1.get(0).getValue())) {
            if (Const.ENABLE_UPDATE_BCCS1.equals(DataUtil.safeToLong(lstConfigEnableBccs1.get(0).getValue()))) {
                System.out.println("----da vao---");
                //tru kho stock_total
                debitTotalIm1(fromOwnerId, fromOwnerType, prodOfferId, Const.GOODS_STATE.NEW);
                //update mat hang stock_kit
                updateStockKitIm1(serial);
            }
        }
    }

    /**
     * ham tru kho
     *
     * @param ownerId
     * @param ownerType
     * @param prodOfferId
     * @param stateId
     * @throws Exception
     * @author thanhnt77
     */
    private void debitTotal(Long ownerId, Long ownerType, Long prodOfferId, Long stateId) throws Exception {
        String sql = " UPDATE stock_total  SET current_quantity  = current_quantity  - 1, available_quantity  = available_quantity - 1 " +
                "WHERE owner_id = #ownerId AND owner_type = #onwerType AND prod_offer_id = #prodOfferId AND state_id = #stateId AND current_quantity > 0 AND available_quantity > 0";
        Query queryObject = em.createNativeQuery(sql);
        queryObject.setParameter("ownerId", ownerId);
        queryObject.setParameter("onwerType", ownerType);
        queryObject.setParameter("prodOfferId", prodOfferId);
        queryObject.setParameter("stateId", stateId);
        int result = queryObject.executeUpdate();
        if (result == 0) {
            throw new LogicException("", "ERR008");
        }
    }

    /**
     * ham tru kho
     *
     * @param ownerId
     * @param ownerType
     * @param prodOfferId
     * @param stateId
     * @throws Exception
     * @author thanhnt77
     */
    private void debitTotalIm1(Long ownerId, Long ownerType, Long prodOfferId, Long stateId) throws Exception {
        String sql = " UPDATE bccs_im.stock_total  SET quantity = quantity - 1, quantity_issue = quantity_issue - 1 " +
                "WHERE owner_id = ? AND owner_type = ? AND stock_model_id = ? AND state_id = ? AND quantity > 0 AND quantity_issue > 0";
        Query queryObject = emLib.createNativeQuery(sql);
        queryObject.setParameter(1, ownerId);
        queryObject.setParameter(2, ownerType);
        queryObject.setParameter(3, prodOfferId);
        queryObject.setParameter(4, stateId);
        int result = queryObject.executeUpdate();
        if (result == 0) {
            throw new LogicException("", "ERR008");
        }
    }

    /**
     * tim kim stockModel cua im1 theo modelCode
     *
     * @param stockModelCode
     * @return
     * @author thanhnt77
     */
    public StockModelIm1DTO getStockModelIm1DTO(String stockModelCode) {
        String sql = "select stock_model_id,stock_model_code,stock_type_id,name,check_serial,status" +
                " from bccs_im.stock_model where stock_model_code=#stockModelCode ";
        Query queryObject = emLib.createNativeQuery(sql);
        queryObject.setParameter("stockModelCode", stockModelCode);
        List<Object[]> lsData = queryObject.getResultList();
        if (!DataUtil.isNullOrEmpty(lsData)) {
            int index = 0;
            Object[] obj = lsData.get(0);
            StockModelIm1DTO stockModelIm1DTO = new StockModelIm1DTO();
            stockModelIm1DTO.setStockModelId(DataUtil.safeToLong(obj[index++]));
            stockModelIm1DTO.setStockModelCode(DataUtil.safeToString(obj[index++]));
            stockModelIm1DTO.setStockTypeId(DataUtil.safeToLong(obj[index++]));
            stockModelIm1DTO.setName(DataUtil.safeToString(obj[index++]));
            stockModelIm1DTO.setCheckSerial(DataUtil.safeToLong(obj[index++]));
            stockModelIm1DTO.setStatus(DataUtil.safeToLong(obj[index]));
            return stockModelIm1DTO;
        }
        return null;
    }

    /**
     * tim kiem stockitIm1
     *
     * @param serial
     * @return
     * @author thanhnt77
     */
    public StockKitIm1DTO getStockKitIm1BySerial(String serial) {
        String queryString = " select a.id, a.stock_model_id, a.owner_id, a.owner_type, a.serial, a.state_id,a.status " +
                " from bccs_im.stock_kit a where 1 = 1  and to_number(a.serial) = to_number(#serial)";
        Query query = emLib.createNativeQuery(queryString);
        query.setParameter("serial", serial);
        List<Object[]> list = query.getResultList();
        if (!DataUtil.isNullOrEmpty(list)) {
            Object[] obj = list.get(0);
            int index = 0;
            StockKitIm1DTO stockKitDTO = new StockKitIm1DTO();
            stockKitDTO.setId(DataUtil.safeToLong(obj[index++]));
            stockKitDTO.setStockModelId(DataUtil.safeToLong(obj[index++]));
            stockKitDTO.setOwnerId(DataUtil.safeToLong(obj[index++]));
            stockKitDTO.setOwnerType(DataUtil.safeToLong(obj[index++]));
            stockKitDTO.setSerial(DataUtil.safeToString(obj[index++]));
            stockKitDTO.setStateId(DataUtil.safeToLong(obj[index++]));
            stockKitDTO.setStatus(DataUtil.safeToLong(obj[index]));
            return stockKitDTO;
        }
        return null;
    }

    /**
     * ham update stock_kit
     *
     * @param serial
     * @throws Exception
     * @author thanhnt77
     */
    private void updateStockKit(String serial) throws Exception {
        String queryString = " UPDATE stock_kit  SET status = 0  WHERE to_number(serial) = to_number(#serial) and status = 1 ";

        Query queryObject = em.createNativeQuery(queryString);
        queryObject.setParameter("serial", serial);
        int result = queryObject.executeUpdate();
        if (result == 0) {
            throw new LogicException("", "ERR009");
        }
    }

    /**
     * ham update stock_kit
     *
     * @param serial
     * @throws Exception
     * @author thanhnt77
     */
    private void updateStockKitIm1(String serial) throws Exception {
        String queryString = " UPDATE bccs_im.stock_kit  SET status = 0  WHERE to_number(serial) = to_number(?) and status = 1 ";

        Query queryObject = emLib.createNativeQuery(queryString);
        queryObject.setParameter(1, serial);
        int result = queryObject.executeUpdate();
        if (result == 0) {
            throw new LogicException("", "ERR009");
        }
    }
}

