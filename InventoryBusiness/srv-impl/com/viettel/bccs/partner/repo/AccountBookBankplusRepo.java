package com.viettel.bccs.partner.repo;
import com.viettel.fw.persistence.BaseRepository;

import java.lang.Long;

import com.viettel.bccs.partner.model.AccountBookBankplus;

public interface AccountBookBankplusRepo extends BaseRepository<AccountBookBankplus, Long>, AccountBookBankplusRepoCustom {

}