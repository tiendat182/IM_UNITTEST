package com.viettel.bccs.inventory.service;
import com.viettel.bccs.inventory.dto.StockDocumentDTO;
import com.viettel.bccs.inventory.dto.VStockTransDTO;
import com.viettel.bccs.inventory.dto.ws.PayNoteAndReportDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface PayNoteAndReportService {

    @WebMethod
    public List<VStockTransDTO> getListStockTransForPay(PayNoteAndReportDTO payNoteAndReportDTO,
                                                  String exportOwnerId,  String importOwnerId,boolean flag);
    @WebMethod
    public BaseMessage createStockDocumentAndUpdateAction(StockDocumentDTO stockDocumentDTO,Long actionId ) throws Exception,LogicException;

    @WebMethod
    public BaseMessage cancelStockDocument(List<VStockTransDTO> selectForCancel,String strCancelPay, String user ) throws Exception,LogicException;

}