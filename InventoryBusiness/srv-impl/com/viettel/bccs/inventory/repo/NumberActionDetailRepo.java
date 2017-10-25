package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.model.NumberActionDetail;
import com.viettel.fw.persistence.BaseRepository;

public interface NumberActionDetailRepo extends BaseRepository<NumberActionDetail, Long>, NumberActionDetailRepoCustom {

}