package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.model.ChannelType;
import com.viettel.fw.persistence.BaseRepository;

public interface ChannelTypeRepo extends BaseRepository<ChannelType, Long>, ChannelTypeRepoCustom {

}