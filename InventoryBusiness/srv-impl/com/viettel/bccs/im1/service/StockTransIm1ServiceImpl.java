package com.viettel.bccs.im1.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.im1.dto.StockTransIm1DTO;
import com.viettel.bccs.im1.model.StockTransIm1;
import com.viettel.bccs.im1.repo.StockTransIm1Repo;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.fw.service.BaseServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.jws.WebMethod;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
public class StockTransIm1ServiceImpl extends BaseServiceImpl implements StockTransIm1Service {

    private final BaseMapper<StockTransIm1, StockTransIm1DTO> mapper = new BaseMapper<>(StockTransIm1.class, StockTransIm1DTO.class);

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM_LIB)
    private EntityManager emIm1;

    @Autowired
    private StockTransIm1Repo repository;
    public static final Logger logger = Logger.getLogger(StockTransIm1Service.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public StockTransIm1DTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<StockTransIm1DTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<StockTransIm1DTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public int updateStatusStockTrans(StockTransIm1DTO dto) throws Exception {
        return repository.updateStatusStockTrans(dto.getStockTransId(), dto.getStockTransStatus());
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public StockTransIm1DTO update(StockTransIm1DTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public StockTransIm1DTO save(StockTransIm1DTO dto) throws Exception {
        return mapper.toDtoBean(repository.save(mapper.toPersistenceBean(dto)));
    }

    public StockTransIm1DTO findOneLock(Long id) throws Exception {
        try {
            StockTransIm1 stockTrans = repository.findOne(id);
            if (!DataUtil.isNullObject(stockTrans)) {
                try {
                    emIm1.lock(stockTrans, LockModeType.PESSIMISTIC_READ);
                    return mapper.toDtoBean(stockTrans);
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.stockTrans.notExists.im1");
                }
            }
            return null;
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.stockTrans.notExists.im1");
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "common.error.happen");
        }
    }

    public StockTransIm1DTO findFromStockTransIdLock(Long id) throws Exception {
        try {
            List<FilterRequest> lst = Lists.newArrayList();
            lst.add(new FilterRequest(StockTransIm1.COLUMNS.FROMSTOCKTRANSID.name(), FilterRequest.Operator.EQ, id));
            List<StockTransIm1DTO> lstStockTrans = findByFilter(lst);
            if (!DataUtil.isNullOrEmpty(lstStockTrans)) {
                try {
                    StockTransIm1 stockTrans = repository.findOne(lstStockTrans.get(0).getStockTransId());
                    emIm1.lock(stockTrans, LockModeType.PESSIMISTIC_READ);
                    return mapper.toDtoBean(stockTrans);
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.stockTrans.notExists.im1");
                }
            }
            return null;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.stockTrans.notExists.im1");
        }
    }
}
