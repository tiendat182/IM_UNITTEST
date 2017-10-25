package com.viettel.bccs.inventory.repo;
import com.viettel.fw.persistence.BaseRepository;
import java.util.List;
import java.lang.Character;
import java.lang.Long;
import com.viettel.bccs.inventory.model.LockUserType;

public interface LockUserTypeRepo extends BaseRepository<LockUserType, Long>, LockUserTypeRepoCustom {

}