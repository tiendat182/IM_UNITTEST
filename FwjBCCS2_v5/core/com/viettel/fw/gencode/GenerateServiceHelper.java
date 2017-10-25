package com.viettel.fw.gencode;


import com.google.common.base.Defaults;
import com.thoughtworks.paranamer.AdaptiveParanamer;
import com.thoughtworks.paranamer.Paranamer;
import com.viettel.fw.common.util.DateUtil;
import com.viettel.ws.common.utils.XmlSchema;
import com.viettel.ws.provider.WsCallerFactory;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.text.MessageFormat;
import java.util.*;

/**
 * Created by thiendn1 on 4/1/2015.
 */
public class GenerateServiceHelper {
    private static GenerateServiceHelper instance;
    private final static Map<Class<?>, Class<?>> map = new HashMap<Class<?>, Class<?>>();

    static {
        instance = new GenerateServiceHelper();
        map.put(boolean.class, Boolean.class);
        map.put(byte.class, Byte.class);
        map.put(short.class, Short.class);
        map.put(char.class, Character.class);
        map.put(int.class, Integer.class);
        map.put(long.class, Long.class);
        map.put(float.class, Float.class);
        map.put(double.class, Double.class);
    }

    public static GenerateServiceHelper getInstance() {
        return instance;
    }

    private String forcePath = null;

    private String generateNameForRequest(Class className) {
        String wsConfigName = className.getSimpleName();
        wsConfigName = wsConfigName.replaceAll("\\.", "-").replaceAll("com-viettel-", "");
        return wsConfigName;
    }

    private String generateNameForReponse(Class className, Method method) {
        String wsConfigName = className.getSimpleName() + "-" + method.getName();
        wsConfigName = wsConfigName.replaceAll("\\.", "-").replaceAll("com-viettel-", "");
        return wsConfigName;
    }

    public void generateWsRequestForInterface(Class className) throws FileNotFoundException {
        StringBuilder wsConfigBuilder = new StringBuilder();
        String wsConfigName = generateNameForRequest(className);

        wsConfigBuilder.append("<configuration>");
        wsConfigBuilder.append("<!--Begin " + wsConfigName + " -->");

        wsConfigBuilder.append("<").append(wsConfigName)
                .append(" config-class=\"com.viettel.ws.common.utils.WsRequestCreator\"");
        wsConfigBuilder.append(" wsAddress=\"???\"")
                .append(" targetNameSpace=")
                .append("\"").append("http://")
                .append(new StringBuilder(generateNameSpace(className.getPackage().getName()))).append("/\"");
        wsConfigBuilder.append(">");
        wsConfigBuilder.append("</").append(wsConfigName).append(">");
        Method[] m = className.getDeclaredMethods();
        wsConfigBuilder.append("<!--End " + wsConfigName + " -->");

        for (int i = 0; i < m.length; i++) {
            String wsConfigOperatorName = wsConfigName + "-" + m[i].getName();
            wsConfigBuilder
                    .append("<").append(wsConfigOperatorName).append(">")
                    .append("<url>").append(wsConfigName).append("</url>")
                    .append("<service>").append(m[i].getName()).append("</service>")
                    .append("</").append(wsConfigOperatorName).append(">");
            wsConfigBuilder.append("<!---->");

        }
        wsConfigBuilder.append("</configuration>");
        try (PrintStream out = new PrintStream(new FileOutputStream(className.getSimpleName() + "Request.xml"))) {
            out.print(XmlSchema.formatXML(wsConfigBuilder.toString()));
            out.flush();
        }
    }

    public void generateWsRequestsForInterface(Class[] classNames) throws FileNotFoundException {
        for (Class className : classNames) {
            generateWsRequestForInterface(className);
        }
    }

    private String generateWsResponseForInterface(Class className, Method method) throws NoSuchMethodException {
        boolean isMultiple = method.getReturnType().equals(List.class);
        StringBuilder wsConfigBuilder = new StringBuilder();
        if (method.getReturnType().getName().equals("void")) {
            return "";
        }
        String wsConfigName = generateNameForReponse(className, method);
        wsConfigBuilder.append("<!--Begin of " + wsConfigName + " />-->");

        wsConfigBuilder.append("<").append(wsConfigName).append(">");
        if (isMultiple) {
            wsConfigBuilder.append("<type>").append("multiple").append("</type>");
            wsConfigBuilder.append("<alias>");
            wsConfigBuilder.append("<tag>").append("ns2:" + method.getName() + "Response").append("</tag>");
            wsConfigBuilder.append("<class>").append("com.viettel.ws.provider.WsDtoContainer").append("</class>");
            wsConfigBuilder.append("</alias>");

            ParameterizedType type = (ParameterizedType) method.getGenericReturnType();
            wsConfigBuilder.append("<alias>");
            wsConfigBuilder.append("<tag>").append("return").append("</tag>");
            wsConfigBuilder.append("<class>").append(((Class) type.getActualTypeArguments()[0]).getName()).append("</class>");
            wsConfigBuilder.append("</alias>");

            wsConfigBuilder.append("<addImplicitCollection>");
            wsConfigBuilder.append("<tag>").append("list").append("</tag>");
            wsConfigBuilder.append("<class>").append("com.viettel.ws.provider.WsDtoContainer").append("</class>");
            wsConfigBuilder.append("</addImplicitCollection>");

        } else {
            wsConfigBuilder.append("<type>").append("single").append("</type>");
            wsConfigBuilder.append("<alias>");
            wsConfigBuilder.append("<tag>").append("return").append("</tag>");
            wsConfigBuilder.append("<class>").append(method.getReturnType().getName()).append("</class>");
            wsConfigBuilder.append("</alias>");

            Field[] fields = method.getReturnType().getDeclaredFields();
            // check all fields in order to detect collection type
            for (Field field : fields) {
                if (field.getType().equals(List.class)) {


                    ParameterizedType type = (ParameterizedType) field.getGenericType();
                    wsConfigBuilder.append("<alias>");
                    wsConfigBuilder.append("<tag>").append(field.getName()).append("</tag>");
                    wsConfigBuilder.append("<class>").append(((Class) type.getActualTypeArguments()[0]).getName()).append("</class>");
                    wsConfigBuilder.append("</alias>");


                    wsConfigBuilder.append("<addImplicitCollection>");
                    wsConfigBuilder.append("<tag>").append(field.getName()).append("</tag>");
                    wsConfigBuilder.append("<class>").append(method.getReturnType().getName()).append("</class>");
                    wsConfigBuilder.append("</addImplicitCollection>");
                }
            }
        }
        wsConfigBuilder.append("</").append(wsConfigName).append(">");
        wsConfigBuilder.append("<!--End of " + wsConfigName + " />-->");
        wsConfigBuilder.append("<!---->");
        wsConfigBuilder.append("<!---->");

        return wsConfigBuilder.toString();
    }

    public void generateWsResponseForInterface(Class className) throws NoSuchMethodException, FileNotFoundException {

        StringBuilder wsConfigBuilder = new StringBuilder();
        wsConfigBuilder.append("<configuration>");
        Method[] method = className.getMethods();
        for (Method testMethod : method) {
            wsConfigBuilder.append(generateWsResponseForInterface(className, testMethod));
        }
        wsConfigBuilder.append("</configuration>");
        try (PrintStream out = new PrintStream(new FileOutputStream(className.getSimpleName() + "Response.xml"))) {
            out.print(XmlSchema.formatXML(wsConfigBuilder.toString()));
            out.flush();
        }
    }

    private String generateNameSpace(String packageName) {
        List<String> list = Arrays.asList(packageName.split("\\."));
        Collections.reverse(list);
        StringBuilder nameSpace = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            nameSpace.append(list.get(i)).append(".");
        }
        if (nameSpace.length() > 0) {
            return nameSpace.deleteCharAt(nameSpace.length() - 1).toString();
        } else {
            return nameSpace.toString();
        }
    }

    public void generateServiceForInterface(Class className) throws NoSuchMethodException, FileNotFoundException {
        generateWsRequestForInterface(className);
        generateWsResponseForInterface(className);
    }

    public void generateServiceForInterface(Class[] classNames) throws NoSuchMethodException, FileNotFoundException {
        for (Class className : classNames) {
            generateWsRequestForInterface(className);
            generateWsResponseForInterface(className);
        }

    }

    public void generateServiceImplByConfig(Class className) throws Exception {

        String objName = className.getSimpleName();

        String basePath = getFileName(className.getPackage().getName());

        StringBuilder query = new StringBuilder();

        query.append(className.getPackage() + ";\n");
        query.append("import com.viettel.ws.provider.WsDtoContainer;\n");
        query.append("import com.viettel.ws.common.utils.WebServiceHandler;\n");
        query.append("import java.util.List;\n");
        query.append("import java.util.ArrayList;\n");

        query.append(MessageFormat.format("public class {0}Impl implements {0} '{'\n\n", objName));

        Method[] methods = className.getDeclaredMethods();

        for (Method method : methods) {
            String requestIdConfig = generateNameForRequest(className) + "-" + method.getName();
            String responseFileConfig = className.getSimpleName() + "Response.xml";
            String serviceIdConfig = generateNameForReponse(className, method);

            query.append("@Override\n");
            query.append("public ").append(method.getGenericReturnType()).append(" ").append(method.getName())
                    .append("(");
            Class[] parameterTypes = method.getParameterTypes();
            if (parameterTypes.length == 0) {
                query.append(")");
            } else {
                for (int i = 0; i < parameterTypes.length; i++) {
                    query.append(parameterTypes[i].getName()).append(" arg").append(i).append(",");
                }
                query.deleteCharAt(query.length() - 1).append(")");
            }
            query.append(" throws Exception  {\n");

            if (parameterTypes.length > 0) {
                query.append("List<Object> bodyParameters = new ArrayList<>();\n");
                for (int i = 0; i < parameterTypes.length; i++) {
                    query.append("bodyParameters.add(arg" + i + ");\n");
                }
                query.append("String responseContent = WebServiceHandler.webServiceOperatorCaller(\"" + requestIdConfig + "\", null, bodyParameters);\n");
            } else {
                query.append("String responseContent = WebServiceHandler.webServiceOperatorCaller(\"" + requestIdConfig + "\", null, null);\n");
            }
            // check return type of method
            boolean isMultiple = method.getReturnType().equals(List.class);

            if (!method.getReturnType().getName().equals("void")) {
                if (isMultiple) {
                    ParameterizedType type = (ParameterizedType) method.getGenericReturnType();
                    query.append("WsDtoContainer wsDtoContainer = (WsDtoContainer)WebServiceHandler.wsServiceHandler(responseContent,\"" + responseFileConfig + "\",\"" + requestIdConfig + "\");\n");
                    query.append("return " + "(" + method.getReturnType().getName() + ")" + " wsDtoContainer.getList();\n");

                } else {

                    query.append(method.getReturnType().getName()).append(" responseObject = ")
                            .append("(").append(method.getReturnType().getName()).append(")")
                            .append("WebServiceHandler.wsServiceHandler(responseContent,\"" + responseFileConfig + "\",\"" + requestIdConfig + "\");\n");
                    query.append("return responseObject;\n");
                }
            }

            query.append("}\n");

        }

        query.append("\n}");
        File file = new File(basePath + "/impl");
        if (!file.exists()) {
            file.mkdirs();
        }
        createFile(basePath + "\\impl\\" + objName + "Impl.java", query.toString(), false);
        System.out.println(query);

    }


    @SuppressWarnings("parameter must not be interface")
    public void generateServiceImplByCode(Class className) throws Exception {

        Paranamer paranamer = new AdaptiveParanamer();
        String objName = className.getInterfaces()[0].getSimpleName();

        StringBuilder classBuilder = new StringBuilder();

        classBuilder.append(className.getPackage() + ".ws;\n");

        classBuilder.append("import java.util.List;\n");
        classBuilder.append("import java.util.ArrayList;\n");
        classBuilder.append("import org.springframework.beans.factory.annotation.Autowired;\n");
        classBuilder.append("import org.springframework.stereotype.Service;\n");
        classBuilder.append("import org.apache.log4j.Logger;\n");
        classBuilder.append("import com.viettel.ws.common.utils.*;\n");
        classBuilder.append("import com.viettel.ws.provider.*;\n");
        classBuilder.append(MessageFormat.format("import {0};\n", WsCallerFactory.class.getName()));

        classBuilder.append(MessageFormat.format("import {0};\n", className.getName()));
        classBuilder.append(MessageFormat.format("import {0};\n", className.getInterfaces()[0].getName()));

        classBuilder.append(MessageFormat.format("@Service(\"WsEsb{0}Impl\")\n", objName));
        classBuilder.append(MessageFormat.format("public class WsEsb{0}Impl extends {0}Impl implements {0} '{'\n\n", objName));
        classBuilder.append(MessageFormat.format("public static final Logger logger = Logger.getLogger(WsEsb{0}Impl.class);\n\n", objName));
        classBuilder.append("@Autowired\nprivate WsCallerFactory wsCallerFactory;\n");

        Method[] methods = className.getDeclaredMethods();


        for (Method method : methods) {
            String[] parameterNames = paranamer.lookupParameterNames(method);

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

            classBuilder.append("@Override\n");
            String returnTypeName = method.getGenericReturnType().toString();

            if (returnTypeName.contains("class")) {
                returnTypeName = method.getGenericReturnType().toString().substring("class".length());

            }
            classBuilder.append(MessageFormat.format("public {0} {1} (", returnTypeName, method.getName()));

            StringBuilder insideMethodBuilder = new StringBuilder();

            Class[] parameterTypes = method.getParameterTypes();

            if (parameterTypes.length == 0) {
                classBuilder.append(")");
            } else {
                for (int i = 0; i < parameterTypes.length; i++) {
//                    query.append(parameterTypes[i].getName()).append(" arg").append(i).append(",");
                    classBuilder.append(MessageFormat.format(" {0} {1},", parameterTypes[i].getName(), parameterNames[i]));

                }
                classBuilder.deleteCharAt(classBuilder.length() - 1).append(")");
            }
            Class[] exceptions = method.getExceptionTypes();
            boolean hasExceptionType = false;
            if (exceptions.length > 0) {
//                classBuilder.append(" throws Exception {\n");
                StringBuilder exceptionBuilders = new StringBuilder();
                for (Class classException : exceptions) {
                    exceptionBuilders.append(classException.getName()).append(",");
                    if (classException.getName().equals(Exception.class.getName())) {
                        hasExceptionType = true;
                    }
                }
                exceptionBuilders.deleteCharAt(exceptionBuilders.length() - 1);
                classBuilder.append(MessageFormat.format(" throws {0}  ", exceptionBuilders));
//
            }
            classBuilder.append("  {\n");

            insideMethodBuilder.append(MessageFormat.format("WsRequestCreator requestCreator = wsCallerFactory.createWsCaller({0}.class, \"{1}\");\n", objName, method.getName()));

            if (parameterTypes.length > 0) {
                insideMethodBuilder.append("List<Object> bodyParameters = new ArrayList<>();\n");
                for (int i = 0; i < parameterTypes.length; i++) {
//                    query.append("bodyParameters.add(arg" + i + ");\n");
                    insideMethodBuilder.append(MessageFormat.format("bodyParameters.add({0});\n", parameterNames[i]));

                }

                insideMethodBuilder.append("requestCreator.setBodyParameters(bodyParameters);\n");

            }
            insideMethodBuilder.append("String response = WebServiceHandler.webServiceCaller(requestCreator);\n");

            if (method.getReturnType().getName().equals("void")) {
                insideMethodBuilder.append("}\n");
                continue;
            }

            insideMethodBuilder.append("List<XStreamStorage> parseObject = new ArrayList<>();\n");

            // check return type of method
            boolean isMultiple = method.getReturnType().equals(List.class);

            if (isMultiple) {

                ParameterizedType type = (ParameterizedType) method.getGenericReturnType();
                insideMethodBuilder.append(MessageFormat.format("Mapper.alias(parseObject, WsDtoContainer.class, \"ns2:{0}Response\");\n", method.getName()));
                insideMethodBuilder.append(MessageFormat.format("Mapper.alias(parseObject, {0}.class, \"return\");\n", ((Class) type.getActualTypeArguments()[0]).getName()));
                insideMethodBuilder.append("Mapper.addImplicitCollection(parseObject, WsDtoContainer.class, \"list\");\n");

                insideMethodBuilder.append("XmlStream xmlStream = new XmlStream();\n");
                insideMethodBuilder.append("xmlStream.config(parseObject);\n");
                insideMethodBuilder.append("xmlStream.isSingleType = false;\n");
                insideMethodBuilder.append("WsDtoContainer wsDtoContainer = (WsDtoContainer) WebServiceHandler.wsServiceHandler(response, xmlStream);\n");
                insideMethodBuilder.append("return " + "(" + method.getReturnType().getName() + ")" + " wsDtoContainer.getList();\n");

            } else {
                Class returnTypeClass = map.get(method.getReturnType());
                if(returnTypeClass!=null){
                    insideMethodBuilder.append(MessageFormat.format("Mapper.alias(parseObject, {0}.class, \"return\");\n", returnTypeClass.getName()));

                }
                else{
                    insideMethodBuilder.append(MessageFormat.format("Mapper.alias(parseObject, {0}.class, \"return\");\n", method.getReturnType().getName()));

                }



                Field[] fields = method.getReturnType().getDeclaredFields();
                // check all fields in order to detect collection type
                for (Field field : fields) {
                    if (field.getType().equals(List.class)) {
                        ParameterizedType type = (ParameterizedType) field.getGenericType();
                        insideMethodBuilder.append(MessageFormat.format("Mapper.addImplicitCollection(parseObject, {0}.class, \"{1}\");\n", method.getReturnType().getName(), field.getName()));
                        insideMethodBuilder.append(MessageFormat.format("Mapper.alias(parseObject, {0}.class, \"{1}\");\n", ((Class) type.getActualTypeArguments()[0]).getName(), field.getName()));

//        Mapper.alias(parseObject, ProductSpecCharacterDTO.class, "dataElements");
                    }
                }
                insideMethodBuilder.append("XmlStream xmlStream = new XmlStream();\n");
                insideMethodBuilder.append("xmlStream.config(parseObject);\n");
                insideMethodBuilder.append("xmlStream.isSingleType = true;\n");

                insideMethodBuilder.append(MessageFormat.format("{0} responseObject = ({0}) WebServiceHandler.wsServiceHandler(response, xmlStream);\n", method.getReturnType().getName()));
                insideMethodBuilder.append("return responseObject;\n");

            }


            if (exceptions.length > 0 && hasExceptionType) {
                classBuilder.append(insideMethodBuilder);
            }
            else{
                insideMethodBuilder.insert(0, "try{\n");
                insideMethodBuilder.append("}\n catch(Exception ex) {\n");
                insideMethodBuilder.append("logger.debug(ex.getMessage());\n");
                insideMethodBuilder.append(MessageFormat.format(" {0} valueReturn = {1} ; \n", method.getReturnType().getName(), Defaults.defaultValue(method.getReturnType())));
                insideMethodBuilder.append("return valueReturn;\n");

                classBuilder.append(insideMethodBuilder);
                classBuilder.append("}\n");

            }

            classBuilder.append("}\n");

        }

        classBuilder.append("\n}");

        String basePath = getFileName(className.getPackage().getName() + ".ws");
        File file = new File(basePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        createFile(basePath + "\\" + objName + "Impl.java", classBuilder.toString(), false);

    }

    public void generateServiceImplByCode(Class[] classNames) throws Exception {

        for (Class className : classNames) {
            generateServiceImplByCode(className);
        }
    }

    private String getFileName(String path) {

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
        path = path.replaceAll("\\.", "\\\\");
        return path;
    }

    private void createFile(String filePath, String fileContent, boolean overrideFile) throws Exception {

        File file = new File(filePath);
        File folder = new File(file.getParent());
        if (!folder.exists()) {
            folder.mkdirs();
        }

        if (file.exists() && !overrideFile) {
            File bkfile = new File(file.getPath() + ".bk" + DateUtil.date2HHMMssNoSlash(new Date()));
            file.renameTo(bkfile);
        }

        FileOutputStream sfile = new FileOutputStream(file);
        sfile.write(fileContent.getBytes());
        sfile.close();

    }

}


