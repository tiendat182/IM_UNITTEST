package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.ProductOfferingDTO;
import com.viettel.bccs.inventory.dto.StockTransDetailDTO;
import com.viettel.bccs.inventory.dto.StockTransInputDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

/**
 * Created by tuydv1 on 1/13/2016.
 */
@WebService
public interface RescueInformationExportStockService {
    /**
     * Desciption: Ham thuc bat validate du lieu vao
     * tuydv1
     *
     */
    @WebMethod
    public BaseMessage validateStockTransForExport(Long productOfferingId, String state, String unit,
                                                   Long avaiableQuantity, String quantity,String serialRecover  ) throws Exception,LogicException;
    /**
     * Desciption: Ham thuc hien Xuat thu hoi
     * tuydv1
     *
     */
    @WebMethod
    public BaseMessage executeStockTransForExport(StockTransInputDTO transInputDTO, String shopCode,
                                                  ProductOfferingDTO stockRecoverCode, String strSerialRecover,List<StockTransDetailDTO> listStockTransDetailDTO, boolean render) throws Exception,LogicException;
}
