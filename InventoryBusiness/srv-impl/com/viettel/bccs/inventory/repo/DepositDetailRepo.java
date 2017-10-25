package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.model.DepositDetail;
import com.viettel.fw.persistence.BaseRepository;

public interface DepositDetailRepo extends BaseRepository<DepositDetail, Long>, DepositDetailRepoCustom {

}