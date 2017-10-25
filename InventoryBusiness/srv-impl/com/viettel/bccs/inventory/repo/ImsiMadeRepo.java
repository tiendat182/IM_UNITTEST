package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.model.ImsiMade;
import com.viettel.fw.persistence.BaseRepository;

public interface ImsiMadeRepo extends BaseRepository<ImsiMade, Long>, ImsiMadeRepoCustom {

}