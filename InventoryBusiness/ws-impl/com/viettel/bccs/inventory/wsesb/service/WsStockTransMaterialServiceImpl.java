package com.viettel.bccs.inventory.wsesb.service;

import com.viettel.bccs.inventory.dto.StockTransDetailDTO;
import com.viettel.bccs.inventory.dto.VStockTransDTO;
import com.viettel.bccs.inventory.service.StockTransMaterialService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.jws.WebMethod;
import java.util.List;

@Service("WsStockTransMaterialServiceImpl")
public class WsStockTransMaterialServiceImpl implements StockTransMaterialService {

    public static final Logger logger = Logger.getLogger(WsStockTransMaterialServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private StockTransMaterialService client;

    @PostConstruct
    public void init() throws Exception {
      try{
         client = wsClientFactory.createWsClient(StockTransMaterialService.class);
      } catch (Exception ex) {
          logger.error("init", ex);
      }
    }

    @Override
    @WebMethod
    public void excuteCreateTransMaterial(VStockTransDTO vStockTransDTO) throws LogicException, Exception {
        client.excuteCreateTransMaterial(vStockTransDTO);
    }

    @Override
    public String excuteCreateTransGift(String staffCode, List<StockTransDetailDTO> lsTransDetailDTOs) throws LogicException, Exception {
       return client.excuteCreateTransGift(staffCode, lsTransDetailDTOs);
    }

    @Override
    public void validateStockTotal(String staffCode, List<StockTransDetailDTO> lsTransDetailDTOs) throws LogicException, Exception {
        client.validateStockTotal(staffCode, lsTransDetailDTOs);
    }
}