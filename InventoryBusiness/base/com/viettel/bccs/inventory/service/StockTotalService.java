package com.viettel.bccs.inventory.service;

import com.viettel.bccs.im1.dto.StockTotalIm1DTO;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.ActionLogDTO;
import com.viettel.bccs.inventory.dto.ActionLogDetailDTO;
import com.viettel.bccs.inventory.dto.StockTotalDTO;
import com.viettel.bccs.inventory.dto.StockTotalMessage;
import com.viettel.bccs.inventory.dto.ws.StockTotalWsDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;
import org.springframework.transaction.annotation.Transactional;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.Date;
import java.util.List;

@WebService
public interface StockTotalService {

    @WebMethod
    public StockTotalDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<StockTotalDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<StockTotalDTO> searchStockTotal(StockTotalDTO stockTotalDTO) throws LogicException, Exception;

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public StockTotalDTO save(StockTotalDTO stockTotalDTO) throws Exception;

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public StockTotalDTO saveForProcessStockSerial(StockTotalDTO stockTotalDTO, ActionLogDTO dto, List<ActionLogDetailDTO> lstDetail) throws Exception;


    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public StockTotalMessage saveForProcessStockNoSerial(Long ownerId, Long ownerType, Long prodOfferId, Long stateId,
                                                         Long qty, Long qtyIssue, Long qtyHang, Long userId,
                                                         Long reasonId, Long transId, Date transDate,
                                                         String transCode, String transType, Const.SourceType sourceType, ActionLogDTO dto, List<ActionLogDetailDTO> lstDetail) throws Exception;

    /**
     * tong gia gia trij hang trong kho nhan
     *
     * @param ownerId
     * @return
     * @throws LogicException
     * @throws Exception
     * @author ThanhNT
     */
    public Long getTotalValueStock(Long ownerId, String ownerType) throws LogicException, Exception;


    @WebMethod
    public Long checkAction(StockTotalDTO stockTotalDTO) throws LogicException, Exception;

    public StockTotalMessage changeStockTotal(Long ownerId, Long ownerType, Long prodOfferId, Long stateId,
                                              Long qty, Long qtyIssue, Long qtyHang, Long userId,
                                              Long reasonId, Long transId, Date transDate,
                                              String transCode, String transType, Const.SourceType sourceType) throws LogicException, Exception;

    @WebMethod
    public List<StockTotalWsDTO> getStockTotalDetail(Long ownerId, Long ownerType, String prodOfferName) throws Exception;

    public StockTotalDTO getStockTotalForProcessStock(Long ownerId, Long ownerType, Long prodOfferId, Long stateId) throws Exception;

    public List<StockTotalDTO> getStockTotalAvailable(Long ownerType, Long productOfferTypeId) throws Exception;
	
	/**
     * ham tra ve tong so current_quantity, available_quantity
     * @author thanhnt77
     * @param prodOfferId
     * @param ownerId
     * @return
     * @throws Exception
     */
    public List<Long> getTotalQuantityStockTotal(Long prodOfferId, Long ownerId) throws Exception;

    @WebMethod
    public StockTotalIm1DTO getStockTotalForProcessStockIm1(Long ownerId, Long ownerType, Long prodOfferId, Long stateId) throws Exception;

    @WebMethod
    public boolean checkStockTotal(Long ownerId, Long prodOfferId, Long qty) throws Exception;

    @WebMethod
    public boolean checkStockTotalIm1(Long ownerId, Long prodOfferId, Long qty) throws Exception;

    @WebMethod
    public boolean checkHaveProductInStockTotal(Long ownerId, Long ownerType) throws Exception;

}