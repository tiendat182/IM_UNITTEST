package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.model.RevokeTrans;
import com.viettel.fw.persistence.BaseRepository;

public interface RevokeTransRepo extends BaseRepository<RevokeTrans, Long>, RevokeTransRepoCustom {

}