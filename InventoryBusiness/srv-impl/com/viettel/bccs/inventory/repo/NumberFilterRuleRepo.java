package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.model.NumberFilterRule;
import com.viettel.fw.persistence.BaseRepository;

public interface NumberFilterRuleRepo extends BaseRepository<NumberFilterRule, Long>, NumberFilterRuleRepoCustom {

}