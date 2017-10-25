package com.viettel.bccs.inventory.repo;
import com.viettel.fw.persistence.BaseRepository;
import java.util.List;
import java.lang.Long;
import java.util.Date;
import com.viettel.bccs.inventory.model.ActiveKitVoffice;

public interface ActiveKitVofficeRepo extends BaseRepository<ActiveKitVoffice, Long>, ActiveKitVofficeRepoCustom {

}