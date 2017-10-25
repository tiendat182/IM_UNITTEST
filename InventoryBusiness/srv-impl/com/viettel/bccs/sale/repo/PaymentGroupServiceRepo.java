package com.viettel.bccs.sale.repo;
import com.viettel.fw.persistence.BaseRepository;
import java.util.List;
import java.util.Date;
import java.lang.Long;
import com.viettel.bccs.sale.model.PaymentGroupService;

public interface PaymentGroupServiceRepo extends BaseRepository<PaymentGroupService, Long>, PaymentGroupServiceRepoCustom {

}