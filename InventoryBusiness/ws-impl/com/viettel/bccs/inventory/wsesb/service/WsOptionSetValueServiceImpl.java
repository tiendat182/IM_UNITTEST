package com.viettel.bccs.inventory.wsesb.service;

import com.viettel.bccs.inventory.dto.OptionSetValueDTO;
import com.viettel.bccs.inventory.service.OptionSetValueService;
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


@Service("WsOptionSetValueServiceImpl")
public class WsOptionSetValueServiceImpl implements OptionSetValueService {

    public static final Logger logger = Logger.getLogger(WsOptionSetValueServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private OptionSetValueService client;

    @PostConstruct
    public void init() {
        try {
            client = wsClientFactory.createWsClient(OptionSetValueService.class);
        } catch (Exception ex) {
            logger.error(ex);
        }
    }

    @Override
    public List<OptionSetValueDTO> findByFilter(List<FilterRequest> filters) {
        return client.findByFilter(filters);
    }

    @Override
    public OptionSetValueDTO findOne(Long id) {
        return client.findOne(id);
    }

    /**
     * get list OptionSetValueDTO by value otionset value'
     *
     * @param strCode code value of otionset
     * @return ls dto OptionSetValue
     * @author ThanhNT
     */
    @Override
    public List<OptionSetValueDTO> getLsOptionSetValueByCode(String strCode) {
        return client.getLsOptionSetValueByCode(strCode);
    }

    @Override
    public Long getValueByNameFromOptionSetValue(String name) throws Exception {
        return client.getValueByNameFromOptionSetValue(name);
    }

    @Override
    public List<OptionSetValueDTO> getByOptionSetCode(String code) throws LogicException, Exception {
        return client.getByOptionSetCode(code);
    }

    /**
     * Get  value name theo  code  cua option_set va name cua option_set_value
     *
     * @param optSetCode code of otionset
     * @param name       name cua optionsetValue
     * @return
     * @throws com.viettel.fw.Exception.LogicException, Exception
     * @author thanhnt
     */
    @Override
    public String getValueByTwoCodeOption(String optSetCode, String name) throws LogicException, Exception {
        return client.getValueByTwoCodeOption(optSetCode, name);
    }

    @Override
    public List<OptionSetValueDTO> getByOptionSetValueByNameCode(String code, String name) throws Exception {
        return client.getByOptionSetValueByNameCode(code, name);
    }

    @Override
    public Date getSysdateFromDB(boolean trunc) throws Exception {
        return client.getSysdateFromDB(trunc);
    }

    @Override
    public List<OptionSetValueDTO> getByOptionsetCodeAndValue(String code, String value) throws Exception {
        return client.getByOptionsetCodeAndValue(code, value);
    }

    @Override
    public List<OptionSetValueDTO> getDebitDayTypeByDebitLevel(String debitLevel) throws Exception {
        return client.getDebitDayTypeByDebitLevel(debitLevel);
    }

    @Override
    public List<OptionSetValueDTO> getStatusOptionSetValueByStockState(String code, List<String> listValue) throws Exception {
        return client.getStatusOptionSetValueByStockState(code, listValue);
    }

    @Override
    public List<OptionSetValueDTO> getDebitLevel(Long debitDayType) throws LogicException, Exception {
        return client.getDebitLevel(debitDayType);
    }

    @Override
    public List<OptionSetValueDTO> getFinanceType() throws LogicException, Exception {
        return client.getFinanceType();
    }

    @Override
    public List<OptionSetValueDTO> getLsProvince(String shopCodeUser, String shopCode, Long prodOfferTypeId, Long prodOfferId, Long stateId) throws Exception {
        return client.getLsProvince(shopCodeUser, shopCode, prodOfferTypeId, prodOfferId, stateId);
    }

    @Override
    public List<OptionSetValueDTO> getDefaultDebitLevel() throws LogicException, Exception {
        return client.getDefaultDebitLevel();
    }
}