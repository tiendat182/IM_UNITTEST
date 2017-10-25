/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.fw.gencode;

import com.mysema.query.codegen.GenericExporter;
import com.mysema.query.codegen.Keywords;
import com.viettel.fw.Exception.CbsGenerateCodeException;
import com.viettel.fw.common.util.DataUtil;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.*;

/*
thiendn1:
- xoa cac class Mapper
- xoa cac class WS
- 20151217: chuyen cac enum sang DTO
*/
public class BaseGenerateCode {


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
    private boolean createMapper = false;
    private boolean createRepository = true;
    private boolean createServiceImpl = true;
    private boolean createIsDelete = false;
    private boolean createWsService = true;
    private boolean writeToFile = false;


    private final GenericExporter exporter;
    private List<SourceWriter> sourceWriters = new ArrayList<>();

    public void addSourceWriter(SourceWriter sourceWriter) {
        sourceWriters.add(sourceWriter);
    }

    public void generateQModel(Class obj) throws CbsGenerateCodeException {
        File file = new File(businessSrcPath + File.separator + "base");
        exporter.setTargetFolder(file);
        exporter.export(obj);
    }


    public BaseGenerateCode(String outputPath, String basePath) {
        this.basePath = basePath;
        if (outputPath != null) {
            businessSrcPath = outputPath;
        } else {
            businessSrcPath = System.getProperty("user.dir") + File.separator + "generatecode";
        }
        addSourceWriter(new FileSourceWriter());
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

        query.append(MessageFormat.format("import {0}.service.{1}Service;\n", basePath, objName));

        query.append("import org.springframework.stereotype.Service;\n");
        query.append("import org.apache.log4j.Logger;\n");
        query.append("import com.viettel.ws.provider.CxfWsClientFactory;\n");
        query.append("import org.springframework.beans.factory.annotation.Autowired;\n");
        query.append("import javax.annotation.PostConstruct;\n");
        query.append("import org.springframework.beans.factory.annotation.Qualifier;\n");

        query.append(MessageFormat.format("@Service\n", objName));
        query.append(MessageFormat.format("public class Ws{0}ServiceImpl implements {0}Service '{'\n\n", objName));
        query.append(MessageFormat.format("    public static final Logger logger = Logger.getLogger(Ws{0}ServiceImpl.class);\n\n", objName));
        query.append("    @Autowired\n");
        query.append("    @Qualifier(\"cxfWsClientFactory\")\n");
        query.append("    CxfWsClientFactory wsClientFactory;\n\n");
        query.append(MessageFormat.format("    private {0}Service client;\n\n", objName));
        query.append("    @PostConstruct\n");
        query.append("    public void init() throws Exception {\n");
        query.append("      try{\n");
        query.append(MessageFormat.format("         client = wsClientFactory.createWsClient({0}Service.class);\n", objName));
        query.append("      } catch (Exception ex) {\n");
        query.append("          logger.error(\"init\", ex);\n");
        query.append("      }\n");
        query.append("    }\n\n");
        query.append("");
        query.append("");
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
        String filePath = webSrcPath + File.separator + "web" + File.separator + "WEB-INF" + File.separator + "ws-endpoint.xml";

        String implementor = null;
        File tmpFile = new File(getFileName("service", objName + "Impl", SrcPathType.BUSINESS_SRV_IMPL));
        if (tmpFile.exists()) {
            implementor = obj.getPackage().getName() + "." + obj.getSimpleName() + "Impl";
        } else {
            implementor = obj.getPackage().getName() + "." + obj.getSimpleName() + "Impl";
        }


//        EndPointInfo wsEndPoint = new EndPointInfo(obj.getSimpleName(), implementor, "/" + projName + "/" + obj.getSimpleName());
//        writeSource(filePath, wsEndPoint);
    }


    /**
     * Sinh code tong hop ngoai tru WS  Service
     *
     * @param obj
     * @throws Exception
     */
    public void generateCode(Class obj) throws Exception {
        if (writeToFile) {
            FileSourceWriter fileSourceWriter = new FileSourceWriter();
            fileSourceWriter.setOverrideFile(overrideFile);
            addSourceWriter(fileSourceWriter);
        }

        if (DataUtil.isNullOrEmpty(basePath)) {
            throw new Exception("Chưa truyền vào basePath (ví dụ: com.viettel.sale");
        }
        createAllFile(obj, basePath);
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
//        if (createWsService) createWsServiceFix(obj);

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
                srcFolder = businessSrcPath + File.separator + "base";
                break;
            case BUSINESS_SRV_IMPL:
                srcFolder = businessSrcPath + File.separator + "srv-impl";
                break;
            case WSBUSINESS_WS_IMPL:
                srcFolder = businessSrcPath + File.separator + "ws-impl";
                break;
            case WEB:
                srcFolder = webSrcPath;
                break;
            default:
                srcFolder = businessSrcPath + File.separator + "base";
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
        packageName = StringUtils.replaceChars(packageName, ".", File.separator);
        return MessageFormat.format("{0}" + File.separator + "{1}" + File.separator + "{2}.java", srcPath, packageName, className);
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

        pkg = pkg.replaceAll("\\.", File.separator);
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
        query.append(MessageFormat.format("import {0}.dto.{1}.COLUMNS;\n", basePath, objName + "DTO"));
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
        HashMap<String, String> furtherImport = new HashMap<>();

        furtherImport.put("com.viettel.fw.dto.BaseDTO", "");
        furtherImport.put("java.io.Serializable", "");

        query.append(MessageFormat.format("public class {0}DTO extends BaseDTO implements Serializable '{'\n", objName));
        query.append("public String getKeySet() {\n return keySet; }\n");
        query.append(createEnum(obj));
        PropertyDescriptor[] propertyDescriptors = getPropertyDescriptors(obj);
        for (PropertyDescriptor des : propertyDescriptors) {
            if (validGenGetSet(des.getPropertyType().getName())) {
                furtherImport.put(des.getPropertyType().getName(), "");
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
        finalQuery.append(getImport(furtherImport));
        finalQuery.append(query);

        writeSource(className, finalQuery.toString());
    }

    private void createMapper(Class obj) throws Exception {

        String srcPath = getSrcFolder(SrcPathType.BUSINESS_SRV_IMPL)
                + File.separator
                + basePath.replaceAll("\\.", File.separator) + File.separator
                + "mapper";
        File fileCheck = new File(srcPath);
        fileCheck.mkdirs();


    }

    private void createServiceInterface(String path, Class obj) throws Exception {
        String objName = obj.getSimpleName();
        String className = getFileName("service", objName + "Service", SrcPathType.BUSINESS_BASE);

        StringBuilder query = new StringBuilder();
        query.append(MessageFormat.format("package {0};\n", path + ".service"));
        query.append(MessageFormat.format("import {0}.dto.{1}DTO;\n", basePath, objName));
        query.append("import java.util.List;\n");
        query.append("import com.viettel.fw.common.util.extjs.FilterRequest;\n");
        query.append("import javax.jws.WebMethod;\n");
        query.append("import javax.jws.WebService;\n");

        query.append("@WebService\n");
        query.append(MessageFormat.format("public interface {0}Service '{'\n\n", objName));
        query.append("    @WebMethod\n");
        query.append(MessageFormat.format("    public {0}DTO findOne(Long id) throws Exception;\n\n", objName));
        query.append("    @WebMethod\n");
        query.append("    public Long count(List<FilterRequest> filters) throws Exception;\n\n");

        query.append("    @WebMethod\n");
        query.append(MessageFormat.format("    public List<{0}DTO> findByFilter(List<FilterRequest> filters) throws Exception;\n\n", objName));

        query.append("\n}");
        writeSource(className, query.toString());
    }

    private void createServiceImpl(String path, Class obj) throws Exception {
        String objName = obj.getSimpleName();
        String className = getFileName("service", objName + "ServiceImpl", SrcPathType.BUSINESS_SRV_IMPL);

        StringBuilder query = new StringBuilder();

        query.append(MessageFormat.format("package {0};\n", path + ".service"));
        query.append(MessageFormat.format("import {0}.repo.{1}Repo;\n", basePath, objName));
        query.append("import com.viettel.fw.common.util.mapper.BaseMapper;\n");
        query.append(MessageFormat.format("import {0}.dto.{1}DTO;\n", basePath, objName));
        query.append(MessageFormat.format("import {0}.model.{1};\n", basePath, objName));
        query.append(MessageFormat.format("import {0}.service.{1}Service;\n", basePath, objName));
        query.append("import com.viettel.fw.common.util.extjs.FilterRequest;\n");
        query.append("import java.util.List;\n");
        query.append("import org.apache.log4j.Logger;\n");
        query.append("import org.springframework.beans.factory.annotation.Autowired;\n");
        query.append("import org.springframework.stereotype.Service;\n");
        query.append("import com.viettel.fw.service.BaseServiceImpl;\n");
        query.append("import javax.jws.WebMethod;\n");
        query.append("import org.springframework.transaction.annotation.Transactional;\n\n");

        query.append("@Service\n");
        query.append(MessageFormat.format("public class {0}ServiceImpl extends BaseServiceImpl implements {0}Service '{'\n\n", objName));

        query.append(MessageFormat.format("    private final BaseMapper<{0},{1}> mapper = " +
                "new BaseMapper<>({0}.class,{1}.class);\n\n", objName, objName + "DTO"));

        query.append("    @Autowired\n");
        query.append(MessageFormat.format("    private {0}Repo repo;\n", objName));
        query.append(MessageFormat.format("    public static final Logger logger = Logger.getLogger({0}Service.class);\n\n", objName));

        query.append("    @WebMethod\n");
        query.append(MessageFormat.format("    public Long count(List<FilterRequest> filters) throws Exception '{'\n", objName));
        query.append("        return repo.count(repo.toPredicate(filters));\n");
        query.append("    }\n\n");

        query.append("    @WebMethod\n");
        query.append(MessageFormat.format("    public {0}DTO findOne(Long id) throws Exception '{'\n", objName));
        query.append("        return mapper.toDtoBean(repo.findOne(id));\n");
        query.append("    }\n\n");

        query.append("    @WebMethod\n");
        query.append(MessageFormat.format("    public List<{0}DTO> findByFilter(List<FilterRequest> filters) throws Exception '{'\n\n", objName));
        query.append("        return mapper.toDtoBean(repo.findAll(repo.toPredicate(filters)));\n");
        query.append("    }\n\n");
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
        }

        return allContent;
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

        List<Class> classes = new ArrayList<>();

        if (mfile.isDirectory()) {
            for (File xfile : mfile.listFiles((dir, name) -> {
                return name.contains(".java") && !name.startsWith("Q");
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
