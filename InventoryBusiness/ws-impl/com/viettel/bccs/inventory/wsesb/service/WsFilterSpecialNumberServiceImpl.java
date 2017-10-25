package com.viettel.bccs.inventory.wsesb.service;
import java.math.BigDecimal;
import java.util.ArrayList;

import com.viettel.bccs.inventory.dto.FilterSpecialNumberDTO;
import com.viettel.bccs.inventory.dto.NumberFilterRuleDTO;
import com.viettel.bccs.inventory.dto.StockNumberDTO;
import com.viettel.bccs.inventory.service.NumberFilterRuleService;
import com.viettel.bccs.inventory.service.StockNumberService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.data.*;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.fw.dto.BaseDTO;
import com.viettel.ws.common.utils.*;

import com.viettel.bccs.inventory.service.FilterSpecialNumberService;
import java.util.List;
import java.lang.Long;
import org.springframework.stereotype.Service;
import org.apache.log4j.Logger;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.springframework.beans.factory.annotation.Autowired;
import javax.annotation.PostConstruct;
import javax.jws.WebMethod;

import org.springframework.beans.factory.annotation.Qualifier;
@Service("WsFilterSpecialNumberServiceImpl")
public class WsFilterSpecialNumberServiceImpl implements FilterSpecialNumberService {

    public static final Logger logger = Logger.getLogger(WsFilterSpecialNumberServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private FilterSpecialNumberService client;

    @PostConstruct
    public void init() throws Exception {
      try{
         client = wsClientFactory.createWsClient(FilterSpecialNumberService.class);
      } catch (Exception ex) {
          logger.error("init", ex);
      }
    }

    @Override
    public FilterSpecialNumberDTO findOne(Long id) throws Exception  {
        return client.findOne(id);
    }

    @Override
    public List<FilterSpecialNumberDTO> findAll() throws Exception {
        return client.findAll();
    }

    @Override
    public List<FilterSpecialNumberDTO> findByFilter(List<FilterRequest> filterList) throws Exception  {
        return client.findByFilter(filterList);
    }

    @Override
    public Long count(List<FilterRequest> filterList) throws Exception {
        return client.count(filterList);
    }

    @Override
    public BaseMessage create(FilterSpecialNumberDTO dto) throws Exception  {
        return client.create(dto);
    }

    @Override
    public BaseMessage update(FilterSpecialNumberDTO dto) throws Exception  {
        return client.update(dto);
    }

    @Override
    public BaseMessage insertBatch(List<StockNumberDTO> listNumber) throws Exception{
        return client.insertBatch(listNumber);
    }

    @Override
    public BaseMessage filterNumber(String userCode, boolean refilter, Long minNumber, List<NumberFilterRuleDTO> lstSelectedRule, Long ownerId,
                                    List<String> lstStatus, Long telecomServiceId, BigDecimal startNumberConvert, BigDecimal endNumberConvert) throws  Exception, LogicException{
        return  client.filterNumber(userCode, refilter, minNumber, lstSelectedRule, ownerId, lstStatus, telecomServiceId, startNumberConvert, endNumberConvert);
    }

    @Override
    @WebMethod
    public List<Object[]> getResultFilter() throws  LogicException, Exception{
        return client.getResultFilter();
    }

    @Override
    @WebMethod
    public List<FilterSpecialNumberDTO> getListSprecialNumberByRule(Long ruleId) throws  LogicException, Exception{
        return client.getListSprecialNumberByRule(ruleId);
    }

    @Override
    @WebMethod
    public BaseMessage updateFiltered(List<FilterSpecialNumberDTO> listNumberFiltered, List<FilterSpecialNumberDTO> listNumberOK, String userUpdateCode, String userIp) throws LogicException,Exception{
        return client.updateFiltered(listNumberFiltered, listNumberOK, userUpdateCode, userIp);
    }

    @Override
    @WebMethod
    public BaseMessage updateAllFiltered(String userUpdateCode, String userIp, List<Long> listFilterId) throws LogicException, Exception {
        return client.updateAllFiltered(userUpdateCode, userIp, listFilterId);
    }
}