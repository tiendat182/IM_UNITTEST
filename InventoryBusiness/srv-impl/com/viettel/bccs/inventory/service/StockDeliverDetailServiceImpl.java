package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.repo.StockDeliverDetailRepo;
import com.viettel.bccs.inventory.dto.StockDeliverDetailDTO;
import com.viettel.bccs.inventory.model.StockDeliverDetail;
import com.viettel.bccs.inventory.service.StockDeliverDetailService;
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
public class StockDeliverDetailServiceImpl extends BaseServiceImpl implements StockDeliverDetailService {

    private final BaseMapper<StockDeliverDetail, StockDeliverDetailDTO> mapper = new BaseMapper<>(StockDeliverDetail.class, StockDeliverDetailDTO.class);

    @Autowired
    private StockDeliverDetailRepo repository;
    public static final Logger logger = Logger.getLogger(StockDeliverDetailService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public StockDeliverDetailDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<StockDeliverDetailDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<StockDeliverDetailDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public StockDeliverDetailDTO save(StockDeliverDetailDTO dto) throws Exception {
        return mapper.toDtoBean(repository.save(mapper.toPersistenceBean(dto)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(StockDeliverDetailDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    public List<StockDeliverDetailDTO> viewStockTotalFull(Long ownerId, Long ownerType) throws Exception {
        return repository.viewStockTotalFull(ownerId, ownerType);
    }

    public List<StockDeliverDetailDTO> getLstDetailByStockDeliverId(Long stockDeliverId) throws Exception {
        return repository.getLstDetailByStockDeliverId(stockDeliverId);
    }
}
