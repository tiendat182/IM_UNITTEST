package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.model.ActionLogDetail;
import com.viettel.fw.persistence.BaseRepository;

public interface ActionLogDetailRepo extends BaseRepository<ActionLogDetail, Long>, ActionLogDetailRepoCustom {

}