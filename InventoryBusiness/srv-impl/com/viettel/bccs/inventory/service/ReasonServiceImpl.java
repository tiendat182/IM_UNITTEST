package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.mysema.query.types.OrderSpecifier;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.ReasonDTO;
import com.viettel.bccs.inventory.model.QReason;
import com.viettel.bccs.inventory.model.Reason;
import com.viettel.bccs.inventory.repo.ReasonRepo;
import com.viettel.fw.Exception.LogicException;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class ReasonServiceImpl extends BaseServiceImpl implements ReasonService {

    private final BaseMapper<Reason, ReasonDTO> mapper = new BaseMapper(Reason.class, ReasonDTO.class);

    @Autowired
    private ReasonRepo repository;
    public static final Logger logger = Logger.getLogger(ReasonService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public ReasonDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<ReasonDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<ReasonDTO> findByFilter(List<FilterRequest> filters) throws Exception {
        OrderSpecifier<Short> sortByName = QReason.reason.haveMoney.desc();
        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters), sortByName));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage create(ReasonDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(ReasonDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @WebMethod
    @Override
    public List<ReasonDTO> getLsReasonByType(String reasonType) throws LogicException, Exception {
        List<FilterRequest> requests = Lists.newArrayList(new FilterRequest(Reason.COLUMNS.REASONTYPE.name(), FilterRequest.Operator.EQ, reasonType),
                new FilterRequest(Reason.COLUMNS.REASONSTATUS.name(), FilterRequest.Operator.EQ, Const.STATUS.ACTIVE));
        List<ReasonDTO> result = findByFilter(requests);
        Collections.sort(result, new Comparator<ReasonDTO>() {
            @Override
            public int compare(ReasonDTO o1, ReasonDTO o2) {
                if (DataUtil.isNullOrEmpty(o1.getReasonName())) {
                    return -1;
                }
                if (DataUtil.isNullOrEmpty(o2.getReasonName())) {
                    return -1;
                }
                return o1.getReasonName().compareTo(o2.getReasonName());
            }
        });
        return result;
    }

    @WebMethod
    @Override
    public List<ReasonDTO> getLsReasonByCode(String reasonCode) throws LogicException, Exception {
        List<FilterRequest> requests = Lists.newArrayList(new FilterRequest(Reason.COLUMNS.REASONCODE.name(), FilterRequest.Operator.EQ, reasonCode),
                new FilterRequest(Reason.COLUMNS.REASONSTATUS.name(), FilterRequest.Operator.EQ, Const.STATUS.ACTIVE));
        return findByFilter(requests);
    }

    @Override
    public ReasonDTO getReason(String reasonCode, String reasonType) throws LogicException, Exception {
        List<FilterRequest> requests = Lists.newArrayList(
                new FilterRequest(Reason.COLUMNS.REASONCODE.name(), FilterRequest.Operator.EQ, reasonCode),
                new FilterRequest(Reason.COLUMNS.REASONTYPE.name(), FilterRequest.Operator.EQ, reasonType),
                new FilterRequest(Reason.COLUMNS.REASONSTATUS.name(), FilterRequest.Operator.EQ, Const.STATUS.ACTIVE));
        List<ReasonDTO> lstReason = findByFilter(requests);
        if (!DataUtil.isNullOrEmpty(lstReason)) {
            return lstReason.get(0);
        }
        return null;
    }
}
