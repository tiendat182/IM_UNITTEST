package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.BrasIppoolDTO;
import com.viettel.bccs.inventory.model.BrasIppool;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;


@WebService
public interface BrasIppoolService {

    @WebMethod
    public BrasIppoolDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<BrasIppoolDTO> findAll() throws Exception;

    @WebMethod
    public List<BrasIppoolDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public BaseMessage create(BrasIppoolDTO brasIppoolDTO) throws Exception, LogicException;

    @WebMethod
    public BaseMessage update(BrasIppoolDTO brasIppoolDTO, String staffCode) throws Exception, LogicException;

    @WebMethod
    public BaseMessage updateStatus(BrasIppoolDTO brasIppoolDTO, String staffCode) throws Exception, LogicException;

    @WebMethod
    public BaseMessage delete(List<Long> lstId) throws Exception, LogicException;
    @WebMethod
    public List<BrasIppoolDTO> search(BrasIppoolDTO brasIppoolDTO) throws Exception, LogicException;
    @WebMethod
    public List<BrasIppoolDTO> searchByIp(BrasIppoolDTO brasIppoolDTO)  throws Exception, LogicException;
    @WebMethod
    public boolean checkDulicateBrasIpPool(String ip) throws Exception, LogicException;

    /**
     * Them moi
     * author Tuydv1
     *
     * @param brasIppoolDTO
     * @return
     * @throws Exception
     * @throws LogicException
     */
    @WebMethod
    public BrasIppoolDTO createNewBrasIppool(BrasIppoolDTO brasIppoolDTO, String staffCode) throws Exception, LogicException;

    @WebMethod
    public boolean checkBrasIdExist(Long brasId) throws Exception, LogicException;

    /**
     * start service for Sale
     */
    @WebMethod
    public BaseMessage lockIppool(String ippool, Long shopId, Long staffId) throws Exception, LogicException;

    @WebMethod
    public BaseMessage unlockIppool(String ippool, Long shopId, Long staffId) throws Exception, LogicException;

    @WebMethod
    public List<String> getListStaticIPProvince(String domain, String province, Long ipType) throws Exception, LogicException;

    @WebMethod
    public List<String> getListStaticIpSpecialProvince(String domain, String province, Long ipType, Long specialType) throws Exception, LogicException;

    @WebMethod
    public BaseMessage registerIppool(String ippool, Long shopId, Long staffId) throws Exception, LogicException;

    @WebMethod
    public BaseMessage releaseIppool(String ippool, Long shopId, Long staffId) throws Exception, LogicException;
    /**
     * End service for sale
     */
    @WebMethod
    public BrasIppoolDTO getBrasViewIpFromCM(String ippool) throws Exception, LogicException;
}