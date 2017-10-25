/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.ws.provider;

import com.viettel.fw.common.util.Const;
import com.viettel.ws.common.utils.WsRequestCreator;
import com.viettel.ws.common.utils.XmlSchema;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;

import javax.xml.namespace.QName;
import javax.xml.soap.*;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author thiendn1
 */
public class WebServiceSchema {


    protected static final String SOAP_PREDIX = "soapenv";
    protected static final String WSSE_PREDIX = "wsse";
    private static final Logger logger = Logger.getLogger("HttpClient");

    private static final String PASSWORD_DIGEST = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordDigest";
    private static final String PASSWORD_TEXT = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText";
    private static final String WSSE_NS = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd";
    private static final String WSU_NS = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd";

    private static SOAPMessage defaultSoapMessage;
    private static SOAPMessage textSecuritySoapMessage;
    private static SOAPMessage digestSecuritySoapMessage;

    static {
        try {
            defaultSoapMessage = getSoapMessageFormatRequest();
            textSecuritySoapMessage = getTextSecurityMessageFormatRequest();
            digestSecuritySoapMessage = getDigestSecurityMessageFormatRequest();

        } catch (SOAPException | TransformerException e) {
            logger.error(e);
        }


    }

    private static SOAPMessage getSoapMessageFormatRequest() throws SOAPException, TransformerException {
        //create SOAP
        SOAPMessage soapMessage = MessageFactory.newInstance().createMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();
        SOAPEnvelope soapEnvelope = soapPart.getEnvelope();
        soapEnvelope.setPrefix(SOAP_PREDIX);
        soapEnvelope.removeNamespaceDeclaration("SOAP-ENV");
        soapEnvelope.addNamespaceDeclaration("ser", "$xmlns:ser$");

        SOAPHeader soapHeader = soapEnvelope.getHeader();
        soapHeader.setPrefix(SOAP_PREDIX);
        soapHeader.setTextContent(Const.WEB_SERVICE_CONS.ARG_HEADER);
        SOAPBody soapBody = soapEnvelope.getBody();
        soapBody.setPrefix(SOAP_PREDIX);
        SOAPBodyElement bodyElement = soapBody.addBodyElement(new QName("$service_name$"));
        bodyElement.setTextContent(Const.WEB_SERVICE_CONS.ARG_BODY);
        return soapMessage;
    }

    /**
     * @return
     * @throws SOAPException
     * @throws TransformerException
     */
    private static SOAPMessage getDigestSecurityMessageFormatRequest() throws SOAPException, TransformerException {
        SOAPMessage soapMessage = getSoapMessageFormatRequest();
        SOAPHeader soapHeader = soapMessage.getSOAPPart().getEnvelope().getHeader();
        SOAPElement security
                = soapHeader.addChildElement("Security", WSSE_PREDIX, WSSE_NS);
        security.addNamespaceDeclaration("mustUnderstand", "0");
        security.addNamespaceDeclaration("wsu", WSU_NS);
        SOAPElement usernameToken
                = security.addChildElement("UsernameToken", WSSE_PREDIX);
        SOAPElement username
                = usernameToken.addChildElement("Username", WSSE_PREDIX);
        username.addTextNode("$usernameToken$");
        SOAPElement password
                = usernameToken.addChildElement("Password", WSSE_PREDIX);
        password.setAttribute("Type", PASSWORD_DIGEST);
        password.addTextNode("$passwordToken$");
        SOAPElement nonce
                = usernameToken.addChildElement("Nonce", WSSE_PREDIX);
        nonce.addTextNode("$nonce$");
        nonce.setAttribute("EncodingType", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#Base64Binary");
        SOAPElement timeStamp
                = usernameToken.addChildElement("Created", "wsu");
        timeStamp.addTextNode("$created$");
        return soapMessage;
    }

    private static SOAPMessage getTextSecurityMessageFormatRequest() throws SOAPException, TransformerException {
        SOAPMessage soapMessage = getSoapMessageFormatRequest();
        SOAPHeader soapHeader = soapMessage.getSOAPPart().getEnvelope().getHeader();
        SOAPElement security
                = soapHeader.addChildElement("Security", WSSE_PREDIX, WSSE_NS);
        security.addNamespaceDeclaration("mustUnderstand", "0");
        security.addNamespaceDeclaration("wsu", WSU_NS);
        SOAPElement usernameToken
                = security.addChildElement("UsernameToken", WSSE_PREDIX);
        SOAPElement username
                = usernameToken.addChildElement("Username", WSSE_PREDIX);
        username.addTextNode("$usernameToken$");
        SOAPElement password
                = usernameToken.addChildElement("Password", WSSE_PREDIX);
        password.setAttribute("Type", PASSWORD_TEXT);
        password.addTextNode("$passwordToken$");
        return soapMessage;
    }

    private String getRequestFormat(SOAPMessage soapMessage) throws SOAPException, TransformerException {
        //create SOAP
        return
                XmlSchema.node2String(soapMessage.getSOAPPart()).replace("xmlns:ser=\"$xmlns:ser$\"", "$xmlns:ser$");
    }



    private String createCnonce(Long time)
            throws UnsupportedEncodingException, NoSuchAlgorithmException {
        java.security.SecureRandom random = java.security.SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(time);
        byte[] nonceBytes = new byte[16];
        random.nextBytes(nonceBytes);
        return new String(org.apache.commons.codec.binary.Base64.encodeBase64(nonceBytes), "UTF-8");
    }


    private static String buildPasswordDigest(String password, String nonce, String dateTime) {
        try {

            ByteBuffer buf = ByteBuffer.allocate(1000);
            buf.put(org.apache.commons.codec.binary.Base64.decodeBase64(nonce));
            buf.put(dateTime.getBytes("UTF-8"));
            buf.put(password.getBytes("UTF-8"));
            byte[] toHash = new byte[buf.position()];
            buf.rewind();
            buf.get(toHash);
            byte[] hash = DigestUtils.sha(toHash);
            return org.apache.commons.codec.binary.Base64.encodeBase64String(hash);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * using httpClient to call soap web service
     *
     * @return
     * @throws java.io.UnsupportedEncodingException
     * @throws IOException
     */
    public String parseParameters(WsRequestCreator wsConfig) throws TransformerException, SOAPException {
        String operator = wsConfig.getServiceName();
        String targetNamespace = wsConfig.getTargetNameSpace();
        String parameters = wsConfig.getBodyParameterTxt();
        String header = wsConfig.getHeaderParameterTxt();
        String request = null;
        ConcurrentHashMap<String, WsTemplate> templates = WebServiceConfigLoader.getInstance().getWsTemplateMap().get(wsConfig.getWsAddress().replaceAll("(?ui)\\?wsdl", ""));
        WsTemplate wsTemplate = null;
        if (templates != null) {
            wsTemplate = templates.get(wsConfig.getServiceName());
        }
        if (templates != null && wsTemplate != null) {
            request = wsTemplate.getSoapMessage();
        } else {
            if (wsConfig.getUsername() != null && wsConfig.getPassword() != null) {
                if (wsConfig.getPasswordType() != null && wsConfig.getPasswordType().equals(WsEndpoint.PW_DIGEST)) {
                    request = getRequestFormat(digestSecuritySoapMessage);
                    Date date = new Date();
                    try {
                        String nonce = createCnonce(date.getTime());
                        request = request.replace("$nonce$", nonce);
                        SimpleDateFormat format = new SimpleDateFormat(
                                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
                        format.setTimeZone(TimeZone.getTimeZone("UTC"));
                        request = request.replace("$created$", format.format(date));
                        request = request.replace("$passwordToken$", buildPasswordDigest(wsConfig.getPassword(), nonce, format.format(date)));
                    } catch (UnsupportedEncodingException e) {
                        logger.debug(e);
                    } catch (NoSuchAlgorithmException e) {
                        logger.debug(e);
                    }
                } else {
                    request = getRequestFormat(textSecuritySoapMessage);
                    request = request.replace("$passwordToken$", wsConfig.getPassword());
                }
                request = request.replace("$usernameToken$", wsConfig.getUsername());
            } else {
                request = getRequestFormat(defaultSoapMessage);
            }
            if (targetNamespace.contains("\"")) {
                if (targetNamespace.contains("xmlns:")) {
                    request = request.replace("$xmlns:ser$", targetNamespace);
                } else {
                    request = request.replace("$xmlns:ser$", "xmlns:" + targetNamespace);
                }
            } else {
                request = request.replace("$xmlns:ser$", "xmlns:ser=\"" + targetNamespace + "\"");
            }
            if (operator.contains(":")) {
                request = request.replace("$service_name$", operator);
            } else {
                operator = "ser:" + operator;
                request = request.replace("$service_name$", operator);
            }
        }
        if (header == null) {
            request = request.replace(Const.WEB_SERVICE_CONS.ARG_HEADER, "");
        } else {
            request = request.replace(Const.WEB_SERVICE_CONS.ARG_HEADER, header);
        }
        if (parameters == null) {
            request = request.replace(Const.WEB_SERVICE_CONS.ARG_BODY, "");
        } else {
            request = request.replace(Const.WEB_SERVICE_CONS.ARG_BODY, parameters);
        }
        return request;
    }
}
