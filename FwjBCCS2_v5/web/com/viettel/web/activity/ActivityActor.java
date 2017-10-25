package com.viettel.web.activity;

import akka.actor.UntypedActor;
import com.viettel.fw.SystemConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by Thien on 22/11/2015.
 */

@Component(value = SystemConfig.ACTIVITI_ACTOR_BEAN)
@Scope("prototype")
@Lazy
public class ActivityActor extends UntypedActor {


    public static final class Get {
    }

    ;

    public static final class Save {
    }

    ;

    public static final class Exit {
    }

    ;

    public static final class Init {

        List<Object> objects;

        public Init(List<Object> objects) {
            this.objects = objects;
        }
    }

    ;

    static final class Update {
        UserActivity activity;

        public Update(UserActivity activity) {
            this.activity = activity;
        }
    }

    ;


    List<Object> actionObjects = new ArrayList<>();

    @Autowired
    UserActivityService userActivityService;


    List<UserActivity> newUserActivities = new ArrayList<>();

    @Autowired
    SystemConfig systemConfig;

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof Init) {
            actionObjects = ((Init) message).objects;

        } else if (message instanceof Get) {
            getSender().tell(actionObjects, getSelf());
        } else if (message instanceof Save) {
            if (!newUserActivities.isEmpty()) {
                userActivityService.commitActivities(newUserActivities);
            }
        } else if (message instanceof Exit) {
            context().stop(self());
        } else if (message instanceof Update) {
            UserActivity userActivity = ((Update) message).activity;
            // check if action happens in the next day
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            if (userActivity.getCreateDate().getTime() > calendar.getTime().getTime() + 3600 * 24 * 1000) {
                // begin the new time
                if (!newUserActivities.isEmpty()) {
                    //flush storage
                    userActivityService.commitActivities(newUserActivities);
                }
                //then reset the data
                actionObjects = userActivityService.resetAction(userActivity.getUserId(), systemConfig.DEFAULT_SYS);
            } else {
                // insert new action into objects
                List<UserActivity> recentUrlActions = (List<UserActivity>) actionObjects.get(UserActivityService.INDEX_RECENT_ACTION);
                List<UserActivity> todayActionInGroup = (List<UserActivity>) actionObjects.get(UserActivityService.INDEX_TODAY_GROUP_ACTION);
                //reset dateTime if url is uplicated
                if (recentUrlActions.isEmpty()) {
                    recentUrlActions.add(0, userActivity);
                    userActivity.setGroup(1);
                    todayActionInGroup.add(0, userActivity);
                } else {
                    //check pre action
                    //neu action bi trung lap, chi ghi nhan gia tri moi nhat
                    UserActivity preActivity = null;
                    for (UserActivity pre : recentUrlActions) {
                        if (pre.getUrl().equals(userActivity.getUrl())) {
                            preActivity = pre;
                            recentUrlActions.remove(pre);
                            recentUrlActions.add(0,preActivity);
                            break;
                        }
                    }
                    if (preActivity != null) {
                        //action da ton tai, ghi nhan vao trong ket qua hien tai + trong thang
                        preActivity.setCreateDate(userActivity.getCreateDate());
                    } else {
                        userActivity.setGroup(1);
                        preActivity = userActivity;
                        recentUrlActions.add(0, userActivity);
                    }
                    //kiem tra xem action da xuat hien trong nhom cau hinh hay chua
                    UserActivity groupActivity = getUserActivity(todayActionInGroup,preActivity.getUrl());
                    //da xuat hien trong group, cong gia tri them 1
                    if(groupActivity!=null){
                        groupActivity.setGroup(groupActivity.getGroup()+1);
                    }
                    else{
                        //chua xuat hien trong group.
                        //neu danh sach grop chua du 10, bo sung url vao cuoi danh sach
                        if(todayActionInGroup.size()<UserActivityService.MAX_ROW){
                            todayActionInGroup.add(preActivity);
                            Collections.sort(todayActionInGroup, new Comparator<UserActivity>() {
                                @Override
                                public int compare(UserActivity o1, UserActivity o2) {
                                    return o1.getGroup() > o2.getGroup() ? -1 : 1;
                                }
                            });
                        }
                    }
                }
                Map<String, Long> currentFacetAction = (Map<String, Long>) actionObjects.get(UserActivityService.INDEX_NOW_ACTION);
                if (currentFacetAction.containsKey(userActivity.getUrl())) {
                    currentFacetAction.put(userActivity.getUrl(), currentFacetAction.get(userActivity.getUrl() + 1));
                }
                Long countToDayAction = (Long) actionObjects.get(UserActivityService.INDEX_TODAY_TOTAL_ACTION);
                countToDayAction++;

                newUserActivities.add(userActivity);
                actionObjects.set(UserActivityService.INDEX_ALLTIME_TOTAL_ACTION, (Long) actionObjects.get(UserActivityService.INDEX_ALLTIME_TOTAL_ACTION) + 1);
                actionObjects.set(UserActivityService.INDEX_NOW_ACTION, currentFacetAction);
                actionObjects.set(UserActivityService.INDEX_TODAY_TOTAL_ACTION, countToDayAction);

                //flush storage if the the activity size is out of limit
                if (newUserActivities.size() > systemConfig.MAX_STORED_ACTIVITY) {
                    userActivityService.commitActivities(newUserActivities);
                    actionObjects = userActivityService.resetAction(userActivity.getUserId(), systemConfig.DEFAULT_SYS);
                }
            }
        }
    }

    private UserActivity getUserActivity(List<UserActivity> list, String url) {
        for (UserActivity userActivity : list) {
            if (userActivity.getUrl().equals(url)) {
                return userActivity;
            }
        }
        return null;
    }
}
