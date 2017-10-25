package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.dto.PartnerContractDTO;
import com.viettel.bccs.inventory.model.PartnerContract;
import com.viettel.bccs.inventory.repo.PartnerContractRepo;
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
import java.util.List;

@Service
public class PartnerContractServiceImpl extends BaseServiceImpl implements PartnerContractService {

    private final BaseMapper<PartnerContract, PartnerContractDTO> mapper = new BaseMapper<>(PartnerContract.class, PartnerContractDTO.class);

    @Autowired
    private PartnerContractRepo repository;
    public static final Logger logger = Logger.getLogger(PartnerContractService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public PartnerContractDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<PartnerContractDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<PartnerContractDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage create(PartnerContractDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(PartnerContractDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @Override
    public PartnerContractDTO findByStockTransID(Long stockTransID) throws LogicException, Exception {
        List<FilterRequest> filterRequests = Lists.newArrayList(new FilterRequest(PartnerContract.COLUMNS.STOCKTRANSID.name(), FilterRequest.Operator.EQ, stockTransID));
        List<PartnerContractDTO> listResult = findByFilter(filterRequests);
        if (DataUtil.isNullOrEmpty(listResult)) {
            return null;
        }
        return listResult.get(0);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PartnerContractDTO save(PartnerContractDTO partnerContractDTO) throws Exception {
        return mapper.toDtoBean(repository.save(mapper.toPersistenceBean(partnerContractDTO)));
    }
}
