package com.viettel.bccs.inventory.repo;
import com.viettel.fw.persistence.BaseRepository;
import java.util.List;
import java.lang.Long;
import java.util.Date;
import java.lang.Integer;
import com.viettel.bccs.inventory.model.RevokeKitDetail;

public interface RevokeKitDetailRepo extends BaseRepository<RevokeKitDetail, Long>, RevokeKitDetailRepoCustom {

}