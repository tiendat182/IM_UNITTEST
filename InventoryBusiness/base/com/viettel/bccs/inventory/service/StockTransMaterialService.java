package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.StockTransDetailDTO;
import com.viettel.bccs.inventory.dto.VStockTransDTO;
import com.viettel.fw.Exception.LogicException;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface StockTransMaterialService {

    /**
     * ham xu ly xuat vat tu trien khai
     * @author thanhnt77
     * @param vStockTransDTO
     * @return
     * @throws Exception
     */
    @WebMethod
    public void excuteCreateTransMaterial(VStockTransDTO vStockTransDTO) throws LogicException, Exception;


    /**
     * ham xu ly xuat kho qua tang
     * @author thanhnt77
     * @param lsTransDetailDTOs
     * @throws LogicException
     * @throws Exception
     */
    public String excuteCreateTransGift(String staffCode, List<StockTransDetailDTO> lsTransDetailDTOs) throws LogicException, Exception;


    /**
     * ham validate so luong hang ton trong kho
     * @author thanhnt77
     * @param staffCode
     * @param lsTransDetailDTOs
     * @throws LogicException
     * @throws Exception
     */
    public void validateStockTotal(String staffCode, List<StockTransDetailDTO> lsTransDetailDTOs) throws LogicException, Exception;

}