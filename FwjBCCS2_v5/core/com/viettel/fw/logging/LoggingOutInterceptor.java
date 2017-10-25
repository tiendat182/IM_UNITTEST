package com.viettel.fw.logging;

import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.GetTextFromBundleHelper;
import org.apache.cxf.interceptor.LoggingMessage;

/**
 * Created by PM2-LAMNV5 on 4/13/2016.
 */
public class LoggingOutInterceptor extends org.apache.cxf.interceptor.LoggingOutInterceptor {
    @Override
    protected String formatLoggingMessage(LoggingMessage buffer) {
        String message = buffer.toString();
        String callId = GetTextFromBundleHelper.getReqId();
        if (!DataUtil.isNullOrEmpty(callId)) {
            message = "[" + callId + "]:" + message;
        }

        return message;
    }
}
