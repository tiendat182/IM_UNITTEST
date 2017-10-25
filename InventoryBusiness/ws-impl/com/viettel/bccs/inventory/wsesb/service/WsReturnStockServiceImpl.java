package com.viettel.bccs.inventory.wsesb.service;
import com.viettel.bccs.inventory.dto.AreaDTO;
import com.viettel.bccs.inventory.dto.StockTransDTO;
import com.viettel.bccs.inventory.dto.StockTransDetailDTO;
import com.viettel.bccs.inventory.service.AreaService;
import com.viettel.bccs.inventory.service.ReturnStockService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.RequiredRoleMap;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.jws.WebMethod;
import java.util.List;

@Service("WsReturnStockServiceImpl")
public class WsReturnStockServiceImpl implements ReturnStockService {

    public static final Logger logger = Logger.getLogger(WsReturnStockServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private ReturnStockService client;

    @PostConstruct
    public void init() throws Exception {
      try{
         client = wsClientFactory.createWsClient(ReturnStockService.class);
      } catch (Exception ex) {
          logger.error("init", ex);
      }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @WebMethod
    public StockTransDTO saveReturnStock(StockTransDTO stockTransDTO, List<StockTransDetailDTO> listTransDetailDTOs, RequiredRoleMap requiredRoleMap) throws Exception, LogicException {
        return client.saveReturnStock(stockTransDTO, listTransDetailDTOs, requiredRoleMap);
    }
}