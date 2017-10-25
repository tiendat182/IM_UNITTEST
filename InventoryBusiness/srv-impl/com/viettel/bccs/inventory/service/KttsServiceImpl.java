package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.fw.service.BaseServiceImpl;
import com.viettel.ws.common.utils.*;
import com.viettel.ws.provider.WsCallerFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author thanhnt77
 */
@Service
public class KttsServiceImpl extends BaseServiceImpl implements KttsService {
    public static final Logger logger = Logger.getLogger(KttsServiceImpl.class);

    @Autowired
    private WsCallerFactory wsCallerFactory;

    @Override
    @WebMethod
    public List<PartnerContractWsDTO> searchContractBCCS(@WebParam(name = "fromDate") String fromDate, @WebParam(name = "toDate") String toDate,
                                                        @WebParam(name = "contractCode") String contractCode) throws LogicException, Exception {
        try {
            WsRequestCreator wsConfig = wsCallerFactory.createWsCaller(Const.KTTS_WS.ENDPOINT_KEY, Const.KTTS_WS.FUNCTION_SEARCH_CONTRACT);
            Map<String, Object> bodyParamMap = new HashMap<>();
            if (!DataUtil.isNullOrEmpty(fromDate)) {
                bodyParamMap.put("fromDate", fromDate);
            }
            if (!DataUtil.isNullOrEmpty(toDate)) {
                bodyParamMap.put("toDate", toDate);
            }
            if (!DataUtil.isNullOrEmpty(contractCode)) {
                bodyParamMap.put("contractCode", contractCode);
            }
            wsConfig.setBodyArgAlias(bodyParamMap);
            String response = WebServiceHandler.webServiceCaller(wsConfig);
            if (!DataUtil.isNullOrEmpty(response)) {
                List<XStreamStorage> parseObject = new ArrayList<>();
                Mapper.alias(parseObject, PartnerContractListWsDTO.class, "ns2:searchContractBCCSResponse");
                Mapper.addImplicitCollection(parseObject, PartnerContractListWsDTO.class, "list");
                Mapper.alias(parseObject, PartnerContractWsDTO.class, "return");
                XmlStream xmlStream = new XmlStream();
                xmlStream.config(parseObject);
                xmlStream.isSingleType = false;
                PartnerContractListWsDTO dto = (PartnerContractListWsDTO) WebServiceHandler.wsServiceHandler(response, xmlStream);
                if (dto != null && dto.getList() != null) {
                    return dto.getList();
                }
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new LogicException(ErrorCode.ERROR_CONNNECT.ERROR_SYSTEM_KTTS, "common.ws.ktts.error", ex);
        }
        return new ArrayList<>();
    }

    @Override
    @WebMethod
    public List<PartnerShipmentWsDTO> searchShipmentBCCS(@WebParam(name = "contractCode") String contractCode) throws LogicException, Exception {
        try {
            WsRequestCreator wsConfig = wsCallerFactory.createWsCaller(Const.KTTS_WS.ENDPOINT_KEY, Const.KTTS_WS.FUNCTION_SEARCH_CONTRACT_DETAIL);
            Map<String, Object> bodyParamMap = new HashMap<>();
            bodyParamMap.put("contractCode", contractCode);
            wsConfig.setBodyArgAlias(bodyParamMap);
            String response = WebServiceHandler.webServiceCaller(wsConfig);
            if (!DataUtil.isNullOrEmpty(response)) {
                List<XStreamStorage> parseObject = new ArrayList<>();
                Mapper.alias(parseObject, PartnerShipmentListWsDTO.class, "ns2:searchShipmentBCCSResponse");
                Mapper.addImplicitCollection(parseObject, PartnerShipmentListWsDTO.class, "lsPartnerDetail");
                Mapper.alias(parseObject, PartnerShipmentWsDTO.class, "return");
                XmlStream xmlStream = new XmlStream();
                xmlStream.config(parseObject);
                xmlStream.isSingleType = false;
                PartnerShipmentListWsDTO dto = (PartnerShipmentListWsDTO) WebServiceHandler.wsServiceHandler(response, xmlStream);
                if (dto != null && dto.getLsPartnerDetail() != null) {
                    return dto.getLsPartnerDetail();
                }
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new LogicException(ErrorCode.ERROR_CONNNECT.ERROR_SYSTEM_KTTS, "common.ws.ktts.error", ex);
        }
        return new ArrayList<>();
    }

    @Override
    @WebMethod
    public BaseMessage getResultImpShipment(@WebParam(name="reportKCSCode") String reportKCSCode,
                                            @WebParam(name="bccsStatus")String bccsStatus) throws LogicException, Exception {
        BaseMessage message = new BaseMessage();
        try {
            WsRequestCreator wsConfig = wsCallerFactory.createWsCaller(Const.KTTS_WS.ENDPOINT_KEY, Const.KTTS_WS.FUNCTION_SEARCH_GET_RESULT_IMP_SHIPMENT);
            Map<String, Object> bodyParamMap = new HashMap<>();
            bodyParamMap.put("reportKCSCode", reportKCSCode);
            bodyParamMap.put("bccsStatus", bccsStatus);
            wsConfig.setBodyArgAlias(bodyParamMap);
            String response = WebServiceHandler.webServiceCaller(wsConfig);
            if (!DataUtil.isNullOrEmpty(response)) {
                List<XStreamStorage> parseObject = new ArrayList<>();
                Mapper.alias(parseObject, PartnerShipmentResultWsDTO.class, "return");
                XmlStream xmlStream = new XmlStream();
                xmlStream.config(parseObject);
                xmlStream.isSingleType = true;
                PartnerShipmentResultWsDTO dto = (PartnerShipmentResultWsDTO) WebServiceHandler.wsServiceHandler(response, xmlStream);
                if (dto != null) {
                    message.setSuccess(!"0".equals(dto.getStatus()));
                    message.setDescription(dto.getReason());
                    return message;
                }
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new LogicException(ErrorCode.ERROR_CONNNECT.ERROR_SYSTEM_KTTS, "common.ws.ktts.error", ex);
        }
        return new BaseMessage();
    }
}