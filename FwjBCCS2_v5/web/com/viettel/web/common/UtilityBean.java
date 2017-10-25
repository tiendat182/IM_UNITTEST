/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.web.common;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.faces.bean.ManagedBean;
import java.io.Serializable;

/**
 *
 * @author quangkm
 */
@Component
@ManagedBean
@Scope("view")
public class UtilityBean implements Serializable {

    private boolean homeActive = false;
    private boolean feedbackActive = false;
    private boolean historyActive = false;
    private boolean notifyActive = false;
    private String homePage = "homePage";

    public UtilityBean(){
        this.homeActive = true;
    }
    
    public void doActiveHome() {
        setHomeActive(true);
        setFeedbackActive(false);
        setHistoryActive(false);
        setNotifyActive(false);
        setHomePage("homePage");
    }

    public void doActiveFeedback() {
        setHomeActive(false);
        setFeedbackActive(true);
        setHistoryActive(false);
        setNotifyActive(false);
        setHomePage("");
    }

    public void doActiveHistory() {
        setHomeActive(false);
        setFeedbackActive(false);
        setHistoryActive(true);
        setNotifyActive(false);
        setHomePage("");
    }

    public void doActiveNotify() {
        setHomeActive(false);
        setFeedbackActive(false);
        setHistoryActive(false);
        setNotifyActive(true);
        setHomePage("");
    }
    /*GETTER SETTER*/

    public boolean isHomeActive() {
        return homeActive;
    }

    public void setHomeActive(boolean homeActive) {
        this.homeActive = homeActive;
    }

    public boolean isFeedbackActive() {
        return feedbackActive;
    }

    public void setFeedbackActive(boolean feedbackActive) {
        this.feedbackActive = feedbackActive;
    }

    public boolean isHistoryActive() {
        return historyActive;
    }

    public void setHistoryActive(boolean historyActive) {
        this.historyActive = historyActive;
    }

    public boolean isNotifyActive() {
        return notifyActive;
    }

    public void setNotifyActive(boolean notifyActive) {
        this.notifyActive = notifyActive;
    }

    public String getHomePage() {
        return homePage;
    }

    public void setHomePage(String homePage) {
        this.homePage = homePage;
    }
}
