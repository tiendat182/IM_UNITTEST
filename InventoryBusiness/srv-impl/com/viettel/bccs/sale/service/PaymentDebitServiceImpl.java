package com.viettel.bccs.sale.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.sale.repo.PaymentDebitRepo;
import com.viettel.bccs.sale.dto.PaymentDebitDTO;
import com.viettel.bccs.sale.model.PaymentDebit;
import com.viettel.bccs.sale.service.PaymentDebitService;
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

import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentDebitServiceImpl extends BaseServiceImpl implements PaymentDebitService {

    private final BaseMapper<PaymentDebit, PaymentDebitDTO> mapper = new BaseMapper<>(PaymentDebit.class, PaymentDebitDTO.class);

    @Autowired
    private PaymentDebitRepo repository;
    public static final Logger logger = Logger.getLogger(PaymentDebitService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public PaymentDebitDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<PaymentDebitDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<PaymentDebitDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public PaymentDebitDTO create(PaymentDebitDTO dto) throws Exception {
        return mapper.toDtoBean(repository.save(mapper.toPersistenceBean(dto)));

    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(PaymentDebitDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    public PaymentDebitDTO getPaymentDebit(Long ownerId, Long ownerType) throws Exception {
        List<FilterRequest> filterRequests = Lists.newArrayList();
        filterRequests.add(new FilterRequest(PaymentDebit.COLUMNS.OWNERID.name(), FilterRequest.Operator.EQ, ownerId));
        filterRequests.add(new FilterRequest(PaymentDebit.COLUMNS.OWNERTYPE.name(), FilterRequest.Operator.EQ, ownerType));
        List<PaymentDebitDTO> result = findByFilter(filterRequests);
        if (!DataUtil.isNullOrEmpty(result)) {
            return result.get(0);
        }
        return null;
    }
}
