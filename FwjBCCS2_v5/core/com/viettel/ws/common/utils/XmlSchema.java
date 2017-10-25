package com.viettel.ws.common.utils;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.viettel.ws.provider.WebServiceConfigLoader;
import com.viettel.ws.provider.WsTemplate;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.springframework.util.ClassUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

public class XmlSchema {
    public static final String tagStart = "\\<\\w+((\\s+\\w+(\\s*\\=\\s*(?:\".*?\"|'.*?'|[^'\"\\>\\s]+))?)+\\s*|\\s*)\\>";
    public static final String tagEnd = "\\</\\w+\\>";
    public static final String tagSelfClosing = "\\<\\w+((\\s+\\w+(\\s*\\=\\s*(?:\".*?\"|'.*?'|[^'\"\\>\\s]+))?)+\\s*|\\s*)/\\>";
    public static final String htmlEntity = "&[a-zA-Z][a-zA-Z0-9]+;";
    public static final Pattern htmlPattern = Pattern.compile("(\\<\\w+((\\s+\\w+(\\s*\\=\\s*(?:\".*?\"|'.*?'|[^'\"\\>\\s]+))?)+\\s*|\\s*)\\>.*\\</\\w+\\>)|(\\<\\w+((\\s+\\w+(\\s*\\=\\s*(?:\".*?\"|'.*?'|[^'\"\\>\\s]+))?)+\\s*|\\s*)/\\>)|(&[a-zA-Z][a-zA-Z0-9]+;)", 32);
    private static final String BOOLEAN_TYPE = "Boolean";
    private static final String BOOL_TYPE = "bool";
    private static final String DATE_TYPE = "Date";
    private static final String LONG_TYPE = "Long";
    private static final String DOUBLE_TYPE = "Double";
    private static final String STRING_TYPE = "String";
    private static final String ARRAYLIST_TYPE = "ArrayList";
    private static final String LIST_TYPE = "List";
    private static final String WS_TYPE = "WsConfig";
    //Quangkm FIX-ATTT:16-5-2016
    public static String FEATURE_DISALLOW_DOCTYPE_DECl = "http://apache.org/xml/features/disallow-doctype-decl";
    public static String FEATURE_EXTERNAL_GENERAL_ENTITIES = "http://xml.org/sax/features/external-general-entities";
    public static String FEATURE_EXTERNAL_PARAMETER_ENTITIES = "http://xml.org/sax/features/external-parameter-entities";
    //End Quangkm

    public static boolean isHtml(String s) {
        boolean ret = false;
        if (s != null) {
            ret = htmlPattern.matcher(s).find();
        }
        return ret;
    }

    public static boolean isXMLLike(String inXMLStr) {
        return isHtml(inXMLStr);
    }

    private static boolean isPrimitiveOrWrapper(Class type) {
        return ClassUtils.isPrimitiveOrWrapper(type);
    }

    public static String formatHeaderParameters(List<Object> list) {
        StringBuilder parameterBuilder = new StringBuilder();
        if ((list == null) || (list.isEmpty())) {
            return "";
        }
        XmlStream xmlStream = new XmlStream();
        XStream xstream = xmlStream.getxStream();
        for (int i = 0; i < list.size(); i++) {
            Object object = list.get(i);
            if (object != null) {
                if (isPrimitiveOrWrapper(object.getClass())) {
                    xstream.alias("arg" + (list.size() - 1), String.class);
                    parameterBuilder.append(xstream.toXML(object.toString()));
                } else if (object.getClass().equals(String.class)) {
                    if (isXMLLike(object.toString())) {
                        String par = "<arg" + (list.size() - 1) + ">" + object.toString() + "</arg" + (list.size() - 1) + ">";
                        parameterBuilder.append(par);
                    } else {
                        xstream.alias("arg" + (list.size() - 1), String.class);
                        parameterBuilder.append(xstream.toXML(object.toString()));
                    }
                } else if ((object instanceof Collection)) {
                    for (Object subObject : (List) object) {
                        if (isPrimitiveOrWrapper(subObject.getClass())) {
                            xstream.alias("arg" + (list.size() - 1), String.class);
                            parameterBuilder.append(xstream.toXML(subObject.toString()));
                        } else {
                            xstream.alias("arg" + (list.size() - 1), subObject.getClass());
                            parameterBuilder.append(xstream.toXML(subObject));
                        }
                    }
                } else {
                    xstream.alias("arg" + (list.size() - 1), object.getClass());
                    parameterBuilder.append(xstream.toXML(object));
                }
            }
        }
        return parameterBuilder.toString();
    }

    public static String formatParameters(List<Object> list) {
        StringBuilder parameterBuilder = new StringBuilder();
        if ((list == null) || (list.isEmpty())) {
            return "";
        }
        XmlStream xmlStream = new XmlStream();
        XStream xstream = xmlStream.getxStream();
        xstream.autodetectAnnotations(true);
        for (int i = 0; i < list.size(); i++) {
            Object object = list.get(i);
            if (object != null) {
                if (isPrimitiveOrWrapper(object.getClass())) {
                    xstream.alias("arg" + i, String.class);
                    parameterBuilder.append(xstream.toXML(object.toString()));
                } else if (object.getClass().equals(String.class)) {
                    if (isXMLLike(object.toString())) {
                        String par = "<arg" + i + ">" + object.toString() + "</arg" + i + ">";
                        parameterBuilder.append(par);
                    } else {
                        xstream.alias("arg" + i, String.class);
                        parameterBuilder.append(xstream.toXML(object.toString()));
                    }
                } else if ((object instanceof Collection)) {
                    for (Object subObject : (List) object) {
                        if (isPrimitiveOrWrapper(subObject.getClass())) {
                            xstream.alias("arg" + i, String.class);
                            parameterBuilder.append(xstream.toXML(subObject.toString()));
                        } else {
                            xstream.alias("arg" + i, subObject.getClass());
                            parameterBuilder.append(xstream.toXML(subObject));
                        }
                    }
                } else {
                    xstream.alias("arg" + i, object.getClass());
                    parameterBuilder.append(xstream.toXML(object));
                }
            }
        }
        return parameterBuilder.toString();
    }

    public static String formatHeaderParameters(WsRequestCreator wsRequestCreator)
            throws Exception {
        if (wsRequestCreator.getHeaderParameterTxt() != null) {
            return wsRequestCreator.getHeaderParameterTxt();
        }
        StringBuilder parameterBuilder = new StringBuilder();
        Map<String, Object> aliasObjects = wsRequestCreator.getHeaderArgAlias();
        XmlStream xmlStream = new XmlStream();
        XStream xstream = xmlStream.getxStream();
        if (aliasObjects != null) {
            Iterator it = aliasObjects.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pairs = (Map.Entry) it.next();
                String alias = (String) pairs.getKey();
                Object object = pairs.getValue();
                if (object != null) {
                    parameterBuilder = parseParameters(xstream, parameterBuilder, object, alias);
                }
            }
            return parameterBuilder.toString();
        }
        List<String> argAlias = null;
        List<Object> list = wsRequestCreator.getHeaderParameters();
        ConcurrentHashMap<String, WsTemplate> templates = (ConcurrentHashMap) WebServiceConfigLoader.getInstance().getWsTemplateMap().get(wsRequestCreator.getWsAddress().replaceAll("(?ui)\\?wsdl", ""));
        if (templates != null) {
            argAlias = ((WsTemplate) templates.get(wsRequestCreator.getServiceName())).getHeaderList();
        } else {
            return formatHeaderParameters(list);
        }
        if ((list == null) || (list.isEmpty())) {
            return "";
        }
        if (argAlias.size() != list.size()) {
            throw new Exception("The numbers of argAlias and parameters are not matched!");
        }
        for (int i = 0; i < list.size(); i++) {
            Object object = list.get(i);
            if (object != null) {
                parameterBuilder = parseParameters(xstream, parameterBuilder, object, (String) argAlias.get(i));
            }
        }
        return parameterBuilder.toString();
    }

    public static String formatParameters(WsRequestCreator wsRequestCreator)
            throws Exception {
        if (wsRequestCreator.getBodyParameterTxt() != null) {
            return wsRequestCreator.getBodyParameterTxt();
        }
        StringBuilder parameterBuilder = new StringBuilder();
        Map<String, Object> aliasObjects = wsRequestCreator.getBodyArgAlias();
        XmlStream xmlStream = new XmlStream();
        XStream xstream = xmlStream.getxStream();
        if (aliasObjects != null) {
            Iterator it = aliasObjects.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pairs = (Map.Entry) it.next();
                String alias = (String) pairs.getKey();
                Object object = pairs.getValue();
                if (object != null) {
                    parameterBuilder = parseParameters(xstream, parameterBuilder, object, alias);
                }
            }
            return parameterBuilder.toString();
        }
        List<String> argAlias = null;
        List<Object> list = wsRequestCreator.getBodyParameters();
        ConcurrentHashMap<String, WsTemplate> templates = null;
        if (WebServiceConfigLoader.getInstance().getWsTemplateMap() != null) {
            templates = (ConcurrentHashMap) WebServiceConfigLoader.getInstance().getWsTemplateMap().get(wsRequestCreator.getWsAddress().replaceAll("(?ui)\\?wsdl", ""));
        }
        if (templates != null) {
            argAlias = ((WsTemplate) templates.get(wsRequestCreator.getServiceName())).getAliasList();
        } else {
            return formatParameters(list);
        }
        if ((list == null) || (list.isEmpty())) {
            return "";
        }
        if (argAlias.size() != list.size()) {
            throw new Exception("The numbers of argAlias and parameters are not matched!");
        }
        for (int i = 0; i < list.size(); i++) {
            Object object = list.get(i);
            if (object != null) {
                parameterBuilder = parseParameters(xstream, parameterBuilder, object, (String) argAlias.get(i));
            }
        }
        return parameterBuilder.toString();
    }

    private static StringBuilder parseParameters(XStream xstream, StringBuilder parameterBuilder, Object object, String alias) {
        if (isPrimitiveOrWrapper(object.getClass())) {
            xstream.alias(alias, String.class);
            parameterBuilder.append(xstream.toXML(object.toString()));
        } else if (object.getClass().equals(String.class)) {
            if (isXMLLike(object.toString())) {
                String par = "<" + alias + ">" + object.toString() + "<" + alias + ">";
                parameterBuilder.append(par);
            } else {
                xstream.alias(alias, String.class);
                parameterBuilder.append(xstream.toXML(object.toString()));
            }
        } else if ((object instanceof Collection)) {
            for (Object subObject : (List) object) {
                if (isPrimitiveOrWrapper(subObject.getClass())) {
                    xstream.alias(alias, String.class);
                    parameterBuilder.append(xstream.toXML(subObject.toString()));
                } else {
                    xstream.alias(alias, subObject.getClass());
                    parameterBuilder.append(xstream.toXML(subObject));
                }
            }
        } else {
            xstream.alias(alias, object.getClass());
            parameterBuilder.append(xstream.toXML(object));
        }
        return parameterBuilder;
    }


    public static String objectToXml(Object object, String alias) {
        if (object != null) {
            StringBuilder parameterBuilder = new StringBuilder();
            XmlStream xmlStream = new XmlStream();
            XStream xstream = xmlStream.getxStream();
            if (isPrimitiveOrWrapper(object.getClass())) {
                xstream.alias(alias, String.class);
                parameterBuilder.append(xstream.toXML(object.toString()));
            } else if (object.getClass().equals(String.class)) {
                if (isXMLLike(object.toString())) {
                    parameterBuilder.append(object.toString());
                } else {
                    xstream.alias(alias, String.class);
                    parameterBuilder.append(xstream.toXML(object.toString()));
                }
            } else {
                xstream.alias(alias, object.getClass());
                parameterBuilder.append(xstream.toXML(object));
            }
            return parameterBuilder.toString();
        }
        return "<null/>";
    }


    public static String objectToXml(Object object) {
        return objectToXml(object, "return");
    }

    public static Object localXmlToObject(String xml, String type, String prefix)
            throws ClassNotFoundException {
        if ((xml != null) && (type != null)) {
            XmlStream xmlStream = new XmlStream();
            XStream xstream = xmlStream.getxStream();
            if (type.equals("List") || (type.equals("ArrayList"))) {
                prefix = "list";
            } else if (prefix == null) {
                prefix = "value";
            }

            if (!xml.contains("<" + prefix + ">")) {
                xml = "<" + prefix + ">" + xml + "</" + prefix + ">";
            }
            switch (type) {
                case "Boolean":
                case "bool":
                    xstream.alias(prefix, Boolean.class);
                    break;
                case "Date":
                    xstream.alias(prefix, Date.class);
                    break;
                case "Long":
                    xstream.alias(prefix, Long.class);
                    break;
                case "Double":
                    xstream.alias(prefix, Double.class);
                    break;
                case "String":
                    xstream.alias(prefix, String.class);
                    break;
                case "ArrayList":
                case "List":
                    xstream.alias("list", List.class);
                    break;
                default:
                    xstream.alias(prefix, Class.forName(type));
            }
            return xstream.fromXML(xml);
        }
        return null;
    }

    public static String convertToFullType(String type) {
        switch (type) {
            case "Boolean":
                type = Boolean.class.getName();
                break;
            case "bool":
                type = Boolean.TYPE.getName();
                break;
            case "Date":
                type = Date.class.getName();
                break;
            case "Long":
                type = Long.class.getName();
                break;
            case "Double":
                type = Double.class.getName();
                break;
            case "String":
                type = String.class.getName();
                break;
            case "ArrayList":
                type = ArrayList.class.getName();
                break;
            case "List":
                type = List.class.getName();
        }
        return type;
    }

    public static Object xmlToObjectFullType(String xml, String type)
            throws ClassNotFoundException {
        if ((xml != null) && (type != null)) {
            XmlStream xmlStream = new XmlStream();
            XStream xstream = xmlStream.getxStream();
            xstream.alias("return", Class.forName(type));
            return xstream.fromXML(xml);
        }
        return null;
    }


    public static String formatXML(String unformattedXml) {
        try {
            Document document = parseXmlFile(unformattedXml);
            OutputFormat format = new OutputFormat(document);
            format.setIndenting(true);
            format.setIndent(3);
            format.setOmitXMLDeclaration(true);
            Writer out = new StringWriter();
            XMLSerializer serializer = new XMLSerializer(out, format);
            serializer.serialize(document);
            return out.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String node2String(Node node)
            throws TransformerFactoryConfigurationError, TransformerException {
        StreamResult xmlOutput = new StreamResult(new StringWriter());
        Transformer transformer = TransformerFactory.newInstance().newTransformer();

        transformer.setOutputProperty("omit-xml-declaration", "yes");
        transformer.transform(new DOMSource(node), xmlOutput);
        return xmlOutput.getWriter().toString();
    }

    public static Document parseXmlFile(String data) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            //Quangkm FIX-ATTT:16-5-2016
            dbf.setFeature(FEATURE_DISALLOW_DOCTYPE_DECl, true);
            dbf.setFeature(FEATURE_EXTERNAL_GENERAL_ENTITIES, false);
            dbf.setFeature(FEATURE_EXTERNAL_PARAMETER_ENTITIES, false);
            dbf.setXIncludeAware(false);
            dbf.setExpandEntityReferences(false);
            //End Quangkm
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(data));
            return db.parse(is);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //thiendn1: cac ham phuc vu cho unit test
    //begin unit test
    public static String xStreamParseObjectToXml(Object ob) {
        return new XStream(new DomDriver()).toXML(ob);
    }

    public static Object xStreamParseXmlToObject(String xml) {
        XStream xStream = new XStream(new DomDriver());
        xStream.ignoreUnknownElements();
        return xStream.fromXML(xml);
    }

    public static Object xStreamParseFileToObject(String filePath) {
        XStream xStream = new XStream(new DomDriver());
        xStream.ignoreUnknownElements();
        return xStream.fromXML(XmlSchema.class.getClassLoader().getResourceAsStream(filePath));
    }

    public static String jaxbParseObjectToXml(Object ob) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(ob.getClass());
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        // output pretty printed
        java.io.StringWriter sw = new StringWriter();
        jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
        jaxbMarshaller.marshal(ob, sw);
        return sw.toString();
    }

    public static Object jaxbParseXmlToObject(Class clazz, String xml) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(clazz);
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        InputStream inputStream = new ByteArrayInputStream(xml.getBytes());
        StreamSource sourceXml = new StreamSource();
        sourceXml.setInputStream(inputStream);
        return unmarshaller.unmarshal(sourceXml, clazz);
    }

    // end unit test



}
