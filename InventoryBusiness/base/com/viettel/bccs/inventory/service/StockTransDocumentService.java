package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.StockTransDocumentDTO;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;
@WebService
public interface StockTransDocumentService {

    @WebMethod
    public StockTransDocumentDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<StockTransDocumentDTO> findAll() throws Exception;

    @WebMethod
    public List<StockTransDocumentDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public StockTransDocumentDTO save(StockTransDocumentDTO stockTransDocumentDTO) throws Exception;

    @WebMethod
    public BaseMessage create(StockTransDocumentDTO productSpecCharacterDTO) throws Exception;
    @WebMethod
    public BaseMessage update(StockTransDocumentDTO productSpecCharacterDTO) throws Exception;


    /**
     * ham tra ve noi dung file upload
     * @author thanhnt77
     * @param stockTransId
     * @return
     * @throws Exception
     */
    public byte[] getAttachFileContent(Long stockTransId) throws Exception;

}