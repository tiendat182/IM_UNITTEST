package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.StockDebitDTO;
import com.viettel.bccs.inventory.dto.StockTransDTO;
import com.viettel.bccs.inventory.model.StockDebit;
import com.viettel.bccs.inventory.repo.StockDebitRepo;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
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
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
public class StockDebitServiceImpl extends BaseServiceImpl implements StockDebitService {

    private final BaseMapper<StockDebit, StockDebitDTO> mapper = new BaseMapper(StockDebit.class, StockDebitDTO.class);
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager emIM;

    public static final Logger logger = Logger.getLogger(StockDebitService.class);
    @Autowired
    private StockDebitRepo repository;

    @Autowired
    private OptionSetValueService optionSetValueService;

    @Autowired
    private StockTransService transService;

    @Autowired
    private StockTotalService stockTotalService;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public StockDebitDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<StockDebitDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<StockDebitDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage create(StockDebitDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(StockDebitDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @Override
    public List<StockDebitDTO> findStockDebit(StockDebitDTO stockDebitDTO) throws Exception {
        List<FilterRequest> filters = Lists.newArrayList();
        if (!DataUtil.isNullOrEmpty(stockDebitDTO.getDebitDayType())) {
            filters.add(new FilterRequest(StockDebit.COLUMNS.DEBITDAYTYPE.name(), FilterRequest.Operator.EQ, stockDebitDTO.getDebitDayType()));
        }
        if (!DataUtil.isNullOrEmpty(stockDebitDTO.getOwnerType())) {
            filters.add(new FilterRequest(StockDebit.COLUMNS.OWNERTYPE.name(), FilterRequest.Operator.EQ, stockDebitDTO.getOwnerType()));
        }
        if (!DataUtil.isNullObject(stockDebitDTO.getOwnerId())) {
            filters.add(new FilterRequest(StockDebit.COLUMNS.OWNERID.name(), FilterRequest.Operator.EQ, stockDebitDTO.getOwnerId()));
        }
        return findByFilter(filters);
    }

    @Override
    public Long totalPriceStock(Long ownerId, String ownerType) throws Exception {
        if (ownerId == null || DataUtil.isNullOrEmpty(ownerType)) {
            return null;
        }
        return repository.totalPriceStock(ownerId, ownerType);
    }

    @Override
    public StockDebitDTO findStockDebitValue(Long ownerId, String ownerType) throws Exception {
        if (ownerId == null) {
            return null;
        }
        //lay ra han muc kho
        StockDebitDTO stockDebitDTO = repository.findLimitStock(ownerId, ownerType, getSysDate(emIM));
        if (stockDebitDTO != null) {
            //lay ra tong gia tri hang trong kho nhan
            stockDebitDTO.setTotalValueReceiveStock(stockDebitDTO.getTotalPrice());
            //lay ra tong gia tri hang trong cac lenh treo cua kho nhan
            stockDebitDTO.setTotalValueTransSuspendReceiveStock(transService.getTotalValueStockSusbpendByOwnerId(ownerId, ownerType));
        }
        return stockDebitDTO;
    }

    @Override
    public StockDebitDTO findStockDebitByStockTrans(Long stockTransId) throws LogicException, Exception {
        if (DataUtil.isNullObject(stockTransId)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "stock.trans.search.validate.validStockTrans");
        }
        StockTransDTO stockTransDTO = transService.findOne(stockTransId);
        if (DataUtil.isNullObject(stockTransDTO)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.trans.search.validate.validStockTrans");
        }
        if (Const.OWNER_TYPE.SHOP.equals(DataUtil.safeToString(stockTransDTO.getToOwnerType())) || Const.OWNER_TYPE.STAFF.equals(DataUtil.safeToString(stockTransDTO.getToOwnerType()))) {
            return repository.findLimitStock(stockTransDTO.getToOwnerId(), DataUtil.safeToString(stockTransDTO.getToOwnerType()), stockTransDTO.getCreateDatetime());
        } else {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.trans.search.validate.validStockTrans");
        }
    }

    @Override
    public List<StockDebitDTO> findStockDebitNative(Long ownerId, String ownerType) throws Exception, LogicException {
        if (DataUtil.isNullOrEmpty(ownerType)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "validate.value.field.debit.day.type");
        }
        if (DataUtil.isNullOrZero(ownerId)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "limit.stock.lookup.invalid.ownerId");
        }
        if (Const.OWNER_TYPE.SHOP.equals(ownerType)) {
            return repository.searchStockDebitForUnit(ownerId);
        } else if (Const.OWNER_TYPE.STAFF.equals(ownerType)) {
            return repository.searchStockDebitForStaff(ownerId);
        } else {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "validate.value.field.debit.day.type");
        }
    }
}
