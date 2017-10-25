package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.mysema.query.types.OrderSpecifier;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.AreaDTO;
import com.viettel.bccs.inventory.model.Area;
import com.viettel.bccs.inventory.model.QArea;
import com.viettel.bccs.inventory.repo.AreaRepo;
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
public class AreaServiceImpl extends BaseServiceImpl implements AreaService {

    //    private final AreaMapper mapper = new AreaMapper();
    private final BaseMapper<Area, AreaDTO> mapper = new BaseMapper(Area.class, AreaDTO.class);

    @Autowired
    private AreaRepo repository;
    public static final Logger logger = Logger.getLogger(AreaService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public AreaDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<AreaDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<AreaDTO> findByFilter(List<FilterRequest> filters) throws Exception {
        OrderSpecifier<String> sortByName = QArea.area.name.asc();
        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters), sortByName));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage create(AreaDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(AreaDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @Override
    public List<AreaDTO> getAllProvince() throws Exception {
        return repository.getAllProvince();
    }

    @Override
    public AreaDTO findByCode(String areaCode) throws Exception {
        if(DataUtil.isNullOrEmpty(areaCode)){
            return null;
        }
        List<FilterRequest> requests = Lists.newArrayList();
        requests.add( new FilterRequest(Area.COLUMNS.AREACODE.name(),FilterRequest.Operator.EQ,areaCode));
        requests.add( new FilterRequest(Area.COLUMNS.STATUS.name(),FilterRequest.Operator.EQ, Const.STATUS_ACTIVE));
        List<AreaDTO> lits = findByFilter(requests);
        if(DataUtil.isNullOrEmpty(lits)){
            return null;
        }
        return lits.get(0);
    }

    @Override
    public List<AreaDTO> search(AreaDTO searchDto) throws Exception {
        return null;
    }
}
