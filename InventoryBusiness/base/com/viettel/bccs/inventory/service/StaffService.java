package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.StaffDTO;
import com.viettel.bccs.inventory.dto.StaffTagConfigDTO;
import com.viettel.bccs.inventory.dto.VShopStaffDTO;
import com.viettel.bccs.inventory.model.Staff;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface StaffService {


    @WebMethod
    public StaffDTO findOne(Long id) throws Exception, LogicException;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception, LogicException;

    @WebMethod
    public List<StaffDTO> findByFilter(List<FilterRequest> filters) throws Exception, LogicException;

    @WebMethod
    public StaffDTO getStaffByStaffCode(String staffCode) throws Exception;

    @WebMethod
    public StaffDTO save(StaffDTO staffDTO) throws Exception;

    /**
     * @param staffOwnerId
     * @return
     * @throws Exception,LogicException
     * @author ThanhNT
     */
    @WebMethod
    public List<Staff> getStaffList(Long staffOwnerId) throws Exception, LogicException;

    /**
     * @param txtSearch
     * @param shopId
     * @return
     * @throws LogicException
     * @throws Exception
     * @author ThanhNT
     */
    @WebMethod
    public List<VShopStaffDTO> getStaffByShopId(String txtSearch, String shopId) throws LogicException, Exception;

    @WebMethod
    public List<VShopStaffDTO> getStaffIsdnMngt(String txtSearch, String shopId) throws LogicException, Exception;

    @WebMethod
    public List<VShopStaffDTO> getStaffIsdnMngtUsrCrt(String txtSearch, String shopId) throws LogicException, Exception;

    @WebMethod
    public List<VShopStaffDTO> getStaffDistribute(String txtSearch, String shopId) throws LogicException, Exception;

    @WebMethod
    public List<VShopStaffDTO> getStaffByShopIdAndChanelType(StaffTagConfigDTO staffTagConfigDTO) throws LogicException, Exception;

    @WebMethod
    public List<StaffDTO> searchStaffByShopId(Long shopId) throws LogicException, Exception;


    /**

     *
     * @param prefixTransCode
     * @param transType
     * @return
     * @throws Exception
     * @author ThanhNT
     */
    @WebMethod
    public String getTransCode(String prefixTransCode, String transType, StaffDTO staffDTO) throws Exception;

    /**
     * ham xu ly validate trans code
     *
     * @param noteCode
     * @param staffId
     * @return
     * @throws Exception
     * @author ThanhNT77
     */
    public Boolean validateTransCode(String noteCode, Long staffId, String transType) throws Exception;

    public VShopStaffDTO findStaffById(String staffId) throws Exception;

    public String getTransCodeDeposit(String prefixTransCode, StaffDTO staffDTO) throws Exception;

    public String getTransCodeByStaffId(Long staffId) throws LogicException, Exception;

    @WebMethod
    public StaffDTO getStaffByOwnerId(Long shopId, String staffCode) throws LogicException, Exception;

}