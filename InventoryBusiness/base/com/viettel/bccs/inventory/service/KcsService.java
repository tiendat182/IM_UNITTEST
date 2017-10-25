package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.KcsDTO;
import com.viettel.bccs.inventory.dto.ProductInStockDTO;
import com.viettel.bccs.inventory.repo.KcsLogRepo;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Hellpoethero on 07/09/2016.
 */
@WebService
public interface KcsService {
    @WebMethod
    List<ProductInStockDTO> findProductInStock(String code, String serial) throws Exception;

    @WebMethod
    List<ProductInStockDTO> findProductInStockIm1(String code, String serial) throws Exception;

    /*@WebMethod
    BaseMessage insertKcsLogDetail(KcsLogDetail kcsLogDetail) throws Exception;*/

/*    @WebMethod
    BaseMessage insertKcsLogSerial(KcsLogSerial kcsLogSerial) throws Exception;*/

/*    @WebMethod
    BaseMessage insertKcsLog(KcsLog kcsLog) throws Exception;*/

    @WebMethod
    BaseMessage importKcs(Long userId, Long ownerId, HashMap<String, KcsDTO> mapKCS) throws Exception;
}
