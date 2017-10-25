package com.viettel.bccs.inventory.service;
import com.viettel.bccs.inventory.dto.LockUserInfoMsgDTO;
import com.viettel.bccs.inventory.dto.LockUserTypeDTO;
import com.viettel.bccs.inventory.model.LockUserType;
import java.util.List;
import org.primefaces.model.LazyDataModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.viettel.fw.common.util.extjs.FilterRequest;
import javax.jws.WebMethod;
import javax.jws.WebService;
import org.springframework.transaction.annotation.Transactional;
import com.viettel.fw.dto.BaseMessage;
@WebService
public interface LockUserTypeService {

    @WebMethod
    public LockUserTypeDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<LockUserTypeDTO> findAll() throws Exception;

    @WebMethod
    public List<LockUserTypeDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public BaseMessage create(LockUserTypeDTO productSpecCharacterDTO) throws Exception;
    @WebMethod
    public BaseMessage update(LockUserTypeDTO productSpecCharacterDTO) throws Exception;

    public List<LockUserTypeDTO> getAllUserLockType() throws Exception;

    /**
     * ham tra ve danh sach thong tin man hinh lock_user
     * @author thanhnt77
     * @param staffId
     * @return
     * @throws Exception
     */
    public List<LockUserInfoMsgDTO> getLockUserInfo(Long staffId) throws Exception;
}