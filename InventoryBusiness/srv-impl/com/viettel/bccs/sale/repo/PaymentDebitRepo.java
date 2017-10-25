package com.viettel.bccs.sale.repo;
import com.viettel.fw.persistence.BaseRepository;
import java.util.List;
import java.util.Date;
import java.lang.Long;
import com.viettel.bccs.sale.model.PaymentDebit;

public interface PaymentDebitRepo extends BaseRepository<PaymentDebit, Long>, PaymentDebitRepoCustom {

}