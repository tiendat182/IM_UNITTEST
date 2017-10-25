package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.mysema.query.types.OrderSpecifier;
import com.viettel.bccs.inventory.dto.ManageTransStockDto;
import com.viettel.bccs.inventory.dto.StockIsdnTransDTO;
import com.viettel.bccs.inventory.model.QStockIsdnTrans;
import com.viettel.bccs.inventory.model.StockIsdnTrans;
import com.viettel.bccs.inventory.repo.StockIsdnTransRepo;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DateUtil;
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
import java.util.Date;
import java.util.List;

@Service
public class StockIsdnTransServiceImpl extends BaseServiceImpl implements StockIsdnTransService {

    private final BaseMapper<StockIsdnTrans, StockIsdnTransDTO> mapper = new BaseMapper<>(StockIsdnTrans.class, StockIsdnTransDTO.class);

    @Autowired
    private StockIsdnTransRepo repository;
    public static final Logger logger = Logger.getLogger(StockIsdnTransService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public StockIsdnTransDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<StockIsdnTransDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<StockIsdnTransDTO> findByFilter(List<FilterRequest> filters) throws Exception {
        OrderSpecifier<Date> sortByUpdateTime = QStockIsdnTrans.stockIsdnTrans.createdTime.desc();
        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters), sortByUpdateTime));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public StockIsdnTransDTO create(StockIsdnTransDTO dto) throws Exception {
        return mapper.toDtoBean(repository.save(mapper.toPersistenceBean(dto)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(StockIsdnTransDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @Override
    public List<StockIsdnTransDTO> search(ManageTransStockDto searchDTO) throws Exception {
        List<FilterRequest> filters = Lists.newArrayList();
        if (!DataUtil.isNullObject(searchDTO.getTransCode())) {
            FilterRequest rq1 = new FilterRequest(StockIsdnTrans.COLUMNS.STOCKISDNTRANSCODE.name(), FilterRequest.Operator.LIKE, searchDTO.getTransCode(), false);
            filters.add(rq1);
        }
        if (!DataUtil.isNullObject(searchDTO.getFromDate())) {
            FilterRequest rq2 = new FilterRequest(StockIsdnTrans.COLUMNS.CREATEDTIME.name(), FilterRequest.Operator.GOE, DateUtil.date2ddMMyyyyString(searchDTO.getFromDate()), false);
            filters.add(rq2);
        }
        if (!DataUtil.isNullObject(searchDTO.getToDate())) {
            FilterRequest rq3 = new FilterRequest(StockIsdnTrans.COLUMNS.CREATEDTIME.name(), FilterRequest.Operator.LOE, DateUtil.date2ddMMyyyyString(searchDTO.getToDate()), false);
            filters.add(rq3);
        }
        if (!DataUtil.isNullObject(searchDTO.getTypeTakeOwner())) {
            FilterRequest rq4 = new FilterRequest(StockIsdnTrans.COLUMNS.FROMOWNERTYPE.name(), FilterRequest.Operator.EQ, searchDTO.getTypeTakeOwner());
            filters.add(rq4);
        }
        if (!DataUtil.isNullObject(searchDTO.getTakeOwnerStockId())) {
            FilterRequest rq5 = new FilterRequest(StockIsdnTrans.COLUMNS.FROMOWNERID.name(), FilterRequest.Operator.EQ, searchDTO.getTakeOwnerStockId());
            filters.add(rq5);
        }
        if (!DataUtil.isNullObject(searchDTO.getTypeReceiveOwner())) {
            FilterRequest rq6 = new FilterRequest(StockIsdnTrans.COLUMNS.TOOWNERTYPE.name(), FilterRequest.Operator.EQ, searchDTO.getTypeReceiveOwner());
            filters.add(rq6);
        }
        if (!DataUtil.isNullObject(searchDTO.getReceiveOwnerStockId())) {
            FilterRequest rq7 = new FilterRequest(StockIsdnTrans.COLUMNS.TOOWNERID.name(), FilterRequest.Operator.EQ, searchDTO.getReceiveOwnerStockId());
            filters.add(rq7);
        }
        if (!DataUtil.isNullObject(searchDTO.getOwnerCreateId())) {
            FilterRequest rq8 = new FilterRequest(StockIsdnTrans.COLUMNS.CREATEDUSERID.name(), FilterRequest.Operator.EQ, searchDTO.getOwnerCreateId());
            filters.add(rq8);
        }
        if (!DataUtil.isNullObject(searchDTO.getReasonId())) {
            FilterRequest rq9 = new FilterRequest(StockIsdnTrans.COLUMNS.REASONID.name(), FilterRequest.Operator.EQ, searchDTO.getReasonId());
            filters.add(rq9);
        }
        if (!DataUtil.isNullObject(searchDTO.getNotes())) {
            FilterRequest rq9 = new FilterRequest(StockIsdnTrans.COLUMNS.NOTE.name(), FilterRequest.Operator.LIKE, searchDTO.getNotes(), false);
            filters.add(rq9);
        }
        return findByFilter(filters);
    }

    @Override
    public List<StockIsdnTransDTO> searchHistory(ManageTransStockDto searchDTO) throws LogicException, Exception {
        return mapper.toDtoBean(repository.searchHistory(searchDTO));
    }
}
