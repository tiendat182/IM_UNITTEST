package com.viettel.bccs.inventory.common;

/**
 * Created by ChungNV7 on 1/12/2016.
 */
public class IsdnConst {

    //gan so NGN
    public static final String SUCCESS = "0";
    public static final String GRANT_SUCCESS = "00";
    public static final String GRANT_FAIL = "7";
    public static final String NGN_NOT_EXIST = "1";
    public static final String NGN_INVALID_CODE = "2";
    public static final String NGN_WRONG_STATUS = "3";
    public static final String NGN_WRONG_SHOP = "4";
    public static final String ISDN_NOT_EXIST = "5";
    public static final String CODE_DUP = "6";
    public static final String NGN_AREA_CODE_EXPIRE = "8";
    //code NGN
    public static final String ARANGE_STOCK_ISDN_STOCK_CODE = "ARANGE_STOCK_ISDN_STOCK_CODE";
    //code PSTN
    public static final String ARANGE_STOCK_ISDN_TK_STOCK_CODE = "ARANGE_STOCK_ISDN_TK_STOCK_CODE";
    //
    public static final String ERR_NGN_CODE = "101";
    public static final String ERR_AREA_CODE = "102";
    public static final String ERR_ISDN = "103";
    public static final Long TYPE_PSTN = 31L;
    public static final Long TYPE_NGN = 23L;

    //xoa so
    public static final String DEL_SUCCESS = "00";
    public static final String DEL_AVAIL = "0";
    public static final String DEL_FAIL = "11";
    public static final String DEL_NOT_RANGE = "3";
    public static final String DEL_INVALID_FROM = "4";
    public static final String DEL_INVALID_TO = "5";
    public static final String DEL_DOUBLE = "9";

    public static final String EXCEL_TYPE = "xls|xlsx";

    public static final String ISDN_ACTION_TYPE_CREATE = "1";
    public static final String ISDN_ACTION_TYPE_FILTER = "2";
    public static final String ISDN_ACTION_TYPE_MODEL = "5";
    public static final String ISDN_ACTION_TYPE_STATUS = "3";
    public static final String ISDN_ACTION_TYPE_DSTRT = "4";
    public static final String ISDN_ACTION_TYPE_EXP = "4";
    public static final String ISDN_ACTION_TYPE_NGN = "8";

}
