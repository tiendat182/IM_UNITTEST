package com.viettel.web.activity;

import org.apache.solr.client.solrj.beans.Field;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.Date;

/**
 * Created by Thien on 21/11/2015.
 */
public class UserActivity {

    @Field("id")
    @JsonProperty("id")
    private String id;

    @Field("user_id")
    @JsonProperty("user_id")
    private String userId;

    @Field("url")
    @JsonProperty("url")
    private String url;

    @Field("url_description")
    @JsonProperty("url_description")
    private String urlDescription;

    @Field("action")
    @JsonProperty("action")
    private String action;

    @Field("action_description")
    @JsonProperty("action_description")
    private String actionDescription;

    @Field("date_create")
    @JsonProperty("date_create")
    private Date createDate;

    @Field("project")
    @JsonProperty("project")
    private String project;

    private int monthTime;

    private long group;

    private int status;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getUrlDescription() {
        return urlDescription;
    }

    public void setUrlDescription(String urlDescription) {
        this.urlDescription = urlDescription;
    }

    public String getActionDescription() {
        return actionDescription;
    }

    public void setActionDescription(String actionDescription) {
        this.actionDescription = actionDescription;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setGroup(long group) {
        this.group = group;
    }

    public long getGroup() {
        return group;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getMonthTime() {
        return monthTime;
    }

    public void setMonthTime(int monthTime) {
        this.monthTime = monthTime;
    }
}
