package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.SignatureImageListObjDTO;
import com.viettel.bccs.inventory.dto.SignatureImageObjDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.service.BaseServiceImpl;
import com.viettel.security.PassTranformer;
import com.viettel.ws.common.utils.*;
import com.viettel.ws.provider.WsCallerFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AutoSignVOfficeServiceImpl extends BaseServiceImpl implements AutoSignVOfficeService {

    public static final Logger logger = Logger.getLogger(AutoSignVOfficeService.class);

    @Autowired
    private WsCallerFactory wsCallerFactory;


    @Override
    public boolean checkAccount(String username, String password) throws LogicException, Exception {
        try {
            WsRequestCreator wsConfig = wsCallerFactory.createWsCaller(AutoSignVOfficeService.class, Const.SERVICE_VOFFICE.CHECK_ACCOUNT, true);
            Map<String, Object> bodyParamMap = new HashMap<>();
            bodyParamMap.put("arg0", wsConfig.getUsername());
            bodyParamMap.put("arg1", wsConfig.getPassword());
            wsConfig.setBodyArgAlias(bodyParamMap);
            String response = WebServiceHandler.webServiceCaller(wsConfig);

            if (!DataUtil.isNullOrEmpty(response)) {
                List<XStreamStorage> parseObject = new ArrayList<>();
                Mapper.alias(parseObject, String.class, "return");
                XmlStream xmlStream = new XmlStream();
                xmlStream.config(parseObject);
                xmlStream.isSingleType = true;
                String result = (String) WebServiceHandler.wsServiceHandler(response, xmlStream);
                return "1".equals(result);
            }
        } catch (Exception ex) {
            logger.error("", ex);
            throw new LogicException(ErrorCode.ERROR_CONNNECT.ERROR_SYSTEM_VOFFICE, "common.voffice.error.ws", ex);
        }
        return false;
    }

    @Override
    public String getSignStatus(String transCode) throws LogicException, Exception {
        try {
            List<SignatureImageObjDTO> lsOffice;

            WsRequestCreator wsConfig = wsCallerFactory.createWsCaller(Const.SIGN_OFFICE_WS.ENDPOINT_KEY, "Ver3AutoSignSystemImpl",
                    Const.SIGN_OFFICE_WS.FUNCTION_SIGN_STATUS_LIST);
            Map<String, Object> bodyParamMap = new HashMap<>();
            ResourceBundle resource = ResourceBundle.getBundle("spring");
            String appCode = resource.getString("voffice.ws.appCode");
            if (appCode != null && appCode.trim().startsWith("ENC(") && appCode.endsWith(")")) {
                appCode = appCode.substring(4, appCode.length() - 1);
                appCode = PassTranformer.decrypt(appCode);
            }
            bodyParamMap.put("appCode", appCode);
            bodyParamMap.put("tranCode", transCode);
            wsConfig.setBodyArgAlias(bodyParamMap);
            String response = WebServiceHandler.webServiceCaller(wsConfig);
            logger.info(response);
            if (!DataUtil.isNullOrEmpty(response)) {
                List<XStreamStorage> parseObject = new ArrayList<>();
                Mapper.alias(parseObject, SignatureImageListObjDTO.class, "ns2:getListSignImageResponse");
                Mapper.alias(parseObject, SignatureImageListObjDTO.class, "return");
                Mapper.addImplicitCollection(parseObject, SignatureImageListObjDTO.class, "lstSignatureImageObj");
                Mapper.alias(parseObject, SignatureImageObjDTO.class, "lstSignatureImageObj");
                XmlStream xmlStream = new XmlStream();
                xmlStream.config(parseObject);
                xmlStream.isSingleType = true;
                SignatureImageListObjDTO lst = (SignatureImageListObjDTO) WebServiceHandler.wsServiceHandler(response, xmlStream);
                lsOffice = lst.getLstSignatureImageObj();
                if (!DataUtil.isNullOrEmpty(lsOffice)) {
                    return lsOffice.get(0).getState();
                }
            }
            return "";
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new LogicException(ErrorCode.ERROR_CONNNECT.ERROR_SYSTEM_VOFFICE, "common.voffice.error.ws", ex);
        }
    }
}
