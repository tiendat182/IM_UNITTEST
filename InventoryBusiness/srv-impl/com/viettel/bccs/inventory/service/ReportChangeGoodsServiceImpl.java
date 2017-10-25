package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.ReportChangeGoodsDTO;
import com.viettel.bccs.inventory.model.ReportChangeGoods;
import com.viettel.bccs.inventory.repo.ReportChangeGoodsRepo;
import com.viettel.fw.Exception.LogicException;
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
import java.util.List;

@Service
public class ReportChangeGoodsServiceImpl extends BaseServiceImpl implements ReportChangeGoodsService {

    private final BaseMapper<ReportChangeGoods, ReportChangeGoodsDTO> mapper = new BaseMapper<>(ReportChangeGoods.class, ReportChangeGoodsDTO.class);

    @Autowired
    private ReportChangeGoodsRepo repository;
    public static final Logger logger = Logger.getLogger(ReportChangeGoodsService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public ReportChangeGoodsDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<ReportChangeGoodsDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<ReportChangeGoodsDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public ReportChangeGoodsDTO create(ReportChangeGoodsDTO dto) throws Exception {
        try {
            ReportChangeGoods reportChangeGoods = repository.save(mapper.toPersistenceBean(dto));
            return mapper.toDtoBean(reportChangeGoods);
        } catch (Exception ex) {
            logger.error("Error: ", ex);
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "common.error.happened");
        }
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(ReportChangeGoodsDTO dto) throws Exception {
        throw new NotImplementedException();
    }
}
