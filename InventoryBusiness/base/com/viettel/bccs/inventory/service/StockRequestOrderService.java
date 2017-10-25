package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.StockRequestOrderDTO;
import com.viettel.bccs.inventory.model.StockRequestOrder;

import java.util.Date;
import java.util.List;

import com.viettel.fw.Exception.LogicException;
import org.primefaces.model.LazyDataModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.viettel.fw.common.util.extjs.FilterRequest;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.springframework.transaction.annotation.Transactional;
import com.viettel.fw.dto.BaseMessage;

@WebService
public interface StockRequestOrderService {

    @WebMethod
    public StockRequestOrderDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<StockRequestOrderDTO> findAll() throws Exception;

    @WebMethod
    public List<StockRequestOrderDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    public StockRequestOrderDTO save(StockRequestOrderDTO stockRequestOrderDTO) throws Exception;

    StockRequestOrderDTO update(StockRequestOrderDTO stockRequestOrderDTO) throws Exception;

    /**
     * ham xu ly tao yeu cau thu hoi hang tot
     *
     * @author thanhnt77
     */
    @WebMethod
    StockRequestOrderDTO createGoodOrderFromProvince(StockRequestOrderDTO stockRequestOrderDTO) throws LogicException, Exception;

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

    /**
     * ham phe duyet/tu choi yeu cau thu hoi hang tot
     *
     * @param stockRequestOrderDTO
     * @throws Exception
     * @author thanhnt77
     */
    public void approveStockRequestOrder(StockRequestOrderDTO stockRequestOrderDTO) throws LogicException, Exception;

    public List<StockRequestOrderDTO> getLstApproveAndSignVoffice() throws Exception;

    public void processCreateExpNote(Long stockRequestOrderId, Long fromOwnerId, Long toOwnerId) throws LogicException, Exception;

    public void processImportStock(Long stockTransId) throws LogicException, Exception;
}