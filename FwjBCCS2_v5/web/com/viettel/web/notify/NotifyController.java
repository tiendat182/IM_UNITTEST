package com.viettel.web.notify;

import com.viettel.fw.SystemConfig;
import com.viettel.fw.common.util.BundleUtil;
import com.viettel.fw.common.util.GetTextFromBundleHelper;
import com.viettel.fw.passport.CustomConnector;
import com.viettel.web.common.controller.BaseController;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.omnifaces.util.Faces;
import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import viettel.passport.client.UserToken;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by vtsoft on 5/1/2015.
 */

@Component
@ManagedBean
@Scope("view")
public class NotifyController extends BaseController {
    public static final Logger logger = Logger.getLogger(NotifyController.class);
    private int MAX_NOTIFY = 10;

    @Autowired
    ObjectMapper mapper;
    @Autowired(required = false)
    NotifyService notifyService;
    private long countNotify = 0;
    private UserToken user;

    private List<NotifyContent> unreadNotifiContents;
    private NotifyContent selectedNotify;

    @Autowired
    SystemConfig systemConfig;

    @PostConstruct
    public void init() {
        MAX_NOTIFY= systemConfig.MAX_SHOW_NOTIFY;
        user = Faces.getSessionAttribute(CustomConnector.VSA_USER_TOKEN);
        loadAllUnreadNotify();
    }

    public void makePush() {
        notifyService.makePush("product", "Test", "Test");
    }

    public void loadAllUnreadNotify() {
        if (systemConfig.IS_RUN_NOTIFY) {
            try {
                Object[] inits = notifyService.initGetUnreadNotifies(user.getUserName(), MAX_NOTIFY);
                countNotify = (long) inits[0];
                unreadNotifiContents = (List<NotifyContent>) inits[1];
            } catch (Exception e) {
                logger.error(e);
            }
        }
    }

    public void handleMessage() throws Exception {
        String[] jsonContent = Faces.getRequestParameter("contentData").split(",");
        byte[] jsonBytes = new byte[jsonContent.length];
        for(int i=0;i<jsonBytes.length;i++){
            jsonBytes[i] = Byte.parseByte(jsonContent[i]);
        }
        NotifyContent notifyContent = mapper.readValue(jsonBytes, NotifyContent.class);
        if(!notifyContent.getTo().equals(user.getUserName())){
            return;
        }
        countNotify++;
        if(notifyContent.getCreatedDate()==null){
            notifyContent.setCreatedDate(new Date(notifyContent.getDateTime()));
        }
        StringBuilder contentBuilder = new StringBuilder();
        contentBuilder.append(GetTextFromBundleHelper.getText(notifyContent.getText()));
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(GetTextFromBundleHelper.getText(notifyContent.getSubject()), contentBuilder.toString()));
        RequestContext.getCurrentInstance().update("pushGrowl");
        unreadNotifiContents.add(0, notifyContent);
        for (int i = 10; i < unreadNotifiContents.size(); i++) {
            unreadNotifiContents.remove(i);
        }
    }



    public void loadNotify() throws Exception {
        Map<String, String> params = Faces.getRequestParameterMap();
        String id = params.get("id");
        for (int i = 0; i < unreadNotifiContents.size(); i++) {
            if (unreadNotifiContents.get(i).getId().equals(id)) {
                selectedNotify = unreadNotifiContents.get(i);
                return;
            }
        }
        FacesContext.getCurrentInstance().validationFailed();

    }
    public void removeNotify() throws Exception {
        Map<String, String> params = Faces.getRequestParameterMap();
        String id = params.get("id");
        for (int i = 0; i < unreadNotifiContents.size(); i++) {
            if (unreadNotifiContents.get(i).getId().equals(id)) {
                unreadNotifiContents.remove(i);
                countNotify--;
                notifyService.deleteNotify(id);
                return;
            }
        }
        FacesContext.getCurrentInstance().validationFailed();
    }


    public long getCountNotify() {
        return countNotify;
    }

    public void setCountNotify(long countNotify) {
        this.countNotify = countNotify;
    }

    public List<NotifyContent> getUnreadNotifiContents() {
        return unreadNotifiContents;
    }

    public void setUnreadNotifiContents(List<NotifyContent> unreadNotifiContents) {
        this.unreadNotifiContents = unreadNotifiContents;
    }

    public NotifyContent getSelectedNotify() {
        return selectedNotify;
    }
}
