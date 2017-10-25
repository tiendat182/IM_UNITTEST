package com.viettel.bccs.im1.repo;

import com.viettel.bccs.im1.model.PaymentDebitIm1;
import com.viettel.bccs.sale.model.PaymentDebit;
import com.viettel.bccs.sale.repo.PaymentDebitRepoCustom;
import com.viettel.fw.persistence.BaseRepository;

public interface PaymentDebitIm1Repo extends BaseRepository<PaymentDebitIm1, Long>, PaymentDebitIm1RepoCustom {

}