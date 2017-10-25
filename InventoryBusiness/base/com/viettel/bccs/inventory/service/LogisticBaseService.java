package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.BillStockDTO;
import com.viettel.bccs.inventory.dto.BillStockResultDTO;
import com.viettel.bccs.inventory.dto.OrderObjectDTO;
import com.viettel.bccs.inventory.dto.ResultLogisticsDTO;
import com.viettel.fw.Exception.LogicException;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

/**
 * Created by hoangnt14 on 7/19/2016.
 */
@WebService
public interface LogisticBaseService {
    @WebMethod
    public ResultLogisticsDTO createNote(String orderAction, BillStockDTO billStockDTO) throws LogicException, Exception;

    @WebMethod
    public ResultLogisticsDTO transStock(BillStockDTO billStockDTO) throws LogicException, Exception;

    @WebMethod
    public ResultLogisticsDTO cancelOrderOrBill(OrderObjectDTO orderObjectDTO) throws LogicException, Exception;

}
