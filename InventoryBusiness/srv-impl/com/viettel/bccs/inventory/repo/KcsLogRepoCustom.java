package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.dto.ProductInStockDTO;

import java.util.List;

/**
 * Created by Hellpoethero on 06/09/2016.
 */
public interface KcsLogRepoCustom {
    /*public Predicate toPredicate(List<FilterRequest> filters);*/

    List<ProductInStockDTO> findProductInStock(String code, String serial) throws Exception;

    List<ProductInStockDTO> findProductInStockIm1(String code, String serial) throws Exception;
}
