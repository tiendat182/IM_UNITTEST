package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.GroupFilterRuleDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface GroupFilterRuleService {

    @WebMethod
    public GroupFilterRuleDTO findOne(Long id) throws LogicException,Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws LogicException,Exception;

    @WebMethod
    public List<GroupFilterRuleDTO> findAll() throws LogicException,Exception;

    @WebMethod
    public List<GroupFilterRuleDTO> findByFilter(List<FilterRequest> filters) throws LogicException,Exception;
	
	@WebMethod
    public BaseMessage create(GroupFilterRuleDTO productSpecCharacterDTO, String userName) throws LogicException,Exception;
    @WebMethod
    public BaseMessage update(GroupFilterRuleDTO productSpecCharacterDTO, String userName) throws LogicException,Exception;

    @WebMethod
    public GroupFilterRuleDTO save(GroupFilterRuleDTO productSpecCharacterDTO) throws LogicException,Exception;
    @WebMethod
    public BaseMessage delete(Long id) throws LogicException,Exception;

    @WebMethod
    public List<GroupFilterRuleDTO> searchByDto(GroupFilterRuleDTO dto) throws  LogicException,Exception;
    @WebMethod
    public BaseMessage deleteByDto(GroupFilterRuleDTO dto, String userName) throws  LogicException,Exception;
    @WebMethod
    public BaseMessage deleteByListDto(List<GroupFilterRuleDTO> listDto, String userName) throws  LogicException,Exception;


    /**
     * ham xu lay lay toan bo danh sach tap luat/nhomluat
     * @author ThanhNT
     * @param isParent
     * @return
     * @throws Exception
     */
    public List<GroupFilterRuleDTO> getListNumberFiler(Long groupFilterRuleId, boolean isParent) throws Exception;

    public List<GroupFilterRuleDTO> search(GroupFilterRuleDTO currentGroup,boolean isGroup) throws Exception;
}