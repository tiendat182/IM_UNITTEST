package com.viettel.bccs.inventory.wsesb.service;

import com.viettel.bccs.inventory.dto.RevokeKitDetailDTO;
import com.viettel.bccs.inventory.dto.RevokeKitResultDTO;
import com.viettel.bccs.inventory.service.RevokeKitDetailService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.jws.WebMethod;
import java.util.Date;
import java.util.List;
@Service("WsRevokeKitDetailServiceImpl")
public class WsRevokeKitDetailServiceImpl implements RevokeKitDetailService {

    public static final Logger logger = Logger.getLogger(WsRevokeKitDetailServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private RevokeKitDetailService client;

    @PostConstruct
    public void init() throws Exception {
      try{
         client = wsClientFactory.createWsClient(RevokeKitDetailService.class);
      } catch (Exception ex) {
          logger.error("init", ex);
      }
    }

    @Override
    @WebMethod
    public RevokeKitDetailDTO findOne(Long id) throws Exception {
        return client.findOne(id);
    }

    @Override
    @WebMethod
    public RevokeKitDetailDTO save(RevokeKitDetailDTO productSpecCharacterDTO) throws LogicException, Exception {
        return client.save(productSpecCharacterDTO);
    }

    @Override
    @WebMethod
    public List<RevokeKitDetailDTO> searchRevokeKitDetailByShopAndDate(Long shopId, Date fromDate, Date toDate) throws LogicException, Exception {
        return client.searchRevokeKitDetailByShopAndDate(shopId, fromDate, toDate);
    }

    @Override
    @WebMethod
    public RevokeKitResultDTO searchRevokeKitBySerialAndIsdn(RevokeKitResultDTO revokeKitResultDTO) throws LogicException, Exception {
        return client.searchRevokeKitBySerialAndIsdn(revokeKitResultDTO);
    }

    @Override
    public RevokeKitResultDTO executeRevokeKit(RevokeKitResultDTO revokeKitResultDTO) throws LogicException, Exception {
        return client.executeRevokeKit(revokeKitResultDTO);
    }
}