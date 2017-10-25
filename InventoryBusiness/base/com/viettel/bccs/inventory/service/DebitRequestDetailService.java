package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.DebitRequestDetailDTO;
import com.viettel.bccs.inventory.dto.DebitRequestReportDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface DebitRequestDetailService {

    public DebitRequestDetailDTO findOne(Long id) throws Exception;

    public Long count(List<FilterRequest> filters) throws Exception;

    public List<DebitRequestDetailDTO> findAll() throws Exception;

    public List<DebitRequestDetailDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    public BaseMessage create(DebitRequestDetailDTO productSpecCharacterDTO) throws Exception;

    public BaseMessage update(DebitRequestDetailDTO productSpecCharacterDTO) throws Exception;

    public BaseMessage validateInternal(DebitRequestDetailDTO checked, List<DebitRequestDetailDTO> lstChecked, BaseMessage msg);


    /**
     * @param debitRequestDetailDTO
     * @return Ket qua validate
     * @throws Exception
     */
    @WebMethod
    public BaseMessage validate(DebitRequestDetailDTO debitRequestDetailDTO) throws LogicException, Exception;

    /**
     * @param debitRequestDetailDTO các tiêu chi tìm kiếm được bao trong đối tượng
     * @return danh sach debitRequestDetail theo cac tieu chi tim kiem da nhap vao
     * @throws Exception
     */
    @WebMethod
    public List<DebitRequestDetailDTO> findByDebitRequestDetail(DebitRequestDetailDTO debitRequestDetailDTO) throws Exception;

    public List<DebitRequestDetailDTO> getLstDetailByRequestId(Long requestId) throws Exception;

    public List<DebitRequestReportDTO> getLstDetailForReport(Long requestId) throws Exception;
}