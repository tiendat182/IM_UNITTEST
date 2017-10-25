/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.bccs.inventory.common;


import com.google.common.collect.Lists;
import com.viettel.fw.common.util.DataUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Khi merge cac phan he vao thi can co mot COMMON COnstants class.
 *
 * @author ThanhNT77
 */
public class Const {

    public static final int INT_CONST_ONE = 1;
    public static final int INT_CONST_ZERO = 0;
    public static final Long LONG_OBJECT_ZERO = 0L;
    public static final Long LONG_OBJECT_ONE = 1L;
    public static final Long LONG_OBJECT_TWO = 2L;
    public static final String STRING_CONST_ZERO = "0";
    public static final String STRING_CONST_ONE = "1";
    public static final String STRING_CONST_TWO = "2";
    public static final String STRING_CONST_THREE = "3";
    public static final int AMOUNT_DAY_TO_CHANGE_HANDSET_DEFAULT = 3;//so ngay duoc phep doi thiet bi

    public static final String SYSTEM_SALE = "SALE";
    public static final String REQUEST_CODE = "RC101";
    public final static String STATUS_ACTIVE = "1";
    public final static String STATUS_INACTIVE = "0";
    public final static String STATUS_DELETE = "2";
    public static final String VT_SHOP_ID = "7282";
    public static final Long L_VT_SHOP_ID = 7282L;
    public static final String SIGN_VOFFICE = "2";
    public static final Long FUNC_CHANEL_TYPE_ID = 8L;//Kho chuc nang
    public static final int FILE_MAX = 5 * 1024 * 1024;
    public static final long MONTH_IN_MILLISECOND = 30l * 24 * 60 * 60 * 1000;
    public static final long DAY_MAX_CONTRACT = 180;
    protected static List EXTENSIONS = new ArrayList<>();
    public final static String TELCO_FOR_NUMBER = "TELCO_FOR_NUMBER";
    public final static int MAX_LENGTH_FILE_UPLOAD = 100;
    public final static String PROCESS_OFFLINE = "1";
    public final static int PROCESS_OFFLINE_SIZE = 100;
    public final static String VOFFICE_SIGNED_STATUS = "1,2,3";
    public final static int MAX_SIZE_ROW_UPLOAD = 10000;
    public final static int MAX_SIZE_ROW_DISTRIBUTE = 65000;
    public static final Long NEW = 1L;
    public static final Long LOCKED = 5L;
    public static final Long SUSPEND = 3L;

    public static final Long OWNER_CUST_ID = 0L;
    public static final Long OWNER_TYPE_STAFF = 2L;
    public static final Long OWNER_TYPE_CUST = 3L;
    public static final Long OWNER_TYPE_REVOKE = 6L;
    public static final String IMPORT = "1";
    public static final String TRANS_IMPORT = "6";
    public static final String ACC_TYPE_TROUBLE = "2";
    public static final Long STATE_OLD = 2L; //Hang cu
    public static final Long STATE_DAMAGE = 3L; //Hang hong
    public static final Long STATE_EXPIRE = 4L; //Hang qua han
    public static final Long STATE_GOOD = 12L; //Hang dat chuan
    public static final Long REASON_EXP_SUPPLIES = 4548L;

    public static final String HLR_STATUS_DEFAULT = "1";
    public static final String HLR_STATUS_REG = "2";
    public static final String AUC_STATUS_DEFAULT = "0";
    public static final String AUC_STATUS_REG = "1";
    public static final String COMMA_SEPARATE = ",";
    public static final Long DEFAULT_BATCH_SIZE = 10000L;
    public static final Long BATCH_SIZE_100000 = 100000L;
    public static final Long BATCH_SIZE_1000 = 1000L;
    public static final Long STOCK_CARD_STRIP = 1L;
    public static final Long ENABLE_UPDATE_BCCS1 = 1L;
    public static final String SERIAL_STATUS = "1";
    public static final String SERIAL_STATUS_NOT_DIVIDE = "0";
    public static final String FORMAT_EMAIL = "@viettel.com.vn";
    public static final Long OTHER_PARTNER_ID = 481L;
    public static final Long TYPE_OF_CHANGE_2G_TO_3G = 3L;

    public static final Long FIND_SERIAL = 1L;//stock_Trans_voffice.find_serial = 1

    public final static String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public final static String regexIpv4 = "^([01]?[0-9]?[0-9]|2[0-4][0-9]|25[0-5])\\.([01]?[0-9]?[0-9]|2[0-4][0-9]|25[0-5])\\.([01]?[0-9]?[0-9]|2[0-4][0-9]|25[0-5])\\.([01]?[0-9]?[0-9]|2[0-4][0-9]|25[0-5])$";
    public final static String regexIpv6 = "^((?:[0-9A-Fa-f]{1,4}))((?::[0-9A-Fa-f]{1,4}))*::((?:[0-9A-Fa-f]{1,4}))((?::[0-9A-Fa-f]{1,4}))*|((?:[0-9A-Fa-f]{1,4}))((?::[0-9A-Fa-f]{1,4})){7}$";

    static {
        EXTENSIONS = new ArrayList<>();
        EXTENSIONS.add(".docx");
        EXTENSIONS.add(".doc");
        EXTENSIONS.add(".xls");
        EXTENSIONS.add(".xlsx");
        EXTENSIONS.add(".pdf");
        EXTENSIONS.add(".png");
        EXTENSIONS.add(".jpg");
        EXTENSIONS.add(".jpeg");
        EXTENSIONS.add(".bpm");
    }

    public final static class STOCK_DELIVER {
        public final static Long STOCK_DELIVER_STATUS_NEW = 0L;
        public final static Long STOCK_DELIVER_STATUS_SIGN = 1L;
        public final static Long STOCK_DELIVER_STATUS_REJECT = 2L;

    }

    public final class DEPOSIT_STATUS {

        public final static String DEPOSIT_HAVE = "1";
        public final static String DEPOSIT_NOT_HAVE = "0";
    }

    public static final class REVOKE_NUMBER {
        public static final String REVOKE_SHOP_MOBILE = "REVOKE_SHOP_MOBILE";
        public static final String REVOKE_SHOP_MOBILE_REUSE = "REVOKE_SHOP_MOBILE_REUSE";
        public static final String DAY_MOBILE_REUSE = "DAY_MOBILE_REUSE";
        public static final String DAY_REVOKE_OWNER = "DAY_REVOKE_OWNER";
        public static final String SHOP_REVOKE_OWNER = "SHOP_REVOKE_OWNER";
        public static final String TIME_LOCK_NUMBER = "TIME_LOCK_NUMBER";
        public static final String TIME_LOCK_NORMAL_NUMBER = "TIME_LOCK_NORMAL_NUMBER";
        public static final String STOCK_LOCK_NUMBER = "STOCK_LOCK_NUMBER";
        public static final String STOCK_LOCK_NORMAL_NUMBER = "STOCK_LOCK_NORMAL_NUMBER";
        public static final String TIME_LOCK_NICE_NUMBER = "TIME_LOCK_NICE_NUMBER";
        public static final String STOCK_LOCK_NICE_NUMBER_10 = "STOCK_LOCK_NICE_NUMBER_10";
        public static final String STOCK_LOCK_NICE_NUMBER_11 = "STOCK_LOCK_NICE_NUMBER_11";
    }


    public static final Long TD_PARTNER_ID = 201L;
    public static final String STOCK_BALANCE_PREFIX_TEMPLATE = "STOCK_BALANCE";
    public static final String DOA_TRANSFER_PREFIX_TEMPLATE = "DOA_TRANSFER";
    public static final String GOODS_REVOKE_PREFIX_TEMPLATE = "GOODS_REVOKE";
    public static final String STOCK_DELIVER_PREFIX_TEMPLATE = "STOCK_DELIVER";
    public static final String STOCK_DEVICE_TEMPLATE = "DEVICE_TRANSFER";
    public static final String DEBIT_REQUEST_PREFIX_TEMPLATE = "DEBIT_REQUEST";
    public static final String HANDLE_PREFIX_TEMPLATE = "HANDLE_PREFIX_TEMPLATE";

    public final class PERSISTENT_UNIT {
        public final static String BCCS_IM = "BCCS_IM";// BCCS_IM tam thoi de la sale
        public final static String BCCS_IM_LIB = "BCCS_IM_LIB"; // DB BCCS_IM cu
        public final static String ANYPAY = "PUAnypayLib";//PUAnypayLib
        public final static String BCCS_SALE = "BCCS_SALE";//DB sale
    }

    public final class STATUS {
        public final static String ACTIVE = "1";
        public final static String NO_ACTIVE = "0";
        public final static String SUCCESS = "3";
    }

    public final class VOFFICE_DOA {
        public final static String SIGN = "1";
        public final static String CANCEL = "2";
        public final static String FINISH = "5";
        public final static String DAY_VOFFICE_DOA = "DAY_VOFFICE_DOA";
        public final static String DAY_VOFFICE_DELIVER = "DAY_VOFFICE_DELIVER";
        public final static String DAY_VOFFICE_DEVICE = "DAY_VOFFICE_DEVICE";
        public final static String DAY_VOFFICE_DEBIT = "DAY_VOFFICE_DEBIT";
        public static final String STOCK_DOA_FILE_NAME = "FILE_TRINH_KY_CHUYEN_HANG_DOA.pdf";
        public static final String GOOD_REVOKE_FILE_NAME = "FILE_TRINH_KY_DIEU_CHUYEN_HANG_THU_HOI.pdf";
        public static final String STOCK_DELIVER_FILE_NAME = "FILE_TRINH_KY_BAN_GIAO_KHO.pdf";
        public static final String STOCK_DEVICE_FILE_NAME = "FILE_TRINH_KY_PHAN_RA_THIET_BI.pdf";
        public static final String DEBIT_REQUEST_FILE_NAME = "FILE_TRINH_KY_LAP_YC_HAN_MUC.pdf";

    }

    public final class STAMP {
        public final static String STAMP_STOCK_MODEL_CODE = "STAMP_STOCK_MODEL_CODE";
    }

    public final class STOCK_REQUEST_DOA {
        public final static String APPROVE = "1";
        public final static String CANCEL = "2";
    }

    public final class STOCK_REQUEST_DEVICE {
        public final static String APPROVE = "3";
        public final static String CANCEL = "4";
    }

    public final class STOCK_REQUEST_ORDER {
        public final static String STATUS_0_CREATE_REQUEST = "0";//lap yeu cau
        public final static String STATUS_1_APPROVE = "1";//duyet yeu cau
        public final static String STATUS_2_REJECT = "2";//tu choi
        public final static String STATUS_3_CANCEL = "3";//huy
        public final static String ORDER_TYPE_1 = "1";
    }

    public final class REGEX {
        public final static String SERIAL_REGEX = "^[A-Za-z0-9]+$";
        public final static String NUMBER_REGEX = "^[0-9]{0,}$";
        public final static String IP_REGEX = "^[0-9.:/]+$";
        public final static String Illegals = "[^A-Za-z0-9]";
        public final static String CODE_REGEX = "^[A-Za-z0-9_]+$";
        public final static String NUMBER_REGEX_INTEGER = "^-?\\d+$";
    }

    public static final class REVOKE_KIT {
        public final static Long VERIFY_STATUS0 = 0L;//chua kiem tra
        public final static Long VERIFY_STATUS1 = 1L;//Kiem tra thanh cong
        public final static Long VERIFY_STATUS2 = 2L;//Kiem tra that bai
        public final static Long REVOKE_STATUS0 = 0L;//Trang thai thu kit thanh cong
        public final static Long REVOKE_STATUS1 = 1L;//Trang thai thu kit thanh cong
        public final static Long REVOKE_STATUS2 = 2L;//Trang thai thu kit that bai
    }

    public final static class SORT_ORDER {
        public final static String ASC = "ASC";
    }

    public final static class TELECOM_SERVICE {
        public final static Long ISDN = 1L;
        public final static Long HOMEPHONE = 2L;
        public final static Long PSTN = 3L;
    }

    public final static class NUMBER_SEARCH {
        public final static Long NUMBER_ROW = 100L;
        public final static Long REPAIR_NUM_ROW = 500L;
        public final static Long NUMBER_ALL = 1000L;
        public final static Long LIMIT_ALL = 10000L;
        public final static String STOCK_TYPE_MOBILE = "1";
        public final static String STOCK_TYPE_HP = "2";
        public final static Long NUMBER_ROW_NGN = 20L;
        public final static Long NUMBER_ROW_TRUNK = 20L;
    }

    public static enum SOLR_CORE {
        LOCATION("dbloc"), SHOP("dbshop"), VSHOPSTAFF("dbshopstaff"), PRODUCTOFFERING("dbproductoffer"), PRODUCTOFFERING_OFFERTYPE("dbproductoffering"), ISDN_MOBILE("dbisdnmobile"), ISDN_HOMEPHONE("dbisdnhomephone"),
        ISDN_PSTN("dbisdnpstn"), OFFERWARRANTY("dbofferwarranty"), ISDN_MYVIETTEL("dbisdnmyviettel"), DB_SIM("dbsim"), DB_KIT("dbkit"), DB_CARD("dbcard");

        private String selectEndpoint;

        SOLR_CORE(String selectEndpoint) {
            this.selectEndpoint = selectEndpoint;
        }

        public String getSelectEndpoint() {
            return selectEndpoint;
        }
    }

    public static class MODE_SERIAL {
        public static final String MODE_ADD_ALL = "1";//add so luong va serial
        public static final String MODE_ADD_SERIAL = "2";//add serial
        public static final String MODE_VIEW = "3";//chi view serial
        public static final String MODE_NO_SERIAL = "4";//ko hien thi column serial
    }

    public final static class SHOP {
        public static final String STATUS_ACTIVE = "1";
        public static final String STATUS_INACTIVE = "0";
        public static final Long SHOP_VTT_ID = 7282L;
        public static final Long SHOP_PARENT_VTT_ID = 7281L;
        public static final Integer LIMIT_QUERY = 10;
    }

    public static final class UPDATE_SERIAL_GPON {
        public static final String TYPE_GPON = "1";
        public static final String TYPE_MULTI = "2";
    }

    public static final class UPDATE_PINCODE_CARD {
        public static final String TYPE_NEW = "1";
        public static final String TYPE_OLD = "2";
        public static final String STYLE_FILE_EXCEL = "2";
        public static final String STYLE_FILE_TXT = "3";
        public static final String STYLE_SINGLE = "1";
        public static final int LENGTH_SERIAL_11 = 24;
        public static final int LENGTH_SERIAL_13 = 28;
        public static final String ACTION_TYPE_ADD_PINCODE = "ADD_PINCODE_ACTION";
        public static final String ACTION_TYPE_UPDATE_PINCODE = "UPDATE_PINCODE_ACTION";
    }

    public static final class SHOP_PINCODE {
        public static final String SHOP_PINCODE = "SHOP_PINCODE";
    }

    public static final class STATE_STATUS {
        public static final Long NEW = 1L;
        public static final Long LOCK = 6L;
        public static final Long SALE = 0L;
        public static final Long DAMAGE = 3L;
        public static final Long RENT = 8L;
        public static final Long DEPOSIT = 9L;
        public static final Long RETRIEVE = 4L;
        public static final Long STATE_DOA = 9L;
    }

    public final static class STOCK_TYPE {
        public static final String STOCK_HANDSET_NAME = "STOCK_HANDSET";
        public static final String STOCK_CARD_NAME = "STOCK_CARD";
        public static final String STOCK_NUMBER_NAME = "STOCK_NUMBER";
        public static final String STOCK_KIT_NAME = "STOCK_KIT";
        public static final String STOCK_SIM_NAME = "STOCK_SIM";
        public static final String STOCK_ACCESSORIES_NAME = "STOCK_ACCESSORIES";
        public static final Long STORE = 1L;
        public static final Long STAFF = 2L;

        public static final Long STOCK_ISDN_MOBILE = 1L; //so mobile
        public static final Long STOCK_ISDN_HOMEPHONE = 2L; //so homephone
        public static final Long STOCK_ISDN_PSTN = 3L; //so pstn
        public static final Long STOCK_SIM_PRE_PAID = 4L; //sim tra truoc
        public static final Long STOCK_SIM_POST_PAID = 5L; //sim tra sau
        public static final Long STOCK_CARD = 6L; //the cao
        public static final Long STOCK_HANDSET = 7L; //handset
        public static final Long STOCK_KIT = 8L; //kit
        public static final Long STOCK_ACCESSORIES = 10L; //accessories
        public static final Long STOCK_NO_SERIAL = 11L; //mat hang no serial
    }

    public final static class REVOKE_TRANS {
        public static final String STATUS_CANCEL = "0";
        public static final String STATUS_NORMAL = "1";
    }

    public final class PRODUCT_WS {
        public final static String ENDPOINT_KEY = "productWsInfo";
        public final static String ENDPOINT_KEY_FOR_IM = "productWsForIM";
        public final static String FUNCTION_FIND_SHOP_CODE_VTN = "findByShopCodeVTN";
        public final static String FUNCTION_FIND_SHOP_CODE_VTN_ORIG = "findByShopCodeVTNOrig";
        public final static String FN_GET_PROFILE_BY_PRODUCT_ID = "getProfileByProductOfferId";
        public final static String FUNCTION_FIND_PRICE = "findPriceBySaleProgramShopStaffAndProductOffering";
        public final static String FUNCTION_FIND_TEAMCODE = "getAreaCodeByShopCode";
    }

    public final class SIGN_OFFICE_WS {
        public final static String ENDPOINT_KEY = "signOfficeWsInfo";
        public final static String FUNCTION_SIGN_STATUS_LIST = "getListSignImage";
    }

    public static class SIGN_STATUS_VOFFICE {
        public static final String CHUA_XU_LY = "0";
        public static final String CHO_XU_LY = "1";
        public static final String TU_CHOI = "2";
        public static final String CHO_KI_CHUNG = "3";
        public static final String DA_KI = "4";
        public static final String CHO_KI_NHAY = "5";
    }

    public final class OPTION_SET {
        public final static String TYPE_DEBIT = "TYPE_DEBIT";
        public final static String DEBIT_DAY_TYPE = "DEBIT_DAY_TYPE";
        public final static String DEBIT_LEVEL = "DEBIT_LEVEL";
        public final static String FINANCE_TYPE = "FINANCE_TYPE";
        public final static String FINANCE_STAFF_MAX_DAY = "FINANCE_STAFF_MAX_DAY";
        public final static String FINANCE_TYPE_DEFAULT = "FINANCE_TYPE_DEFAULT";
        public final static String PRODUCT_STATUS = "PRODUCT_STATUS";
        public final static String STOCK_TRANS_STATUS_EX = "STOCK_TRANS_STATUS_EX";
        public final static String STOCK_TRANS_STATUS_IM = "STOCK_TRANS_STATUS_IM";
        public final static String STOCK_TRANS_STATUS_CMD = "STOCK_TRANS_STATUS_CMD";
        public final static String STOCK_TRANS_STATUS_NOT = "STOCK_TRANS_STATUS_NOT";
        public final static String LIMIT_AUTOCOMPLETE = "LIMIT_AUTOCOMPLETE";
        public final static String LIMIT_SHOW_PRODUCT_OFFER = "LIMIT_SHOW_PRODUCT_OFFER";
        public final static String AMOUNT_DAY_TO_EXPORT_STOCK = "AMOUNT_DAY_TO_EXPORT_STOCK";
        public static final String PRODUCT_OFFER_UNIT = "PRODUCT_OFFER_UNIT";
        public static final String REGION_SHOP = "REGION_SHOP";
        public static final String STOCK_TRANS_STATUS = "STOCK_TRANS_STATUS";
        public static final String STOCK_TRANS_STATUS_AGENT = "STOCK_TRANS_STATUS_AGENT";
        public static final String STOCK_STATE = "STOCK_STATE";
        public static final String STOCK_MODEL_UNIT = "STOCK_MODEL_UNIT";
        public static final String BROKEN = "3";
        public static final String ALWAYS_WARRANTY = "ALWAYS_WARRANTY";
        public static final String MAX_ROW_SERIAL = "MAX_ROW_SERIAL";
        public static final String MAX_ROW_REVOKE_KIT = "MAX_ROW_REVOKE_KIT";
        public static final String MAX_ROW_REVOKE_KIT_SEARCH = "MAX_ROW_REVOKE_KIT_SEARCH";
        public static final String STOCK_TRANS_DEPOSIT_STATUS = "STOCK_TRANS_DEPOSIT_STATUS";
        public static final String AGENT_RETRIEVE_EXPENSE_STATUS = "AGENT_RETRIEVE_EXPENSE_STATUS";
        public static final String STOCK_TRANS_AGENT_EXPORT_STATUS = "STOCK_TRANS_AGENT_EXPORT_STATUS";
        public static final String MIN_QUANTITY_EXPORT_OFFLINE = "MIN_QUANTITY_EXPORT_OFFLINE";
        public static final String CURRENCY_TYPE = "CURRENCY_TYPE";
        public static final String ISDN_VTT = "ISDN_VTT";
        public static final String ISDN_LOOKUP_TYPE = "ISDN_LOOKUP_TYPE";
        public static final String ISDN_LOOKUP = "ISDN_LOOKUP";
        public static final String LOOK_UP_ISND_ROLE_TYPE = "LOOK_UP_ISDN_ROLE_TYPE";
        public static final String CAPTCHA_CHANNEL_OF_SHOP = "CAPTCHA_CHANNEL_OF_SHOP";
        public static final String CAPTCHA_CHANNEL_OF_STAFF = "CAPTCHA_CHANNEL_OF_STAFF";
        public static final String CAPTCHA_FOR_SHOP = "CAPTCHA_FOR_SHOP";
        public static final String CAPTCHA_FOR_STAFF = "CAPTCHA_FOR_STAFF";
        public static final String LOOK_UP_SPECIAL_STOCK = "LOOK_UP_SPECIAL_STOCK";
        public static final String NUM_ROWS_IMPORT_APN_AND_APNIP = "NUM_ROWS_IMPORT_APN_AND_APNIP";
        public static final String LOGISTIC_SHOP_ID_LIST = "LOGISTIC_SHOP_ID_LIST";
        public static final String CHECK_STOCK_CARD_VC = "CHECK_STOCK_CARD_VC";
        public static final String STOCK_MODEL_CARD_DATA = "STOCK_MODEL_CARD_DATA";// Mat hang the cao data
        public static final String CARD_CHANGE_REASON_GROUP = "CARD_CHANGE_REASON_GROUP";// Ly do doi the cao hong
        public static final String LOOKUP_SIZE_IMP = "LOOKUP_SIZE_IMP";// gioi han size cua file upload tra cuu serial theo file
        public static final String CHANGE_DAMAGE_PRODUCT_OFFER_TYPE = "CHANGE_DAMAGE_PRODUCT_OFFER_TYPE";
        public static final String DEFAULT_DAY_TYPE_FOR_MAX_DEBIT_SHOP = "DEFAULT_DAY_TYPE_FOR_MAX_DEBIT_SHOP";
        public static final String DEFAULT_DAY_TYPE_FOR_MAX_DEBIT_AGENT = "DEFAULT_DAY_TYPE_FOR_MAX_DEBIT_AGENT"; // Han muc mac dinh cho dai ly
        public static final String DEFAULT_DAY_TYPE_FOR_MAX_DEBIT = "DEFAULT_DAY_TYPE_FOR_MAX_DEBIT"; // Han muc mac dinh nv
        public static final String DEFAULT_DAY_TYPE_FOR_MAX_DEBIT_COLLABORATOR = "DEFAULT_DAY_TYPE_FOR_MAX_DEBIT_COLLABORATOR"; // han muc mac dinh cho CTV
        public static final String EMPTY_SIM_TYPE = "SIM_MODEL_TYPE";
        public static final String IMPORT_TYPE_PARTNER = "IMPORT_TYPE_PARTNER";
        public static final String CTV_STOCK_ISDN_MBCCS = "CTV_STOCK_ISDN_MBCCS";
        public static final String SHOP_CODE_LOOKUP_ISDN = "SHOP_CODE_LOOKUP_ISDN";
        public static final String MAX_ROW_IMPORT_TYPE = "MAX_ROW_IMPORT_TYPE";
        public static final String DAY_STOCK_CYCLE = "DAY_STOCK_CYCLE";
        public static final String STOCK_OWNER_NOT_PROCESS = "STOCK_SHOP_NOT_PROCESS";
        public static final String AMOUNT_DAY_TO_CHANGE_HANDSET = "AMOUNT_DAY_TO_CHANGE_HANDSET";
        public static final String STOCK_LOCK_NUMBER = "STOCK_LOCK_NUMBER";
        public static final String QUANTITY_LOCK_NUMBER = "QUANTITY_LOCK_NUMBER";
        public static final String STOCK_LOCK_NORMAL_NUMBER = "STOCK_LOCK_NORMAL_NUMBER";
        public static final String QUANTITY_LOCK_NORMAL_NUMBER = "QUANTITY_LOCK_NORMAL_NUMBER";
        public static final String PROVINCE_ORDER = "PROVINCE_ORDER";
        public static final String STOCK_LOCK_NICE_NUMBER_10 = "STOCK_LOCK_NICE_NUMBER_10";
        public static final String STOCK_LOCK_NICE_NUMBER_11 = "STOCK_LOCK_NICE_NUMBER_11";
        public static final String STOCK_LOCK_QUANTITY_NICE_NUMBER = "STOCK_LOCK_QUANTITY_NICE_NUMBER";
        public static final String GOODS_STATE = "GOODS_STATE";
        public static final String THRESHOLD_EXP_SERIAL = "THRESHOLD_EXP_SERIAL";
        public static final String THRESHOLD_VT = "THRESHOLD_VT";
        public static final String THRESHOLD_BRANCH = "THRESHOLD_BRANCH";
        public static final String VT_TTBH_HANGHONG = "VT_TTBH_HANGHONG";
        public static final String MASTERIAL_STOCK = "MASTERIAL_STOCK";
        public static final String WARRANTY_OFFER_STATUS = "WARRANTY_OFFER_STATUS";
    }

    public final static class OWNER_TYPE {
        public final static String SHOP = "1";
        public final static String STAFF = "2";
        public final static String CUSTOMER = "3";
        public final static String PARTNER = "4";
        public final static Long SHOP_LONG = 1L;
        public final static Long STAFF_LONG = 2L;
        public final static Long CUSTOMER_LONG = 3L;
        public final static Long PARTNER_LONG = 4L;
    }

    public final class STOCK_LIMIT_PATH {
        public final static String TEMPLATE_PATH = "/u01/app/binhnt22/bccs2_inventory_8800/webapps/BCCS_IM/resources/";
        public final static String TEMPLATE_FOLDER = "/templates/";
        public final static String OUTPUT_FOLDER = "/report_out/";

    }

    public final class INVOICE_TYPE {
        public final static String TYPE_PRINT = "1";
        public final static String TYPE_INPRINT = "2";
        public final static String STATUS_ACTIVE = "1";
        public final static String STATUS_INACTIVE = "0";
        public final static String TYPE = "INVOICE_FORM";
        public final static String INVOICE_TYPE_SALE = "1";
        public final static String INVOICE_TYPE_PAYMENT = "2";
        public final static String INVOICE_TYPE_OTHER = "2";
        public final static String ACTION_LOG_INVOICE_LIST_ADD = "1";
        public final static String ACTION_LOG_INVOICE_LIST_UPDATE = "2";
        public final static String TEMPLATE_LOG_ADD_NEW = "1";
        public final static String TEMPLATE_LOG_ASSIGN = "2";
        public final static String TEMPLATE_LOG_REVEIVE = "4";
        public final static String TEMPLATE_LOG_RETRIEVE = "6";
        public final static String TEMPLATE_LOG_BE_RETRIEVE = "5";
        public final static String TEMPLATE_TYPE_ADD_NEW = "1";
        public final static String TEMPLATE_TYPE_ASSIGN = "2";


    }

    public final static class INVOICE_SERIAL {
        public final static String INVOICE_TYPE = "INVOICE_TYPE";
        public final static Long INVOICETRANS_NOSTOCK = 2L;
        public final static Long INVOICETRANS_NORMAL = 1L;
    }

    public final class BRAS {
        public final static String EQUIPMENT_TYPE = "2";
        public final static String BRAS_IPPOOL_STATUS = "BRAS_IPPOOL_STATUS";
        public final static String BRAS_IPPOOL_TYPE_SPECIFIC = "BRAS_IPPOOL_TYPE_SPECIFIC";
        public final static String SHOP_TYPE = "1";
    }

    public final class DEBIT_REQUEST_STATUS {
        public static final String STATUS_REJECT = "0";
        public static final String STATUS_CREATED = "1";
        public static final String STATUS_APPROVED = "2";
        public static final String STATUS_APPROVED_UNIT = "3";
    }

    public final class RESULT_CODE {
        public static final int EXCEPTION = 3;
        public static final int CONSTAINT = 2;
        public static final int SUCCESS = 1;
    }

    public final class AGENT_APPROVE_SALE {
        public static final String APPROVE_SALE_STATUS = "APPROVE_SALE_STATUS";

    }

    public static final class PRODUCT_OFFER_TYPE {
        public static final Long MOBILE = 1L;
        public static final Long HP = 2L;
        public static final Long PSTN = 3L;
        public static final Long PHONE = 7L;
        public static final Long ACCESSORIES = 10L;
        public static final Long KIT = 8L;
        public static final Long EMPTY_SIM = 4L;
        public static final Long HOMEPHONE = 5L;
        public static final Long CHECK_EXP = 1L;//kho hang hoa
        public static final Long CHECK_EXP_ISDN = 0L;//kho so
        //Start xuat kho cho dai ly Hungpm6
        public static final Long CARD = 6L;
        //End xuat kho cho dai ly Hungpm6
        public static final Long PRODUCT_NO_SERIAL = 11L;
        public static final String NO_SERIAL = "NO_SERIAL";
        public static final String SERIAL = "SERIAL";
    }

    public static final class STOCK_TRANS_STATUS {
        public static final String PROCESSING = "0";
        public static final String EXPORT_ORDER = "1";
        public static final String EXPORT_NOTE = "2";
        public static final String EXPORTED = "3";
        public static final String IMPORT_ORDER = "4";
        public static final String IMPORT_ORDER_PARTNER = "1";
        public static final String IMPORT_NOTE = "5";
        public static final String IMPORTED = "6";
        public static final String NONE_SERIAL = "1,2";
        public static final String DESTROYED = "7";
        public static final String NOT_ACCEPT_IMPORT = "8";
        public static final String CANCEL = "7";
        public static final String REJECT = "8";
        public static final String DESTROY_IM1 = "5";
        public static final String CANCEL_IM1 = "6";
    }

    public static final class STOCK_TRANS_SOURCE_TYPE {
        public static final Long SOURCE_WEB = 1L;
        public static final Long SOURCE_MBCCS = 2L;
        public static final Long SOURCE_WARRANTY = 3L;
    }

    public static final class WARRANTY_TYPE_ACTION {
        public static final String EXPORT = "1";//ham xuat kho
        public static final String IMPORT = "2";//ham nhap kho
        public static final String EXP_IMP_FOR_STAFF = "3";//ham nhap/xuat kho cho nhan vien
        public static final String EXPORT_CHANGE_STATEID = "4";//ham nhap kho co thay doi trang thai stateId
        public static final String IMPORT_GPON = "5";//nhap kho mat hang gpon
    }

    public static final class STOCK_TOTAL {
        public static final Long PLUS_QUANTITY = 1L;
        public static final Long MINUS_QUANTITY = -1L;
        public static final Long PRODUCT_NEW = 1L;
        public static final Long QUANTITY = 0L;
    }

    public static final class STOCK_TRANS {
        public static final String ORDER = "100";
        public static final String ORDER_AND_NOTE = "220";
        public static final String ORDER_AND_NOTE_STAFF = "221";
        public static final String NOTE_ORDER = "110";
        public static final String EXPORT_NOTE = "011";
        public static final String NOTE = "010";
        public static final String EXPORT = "001";
        public static final String ORDER_AGENT = "400";
        public static final String NOTE_AGENT = "040";
        public static final String NOTE_CHANGE_PRODUCT = "201";
        public static final String EXPORT_AGENT = "004";
        public static final String NOTE_AGENT_RETRIEVE = "500";
        public static final String RETRIEVE_EXPENSE = "505";
        public static final String COOLLABORATOR_RETRIEVE = "501";
        public static final String EXPORT_COLLABORATOR = "006";
        public static final String EXPORT_DEMO = "555";
        public static final String PARTNER = "009";
        public static final String PARTNER_BALANCE = "090";
        public static final String CHANGE_PRODUCT_DAMAGE = "012";
        public static final String PRODUCT_EXCHANGE_KIT = "069";
        public static final String IS_TRANFER = "1";
        public static final String IS_LOGISTIC = "1";
        public static final String IS_NOT_ERP = "0";
        public static final Long STOCK_TRANS_TYPE_DEPOSIT = 1L;
        public static final Long STOCK_TRANS_TYPE_RETRIEVE_DEPOSIT = 3L;
        public static final Long STOCK_TRANS_TYPE_EXPENSE = 6L;
        public static final Long IS_TRANSFER = 3L; //isAutoGen=3 : Giao dich xuat dieu chuyen chi nhanh
        public static final Long IS_AUTO_GEN_LOGISTIC = 2L;
        public static final Long IS_AUTO_GEN_LOGISTIC_THREE_REGION = 1L;
        public static final String PAY_STATUS_ORDER = "0";
        public static final String STOCK_TRANS_DEPOSIT = "1";
        public static final String STOCK_TRANS_DEPOSIT_CANCEl = "111";
        public static final String STOCK_TRANS_STATUS_DEPOSIT = "3";
        public static final String STOCK_TRANS_PAY = "2";
        public static final String CANCEL_TRANS = "200";
        public static final String CANCEL_TRANS_AGENT = "210";
        public static final String REJECT_TRANS = "300";
        public static final String ORDER_FROM_STAFF = "ORDER_FROM_STAFF";
        public static final String NOTE_FROM_STAFF = "NOTE_FROM_STAFF";
        public static final String STATUS_OFFLINE = "0";
        public static final Long IMPORT_STOCK_FROM_PARTNER_REASON_ID = 4453L; //ma cua ly do nhap hang tu doi tac (fix cung)
        public static final Long EXPORT_STOCK_FROM_PARTNER_REASON_ID = 4454L; //ma cua ly do nhap hang tu doi tac (fix cung)
        public static final Long IMPORT_STOCK_PROCESS_REASON_ID = 200141L; //ma cua ly do nhap hang tu doi tac (fix cung)
        public static final Long EXPORT_STOCK_PROCESS_REASON_ID = 200140L; //ma cua ly do nhap hang tu doi tac (fix cung)

        //Loai ma giao dich
        public static final String TRANS_CODE_LX = "LX";//Lenh xuat
        public static final String TRANS_CODE_PX = "PX";//Phieu xuat
        public static final String TRANS_CODE_LN = "LN";//Lenh nhap
        public static final String TRANS_CODE_PN = "PN";//Phieu nhap
        public static final String TRANS_CODE_PT = "PT";//Phieu thu
        public static final String TRANS_CODE_PC = "PC";//Phieu chi
        public static final String TRANS_CODE_PARTNER = "PN_FRM_PARTNER";//Phieu nhap
        public static final String TRANS_CODE_DEPOSIT_PT = "VT_PT";//Phieu thu tien dat coc
        public static final Long BANKPLUS_STATUS_NOT_APPROVE = 0L;//0: Chua tao yeu cau duyet sang BankPlus
        public static final Long BANKPLUS_STATUS_PENDING_1 = 1L;//2 hoac khac: Da tao yeu cau thanh toan sang BankPlus nhung chua nhan duoc ket qua
        // OBJECT_TYPE
        public static final String OBJECT_TYPE_AGENT_EXPORT = "AGENT_EXPORT";

    }

    public static final class STOCK_BALANCE {
        public static final String STOCK_BALANCE_TYPE = "STOCK_BALANCE_TYPE";
        public static final Long BALANCE_TYPE_STATUS_CREATE = 0L;
        public static final Long BALANCE_TYPE_EXPORT = 1L;
        public static final Long BALANCE_TYPE_IMPORT = 2L;
        public static final String STOCK_BALANCE_PREFIX_TEMPLATE = "STOCK_BALANCE";
        public static final String STOCK_BALANCE_FILE_NAME = "FILE_TRINH_KY_DE_XUAT_CAN_KHO.pdf";
        public static final String STOCK_BALANCE_SERIAL_FILE_NAME = "PHU+LUC+SERIAL.pdf";
        public static final Long EXPORT = 1L;
    }

    public static final class REASON_TYPE {
        public static final String EXP_STOCK_WARRANTY = "EXP_STOCK_WARRANTY";
        public static final String IMP_STOCK_WARRANTY = "IMP_STOCK_WARRANTY";
        public static final String STOCK_EXP_UNDER = "STOCK_EXP_UNDER";
        public static final String STOCK_IMP_UNDER = "STOCK_IMP_UNDER";
        public static final String DISTRIBUTE_ISDN_TYPE = "DISTRIBUTE_ISDN";
        public static final String DISTRIBUTE_ISDN_AUTO_TYPE = "DISTRIBUTE_ISDN_AUTO";
        public static final String STOCK_EXP_STAFF = "STOCK_EXP_STAFF";
        public static final String STOCK_IMP_STAFF = "STOCK_IMP_STAFF_SHOP";
        public static final String STOCK_EXP_STAFF_SHOP = "STOCK_EXP_STAFF_SHOP";
        public static final String STOCK_EXP_COLL = "STOCK_EXP_COLL";
        public static final String STOCK_IMP_COLL = "STOCK_IMP_CTV";
        public static final String STOCK_IMP_FROM_STAFF = "STOCK_IMP_FROM_STAFF";
        public static final String EXPORT_RESCUE = "EXPORT_RESCUE"; // ly do xuat hang UCTT
        public static final String RETRIEVE_STOCK = "RETRIEVE_STOCK"; //phan biet la thu hoi hang
        public static final String NHDT = "NHDT"; //loai ly do lap lenh doi tac
        public static final String CHANGE_DAMAGED_GOODS = "CHANGE_DAMAGED_GOODS"; //loai ly do lap lenh doi tac
        public static final String DEVICE_TRANSFER = "DEVICE_TRANSFER";// phan ra thiet bi

    }

    public static final class REASON_ID {
        public static final Long XUAT_KHO_CAP_DUOI = 4544L;
        public static final Long XUAT_KHO_NHAN_VIEN = 4547L;
        public static final Long IMPORT_FROM_PARTNER = 200141L; //ma cua ly do nhap hang tu doi tac (fix cung)
        public static final Long IMPORT_PARTNER = 4453L; //ma cua ly do nhap hang tu doi tac (fix cung)
        public static final Long EXPORT_TO_PARTNER = 200140L; //ma cua ly do xuat tra hang doi tac (fix cung)
        public static final Long EXP_SIM_IMP_FROM_PARTNER = 200120L; //ma cua ly do xuat tra hang doi tac (fix cung)
        public static final Long EXP_LIQUIDATE = 2255333L; //ma cua ly do xuat hang thanh ly
        public static final Long STAFF_EXPORT_STOCK_TO_SHOP = 4568L; //Nhan vien xuat tra hang
        public static final Long EXP_BALANCE = 2255334L;//ma cua ly do xuat hang can kho cho doi tac khac
        public static final Long IMP_BALANCE = 2255335L;//ma cua ly do nhap hang can kho cho doi tac khac

    }

    public static final class PRODUCT_OFFERING {
        public static final Long PRODUCT_OFFER_TYPE = 7L;
        public static final Long PARRENT_ID = 100L;
        public static final Long CHECK_EXP_PHONE = 0L;
        public final static Long PRICE_TYPE_ROOT = 23L;
        public final static Long PRICE_POLICY_VT = 1L;
        public final static String EXPORT_RESCUE_INFO_2 = "EXPORT_RESCUE_INFO_2";// Xuat co thu hoi hang cua khach hang
        public final static String _UCTT = "_UCTT";//code mat hang la ung cuu thong tin
        public final static Long CHECK_SERIAL = 1L; //check mat hang co serial khong
        public final static String _CHECK_SERIAL = "1";
        public final static Long NO_SERIAL = 0L; //mat hang no serial khong

    }

    public static final class STOCK_STRANS_DEPOSIT {
        public static final Long REASON_ID = 382L;//thu tien dat coc dai ly
        public static final String HTTT = "10";//TK Bank+
        public static final String DEPOSIT_PAY_METHOD = "DEPOSIT_PAY_METHOD";//TK Bank+
        public static final String DEPOSIT_ORDER = "0";
        public static final String DEPOSIT_NOTE = "1";
        public static final String DEPOSIT_CANCEL = "2";
        public static final String DEPOSIT_RETRIEVE_NOTE = "3";
        public static final String DEPOSIT_RETRIEVE_EXPENSE = "4";
        public static final String DEPOSIT_IMPORT = "5";
        public static final String PAY_ORDER = "0";
        public static final String PAY_TRANS = "1";
        public static final String DEPOSIT_TYPE_EXPENSE = "2";

        // loai thong tin tai khoan bankplus
        public static final Long DEPOSIT_TRANS_TYPE_CREATE = 0L;//Khoi tao tai khoan
        public static final Long DEPOSIT_TRANS_TYPE_CHARGE = 1L;//Nap tien/rut tien cho tai khoan deposit
        public static final Long DEPOSIT_TRANS_TYPE_MINUS_EXPORT = 2L;//Tru tien xuat dat coc/ cong tien thu hoi
        public static final Long DEPOSIT_TRANS_TYPE_ADD_EXPORT = 3L;//Cong tien xuat dat coc/ cong tien thu hoi
        public static final Long DEPOSIT_TRANS_TYPE_ADD_IMPORT = 4L;//tru tien xuat dat coc/ cong tien thu hoi
        public static final Long DEPOSIT_TRANS_TYPE_DESTROY_RECEIPT = 9L;//Huy phieu thu/chi
        public static final Long DEPOSIT_TRANS_TYPE_INVOICE = 8L;//Lap/huy hoa don thay
        public static final Long DEPOSIT_TRANS_TYPE_ADD_PUNISH = 22L;//Cong tien dat coc ban phat
        public static final Long DEPOSIT_TRANS_TYPE_MINUS_PUNISH = 23L;//Tru tien dat coc ban phat
        public static final Long DEPOSIT_TRANS_TYPE_ADD_DAMAGE_GOODS = 29L;//Cong tien dat coc cho dai ly khi doi hang hong
    }

    public enum TEMPLATE {
        SEPARATOR("_"), EXTENSION(".jasper"), ENDPREFIX("2007"), MEXTENSION(".pdf"), PREFIX("bccs2"), DEFAULT("giao_dich");

        TEMPLATE(String val) {
            value = val;
        }

        String value;

        public String toString() {
            return value;
        }
    }

    public static final class STOCK_STRANS_PAY {
        public static final String PAY_ORDER = "0";
        public static final String PAY_NOTE = "1";
    }

    public static final class STOCK_STRANS_DETAIL {
        public static final String STATUS_NEW = "1";
    }

    public static final class STOCK_TRANS_TYPE {
        public static final String EXPORT = "1";//lenh xuat
        public static final String EXPORT_REQUEST = "111";//lenh xuat tu yeu cau
        public static final String IMPORT = "2";//lenh nhap
        public static final String ISDN = "6";// lenh xuat so
        public static final String AGENT = "3";// phieu thu hoi hang tu dai ly
        public static final String AGENT_IMP = "7";// phieu thu hoi hang tu dai ly
        public static final String STAFF_IMP = "13";// nhan vien xac nhan nhap hang
        public static final String STAFF_EXP = "14";// nhan vien xuat tra hang
        public static final String UNIT = "1";
        public static final String DEPOSIT = "1";
        public static final String PAY = "2";
        public static final Long EXPORT_KIT = 12L;// nhan vien xac nhan nhap hang
        public static final Long REVOKE_KIT = 11L;// nhan vien xac nhan nhap hang
    }

    public static final class STOCK_TEMPLATE_PREFIX {
        public static final String IMPORT = "N";
        public static final String EXPORT = "X";
        public static final String COMMAND = "L";
        public static final String NOTE = "P";
    }

    public static final class STOCK_TRANS_ACTION_TYPE {
        public final static String COMMAND = "1";
        public final static String NOTE = "2";
    }

    public static final class STOCK_TRANSPORT_TYPE {
        public final static String YES = "1";
        public final static String NO = "2";
    }

    public final class IM_COMPONENT {
        public static final String STOCK_LIMIT_APPROVE = "BCCS2_IM_QLKHO_HANMUCKHO_QLYEUCAU_DUYET";//duyet yeu cau han muc
        public static final String STOCK_LIMIT_DELETE = "BCCS2_IM_QLKHO_HANMUCKHO_QLYEUCAU_XOA";//xoa/tu choi yeu cau han muc
    }

    public final class PERMISION {
        public static final String ROLE_XUAT_KHO_BA_MIEN = "BCCS2_IM_QLKHO_XUAT_KHO_BA_MIEN";//quyet xuat kho 3 mien
        public static final String ROLE_TRANSPORT_STOCK = "BCCS2_IM_QLKHO_VANCHUYEN";//quyet van chuyen
        public static final String ROLE_SYNC_LOGISTIC = "BCCS2_IM_QLKHO_DONGBO_LOGISTIC";//quyen logistic
        public static final String ROLE_STOCK_NUM_SHOP = "BCCS2_IM_QLKHO_LAPPHIEU_XUATKHO_CAPDUOI";//quyen stock num shop
        public static final String ROLE_DISTRIBUTION_NICE_ISDN = "BCCS2_IM_QLSO_PHANPHOISO_DEP";//quyen stock num shop
        public static final String ROLE_DISTRIBUTION_NORMAL_ISDN = "BCCS2_IM_QLSO_PHANPHOISO_THUONG";//quyen stock num shop
        public static final String ROLE_DISTRIBUTION = "BCCS2_IM_QLSO_PHANPHOISO_TOANBO_KHO";//quyen stock num shop
        public static final String ROLE_LOOK_UP_SPECIAL_STOCK = "BCCS2_IM_QLSO_TRACUU_SODEP";
        public static final String LOOK_UP_ISDN_ROLE_TYPE_KHO = "BCCS2_IM_QLSO_TRACUUKHO_MACDINH";
        public static final String ROLE_LOOK_UP_STOCK_EXT = "BCCS2_IM_QLSO_TRACUU_MORONG";
        public static final String ROLE_SIGN_CA = "BCCS2_IM_QLSO_KY_VO";
        public static final String ROLE_HUY_TRA_PHIEU = "BCCS2_IM_QLKHO_TIENICH_HUY_TRAPHIEU";
        public static final String ROLE_EXPORT_RETURN_WARRANTY = "BCCS2_IM_QLKHO_TRA_BAOHANH";
        public static final String ROLE_RECEIVE_WARRANTY = "BCCS2_IM_QLKHO_NHAN_BAOHANH";
        public static final String ROLE_XUAT_DAT_COC = "ROLE_XUAT_DAT_COC";
        public static final String ROLE_XUAT_BAN_DUT = "ROLE_XUAT_BAN_DUT";
        public static final String ROLE_AUTO_ORDER_NOTE = "BCCS2_IM_QLKHO_LAPPHIEU_TUDONG";//lap phieu tu dong
        public static final String BCCS2_IM_QLSO_TRACUUSO_GIUSO = "BCCS2_IM_QLSO_TRACUUSO_GIUSO";
        public static final String ROLE_DONGBO_ERP = "BCCS2_IM_QLKHO_DONGBO_ERP";
        public static final String BCCS2_IM_QLKHO_THU_HOI_KIT_THU_HOI = "BCCS2_IM_QLKHO_THU_HOI_KIT_THU_HOI";
    }

    public final class PRODUCT_OFFER_TABLE {
        public static final String STOCK_NUMBER = "STOCK_NUMBER";
    }

    public final class PREFIX_TEMPLATE {
        public static final String LX_TTH_CN = "LX_TTH_CN";
        public static final String LX = "LX";
        public static final String PXS = "PXS";
    }

    public final class SERVICE_VOFFICE {
        public final static String CHECK_ACCOUNT = "ser:checkAccount";
    }

    public static final class LOGISTIC {
        public static final Long REQ_TYPE_NEW = 1L;
        public static final Long STATUS_NEW = 0L;
        public static final Long HAVE_CHECK_SERIAL = 1L;
    }

    public final class LOGISTICS {
        public final static String FAILED = "0";
        public final static String SUCCESS = "1";
        public final static String ORDER_TYPE_EXPORT = "2";
        public final static String ORDER_TYPE_IMPORT = "1";


    }

    public final class DELIVERING_STOCK {
        public final static String FAILED = "1";
        public final static String SUCCESS = "0";


    }

    public final class BRAS_IPPOOL {
        public final static String RESOURCE_TEMPLATE_PATH = "/resources/templates/";
        public final static String RESOURCE_OUT_PATH = "/resources/report_out/";
        public final static String BRAS_IPPOOL_INFO = "brasIppoolPattern.xls";
        public final static String BRAS_IPPOOL_SEARCH_EXPORT = "brasIppoolSearch.xls";
        public final static String BRAS_IPPOOL_INFO_DELETE = "brasIppoolForDelete.xls";
        public final static String BRAS_IPPOOL_INFO_EDIT = "brasIppoolForEdit.xls";
        public final static String BRAS_IPPOOL_VIEW_IP = "brasIppooViewIp.xls";
        public final static String IP_REGEX = "^[0-9.:/]+$";
        public final static String BRAS_IPPOOL_ERROR = "bras_ippol_error";
        public final static String XLS = ".xls";
        public final static String BRAS_IPPOOL_ERROR_FILE = "brasIppoolError.xls";
        public final static String BRAS_IPPOOL_VIEW_IP_EXPORT = "brasIppoolViewIpResult.xls";
        public final static String BRAS_IPPOOL_DELETE_ERROR_FILE = "brasIppoolDeleteError.xls";
        public final static String BRAS_IPPOOL_EDIT_ERROR_FILE = "brasIppoolEditError.xls";
        public final static String BRAS_IPPOOL_STATUS_FREE = "1";
        public final static String BRAS_IPPOOL_STATUS_USE = "2";
        public final static String BRAS_IPPOOL_STATUS_LOCK = "3";
    }

    public final class ISDN {
        public static final String NEW_NUMBER = "1";
        public static final String STOP_USE = "3";
        public static final String USING = "2";
        public static final String KITED = "4";
        public static final String LOCK = "5";
    }

    public final class NUMBER_ACTION_TYPE {
        public static final String LOC_SO = "2";
        public static final String DISTRIBUTE = "4";
        public static final String KEEP_ISDN = "9";
    }

    public final class PREFIX_REGION {
        public static final String LX = "LX_";
        public static final String LN = "LN_";
        public static final String PX = "PX_";
        public static final String PN = "PN_";
        public static final String PN_CN = "PN_CN_";
    }

    public static final class STOCK_NUMBER_STATUS {
        public static final String NEW = "1";
        public static final String USING = "2";
        public static final String NOT_USE = "3";
        public static final String KITED = "4";
        public static final String LOCK = "5";
    }

    public static final class STOCK_GOODS {
        public static final Long STATUS_NEW = 1L;
        public static final Long STATUS_CONFIRM = 3L;
        public static final Long STATUS_SALE = 0L;
        public static final Long STATUS_LOCK = 4L;
    }

    public static final class REPORT_TEMPLATE {
        public static final String STOCK_TRANS_TEMPLATE_SUB_PATH = "stock_trans_template/";
        public static final String STOCK_TRANS_HANDOVER_REPORT = "BBBGCT";
        public static final String SERIAL_DETAIL_TEMPLATE = "stock_trans_template/detail_serial.xls";
        public static final String PX_ISDN_TEMPLATE = "stock_trans_template/PX_ISDN.xls";
        public static final String DETAIL_LX_ISDN_TEMPLATE = "stock_trans_template/detail_LX_ISDN.xls";
        public static final String INPUT_ISDN_TEMPLATE = "stock_trans_template/INPUT_ISDN.xls";
        public static final String CREATE_EXP_CMD_ISDN_FROM_FILE_PATTERN_TEMPLATE = "stock_trans_template/createExpCmdIsdnFromFilePattern.xls";
        public static final String CREATE_EXP_CMD_FROM_FILE_PATTERN_TEMPLATE = "stock_trans_template/createExpCmdFromFilePattern.xls";
        public static final String CREATE_EXP_STAFF_FROM_FILE_PATTERN_TEMPLATE = "stock_trans_template/createExpStaffFromFilePattern.xls";
        public static final String ERR_ISDN_FROM_FILE_TEMPLATE = "stock_trans_template/ERR_ISDN_FROM_FILE_TEMPLATE.xls";
        public static final String ERR_FROM_FILE_TEMPLATE_CREATE_ORDER = "stock_trans_template/createExpCmdFromFileErr.xls";
        public static final String ERR_FROM_FILE_TEMPLATE_CREATE_ORDER_STOCK = "stock_trans_template/createExpCmdUnderlyingFromFilePatternErr.xls";
        public static final String ERR_FROM_FILE_TEMPLATE_REVOKE_KIT = "template_revoke_kit.xls";
        public static final String ERR_FROM_FILE_TEMPLATE_SEARCH_REVOKE_KIT = "template_revoke_search_kit.xls";
        public static final String ERR_FROM_FILE_TEMPLATE_REVOKE_KIT_OUTPUT = "template_revoke_kit_ouput.xls";
        public static final String ERR_FROM_FILE_TEMPLATE_REVOKE_KIT_EXPORT = "template_revoke_kit_export.xls";
        public static final String ERR_FROM_FILE_TEMPLATE_REVOKE_KIT_ERR = "template_revoke_kit_ouput.xls";
        public static final String ERR_FROM_FILE_TEMPLATE_REVOKE_SEARCH_KIT_ERR = "template_revoke_kit_search_ouput.xls";
        public static final String FROM_FILE_TEMPLATE_CREATE_ORDER_STOCK = "stock_trans_template/createExpCmdUnderlyingFromFilePattern.xls";
        public static final String RESULT_FROM_FILE_TEMPLATE_CREATE_ORDER = "stock_trans_template/createExpCmdFromFileResult.xls";
        public static final String IMPORT_SERIAL_BY_PROD_NAME = "mau_file_nhap_serial_nhieu_mat_hang.xls";
        public static final String IMPORT_SERIAL = "import_serial/mau_file_nhap_serial.xls";
        public static final String SEARCH_SERIAL = "import_serial/LookupSerialByFile_List.xls";
        public static final String SEARCH_SERIAL_EXPORT = "import_serial/LookupSerialByFile_List_Export.xls";
        public static final String SEARCH_SERIAL_ERROR = "import_serial/LookupSerialByFile_List_Error.xls";
        public static final String IMPORT_SERIAL_FILENAME = "mau_file_nhap_serial.xls";
        public static final String IMPORT_SERIAL_FORDER = "import_serial/";
        public static final String IMPORT_SERIAL_ERROR = "import_serial/mau_file_nhap_serial_loi.xls";
        public static final String PRINT_LIST_TRANS = "stock_trans_template/list_trans_template.xls";
        public static final String PRINT_LIST_TRANS_DETAIL = "stock_trans_template/list_trans_detail_template.xls";
        public static final String IMPORT_LIST_PRODUCT = "import_serial/mau_file_nhap_serial_nhieu_mat_hang.xls";
        public static final String IMPORT_LIST_PRODUCT_ERROR = "import_serial/mau_file_loi_nhap_serial_nhieu_mat_hang.xls";
        public static final String EXPORT_TRANS_DETAIL_TEMPLATE = "template_import_stock_trans_detail.xls";
        public static final String DIVIDE_SERIAL = "divide_serial/divide_serial.xls";
        public static final String DIVIDE_SERIAL_RESULT = "divide_serial/divide_serial_result.xls";
        public static final String DIVIDE_SERIAL_ERROR = "divide_serial/divide_serial_error.xls";
        public static final String PX_CN = "PX_CN.xls";
        public static final String STOCK_BALANCE_ATTACH = "jasper/TK_STOCK_BALANCE_SERIAL_ATTACH.jasper";
        public static final String STOCK_BALANCE_REPORT = "jasper/TK_STOCK_BALANCE_REQUEST.jasper";
        public static final String DOA_TRANSFER = "TK_DOA_TRANSFER.jasper";
        public static final String GOOD_REVOKE = "GOOD_REVOKE.jasper";
        public static final String STOCK_DELIVER = "STOCK_DELIVER.jasper";
        public static final String STOCK_DEVICE = "STOCK_DEVICE.jasper";
        public static final String IMPORT_DIVIDE_DEVICE = "template_import_divide_divice.xls";
        public static final String IMPORT_DEVICE_TEMPLATE = "IMPORT_DEVICE_TEMPLATE.xls";
        public static final String DEBIT_REQUEST = "DEBIT_REQUEST.jasper";
    }

    public static final class PRINT_TYPE {
        public static final String PRINT_BY_FILE = "1";
        public static final String PRINT_BY_INPUT = "2";
    }

    public static final class EXPORT_FILE_NAME {
        public static final String IMPORT_PARTNER_SERIAL_ERROR = "danh_sach_dong_loi.txt";
        public static final String LIST_SERIAL_NAME = "danh_sach_serial.txt";
        public static final String TEMPLATE_SERIAL_TXT = "mau_template_import.txt";
        public static final String LIST_NOTE = "list_note.xls";
        public static final String DETAIL_SERIAL_ISDN = "detail_serial_isdn.xls";
        public static final String PX_ISDN = "PX_ISDN.xls";
        public static final String ERR_FIELD_ISDN = "ERR_FIELD_ISDN.xls";
    }

    public static final class REASON_CODE {
        public static final String IMPORT_RESCUE = "IMPORT_RESCUE";
        public static final String STOCK_IMP_SERNIOR = "STOCK_IMP_SERNIOR";
        public static final String STOCK_EXP_SERNIOR = "STOCK_EXP_SERNIOR";
        public static final String STOCK_EXP_RECOVER = "STOCK_EXP_RECOVER";
        public static final String IMP_UNDER_REJECT = "IMP_UNDER_REJECT";
        public static final String EXP_TO_COL = "BHDCCTV";
        public static final String EXP_WARRANTY_INFO = "EXP_WARRANTY_INFO";
        public static final String IMP_WARRANTY_INFO = "IMP_WARRANTY_INFO";
        public static final String CDMH = "CDMH";
        public static final String CDKIT = "CDKIT";
        public static final String REASON_GOODS_REVOKE = "REASON_GOODS_REVOKE";
        public static final String EXP_MATERIAL_DEPLOY = "EXP_MATERIAL_DEPLOY";
        public static final String EXP_MATERIAL_GIFT = "EXP_MATERIAL_GIFT";
        public static final String DEVICE_TRANSFER = "DEVICE_TRANSFER";
    }

    public static final class STOCK_TRANS_ACTION {
        public final static String PN0000 = "PN0000";
    }

    public final static class VT_UNIT {
        public final static String VT = "1";
        public final static String AGENT = "2";
    }

    public final static class CHANNEL_TYPE {
        public final static String OBJECT_TYPE_SHOP = "1";
        public final static String OBJECT_TYPE_STAFF = "2";
        public final static Long CHANNEL_TYPE_COLLABORATOR = 10L; // NVDB/DB
        public static final Long CHANNEL_TYPE_POINT_OF_SALE = 80043L; //id cua kenh DB
        public static final String POINT_OF_SALE_DB = "1"; // DB
        public static final String POINT_OF_SALE_CTV = "2";// CTV
        public static final Long CHANNEL_TYPE_AGENT_VTT = 9L; //Kenh DL VTT
        public static final Long CHANNEL_TYPE_AGENT = 4L; //Kenh DL
        public static final Long CHANNEL_TYPE_BH = 1L;
        public static final Long CHANNEL_TYPE_KCN = 8L; //Kenh DL VTT
        public static final Long CHANNEL_TYPE_TDKT = 22L; //Kenh DL
    }

    public static final class DEPOSIT_TRANS_TYPE {
        public final static Long TYPE_DEPOSIT = 1L;// Dat coc
        public final static Long TYPE_PAY = 2L;// Ban dat
    }

    public static final class PRODUCT_OFFER_PRICE {
        public final static Long PRICE_TYPE_SALE_AGENT = 1L;// Gia ban le
        public final static Long PRICE_TYPE_DEPOSIT = 5L; // Gia dat coc
        public final static Long PRICE_TYPE_PAY = 1L; // Gia ban le
        public final static Long PRICE_TYPE_SERVICE = 2L; // Gia dich vu
        public final static Long PRICE_TYPE_SALE = 102L; // Gia ban dut
    }

    public static final class FROM_STOCK {
        public static final String FROM_SENIOR = "1";
        public static final String FROM_UNDERLYING = "2";
        public static final String FROM_STAFF = "3";
        public static final String STAFF_SHOP = "4";

    }

    public static final class VOFFICE_STATUS {
        public static final String NOT_SIGNED = "0";
        public static final String SIGNED = "1";
        public static final String REJECT = "2";
        public static final String PROCESSING = "3";
        public static final String FAILED = "4";
        public static final String SIGN_ERROR = "-1";
        public static final String VOFFICE_REJECT_1 = "1";
        public static final String VOFFICE_REJECT_2 = "2";
        public static final String VOFFICE_REJECT_4 = "4";
        public static final String VOFFICE_SIGNED = "3";
        public static final String VOFFICE_RELEASED = "5";

    }


    public static final class SALE_WS {
        public static final String ENDPOINTKEY_SALE_AGENT = "externalSaleService";
        public static final String ENDPOINTKEY_SALE_SYSTEM_AGENT = "externalSystemSaleService";
        public static final String SERVICE_SALE_AGENT = "ExternalSaleService";
        public static final String FUNCTION_AGENT_ADD_GOOD = "addGoodAgent";
        public static final String FUNCTION_AGENT_SALE = "saleToAgent";
        //Duyet dai ly
        public static final String SERVICE_APPROVE_AGENT = "ExternalStockOrderAgentService";
        public static final String FUNCTION_APPROVE_REQUEST = "approveRequest";
        public static final String FUNCTION_DENY_REQUEST = "denyRequest";
        public static final String FUNCTION_STOCK_ORDER_AGENT = "getStockOrderAgent";
        public static final String FUNCTION_STOCK_ORDER_AGENT_DETAIL = "getStockOrderAgentDetail";
        public static final String FUNCTION_VIEW_CHANGE_GOOD = "viewChangeGood";
        public static final String FUNCTION_SAVE_CHANGE_GOOD = "changeGood";
        public static final String FUNCTION_COUNT_TOTAL_PRODUCT_TEAMCODE = "countTotalStockModelByTeamCode";
        // Lay ly do
        public static final String ENDPOINTKEY_SALE = "saleReasonService";
        public static final String SERVICE_REASON_SALE_AGENT = "SellReasonService";//SaleReasonService
        public static final String FUNCTION_GET_REASON = "findBySaleTransType";
        // SaleTransDetailService
        public static final String SERVICE_SALE_TRANS_DETAIL = "SaleTransDetailService";
        public static final String FUNCTION_FIND_BY_SALE_TRANS = "findBySaleTransId";
        // AppParam
        public static final String ENDPOINTKEY_APPPARAM_SALE_AGENT = "saleAppParamsService";
        public static final String SERVICE_APPPARAM_SALE_AGENT = "OptionSetValueService";//AppParamsService
        public static final String FUNCTION_GET_PAY_METHOD = "findAgentPayMethod";
        // Sale program
        public static final String ENDPOINTKEY_SALE_PROGRAM = "salesProgram";
        public static final String SERVICE_SALE_PROGRAM = "SaleProgramService";
        public static final String FUNCTION_GET_PROGRAM = "findByFilter";

        //Check ngay nop tien
        public static final String SERVICE_SALE_TRANS = "SaleTransService";
        public static final String FUNCTION_GET_EXPRIRED_DAY = "findTransExpiredPay";
        //sale service

        public static final String FUNCTION_CHECK_DEBIT_STAFF = "checkPaymentDebitForStaff";
        public static final String FUNCTION_SALE_VALIDATE_ACTIVE_SUB = "checkActiveSubscriber";
        public static final String FUNCTION_SALE_CHECK_ACTIVE_SERIAL_SUB = "checkActiveSerial";

        public static final String END_POINT_SALE_INTERFACE = "externalSystem/InterfaceSaleIM";

    }

    public static final class DEBIT_TYPE {
        public static final String DEBIT_STOCK = "1";
        public static final String DEBIT_PAYMENT = "2";
    }

    public static final class CC_WS {
        public static final String ENDPOINTKEY = "customerCareService";
        public static final String SERVICE_HISTORY_SEARCH = "HistorySearchService";
        public static final String FUNCTION_HISTORY_CARD_NUMBER_LOCK = "getHistoryCardNumberLock";
        public static final String FUNCTION_GET_INFOR_CARD_NUMBER = "getInforCardNumber";

        // Lock card
        public static final String ENDPOINTKEY_LOCKCARD = "ccService";
        public static final String SERVICE_BLOCK_CARD = "McScratchBlockService";
        public static final String FUNCTION_LOCK_CARD = "lockCard";
        public static final String REG_TYPE_LOCK_CARD_OCS = "1";
        public static final String RESPONSE_CODE_SUCCESS = "0";
    }

    public static final class KTTS_WS {
        public static final String ENDPOINT_KEY = "kttsService";
        public static final String FUNCTION_SEARCH_CONTRACT = "searchContractBCCS";
        public static final String FUNCTION_SEARCH_CONTRACT_DETAIL = "searchShipmentBCCS";
        public static final String FUNCTION_SEARCH_GET_RESULT_IMP_SHIPMENT = "getResultImpShipment";

    }


    //End Trang thai dat coc hungpm6
    //Start Trang thai ban dut hungpm6
    public static final class PAY_STATUS {
        public static final String NOT_PAY = "0";   // chua thanh toan
        public static final String PAY_HAVE = "1";  // da thanh toan
    }
    //End Trang thai ban dut hungpm6

    //Start Trang thai the cao tren VC hungpm6
    public static final class VC_STATUS {
        public static final String NOT = "0";   // chua co tren he thong vc
        public static final String HAVE = "1";  // da co tren he thong vc
    }
    //End Trang thai the cao tren VC hungpm6

    public static final class STOCK_DEMO {
        public static final Long EXPORT = 1L;
        public static final Long IMPORT = 2L;
    }

    public static final class GOODS_STATE {
        public static final Long NEW = 1L; //Hang moi
        public static final String NEW_NAME = "NEW"; //Hang moi
        public static final Long OLD = 2L; //Hang cu
        public static final Long BROKEN = 3L; //Hang hong
        public static final String DAMAGE_NAME = "DAMAGE"; //Hang hong
        public static final Long RETRIVE = 4L; //Hang thu hoi
        public static final Long RESCUE = 5L; //Hang ung cuu thong tin
        public static final Long LOCK = 6L; //Hang bi khoa
        public static final Long WARRANTY_CUST_BORROW = 7L; //Hang bao hanh cho khach muon
        public static final Long RECOVER_WARRANTY = 8L; //hang thu hoi bao hanh
        public static final Long DOA = 9L; //hang DOA
        public static final Long BROKEN_WARRANTY = 11L; //hang hong bao hanh
        public static final Long BROKEN_15_DAY_CUSTOMER = 10L; //hang hong 15 ngay KH
        public static final Long GOOD_RETRIVE = 12L; //hang thu hoi tot
        public static final Long DEMO = 13L; //hang demo
    }

    public static final class REQUEST_STATUS {
        public static final String CREATE_REQUEST = "0"; //Lap yeu cau
        public static final String APROVE = "1"; //da duyet
        public static final String IGNORE = "2"; //Tu choi
    }

    public static final class RECEIPT_EXPENSE {
        public static final Long RECEIPT_TYPE_EXPORT_AGENT = 3L; //Xuat hang DL
        public static final Long RECEIPT_TYPE_EXPORT_CTV = 8L; //Xuat hang CTV
    }

    public static final class MODE_SHOP {
        public static final String ISDN = "ISDN";
        public static final String ISDN_MNGT = "ISDN_MNGT";
        public static final String ISDN_DSTRT = "ISDN_DSTRT";
        public static final String ISDN_MNGT_USR_CRT = "ISDN_MNGT_USR_CRT";
        public static final String SHOP_TYPE_BRANCH = "2";
        public static final String EMPTY = "EMPTY";
        public static final String CHILD_OF_PARRENT_SHOP = "CHILD_SHOP";
        public static final String CHILD_SHOP_ALL_STAFF = "CHILD_SHOP_ALL_STAFF";
        public static final String PAYNOTEANDREPORT = "PAYNOTEANDREPORT";
        public static final String CHILD_OF_PARRENT_SHOP_NO_STAFF = "CHILD_SHOP_NO_STAFF";
        public static final String PARTNER = "PARTNER";
        public static final String LIST_SHOP_CODE = "LIST_SHOP_CODE";
        public static final String LIST_SHOP_TRANSFER = "LIST_SHOP_TRANSFER";
        public static final String LIST_SHOP_BRANCH_AND_VTT = "LIST_SHOP_BRANCH_AND_VTT";
        public static final String LIST_SHOP_KV = "LIST_SHOP_KV";
    }

    public static final class STOCK_TOTAL_AUDIT {
        public static final Long SOURCE_TYPE_STOCK_TRANS = 2L;
        public static final Long TRANS_TYPE = 1L;
    }

    public static final class INSPECT_APPROVE_STATUS {
        public static final Long NOT_HAVE_APPROVE = 0L;
        public static final Long HAVE_APPROVE = 1L;
        public static final Long CANCEL_APPROVE = 2L;
        public static final Long NOT_HAVE_FINISH = 0L;
        public static final Long HAVE_FINISH = 1L;
    }

    public static final class CONFIG_PRODUCT {
        public static final Long TYPE_DEPOSIT = 1L;
        public static final Long TYPE_PAY = 2L;
        public static final Long TYPE_DOA = 9L;
        public static final Long TYPE_MATERIAL = 3L;
        public static final Long TYPE_DIVIDE = 1611L;

    }

    public static final class FILE_TEMPLATE {
        public static final String FILE_NAME_TEMPLATE = "mau_file_nhap_serial.xls";
        public static final String FILE_NAME_PT_DL_TEMPLATE = "PT_DL.xls";
        public static final String IMPORT_CHANGE_GOODS_TEMPLATE = "tempate_import_change_goods.xls";
        public static final String TEMPLATE_ERROR_IMPORT_CHANGE_GOODS_FILE = "errorImporChangeGoodsByFile.xls";
        public static final String TEMPLATE_ERROR_IMPORT_CHANGE_GOODS = "errorImporChangeGoodsByFile";
        public static final String TEMPLATE_STOCK_CYCLE_MAU_01 = "stock_total_cycle_mau01.xls";
        public static final String TEMPLATE_GATHER_TRANSFER_ORDER = "gather_transfer_order.xls";
    }

    public static final class STOCK_CYCLE_TYPE {
        public static final Long EXIST_IN_CYCLE = 0L;
        public static final Long EXIST_IN_CYCLE1 = 1L;
        public static final Long EXIST_IN_CYCLE2 = 2L;
        public static final Long EXIST_IN_CYCLE3 = 3L;
        public static final Long EXIST_IN_CYCLE4 = 4L;
        public static final Long EXIST_IN_CYCLE4_OVER = 5L;
        public static final Long EXIST_IN_CYCLE_TOTAL = 6L;
    }

    public static final class ACCOUNT_BALANCE {
        public static final Long BALANCE_TYPE_BANKPLUS = 3L;
    }

    public final static class CHANNEL_TYPE_ID {
        public final static String CHANNEL_TYPE_STAFF = "14";//Kenh nhan vien
        public final static String CHANNEL_TYPE_INSURRANCE = "20";//Kenh BHLD
        public final static String CHANNEL_TYPE_AGENT = "4";//Kenh Dai ly
    }

    public static final class PAY_NOTE_AND_REPORT {
        public static final String PX_BBBG_ = "PX_BBBG_";
        public static final String PDF = ".pdf";
        public static final String LIST_STOCK_DOCUMENT = "ListStockDocument";
    }

    public final static class CURRENCY_TYPE {
        public final static String VND = "VND";
    }

    public static final class IMP_TYPE {
        public static final Long IMP_BY_FILE = 1L; //import theo file
        public static final Long IMP_BY_SERIAL_RANGE = 2L; //import theo dai serial
        public static final Long IMP_BY_QUANTITY = 3L; //import theo so luong
    }

    public static final class STOCK_ORDER_AGENT {
        public final static String LIST_BANK = "LIST_BANK";
        public static final Long STATUS_CREATE_REQUEST = 0L;
        public static final Long STATUS_CREATE_ORDER = 1L;
        public static final Long STATUS_DENY_REQUEST = 2L;
        public static final Long STATUS_CANCEL_REQUEST = 3L;
        public static final Long STATUS_NOREQUEST = 0L;
        public static final Long STATUS_CANCEL = 3L;

    }

    public static enum StaffSearchMode {
        allChild(1), connectChild(2), allChildNotCurrentStaff(3), allChildShopAndParentShop(4);;

        StaffSearchMode(int value) {
            val = value;
        }

        private int val;

        public int value() {
            return val;
        }
    }

    public enum ConfigListIDCheck {
        EXPORT(Lists.newArrayList(1L, 2L, 3L), 1L),
        IMPORT(Lists.newArrayList(4L, 5L, 6L), 2L),
        SUPORT_OWNER_TYPE_LIST(Lists.newArrayList(1L, 2L, 4L), 0L);

        ConfigListIDCheck(List<Long> values, Long type) {
            this.values = values;
            this.type = type;
        }

        List<Long> values;
        Long type;
        String sVal;
        Long lVal;

        public String toString() {
            return sVal;
        }

        public Long toLong() {
            return lVal;
        }


        public boolean validate(Long value) {
            return values.contains(value);
        }

        public boolean sValidate(String value) {
            return values.contains(Long.valueOf(value.trim()));
        }


        public boolean lValidate(Long val) {
            return DataUtil.safeEqual(val, lVal);
        }

        public Long getValue() {
            return type;
        }
    }

    public static final String SYSTEM = "SYSTEM";

    public enum REPORT_PARAMS {
        RECEIPTNO("receiptNo"), FROMOWNER("fromOwnerName"),
        FROMADDRESS("fromOwnerAddress"),
        TOADDRESS("toOwnerAddress"),
        TOOWNER("toOwnerName"), DATEREQUEST("dateRequest"),
        REASONNAME("reasonName"),
        DATASOURCE("lstStockModel"),
        STOCKTRANSTYPE("stockTransType"), NOTE("note"), ACTIONCODE("actionCode"), ACTIONTYPE("actionType");

        REPORT_PARAMS(String value) {
            this.value = value;
        }

        String value;

        public String toString() {
            return value;
        }
    }

    public final static class STOCK_TRANS_VOFFICE {
        public static final Long VND = 1L;
        public static final Long V_SIGNED = 1L;
        public static final Long OWNERTYPE_SHOP = 1L;
        public static final Long OWNERTYPE_STAFF = 2L;
        public static final Long OWNERTYPE_PARTNER = 4L;
        public static final Long TRANG_THAI_ACTIVE = 1L;
        public static final Long V_SIGNING = 3L;
        public static final Long V_ERROR = 4L;
        public static final Long V_BALANCED = 5L;
        public static final Long V_VALIDATE_ERROR = -1L;

    }

    public static enum ConfigIDCheck {
        importPartnerStateIDs(Lists.newArrayList(3L, 1L)),
        importPartnerUpdateStatus(Lists.newArrayList(1L, 2L)),
        importPartnerApproveStatus(Lists.newArrayList(1L)),
        importPartnerOtherInput(Lists.newArrayList(4L, 5L, 8L)),
        importPartnerSim(Lists.newArrayList(4L, 5L)),
        importPartnerKit(Lists.newArrayList(8L)),
        HANDSET(Lists.newArrayList(7L)),
        ACCESSORIES(Lists.newArrayList(10L)),
        KIT(Lists.newArrayList(8L)),
        CARD(Lists.newArrayList(6L)),
        SIM(Lists.newArrayList(4L, 5L)),
        NUMBER(Lists.newArrayList(1L, 2L, 3L)),
        nhung_trang_thai_duoc_phep_exoport(Lists.newArrayList(1L)),
        nhung_trang_thai_duoc_phep_divide(Lists.newArrayList(0L, 1L)),
        kho_don_vi(Lists.newArrayList(1L)),
        kho_nhan_vien(Lists.newArrayList(2l)),
        danh_sach_action_khong_can_actioncode(Lists.newArrayList(3L, 6L, 7L, 8L));


        ConfigIDCheck(List<Long> value) {
            val = value;
        }

        private List<Long> val;

        public List<Long> value() {
            return val;
        }

        public boolean validate(Long state) {
            return val.contains(state);
        }

        public boolean sValidate(String state) {
            if (DataUtil.isNullOrEmpty(state)) {
                return false;
            }
            for (Long l : val) {
                if (DataUtil.safeEqual(l.toString(), state)) {
                    return true;
                }
            }
            return false;
        }
    }

    public enum PrefixCode {
        importPartnerRequestCode("RC_");

        PrefixCode(String val) {
            prefix = val;
        }

        private String prefix;

        public String value() {
            return prefix;
        }
    }

    /**
     * cau hinh app cho gui tin nhan
     */
    public enum AppProperties4Sms {
        appName("IM"), appID("1"),
        channel("155"), moHisID("1"), retryNum("0"), profileSparator(","), prefixTable("ERR$_");

        AppProperties4Sms(Object val) {
            value = val;
        }

        private Object value;

        public Object getValue() {
            return value;
        }

        public String stringValue() {
            return value.toString();
        }

        public Long getLongValue() {
            return DataUtil.safeToLong(value);
        }
    }

    public enum LongSimpleItem {
        importPartnerRequestStatusCreate(0L),
        importPartnerRequestStatusApproved(1L),
        importPartnerRequestStatusRejected(2L),
        importPartnerRequestStatusImported(3L),
        importPartnerInputByFile(1L),
        importPartnerInputByRank(2L),
        importPartnerInputByNumber(3L),
        importPartnerRankSerial1(1L),
        importPartnerRankSerial50(50000L), maxRange(Long.MAX_VALUE);
        Long value;

        LongSimpleItem(Long val) {
            this.value = val;
        }

        public Long getValue() {
            return value;
        }
    }

    public interface CODE_SERVICE {

        public String PRICE_TYPE_RETAIL = "1";
        public String CANCEL_ORDER_STATE = "11";
        public String NEW_ORDER_STATE = "0";
        public String STATUS_ACTIVE = "1";
        public String ERR_UNKNOWN = "0";
        public String SUCCESS = "1";
        public String ERR_PACKAGE_CODE_NOT_FOUND = "ERR_PACKAGE_CODE_NOT_FOUND";
        public String ERR_PRODUCT_CODE_NOT_VALID = "PRODUCT_ID_NOT_VALID";
        public String ERR_PRICE_NOT_FOUND = "ERR_PRICE_NOT_FOUND";
        public String ERR_INVALID_USERPASS = "INVALID_USERPASS";
        public String ERR_INVALID_PARAMETER = "ERR_INVALID_PARAMETER";
        public String ERR_STOCK_LIST_NOT_VALID = "ERR_STOCK_LIST_NOT_VALID";
        public String ERR_QUANTITY_NOT_ENOUGH = "ERR_QUANTITY_NOT_ENOUGH";
        public String ERR_ORDER_NOT_FOUND = "ERR_ORDER_NOT_FOUND";
        public String ERR_ORDER_STATE_NOT_VALID = "ERR_ORDER_STATE_NOT_VALID";
        public String ERR_PAYMENT_ID_NOT_VALID = "ERR_PAYMENT_ID_NOT_VALID";
        public String ERR_EXCEPTION = "ERR_EXCEPTION";
    }

    public enum SourceType {
        SALE_TRANS(1L), STOCK_TRANS(2L), CMD_TRANS(3L), STICK_TRANS(4L), OTHER(5L);

        private final Long id;

        SourceType(Long id) {
            this.id = id;
        }

        public Long getValue() {
            return id;
        }
    }


    public enum StockStateType {
        USED(1L), OLD(2L), DAMAGE(3L);//Da su dung

        private final Long id;

        StockStateType(Long id) {
            this.id = id;
        }

        public Long getValue() {
            return id;
        }

    }

    public static final class STOCK_INSPECT {
        public static final String STATE = "GOOD_STATE";
        public static final String STOCK_INSPECT_STATUS = "STOCK_INSPECT_STATUS";
        public static final String CHECK_STOCK_DAY_IN_MOTH = "CHECK_STOCK_DAY_IN_MOTH";
        public static final String SEPARATE_CHAR_STOCK_INSPECT = "SEPARATE_CHAR_STOCK_INSPECT";
        public static final String ABOVE_QUANTITY = "ABOVE_QUANTITY";
        public static final String BELLOW_QUANTITY = "BELLOW_QUANTITY";
        public static final String ABOVE = "ABOVE";
        public static final String BELLOW = "BELLOW";
        public static final String INPUT_METHOD = "CHECK_STOCK_INPUT";
        public static final Long INPUT_METHOD_MANUAL = 1L;
        public static final Long INPUT_METHOD_MACHINE = 2L;
        public static final Long IS_FINISHED = 1L;
        public static final Long IS_NOT_FINISH = 0L;
        public static final Long QUANTITY_ZERO = 0L;
        public static final Long STATUS_NOT_APPROVE = 0L;
        public static final Long MAX_QUANTITY_STOCK_INSPECT = 50000L;
    }

    public static final class PROCESS_STOCK {
        public static final String PROCESS_STOCK_STATUS = "PROCESS_STOCK_STATUS";
        public static final String ACTION_BALANCE_STOCK_TOTAL = "BALANCE_STOCK_TOTAL";
        public static final String TABLE_NAME = "STOCK_TOTAL";
        public static final String COLUMN_NAME_CURRENT = "CURRENT_QUANTITY";
        public static final String COLUMN_NAME_AVAIABLE = "AVAILABLE_QUANTITY";
    }

    public static final class UNLOCK_G6 {
        public static final String ERR_NOTEXIST_EMEI = "ERR_NOTEXIST_EMEI";
        public static final String ERR_VALIDATE_EMEI = "ERR_VALIDATE_EMEI";
        public static final String SUCCESS = "0";
        public static final String FAILED = "1";
    }

    public enum MapTableProdType {
        mapper();

        MapTableProdType() {
        }

        public String getTableName(Long ID) {
            if (ConfigIDCheck.HANDSET.validate(ID) || ConfigIDCheck.ACCESSORIES.validate(ID)) {
                return "STOCK_HANDSET";
            }
            if (ConfigIDCheck.KIT.validate(ID)) {
                return "STOCK_KIT";
            }
            if (ConfigIDCheck.CARD.validate(ID)) {
                return "STOCK_CARD";
            }
            if (ConfigIDCheck.SIM.validate(ID)) {
                return "STOCK_SIM";
            }
            if (ConfigIDCheck.NUMBER.validate(ID)) {
                return "STOCK_NUMBER";
            }
            return "";
        }
    }

    public static final class IMPORT_APN_TYPE {
        public final static String IMPORT_APN = "1";
        public final static String IMPORT_APN_IP = "2";
    }

    public static final class LIMIT_STOCK {
        public static final String TEMPLATE_CREATE_ORDER_SHOP = "import_shop_debit_template.xls";
        public static final String TEMPLATE_CREATE_ORDER_SHOP_ERROR = "import_shop_debit.xls";
        public static final String TEMPLATE_CREATE_ORDER_STAFF = "import_debit_template.xls";
        public static final String TEMPLATE_CREATE_ORDER_STAFF_ERROR = "import_debit.xls";
    }

    public static final class CHANGE_PRODUCT {

        public static final String APPROVE_CHANGE_PRODUCT_STATUS = "APPROVE_CHANGE_PRODUCT_STATUS";
        public static final Long CHANGE_MODEL_TRANS_NEW = 0L;
        public static final Long CHANGE_MODEL_TRANS_APPROVE = 1L;
        public static final Long CHANGE_MODEL_TRANS_CANCEL = 2L;
        public static final Long CHANGE_MODEL_TRANS_STATUS_WAIT_TOOLKIT = 3L;
        public static final Long CHANGE_MODEL_TRANS_STATUS_SUCCESS = 5L;
        public static final Long CHANGE_MODEL_TRANS_STATUS_WAIT_DESTROY = 6L;

    }

    public static final class PROCESS_TOOLKIT {

        public static final Long PRE_CANCEL_STATUS = 6L;
        public static final Long PRE_APPROVE_STATUS = 5L;
        public static final Long CANCEL_STATUS = 4L;
        public static final Long APPROVE_STATUS = 1L;
        public static final String DAY_APPROVE_CHANGE_TOOLKIT = "DAY_APPROVE_CHANGE_TOOLKIT";
    }

    //hoangnt
    public static final class STOCK_TRANS_RESCUE {
        public static final String WARRANTY_STATUS = "WARRANTY_STATUS";
        public static final Long STATUS_NOT_RESCUE = 0L;
        public static final Long STATUS_HAVE_RESCUE = 1L;
        public static final Long STATUS_DELETE = 2L;
        public static final Long STATUS_ACCEPT = 3L;
        public static final Long STATUS_CANCEL = 4L;
        public static final Long STATUS_RETURN = 5L;
        public static final Long STATUS_RECEIVE = 6L;
    }

    public static final class LIQUIDATE_STATUS {
        public static final String NEW = "0";
        public static final String APPROVE = "1";
        public static final String REJECT = "2";
    }

    public enum BalanceRequestStatus {
        create(0L), approved(1L), reject(2L), balanced(3l);

        BalanceRequestStatus(Long val) {
            this.val = val;
        }

        Long val;

        public String toString() {
            return val.toString();
        }

        public Long toLong() {
            return val;
        }
    }

    public enum ConfigStockTrans {
        LX(1L, "LX"),
        PX(2L, "PX"),
        XK(3L, "PX"),
        LN(4L, "LN"),
        PN(5L, "PN"),
        NK(6L, "PN"),
        PXS(6L, "PXS"), FR_PARTNER(4L, "FRM_PARTNER");

        ConfigStockTrans(Long lVal, String sVal) {
            this.sVal = sVal;
            this.lVal = lVal;
        }

        String sVal;
        Long lVal;

        public String toString() {
            return sVal;
        }

        public Long toLong() {
            return lVal;
        }

        public boolean validate(String val) {
            return DataUtil.safeEqual(lVal.toString(), val);
        }

        public boolean lValidate(Long val) {
            return DataUtil.safeEqual(val, lVal);
        }
    }

    public static final class JASPER_TEMPLATE {
        public static final String SEPARATOR = "_";
        public static final String ENDPREFIX = "2007";
        public static final String EXTENSION = ".jasper";
    }

    public static final class THREE_REGION {
        public static final String VT_MB = "VT_MB";
        public static final String VT_MN = "VT_MN";
        public static final String VT_MT = "VT_MT";
    }

    public static final class SALE_TRANS {
        public static final String SALE_TRANS_STATUS_CANCEL = "4"; //huy
        public static final Long SALE_BILLED = 3L;//GIAO DICH DA LAP HOA DON
        public static final String PRODUCT_OFFERING_CODE_TCDT = "TCDT";       //Ma the cao dien tu
        public static final Long PRODUCT_OFFERING_ID_TCDT = 1076L;       //Ma the cao dien tu
        public static final String PRODUCT_OFFERING_CODE_AIRTIME = "TCANYPAY2";       //Ma TCDT
        public static final String SALE_TRANSFER_GOOD = "1"; //�� giao h�ng
        public static final Long REQUEST_TYPE_SALE_AGENTS = 4L;
        public static final Long REQUEST_TYPE_CHANGE_GOODS = 2L;
        public static final Long SALE_TYPE = 1L; //gd ban
        public static final Long SALE_TRANS_TYPE_RETAIL = 1L; //ban le
        public static final Long SALE_TRANS_TYPE_AGENT = 2L; //ban dai ly
        public static final Long PAY_METHOD_BANKPLUS = 16L; //Hinh thuc hanh toan bankplus
    }

    public enum SaleTransStatus {
        DESTROY("4"), //Da huy
        INVOICE("3"), //Da lap hoa don
        NOT_INVOICE("2"), //Chua lap hoa don
        NOT_PAYMENT("1"); //Trang thai giao dich la chua thanh toan khi thanh toan qua POS

        private final String id;

        SaleTransStatus(String id) {
            this.id = id;
        }

        public String getValue() {
            return id;
        }
    }

    public static class EXPORT_PARTNER {
        public static final String EXP_BY_QUANTITY = "1"; //export theo so luong
        public static final String EXP_BY_SERIAL_RANGE = "2"; //export theo dai serial
        public static final String EXP_BY_FILE = "3"; //export theo file
    }

    public enum SaleTransType {
        RETAIL("1"), //Ban le
        RETAIL_WARRANTY("44"), //Ban hang bao hanh cho khach hang muon
        RETAIL_PROMOTION("46"), //Khuyen mai thiet bi dau cuoi
        RETAIL_52("52"), //????
        RETAIL_BANKPLUS("62"), //Ban le qua BankPlus
        RETAIL_POSEDC("69"), //Ban le qua may posdc
        AGENT("2"), //Ban DL
        COLL("3"), //Ban CTV/DB
        PROMOTION("5"),//Ban khuyen mai
        INTERNAL("6"), //Ban noi bo
        PUNISH("10"), //Xuat ban phat
        RESCUE("42"), //Loai giao dich ban hang ung cuu thong tin
        WARRANTY("14"),
        BULKSMS("38"),
        SERVICE("4"),// Loai giao dich dich vu,
        //        Tienph2
        REPAIR_SERVICE("25"),// Sua chua dich vu
        DAMAGE_TYPE("12"),// Ban hang hong
        RETAIL_FOR_COL("9"), // Giao dich ban thay cho cong tac vien
        CTV_AGENT_SERVICE("7"), // Cong tac vien, dai ly lam dich vu , cong tac vien ban le cho khach hang
        SELF_CARE("15"), //Ban hang qua may SelfCare
        TOPUP("36"), // Giao dich Topup
        VIETTEL_SHOP("28"), // Giao dich ban le tu ViettelShop
        AMS_SMS_BANKPLUS("40"), // Giao dich AMS, SMS Bankplus
        ADJUST("41"), // Dieu chinh giam doanh thu ban hang
        NOT_MINUS_STOCK("13"),// Giao dich khong tru kho hang hoa
        ABNORMAL("35"),//Giao dich len doanh thu bat thuong
        TKTT_OUT_OF_DATE("34"),// Giao dich len doanh thu tu TKTT do qua han
        ANYPAY_BANKPLUS("31")    // Giao dich nap anypay tu bankplus
        ; //Xuat ban hang bao hanh


        private final String id;

        SaleTransType(String id) {
            this.id = id;
        }

        public String getValue() {
            return id;
        }

        public static SaleTransType findByValue(String value) {
            for (SaleTransType v : values()) {
                if (DataUtil.safeEqual(v.getValue(), value)) {
                    return v;
                }
            }
            return RETAIL;
        }
    }

    public enum ChangeDamagedProduct {
        SINGLE_TYPE("1"), FILE_TYPE("2");

        private final String id;

        ChangeDamagedProduct(String id) {
            this.id = id;
        }

        public String getValue() {
            return id;
        }
    }

    public static class STOCK_ORDER {
        public static final Long STATUS_NEW = 0L;
        public static final Long STATUS_APPROVE = 1L;
        public static final Long STATUS_CANCEL = 2L;
        public static final Long STATUS_REFUSE = 3L;
        public static final Long REASON_ID = 4547L;
    }

    public static class CHANGE_MODEL_TRANS_REQUEST_TYPE {
        public static final Long CREATE_REQUEST_KIT = 2L;
        public static final Long CREATE_REQUEST_STOCK = 1L;
    }

    public static class STATUS_KCS {
        public static final String STATUS_ORDER = "1";
        public static final String STATUS_NOTE = "2";
        public static final String STATUS_IMPORT = "3";
    }


    public static final class SUBSCRIBER {
        public static final String ACT_STATUS_INACTIVE = "030";
        public static final Long STATUS_ACTIVE = 2L;

        public static final String REASON_DNTD = "DNTD";
    }

    public static final class DIVIDE_DEVICE {
        public static final String PREFIX_TEMPLATE = "DEVICE_TRANSFER";
    }

    public static final class DEVICE_CONFIG {
        public static final Long PROB_OFFER_TYPE_ID = 7L;
        public static final Long ITEM_OFFER_TYPE_ID = 11L;
    }

    public static final class PRODUCE_IMSI_RANGE {
        public static final Long PROB_OFFER_TYPE_ID_FOR_SIM = 4L;
        public static final String GSM_TYPE = "1";
        public static final String USIM_TYPE = "2";
        public static final String USIM_TRANSPORT_KEY = "108";
        public static final String GSM_TRANSPORT_KEY = "50";
        public static final String IMSI = "IMSI";
        public static final Long POST_SIM = 4L;
        public static final Long EMPTY_AND_HOMEPHONE_SIM = 5L;
    }

}

