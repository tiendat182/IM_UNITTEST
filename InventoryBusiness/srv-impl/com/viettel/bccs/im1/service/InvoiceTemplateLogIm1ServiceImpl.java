package com.viettel.bccs.im1.service;

import com.viettel.bccs.im1.dto.InvoiceTemplateLogIm1DTO;
import com.viettel.bccs.im1.model.InvoiceTemplateLogIm1;
import com.viettel.bccs.im1.repo.InvoiceTemplateLogIm1Repo;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.ErrorCode;
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
public class InvoiceTemplateLogIm1ServiceImpl extends BaseServiceImpl implements InvoiceTemplateLogIm1Service {

    private final BaseMapper<InvoiceTemplateLogIm1, InvoiceTemplateLogIm1DTO> mapper = new BaseMapper<>(InvoiceTemplateLogIm1.class, InvoiceTemplateLogIm1DTO.class);

    @Autowired
    private InvoiceTemplateLogIm1Repo repository;
    public static final Logger logger = Logger.getLogger(InvoiceTemplateLogIm1Service.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public InvoiceTemplateLogIm1DTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<InvoiceTemplateLogIm1DTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<InvoiceTemplateLogIm1DTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage create(InvoiceTemplateLogIm1DTO dto) throws Exception {
        BaseMessage baseMessage = new BaseMessage(false);
        try {
            InvoiceTemplateLogIm1 invoiceTemplateLog = mapper.toPersistenceBean(dto);
            repository.save(invoiceTemplateLog);
            //Luwu log vao bang action_log
            baseMessage.setSuccess(true);
            return baseMessage;
        } catch (Exception ex) {
            logger.error("Error: ", ex);
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "Error Exception");
        }
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(InvoiceTemplateLogIm1DTO dto) throws Exception {
        throw new NotImplementedException();
    }
}
