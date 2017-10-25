package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.model.ReasonGroup;
import com.viettel.fw.persistence.BaseRepository;

public interface ReasonGroupRepo extends BaseRepository<ReasonGroup, Long>, ReasonGroupRepoCustom {

}