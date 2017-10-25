package com.viettel.bccs.inventory.repo;
import com.viettel.bccs.inventory.model.DeviceConfig;
import com.viettel.fw.persistence.BaseRepository;
import java.util.List;
import java.util.Date;
import java.lang.Long;

public interface DeviceConfigRepo extends BaseRepository<DeviceConfig, Long>, DeviceConfigRepoCustom {

}