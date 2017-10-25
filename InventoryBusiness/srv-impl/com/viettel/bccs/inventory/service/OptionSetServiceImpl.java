package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.OptionSetDTO;
import com.viettel.bccs.inventory.dto.OptionSetValueDTO;
import com.viettel.bccs.inventory.model.OptionSet;
import com.viettel.bccs.inventory.model.OptionSetValue;
import com.viettel.bccs.inventory.repo.OptionSetRepo;
import com.viettel.bccs.inventory.repo.OptionSetValueRepo;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.fw.service.BaseServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.jws.WebMethod;
import java.util.ArrayList;
import java.util.List;

@Service
public class OptionSetServiceImpl extends BaseServiceImpl implements OptionSetService {

    private final BaseMapper<OptionSet, OptionSetDTO> mapper = new BaseMapper(OptionSet.class, OptionSetDTO.class);
    private final BaseMapper<OptionSetValue, OptionSetValueDTO> optionSetValueMapper = new BaseMapper(OptionSetValue.class, OptionSetValueDTO.class);

    @Autowired
    private OptionSetRepo repository;
    @Autowired
    private OptionSetValueRepo optionSetValueRepository;
    public final static Logger logger = Logger.getLogger(OptionSetService.class);

    private enum AlterMode {
        CREATE, UPDATE
    }

    private AlterMode alterMode;

    @WebMethod
    @Override
    public List<OptionSetDTO> findByFilter(List<FilterRequest> filters) {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Override
    public List<FilterRequest> getRequestListByCondition(OptionSetDTO optionSetDTO) {
        List<FilterRequest> requestList = new ArrayList<>();
        requestList.add(new FilterRequest("NAME", optionSetDTO.getName()));
        requestList.add(new FilterRequest("CODE", optionSetDTO.getCode()));
        requestList.add(new FilterRequest("STATUS", optionSetDTO.getStatus()));
        return requestList;
    }

    @WebMethod
    @Override
    public OptionSetDTO findById(Long optionSetId) {
        OptionSetDTO optionSetDto = mapper.toDtoBean(repository.findOne(optionSetId));
        if (optionSetDto != null) {
            optionSetDto.setLsOptionSetValueDto(optionSetValueMapper.toDtoBean(optionSetValueRepository.getLsOptionSetValueById(optionSetId)));
        }
        return optionSetDto;
    }
}