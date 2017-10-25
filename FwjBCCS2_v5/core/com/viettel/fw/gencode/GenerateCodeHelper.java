/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.fw.gencode;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mysema.query.codegen.GenericExporter;
import com.mysema.query.codegen.Keywords;
import com.thoughtworks.paranamer.AdaptiveParanamer;
import com.thoughtworks.paranamer.Paranamer;
import com.viettel.fw.Exception.CbsGenerateCodeException;
import com.viettel.fw.common.util.DataUtil;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.*;

/**
 * @author lamnv5
 */
public class GenerateCodeHelper {


    public enum SrcPathType {
        BUSINESS_BASE,
        BUSINESS_SRV_IMPL,
        WSBUSINESS_WS_IMPL,
        WEB
    }

    private Class modelObj;
    private String basePath;
    private String business;
    private String businessSrcPath;
    private String wsBusinessSrcPath;
    private String webSrcPath;
    private String projName;

    private boolean overrideFile = false;
    private String forcePath = null;
    private boolean createQModel = true;
    private boolean createDTO = true;
    private boolean createMapper = true;
    private boolean createRepository = true;
    private boolean createServiceImpl = true;
    private boolean createIsDelete = false;
    private boolean createWsService = false;
    private boolean writeToFile = false;


    private final GenericExporter exporter;
    private List<SourceWriter> sourceWriters = new ArrayList<>();

    public void addSourceWriter(SourceWriter sourceWriter) {
        sourceWriters.add(sourceWriter);
    }

    public void generateQModel(Class obj) throws CbsGenerateCodeException {
        File file = new File(businessSrcPath + "\\base\\java");
        if (!file.exists()) {
            file = new File(businessSrcPath + "\\base\\src");
            if (!file.exists()) {
                file = new File(businessSrcPath + "\\base");
                if (!file.exists()) {
                    file = new File(businessSrcPath);
                }
            }
        }

        exporter.setTargetFolder(file);
        exporter.export(obj);
    }


    public GenerateCodeHelper(String basePath) {
        this.basePath = basePath;

        exporter = new GenericExporter();
        exporter.setKeywords(Keywords.JPA);
        exporter.setEntityAnnotation(Entity.class);
        exporter.setEmbeddableAnnotation(Embeddable.class);
        exporter.setEmbeddedAnnotation(Embedded.class);
        exporter.setSupertypeAnnotation(MappedSuperclass.class);
        exporter.setSkipAnnotation(Transient.class);

    }

    public void createWsServiceFix(Class obj) throws Exception {

        String objName = obj.getSimpleName();

        String fileName = getFileName("wsesb.service", "Ws" + objName + "ServiceImpl", SrcPathType.WSBUSINESS_WS_IMPL);
        StringBuilder query = new StringBuilder();
        query.append("package " + basePath + ".wsesb.service;\n");

        query.append("import java.util.ArrayList;\n");
        query.append(MessageFormat.format("import {0}.dto.{1}DTO;\n", basePath, objName));
        query.append("import com.viettel.fw.common.data.*;\n");
        query.append("import com.viettel.fw.common.util.extjs.FilterRequest;\n");
//        query.append(MessageFormat.format("import {0}.service.{1}ServiceImpl;\n", basePath, objName));
        query.append("import com.viettel.fw.dto.BaseMessage;\n");
        query.append("import com.viettel.fw.common.data.CommonSort;\n");
        query.append("import com.viettel.fw.dto.BaseDTO;\n");
        query.append("import com.viettel.ws.common.utils.*;\n\n");
        query.append(MessageFormat.format("import {0}.service.{1}Service;\n", basePath, objName));

        query.append("import java.util.List;\n");
        query.append("import java.lang.Long;\n");
        query.append("import org.springframework.stereotype.Service;\n");
        query.append("import org.apache.log4j.Logger;\n");
        query.append("import com.viettel.ws.provider.CxfWsClientFactory;\n");
        query.append("import org.springframework.beans.factory.annotation.Autowired;\n");
        query.append("import javax.annotation.PostConstruct;\n");
        query.append("import org.springframework.beans.factory.annotation.Qualifier;\n");

        query.append(MessageFormat.format("@Service(\"Ws{0}ServiceImpl\")\n", objName));
//        query.append(MessageFormat.format("public class Ws{0}ServiceImpl extends {0}ServiceImpl implements {0}Service '{'\n\n", objName));
        query.append(MessageFormat.format("public class Ws{0}ServiceImpl implements {0}Service '{'\n\n", objName));
        query.append(MessageFormat.format("    public static final Logger logger = Logger.getLogger(Ws{0}ServiceImpl.class);\n\n", objName));
        query.append("    @Autowired\n");
        query.append("    @Qualifier(\"cxfWsClientFactory\")\n");
        query.append("    CxfWsClientFactory wsClientFactory;\n\n");
        query.append(MessageFormat.format("    private {0}Service client;\n\n", objName));
        query.append("    @PostConstruct\n");
        query.append("    public void init() throws Exception {\n");
//        query.append(MessageFormat.format("        client = wsClientFactory.createWsClient({0}Service.class, {0}ServiceImpl.class);\n", objName));
        query.append("      try{\n");
        query.append(MessageFormat.format("         client = wsClientFactory.createWsClient({0}Service.class);\n", objName));
        query.append("      } catch (Exception ex) {\n");
        query.append("          logger.error(\"init\", ex);\n");
        query.append("      }\n");
        query.append("    }\n\n");
        query.append("");
        query.append("");

        query.append("    @Override\n");
        query.append(MessageFormat.format("    public {0}DTO findOne(Long id) throws Exception  '{'\n", objName));
        query.append("        return client.findOne(id);\n");
        query.append("    }\n\n");

        query.append("    @Override\n");
//        query.append("    public List findAll() throws Exception {\n");
        query.append(MessageFormat.format("    public List<{0}DTO> findAll() throws Exception '{'\n", objName));
        query.append("        return client.findAll();\n");
        query.append("    }\n\n");

        query.append("    @Override\n");
//        query.append("    public List findByFilter(List filterList) throws Exception  {\n");
        query.append(MessageFormat.format("    public List<{0}DTO> findByFilter(List<FilterRequest> filterList) throws Exception  '{'\n", objName));
        query.append("        return client.findByFilter(filterList);\n");
        query.append("    }\n\n");


        query.append("    @Override\n");
        query.append("    public Long count(List<FilterRequest> filterList) throws Exception {\n");
        query.append("        return client.count(filterList);\n");
        query.append("    }\n\n");


        query.append("    @Override\n");
        query.append(MessageFormat.format("    public BaseMessage create({0}DTO dto) throws Exception  '{'\n", objName));
        query.append("        return client.create(dto);\n");
        query.append("    }\n\n");


        query.append("    @Override\n");
        query.append(MessageFormat.format("    public BaseMessage update({0}DTO dto) throws Exception  '{'\n", objName));
        query.append("        return client.update(dto);\n");
        query.append("    }\n\n");

        query.append("}");

        writeSource(fileName, query.toString());
    }

    /**
     * Sinh code WS Service
     *
     * @param obj
     * @param business
     * @throws Exception
     */
    public void generateWsEndPoint(Class obj, String business) throws Exception {
        String objName = obj.getSimpleName();
        String filePath = webSrcPath + "\\web\\WEB-INF\\ws-endpoint.xml";

        String implementor = null;
        File tmpFile = new File(getFileName("service", objName + "Impl", SrcPathType.BUSINESS_SRV_IMPL));
        if (tmpFile.exists()) {
            implementor = obj.getPackage().getName() + "." + obj.getSimpleName() + "Impl";
        } else {
            implementor = obj.getPackage().getName() + "." + obj.getSimpleName() + "Impl";
        }


        EndPointInfo wsEndPoint = new EndPointInfo(obj.getSimpleName(), implementor, "/" + projName + "/" + obj.getSimpleName());
        writeSource(filePath, wsEndPoint);
    }

    /**
     * Sinh code WS Service
     *
     * @param obj
     * @param business
     * @throws Exception
     */

    private static final List<String> EXCLUSE_METHODS = Lists.newArrayList("findAllLazy", "findAllLazyLookup", "findByFilterWithPage");

    public void generateWsServiceFromClass(boolean isGenInBusinessProj, Class obj, String business) throws Exception {
        String objName = obj.getSimpleName();

        String fileName = null;
        if (isGenInBusinessProj) {
            fileName = getFileName("service", "Ws" + objName + "Impl", SrcPathType.BUSINESS_SRV_IMPL);
        } else {
            fileName = getFileName("wsesb.service", "Ws" + objName + "Impl", SrcPathType.WSBUSINESS_WS_IMPL);
        }

        StringBuilder query = new StringBuilder();
        StringBuilder importQuery = new StringBuilder();

        HashMap<String, String> futherImport = new HashMap<>();

        if (isGenInBusinessProj) {
            importQuery.append("package " + basePath + ".service;\n");
        } else {
            importQuery.append("package " + basePath + ".wsesb.service;\n");
        }

        futherImport.put("org.apache.log4j.Logger", "");
        futherImport.put("com.viettel.fw.common.data.*", "");
        futherImport.put("com.viettel.fw.common.util.extjs.FilterRequest", "");
        futherImport.put("com.viettel.fw.service.WsBaseServiceImpl", "");
        futherImport.put(obj.getName(), "");

        futherImport.put("java.util.List", "");
        futherImport.put("java.lang.Long", "");
        futherImport.put("org.springframework.stereotype.Service", "");
        futherImport.put("org.apache.log4j.Logger", "");
        futherImport.put("com.viettel.ws.provider.CxfWsClientFactory", "");
        futherImport.put("org.springframework.beans.factory.annotation.Autowired", "");
        futherImport.put("javax.annotation.PostConstruct", "");
        futherImport.put("org.springframework.beans.factory.annotation.Qualifier", "");


        query.append(MessageFormat.format("@Service(\"Ws{0}Impl\")\n", objName));
        if (isGenInBusinessProj) {
            query.append(MessageFormat.format("public class Ws{0}Impl implements {0} '{'\n\n", objName));
        } else {
            String dtoName = objName.replaceFirst("Service", "DTO");
            futherImport.put("com.viettel.bccs.sale.dto." + dtoName, "");
            query.append(MessageFormat.format("public class Ws{0}Impl extends  WsBaseServiceImpl<{1}>  implements {2} '{'\n\n", objName, dtoName, objName));
        }

        query.append(MessageFormat.format("    public static final Logger logger = Logger.getLogger(Ws{0}Impl.class);\n\n", objName));

        query.append("    @Autowired\n");
        query.append("    @Qualifier(\"CxfWsClientFactory\")\n");
        query.append("    CxfWsClientFactory wsClientFactory;\n\n");
        query.append(MessageFormat.format("    private {0} client;\n\n", objName));
        query.append("    @PostConstruct\n");
        query.append("    public void init() {\n");
        query.append("        try {\n");
        query.append(MessageFormat.format("            client = wsClientFactory.createWsClient({0}.class);\n", objName));
        query.append("        } catch (Exception ex) {\n");
        query.append("            logger.error(ex);\n");
        query.append("        }\n");
        query.append("    }\n");


        Method[] methods = obj.getDeclaredMethods();

        Paranamer paranamer = new AdaptiveParanamer();
        for (Method method : methods) {
            try {
                if (!Modifier.isPublic(method.getModifiers())) {
                    continue;
                }
                if (EXCLUSE_METHODS.contains(method.getName())) {
                    continue;
                }

                //source compile debug moi co ten cac tham so trong phuong thuc
                String[] parameterNames = null;
                try {
                    parameterNames = paranamer.lookupParameterNames(method);
                } catch (Exception ex) {
                    //ex.printStackTrace();
                }

                futherImport.put(method.getReturnType().getName(), "x");


                query.append("    @Override\n");
                query.append("    public ").append(method.getReturnType().getSimpleName()).append(" ").append(method.getName())
                        .append("(");
                Class[] parameterTypes = method.getParameterTypes();


                StringBuilder callParams = new StringBuilder("");

                if (parameterTypes.length == 0) {
                    query.append(")");
                } else {
                    StringBuilder bodyParams = new StringBuilder("");
                    for (int i = 0; i < parameterTypes.length; i++) {
                        futherImport.put(parameterTypes[i].getName(), "x");

                        String argName = "arg" + i;
                        try {
                            if (parameterNames != null)
                                argName = parameterNames[i];
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                        bodyParams.append(parameterTypes[i].getSimpleName()).append(" ").append(argName).append(",");
                        callParams.append(argName).append(",");
                    }

                    if (bodyParams.length() > 0) bodyParams.deleteCharAt(bodyParams.length() - 1);
                    query.append(bodyParams).append(")");

                }


                if (callParams.length() > 0) callParams.deleteCharAt(callParams.length() - 1);

                if (method.getExceptionTypes().length == 0) {
                    query.append(" {\n");
                } else {
                    String throwEx = "";
                    for (Class clas : method.getExceptionTypes()) {
                        if (clas.getSimpleName().equals("LogicException")) continue;

                        futherImport.put(clas.getName(), "");
                        throwEx += clas.getSimpleName() + ",";
                    }

                    futherImport.put("com.viettel.fw.Exception.LogicException", "");
                    throwEx += " LogicException";
                    //throwEx = throwEx.substring(0, throwEx.length() - 1);
                    query.append(" throws " + throwEx + " {\n");
                }

                if (method.getReturnType().equals(Void.TYPE)) {
                    query.append("        client." + method.getName() + "(").append(callParams).append(");\n");
                } else {
                    query.append("        return client." + method.getName() + "(").append(callParams).append(");\n");
                }
                query.append("    }\n\n");
            } catch (Exception ex) {
                ex.printStackTrace();
                writeSource("Error", ex.toString());
            }
        }

        query.append("\n}");

        importQuery.append(getImport(futherImport));
        StringBuilder finalQuery = importQuery.append("\n\n").append(query);

        writeSource(fileName, finalQuery.toString());
    }

    public void generateWsEsb(String type, Class obj, String business) throws Exception {
        List<String> ips = Lists.newArrayList("http://10.58.71.129:8090/bpm/sale/",
                "http://10.58.71.130:8090/bpm/sale/",
                "http://10.58.71.44:8090/bpm/sale/");

        String proxy = MessageFormat.format("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<proxy xmlns=\"http://ws.apache.org/ns/synapse\"\n" +
                "       name=\"{0}\"\n" +
                "       transports=\"https,http\"\n" +
                "       statistics=\"enable\"\n" +
                "       trace=\"disable\"\n" +
                "       startOnLoad=\"true\">\n" +
                "   <target endpoint=\"{0}\">\n" +
                "      <outSequence>\n" +
                "         <send/>\n" +
                "      </outSequence>\n" +
                "   </target>\n" +
                "   <publishWSDL uri=\"{1}{0}?wsdl\"/>\n" +
                "   <parameter name=\"disableOperationValidation\">true</parameter>\n" +
                "   <description/>\n" +
                "</proxy>", obj.getSimpleName(), ips.get(0));


        String endpoint = MessageFormat.format("<endpoint xmlns=\"http://ws.apache.org/ns/synapse\" name=\"{0}\">\n" +
                "   <loadbalance algorithm=\"org.apache.synapse.endpoints.algorithms.RoundRobin\">\n", obj.getSimpleName());

        int i = 1;
        for (String ip : ips) {
            endpoint +=
                    MessageFormat.format(
                            "      <endpoint name=\"{0}_{2}\">\n" +
                                    "         <wsdl uri=\"{1}{0}?wsdl\" service=\"{0}ImplService\" port=\"{0}ImplPort\">\n" +
                                    "            <suspendOnFailure>\n" +
                                    "               <progressionFactor>1.0</progressionFactor>\n" +
                                    "            </suspendOnFailure>\n" +
                                    "            <markForSuspension>\n" +
                                    "               <retriesBeforeSuspension>0</retriesBeforeSuspension>\n" +
                                    "               <retryDelay>0</retryDelay>\n" +
                                    "            </markForSuspension>\n" +
                                    "         </wsdl>\n" +
                                    "      </endpoint>\n", obj.getSimpleName(), ip, i++);
        }
        endpoint +=
                "   </loadbalance>\n" +
                        "</endpoint>";

        if (type.equals("proxy")) {
            writeSource(obj.getSimpleName(), proxy);
        } else {
            writeSource(obj.getSimpleName(), endpoint);
        }
    }

    public void scanWsErrMethod(Class obj, String business) throws Exception {
        String objName = obj.getSimpleName();

        String fileName = null;

        fileName = getFileName("service", objName, SrcPathType.BUSINESS_SRV_IMPL);

        StringBuilder query = new StringBuilder();
        query.append("class ").append(objName).append("{\n");

        HashMap<String, String> futherImport = new HashMap<>();
        List<String> candidateMissLst = new ArrayList<>();

        Method[] methods = obj.getDeclaredMethods();
        boolean hasDuplicate = false;
        boolean hasNotExposeWs = false;

        for (Method method : methods) {
            try {
                boolean isWebMethod = false;
                for (Annotation annotation : method.getAnnotations()) {
                    if (annotation.annotationType().getSimpleName().equals("WebMethod")) {
                        isWebMethod = true;
                        break;
                    }
                }

                if (Modifier.isPublic(method.getModifiers()) && !isWebMethod) {
                    candidateMissLst.add(method.getName());
                }

                /*
                if (!isWebMethod) {
                    continue;
                }*/

                if (futherImport.containsKey(method.getName()) && Modifier.isPublic(method.getModifiers())) {
                    hasDuplicate = true;
                    query.append("duplicate: " + method.getName() + " " + futherImport.get(method.getName()) + "\n");
                    query.append("duplicate: " + method.getName() + " " + (isWebMethod ? "(WebMethod)" : "") + "\n");
                } else {
                    futherImport.put(method.getName(), isWebMethod ? "(WebMethod)" : "");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                writeSource("Error", ex.toString());
            }
        }

        List<String> missLst = Lists.newArrayList();
        HashMap<String, String> excluse = Maps.newHashMap();
        excluse.put("findAll", "");
        excluse.put("findAllLazy", "");
        excluse.put("findByFilterWithPage", "");
        excluse.put("findAllLazyLookup", "");


        for (String miss : candidateMissLst) {
            if (!futherImport.containsKey(miss) && !excluse.containsKey(miss)) {
                hasNotExposeWs = true;
                missLst.add(miss);
                query.append("miss: " + miss).append("\n");
            }
        }

        query.append("\n}");


        //Reset neu ko bi duplicate hoac khong bi miss ws
        if (!hasDuplicate && !hasNotExposeWs) {
            query = new StringBuilder();
            fileName = "";
        }

        writeSource(fileName, query.toString());
    }

    /**
     * Sinh code WS Service
     *
     * @param obj
     * @param business
     * @throws Exception
     */
    public void generateWsServiceFromClassOldWay(boolean isGenInBusinessProj, Class obj, String business) throws Exception {
        String objName = obj.getSimpleName();

        String fileName = null;
        if (isGenInBusinessProj) {
            fileName = getFileName("service", "Ws" + objName + "Impl", SrcPathType.BUSINESS_SRV_IMPL);
        } else {
            fileName = getFileName("wsesb.service", "Ws" + objName + "Impl", SrcPathType.WSBUSINESS_WS_IMPL);
        }

        StringBuilder query = new StringBuilder();
        StringBuilder importQuery = new StringBuilder();

        HashMap<String, String> futherImport = new HashMap<>();

        if (isGenInBusinessProj) {
            importQuery.append("package " + basePath + ".service;\n");
        } else {
            importQuery.append("package " + basePath + ".wsesb.service;\n");
        }

        futherImport.put("org.apache.log4j.Logger", "");
        futherImport.put("com.viettel.fw.common.data.*", "");
        futherImport.put("com.viettel.ws.common.utils.*", "");
        futherImport.put("com.viettel.fw.common.util.extjs.FilterRequest", "");
        futherImport.put(obj.getName(), "");

        if (!isGenInBusinessProj) {
            futherImport.put(obj.getPackage().getName() + "." + objName + "Impl", "");
        }

        futherImport.put("com.viettel.ws.provider.WsCallerFactory", "");
        futherImport.put("org.springframework.beans.factory.annotation.Autowired", "");
        futherImport.put("org.springframework.stereotype.Service", "");
        futherImport.put("com.viettel.ws.provider.WsDtoContainer", "");
        futherImport.put("java.util.List", "");
        futherImport.put("java.util.ArrayList", "");

        query.append(MessageFormat.format("@Service(\"Ws{0}Impl\")\n", objName));
        if (isGenInBusinessProj) {
            query.append(MessageFormat.format("public class Ws{0}Impl implements {0} '{'\n\n", objName));
        } else {
            query.append(MessageFormat.format("public class Ws{0}Impl extends {0}Impl implements {0} '{'\n\n", objName));
        }

        query.append(MessageFormat.format("    public static final Logger logger = Logger.getLogger(Ws{0}Impl.class);\n\n", objName));
        query.append("    @Autowired\n");
        query.append("    private WsCallerFactory wsCallerFactory;\n");

        Method[] methods = obj.getDeclaredMethods();

        Paranamer paranamer = new AdaptiveParanamer();
        for (Method method : methods) {
            try {
                boolean isWebMethod = false;
                for (Annotation annotation : method.getAnnotations()) {
                    if (annotation.annotationType().getSimpleName().equals("WebMethod")) {
                        isWebMethod = true;
                        break;
                    }
                }
                if (!isWebMethod) {
                    continue;
                }

                //source compile debug moi co ten cac tham so trong phuong thuc
                String[] parameterNames = null;
                try {
                    parameterNames = paranamer.lookupParameterNames(method);
                } catch (Exception ex) {
                    //ex.printStackTrace();
                }

                futherImport.put(method.getReturnType().getName(), "x");

                StringBuilder bodyParams = new StringBuilder("\n        List<Object> bodyParameters = new ArrayList<>();\n");

                query.append("    @Override\n");
                query.append("    public ").append(method.getReturnType().getSimpleName()).append(" ").append(method.getName())
                        .append("(");
                Class[] parameterTypes = method.getParameterTypes();


                if (parameterTypes.length == 0) {
                    query.append(")");
                } else {
                    for (int i = 0; i < parameterTypes.length; i++) {
                        futherImport.put(parameterTypes[i].getName(), "x");

                        String argName = "arg" + i;
                        try {
                            if (parameterNames != null)
                                argName = parameterNames[i];
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                        query.append(parameterTypes[i].getSimpleName()).append(" ").append(argName).append(",");
                        bodyParams.append("        bodyParameters.add(" + argName + ");\n");
                    }
                    query.deleteCharAt(query.length() - 1).append(")");
                }
                query.append(" throws Exception  {\n");
                query.append(MessageFormat.format("        WsRequestCreator requestCreator = wsCallerFactory.createWsCaller({0}.class, \"{1}\");", objName, method.getName()));
                //TODO: Lieu co can phan truong hop ko co parameter va co parameter?
                query.append(bodyParams).append("\n\n");
                query.append("        requestCreator.setBodyParameters(bodyParameters);\n");
                query.append("        String response = WebServiceHandler.webServiceCaller(requestCreator);\n");

                //TODO: response
                query.append(generateWsResponse(futherImport, obj, method)).append("\n");

                boolean isMultiple = method.getReturnType().equals(List.class);

                if (!method.getReturnType().getName().equals("void")) {
                    if (isMultiple) {
                        query.append("        WsDtoContainer wsDtoContainer = (WsDtoContainer) WebServiceHandler.wsServiceHandler(response, xmlStream);\n");
                        query.append("        return (List) wsDtoContainer.getList();\n");
                    } else {
                        query.append("        ").append(method.getReturnType().getSimpleName()).append(" responseObject = ")
                                .append("(").append(method.getReturnType().getSimpleName()).append(")")
                                .append(" WebServiceHandler.wsServiceHandler(response, xmlStream);\n");

                        query.append("        return responseObject;\n");
                    }
                }
                query.append("    }\n\n");
            } catch (Exception ex) {
                ex.printStackTrace();
                writeSource("Error", ex.toString());
            }
        }

        query.append("\n}");

        importQuery.append(getImport(futherImport));
        StringBuilder finalQuery = importQuery.append("\n\n").append(query);

        writeSource(fileName, finalQuery.toString());
    }

    /**
     * Sinh code tong hop ngoai tru WS  Service
     *
     * @param obj
     * @param business
     * @throws Exception
     */
    public void generateCode(Class obj, String business) throws Exception {
        if (writeToFile) {
            FileSourceWriter fileSourceWriter = new FileSourceWriter();
            fileSourceWriter.setOverrideFile(overrideFile);
            addSourceWriter(fileSourceWriter);
        }

        if (DataUtil.isNullOrEmpty(basePath)) {
            throw new Exception("Chưa truyền vào basePath (ví dụ: com.viettel.sale");
        }

        if (Strings.isNullOrEmpty(business)) {
            createAllFile(obj, basePath);
        } else {
            createAllFile(obj, basePath + "." + business);
        }
    }

    /**
     * Sinh ws respone
     *
     * @param futherImport
     * @param className
     * @param method
     * @return
     * @throws NoSuchMethodException
     */
    private String generateWsResponse(HashMap<String, String> futherImport, Class className, Method method) throws NoSuchMethodException {
        boolean isMultiple = method.getReturnType().equals(List.class);
        StringBuilder wsConfigBuilder = new StringBuilder();
        if (method.getReturnType().getName().equals("void")) {
            return "";
        }
        wsConfigBuilder.append("        XmlStream xmlStream = new XmlStream();\n");
        wsConfigBuilder.append("        List<XStreamStorage> parseObject = new ArrayList<>();\n");
        if (isMultiple) {
            wsConfigBuilder.append("        Mapper.alias(parseObject, WsDtoContainer.class, \"return\");\n");
            wsConfigBuilder.append("        Mapper.addImplicitCollection(parseObject, WsDtoContainer.class, \"list\");\n");
            ParameterizedType type = (ParameterizedType) method.getGenericReturnType();

            Class elementClass = (Class) (type.getActualTypeArguments()[0]);
            futherImport.put(elementClass.getName(), "x");

            wsConfigBuilder.append("        Mapper.alias(parseObject, ").append(elementClass.getSimpleName()).append(".class, \"list\");\n");
        } else {
            wsConfigBuilder.append("        Mapper.alias(parseObject, ").append(method.getReturnType().getName()).append(".class, \"return\");\n");

            Field[] fields = method.getReturnType().getDeclaredFields();
            // check all fields in order to detect collection type
            for (Field field : fields) {
                if (field.getType().equals(List.class)) {
                    ParameterizedType type = (ParameterizedType) field.getGenericType();
                    Class elementClass = ((Class) type.getActualTypeArguments()[0]);
                    futherImport.put(elementClass.getName(), "x");

                    wsConfigBuilder.append("        Mapper.alias(parseObject, ").append(elementClass.getSimpleName()).append(".class, \"").append(field.getName()).append("\");\n");
                    wsConfigBuilder.append("        Mapper.addImplicitCollection(parseObject, ").append(method.getReturnType().getSimpleName()).append(".class, \"").append(field.getName()).append("\");\n");
                }
            }
        }

        wsConfigBuilder.append("        xmlStream.config(parseObject);\n");

        /* x_comment: luon de single vi List duoc map voi WsDtoContainer.list roi
        if (isMultiple) {
            wsConfigBuilder.append("        xmlStream.isSingleType = false;\n");
        } else {
            wsConfigBuilder.append("        xmlStream.isSingleType = true;\n");
        }
        */

        wsConfigBuilder.append("        xmlStream.isSingleType = true;\n");
        return wsConfigBuilder.toString();
    }

    /**
     * Lay ra chuoi import
     *
     * @param furtherImport
     * @return
     */
    private static String getImport(HashMap<String, String> furtherImport) {
        String strImp = "";
        for (String e : furtherImport.keySet()) {
            if (e.equals("int") || e.endsWith("String") || e.endsWith("String;") || e.equals("void") || e.equals("short") || e.equals("long") || e.equals("boolean"))
                continue;
            strImp += "import " + e + ";\n";
        }

        return strImp;
    }

    /**
     * Sinh repo
     *
     * @param obj
     * @throws Exception
     */
    public void generateRepo(Class obj) throws Exception {
        if (DataUtil.isNullOrEmpty(basePath)) {
            throw new Exception("Chưa truyền vào basePath (ví dụ: com.viettel.sale");
        }
        if (DataUtil.isNullOrEmpty(business)) {
            throw new Exception("Chưa truyền vào bussines (ví dụ: rule)");
        }

        createRepository(obj);
    }


    private void createAllFile(Class obj, String path) throws Exception {
        writeEnumToFile(obj);

        if (createQModel)
            generateQModel(obj);
        if (createDTO) {
            createDTO(obj);
        }
        if (createMapper) {
            createMapper(obj);
        }
        if (createRepository) {
            createRepository(obj);
        }
        if (createRepository) {
            createRepositoryCustom(path, obj);
            createRepositoryImpl(obj);
        }
        if (createServiceImpl) {
            createServiceInterface(path, obj);
            createServiceImpl(path, obj);
        }
        if (createWsService) createWsServiceFix(obj);

    }

    private void writeSource(String filePath, Object fileContent) throws Exception {
        for (SourceWriter sourceWriter : sourceWriters) {
            sourceWriter.writeSource(filePath, fileContent);
        }
    }

    private String getSrcFolder(SrcPathType srcPathType) {
        String srcFolder = "";
        switch (srcPathType) {
            case BUSINESS_BASE:
                srcFolder = businessSrcPath + "\\base";
                break;
            case BUSINESS_SRV_IMPL:
                srcFolder = businessSrcPath + "\\srv-impl";
                break;
            case WSBUSINESS_WS_IMPL:
                srcFolder = businessSrcPath + "\\ws-impl";
                break;
            case WEB:
                srcFolder = webSrcPath;
                break;
            default:
                srcFolder = businessSrcPath + "\\base";
        }

        return srcFolder;
    }

    private String getFileName(String packageName, String className, SrcPathType srcPathType) {

        String srcPath = getSrcFolder(srcPathType);

        File fileCheck = new File(srcPath + File.separator + "src");
        if (fileCheck.exists() && fileCheck.isDirectory()) {
            srcPath = fileCheck.getAbsolutePath();
        } else {
            fileCheck = new File(srcPath + File.separator + "java");
            if (fileCheck.exists() && fileCheck.isDirectory()) {
                srcPath = fileCheck.getAbsolutePath();
            }
        }

        packageName = basePath + "." + packageName;
        packageName = packageName.replaceAll("\\.", "\\\\");
        return MessageFormat.format("{0}\\{1}\\{2}.java", srcPath, packageName, className);
    }

    private String getSrcFolder(String pkg) throws CbsGenerateCodeException {

        String sysPath = System.getProperty("user.dir");
        File fileCheck = new File(System.getProperty("user.dir") + File.separator + "src");
        if (fileCheck.exists() && fileCheck.isDirectory()) {
            sysPath = fileCheck.getAbsolutePath();
        } else {
            fileCheck = new File(System.getProperty("user.dir") + File.separator + "java");
            if (fileCheck.exists() && fileCheck.isDirectory()) {
                sysPath = fileCheck.getAbsolutePath();
            }
        }

        String srcPath = (StringUtils.isNotBlank(forcePath) ? forcePath : sysPath);
        File file = new File(srcPath);
        if (!file.exists()) {
            srcPath = (StringUtils.isNotBlank(forcePath) ? forcePath : sysPath);
            file = new File(srcPath);
            if (!file.exists()) {
                throw new CbsGenerateCodeException("SRC_FOLDER_NOTFOUND",
                        MessageFormat.format("Khong ton tai thu muc source code: {0}", srcPath));
            }
        }

        pkg = pkg.replaceAll("\\.", "\\\\");
        return MessageFormat.format("{0}\\{1}", srcPath, pkg);
    }

    private static boolean validGenGetSet(String property) {
        //return !property.contains("com.viettel") && !property.contains("Class");
        return !property.contains("Class");
    }

    public void refactorPredicate(String business, Class obj) throws Exception {
        String objName = obj.getSimpleName();
        String repoFileName = getFileName("repo", objName + "RepositoryImpl", SrcPathType.BUSINESS_BASE);
        String serviceFileName = getFileName("service", objName + "ServiceImpl", SrcPathType.BUSINESS_BASE);

        ArrayList functionContent = new ArrayList<>();
        ArrayList remainContent = new ArrayList<>();

        getFunctionAndRemainContent(serviceFileName, "public Predicate toPredicate", functionContent, remainContent);

        StringBuilder query = appendFunctionToFile(repoFileName, functionContent);
        writeSource(repoFileName, query.toString());

        StringBuilder query2 = new StringBuilder();
        for (Object line : remainContent) {
            query2.append(line.toString()).append(System.lineSeparator());
        }

        writeSource(serviceFileName, query2.toString());
    }

    private void getFunctionAndRemainContent(String fileName, String pattern, ArrayList<String> functionContent, ArrayList<String> remainContent) throws IOException {
        File file = new File(fileName);
        if (!file.exists()) {
            return;
        }

        String sourceFileName = file.getPath();

        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(sourceFileName));

            String line;
            Stack stack = new Stack();

            boolean needAdd = false;
            while ((line = br.readLine()) != null) {
                if (line.contains(pattern)) {
                    needAdd = true;
                    stack.push("O");
                    functionContent.add(line);
                    continue;
                }

                if (needAdd) {
                    if (line.contains("{")) {
                        stack.push("O");
                    }
                    if (line.contains("}")) {
                        stack.pop();
                    }
                }

                if (needAdd) {
                    functionContent.add(line);
                } else {
                    //chuan hoa lai
                    /*
                     if (line.contains("Predicate predicate = toPredicate(filters);")) {
                     line = "\t\tPredicate predicate = repository.toPredicate(filters);";
                     }*/

                    if (line.contains("toPredicate(")) {
                        line = line.replace("toPredicate(", "repository.toPredicate(");
                    }

                    if (line.contains("getRepository()")) {
                        line = line.replace("getRepository()", "repository");
                    }

                    remainContent.add(line);
                }

                if (stack.empty() && needAdd) {
                    needAdd = false;
                }
            }

            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                br.close();
            }
        }
    }

    private void createRepository(Class obj) throws Exception {
        String objName = obj.getSimpleName();
        String className = getFileName("repo", objName + "Repo", SrcPathType.BUSINESS_SRV_IMPL);
        StringBuilder query = new StringBuilder();
        query.append(MessageFormat.format("package {0}.repo;\n", basePath));

        query.append("import com.viettel.fw.persistence.BaseRepository;\n");
        query.append("import java.util.List;\n");

        PropertyDescriptor[] propertyDescriptors = getPropertyDescriptors(obj);
        List<String> importLst = new ArrayList<>();
        for (PropertyDescriptor des : propertyDescriptors) {
            if (des.getPropertyType().getName().contains("long")
                    || des.getPropertyType().getName().contains("Class")
                    || des.getPropertyType().getName().contains("String")
                    || des.getName().contains("id")) {
                continue;
            }

            if (!importLst.contains(des.getPropertyType().getName())) {
                importLst.add(des.getPropertyType().getName());
            }
        }

        for (String item : importLst) {
            query.append(MessageFormat.format("import {0};\n", item));
        }

        query.append(MessageFormat.format("import {0}.model.{1};\n", basePath, objName));
        query.append("\n");
        query.append(MessageFormat.format("public interface {0}Repo extends BaseRepository<{0}, Long>, {0}RepoCustom '{'\n", objName));

        /*x_comment: ko gen cac ham findBy
        for (PropertyDescriptor des : propertyDescriptors) {
            if (validGenGetSet(des.getPropertyType().getName())) {
                query.append(MessageFormat.format("\n\n    public List<{0}> findBy{1}({2} {3});", objName, DataUtil.upperFirstChar(des.getName()), des.getPropertyType().getSimpleName(), des.getName()));
            }
        }*/

        query.append("\n}");
        writeSource(className, query.toString());
    }

    private void createRepositoryCustom(String path, Class obj) throws Exception {
        String objName = obj.getSimpleName();
        String className = getFileName("repo", objName + "RepoCustom", SrcPathType.BUSINESS_SRV_IMPL);

        StringBuilder query = new StringBuilder();
        query.append(MessageFormat.format("package {0}.repo;\n", basePath));
        query.append("import com.viettel.fw.common.util.extjs.FilterRequest;\n");
        query.append("import com.mysema.query.types.Predicate;\n");
        query.append("import java.util.List;\n");
        query.append(MessageFormat.format("import {0}.model.{1};\n", basePath, objName));
        query.append(MessageFormat.format("public interface {0}RepoCustom '{'\n", objName));
        query.append(" public Predicate toPredicate(List<FilterRequest> filters); \n");
        query.append("\n}");
        writeSource(className, query.toString());
    }

    public void createRepositoryImpl(Class obj) throws Exception {
        String objName = obj.getSimpleName();
        String className = getFileName("repo", objName + "RepoImpl", SrcPathType.BUSINESS_SRV_IMPL);
        List<Object[]> properties = getPropertiesX(obj);

        StringBuilder query = new StringBuilder();
        query.append(MessageFormat.format("package {0}.repo;\n", basePath));
        query.append("import com.viettel.fw.common.util.extjs.FilterRequest;\n");
        query.append("import com.mysema.query.types.Predicate;\n");
        query.append("import com.mysema.query.types.expr.BooleanExpression;\n");
        query.append("import com.mysema.query.types.template.BooleanTemplate;\n");
        query.append(MessageFormat.format("import {0}.model.{1}.COLUMNS;\n", basePath, objName));
        query.append(MessageFormat.format("import {0}.model.Q{1};\n", basePath, objName));
        query.append("import java.util.List;\n");
        query.append("import com.viettel.fw.common.util.DbUtil;\n");
        query.append("import org.apache.log4j.Logger;\n");

        query.append(MessageFormat.format("public class {0}RepoImpl implements {0}RepoCustom '{'\n\n", objName));
        query.append(MessageFormat.format("    public static final Logger logger = Logger.getLogger({0}RepoCustom.class);\n\n", objName));
        query.append("    @Override\n");
        query.append("    public Predicate toPredicate(List<FilterRequest> filters) {\n");
        query.append("        logger.info(\"Entering predicates :: \" + filters);\n");
        query.append(MessageFormat.format("        Q{0} {1} = Q{0}.{1};", objName, DataUtil.lowerFirstChar(objName))).append("\n");
        query.append("        BooleanExpression result = BooleanTemplate.create(\"1 = 1\");\n");
        query.append("        try {\n");
        query.append("            for (FilterRequest filter : filters) {\n");
        query.append("                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());\n");
        query.append("                BooleanExpression expression = null;\n");
        query.append("                switch (column) {\n");
        for (Object[] property : properties) {
            if (!String.valueOf(property[0]).toUpperCase().contains("CLASS")) {
                if (((Class) property[1]).getSimpleName().equals(List.class.getSimpleName())) {
                    continue;
                }

                String expressMethod = "DbUtil.createStringExpression";
                String newEx = null;
                if (property[1].equals(Long.TYPE) ||
                        ((Class) property[1]).getSimpleName().equals(Long.class.getSimpleName())) {
                    expressMethod = "DbUtil.createLongExpression";
                }
                if (((Class) property[1]).getSimpleName().equals(BigDecimal.class.getSimpleName())) {
                    expressMethod = "DbUtil.createBigDecimalExpression";
                }
                if (((Class) property[1]).getSimpleName().equals(Date.class.getSimpleName())) {
                    expressMethod = "DbUtil.createDateExpression";
                }

                if (property[1].equals(Character.TYPE) ||
                        ((Class) property[1]).getSimpleName().equals(Character.class.getSimpleName())) {
                    expressMethod = "DbUtil.createCharacterExpression";
                }

                //DbUtil.createShortExpression
                if (property[1].equals(Short.TYPE) ||
                        ((Class) property[1]).getSimpleName().equals(Short.class.getSimpleName())) {
                    expressMethod = "DbUtil.createShortExpression";
                }

                if (((Class) property[1]).getCanonicalName().contains("com.viettel")) {
                    expressMethod = "DbUtil.createLongExpression";
                    newEx = property[0] + "." + property[0];
                }

                query.append("                    case " + String.valueOf(property[0]).toUpperCase() + ":\n");
                query.append("                        expression = " + expressMethod + "(" + DataUtil.lowerFirstChar(objName) + "." + DataUtil.defaultIfNull(newEx, property[0]) + ", filter); \n");
                query.append("                        break;\n");
            }
        }

        query.append("                }\n");
        query.append("                if (expression != null) {\n");
        query.append("                        result = result.and(expression);\n");
        query.append("                }\n");
        query.append("            }\n");
        query.append("        } catch (Exception ex) {\n");
        query.append("            logger.error(ex);\n");
        query.append("        }\n");
        if (createIsDelete) {
            query.append("        result = result.and(" + DataUtil.lowerFirstChar(objName) + ".isDelete.isNull().or(" + DataUtil.lowerFirstChar(objName) + ".isDelete.eq(Const.STATUS.EFFECTIVE)));\n");
        }
        query.append("        logger.info(\"Result Predicate :: \" + (result != null ? result.toString() : \"\"));\n");
        query.append("        logger.info(\"Exiting predicates\");\n");
        query.append("        return result;\n");
        query.append("    }\n");
        query.append("\n}");
        writeSource(className, query.toString());
    }

    private void createDTO(Class obj) throws Exception {
        String objName = obj.getSimpleName();
        String className = getFileName("dto", objName + "DTO", SrcPathType.BUSINESS_BASE);

        StringBuilder query = new StringBuilder();
        HashMap<String, String> futherImport = new HashMap<>();

        futherImport.put("com.viettel.fw.dto.BaseDTO", "");
        futherImport.put("java.io.Serializable", "");

        query.append(MessageFormat.format("public class {0}DTO extends BaseDTO implements Serializable '{'\n", objName));
        query.append("public String getKeySet() {\n return keySet; }\n");

        PropertyDescriptor[] propertyDescriptors = getPropertyDescriptors(obj);
        for (PropertyDescriptor des : propertyDescriptors) {
            if (validGenGetSet(des.getPropertyType().getName())) {
                futherImport.put(des.getPropertyType().getName(), "");
                query.append(MessageFormat.format("    private {0} {1};\n", des.getPropertyType().getSimpleName(), des.getName()));
            }
        }
        for (PropertyDescriptor des : propertyDescriptors) {
            if (validGenGetSet(des.getPropertyType().getName())) {
                query.append(MessageFormat.format("    public {0} get{1}() '{'\n", des.getPropertyType().getSimpleName(), DataUtil.upperFirstChar(des.getName())));
                query.append(MessageFormat.format("        return this.{0};\n", des.getName()));
                query.append("    }\n");
                query.append(MessageFormat.format("    public void set{0}({1} {2}) '{'\n", DataUtil.upperFirstChar(des.getName()), des.getPropertyType().getSimpleName(), des.getName()));
                query.append(MessageFormat.format("        this.{0} = {0};\n", des.getName()));
                query.append("    }\n");
            }
        }

        query.append("}\n");

        StringBuilder finalQuery = new StringBuilder();
        finalQuery.append(MessageFormat.format("package {0}.dto;\n", basePath));
        finalQuery.append(getImport(futherImport));
        finalQuery.append(query);

        writeSource(className, finalQuery.toString());
    }

    private void createMapper(Class obj) throws Exception {
        String objName = obj.getSimpleName();
        String className = getFileName("mapper", objName + "Mapper", SrcPathType.BUSINESS_BASE);

        StringBuilder query = new StringBuilder();
        query.append(MessageFormat.format("package {0}.mapper;\n", basePath));
        query.append("import com.viettel.fw.common.util.mapper.BaseMapper;\n");
        query.append(MessageFormat.format("import {0}.dto.{1}DTO;\n", basePath, objName));
        query.append(MessageFormat.format("import {0}.model.{1};\n", basePath, objName));
        query.append(MessageFormat.format("public class {0}Mapper extends BaseMapper<{0}, {0}DTO> '{'\n", objName));
        query.append("    @Override\n");
        query.append(MessageFormat.format("    public {0}DTO toDtoBean({0} model) '{'\n", objName));
        query.append(MessageFormat.format("        {0}DTO obj = null;\n", objName));
        query.append("        if (model != null) {\n");
        query.append(MessageFormat.format("           obj =   new {0}DTO(); \n", objName));
        PropertyDescriptor[] propertyDescriptors = getPropertyDescriptors(obj);
        for (PropertyDescriptor des : propertyDescriptors) {
            if (validGenGetSet(des.getPropertyType().getName())) {
                query.append(MessageFormat.format("            obj.set{0}(model.get{0}());\n", DataUtil.upperFirstChar(des.getName())));
            }
        }
        query.append("        }\n");
        query.append("        return obj;\n");
        query.append("    }\n");
        query.append("    @Override\n");
        query.append(MessageFormat.format("    public {0} toPersistenceBean({0}DTO dtoBean) '{'\n", objName));
        query.append(MessageFormat.format("        {0} obj = null;\n", objName));
        query.append("        if (dtoBean != null) {\n");
        query.append(MessageFormat.format("           obj =   new {0}(); \n", objName));
        for (PropertyDescriptor des : propertyDescriptors) {
            if (validGenGetSet(des.getPropertyType().getName())) {
                query.append(MessageFormat.format("            obj.set{0}(dtoBean.get{0}());\n", DataUtil.upperFirstChar(des.getName())));
            }
        }
        query.append("        }\n");
        query.append("        return obj;\n");
        query.append("    }\n");
        query.append("}\n");
        writeSource(className, query.toString());
    }

    private void createServiceInterface(String path, Class obj) throws Exception {
        String objName = obj.getSimpleName();
        String className = getFileName("service", objName + "Service", SrcPathType.BUSINESS_BASE);

        StringBuilder query = new StringBuilder();
        query.append(MessageFormat.format("package {0};\n", path + ".service"));
        query.append(MessageFormat.format("import {0}.dto.{1}DTO;\n", basePath, objName));
        query.append(MessageFormat.format("import {0}.model.{1};\n", basePath, objName));
        query.append("import java.util.List;\n");
        query.append("import org.primefaces.model.LazyDataModel;\n");
        query.append("import org.springframework.data.domain.Page;\n");
        query.append("import org.springframework.data.domain.Pageable;\n");
//        query.append("import com.viettel.web.ext.dto.LookUpDetailDTO;\n");
        query.append("import com.viettel.fw.common.util.extjs.FilterRequest;\n");
        query.append("import javax.jws.WebMethod;\n");
        query.append("import javax.jws.WebService;\n");
        query.append("import org.springframework.transaction.annotation.Transactional;\n");
        query.append("import com.viettel.fw.dto.BaseMessage;\n");
//        query.append("import com.viettel.fw.mapper.SortOrderMapper;\n\n");

        query.append("@WebService\n");
        query.append(MessageFormat.format("public interface {0}Service '{'\n\n", objName));
        query.append("    @WebMethod\n");
        query.append(MessageFormat.format("    public {0}DTO findOne(Long id) throws Exception;\n\n", objName));
        query.append("    @WebMethod\n");
        query.append("    public Long count(List<FilterRequest> filters) throws Exception;\n\n");
        query.append("    @WebMethod\n");
        query.append(MessageFormat.format("    public List<{0}DTO> findAll() throws Exception;\n\n", objName));
        query.append("    @WebMethod\n");
        query.append(MessageFormat.format("    public List<{0}DTO> findByFilter(List<FilterRequest> filters) throws Exception;\n\n", objName));
        query.append("    @WebMethod\n");
        query.append(MessageFormat.format("    public BaseMessage create({0}DTO productSpecCharacterDTO) throws Exception;\n", objName));
        query.append("    @WebMethod\n");
        query.append(MessageFormat.format("    public BaseMessage update({0}DTO productSpecCharacterDTO) throws Exception;\n", objName));

        query.append("\n}");
        writeSource(className, query.toString());
    }

    private void createServiceImpl(String path, Class obj) throws Exception {
        String objName = obj.getSimpleName();
        String className = getFileName("service", objName + "ServiceImpl", SrcPathType.BUSINESS_SRV_IMPL);

        StringBuilder query = new StringBuilder();

        query.append(MessageFormat.format("package {0};\n", path + ".service"));
        query.append("import com.google.common.collect.Lists;\n");
        query.append(MessageFormat.format("import {0}.repo.{1}Repo;\n", basePath, objName));
//        query.append(MessageFormat.format("import {0}.mapper.{1}Mapper;\n", basePath, objName));
        query.append(MessageFormat.format("import {0}.dto.{1}DTO;\n", basePath, objName));
        query.append(MessageFormat.format("import {0}.model.{1};\n", basePath, objName));
        query.append(MessageFormat.format("import {0}.service.{1}Service;\n", basePath, objName));
        query.append("import com.viettel.fw.dto.BaseMessage;\n");

        query.append("import com.viettel.fw.common.util.mapper.BaseMapper;\n");
        query.append("import com.viettel.fw.common.util.extjs.FilterRequest;\n");
//        query.append("import com.viettel.web.ext.dto.LookUpLazyDTO;\n");
//        query.append("import com.viettel.web.ext.dto.LookUpDetailDTO;");
        query.append("import java.util.List;\n");
        query.append("import org.apache.log4j.Logger;\n");
        query.append("import org.springframework.beans.factory.annotation.Autowired;\n");
        query.append("import org.springframework.stereotype.Service;\n");
        query.append("import com.viettel.fw.service.BaseServiceImpl;\n");
        query.append("import org.springframework.data.domain.Page;\n");
        query.append("import org.springframework.data.domain.PageRequest;\n");
        query.append("import org.springframework.data.domain.Pageable;\n");
        query.append("import org.primefaces.model.LazyDataModel;\n");
        query.append("import org.primefaces.model.SortOrder;\n");
        query.append("import org.springframework.data.domain.Sort;\n");
        query.append("import sun.reflect.generics.reflectiveObjects.NotImplementedException;\n");
        query.append("import javax.jws.WebMethod;\n");
        query.append("import org.springframework.transaction.annotation.Transactional;\n");
        query.append("import com.viettel.fw.common.data.CommonSort;\n");
//        query.append("import com.viettel.fw.mapper.SortOrderMapper;\n");
        query.append("import java.util.ArrayList;\n");
        query.append("import java.util.Map;\n\n");

        query.append("@Service\n");
        query.append(MessageFormat.format("public class {0}ServiceImpl extends BaseServiceImpl implements {0}Service '{'\n\n", objName));
//        query.append(MessageFormat.format("    private final {0}Mapper mapper = new {0}Mapper();\n\n", objName));
        query.append(MessageFormat.format("    private final BaseMapper<{0}, {0}DTO> mapper = new BaseMapper<>({0}.class, {0}DTO.class);\n\n", objName));

        query.append("    @Autowired\n");
        query.append(MessageFormat.format("    private {0}Repo repository;\n", objName));
        query.append(MessageFormat.format("    public static final Logger logger = Logger.getLogger({0}Service.class);\n\n", objName));

        query.append("    @WebMethod\n");
        query.append(MessageFormat.format("    public Long count(List<FilterRequest> filters) throws Exception '{'\n", objName));
        query.append("        return repository.count(repository.toPredicate(filters));\n");
        query.append("    }\n\n");

        query.append("    @WebMethod\n");
        query.append(MessageFormat.format("    public {0}DTO findOne(Long id) throws Exception '{'\n", objName));
        query.append("        return mapper.toDtoBean(repository.findOne(id));\n");
        query.append("    }\n\n");

        query.append("    @WebMethod\n");
        query.append(MessageFormat.format("    public List<{0}DTO> findAll() throws Exception '{'\n", objName));
        query.append("        return mapper.toDtoBean(repository.findAll());\n");
        query.append("    }\n\n");

        query.append("    @WebMethod\n");
        query.append(MessageFormat.format("    public List<{0}DTO> findByFilter(List<FilterRequest> filters) throws Exception '{'\n\n", objName));
        query.append("        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));\n");
        query.append("    }\n\n");

        query.append("    @WebMethod\n");
        query.append("    @Transactional(rollbackFor = Exception.class)\n");
        query.append(MessageFormat.format("    public BaseMessage create({0}DTO dto) throws Exception '{'\n", objName));
        query.append("        throw new NotImplementedException();\n");
        query.append("    }\n\n");
        query.append("    @WebMethod\n");
        query.append("    @Transactional(rollbackFor = Exception.class)\n");
        query.append(MessageFormat.format("    public BaseMessage update({0}DTO dto) throws Exception '{'\n", objName));
        query.append("        throw new NotImplementedException();\n");
        query.append("    }\n");
        query.append("}\n");

        writeSource(className, query.toString());
    }

    private static String createEnum(Class obj) throws Exception {
        String enumname = "public static enum COLUMNS '{'{0}'}';";
        String params = "";

        for (String name : getProperties(obj)) {
            name = name.toUpperCase();
            if (name.equals("CLASS")) {
                continue;
            }

            if (DataUtil.isNullOrEmpty(params)) {
                params = name;
            } else {
                params += ", " + name;
            }
        }

        if (!DataUtil.isNullOrEmpty(params)) {
            params += ", " + "EXCLUSE_ID_LIST";
        }
        return MessageFormat.format(enumname, params);
    }

    /**
     * Them 1 function vao file
     *
     * @param sourceFileName
     * @param functionContent
     * @throws IOException
     */
    private StringBuilder appendFunctionToFile(String sourceFileName, List<String> functionContent) throws IOException {
        BufferedReader br = null;
        PrintWriter pw = null;
        StringBuilder allContent = new StringBuilder();

        try {
            br = new BufferedReader(new FileReader(sourceFileName));

            String line;
            while ((line = br.readLine()) != null) {
                allContent.append(line).append(System.lineSeparator());
                if (line.contains("public class")) {
                    for (String x : functionContent) {
                        allContent.append(x).append(System.lineSeparator());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                br.close();
            }
            if (pw != null) {
                pw.close();
            }
        }

        return allContent;
    }

    private void writeEnumToFile(Class obj) throws IOException {
        File file = new File(getFileName("model", obj.getSimpleName(), SrcPathType.BUSINESS_BASE));
        if (!file.exists()) {
            return;
        }

        String sourceFileName = file.getPath();
        String destinationFileName = sourceFileName;

        BufferedReader br = null;
        PrintWriter pw = null;
        ArrayList<String> allContent = new ArrayList<>();
        boolean cancel = false;

        try {
            br = new BufferedReader(new FileReader(sourceFileName));

            String line;
            while ((line = br.readLine()) != null) {
                allContent.add(line);
                if (line.contains("public class")) {
                    allContent.add(createEnum(obj));
                }

                if (line.contains("public static enum COLUMNS")) {
                    cancel = true;
                    break;
                }
            }

            br.close();

            if (!cancel) {
                pw = new PrintWriter(new FileWriter(destinationFileName));
                for (String item : allContent) {
                    pw.println(item);
                }

                pw.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                br.close();
            }
            if (pw != null) {
                pw.close();
            }
        }
    }

    private static List<String> getProperties(Class obj) throws Exception {
        List<String> propertyNames = new ArrayList<>();
        for (PropertyDescriptor propertyDescriptor : getPropertyDescriptors(obj)) {
            propertyNames.add(propertyDescriptor.getName());
        }

        return propertyNames;
    }

    private static List<Object[]> getPropertiesX(Class obj) throws Exception {
        List<Object[]> propertyNames = new ArrayList<>();
        for (PropertyDescriptor propertyDescriptor : getPropertyDescriptors(obj)) {
            propertyNames.add(new Object[]{propertyDescriptor.getName(), propertyDescriptor.getPropertyType()});
        }

        return propertyNames;
    }

    private static PropertyDescriptor[] getPropertyDescriptors(Class obj) throws Exception {
        return Introspector.getBeanInfo(obj).getPropertyDescriptors();
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public boolean isCreateDTO() {
        return createDTO;
    }

    public void setCreateDTO(boolean createDTO) {
        this.createDTO = createDTO;
    }

    public boolean isCreateMapper() {
        return createMapper;
    }

    public void setCreateMapper(boolean createMapper) {
        this.createMapper = createMapper;
    }

    public boolean isCreateRepository() {
        return createRepository;
    }

    public void setCreateRepository(boolean createRepository) {
        this.createRepository = createRepository;
    }

    public boolean isCreateServiceImpl() {
        return createServiceImpl;
    }

    public void setCreateServiceImpl(boolean createServiceImpl) {
        this.createServiceImpl = createServiceImpl;
    }

    public List<Class> getAllModelClass() throws Exception {
        String filePath = getSrcFolder(basePath + ".model");
        File mfile = new File(filePath);

        List<Class> classes = new ArrayList<Class>();

        if (mfile.isDirectory()) {
            for (File xfile : mfile.listFiles(new FilenameFilter() {

                @Override
                public boolean accept(File dir, String name) {
                    if (name.contains(".java") && !name.startsWith("Q")) {
                        return true;
                    } else {
                        return false;
                    }
                }
            })) {
                Class<?> mClass = Class.forName(basePath + ".model." + xfile.getName().replaceFirst(".java", ""));
                classes.add(mClass);
            }
        }

        return classes;
    }

    public boolean isCreateIsDelete() {
        return createIsDelete;
    }

    public void setCreateIsDelete(boolean createIsDelete) {
        this.createIsDelete = createIsDelete;
    }

    public String getForcePath() {
        return forcePath;
    }

    public void setForcePath(String forcePath) {
        this.forcePath = forcePath;
    }

    public Class getModelObj() {
        return modelObj;
    }

    public void setModelObj(Class modelObj) {
        this.modelObj = modelObj;
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public boolean isOverrideFile() {
        return overrideFile;
    }

    public void setOverrideFile(boolean overrideFile) {
        this.overrideFile = overrideFile;
    }

    public boolean isCreateQModel() {
        return createQModel;
    }

    public void setCreateQModel(boolean createQModel) {
        this.createQModel = createQModel;
    }

    public boolean isCreateWsService() {
        return createWsService;
    }

    public void setCreateWsService(boolean createWsService) {
        this.createWsService = createWsService;
    }

    public String getBusinessSrcPath() {
        return businessSrcPath;
    }

    public void setBusinessSrcPath(String businessSrcPath) {
        this.businessSrcPath = businessSrcPath;
    }

    public String getWsBusinessSrcPath() {
        return wsBusinessSrcPath;
    }

    public void setWsBusinessSrcPath(String wsBusinessSrcPath) {
        this.wsBusinessSrcPath = wsBusinessSrcPath;
    }

    public String getWebSrcPath() {
        return webSrcPath;
    }

    public void setWebSrcPath(String webSrcPath) {
        this.webSrcPath = webSrcPath;
    }

    public boolean isWriteToFile() {
        return writeToFile;
    }

    public void setWriteToFile(boolean writeToFile) {
        this.writeToFile = writeToFile;
    }

    public String getProjName() {
        return projName;
    }

    public void setProjName(String projName) {
        this.projName = projName;
    }
}
