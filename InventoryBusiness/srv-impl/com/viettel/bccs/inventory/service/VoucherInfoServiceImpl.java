package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.VoucherInfoDTO;
import com.viettel.bccs.inventory.model.VoucherInfo;
import com.viettel.bccs.inventory.repo.VoucherInfoRepo;
import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.fw.service.BaseServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.jws.WebMethod;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by HoangAnh on 9/1/2016.
 */
@Service
public class VoucherInfoServiceImpl extends BaseServiceImpl implements VoucherInfoService {

    private final BaseMapper<VoucherInfo, VoucherInfoDTO> mapper = new BaseMapper(VoucherInfo.class, VoucherInfoDTO.class);

    @Autowired
    private VoucherInfoRepo voucherInfoRepo;

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    public final static Logger logger = Logger.getLogger(VoucherInfoServiceImpl.class);

    @WebMethod
    public List<VoucherInfoDTO> findAll() {
        return mapper.toDtoBean(voucherInfoRepo.findAll());
    }

    @WebMethod
    public BaseMessage insertListVoucherInfo(List<VoucherInfoDTO> voucherInfoDTOs) {
        BaseMessage baseMessage = new BaseMessage();
        List<VoucherInfo> voucherInfoList = mapper.toPersistenceBean(voucherInfoDTOs);
        try {
            saveImportVoucher(voucherInfoList);
            baseMessage.setSuccess(true);
        } catch(Exception e) {
            baseMessage.setSuccess(false);
            baseMessage.setKeyMsg(e.getMessage());
            logger.debug(e.getMessage(), e);
        }
        return baseMessage;
    }

    @WebMethod
    public VoucherInfoDTO findBySerial(String serial) {
        VoucherInfo voucherInfo = voucherInfoRepo.findBySerial(serial);
        if (voucherInfo == null) return null;
        return mapper.toDtoBean(voucherInfo);
    }

    @Override
    public BaseMessage deleteVoucherInfo(Long id) {
        voucherInfoRepo.delete(id);
        return new BaseMessage(true);
    }

    @Transactional(rollbackOn = Exception.class)
    public void saveImportVoucher(List<VoucherInfo> lst) throws Exception {
        for (VoucherInfo voucherInfo : lst) {
            voucherInfo.setCreateDate(DbUtil.getSysDate(em));
            voucherInfoRepo.save(voucherInfo);
        }
    }
}
