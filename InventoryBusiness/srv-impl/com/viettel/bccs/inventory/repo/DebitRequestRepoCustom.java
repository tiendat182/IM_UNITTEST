package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.dto.DebitRequestDTO;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;

import java.util.List;

import com.viettel.bccs.inventory.model.DebitRequest;

public interface DebitRequestRepoCustom {
    public Predicate toPredicate(List<FilterRequest> filters);

    /**
     * @param debitRequestId debit request id
     * @return mảng byte chứa nội dung file
     * @throws Exception
     */
    public byte[] getAttachFileContent(Long debitRequestId) throws Exception;

    /**
     * @param debitRequestDTO
     * @return
     * @throws Exception
     */
    public int deleteDebitRequest(DebitRequestDTO debitRequestDTO) throws Exception;

    /**
     * @param debitRequestDTO
     * @return
     * @throws Exception
     */
    public List<DebitRequest> findDebitRequest(DebitRequestDTO debitRequestDTO) throws Exception;

    public Long getSequence() throws Exception;

    public Long getRevenueInMonth(Long ownerId, String ownerType) throws Exception;
}