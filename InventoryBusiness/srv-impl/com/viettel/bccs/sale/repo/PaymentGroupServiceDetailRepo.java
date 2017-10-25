package com.viettel.bccs.sale.repo;
import com.viettel.fw.persistence.BaseRepository;
import java.util.List;
import java.lang.Long;
import com.viettel.bccs.sale.model.PaymentGroupServiceDetail;

public interface PaymentGroupServiceDetailRepo extends BaseRepository<PaymentGroupServiceDetail, Long>, PaymentGroupServiceDetailRepoCustom {

}