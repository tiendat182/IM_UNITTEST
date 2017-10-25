/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.ws.common.utils;

import com.viettel.ws.provider.WebServiceConfigLoader;
import com.viettel.ws.provider.WebServiceResponseLoader;
import com.viettel.ws.provider.WsDtoJaxbContainer;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPMessage;
import javax.xml.stream.XMLInputFactory;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author thiendn1
 */
public class WebServiceHandler {

    private static final WebServiceCaller webServiceCaller = new WebServiceCaller();
    private static final WebServiceResponseLoader webServiceResponseLoader = new WebServiceResponseLoader();

    public static List parseResponses(String response, String[] tags, Class[] classTypes) throws Exception {
        List<Object> listObjects = new ArrayList<>();
        Document doc = XmlSchema.parseXmlFile(response);
        int indexClass = 0;
        for (String tag : tags) {
            NodeList nodeList = doc.getElementsByTagName(tag);
            XmlStream xmlStream = new XmlStream();
            xmlStream.getxStream().alias(tag, classTypes[indexClass]);
            indexClass++;
            Object object = xmlStream.getxStream().fromXML(
                    XmlSchema.node2String(nodeList.item(0)));
            listObjects.add(object);
        }
        return listObjects;
    }

    public static String webServiceCaller(WsRequestCreator wsConfig) throws Exception {
        return webServiceCaller.webServiceCaller(wsConfig);
    }

    public static String webServiceCaller(String wsConfigKey, List<Object> wsHeaderParameters,
                                          List<Object> wsParameters) throws Exception {
        WsRequestCreator wsConfig = WebServiceConfigLoader.getInstance().getWsConfig(wsConfigKey);
        wsConfig.setHeaderParameters(wsHeaderParameters);
        wsConfig.setBodyParameters(wsParameters);
        return webServiceCaller.webServiceCaller(wsConfig);
    }

    public static String webServiceAliasCaller(String wsConfigKey, Map<String, Object> wsHeaderParameters,
                                               Map<String, Object> wsParameters) throws Exception {
        WsRequestCreator wsConfig = WebServiceConfigLoader.getInstance().getWsConfig(wsConfigKey);
        wsConfig.setBodyArgAlias(wsParameters);
        wsConfig.setHeaderArgAlias(wsHeaderParameters);
        return webServiceCaller.webServiceCaller(wsConfig);
    }

    public static String webServiceOperatorCaller(String wsOperatorKey, List<Object> wsHeaderParameters,
                                                  List<Object> wsParameters) throws Exception {
        WsRequestCreator wsConfig = WebServiceConfigLoader.getInstance().getWsConfigOperator(wsOperatorKey);
        wsConfig.setBodyParameters(wsParameters);
        wsConfig.setHeaderParameters(wsHeaderParameters);
        return webServiceCaller.webServiceCaller(wsConfig);
    }

    //
    public static String webServiceOperatorAliasCaller(String wsOperatorKey,
                                                       Map<String, Object> wsHeaderParameters, Map<String, Object> wsParameters) throws Exception {
        WsRequestCreator wsConfig = WebServiceConfigLoader.getInstance().getWsConfigOperator(wsOperatorKey);
        wsConfig.setBodyArgAlias(wsParameters);
        wsConfig.setHeaderArgAlias(wsHeaderParameters);
        return webServiceCaller.webServiceCaller(wsConfig);
    }


    public static Object wsServiceHandler(String unhandleContent,
                                          XmlStream xmlStream) throws Exception {
        InputStream is = new ByteArrayInputStream(unhandleContent.getBytes("UTF-8"));
        SOAPMessage doc = MessageFactory.newInstance().createMessage(null, is);
        doc.setProperty(XMLInputFactory.SUPPORT_DTD, false);
        SOAPBody tags = doc.getSOAPBody();
        NodeList nList = null;
        if (xmlStream.getXmlStreamType() == XmlStream.SINGLE_TYPE) {
            nList = doc.getSOAPBody().getElementsByTagName(tags.getChildNodes().item(0).getNodeName());
            if (nList.item(0).getChildNodes().item(0) != null) {
                nList = doc.getSOAPBody().getElementsByTagName(nList.item(0).getChildNodes().item(0).getNodeName());
            } else {
                return null;
            }

        } else {
            nList = doc.getSOAPBody().getElementsByTagName(tags.getChildNodes().item(0).getNodeName());
        }
        return xmlStream.getxStream().fromXML(XmlSchema.node2String(nList.item(0)));
    }

    public static Object wsServiceHandler(String unhandleContent, String configFile, String configId) throws Exception {
        return webServiceResponseLoader.wsServiceHandler(configFile, configId, unhandleContent);
    }

    public static Object parseResponse(String response, String tag, Class classTypeName) throws Exception {
        Document doc = XmlSchema.parseXmlFile(response);
        NodeList tags = doc.getElementsByTagName(tag);
        XmlStream xmlStream = new XmlStream();
        xmlStream.getxStream().alias(tag, classTypeName);
        return xmlStream.getxStream().fromXML(
                XmlSchema.node2String(tags.item(0)));
    }

    public static <T> Object defaultWebServiceHandler(WsRequestCreator wsConfig, Class<T> returnClazz, boolean isListResponse, GenericWebInfo genericWebInfo, Object... wsParameters) throws Exception {
        if (genericWebInfo != null) {
            List<Object> wsHeaderParameters = new ArrayList<>();
            wsHeaderParameters.add(genericWebInfo);
            wsConfig.setHeaderParameters(wsHeaderParameters);
        }
        if (wsParameters != null) {
            wsConfig.setBodyParameters(Arrays.asList(wsParameters));
        }
        String xml = webServiceCaller.webServiceCaller(wsConfig);
        InputStream is = new ByteArrayInputStream(xml.getBytes("UTF-8"));
        SOAPMessage doc = MessageFactory.newInstance().createMessage(null, is);
        doc.setProperty(XMLInputFactory.SUPPORT_DTD, false);
        SOAPBody tags = doc.getSOAPBody();
        NodeList nList = null;
        if (!isListResponse) {
            nList = doc.getSOAPBody().getElementsByTagName(tags.getChildNodes().item(0).getNodeName());
            if (nList.item(0).getChildNodes().item(0) != null) {
                nList = doc.getSOAPBody().getElementsByTagName(nList.item(0).getChildNodes().item(0).getNodeName());
                return parseObject(returnClazz, nList.item(0));
            } else {
                return null;
            }
        } else {
            nList = doc.getSOAPBody().getElementsByTagName(tags.getChildNodes().item(0).getNodeName());
            return parseListObject(returnClazz, nList.item(0));
        }
    }

    private static <T> T parseObject(Class<T> clazz, String xml) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(clazz);
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        InputStream inputStream = new ByteArrayInputStream(xml.getBytes());
        StreamSource sourceXml = new StreamSource();
        sourceXml.setInputStream(inputStream);
        JAXBElement<T> jaxbElement = unmarshaller.unmarshal(sourceXml, clazz);
        return jaxbElement.getValue();
    }


    private static <T> T parseObject(Class<T> clazz, Node nodeContent) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(clazz);
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        JAXBElement<T> jaxbElement = unmarshaller.unmarshal(nodeContent, clazz);
        return jaxbElement.getValue();
    }


    private static <T> List<T> parseListObject(Class<T> clazz, String xml) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(WsDtoJaxbContainer.class);
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        StreamSource sourceXml = new StreamSource(new StringReader(xml));
        JAXBElement<WsDtoJaxbContainer> jaxbElement = unmarshaller.unmarshal(sourceXml, WsDtoJaxbContainer.class);
        WsDtoJaxbContainer<T> wrapper = jaxbElement.getValue();
        List<T> list = wrapper.getList();
        if (list != null && !list.isEmpty()) {
            List<T> composeList = new ArrayList<>();
            for (Object content : list) {
                JAXBElement<T> responses = unmarshaller.unmarshal((Node) content, clazz);
                T user = responses.getValue();
                composeList.add(user);
            }
            wrapper.setList(composeList);
        }
        return wrapper.getList();
    }

    private static <T> List<T> parseListObject(Class<T> clazz, Node nodeContent) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(WsDtoJaxbContainer.class, clazz);
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        JAXBElement<WsDtoJaxbContainer> jaxbElement = unmarshaller.unmarshal(nodeContent, WsDtoJaxbContainer.class);
        WsDtoJaxbContainer<T> wrapper = jaxbElement.getValue();
        List<T> list = wrapper.getList();
        if (list != null && !list.isEmpty()) {
            List<T> composeList = new ArrayList<>();
            for (Object content : list) {
                JAXBElement<T> responses = unmarshaller.unmarshal((Node) content, clazz);
                T user = responses.getValue();
                composeList.add(user);
            }
            wrapper.setList(composeList);
        }
        return wrapper.getList();
    }

    public static List<Object> wsServiceHandlerList(String unhandleContent,
                                                    XmlStream xmlStream) throws Exception {
        InputStream is = new ByteArrayInputStream(unhandleContent.getBytes("UTF-8"));
        SOAPMessage doc = MessageFactory.newInstance().createMessage(null, is);
        doc.setProperty(XMLInputFactory.SUPPORT_DTD, false);
        SOAPBody tags = doc.getSOAPBody();
        NodeList nList = null;
        if (xmlStream.getXmlStreamType() == XmlStream.SINGLE_TYPE) {
            nList = doc.getSOAPBody().getElementsByTagName(tags.getChildNodes().item(0).getNodeName());
            if (nList.item(0).getChildNodes().item(0) == null) {
                return null;
            }
            nList = doc.getSOAPBody().getElementsByTagName(nList.item(0).getChildNodes().item(0).getNodeName());
        } else {
            nList = doc.getSOAPBody().getElementsByTagName(tags.getChildNodes().item(0).getNodeName());
        }
        List<Object> returnList = new ArrayList();
        for (int i = 0; i < nList.getLength(); i++) {
            returnList.add(xmlStream.getxStream().fromXML(XmlSchema.node2String(nList.item(i))));
        }
        return returnList;
    }

}
