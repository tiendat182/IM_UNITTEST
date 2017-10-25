package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.dto.ReceiptExpenseDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;

import java.util.List;

import com.viettel.bccs.inventory.model.ReceiptExpense;

public interface ReceiptExpenseRepoCustom {
    public Predicate toPredicate(List<FilterRequest> filters);

    public ReceiptExpenseDTO findByStockTransIdAndType(Long stockTransId, String type) throws Exception, LogicException;

    public List<ReceiptExpense> getReceiptExpenseFromReceiptNo(String receiptNo) throws Exception;
}