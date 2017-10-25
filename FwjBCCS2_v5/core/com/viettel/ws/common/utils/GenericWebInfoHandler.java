package com.viettel.ws.common.utils;

import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.GetTextFromBundleHelper;
import org.apache.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by LamNV5 on 5/26/2015.
 */
public class GenericWebInfoHandler implements SOAPHandler<SOAPMessageContext> {
    public static final Logger logger = Logger.getLogger(GenericWebInfoHandler.class);

    private static final QName HEADER_TYPE_GENERIC_WEB_INFO =
            new QName("http://service.ws.viettel.com/", "genericWebInfo");

    private static final Set<QName> HEADER_TYPES =
            new HashSet<QName>(Arrays.asList(new QName[]{HEADER_TYPE_GENERIC_WEB_INFO}));

    private JAXBContext jaxbContext = null;

    public GenericWebInfoHandler() {
        try {
            jaxbContext = JAXBContext.newInstance(GenericWebInfo.class);
        } catch(JAXBException eJaxb) {
            eJaxb.printStackTrace();
        }
    }

    @Override
    public void close(MessageContext messageContext) {
    }

    @Override
    public boolean handleMessage(SOAPMessageContext messageContext) {
        Object[] matchingHeaders = messageContext.getHeaders(HEADER_TYPE_GENERIC_WEB_INFO,
                jaxbContext, true);
        GenericWebInfo headerInfo = null;
        if (matchingHeaders != null && matchingHeaders.length == 1) {
            headerInfo = (GenericWebInfo) matchingHeaders[0];
        }

        if (headerInfo == null) {
            headerInfo = new GenericWebInfo();
            headerInfo.setReqId(DataUtil.getRandomKpiId());
            headerInfo.setLanguage("vi");
            headerInfo.setCountry("VN");
        }

        if (DataUtil.isNullOrEmpty(headerInfo.getReqId())) {
            headerInfo.setReqId(DataUtil.getRandomKpiId());
        }

        //Day vao ThreadContext cua Log4j2
        ThreadContext.put("kpi", headerInfo.getReqId());
        //Day vao ThreadLocal dung khi GetTextFromBundleHelper.getGenericWebInfo
        GetTextFromBundleHelper.setGenericWebInfo(headerInfo);

        return true;
    }

    @Override
    public Set<QName> getHeaders() {
        return HEADER_TYPES;
    }

    @Override
    public boolean handleFault(SOAPMessageContext messageContext) {
        return true;
    }
}
