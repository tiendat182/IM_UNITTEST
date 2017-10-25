/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.fw.service;

import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.BundleUtil;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.GetTextFromBundleHelper;
import com.viettel.ws.common.utils.GenericWebInfo;
import com.viettel.ws.common.utils.Locate;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.headers.Header;
import org.w3c.dom.Element;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.ws.WebServiceContext;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

/**
 * @author LamNV5, add 27/05/2015
 */
public abstract class
BaseServiceImpl {

    public Locate getLocate() {
        return GetTextFromBundleHelper.getLocate();
    }

    public GenericWebInfo getGenericWebInfo() {
        return GetTextFromBundleHelper.getGenericWebInfo();
    }

    public String getText(String key) {
        return BundleUtil.getText(getLocate(), key);
    }

    public String getTextParam(String key, String... params) {
        return MessageFormat.format(getText(key), params);
    }

    //    + " " + getText("common.currency.code") toanld2 bo don vi
    public String convertNumberUsingCurrentLocate(Number number) {
        return DataUtil.convertNumberUsingCurrentLocale(number);
    }

    public Date getSysDate(EntityManager em) throws Exception {
        return DbUtil.getSysDate(em);
    }

    public Date getTruncSysdate(EntityManager em) throws Exception {
        return DbUtil.getTruncSysdate(em);
    }

    public Long getSequence(EntityManager em, String sequence) throws Exception {
        return DbUtil.getSequence(em, sequence);
    }

    /**
     * Bien cac ki tu dac biet ve dang ascii
     *
     * @param input
     * @return
     */
    public String convertCharacter(String input) {
        if (DataUtil.isNullOrEmpty(input)) {
            return "";
        }
        return DataUtil.stripAccents(input);
    }

    public String toLowercaseVietnamese(String input) {
        String temp = convertCharacter(input);
        return StringUtils.lowerCase(temp);
    }

    /**
     * tao logic exception
     *
     * @param system
     * @param errorCode
     * @param key
     * @param param
     * @return
     * @author ThanhNT
     */
    protected LogicException logicExceptionType(String system, String errorCode, String key, Object... param) {
        LogicException logicException = new LogicException(errorCode, key, param);
        logicException.setSystem(system);
        return logicException;
    }
}
