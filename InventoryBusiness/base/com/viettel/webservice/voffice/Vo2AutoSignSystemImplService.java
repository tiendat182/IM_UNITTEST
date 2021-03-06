
package com.viettel.webservice.voffice;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "Vo2AutoSignSystemImplService", targetNamespace = "http://service.ws_autosign.voffice.viettel.com/", wsdlLocation = "http://192.168.176.68:8868/WS_AutoSign/Vo2AutoSignSystemImpl?wsdl")
public class Vo2AutoSignSystemImplService
    extends Service
{

    private final static URL VO2AUTOSIGNSYSTEMIMPLSERVICE_WSDL_LOCATION;
    private final static WebServiceException VO2AUTOSIGNSYSTEMIMPLSERVICE_EXCEPTION;
    private final static QName VO2AUTOSIGNSYSTEMIMPLSERVICE_QNAME = new QName("http://service.ws_autosign.voffice.viettel.com/", "Vo2AutoSignSystemImplService");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("http://192.168.176.68:8868/WS_AutoSign/Vo2AutoSignSystemImpl?wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        VO2AUTOSIGNSYSTEMIMPLSERVICE_WSDL_LOCATION = url;
        VO2AUTOSIGNSYSTEMIMPLSERVICE_EXCEPTION = e;
    }

    public Vo2AutoSignSystemImplService() {
        super(__getWsdlLocation(), VO2AUTOSIGNSYSTEMIMPLSERVICE_QNAME);
    }

    public Vo2AutoSignSystemImplService(WebServiceFeature... features) {
        super(__getWsdlLocation(), VO2AUTOSIGNSYSTEMIMPLSERVICE_QNAME, features);
    }

    public Vo2AutoSignSystemImplService(URL wsdlLocation) {
        super(wsdlLocation, VO2AUTOSIGNSYSTEMIMPLSERVICE_QNAME);
    }

    public Vo2AutoSignSystemImplService(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, VO2AUTOSIGNSYSTEMIMPLSERVICE_QNAME, features);
    }

    public Vo2AutoSignSystemImplService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public Vo2AutoSignSystemImplService(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns Vo2AutoSignSystemImpl
     */
    @WebEndpoint(name = "Vo2AutoSignSystemImplPort")
    public Vo2AutoSignSystemImpl getVo2AutoSignSystemImplPort() {
        return super.getPort(new QName("http://service.ws_autosign.voffice.viettel.com/", "Vo2AutoSignSystemImplPort"), Vo2AutoSignSystemImpl.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns Vo2AutoSignSystemImpl
     */
    @WebEndpoint(name = "Vo2AutoSignSystemImplPort")
    public Vo2AutoSignSystemImpl getVo2AutoSignSystemImplPort(WebServiceFeature... features) {
        return super.getPort(new QName("http://service.ws_autosign.voffice.viettel.com/", "Vo2AutoSignSystemImplPort"), Vo2AutoSignSystemImpl.class, features);
    }

    private static URL __getWsdlLocation() {
        if (VO2AUTOSIGNSYSTEMIMPLSERVICE_EXCEPTION!= null) {
            throw VO2AUTOSIGNSYSTEMIMPLSERVICE_EXCEPTION;
        }
        return VO2AUTOSIGNSYSTEMIMPLSERVICE_WSDL_LOCATION;
    }

}
