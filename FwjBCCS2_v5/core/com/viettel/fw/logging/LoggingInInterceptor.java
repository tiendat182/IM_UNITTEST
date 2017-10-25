/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.viettel.fw.logging;

import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.GetTextFromBundleHelper;
import org.apache.cxf.common.injection.NoJSR250Annotations;
import org.apache.cxf.interceptor.LoggingMessage;


/**
 * A simple logging handler which outputs the bytes of the message to the
 * Logger.
 */
@NoJSR250Annotations
public class LoggingInInterceptor extends org.apache.cxf.interceptor.LoggingInInterceptor {
    @Override
    protected String formatLoggingMessage(LoggingMessage loggingMessage) {
        String message = loggingMessage.toString();
        String callId = GetTextFromBundleHelper.getReqId();
        if (!DataUtil.isNullOrEmpty(callId)) {
            message = "[" + callId  + "]:" + message;
        }

        return message;
    }

}
