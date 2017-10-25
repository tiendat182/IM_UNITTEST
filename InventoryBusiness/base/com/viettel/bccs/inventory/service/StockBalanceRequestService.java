package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.ProductOfferingTotalDTO;
import com.viettel.bccs.inventory.dto.StockBalanceDetailDTO;
import com.viettel.bccs.inventory.dto.StockBalanceRequestDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface StockBalanceRequestService {

    @WebMethod
    public StockBalanceRequestDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<StockBalanceRequestDTO> findAll() throws Exception;

    @WebMethod
    public List<StockBalanceRequestDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public BaseMessage create(StockBalanceRequestDTO productSpecCharacterDTO) throws Exception;

    @WebMethod
    public StockBalanceRequestDTO save(StockBalanceRequestDTO stockBalanceRequestDTO) throws Exception;

    @WebMethod
    public List<StockBalanceRequestDTO> searchStockBalanceRequest(StockBalanceRequestDTO stockBalanceRequestDTO) throws LogicException, Exception;

    @WebMethod
    public void saveStockBalanceRequest(StockBalanceRequestDTO stockBalanceRequestDTO,
                                        List<StockBalanceDetailDTO> lstDetailDTO) throws LogicException, Exception;

    @WebMethod
    public void validateStockBalanceRequest(StockBalanceRequestDTO stockBalanceRequestDTO,
                                            List<StockBalanceDetailDTO> lstDetailDTO) throws LogicException, Exception;

    @WebMethod
    public List<ProductOfferingTotalDTO> getProductOfferingExport(Long ownerId, Long ownerType, Long prodOfferId) throws LogicException, Exception;

    @WebMethod
    public List<ProductOfferingTotalDTO> getProductOfferingImport(Long ownerType, Long ownerId, Long prodOfferId) throws Exception;

    @WebMethod
    public void doApproveStockBalanceRequest(StockBalanceRequestDTO stockBalanceRequestDTO) throws LogicException, Exception;

    @WebMethod
    public Long getMaxId() throws LogicException, Exception;

    @WebMethod
    public BaseMessage update(StockBalanceRequestDTO productSpecCharacterDTO) throws Exception;

    @WebMethod
    public void updateRequest(StockBalanceRequestDTO stockBalanceRequestDTO) throws LogicException, Exception;


}