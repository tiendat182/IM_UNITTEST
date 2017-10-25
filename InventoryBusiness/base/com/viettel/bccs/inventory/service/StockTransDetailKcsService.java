package com.viettel.bccs.inventory.service;
import com.viettel.bccs.inventory.dto.StockTransDetailKcsDTO;
import com.viettel.bccs.inventory.model.StockTransDetailKcs;
import java.util.List;
import org.primefaces.model.LazyDataModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.viettel.fw.common.util.extjs.FilterRequest;
import javax.jws.WebMethod;
import javax.jws.WebService;
import org.springframework.transaction.annotation.Transactional;
import com.viettel.fw.dto.BaseMessage;
@WebService
public interface StockTransDetailKcsService {

    @WebMethod
    public StockTransDetailKcsDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<StockTransDetailKcsDTO> findAll() throws Exception;

    @WebMethod
    public List<StockTransDetailKcsDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public BaseMessage create(StockTransDetailKcsDTO productSpecCharacterDTO) throws Exception;
    @WebMethod
    public BaseMessage update(StockTransDetailKcsDTO productSpecCharacterDTO) throws Exception;

    @WebMethod
    public StockTransDetailKcsDTO save (StockTransDetailKcsDTO stockTransDetailKcsDTO) throws Exception;

}