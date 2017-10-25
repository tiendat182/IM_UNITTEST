package com.viettel.bccs.inventory.service.common;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.fw.common.util.BundleUtil;
import com.viettel.fw.common.util.GetTextFromBundleHelper;
import com.viettel.fw.service.BaseServiceImpl;
import jxl.Sheet;
import jxl.Workbook;
import jxl.Cell;
import jxl.write.*;
import jxl.write.Pattern;
import org.apache.cxf.jaxws.context.WebServiceContextImpl;
import org.apache.cxf.jaxws.context.WrappedMessageContext;
import org.apache.log4j.Logger;

import javax.servlet.ServletRequest;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import java.io.File;
import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.*;

/**
 * service Util cua IM, code ca lu ten trang thai cac kieu vao day
 * Created by ThanhNT77 on 10/4/2016.
 */
public class IMServiceUtil extends BaseServiceImpl {

    public static final Logger logger = Logger.getLogger(IMServiceUtil.class);

    private static WebServiceContext wsContext = new WebServiceContextImpl();

    /**
     * @author ThanhNT
     * @param prodOfferTypeId
     * @return
     */
    public static String getTableNameByOfferType(Long prodOfferTypeId) {
        String tableName = "";
        if (Const.STOCK_TYPE.STOCK_CARD.equals(prodOfferTypeId)) {
            tableName = "STOCK_CARD";
        } else if (Const.STOCK_TYPE.STOCK_HANDSET.equals(prodOfferTypeId) || Const.STOCK_TYPE.STOCK_ACCESSORIES.equals(prodOfferTypeId)) {
            tableName = "STOCK_HANDSET";
        } else if (Const.STOCK_TYPE.STOCK_KIT.equals(prodOfferTypeId)) {
            tableName = "STOCK_KIT";
        } else if (Const.STOCK_TYPE.STOCK_SIM_POST_PAID.equals(prodOfferTypeId)) {
            tableName = "STOCK_SIM";
        } else if (Const.STOCK_TYPE.STOCK_SIM_PRE_PAID.equals(prodOfferTypeId)) {
            tableName = "STOCK_SIM";
        } else if (Const.STOCK_TYPE.STOCK_ISDN_MOBILE.equals(prodOfferTypeId) ||
                Const.STOCK_TYPE.STOCK_ISDN_HOMEPHONE.equals(prodOfferTypeId) ||
                Const.STOCK_TYPE.STOCK_ISDN_PSTN.equals(prodOfferTypeId)) {
            tableName = "STOCK_NUMBER";
        }
        return tableName;
    }

    public static String getTextKey(String key) {
        return BundleUtil.getText(GetTextFromBundleHelper.getLocate(), key);
    }

    public static String getTextKeyParam(String key, String ... params) {
        return MessageFormat.format(getTextKey(key), params);
    }

    /**
     * ham tra ve trang thai mat hang
     * @author thanhnt
     * @param stateId
     * @return
     */
    public static String getStateName(Long stateId) {
        String stateName = "";
        if (Const.GOODS_STATE.NEW.equals(stateId)) {
            stateName = getTextKey("create.request.import.product.status1");//hang moi
        } else if (Const.GOODS_STATE.OLD.equals(stateId)) {
            stateName = getTextKey("create.request.import.product.status2");//hang cu
        } else if (Const.GOODS_STATE.BROKEN.equals(stateId)) {
            stateName = getTextKey("create.request.import.product.status3");//hang hong
        } else if (Const.GOODS_STATE.RETRIVE.equals(stateId)) {
            stateName = getTextKey("create.request.import.product.status4");//hang thu hoi
        } else if (Const.GOODS_STATE.RESCUE.equals(stateId)) {
            stateName = getTextKey("create.request.import.product.status5");//hang ung cuu thong tin
        } else if (Const.GOODS_STATE.LOCK.equals(stateId)) {
            stateName = getTextKey("create.request.import.product.status6");//hang bi khoa
        } else if (Const.GOODS_STATE.WARRANTY_CUST_BORROW.equals(stateId)) {
            stateName = getTextKey("create.request.import.product.status7");//hang bao hanh cho khach muon
        } else if (Const.GOODS_STATE.RECOVER_WARRANTY.equals(stateId)) {
            stateName = getTextKey("create.request.import.product.status8");//hang thu hoi bao hanh
        }
        return stateName;
    }

    /**
     * ham tra ve ten trang thai yeu cau DOA
     * @author thanhnt
     * @param status
     * @return
     */
    public static String getStatusRequestName(String status) {
        String statusName = "";
        if (Const.REQUEST_STATUS.CREATE_REQUEST.equals(status)) {
            statusName = getTextKey("mn.stock.branch.btn.create.request");//lap yeu cau
        } else if (Const.REQUEST_STATUS.APROVE.equals(status)) {
            statusName = getTextKey("debit.request.status3");//da duyet
        } else if (Const.REQUEST_STATUS.IGNORE.equals(status)) {
            statusName = getTextKey("debit.select.status0");//tu choi
        }
        return statusName;
    }

    /**
     * ham tra ve loai kho theo ownerType
     * author thanhnt
     * @param ownerType
     * @return
     */
    public static String getOwnerTypeName(String ownerType) {
        return Const.OWNER_TYPE.SHOP.equals(ownerType) ? getTextKey("stock.trans.ui.stocktype.unit") : getTextKey("stock.trans.ui.stocktype.staff");
    }

    /**
     * ham tra ve loai kho theo ownerType
     * author thanhnt
     * @param status
     * @return
     */
    public static String getStockTransTypeName(String status) {
        return "1".equals(status) ? getTextKey("mn.stock.export") : getTextKey("mn.stock.agency.retrieve.import.btn.submit");
    }

    /**
     * ham tra ve loai kho theo ownerType
     * author thanhnt
     * @param status
     * @return
     */
    public static String getConnectionStatus(String status) {
        return "0".equals(status) ? getTextKey("mn.stock.utilities.look.serial.connection.status0") : getTextKey("mn.stock.utilities.look.serial.connection.status1");
    }

    /**
     * ham tra ve tinh trang mat hang trong kho
     * @author thanhnt
     * @param status
     * @return
     */
    public static String getProdStatusName(Long status) {
        if (status != null) {
            if (status.equals(0L)) {
                return getTextKey("mn.stock.utilities.look.serial.prod.status0");//da ban
            } else if (status.equals(1L)) {
                return getTextKey("mn.stock.utilities.look.serial.prod.status1");//trong kho
            } else if (status.equals(3L)) {
                return getTextKey("mn.stock.utilities.look.serial.prod.status3");//cho xac nhan nhap
            } else if (status.equals(4L)) {
                return getTextKey("mn.stock.utilities.look.serial.prod.status4");//cho chuyen doi DOA
            } else if (status.equals(5L)) {
                return getTextKey("mn.stock.utilities.look.serial.prod.status5");//da huy
            } else if (status.equals(8L)) {
                return getTextKey("mn.stock.utilities.look.serial.prod.status8");//cho thue
            } else if (status.equals(9L)) {
                return getTextKey("mn.stock.utilities.look.serial.prod.status9");//cho muon
            }
        }
        return "";
    }

    public static String connectData(Object... objs) {
        StringBuilder dataConnected = new StringBuilder();
        if (objs == null || objs.length == 0) {
            return "";
        }
        try {
            for (int i = 0; i < objs.length; i++) {
                Object obj = objs[i];
                if (obj instanceof List) {
                    dataConnected.append(connectLstObject((List) obj)).append("@");
                } else if (obj instanceof Object) {
                    dataConnected.append(obj).append("@");
                }
            }
            if (dataConnected.toString().length() > 1) {
                String strDataConnected = dataConnected.toString();
                return strDataConnected.substring(0, strDataConnected.length() - 1);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return dataConnected.toString();
    }

    public static String connectLstOfLstObject(List... lstObject) {
        StringBuilder strBuff = new StringBuilder();
        if (lstObject == null) {
            return "";
        }

        try {
            boolean isExists = false;
            for (int i = 0; i < lstObject.length; i++) {
                isExists = true;
                List lst = lstObject[i];
                strBuff.append(connectLstObject(lst)).append("@");
            }
            if (isExists) {
                String connectString = strBuff.toString();
                return connectString.substring(0, connectString.length() - 1);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }

        return strBuff.toString();
    }

    public static String connectLstObject(List lst) {
        StringBuilder strBuff = new StringBuilder();
        if (lst == null) {
            return "";
        }

        try {
            if (!lst.isEmpty()) {
                for (int i = 0; i < lst.size(); i++) {
                    strBuff.append("{").append(connectObjectString(lst.get(i))).append("}");
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return strBuff.toString();
    }

    public static String connectObjectString(Object object) throws Exception {

        if (object == null) {
            return "";
        }
        StringBuilder connectString = new StringBuilder();
        try {
            Object someObject = object;
            boolean isExists = false;
            for (Field field : someObject.getClass().getDeclaredFields()) {

                // You might want to set modifier to public first.
                field.setAccessible(true);
                Object value = field.get(someObject);
                if (value != null) {
                    isExists = true;
                    connectString.append(field.getName()).append("=").append(value).append(";");
                }
            }

            if (isExists) {
                String strConnectString = connectString.toString();
                return strConnectString.substring(0, strConnectString.length() - 1);
            }
            return connectString.toString();
        } catch (Exception ex) {
            throw ex;
        }
    }

    public static String getIpClient() throws Exception {
     return ((ServletRequest) ((((WrappedMessageContext) wsContext.getMessageContext()).getWrappedMessage()).getContextualProperty("HTTP.REQUEST"))).getRemoteAddr();
    }

    public static String getIpLocal() throws Exception {
        return ((ServletRequest) ((((WrappedMessageContext) wsContext.getMessageContext()).getWrappedMessage()).getContextualProperty("HTTP.REQUEST"))).getLocalAddr();
    }

    public static int getPortLocal() throws Exception {
        return ((ServletRequest) ((((WrappedMessageContext) wsContext.getMessageContext()).getWrappedMessage()).getContextualProperty("HTTP.REQUEST"))).getLocalPort();
    }

    public static String unAccent(String s) {
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("").replaceAll("�", "D").replaceAll("d", "d");
    }
}
