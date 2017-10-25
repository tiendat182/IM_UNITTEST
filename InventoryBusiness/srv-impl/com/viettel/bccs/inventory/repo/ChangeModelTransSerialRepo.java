package com.viettel.bccs.inventory.repo;
import com.viettel.fw.persistence.BaseRepository;
import java.util.List;
import java.lang.Long;
import java.util.Date;
import com.viettel.bccs.inventory.model.ChangeModelTransSerial;

public interface ChangeModelTransSerialRepo extends BaseRepository<ChangeModelTransSerial, Long>, ChangeModelTransSerialRepoCustom {

}