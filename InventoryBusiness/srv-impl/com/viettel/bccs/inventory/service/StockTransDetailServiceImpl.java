package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.dto.StockTransDetailDTO;
import com.viettel.bccs.inventory.model.StockTransDetail;
import com.viettel.bccs.inventory.repo.StockTransDetailRepo;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.fw.service.BaseServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.jws.WebMethod;
import java.util.List;

@Service
public class StockTransDetailServiceImpl extends BaseServiceImpl implements StockTransDetailService {

    private final BaseMapper<StockTransDetail, StockTransDetailDTO> mapper = new BaseMapper(StockTransDetail.class, StockTransDetailDTO.class);

    @Autowired
    private StockTransDetailRepo repository;
    public static final Logger logger = Logger.getLogger(StockTransDetailService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public StockTransDetailDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<StockTransDetailDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<StockTransDetailDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public StockTransDetailDTO save(StockTransDetailDTO stockTransDetailDTO) throws Exception {

        return mapper.toDtoBean(repository.save(mapper.toPersistenceBean(stockTransDetailDTO)));
    }

    @WebMethod
    public List<StockTransDetailDTO> findByStockTransId(Long stockTransId) throws Exception {
        return repository.getDetailByStockTransId(stockTransId);
    }

    @WebMethod
    public StockTransDetailDTO getSingleDetail(Long stockTransId, Long prodOfferId, Long stateId) throws Exception {
        List<FilterRequest> lst = Lists.newArrayList();
        lst.add(new FilterRequest(StockTransDetail.COLUMNS.STOCKTRANSID.name(), FilterRequest.Operator.EQ, stockTransId));
        lst.add(new FilterRequest(StockTransDetail.COLUMNS.PRODOFFERID.name(), FilterRequest.Operator.EQ, prodOfferId));
        lst.add(new FilterRequest(StockTransDetail.COLUMNS.STATEID.name(), FilterRequest.Operator.EQ, stateId));
        List<StockTransDetailDTO> lstDetail = findByFilter(lst);
        if (!DataUtil.isNullOrEmpty(lstDetail)) {
            return lstDetail.get(0);
        }
        return null;
    }
}
