package com.viettel.bccs.inventory.wsesb.service;

import com.viettel.bccs.inventory.dto.ApnDTO;
import com.viettel.bccs.inventory.service.ApnService;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.jws.WebMethod;
import java.util.List;
@Service("WsApnServiceImpl")
public class WsApnServiceImpl implements ApnService {

    public static final Logger logger = Logger.getLogger(WsApnServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private ApnService client;

    @PostConstruct
    public void init() throws Exception {
      try{
         client = wsClientFactory.createWsClient(ApnService.class);
      } catch (Exception ex) {
          logger.error("init", ex);
      }
    }

    @Override
    public ApnDTO findOne(Long id) throws Exception  {
        return client.findOne(id);
    }

    @Override
    public List<ApnDTO> findAll() throws Exception {
        return client.findAll();
    }

    @Override
    public List<ApnDTO> findByFilter(List<FilterRequest> filterList) throws Exception  {
        return client.findByFilter(filterList);
    }

    @Override
    public Long count(List<FilterRequest> filterList) throws Exception {
        return client.count(filterList);
    }

    @Override
    public BaseMessage create(ApnDTO dto) throws Exception  {
        return client.create(dto);
    }

    @Override
    public BaseMessage update(ApnDTO dto) throws Exception  {
        return client.update(dto);
    }

    @Override
    @WebMethod
    public List<ApnDTO> checkDuplicateApn(List<ApnDTO> listApn, String typeCheck) {
        return client.checkDuplicateApn(listApn, typeCheck);
    }

    @Override
    @WebMethod
    public BaseMessage insertListAPN(List<ApnDTO> listApn) throws Exception {
        return client.insertListAPN(listApn);
    }

    @Override
    @WebMethod
    public List<ApnDTO> searchApn(ApnDTO apnDTO) throws Exception {
        return client.searchApn(apnDTO);
    }

    @Override
    @WebMethod
    public List<ApnDTO> searchApnAutoComplete(String input) throws Exception {
        return client.searchApnAutoComplete(input);
    }

    @Override
    @WebMethod
    public ApnDTO findApnById(Long apnId) throws Exception{
        return client.findApnById(apnId);
    }

    @Override
    @WebMethod
    public List<ApnDTO> searchApnCorrect(ApnDTO apnDTO) throws Exception {
        return client.searchApnCorrect(apnDTO);
    }

}