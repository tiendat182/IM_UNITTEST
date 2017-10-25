/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.fw.common.util;

/**
 * Khi merge cac phan he vao thi can co mot COMMON COnstants class.
 *
 * @author Khuong Dao
 */
public class Const {

    public static final String URL_BEAN = "URLBean";
    public static final String VSA_ALLOW_URL = "VSA_ALLOW_URL";
    public static final String LOCK_ALLOW_URL = "LOCK_ALLOW_URL";
    public static final String DEFAULT_SYS = "DEFAULT_SYS";
    public static final String ACTIVITY_ACTOR = "activity:";
    public static final String LOGGING_COLLECTOR_ACTOR = "logging:";
    public static final String LOGGING_ANALYTIC_ACTOR = "loggingAnalytic";
    public static final String FAVORITE_ACTOR = "favorite";
    public static final String LOGGING_KPI_ACTOR = "kpi";
    public static final String WEBSERVICE_CONFIGURATION = "webServiceConfiguration";
    public static final String BAR_MODEL_1 = "BAR_MODEL_1";
    public static final String BAR_MODEL_2 = "BAR_MODEL_2";
    public static final String BAR_MODEL_3 = "BAR_MODEL_3";

    public static class LOGISTIC_WS_STATUS {

        public static final String SUCCESS = "1";
        public static final String REJECT = "2";
        public static final String CALL_WS = "3";
    }

    public final class LOGISTICS {
        public final static String FAILED = "0";
        public final static String SUCCESS = "1";
        public final static String ORDER_TYPE_EXPORT = "2";
        public final static String ORDER_TYPE_IMPORT = "1";


    }

    public final static class LOGISTICS_STATUS {

        public final static Long FAIL = 4L;
        public final static Long SUCCESS = 1L;
        public final static Long REJECT = 2L;
    }

    public final class DEPOSIT_STATUS {

        public final static String DEPOSIT_HAVE = "1";
        public final static String DEPOSIT_NOT_HAVE = "0";
    }

    public static final String USER_DTO_TOKEN = "USER_DTO_TOKEN";
    public static final String CALL_ID_TOKEN = "CALL_ID_TOKEN";
    public static final String KPI_ID_TOKEN = "KPI_ID_TOKEN";

    public final static String STAFF_TOKEN = "STAFF_TOKEN";
    public final static String STATUS_ACTIVE = "1";
    public static final String SEPARATOR_OF_FILE_NAME = "_";

    public final class NOTIFY {
        public final static String CHANNEL = "notification";
        public final static int STATE_DELETE = 0;
        public final static int STATE_READ = 2;
        public final static int STATE_UNREAD = 1;
        public final static int STATE_DRAFT = 3;
        public final static int TYPE_NORMAL = 1;
        public final static int TYPE_DRAF = 2;

    }


    public final class WEB_SERVICE_CONS {

        public final static String SEPERATE_URL = "/";
        public final static String SEPERATE_OPERATOR = ":";
        public final static String ARG_HEADER = "$headerArgument$";
        public final static String ARG_BODY = "$argument$";
    }

    public final class ERROR_CODE {
        public final static String ERROR_TEST = "LOGIC_EXCEPTION";

    }

    public final class ERROR_CODE_NUMBER {
        public final static String ERROR_TEST = "696";

        public final static String SUCCESSFUL = "000";

        // Loi nguoi dung 1xx
        public final static String ERROR_USER_LOGIN = "100";
        public final static String ERROR_USER_PERMISSION = "101";
        public final static String ERROR_USER_INVALID_INPUT = "102";
        public final static String ERROR_USER_MISSING_INPUT = "103";
        public final static String ERROR_USER_WRONG_ACTION = "104";

        // Loi co ban trong lap trinh 2xx || 3xx

        public final static String ERROR_NULL_POINTER_EX = "200";
        public final static String ERROR_ARRAY_OUT_OF_BOUNDS_EX = "201";
        public final static String ERROR_CLASS_NOT_FOUND = "202";
        public final static String ERROR_NUMBER_FORMAT_EX = "204";
        public final static String ERROR_PARSE_EX = "204";
        public final static String ERROR_CLASS_CAST_EX = "206";


        // Loi memmory 4xx

        // Loi lien quan den ket noi he thong ngoai 5xx
        public final static String ERROR_CONNECT_EX = "501";
        public final static String ERROR_TIMOUT_EX = "503";

        //Thao tac file
        public final static String ERROR_FILE_NOT_FOUND_EX = "600";

        //Thiendn1: dinh nghia loi nghiep vu chung
        public final static String ERROR_BUSINESS_CODE = "700";


        // Loi chua xac dinh
        public final static String ERROR_UNKNOW_EX = "999";


        // Error
        public final static String ERROR_VIR_MACHINE_ERROR = "996";
        public final static String ERROR_ASSERT_ERROR = "997";

    }

    public final class ID_ERROR {
        public final static int max_lengh = 6;
    }

    //quangkm added isdn pattern for search
    public final static String CHECK_ISDN_ADD_PREFIX = "^0+(?!$)|^84(?!$)";

    public final static boolean IS_LOGGER_CONSOLE = true;
    public final static Long MAX_ID_ERROR = 99999L;

    public static final class LOGGING_INFO {
        public static final String TRANS_OK = "1";
        public static final String TRANS_NOK = "0";
        public static final String ERROR_CODE_DEFAULT = "0";
        public static final String WRITE_LOG_OK = "1";
        public static final String WRITE_LOG_NOK = "0";
    }

}
