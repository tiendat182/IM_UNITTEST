package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.mysema.query.types.OrderSpecifier;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.model.QStockTransAction;
import com.viettel.bccs.inventory.model.StockTransAction;
import com.viettel.bccs.inventory.repo.StockTransActionRepo;
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
import java.util.Date;
import java.util.List;

@Service
public class StockTransActionServiceImpl extends BaseServiceImpl implements StockTransActionService {

    private final BaseMapper<StockTransAction, StockTransActionDTO> mapper = new BaseMapper(StockTransAction.class, StockTransActionDTO.class);

    @Autowired
    private StockTransActionRepo repository;
    @Autowired
    private StockTransService stockTransService;
    public static final Logger logger = Logger.getLogger(StockTransActionService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public StockTransActionDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<StockTransActionDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<StockTransActionDTO> findByFilter(List<FilterRequest> filters) throws Exception {
        OrderSpecifier<Date> sortByCreateDate = QStockTransAction.stockTransAction.createDatetime.desc();
        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters), sortByCreateDate));
    }

    @WebMethod

    @Transactional(rollbackFor = Exception.class)
    public StockTransActionDTO save(StockTransActionDTO stockTransActionDTO) throws Exception {

        return mapper.toDtoBean(repository.saveAndFlush(mapper.toPersistenceBean(stockTransActionDTO)));
    }

    @Override
    public Long getSequence() throws Exception {
        return repository.getSequence();
    }

    @Override
    public boolean checkExist(List<FieldExportFileDTO> lstFieldExportIsdn) throws LogicException, Exception {
        for (FieldExportFileDTO dto : lstFieldExportIsdn) {
            List<FilterRequest> requests = Lists.newArrayList();
            requests.add(new FilterRequest(StockTransAction.COLUMNS.ACTIONCODE.name(), FilterRequest.Operator.EQ, dto.getActionCode()));
            List<StockTransActionDTO> lstResult = findByFilter(requests);
            if (!DataUtil.isNullOrEmpty(lstResult)) {
                for (StockTransActionDTO stockTransActionDTO : lstResult) {
//                    if(DataUtil.safeEqual(stockTransDTO.getToOwnerId(), dto.getToOwnerId())){
//                        throw new LogicException("", "mn.isdn.manage.create.field.err.action.exist");
//                    }
                }
            }
        }
        return false;
    }

    @Override
    public StockTransActionDTO getStockTransActionDTOById(Long actionId) throws LogicException, Exception {
        List<FilterRequest> requests = Lists.newArrayList();
        StockTransActionDTO result = new StockTransActionDTO();
        requests.add(new FilterRequest(StockTransAction.COLUMNS.STOCKTRANSACTIONID.name(), FilterRequest.Operator.EQ, actionId));
        List<StockTransActionDTO> lstResult = findByFilter(requests);
        if (!DataUtil.isNullOrEmpty(lstResult)) {
            result = lstResult.get(0);
        }
        return result;
    }

    @Override
    public StockTransActionDTO findStockTransActionDTO(StockTransActionDTO stockTransActionDTO) {
        return mapper.toDtoBean(repository.findStockTransAction(stockTransActionDTO));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertStockTransActionNative(StockTransActionDTO stockTransActionDTO) throws Exception {
        repository.insertStockTransActionNative(stockTransActionDTO);
    }

    @Override
    public List<StockTransActionDTO> getStockTransActionByToOwnerId(Long toOnwerId, Long toOwnerType) throws Exception {
        return mapper.toDtoBean(repository.getStockTransActionByToOwnerId(toOnwerId, toOwnerType));
    }

    public StockTransActionDTO getStockTransActionByIdAndStatus(Long stockTransId, List<String> lstStatus) throws Exception {
        return mapper.toDtoBean(repository.getStockTransActionByIdAndStatus(stockTransId, lstStatus));
    }

    public int unlockUserInfo(Long stockTransActionId) throws Exception {
        return repository.unlockUserInfo(stockTransActionId);
    }

    public int deleteStockTransActionByIdAndStatus(Long stockTransId, List<String> lstStatus) throws Exception {
        return repository.deleteStockTransActionByIdAndStatus(stockTransId, lstStatus);
    }

    public List<StockDeliveringBillDetailDTO> getStockDeliveringResult(Date startTime, Date endTime) throws LogicException, Exception {
        return repository.getStockDeliveringResult(startTime, endTime);
    }

    @Override
    public List<VActionGoodsStatisticDTO> getListVActionStatistic(String actionCode, Long ownerId, Long ownerType, Long ownerLoginId,
                                                                  Long ownerLoginType, Date fromDate, Date toDate, String transType, String typeOrderNote) throws LogicException, Exception {
        return repository.getListVActionStatistic(actionCode, ownerId, ownerType, ownerLoginId, ownerLoginType, fromDate, toDate, transType, typeOrderNote);
    }

    public int deleteStockTransAction(Long stockTransActionId) throws Exception {
        return repository.deleteStockTransAction(stockTransActionId);
    }
}
