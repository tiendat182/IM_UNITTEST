package com.viettel.bccs.partner.repo;
import com.viettel.fw.persistence.BaseRepository;

import java.lang.Long;

import com.viettel.bccs.partner.model.AccountBalance;

public interface AccountBalanceRepo extends BaseRepository<AccountBalance, Long>, AccountBalanceRepoCustom {

}