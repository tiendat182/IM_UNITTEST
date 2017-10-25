package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.model.SignFlow;
import com.viettel.fw.persistence.BaseRepository;

public interface SignFlowRepo extends BaseRepository<SignFlow, Long>, SignFlowRepoCustom {

}