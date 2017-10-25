package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.model.ImsiInfo;
import com.viettel.fw.persistence.BaseRepository;

public interface ImsiInfoRepo extends BaseRepository<ImsiInfo, Long>, ImsiInfoRepoCustom {

}