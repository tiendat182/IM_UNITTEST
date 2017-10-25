package com.viettel.bccs.inventory.wsesb.service;

import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.message.AddGoodSaleMessage;
import com.viettel.bccs.inventory.service.ProductOfferPriceService;
import com.viettel.bccs.inventory.service.SaleWs;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.jws.WebMethod;
import java.util.Date;
import java.util.List;

@Service("WsSaleWsImpl")
public class WsSaleWsImpl implements SaleWs {

    public static final Logger logger = Logger.getLogger(WsSaleWsImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private SaleWs client;

    @PostConstruct
    public void init() throws Exception {
        try {
            client = wsClientFactory.createWsClient(SaleWs.class);
        } catch (Exception ex) {
            logger.error("init", ex);
        }
    }

    @Override
    @WebMethod
    public AddGoodSaleMessage addGoodAgent(SaleTransInfoMin transInfoMin, Long shopId, Long staffID, Long salesProgramId, Long payMethodId, Long reasonId) throws Exception, LogicException {
        return client.addGoodAgent(transInfoMin, shopId, staffID, salesProgramId, payMethodId, reasonId);
    }

    @Override
    @WebMethod
    public AddGoodSaleMessage saleToAgent(SaleTransInfoMin transInfoMin, Long shopId, Long staffID, Long salesProgramId, Long payMethodId, Long reasonId) throws Exception, LogicException {
        return client.saleToAgent(transInfoMin, shopId, staffID, salesProgramId, payMethodId, reasonId);
    }

    @Override
    @WebMethod
    public List<ReasonSaleToAgentDTO> getReasonSaleToAgent() throws Exception, LogicException {
        return client.getReasonSaleToAgent();
    }

    @Override
    @WebMethod
    public List<OptionSetValueDTO> getPayMethod() throws Exception, LogicException {
        return client.getPayMethod();
    }

    @Override
    @WebMethod
    public List<ProgramSaleDTO> getProgram() throws Exception, LogicException {
        return client.getProgram();
    }

    @Override
    @WebMethod
    public List<StockApproveAgentDTO> getStockOrderAgent(Date fromDate, Date toDate, Long shopId, Long approveStatus, String receiverShopCode) throws Exception, LogicException {
        return client.getStockOrderAgent(fromDate, toDate, shopId, approveStatus, receiverShopCode);
    }

    @Override
    @WebMethod
    public List<StockApproveAgentDetailDTO> getStockOrderAgentDetail(Long stockTransId, Date fromDate, Date toDate) throws Exception, LogicException {
        return client.getStockOrderAgentDetail(stockTransId, fromDate, toDate);
    }

    @Override
    @WebMethod
    public BaseMessage approveRequest(Long saleTransId, Date saleTransDate, Long approveStaffId) throws Exception, LogicException {
        return client.approveRequest(saleTransId, saleTransDate, approveStaffId);
    }

    @Override
    @WebMethod
    public BaseMessage denyRequest(Long saleTransId, Date saleTransDate, Long denyStaffId, String denyStaffCode) throws Exception, LogicException {
        return client.denyRequest(saleTransId, saleTransDate, denyStaffId, denyStaffCode);
    }

    @Override
    @WebMethod
    public void findTransExpiredPayWS(Long staffId) throws LogicException, Exception {
        client.findTransExpiredPayWS(staffId);
    }

    @Override
    public BaseMessage checkActiveSubscriber(String isdn) throws LogicException, Exception {
        return client.checkActiveSubscriber(isdn);
    }

    @Override
    public BaseMessage checkActiveSerial(String fromSerial, String toSerial) throws LogicException, Exception {
        return client.checkActiveSerial(fromSerial, toSerial);
    }

    @Override
    @WebMethod
    public ChangeGoodMessage viewChangeGood(Long oldProdOfferId, String oldSerial, int dateInterval, Long staffId, Date fromDate, Date toDate) throws Exception, LogicException {
        return client.viewChangeGood(oldProdOfferId, oldSerial, dateInterval, staffId, fromDate, toDate);
    }

    @Override
    @WebMethod
    public ChangeGoodMessage changeGood(Long oldProdOfferId, String oldSerial, Long newProdOfferId, String newSerial, int dateInterval, Long staffId, Date fromDate, Date toDate) throws Exception, LogicException {
        return client.changeGood(oldProdOfferId, oldSerial, newProdOfferId, newSerial, dateInterval, staffId, fromDate, toDate);
    }

    @Override
    public List<SubGoodsDTO> getLsSubGoodsByTeamCodeAndProdOfferId(String teamCode, Long prodOfferId) throws Exception, LogicException {
        return client.getLsSubGoodsByTeamCodeAndProdOfferId(teamCode, prodOfferId);
    }


}