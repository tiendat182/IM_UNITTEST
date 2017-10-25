package com.viettel.bccs.inventory.repo;

import com.mysema.query.types.Predicate;
import com.viettel.bccs.inventory.dto.OptionSetValueDTO;
import com.viettel.bccs.inventory.model.OptionSetValue;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;

import java.util.List;

public interface OptionSetValueRepoCustom {
    public Predicate toPredicate(List<FilterRequest> filters);

    public List<OptionSetValue> getLsOptionSetValueById(Long optionSetId);

    /**
     * get list OptionSetValue by value otionset value by value code
     *
     * @param strCode code
     * @return ls dto OptionSetValue
     * @author ThanhNT
     */
    public List<OptionSetValue> getLsOptionSetValueByCode(String strCode);

    public OptionSetValue getOptionSetValue(String optionSetType, String optionSetValueCode) throws Exception;

    public Long getValueByNameFromOptionSetValue(String name) throws Exception;

    /**
     * Get  OptionSetValue name theo OptionSetId va code
     *
     * @param optSetCode
     * @param code
     * @return
     * @throws Exception
     * @author KhuongDV
     */
    public String getValueByOptionSetIdAndCode(String optSetCode, String code) throws Exception;

    /**
     * @param code
     * @return
     * @throws Exception
     * @author KhuongDV Get List OptionSetValueDTO theo optionSetID
     */
    public List<OptionSetValue> getByOptionSetCode(String code) throws Exception;


    public List<OptionSetValue> getByOptionSetValueByNameCode(String code, String name) throws Exception;

    /**
     * Used to check value of combobox from service - validate in service.
     *
     * @param code  code of value need to check
     * @param value value need to check
     * @return
     * @author LuanNT23
     */
    public List<OptionSetValue> getByOptionsetCodeAndValue(String code, String value);

    /**
     * @param debitLevel
     * @return
     * @throws Exception
     * @author LuanNT23
     */
    public List<OptionSetValue> getDebitDayTypeByDebitLevel(String debitLevel) throws Exception;

    public List<OptionSetValue> getStatusOptionSetValueByStockState(String code, List<String> listValue) throws Exception;

    public List<OptionSetValueDTO> getDebitLevel(Long debitDayType) throws Exception;

    public List<OptionSetValueDTO> getFinanceType() throws Exception;

    /**
     * ham tra ve danh sach tinh co ton tai mat hang
     *
     * @param shopCode
     * @param prodOfferTypeId
     * @param prodOfferId
     * @param stateId
     * @return
     * @throws Exception
     * @author thanhnt77
     */
    List<OptionSetValueDTO> getLsProvince(String shopCodeUser, String shopCode,
                                          Long prodOfferTypeId, Long prodOfferId, Long stateId) throws Exception;

    public List<OptionSetValueDTO> getDefaultDebitLevel() throws LogicException, Exception;
}