package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.model.StockTransSerial;
import com.viettel.bccs.inventory.repo.ChangeModelTransDetailRepo;
import com.viettel.bccs.inventory.model.ChangeModelTransDetail;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.fw.common.util.extjs.FilterRequest;

import java.math.BigInteger;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.viettel.fw.service.BaseServiceImpl;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.jws.WebMethod;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.transaction.annotation.Transactional;


@Service
public class ChangeModelTransDetailServiceImpl extends BaseServiceImpl implements ChangeModelTransDetailService {

    private final BaseMapper<ChangeModelTransDetail, ChangeModelTransDetailDTO> mapper = new BaseMapper<>(ChangeModelTransDetail.class, ChangeModelTransDetailDTO.class);

    @Autowired
    private ChangeModelTransDetailRepo repository;
    @Autowired
    private ChangeModelTransService changeModelTransService;
    @Autowired
    private ChangeModelTransSerialService changeModelTransSerialService;
    @Autowired
    private ProductOfferingService productOfferingService;
    @Autowired
    private ProductOfferTypeService productOfferTypeService;

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    public static final Logger logger = Logger.getLogger(ChangeModelTransDetailService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public ChangeModelTransDetailDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<ChangeModelTransDetailDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<ChangeModelTransDetailDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public ChangeModelTransDetailDTO save(ChangeModelTransDetailDTO dto) throws Exception {
        ChangeModelTransDetail changeModelTransDetail = repository.save(mapper.toPersistenceBean(dto));
        return mapper.toDtoBean(changeModelTransDetail);
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(ChangeModelTransDetailDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    public List<ChangeModelTransDetailDTO> viewDetail(Long changeModelTransId) throws Exception {
        return repository.viewDetail(changeModelTransId);
    }

    public Long getNewProdOfferId(Long oldProdOfferId, Long changeModelTransId, Long stateId) throws Exception {
        List<FilterRequest> requests = Lists.newArrayList();
        requests.add(new FilterRequest(ChangeModelTransDetail.COLUMNS.CHANGEMODELTRANSID.name(), FilterRequest.Operator.EQ, changeModelTransId));
        requests.add(new FilterRequest(ChangeModelTransDetail.COLUMNS.OLDPRODOFFERID.name(), FilterRequest.Operator.EQ, oldProdOfferId));
        requests.add(new FilterRequest(ChangeModelTransDetail.COLUMNS.STATEID.name(), FilterRequest.Operator.EQ, stateId));
        List<ChangeModelTransDetailDTO> lst = findByFilter(requests);
        if (!DataUtil.isNullOrEmpty(lst)) {
            return lst.get(0).getNewProdOfferId();
        }
        return null;
    }

    public List<ChangeModelTransDetailDTO> getLstDetailByChangeModelTransId(Long changeModelTransId) throws Exception {
        List<FilterRequest> requests = Lists.newArrayList();
        requests.add(new FilterRequest(ChangeModelTransDetail.COLUMNS.CHANGEMODELTRANSID.name(), FilterRequest.Operator.EQ, changeModelTransId));
        List<ChangeModelTransDetailDTO> lstDetail = findByFilter(requests);
        return lstDetail;
    }

}
