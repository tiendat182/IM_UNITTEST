package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.model.LogUnlockUserInfo;
import com.viettel.fw.persistence.BaseRepository;

/**
 * Created by Hellpoethero on 29/08/2016.
 */
public interface LogUnlockUserInfoRepo extends BaseRepository<LogUnlockUserInfo, Long>, LogUnlockUserInfoRepoCustom {
}
