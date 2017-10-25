package com.viettel.bccs.sale.repo;

import com.mysema.query.types.Predicate;
import com.viettel.bccs.sale.model.SaleTransDetail;
import com.viettel.fw.common.util.extjs.FilterRequest;

import java.util.Date;
import java.util.List;

public interface SaleTransDetailRepoCustom {
    public Predicate toPredicate(List<FilterRequest> filters);

    public List<SaleTransDetail> findBySaleTransId(Long saleTransId);

    public List<SaleTransDetail> findSaleTransDetails(Long saleTransId, Long prodOfferId, Date saleTransDate);
}