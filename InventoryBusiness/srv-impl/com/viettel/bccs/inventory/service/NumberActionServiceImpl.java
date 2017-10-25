package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.mysema.query.types.OrderSpecifier;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.NumberActionDTO;
import com.viettel.bccs.inventory.dto.SearchNumberRangeDTO;
import com.viettel.bccs.inventory.model.NumberAction;
import com.viettel.bccs.inventory.model.QNumberAction;
import com.viettel.bccs.inventory.repo.NumberActionRepo;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.fw.service.BaseServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.jws.WebMethod;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;


@Service
public class NumberActionServiceImpl extends BaseServiceImpl implements NumberActionService {

    private final BaseMapper<NumberAction, NumberActionDTO> mapper = new BaseMapper(NumberAction.class, NumberActionDTO.class);
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @Autowired
    private NumberActionRepo repository;
    public static final Logger logger = Logger.getLogger(NumberActionService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public NumberActionDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<NumberActionDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<NumberActionDTO> findByFilter(List<FilterRequest> filters) throws Exception {
        OrderSpecifier<Date> sortByName = QNumberAction.numberAction.createDate.desc();
        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters), sortByName));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public NumberActionDTO create(NumberActionDTO dto) throws Exception {
        dto.setCreateDate(getSysDate(em));
        NumberActionDTO result = mapper.toDtoBean(repository.save(mapper.toPersistenceBean(dto)));
        return result;
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(NumberActionDTO dto) throws Exception {
        BaseMessage result = new BaseMessage();
        result.setSuccess(true);
        repository.save(mapper.toPersistenceBean(dto));
        return result;
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public Boolean checkOverlap(Long fromIsdn, Long toIsdn, String telecomServiceId) throws Exception {

        return repository.checkOverlap(fromIsdn, toIsdn, telecomServiceId);
    }

    @Override
    public List<NumberActionDTO> search(SearchNumberRangeDTO searchDTO) throws Exception {
        return repository.search(searchDTO);
//        List<FilterRequest> filters = Lists.newArrayList();
//        if (!DataUtil.isNullObject(searchDTO.getStartRange()) && !DataUtil.isNullObject(searchDTO.getEndRange())) {
//            Long[] fromToIsdn = new Long[2];
//            fromToIsdn[0] = Long.valueOf((!DataUtil.isNullObject(searchDTO.getPstnCode()) ? searchDTO.getPstnCode() : "") + searchDTO.getStartRange());
//            fromToIsdn[1] = Long.valueOf((!DataUtil.isNullObject(searchDTO.getPstnCode()) ? searchDTO.getPstnCode() : "") + searchDTO.getEndRange());
//            FilterRequest filterRequest0 = new FilterRequest(NumberAction.COLUMNS.FROMTOISDN.name(), FilterRequest.Operator.GOE, fromToIsdn);
//            filters.add(filterRequest0);
//        } else if (!DataUtil.isNullObject(searchDTO.getStartRange())) {
//            FilterRequest filterRequest1 = new FilterRequest(NumberAction.COLUMNS.FROMISDN.name(), FilterRequest.Operator.GOE, Long.valueOf((!DataUtil.isNullObject(searchDTO.getPstnCode()) ? searchDTO.getPstnCode() : "") + searchDTO.getStartRange()));
//            filters.add(filterRequest1);
//        } else if (!DataUtil.isNullObject(searchDTO.getEndRange())) {
//            FilterRequest filterRequest2 = new FilterRequest(NumberAction.COLUMNS.TOISDN.name(), FilterRequest.Operator.LOE, Long.valueOf((!DataUtil.isNullObject(searchDTO.getPstnCode()) ? searchDTO.getPstnCode() : "") + searchDTO.getEndRange()));
//            filters.add(filterRequest2);
//        }
//        if (!DataUtil.isNullObject(searchDTO.getServiceType())) {
//            FilterRequest filterRequest3 = new FilterRequest(NumberAction.COLUMNS.TELECOMSERVICEID.name(), FilterRequest.Operator.EQ, Long.valueOf(searchDTO.getServiceType()));
//            filters.add(filterRequest3);
//        }
//        if (!DataUtil.isNullObject(searchDTO.getPstnCode())) {
//            FilterRequest filterRequest4 = new FilterRequest(NumberAction.COLUMNS.FROMISDN.name(), FilterRequest.Operator.LIKE, searchDTO.getPstnCode() + "%");
//            filters.add(filterRequest4);
//        }
//        if (!DataUtil.isNullObject(searchDTO.getPlanningType())) {
//            FilterRequest filterRequest5 = new FilterRequest(NumberAction.COLUMNS.SERVICETYPE.name(), FilterRequest.Operator.EQ, searchDTO.getPlanningType());
//            filters.add(filterRequest5);
//        }
//        if (!DataUtil.isNullObject(searchDTO.getStatus())) {
//            FilterRequest filterRequest7 = new FilterRequest(NumberAction.COLUMNS.STATUS.name(), FilterRequest.Operator.EQ, searchDTO.getStatus());
//            filters.add(filterRequest7);
//        }
//        FilterRequest filterRequest6 = new FilterRequest(NumberAction.COLUMNS.ACTIONTYPE.name(), FilterRequest.Operator.EQ, "1");
//        filters.add(filterRequest6);
//        return findByFilter(filters);
    }
}
