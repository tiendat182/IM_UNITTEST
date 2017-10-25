package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.ChangeDamagedProductDTO;
import com.viettel.bccs.inventory.dto.ErrorChangeProductDTO;
import com.viettel.bccs.inventory.dto.StaffDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface ChangeDamagedProductService {

    @WebMethod
    public List<ChangeDamagedProductDTO> checkNewSerialInput(String newSerial, Long staffId) throws LogicException, Exception;

    @WebMethod
    public BaseMessage saveChangeDamagedProduct(Long productOfferTypeId, Long prodOfferId, Long reasonId, List<ChangeDamagedProductDTO> lstChangeProduct, Long staffId) throws LogicException, Exception;

    @WebMethod
    public List<ErrorChangeProductDTO> saveChangeDamagedProductFile(Long productOfferTypeId, Long prodOfferId, Long reasonId, List<ChangeDamagedProductDTO> lstChangeProduct, Long staffId) throws LogicException, Exception;
}