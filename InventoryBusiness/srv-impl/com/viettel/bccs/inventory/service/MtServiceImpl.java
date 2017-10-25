package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.MtDTO;
import com.viettel.bccs.inventory.model.Mt;
import com.viettel.bccs.inventory.repo.MtRepo;
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
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

@Service
public class MtServiceImpl extends BaseServiceImpl implements MtService {

    private final BaseMapper<Mt, MtDTO> mapper = new BaseMapper<>(Mt.class, MtDTO.class);
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;
    @Autowired
    private MtRepo repository;
    public static final Logger logger = Logger.getLogger(MtService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public MtDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<MtDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<MtDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage create(MtDTO dto) throws Exception, LogicException {
        BaseMessage result = new BaseMessage(true);
        try {
            Mt mt = mapper.toPersistenceBean(dto);
            repository.save(mt);
            result.setSuccess(true);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            result.setSuccess(false);
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "payNoteAndReport.create.mt.error");
        }
        return result;
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(MtDTO dto) throws Exception {
        repository.save(mapper.toPersistenceBean(dto));
        return new BaseMessage();
    }

    @Override
    public BaseMessage saveSms(String isdn, String content) throws Exception {
        BaseMessage result = new BaseMessage(true);
        try {
            Date currentDate = getSysDate(em);
            MtDTO mtDTO = new MtDTO();
            mtDTO.setMsisdn(isdn);
            mtDTO.setMessage(content);
            mtDTO.setMoHisId(Const.AppProperties4Sms.moHisID.getLongValue());
            mtDTO.setRetryNum(Const.AppProperties4Sms.retryNum.getLongValue());
            mtDTO.setAppId(Const.AppProperties4Sms.appID.getValue().toString());
            mtDTO.setReceiveTime(currentDate);
            mtDTO.setChannel(Const.AppProperties4Sms.channel.getValue().toString());
            mtDTO.setAppName(Const.AppProperties4Sms.appName.getValue().toString());
            Mt mt = mapper.toPersistenceBean(mtDTO);
            repository.save(mt);
            result.setSuccess(true);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            result.setSuccess(false);
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "payNoteAndReport.create.mt.error");
        }
        return result;
    }

    @Override
    @Transactional
    public void delete(Long mtID) throws LogicException, Exception {
        repository.deleteMessage(mtID);
    }
}
