package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.cc.dto.CardInfoExtraDTO;
import com.viettel.bccs.cc.dto.HistoryInforCardNumberDTO;
import com.viettel.bccs.cc.dto.RefillInfoDTO;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.message.AddGoodSaleMessage;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.fw.service.BaseServiceImpl;
import com.viettel.ws.common.utils.*;
import com.viettel.ws.provider.WsCallerFactory;
import com.viettel.ws.provider.WsDtoContainer;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.jws.WebMethod;
import java.util.*;

@Service
public class CustomerCareWsImpl extends BaseServiceImpl implements CustomerCareWs {
    public static final Logger logger = Logger.getLogger(CustomerCareWs.class);
    @Autowired
    private WsCallerFactory wsCallerFactory;
    @Value("${customerCare.ws.username}")
    private String username;
    @Value("${customerCare.ws.password}")
    private String password;

    @Override
    public CardNumberLockDTO getHistoryCardNumber(String serial) throws Exception, LogicException {
        try {
            WsRequestCreator wsConfig = wsCallerFactory.createWsCaller(Const.CC_WS.ENDPOINTKEY,
                    Const.CC_WS.SERVICE_HISTORY_SEARCH, Const.CC_WS.FUNCTION_HISTORY_CARD_NUMBER_LOCK);
            Map<String, Object> bodyParamMap = new HashMap<>();
            bodyParamMap.put("username", username);
            bodyParamMap.put("password", password);
            bodyParamMap.put("serial", serial);
//            bodyParamMap.put("status", 1);
            wsConfig.setBodyArgAlias(bodyParamMap);
            String response = WebServiceHandler.webServiceCaller(wsConfig);
            logger.info(response);
            if (!DataUtil.isNullOrEmpty(response)) {
                List<XStreamStorage> parseObject = new ArrayList<>();
                Mapper.alias(parseObject, WsDtoContainer.class, "ns2:getHistoryCardNumberLockResponse");
                Mapper.addImplicitCollection(parseObject, WsDtoContainer.class, "list");
                Mapper.alias(parseObject, CardNumberLockDTO.class, "return");
                XmlStream xmlStream = new XmlStream();
                xmlStream.config(parseObject);
                xmlStream.isSingleType = false;
                WsDtoContainer lst = (WsDtoContainer) WebServiceHandler.wsServiceHandler(response, xmlStream);
                List<CardNumberLockDTO> result = (java.util.List) lst.getList();
                if (!DataUtil.isNullOrEmpty(result)) {
                    return result.get(0);
                }
            }
            return null;
        } catch (Exception ex) {
            logger.error("error getHistoryCardNumber " + ex.getMessage(), ex);
            throw new LogicException(ErrorCode.ERROR_CONNNECT.ERROR_SYSTEM_CC, "stock.error.call.ccService.check.card.lock", serial);
        }
    }

    @Override
    public HistoryInforCardNumberDTO getInforCardNumber(String serial, String regType, boolean isCheck, Date fromDate, Date toDate) throws LogicException, Exception {
        try {
            HistoryInforCardNumberDTO historyInforCardNumberDTO = new HistoryInforCardNumberDTO();
            historyInforCardNumberDTO.setResult(false);
            WsRequestCreator wsConfig = wsCallerFactory.createWsCaller(Const.CC_WS.ENDPOINTKEY,
                    Const.CC_WS.SERVICE_HISTORY_SEARCH, Const.CC_WS.FUNCTION_GET_INFOR_CARD_NUMBER);
            Map<String, Object> bodyParamMap = new HashMap<>();
            bodyParamMap.put("username", username);
            bodyParamMap.put("password", password);
            bodyParamMap.put("serial", serial);
            bodyParamMap.put("regType", regType);
            bodyParamMap.put("isCheckExtraCard", isCheck);
            bodyParamMap.put("fromDate", fromDate);
            bodyParamMap.put("toDate", toDate);
            wsConfig.setBodyArgAlias(bodyParamMap);
            String response = WebServiceHandler.webServiceCaller(wsConfig);
            if (!DataUtil.isNullOrEmpty(response)) {
                List<XStreamStorage> parseObject = new ArrayList<>();
                Mapper.alias(parseObject, HistoryInforCardNumberDTO.class, "return");
                Mapper.addImplicitCollection(parseObject, HistoryInforCardNumberDTO.class, "lstCardNumberLock");
                Mapper.alias(parseObject, CardNumberLockDTO.class, "lstCardNumberLock");
                Mapper.addImplicitCollection(parseObject, HistoryInforCardNumberDTO.class, "lstRefillInfoHistoryCardNumberOnOther");
                Mapper.alias(parseObject, RefillInfoDTO.class, "lstRefillInfoHistoryCardNumberOnOther");
                Mapper.addImplicitCollection(parseObject, HistoryInforCardNumberDTO.class, "lstRefillInfoHistoryCardNumberOnOCS");
                Mapper.alias(parseObject, RefillInfoDTO.class, "lstRefillInfoHistoryCardNumberOnOCS");
                Mapper.addImplicitCollection(parseObject, HistoryInforCardNumberDTO.class, "listCardInfoExtra");
                Mapper.alias(parseObject, CardInfoExtraDTO.class, "listCardInfoExtra");
                XmlStream xmlStream = new XmlStream();
                xmlStream.config(parseObject);
                xmlStream.isSingleType = true;
                historyInforCardNumberDTO = (HistoryInforCardNumberDTO) WebServiceHandler.wsServiceHandler(response, xmlStream);
                if (historyInforCardNumberDTO != null && historyInforCardNumberDTO.isResult()) {
                    historyInforCardNumberDTO.setResult(true);
                }
            }
            return historyInforCardNumberDTO;
        } catch (Exception ex) {
            logger.error("error getHistoryCardNumber " + ex.getMessage(), ex);
            throw new LogicException(ErrorCode.ERROR_CONNNECT.ERROR_SYSTEM_CC, "stock.error.call.ccService.check.card.lock", serial);
        }
    }

    @Override
    public BaseMessage lockedCard(String user, String serial, String shopCode, String reason, String isdn, String ip, String valueCard) throws Exception {
        try {
            BaseMessage result = new BaseMessage();
            WsRequestCreator wsConfig = wsCallerFactory.createWsCaller(Const.CC_WS.ENDPOINTKEY_LOCKCARD,
                    Const.CC_WS.SERVICE_BLOCK_CARD, Const.CC_WS.FUNCTION_LOCK_CARD);
            Map<String, Object> bodyParamMap = new HashMap<>();
            bodyParamMap.put("serial", serial);
            bodyParamMap.put("shopCode", shopCode);
            bodyParamMap.put("reason", reason);
            bodyParamMap.put("isdn", isdn);
            bodyParamMap.put("user", user);
            bodyParamMap.put("ip", ip);
            bodyParamMap.put("valueCard", valueCard);
            wsConfig.setBodyArgAlias(bodyParamMap);
            String response = WebServiceHandler.webServiceCaller(wsConfig);
            if (!DataUtil.isNullOrEmpty(response)) {
                List<XStreamStorage> parseObject = new ArrayList<>();
                Mapper.alias(parseObject, BaseMessage.class, "return");
                XmlStream xmlStream = new XmlStream();
                xmlStream.config(parseObject);
                xmlStream.isSingleType = true;
                result = (BaseMessage) WebServiceHandler.wsServiceHandler(response, xmlStream);
            }
            return result;
        } catch (Exception ex) {
            logger.error("error getHistoryCardNumber " + ex.getMessage(), ex);
            throw new LogicException(ErrorCode.ERROR_CONNNECT.ERROR_SYSTEM_CC, "stock.error.call.ccService.card.lock", serial);
        }
    }
}