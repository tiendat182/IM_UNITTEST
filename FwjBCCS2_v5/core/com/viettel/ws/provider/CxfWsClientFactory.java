package com.viettel.ws.provider;

import com.google.common.collect.Lists;
import com.viettel.fw.SystemConfig;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.ws.common.utils.InjectGenericWebInfoInterceptor;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.clustering.FailoverFeature;
import org.apache.cxf.clustering.FailoverStrategy;
import org.apache.cxf.clustering.RandomStrategy;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.feature.Feature;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.apache.wss4j.common.ext.WSPasswordCallback;
import org.apache.wss4j.dom.WSConstants;
import org.apache.wss4j.dom.handler.WSHandlerConstants;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import java.io.IOException;
import java.util.*;

/**
 * Created by LamNV5 on 4/27/2015.
 */
public class CxfWsClientFactory implements CallbackHandler {
    private Map<String, WsEndpoint> wsEndpointMap;
    private List<String> keys;

    @Autowired
    LoggingInInterceptor loggingInInterceptor;

    @Autowired
    LoggingOutInterceptor loggingOutInterceptor;

    @Autowired
    InjectGenericWebInfoInterceptor injectGenericWebInfoInterceptor; //new InjectGenericWebInfoInterceptor();

    @Autowired
    SystemConfig systemConfig;


    public CxfWsClientFactory() {
    }

    @PostConstruct
    public void init() {

    }

    /**
     * Callback handle for password verify
     *
     * @param callbacks
     * @throws IOException
     * @throws UnsupportedCallbackException
     */
    public void handle(Callback[] callbacks) throws IOException,
            UnsupportedCallbackException {
        WSPasswordCallback pc = (WSPasswordCallback) callbacks[0];

        for (WsEndpoint wsEndpoint : wsEndpointMap.values()) {
            if (pc.getIdentifier().equals(wsEndpoint.getUserName())) {
                pc.setPassword(wsEndpoint.getPassword());
            }
        }
    }


    /**
     * Tao ra cxf-ws-client trong truong hop ServiceInterace va ServiceImpl khac package
     *
     * @param interfaceClass
     * @param <T>
     * @return
     * @throws Exception
     */
    public <T> T createWsClient(Class<T> interfaceClass) throws Exception {

        WsEndpoint wsEndpoint = getWsEndpoint(interfaceClass);
        if (wsEndpoint == null) {
            throw new WsEndpointNotFound(interfaceClass.getName());
        }
        return createWsClient(interfaceClass, wsEndpoint);
    }

    public <T> T createWsClient(Class<T> interfaceClass, String enpointKey) throws Exception {

        WsEndpoint wsEndpoint = wsEndpointMap.get(enpointKey);
        getWsEndpoint(interfaceClass);

        return createWsClient(interfaceClass, wsEndpoint);

    }

    private <T> T createWsClient(Class<T> interfaceClass, WsEndpoint wsEndpoint) throws Exception {
        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setServiceClass(interfaceClass);
        Map<String, Object> properties = factory.getProperties();
        if (properties == null) {
            properties = new HashMap<String, Object>();
        }

        properties.put("set-jaxb-validation-event-handler", "false");
        factory.setProperties(properties);

        factory.setAddress(wsEndpoint.getAddressConnectBean(interfaceClass.getSimpleName()));

        //R_XX START: clustering-failover
        if (!DataUtil.isNullOrEmpty(wsEndpoint.getAlternateAddressList())) {
            FailoverFeature failoverFeature = new FailoverFeature();
            RandomStrategy failoverStrategy = new RandomStrategy();
            failoverStrategy.setAlternateAddresses(wsEndpoint.getAlternateAddressConnectBean(interfaceClass.getSimpleName()));

            failoverFeature.setStrategy(failoverStrategy);
            factory.getFeatures().add(failoverFeature);
        }
        //R_XX END

        //Neu co cau hinh username => dang ky
        if (StringUtils.isNotBlank(wsEndpoint.getUserName())) {
            Map<String, Object> outProps = new HashMap<>();
            outProps.put(WSHandlerConstants.ACTION, WSHandlerConstants.USERNAME_TOKEN);
            // Specify our username
            outProps.put(WSHandlerConstants.USER, wsEndpoint.getUserName());
            // Password type : plain text
            if (WsEndpoint.PW_TEXT.equals(wsEndpoint.getPasswordType()) || WsEndpoint.PW_NONE.equals(wsEndpoint.getPasswordType())) {
                outProps.put(WSHandlerConstants.PASSWORD_TYPE, WSConstants.PW_TEXT);

            } else if (WsEndpoint.PW_DIGEST.equals(wsEndpoint.getPasswordType())) {
                outProps.put(WSHandlerConstants.PASSWORD_TYPE, WSConstants.PW_DIGEST);
            }
            // for hashed password use:
            //properties.put(WSHandlerConstants.PASSWORD_TYPE, WSConstants.PW_DIGEST);
            // Callback used to retrieve password for given user.
            outProps.put(WSHandlerConstants.PW_CALLBACK_REF, this);
            if (wsEndpoint.isIgnoreSecurity()) {
                outProps.put(WSHandlerConstants.MUST_UNDERSTAND, "false");
            }
            WSS4JOutInterceptor wssOut = new WSS4JOutInterceptor(outProps);
            factory.getOutInterceptors().add(wssOut);
        }

        //Logging in
//        factory.getInInterceptors().add(loggingInInterceptor);
        //Logging out
//        factory.getOutInterceptors().add(loggingOutInterceptor);
        //Inject generic web info
        //Gzip uncompress interceptor
        factory.getInInterceptors().add(new org.apache.cxf.transport.common.gzip.GZIPInInterceptor());
        //MinhNH
        if (wsEndpoint.isInjectGenericWebInfo()) {
            factory.getOutInterceptors().add(injectGenericWebInfoInterceptor);
        }

        T serviceClient = (T) factory.create();

        //Compress data START, 30/05/2015
        Client proxy = ClientProxy.getClient(serviceClient);
        HTTPConduit http = (HTTPConduit) proxy.getConduit();
        HTTPClientPolicy httpClientPolicy = new HTTPClientPolicy();

        httpClientPolicy.setAllowChunking(false);
        httpClientPolicy.setAccept("text/xml");
        httpClientPolicy.setAcceptEncoding("gzip, deflate"); //gzip,deflate,sdch
        httpClientPolicy.setCacheControl("No-Cache");
        httpClientPolicy.setContentType("text/xml");

        //thiendn1: set time out
        if (wsEndpoint.getConnectionTimeout() > 0) {
            httpClientPolicy.setConnectionTimeout(wsEndpoint.getConnectionTimeout());
        }
        if (wsEndpoint.getReceiveTimeout() > 0) {
            httpClientPolicy.setReceiveTimeout(wsEndpoint.getReceiveTimeout());
        }
        http.setClient(httpClientPolicy);
        //Compress data END

        return serviceClient;
    }

    /**
     * @param callClass
     * @return com.viettel.sale => ep1
     * com.viettel.sale.connect => ep2
     * com.viettel.sale.connect.InputSubImpl => ep3
     * <p>
     * input: com.viettel.sale.trans.SaleTransImpl
     * output: ep1
     * <p>
     * input: com.viettel.sale.connect.InputCustImpl
     * output: ep2
     * <p>
     * input: com.viettel.sale.connect.InputSubImpl
     * output: ep3
     */
    private WsEndpoint getWsEndpoint(Class callClass) {
        String classPath = callClass.getName();
        for (String key : keys) {
            if (classPath.startsWith(key))
                return wsEndpointMap.get(key);
        }

        return null;
    }

    public Map<String, WsEndpoint> getWsEndpointMap() {
        return wsEndpointMap;
    }

    public void setWsEndpointMap(Map<String, WsEndpoint> wsEndpointMap) {
        this.wsEndpointMap = wsEndpointMap;
        keys = Lists.newArrayList(wsEndpointMap.keySet());
        Collections.sort(keys, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o2.compareTo(o1);
            }
        });
    }
}
