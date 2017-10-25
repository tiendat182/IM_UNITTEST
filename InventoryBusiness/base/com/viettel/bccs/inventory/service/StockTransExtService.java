package com.viettel.bccs.inventory.service;
import com.viettel.bccs.inventory.dto.StockTransExtDTO;
import com.viettel.bccs.inventory.model.StockTransExt;
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
public interface StockTransExtService {

    @WebMethod
    public StockTransExtDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<StockTransExtDTO> findAll() throws Exception;

    @WebMethod
    public List<StockTransExtDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public BaseMessage create(StockTransExtDTO productSpecCharacterDTO) throws Exception;
    @WebMethod
    public BaseMessage update(StockTransExtDTO productSpecCharacterDTO) throws Exception;

}