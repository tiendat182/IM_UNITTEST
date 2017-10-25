package com.viettel.ws.provider;

import com.google.common.collect.Lists;
import com.viettel.ws.common.utils.WsRequestCreator;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Created by vtsoft on 4/3/2015.
 */
public class WsCallerFactory {
    private Map<String, WsEndpoint> wsEndpointMap;
    private List<String> keys;

    public WsCallerFactory() {
    }

    /**
     * Ham tao WSCaller mac dinh voi fixAddress = false
     *
     * @param callClass
     * @param function
     * @return
     * @throws Exception
     */
    public WsRequestCreator createWsCaller(Class callClass, String function) throws Exception {
        return createWsCaller(callClass, function, false);
    }

    public WsRequestCreator createWsCaller(String endpointKey, String function) throws Exception {
        return createWsCaller(getWsEndpoint(endpointKey),null, function);
    }
    public WsRequestCreator createWsCaller(String endpointKey,String service, String function) throws Exception {
        WsEndpoint wsEndpoint = getWsEndpoint(endpointKey);
        return createWsCaller(wsEndpoint,service, function);
    }
    
    public WsRequestCreator createWsCaller(Class callClass, String function, boolean fixAddress) throws Exception {
        WsEndpoint wsEndpoint = getWsEndpoint(callClass);
        if (wsEndpoint == null) {
            throw new WsEndpointNotFound(callClass.getName());
        }
        String address = null;
        if (!wsEndpoint.getAddress().endsWith("wsdl")) {
            if (fixAddress) {
                address = wsEndpoint.getAddress() + "?wsdl";
            } else {
                address = wsEndpoint.getAddress() + callClass.getSimpleName() + "?wsdl";
            }
        } else {
            address = wsEndpoint.getAddress();
        }
        WsRequestCreator requestCreator = createWsCaller(wsEndpoint,null, function);
        requestCreator.setWsAddress(address);
        return requestCreator;
    }


    public WsRequestCreator createWsCaller(WsEndpoint wsEndpoint,String service, String function) throws Exception {
        if (wsEndpoint == null) {
            throw new WsEndpointNotFound(wsEndpoint.getName());
        }
        ///ProductSpecCharacterServiceImpl?wsdl
        WsRequestCreator requestCreator = new WsRequestCreator();
        if(service==null){
            requestCreator.setWsAddress(wsEndpoint.getAddress());
        }
        else{
            if(wsEndpoint.getAddress().endsWith("/")){
                requestCreator.setWsAddress(wsEndpoint.getAddress()+service+"?wsdl");

            }
            else{
                requestCreator.setWsAddress(wsEndpoint.getAddress()+"/"+service+"?wsdl");

            }
        }
        requestCreator.setTargetNameSpace(wsEndpoint.getTargetNameSpace());
        requestCreator.setServiceName(function);
        requestCreator.setUsername(wsEndpoint.getUserName());
        requestCreator.setPassword(wsEndpoint.getPassword());
        requestCreator.setConnectionTimeout(wsEndpoint.getReceiveTimeout());
        requestCreator.setReceiveTimeout(wsEndpoint.getReceiveTimeout());
        return requestCreator;
    }

    /**
     * @param callClass
     * @return com.viettel.sale => ep1
     * com.viettel.sale.connect => ep2
     * com.viettel.sale.connect.InputSubImpl => ep3
     * <p/>
     * input: com.viettel.sale.trans.SaleTransImpl
     * output: ep1
     * <p/>
     * input: com.viettel.sale.connect.InputCustImpl
     * output: ep2
     * <p/>
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

    private WsEndpoint getWsEndpoint(String keyName) {
        return wsEndpointMap.get(keyName);
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

    public WsRequestCreator createWsCaller(String callClass, String function, boolean fixAddress) throws Exception {

        WsEndpoint wsEndpoint = getWsEndpoint(callClass);
        if (wsEndpoint == null) {
            throw new WsEndpointNotFound(callClass);
        }

        ///ProductSpecCharacterServiceImpl?wsdl
        WsRequestCreator requestCreator = new WsRequestCreator();

        if (wsEndpoint.getAddress().endsWith("wsdl")) {
            requestCreator.setWsAddress(wsEndpoint.getAddress());
        } else {
            if (fixAddress) {
                requestCreator.setWsAddress(wsEndpoint.getAddress() + "?wsdl");
            } else {
                requestCreator.setWsAddress(wsEndpoint.getAddress() + callClass + "?wsdl");
            }
        }
        requestCreator.setTargetNameSpace(wsEndpoint.getTargetNameSpace());
        requestCreator.setServiceName(function);
        requestCreator.setUsername(wsEndpoint.getUserName());
        requestCreator.setPassword(wsEndpoint.getPassword());

        return requestCreator;
    }
}
