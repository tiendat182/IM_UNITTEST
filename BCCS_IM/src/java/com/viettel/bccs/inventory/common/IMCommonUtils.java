/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.bccs.inventory.common;

import com.viettel.bccs.inventory.common.expression.Expression;
import com.viettel.bccs.inventory.dto.NumberFilterRuleDTO;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
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
}
