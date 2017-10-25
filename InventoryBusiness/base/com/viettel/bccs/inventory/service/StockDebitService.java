package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.StockDebitDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface StockDebitService {

    @WebMethod
    public StockDebitDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<StockDebitDTO> findAll() throws Exception;

    @WebMethod
    public List<StockDebitDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public BaseMessage create(StockDebitDTO productSpecCharacterDTO) throws Exception;

    @WebMethod
    public BaseMessage update(StockDebitDTO productSpecCharacterDTO) throws Exception;

    @WebMethod
    List<StockDebitDTO> findStockDebit(StockDebitDTO stockDebitDTO) throws Exception;

    @WebMethod
    public Long totalPriceStock(Long ownerId, String ownerType) throws Exception;

    /**
     * ham tra ve han muc kho
     *
     * @param ownerId
     * @return
     * @throws Exception
     * @author ThanhNT
     */
    @WebMethod
    public StockDebitDTO findStockDebitValue(Long ownerId, String ownerType) throws Exception;

    /**
     * @param stockTransId id cua giao dich kho
     * @return han muc kho/han muc kho mac dinh
     * @throws Exception
     * @desc ham xem han muc kho
     * @author LuanNT23
     */
    @WebMethod
    public StockDebitDTO findStockDebitByStockTrans(Long stockTransId) throws LogicException, Exception;

    /**
     * @param ownerId   id cua cua hang/ nhan vien  -- co the trong
     * @param ownerType loai doi tuong -- co the trong
     * @return danh sach stock_debit
     * @throws Exception
     * @throws LogicException
     */
    @WebMethod
    public List<StockDebitDTO> findStockDebitNative(Long ownerId, String ownerType) throws Exception, LogicException;


}