package com.viettel.bccs.inventory.repo;
import com.viettel.fw.persistence.BaseRepository;
import java.util.List;
import java.util.Date;
import java.lang.Long;
import com.viettel.bccs.inventory.model.ChangeModelTrans;

public interface ChangeModelTransRepo extends BaseRepository<ChangeModelTrans, Long>, ChangeModelTransRepoCustom {

}