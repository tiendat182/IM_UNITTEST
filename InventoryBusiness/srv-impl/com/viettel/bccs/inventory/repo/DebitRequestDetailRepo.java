package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.model.DebitRequestDetail;
import com.viettel.fw.persistence.BaseRepository;

public interface DebitRequestDetailRepo extends BaseRepository<DebitRequestDetail, Long>, DebitRequestDetailRepoCustom {

}