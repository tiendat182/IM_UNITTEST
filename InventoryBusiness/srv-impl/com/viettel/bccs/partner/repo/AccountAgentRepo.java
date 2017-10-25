package com.viettel.bccs.partner.repo;
import com.viettel.fw.persistence.BaseRepository;

import java.lang.Long;

import com.viettel.bccs.im1.model.AccountAgent;

public interface AccountAgentRepo extends BaseRepository<AccountAgent, Long>, AccountAgentRepoCustom {

}