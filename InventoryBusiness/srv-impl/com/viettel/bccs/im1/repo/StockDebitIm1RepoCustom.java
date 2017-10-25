package com.viettel.bccs.im1.repo;

import com.mysema.query.types.Predicate;
import com.viettel.bccs.im1.model.StockDebitIm1;
import com.viettel.bccs.inventory.dto.StockDebitDTO;
import com.viettel.bccs.inventory.model.DebitRequestDetail;
import com.viettel.bccs.inventory.model.StockDebit;
import com.viettel.fw.common.util.extjs.FilterRequest;

import java.util.Date;
import java.util.List;

public interface StockDebitIm1RepoCustom {

    public StockDebitIm1 buildStockDebitFromDebitRequestDetail(DebitRequestDetail debitDetail, String staff) throws Exception;

}