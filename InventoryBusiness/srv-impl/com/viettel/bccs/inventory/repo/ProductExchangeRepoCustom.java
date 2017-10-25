package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.dto.ProductExchangeDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;

import java.util.Date;
import java.util.List;

import com.viettel.bccs.inventory.model.ProductExchange;

public interface ProductExchangeRepoCustom {
    public Predicate toPredicate(List<FilterRequest> filters);

    List<ProductExchangeDTO> searchProductExchange(ProductExchangeDTO productExchangeDTO) throws Exception, LogicException;

    List<ProductExchange> checkProductExchange(Long prodOfferId, Long prodOfferIdNew, Date fromDate, Date toDate) throws Exception, LogicException;

}