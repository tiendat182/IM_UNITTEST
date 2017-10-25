package com.viettel.web.activity;

import akka.actor.ActorSystem;
import com.google.common.collect.Lists;
import com.ocpsoft.pretty.PrettyContext;
import com.viettel.fw.SystemConfig;
import com.viettel.fw.common.util.Const;
import com.viettel.fw.passport.CustomConnector;
import com.viettel.web.common.MenuBean;
import org.omnifaces.util.Faces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import viettel.passport.client.UserToken;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Thien on 22/11/2015.
 */

@ManagedBean
@Scope("view")
@Component
public class ActivityListener {

    @Autowired(required = false)
    ActorSystem actorSystem;

    @Autowired
    MenuBean menuBean;

    @Autowired
    SystemConfig systemConfig;

    private String trigger;
    private List<String> excludeUrl = Lists.newArrayList("/", "/home");

    @PostConstruct
    public void init() {
        if (!systemConfig.IS_RUN_DASHBOARD) {
            return;
        }
        String requestURL = PrettyContext.getCurrentInstance().getRequestURL().toURL();
        if (excludeUrl.contains(requestURL)) {
            return;
        }
        String url = requestURL.replaceFirst("//", "");
        if (menuBean.getUrlName(url) == null) {
            //chi day cac duong dan duoc dat ten
            return;
        }
        String sessionId = Faces.getSessionId();
        UserToken userToken = Faces.getSessionAttribute(CustomConnector.VSA_USER_TOKEN);
        String userName = userToken.getUserName();
        UserActivity userActivity = new UserActivity();
        userActivity.setUserId(userName);
        userActivity.setUrl(url);
        userActivity.setUrlDescription(UserActivityService.URL_NORMAL);
        userActivity.setCreateDate(new Date(System.currentTimeMillis()));
        userActivity.setProject(systemConfig.DEFAULT_SYS);
        actorSystem.actorSelection("user/" + Const.ACTIVITY_ACTOR + sessionId).tell(new ActivityActor.Update(userActivity), null);
    }

    public String getTrigger() {
        return trigger;
    }
}
