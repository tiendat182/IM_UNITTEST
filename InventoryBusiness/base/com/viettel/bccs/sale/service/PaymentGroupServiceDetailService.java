package com.viettel.bccs.sale.service;
import com.viettel.bccs.sale.dto.PaymentGroupServiceDetailDTO;
import com.viettel.bccs.sale.model.PaymentGroupServiceDetail;
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
public interface PaymentGroupServiceDetailService {

    @WebMethod
    public PaymentGroupServiceDetailDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<PaymentGroupServiceDetailDTO> findAll() throws Exception;

    @WebMethod
    public List<PaymentGroupServiceDetailDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public BaseMessage create(PaymentGroupServiceDetailDTO productSpecCharacterDTO) throws Exception;
    @WebMethod
    public BaseMessage update(PaymentGroupServiceDetailDTO productSpecCharacterDTO) throws Exception;

}