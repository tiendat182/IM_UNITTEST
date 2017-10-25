package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.model.ReceiptExpense;
import com.viettel.fw.persistence.BaseRepository;

public interface ReceiptExpenseRepo extends BaseRepository<ReceiptExpense, Long>, ReceiptExpenseRepoCustom {

}