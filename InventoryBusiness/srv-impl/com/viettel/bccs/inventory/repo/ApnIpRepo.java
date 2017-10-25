package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.model.ApnIp;
import com.viettel.fw.persistence.BaseRepository;

public interface ApnIpRepo extends BaseRepository<ApnIp, Long>, ApnIpRepoCustom {

}