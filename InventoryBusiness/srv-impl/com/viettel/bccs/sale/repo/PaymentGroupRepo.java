package com.viettel.bccs.sale.repo;
import com.viettel.fw.persistence.BaseRepository;
import java.util.List;
import java.util.Date;
import java.lang.Integer;
import java.lang.Long;
import com.viettel.bccs.sale.model.PaymentGroup;

public interface PaymentGroupRepo extends BaseRepository<PaymentGroup, Long>, PaymentGroupRepoCustom {

}