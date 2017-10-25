package com.viettel.web.activity.favorite;

import akka.actor.ActorRef;
import com.viettel.fw.SystemConfig;
import com.viettel.fw.passport.CustomConnector;
import com.viettel.web.activity.UserActivity;
import com.viettel.web.activity.UserActivityService;
import org.omnifaces.util.Faces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import viettel.passport.client.UserToken;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by thiendn1 on 9/12/2015.
 */

@Component
@ManagedBean
@Scope("session")
public class FavoriteActivityController {
    @Autowired(required = false)
    UserActivityService userActivityService;

    @Value("${defaultSys}")
    String defaultSyste;


    UserToken user;

    @Autowired
    @Qualifier(SystemConfig.FAVORITE_ACTOR)
    protected ActorRef favoriteActor;

    List<UserActivity> activeFavoriteActivities = new ArrayList<>();
    List<UserActivity> inactiveFavoriteActivities = new ArrayList<>();

    @Autowired
    SystemConfig systemConfig;

    @PostConstruct
    public void init() {
        if (!systemConfig.IS_RUN_FAVORITE) {
            return;
        }
        user = Faces.getSessionAttribute(CustomConnector.VSA_USER_TOKEN);
        try {
            List<UserActivity> favoriteActivities = userActivityService.getFavoriteActivity(user.getUserName(), defaultSyste, 1000);
            for (int j = 0; j < favoriteActivities.size(); j++) {
                UserActivity user = favoriteActivities.get(j);
                if (user.getStatus() == UserActivityService.STATUS_ACTIVE) {
                    activeFavoriteActivities.add(user);
                } else {
                    inactiveFavoriteActivities.add(user);
                }
            }
        } catch (Exception e) {
            activeFavoriteActivities = new ArrayList<>();
        }
    }

    @PreDestroy
    protected void destroy() {
        List<UserActivity> userActivities = new ArrayList<>();
        userActivities.addAll(activeFavoriteActivities);
        userActivities.addAll(inactiveFavoriteActivities);
        if (!userActivities.isEmpty()) {
            favoriteActor.tell(new FavoriteActor.Exit(userActivities), null);
        }
    }


    public void addFavorite() {
        Map<String, String> params = Faces.getRequestParameterMap();
        String href = params.get("href");
        //remove contextPath
        String contextPath = Faces.getRequestContextPath();
        String url = null;
        if(href.startsWith(contextPath+"/")){
            url = href.substring(contextPath.length(), href.length());
        }
        else{
            url = href;

        }
        //kiem tra xem duong dan da ton tai hay chua
        UserActivity existedAcitity = isURLExisted(activeFavoriteActivities, url);
        if (existedAcitity != null) {
            existedAcitity.setCreateDate(new Date());
            activeFavoriteActivities.remove(existedAcitity);
            activeFavoriteActivities.add(0, existedAcitity);
            return;
        }
        //kiem tra xem duong dan da ton tai trong toan bo danh sach hay chua

        existedAcitity = isURLExisted(inactiveFavoriteActivities, url);
        if (existedAcitity != null) {
            existedAcitity.setCreateDate(new Date());
            //active activity
            existedAcitity.setStatus(UserActivityService.STATUS_ACTIVE);
            inactiveFavoriteActivities.remove(existedAcitity);
            activeFavoriteActivities.add(0, existedAcitity);
            return;
        }

        String userName = user.getUserName();
        UserActivity userActivity = new UserActivity();
        userActivity.setUserId(userName);
        userActivity.setUrl(url);
        userActivity.setCreateDate(new Date(System.currentTimeMillis()));
        userActivity.setProject(defaultSyste);
        userActivity.setUrlDescription(UserActivityService.URL_FAVORITE);
        activeFavoriteActivities.add(0, userActivity);
    }

    public void delete(String url) {
        UserActivity existedAcitity = isURLExisted(activeFavoriteActivities, url);
        existedAcitity.setStatus(UserActivityService.STATUS_INACTIVE);
        inactiveFavoriteActivities.add(existedAcitity);
        activeFavoriteActivities.remove(existedAcitity);
    }

    private UserActivity isURLExisted(List<UserActivity> favoriteActivities, String url) {
        for (UserActivity userActivity : favoriteActivities) {
            if (userActivity.getUrl().equals(url)) {
                return userActivity;
            }
        }
        return null;
    }


    public List<UserActivity> getSubFavoriteActivities() {
        List<UserActivity> subFavorites = new ArrayList<>();
        int limit = 0;
        for (int j = 0; j < activeFavoriteActivities.size(); j++) {
            UserActivity user = activeFavoriteActivities.get(j);
            if (user.getStatus() == UserActivityService.STATUS_ACTIVE) {
                subFavorites.add(user);
                if (++limit >= 5) {
                    break;
                }
            }
        }
        return subFavorites;
    }

    public List<UserActivity> getActiveFavoriteActivities() {
        return activeFavoriteActivities;
    }

    public void setActiveFavoriteActivities(List<UserActivity> activeFavoriteActivities) {
        this.activeFavoriteActivities = activeFavoriteActivities;
    }
}
