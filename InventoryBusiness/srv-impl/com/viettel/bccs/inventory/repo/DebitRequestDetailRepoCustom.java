package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.dto.DebitRequestDTO;
import com.viettel.bccs.inventory.dto.DebitRequestDetailDTO;
import com.viettel.bccs.inventory.dto.DebitRequestReportDTO;
import com.viettel.bccs.inventory.model.DebitRequest;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;

import java.util.List;

public interface DebitRequestDetailRepoCustom {
    Predicate toPredicate(List<FilterRequest> filters);

    /**
     * @param debitRequest doi tuong debitRequest
     * @return Danh sách DebitRequestDetail tương ứng của DebitRequest truyền vào
     * @throws Exception
     */
    List<DebitRequestDetailDTO> findDebitRequestDetailByDebitRequset(DebitRequest debitRequest) throws Exception;

    /**
     * @param debitRequestDTO Đối tượng debit_request
     * @return
     * @throws Exception
     */
    int deleteDebitRequestDetailByDebitRequest(DebitRequestDTO debitRequestDTO) throws Exception;

    public List<DebitRequestReportDTO> getLstDetailForReport(Long requestId) throws Exception;

    public Long checkExsitRequestDetail(DebitRequestDetailDTO debitRequestDetailDTO) throws Exception;
}