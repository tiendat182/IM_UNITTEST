package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.OptionSetValueDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;

import javax.jws.WebService;
import java.util.Date;
import java.util.List;

@WebService
public interface OptionSetValueService {


    List<OptionSetValueDTO> findByFilter(List<FilterRequest> filters);

    OptionSetValueDTO findOne(Long id);

    /**
     * get list OptionSetValueDTO by value otionset value'
     *
     * @param strCode code value of otionset
     * @return ls dto OptionSetValue
     * @author ThanhNT
     */
    List<OptionSetValueDTO> getLsOptionSetValueByCode(String strCode);

    Long getValueByNameFromOptionSetValue(String name) throws Exception;

    List<OptionSetValueDTO> getByOptionSetCode(String code) throws LogicException, Exception;


    /**
     * Get  value name theo  code  cua option_set va name cua option_set_value
     *
     * @param optSetCode code of otionset
     * @param name       name cua optionsetValue
     * @return
     * @throws com.viettel.fw.Exception.LogicException, Exception
     * @author thanhnt
     */
    String getValueByTwoCodeOption(String optSetCode, String name) throws LogicException, Exception;

    List<OptionSetValueDTO> getByOptionSetValueByNameCode(String code, String name) throws Exception;

    /**
     * @param trunc =true if want truncated date before return
     * @return date value of system
     * @throws Exception
     * @author LuanNT23
     */
    Date getSysdateFromDB(boolean trunc) throws Exception;

    /**
     * @param code
     * @param value
     * @return
     * @throws Exception
     */
    public List<OptionSetValueDTO> getByOptionsetCodeAndValue(String code, String value) throws Exception;


    /**
     * @param debitLevel
     * @return
     * @throws Exception
     * @author LuanNT23
     */
    public List<OptionSetValueDTO> getDebitDayTypeByDebitLevel(String debitLevel) throws Exception;

    /**
     * @param code
     * @param listValue
     * @return
     * @throws Exception
     */
    public List<OptionSetValueDTO> getStatusOptionSetValueByStockState(String code, List<String> listValue) throws Exception;

    /**
     * @return
     * @throws LogicException
     * @throws Exception
     * @author LuanNT23
     */
    public List<OptionSetValueDTO> getDebitLevel(Long debitDayType) throws LogicException, Exception;

    /**
     * @return
     * @throws LogicException
     * @throws Exception
     * @author LuanNT23
     */
    public List<OptionSetValueDTO> getFinanceType() throws LogicException, Exception;

    /**
     * ham tra ve danh sach tinh co ton tai mat hang
     *
     * @param shopCodeUser
     * @param shopCode
     * @param prodOfferTypeId
     * @param prodOfferId
     * @param stateId
     * @return
     * @throws Exception
     * @author thanhnt77
     */
    List<OptionSetValueDTO> getLsProvince(String shopCodeUser, String shopCode, Long prodOfferTypeId, Long prodOfferId, Long stateId) throws Exception;

    public List<OptionSetValueDTO> getDefaultDebitLevel() throws LogicException, Exception;

}