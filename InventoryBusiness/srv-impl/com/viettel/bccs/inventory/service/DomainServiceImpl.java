package com.viettel.bccs.inventory.service;
import com.google.common.collect.Lists;
import com.mysema.query.types.OrderSpecifier;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.DomainDTO;
import com.viettel.bccs.inventory.model.Domain;
import com.viettel.bccs.inventory.model.QDomain;
import com.viettel.bccs.inventory.repo.DomainRepo;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.fw.service.BaseServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.jws.WebMethod;
import java.util.List;

@Service
public class DomainServiceImpl extends BaseServiceImpl implements DomainService {

    private final BaseMapper<Domain, DomainDTO> mapper = new BaseMapper(Domain.class, DomainDTO.class);

    @Autowired
    private DomainRepo repository;
    public static final Logger logger = Logger.getLogger(DomainService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public DomainDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<DomainDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<DomainDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        OrderSpecifier<String> sortByName = QDomain.domain1.domain.asc();
        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters), sortByName));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage create(DomainDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(DomainDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @Override
    public DomainDTO findByDomain(String domain) throws Exception, LogicException {
        List<FilterRequest> requests = Lists.newArrayList();
        requests.add(new FilterRequest(Domain.COLUMNS.DOMAIN.name(), FilterRequest.Operator.EQ,domain));
        requests.add(new FilterRequest(Domain.COLUMNS.STATUS.name(), FilterRequest.Operator.EQ, Const.STATUS_ACTIVE));
        List<DomainDTO> list = findByFilter(requests);
        if(!DataUtil.isNullOrEmpty(list)){
            return list.get(0);
        }
        return null;
    }
    @Override
    public List<DomainDTO> findAllAction() throws Exception, LogicException {
        List<FilterRequest> requests = Lists.newArrayList();
        requests.add(new FilterRequest(Domain.COLUMNS.STATUS.name(), FilterRequest.Operator.EQ, Const.STATUS_ACTIVE));
        List<DomainDTO> list = findByFilter(requests);
        return list;
    }

    @Override
    public DomainDTO findByDomainId(Long domainId) throws Exception, LogicException {
        List<FilterRequest> requests = Lists.newArrayList();
        requests.add(new FilterRequest(Domain.COLUMNS.ID.name(), FilterRequest.Operator.EQ,domainId));
        requests.add(new FilterRequest(Domain.COLUMNS.STATUS.name(), FilterRequest.Operator.EQ, Const.STATUS_ACTIVE));
        List<DomainDTO> list = findByFilter(requests);
        if(!DataUtil.isNullOrEmpty(list)){
            return list.get(0);
        }
        return null;
    }


}
