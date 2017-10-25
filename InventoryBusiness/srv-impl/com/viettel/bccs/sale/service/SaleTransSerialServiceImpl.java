package com.viettel.bccs.sale.service;

import com.viettel.bccs.sale.dto.SaleTransSerialDTO;
import com.viettel.bccs.sale.model.SaleTransSerial;
import com.viettel.bccs.sale.repo.SaleTransSerialRepo;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.common.util.mapper.BaseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.jws.WebMethod;
import java.util.Date;
import java.util.List;

/**
 * Created by PM2-LAMNV5 on 12/23/2015.
 */
@Service
public class SaleTransSerialServiceImpl implements SaleTransSerialService {
    @Autowired
    private SaleTransSerialRepo repository;

    private final BaseMapper<SaleTransSerial, SaleTransSerialDTO> mapper = new BaseMapper<>(SaleTransSerial.class, SaleTransSerialDTO.class);

    @Override
    public void save(SaleTransSerialDTO saleTransSerial) {
        if (saleTransSerial == null) {
            return;
        }
        repo.save(mapper.toPersistenceBean(saleTransSerial));
    }

    @Autowired
    private SaleTransSerialRepo repo;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repo.count(repo.toPredicate(filters));
    }

    @WebMethod
    public SaleTransSerialDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repo.findOne(id));
    }

    @WebMethod
    public List<SaleTransSerialDTO> findAll() throws Exception {
        return mapper.toDtoBean(repo.findAll());
    }

    @WebMethod
    public List<SaleTransSerialDTO> findByFilter(List<FilterRequest> filters) throws Exception {
        return mapper.toDtoBean(repo.findAll(repo.toPredicate(filters)));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SaleTransSerialDTO createNewSaleTransSerialDTO(SaleTransSerialDTO saleTransDetailDTO) throws LogicException, Exception {
        return mapper.toDtoBean(repository.saveAndFlush(mapper.toPersistenceBean(saleTransDetailDTO)));
    }

    @Override
    public List<SaleTransSerialDTO> findByDetailId(Long saleTransDetailId, Date saleTransDate) {
        return mapper.toDtoBean(repo.findByDetailId(saleTransDetailId, saleTransDate));
    }

    @Override
    public SaleTransSerialDTO update(SaleTransSerialDTO saleTransDetailDTO) throws Exception {
        return mapper.toDtoBean(repository.saveAndFlush(mapper.toPersistenceBean(saleTransDetailDTO)));
    }
}
