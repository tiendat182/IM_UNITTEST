package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.common.RevokeMessage;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.Date;
import java.util.List;

@WebService
public interface RevokeTransService {

    @WebMethod
    public RevokeTransDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<RevokeTransDTO> findAll() throws Exception;

    @WebMethod
    public List<RevokeTransDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public BaseMessage create(RevokeTransDTO productSpecCharacterDTO) throws Exception;

    @WebMethod
    public BaseMessage update(RevokeTransDTO productSpecCharacterDTO) throws Exception;

    public RevokeTransDTO save(RevokeTransDTO revokeTransDTO) throws Exception;

    public void createRevokeTrans(String strParams, Date createDate, String telecomSerivce, ShopDTO pos,
                                  Long stockTransId, List<StockWarrantyDTO> stockList, String contractNo,
                                  String accountId, List<StockWarrantyDTO> lstValidSerial, RevokeTransDTO revokeTrans,
                                  StaffDTO staff, Long revokeType) throws LogicException, Exception;

    public RevokeMessage revoke(String telecomSerivce, String posCodeVTN, String staffIdNo, List<StockWarrantyDTO> stockList,
                                String contractNo, String accountId) throws LogicException, Exception;

    public void saveAPDeploymentByIdNo(String staffIdNo, String account, String troubleType, List<StockTransDetailDTO> lstStockTransDetail, String transId) throws LogicException, Exception;

    /**
     * ham restore giao giao dich
     * @author thanhnt77
     * @param staffIdNo
     * @param account
     * @param troubleType
     * @param lstStockTransDetail
     * @throws LogicException
     * @throws Exception
     */
    public void restoreAPDeploymentByIdNo(String staffIdNo, String account, String troubleType, List<StockTransDetailDTO> lstStockTransDetail, String transId) throws LogicException, Exception;

    public void saveAPDeploymentByColl(String staffCode, List<StockTransDetailDTO> lstStockTransDetail) throws LogicException, Exception;

}