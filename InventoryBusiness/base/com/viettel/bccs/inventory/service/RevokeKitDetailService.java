package com.viettel.bccs.inventory.service;
import com.viettel.bccs.inventory.dto.RevokeKitDetailDTO;
import com.viettel.bccs.inventory.dto.RevokeKitResultDTO;
import com.viettel.bccs.inventory.model.RevokeKitDetail;

import java.util.Date;
import java.util.List;

import com.viettel.fw.Exception.LogicException;
import org.primefaces.model.LazyDataModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.viettel.fw.common.util.extjs.FilterRequest;
import javax.jws.WebMethod;
import javax.jws.WebService;
import org.springframework.transaction.annotation.Transactional;
import com.viettel.fw.dto.BaseMessage;
@WebService
public interface RevokeKitDetailService {

    @WebMethod
    public RevokeKitDetailDTO findOne(Long id) throws Exception;

    @WebMethod
    public RevokeKitDetailDTO save(RevokeKitDetailDTO productSpecCharacterDTO) throws LogicException, Exception;

    /**
     * ham tim kiem thu hoi kit
     * @author thanhnth77
     * @param shopId
     * @param fromDate
     * @param toDate
     * @return
     * @throws LogicException
     * @throws Exception
     */
    @WebMethod
    public List<RevokeKitDetailDTO> searchRevokeKitDetailByShopAndDate(Long shopId, Date fromDate, Date toDate) throws LogicException, Exception;

    /**
     * ham xu ly tra cuu thu hoi kit serial va isdn
     * @author thanhnt77
     * @param revokeKitResultDTO
     * @return
     * @throws LogicException
     * @throws Exception
     */
    @WebMethod
    public RevokeKitResultDTO searchRevokeKitBySerialAndIsdn(RevokeKitResultDTO revokeKitResultDTO) throws LogicException, Exception;

    /**
     * ham xu ly thu hoi kit
     * @author thanhnt77
     * @param revokeKitResultDTO
     * @return
     * @throws LogicException
     * @throws Exception
     */
    public RevokeKitResultDTO executeRevokeKit(RevokeKitResultDTO revokeKitResultDTO) throws LogicException, Exception;

}