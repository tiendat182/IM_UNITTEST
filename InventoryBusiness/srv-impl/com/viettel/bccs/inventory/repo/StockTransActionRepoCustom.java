package com.viettel.bccs.inventory.repo;

import com.mysema.query.types.Predicate;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.model.StockTransAction;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;

import java.sql.Connection;
import java.util.Date;
import java.util.List;

public interface StockTransActionRepoCustom {
    public Predicate toPredicate(List<FilterRequest> filters);

    public Long getSequence() throws Exception;

    public StockTransAction findStockTransAction(StockTransActionDTO stockTransActionDTO);

    public void insertStockTransActionNative(StockTransActionDTO stockTransActionDTO) throws Exception;

    public List<StockTransAction> getStockTransActionByToOwnerId(Long toOnwerId, Long toOwnerType) throws Exception;

    public void updateActionCodeShop(Connection conn, String actionCodeShop, Long stockTransActionId) throws Exception;

    public int unlockUserInfo(Long stockTransActionId) throws Exception;

    public StockTransAction getStockTransActionByIdAndStatus(Long stockTransId, List<String> lstStatus) throws Exception;

    public int deleteStockTransActionByIdAndStatus(Long stockTransId, List<String> lstStatus) throws Exception;

    public List<StockDeliveringBillDetailDTO> getStockDeliveringResult(Date startTime, Date endTime) throws LogicException, Exception;

    /**
     * ham xu ly tim kiem gom kho dieu chuyen
     *
     * @param actionCode
     * @param ownerId
     * @param ownerType
     * @param ownerLoginId
     * @param ownerLoginType
     * @param fromDate
     * @param toDate
     * @param transType
     * @param typeOrderNote
     * @return
     * @throws LogicException
     * @throws Exception
     * @author thanhnt77
     */
    public List<VActionGoodsStatisticDTO> getListVActionStatistic(String actionCode, Long ownerId, Long ownerType, Long ownerLoginId,
                                                                  Long ownerLoginType, Date fromDate, Date toDate, String transType, String typeOrderNote) throws LogicException, Exception;

    public int deleteStockTransAction(Long stockTransActionId) throws Exception;

    public List<String> findListStockTransAction(VStockTransDTO vStockTransDTO);

}