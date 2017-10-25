package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.StockTransDocumentDTO;
import com.viettel.bccs.inventory.model.StockTransDocument;
import com.viettel.bccs.inventory.repo.StockTransDocumentRepo;
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
public class StockTransDocumentServiceImpl extends BaseServiceImpl implements StockTransDocumentService {

    private final BaseMapper<StockTransDocument, StockTransDocumentDTO> mapper = new BaseMapper<>(StockTransDocument.class, StockTransDocumentDTO.class);

    @Autowired
    private StockTransDocumentRepo repository;
    public static final Logger logger = Logger.getLogger(StockTransDocumentService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public StockTransDocumentDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<StockTransDocumentDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<StockTransDocumentDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage create(StockTransDocumentDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(StockTransDocumentDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StockTransDocumentDTO save(StockTransDocumentDTO stockTransDocumentDTO) throws Exception {
        return mapper.toDtoBean(repository.save(mapper.toPersistenceBean(stockTransDocumentDTO)));
    }

    @Override
    public byte[] getAttachFileContent(Long stockTransId) throws Exception {
        return repository.getAttachFileContent(stockTransId);
    }
}
