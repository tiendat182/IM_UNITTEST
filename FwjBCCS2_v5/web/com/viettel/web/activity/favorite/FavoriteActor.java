package com.viettel.web.activity.favorite;

import akka.actor.UntypedActor;
import com.viettel.fw.SystemConfig;
import com.viettel.web.activity.UserActivity;
import com.viettel.web.activity.UserActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Thien on 22/11/2015.
 */

@Component(value = SystemConfig.FAVORITE_ACTOR_BEAN)
@Scope("prototype")
@Lazy
public class FavoriteActor extends UntypedActor {

    public static final class Exit {
        private List<UserActivity> list;

        public Exit(List<UserActivity> list) {
            this.list = list;
        }
    }

    @Autowired(required = false)
    UserActivityService userActivityService;

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof Exit) {
            userActivityService.commitActivities(((Exit) message).list);
        }
    }

}
