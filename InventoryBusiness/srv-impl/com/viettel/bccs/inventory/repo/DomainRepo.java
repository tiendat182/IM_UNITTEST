package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.model.Domain;
import com.viettel.fw.persistence.BaseRepository;

public interface DomainRepo extends BaseRepository<Domain, Long>, DomainRepoCustom {

}