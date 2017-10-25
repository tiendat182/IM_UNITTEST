package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.ThreadCloseStockDTO;
import com.viettel.bccs.inventory.model.ThreadCloseStock;
import com.viettel.bccs.inventory.repo.ThreadCloseStockRepo;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DateUtil;
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
import java.util.Date;
import java.util.List;

@Service
public class ThreadCloseStockServiceImpl extends BaseServiceImpl implements ThreadCloseStockService {

    private final BaseMapper<ThreadCloseStock, ThreadCloseStockDTO> mapper = new BaseMapper<>(ThreadCloseStock.class, ThreadCloseStockDTO.class);
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @Autowired
    private ThreadCloseStockRepo repository;
    public static final Logger logger = Logger.getLogger(ThreadCloseStockService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public ThreadCloseStockDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<ThreadCloseStockDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<ThreadCloseStockDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage create(ThreadCloseStockDTO dto) throws Exception {
        BaseMessage baseMessage = new BaseMessage(false);
        try {
            repository.save(mapper.toPersistenceBean(dto));
            //Luwu log vao bang action_log
            baseMessage.setSuccess(true);
            return baseMessage;
        } catch (Exception ex) {
            logger.error("Error: ", ex);
            throw ex;
        }
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(ThreadCloseStockDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @Override
    public boolean isExecute(ThreadCloseStockDTO dto) throws Exception {
        Date currentDate = getSysDate(em);
        int hour = Integer.valueOf(DateUtil.dateToStringWithPattern(currentDate, "HH"));
        int minute = Integer.valueOf(DateUtil.dateToStringWithPattern(currentDate, "mm"));
        int second = Integer.valueOf(DateUtil.dateToStringWithPattern(currentDate, "ss"));
        int hour_config = DataUtil.safeToInt(dto.getStartHour());
        int minute_config = DataUtil.safeToInt(dto.getStartMinute());
        int second_config = DataUtil.safeToInt(dto.getStartSecond());
        if (hour > hour_config) {
            return true;
        } else if (hour == hour_config) {
            if (minute > minute_config) {
                return true;
            } else if (minute == minute_config) {
                if (second >= second_config) {
                    return true;
                }
            }
        }
        return false;
    }
}
