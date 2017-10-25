package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.BillStockDTO;
import com.viettel.bccs.inventory.dto.BillStockResultDTO;
import com.viettel.bccs.inventory.dto.OrderObjectDTO;
import com.viettel.fw.Exception.LogicException;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

/**
 * Created by hoangnt14 on 7/14/2016.
 */
@WebService
public interface LogisticService {

    @WebMethod
    public BillStockResultDTO createBill(List<BillStockDTO> lstBillStockDTO) throws LogicException, Exception;

    @WebMethod
    public BillStockResultDTO transStock(BillStockDTO billStockDTO) throws LogicException, Exception;

    @WebMethod
    public BillStockResultDTO cancelOrderOrBill(OrderObjectDTO orderObjectDTO) throws LogicException, Exception;
}
