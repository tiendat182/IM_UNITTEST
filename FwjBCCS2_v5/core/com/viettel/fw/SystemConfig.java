package com.viettel.fw;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by thiendn1 on 25/12/2015.
 */
@Component
public class SystemConfig {
    public static final String ACTIVITI_ACTOR_BEAN = "activityActorBean";
    public static final String FAVORITE_ACTOR_BEAN = "favoriteActorBean";
    public static final String LOG_ANALYTIC_ACTOR_BEAN = "logAnalyticActorBean";
    public static final String LOG_COLLECTOR_ACTOR_BEAN = "logCollectorActorBean";
    public static final String LOG_ANALYTIC_ACTOR = "logAnalyticActor";
    public static final String FAVORITE_ACTOR = "favoriteActor";
    public static final String CMD_BUS_BEAN = "cmdBusBean";
    public static final String CMD_EXECUTE_BEAN = "cmdExecuteBean";
    public static final String CMD_BUS_ACTOR = "cmdBusActor";
    public static final String CMD_EXECUTE_ACTOR = "cmdExecuteActor";



    @Value("${defaultSys:null}")
    public  String DEFAULT_SYS;

    @Value("${dashboard.run:false}")
    public  boolean IS_RUN_DASHBOARD;
    @Value("${dashboard.link:null}")
    public  String DASHBOARD_LINK;
    @Value("${dashboard.username:null}")
    public String DASHBOARD_USERNAME;
    @Value("${dashboard.password:null}")
    public String DASHBOARD_PASSWORD;

    @Value("${dashboard.maxStoredActivity:50}")
    public  int MAX_STORED_ACTIVITY;

    @Value("${favorite.run:false}")
    public  boolean IS_RUN_FAVORITE;
    @Value("${favorite.numberActor:10}")
    public  int FAVORITE_NUM_ACTOR;

    @Value("${notify.run:false}")
    public  boolean IS_RUN_NOTIFY;
    @Value("${notify.growLife:10000}")
    public  int GROW_LIFE;
    @Value("${notify.maxShowNotify:10}")
    public  int MAX_SHOW_NOTIFY;
    @Value("${notify.link:null}")
    public  String NOTIFY_LINK;
    @Value("${notify.username:null}")
    public  String NOTIFY_USERNAME;
    @Value("${notify.password:null}")
    public  String NOTIFY_PASSWORD;
    @Value("${notify.pushServer:null}")
    public  String PUSH_SERVER;

    @Value("${log.sys.run:false}")
    public  boolean IS_RUN_SYS_LOG;
    @Value("${log.kpi.run:false}")
    public  boolean IS_RUN_KPI_LOG;
    @Value("${log.dev.run:true}")
    public  boolean IS_RUN_DEV_LOG;
    @Value("${log.numberActor:10}")
    public  int LOG_NUM_ACTOR;

    @Value("${process.numActor:100}")
    public int MAX_NUM_ACTOR;
    @Value("${process.SCAN_INTERVAL:5000}")
    public long SCAN_INTERVAL;

    @Value("${solr.url:null}")
    public String SOLR_URL;

    @Value("${solr.username:null}")
    public String SOLR_USERNAME;

    @Value("${solr.password:null}")
    public String SOLR_PASSWORD;

    @Value("${dashboard.new.run:false}")
    public  boolean IS_RUN_DASHBOARD_NEW;

}
