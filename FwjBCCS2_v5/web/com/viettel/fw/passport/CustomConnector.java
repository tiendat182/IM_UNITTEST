package com.viettel.fw.passport;

import org.apache.log4j.Logger;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class CustomConnector {

    public static final String VSA_USER_TOKEN = "vsaUserToken";
    public static final String DEFAULT = "vsaUserToken";
    public static final String LOGIN_DATE = "loginDate";
    public static final String LOGIN_RETRY = "loginRetry";
    public static String passportLoginURL;
    public static String serviceURL;
    public static String domainCode;
    public static String passportValidateURL;
    public static String passportServiceUrl;
    public static String errorUrl;
    public static String[] allowedUrls;
    public static final String FILE_URL = "cas";
    public static ResourceBundle rb = null;
    private static Logger log = Logger.getLogger(CustomConnector.class);
    private final boolean showAllMenu = false;

    static {
        try {
            if (rb == null) {
                rb = ResourceBundle.getBundle("cas");
                passportLoginURL = rb.getString("loginUrl");
                serviceURL = rb.getString("service");
                domainCode = rb.getString("domainCode");
                passportValidateURL = rb.getString("validateUrl");
                passportServiceUrl = rb.getString("passportServiceUrl");
                errorUrl = rb.getString("errorUrl");
                allowedUrls = rb.getString("AllowUrl").split(",");
            }
        } catch (MissingResourceException e) {
            log.error(e.getMessage(), e);
        }
    }


}
