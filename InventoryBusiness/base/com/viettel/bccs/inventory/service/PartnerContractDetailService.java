package com.viettel.bccs.inventory.service;
import com.viettel.bccs.inventory.dto.PartnerContractDetailDTO;
import com.viettel.bccs.inventory.model.PartnerContractDetail;
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
public interface PartnerContractDetailService {

    @WebMethod
    public PartnerContractDetailDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<PartnerContractDetailDTO> findAll() throws Exception;

    @WebMethod
    public List<PartnerContractDetailDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public BaseMessage create(PartnerContractDetailDTO productSpecCharacterDTO) throws Exception;
    @WebMethod
    public BaseMessage update(PartnerContractDetailDTO productSpecCharacterDTO) throws Exception;

    @WebMethod
    PartnerContractDetailDTO save(PartnerContractDetailDTO partnerContractDetailDTO) throws Exception;

}