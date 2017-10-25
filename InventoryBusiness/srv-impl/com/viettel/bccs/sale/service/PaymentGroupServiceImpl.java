package com.viettel.bccs.sale.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.sale.dto.PaymentGroupDayTypeDTO;
import com.viettel.bccs.sale.repo.PaymentGroupRepo;
import com.viettel.bccs.sale.dto.PaymentGroupDTO;
import com.viettel.bccs.sale.model.PaymentGroup;
import com.viettel.bccs.sale.service.PaymentGroupService;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.fw.common.util.extjs.FilterRequest;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.viettel.fw.service.BaseServiceImpl;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.jws.WebMethod;
import javax.xml.crypto.Data;

import org.springframework.transaction.annotation.Transactional;


@Service
public class PaymentGroupServiceImpl extends BaseServiceImpl implements PaymentGroupService {

    private final BaseMapper<PaymentGroup, PaymentGroupDTO> mapper = new BaseMapper<>(PaymentGroup.class, PaymentGroupDTO.class);

    @Autowired
    private PaymentGroupRepo repository;
    public static final Logger logger = Logger.getLogger(PaymentGroupService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public PaymentGroupDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<PaymentGroupDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<PaymentGroupDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage create(PaymentGroupDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(PaymentGroupDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    public List<PaymentGroupDTO> getLstPaymentGroup() throws Exception {
        return repository.getLstPaymentGroup();
    }

    public PaymentGroupDTO getPaymentGroupByName(String name) throws Exception {
        return repository.getPaymentGroupByName(name);
    }

    public List<PaymentGroupDayTypeDTO> getLstDayTypeByPaymentGroupId(Long paymentGroupId) throws Exception {
        return repository.getLstDayTypeByPaymentGroupId(paymentGroupId);
    }
}
