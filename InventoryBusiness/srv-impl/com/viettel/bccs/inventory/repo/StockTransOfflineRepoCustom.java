package com.viettel.bccs.inventory.repo;

import com.mysema.query.types.Predicate;
import com.viettel.bccs.inventory.dto.SendSmsDTO;
import com.viettel.bccs.inventory.common.BaseMsg;
import com.viettel.bccs.inventory.dto.PartnerContractDTO;
import com.viettel.bccs.inventory.model.PartnerContract;
import com.viettel.bccs.inventory.model.StockTrans;
import com.viettel.fw.common.util.extjs.FilterRequest;

import java.util.List;

public interface StockTransOfflineRepoCustom {
    public Predicate toPredicate(List<FilterRequest> filters);

    public List<StockTrans> getListStockTransOffline(int processTotal, int jobNumber) throws Exception;

    public PartnerContract getPartnerContractByStockTransId(Long stockTransId) throws Exception;

    public BaseMsg impStockToCompany(Long stockTransId, SendSmsDTO sendSmsDTO, PartnerContractDTO partnerContract) throws Exception;
}