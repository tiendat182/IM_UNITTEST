package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.repo.ChangeModelTransRepo;
import com.viettel.bccs.inventory.model.ChangeModelTrans;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DateUtil;
import com.viettel.fw.common.util.RequiredRoleMap;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.fw.common.util.extjs.FilterRequest;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.viettel.fw.service.BaseServiceImpl;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.jws.WebMethod;

import org.springframework.transaction.annotation.Transactional;


@Service
public class ChangeModelTransServiceImpl extends BaseServiceImpl implements ChangeModelTransService {

    private final BaseMapper<ChangeModelTrans, ChangeModelTransDTO> mapper = new BaseMapper<>(ChangeModelTrans.class, ChangeModelTransDTO.class);

    @Autowired
    private ChangeModelTransRepo repository;
    @Autowired
    private ChangeModelTransBaseService changeModelTransBaseService;
    @Autowired
    private StaffService staffService;

    public static final Logger logger = Logger.getLogger(ChangeModelTransService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public ChangeModelTransDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<ChangeModelTransDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<ChangeModelTransDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public ChangeModelTransDTO save(ChangeModelTransDTO dto) throws Exception {
        ChangeModelTrans changeModelTrans = repository.save(mapper.toPersistenceBean(dto));
        return mapper.toDtoBean(changeModelTrans);
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(ChangeModelTransDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    public List<ApproveChangeProductDTO> getLstChangeStockModel(ApproveChangeProductDTO searchInputDTO) throws LogicException, Exception {
        if (DataUtil.isNullObject(searchInputDTO.getStartDate()) || DataUtil.isNullObject(searchInputDTO.getEndDate())) {
            throw new LogicException("", "stock.rescue.warranty.date.require");
        }
        if (DateUtil.compareDateToDate(searchInputDTO.getStartDate(), searchInputDTO.getEndDate()) > 0) {
            throw new LogicException("", "stock.trans.from.than.to");
        }
        return repository.getLstChangeStockModel(searchInputDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void approveChangeProductKit(Long changeModelTransId, Long staffId, StockTransDTO stockTransVoficeDTO, RequiredRoleMap requiredRoleMap) throws LogicException, Exception {
        changeModelTransBaseService.approveChangeProductKit(changeModelTransId, staffId, stockTransVoficeDTO, requiredRoleMap);
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public void approveChangeProduct(Long changeModelTransId, StaffDTO approveStaffDTO, StockTransDTO stockTransVoficeDTO, RequiredRoleMap requiredRoleMap) throws LogicException, Exception {
        changeModelTransBaseService.approveChangeProduct(changeModelTransId, approveStaffDTO, stockTransVoficeDTO, requiredRoleMap);
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public void cancelChangeProduct(Long changeModelTransId, StaffDTO approveStaffDTO) throws LogicException, Exception {
        changeModelTransBaseService.cancelChangeProduct(changeModelTransId, approveStaffDTO);
    }

    public List<ChangeModelTrans> getLstCancelRequestThread(Long maxDay) throws LogicException, Exception {
        return repository.getLstCancelRequestThread(maxDay);
    }

    public List<ChangeModelTrans> getLstApproveRequestThread(Long maxDay) throws LogicException, Exception {
        return repository.getLstApproveRequestThread(maxDay);
    }

    @Transactional(rollbackFor = Exception.class)
    public void processCancelRequest(Long changeModelTransId) throws LogicException, Exception {
        changeModelTransBaseService.processCancelRequest(changeModelTransId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void processApproveRequest(Long changeModelTransId) throws LogicException, Exception {
        changeModelTransBaseService.processApproveRequest(changeModelTransId);
    }
    
     @Override
    public ChangeModelTransDTO updateChangeModelTransStatus(Long changeModelTransId, Long changeModelTransStatus) throws Exception, LogicException {
        ChangeModelTransDTO changeModelTransDTO = findOne(changeModelTransId);
        if (DataUtil.isNullObject(changeModelTransDTO)) {
            throw new LogicException("", getText("mn.approve.product.exchange.changeModelTrans.Id.err"));
        }
        changeModelTransDTO.setStatus(changeModelTransStatus);
        ChangeModelTransDTO changeModelTransSaveDTO = save(changeModelTransDTO);
        return changeModelTransSaveDTO;
    }
}
