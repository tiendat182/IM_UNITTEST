package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.model.Apn;
import com.viettel.fw.persistence.BaseRepository;

public interface ApnRepo extends BaseRepository<Apn, Long>, ApnRepoCustom {

}