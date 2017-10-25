package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.dto.ProductExchangeDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.persistence.BaseRepository;

import java.util.List;
import java.util.Date;
import java.lang.Long;

import com.viettel.bccs.inventory.model.ProductExchange;

public interface ProductExchangeRepo extends BaseRepository<ProductExchange, Long>, ProductExchangeRepoCustom {

    @Override
    public List<ProductExchangeDTO> searchProductExchange(ProductExchangeDTO productExchangeDTO) throws Exception, LogicException;

    List<ProductExchange> checkProductExchange(Long prodOfferId, Long prodOfferIdNew, Date fromDate, Date toDate) throws Exception, LogicException;
}