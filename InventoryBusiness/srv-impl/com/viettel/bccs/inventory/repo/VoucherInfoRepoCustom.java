package com.viettel.bccs.inventory.repo;

import com.mysema.query.types.Predicate;
import com.viettel.bccs.inventory.model.VoucherInfo;
import com.viettel.fw.common.util.extjs.FilterRequest;

import java.util.List;

/**
 * @author HoangAnh
 */
public interface VoucherInfoRepoCustom {
    /*public Predicate toPredicate(List<FilterRequest> filters);*/

    public VoucherInfo findBySerial(String serial);
}