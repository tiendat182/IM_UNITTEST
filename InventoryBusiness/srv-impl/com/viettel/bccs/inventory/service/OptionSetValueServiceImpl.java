package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.OptionSetValueDTO;
import com.viettel.bccs.inventory.model.OptionSetValue;
import com.viettel.bccs.inventory.repo.OptionSetValueRepo;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.fw.service.BaseServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.googlecode.ehcache.annotations.Cacheable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OptionSetValueServiceImpl extends BaseServiceImpl implements OptionSetValueService {

    private final BaseMapper<OptionSetValue, OptionSetValueDTO> mapper = new BaseMapper(OptionSetValue.class, OptionSetValueDTO.class);
    @Autowired
    private OptionSetValueRepo repository;
    public final static Logger logger = Logger.getLogger(OptionSetValueService.class);

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @Override
    public List<OptionSetValueDTO> findByFilter(List<FilterRequest> filters) {
        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @Override
    public OptionSetValueDTO findOne(Long id) {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @Override
    @Cacheable(cacheName = "optionSetValueServiceImpl.getLsOptionSetValueByCode")
    public List<OptionSetValueDTO> getLsOptionSetValueByCode(String strCode) {
        return mapper.toDtoBean(repository.getLsOptionSetValueByCode(strCode));
    }

    @Override
    @Cacheable(cacheName = "optionSetValueServiceImpl.getValueByNameFromOptionSetValue")
    public Long getValueByNameFromOptionSetValue(String name) throws Exception {
        return repository.getValueByNameFromOptionSetValue(name);
    }

    /**
     * @param code
     * @return
     * @throws Exception
     * @author KhuongDV Get List OptionSetValueDTO theo optionSetID
     */
    @Override
    @Cacheable(cacheName = "optionSetValueServiceImpl.getByOptionSetCode")
    public List<OptionSetValueDTO> getByOptionSetCode(String code) throws Exception {
        if (code == null || code.isEmpty()) {
            return new ArrayList<>();
        }
        List<OptionSetValue> listModel = repository.getByOptionSetCode(code);
        return mapper.toDtoBean(listModel);
    }

    @Override
    @Cacheable(cacheName = "optionSetValueServiceImpl.getValueByTwoCodeOption")
    public String getValueByTwoCodeOption(String optSetCode, String name) throws Exception {
        return repository.getValueByOptionSetIdAndCode(optSetCode, name);
    }


    @Override
    @Cacheable(cacheName = "optionSetValueServiceImpl.getByOptionSetValueByNameCode")
    public List<OptionSetValueDTO> getByOptionSetValueByNameCode(String code, String name) throws Exception {
        if (code == null || code.isEmpty()) {
            return new ArrayList<>();
        }
        List<OptionSetValue> listModel = repository.getByOptionSetValueByNameCode(code, name);
        return mapper.toDtoBean(listModel);
    }

    @Override
//    @Cacheable(cacheName = "optionSetValueServiceImpl.getSysdateFromDB")
    public Date getSysdateFromDB(boolean trunc) throws Exception {
        if (trunc) {
            return DbUtil.getTruncSysdate(em);
        }

        return DbUtil.getSysDate(em);
    }

    @Override
    @Cacheable(cacheName = "optionSetValueServiceImpl.getByOptionsetCodeAndValue")
    public List<OptionSetValueDTO> getByOptionsetCodeAndValue(String code, String value) throws Exception {
        return mapper.toDtoBean(repository.getByOptionsetCodeAndValue(code, value));
    }

    @Override
    @Cacheable(cacheName = "optionSetValueServiceImpl.getDebitDayTypeByDebitLevel")
    public List<OptionSetValueDTO> getDebitDayTypeByDebitLevel(String debitLevel) throws Exception {
        return mapper.toDtoBean(repository.getDebitDayTypeByDebitLevel(debitLevel));
    }

    @Override
    @Cacheable(cacheName = "optionSetValueServiceImpl.getStatusOptionSetValueByStockState")
    public List<OptionSetValueDTO> getStatusOptionSetValueByStockState(String code, List<String> listValue) throws Exception {
        return mapper.toDtoBean(repository.getStatusOptionSetValueByStockState(code, listValue));
    }

    @Override
    @Cacheable(cacheName = "optionSetValueServiceImpl.getDebitLevel")
    public List<OptionSetValueDTO> getDebitLevel(Long debitDayType) throws LogicException, Exception {
        return repository.getDebitLevel(debitDayType);
    }

    @Override
    @Cacheable(cacheName = "optionSetValueServiceImpl.getFinanceType")
    public List<OptionSetValueDTO> getFinanceType() throws LogicException, Exception {
        return repository.getFinanceType();
    }

    @Override
    public List<OptionSetValueDTO> getLsProvince(String shopCodeUser, String shopCode, Long prodOfferTypeId, Long prodOfferId, Long stateId) throws Exception {
        return repository.getLsProvince(shopCodeUser, shopCode, prodOfferTypeId, prodOfferId, stateId);
    }

    public List<OptionSetValueDTO> getDefaultDebitLevel() throws LogicException, Exception {
        return repository.getDefaultDebitLevel();
    }
}