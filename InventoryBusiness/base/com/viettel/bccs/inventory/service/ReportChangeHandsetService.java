package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.ReportChangeHandsetDTO;
import com.viettel.bccs.inventory.dto.StockTransActionDTO;
import com.viettel.bccs.inventory.dto.StockTransDTO;
import com.viettel.bccs.inventory.dto.StockTransDetailDTO;
import com.viettel.bccs.inventory.model.ReportChangeHandset;

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
public interface ReportChangeHandsetService {

    @WebMethod
    public ReportChangeHandsetDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<ReportChangeHandsetDTO> findAll() throws Exception;

    @WebMethod
    public List<ReportChangeHandsetDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public ReportChangeHandsetDTO create(ReportChangeHandsetDTO reportChangeHandsetDTO) throws Exception;

    @WebMethod
    public BaseMessage update(ReportChangeHandsetDTO productSpecCharacterDTO) throws Exception;

    /**
     * ham tra ve danh sach doi hang dat coc
     * @author thanhnt77
     * @param prodOfferId
     * @param serial
     * @param shopPath
     * @return
     * @throws Exception
     */
    @WebMethod
    public List<ReportChangeHandsetDTO> getLsReportChangeHandsetTerminal(Long prodOfferId, String serial, String shopPath) throws Exception;

    /**
     * ham tra ve danh sach bao cao ham doi thiet bi
     * @author thanhnt77
     * @param stockTransDTO
     * @param stockTransActionDTO
     * @param lstStockTransDetail
     * @return
     * @throws LogicException
     * @throws Exception
     */
    public List<ReportChangeHandsetDTO> getLsChangeHandsetTerminalDevide(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO,
                                                                         List<StockTransDetailDTO> lstStockTransDetail) throws LogicException, Exception;
}