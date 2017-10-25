package com.viettel.bccs.inventory.wsesb.service;

import com.viettel.bccs.inventory.dto.NumberFilterRuleDTO;
import com.viettel.bccs.inventory.dto.StaffDTO;
import com.viettel.bccs.inventory.service.NumberFilterRuleService;
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

@Service("WsNumberFilterRuleServiceImpl")
public class WsNumberFilterRuleServiceImpl implements NumberFilterRuleService {

    public static final Logger logger = Logger.getLogger(WsNumberFilterRuleServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private NumberFilterRuleService client;

    @PostConstruct
    public void init() throws Exception {
        try {
            client = wsClientFactory.createWsClient(NumberFilterRuleService.class);
        } catch (Exception ex) {
            logger.error("init", ex);
        }
    }

    @Override
    @WebMethod
    public NumberFilterRuleDTO findOne(Long id) throws Exception {
        return client.findOne(id);
    }

    @Override
    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return client.count(filters);
    }

    @Override
    @WebMethod
    public List<NumberFilterRuleDTO> findAll() throws Exception {
        return client.findAll();
    }

    @Override
    @WebMethod
    public List<NumberFilterRuleDTO> findByFilter(List<FilterRequest> filters) throws Exception {
        return client.findByFilter(filters);
    }

    @Override
    @WebMethod
    public BaseMessage create(NumberFilterRuleDTO productSpecCharacterDTO, StaffDTO staffDTO) throws Exception {
        return client.create(productSpecCharacterDTO, staffDTO);
    }

    @Override
    @WebMethod
    public BaseMessage update(NumberFilterRuleDTO productSpecCharacterDTO, StaffDTO staffDTO) throws Exception {
        return client.update(productSpecCharacterDTO, staffDTO);
    }

    @Override
    @WebMethod
    public Long checkInsertConditionTelecomServiceId(NumberFilterRuleDTO numberFilterRuleDTO) throws Exception {
        return client.checkInsertConditionTelecomServiceId(numberFilterRuleDTO);
    }

    @WebMethod
    public Long checkEditConditionTelecomServiceId(NumberFilterRuleDTO numberFilterRuleDTO) throws Exception {
        return client.checkEditConditionTelecomServiceId(numberFilterRuleDTO);
    }


    @Override
    @WebMethod
    public Boolean checkExistNameNumberFilter(String name, Long filterId) throws Exception {
        return client.checkExistNameNumberFilter(name, filterId);
    }

    @Override
    @WebMethod
    public List<NumberFilterRuleDTO> findRuleAggregate(NumberFilterRuleDTO numberFilterRule, boolean extract) throws Exception {
        return client.findRuleAggregate(numberFilterRule, extract);
    }

    @Override
    @WebMethod
    public BaseMessage deleteListNumberFiler(List<NumberFilterRuleDTO> lsFinanceTypeDTOs, StaffDTO staffDTO) throws Exception {
        return client.deleteListNumberFiler(lsFinanceTypeDTOs, staffDTO);
    }

    @Override
    @WebMethod
    public List<NumberFilterRuleDTO> searchHightOrderRule(Long telecomServiceId, Long minNumber) throws Exception {
        return client.searchHightOrderRule(telecomServiceId, minNumber);
    }

    @Override
    public List<NumberFilterRuleDTO> getListNumerFilterRule(NumberFilterRuleDTO numberFilterRuleDTO) throws Exception {
        return client.getListNumerFilterRule(numberFilterRuleDTO);
    }

    @Override
    public boolean checkBeforeDelete(Long groupFilterId) throws Exception {
        return client.checkBeforeDelete(groupFilterId);
    }
}