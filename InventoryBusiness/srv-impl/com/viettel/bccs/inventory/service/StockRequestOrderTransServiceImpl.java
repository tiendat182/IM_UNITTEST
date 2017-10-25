package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.repo.StockRequestOrderTransRepo;
import com.viettel.bccs.inventory.dto.StockRequestOrderTransDTO;
import com.viettel.bccs.inventory.model.StockRequestOrderTrans;
import com.viettel.bccs.inventory.service.StockRequestOrderTransService;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.fw.common.util.extjs.FilterRequest;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.viettel.fw.service.BaseServiceImpl;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.jws.WebMethod;

import org.springframework.transaction.annotation.Transactional;


@Service
public class StockRequestOrderTransServiceImpl extends BaseServiceImpl implements StockRequestOrderTransService {

    private final BaseMapper<StockRequestOrderTrans, StockRequestOrderTransDTO> mapper = new BaseMapper<>(StockRequestOrderTrans.class, StockRequestOrderTransDTO.class);

    @Autowired
    private StockRequestOrderTransRepo repository;
    public static final Logger logger = Logger.getLogger(StockRequestOrderTransService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public StockRequestOrderTransDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<StockRequestOrderTransDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<StockRequestOrderTransDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public StockRequestOrderTransDTO save(StockRequestOrderTransDTO dto) throws Exception {
        return mapper.toDtoBean(repository.save(mapper.toPersistenceBean(dto)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(StockRequestOrderTransDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    public StockRequestOrderTransDTO getOrderTrans(Long stockRequestOrderId, Long fromOwnerId, Long toOwnerId, Long type) throws Exception {
        List<FilterRequest> lst = Lists.newArrayList();
        lst.add(new FilterRequest(StockRequestOrderTrans.COLUMNS.STOCKREQUESTORDERID.name(), FilterRequest.Operator.EQ, stockRequestOrderId));
        lst.add(new FilterRequest(StockRequestOrderTrans.COLUMNS.FROMOWNERID.name(), FilterRequest.Operator.EQ, fromOwnerId));
        lst.add(new FilterRequest(StockRequestOrderTrans.COLUMNS.TOOWNERID.name(), FilterRequest.Operator.EQ, toOwnerId));
        lst.add(new FilterRequest(StockRequestOrderTrans.COLUMNS.STOCKTRANSTYPE.name(), FilterRequest.Operator.EQ, type));
        List<StockRequestOrderTransDTO> lstResult = findByFilter(lst);
        if (!DataUtil.isNullOrEmpty(lstResult)) {
            return lstResult.get(0);
        }
        return null;
    }

    public StockRequestOrderTransDTO getOrderTransByStockTransId(Long stockTransId) throws Exception {
        List<FilterRequest> lst = Lists.newArrayList();
        lst.add(new FilterRequest(StockRequestOrderTrans.COLUMNS.STOCKTRANSID.name(), FilterRequest.Operator.EQ, stockTransId));
        List<StockRequestOrderTransDTO> lstResult = findByFilter(lst);
        if (!DataUtil.isNullOrEmpty(lstResult)) {
            return lstResult.get(0);
        }
        return null;
    }
}
