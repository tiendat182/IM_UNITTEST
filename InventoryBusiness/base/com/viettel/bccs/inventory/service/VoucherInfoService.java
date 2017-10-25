package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.VoucherInfoDTO;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.List;

/**
 * Created by HoangAnh on 9/1/2016.
 */
@WebService
public interface VoucherInfoService {

    @WebMethod
    List<VoucherInfoDTO> findAll();

    @WebMethod
    BaseMessage insertListVoucherInfo(@WebParam(name = "voucherInfoDTOs") List<VoucherInfoDTO> voucherInfoDTOs);

    @WebMethod
    VoucherInfoDTO findBySerial(@WebParam(name = "serial") String serial);

    @WebMethod
    BaseMessage deleteVoucherInfo(Long id);
}
