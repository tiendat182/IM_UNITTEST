package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.mysema.query.types.OrderSpecifier;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.InvoiceTemplateTypeDTO;
import com.viettel.bccs.inventory.model.InvoiceTemplateType;
import com.viettel.bccs.inventory.model.QInvoiceTemplateType;
import com.viettel.bccs.inventory.repo.InvoiceTemplateTypeRepo;
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
public class InvoiceTemplateTypeServiceImpl extends BaseServiceImpl implements InvoiceTemplateTypeService {

//    private final InvoiceTemplateTypeMapper mapper = new InvoiceTemplateTypeMapper();
    private final BaseMapper<InvoiceTemplateType, InvoiceTemplateTypeDTO> mapper = new BaseMapper(InvoiceTemplateType.class, InvoiceTemplateTypeDTO.class);

    @Autowired
    private InvoiceTemplateTypeRepo repository;
    public static final Logger logger = Logger.getLogger(InvoiceTemplateTypeService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public InvoiceTemplateTypeDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<InvoiceTemplateTypeDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<InvoiceTemplateTypeDTO> findByFilter(List<FilterRequest> filters) throws Exception {
        OrderSpecifier<Long> sortByName = QInvoiceTemplateType.invoiceTemplateType.invoiceTemplateTypeId.desc();
        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters), sortByName));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage create(InvoiceTemplateTypeDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(InvoiceTemplateTypeDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @Override
    public List<InvoiceTemplateTypeDTO> getInvoiceTemplateType() throws Exception {
        List<FilterRequest> lst = Lists.newArrayList();
        lst.add(new FilterRequest(InvoiceTemplateType.COLUMNS.STATUS.name(), FilterRequest.Operator.EQ, Const.STATUS_ACTIVE));
        return findByFilter(lst);
    }
}
