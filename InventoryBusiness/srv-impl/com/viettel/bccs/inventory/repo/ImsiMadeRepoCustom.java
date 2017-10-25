package com.viettel.bccs.inventory.repo;

import com.mysema.query.types.Predicate;
import com.viettel.bccs.inventory.dto.ImsiMadeDTO;
import com.viettel.bccs.inventory.dto.OutputImsiProduceDTO;
import com.viettel.bccs.inventory.dto.PartnerDTO;
import com.viettel.bccs.inventory.model.Partner;
import com.viettel.fw.common.util.extjs.FilterRequest;

import java.util.Date;
import java.util.List;

public interface ImsiMadeRepoCustom {
    public Predicate toPredicate(List<FilterRequest> filters);
    public List<OutputImsiProduceDTO> getListImsiRange(String startImsi, String endImsi, String docCode, Date fromDate, Date toDate, Long status) throws Exception;
    public Long checkImsiRange(String fromImsi, String toImsi) throws Exception;
    public List<Partner> getListPartnerA4keyNotNull() throws Exception;
    public Long getQuantityByImsi(String fromImsi, String toImsi) throws Exception;
    public List<ImsiMadeDTO> getImsiRangeByDate(Date fromDate, Date toDate) throws Exception;
    public boolean validateImsiRange(String fromImsi, String toImsi) throws Exception;
    //public void updateImsiInfoTo2Status(String fromImsi, String toImsi) throws Exception;
}