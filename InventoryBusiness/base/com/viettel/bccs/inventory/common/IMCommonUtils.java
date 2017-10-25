/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.bccs.inventory.common;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.expression.Expression;
import com.viettel.bccs.inventory.dto.NumberFilterRuleDTO;
import org.apache.log4j.Logger;

import com.viettel.bccs.inventory.dto.StockTransDetailDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.security.PassTranformer;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * @author quangkm
 */
public class IMCommonUtils {
    public static final Logger logger = Logger.getLogger(IMCommonUtils.class);

    public static NumberFilterRuleDTO checkSpecialNumber(String isdn, List<NumberFilterRuleDTO> lstRules) throws Exception {
        isdn = isdn.trim();
        firstLoop:
        for (NumberFilterRuleDTO numberFilterRule : lstRules) {

            String format = numberFilterRule.getMaskMapping();
            String condition = numberFilterRule.getCondition();

            //Check length
            if (isdn.length() != format.length()) {
                continue firstLoop;
            }
            //Check format
            char[] cFormat = format.toCharArray();

            char[] cIsdn = isdn.toCharArray();
            for (int idx = 0; idx < format.length(); idx++) {
                char c = cFormat[idx];
                char i = cIsdn[idx];
                for (int sIndex = idx + 1; sIndex < format.length(); sIndex++) {
                    if (cFormat[sIndex] == c && cIsdn[sIndex] != i) {
                        continue firstLoop;
                    }
                }
            }
            //check condition
            Map<String, BigDecimal> variables = new LinkedHashMap<String, BigDecimal>();
            for (int idx = 0; idx < format.length(); idx++) {
                Long num = Long.valueOf(String.valueOf(cIsdn[idx]));
                variables.put(String.valueOf(cFormat[idx]), BigDecimal.valueOf(num));
            }
            BigDecimal result;
            try {
                Expression expression = new Expression(condition);
                result = expression.eval(variables);
                if (Integer.valueOf(result.toString()) > 0) {
                    return numberFilterRule;
                } else {
                    continue firstLoop;
                }

            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
                continue firstLoop;
            }
        }
        return null;
    }

    public static boolean checkSpecialOneNumber(String isdn, String condition, String mask) throws Exception {

        //Check length
        if (isdn.length() != mask.length()) {
            return false;
        }
        //Check format
        char[] cFormat = mask.toCharArray();
        char[] cIsdn = isdn.toCharArray();
        for (int idx = 0; idx < mask.length(); idx++) {
            char c = cFormat[idx];
            char i = cIsdn[idx];
            for (int sIndex = idx + 1; sIndex < mask.length(); sIndex++) {
                if (cFormat[sIndex] == c && cIsdn[sIndex] != i) {
                    return false;
                }
            }
        }
        //check condition
        Map<String, BigDecimal> variables = new LinkedHashMap<String, BigDecimal>();
        for (int idx = 0; idx < mask.length(); idx++) {
            Long num = Long.valueOf(String.valueOf(cIsdn[idx]));
            variables.put(String.valueOf(cFormat[idx]), BigDecimal.valueOf(num));
        }
        BigDecimal result;
        try {
            Expression expression = new Expression(condition);
            result = expression.eval(variables);
            if (Integer.valueOf(result.toString()) > 0) {
                return true;
            }

        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }

        return false;
    }

    private static String checkValidCondition(String format, String condition) {
        format = format.trim();
        condition = condition.trim();
        char[] cFormat = format.toCharArray();
        char[] cFormatPattern = new char[cFormat.length];
        for (int i = 0; i < cFormatPattern.length; i++) {
            cFormatPattern[i] = '0';
        }

        //kiem tra condition co dung khong
        Map<String, BigDecimal> variables = new LinkedHashMap<String, BigDecimal>();
        for (int idx = 0; idx < format.length(); idx++) {
            Long num = Long.valueOf(String.valueOf(cFormatPattern[idx]));
            variables.put(String.valueOf(cFormat[idx]), BigDecimal.valueOf(num));
        }
        BigDecimal result;
        try {
            Expression expression = new Expression(condition);
            result = expression.eval(variables);
            return "success";
        } catch (RuntimeException ex) {
            logger.error(ex.getMessage(), ex);
            return ex.getMessage();
        }
    }


    public static void main(String[] args) throws Exception {
        boolean d = checkSpecialOneNumber("985000111", "(A==0)&&(B!=A)", "XYZAAABBB");
        System.out.println(d);

    }

    public static void checkDuplicateStockTrans(List<StockTransDetailDTO> stockTransDetailDTOs) throws LogicException, Exception {
        if (stockTransDetailDTOs == null) {
            return;
        }
        List<String> lsCheck = Lists.newArrayList();
        for (StockTransDetailDTO stockTransDetailDTO : stockTransDetailDTOs) {
            String data = DataUtil.safeToString(stockTransDetailDTO.getProdOfferTypeId()) + DataUtil.safeToString(stockTransDetailDTO.getProdOfferId()) + DataUtil.safeToString(stockTransDetailDTO.getStateId());
            if (lsCheck.contains(data)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_DUPLICATE_OBJECT, "export.order.stocktrans.code.duplicate.msg", stockTransDetailDTO.getProdOfferName());
            }
            lsCheck.add(data);
        }
    }

    //    @Deprecated
    public static Connection getDBIMConnection() throws ClassNotFoundException, Exception {
        ResourceBundle resource = ResourceBundle.getBundle("spring");
        String url = resource.getString("inventory.jdbc.url");
        if (url != null && url.trim().startsWith("ENC(") && url.endsWith(")")) {
            url = url.substring(4, url.length() - 1);
            url = PassTranformer.decrypt(url);
        }
        String user = resource.getString("inventory.jdbc.username");
        if (user != null && user.trim().startsWith("ENC(") && user.endsWith(")")) {
            user = user.substring(4, user.length() - 1);
            user = PassTranformer.decrypt(user);
        }

        String pass = resource.getString("inventory.jdbc.password");
        if (pass != null && pass.trim().startsWith("ENC(") && pass.endsWith(")")) {
            pass = pass.substring(4, pass.length() - 1);
            pass = PassTranformer.decrypt(pass);
        }
        Class.forName("oracle.jdbc.driver.OracleDriver");
        Connection con = DriverManager.getConnection(url, user, pass);
        return con;
    }

    public static Connection getDBIMConnectionIM1() throws ClassNotFoundException, Exception {
        ResourceBundle resource = ResourceBundle.getBundle("spring");
        String url = resource.getString("imlib.jdbc.url");
        if (url != null && url.trim().startsWith("ENC(") && url.endsWith(")")) {
            url = url.substring(4, url.length() - 1);
            url = PassTranformer.decrypt(url);
        }
        String user = resource.getString("imlib.jdbc.username");
        if (user != null && user.trim().startsWith("ENC(") && user.endsWith(")")) {
            user = user.substring(4, user.length() - 1);
            user = PassTranformer.decrypt(user);
        }

        String pass = resource.getString("imlib.jdbc.password");
        if (pass != null && pass.trim().startsWith("ENC(") && pass.endsWith(")")) {
            pass = pass.substring(4, pass.length() - 1);
            pass = PassTranformer.decrypt(pass);
        }
        Class.forName("oracle.jdbc.driver.OracleDriver");
        Connection con = DriverManager.getConnection(url, user, pass);
        return con;
    }

    public static String validateIsdn(String isdn) {

        if (isdn == null) {
            return "";
        }
        isdn = isdn.startsWith("0") ? isdn.substring(1) : isdn;
        isdn = isdn.startsWith("84") ? isdn.substring(2) : isdn;
        isdn = (isdn.length() > 10) ? isdn.substring(isdn.length() - 10) : isdn;

        return isdn;
    }

}
