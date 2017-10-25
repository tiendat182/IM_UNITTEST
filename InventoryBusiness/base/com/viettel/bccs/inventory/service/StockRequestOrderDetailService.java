package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.StockRequestOrderDetailDTO;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface StockRequestOrderDetailService {

    @WebMethod
    public StockRequestOrderDetailDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<StockRequestOrderDetailDTO> findAll() throws Exception;

    @WebMethod
    public List<StockRequestOrderDetailDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    StockRequestOrderDetailDTO save(StockRequestOrderDetailDTO stockRequestOrderDetailDTO) throws Exception;

    /**
     * tim kiem yeu cau dat hang
     *
     * @param stockRequestOrderId
     * @param status
     * @return
     * @throws Exception
     * @author thanhnt77
     */
    public List<StockRequestOrderDetailDTO> findSearchStockRequestOrder(Long stockRequestOrderId, String status) throws Exception;

    public List<StockRequestOrderDetailDTO> getLstByStockRequestId(Long stockRequestOrderId, Long fromOwnerId, Long toOwnerId) throws Exception;

    public List<StockRequestOrderDetailDTO> getOrderDetailByStockTransId(Long stockTransId) throws Exception;

    public List<StockRequestOrderDetailDTO> getLstDetailTemplate(Long stockRequestOrderId) throws Exception;

    public List<StockRequestOrderDetailDTO> getLstDetailToExport() throws Exception;

    public int updateCancelNote(Long stockRequestOrderId) throws Exception;
}