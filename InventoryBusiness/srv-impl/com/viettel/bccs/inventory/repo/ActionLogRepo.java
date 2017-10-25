package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.model.ActionLog;
import com.viettel.fw.persistence.BaseRepository;

public interface ActionLogRepo extends BaseRepository<ActionLog, Long>, ActionLogRepoCustom {

}