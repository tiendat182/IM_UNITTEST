package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.message.AddGoodSaleMessage;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.Date;
import java.util.List;

@WebService
public interface SaleWs {
    @WebMethod
    public AddGoodSaleMessage addGoodAgent(SaleTransInfoMin transInfoMin, Long shopId, Long staffID, Long salesProgramId, Long payMethodId, Long reasonId) throws Exception, LogicException;

    @WebMethod
    public AddGoodSaleMessage saleToAgent(SaleTransInfoMin transInfoMin, Long shopId, Long staffID, Long salesProgramId, Long payMethodId, Long reasonId) throws Exception, LogicException;

    @WebMethod
    public List<ReasonSaleToAgentDTO> getReasonSaleToAgent() throws Exception, LogicException;

    @WebMethod
    public List<OptionSetValueDTO> getPayMethod() throws Exception, LogicException;

    @WebMethod
    public List<ProgramSaleDTO> getProgram() throws Exception, LogicException;

    @WebMethod
    public BaseMessage approveRequest(Long saleTransId, Date saleTransDate, Long approveStaffId) throws Exception, LogicException;

    @WebMethod
    public BaseMessage denyRequest(Long saleTransId, Date saleTransDate, Long denyStaffId, String denyStaffCode) throws Exception, LogicException;

    @WebMethod
    public List<StockApproveAgentDTO> getStockOrderAgent(Date fromDate, Date toDate, Long shopId, Long approveStatus, String receiverShopCode) throws Exception, LogicException;

    @WebMethod
    public List<StockApproveAgentDetailDTO> getStockOrderAgentDetail(Long saleTransId, Date fromDate, Date toDate) throws Exception, LogicException;

    @WebMethod
    public void findTransExpiredPayWS(Long staffId) throws LogicException, Exception;

    @WebMethod
    BaseMessage checkActiveSubscriber(String isdn) throws LogicException, Exception;

    @WebMethod
    BaseMessage checkActiveSerial(String fromSerial, String toSerial) throws LogicException, Exception;

    /**
     * ham xem chi tiet giao dich ben sale
     *
     * @param oldProdOfferId
     * @param oldSerial
     * @param dateInterval
     * @param staffId
     * @return
     * @throws Exception
     * @throws LogicException
     * @author thanhnt77
     */
    @WebMethod
    ChangeGoodMessage viewChangeGood(Long oldProdOfferId, String oldSerial, int dateInterval, Long staffId, Date fromDate, Date toDate) throws Exception, LogicException;

    /**
     * ham sinh giao dich doi thiet bi dau cuoi ben sale
     *
     * @param oldProdOfferId
     * @param oldSerial
     * @param newProdOfferId
     * @param newSerial
     * @param dateInterval
     * @param staffId
     * @return
     * @throws Exception
     * @throws LogicException
     * @author thanhnt77
     */
    @WebMethod
    ChangeGoodMessage changeGood(Long oldProdOfferId, String oldSerial, Long newProdOfferId, String newSerial, int dateInterval, Long staffId, Date fromDate, Date toDate) throws Exception, LogicException;

    /**
     *
     * @param fromDate
     * @param toDate
     * @param shopId
     * @param approveStatus
     * @param receiverShopCode
     * @return
     * @throws Exception
     * @throws LogicException
     */
    /**
     * ham tra danh sach
     *
     * @param shopCode
     * @param prodOfferId
     * @return
     * @throws Exception
     * @throws LogicException
     * @author thanhnt77
     */
    List<SubGoodsDTO> getLsSubGoodsByTeamCodeAndProdOfferId(String teamCode, Long prodOfferId) throws Exception, LogicException;
}