package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.AreaDTO;
import com.viettel.bccs.inventory.dto.StockTransDTO;
import com.viettel.bccs.inventory.dto.StockTransDetailDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.RequiredRoleMap;
import org.springframework.transaction.annotation.Transactional;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

/**
 * Created by Dungha7 on 9/27/2016.
 */
@WebService
public interface ReturnStockService {

    @WebMethod
    StockTransDTO saveReturnStock(StockTransDTO stockTransDTO, List<StockTransDetailDTO> listTransDetailDTOs, RequiredRoleMap requiredRoleMap) throws Exception, LogicException;

}
