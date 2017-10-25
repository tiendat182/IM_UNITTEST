package com.viettel.bccs.sale.repo;

import com.mysema.query.types.Predicate;
import com.viettel.bccs.sale.dto.SaleTransBankplusDTO;
import com.viettel.bccs.sale.dto.SaleTransDTO;
import com.viettel.bccs.sale.model.SaleTrans;
import com.viettel.fw.common.util.extjs.FilterRequest;

import java.util.Date;
import java.util.List;

public interface SaleTransRepoCustom {
    Predicate toPredicate(List<FilterRequest> filters);

    public List<SaleTrans> findSaleTransByStockTransId(Long stockTransId, Date saleTransDate);

    public List<SaleTransDTO> findTransExpiredPay(Long staffId) throws Exception;

    List<SaleTransDTO> getListSaleTransByStaffId(Long staffId) throws Exception;

    List<SaleTransBankplusDTO> getListSaleTransBankplusByStaffId(Long staffId) throws Exception;
}