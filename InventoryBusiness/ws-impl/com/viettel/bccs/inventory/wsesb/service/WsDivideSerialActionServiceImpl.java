package com.viettel.bccs.inventory.wsesb.service;
import java.util.ArrayList;
import com.viettel.bccs.inventory.dto.DivideSerialActionDTO;
import com.viettel.bccs.inventory.dto.StockTransFullDTO;
import com.viettel.bccs.inventory.dto.StockTransSerialDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.data.*;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.fw.dto.BaseDTO;
import com.viettel.ws.common.utils.*;

import com.viettel.bccs.inventory.service.DivideSerialActionService;
import java.util.List;
import java.lang.Long;
import org.springframework.stereotype.Service;
import org.apache.log4j.Logger;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.springframework.beans.factory.annotation.Autowired;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Qualifier;
@Service("WsDivideSerialActionServiceImpl")
public class WsDivideSerialActionServiceImpl implements DivideSerialActionService {

    public static final Logger logger = Logger.getLogger(WsDivideSerialActionServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private DivideSerialActionService client;

    @PostConstruct
    public void init() throws Exception {
      try{
         client = wsClientFactory.createWsClient(DivideSerialActionService.class);
      } catch (Exception ex) {
          logger.error("init", ex);
      }
    }

    @Override
    public DivideSerialActionDTO findOne(Long id) throws Exception  {
        return client.findOne(id);
    }

    @Override
    public List<DivideSerialActionDTO> findAll() throws Exception {
        return client.findAll();
    }

    @Override
    public List<DivideSerialActionDTO> findByFilter(List<FilterRequest> filterList) throws Exception  {
        return client.findByFilter(filterList);
    }

    @Override
    public Long count(List<FilterRequest> filterList) throws Exception {
        return client.count(filterList);
    }

    @Override
    public DivideSerialActionDTO save(DivideSerialActionDTO dto) throws Exception  {
        return client.save(dto);
    }

    @Override
    public BaseMessage update(DivideSerialActionDTO dto) throws Exception  {
        return client.update(dto);
    }

    @Override
    public List<StockTransFullDTO> divideSerial(DivideSerialActionDTO divideSerialActionDTO) throws Exception {
        return client.divideSerial(divideSerialActionDTO);
    }

    @Override
    public List<StockTransFullDTO> searchStockTrans(DivideSerialActionDTO divideSerialActionDTO) throws Exception {
        return client.searchStockTrans(divideSerialActionDTO);
    }

    @Override
    public List<StockTransFullDTO> getListSerial(DivideSerialActionDTO divideSerialActionDTO) throws Exception {
        return client.getListSerial(divideSerialActionDTO);
    }

}