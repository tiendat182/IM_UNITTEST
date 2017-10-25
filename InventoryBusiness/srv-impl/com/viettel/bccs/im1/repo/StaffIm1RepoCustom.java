package com.viettel.bccs.im1.repo;

import com.mysema.query.types.Predicate;
import com.viettel.bccs.im1.model.StaffIm1;
import com.viettel.bccs.inventory.dto.StaffDTO;
import com.viettel.fw.common.util.extjs.FilterRequest;

import java.sql.Connection;
import java.util.List;


public interface StaffIm1RepoCustom {
    public Predicate toPredicate(List<FilterRequest> filters);

    public StaffIm1 findOneStaff(Long staffId);

    public String getTransCode(String prefixTransCode, String transType, StaffDTO staffDTO) throws Exception;

    public String getTransCodeDeposit(String prefixTransCode, StaffDTO staffDTO) throws Exception;

    public String getTransCodeByStaffId(Long staffId) throws Exception;

    public void increaseStockNum(Connection conn, Long staffId, String column) throws Exception;
}