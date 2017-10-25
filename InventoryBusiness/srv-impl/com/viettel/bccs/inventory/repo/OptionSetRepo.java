package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.model.OptionSet;
import com.viettel.fw.persistence.BaseRepository;

public interface OptionSetRepo extends BaseRepository<OptionSet, Long>, OptionSetRepoCustom {

    /*x_1405_4_temp
    public List<OptionSet> findByCode(String code);

    public List<OptionSet> findByCreatedBy(String createdBy);

    public List<OptionSet> findByCreationDate(Date creationDate);

    public List<OptionSet> findByDescription(String description);

    public List<OptionSet> findById(Long id);

    public List<OptionSet> findByLastUpdateDate(Date lastUpdateDate);

    public List<OptionSet> findByLastUpdatedBy(String lastUpdatedBy);

    public List<OptionSet> findByName(String name);

    public List<OptionSet> findByStatus(String status);
    */
}