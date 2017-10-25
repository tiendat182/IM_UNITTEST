package com.viettel.bccs.im1.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.im1.dto.PaymentDebitIm1DTO;
import com.viettel.bccs.im1.model.PaymentDebitIm1;
import com.viettel.bccs.im1.repo.PaymentDebitIm1Repo;
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
public class PaymentDebitIm1ServiceImpl extends BaseServiceImpl implements PaymentDebitIm1Service {

    private final BaseMapper<PaymentDebitIm1, PaymentDebitIm1DTO> mapper = new BaseMapper<>(PaymentDebitIm1.class, PaymentDebitIm1DTO.class);

    @Autowired
    private PaymentDebitIm1Repo repository;
    public static final Logger logger = Logger.getLogger(PaymentDebitIm1Service.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public PaymentDebitIm1DTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<PaymentDebitIm1DTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<PaymentDebitIm1DTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage create(PaymentDebitIm1DTO dto) throws Exception {
        BaseMessage baseMessage = new BaseMessage();
        repository.save(mapper.toPersistenceBean(dto));
        baseMessage.setSuccess(true);
        return baseMessage;
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(PaymentDebitIm1DTO dto) throws Exception {
        throw new NotImplementedException();
    }

    public PaymentDebitIm1DTO getPaymentDebit(Long ownerId, Long ownerType) throws Exception {
        List<FilterRequest> filterRequests = Lists.newArrayList();
        filterRequests.add(new FilterRequest(PaymentDebitIm1.COLUMNS.OWNERID.name(), FilterRequest.Operator.EQ, ownerId));
        filterRequests.add(new FilterRequest(PaymentDebitIm1.COLUMNS.OWNERTYPE.name(), FilterRequest.Operator.EQ, ownerType));
        List<PaymentDebitIm1DTO> result = findByFilter(filterRequests);
        if (!DataUtil.isNullOrEmpty(result)) {
            return result.get(0);
        }
        return null;
    }
}
