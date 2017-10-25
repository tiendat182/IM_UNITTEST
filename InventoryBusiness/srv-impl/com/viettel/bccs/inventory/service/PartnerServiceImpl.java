package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.mysema.query.types.OrderSpecifier;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.PartnerDTO;
import com.viettel.bccs.inventory.model.Partner;
import com.viettel.bccs.inventory.model.QInvoiceType;
import com.viettel.bccs.inventory.model.QPartner;
import com.viettel.bccs.inventory.repo.PartnerRepo;
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
import java.util.Date;
import java.util.List;

@Service
public class PartnerServiceImpl extends BaseServiceImpl implements PartnerService {

    private final BaseMapper<Partner, PartnerDTO> mapper = new BaseMapper<>(Partner.class, PartnerDTO.class);

    @Autowired
    private PartnerRepo repository;
    public static final Logger logger = Logger.getLogger(PartnerService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public PartnerDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<PartnerDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<PartnerDTO> findByFilter(List<FilterRequest> filters) throws Exception {
        OrderSpecifier<String> sortByName = QPartner.partner.partnerName.desc();
        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters), sortByName));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage create(PartnerDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(PartnerDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @Override
    public List<PartnerDTO> findPartner(PartnerDTO partnerDTO) throws Exception {
        return mapper.toDtoBean(repository.findPartner(partnerDTO));
    }

    @WebMethod
    @Override
    public List<PartnerDTO> getAllPartner() throws Exception {
        List<FilterRequest> lst = Lists.newArrayList();
        lst.add(new FilterRequest(Partner.COLUMNS.STATUS.name(), FilterRequest.Operator.EQ, Const.STATUS.ACTIVE));
        return findByFilter(lst);
    }
}
