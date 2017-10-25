package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.model.KcsLogDetail;
import com.viettel.fw.persistence.BaseRepository;

/**
 * Created by Hellpoethero on 06/09/2016.
 */
public interface KcsLogDetailRepo extends BaseRepository<KcsLogDetail, Long>, KcsLogDetailRepoCustom {
}
