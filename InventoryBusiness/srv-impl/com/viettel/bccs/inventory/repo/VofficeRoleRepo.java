package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.model.VofficeRole;
import com.viettel.fw.persistence.BaseRepository;

public interface VofficeRoleRepo extends BaseRepository<VofficeRole, Long>, VofficeRoleRepoCustom {

}