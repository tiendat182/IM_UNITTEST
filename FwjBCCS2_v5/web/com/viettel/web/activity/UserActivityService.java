package com.viettel.web.activity;

import com.viettel.fw.SystemConfig;
import com.viettel.solrj.SolrDao;
import com.viettel.solrj.SolrServerFactory;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.Group;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

/**
 * Created by Thien on 21/11/2015.
 */

@Service
@Lazy
public class UserActivityService {
    public static final int MAX_ROW = 10;

    public static final int INDEX_YESTERDAY_TOTAL_ACTION = 0;
    public static final int INDEX_ALLTIME_TOTAL_ACTION = 1;
    public static final int INDEX_TODAY_TOTAL_ACTION = 2;
    public static final int INDEX_RECENT_ACTION = 3;
    public static final int INDEX_TODAY_GROUP_ACTION = 4;
    public static final int INDEX_NOW_ACTION = 5;

    public static final String URL_FAVORITE = "favorite";
    public static final String URL_NORMAL = "normal";

    public static final int STATUS_ACTIVE = 0;
    public static final int STATUS_INACTIVE = 1;

    @Autowired
    ObjectMapper mapper;
    @Autowired
    SystemConfig systemConfig;

    public enum FACET_RANGE {DAY, WEEK, MONTH, ALL_TIME_BEFORE_TODAY, ALL_TIME}

    ;

    public enum FACET_TIME {TODAY, YESTERDAY, SEVEN_DAYS_AGO, THIS_MONTH, ALL_TIME_BEFORE_TODAY, ALL_TIME}

    ;


    public void commitActivities(List<UserActivity> userActivities) throws IOException, SolrServerException {
        List<String> deleteIds = new ArrayList<>();
        for (int i = 0; i < userActivities.size(); i++) {
            UserActivity user = userActivities.get(i);
            if (user.getStatus() == UserActivityService.STATUS_INACTIVE && user.getId() != null) {
                deleteIds.add(user.getId());
                userActivities.remove(i);
                i--;
            }
        }
        SolrDao solrDao = new SolrDao(systemConfig.DASHBOARD_LINK, systemConfig.DASHBOARD_USERNAME, systemConfig.DASHBOARD_PASSWORD);
        if (!userActivities.isEmpty()) {
            solrDao.put(userActivities);
        }
        if (!deleteIds.isEmpty()) {
            solrDao.delete(deleteIds);
        }

    }

    public List<Object> resetAction(final String userName, final String projectId) throws IOException, SolrServerException {
        List<Object> objects = new ArrayList<>();
        objects.add(UserActivityService.INDEX_YESTERDAY_TOTAL_ACTION, countActionInRange(userName, projectId, URL_NORMAL, UserActivityService.FACET_TIME.YESTERDAY));
        objects.add(UserActivityService.INDEX_ALLTIME_TOTAL_ACTION, countActionInRange(userName, projectId, URL_NORMAL, FACET_TIME.ALL_TIME));
        objects.add(UserActivityService.INDEX_TODAY_TOTAL_ACTION, countActionInRange(userName, projectId, URL_NORMAL, FACET_TIME.TODAY));
        objects.add(UserActivityService.INDEX_RECENT_ACTION, getRecentActions(userName, projectId, URL_NORMAL, 10));
        objects.add(UserActivityService.INDEX_TODAY_GROUP_ACTION, getActionInRange(userName, projectId, URL_NORMAL, FACET_TIME.THIS_MONTH, 10));
        objects.add(UserActivityService.INDEX_NOW_ACTION, new HashMap<String, Long>());
        return objects;
    }

    public long countActionInRange(String userId, String projectId, String type, FACET_TIME facet_time) throws IOException, SolrServerException {
        return queryActionInRange(userId, projectId, URL_NORMAL, facet_time, null, false).getResults().getNumFound();
    }

    public List<UserActivity> getActionInRange(String userId, String projectId, String type, FACET_TIME facet_time, Integer limit) throws IOException, SolrServerException {
        QueryResponse response = queryActionInRange(userId, projectId, URL_NORMAL, facet_time, limit, true);
        List<Group> groups = response.getGroupResponse().getValues().get(0).getValues();
        List<UserActivity> userActivities = new ArrayList<>();
        for (Group group : groups) {
            long numFound = group.getResult().getNumFound();
            UserActivity activity = new UserActivity();
            activity.setUrl((String) group.getResult().get(0).get("url"));
            activity.setGroup(numFound);
            userActivities.add(activity);
        }
        Collections.sort(userActivities, new Comparator<UserActivity>() {
            @Override
            public int compare(UserActivity o1, UserActivity o2) {
                return o1.getGroup() > o2.getGroup() ? -1 : 1;
            }
        });
        return userActivities;
    }

    private QueryResponse queryActionInRange(String userId, String projectId, String type, FACET_TIME facet_time, Integer limit, Boolean group) throws IOException, SolrServerException {
        HttpSolrClient server = null;
        server = getHttpSolrClient();
        SolrQuery solrQuery = new SolrQuery();
        String query = "user_id:" + userId + " AND project:" + projectId + " AND url_description:" + type;
        solrQuery.setFacet(true);
        solrQuery.set("facet.field", "url");
        solrQuery.setSort("date_create", SolrQuery.ORDER.desc);
        if (limit != null) {
            solrQuery.setRows(limit.intValue());
        }
        if (group) {
            solrQuery.setParam("group", true);
            solrQuery.setParam("group.field", "url");
        }
        switch (facet_time) {
            case TODAY:
                query += " AND date_create:[NOW/DAY TO *]";
                break;
            case YESTERDAY:
                query += " AND date_create:[NOW/DAY-1DAY TO NOW/DAY]";
                break;
            case SEVEN_DAYS_AGO:
                query += " AND date_create:[NOW/DAY-7DAY TO *]";
                break;
            case THIS_MONTH:
                query += " AND date_create:[NOW/MONTH TO *]";
                break;
            case ALL_TIME_BEFORE_TODAY:
                query += " AND date_create:[* TO *]";
                break;
            case ALL_TIME:
                query += " AND date_create:[* TO *]";
                break;
            default:
                break;
        }
        solrQuery.setQuery(query);
        return server.query(solrQuery);
    }

    //lay ve danh sach (default = 10) thao tac gan day nhat
    public List<UserActivity> getRecentActions(String userId, String projectId, String type, Integer limit) throws IOException, SolrServerException {
        HttpSolrClient server = null;
        server = getHttpSolrClient();
        String query = "user_id:" + userId + " AND project:" + projectId + " AND url_description:" + type;
        SolrQuery solrQuery = new SolrQuery();
        solrQuery.setQuery(query);
        solrQuery.setParam("group", true);
        solrQuery.setParam("group.field", "url");
        if (limit != null) {
            solrQuery.setRows(limit);
        }
        solrQuery.setSort("date_create", SolrQuery.ORDER.desc);
        QueryResponse response = server.query(solrQuery);
        List<Group> groups = response.getGroupResponse().getValues().get(0).getValues();
        List<UserActivity> userActivities = new ArrayList<>();
        for (Group group : groups) {
            SolrDocument solrDocument = group.getResult().get(0);
            UserActivity activity = mapper.readValue(mapper.writeValueAsString(solrDocument), UserActivity.class);
            userActivities.add(activity);
        }
        return userActivities;


    }

    //lay ve danh sach favorite (default = 10) thao tac gan day nhat
    public List<UserActivity> getFavoriteActivity(String userId, String projectId, Integer limit) throws IOException, SolrServerException {
        HttpSolrClient server = null;
        server = getHttpSolrClient();
        String query = "user_id:" + userId + " AND project:" + projectId + " AND url_description: " + URL_FAVORITE;
        SolrQuery solrQuery = new SolrQuery();
        solrQuery.setQuery(query);
        solrQuery.setParam("group", true);
        solrQuery.setParam("group.field", "url");
        if (limit != null) {
            solrQuery.setRows(limit);
        }
        solrQuery.setSort("date_create", SolrQuery.ORDER.desc);
        QueryResponse response = server.query(solrQuery);
        List<Group> groups = response.getGroupResponse().getValues().get(0).getValues();
        List<UserActivity> userActivities = new ArrayList<>();
        for (Group group : groups) {
            SolrDocument solrDocument = group.getResult().get(0);
            UserActivity activity = mapper.readValue(mapper.writeValueAsString(solrDocument), UserActivity.class);
            userActivities.add(activity);
        }
        return userActivities;


    }


    public Map<String, Long> mapFacetAllActions(String userId, String projectId, FACET_RANGE facet_range) throws IOException, SolrServerException {

        QueryResponse rsp = queryFacetAllActions(userId, projectId, facet_range);
        List<FacetField> fields = rsp.getFacetFields();
        Map<String, Long> map = new HashMap<>();
        if (fields != null && !fields.isEmpty()) {
            for (FacetField.Count count : fields.get(0).getValues()) {
                map.put(count.getName(), count.getCount());
            }
        }
        return map;
    }

    // lay ve danh sach cac thao tac trong 1 khoang thoi gian
    public QueryResponse queryFacetAllActions(String userId, String projectId, FACET_RANGE facet_range) throws IOException, SolrServerException {
        HttpSolrClient server = null;
        server = getHttpSolrClient();
        SolrQuery solrQuery = new SolrQuery();
        String query = "user_id:" + userId + " AND project: " + projectId;
        solrQuery.setFacet(true);
        solrQuery.set("facet.field", "url");
        solrQuery.set("facet.sort", "count");
        solrQuery.set("facet.limit", "5");
        solrQuery.set("facet.mincount", "1");

        solrQuery.setSort("date_create", SolrQuery.ORDER.desc);

        switch (facet_range) {
            case DAY:
                query += " AND date_create:[NOW/DAY TO *]";
                break;
            case WEEK:
                int dayInWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1;
                String subQuery = "[NOW/DAY-" + dayInWeek + "DAY" + " TO *]";
                query += " AND date_create:" + subQuery;
                break;
            case MONTH:
                query += " AND date_create:[NOW/MONTH TO *]";
                break;
            case ALL_TIME_BEFORE_TODAY:
                query += " AND date_create:[* TO NOW/DAY]";
                break;
            case ALL_TIME:
                query += " AND date_create:[* TO *]";
                break;
            default:
                break;
        }
        solrQuery.setQuery(query);
        return server.query(solrQuery);

    }

    private HttpSolrClient getHttpSolrClient() {
        return (HttpSolrClient) SolrServerFactory.getInstance().createServer(systemConfig.DASHBOARD_LINK, systemConfig.DASHBOARD_USERNAME, systemConfig.DASHBOARD_PASSWORD);
    }

}
