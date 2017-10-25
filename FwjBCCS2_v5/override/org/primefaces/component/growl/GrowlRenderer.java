/*
 * Copyright 2009-2014 PrimeTek.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.primefaces.component.growl;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.renderkit.UINotificationRenderer;
import org.primefaces.util.HTML;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GrowlRenderer extends UINotificationRenderer {

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        Growl growl = (Growl) component;
        String clientId = growl.getClientId(context);
        String widgetVar = growl.resolveWidgetVar();

        writer.startElement("span", growl);
        writer.writeAttribute("id", clientId, "id");

        if (RequestContext.getCurrentInstance().getApplicationContext().getConfig().isClientSideValidationEnabled()) {
            writer.writeAttribute("class", "ui-growl-pl", null);
            writer.writeAttribute(HTML.WIDGET_VAR, widgetVar, null);
            writer.writeAttribute("data-global", growl.isGlobalOnly(), null);
            writer.writeAttribute("data-summary", growl.isShowSummary(), null);
            writer.writeAttribute("data-detail", growl.isShowDetail(), null);
            writer.writeAttribute("data-severity", getClientSideSeverity(growl.getSeverity()), null);
            writer.writeAttribute("data-redisplay", String.valueOf(growl.isRedisplay()), null);
        }

        writer.endElement("span");

        startScript(writer, clientId);

        writer.write("$(function(){");
        writer.write("PrimeFaces.cw('Growl','" + widgetVar + "',{");
        writer.write("id:'" + clientId + "'");
        writer.write(",sticky:" + growl.isSticky());
        writer.write(",life:" + growl.getLife());
        writer.write(",escape:" + growl.isEscape());

        writer.write(",msgs:");
        encodeMessages(context, growl);

        writer.write("});});");

        endScript(writer);
    }

    protected void encodeMessages(FacesContext context, Growl growl) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String _for = growl.getFor();
        Iterator<FacesMessage> messages;
        if (_for != null)
            messages = context.getMessages(_for);
        else
            messages = growl.isGlobalOnly() ? context.getMessages(null) : context.getMessages();


        List<FacesMessage> messageList = Lists.newArrayList(messages);
        Map<FacesMessage.Severity, List<FacesMessage>> messageMap = messageList.stream().
                collect(Collectors.groupingBy(FacesMessage::getSeverity));
        JsonArray messageArray = new JsonArray();
        for (Map.Entry<FacesMessage.Severity, List<FacesMessage>> severityFacesMessageEntry : messageMap.entrySet()) {
            List<FacesMessage> facesMessages = severityFacesMessageEntry.getValue();

            JsonObject currMsg = new JsonObject();
            String severityName = getSeverityNameFromEnum(severityFacesMessageEntry.getKey());
            facesMessages.stream()
                    .filter(message -> shouldRender(growl, message, severityName))
                    .forEach(message -> {
                        String summary = growl.isShowSummary() ? escapeText(message.getSummary()) : "";
                        String detail = growl.isShowDetail() ? escapeText(message.getDetail()) : "";
                        detail = StringUtils.replace(detail, "\\/", "/");
                        currMsg.addProperty("summary", summary);
                        currMsg.addProperty("detail", Joiner.on("\n").skipNulls().join(currMsg.get("detail") == null ? null : currMsg.get("detail").getAsString(), detail));
                        message.rendered();
                    });
            currMsg.addProperty("severity", severityName);
            messageArray.add(currMsg);
        }
        writer.write(messageArray.toString());
    }

    protected String getSeverityNameFromEnum(FacesMessage.Severity message) {
        String severity = null;
        if (message == FacesMessage.SEVERITY_INFO)
            severity = "info";
        else if (message == FacesMessage.SEVERITY_ERROR)
            severity = "error";
        else if (message == FacesMessage.SEVERITY_WARN)
            severity = "warn";
        else if (message == FacesMessage.SEVERITY_FATAL)
            severity = "fatal";

        return severity;
    }
}