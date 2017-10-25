package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.model.Deposit;
import com.viettel.fw.persistence.BaseRepository;

public interface DepositRepo extends BaseRepository<Deposit, Long>, DepositRepoCustom {

}