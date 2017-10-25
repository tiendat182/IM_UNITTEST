package com.viettel.bccs.inventory.wsesb.service;
import java.util.ArrayList;

import com.viettel.bccs.inventory.dto.ManageTransStockDto;
import com.viettel.bccs.inventory.dto.StockIsdnTransDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.data.*;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.fw.dto.BaseDTO;
import com.viettel.ws.common.utils.*;

import com.viettel.bccs.inventory.service.StockIsdnTransService;
import java.util.List;
import java.lang.Long;
import org.springframework.stereotype.Service;
import org.apache.log4j.Logger;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.springframework.beans.factory.annotation.Autowired;
import javax.annotation.PostConstruct;
import javax.jws.WebMethod;

import org.springframework.beans.factory.annotation.Qualifier;
@Service("WsStockIsdnTransServiceImpl")
public class WsStockIsdnTransServiceImpl implements StockIsdnTransService {

    public static final Logger logger = Logger.getLogger(WsStockIsdnTransServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private StockIsdnTransService client;

    @PostConstruct
    public void init() throws Exception {
      try{
         client = wsClientFactory.createWsClient(StockIsdnTransService.class);
      } catch (Exception ex) {
          logger.error("init", ex);
      }
    }

    @Override
    public StockIsdnTransDTO findOne(Long id) throws Exception  {
        return client.findOne(id);
    }

    @Override
    public List<StockIsdnTransDTO> findAll() throws Exception {
        return client.findAll();
    }

    @Override
    public List<StockIsdnTransDTO> findByFilter(List<FilterRequest> filterList) throws Exception  {
        return client.findByFilter(filterList);
    }

    @Override
    public Long count(List<FilterRequest> filterList) throws Exception {
        return client.count(filterList);
    }

    @Override
    public StockIsdnTransDTO create(StockIsdnTransDTO dto) throws Exception  {
        return client.create(dto);
    }

    @Override
    public BaseMessage update(StockIsdnTransDTO dto) throws Exception  {
        return client.update(dto);
    }

    @Override
    @WebMethod
    public List<StockIsdnTransDTO> search(ManageTransStockDto searchDTO) throws Exception {
        return client.search(searchDTO);
    }

    @Override
    @WebMethod
    public List<StockIsdnTransDTO> searchHistory(ManageTransStockDto searchDTO) throws LogicException, Exception {
        return client.searchHistory(searchDTO);
    }
}