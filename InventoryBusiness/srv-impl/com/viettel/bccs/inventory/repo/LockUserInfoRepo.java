package com.viettel.bccs.inventory.repo;
import com.viettel.fw.persistence.BaseRepository;
import java.util.List;
import java.lang.Short;
import java.util.Date;
import java.lang.Long;
import java.math.BigInteger;
import com.viettel.bccs.inventory.model.LockUserInfo;

public interface LockUserInfoRepo extends BaseRepository<LockUserInfo, Long>, LockUserInfoRepoCustom {

}