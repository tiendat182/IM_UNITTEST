package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.mysema.query.types.OrderSpecifier;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.SignFlowDetailDTO;
import com.viettel.bccs.inventory.model.QSignFlowDetail;
import com.viettel.bccs.inventory.model.SignFlowDetail;
import com.viettel.bccs.inventory.repo.SignFlowDetailRepo;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.ErrorCode;
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
public class SignFlowDetailServiceImpl extends BaseServiceImpl implements SignFlowDetailService {

    private final BaseMapper<SignFlowDetail, SignFlowDetailDTO> mapper = new BaseMapper(SignFlowDetail.class, SignFlowDetailDTO.class);

    @Autowired
    private SignFlowDetailRepo repository;
    public static final Logger logger = Logger.getLogger(SignFlowDetailService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public SignFlowDetailDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<SignFlowDetailDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<SignFlowDetailDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public SignFlowDetailDTO create(SignFlowDetailDTO dto) throws Exception,LogicException {
        try {
            SignFlowDetail signFlowDetail = mapper.toPersistenceBean(dto);
            signFlowDetail = repository.save(signFlowDetail);
            return mapper.toDtoBean(signFlowDetail);
        } catch (Exception ex) {
            logger.error("Error: ", ex);
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "common.error.happened");
        }
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public SignFlowDetailDTO update(SignFlowDetailDTO dto) throws Exception {
        try {
            SignFlowDetail signFlowDetail = mapper.toPersistenceBean(dto);
            signFlowDetail = repository.save(signFlowDetail);
            return mapper.toDtoBean(signFlowDetail);
        } catch (Exception ex) {
            logger.error("Error: ", ex);
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "common.error.happened");
        }
    }

    @WebMethod
    public List<SignFlowDetailDTO> findBySignFlowId(Long signFlowId) throws Exception {
        OrderSpecifier<Long> signOrder = QSignFlowDetail.signFlowDetail.signOrder.asc();
        List<FilterRequest> requests = Lists.newArrayList(new FilterRequest(SignFlowDetail.COLUMNS.SIGNFLOWID.name(),
                FilterRequest.Operator.EQ, signFlowId), new FilterRequest(SignFlowDetail.COLUMNS.STATUS.name(), FilterRequest.Operator.EQ, Const.STATUS.ACTIVE));
        return mapper.toDtoBean(repository.findAll(repository.toPredicate(requests), signOrder));
    }

    @WebMethod
    public List<SignFlowDetailDTO> findByVofficeRoleId(Long vofficeRoleId) throws Exception {
        OrderSpecifier<Long> signOrder = QSignFlowDetail.signFlowDetail.signOrder.asc();
        List<FilterRequest> requests = Lists.newArrayList(new FilterRequest(SignFlowDetail.COLUMNS.VOFFICEROLEID.name(),
                FilterRequest.Operator.EQ, vofficeRoleId), new FilterRequest(SignFlowDetail.COLUMNS.STATUS.name(), FilterRequest.Operator.EQ, Const.STATUS.ACTIVE));
        return mapper.toDtoBean(repository.findAll(repository.toPredicate(requests), signOrder));
    }
}
