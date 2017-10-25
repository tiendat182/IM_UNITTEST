package com.viettel.bccs.sale.repo;

import com.mysema.query.types.Predicate;
import com.viettel.bccs.sale.model.SaleTransSerial;
import com.viettel.fw.common.util.extjs.FilterRequest;

import java.util.Date;
import java.util.List;

public interface SaleTransSerialRepoCustom {
    public Predicate toPredicate(List<FilterRequest> filters);

    public List<SaleTransSerial> findByDetailId(Long saleTransDetailId, Date saleTransDate);

    public List<SaleTransSerial> findBySaleTransDetailIdAndDate(Long saleTransDetailId, Date saleTransDate) throws Exception;
}