package com.viettel.bccs.inventory.wsesb.service;

import com.viettel.bccs.inventory.dto.StaffDTO;
import com.viettel.bccs.inventory.dto.StaffTagConfigDTO;
import com.viettel.bccs.inventory.dto.VShopStaffDTO;
import com.viettel.bccs.inventory.model.Staff;
import com.viettel.bccs.inventory.service.StaffService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.jws.WebMethod;
import java.util.List;

@Service("WsStaffServiceImpl")
public class WsStaffServiceImpl implements StaffService {

    public static final Logger logger = Logger.getLogger(WsStaffServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private StaffService client;

    @PostConstruct
    public void init() {
        try {
            client = wsClientFactory.createWsClient(StaffService.class);
        } catch (Exception ex) {
            logger.error(ex);
        }
    }

    @Override
    @WebMethod
    public StaffDTO findOne(Long id) throws Exception, LogicException {
        return client.findOne(id);
    }

    @Override
    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception, LogicException {
        return client.count(filters);
    }

    @Override
    @WebMethod
    public List<StaffDTO> findByFilter(List<FilterRequest> filters) throws Exception, LogicException {
        return client.findByFilter(filters);
    }

    @Override
    @WebMethod
    public StaffDTO getStaffByStaffCode(String staffCode) throws Exception {
        return client.getStaffByStaffCode(staffCode);
    }

    @Override
    @WebMethod
    public StaffDTO save(StaffDTO staffDTO) throws Exception {
        return client.save(staffDTO);
    }

    @Override
    @WebMethod
    public List<Staff> getStaffList(Long staffOwnerId) throws Exception, LogicException {
        return client.getStaffList(staffOwnerId);
    }

    @Override
    @WebMethod
    public List<VShopStaffDTO> getStaffByShopId(String txtSearch, String shopId) throws LogicException, Exception {
        return client.getStaffByShopId(txtSearch, shopId);
    }

    @WebMethod
    public List<VShopStaffDTO> getStaffIsdnMngt(String txtSearch, String shopId) throws LogicException, Exception {
        return client.getStaffIsdnMngt(txtSearch, shopId);
    }

    @WebMethod
    public List<VShopStaffDTO> getStaffIsdnMngtUsrCrt(String txtSearch, String shopId) throws LogicException, Exception {
        return client.getStaffIsdnMngtUsrCrt(txtSearch, shopId);
    }

    @Override
    public List<VShopStaffDTO> getStaffDistribute(String txtSearch, String shopId) throws LogicException, Exception {
        return client.getStaffDistribute(txtSearch, shopId);
    }

    @Override
    @WebMethod
    public List<StaffDTO> searchStaffByShopId(Long shopId) throws LogicException, Exception {
        return client.searchStaffByShopId(shopId);
    }

    @Override
    @WebMethod
    public String getTransCode(String prefixTransCode, String transType, StaffDTO staffDTO) throws Exception {
        return client.getTransCode(prefixTransCode, transType, staffDTO);
    }

    @Override
    public Boolean validateTransCode(String noteCode, Long staffId, String transType) throws Exception {
        return client.validateTransCode(noteCode, staffId, transType);
    }

    @Override
    public VShopStaffDTO findStaffById(String staffId) throws Exception {
        return client.findStaffById(staffId);
    }

    @Override
    @WebMethod
    public List<VShopStaffDTO> getStaffByShopIdAndChanelType(StaffTagConfigDTO staffTagConfigDTO) throws LogicException, Exception {
        return client.getStaffByShopIdAndChanelType(staffTagConfigDTO);
    }

    @Override
    public String getTransCodeDeposit(String prefixTransCode, StaffDTO staffDTO) throws Exception {
        return client.getTransCodeDeposit(prefixTransCode, staffDTO);
    }

    @Override
    @WebMethod
    public String getTransCodeByStaffId(Long staffId) throws LogicException, Exception {
        return client.getTransCodeByStaffId(staffId);
    }

    @Override
    @WebMethod
    public StaffDTO getStaffByOwnerId(Long shopId, String staffCode) throws LogicException, Exception {
        return client.getStaffByOwnerId(shopId, staffCode);
    }
}