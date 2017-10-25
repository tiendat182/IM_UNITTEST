package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.FinanceTypeDTO;
import com.viettel.bccs.inventory.dto.StaffDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface FinanceTypeService {

    @WebMethod
    public FinanceTypeDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<FinanceTypeDTO> findAll() throws Exception;

    @WebMethod
    public List<FinanceTypeDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public BaseMessage create(FinanceTypeDTO productSpecCharacterDTO, StaffDTO staffDTO) throws Exception;
    @WebMethod
    public BaseMessage update(FinanceTypeDTO productSpecCharacterDTO, StaffDTO staffDTO) throws Exception;

    @WebMethod
    public BaseMessage deleteListFinance(List<FinanceTypeDTO> lsFinanceTypeDTOs, StaffDTO staffDTO) throws Exception;

    /**
     * Ham xu ly tim kiem cau hinh han muc nop tien
     * @author ThanhNT
     * @param searchFinanceTypeDTO
     * @return
     * @throws LogicException
     * @throws Exception
     */
    @WebMethod
    public List<FinanceTypeDTO> searchLsFinanceTypeDto(FinanceTypeDTO searchFinanceTypeDTO) throws LogicException, Exception;

}