package com.viettel.bccs.sale.service;

import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.sale.dto.SaleTransDetailDTO;
import com.viettel.bccs.sale.model.SaleTransDetail;
import com.viettel.bccs.sale.repo.SaleTransDetailRepo;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.fw.service.BaseServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.jws.WebMethod;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SaleTransDetailServiceImpl extends BaseServiceImpl implements SaleTransDetailService {

    private final BaseMapper<SaleTransDetail, SaleTransDetailDTO> mapper = new BaseMapper<>(SaleTransDetail.class, SaleTransDetailDTO.class);

    @Autowired
    private SaleTransDetailRepo repository;

    public static final Logger logger = Logger.getLogger(SaleTransDetailService.class);

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_SALE)
    private EntityManager em;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public SaleTransDetailDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    @Override
    public List<SaleTransDetailDTO> findBySaleTransId(Long saleTransId) throws Exception {
        if (DataUtil.isNullOrZero(saleTransId)) {
            return new ArrayList<>();
        }
        return mapper.toDtoBean(repository.findBySaleTransId(saleTransId));
    }

    @Override
    public List<SaleTransDetailDTO> findSaleTransDetails(Long saleTransId, Long prodOfferId, Date saleTransDate) throws Exception {
        return mapper.toDtoBean(repository.findSaleTransDetails(saleTransId, prodOfferId, saleTransDate));
    }


    @WebMethod
    public List<SaleTransDetailDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<SaleTransDetailDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void save(SaleTransDetailDTO saleTransDetailDTO) throws LogicException, Exception {
        repository.save(mapper.toPersistenceBean(saleTransDetailDTO));
    }

    public BaseMapper<SaleTransDetail, SaleTransDetailDTO> getMapper() {
        return mapper;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public SaleTransDetailDTO update(SaleTransDetailDTO saleTransDetailDTO) throws Exception {
        return mapper.toDtoBean(repository.saveAndFlush(mapper.toPersistenceBean(saleTransDetailDTO)));
    }
}