package com.viettel.bccs.inventory.common;

import com.viettel.fw.common.util.DataUtil;
import org.apache.log4j.Logger;
import sendmt.MtStub;

/**
 * @author luannt23.
 * @comment
 * @date 3/5/2016.
 */
public class SmsClient {
    private String url;
    private String xmlns;
    private String user;
    private String password;
    private String sessionId;
    private String serviceId;
    private String sender;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getXmlns() {
        return xmlns;
    }

    public void setXmlns(String xmlns) {
        this.xmlns = xmlns;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    private Logger logger = Logger.getLogger(SmsClient.class);

    public int sendSMS(String receiver, String content) {
        if (receiver == null || DataUtil.safeEqual(receiver.trim(), "")) {
            return 0;
        }
        try {
            MtStub stub = stubGenerate();
            receiver = receiver.trim();
            if (receiver.startsWith("0")) {
                receiver = "84" + receiver.substring(1);
            } else if (!receiver.startsWith("84")) {
                receiver = "84" + receiver;
            }
            return stub.send(sessionId, serviceId, sender, receiver, "0", content, "1");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return 0;
    }

    public MtStub stubGenerate() {
        MtStub stub = new MtStub(url, xmlns, user, password);
        return stub;
    }
}
