package com.viettel.bccs.inventory.wsesb.service;

import com.viettel.bccs.inventory.dto.GroupFilterRuleDTO;
import com.viettel.bccs.inventory.service.GroupFilterRuleService;
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
import java.util.List;

@Service("WsGroupFilterRuleServiceImpl")
public class WsGroupFilterRuleServiceImpl implements GroupFilterRuleService {

    public static final Logger logger = Logger.getLogger(WsGroupFilterRuleServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private GroupFilterRuleService client;

    @PostConstruct
    public void init() throws LogicException, Exception {
        try {
            client = wsClientFactory.createWsClient(GroupFilterRuleService.class);
        } catch (Exception ex) {
            logger.error("init", ex);
        }
    }

    @Override
    @WebMethod
    public GroupFilterRuleDTO findOne(Long id) throws LogicException, Exception {
        return client.findOne(id);
    }

    @Override
    @WebMethod
    public Long count(List<FilterRequest> filters) throws LogicException, Exception {
        return client.count(filters);
    }

    @Override
    @WebMethod
    public List<GroupFilterRuleDTO> findAll() throws LogicException, Exception {
        return client.findAll();
    }

    @Override
    @WebMethod
    public List<GroupFilterRuleDTO> findByFilter(List<FilterRequest> filters) throws LogicException, Exception {
        return client.findByFilter(filters);
    }

    @Override
    @WebMethod
    public BaseMessage create(GroupFilterRuleDTO productSpecCharacterDTO, String userName) throws LogicException, Exception {
        return client.create(productSpecCharacterDTO, userName);
    }

    @Override
    @WebMethod
    public BaseMessage update(GroupFilterRuleDTO productSpecCharacterDTO, String userName) throws LogicException, Exception {
        return client.update(productSpecCharacterDTO, userName);
    }

    @Override
    @WebMethod
    public GroupFilterRuleDTO save(GroupFilterRuleDTO productSpecCharacterDTO) throws LogicException, Exception {
        return client.save(productSpecCharacterDTO);
    }

    @Override
    @WebMethod
    public BaseMessage delete(Long id) throws LogicException, Exception {
        return client.delete(id);
    }

    @Override
    @WebMethod
    public List<GroupFilterRuleDTO> searchByDto(GroupFilterRuleDTO dto) throws LogicException, Exception {
        return client.searchByDto(dto);
    }

    @Override
    @WebMethod
    public BaseMessage deleteByDto(GroupFilterRuleDTO dto, String userName) throws LogicException, Exception {
        return client.deleteByDto(dto, userName);
    }

    @Override
    @WebMethod
    public BaseMessage deleteByListDto(List<GroupFilterRuleDTO> listDto, String userName) throws LogicException, Exception {
        return client.deleteByListDto(listDto, userName);
    }

    @Override
    public List<GroupFilterRuleDTO> getListNumberFiler(Long groupFilterRuleId, boolean isParent) throws Exception {
        return client.getListNumberFiler(groupFilterRuleId, isParent);
    }

    @Override
    public List<GroupFilterRuleDTO> search(GroupFilterRuleDTO currentGroup,boolean isGroup) throws Exception {
        return client.search(currentGroup,isGroup);
    }
}