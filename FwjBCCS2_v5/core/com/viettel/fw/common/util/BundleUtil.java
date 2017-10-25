package com.viettel.fw.common.util;

import com.google.common.collect.Maps;
import com.sun.faces.application.ApplicationAssociate;
import com.sun.faces.application.ApplicationResourceBundle;
import com.viettel.fw.Exception.UnsupportedEnvironmentException;
import com.viettel.fw.bundle.MultiResourceBundle;
import com.viettel.ws.common.utils.Locate;
import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @author LamNV5
 * @since 26/05/2015
 */
public class BundleUtil {
    private static Logger logger = Logger.getLogger(BundleUtil.class);
    private static HashMap<Locate, ResourceBundle> resourceBundleMap = Maps.newHashMap();

    private String languageLocation;
    private List<Locate> supportedLanguages;
    private static String runEnvironment;
    private static String languageSort;

    public BundleUtil() {

    }

    public void init() {
        for (Locate locale : supportedLanguages) {
            Locale jLocale = new Locale(locale.getLanguage(), locale.getCountry());
            resourceBundleMap.put(locale,new MultiResourceBundle(languageLocation,jLocale));
        }
    }

    public static String getText(Locate locate, String key) {
        String result = key;
        try {
//             Khi nao can thi mo comment dong duoi, chay 1 lan xong dong comment lai thi lang se duoc reload
//            reloadBundle();
            ResourceBundle bundle = resourceBundleMap.get(locate);
            if (key != null) {
                result = bundle.getString(key);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return result;
        }

        return result;
    }

    /**
     * KHONG DUOC DUNG HAM NAY O SERVICE, MA TOT NHAT LA KHONG NEN DUNG, BASE CONTROLLER VA BASE SERVICE DA HO TRO SAN
     *
     * @param key
     * @return
     */
    @Deprecated
    public static String getText(String key) {
        String result = key;
        try {
            checkEnvironment("getText(String key)", "service");
            ResourceBundle bundle = resourceBundleMap.get(
                    new Locate(FacesContext.getCurrentInstance().getViewRoot().getLocale().getLanguage(),
                            FacesContext.getCurrentInstance().getViewRoot().getLocale().getCountry()));
            result = bundle.getString(key);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return result;
        }

        return result;
    }

    public static void addFacesMsg(FacesMessage.Severity severity, String messageId, String summaryKey, String detailKey, String moreInfo) {
        try {
            checkEnvironment("addFacesMsg", "service");

            String tempMoreInfo = DataUtil.isNullOrEmpty(moreInfo) ? "" : moreInfo;
            String tempMessageId = DataUtil.isNullOrEmpty(messageId) ? "" : messageId;
            if (!DataUtil.isNullOrEmpty(summaryKey)) {
                if (!DataUtil.isNullOrEmpty(detailKey)) {
                    FacesContext.getCurrentInstance().addMessage(tempMessageId, new FacesMessage(severity, getText(summaryKey), getText(detailKey) + ". " + tempMoreInfo));
                } else {
                    FacesContext.getCurrentInstance().addMessage(tempMessageId, new FacesMessage(severity, getText(summaryKey), "" + " " + tempMoreInfo));
                }
            } else {
                if (!DataUtil.isNullOrEmpty(detailKey)) {
                    FacesContext.getCurrentInstance().addMessage(tempMessageId, new FacesMessage(severity, "", getText(detailKey) + ". " + tempMoreInfo));
                } else {
                    FacesContext.getCurrentInstance().addMessage(tempMessageId, new FacesMessage(severity, "", "" + " " + tempMoreInfo));
                }
            }
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public static void sendSuccessMsg(String areaId, String errorCode, String description) throws Exception {
        description = modifyMsg(description);
        checkEnvironment("sendSuccessMsg", "service");
        FacesContext.getCurrentInstance().addMessage(areaId, new FacesMessage(FacesMessage.SEVERITY_INFO, errorCode, description));
        RequestContext.getCurrentInstance().update(areaId);
    }

    public static void sendErrorMsg(String areaId, String errorCode, String description) throws UnsupportedEnvironmentException {
        description = modifyMsg(description);
        checkEnvironment("sendErrorMsg", "service");
        FacesContext.getCurrentInstance().addMessage(areaId, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorCode, description));
        RequestContext.getCurrentInstance().update(areaId);
    }


    public static void sendWarnMsg(String areaId, String errorCode, String description) throws UnsupportedEnvironmentException {
        description = modifyMsg(description);
        checkEnvironment("sendWarnMsg", "service");
        FacesContext.getCurrentInstance().addMessage(areaId, new FacesMessage(FacesMessage.SEVERITY_WARN, errorCode, description));
        RequestContext.getCurrentInstance().update(areaId);

    }

    private static String modifyMsg(String msg) {
        msg = DataUtil.safeToString(msg);
        if (msg.contains("SQLTimeoutException")) {
            msg = GetTextFromBundleHelper.getText("err.timeout.db");
        }

        msg = "[" + GetTextFromBundleHelper.getReqId() + "] " + msg;

        return msg;
    }

    public static void reloadBundle() {
        try {
            ResourceBundle.clearCache();
            ResourceBundle.clearCache(Thread.currentThread().getContextClassLoader());

            ApplicationResourceBundle appBundle = ApplicationAssociate.getCurrentInstance().getResourceBundles().get("lang");
            Map<Locale, ResourceBundle> resources = getFieldValue(appBundle, "resources");
            resources.clear();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> T getFieldValue(Object object, String fieldName) {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return (T) field.get(object);
        } catch (Exception e) {
            return null;
        }
    }

    public void setLanguageLocation(String languageLocation) {
        this.languageLocation = languageLocation;
    }

    public void setSupportedLanguages(List<Locate> supportedLanguages) {
        this.supportedLanguages = supportedLanguages;
    }

    public static void checkEnvironment(String function, String rejectEnvironment) throws UnsupportedEnvironmentException {
        if (runEnvironment.equals(rejectEnvironment)) {
            throw new UnsupportedEnvironmentException("Khong duoc phep goi ham " + function + " trong moi truong " + rejectEnvironment);
        }
    }

    public static String getSortCode() {
        return languageSort;
    }

    public void setSortCode(String sortCode) {
        BundleUtil.languageSort = sortCode;
    }

    public void setRunEnvironment(String runEnvironment) {
        BundleUtil.runEnvironment = runEnvironment;
    }

    public static String getRunEnvironment() {
        return BundleUtil.runEnvironment;
    }


}
