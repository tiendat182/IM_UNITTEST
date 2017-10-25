/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.web.notify;

import com.viettel.fw.dto.BaseDTO;
import org.apache.solr.client.solrj.beans.Field;
import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * @author vtsoft
 */

public class NotifyContent extends BaseDTO implements Serializable {

    @Field("id")
    @JsonProperty("id")
    private String id;
    @Field("to")
    @JsonProperty("to")
    private String to;
    @Field("from")
    @JsonProperty("from")
    private String from;
    @Field("subject")
    @JsonProperty("subject")
    private String subject;
    @Field("text")
    @JsonProperty("text")
    private String text;
    @Field("date_create")
    @JsonProperty("date_create")
    private Date createdDate;
    @Field("status")
    @JsonProperty("status")
    private int status;
    @Field("url")
    @JsonProperty("url")
    private String url;

    @JsonProperty("dateTime")
    private long dateTime;

    public NotifyContent() {
    }

    private boolean needUpdateStatus;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
        if(createdDate!=null){
            dateTime = createdDate.getTime();
        }
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isNeedUpdateStatus() {
        return needUpdateStatus;
    }

    public void setNeedUpdateStatus(boolean needUpdateStatus) {
        this.needUpdateStatus = needUpdateStatus;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }
}
