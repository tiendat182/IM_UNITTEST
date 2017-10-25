package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.dto.StockRequestOrderDTO;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;

import java.util.Date;
import java.util.List;

import com.viettel.bccs.inventory.model.StockRequestOrder;

public interface StockRequestOrderRepoCustom {
    public Predicate toPredicate(List<FilterRequest> filters);

    /**
     * ham xu ly tim kiem lap yeu cau
     *
     * @param orderCode
     * @param fromDate
     * @param endDate
     * @param status
     * @param ownerId
     * @param ownerType
     * @return
     * @throws Exception
     * @author thanhnt77
     */
    List<StockRequestOrderDTO> findSearhStockOrder(String orderCode, Date fromDate, Date endDate, String status, Long ownerId, Long ownerType) throws Exception;

    public List<StockRequestOrderDTO> getLstApproveAndSignVoffice() throws Exception;

}