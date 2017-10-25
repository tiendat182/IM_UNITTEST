package com.viettel.bccs.sale.service;
import com.google.common.collect.Lists;
import com.viettel.bccs.sale.repo.PaymentGroupServiceDetailRepo;
import com.viettel.bccs.sale.dto.PaymentGroupServiceDetailDTO;
import com.viettel.bccs.sale.model.PaymentGroupServiceDetail;
import com.viettel.bccs.sale.service.PaymentGroupServiceDetailService;
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
public class PaymentGroupServiceDetailServiceImpl extends BaseServiceImpl implements PaymentGroupServiceDetailService {

    private final BaseMapper<PaymentGroupServiceDetail, PaymentGroupServiceDetailDTO> mapper = new BaseMapper<>(PaymentGroupServiceDetail.class, PaymentGroupServiceDetailDTO.class);

    @Autowired
    private PaymentGroupServiceDetailRepo repository;
    public static final Logger logger = Logger.getLogger(PaymentGroupServiceDetailService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public PaymentGroupServiceDetailDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<PaymentGroupServiceDetailDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<PaymentGroupServiceDetailDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage create(PaymentGroupServiceDetailDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(PaymentGroupServiceDetailDTO dto) throws Exception {
        throw new NotImplementedException();
    }
}
