package com.viettel.bccs.inventory.service;
import com.viettel.bccs.inventory.dto.StockSimInfoDTO;
import java.util.List;
import com.viettel.fw.common.util.extjs.FilterRequest;
import javax.jws.WebMethod;
import javax.jws.WebService;
@WebService
public interface StockSimInfoService {

    @WebMethod
    public StockSimInfoDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<StockSimInfoDTO> findByFilter(List<FilterRequest> filters) throws Exception;


}