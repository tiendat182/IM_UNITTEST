package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.message.AddGoodSaleMessage;
import com.viettel.bccs.sale.dto.SaleTransDTO;
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
import org.springframework.stereotype.Service;

import javax.jws.WebMethod;
import java.util.*;

@Service
public class SaleWsImpl extends BaseServiceImpl implements SaleWs {
    public static final Logger logger = Logger.getLogger(SaleWs.class);
    @Autowired
    private WsCallerFactory wsCallerFactory;

    @Override
    @WebMethod
    public AddGoodSaleMessage addGoodAgent(SaleTransInfoMin transInfoMin, Long shopId, Long staffID, Long salesProgramId, Long payMethodId, Long reasonId) throws Exception, LogicException {
        try {
            WsRequestCreator wsConfig = wsCallerFactory.createWsCaller(Const.SALE_WS.ENDPOINTKEY_SALE_AGENT,
                    Const.SALE_WS.SERVICE_SALE_AGENT, Const.SALE_WS.FUNCTION_AGENT_ADD_GOOD);
            Map<String, Object> bodyParamMap = new HashMap<>();
            bodyParamMap.put("saleTransInfoMin", transInfoMin);
            bodyParamMap.put("shopId", shopId);
            bodyParamMap.put("staffId", staffID);
            bodyParamMap.put("salesProgramId", salesProgramId);
            bodyParamMap.put("payMethodId", payMethodId);
            bodyParamMap.put("reasonId", reasonId);
            wsConfig.setBodyArgAlias(bodyParamMap);
            String response = WebServiceHandler.webServiceCaller(wsConfig);
            logger.info(response);
            if (!DataUtil.isNullOrEmpty(response)) {
                List<XStreamStorage> parseObject = new ArrayList<>();
                Mapper.alias(parseObject, AddGoodSaleMessage.class, "return");
                XmlStream xmlStream = new XmlStream();
                xmlStream.config(parseObject);
                xmlStream.isSingleType = true;
                AddGoodSaleMessage result = (AddGoodSaleMessage) WebServiceHandler.wsServiceHandler(response, xmlStream);
                return result;
            }
            return null;
        } catch (Exception ex) {
            logger.error("error addGoodAgent " + ex.getMessage(), ex);
            throw new LogicException(ErrorCode.ERROR_CONNNECT.ERROR_SYSTEM_SALE, "error.connect.sale.service", ex);
        }
    }

    @Override
    @WebMethod
    public AddGoodSaleMessage saleToAgent(SaleTransInfoMin transInfoMin, Long shopId, Long staffID, Long salesProgramId, Long payMethodId, Long reasonId) throws Exception, LogicException {
        try {
            WsRequestCreator wsConfig = wsCallerFactory.createWsCaller(Const.SALE_WS.ENDPOINTKEY_SALE_AGENT,
                    Const.SALE_WS.SERVICE_SALE_AGENT, Const.SALE_WS.FUNCTION_AGENT_SALE);
            Map<String, Object> bodyParamMap = new HashMap<>();
            bodyParamMap.put("saleTransInfoMin", transInfoMin);
            bodyParamMap.put("shopId", shopId);
            bodyParamMap.put("staffId", staffID);
            bodyParamMap.put("salesProgramId", salesProgramId);
            bodyParamMap.put("payMethodId", payMethodId);
            bodyParamMap.put("reasonId", reasonId);
            wsConfig.setBodyArgAlias(bodyParamMap);
            String response = WebServiceHandler.webServiceCaller(wsConfig);
            logger.info(response);
            if (!DataUtil.isNullOrEmpty(response)) {
                List<XStreamStorage> parseObject = new ArrayList<>();
                Mapper.alias(parseObject, AddGoodSaleMessage.class, "return");
                XmlStream xmlStream = new XmlStream();
                xmlStream.config(parseObject);
                xmlStream.isSingleType = true;
                AddGoodSaleMessage result = (AddGoodSaleMessage) WebServiceHandler.wsServiceHandler(response, xmlStream);
                return result;
            }
            return null;
        } catch (Exception ex) {
            logger.error("error addGoodAgent " + ex.getMessage(), ex);
            throw new LogicException(ErrorCode.ERROR_CONNNECT.ERROR_SYSTEM_SALE, "error.connect.sale.service", ex);
        }
    }

    @Override
    @WebMethod
    public List<ReasonSaleToAgentDTO> getReasonSaleToAgent() throws Exception, LogicException {
        try {
            WsRequestCreator wsConfig = wsCallerFactory.createWsCaller(Const.SALE_WS.ENDPOINTKEY_SALE,
                    Const.SALE_WS.SERVICE_REASON_SALE_AGENT, Const.SALE_WS.FUNCTION_GET_REASON);
            Map<String, Object> bodyParamMap = new HashMap<>();
            bodyParamMap.put("arg0", "REASON_SALE_TRANS_AGENT");// Lay ly do ban hang cho dai ly. old_value = 2
            wsConfig.setBodyArgAlias(bodyParamMap);
            String response = WebServiceHandler.webServiceCaller(wsConfig);
            logger.info(response);
            if (!DataUtil.isNullOrEmpty(response)) {
                List<XStreamStorage> parseObject = new ArrayList<>();
                Mapper.alias(parseObject, WsDtoContainer.class, "ns2:findBySaleTransTypeResponse");
                Mapper.addImplicitCollection(parseObject, WsDtoContainer.class, "list");
                Mapper.alias(parseObject, ReasonSaleToAgentDTO.class, "return");
                XmlStream xmlStream = new XmlStream();
                xmlStream.config(parseObject);
                xmlStream.isSingleType = false;
                WsDtoContainer lst = (WsDtoContainer) WebServiceHandler.wsServiceHandler(response, xmlStream);
                List<ReasonSaleToAgentDTO> result = (java.util.List) lst.getList();
                return result;
            }
            return null;
        } catch (Exception ex) {
            logger.error("error getReasonSaleToAgent " + ex.getMessage(), ex);
            throw new LogicException(ErrorCode.ERROR_CONNNECT.ERROR_SYSTEM_SALE, "[Sale] Lỗi lấy lý do bán hàng", ex);
        }
    }

    @Override
    @WebMethod
    public List<OptionSetValueDTO> getPayMethod() throws Exception, LogicException {
        try {
            WsRequestCreator wsConfig = wsCallerFactory.createWsCaller(Const.SALE_WS.ENDPOINTKEY_SALE,
                    Const.SALE_WS.SERVICE_APPPARAM_SALE_AGENT, Const.SALE_WS.FUNCTION_GET_PAY_METHOD);//ENDPOINTKEY_APPPARAM_SALE_AGENT
            String response = WebServiceHandler.webServiceCaller(wsConfig);
            logger.info(response);
            if (!DataUtil.isNullOrEmpty(response)) {
                List<XStreamStorage> parseObject = new ArrayList<>();
                Mapper.alias(parseObject, WsDtoContainer.class, "ns2:findAgentPayMethodResponse");
                Mapper.addImplicitCollection(parseObject, WsDtoContainer.class, "list");
                Mapper.alias(parseObject, OptionSetValueDTO.class, "return");
                XmlStream xmlStream = new XmlStream();
                xmlStream.config(parseObject);
                xmlStream.isSingleType = false;
                WsDtoContainer lst = (WsDtoContainer) WebServiceHandler.wsServiceHandler(response, xmlStream);
                List<OptionSetValueDTO> result = (java.util.List) lst.getList();
                return result;
            }
            return null;
        } catch (Exception ex) {
            logger.error("error getPayMethod " + ex.getMessage(), ex);
            throw new LogicException(ErrorCode.ERROR_CONNNECT.ERROR_SYSTEM_SALE, "error.connect.sale.service", ex);
        }
    }

    @Override
    @WebMethod
    public List<ProgramSaleDTO> getProgram() throws Exception, LogicException {
        try {
            WsRequestCreator wsConfig = wsCallerFactory.createWsCaller(Const.SALE_WS.ENDPOINTKEY_SALE_PROGRAM,
                    Const.SALE_WS.SERVICE_SALE_PROGRAM, Const.SALE_WS.FUNCTION_GET_PROGRAM);
            Map<String, Object> bodyParamMap = new HashMap<>();
            wsConfig.setBodyArgAlias(bodyParamMap);
            String response = WebServiceHandler.webServiceCaller(wsConfig);
            logger.info(response);
            if (!DataUtil.isNullOrEmpty(response)) {
                List<XStreamStorage> parseObject = new ArrayList<>();
                Mapper.alias(parseObject, WsDtoContainer.class, "ns2:findByFilterResponse");
                Mapper.addImplicitCollection(parseObject, WsDtoContainer.class, "list");
                Mapper.alias(parseObject, ProgramSaleDTO.class, "return");
                XmlStream xmlStream = new XmlStream();
                xmlStream.config(parseObject);
                xmlStream.isSingleType = false;
                WsDtoContainer lst = (WsDtoContainer) WebServiceHandler.wsServiceHandler(response, xmlStream);
                List<ProgramSaleDTO> result = (java.util.List) lst.getList();
                return result;
            }
        } catch (Exception ex) {
            logger.error("error getProgram " + ex.getMessage(), ex);
//            throw new LogicException(ErrorCode.ERROR_CONNNECT.ERROR_SYSTEM_SALE, "error.connect.sale.service", ex);
        }
        return null;
    }

    @Override
    public BaseMessage approveRequest(Long saleTransId, Date saleTransDate, Long approveStaffId) throws Exception, LogicException {
        try {
            WsRequestCreator wsConfig = wsCallerFactory.createWsCaller(Const.SALE_WS.ENDPOINTKEY_SALE_AGENT,
                    Const.SALE_WS.SERVICE_APPROVE_AGENT, Const.SALE_WS.FUNCTION_APPROVE_REQUEST);
            Map<String, Object> bodyParamMap = new HashMap<>();
            bodyParamMap.put("saleTransId", saleTransId);
            bodyParamMap.put("approveStaffId", approveStaffId);
            bodyParamMap.put("saleTransDate", saleTransDate);
            wsConfig.setBodyArgAlias(bodyParamMap);
            String response = WebServiceHandler.webServiceCaller(wsConfig);
            logger.info(response);
            if (!DataUtil.isNullOrEmpty(response)) {
                List<XStreamStorage> parseObject = new ArrayList<>();
                Mapper.alias(parseObject, BaseMessage.class, "return");
                XmlStream xmlStream = new XmlStream();
                xmlStream.config(parseObject);
                xmlStream.isSingleType = true;
                BaseMessage result = (BaseMessage) WebServiceHandler.wsServiceHandler(response, xmlStream);
                return result;
            }
            return null;
        } catch (Exception ex) {
            logger.error("error addGoodAgent " + ex.getMessage(), ex);
            throw new LogicException(ErrorCode.ERROR_CONNNECT.ERROR_SYSTEM_SALE, "error.connect.sale.service", ex);
        }
    }

    @Override
    public BaseMessage denyRequest(Long saleTransId, Date saleTransDate, Long denyStaffId, String denyStaffCode) throws Exception, LogicException {
        try {
            WsRequestCreator wsConfig = wsCallerFactory.createWsCaller(Const.SALE_WS.ENDPOINTKEY_SALE_AGENT,
                    Const.SALE_WS.SERVICE_APPROVE_AGENT, Const.SALE_WS.FUNCTION_DENY_REQUEST);
            Map<String, Object> bodyParamMap = new HashMap<>();
            bodyParamMap.put("saleTransId", saleTransId);
            bodyParamMap.put("denyStaffId", denyStaffId);
            bodyParamMap.put("saleTransDate", saleTransDate);
            bodyParamMap.put("denyStaffCode", denyStaffCode);
            wsConfig.setBodyArgAlias(bodyParamMap);
            String response = WebServiceHandler.webServiceCaller(wsConfig);
            logger.info(response);
            if (!DataUtil.isNullOrEmpty(response)) {
                List<XStreamStorage> parseObject = new ArrayList<>();
                Mapper.alias(parseObject, BaseMessage.class, "return");
                XmlStream xmlStream = new XmlStream();
                xmlStream.config(parseObject);
                xmlStream.isSingleType = true;
                BaseMessage result = (BaseMessage) WebServiceHandler.wsServiceHandler(response, xmlStream);
                return result;
            }
            return null;
        } catch (Exception ex) {
            logger.error("error addGoodAgent " + ex.getMessage(), ex);
            throw new LogicException(ErrorCode.ERROR_CONNNECT.ERROR_SYSTEM_SALE, "error.connect.sale.service", ex);
        }
    }

    @Override
    public List<StockApproveAgentDTO> getStockOrderAgent(Date fromDate, Date toDate, Long shopId, Long approveStatus, String receiverShopCode) throws Exception, LogicException {
        try {
            List<StockApproveAgentDTO> result = Lists.newArrayList();
            WsRequestCreator wsConfig = wsCallerFactory.createWsCaller(Const.SALE_WS.ENDPOINTKEY_SALE_AGENT,
                    Const.SALE_WS.SERVICE_APPROVE_AGENT, Const.SALE_WS.FUNCTION_STOCK_ORDER_AGENT);
            Map<String, Object> bodyParamMap = new HashMap<>();
            bodyParamMap.put("shopId", shopId);
            bodyParamMap.put("approveStatus", approveStatus);
            bodyParamMap.put("receiverShopCode", receiverShopCode);
            bodyParamMap.put("fromDate", fromDate);
            bodyParamMap.put("toDate", toDate);
            wsConfig.setBodyArgAlias(bodyParamMap);
            String response = WebServiceHandler.webServiceCaller(wsConfig);
            logger.info(response);
            if (!DataUtil.isNullOrEmpty(response)) {
                List<XStreamStorage> parseObject = new ArrayList<>();
                Mapper.alias(parseObject, WsDtoContainer.class, "ns2:getStockOrderAgentResponse");
                Mapper.addImplicitCollection(parseObject, WsDtoContainer.class, "list");
                Mapper.alias(parseObject, StockApproveAgentDTO.class, "return");
                XmlStream xmlStream = new XmlStream();
                xmlStream.config(parseObject);
                xmlStream.isSingleType = false;
                WsDtoContainer lst = (WsDtoContainer) WebServiceHandler.wsServiceHandler(response, xmlStream);
                result = (java.util.List) lst.getList();
            }
            return result;
        } catch (Exception ex) {
            logger.error("error getPayMethod " + ex.getMessage(), ex);
            throw new LogicException(ErrorCode.ERROR_CONNNECT.ERROR_SYSTEM_SALE, "error.connect.sale.service", ex);
        }
    }

    @Override
    public List<StockApproveAgentDetailDTO> getStockOrderAgentDetail(Long saleTransId, Date fromDate, Date toDate) throws Exception, LogicException {
        try {
            WsRequestCreator wsConfig = wsCallerFactory.createWsCaller(Const.SALE_WS.ENDPOINTKEY_SALE_AGENT,
                    Const.SALE_WS.SERVICE_APPROVE_AGENT, Const.SALE_WS.FUNCTION_STOCK_ORDER_AGENT_DETAIL);
            Map<String, Object> bodyParamMap = new HashMap<>();
            bodyParamMap.put("saleTransId", saleTransId);
            bodyParamMap.put("fromDate", fromDate);
            bodyParamMap.put("toDate", toDate);
            wsConfig.setBodyArgAlias(bodyParamMap);
            String response = WebServiceHandler.webServiceCaller(wsConfig);
            logger.info(response);
            if (!DataUtil.isNullOrEmpty(response)) {
                List<XStreamStorage> parseObject = new ArrayList<>();
                Mapper.alias(parseObject, WsDtoContainer.class, "ns2:getStockOrderAgentDetailResponse");
                Mapper.addImplicitCollection(parseObject, WsDtoContainer.class, "list");
                Mapper.alias(parseObject, StockApproveAgentDetailDTO.class, "return");
                XmlStream xmlStream = new XmlStream();
                xmlStream.config(parseObject);
                xmlStream.isSingleType = false;
                WsDtoContainer lst = (WsDtoContainer) WebServiceHandler.wsServiceHandler(response, xmlStream);
                List<StockApproveAgentDetailDTO> result = (java.util.List) lst.getList();
                return result;
            }
            return null;
        } catch (Exception ex) {
            logger.error("error getPayMethod " + ex.getMessage(), ex);
            throw new LogicException(ErrorCode.ERROR_CONNNECT.ERROR_SYSTEM_SALE, "error.connect.sale.service", ex);
        }
    }

    @Override
    public void findTransExpiredPayWS(Long staffId) throws LogicException, Exception {
        try {
            WsRequestCreator wsConfig = wsCallerFactory.createWsCaller(Const.SALE_WS.ENDPOINTKEY_SALE_AGENT, Const.SALE_WS.SERVICE_SALE_AGENT, Const.SALE_WS.FUNCTION_CHECK_DEBIT_STAFF);
            BaseMessage message = (BaseMessage) WebServiceHandler.defaultWebServiceHandler(wsConfig, BaseMessage.class, false, getGenericWebInfo(), staffId);
            if (!message.isSuccess()) {
                LogicException ex = new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "");
                ex.setDescription(message.getDescription());
                ex.setKeyMsg(message.getKeyMsg());
                throw ex;
            }
        } catch (Exception ex) {
            logger.error("error getPayMethod " + ex.getMessage(), ex);
            throw new LogicException(ErrorCode.ERROR_CONNNECT.ERROR_SYSTEM_SALE, "error.connect.sale.service", ex);
        }
    }

    @Override
    @WebMethod
    public BaseMessage checkActiveSubscriber(String isdn) throws LogicException, Exception {
        try {
            BaseMessage message = new BaseMessage();
            WsRequestCreator wsConfig = wsCallerFactory.createWsCaller(Const.SALE_WS.ENDPOINTKEY_SALE_SYSTEM_AGENT, Const.SALE_WS.END_POINT_SALE_INTERFACE, Const.SALE_WS.FUNCTION_SALE_VALIDATE_ACTIVE_SUB);

            Map<String, Object> bodyParamMap = new HashMap<>();
            bodyParamMap.put("isdn", isdn);
            wsConfig.setBodyArgAlias(bodyParamMap);
            String response = WebServiceHandler.webServiceCaller(wsConfig);
            logger.info(response);
            if (!DataUtil.isNullOrEmpty(response)) {
                List<XStreamStorage> parseObject = new ArrayList<>();
                Mapper.alias(parseObject, BaseMessage.class, "return");
                Mapper.aliasField(parseObject, BaseMessage.class, "code", "errorCode");
                Mapper.aliasField(parseObject, BaseMessage.class, "message", "description");
                XmlStream xmlStream = new XmlStream();
                xmlStream.config(parseObject);
                xmlStream.isSingleType = true;
                message = (BaseMessage) WebServiceHandler.wsServiceHandler(response, xmlStream);
                if (Const.STRING_CONST_ZERO.equals(message.getErrorCode())) {
                    message.setSuccess(true);
                }
                return message;
            }
            return message;

        } catch (Exception ex) {
            logger.error("error getPayMethod " + ex.getMessage(), ex);
            throw new LogicException(ErrorCode.ERROR_CONNNECT.ERROR_SYSTEM_SALE, "error.connect.sale.service", ex);
        }
    }


    @Override
    @WebMethod
    public BaseMessage checkActiveSerial(String fromSerial, String toSerial) throws LogicException, Exception {
        try {
            BaseMessage message = new BaseMessage();
            WsRequestCreator wsConfig = wsCallerFactory.createWsCaller(Const.SALE_WS.ENDPOINTKEY_SALE_SYSTEM_AGENT, Const.SALE_WS.END_POINT_SALE_INTERFACE, Const.SALE_WS.FUNCTION_SALE_CHECK_ACTIVE_SERIAL_SUB);

            Map<String, Object> bodyParamMap = new HashMap<>();
            bodyParamMap.put("fromSerial", fromSerial);
            bodyParamMap.put("toSerial", toSerial);
            wsConfig.setBodyArgAlias(bodyParamMap);
            String response = WebServiceHandler.webServiceCaller(wsConfig);
            logger.info(response);
            if (!DataUtil.isNullOrEmpty(response)) {
                List<XStreamStorage> parseObject = new ArrayList<>();
                Mapper.alias(parseObject, BaseMessage.class, "return");
                Mapper.aliasField(parseObject, BaseMessage.class, "code", "errorCode");
                Mapper.aliasField(parseObject, BaseMessage.class, "message", "description");
                XmlStream xmlStream = new XmlStream();
                xmlStream.config(parseObject);
                xmlStream.isSingleType = true;
                message = (BaseMessage) WebServiceHandler.wsServiceHandler(response, xmlStream);
                if (Const.STRING_CONST_ZERO.equals(message.getErrorCode())) {
                    message.setSuccess(true);
                }
                return message;
            }
            return message;

        } catch (Exception ex) {
            logger.error("error getPayMethod " + ex.getMessage(), ex);
            throw new LogicException(ErrorCode.ERROR_CONNNECT.ERROR_SYSTEM_SALE, "error.connect.sale.service", ex);
        }
    }

    @Override
    public ChangeGoodMessage viewChangeGood(Long oldProdOfferId, String oldSerial, int dateInterval, Long staffId, Date fromDate, Date toDate) throws Exception, LogicException {
        try {
            WsRequestCreator wsConfig = wsCallerFactory.createWsCaller(Const.SALE_WS.ENDPOINTKEY_SALE_AGENT,
                    Const.SALE_WS.SERVICE_SALE_AGENT, Const.SALE_WS.FUNCTION_VIEW_CHANGE_GOOD);
            Map<String, Object> bodyParamMap = new HashMap<>();
            bodyParamMap.put("oldProdOfferId", oldProdOfferId);
            bodyParamMap.put("oldSerial", oldSerial);
            bodyParamMap.put("dateInterval", dateInterval);
            bodyParamMap.put("staffId", staffId);
            bodyParamMap.put("fromDate", fromDate);
            bodyParamMap.put("toDate", toDate);
            wsConfig.setBodyArgAlias(bodyParamMap);
            String response = WebServiceHandler.webServiceCaller(wsConfig);
            logger.info(response);
            if (!DataUtil.isNullOrEmpty(response)) {
                List<XStreamStorage> parseObject = new ArrayList<>();
                Mapper.alias(parseObject, ChangeGoodMessage.class, "return");
                XmlStream xmlStream = new XmlStream();
                xmlStream.config(parseObject);
                xmlStream.isSingleType = true;
                ChangeGoodMessage lst = (ChangeGoodMessage) WebServiceHandler.wsServiceHandler(response, xmlStream);
//                return lst != null ? lst.getReportChangeHandsetDTO() : null;
                return lst;
            }
        } catch (Exception ex) {
            logger.error("error getPayMethod " + ex.getMessage(), ex);
            throw new LogicException(ErrorCode.ERROR_CONNNECT.ERROR_SYSTEM_SALE, "error.connect.sale.service", ex);
        }
        return null;
    }

    @Override
    public ChangeGoodMessage changeGood(Long oldProdOfferId, String oldSerial, Long newProdOfferId, String newSerial, int dateInterval, Long staffId, Date fromDate, Date toDate) throws Exception, LogicException {
        try {
            WsRequestCreator wsConfig = wsCallerFactory.createWsCaller(Const.SALE_WS.ENDPOINTKEY_SALE_AGENT, Const.SALE_WS.SERVICE_SALE_AGENT, Const.SALE_WS.FUNCTION_SAVE_CHANGE_GOOD);
            Map<String, Object> bodyParamMap = new HashMap<>();
            bodyParamMap.put("oldProdOfferId", oldProdOfferId);
            bodyParamMap.put("oldSerial", oldSerial);
            bodyParamMap.put("newProdOfferId", newProdOfferId);
            bodyParamMap.put("newSerial", newSerial);
            bodyParamMap.put("dateInterval", dateInterval);
            bodyParamMap.put("staffId", staffId);
            bodyParamMap.put("fromDate", fromDate);
            bodyParamMap.put("toDate", toDate);
            wsConfig.setBodyArgAlias(bodyParamMap);
            String response = WebServiceHandler.webServiceCaller(wsConfig);
            logger.info(response);
            if (!DataUtil.isNullOrEmpty(response)) {
                List<XStreamStorage> parseObject = new ArrayList<>();
                Mapper.alias(parseObject, ChangeGoodMessage.class, "return");
                XmlStream xmlStream = new XmlStream();
                xmlStream.config(parseObject);
                xmlStream.isSingleType = true;
                ChangeGoodMessage lst = (ChangeGoodMessage) WebServiceHandler.wsServiceHandler(response, xmlStream);
                return lst;
            }
        } catch (Exception ex) {
            logger.error("error getPayMethod " + ex.getMessage(), ex);
            throw new LogicException(ErrorCode.ERROR_CONNNECT.ERROR_SYSTEM_SALE, "error.connect.sale.service", ex);
        }
        return null;
    }

    @Override
    public List<SubGoodsDTO> getLsSubGoodsByTeamCodeAndProdOfferId(String teamCode, Long prodOfferId) throws Exception, LogicException {
        try {
            WsRequestCreator wsConfig = wsCallerFactory.createWsCaller(Const.SALE_WS.ENDPOINTKEY_SALE,
                    "SubGoodsService", Const.SALE_WS.FUNCTION_COUNT_TOTAL_PRODUCT_TEAMCODE);
            Map<String, Object> bodyParamMap = new HashMap<>();
//            bodyParamMap.put("arg0", "HNI09");//HNI09
//            bodyParamMap.put("arg1", 22526L);//22526
            bodyParamMap.put("arg0", teamCode);//HNI09
            bodyParamMap.put("arg1", prodOfferId);//22526
            wsConfig.setBodyArgAlias(bodyParamMap);
            String response = WebServiceHandler.webServiceCaller(wsConfig);
            logger.info(response);
            if (!DataUtil.isNullOrEmpty(response)) {
                List<XStreamStorage> parseObject = new ArrayList<>();
                Mapper.alias(parseObject, WsDtoContainer.class, "ns2:countTotalStockModelByTeamCodeResponse");
                Mapper.addImplicitCollection(parseObject, WsDtoContainer.class, "list");
                Mapper.alias(parseObject, SubGoodsDTO.class, "return");
                XmlStream xmlStream = new XmlStream();
                xmlStream.config(parseObject);
                xmlStream.isSingleType = false;
                WsDtoContainer lst = (WsDtoContainer) WebServiceHandler.wsServiceHandler(response, xmlStream);
                List<SubGoodsDTO> result = (java.util.List) lst.getList();
                return result;
            }
            return null;
        } catch (Exception ex) {
            logger.error("error getPayMethod " + ex.getMessage(), ex);
            throw new LogicException(ErrorCode.ERROR_CONNNECT.ERROR_SYSTEM_SALE, "error.connect.sale.service", ex);
        }
    }
}