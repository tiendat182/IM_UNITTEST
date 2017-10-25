package com.viettel.bccs.inventory.service;
import com.viettel.bccs.inventory.dto.RevokeKitTransDTO;
import com.viettel.bccs.inventory.model.RevokeKitTrans;
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
public interface RevokeKitTransService {

    @WebMethod
    public RevokeKitTransDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public RevokeKitTransDTO save(RevokeKitTransDTO dto) throws LogicException, Exception;

}