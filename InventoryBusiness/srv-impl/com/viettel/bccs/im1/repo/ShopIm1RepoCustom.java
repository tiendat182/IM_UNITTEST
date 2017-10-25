package com.viettel.bccs.im1.repo;

import com.mysema.query.types.Predicate;
import com.viettel.fw.common.util.extjs.FilterRequest;

import java.sql.Connection;
import java.util.List;

public interface ShopIm1RepoCustom {
    public Predicate toPredicate(List<FilterRequest> filters);

    public void increaseStockNum(Connection conn, Long shopId) throws Exception;

    public void deleteStock(List<Long> lstShopId) throws Exception;
}