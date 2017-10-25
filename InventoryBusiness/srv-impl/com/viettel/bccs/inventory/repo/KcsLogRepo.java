package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.model.KcsLog;
import com.viettel.fw.persistence.BaseRepository;

/**
 * Created by Hellpoethero on 06/09/2016.
 */
public interface KcsLogRepo extends BaseRepository<KcsLog, Long>, KcsLogRepoCustom  {
}
