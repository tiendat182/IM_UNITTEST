package com.viettel.bccs.inventory.repo;
import com.viettel.bccs.inventory.model.Staff;
import com.viettel.fw.persistence.BaseRepository;


public interface StaffRepo extends BaseRepository<Staff, Long>, StaffRepoCustom {

}