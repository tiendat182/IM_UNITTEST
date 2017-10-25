package com.viettel.bccs.inventory.repo;
import com.viettel.fw.persistence.BaseRepository;
import java.util.List;
import java.lang.Long;
import java.util.Date;
import com.viettel.bccs.inventory.model.ChangeModelTransDetail;

public interface ChangeModelTransDetailRepo extends BaseRepository<ChangeModelTransDetail, Long>, ChangeModelTransDetailRepoCustom {

}