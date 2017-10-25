package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.BrasDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;


@WebService
public interface BrasService {

    @WebMethod
    public BrasDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<BrasDTO> findAll() throws Exception;

    @WebMethod
    public List<BrasDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public BaseMessage create(BrasDTO brasDTO) throws Exception;

    @WebMethod
    public BaseMessage update(BrasDTO brasDTO, String staffCode) throws Exception, LogicException;

    @WebMethod
    public BrasDTO createNewBras(BrasDTO brasDTO, String staffCode) throws Exception, LogicException;

    @WebMethod
    public BaseMessage delete(List<Long> lstId) throws Exception, LogicException;

    @WebMethod
    public List<BrasDTO> search(BrasDTO brasDTO) throws Exception, LogicException;

    @WebMethod
    public BrasDTO findByBrasIp(String ipBras) throws Exception, LogicException;
    @WebMethod
    public List<BrasDTO> findAllAction() throws Exception;
    @WebMethod
    public BrasDTO findByBrasId(Long BrasId) throws Exception, LogicException;

    @WebMethod
    public BrasDTO createBras(BrasDTO brasDTO, String staffCode) throws Exception, LogicException;



}