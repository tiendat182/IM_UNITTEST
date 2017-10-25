package com.viettel.bccs.product.service;


import com.viettel.bccs.product.dto.SaleProgramDTO;
import com.viettel.fw.common.util.extjs.FilterRequest;

import javax.jws.WebMethod;
import java.util.List;

/**
 * Created by PM2-LAMNV5 on 07/12/2015.
 */
public interface SaleProgramService {
    @WebMethod
    SaleProgramDTO findByCode(String code) throws Exception;

    @WebMethod
    SaleProgramDTO findOne(Long id) throws Exception;

    @WebMethod
    Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    List<SaleProgramDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    List<SaleProgramDTO> findAllSalesProgramActive() throws Exception;
}
