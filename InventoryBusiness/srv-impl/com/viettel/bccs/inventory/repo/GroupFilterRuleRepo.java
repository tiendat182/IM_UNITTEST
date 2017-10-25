package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.model.GroupFilterRule;
import com.viettel.fw.persistence.BaseRepository;

public interface GroupFilterRuleRepo extends BaseRepository<GroupFilterRule, Long>, GroupFilterRuleRepoCustom {

}