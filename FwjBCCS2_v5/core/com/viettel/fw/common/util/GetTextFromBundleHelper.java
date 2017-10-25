package com.viettel.fw.common.util;

import com.viettel.fw.dto.UserDTO;
import com.viettel.ws.common.utils.GenericWebInfo;
import com.viettel.ws.common.utils.Locate;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.cxf.headers.Header;
import org.apache.cxf.jaxws.context.WebServiceContextImpl;
import org.apache.cxf.jaxws.context.WrappedMessageContext;
import org.apache.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.omnifaces.util.Faces;
import org.w3c.dom.Element;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.WebServiceContext;
import java.io.ByteArrayOutputStream;
import java.text.MessageFormat;
import java.util.List;

/**
 * Created by LamNV5 on 5/28/2015.
 */
public class GetTextFromBundleHelper {

    private static ThreadLocal<GenericWebInfo> webInfoContext = new ThreadLocal();
    public static final Logger logger = Logger.getLogger(GetTextFromBundleHelper.class);

    public static Locate getLocate() {
        try {
            GenericWebInfo genericWebInfo = getGenericWebInfo();
            if (DataUtil.isNullOrEmpty(genericWebInfo.getLanguage())) {
                genericWebInfo.setLanguage("vi");
                genericWebInfo.setCountry("VN");
            }
            return new Locate(genericWebInfo.getLanguage(), genericWebInfo.getCountry());
        } catch (Exception ex) {
            return new Locate("vi", "VN");
        }
    }

    public static void setGenericWebInfo(GenericWebInfo webInfo) {
        webInfoContext.set(webInfo);
    }

    public static GenericWebInfo getGenericWebInfo() {
        return getGenericWebInfo(false, null);
    }

    /**
     * Lay tu ThreadLocal duoc set vao tu buoc:
     * + Doi voi service: GenericWebInfoHandler.handleMessage
     * + Doi voi web: LoggingFilter.doFilterInternal
     * @param reset
     * @param kpiId
     * @return
     */
    public static GenericWebInfo getGenericWebInfo(boolean reset, String kpiId) {
        GenericWebInfo genericWebInfo = webInfoContext.get();
        if (genericWebInfo == null || reset) {
            genericWebInfo = new GenericWebInfo();
            genericWebInfo.setReqId(DataUtil.defaultIfNull(kpiId, DataUtil.getRandomKpiId()));
            genericWebInfo.setLanguage("vi");
            genericWebInfo.setCountry("VN");

            //Day vao thread context
            ThreadContext.put("kpi", genericWebInfo.getReqId());
            webInfoContext.set(genericWebInfo);
        }

        if (DataUtil.isNullOrEmpty(genericWebInfo.getReqId())) {
            genericWebInfo.setReqId(DataUtil.getRandomKpiId());
        }

        return genericWebInfo;
    }

    public static String getText(String key) {
        return BundleUtil.getText(getLocate(), key);
    }

    public static String getTextParam(String key, String... params) {
        return MessageFormat.format(getText(key), params);
    }

    public static String getReqId() {
        return getGenericWebInfo().getReqId();
    }
}
