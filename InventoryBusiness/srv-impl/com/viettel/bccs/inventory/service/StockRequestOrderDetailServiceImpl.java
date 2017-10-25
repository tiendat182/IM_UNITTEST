package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.StockRequestOrderDetailDTO;
import com.viettel.bccs.inventory.model.StockRequestOrderDetail;
import com.viettel.bccs.inventory.repo.StockRequestOrderDetailRepo;
import com.viettel.fw.common.util.DataUtil;
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
public class StockRequestOrderDetailServiceImpl extends BaseServiceImpl implements StockRequestOrderDetailService {

    private final BaseMapper<StockRequestOrderDetail, StockRequestOrderDetailDTO> mapper = new BaseMapper<>(StockRequestOrderDetail.class, StockRequestOrderDetailDTO.class);

    @Autowired
    private StockRequestOrderDetailRepo repository;
    public static final Logger logger = Logger.getLogger(StockRequestOrderDetailService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public StockRequestOrderDetailDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<StockRequestOrderDetailDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<StockRequestOrderDetailDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public StockRequestOrderDetailDTO save(StockRequestOrderDetailDTO dto) throws Exception {
        return mapper.toDtoBean(repository.save(mapper.toPersistenceBean(dto)));
    }

    public List<StockRequestOrderDetailDTO> findSearchStockRequestOrder(Long stockRequestOrderId, String status) throws Exception {
        return repository.getListStockRequestOrderDetail(stockRequestOrderId, status);
    }

    public List<StockRequestOrderDetailDTO> getLstByStockRequestId(Long stockRequestOrderId, Long fromOwnerId, Long toOwnerId) throws Exception {
        List<FilterRequest> lst = Lists.newArrayList();
        lst.add(new FilterRequest(StockRequestOrderDetail.COLUMNS.STOCKREQUESTORDERID.name(), FilterRequest.Operator.EQ, stockRequestOrderId));
        lst.add(new FilterRequest(StockRequestOrderDetail.COLUMNS.FROMOWNERID.name(), FilterRequest.Operator.EQ, fromOwnerId));
        lst.add(new FilterRequest(StockRequestOrderDetail.COLUMNS.TOOWNERID.name(), FilterRequest.Operator.EQ, toOwnerId));
        lst.add(new FilterRequest(StockRequestOrderDetail.COLUMNS.APPROVEDQUANTITY.name(), FilterRequest.Operator.GT, 0L));
        return findByFilter(lst);
    }

    public List<StockRequestOrderDetailDTO> getOrderDetailByStockTransId(Long stockTransId) throws Exception {
        List<FilterRequest> lst = Lists.newArrayList();
        lst.add(new FilterRequest(StockRequestOrderDetail.COLUMNS.EXPORTTRANSID.name(), FilterRequest.Operator.EQ, stockTransId));
        List<StockRequestOrderDetailDTO> lstResult = findByFilter(lst);
        return lstResult;
    }

    public List<StockRequestOrderDetailDTO> getLstDetailTemplate(Long stockRequestOrderId) throws Exception {
        return repository.getLstDetailTemplate(stockRequestOrderId);
    }

    public List<StockRequestOrderDetailDTO> getLstDetailToExport() throws Exception {
        return repository.getLstDetailToExport();
    }

    @Transactional(rollbackFor = Exception.class)
    public int updateCancelNote(Long stockRequestOrderId) throws Exception {
        return repository.updateCancelNote(stockRequestOrderId);
    }
}
