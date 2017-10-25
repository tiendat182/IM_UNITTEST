package com.viettel.bccs.sale.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.sale.repo.PaymentGroupServiceRepo;
import com.viettel.bccs.sale.dto.PaymentGroupServiceDTO;
import com.viettel.bccs.sale.model.PaymentGroupService;
import com.viettel.bccs.sale.service.PaymentGroupServiceService;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.fw.common.util.extjs.FilterRequest;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.viettel.fw.service.BaseServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.springframework.data.domain.Sort;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.jws.WebMethod;

import org.springframework.transaction.annotation.Transactional;


@Service
public class PaymentGroupServiceServiceImpl extends BaseServiceImpl implements PaymentGroupServiceService {

    private final BaseMapper<PaymentGroupService, PaymentGroupServiceDTO> mapper = new BaseMapper<>(PaymentGroupService.class, PaymentGroupServiceDTO.class);

    @Autowired
    private PaymentGroupServiceRepo repository;
    public static final Logger logger = Logger.getLogger(PaymentGroupServiceService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public PaymentGroupServiceDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<PaymentGroupServiceDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<PaymentGroupServiceDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage create(PaymentGroupServiceDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(PaymentGroupServiceDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    public List<PaymentGroupServiceDTO> getLstGroupService() throws Exception {
        List<FilterRequest> lst = Lists.newArrayList();
        lst.add(new FilterRequest(PaymentGroupService.COLUMNS.STATUS.name(), FilterRequest.Operator.EQ, DataUtil.safeToLong(Const.STATUS_ACTIVE)));
        return findByFilter(lst);
    }

    public PaymentGroupServiceDTO getPaymentGroupServiceByName(String name) throws Exception {
        return repository.getPaymentGroupServiceByName(name);
    }
}
