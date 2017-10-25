package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.ProductErpDTO;
import com.viettel.bccs.inventory.model.ProductErp;
import com.viettel.bccs.inventory.repo.ProductErpRepo;
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
public class ProductErpServiceImpl extends BaseServiceImpl implements ProductErpService {

    private final BaseMapper<ProductErp, ProductErpDTO> mapper = new BaseMapper<>(ProductErp.class, ProductErpDTO.class);

    @Autowired
    private ProductErpRepo repository;
    public static final Logger logger = Logger.getLogger(ProductErpService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public ProductErpDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<ProductErpDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<ProductErpDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage create(ProductErpDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(ProductErpDTO dto) throws Exception {
        throw new NotImplementedException();
    }
}
