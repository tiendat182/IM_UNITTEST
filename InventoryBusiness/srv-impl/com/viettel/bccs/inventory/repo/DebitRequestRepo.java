package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.model.DebitRequest;
import com.viettel.fw.persistence.BaseRepository;

public interface DebitRequestRepo extends BaseRepository<DebitRequest, Long>, DebitRequestRepoCustom {
}