package com.viettel.fw.logging;

import com.viettel.fw.bean.ApplicationContextProvider;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.GetTextFromBundleHelper;
import org.apache.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by PM2-LAMNV5 on 5/23/2016.
 */
public class PerformanceProxy implements java.lang.reflect.InvocationHandler {
    private ThreadLocal callIdLocal = new ThreadLocal<String>();

    protected static Logger logger4J = Logger.getLogger("performanceLogger");
    protected static Logger logger4Jerr = Logger.getLogger("errorLogger");

    private Object obj;
    private String originClassName;
    private TraceInterceptor traceInterceptor;

    public static Object newInstance(String originClassName, Object obj) {
        return java.lang.reflect.Proxy.newProxyInstance(
                obj.getClass().getClassLoader(),
                obj.getClass().getInterfaces(),
                new PerformanceProxy(originClassName, obj));
    }

    private PerformanceProxy(String originClassName, Object obj) {
        traceInterceptor = ApplicationContextProvider.getApplicationContext().getBean(TraceInterceptor.class);
        this.originClassName = originClassName;
        this.obj = obj;
    }

    public Object invoke(Object proxy, Method m, Object[] args)
            throws Throwable {
        Object result = null;
        String message = "";
        String callId = "";
        long startTime = 0;
        Throwable ex = null;
        try {
            callId = GetTextFromBundleHelper.getReqId();
            if (DataUtil.isNullOrEmpty(callId)) {
                callId = (String) callIdLocal.get();
                if (DataUtil.isNullOrEmpty(callId)) {
                    callId = String.valueOf(System.currentTimeMillis());
                    callIdLocal.set(callId);
                }
            }

            message = callId + "|Entering|" + originClassName + "." + m.getName() + "|0|0|0|";
            logger4J.info(message);
            startTime = System.currentTimeMillis();
            result = m.invoke(obj, args);

        } catch (InvocationTargetException e) {
            ex = e.getTargetException();
            ex = new Exception("[" + callId + "]:" + ex.getMessage(), ex);
            logger4Jerr.error(ex);

            throw e.getTargetException();
        } catch (Exception e) {
            logger4Jerr.error(e);
            throw ex;
        } finally {
            long invocationTime = System.currentTimeMillis() - startTime;
            message = callId + "|Leaving|" + originClassName + "." + m.getName() + "|" + invocationTime;
            if (invocationTime > traceInterceptor.getExceedTimeWarring()) {
                if (invocationTime > traceInterceptor.getExceedExtraWarring()) {
                    message += traceInterceptor.getExceedTimeWarringMsg() + "-EXTRA";
                } else {
                    message += traceInterceptor.getExceedTimeWarringMsg();
                }
            } else {
                message += "|";
            }

            if (m.getReturnType() == void.class) {
                message += "|"; //"void"
            } else if (result == null) {
                message += "|";
            } else {
                //Chi fill so luong ban ghi khi return type la list
                if (m.getReturnType() == List.class) {
                    int qty = ((List) result).size();
                    message += ((qty <= traceInterceptor.getExceedQtyWarring()) ? "|" : traceInterceptor.getExceedQtyWarringMsg());
                } else {
                    message += "|";
                }
            }

            if (ex != null) {
                message += "|" + ex.getMessage();
            } else {
                message += "|";
            }

            logger4J.info(message);
        }

        return result;
    }
}