package com.viettel.bccs.inventory.service;


import com.viettel.bccs.inventory.dto.PartnerShipmentWsDTO;
import com.viettel.bccs.inventory.dto.PartnerContractWsDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import java.util.List;

/**
 * @author thanhnt77
 */
public interface KttsService {

    /**
     * ham tim kiem kho tang tai san
     * @author thanhnt77
     * @param fromDate
     * @param toDate
     * @param contractCode
     * @return
     * @throws LogicException
     * @throws Exception
     */
    @WebMethod
    List<PartnerContractWsDTO> searchContractBCCS(@WebParam(name="fromDate") String fromDate,@WebParam(name="toDate") String toDate,@WebParam(name="contractCode") String contractCode) throws LogicException, Exception;

    /**
     * ham tim kiem chi tiet kho tang tai san
     * @author thanhnt77
     * @param contractCode
     * @return
     * @throws LogicException
     * @throws Exception
     */
    @WebMethod
    List<PartnerShipmentWsDTO> searchShipmentBCCS(@WebParam(name="contractCode") String contractCode) throws LogicException, Exception;

    /**
     * ham tim kiem chi tiet kho tang tai san
     * @author thanhnt77
     * @param reportKCSCode
     * @return
     * @throws LogicException
     * @throws Exception
     */
    @WebMethod
    BaseMessage getResultImpShipment(@WebParam(name="reportKCSCode") String reportKCSCode, @WebParam(name="bccsStatus")String bccsStatus) throws LogicException, Exception;

}