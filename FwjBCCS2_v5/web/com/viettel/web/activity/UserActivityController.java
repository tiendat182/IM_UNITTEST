package com.viettel.web.activity;

import akka.actor.ActorRef;
import com.viettel.fw.SystemConfig;
import com.viettel.fw.passport.CustomConnector;
import com.viettel.web.common.MenuBean;
import com.viettel.web.common.controller.BaseController;
import org.apache.log4j.Logger;
import org.omnifaces.util.Faces;
import org.primefaces.model.chart.PieChartModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import viettel.passport.client.UserToken;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by thiendn1 on 24/11/2015.
 */
@Component
@ManagedBean
@Scope("view")
public class UserActivityController extends BaseController {
    public static final Logger logger = Logger.getLogger(UserActivityController.class);
    private final int RANGE_ALL = 0;
    private final int RANGE_WEEK = 1;
    private final int RANGE_MONTH = 2;

    @Autowired(required = false)
    private UserActivityService userActivityService;
    @Autowired(required = false)
    private MenuBean menuBean;
    @Autowired(required = false)
    private ActivitySession activitySession;
    String defaultSyste;
    private List<UserActivity> recentUrlActions;
    private List<UserActivity> todayActionInGroup;
    private long yesterdayTotalActions = 0;
    private long allTimeTotalAction = 0;
    private UserToken user;
    private int rangeTime;
    private Long countTodayAction;

    @Autowired(required = false)
    @Qualifier(SystemConfig.LOG_ANALYTIC_ACTOR)
    private ActorRef loggingAnalyticActor;


    private boolean showSummary = true;


    private PieChartModel barModel = new PieChartModel();

    @Autowired
    SystemConfig systemConfig;

    @PostConstruct
    private void init() {

        if (!systemConfig.IS_RUN_DASHBOARD) {
            return;
        }

        defaultSyste = systemConfig.DEFAULT_SYS;
        user = Faces.getSessionAttribute(CustomConnector.VSA_USER_TOKEN);
        try {
            List<Object> objects = null;
            if (activitySession.isInit()) {
                objects = userActivityService.resetAction(user.getUserName(), defaultSyste);
                activitySession.postAction(objects);
                activitySession.setInit(false);
            } else {
                // call actor and get objects
                objects = activitySession.getActions();
            }
            if (objects != null) {
                yesterdayTotalActions = (Long) objects.get(UserActivityService.INDEX_YESTERDAY_TOTAL_ACTION);
                allTimeTotalAction = (Long) objects.get(UserActivityService.INDEX_ALLTIME_TOTAL_ACTION);
                List<UserActivity> userActivities = (List<UserActivity>) objects.get(UserActivityService.INDEX_RECENT_ACTION);
                recentUrlActions = new ArrayList<>();
                for (int i = 0; i < Math.min(10, userActivities.size()); i++) {
                    recentUrlActions.add(userActivities.get(i));
                }

                userActivities = (List<UserActivity>) objects.get(UserActivityService.INDEX_TODAY_GROUP_ACTION);
                todayActionInGroup = new ArrayList<>();
                for (int i = 0; i < Math.min(10, userActivities.size()); i++) {
                    todayActionInGroup.add(userActivities.get(i));
                }
                countTodayAction = (Long) objects.get(UserActivityService.INDEX_TODAY_TOTAL_ACTION);
            }
        } catch (Exception e) {
            logger.error(e);
            todayActionInGroup = new ArrayList<>();
            recentUrlActions = new ArrayList<>();
        }
        initBarModel();

    }


    public void initBarModel() {
        if (!systemConfig.IS_RUN_DASHBOARD) {
            return;
        }
        barModel = new PieChartModel();
        Map<String, Long> list = null;
        try {
            if (rangeTime == RANGE_ALL) {
                list = userActivityService.mapFacetAllActions(user.getUserName(), defaultSyste, UserActivityService.FACET_RANGE.ALL_TIME);
            } else if (rangeTime == RANGE_WEEK) {
                list = userActivityService.mapFacetAllActions(user.getUserName(), defaultSyste, UserActivityService.FACET_RANGE.WEEK);
            } else if (rangeTime == RANGE_MONTH) {
                list = userActivityService.mapFacetAllActions(user.getUserName(), defaultSyste, UserActivityService.FACET_RANGE.MONTH);
            }

            // call actor and get objects
            List<Object> objects = activitySession.getActions();
            if (objects != null) {
                Map<String, Long> currentActivities = (Map<String, Long>) objects.get(UserActivityService.INDEX_NOW_ACTION);
                list.putAll(currentActivities);
            }

            for (Map.Entry entry : list.entrySet()) {
                String name = menuBean.getUrlName((String) entry.getKey());
                //chi hien thi cac url duoc dat ten
                if (name == null) {
                    continue;
                }
                barModel.set(name, (Long) entry.getValue());
            }
            barModel.setLegendPosition("e");
            barModel.setShowDataLabels(true);
        } catch (Exception e) {
            logger.error(e);
        }
    }


    public List<UserActivity> getTodayActionInGroup() {
        return todayActionInGroup;
    }

    public UserToken getUser() {
        return user;
    }


    public String getCompareToYesterdayRate() {
        if (yesterdayTotalActions == 0) {
            return "N/A";
        }
        return String.valueOf(getCountTodayAction() * 100 / yesterdayTotalActions);
    }

    public long getYesterdayTotalActions() {
        return yesterdayTotalActions;
    }

    public long getAllTimeTotalAction() {
        return allTimeTotalAction;
    }

    public List<UserActivity> getRecentUrlActions() {
        return recentUrlActions;
    }

    public int getRangeTime() {
        return rangeTime;
    }

    public void setRangeTime(int rangeTime) {
        this.rangeTime = rangeTime;
    }

    public int getRANGE_ALL() {
        return RANGE_ALL;
    }

    public int getRANGE_WEEK() {
        return RANGE_WEEK;
    }

    public int getRANGE_MONTH() {
        return RANGE_MONTH;
    }

    public PieChartModel getBarModel() {
        return barModel;
    }

    public Long getCountTodayAction() {
        return countTodayAction;
    }

    public boolean isShowSummary() {
        return showSummary;
    }

    public void setShowSummary(boolean showSummary) {
        this.showSummary = showSummary;
    }
}
