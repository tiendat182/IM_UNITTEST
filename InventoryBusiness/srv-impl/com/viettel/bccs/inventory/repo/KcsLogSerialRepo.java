package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.model.KcsLogSerial;
import com.viettel.fw.persistence.BaseRepository;

/**
 * Created by Hellpoethero on 06/09/2016.
 */
public interface KcsLogSerialRepo extends BaseRepository<KcsLogSerial, Long>, KcsLogSerialRepoCustom {
}
