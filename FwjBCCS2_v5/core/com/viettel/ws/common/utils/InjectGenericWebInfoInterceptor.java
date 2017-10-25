package com.viettel.ws.common.utils;

import com.viettel.fw.common.util.Const;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.dto.UserDTO;
import com.viettel.fw.passport.CustomConnector;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.interceptor.AbstractSoapInterceptor;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.phase.Phase;
import org.omnifaces.util.Faces;
import org.springframework.stereotype.Service;
import viettel.passport.client.UserToken;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.soap.*;

/**
 * Created by LamNV5 on 5/27/2015.
 */
//@Service
public class InjectGenericWebInfoInterceptor extends AbstractSoapInterceptor {



    public InjectGenericWebInfoInterceptor() {
        super(Phase.WRITE);
    }

    @Override
    public void handleMessage(SoapMessage message) throws Fault {
        SOAPMessage sm = message.getContent(SOAPMessage.class);
        try {
            SOAPFactory sf = SOAPFactory.newInstance();
            SOAPHeader sh = sm.getSOAPHeader();
            if (sh == null) {
                sh = sm.getSOAPPart().getEnvelope().addHeader();
            }

            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            String ipAddress = request.getHeader("X-FORWARDED-FOR");
            if (ipAddress == null) {
                ipAddress = request.getRemoteAddr();
            }
            UserDTO userDTO = (UserDTO) request.getSession().getAttribute(Const.USER_DTO_TOKEN);
            if (userDTO == null) {
                userDTO = new UserDTO();
                UserToken userToken = (UserToken) request.getSession().getAttribute(CustomConnector.VSA_USER_TOKEN);
                if (userToken != null && userToken.getUserName() != null) {
                    userDTO.setStaffCode(userToken.getUserName().toUpperCase());
                }
            }
            String language = FacesContext.getCurrentInstance().getViewRoot().getLocale().getLanguage();
            String country = FacesContext.getCurrentInstance().getViewRoot().getLocale().getCountry();
            String callId = request.getHeader("VTS-KPIID");
            if (callId == null) {
                callId = (String) request.getAttribute(Const.CALL_ID_TOKEN);
                if (callId == null) {
                    callId = RandomStringUtils.randomNumeric(10);
                }
            }
            request.setAttribute(Const.CALL_ID_TOKEN, callId);
            final Marshaller marshaller = JAXBContext.newInstance(GenericWebInfo.class).createMarshaller();
            GenericWebInfo objectGeneric = new GenericWebInfo();
            objectGeneric.setStaffCode(userDTO.getStaffCode());
            objectGeneric.setShopCode(userDTO.getShopCode());
            objectGeneric.setShopId(userDTO.getShopId());
            objectGeneric.setStaffId(userDTO.getStaffId());
            objectGeneric.setIpAddress(ipAddress);
            objectGeneric.setLanguage(language);
            objectGeneric.setCountry(country);
            objectGeneric.setReqId(callId);

            try {
                //try set sessionId
                objectGeneric.setSessionId(Faces.getSessionId()+"@"+callId);
                objectGeneric.setServerPort(String.valueOf(request.getServerPort()));
                objectGeneric.setServerAddress(String.valueOf(request.getServerName()));
            }catch (Exception e){

            }
            marshaller.marshal(objectGeneric, sh);

            //20160330 thiendn1:thay doi su dung phuong thuc khac
//
//            Name genericWebInfo = sf.createName("genericWebInfo", "ns2", "http://service.ws.viettel.com/");
//            SOAPHeaderElement shElement = sh.addHeaderElement(genericWebInfo);
//
//            SOAPElement staffElement = shElement.addChildElement("staffCode");
//            staffElement.addTextNode(DataUtil.defaultIfNull(userDTO.getStaffCode(), ""));
//            shElement.addChildElement(staffElement);
//
//            SOAPElement shopElement = shElement.addChildElement("shopCode");
//            shopElement.addTextNode(DataUtil.defaultIfNull(userDTO.getShopCode(), ""));
//            shElement.addChildElement(shopElement);
//
//            SOAPElement shopIdElement = shElement.addChildElement("shopId");
//            shopIdElement.addTextNode(userDTO.getShopId() != null ? userDTO.getShopId().toString() : "");
//            shElement.addChildElement(shopIdElement);
//
//            SOAPElement staffIdElement = shElement.addChildElement("staffId");
//            staffIdElement.addTextNode(userDTO.getStaffId() != null ? userDTO.getStaffId().toString() : "");
//            shElement.addChildElement(staffIdElement);
//
//            SOAPElement ipAddressElement = shElement.addChildElement("ipAddress");
//            ipAddressElement.addTextNode(ipAddress);
//            shElement.addChildElement(ipAddressElement);
//
//            SOAPElement languageElement = shElement.addChildElement("language");
//            languageElement.addTextNode(language);
//            shElement.addChildElement(languageElement);
//
//            SOAPElement countryElement = shElement.addChildElement("country");
//            countryElement.addTextNode(country);
//            shElement.addChildElement(countryElement);
//
//            String callId = (String) request.getAttribute(Const.CALL_ID_TOKEN);
//            if (callId == null) {
//                callId = RandomStringUtils.randomNumeric(15);
//                request.setAttribute(Const.CALL_ID_TOKEN, callId);
//            }
//            SOAPElement reqIdElement = shElement.addChildElement("reqId");
//            reqIdElement.addTextNode(String.valueOf(callId));
//            shElement.addChildElement(reqIdElement);
        } catch (SOAPException|JAXBException e) {
            throw new Fault(e);
        }
    }
}
