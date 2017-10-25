package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.model.Reason;
import com.viettel.fw.persistence.BaseRepository;

public interface ReasonRepo extends BaseRepository<Reason, Long>, ReasonRepoCustom {

}